package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ADefined_type;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

class IdForward {
  protected String typeName = null;
  protected Vector forwards = null; //of IdForwardElement's.

  // for internal usage. It's used only for cases, when
  // we need to deal with select types.
  protected EEntity selectType = null;
  // this stores the named_type, where the selectType was
  // found or entity_definition, if selectType concept
  // was not applicable.
  protected EEntity namedType = null;

  public EEntity getSelectType() {
    return selectType;
  }

  public EEntity getNamedType() {
    return namedType;
  }

  public boolean parse(ANamed_type types, EEntity_definition domain, EDefined_type selectType, EEntity_mapping eMapping, EAttribute_mapping aMapping)
      throws SdaiException {
    // let's save the type information immediately
    parse(domain, eMapping, aMapping);
    // now, create all SelectForward elements
    SdaiIterator it = types.createIterator();
    while (it.next()) {
      EEntity type = (EEntity) types.getCurrentMemberObject(it);
      if (type == domain) {
        parse(selectType, eMapping, aMapping);
        return true;
      }
      else if (type instanceof EDefined_type) {
        // get it's domain
        EEntity insideDomain = ((EDefined_type) type).getDomain(null);
        if (!(insideDomain instanceof ESelect_type)) {
//!!					System.out.println("PROBLEM: IdForward.parse(ANamed_type):"+
//!!						" insideDomain is not select type!"+insideDomain+aMapping);
          return false;
        }
        ESelect_type insideType = (ESelect_type) insideDomain;
        ANamed_type insideTypes = insideType.getSelections(null);
        // do recursion
        if (parse(insideTypes, domain, (EDefined_type) type, eMapping, aMapping)) {
          // add myself to Select forwards and return:
          parse(selectType, eMapping, aMapping);
          return true;
        }
      }
    }
    return false;
  }

  protected void parse(EEntity_definition entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    namedType = entry;
    typeName = entry.getName(null);
    jsdai.tools.MappingDocOperations.lastRefEntityDef = entry;
  }

  protected void parse(ESimple_type entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EInteger_type) {
      typeName = "integer";
      return;
    }
    if (entry instanceof ENumber_type) {
      typeName = "number";
      return;
    }
    if (entry instanceof EReal_type) {
      typeName = "real";
      return;
    }
    if (entry instanceof EBoolean_type) {
      typeName = "boolean";
      return;
    }
    if (entry instanceof ELogical_type) {
      typeName = "logical";
      return;
    }
    if (entry instanceof EBinary_type) {
      typeName = "binary";
      return;
    }
    if (entry instanceof EString_type) {
      typeName = "string";
      return;
    }
    else {
//!!			System.out.println("PROBLEM: IdForward.parse(ESimple_type)");
    }
  }

  protected void parse(EDefined_type entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry.getDomain(null) instanceof ESelect_type) {
      selectType = entry.getDomain(null);
      namedType = entry;
    }
    else {
      namedType = entry;
    }
    if (forwards == null) {
      forwards = new Vector();
    }
    SelectForward s = new SelectForward();
    s.parse(entry, eMapping, aMapping);
    forwards.insertElementAt(s, 0);
    /*
     * if (aMapping == null) {
     * typeName = "NOT ENOUGH INFO!";
     * System.out.println("NOT ENOUGH INFO FOR PARSING REF PATH: "+entry+eMapping+aMapping);
     * return;
     * }
     */
    // it's completely wrong to determine the last element in select.
    // What we need, we need to determine exact type from this select
    // only. The rest is not our problem.
    // determine exact type from select
    //	determineExactTypeFromSelect(entry, eMapping, aMapping);
  }

  protected void parse(ESelect_constraint entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    // get aggregate of defined_types:
    ADefined_type agTypes = entry.getData_type(null);
    SdaiIterator it = agTypes.createIterator();
    while (it.next()) {
      EDefined_type type = (EDefined_type) agTypes.getCurrentMemberObject(it);
      parse(type, eMapping, aMapping);
    }
  }

  /*
   * protected void parse(EEntity_constraint entry,
   * EEntity_mapping eMapping,
   * EAttribute_mapping aMapping) throws SdaiException {
   * EEntity_definition eDef = entry.getDomain(null);
   * EEntity attr = entry.getAttribute(null);
   * parse(attr, eMapping, aMapping);
   * // now determine the type name:
   *
   * }
   */
  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EAggregation_type) {
      EEntity elementType = ((EAggregation_type) entry).getElement_type(null);
      parse(elementType, eMapping, aMapping);
      return;
    }
    if (entry instanceof ESimple_type) {
      parse((ESimple_type) entry, eMapping, aMapping);
      return;
    }
    // two cases for ENamed_type: eDef and eDefined_type
    if (entry instanceof EEntity_definition) {
      EEntity_definition eDef = (EEntity_definition) entry;
      parse(eDef, eMapping, aMapping);
      return;
    }
    if (entry instanceof EDefined_type) {
      //		System.out.println("iForward: parse EDefined_type"+entry+aMapping+eMapping);
      parse((EDefined_type) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof ESelect_constraint) {
      parse((ESelect_constraint) entry, eMapping, aMapping);
      return;
    }
    /*
     * if (entry instanceof EEntity_constraint) {
     * parse((EEntity_constraint) entry, eMapping, aMapping);
     * return;
     * }
     */
//!!		System.out.println("PROBLEM: IdForward.parse(EEntity)"+entry+eMapping+aMapping);
    typeName = "unknown!";
  }

  /*
   * protected void determineExactTypeFromSelect(EDefined_type entry,
   * EEntity_mapping eMapping,
   * EAttribute_mapping aMapping) throws SdaiException{
   * if (aMapping.testDomain(null)) {
   * EEntity domain = aMapping.getDomain(null);
   * if (!(domain instanceof EEntity_mapping)) {
   * typeName = "NOT ENOUGH INFO!";
   * System.out.println("PROBLEM: not entity mapping found! IdForward.parse()"+aMapping+domain);
   * return;
   * }
   * EEntity_mapping domMapping = (EEntity_mapping) domain;
   * EEntity target = domMapping.getTarget(null);
   * if (!(target instanceof EEntity_definition)) {
   * System.out.println("PROBLEM: not entity_definition found! IdForward.parse()"+aMapping+target);
   * typeName = "NOT ENOUGH INFO!";
   * return;
   * }
   * typeName = ((EEntity_definition) target).getName(null);
   * jsdai.tools.MappingDocOperations.lastReferencedEntityDef = (EEntity_definition) target;
   * }
   * else {
   * typeName = ((EEntity_definition) eMapping.getTarget(null)).getName(null);
   * jsdai.tools.MappingDocOperations.lastReferencedEntityDef = (EEntity_definition) target;
   * }
   * }
   */
  public String display() {
    String result = "";
    if (forwards != null) {
      int count = forwards.size();
      for (int i = 0; i < count; i++) {
        IdForwardElement s = (IdForwardElement) forwards.get(i);
        result += s.display();
      }
    }
    if (typeName != null) {
      result += typeName;
    }
    return result;
  }
}