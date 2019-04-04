/*
 * EntityInfo.java
 *
 * Created on Tre�iadienis, 2001, Baland�io 4, 17.24
 */

package jsdai.mappingUtils;

import java.util.LinkedList;

import jsdai.SExtended_dictionary_schema.EEntity_definition;

/**
 * @author Vaidas Narg�las
 */
public class EntityInfo {
  public EEntity_definition entity;

  public LinkedList mappings;

  /**
   * Creates new EntityInfo
   */
  public EntityInfo(EEntity_definition entity) {
    this.entity = entity;
    mappings = new LinkedList();
  }

}
