package jsdai.tools.refPath;

import java.io.IOException;
import java.util.Vector;

import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EOr_constraint_relationship;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class OrElement implements Path {
  protected Vector orElements = null;

  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException, IOException {
    if (entry instanceof EOr_constraint_relationship) {
      if (orElements == null) {
        orElements = new Vector();
      }
      EOr_constraint_relationship entity = (EOr_constraint_relationship) entry;
      EEntity element1 = entity.getElement1(null);
      AttributeReferencePath path1 = new AttributeReferencePath();
      path1.parse(element1, eMapping, aMapping);
      orElements.add(path1.getPath());

      EEntity element2 = entity.getElement2(null);
      AttributeReferencePath path2 = new AttributeReferencePath();
      path2.parse(element2, eMapping, aMapping);
      orElements.add(path2.getPath());

      return;
    }
//!!		System.out.println("PROBLEM: OrElement.parse(EEntity)"+entry+eMapping+aMapping);
  }

  public String display() {
    if (orElements == null) {
      return "( )";
    }
    else {
      String result = "";
      int count = orElements.size();
      for (int i = 0; i < count; i++) {
        PathElements path = (PathElements) orElements.get(i);
        if (path != null) {
          result = result + "(" + path.display() + ")";
        }
        else {
          result = result + "(" + ")";
        }
      }
      if (result.length() == 0) {
        return "( )";
      }
      else {
        return result;
      }
    }
  }
}