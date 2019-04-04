/*
 * LKSoft Baltic UAB
 * @author Viktoras Kovaliovas
 * Created on Mar 3, 2004 3:02:01 PM
 */
package jsdai.tools.validator;

import java.util.*;

import org.w3c.dom.*;

import jsdai.dictionary.*;
import jsdai.lang.*;

public class EntityListParser {

  private EntityListParser() {
  }

  public static boolean belongs(Object entities, EEntity_definition eEntDef)
      throws SdaiException {

    Set params[] = (Set[]) entities;
    if (params[0].contains(eEntDef.getName(null))) {
      return true;
    }

    for (Iterator i = params[1].iterator(); i.hasNext(); ) {
      EEntity_definition eDef = (EEntity_definition) i.next();
      if (eEntDef.isSubtypeOf(eDef)) {
        return true;
      }
    }

    return false;
  }

  public static Object parseEntityList(ESchema_definition schema, Element props)
      throws SdaiException {

    String schemaName = schema.getName(null);

    Set ignoreSet = new HashSet();
    Set ignoreTreeSet = new HashSet();
    Set ignoreParams[] = new Set[] { ignoreSet, ignoreTreeSet };

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
          ignoreTreeSet.add(schema.getEntityDefinition(value));
        }
      }

      break;
    }

    return ignoreParams;
  }
}