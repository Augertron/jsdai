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

package jsdai.lang;

import jsdai.dictionary.*;

/**
 * This class is a supertype of several classes designed for description 
 * of the named data types (entity definitions, simple, enumeration, 
 * aggregation, select and defined types).
 * The class is for internal JSDAI use only.
 */
public abstract class DataType extends CEntity implements EData_type {

	static final int DATA_TYPE = 1;

	static final int NUMBER = 2;
	static final int REAL = 3;
	static final int INTEGER = 4;
	static final int LOGICAL = 5;
	static final int BOOLEAN = 6;
	static final int STRING = 7;
	static final int BINARY = 8;

	static final int ENTITY = 9;

	static final int DEFINED_TYPE = 10;

	static final int LIST = 11;
	static final int SET = 12;
	static final int BAG = 13;
	static final int ARRAY = 14;
	static final int AGGREGATE = 15;

	static final int ENUMERATION = 16;
	static final int EXTENDED_ENUM = 17;
	static final int EXTENSIBLE_ENUM = 18;
	static final int EXTENDED_EXTENSIBLE_ENUM = 19;

	static final int SELECT = 20;
	static final int NON_EXT_SELECT = 21;
	static final int EXTENDED_SELECT = 22;
	static final int EXT_NON_EXT_SELECT = 23;
	static final int ENT_EXT_NON_EXT_SELECT = 24;
	static final int ENTITY_SELECT = 25;
	static final int ENT_NON_EXT_SELECT = 26;


	static final int EXTENSIBLE_SELECT = 27;
	static final int EXT_EXT_SELECT = 28;
	static final int ENT_EXT_SELECT = 29;
	static final int ENT_EXT_EXT_SELECT = 30;


	String nameUpperCase;
	protected int express_type;

	A_string typeof_aggr;
	ESchema_definition typeof_schema = null;
	Value typeof_value = null;

	protected DataType() { }

	protected String getNameUpperCase(){
//		synchronized (syncObject) {
		if (nameUpperCase == null) {
			try {
				nameUpperCase = ((EData_type)this).getName(null).toUpperCase();
			} catch (Exception ex) {
				return null;
			}
		}
		return nameUpperCase;
//		} // syncObject
	}


	boolean allow_entity() throws SdaiException {
		if (express_type == ENTITY) {
			return true;
		} else if (express_type >= LIST && express_type <= AGGREGATE) {
			return ((AggregationType)this).allow_entity_aggregate((EAggregation_type)this);
		} else if (express_type != DEFINED_TYPE) {
			return false;
		}
		DataType type = this;
		while (type.express_type == DEFINED_TYPE) 	{
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type < SELECT || type.express_type > ENT_EXT_EXT_SELECT) {
			return type.allow_entity();
		}
		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
		throw new SdaiException(SdaiException.SY_ERR, base);
	}


	boolean search_entity(CEntity_definition def_for_value) throws SdaiException {
		if (express_type == ENTITY) {
			if (def_for_value.isSubtypeOf((CEntity_definition)this)) {
				return true;
			} else {
				return false;
			}
		} else if (express_type >= LIST && express_type <= AGGREGATE) {
			return ((AggregationType)this).search_entity_aggregate(def_for_value);
		} else if (express_type != DEFINED_TYPE) {
			return false;
		}
		DataType type = this;
		while (type.express_type == DEFINED_TYPE) 	{
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type < SELECT || type.express_type > ENT_EXT_EXT_SELECT) {
			return type.search_entity(def_for_value);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
		throw new SdaiException(SdaiException.SY_ERR, base);
	}


	final String get_data_type_name() throws SdaiException, java.io.IOException {
		DataType type = this;
		while (type.express_type == DEFINED_TYPE) 	{
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= SELECT && type.express_type <= ENT_EXT_EXT_SELECT) {
			return "select type";
		}
		if (type.express_type >= ENUMERATION && type.express_type <= EXTENDED_EXTENSIBLE_ENUM) {
			return "enumeration";
		}
		if (type.express_type >= LIST && type.express_type <= AGGREGATE) {
			return "aggregate";
		}
		switch (type.express_type) {
			case INTEGER:
				return "integer";
			case REAL:
			case NUMBER:
				return "real";
			case LOGICAL:
				return "logical";
			case BOOLEAN:
				return "boolean";
			case STRING:
				return "string";
			case BINARY:
				return "binary";
			case ENTITY:
				return "entity instance";
		}
		return "";
	}


}
