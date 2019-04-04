package jsdai.tools.refPath;

import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

public class ConstrainingElement implements PathElement {
  protected String attribute = null;
  protected RPAggregate aggregate = null;
  protected Value value = null;

  //if these both values are null, then "=" should be output.
  protected IdForwardElement element = null;
  protected Vector elements = null; // SelectOrNestedAggregate

  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping)
      throws SdaiException {
    if (entry instanceof EString_constraint) {
      parse((EString_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EInteger_constraint) {
      parse((EInteger_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EReal_constraint) {
      parse((EReal_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof ELogical_constraint) {
      parse((ELogical_constraint) entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EEnumeration_constraint) {
      parse((EEnumeration_constraint) entry, eMapping, aMapping);
      return;
    }

//!!		System.out.println("PROBLEM: ConstrainingElement.parse(EEntity)"+
//!!				entry + eMapping + aMapping);
  }

  protected void parse(EEnumeration_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    value = new Value();
    value.parse(entry, eMapping);
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
/*		// do not know whether this is used anywhere in 210, 212, 214, so commented out.
		if (attr instanceof EDerived_attribute) {
			EDerived_attribute eAttr = (EDerived_attribute) attr;
			attribute = eAttr.getParent(null).getName(null) +
							"."+eAttr.getName(null);
			return;
		}
*/
    System.out.println("PROBLEM: ConstrainingElement.parse(EEnumeration)" +
        entry + eMapping + aMapping);

  }

  protected void parse(ELogical_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    value = new Value();
    value.parse(entry, eMapping);
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
/*		// do not know whether this is used anywhere in 210, 212, 214, so commented out.
		if (attr instanceof EDerived_attribute) {
			EDerived_attribute eAttr = (EDerived_attribute) attr;
			attribute = eAttr.getParent(null).getName(null) +
							"."+eAttr.getName(null);
			return;
		}
*/
    System.out.println("PROBLEM: ConstrainingElement.parse(ELogical)" +
        entry + eMapping + aMapping);

  }

  protected void parse(EReal_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    value = new Value();
    value.parse(entry, eMapping);
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
    if (attr instanceof EDerived_attribute) {
      EDerived_attribute eAttr = (EDerived_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
//!!		System.out.println("PROBLEM: ConstrainingElement.parse(EReal)"+
//!!				entry+eMapping+aMapping);		
  }

  protected void parse(EInteger_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    value = new Value();
    value.parse(entry, eMapping);
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
//!!		System.out.println("PROBLEM: ConstrainingElement.parse(EInteger)"+
//!!				entry+eMapping+aMapping);		
  }

  protected void parse(EString_constraint entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping) throws SdaiException {
    value = new Value();
    value.parse(entry, eMapping);
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EExplicit_attribute) {
      EExplicit_attribute eAttr = (EExplicit_attribute) attr;
      attribute = eAttr.getParent(null).getName(null) +
          "." + eAttr.getName(null);
      return;
    }
//!!		System.out.println("PROBLEM: ConstrainingElement.parse(EString)"+
//!!				entry+eMapping+aMapping);
  }

  public String display() {
    String result = "";
    if (attribute != null) {
      result += attribute;
    }
    if (aggregate != null) {
      result += aggregate;
    }
    if ((element == null) && (elements == null)) {
      result += "=";
    }
    else {
      result += "-><BR>";
      if (element != null) {
        result += element.display();
      }
      if (elements != null) {
        int count = elements.size();
        for (int i = 0; i < count; i++) {
          IdForwardElement s = (IdForwardElement) elements.get(i);
          result += s.display();
        }
      }
    }
    if (value != null) {
      result += value.display();
    }
    return result;
  }

}
