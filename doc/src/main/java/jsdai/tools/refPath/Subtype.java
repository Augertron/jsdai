package jsdai.tools.refPath;

import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class Subtype {
  protected String entityName = null;

  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException {
    if (entry instanceof EEntity_definition) {
      entityName = ((EEntity_definition) entry).getName(null);
      return;
    }
//!!		System.out.println("PROBLEM: subtype is not eDef"+entry+eMapping+aMapping);
  }

  public String display() {
    if (entityName == null) {
      return " => <BR>";
    }
    else {
      return " => <BR>" + entityName;
    }
  }
}