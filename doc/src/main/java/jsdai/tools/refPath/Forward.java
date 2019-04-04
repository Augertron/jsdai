package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class Forward implements Path {
  protected String attribute = null;
  protected RPAggregate aggregate = null;
  protected IdForward iForward = null;
  protected Vector subtypes = null;

  public void parse(EEntity entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping)
      throws SdaiException {
    if (entry instanceof EExplicit_attribute) {
      //System.out.println("Forward: explicit"+entry+eMapping+aMapping);
      parse((EExplicit_attribute) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EDerived_attribute) {
      //System.out.println("Forward: derived"+entry+eMapping+aMapping);
      parse((EDerived_attribute) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EInverse_attribute) {
      //EExplicit_attribute a1 = ((EInverse_attribute) entry).getInverted_attr(null);
      //System.out.println("Forward: inverted"+a1+eMapping+aMapping);
      //parse(a1, eMapping, aMapping);
      parse((EInverse_attribute) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EEntity_constraint) {
      parse((EEntity_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EAggregate_member_constraint) {
      parse((EAggregate_member_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof ESelect_constraint) {
      parse((ESelect_constraint) entry, eMapping, aMapping);
      return;
    }
//!!		System.out.println("PROBLEM: Forward.parse(Eentity)"+entry+eMapping+aMapping);
  }

  protected void parse(EExplicit_attribute entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    // take attribute name:
    attribute = entry.getParent(null).getName(null) + "." +
        entry.getName(null);
    // take forward reference:
    iForward = new IdForward();
    iForward.parse(entry.getDomain(null), eMapping, aMapping);

  }

  protected void parse(EInverse_attribute entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    // take attribute name:
    attribute = entry.getParent(null).getName(null) + "." +
        entry.getName(null);
    // take forward reference:
    iForward = new IdForward();
    iForward.parse(entry.getDomain(null), eMapping, aMapping);
  }

  protected void parse(EDerived_attribute entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    // take attribute name:
    attribute = entry.getParent(null).getName(null) + "." +
        entry.getName(null);
    // take forward reference:
    iForward = new IdForward();
    iForward.parse(entry.getDomain(null), eMapping, aMapping);
  }

  protected void parse(EAggregate_member_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    // parse info for aggregate:
    aggregate = new RPAggregate();
    aggregate.parse(entry, eMapping, aMapping);
    // parse info for attribute:
    EEntity atr = entry.getAttribute(null);
    parse(atr, eMapping, aMapping);
  }

  protected void parse(EEntity_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EAggregate_member_constraint) {
      parse((EAggregate_member_constraint) attr, eMapping, aMapping);
    }
    else if (attr instanceof EExplicit_attribute) {
      parse((EExplicit_attribute) attr, eMapping, aMapping);
    }
    else if (attr instanceof EInverse_attribute_constraint) {
//!!			System.out.println("PROBLEM: Forward. Entity_constraint references " +
//!!									"inverse_attribute_constraint, which is forbidden here." +
//!!									entry + eMapping + aMapping);
    }
    else {
      //!!System.out.println("PROBLEM: Forward.parse(EEntity-constr)"+attr+eMapping+aMapping);
    }
    // loose any parsing for second part of Forward, as entity_constraint is very special
    // about this.
//		iForward = null;
    EEntity_definition domain = entry.getDomain(null);
    if (jsdai.tools.MappingDocOperations.isSelectInside(entry)) {
      ESelect_type selectType = (ESelect_type) iForward.getSelectType();
      EDefined_type definedType = (EDefined_type) iForward.getNamedType();
      iForward = new IdForward();

      ANamed_type types = selectType.getSelections(null);
      // one more question: here we need to add check for cases, when given
      // domain is a far far subtype of smth, that belongs to mentioned select
      // type. Info about subtypes should be parsed at this level, not inside
      // of iForward.
      EEntity_definition topSubtype = buildSubtypesChain(types, aMapping, eMapping, domain);
      // domain is showing the goal of traveling through selects:
      if (!iForward.parse(types, topSubtype, definedType, eMapping, aMapping)) {
//!!				System.out.println("PROBLEM: Forward.parse(Ent_const)"+aMapping+entry);
      }
    }
    else {
      // this means that we may need to build only subtypes chain here:
      // iForward already contains info required for display, so build only
      // subtypes chain.
      //System.out.println("AMAPPING "+aMapping);
      EEntity_definition eDef = (EEntity_definition) iForward.getNamedType();
      ANamed_type types = new ANamed_type();
      types.addUnordered(eDef);
      buildSubtypesChain(types, aMapping, eMapping, domain);
      //System.out.println("PROBLEM: reporting cases where ent_constr not select!"+aMapping+entry);
    }
  }

  protected EEntity_definition buildSubtypesChain(
      ANamed_type types,
      EAttribute_mapping aMapping,
      EEntity_mapping eMapping,
      EEntity_definition domain)
      throws SdaiException {

    // parse given types aggregate, building from it a flat array,
    // containing only entity_definitions from all levels.
    // then check if our domain is within that array. If yes-return
    // domain. If not - try to find a subtype relationship from domain
    // to any of entity_definitions found. When found, build a subtypes
    // chain and return top most entity_definition.
    Vector flatList = new Vector();
    jsdai.tools.MappingDocOperations.buildFlatArray(types,
        aMapping,
        flatList);
    //System.out.println("flat list is "+flatList);
    //System.out.println("domain is "+domain);
    if (flatList.contains(domain)) {
      return domain;
    }
    // oh yes, now we must build a subtypes list:
    Vector subtypesChain = null;
    for (int i = 0; i < flatList.size(); i++) {
      subtypesChain = new Vector();
      EEntity_definition startType = (EEntity_definition) flatList.get(i);
      if (jsdai.tools.MappingDocOperations.buildWalkChain(subtypesChain, domain, startType)) {
        // create subtypes objects
        if (subtypes == null) {
          subtypes = new Vector();
        }
        for (int j = 0; j < subtypesChain.size(); j++) {
          Subtype s = new Subtype();
          s.parse((EEntity) subtypesChain.get(j), eMapping, aMapping);
          subtypes.add(s);
        }
        // return entity_definition for walking through selects:
        return startType;
      }
    }
//!!		System.out.println("PROBLEM: Forward.buildSubtypesChain"+aMapping);
    return domain;
  }

  protected void parse(ESelect_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping)
      throws SdaiException {
    EEntity a1 = entry.getAttribute(null);
    if (a1 instanceof EAttribute) {
      EAttribute eAtr = (EAttribute) a1;
      attribute = eAtr.getParent(null).getName(null) + "." +
          eAtr.getName(null);
    }
    else if (a1 instanceof EAggregate_member_constraint) {
      //!! this is an arguable case, because nobody can
      // 	say whether we should still parse on our own line
      //		the iForward, or should we use the stuff that was left
      // 	from older parsings..
      parse((EAggregate_member_constraint) a1, eMapping, aMapping);
    }
    else {
      //!!System.out.println("PROBLEM: Forward.parse(ESelect_const)"+a1+entry+eMapping+aMapping);
    }
    if (iForward == null) {
      iForward = new IdForward();
    }
    iForward.parse(entry, eMapping, aMapping);
  }

  public String display() {
    String result = "";
    if (attribute != null) {
      result += attribute;
    }
    if (aggregate != null) {
      result += aggregate.display();
    }
    result += "-><BR>";
    if (iForward != null) {
      result += iForward.display();
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
