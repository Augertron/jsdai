/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.tools;

import java.io.*;
//import java.io.File;
//import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lksoft.util.ArgumentsParser;

import jsdai.SExtended_dictionary_schema.AEntity_declaration;
import jsdai.SExtended_dictionary_schema.EEntity_declaration;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EReferenced_declaration;
import jsdai.SExtended_dictionary_schema.EUsed_declaration;
import jsdai.lang.*;

public final class ConformanceOptionsGenerator {
// /* Remove this comment if you want to use this class
  private static final String REPOSITORY_NAME = "ExpressCompilerRepo3";
//  private static final String ARM_REPOSITORY_LOCATION = "D:\\workspace\\stepmod_LKSOFT\\modules\\stepmod\\build_arm2\\repositories\\ExpressCompilerRepo2.sdai";
  private static String ARM_REPOSITORY_LOCATION = "REPOSITORIES\\arm.sdai";
  private static String MIM_REPOSITORY_LOCATION = "REPOSITORIES\\mim.sdai";
//  private static final String MIM_REPOSITORY_LOCATION = "D:\\workspace\\stepmod_LKSOFT\\modules\\stepmod\\build_aim2\\repositories\\ExpressCompilerRepo3.sdai";
  static SdaiRepository reposARM = null;
  static SdaiRepository reposMIM = null;
  private static Map armEntityNameToCCO;
  private static Map mimEntityNameToCCO;
  private static Map coIdToCO;
  
  private ConformanceOptionsGenerator() { }

  private static File[] parseArgs(String[] args)throws IOException {
    ArgumentsParser.Value ccsArg = new ArgumentsParser.Value("-ccs", true);
    ccsArg.setValueName("ccs");
    ccsArg.setDescription("ccs.xml file containing input information for conformance classes. Example: c:/workspace/stepmod/data/application_protocols/config_control_design_ed2/ccs.xml");

    ArgumentsParser.Value coMimArg = new ArgumentsParser.Value("-comim", true);
    coMimArg.setValueName("comim");
    coMimArg.setDescription("comim.xml file containing mim_in_cc/co stuff. Example: c:/workspace/stepmod/data/application_protocols/config_control_design_ed2/comim.xml");

    ArgumentsParser.Value coArmArg = new ArgumentsParser.Value("-coarm", true);
    coArmArg.setValueName("coarm");
    coArmArg.setDescription("coarm.xml containing arm_in_cc/co stuff. Example: c:/workspace/stepmod/data/application_protocols/config_control_design_ed2/coarm.xml");

    ArgumentsParser.Value armRepoArg = new ArgumentsParser.Value("-arm_repo", true);
    armRepoArg.setValueName("ARM_REPO");
    armRepoArg.setDescription("path to the ARM dictionary data repository file (.sdai or .exd)");

    ArgumentsParser.Value mimRepoArg = new ArgumentsParser.Value("-mim_repo", true);
    mimRepoArg.setValueName("MIM_REPO");
    mimRepoArg.setDescription("path to the MIM dictionary data repository file (.sdai or .exd)");

    
    List argList = Arrays.asList(new Object[] {ccsArg, coArmArg, coMimArg, armRepoArg, mimRepoArg});
    if (!ArgumentsParser.parse(args, argList)) {
      System.out.println(ArgumentsParser.getUsage(ConformanceOptionsGenerator.class.getName(), argList));
      return null;
    }


    File ccsFile = new File(ccsArg.getValue());
    if (!ccsFile.exists()) {
      System.out.println("Specified ccs.xml file does not exists." + ccsFile+" EXIT the program ");
      return null;
    }

    File coArmFile = new File(coArmArg.getValue());
    
    File coMimFile = new File(coMimArg.getValue());
    File[] files = {ccsFile, coArmFile, coMimFile};

    ARM_REPOSITORY_LOCATION = armRepoArg.getValue();
    MIM_REPOSITORY_LOCATION = mimRepoArg.getValue();
    
    return files;
  }

  public static void main(String[] args)
    throws SdaiException {
    boolean success = false;    
    try {
      File[] parsedArgs = parseArgs(args);
      if (parsedArgs == null) {
        return;
      }
      armEntityNameToCCO = new HashMap();
      mimEntityNameToCCO = new HashMap();
      coIdToCO = new HashMap();
      File ccsFile = parsedArgs[0];
      File coARMFile = parsedArgs[1];
      File coMimFile = parsedArgs[2];
      
//      System.out.println("Opening repo: " + REPOSITORY_NAME);
//      SdaiRepository repo = Utils.getRepo(REPOSITORY_NAME);
//      if (repo != null) {
        System.out.println("Parsing ccs.xml ...");
        success = parseXMLandGenerateCOs(ccsFile, coARMFile.getAbsolutePath(), coMimFile.getAbsolutePath());
//      }
    } catch (IOException e) {
      e.printStackTrace(); 
    } finally {
      SdaiSession session = SdaiSession.getSession();
      if (session != null) {
        session.closeSession();
      }
    }

    if (success) {
      System.out.println("Success.");
    } else {
      System.out.println("Failed.");
    }
  }

  private static boolean parseXMLandGenerateCOs(File ccsFile, String coARMFileName, String coMIMFileName) {
    // Need 2 documents as we want to smoothly include them into master ccs.xml
    Document coDocARM = null;
    Document coDocMIM = null;
    Document ccsDoc = null;
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      docFactory.setValidating(false);
      docFactory.setCoalescing(true);
      // CCS file
      DocumentBuilder builder = docFactory.newDocumentBuilder();
      ccsDoc = builder.parse(ccsFile);
      // CO file
      coDocARM = builder.newDocument();
      coDocMIM = builder.newDocument();
      Element root = ccsDoc.getDocumentElement();
      // String topModuleName = root.getAttribute("module");
      //Element coRoot = coDoc.createElement("conformance");
      //coRoot.setAttribute("module", topModuleName);
      Element arms_in_ccs = coDocARM.createElement("arms_in_ccs");
      coDocARM.appendChild(arms_in_ccs);
      // coDoc.appendChild(coRoot);
      Element mims_in_ccs = coDocMIM.createElement("mims_in_ccs");
      coDocMIM.appendChild(mims_in_ccs);
      //coDoc.appendChild(mims_in_ccs);

      startSession();     
      
      NodeList nodes = root.getChildNodes();
      // System.err.println(" Root "+topModuleName);
      for(int rIndex=0; rIndex < nodes.getLength(); rIndex++){
        Node node = nodes.item(rIndex);
        if(node.getNodeName().equals("cc")){
          NodeList ccNodes = node.getChildNodes();
          Element coElement = (Element)node;
          String ccId = coElement.getAttribute("id");
          ConformanceClass cc =  new ConformanceClass(ccId);
          for(int ccIndex=0; ccIndex < ccNodes.getLength(); ccIndex++){
            Node ccNode = ccNodes.item(ccIndex);
            String nodeName = ccNode.getNodeName();
            if(nodeName.equals("module")){
              Element moduleNode = (Element)ccNode;
              String moduleName = moduleNode.getAttribute("name");
              processCCOModule(cc, moduleNode, true);
              processCCOModule(cc, moduleNode, false);
            }
          }
        }else if(node.getNodeName().equals("co")){
          NodeList coNodes = node.getChildNodes();
          Element coElement = (Element)node;
          String coName = coElement.getAttribute("name");
          ConformanceOption co =  new ConformanceOption(coName);
          coIdToCO.put(coName, co);
          for(int coIndex=0; coIndex < coNodes.getLength(); coIndex++){
            Node coNode = coNodes.item(coIndex);
            String nodeName = coNode.getNodeName();
            if(nodeName.equals("module")){
              Element moduleNode = (Element)coNode;
              processCCOModule(co, moduleNode, true);
              processCCOModule(co, moduleNode, false);
              // Process separate entities
                            
              // System.err.println(co.getAttribute("id")+" = "+moduleNode.getAttribute("name")); 
            }else if(nodeName.equals("arm_entity")){
              String entityName = parseEntityName(coNode);
              putNameCoLink(co, entityName, armEntityNameToCCO);
              co.addArmEntity(entityName);
              // System.err.println(entityName+" -> "+co);
            }else if(nodeName.equals("mim_entity")){
              String entityName = parseEntityName(coNode);
              putNameCoLink(co, entityName, mimEntityNameToCCO);
              co.addMimEntity(entityName);
              // System.err.println(entityName+" -> "+co);
            // Due to recursive nature of this reference - it will be processed later.  
            }else if(nodeName.equals("co_ref")){
              Element coIdNode = (Element)coNode;
              co.addCo(coIdNode.getAttribute("name"));
            }else if(nodeName.equals("#text")){
              // DO nothing here
            }else if(nodeName.equals("description")){
              // DO nothing here
            }else{
              System.err.println("Unsupported type of element in CO - "+nodeName);
            }
          }
        }
      }
      // Process possible links COs to other COs
      processCOLinks();
      create_x_in_ccs(coDocARM, arms_in_ccs, true);
      create_x_in_ccs(coDocMIM, mims_in_ccs, false);
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SdaiException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      Utils.saveToFile(coARMFileName, coDocARM, null);
      Utils.saveToFile(coMIMFileName, coDocMIM, null);
      // Utils.saveToFile(ccsFile.getAbsolutePath(), ccsDoc, null);
      // System.err.println(coFileName+" 0 "+codtdFileName+" "+coDoc);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      System.out.println("PROBLEM: " + e.getMessage());
      e.printStackTrace();
    }

    return true;
  }

  private static void processCOLinks()throws SdaiException {
    Set keys = coIdToCO.keySet();
    Object[] cos = keys.toArray();
    // Collect all entities systematically
    for(int i=0; i<cos.length; i++){
      String coId = (String)cos[i];
      ConformanceOption co = (ConformanceOption)coIdToCO.get(coId);
      // Search for leaves
      if(!co.containsCOs()){
        co.setFullyProcessed(true);
      // Now need to recursively process the references 
      }else{
        processCoRecursive(co);
      }
    }
    // Now we can create missing arm_in_ccs/mim_in_ccs
    for(int i=0; i<cos.length; i++){
      String coId = (String)cos[i];
      ConformanceOption co = (ConformanceOption)coIdToCO.get(coId);
      // Only those having COs may have something missing
      if(co.containsCOs()){
        List armEntities = (List)co.getArmEntities();
        for(int j=0,entitiesCount=armEntities.size(); j<entitiesCount; j++){
          putNameCoLink(co, (String)armEntities.get(j), (Map)armEntityNameToCCO);
        }
        List mimEntities = (List)co.getMimEntities();
        for(int j=0,entitiesCount=mimEntities.size(); j<entitiesCount; j++){
          putNameCoLink(co, (String)mimEntities.get(j), (Map)mimEntityNameToCCO);
        }
      }
    }
  }

  private static void processCoRecursive(ConformanceOption co) {
    List coIds = co.getCos();
    for(int j=0,cosCount=coIds.size(); j<cosCount; j++){
      String coId = (String)coIds.get(j); 
      ConformanceOption coRef = (ConformanceOption)coIdToCO.get(coId);
      if(coRef == null){
        System.err.println("No referenced module found - "+coId);
        continue;
      }
      // System.err.println(co+" "+j+" "+coId+" "+coRef);
      if((coRef.containsCOs())&&(!coRef.isFullyProcessed())){
        processCoRecursive(coRef);
      }
      co.processCoReference(coRef);
    }
    co.setFullyProcessed(true);
  }

  private static String parseEntityName(Node node) {
    NodeList nodes = node.getChildNodes();
    // Assumption - there must be eaxactly one node
    // System.err.println(" Node "+nodes.item(1).getNodeValue());
    Element expressRef = (Element)nodes.item(1);
    String name = expressRef.getAttribute("linkend");
    StringTokenizer st = new StringTokenizer(name, ":.");
    String result = null;
    while(st.hasMoreElements()){
      result = (String)st.nextElement();
    }
    return result;
  }

  private static void processCCOModule(Object key, Element moduleNode, boolean isARM)throws SdaiException {
    String moduleName = moduleNode.getAttribute("name");
    String schemaName;
    SdaiSession session = SdaiSession.getSession();
    SdaiRepository repos;
    Map entityNameToCCO;
    if(isARM){
      schemaName = (moduleName+"_arm_dictionary_data").toUpperCase();
      if(reposARM == null){
        reposARM = repos = session.linkRepository(REPOSITORY_NAME, ARM_REPOSITORY_LOCATION);
        repos.openRepository();
      }else{
        repos = reposARM;
      }
      entityNameToCCO = armEntityNameToCCO;
    }else{
      schemaName = (moduleName+"_mim_dictionary_data").toUpperCase();
      if(reposMIM == null){
        reposMIM = repos = session.linkRepository(REPOSITORY_NAME, MIM_REPOSITORY_LOCATION);
        repos.openRepository();       
      }else{
        repos = reposMIM;
      }
      
      entityNameToCCO = mimEntityNameToCCO;
    }
      // Process Module
    SdaiModel topModel = repos.findSdaiModel(schemaName);
    if(topModel == null){
      System.err.println(" No schema found "+schemaName);
      return;
    }
    if(topModel.getMode() != SdaiModel.READ_ONLY){      
      topModel.startReadOnlyAccess();
    }
    AEntity_declaration declarations = (AEntity_declaration)topModel.getInstances(EEntity_declaration.class);
    String completness = moduleNode.getAttribute("completness");
    // Form a list of declarations to remove
    List declarationsToSkip = new ArrayList(); 
    if(completness.equals("substractive")){
      NodeList moduleNodes = moduleNode.getChildNodes();
      for(int moIndex=0; moIndex < moduleNodes.getLength(); moIndex++){
        Node moNode = moduleNodes.item(moIndex);
        String nodeName = moNode.getNodeName();
        if(nodeName.equals("arm_entity")){
          if(isARM){
            String entityName = parseEntityName(moNode);
            declarationsToSkip.add(entityName);
          }
        }else if(nodeName.equals("mim_entity")){
          if(!isARM){
            String entityName = parseEntityName(moNode);
            declarationsToSkip.add(entityName);
          }
        }
      }
    }
    
    if((completness.equals("complete"))||(completness.equals("substractive"))){
      for(int i=1,count=declarations.getMemberCount(); i<= count; i++){
        EEntity_declaration declaration = declarations.getByIndex(i);
        if(!((declaration instanceof EUsed_declaration)||(declaration instanceof EReferenced_declaration))){
          continue;
        }
        EEntity_definition definition = (EEntity_definition)declaration.getDefinition(null);
        // Skip complexes
        if(definition.getComplex(null)){
          continue;
        }
        String entityName = definition.getName(null);
        if(declarationsToSkip.contains(entityName)){
//          System.err.println(" Skipping "+entityName);
          System.out.println("INFO: Skipping "+entityName);
          continue;
        }
        // System.err.println(i+"-th "+definition);
        putNameCoLink(key, isARM, entityNameToCCO, entityName);
        // System.err.println(definition+" "+entityNameToCCO.get(definition.getName(null)));      
      }
    }else if(completness.equals("selective")){
      NodeList moduleNodes = moduleNode.getChildNodes();
      for(int moIndex=0; moIndex < moduleNodes.getLength(); moIndex++){
        Node moNode = moduleNodes.item(moIndex);
        String nodeName = moNode.getNodeName();
        if(nodeName.equals("arm_entity")){
          if(isARM){
            String entityName = parseEntityName(moNode);
            putNameCoLink(key, isARM, entityNameToCCO, entityName);
          }
        }else if(nodeName.equals("mim_entity")){
          if(!isARM){
            String entityName = parseEntityName(moNode);
            putNameCoLink(key, isARM, entityNameToCCO, entityName);
          }
        }else if(!nodeName.equals("#text")){
          System.err.println("Unsupported node in module "+nodeName);
        }
      }
    }
    // Process entity
  }

  private static void putNameCoLink(Object key, boolean isARM, Map entityNameToCCO, String entityName) throws SdaiException {
    putNameCoLink(key, entityName, entityNameToCCO);
    if(key instanceof ConformanceOption){
      ConformanceOption co = (ConformanceOption)key;
      if(isARM)
        co.addArmEntity(entityName);
      else
        co.addMimEntity(entityName);
    }
  }

  private static SdaiSession startSession() throws SdaiException {
    // General stuff later likely to be moved to other method
    SdaiSession session = SdaiSession.openSession();
    // For now process ARM only
    // General stuff
    session.startTransactionReadOnlyAccess();
    return session;
  }

  private static void putNameCoLink(Object key, String entityName, Map EntityNameToCCO) throws SdaiException {
    List cco = (List)EntityNameToCCO.get(entityName);
    // System.err.println(definition+" - "+entityNameToCCO.get(definition.getName(null))+" "+entityNameToCCO.size());
    if(cco == null){
      cco = new ArrayList();
      EntityNameToCCO.put(entityName, cco);
    }
    if(!cco.contains(key)){
      cco.add(key);
    }
  }

  private static void create_x_in_ccs(Document coDoc, Element parent, boolean isARM)throws SdaiException{
    // Finally create XML stuff
    Map entityNameToCCO;
    if(isARM){
      entityNameToCCO = armEntityNameToCCO; 
    }else{
      entityNameToCCO = mimEntityNameToCCO;
    }
    Set keys = entityNameToCCO.keySet();
    // System.err.println(" SIZE "+keys.size());
    Iterator it = keys.iterator();
    while(it.hasNext()){
      Element entity_in_cc;
      if(isARM){
        entity_in_cc = coDoc.createElement("arm_entity_in_cc");
      }else{
        entity_in_cc = coDoc.createElement("mim_entity_in_cc");
      }
      parent.appendChild(entity_in_cc);
      String definition = (String)it.next();
      entity_in_cc.setAttribute("name", definition);
      List cco = (List)entityNameToCCO.get(definition);
      for(int i=0,count=cco.size(); i<count; i++){
        Object currentId = cco.get(i);
        if(currentId instanceof ConformanceOption){
          ConformanceOption co = (ConformanceOption)currentId;
          Element co_id = coDoc.createElement("co_ref");
          co_id.setAttribute("name", co.getId());
          entity_in_cc.appendChild(co_id);
        }else if(currentId instanceof ConformanceClass){
          ConformanceClass cc = (ConformanceClass)currentId;
          Element cc_id = coDoc.createElement("cc_id");
          cc_id.setAttribute("id", cc.getId());
          entity_in_cc.appendChild(cc_id);
          // System.err.println(" TODO - implement cc_id ");
        }else{
          throw new SdaiException(SdaiException.ED_NVLD," Not valid member of map "+currentId);
        }
      }
    }
  }
// */ 

//    public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final jsdai.util.UtilMonitor monitor)
    public static Runnable initAsRunnable(final String sdaireposDirectory, final String[] args, final String[] args2)
    throws SdaiException {


      Properties jsdaiProperties = new Properties();
      jsdaiProperties.setProperty("repositories", sdaireposDirectory);

      // perhaps no need for other jsdai properties for now

      SdaiSession.setSessionProperties(jsdaiProperties);
     
      

      return new Runnable() {
        public void run() {
          run_main(args, args2);
        }
      };
    }

  static void run_main(String [] args, String [] args2) {

      PrintStream pout = null; 
      PrintStream perr = null;
      BufferedOutputStream bout = null; 
      BufferedOutputStream berr = null;
      FileOutputStream fout = null; 
      FileOutputStream ferr = null;

      PrintStream prevOut = null;
      PrintStream prevErr = null;

      String [] trimmed_args = null;

try {


        fout = new FileOutputStream(args2[0]);
        bout = new BufferedOutputStream(fout);
        pout = new PrintStream(bout);
        prevOut = System.out;
        System.setOut(pout);
        ferr = new FileOutputStream(args2[1]);
        berr = new BufferedOutputStream(ferr);
        perr = new PrintStream(berr);
        prevErr = System.err;
        System.setErr(perr);


    

    main(args);


} catch (Throwable eee) {

  System.err.println("Exception occurred: " + eee);
  eee.printStackTrace();
}
/*
    try {
      SdaiSession runningSession = SdaiSession.getSession();
      if (runningSession != null) {
        if (runningSession.testActiveTransaction()) {
          runningSession.getActiveTransaction().abort();
        }
        runningSession.closeSession();
      }
    } catch (SdaiException e) {
      e.printStackTrace();
    }
*/
    if (pout != null) {
      pout.flush();
      pout.close();
    }
    if(prevOut != null) {
      System.setOut(prevOut);
    }
    if (perr != null) {
      perr.flush();
      perr.close();
    }
    if(prevErr != null){
      System.setErr(prevErr);
    }

//    if(exitOnError && exitCode != 0) {
//      System.exit(exitCode);
//    }
    return;

} // run_main   

}
