package jsdai.tools.refPath;

import java.io.IOException;

import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

class Constraint implements PathElement {

  protected LastConstraintElement lastElement = null;
  protected PathElements path = null;

  public void parse(EEntity entry, EEntity_mapping eMapping, EAttribute_mapping aMapping) throws SdaiException, IOException {
    if (entry instanceof ELogical_constraint) {
      ConstrainingElement e = new ConstrainingElement();
      e.parse(entry, eMapping, aMapping);
      lastElement = e;
      return;
    }
    if (entry instanceof EInteger_constraint) {
      ConstrainingElement e = new ConstrainingElement();
      e.parse(entry, eMapping, aMapping);
      lastElement = e;
      return;
    }
    if (entry instanceof EReal_constraint) {
      ConstrainingElement e = new ConstrainingElement();
      e.parse(entry, eMapping, aMapping);
      lastElement = e;
      return;
    }
    if (entry instanceof ESelect_constraint) {
//!!			System.out.println("Loose control for select"+entry+aMapping+eMapping);
      return;
    }
    if (entry instanceof EString_constraint) {
      // create constraining element and pass control there:
      ConstrainingElement e = new ConstrainingElement();
      e.parse(entry, eMapping, aMapping);
      lastElement = e;
      return;
    }
    if (entry instanceof EEnumeration_constraint) {
      ConstrainingElement e = new ConstrainingElement();
      e.parse(entry, eMapping, aMapping);
      lastElement = e;
      return;
    }
    AttributeReferencePath arPath = new AttributeReferencePath();
    arPath.parse(entry, eMapping, aMapping);
    PathElements result = arPath.getPath();
    if (result != null) {
      lastElement = result.getLast();
      result.removeLast();
      if (!result.isEmpty()) {
        path = result;
      }
    }
  }

  public String display() {
    String result = "{";
    if (path != null) {
      result += path.display();
    }
    if (lastElement != null) {
      result += lastElement.display();
    }
    result += "}";
    return result;
  }

}
