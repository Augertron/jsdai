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

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

import java.lang.reflect.*;

public class SExtended_dictionary_schema extends SSuper {

	public static final String time_stamp = "2005-01-20 T17:33:51";
	public static final String version = "3.6.300, 2005-01-10";
	public static final SSuper ss;

	static{
		ss = SSuper.initSuper(new SExtended_dictionary_schema());
		initDefinedDataTypes();
		initNonDefinedDataTypes();
	}
	protected CEntity makeInstanceX(Class c) throws java.lang.InstantiationException, java.lang.IllegalAccessException {
			return (CEntity)c.newInstance();
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
	public static jsdai.dictionary.EDefined_type _st_base_type;
	public static jsdai.dictionary.EDefined_type _st_constructed_type;
	public static jsdai.dictionary.EDefined_type _st_declaration_scope_type;
	public static jsdai.dictionary.EDefined_type _st_declaration_type;
	public static jsdai.dictionary.EDefined_type _st_documentation_object;
	public static jsdai.dictionary.EDefined_type _st_entity_or_subtype_expression;
	public static jsdai.dictionary.EDefined_type _st_entity_or_view_or_subtype_expression;
	public static jsdai.dictionary.EDefined_type _st_explicit_or_derived;
	public static jsdai.dictionary.EDefined_type _st_express_id;
	public static jsdai.dictionary.EDefined_type _st_info_object_id;
	public static jsdai.dictionary.EDefined_type _st_map_or_view_definition_select;
	public static jsdai.dictionary.EDefined_type _st_schema_map_or_view_definition;
	public static jsdai.dictionary.EDefined_type _st_entity_or_view_definition_or_simple_type;
	public static jsdai.dictionary.EDefined_type _st_type_or_rule;
	public static jsdai.dictionary.EDefined_type _st_underlying_type;

	static void initDefinedDataTypes(){
		_st_base_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("base_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_constructed_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("constructed_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_declaration_scope_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("declaration_scope_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_declaration_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("declaration_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_documentation_object = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("documentation_object",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_entity_or_subtype_expression = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("entity_or_subtype_expression",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_entity_or_view_or_subtype_expression = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("entity_or_view_or_subtype_expression",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_explicit_or_derived = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("explicit_or_derived",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_express_id = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("express_id",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_info_object_id = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("info_object_id",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_map_or_view_definition_select = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("map_or_view_definition_select",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_schema_map_or_view_definition = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("schema_map_or_view_definition",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_entity_or_view_definition_or_simple_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("entity_or_view_definition_or_simple_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_type_or_rule = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("type_or_rule",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_underlying_type = (jsdai.dictionary.EDefined_type)SdaiSession.findDataType("underlying_type",jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
	}

	public static jsdai.dictionary.EData_type _st_list_0_parameter;
	public static jsdai.dictionary.EData_type _st_list_1_string;
	public static jsdai.dictionary.EData_type _st_list_0_unique_entity_or_view_definition;
	public static jsdai.dictionary.EData_type _st_list_0_unique_express_id;
	public static jsdai.dictionary.EData_type _st_list_1_express_id;
	public static jsdai.dictionary.EData_type _st_list_1_entity_definition;
	public static jsdai.dictionary.EData_type _st_set_1_interfaced_declaration;
	public static jsdai.dictionary.EData_type _st_set_1_named_type;
	public static jsdai.dictionary.EData_type _st_set_1_referenced_declaration;
	public static jsdai.dictionary.EData_type _st_set_0_entity_definition;
	public static jsdai.dictionary.EData_type _st_set_1_entity_or_view_or_subtype_expression;
	public static jsdai.dictionary.EData_type _st_list_1_attribute;
	public static jsdai.dictionary.EData_type _st_set_1_used_declaration;
	public static jsdai.dictionary.EData_type _st_list_0_unique_view_definition;
	public static jsdai.dictionary.EData_type _st_list_0_unique_entity_definition;
	public static jsdai.dictionary.EData_type _st_set_1_entity_or_subtype_expression;
	public static jsdai.dictionary.EData_type _st_generalset_0_entity_or_view_or_subtype_expression;
	public static jsdai.dictionary.EData_type _st_generalset_0_entity_or_subtype_expression;
	public static jsdai.dictionary.EData_type _st_generallist_0_entity_or_view_definition;
	public static jsdai.dictionary.EData_type _st_generallist_0_entity_definition;

	static void initNonDefinedDataTypes(){
		_st_list_1_string = SdaiSession.findDataType("_LIST_1_STRING", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_1_attribute = SdaiSession.findDataType("_LIST_1_attribute", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_referenced_declaration = SdaiSession.findDataType("_SET_1_referenced_declaration", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_generalset_0_entity_or_subtype_expression = SdaiSession.findDataType("_GENERALSET_0_entity_or_subtype_expression", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_0_unique_express_id = SdaiSession.findDataType("_LIST_0_UNIQUE_express_id", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_interfaced_declaration = SdaiSession.findDataType("_SET_1_interfaced_declaration", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_used_declaration = SdaiSession.findDataType("_SET_1_used_declaration", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_0_unique_entity_definition = SdaiSession.findDataType("_LIST_0_UNIQUE_entity_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_0_entity_definition = SdaiSession.findDataType("_SET_0_entity_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_0_parameter = SdaiSession.findDataType("_LIST_0_parameter", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_0_unique_view_definition = SdaiSession.findDataType("_LIST_0_UNIQUE_view_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_0_unique_entity_or_view_definition = SdaiSession.findDataType("_LIST_0_UNIQUE_entity_or_view_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_entity_or_view_or_subtype_expression = SdaiSession.findDataType("_SET_1_entity_or_view_or_subtype_expression", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_1_entity_definition = SdaiSession.findDataType("_LIST_1_entity_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_generallist_0_entity_or_view_definition = SdaiSession.findDataType("_GENERALLIST_0_entity_or_view_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_entity_or_subtype_expression = SdaiSession.findDataType("_SET_1_entity_or_subtype_expression", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_generalset_0_entity_or_view_or_subtype_expression = SdaiSession.findDataType("_GENERALSET_0_entity_or_view_or_subtype_expression", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_set_1_named_type = SdaiSession.findDataType("_SET_1_named_type", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_generallist_0_entity_definition = SdaiSession.findDataType("_GENERALLIST_0_entity_definition", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
		_st_list_1_express_id = SdaiSession.findDataType("_LIST_1_express_id", jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema.class);
	}

}
