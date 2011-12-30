
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

package jsdai.express_g.util.xml;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import org.w3c.dom.*;

/**
 * Converts Express schema data to xml format.
 */
public class ExpressXmlConverter {

  public static boolean validate = true;
  public static String moduleLocation = "c:/stepmod_modules/stepmod/data/modules/";
  public static final String EXPRESS_XML_DOCTYPE_VALUE = "../../../dtd/express.dtd";

//  static final String FILE_EXTENSION = ".xml";
  static final String STYLESHEET_TARGET = "xml-stylesheet";
  static final String STYLESHEET_DATA = "type=\"text/xsl\" href=\"../../../xsl/express.xsl\"";
  static final String EXPRESS_MOD_PREFIX = "_EXPRESS_";
  static final String DICT_MOD_SUFIX = "_DICTIONARY_DATA";
  static final String LANGUAGE_VERSION = "2";
  static final String VAR_PARAMETER = "VAR ";
  static final String MIM_SCHEMA = "_mim";
  static final String ARM_SCHEMA = "_arm";
  static final int MIM_SUFFIX_LENGTH = MIM_SCHEMA.length();


/**
 *  Constructor for the Converter object
 */
  private ExpressXmlConverter() { }
    
  
  public static void main(String[] args){
/*
   An example of usage of convertSchemaInstance method (the statements like those 
   given below can be included into any appropriate application):

   SdaiRepository EC_repo = session.linkRepository("ExpressCompilerRepo", null);
   EC_repo.openRepository();
   ASchemaInstance EC_schemas = EC_repo.getSchemas();
   SdaiIterator iter_EC_schemas = EC_schemas.createIterator();
   while (iter_EC_schemas.next()) {
      SchemaInstance EC_sch = (SchemaInstance)EC_schemas.getCurrentMember(iter_EC_schemas);
      String sch_nm = EC_sch.getName();
      if (sch_nm.equals("automotive_design")) {
         Converter.convertSchemaInstance(EC_sch, "automotive_design.xml");
      } else if (sch_nm.equals("geometry_schema")) {
         Converter.convertSchemaInstance(EC_sch, "geometry_schema.xml");
      }
   }
*/
  }



  public static void convertSchemaInstance(SchemaInstance sch_inst, String file_name) throws SdaiException {
    SdaiRepository repo = sch_inst.getRepository();
    SdaiModel work = repo.createSdaiModel("working", SExtended_dictionary_schema.class);
    SdaiSession session = repo.getSession();
    String searched_name = sch_inst.getName().toUpperCase() + DICT_MOD_SUFIX;
    ASdaiModel assoc_mods = sch_inst.getAssociatedModels();
    SdaiIterator iter_assoc = assoc_mods.createIterator();
    SdaiModel model = null;
    while (iter_assoc.next()) {
      SdaiModel mod = assoc_mods.getCurrentMember(iter_assoc);
      if (mod.getName().equals(searched_name)) {
        model = mod;
        break;
      }
    }
    if (model != null) {
      if (model.getMode() == SdaiModel.NO_ACCESS) {
        model.startReadOnlyAccess();
      }
      convertModel(model, session, repo, work, file_name, null, false);
    } else {
      throw new SdaiException(SdaiException.SY_ERR);
    }
    work.deleteSdaiModel();
  }


  public static void convertModel(SdaiModel model, SdaiSession session, SdaiRepository repo, 
      SdaiModel work, String file_name, String ref, boolean no_arm) throws SdaiException {
    int i;
    EWhere_rule [] wh_rules = null;
    EUniqueness_rule [] un_rules = null;
    Document doc = null;
    try {
      DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
      f.setValidating(validate);
      if (validate) {
        f.setIgnoringElementContentWhitespace(true);
        f.setIgnoringComments(true);
      }
      f.setCoalescing(true);
      DocumentBuilder builder = f.newDocumentBuilder();
      builder.setErrorHandler(new MyErrorHandler());
      doc = builder.newDocument();
    } catch (ParserConfigurationException e) {
      System.out.println("PROBLEM: " + e.getMessage());
      e.printStackTrace();
    }

    ProcessingInstruction style_sheet = doc.createProcessingInstruction(STYLESHEET_TARGET, STYLESHEET_DATA);
    doc.appendChild(style_sheet);

    EDeclaration decl;
    ASub_supertype_constraint subtypes = null;
    jsdai.dictionary.ESchema_definition a_schema = work.getUnderlyingSchema();
    ASdaiModel domain = new ASdaiModel();
    domain.addByIndex(1, model);
    SdaiContext context = new SdaiContext(a_schema, domain, work);
    session.setSdaiContext(context);

    String mod_name = model.getName();
    jsdai.dictionary.ESchema_definition und_schema = model.getUnderlyingSchema();
    jsdai.dictionary.EEntity_definition sch_def_def = und_schema.getEntityDefinition("schema_definition");
    AEntity sch_defs = model.getExactInstances(sch_def_def);
    ESchema_definition schema_def = (ESchema_definition)sch_defs.getByIndexEntity(1);
    String schema_name = schema_def.getName(null);
//System.out.println("Converter   schema_name = " + schema_name);
    String xpr_model_name = EXPRESS_MOD_PREFIX + schema_name.toUpperCase();
    SdaiModel xpr_model = repo.findSdaiModel(xpr_model_name);
    if (xpr_model != null && xpr_model.getMode() == SdaiModel.NO_ACCESS) {
      xpr_model.startReadOnlyAccess();
    }
    Element root = doc.createElement("express");
    Attr atr = doc.createAttribute("language_version");
    atr.setValue(LANGUAGE_VERSION);
    root.setAttributeNode(atr);
    atr = doc.createAttribute("rcs.date");
//    SdaiCalendar cal = new SdaiCalendar();
//    String date_time = cal.longToTimeStamp(System.currentTimeMillis());
//    atr.setValue(date_time);
    String aux_str = "$" + "Date$";
    atr.setValue(aux_str);
    root.setAttributeNode(atr);
    atr = doc.createAttribute("rcs.revision");
//    atr.setValue("1.0");
    aux_str = "$" + "Revision$";
    atr.setValue(aux_str);
    root.setAttributeNode(atr);
    atr = doc.createAttribute("description.file");
    String description_type;
    if (no_arm) {
    	description_type = "descriptions.xml";
    } else {
       int sch_nm_ln = schema_name.length();
       if (sch_nm_ln >= MIM_SUFFIX_LENGTH) {
         String suffix = schema_name.substring(sch_nm_ln - MIM_SUFFIX_LENGTH);
         if (suffix.equals(MIM_SCHEMA)) {
           description_type = "mim_descriptions.xml";
         } else if (suffix.equals(ARM_SCHEMA)) {
           description_type = "arm_descriptions.xml";
         } else {
           description_type = "descriptions.xml";
         }
       } else {
         description_type = "descriptions.xml";
       }
    }
//    atr.setValue("arm_descriptions.xml");
    atr.setValue(description_type);
    root.setAttributeNode(atr);
    if (ref != null) {
       atr = doc.createAttribute("reference");
       atr.setValue(ref);
       root.setAttributeNode(atr);
    }

    Element appl_elem = doc.createElement("application");
    root.appendChild(appl_elem);
    atr = doc.createAttribute("name");
    atr.setValue("JSDAI");
    appl_elem.setAttributeNode(atr);
    atr = doc.createAttribute("owner");
    atr.setValue("LKSoft");
    appl_elem.setAttributeNode(atr);
    atr = doc.createAttribute("url");
    atr.setValue("www.lksoft.com");
    appl_elem.setAttributeNode(atr);
    atr = doc.createAttribute("version");
    atr.setValue("4.0 beta");
    appl_elem.setAttributeNode(atr);
    atr = doc.createAttribute("source");
    atr.setValue(schema_name.toLowerCase() + " schema_instance");
    appl_elem.setAttributeNode(atr);

    Element sch_elem = doc.createElement("schema");
    atr = doc.createAttribute("name");
    atr.setValue(schema_name);
    sch_elem.setAttributeNode(atr);
    if (und_schema.testIdentification(null)) {
      atr = doc.createAttribute("version");
      atr.setValue(und_schema.getIdentification(null));
      sch_elem.setAttributeNode(atr);
    }
    jsdai.dictionary.EEntity_definition xpr_code_def = und_schema.getEntityDefinition("express_code");
    AEntity xpr_code_aggr = null;
    if (xpr_model != null) {
      xpr_code_aggr = xpr_model.getInstances(xpr_code_def);
    }

    jsdai.dictionary.EEntity_definition ref_spec_def = und_schema.getEntityDefinition("reference_from_specification");
    AEntity ref_specs = model.getExactInstances(ref_spec_def);
    jsdai.dictionary.EEntity_definition use_spec_def = und_schema.getEntityDefinition("use_from_specification");
    AEntity use_specs = model.getExactInstances(use_spec_def);
    jsdai.dictionary.EEntity_definition inter_spec_def = und_schema.getEntityDefinition("interface_specification");
    AEntity inter_specs = model.getExactInstances(inter_spec_def);
    ESchema_definition inter_schema;
    SdaiIterator iter_auxil = null;
    SdaiIterator iter_auxil2 = null;
    SdaiIterator iter;
    SdaiIterator iter_specs = null;
    if (use_specs.getMemberCount() > 0) {
      if (iter_specs == null) {
        iter_specs = use_specs.createIterator();
      }
      while (iter_specs.next()) {
        EUse_from_specification use_spec = 
          (EUse_from_specification)use_specs.getCurrentMemberEntity(iter_specs);
        Element inter_use_elem = doc.createElement("interface");
        sch_elem.appendChild(inter_use_elem);
        atr = doc.createAttribute("kind");
        atr.setValue("use");
        inter_use_elem.setAttributeNode(atr);
        atr = doc.createAttribute("schema");
        inter_schema = (ESchema_definition)use_spec.getForeign_schema(null);
        atr.setValue(getCorrectedName(inter_schema));
        inter_use_elem.setAttributeNode(atr);
        if (use_spec.testItems(null)) {
          iter = convertInterfacedItems(use_spec.getItems(null), iter_auxil, inter_use_elem, doc);
          if (iter != null) {
            iter_auxil = iter;
          }
        }
      }
    }
    if (ref_specs.getMemberCount() > 0) {
      if (iter_specs == null) {
        iter_specs = ref_specs.createIterator();
      } else {
        ref_specs.attachIterator(iter_specs);
      }
      while (iter_specs.next()) {
        EReference_from_specification ref_spec = 
          (EReference_from_specification)ref_specs.getCurrentMemberEntity(iter_specs);
        Element inter_ref_elem = doc.createElement("interface");
        sch_elem.appendChild(inter_ref_elem);
        atr = doc.createAttribute("kind");
        atr.setValue("reference");
        inter_ref_elem.setAttributeNode(atr);
        atr = doc.createAttribute("schema");
        inter_schema = (ESchema_definition)ref_spec.getForeign_schema(null);
        atr.setValue(getCorrectedName(inter_schema));
        inter_ref_elem.setAttributeNode(atr);
        if (ref_spec.testItems(null)) {
          iter = convertInterfacedItems(ref_spec.getItems(null), iter_auxil, inter_ref_elem, doc);
          if (iter != null) {
            iter_auxil = iter;
          }
        }
      }
    }
    if (inter_specs.getMemberCount() > 0) {
      if (iter_specs == null) {
        iter_specs = inter_specs.createIterator();
      } else {
        inter_specs.attachIterator(iter_specs);
      }
      while (iter_specs.next()) {
        EInterface_specification inter_spec = 
          (EInterface_specification)inter_specs.getCurrentMemberEntity(iter_specs);
        Element inter_elem = doc.createElement("interface");
        sch_elem.appendChild(inter_elem);
        atr = doc.createAttribute("schema");
        inter_schema = (ESchema_definition)inter_spec.getForeign_schema(null);
        atr.setValue(getCorrectedName(inter_schema));
        inter_elem.setAttributeNode(atr);
        if (inter_spec.testItems(null)) {
          iter = convertInterfacedItems(inter_spec.getItems(null), iter_auxil, inter_elem, doc);
          if (iter != null) {
            iter_auxil = iter;
          }
        }
      }
    }

    jsdai.dictionary.EEntity_definition const_loc_decls_def = und_schema.getEntityDefinition("constant_declaration+local_declaration");
    AEntity const_decls = model.getInstances(const_loc_decls_def);
    SdaiIterator iter_const_decls = const_decls.createIterator();
    while (iter_const_decls.next()) {
      decl = (EDeclaration)const_decls.getCurrentMemberEntity(iter_const_decls);
      EConstant_definition const_def = (EConstant_definition)decl.getDefinition(null);
      Element const_elem = doc.createElement("constant");
      sch_elem.appendChild(const_elem);
      atr = doc.createAttribute("name");
      atr.setValue(const_def.getName(null));
      const_elem.setAttributeNode(atr);
      if (xpr_code_aggr != null) {
        iter = takeExpression(xpr_code_aggr, const_def, iter_auxil, const_elem, "expression", doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
//RR-pdb-support      convertUnderType((EData_type)const_def.getDomain(null), const_elem, doc, null, 0);
      convertUnderType((EData_type)const_def.getDomain(null), const_elem, doc, null, 0, xpr_code_aggr);
    }

    jsdai.dictionary.EEntity_definition def_type_def = und_schema.getEntityDefinition("defined_type");
    AEntity def_types = model.getInstances(def_type_def);
    int dt_count = 0;
    EDefined_type [] defin_types = new EDefined_type[def_types.getMemberCount()];
    SdaiIterator iter_def_types = def_types.createIterator();
    while (iter_def_types.next()) {
      defin_types[dt_count++] = (EDefined_type)def_types.getCurrentMemberEntity(iter_def_types);
    }
    Arrays.sort(defin_types, new SorterForTypes());
    for (i = 0; i < dt_count; i++) {
      EDefined_type def_type = defin_types[i];
//    while (iter_def_types.next()) {
//      EDefined_type def_type = (EDefined_type)def_types.getCurrentMemberEntity(iter_def_types);
      Element def_type_elem = doc.createElement("type");
      sch_elem.appendChild(def_type_elem);
      atr = doc.createAttribute("name");
      atr.setValue(def_type.getName(null));
      def_type_elem.setAttributeNode(atr);
//RR-pdb-suport      convertDefType((EData_type)def_type.getDomain(null), def_type_elem, doc);
      convertDefType((EData_type)def_type.getDomain(null), def_type_elem, doc, xpr_code_aggr);
      if (xpr_code_aggr != null) {
        iter = convertWhereRules(def_type.getWhere_rules(null, null), wh_rules, xpr_code_aggr, iter_auxil, iter_auxil2, 
          def_type_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
    }

    String sup_expression;
    jsdai.dictionary.EEntity_definition ent_loc_decls_def = und_schema.getEntityDefinition("entity_declaration+local_declaration");
    AEntity decls = model.getInstances(ent_loc_decls_def);
    EEntity_definition def;
    int e_count = 0;
    EEntity_definition [] entities =  new EEntity_definition[decls.getMemberCount()];
    EAttribute [] attribs = null;
    SdaiIterator iter_decls = decls.createIterator();
    while (iter_decls.next()) {
      decl = (EDeclaration)decls.getCurrentMemberEntity(iter_decls);
      def = (EEntity_definition)decl.getDefinition(null);
      if (def.getComplex(null)) {
        continue;
      }
      entities[e_count++] = def;
    }
    Arrays.sort(entities, 0, e_count, new SorterForEntities());
    for (i = 0; i < e_count; i++) {
      def = entities[i];
//    while (iter_decls.next()) {
//      decl = (EDeclaration)decls.getCurrentMemberEntity(iter_decls);
//      EEntity_definition def = (EEntity_definition)decl.getDefinition(null);
//      if (def.getComplex(null)) {
//        continue;
//      }
      Element entity_elem = doc.createElement("entity");
      sch_elem.appendChild(entity_elem);
      atr = doc.createAttribute("name");
      atr.setValue(getCorrectedName(def));
      entity_elem.setAttributeNode(atr);
      if (def.testAbstract_entity(null) && def.getAbstract_entity(null)) {
        atr = doc.createAttribute("abstract.entity");
        atr.setValue("YES");
        entity_elem.setAttributeNode(atr);
      }
      if (!def.getInstantiable(null)) {
        atr = doc.createAttribute("abstract.supertype");
        atr.setValue("YES");
        entity_elem.setAttributeNode(atr);
      }
      AEntity_definition supertypes = def.getSupertypes(null);
      if (supertypes.getMemberCount() > 0) {
        if (iter_auxil == null) {
          iter_auxil = supertypes.createIterator();
        } else {
          supertypes.attachIterator(iter_auxil);
        }
        atr = doc.createAttribute("supertypes");
        String s_types = "";
        int count = 0;
        while (iter_auxil.next()) {
          EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberEntity(iter_auxil);
          count++;
          if (count > 1) {
            s_types = s_types + " ";
          }
          s_types = s_types + getCorrectedName(supertype);
        }
        atr.setValue(s_types);
        entity_elem.setAttributeNode(atr);
      }
      sup_expression = convertSuperExpression(def, iter_auxil, subtypes);
      if (!sup_expression.equals("")) {
        atr = doc.createAttribute("super.expression");
        atr.setValue(sup_expression);
        entity_elem.setAttributeNode(atr);
      }
      AAttribute ent_attrs = def.getAttributes(null, null);
      int ln = ent_attrs.getMemberCount();
      if (attribs == null || ln > attribs.length) {
        attribs = new EAttribute[ln];
      }
      if (iter_auxil == null) {
        iter_auxil = ent_attrs.createIterator();
      } else {
        ent_attrs.attachIterator(iter_auxil);
      }
      int j;
      EAttribute ent_at;
      int attr_count = 0;
      while (iter_auxil.next()) {
        attribs[attr_count++] = (EAttribute)ent_attrs.getCurrentMemberEntity(iter_auxil);
      }
      for (j = 0; j < attr_count/2; j++) {
        ent_at = attribs[j];
        attribs[j] = attribs[attr_count - j - 1];
        attribs[attr_count - j - 1] = ent_at;
      }
      for (j = 0; j < attr_count; j++) {
        ent_at = attribs[j];
//      while (iter_auxil.next()) {
//        ent_at = (EAttribute)ent_attrs.getCurrentMemberEntity(iter_auxil);
        if (ent_at instanceof EExplicit_attribute) {
//RR-pdb-support            convertExplicitAttribute((EExplicit_attribute)ent_at, entity_elem, doc);
          convertExplicitAttribute((EExplicit_attribute)ent_at, entity_elem, doc, xpr_code_aggr);
        }
      }
      for (j = 0; j < attr_count; j++) {
        ent_at = attribs[j];
//      iter_auxil.beginning();
//      while (iter_auxil.next()) {
//        ent_at = (EAttribute)ent_attrs.getCurrentMemberEntity(iter_auxil);
        if (ent_at instanceof EDerived_attribute) {
          iter = convertDerivedAttribute((EDerived_attribute)ent_at, xpr_code_aggr, iter_auxil2, entity_elem, doc);
          if (iter != null) {
            iter_auxil2 = iter;
          }
        }
      }
      for (j = 0; j < attr_count; j++) {
        ent_at = attribs[j];
//      iter_auxil.beginning();
//      while (iter_auxil.next()) {
//        ent_at = (EAttribute)ent_attrs.getCurrentMemberEntity(iter_auxil);
        if (ent_at instanceof EInverse_attribute) {
//RR-pdb-support            convertInverseAttribute((EInverse_attribute)ent_at, entity_elem, doc);
          convertInverseAttribute((EInverse_attribute)ent_at, entity_elem, doc, xpr_code_aggr);
        }
      }
      iter = convertUniquenessRules(def.getUniqueness_rules(null, null), un_rules, iter_auxil, entity_elem, doc);
      if (iter != null) {
        iter_auxil = iter;
      }
      if (xpr_code_aggr != null) {
        iter = convertWhereRules(def.getWhere_rules(null, null), wh_rules, xpr_code_aggr, iter_auxil, 
          iter_auxil2, entity_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
    }

    jsdai.dictionary.EEntity_definition subt_constr_decls_def = und_schema.getEntityDefinition(
      "local_declaration+subtype_constraint_declaration");
    AEntity subt_constr_decls = model.getInstances(subt_constr_decls_def);
    SdaiIterator iter_subt_constr_decls = subt_constr_decls.createIterator();
    while (iter_subt_constr_decls.next()) {
      decl = (EDeclaration)subt_constr_decls.getCurrentMemberEntity(iter_subt_constr_decls);
      ESub_supertype_constraint subt_constr = (ESub_supertype_constraint)decl.getDefinition(null);
      Element subt_constr_elem = doc.createElement("subtype.constraint");
      sch_elem.appendChild(subt_constr_elem);
      atr = doc.createAttribute("name");
      atr.setValue(subt_constr.getName(null));
      subt_constr_elem.setAttributeNode(atr);
      atr = doc.createAttribute("entity");
      atr.setValue(getCorrectedName(subt_constr.getGeneric_supertype(null)));
      subt_constr_elem.setAttributeNode(atr);
      if (subt_constr.testAbstract_supertype(null) && subt_constr.getAbstract_supertype(null)) {
        atr = doc.createAttribute("abstract.supertype");
        atr.setValue("YES");
        subt_constr_elem.setAttributeNode(atr);
      }
      if (subt_constr.testTotal_cover(null)) {
        iter = convertEntitiesAggr(subt_constr.getTotal_cover(null), "totalover", iter_auxil, subt_constr_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
      if (subt_constr.testConstraint(null)) {
        ESubtype_expression expression = subt_constr.getConstraint(null);
        if (expression != null ) {
          sup_expression = convertSubtypeConstraint(expression);
          if (!sup_expression.equals("")) {
            atr = doc.createAttribute("super.expression");
            atr.setValue(sup_expression);
            subt_constr_elem.setAttributeNode(atr);
          }
        }
      }
    }

    jsdai.dictionary.EEntity_definition rule_decls_def = und_schema.getEntityDefinition("local_declaration+rule_declaration");
    AEntity rule_decls = model.getInstances(rule_decls_def);
    int r_count = 0;
    EGlobal_rule [] rules = new EGlobal_rule[rule_decls.getMemberCount()];
    SdaiIterator iter_rule_decls = rule_decls.createIterator();
    while (iter_rule_decls.next()) {
      decl = (EDeclaration)rule_decls.getCurrentMemberEntity(iter_rule_decls);
      rules[r_count++] = (EGlobal_rule)decl.getDefinition(null);
    }
    Arrays.sort(rules, new SorterForRules());
    for (i = 0; i < r_count; i++) {
      EGlobal_rule glob_rule = rules[i];
//    while (iter_rule_decls.next()) {
//      decl = (EDeclaration)rule_decls.getCurrentMemberEntity(iter_rule_decls);
//      EGlobal_rule glob_rule = (EGlobal_rule)decl.getDefinition(null);
      Element rule_elem = doc.createElement("rule");
      sch_elem.appendChild(rule_elem);
      atr = doc.createAttribute("name");
      atr.setValue(getCorrectedName(glob_rule));
//      atr.setValue(glob_rule.getName(null));
      rule_elem.setAttributeNode(atr);
      iter = convertEntitiesAggr(glob_rule.getEntities(null), "appliesto", iter_auxil, rule_elem, doc);
      if (iter != null) {
        iter_auxil = iter;
      }
      if (xpr_code_aggr != null) {
        iter = convertAlgorithm(xpr_code_aggr, glob_rule, iter_auxil, rule_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
        iter = convertWhereRules(glob_rule.getWhere_rules(null, null), wh_rules, xpr_code_aggr, iter_auxil, 
          iter_auxil2, rule_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
    }

    jsdai.dictionary.EEntity_definition funct_decls_def = und_schema.getEntityDefinition("function_declaration+local_declaration");
    AEntity funct_decls = model.getInstances(funct_decls_def);
    int f_count = 0;
    EFunction_definition [] functions = new EFunction_definition[funct_decls.getMemberCount()];
    SdaiIterator iter_funct_decls = funct_decls.createIterator();
    while (iter_funct_decls.next()) {
      decl = (EDeclaration)funct_decls.getCurrentMemberEntity(iter_funct_decls);
      functions[f_count++] = (EFunction_definition)decl.getDefinition(null);
    }
    Arrays.sort(functions, new SorterForFunctions());
    for (i = 0; i < f_count; i++) {
      EFunction_definition funct_def = functions[i];
//    while (iter_funct_decls.next()) {
//      decl = (EDeclaration)funct_decls.getCurrentMemberEntity(iter_funct_decls);
//      EFunction_definition funct_def = (EFunction_definition)decl.getDefinition(null);
      Element funct_elem = doc.createElement("function");
      sch_elem.appendChild(funct_elem);
      atr = doc.createAttribute("name");
      atr.setValue(getCorrectedName(funct_def));
//      atr.setValue(funct_def.getName(null));
      funct_elem.setAttributeNode(atr);
//RR-pdb-support      iter = convertParameters(funct_def.getParameters(null), iter_auxil, funct_elem, doc);
      iter = convertParameters(funct_def.getParameters(null), iter_auxil, funct_elem, doc, xpr_code_aggr);
      if (iter != null) {
        iter_auxil = iter;
      }
      if (funct_def.testReturn_type_label(null)) {
//RR-pdb-support          convertUnderType(funct_def.getReturn_type(null), funct_elem, doc, funct_def.getReturn_type_label(null), 0);
        convertUnderType(funct_def.getReturn_type(null), funct_elem, doc, funct_def.getReturn_type_label(null), 0, xpr_code_aggr);
      } else {
//RR-pdb-support          convertUnderType(funct_def.getReturn_type(null), funct_elem, doc, null, 0);
        convertUnderType(funct_def.getReturn_type(null), funct_elem, doc, null, 0, xpr_code_aggr);
      }
      if (xpr_code_aggr != null) {
        iter = convertAlgorithm(xpr_code_aggr, funct_def, iter_auxil, funct_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
    }

    jsdai.dictionary.EEntity_definition proc_decls_def = und_schema.getEntityDefinition("local_declaration+procedure_declaration");
    AEntity proc_decls = model.getInstances(proc_decls_def);
    SdaiIterator iter_proc_decls = proc_decls.createIterator();
    while (iter_proc_decls.next()) {
      decl = (EDeclaration)proc_decls.getCurrentMemberEntity(iter_proc_decls);
      EProcedure_definition proc_def = (EProcedure_definition)decl.getDefinition(null);
      Element proc_elem = doc.createElement("procedure");
      sch_elem.appendChild(proc_elem);
      atr = doc.createAttribute("name");
      atr.setValue(getCorrectedName(proc_def));
//      atr.setValue(proc_def.getName(null));
      proc_elem.setAttributeNode(atr);
//RR-pdb-support      iter = convertParameters(proc_def.getParameters(null), iter_auxil, proc_elem, doc);
      iter = convertParameters(proc_def.getParameters(null), iter_auxil, proc_elem, doc, xpr_code_aggr);
      if (iter != null) {
        iter_auxil = iter;
      }
      if (xpr_code_aggr != null) {
        iter = convertAlgorithm(xpr_code_aggr, proc_def, iter_auxil, proc_elem, doc);
        if (iter != null) {
          iter_auxil = iter;
        }
      }
    }

    root.appendChild(sch_elem);
    doc.appendChild(root);
//    String file_name = schema_name + FILE_EXTENSION;
    try {
      saveToFile(file_name, doc, EXPRESS_XML_DOCTYPE_VALUE);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      System.out.println("PROBLEM: " + e.getMessage());
      e.printStackTrace();
    }
  }


  private static SdaiIterator convertInterfacedItems(AInterfaced_declaration i_items, SdaiIterator iter_auxil, 
      Element parent, Document doc) throws SdaiException {
    if (i_items.getMemberCount() > 0) {
      if (iter_auxil == null) {
        iter_auxil = i_items.createIterator();
      } else {
        i_items.attachIterator(iter_auxil);
      }
      while (iter_auxil.next()) {
        EInterfaced_declaration item = (EInterfaced_declaration)i_items.getCurrentMemberEntity(iter_auxil);
        if (item instanceof EImplicit_declaration) {
          continue;
        }
        Element item_elem = doc.createElement("interfaced.item");
        parent.appendChild(item_elem);
        Attr atr = doc.createAttribute("name");
        EEntity item_def =  item.getDefinition(null);
        if (item_def instanceof EDefined_type) {
          atr.setValue(((EDefined_type)item_def).getName(null));
        } else if (item_def instanceof EEntity_definition) {
          atr.setValue(getCorrectedName((EEntity_definition)item_def));
        } else if (item_def instanceof EAlgorithm_definition) {
          atr.setValue(((EAlgorithm_definition)item_def).getName(null));
        } else if (item_def instanceof EConstant_definition) {
          atr.setValue(((EConstant_definition)item_def).getName(null));
        } else {
//
        }
        item_elem.setAttributeNode(atr);
        if (item.testAlias_name(null)) {
          atr = doc.createAttribute("alias");
          atr.setValue(item.getAlias_name(null));
          item_elem.setAttributeNode(atr);
        }
      }
    }
    return iter_auxil;
  }


  private static SdaiIterator takeExpression(AEntity xpr_code_aggr, EEntity owner, SdaiIterator iter_auxil, 
      Element parent, String attr_name, Document doc) throws SdaiException {
    if (iter_auxil == null) {
      iter_auxil = xpr_code_aggr.createIterator();
    } else {
      xpr_code_aggr.attachIterator(iter_auxil);
    }
    String value = null;
    while (iter_auxil.next()) {
      EExpress_code xpr_code = (EExpress_code)xpr_code_aggr.getCurrentMemberEntity(iter_auxil);
      if (xpr_code.getTarget(null) == owner) {
        A_string expression_aggr = xpr_code.getValues(null);
        value = expression_aggr.getByIndex(1);
        break;
      }
    }
    if (value != null) {
      Attr atr = doc.createAttribute(attr_name);
      atr.setValue(value);
      parent.setAttributeNode(atr);
    }
    return iter_auxil;
  }


  private static String convertSuperExpression(EEntity_definition definition, SdaiIterator iter_auxil, ASub_supertype_constraint subtypes) throws SdaiException {
    if (subtypes == null) {
      subtypes = new ASub_supertype_constraint();
    }
    String super_expres = "";
    CSub_supertype_constraint.usedinGeneric_supertype(null, definition, null, subtypes);
    if (subtypes.getMemberCount() > 0) {
      if (iter_auxil == null) {
        iter_auxil = subtypes.createIterator();
      } else {
        subtypes.attachIterator(iter_auxil);
      }
      while (iter_auxil.next()) {
        ESub_supertype_constraint subtp = subtypes.getCurrentMember(iter_auxil);
        if (subtp.testConstraint(null) && !subtp.testName(null)) {
          super_expres += convertSubtypeConstraint(subtp.getConstraint(null));
        }
      }
    } 
    subtypes.clear();
    if (super_expres.length() > 1 && super_expres.substring(0,1).equals("(")) {
      return super_expres.substring(1, super_expres.length() - 1);
    }
    return super_expres;
  }


  private static String convertSubtypeConstraint(ESubtype_expression expression) throws SdaiException {
    String s_expres = "";
    if (expression.getGeneric_operands(null).getMemberCount() > 1) {
      if (expression instanceof EOneof_subtype_expression) {
        s_expres += "ONEOF (";
        s_expres += convertOperands(expression, ", ");
        s_expres += ")";
      } else if (expression instanceof EAndor_subtype_expression) {
        s_expres += "(";
        s_expres += convertOperands(expression, " ANDOR ");
        s_expres += ")";
      } else if (expression instanceof EAnd_subtype_expression) {
        s_expres += "(";
        s_expres += convertOperands(expression, " AND ");
        s_expres += ")";
      }
    } else {
      s_expres += convertOperands(expression, "");
    }
    return s_expres;
  }


  private static String convertOperands(ESubtype_expression expression, String separator) throws SdaiException {
    String oper_expres = "";
    AEntity operands = expression.getGeneric_operands(null);
    SdaiIterator it_oper = operands.createIterator();
    boolean first = true;
    while (it_oper.next()) {
      if (first) {
        first = false;
      } else {
        oper_expres += separator;
      }
      EEntity oper = operands.getCurrentMemberEntity(it_oper);
      if (oper instanceof EEntity_definition) {
        oper_expres += getCorrectedName((EEntity_definition)oper);
      } else if (oper instanceof ESubtype_expression) {
        oper_expres += convertSubtypeConstraint((ESubtype_expression)oper);
      }
    }
    return oper_expres;
  }


  private static SdaiIterator convertWhereRules(AWhere_rule w_rules, EWhere_rule [] wh_rules, AEntity xpr_code_aggr, 
      SdaiIterator iter_auxil, SdaiIterator iter_auxil2, Element parent, Document doc) throws SdaiException {
    int ln = w_rules.getMemberCount();
    if (ln > 0) {
      if (wh_rules == null || ln > wh_rules.length) {
        wh_rules = new EWhere_rule[ln];
      }
      if (iter_auxil == null) {
        iter_auxil = w_rules.createIterator();
      } else {
        w_rules.attachIterator(iter_auxil);
      }
      EWhere_rule w_rule;
      int wr_count = 0;
      while (iter_auxil.next()) {
        wh_rules[wr_count++] = (EWhere_rule)w_rules.getCurrentMemberEntity(iter_auxil);
      }
      Arrays.sort(wh_rules, 0, wr_count, new SorterForWheres());
      for (int i = 0; i < wr_count; i++) {
        w_rule = wh_rules[i];
//      while (iter_auxil.next()) {
//        EWhere_rule w_rule = (EWhere_rule)w_rules.getCurrentMemberEntity(iter_auxil);
        Element w_rule_elem = doc.createElement("where");
        parent.appendChild(w_rule_elem);
        Attr atr;
        if (w_rule.testLabel(null)) {
          atr = doc.createAttribute("label");
          atr.setValue(getCorrectedName(w_rule));
//          atr.setValue(w_rule.getLabel(null));
          w_rule_elem.setAttributeNode(atr);
        }
        takeExpression(xpr_code_aggr, w_rule, iter_auxil2, w_rule_elem, "expression", doc);
      }
    }
    return iter_auxil;
  }


/*

	BUG #3634

  need to change
  
  from:

		<unique.attribute attribute="in_set" entity-ref="area_in_set"/>  
  
  to:

		<unique.attribute attribute="SELF\area_in_set.in_set"/>

IMPLEMENTED.

TEST:

previous:

      <entity name="drawing_sheet_revision_usage" supertypes="area_in_set">
         <explicit name="sheet_number">
            <typename name="identifier"/>
         </explicit>
         <unique label="UR1">
            <unique.attribute attribute="sheet_number"/>
            <unique.attribute attribute="in_set" entity-ref="area_in_set"/>
         </unique>
         <where expression="('DRAWING_DEFINITION_SCHEMA.DRAWING_SHEET_REVISION' IN &#10;                  TYPEOF(SELF\area_in_set.area)) &#10;              AND&#10;              ('DRAWING_DEFINITION_SCHEMA.DRAWING_REVISION' &#10;               IN TYPEOF (SELF\area_in_set.in_set))" label="WR1"/>
      </entity>


now:

      <entity name="drawing_sheet_revision_usage" supertypes="area_in_set">
         <explicit name="sheet_number">
            <typename name="identifier"/>
         </explicit>
         <unique label="UR1">
            <unique.attribute attribute="sheet_number"/>
            <unique.attribute attribute="SELF\area_in_set.in_set"/>
         </unique>
         <where expression="('DRAWING_DEFINITION_SCHEMA.DRAWING_SHEET_REVISION' IN &#10;                  TYPEOF(SELF\area_in_set.area)) &#10;              AND&#10;              ('DRAWING_DEFINITION_SCHEMA.DRAWING_REVISION' &#10;               IN TYPEOF (SELF\area_in_set.in_set))" label="WR1"/>
      </entity>




*/

  private static SdaiIterator convertUniquenessRules(AUniqueness_rule u_rules, EUniqueness_rule [] un_rules, 
      SdaiIterator iter_auxil, Element parent, Document doc) throws SdaiException {
    int i;
    SdaiIterator iter_attrs = null;
    Attr atr;
    int ln = u_rules.getMemberCount();
    if (ln > 0) {
      if (un_rules == null || ln > un_rules.length) {
        un_rules = new EUniqueness_rule[ln];
      }
      if (iter_auxil == null) {
        iter_auxil = u_rules.createIterator();
      } else {
        u_rules.attachIterator(iter_auxil);
      }
      EUniqueness_rule u_rule;
      int ur_count = 0;
      while (iter_auxil.next()) {
        un_rules[ur_count++] = (EUniqueness_rule)u_rules.getCurrentMemberEntity(iter_auxil);
      }
      for (i = 0; i < ur_count/2; i++) {
        u_rule = un_rules[i];
        un_rules[i] = un_rules[ur_count - i - 1];
        un_rules[ur_count - i - 1] = u_rule;
      }
      for (i = 0; i < ur_count; i++) {
        u_rule = un_rules[i];
//      while (iter_auxil.next()) {
//        EUniqueness_rule u_rule = (EUniqueness_rule)u_rules.getCurrentMemberEntity(iter_auxil);
        Element u_rule_elem = doc.createElement("unique");
        parent.appendChild(u_rule_elem);
        if (u_rule.testLabel(null)) {
          atr = doc.createAttribute("label");
          atr.setValue(getCorrectedName(u_rule));
//          atr.setValue(u_rule.getLabel(null));
          u_rule_elem.setAttributeNode(atr);
        }
        EEntity_definition def = u_rule.getParent_entity(null);
        String def_name = def.getName(null);
        AAttribute attrs = u_rule.getAttributes(null);
        int count = attrs.getMemberCount();
        if (iter_attrs == null) {
          iter_attrs = attrs.createIterator();
        } else {
          attrs.attachIterator(iter_attrs);
        }
        while (iter_attrs.next()) {
          EAttribute attrib = (EAttribute)attrs.getCurrentMemberEntity(iter_attrs);
          Element attrib_elem = doc.createElement("unique.attribute");
          u_rule_elem.appendChild(attrib_elem);
          EEntity_definition parent_def = attrib.getParent_entity(null);

// old implementation ---------------------------------------------
/*
          if (parent_def != def) {
            atr = doc.createAttribute("entity-ref");
            atr.setValue(getCorrectedName(parent_def));
            attrib_elem.setAttributeNode(atr);
          }
          atr = doc.createAttribute("attribute");
          atr.setValue(attrib.getName(null));
          attrib_elem.setAttributeNode(atr);
*/
// end of old implementation ---------------------------------------------
          if (parent_def != def) {
	          atr = doc.createAttribute("attribute");
  	        atr.setValue("SELF\\" + getCorrectedName(parent_def) + "." + attrib.getName(null));
    	      attrib_elem.setAttributeNode(atr);
          } else {
	          atr = doc.createAttribute("attribute");
  	        atr.setValue(attrib.getName(null));
    	      attrib_elem.setAttributeNode(atr);
          }

        }
      }
    }
    return iter_auxil;
  }


//RR-pdb-support  private static SdaiIterator convertParameters(AParameter parameters, SdaiIterator iter_auxil, Element parent, Document doc) throws SdaiException {
  private static SdaiIterator convertParameters(AParameter parameters, SdaiIterator iter_auxil, Element parent, Document doc, AEntity xpr_code_aggr) throws SdaiException {
    if (parameters.getMemberCount() > 0) {
      if (iter_auxil == null) {
        iter_auxil = parameters.createIterator();
      } else {
        parameters.attachIterator(iter_auxil);
      }
      while (iter_auxil.next()) {
        EParameter param = (EParameter)parameters.getCurrentMemberEntity(iter_auxil);
        Element param_elem = doc.createElement("parameter");
        parent.appendChild(param_elem);
        String name = param.getName(null);
        if (param.testVar_type(null) && param.getVar_type(null)) {
          name = VAR_PARAMETER + name;
        }
        Attr atr = doc.createAttribute("name");
        atr.setValue(name);
        param_elem.setAttributeNode(atr);
        A_string labels = null;
        if (param.testType_labels(null)) {
          labels = param.getType_labels(null);
        }
//RR-pdb-support        convertUnderType(param.getParameter_type(null), param_elem, doc, labels, 0);
        convertUnderType(param.getParameter_type(null), param_elem, doc, labels, 0, xpr_code_aggr);
      }
    }
    return iter_auxil;
  }


  private static SdaiIterator convertAlgorithm(AEntity xpr_code_aggr, EEntity def, 
      SdaiIterator iter_auxil, Element parent, Document doc) throws SdaiException {
    if (iter_auxil == null) {
      iter_auxil = xpr_code_aggr.createIterator();
    } else {
      xpr_code_aggr.attachIterator(iter_auxil);
    }
    String value = null;
    while (iter_auxil.next()) {
      EExpress_code xpr_code = (EExpress_code)xpr_code_aggr.getCurrentMemberEntity(iter_auxil);
      if (xpr_code.getTarget(null) == def) {
        A_string expression_aggr = xpr_code.getValues(null);
        value = expression_aggr.getByIndex(1);
        break;
      }
    }
    if (value != null && !value.equals("") && !value.equals(" ")) {
      Element alg_elem = doc.createElement("algorithm");
      parent.appendChild(alg_elem);
      Text txt = doc.createTextNode(value);
      alg_elem.appendChild(txt);
    }
    return iter_auxil;
  }


  private static void convertRedeclaration(EAttribute attrib, String atr_name, Element parent, Document doc)
      throws SdaiException {
    Element redecl_elem = doc.createElement("redeclaration");
    parent.appendChild(redecl_elem);
    Attr atr = doc.createAttribute("entity-ref");
    atr.setValue(getCorrectedName(attrib.getParent_entity(null)));
    redecl_elem.setAttributeNode(atr);
    String o_name = attrib.getName(null);
    if (!(o_name.equals(atr_name))) {
      atr = doc.createAttribute("old_name");
      atr.setValue(o_name);
      redecl_elem.setAttributeNode(atr);
    }
  }


//RR-pdb-support  private static void convertDefType(EData_type type, Element parent, Document doc) throws SdaiException {
  private static void convertDefType(EData_type type, Element parent, Document doc, AEntity xpr_code_aggr) throws SdaiException {
    if (type instanceof EAggregation_type) {
//RR-pdb-support        convertTypeAggr((EAggregation_type)type, parent, doc, true, null, 0);
      convertTypeAggr((EAggregation_type)type, parent, doc, true, null, 0, xpr_code_aggr);
    } else if (type instanceof ESimple_type) {
//RR-pdb-support        convertTypeSimple((ESimple_type)type, parent, doc);
      convertTypeSimple((ESimple_type)type, parent, doc, xpr_code_aggr);
    } else if (type instanceof EDefined_type || type instanceof EEntity_definition) {
      convertTypeNamed(type, parent, doc);
    } else if (type instanceof ESelect_type) {
      convertTypeSelect((ESelect_type)type, parent, doc);
    } else if (type instanceof EEnumeration_type) {
      convertTypeEnumeration((EEnumeration_type)type, parent, doc);
    } else {
//
    }
  }


  private static void convertTypeSelect(ESelect_type sel_type, Element parent, Document doc) throws SdaiException {
    Element elem = doc.createElement("select");
    parent.appendChild(elem);
    if (sel_type instanceof EExtensible_select_type) {
      Attr ext = doc.createAttribute("extensible");
      ext.setValue("YES");
      elem.setAttributeNode(ext);
    }
    if (sel_type instanceof EEntity_select_type) {
      Attr generic = doc.createAttribute("genericentity");
      generic.setValue("YES");
      elem.setAttributeNode(generic);
    }
    if (sel_type instanceof EExtended_select_type) {
      Attr based = doc.createAttribute("basedon");
      EData_type bas = ((EExtended_select_type)sel_type).getIs_based_on(null);
      if (bas instanceof EDefined_type) {
        based.setValue(bas.getName(null));
      } else {
        String str = bas.getName(null);
        str = str.substring(8);
        based.setValue(str);
      }
//      based.setValue(bas.getName(null));
      elem.setAttributeNode(based);
    }
    ANamed_type sels = sel_type.getSelections(null);
    int ln = sels.getMemberCount();
    if (ln > 0) {
      Attr items = doc.createAttribute("selectitems");
      String selections = "";
      for (int i = 1; i <= ln; i++) {
        EData_type alternative = (EData_type)sels.getByIndexObject(i);
        if (i > 1) {
          selections = selections + " ";
        }
        String alt_name = alternative.getName(null);
//        if (alternative instanceof EEntity_definition) {
//          alt_name = alt_name.substring(0, 1).toUpperCase() + alt_name.substring(1).toLowerCase();
//        }
        selections = selections + alt_name;
      }
      items.setValue(selections);
      elem.setAttributeNode(items);
    }
  }


  private static void convertTypeEnumeration(EEnumeration_type enum_type, Element parent, Document doc) throws SdaiException {
    Element elem = doc.createElement("enumeration");
    parent.appendChild(elem);
    if (enum_type instanceof EExtensible_enumeration_type) {
      Attr ext = doc.createAttribute("extensible");
      ext.setValue("YES");
      elem.setAttributeNode(ext);
    }
    if (enum_type instanceof EExtended_enumeration_type) {
      Attr based = doc.createAttribute("basedon");
      EData_type bas = ((EExtended_enumeration_type)enum_type).getIs_based_on(null);
      based.setValue(bas.getName(null));
      elem.setAttributeNode(based);
    }
    A_string elems = enum_type.getElements(null);
    int ln = elems.getMemberCount();
    if (ln > 0) {
      Attr items = doc.createAttribute("items");
      String elements = "";
      for (int i = 1; i <= ln; i++) {
        String element = (String)elems.getByIndexObject(i);
        if (i > 1) {
          elements = elements + " ";
        }
        elements = elements + element;
      }
      items.setValue(elements);
      elem.setAttributeNode(items);
    }
  }


  private static SdaiIterator convertEntitiesAggr(AEntity_definition entities, String attr_name, 
      SdaiIterator iter_auxil, Element parent, Document doc) throws SdaiException {
    Attr atr = doc.createAttribute(attr_name);
    if (iter_auxil == null) {
      iter_auxil = entities.createIterator();
    } else {
      entities.attachIterator(iter_auxil);
    }
    String ents = "";
    int i = 0;
    while (iter_auxil.next()) {
      EEntity_definition ent_def = (EEntity_definition)entities.getCurrentMemberEntity(iter_auxil);
      i++;
      String name_norm = getCorrectedName(ent_def);
      if (i > 1) {
        ents = ents + " ";
      }
      ents = ents + name_norm;
    }
    atr.setValue(ents);
    parent.setAttributeNode(atr);
    return iter_auxil;
  }


//RR-pdb-support  private static void convertExplicitAttribute(EExplicit_attribute ent_at, Element parent, Document doc) throws SdaiException {
  private static void convertExplicitAttribute(EExplicit_attribute ent_at, Element parent, Document doc, AEntity xpr_code_aggr) throws SdaiException {
    Element attr_elem = doc.createElement("explicit");
    parent.appendChild(attr_elem);
    Attr atr = doc.createAttribute("name");
    String a_name = ent_at.getName(null);
    atr.setValue(a_name);
    attr_elem.setAttributeNode(atr);
    if (ent_at.getOptional_flag(null)) {
      atr = doc.createAttribute("optional");
      atr.setValue("YES");
      attr_elem.setAttributeNode(atr);
    }
//RR-pdb-support    convertUnderType((EData_type)ent_at.getDomain(null), attr_elem, doc, null, 0);
    convertUnderType((EData_type)ent_at.getDomain(null), attr_elem, doc, null, 0, xpr_code_aggr);
    if (ent_at.testRedeclaring(null)) {
      convertRedeclaration(ent_at.getRedeclaring(null), a_name, attr_elem, doc);
    }
  }


  private static SdaiIterator convertDerivedAttribute(EDerived_attribute ent_at, AEntity xpr_code_aggr, 
      SdaiIterator iter_auxil2, Element parent, Document doc) throws SdaiException {
    Element attr_elem = doc.createElement("derived");
    parent.appendChild(attr_elem);
    Attr atr = doc.createAttribute("name");
    String a_name = ent_at.getName(null);
    atr.setValue(a_name);
    attr_elem.setAttributeNode(atr);
    if (xpr_code_aggr != null) {
      SdaiIterator iter = takeExpression(xpr_code_aggr, ent_at, iter_auxil2, attr_elem, "expression", doc);
      if (iter != null) {
        iter_auxil2 = iter;
      }
    }
//RR-pdb-support    convertUnderType((EData_type)ent_at.getDomain(null), attr_elem, doc, null, 0);
    convertUnderType((EData_type)ent_at.getDomain(null), attr_elem, doc, null, 0, xpr_code_aggr);
    if (ent_at.testRedeclaring(null)) {
      convertRedeclaration((EAttribute)ent_at.getRedeclaring(null), a_name, attr_elem, doc);
    }
    return iter_auxil2;
  }


//RR-pdb-support  private static void convertInverseAttribute(EInverse_attribute ent_at, Element parent, Document doc) 
  private static void convertInverseAttribute(EInverse_attribute ent_at, Element parent, Document doc, AEntity xpr_code_aggr) 
      throws SdaiException {
    Element attr_elem = doc.createElement("inverse");
    parent.appendChild(attr_elem);
    Attr atr = doc.createAttribute("name");
    String a_name = ent_at.getName(null);
    atr.setValue(a_name);
    attr_elem.setAttributeNode(atr);
    atr = doc.createAttribute("entity");
    EEntity_definition def = ent_at.getDomain(null);
    atr.setValue(getCorrectedName(def));
    attr_elem.setAttributeNode(atr);
    atr = doc.createAttribute("attribute");
    EExplicit_attribute inver_attr = ent_at.getInverted_attr(null);
    atr.setValue(inver_attr.getName(null));
    attr_elem.setAttributeNode(atr);
    if (ent_at.testMin_cardinality(null)) {
      Element inv_aggr_elem = doc.createElement("inverse.aggregate");
      attr_elem.appendChild(inv_aggr_elem);
      atr = doc.createAttribute("type");
      if (ent_at.getDuplicates(null)) {
        atr.setValue("BAG");
      } else {
        atr.setValue("SET");
      }
      inv_aggr_elem.setAttributeNode(atr);
      EBound bnd = ent_at.getMin_cardinality(null);
      atr = doc.createAttribute("lower");
//RR-pdb-support      atr.setValue(Integer.toString(bnd.getBound_value(null)));
      atr.setValue(getBoundString(bnd, xpr_code_aggr));
      inv_aggr_elem.setAttributeNode(atr);
      atr = doc.createAttribute("upper");
      if (ent_at.testMax_cardinality(null)) {
        bnd = ent_at.getMax_cardinality(null);
//-pdb-support        atr.setValue(Integer.toString(bnd.getBound_value(null)));
	      atr.setValue(getBoundString(bnd, xpr_code_aggr));
      } else {
        atr.setValue("?");
      }
      inv_aggr_elem.setAttributeNode(atr);
    }
    if (ent_at.testRedeclaring(null)) {
      convertRedeclaration(ent_at.getRedeclaring(null), a_name, attr_elem, doc);
    }
  }


//RR-pdb-support  private static void convertUnderType(EData_type type, Element parent, Document doc, Object labels, int index) throws SdaiException {
  private static void convertUnderType(EData_type type, Element parent, Document doc, Object labels, int index, AEntity xpr_code_aggr) throws SdaiException {
    Element elem;
    Attr type_atr;
    String lb;
    if (type instanceof EAggregation_type) {
//RR-pdb-suport        convertTypeAggr((EAggregation_type)type, parent, doc, false, labels, index);
      convertTypeAggr((EAggregation_type)type, parent, doc, false, labels, index, xpr_code_aggr);
    } else if (type instanceof ESimple_type) {
//RR-pdb-support        convertTypeSimple((ESimple_type)type, parent, doc);
      convertTypeSimple((ESimple_type)type, parent, doc, xpr_code_aggr);
    } else if (type instanceof EDefined_type || type instanceof EEntity_definition) {
      convertTypeNamed(type, parent, doc);
//    } else if (type == ExpressTypes.GENERIC_ENTITY_TYPE) {
    } else if (type.getName(null).equals("_ENTITY")) {
      elem = doc.createElement("builtintype");
      parent.appendChild(elem);
      type_atr = doc.createAttribute("type");
      type_atr.setValue("GENERICENTITY");
      elem.setAttributeNode(type_atr);
      if (labels != null) {
        lb = getLabel(labels, index);
        if (lb != null) {
          type_atr = doc.createAttribute("typelabel");
          type_atr.setValue(lb);
          elem.setAttributeNode(type_atr);
        }
      }
//    } else if (type == ExpressTypes.GENERIC_TYPE) {
    } else if (type.getName(null).equals("_GENERIC")) {
      elem = doc.createElement("builtintype");
      parent.appendChild(elem);
      type_atr = doc.createAttribute("type");
      type_atr.setValue("GENERIC");
      elem.setAttributeNode(type_atr);
      if (labels != null) {
        lb = getLabel(labels, index);
        if (lb != null) {
          type_atr = doc.createAttribute("typelabel");
          type_atr.setValue(lb);
          elem.setAttributeNode(type_atr);
        }
      }
    } else {
//
    }
  }

//RR-pdb-support  private static void convertTypeAggr(EAggregation_type aggr_type, Element parent, Document doc, boolean def_type, Object labels, int index) throws SdaiException {
  private static void convertTypeAggr(EAggregation_type aggr_type, Element parent, Document doc, boolean def_type, Object labels, int index, AEntity xpr_code_aggr) throws SdaiException {
    EBound bnd;
    Attr un, low, upp;
    Element elem = doc.createElement("aggregate");
    parent.appendChild(elem);
    Attr type_aggr = doc.createAttribute("type");
    int var_aggr = 0;
    if (aggr_type instanceof ESet_type) {
      type_aggr.setValue("SET");
      elem.setAttributeNode(type_aggr);
    } else if (aggr_type instanceof EList_type) {
      type_aggr.setValue("LIST");
      elem.setAttributeNode(type_aggr);
      var_aggr = 1;
    } else if (aggr_type instanceof EBag_type) {
      type_aggr.setValue("BAG");
      elem.setAttributeNode(type_aggr);
    } else if (aggr_type instanceof EArray_type) {
      type_aggr.setValue("ARRAY");
      elem.setAttributeNode(type_aggr);
      var_aggr = 2;
    } else {
      type_aggr.setValue("AGGREGATE");
      elem.setAttributeNode(type_aggr);
      if (labels != null) {
        String lb = getLabel(labels, index);
        if (lb != null) {
          type_aggr = doc.createAttribute("typelabel");
          type_aggr.setValue(lb);
          elem.setAttributeNode(type_aggr);
        }
      }
      var_aggr = 3;
    }
    if (var_aggr == 1 && ((EList_type)aggr_type).getUnique_flag(null)) {
      un = doc.createAttribute("unique");
      un.setValue("YES");
      elem.setAttributeNode(un);
    }
    if (var_aggr < 2) {
      EVariable_size_aggregation_type var_aggr_type = (EVariable_size_aggregation_type)aggr_type;
      bnd = var_aggr_type.getLower_bound(null);
      low = doc.createAttribute("lower");
//RR-pdb-support      low.setValue(Integer.toString(bnd.getBound_value(null)));
      low.setValue(getBoundString(bnd, xpr_code_aggr));
      elem.setAttributeNode(low);
      upp = doc.createAttribute("upper");
      if (var_aggr_type.testUpper_bound(null)) {
        bnd = var_aggr_type.getUpper_bound(null);
//RR-pdb-support        upp.setValue(Integer.toString(bnd.getBound_value(null)));
      	upp.setValue(getBoundString(bnd, xpr_code_aggr));
      } else {
        upp.setValue("?");
      }
      elem.setAttributeNode(upp);
    } else if (var_aggr == 2) {
      EArray_type arr = (EArray_type)aggr_type;
      if (arr.getOptional_flag(null)) {
        Attr opt = doc.createAttribute("optional");
        opt.setValue("YES");
        elem.setAttributeNode(opt);
      }
      if (arr.getUnique_flag(null)) {
        un = doc.createAttribute("unique");
        un.setValue("YES");
        elem.setAttributeNode(un);
      }
      if (arr.testLower_index(null)) {
        bnd = arr.getLower_index(null);
        low = doc.createAttribute("lower");
//RR-pdb-support        low.setValue(Integer.toString(bnd.getBound_value(null)));
	      low.setValue(getBoundString(bnd, xpr_code_aggr));
        elem.setAttributeNode(low);
      }
      if (arr.testUpper_index(null)) {
        bnd = arr.getUpper_index(null);
        low = doc.createAttribute("upper");
//RR-pdb-support        low.setValue(Integer.toString(bnd.getBound_value(null)));
	      low.setValue(getBoundString(bnd, xpr_code_aggr));
        elem.setAttributeNode(low);
      }
    }
    if (def_type) {
//RR-pdb-support        convertDefType((EData_type)aggr_type.getElement_type(null), parent, doc);
      convertDefType((EData_type)aggr_type.getElement_type(null), parent, doc, xpr_code_aggr);
    } else {
//RR-pdb-support        convertUnderType((EData_type)aggr_type.getElement_type(null), parent, doc, labels, index + 1);
      convertUnderType((EData_type)aggr_type.getElement_type(null), parent, doc, labels, index + 1, xpr_code_aggr);
    }
  }


//RR-pdb-support  private static void convertTypeSimple(ESimple_type type, Element parent, Document doc) throws SdaiException {
  private static void convertTypeSimple(ESimple_type type, Element parent, Document doc, AEntity xpr_code_aggr) throws SdaiException {
    EBound bnd;
    Attr width, fix;
    Element elem = doc.createElement("builtintype");
    parent.appendChild(elem);
    Attr type_atr = doc.createAttribute("type");
    if (type instanceof ENumber_type) {
      type_atr.setValue("NUMBER");
      elem.setAttributeNode(type_atr);
    } else if (type instanceof EInteger_type) {
      type_atr.setValue("INTEGER");
      elem.setAttributeNode(type_atr);
    } else if (type instanceof EReal_type) {
      type_atr.setValue("REAL");
      elem.setAttributeNode(type_atr);
      if (((EReal_type)type).testPrecision(null)) {
        bnd = ((EReal_type)type).getPrecision(null);
        Attr prec = doc.createAttribute("precision");
//RR-pdb-support        prec.setValue(Integer.toString(bnd.getBound_value(null)));
	      prec.setValue(getBoundString(bnd, xpr_code_aggr));
        elem.setAttributeNode(prec);
      }
    } else if (type instanceof EBoolean_type) {
      type_atr.setValue("BOOLEAN");
      elem.setAttributeNode(type_atr);
    } else if (type instanceof ELogical_type) {
      type_atr.setValue("LOGICAL");
      elem.setAttributeNode(type_atr);
    } else if (type instanceof EBinary_type) {
      type_atr.setValue("BINARY");
      elem.setAttributeNode(type_atr);
      if (((EBinary_type)type).testWidth(null)) {
        bnd = ((EBinary_type)type).getWidth(null);
        width = doc.createAttribute("width");
//RR-pdb-support        width.setValue(Integer.toString(bnd.getBound_value(null)));
	      width.setValue(getBoundString(bnd, xpr_code_aggr));
        elem.setAttributeNode(width);
      }
      if (((EBinary_type)type).getFixed_width(null)) {
        fix = doc.createAttribute("fixed");
        fix.setValue("YES");
        elem.setAttributeNode(fix);
      }
    } else if (type instanceof EString_type) {
      type_atr.setValue("STRING");
      elem.setAttributeNode(type_atr);
      EString_type str_type = (EString_type)type;
      if (str_type.testWidth(null)) {
        bnd = str_type.getWidth(null);
        width = doc.createAttribute("width");
//RR-pdb-support        width.setValue(Integer.toString(bnd.getBound_value(null)));
	      width.setValue(getBoundString(bnd, xpr_code_aggr));
        elem.setAttributeNode(width);
      }
      if (str_type.testFixed_width(null) && str_type.getFixed_width(null)) {
        fix = doc.createAttribute("fixed");
        fix.setValue("YES");
        elem.setAttributeNode(fix);
      }
    } else {
//
    }
  }

  //RR-pdb-support      
	private static String getBoundString_OLD(EBound bnd) throws SdaiException {
	  String result = "_pdb_";
	  if (bnd == null) {
	  	result = "?";
	  } else
	  if (bnd instanceof EInteger_bound) {
	  	result = Integer.toString(bnd.getBound_value(null));
	  } else {
	  	// population-dependent-bound
				String result2 = null;
				
				// asmExpressDomain
				ASdaiModel asmExpressDomain = new ASdaiModel();
				// need to add all _EXPRESS_ models to it, or at least the needed model
				
        AEntity aeUsers = new AEntity();
        bnd.findEntityInstanceUsers(asmExpressDomain, aeUsers);
        SdaiIterator iter_users = aeUsers.createIterator();
        while (iter_users.next()) {
        	EEntity eUser = aeUsers.getCurrentMemberEntity(iter_users);
          if (eUser instanceof EExpress_code) {
          	EExpress_code ec = (EExpress_code) eUser;
            A_string asECValues = ec.getValues(null);
            SdaiIterator iter_ecv = asECValues.createIterator();
            while (iter_ecv.next()) {
	          	String expr = (String)asECValues.getCurrentMember(iter_ecv);  
							result2 = expr; // possibly add some formatting if needed
							break;
            }
          }
        }
				if (result2 == null) {
					 result = "??";
				} else {
					 result = result2;
				}

	  } // pdb
		return result;
	}
	private static String getBoundString(EBound bnd, AEntity xpr_code_aggr) throws SdaiException {
	  String result = "";
	  if (bnd == null) {
	  	result = "?";
	  } else
	  if (bnd instanceof EInteger_bound) {
	  	result = Integer.toString(bnd.getBound_value(null));
	  } else {
	  	// population-dependent-bound
			SdaiIterator iter_auxil	= xpr_code_aggr.createIterator();
	    String value = null;
  	  while (iter_auxil.next()) {
    	  EExpress_code xpr_code = (EExpress_code)xpr_code_aggr.getCurrentMemberEntity(iter_auxil);
      	if (xpr_code.getTarget(null) == bnd) {
        	A_string expression_aggr = xpr_code.getValues(null);
	        value = expression_aggr.getByIndex(1);
  	      break;
    	  }
    	} // while
			result = value;	
		}	
		return result;
	}

  private static String getLabel(Object labels, int index) throws SdaiException {
    String lb;
    if (labels instanceof A_string) {
      A_string lab_aggr = (A_string)labels;
      if (index < lab_aggr.getMemberCount()) {
        lb = lab_aggr.getByIndex(index+1);
        if (lb.length() > 0) {
          return lb;
        }
      }
    } else if (labels instanceof String) {
      lb = (String)labels;
      if (lb.length() > 0) {
        return lb;
      }
    }
    return null;
  }


  private static void convertTypeNamed(EData_type type, Element parent, Document doc) throws SdaiException {
    Element elem = doc.createElement("typename");
    parent.appendChild(elem);
    Attr atr = doc.createAttribute("name");
    String tp_name = type.getName(null);
//    if (type instanceof EEntity_definition) {
//      tp_name = tp_name.substring(0, 1).toUpperCase() + tp_name.substring(1).toLowerCase();
//    }
    atr.setValue(tp_name);
    elem.setAttributeNode(atr);
  }

  private static String getCorrectedName(EData_type def) throws SdaiException {
    String ent_name = def.getName(null);
//    return ent_name.substring(0, 1).toUpperCase() + ent_name.substring(1).toLowerCase();
    return ent_name;
  }
  private static String getCorrectedName(ESchema_definition sch) throws SdaiException {
    String sch_name = sch.getName(null);
//    return sch_name.substring(0, 1).toUpperCase() + sch_name.substring(1).toLowerCase();
    return sch_name;
  }
  private static String getCorrectedName(EGlobal_rule rule) throws SdaiException {
    String rule_name = rule.getName(null);
//    return rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
    return rule_name;
  }
  private static String getCorrectedName(EWhere_rule rule) throws SdaiException {
    String rule_name = rule.getLabel(null);
//    return rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
    return rule_name;
  }
  private static String getCorrectedName(EUniqueness_rule rule) throws SdaiException {
    String rule_name = rule.getLabel(null);
//    return rule_name.substring(0, 1).toUpperCase() + rule_name.substring(1).toLowerCase();
    return rule_name;
  }
  private static String getCorrectedName(EFunction_definition funct) throws SdaiException {
    String funct_name = funct.getName(null);
//    return funct_name.substring(0, 1).toUpperCase() + funct_name.substring(1).toLowerCase();
    return funct_name;
  }
  private static String getCorrectedName(EProcedure_definition proc) throws SdaiException {
    String proc_name = proc.getName(null);
//    return proc_name.substring(0, 1).toUpperCase() + proc_name.substring(1).toLowerCase();
    return proc_name;
  }




  static class SorterForTypes implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        return ((EDefined_type)o1).getName(null).compareTo(((EDefined_type)o2).getName(null));
      } catch (SdaiException e) {
        e.printStackTrace(System.out);
        return 0;
      }
    }
  }


  static class SorterForEntities implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        return getCorrectedName((EEntity_definition)o1).compareTo(getCorrectedName((EEntity_definition)o2));
      } catch (SdaiException e) {
        e.printStackTrace(System.out);
        return 0;
      }
    }
  }


  static class SorterForRules implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        return ((EGlobal_rule)o1).getName(null).compareTo(((EGlobal_rule)o2).getName(null));
      } catch (SdaiException e) {
        e.printStackTrace(System.out);
        return 0;
      }
    }
  }


  static class SorterForFunctions implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        return ((EFunction_definition)o1).getName(null).compareTo(((EFunction_definition)o2).getName(null));
      } catch (SdaiException e) {
        e.printStackTrace(System.out);
        return 0;
      }
    }
  }


  static class SorterForWheres implements Comparator {
    public int compare(Object o1, Object o2) {
      try {
        if (((EWhere_rule)o1).getOrder(null) < ((EWhere_rule)o2).getOrder(null)) {
          return -1;
        } else {
          return 1;
        }
      } catch (SdaiException e) {
        e.printStackTrace(System.out);
        return 0;
      }
    }
  }


  /**
   *@author     TomasB
   *@created    March 15, 2004
   */
  static class MyErrorHandler implements ErrorHandler {
    /**
     *@param  exception
     *@exception  SAXParseException
     */
    public void error(SAXParseException exception)
      throws SAXParseException {
      System.out.println("\r\n** ERROR:"
           + "\r\n line " + exception.getLineNumber()
           + "\r\n url " + exception.getSystemId());
      throw exception;
    }


    /**
     *@param  exception
     *@exception  SAXParseException
     */
    public void fatalError(SAXParseException exception)
      throws SAXParseException {
      System.out.println("\r\n** FATAL ERROR:"
           + "\r\n line " + exception.getLineNumber()
           + "\r\n url " + exception.getSystemId());
      throw exception;
    }


    /**
     *@param  exception
     *@exception  SAXParseException
     */
    public void warning(SAXParseException exception)
      throws SAXParseException {
      System.out.println("** Warning"
           + ", line " + exception.getLineNumber()
           + ", uri " + exception.getSystemId());
      System.out.println("   " + exception.getMessage());
    }
  }
 
  public static void saveToFile(String fileName, Document what, String dtd)
    throws IOException, TransformerException {

    TransformerFactory fact = TransformerFactory.newInstance();
    Transformer t = fact.newTransformer();
    t.setOutputProperty(OutputKeys.INDENT, "yes");
    if(dtd != null){
      t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
    }
    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
    t.transform(new DOMSource(what), new StreamResult(new File(fileName)));
  }
 
  
}

