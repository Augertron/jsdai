/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

// Special class for schema definition

package jsdai.SMapping_schema;

import jsdai.lang.*;

import java.lang.reflect.*;

public class SMapping_schema extends SSuper {

  public static final String time_stamp = "2013-04-15 T10:50:36";
  public static final SSuper ss;

  static {
    ss = SSuper.initSuper(new SMapping_schema());
    initDefinedDataTypes();
    initNonDefinedDataTypes();
  }

  protected CEntity makeInstanceX(Class c) throws java.lang.InstantiationException, java.lang.IllegalAccessException {
    return (CEntity) c.newInstance();
  }

  protected void setDataField(Class cl, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
    Field fd = cl.getDeclaredField(name);
    fd.set(null, value);
  }

  protected Object getObject(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    return field.get(obj);
  }

  protected int getInt(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    return field.getInt(obj);
  }

  protected double getDouble(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    return field.getDouble(obj);
  }

  protected void setObject(Object obj, Field field, Object value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    field.set(obj, value);
  }

  protected void setInt(Object obj, Field field, int value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    field.setInt(obj, value);
  }

  protected void setDouble(Object obj, Field field, double value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
    field.setDouble(obj, value);
  }

  public static jsdai.dictionary.EDefined_type _st_attribute_mapping_path_select;
  public static jsdai.dictionary.EDefined_type _st_entity_or_attribute;
  public static jsdai.dictionary.EDefined_type _st_attribute_mapping_domain_select;
  public static jsdai.dictionary.EDefined_type _st_aggregate_member_constraint_select;
  public static jsdai.dictionary.EDefined_type _st_attribute_select;
  public static jsdai.dictionary.EDefined_type _st_attribute_value_constraint_select;
  public static jsdai.dictionary.EDefined_type _st_constraint_select;
  public static jsdai.dictionary.EDefined_type _st_inverse_attribute_constraint_select;
  public static jsdai.dictionary.EDefined_type _st_path_constraint_select;
  public static jsdai.dictionary.EDefined_type _st_select_constraint_select;

  static void initDefinedDataTypes() {
    _st_attribute_mapping_path_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("attribute_mapping_path_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_entity_or_attribute = (jsdai.dictionary.EDefined_type) SdaiSession.findDataType("entity_or_attribute", jsdai.SMapping_schema.SMapping_schema.class);
    _st_attribute_mapping_domain_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("attribute_mapping_domain_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_aggregate_member_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("aggregate_member_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_attribute_select = (jsdai.dictionary.EDefined_type) SdaiSession.findDataType("attribute_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_attribute_value_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("attribute_value_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession.findDataType("constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_inverse_attribute_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("inverse_attribute_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_path_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("path_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_select_constraint_select = (jsdai.dictionary.EDefined_type) SdaiSession
        .findDataType("select_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
  }

  public static jsdai.dictionary.EData_type _st_set_1_uof_mapping;
  public static jsdai.dictionary.EData_type _st_set_1_schema_mapping;
  public static jsdai.dictionary.EData_type _st_set_1_entity_mapping;
  public static jsdai.dictionary.EData_type _st_list_1_defined_type;
  public static jsdai.dictionary.EData_type _st_list_1_attribute_mapping_path_select;
  public static jsdai.dictionary.EData_type _st_list_1_named_type;
  public static jsdai.dictionary.EData_type _st_set_2_constraint_select;

  static void initNonDefinedDataTypes() {
    _st_set_2_constraint_select = SdaiSession.findDataType("_SET_2_constraint_select", jsdai.SMapping_schema.SMapping_schema.class);
    _st_set_1_entity_mapping = SdaiSession.findDataType("_SET_1_entity_mapping", jsdai.SMapping_schema.SMapping_schema.class);
    _st_set_1_schema_mapping = SdaiSession.findDataType("_SET_1_schema_mapping", jsdai.SMapping_schema.SMapping_schema.class);
    _st_list_1_named_type = SdaiSession.findDataType("_LIST_1_named_type", jsdai.SMapping_schema.SMapping_schema.class);
    _st_list_1_defined_type = SdaiSession.findDataType("_LIST_1_defined_type", jsdai.SMapping_schema.SMapping_schema.class);
    _st_set_1_uof_mapping = SdaiSession.findDataType("_SET_1_uof_mapping", jsdai.SMapping_schema.SMapping_schema.class);
    _st_list_1_attribute_mapping_path_select = SdaiSession.findDataType("_LIST_1_attribute_mapping_path_select", jsdai.SMapping_schema.SMapping_schema.class);
  }

}
