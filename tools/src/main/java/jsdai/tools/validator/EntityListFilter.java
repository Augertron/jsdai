/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Aug 31, 2004 9:51:57 AM
 */
package jsdai.tools.validator;

import java.util.*;

import org.w3c.dom.*;

import jsdai.dictionary.*;
import jsdai.lang.*;

public class EntityListFilter
    implements EntityFilter {

  private TestEnvironment env;
  private Set ignoreSet;
  private Set ignoreTreeSet;

  public EntityListFilter() {
  }

  public boolean init(TestEnvironment env, Element props) {
    this.env = env;

    try {
      ESchema_definition schema = env.sourceSchema.getNativeSchema();
      String schemaName = schema.getName(null);

      ignoreSet = new HashSet();
      ignoreTreeSet = new HashSet();

      NodeList schemas = props.getChildNodes();
      for (int i = 0, n = schemas.getLength(); i < n; i++) {
        if (!(schemas.item(i) instanceof Element)) {
          continue;
        }

        Element elem = (Element) schemas.item(i);
        if (!elem.getTagName().equals("EntityList")) {
          continue;
        }

        if (!schemaName.equals(elem.getAttribute("schemaName"))) {
          continue;
        }

        NodeList ignores = elem.getChildNodes();
        for (int j = 0, nn = ignores.getLength(); j < nn; j++) {
          if (!(ignores.item(j) instanceof Element)) {
            continue;
          }

          Element ignore = (Element) ignores.item(j);
          String value = ignore.getChildNodes().item(0).getNodeValue();
          if (ignore.getTagName().equals("entity")) {
            ignoreSet.add(value);
          }
          else if (ignore.getTagName().equals("entity_tree")) {
            try {
              ignoreTreeSet.add(schema.getEntityDefinition(value));
            }
            catch (SdaiException e) {
              System.err.println("Unable to resolve " + value);
            }
          }
        }

        break;
      }
    }
    catch (SdaiException e) {
      env.logger.throwing("EntityListFilter", "init", e);
      return false;
    }

    return true;
  }

  public boolean ignore(EEntity eEnt) {
    try {
      EEntity_definition eEntDef = eEnt.getInstanceType();
      if (ignoreSet.contains(eEntDef.getName(null))) {
        return true;
      }

      for (Iterator i = ignoreTreeSet.iterator(); i.hasNext(); ) {
        EEntity_definition eDef = (EEntity_definition) i.next();
        if (eEntDef.isSubtypeOf(eDef)) {
          return true;
        }
      }
    }
    catch (SdaiException e) {
      env.logger.throwing("EntityListFilter", "ignore", e);
    }

    return false;
  }
}