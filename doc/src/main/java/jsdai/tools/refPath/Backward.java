package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class Backward implements Path {

  protected IdBackward id = null;
  protected AttrBackward attr = null;

  public void parse(EEntity entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EInverse_attribute_constraint) {
      parse((EInverse_attribute_constraint) entry, eMapping, aMapping);
      // now, pass info to next path elements:
      if (attr != null) {
        jsdai.tools.MappingDocOperations.lastRefEntityDef = attr.usedDefinition;
      }
      return;
    }
    if (entry instanceof EEntity_constraint) {
      parse((EEntity_constraint) entry, eMapping, aMapping);
      // now, pass info to next path elements:
      if (attr != null) {
        jsdai.tools.MappingDocOperations.lastRefEntityDef = attr.usedDefinition;
      }
      return;
    }
//!!		System.out.println("PROBLEM: Backward.parse(EEntity)"+entry+eMapping+aMapping);
  }

  protected void parse(EInverse_attribute_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
//		System.out.println("inverse received for backward parsing!"+entry+aMapping);																
    EEntity invAttr = entry.getInverted_attribute(null);
    if (invAttr instanceof EAggregate_member_constraint) {
      EAggregate_member_constraint c = (EAggregate_member_constraint) invAttr;
      attr = new AttrBackward();
      attr.parse(c, eMapping, aMapping);
      id = new IdBackward();
      id.parse(c, eMapping, aMapping);
      if (jsdai.tools.MappingDocOperations.isSelectInside(entry)) {
        if (!(eMapping.getTarget(null) instanceof EEntity_definition)) {
//!!					System.out.println("PROBLEM: ent. mapping target is not ent_definition!" +
//!!						aMapping + eMapping + entry);
          return;
        }
        //EEntity_definition domain = (EEntity_definition) eMapping.getTarget(null);
        EEntity_definition domain =
            jsdai.tools.MappingDocOperations.getAssumedDomain(aMapping, eMapping);
        // we should build a chain through select:
        ESelect_type selectType = (ESelect_type) attr.getSelectType();
        EDefined_type definedType = (EDefined_type) attr.getNamedType();
        id = new IdBackward();
        ANamed_type types = selectType.getSelections(null);
//				System.out.println("Start2:"+entry);
        EEntity_definition topSubtype = buildSubtypesChain(types, aMapping, eMapping, domain, false);
//				System.out.println("domain was "+domain);
//				System.out.println("topSubtype was "+topSubtype);
        if (!id.parse(types, topSubtype, definedType, eMapping, aMapping)) {
//!!					System.out.println("PROBLEM: a)Backward.parse(inverse attr. constraint):"+
//!!					" attribute of given inverse attr. constraint can not be followed to"+
//!!					" requested entity_definition (specified via entity_mapping.target)."+
//!!					aMapping+entry);
        }
      }
      else {
        // don't do anything - default parsing is OK, as inverse_attribute_constraint
        // do not contain info about required type.
      }
      return;
    }
    if (invAttr instanceof EAttribute) {
      attr = new AttrBackward();
      attr.parse(invAttr, eMapping, aMapping);
      id = new IdBackward();
      id.parse(invAttr, eMapping, aMapping);
      if (jsdai.tools.MappingDocOperations.isSelectInside(entry)) {
        if (!(eMapping.getTarget(null) instanceof EEntity_definition)) {
//!!					System.out.println("PROBLEM: ent. mapping target is not ent_definition!" +
//!!						aMapping + eMapping + entry);
          return;
        }

        EEntity_definition domain =
            jsdai.tools.MappingDocOperations.getAssumedDomain(aMapping, eMapping);
//!!				EEntity_definition domain = (EEntity_definition)((EAttribute) invAttr).getParent(null);
        // we should build a chain through select:
        ESelect_type selectType = (ESelect_type) attr.getSelectType();
        EDefined_type definedType = (EDefined_type) attr.getNamedType();
        id = new IdBackward();
        ANamed_type types = selectType.getSelections(null);
//				System.out.println("Start3:"+entry);
        EEntity_definition topSubtype = buildSubtypesChain(types, aMapping, eMapping, domain, false);
        //		System.out.println("domain was "+domain);
        //		System.out.println("topSubtype was "+topSubtype);
        if (!id.parse(types, topSubtype, definedType, eMapping, aMapping)) {
//!!					System.out.println("PROBLEM: b) Backward.parse(inverse attr. constraint):"+
//!!					" attribute of given inverse attr. constraint can not be followed to"+
//!!					" requested entity_definition (specified via entity_mapping.target)."+
//!!					aMapping+entry);
        }
      }
      else {
        // don't do anything - default parsing is OK, as inverse_attribute_constraint
        // do not contain info about required type.
      }
      return;
    }
    if (invAttr instanceof EEntity_constraint) {
//!!			System.out.println("PROBLEM: Backward for EEntity_constraint"+invAttr+eMapping+aMapping);
      return;
    }
    if (invAttr instanceof ESelect_constraint) {
//!!			System.out.println("PROBLEM: Backward for ESelect_constraint"+invAttr+eMapping+aMapping);
      return;
    }
//!!		System.out.println("PROBLEM: Backward.parse"+entry+eMapping+aMapping);
  }

  protected void parse(EEntity_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
//		System.out.println("entity constraint received for backward parsing!"+entry+aMapping);								
    EEntity cAttr = entry.getAttribute(null);
    if (cAttr instanceof EAggregate_member_constraint) {
//!!			System.out.println("PROBLEM: Backward. Entity_constraint references " +
//!!									"aggregate_constraint, dont' know how to respond." +
//!!									entry + eMapping + aMapping);
      //parse((EAggregate_member_constraint) attr, eMapping, aMapping);
    }
    else if (cAttr instanceof EExplicit_attribute) {
      //parse((EExplicit_attribute) attr, eMapping, aMapping);
//!!			System.out.println("PROBLEM: Backward. Entity_constraint references " +
//!!									"explicit_attribute, which is not expected here?" +
//!!									entry + eMapping + aMapping);
    }
    else if (cAttr instanceof EInverse_attribute_constraint) {
      parse((EInverse_attribute_constraint) cAttr, eMapping, aMapping);
    }
    else {
//!!			System.out.println("PROBLEM: Backward.parse(EEntity-constr)"+cAttr+eMapping+aMapping);
    }
//		System.out.println("entity constraint proceeds with parsing!");											
    // ok. Now check for cases, when (possibly) we need to display subtypes information:
    // query idBackward object for who/what entity was used to create name for entityName:
    EEntity_definition domain = entry.getDomain(null);
    if (jsdai.tools.MappingDocOperations.isSelectInside(entry)) {
      ESelect_type selectType = (ESelect_type) attr.getSelectType();
      EDefined_type definedType = (EDefined_type) attr.getNamedType();
      id = new IdBackward();
      ANamed_type types = selectType.getSelections(null);
//			System.out.println("Start4: "+entry);
      EEntity_definition topSubtype = buildSubtypesChain(types, aMapping, eMapping, domain, true);
      if (!id.parse(types, topSubtype, definedType, eMapping, aMapping)) {
//!!				System.out.println("PROBLEM: Backward.parse(entity_constraint):"+
//!!					" attribute of given entity_constraint can not be followed to"+
//!!					" requested entity_definition (specified via entity_constraint.domain)."+
//!!					aMapping+entry);
      }
    }
    else {
      EEntity_definition eDef = (EEntity_definition) attr.getNamedType();
      ANamed_type types = new ANamed_type();
      types.addUnordered(eDef);
//			System.out.println("Start5"+entry);
      buildSubtypesChain(types, aMapping, eMapping, domain, true);
    }
  }

  protected EEntity_definition buildSubtypesChain(
      ANamed_type types,
      EAttribute_mapping aMapping,
      EEntity_mapping eMapping,
      EEntity_definition domain,
      boolean listSubtypes)
      throws SdaiException {
    // parse given types aggregate, building from it a flat array,
    // containing only entity_definitions from all levels.
    // then check if our domain is within that array. If yes-return
    // domain. If not - try to find a subtype relationship from domain
    // to any of entity_definitions found. When found, build a subtypes
    // chain and return top most entity_definition.
    Vector flatList = new Vector();
    jsdai.tools.MappingDocOperations.buildFlatArray(types, aMapping, flatList);
//		System.out.println("flat list is "+ flatList);
//		System.out.println("domain is "+ domain);
    if (flatList.contains(domain)) {
      return domain;
    }
    // oh yes, now we must build a subtypes list:
    Vector subtypesChain = null;
    for (int i = 0; i < flatList.size(); i++) {
      subtypesChain = new Vector();
      EEntity_definition startType = (EEntity_definition) flatList.get(i);
      if (jsdai.tools.MappingDocOperations.buildWalkChain(subtypesChain, domain, startType)) {
        if (listSubtypes) {
          // this check is important, because it allows us to avoid generating subtypes,
          // when we needed to generate only selects (from inverse_attribute_constraint).
          if (attr == null) {
            attr = new AttrBackward();
          }
          attr.parse(subtypesChain, eMapping, aMapping);
        }
        // return entity_definition for walking through selects:
        return startType;
      }
    }
//!!		System.out.println("PROBLEM: Backward.buildSubtypesChain"+aMapping+types+domain);
    return domain;
  }

  public String display() {
    String result = "";
    if (id == null) {
      result += "";
    }
    if (attr == null) {
      result += " <- <BR>";
    }
    if ((id == null) || (attr == null)) {
      return result;
    }
    return id.display() + " <- <BR>" + attr.display();
  }

}
