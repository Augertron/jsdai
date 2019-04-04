/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 3, 2004 2:31:36 PM
 */
package jsdai.tools.validator;

import java.io.*;
import java.util.*;

import jsdai.dictionary.*;
import jsdai.lang.*;

import org.w3c.dom.*;

public class DiffChecker implements ITestSuite {

  private boolean updateStats;
  private Object entities;

  public boolean init(TestEnvironment testEnvironment, Element props) {
    try {
      updateStats = getUpdateStats(props);
      entities = EntityListParser.parseEntityList(testEnvironment.sourceSchema.getNativeSchema(), props);
    }
    catch (SdaiException e) {
      testEnvironment.logger.warning("Failed to read ignore list.");
      testEnvironment.logger.throwing(null, null, e);
    }

    return true;
  }

  private boolean getUpdateStats(Element props) {

    NodeList modes = props.getElementsByTagName("mode");
    Element mode = (Element) modes.item(0);

    return Boolean.valueOf(mode.getAttribute("updateStats")).booleanValue();
  }

  public String getName() {
    return "Difference checker";
  }

  public String run(TestEnvironment testEnvironment) {

    int diffs = 0;

    // read stats
    String statsFileName = testEnvironment.stepFileName + ".stats";
    File statsFile = new File(statsFileName);

    if (updateStats || !statsFile.exists()) {
      if (updateStats) {
        testEnvironment.logger.fine("Updating stats...");
      }
      else {
        testEnvironment.logger.fine("Creating stats...");
      }

      updateStats(testEnvironment, statsFile);
    }
    else {
      diffs = compare(testEnvironment, statsFile);
    }

    return diffs == 0 ? TEST_SUCCESS : TEST_FAILED;
  }

  private int compare(TestEnvironment testEnvironment, File statsFile) {

    int diffs = 0;

    PrintWriter writer = null;

    Map oldStats = getOldStats(statsFile);
    Map newStats = getStats(testEnvironment);
    for (Iterator i = newStats.keySet().iterator(); i.hasNext(); ) {
      String def = (String) i.next();
      Integer newN = (Integer) newStats.get(def);
      Integer oldN = (Integer) oldStats.get(def);
      if (oldN == null) {
        if (writer == null) {
          try {
            writer = new PrintWriter(new FileWriter(statsFile, true));
          }
          catch (FileNotFoundException e) {
            // should never happen
            e.printStackTrace();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
        }

        if (writer != null) {
          writer.println(createString(def, newN));
          testEnvironment.logger.severe("Added new entry: " + def);
        }
      }
      else {
        if (oldN.intValue() != newN.intValue()) {
          testEnvironment.logger.severe((newN.intValue() - oldN.intValue()) + " = " + oldN + " -> " + newN + "; " + def);
          diffs++;
        }
      }
    }

    if (writer != null) {
      writer.close();
    }

    return diffs;
  }

  private void updateStats(TestEnvironment testEnvironment, File statsFile) {
    try {
      PrintWriter writer = new PrintWriter(new FileOutputStream(statsFile));

      Map stats = getStats(testEnvironment);
      for (Iterator i = stats.keySet().iterator(); i.hasNext(); ) {
        String def = (String) i.next();
        Integer n = (Integer) stats.get(def);
        writer.println(createString(def, n));
      }

      writer.close();
    }
    catch (FileNotFoundException e) {
      // should never happen because new file is created
      e.printStackTrace();
    }
  }

  private Map getStats(TestEnvironment testEnvironment) {

    Map stats = new HashMap();

    try {
      ESchema_definition eSchemaDef = testEnvironment.sourceSchema.getNativeSchema();
      AEntity aEntDec = eSchemaDef.findEntityInstanceSdaiModel().getInstances(CEntity_declaration.class);
      for (SdaiIterator i = aEntDec.createIterator(); i.next(); ) {
        EEntity_declaration eEntDec = (EEntity_declaration) aEntDec.getCurrentMemberEntity(i);
        EEntity_definition eDef = (EEntity_definition) eEntDec.getDefinition(null);
        if (EntityListParser.belongs(entities, eDef)) {
          continue;
        }

        int n = testEnvironment.dataDomain.getExactInstances(eDef).getMemberCount();
        stats.put(eDef.getName(null), new Integer(n));
      }
    }
    catch (SdaiException e) {
      e.printStackTrace();
    }

    return stats;
  }

  private Map getOldStats(File statsFile) {

    Map oldStats = new HashMap();

    // read old values
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(statsFile));
      String s;
      while ((s = reader.readLine()) != null) {
        int i = s.indexOf('=');
        String entDef = s.substring(0, i).trim();
        Integer n = new Integer(Integer.parseInt(s.substring(i + 1).trim()));
        oldStats.put(entDef, n);
      }
    }
    catch (FileNotFoundException e) {
      // should never happen
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return oldStats;
  }

  private String createString(String def, Integer n) {
    return def + '=' + n;
  }
}