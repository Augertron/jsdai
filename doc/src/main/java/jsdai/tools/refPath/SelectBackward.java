package jsdai.tools.refPath;

import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.SdaiException;

class SelectBackward implements IdBackwardElement {
  protected String selectTypeName = null;

  public void parse(EDefined_type entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    selectTypeName = entry.getName(null);
  }

  public String display() {
    if (selectTypeName == null) {
      return " = ";
    }
    else {
      return " = " + selectTypeName;
    }
  }
}