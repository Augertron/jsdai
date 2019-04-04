package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAggregation_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;

class IdBackward {
  protected String entityName = null;
  protected Vector backwards = null; //of IdBackwardElement's.

  public boolean parse(ANamed_type types, EEntity_definition domain, EDefined_type selectType, EEntity_mapping eMapping, EAttribute_mapping aMapping)
      throws SdaiException {
    parse(domain, eMapping, aMapping);

    SdaiIterator it = types.createIterator();
    while (it.next()) {
      EEntity type = (EEntity) types.getCurrentMemberObject(it);
      if (type == domain) {
        parse(selectType, eMapping, aMapping);
        return true;
      }
      EEntity insideDomain = jsdai.tools.MappingDocOperations.getSelectOrDefinition(type, aMapping);
      if (insideDomain == null) {
        return false;
      }
      if (insideDomain instanceof EEntity_definition) {
        continue;
      }
      if (type instanceof EDefined_type) {
        ESelect_type insideType = (ESelect_type) insideDomain;
        ANamed_type insideTypes = insideType.getSelections(null);
        // do recursion
        if (parse(insideTypes, domain, (EDefined_type) type, eMapping, aMapping)) {
          parse(selectType, eMapping, aMapping);
          return true;
        }
      }
    }
    return false;
  }

  protected void parse(EDefined_type entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    entityName = ((EEntity_definition) eMapping.getTarget(null)).getName(null);
    if (backwards == null) {
      backwards = new Vector();
    }
    SelectBackward s = new SelectBackward();
    s.parse(entry, eMapping, aMapping);
    backwards.add(s);
  }

  protected void parse(ESimple_type entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EInteger_type) {
      entityName = "integer";
      return;
    }
    if (entry instanceof ENumber_type) {
      entityName = "number";
      return;
    }
    if (entry instanceof EReal_type) {
      entityName = "real";
      return;
    }
    if (entry instanceof EBoolean_type) {
      entityName = "boolean";
      return;
    }
    if (entry instanceof ELogical_type) {
      entityName = "logical";
      return;
    }
    if (entry instanceof EBinary_type) {
      entityName = "binary";
      return;
    }
    if (entry instanceof EString_type) {
      entityName = "string";
      return;
    }
    else {
//!!			System.out.println("PROBLEM: IdBackward.parse(ESimple_type)");
    }
  }

  protected void parse(EAttribute entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    EEntity domain = null;
    if (entry instanceof EExplicit_attribute) {
      domain = ((EExplicit_attribute) entry).getDomain(null);
    }
    if (entry instanceof EDerived_attribute) {
      domain = ((EDerived_attribute) entry).getDomain(null);
    }
    if (entry instanceof EInverse_attribute) {
//!!			System.out.println("Inverted Attribute detected!"+entry+aMapping);
      EExplicit_attribute a1 = ((EInverse_attribute) entry).getInverted_attr(null);
      domain = a1.getDomain(null);
    }
    if (domain instanceof EAggregation_type) {
      EEntity elementType = ((EAggregation_type) domain).getElement_type(null);
      parse(elementType, eMapping, aMapping);
      return;
    }
    if (domain instanceof EEntity_definition) {
      entityName = ((EEntity_definition) domain).getName(null);
      return;
    }
    if (domain instanceof EDefined_type) {
      parse((EDefined_type) domain, eMapping, aMapping);
      return;
    }

//!!		System.out.println("PROBLEM: IdBackward.parse(EExpl_atr): dont know how to parse."+entry+eMapping+domain);
  }

  protected void parse(EAggregate_member_constraint entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EAttribute) {
      parse((EAttribute) attr, eMapping, aMapping);
      return;
    }
//!!		System.out.println("PROBLEM: dont know how to parse: IdBackward.parse(EAgr_memb_const)"+attr);
  }

  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EAttribute) {
      parse((EAttribute) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EAggregate_member_constraint) {
      parse((EAggregate_member_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EInverse_attribute_constraint) {
      EEntity attr = ((EInverse_attribute_constraint) entry).getInverted_attribute(null);
      parse(attr, eMapping, aMapping);
      return;
    }
    if (entry instanceof EDefined_type) {
      parse((EDefined_type) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof ESimple_type) {
      parse((ESimple_type) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EEntity_definition) {
      entityName = ((EEntity_definition) entry).getName(null);
      return;
    }
    if (entry instanceof EAggregation_type) {
      // get the base type
      EEntity baseType = ((EAggregation_type) entry).getElement_type(null);
      parse(baseType, eMapping, aMapping);
      return;
    }
    // there are two more cases from inverse_attribute_constraint.inv_attrib
    // but they are not yet impl.
//!!		System.out.println("PROBLEM: dont know how to parse:"+
//!!				" IdBackward.parse(EEntity)" + entry + eMapping + aMapping);
  }

  public String display() {
    String result = "";
    if (entityName != null) {
      result += entityName;
    }
    if (backwards != null) {
      int count = backwards.size();
      for (int i = 0; i < count; i++) {
        IdBackwardElement s = (IdBackwardElement) backwards.get(i);
        result += s.display();
      }
    }
    return result;
  }
}
