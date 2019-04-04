package jsdai.tools.refPath;

import java.io.IOException;

import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SMapping_schema.EAggregate_member_constraint;
import jsdai.SMapping_schema.EAnd_constraint_relationship;
import jsdai.SMapping_schema.EAttribute_mapping;
import jsdai.SMapping_schema.EEntity_constraint;
import jsdai.SMapping_schema.EEntity_mapping;
import jsdai.SMapping_schema.EEnumeration_constraint;
import jsdai.SMapping_schema.EInteger_constraint;
import jsdai.SMapping_schema.EInverse_attribute_constraint;
import jsdai.SMapping_schema.ELogical_constraint;
import jsdai.SMapping_schema.EOr_constraint_relationship;
import jsdai.SMapping_schema.EPath_constraint;
import jsdai.SMapping_schema.EReal_constraint;
import jsdai.SMapping_schema.ESelect_constraint;
import jsdai.SMapping_schema.EString_constraint;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;

public class AttributeReferencePath {

  protected PathElements path = null;

  public PathElements getPath() {
    return path;
  }

  public void parse(EEntity entry,
      EEntity_mapping eMapping,
      EAttribute_mapping aMapping)
      throws SdaiException, IOException {
    //System.out.println("Visited entry "+entry+aMapping);
    if (jsdai.tools.MappingDocOperations.isElementInPath(entry)) {
      //System.out.println("entry will be abandoned"+entry);
      return;
    }

    if (path == null) {
      path = new PathElements();
    }
    if (entry instanceof EAttribute) {
      Forward f = new Forward();
      path.add(f);
      f.parse(entry, eMapping, aMapping);
      return;
    }

    if (entry instanceof EInverse_attribute_constraint) {
      //System.out.println("1Backward is created"+entry+aMapping);
      Backward b = new Backward();
      path.add(b);
      b.parse(entry, eMapping, aMapping);
      return;
    }

    if (entry instanceof EPath_constraint) {
      EPath_constraint pc = (EPath_constraint) entry;
      if (pc.testElement1(null)) {
        EEntity element1 = ((EPath_constraint) entry).getElement1(null);
        if (jsdai.tools.MappingDocOperations.isElementInPath(element1)) {
//!!						System.out.println("PROBLEM: AtrRefPath.parse(): element1 is in path!"+
//!!							aMapping+entry);
          return;
        }
        parse(element1, eMapping, aMapping);
      }
      else {
        System.out.println("PROBLEM: Path_constraint has not set element1!" + pc);
      }
      if (pc.testElement2(null)) {
        EEntity element2 = ((EPath_constraint) entry).getElement2(null);
        if (jsdai.tools.MappingDocOperations.isElementInPath(element2)) {
          // do not display element2
          return;
        }
        parse(element2, eMapping, aMapping); // turetu buti true paskutinis parametras.
      }
      else {
        System.out.println("PROBLEM: Path_constraint has not set element2!" + pc);
      }
      return;
    }

    if (entry instanceof EEntity_constraint) {
      if (forwardExpected((EEntity_constraint) entry)) {
        Forward f = new Forward();
        path.add(f);
        f.parse(entry, eMapping, aMapping);
      }
      else {
        //System.out.println("Backward is created"+entry+aMapping);
        Backward b = new Backward();
        path.add(b);
        b.parse(entry, eMapping, aMapping);
      }
      return;
    }
    if (entry instanceof EAggregate_member_constraint) {
      Forward f = new Forward();
      path.add(f);
      f.parse(entry, eMapping, aMapping);
      return;
    }

    if (entry instanceof EString_constraint) {
      Constraint c = new Constraint();
      path.add(c);
      c.parse(entry, eMapping, aMapping);
//				ConstrainingElement ce = new ConstrainingElement();
//				path.add(ce);
//				ce.parse(entry, eMapping, aMapping);

      return;
    }
    if (entry instanceof EOr_constraint_relationship) {
      OrElement o = new OrElement();
      path.add(o);
      o.parse(entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EAnd_constraint_relationship) {
      // as suggested in alfonsas notation, replace this with
      // two constraint elements on path:
      Constraint c1 = new Constraint();
      Constraint c2 = new Constraint();
      EEntity element1 = ((EAnd_constraint_relationship) entry).getElement1(null);
      EEntity element2 = ((EAnd_constraint_relationship) entry).getElement2(null);
      if (jsdai.tools.MappingDocOperations.isElementInPath(element1)) {
        //System.out.println("WARNING: AtrRefPath.parse(): and constr element1 is in" +
        //	" path already. Skipping element1."+element1+aMapping);
      }
      else {
        c1.parse(element1, eMapping, aMapping);
        path.add(c1);
      }
      if (jsdai.tools.MappingDocOperations.isElementInPath(element2)) {
        //System.out.println("WARNING: AtrRefPath.parse(): and constr element2 is in" +
        //	" path already. Skipping element2."+element2+aMapping);
      }
      else {
        c2.parse(element2, eMapping, aMapping);
        path.add(c2);
      }
      return;
    }

    if (entry instanceof ESelect_constraint) {
//				Constraint c = new Constraint();
//				path.add(c);
//				c.parse(entry, eMapping, aMapping);
//				
      Forward f = new Forward();
      path.add(f);
      f.parse(entry, eMapping, aMapping);

      return;
    }
    if (entry instanceof EInteger_constraint) {
      Constraint c = new Constraint();
      path.add(c);
      c.parse(entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof EReal_constraint) {
      Constraint c = new Constraint();
      path.add(c);
      c.parse(entry, eMapping, aMapping);
      return;
    }
    if (entry instanceof ELogical_constraint) {
      Constraint c = new Constraint();
      path.add(c);
      c.parse(entry, eMapping, aMapping);
      return;
    }
    // I doubt whether this is good solution.
    // Please investigate boolean_csg_result.csg_operator from ap210
    if (entry instanceof EEnumeration_constraint) {
      Constraint c = new Constraint();
      path.add(c);
      c.parse(entry, eMapping, aMapping);
      return;
    }

    System.out.println("Unknown entry passed!" + entry + eMapping);
  }

  public String display() {
    if (path == null) {
      return ".";
    }
    else {
      String result = "";
      result += path.display();
      result += ".";
      return result;
    }
  }

  public boolean forwardExpected(EEntity_constraint entry)
      throws SdaiException {
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EInverse_attribute_constraint) {
      return false; // backward expected.
    }
    if (attr instanceof EAggregate_member_constraint) {
      return forwardExpected((EAggregate_member_constraint) attr);
    }
    return true;
  }

  public boolean forwardExpected(EAggregate_member_constraint entry)
      throws SdaiException {
    EEntity attr = entry.getAttribute(null);
    if (attr instanceof EInverse_attribute_constraint) {
      return false;
    }
    if (attr instanceof EAggregate_member_constraint) {
      return forwardExpected((EAggregate_member_constraint) attr);
    }
    if (attr instanceof ESelect_constraint) {
      return forwardExpected((ESelect_constraint) attr);
    }
    return true;
  }

  public boolean forwardExpected(ESelect_constraint entry)
      throws SdaiException {
    if (entry instanceof EAggregate_member_constraint) {
      return forwardExpected((EAggregate_member_constraint) entry);
    }
    return true;
  }
}