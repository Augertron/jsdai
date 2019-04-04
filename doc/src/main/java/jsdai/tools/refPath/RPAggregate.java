package jsdai.tools.refPath;

import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.SdaiException;

// RP- reference path. Name RPAggregate was adopted to avoid
// confusion with name Aggregate, defined in jsdai.lang.
class RPAggregate {
  protected int digits = -1;

  public void parse(EAggregate_member_constraint entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (!entry.testMember(null)) {
      digits = -1;
    }
    else {
      digits = entry.getMember(null);
    }
  }

  //  negative value is treated as value, that indicates
  // 'any'.
  public String display() {
    if (digits < 0) {
      return "[i]";
    }
    else {
      return "[" + String.valueOf(digits) + "]";
    }
  }
}