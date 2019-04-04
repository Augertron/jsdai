package jsdai.tools.refPath;

import java.io.IOException;

import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

public class EntityReferencePath {

  protected Constraint c = null;

  public Constraint getConstraint() {
    return c;
  }

  public void parse(EEntity entry,
      EEntity_mapping eMapping)
      throws SdaiException, IOException {
    //System.out.println("Visited entry "+entry);
    if (jsdai.tools.MappingDocOperations.isElementInPath(entry)) {
      //System.out.println("entry will be abandoned"+entry);
      return;
    }

    if (c == null) {
      c = new Constraint();
    }

    c.parse(entry, eMapping, null);
  }

  public String display() {
    if (c == null) {
      return ".";
    }
    else {
      String result = "";
      result += c.display();
      result += ".";
      return result;
    }
  }

}