package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class AttrBackward {
  protected String attribute = null;
  protected RPAggregate aggregate = null;
  protected Vector subtypes = null;

  protected EEntity selectType = null;
  protected EEntity namedType = null;
  public EEntity_definition usedDefinition = null;

  public EEntity getSelectType() {
    return selectType;
  }

  public EEntity getNamedType() {
    return namedType;
  }

  public void parse(Vector subtypesChain, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (subtypes == null) {
      subtypes = new Vector();
    }
    for (int j = 0; j < subtypesChain.size(); j++) {
      Subtype s = new Subtype();
      s.parse((EEntity) subtypesChain.get(j), eMapping, aMapping);
      subtypes.add(s);
    }
  }

  protected void parse(EAttribute entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EInverse_attribute) {
      EAttribute invAttr = ((EInverse_attribute) entry).getInverted_attr(null);
      parse(invAttr, eMapping, aMapping);
      return;
    }
    // take attribute name:
    attribute = entry.getParent(null).getName(null) + "." + entry.getName(null);
    usedDefinition = (EEntity_definition) entry.getParent(null);
    // remember the entity, which was used to make attribute name:
    namedType = entry.getParent(null);
    // test if given attribute is of select type/or even don't test-
    // just take the domain from current attribute and return;
    EDefined_type type = null;
    type = jsdai.tools.MappingDocOperations.getDefinedOrNull(entry, aMapping);
    if (type == null) {
      selectType = null;
      return;
    }
    if (type.getDomain(null) instanceof ESelect_type) {
      // change namedType to defined_type:
      namedType = type;
      selectType = type.getDomain(null);
    }
    else {
      selectType = null;
    }
  }

  protected void parse(EAggregate_member_constraint entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    // parse info for aggregate:
    aggregate = new RPAggregate();
    aggregate.parse(entry, eMapping, aMapping);
    // parse info for attribute:
    EEntity atr = entry.getAttribute(null);
    if (atr instanceof EExplicit_attribute) {
      parse((EExplicit_attribute) atr, eMapping, aMapping);
    }
    else if (atr instanceof EInverse_attribute) {
      EExplicit_attribute a1 = ((EInverse_attribute) atr).getInverted_attr(null);
      parse(a1, eMapping, aMapping);
    }
    else {
      //!!	System.out.println("PROBLEM: dont know how to parse:"+
      //!!		" AttrBackward.parse(EAgr_memb_constr)" + atr+ entry + eMapping + aMapping);
    }
  }

  /*
   * protected void parse(EEntity_constraint entry,
   * EEntity_mapping eMapping,
   * EAttribute_mapping aMapping) throws SdaiException {
   * EEntity attr = entry.getAttribute(null);
   * parse(attr, eMapping, aMapping);
   * // now, entity_constraint requires from us to parse subtype
   * EEntity_definition eDef = entry.getDomain(null);
   * Subtype s = new Subtype();
   * if (subtypes == null)
   * subtypes = new Vector();
   * subtypes.add(s);
   * s.parse(eDef, eMapping, aMapping);
   * }
   */
  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EAttribute) {
      parse((EAttribute) entry, eMapping, aMapping);
      return;
    }

    if (entry instanceof EAggregate_member_constraint) {
      parse((EAggregate_member_constraint) entry, eMapping, aMapping);
      return;
    }
    /*
     * if (entry instanceof EEntity_constraint) {
     * parse((EEntity_constraint) entry, eMapping, aMapping);
     * return;
     * }
     */
    /*
     * if (entry instanceof EInverse_attribute_constraint) {
     * EEntity attr = ((EInverse_attribute_constraint) entry).getInverted_attribute(null);
     * parse(attr, eMapping, aMapping);
     * return;
     * }
     */
    // there are two more cases from inverse_attribute_constraint.inv_attrib
    // but they are not yet impl.
    //!!	System.out.println("PROBLEM: dont know how to parse:"+
    //!!			" AttrBackward.parse(EEntity)" + entry + eMapping + aMapping);
  }

  public String display() {
    String result = "";
    if (attribute != null) {
      result += attribute;
    }
    if (aggregate != null) {
      result += aggregate.display();
    }
    if (subtypes != null) {
      int count = subtypes.size();
      for (int i = 0; i < count; i++) {
        Subtype s = (Subtype) subtypes.get(i);
        result += s.display();
      }
    }
    return result;
  }
}
