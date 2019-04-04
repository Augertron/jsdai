package jsdai.tools.refPath;

import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.lang.SdaiException;

class Value {
  protected Object internalValue = null;

  public void parse(EString_constraint entry, EEntity_mapping eMapping) throws SdaiException {
    internalValue = entry.getConstraint_value(null);
  }

  public void parse(EInteger_constraint entry, EEntity_mapping eMapping) throws SdaiException {
    internalValue = new Integer(entry.getConstraint_value(null));
  }

  public void parse(EReal_constraint entry, EEntity_mapping eMapping) throws SdaiException {
    internalValue = new Double(entry.getConstraint_value(null));
  }

  public void parse(ELogical_constraint entry, EEntity_mapping eMapping) throws SdaiException {
    int value = entry.getConstraint_value(null);
    if (value == 1) {
      internalValue = ".FALSE.";
    }
    else if (value == 2) {
      internalValue = ".TRUE.";
    }
    else if (value == 3) {
      internalValue = ".UNKNOWN.";
    }
    else {
      System.out.println("Logical constraint is invalid:" + entry);
    }
  }

  public void parse(EEnumeration_constraint entry, EEntity_mapping eMapping) throws SdaiException {
    internalValue = entry.getConstraint_value(null);
  }

  public String display() {
    if (internalValue instanceof String) {
      return "'" + internalValue.toString() + "'";
    }
    else {
      return internalValue.toString();
    }
  }
}