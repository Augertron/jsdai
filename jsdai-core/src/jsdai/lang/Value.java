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

import java.io.*;
import jsdai.dictionary.*;

/** This class represents instances of any data type defined in EXPRESS language 
 * and their values. The data types may be simple, such as integer, double, 
 * string and so on, entities, aggregates including nested, and defined data types. 
 * These instances are used not only as values of entity attributes but also 
 * may implement other items of the Express code, for example, expressions and identifiers. 
 * Moreover, objects of this class are used to represent parameters and return type
 * for functions and procedures written in EXPRESS.
 * For all EXPRESS operators, built-in functions and built-in procedures 
 * the corresponding methods are included in this class.
 * Within the class, the following two data types are defined:
 * <ul>
 * <li> <B>declared</B> data type which specifies a possible type of value that can 
 *   be wrapped in an object of the class; This type can be accessed using 
 *   {@link #getDeclaredType getDeclaredType} method;
 * <li> <B>actual</B> data type which specifies the type of value that is wrapped 
 *   in an object of the class; This type can be accessed using 
 *   {@link #getActualType getActualType} method.
 * </ul>
 * The actual data type shall coincide with the declared data type or be in a 
 * specialization relation with it (as specified in ISO 10303-11::9.2.6 Specialization).
 * Additionally, the type of the value represented by the class is characterized by the 
 * type indicator, which is an integer constant. This indicator can be accessed using 
 * {@link #getActualJavaType getActualJavaType} method. The specific constants indicating 
 * the supported data types are as follows:
 * <ul>
 * <li>   8 for LOGICAL data type;
 * <li>   9 for BOOLEAN data type;
 * <li>  11 for INDETERMINATE;
 * <li>  20 for BINARY data type;
 * <li>  22 for INTEGER data type;
 * <li>  23 for REAL and NUMBER data types;
 * <li>  24 for STRING data type;
 * <li>  25 for ENUMERATION data type;
 * <li>  51 for defined data type;
 * <li>  52 for ENTITY data type;
 * <li>  54 for general aggregation data type;
 * <li> 101 for any data type compatible with AGGREGATE OF INTEGER;
 * <li> 102 for any data type compatible with AGGREGATE OF REAL;
 * <li> 103 for any data type compatible with AGGREGATE OF STRING;
 * <li> 104 for any data type compatible with AGGREGATE OF LOGICAL;
 * <li> 105 for any data type compatible with AGGREGATE OF BOOLEAN;
 * <li> 106 for any data type compatible with AGGREGATE OF ENUMERATION;
 * <li> 107 for any data type compatible with AGGREGATE OF BINARY;
 * <li> 108 for any data type compatible with AGGREGATE OF ENTITY;
 * <li> 109 for aggregation data types representing aggregates (even nested) 
 *    whose elements are of select data type;
 * <li> 110 for any data type compatible with AGGREGATE OF AGGREGATE OF INTEGER;
 * <li> 111 for any data type compatible with AGGREGATE OF AGGREGATE OF REAL;
 * <li> 112 for any data type compatible with AGGREGATE OF AGGREGATE OF STRING;
 * <li> 113 for any data type compatible with AGGREGATE OF AGGREGATE OF LOGICAL;
 * <li> 114 for any data type compatible with AGGREGATE OF AGGREGATE OF BOOLEAN;
 * <li> 115 for any data type compatible with AGGREGATE OF AGGREGATE OF ENUMERATION;
 * <li> 116 for any data type compatible with AGGREGATE OF AGGREGATE OF AGGREGATE OF REAL;
 * <li> 150 for a collection of values of all attributes of an entity data type;
 * <li> 151 for a collection of values of all attributes of a simple entity data type.
 * </ul>
 * The term "compatible" used above follows the EXPRESS specialization 
 * definition (clause 9.2.6) with some analogous extensions.
<p>
 * The JSDAI-ExpressCompiler is using objects of <code>Value</code> to implement 
 * expressions, defined in EXPRESS functions, procedures, where-rules and derived 
 * attributes. Within EXPRESS expressions methods of this class implementing 
 * operators are used.
 */

public class Value {
//public static boolean prnt = false;
//public static CEntity eee;

/** The type of a value wrapped in an object of this class. */
	public int tag;

/**
	The field to store a value of one of the following types: 
	integer, logical, boolean, and also instance identifier in the case of the 
	entity instance.
*/
	int integer;

/**
	The field to store a real value.
*/
	double real;

/**
	The field to store a value of either string or enumeration type. 
	Also, in this field the name of the defined type is stored.
*/
	String string;

/**
	The field to store a value of one of the following types: binary, 
	entity data type, EntityValue or an array of EntityValue. 
*/
	Object reference;

/**
	The field to store aggregate value. The first element of this array is also 
	used a value given through a defined type. The name of the defined type is 
	stored in <code>string</code>. 
*/
	Value nested_values[];

/**
	The count of elements in <code>nested_values</code>.
*/
	int length;

/**
	The type of the data the values having which can be wrapped by this 
	object of Value.
*/
	EData_type d_type;

/**
	The type of the value wrapped by an object of this class. It should be 
	consistent with <code>v_type</code>.
*/
	EData_type v_type;

/**
	Represents indeterminate (denoted as &#63) in Express language. 
*/
	public static final int INDETERMINATE = PhFileReader.MISSING;

/**
	The owner of the aggregate wrapped by this object of Value.
	The field is used in methods designed for processing of expressions in 
	Express language.
*/
	SdaiCommon agg_owner;

/**
	An auxiliary variable used in methods designed for processing of expressions in 
	Express language.
*/
	int aux;

/**
	An instance of the class ComplexEntityValue used in methods designed for 
	processing of expressions in Express language.
*/
	ComplexEntityValue compl_ent_values;

/**
	The number identifying a select path following which the value represented 
	by this object of Value can be reached. 
*/
	int sel_number;


// Statics

/**
	The standard representation for an integer number in Format function 
	of Express language. 
*/
	private static final String INT_FORMAT = "7I";

/**
	The standard representation for a real number in Format function 
	of Express language. 
*/
	private static final String REAL_FORMAT = "10E";

	Object [] types;

	int types_count;

	boolean [] is_included;

	static final int NUMBER_OF_TYPES = 16;

	static final int NUMBER_OF_UNQUALIFIED_TYPES = 11;
	private static final int WRONG_VALUE_TAG = -99;

//	private static final boolean use_asterisk = false;
	private static final boolean use_asterisk = true;

// A cluster of constants.
	private static final int PAIRS_LIST_LENGTH = 16;
	private static final int ZEROS_COUNT = 16;
	private static final int MAX_POWER = 308;
	private static final int BYTES_COUNT_IN_ENTITY_NAME = 512;

	static final int A_INTEGER 				= 101;
	static final int A_DOUBLE 					= 102;
	static final int A_STRING 					= 103;
	static final int A_LOGICAL 				= 104;
	static final int A_BOOLEAN 				= 105;
	static final int A_ENUMERATION 			= 106;
	static final int A_BINARY 					= 107;
	static final int AENTITY 					= 108;
	static final int AMIXED 					= 109;
	static final int AA_INTEGER 				= 110;
	static final int AA_DOUBLE 				= 111;
	static final int AA_STRING 				= 112;
	static final int AA_LOGICAL 				= 113;
	static final int AA_BOOLEAN 				= 114;
	static final int AA_ENUMERATION 			= 115;
	static final int AAA_DOUBLE 				= 116;
	private static final int COMPLEX_ENTITY_VALUE 	= 150;
	private static final int ENTITY_VALUE 	      	= 151;
	static final int EXCPT						= 201;

	static final char AT = '@';
	static final char UPPER = '^';
	static final char QUESTION_MARK = '?';
	static final char CONJUNCTION = '&';
	static final char ANY = '#';
	static final char DOLLAR_SIGN = '$';
	static final char ASTERISK = '*';
	static final char BACKSLASH = '\\';
	static final char NEGATION = '!';
	static final char SPACE = ' ';
	static final char CAPITAL_I = 'I';
	static final char CAPITAL_F = 'F';
	static final char CAPITAL_E = 'E';
	static final char ZERO = '0';


	static final int NUMBER_OF_CHARACTERS_IN_VALUE = 512;




	Value() {
//		nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
//		route = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
	}



/**
 * Returns declared data type attached to this object of <code>Value</code>.
 * @return declared data type.
 * @see #getActualType
 * @see #getActualJavaType
 */
	public EData_type getDeclaredType() {
		return d_type;
	}


/**
 * Returns actual data type attached to this object of <code>Value</code>.
 * The value (provided not INDETERMINATE) of this type is wrapped in the object. 
 * If value is INDETERMINATE, then null is returned.
 * @return actual data type.
 * @see #getDeclaredType
 * @see #getActualJavaType
 */
	public EData_type getActualType() {
		return v_type;
	}


/**
 * Returns an indicator specifying actual data type attached to this object of 
 * <code>Value</code>. The specific constants indicating 
 * the supported data types are as follows:
 * <ul>
 * <li>   8 for LOGICAL data type;
 * <li>   9 for BOOLEAN data type;
 * <li>  11 for INDETERMINATE;
 * <li>  12 for the case of value redefinition;
 * <li>  20 for BINARY data type;
 * <li>  22 for INTEGER data type;
 * <li>  23 for REAL and NUMBER data types;
 * <li>  24 for STRING data type;
 * <li>  25 for ENUMERATION data type;
 * <li>  51 for defined data type;
 * <li>  52 for ENTITY data type;
 * <li>  54 for general aggregation data type;
 * <li> 101 for any data type compatible with AGGREGATE OF INTEGER;
 * <li> 102 for any data type compatible with AGGREGATE OF REAL;
 * <li> 103 for any data type compatible with AGGREGATE OF STRING;
 * <li> 104 for any data type compatible with AGGREGATE OF LOGICAL;
 * <li> 105 for any data type compatible with AGGREGATE OF BOOLEAN;
 * <li> 106 for any data type compatible with AGGREGATE OF ENUMERATION;
 * <li> 107 for any data type compatible with AGGREGATE OF BINARY;
 * <li> 108 for any data type compatible with AGGREGATE OF ENTITY;
 * <li> 109 for aggregation data types representing aggregates (even nested) 
 *    whose elements are of select data type;
 * <li> 110 for any data type compatible with AGGREGATE OF AGGREGATE OF INTEGER;
 * <li> 111 for any data type compatible with AGGREGATE OF AGGREGATE OF REAL;
 * <li> 112 for any data type compatible with AGGREGATE OF AGGREGATE OF STRING;
 * <li> 113 for any data type compatible with AGGREGATE OF AGGREGATE OF LOGICAL;
 * <li> 114 for any data type compatible with AGGREGATE OF AGGREGATE OF BOOLEAN;
 * <li> 115 for any data type compatible with AGGREGATE OF AGGREGATE OF ENUMERATION;
 * <li> 116 for any data type compatible with AGGREGATE OF AGGREGATE OF AGGREGATE OF REAL;
 * <li> 150 for a collection of values of all attributes of an entity data type;
 * <li> 151 for a collection of values of all attributes of a simple entity data type.
 * </ul>
 * @return an integer constant specifying actual data type.
 * @see #getDeclaredType
 * @see #getActualType
  */
	public int getActualJavaType() {
		return tag;
	}


	final void unset_Value() throws SdaiException {
		reference = null;
		d_type = null;
		v_type = null;
		agg_owner = null;
		if (nested_values != null) {
			for (int i = 0; i < nested_values.length; i++) {
				if (nested_values[i] != null) {
					nested_values[i].unset_Value();
				}
			}
		}
		if (compl_ent_values != null) {
			compl_ent_values.unset_ComplexEntityValue();
		}
	}


// Method below is for debugging purposes
/*	final void check_references_Value() throws SdaiException {
		if (reference != null && reference instanceof EEntity) {
			SdaiSession.cnt++;
			System.out.println("Value *** reference: " + (EEntity)reference);
		}
		if (nested_values != null) {
			for (int i = 0; i < nested_values.length; i++) {
				if (nested_values[i] != null) {
					nested_values[i].check_references_Value();
				}
			}
		}
	}*/


	boolean copyValue(Value val, CEntity owning_instance) throws SdaiException {
		boolean con_created = false;
		tag = val.tag;
		switch (tag) {
			case PhFileReader.INTEGER:
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				integer = val.integer;
				break;
			case PhFileReader.REAL:
				real = val.real;
				break;
			case PhFileReader.ENUM:
			case PhFileReader.STRING:
				string = val.string;
			case PhFileReader.BINARY:
				reference = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				string = val.string;
				length = 1;
				if (nested_values == null) {
					nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (nested_values[0] == null) {
					nested_values[0] = new Value();
				}
				nested_values[0].copyValue(val.nested_values[0], owning_instance);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				integer = val.integer;
				if (val.reference instanceof SdaiModel.Connector) {
					reference = ((SdaiModel.Connector)val.reference).copyConnector(owning_instance);
					con_created = true;
				} else {
					CEntity inst = (CEntity)val.reference;
					if (inst.owning_model == null) {
						tag = PhFileReader.MISSING;
					} else {
						reference = val.reference;
					}
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (nested_values == null || val.length > nested_values.length) {
					enlarge_nested_values(val.length);
				}
				length = val.length;
				reference = val.reference;
				boolean con_found = false;
				for (int i = 0; i < val.length; i++) {
					if (nested_values[i] == null) {
						nested_values[i] = new Value();
					}
					boolean res = nested_values[i].copyValue(val.nested_values[i], owning_instance);
					if (res) {
						con_found = true;
					}
				}
				if (con_found) {
					real = Double.NaN;
				}
				break;
			default:
		}
		return con_created;
	}


	void copyValueStepFile(Value val) throws SdaiException {
		tag = val.tag;
		switch (tag) {
			case PhFileReader.INTEGER:
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				integer = val.integer;
				break;
			case PhFileReader.REAL:
				real = val.real;
				break;
			case PhFileReader.ENUM:
			case PhFileReader.STRING:
				string = val.string;
			case PhFileReader.BINARY:
				reference = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				string = val.string;
				integer = val.integer;
				length = 1;
				if (nested_values == null) {
					nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (nested_values[0] == null) {
					nested_values[0] = new Value();
				}
				nested_values[0].copyValueStepFile(val.nested_values[0]);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				integer = val.integer;
				reference = val.reference;
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (nested_values == null || val.length > nested_values.length) {
					enlarge_nested_values(val.length);
				}
				length = val.length;
				reference = val.reference;
				for (int i = 0; i < val.length; i++) {
					if (nested_values[i] == null) {
						nested_values[i] = new Value();
					}
					nested_values[i].copyValueStepFile(val.nested_values[i]);
				}
				break;
			default:
		}
	}


	boolean look4connectors(CEntity owning_instance) throws SdaiException {
		boolean con_created = false;
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				nested_values[0].look4connectors(owning_instance);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (reference instanceof SdaiModel.Connector) {
					reference = ((SdaiModel.Connector)reference).copyConnector(owning_instance);
					con_created = true;
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				boolean con_found = false;
				for (int i = 0; i < length; i++) {
					boolean res = nested_values[i].look4connectors(owning_instance);
					if (res) {
						con_found = true;
					}
				}
				if (con_found) {
					real = Double.NaN;
				}
				break;
			default:
		}
		return con_created;
	}


	boolean move_connectors(CEntity owning_instance, SdaiModel old_model) throws SdaiException {
		boolean con_moved = false;
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].move_connectors(owning_instance, old_model);
			case PhFileReader.ENTITY_REFERENCE:
				if (reference instanceof SdaiModel.Connector) {
					SdaiModel.Connector old_con = (SdaiModel.Connector)reference;
					reference = old_con.copyConnector(owning_instance);
					SdaiModel saved_model = owning_instance.owning_model;
					owning_instance.owning_model = old_model;
					old_con.disconnect();
					owning_instance.owning_model = saved_model;
					con_moved = true;
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					boolean res = nested_values[i].move_connectors(owning_instance, old_model);
					if (res) {
						con_moved = true;
					}
				}
				break;
			default:
		}
		return con_moved;
	}


	boolean resolve_references(CEntity owning_instance) throws SdaiException {
		boolean resolved = false;
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].resolve_references(owning_instance);
			case PhFileReader.ENTITY_REFERENCE:
				if (reference instanceof SdaiModel.Connector) {
					SdaiModel.Connector con = (SdaiModel.Connector)reference;
					if (con.modelInConnector != null) {
						con.resolveConnector(false, true, false);
					}
					return true;
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					boolean res = nested_values[i].resolve_references(owning_instance);
					if (res) {
						resolved = true;
					}
				}
				break;
			default:
		}
		return resolved;
	}


	void find_references(AEntity refs, SdaiIterator it_refs) throws SdaiException {
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				nested_values[0].find_references(refs, it_refs);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				refs.addAfter(it_refs, (CEntity)reference);
				it_refs.next();
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					nested_values[i].find_references(refs, it_refs);
				}
				break;
			default:
		}
	}


	void find_user(CEntity user, CEntity target, AEntity users) throws SdaiException {
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				nested_values[0].find_user(user, target, users);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (reference == target) {
					if (users.myType == null || users.myType.express_type == DataType.LIST) {
						users.addAtTheEnd(user, null);
					} else {
						users.setForNonList(user, users.myLength, null, null);
					}
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					nested_values[i].find_user(user, target, users);
				}
				break;
			default:
		}
	}


	int find_user_count(CEntity user, CEntity target, int count) throws SdaiException {
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				count = nested_values[0].find_user_count(user, target, count);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (reference == target) {
					count++;
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					count = nested_values[i].find_user_count(user, target, count);
				}
				break;
			default:
		}
		return count;
	}


	boolean user_exists(CEntity target) throws SdaiException {
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].user_exists(target);
			case PhFileReader.ENTITY_REFERENCE:
				if (reference == target) {
					return true;
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				for (int i = 0; i < length; i++) {
					boolean res = nested_values[i].user_exists(target);
					if (res) {
						return true;
					}
				}
				break;
			default:
		}
		return false;
	}



//--------------------------------  get methods  ------------------------------

/**
 * Returns an integer value wrapped in an object of this class.
 * To represent unset case, Integer.MIN_VALUE is used.
 * @return integer value described by this class.
 */
	public int getInteger() throws SdaiException {
		return getInteger(null);
	}
	int getInteger(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.INTEGER) {
			return integer;
		} else if (tag == INDETERMINATE) {
			return Integer.MIN_VALUE;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_INRE, 
				staticFields.current_instance_identifier);
			return Integer.MIN_VALUE;
		} else if (tag == PhFileReader.REAL && (real - (int)real == 0.)) {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				tag = PhFileReader.INTEGER;
				integer = (int)real;
				return integer;
			} else {
				return (int)real;
			}
		} else if (tag == PhFileReader.REAL) {
			return (int)real;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BINT, 
											   staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return Integer.MIN_VALUE;
		}
	}


/**
 * Returns a real value wrapped in an object of this class.
 * To represent unset case, Double.NaN is used.
 * @return real value described by this class.
 */
	public double getDouble() throws SdaiException {
		return getDouble(null);
	}
	double getDouble(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.REAL) {
			return real;
		} else if (tag == INDETERMINATE) {
			return Double.NaN;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_DORE, 
				staticFields.current_instance_identifier);
			return Double.NaN;
		} else if (tag == PhFileReader.INTEGER) {
			staticFields = StaticFields.get();
			if (staticFields.importing) {	
				tag = PhFileReader.REAL;
				real = integer;
				return real;
			} else {
				return integer;
			}
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BREA, 
					staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return Double.NaN;
		}
	}


/**
 * Returns a string value wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return string value described by this class.
 */
	public String getString() throws SdaiException {
		return getString(null);
	}
	String getString(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.STRING) {
			return string;
		} else if (tag == INDETERMINATE) {
			return null;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_STRE, 
				staticFields.current_instance_identifier);
			return null;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BSTR, 
					staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return null;
		}
	}


/**
 * Returns an integer representing the logical value wrapped in an object 
 * of this class. The following integers are possible:
 * <ul><li> 0 for unset case; 
 * <li> 1 for FALSE;
 * <li> 2 for TRUE;
 * <li> 3 for UNKNOWN.</ul>
 * @return a constant representing the logical value described by this class.
 */
	public int getLogical() throws SdaiException {
		return getLogical(null);
	}
	int getLogical(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.LOGICAL) {
			return integer + 1;
		} else if (tag == PhFileReader.BOOLEAN) {
			return integer + 1;
		} else if (tag == INDETERMINATE) {
			return 0;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_LORE, 
				staticFields.current_instance_identifier);
			return 0;
		} else if (tag == PhFileReader.INTEGER && integer >= 0 && integer < 3) {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				tag = PhFileReader.LOGICAL;
			}
			return integer + 1;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BLOG, 
					staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return 0;
		}
	}


/**
 * Returns an integer representing the boolean value wrapped in an object 
 * of this class. The following integers are possible:
 * <ul><li> 0 for unset case; 
 * <li> 1 for FALSE;
 * <li> 2 for TRUE.</ul>
 * @return a constant representing the boolean value described by this class.
 */
	public int getBoolean() throws SdaiException {
		return getBoolean(null);
	}
	int getBoolean(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.BOOLEAN || tag == PhFileReader.LOGICAL) {
			return integer + 1;
		} else if (tag == INDETERMINATE) {
			return 0;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BORE, 
				staticFields.current_instance_identifier);
			return 0;
		} else if (tag == PhFileReader.INTEGER && integer >= 0 && integer < 2) {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				tag = PhFileReader.BOOLEAN;
			}
			return integer + 1;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BBOO, 
					staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return 0;
		}
	}


/**
 * Returns an integer representing enumeration value wrapped in an object 
 * of this class. In the case of unset, 0 is returned.
 * @return a constant representing enumeration value described by this class.
 */
	public int getEnumeration() throws SdaiException {
		if (tag == PhFileReader.ENUM) {
			EEnumeration_type enum_tp;
			if (((DataType)v_type).express_type == DataType.DEFINED_TYPE) {
				enum_tp = (EEnumeration_type)((CDefined_type)v_type).getDomain(null);
			} else {
				enum_tp = (EEnumeration_type)v_type;
			}
//			return getEnumeration((EEnumeration_type)v_type, null, null, null);
			return getEnumeration(enum_tp, null, null, null);
		} else {
			return 0;
		}
	}


	int getEnumeration(EEnumeration_type enum_type, CExplicit_attribute attrib, SdaiContext context, 
			SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.ENUM) {
			A_string elements = enum_type.getElements(null, context);
			for (int i = 1; i <= elements.myLength; i++) {
				String item = elements.getByIndex(i);
				if (string.equalsIgnoreCase(item)) {
					return i;
				}
			}
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BEN1, 
					staticFields.current_instance_identifier, attrib);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//		Print warning message
			}
			return 0;
		} else if (tag == INDETERMINATE) {
			return 0;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_ENRE, 
				staticFields.current_instance_identifier, attrib);
			return 0;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BEN2, 
					staticFields.current_instance_identifier, attrib);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return 0;
		}
	}


/**
 * Returns a binary value wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return binary value described by this class.
 */
	public Binary getBinary() throws SdaiException {
		return getBinary(null);
	}
	Binary getBinary(SdaiSession session) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.BINARY) {
			return (Binary)reference;
		} else if (tag == INDETERMINATE) {
			return null;
		} else if (tag == PhFileReader.REDEFINE) {
			staticFields = StaticFields.get();
			EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BIRE, 
				staticFields.current_instance_identifier);
			return null;
		} else {
			staticFields = StaticFields.get();
			if (staticFields.importing) {
				EntityValue.printWarningToLogo(session, AdditionalMessages.RD_BBIN, 
					staticFields.current_instance_identifier);
//				unset_wrong_references(staticFields.c_instance);
			} else {
//			Print warning message
			}
			return null;
		}
	}


	InverseEntity getInstance(CEntity inst, CExplicit_attribute attr) throws SdaiException {
		StaticFields staticFields;
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			if (reference instanceof CEntity) {
				CEntity ref = (CEntity)reference;
				if (!(ref.owning_model != null && ref.owning_model.optimized)) {
					inst.addToInverseList(ref);
				}
				return ref;
			} else {
				return (SdaiModel.Connector)reference;
			}
		} else if (tag == PhFileReader.ENTITY_REFERENCE_SPECIAL) {
			CLateBindingEntity late_ref = (CLateBindingEntity)reference;
			if (late_ref != null) {
				late_ref.inverseAdd(inst);
			}
			return late_ref;
//			return (CLateBindingEntity)reference;
		} else if (tag == INDETERMINATE) {
			return null;
		} else if (tag == PhFileReader.REDEFINE) {
			boolean redecl = false;
			if (attr != null) {
				CEntityDefinition def = (CEntityDefinition)inst.getInstanceType();
				redecl = def.checkIfDerived(attr);
			}
			if (!redecl) {
				staticFields = StaticFields.get();
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_RIRE, 
					staticFields.current_instance_identifier);
			}
			return null;
		}
		if (aux != PhFileReader.ENTITY_REFERENCE) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BREF, 
				inst.instance_identifier);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
		}
		return null;
	}

	/**
	 * Prints a warning message to the log file in the case where a submitted attribute of an 
	 * entity instance is redefined, yet it has a value assigned, which is meaningless. 
	 * @param inst given entity instance.
	 * @param attr the attribute to be checked.
	 * @since 4.1.2
	 */
	public void checkRedefine(CEntity inst, EAttribute attr) throws SdaiException {
		if (!inst.owning_model.repository.importing || tag == PhFileReader.REDEFINE) {
			return;
		}
		EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_REDV, 
			inst.instance_identifier, attr.getName(null));
	}


	Object getMixedValue(CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		CEntity domain = (CEntity)attr.getDomain(null);
		DataType dom = (DataType)domain;
		while (dom.express_type == DataType.DEFINED_TYPE) {
			dom = (DataType)((CDefined_type)dom).getDomain(null);
		}
		if (dom.express_type < DataType.SELECT || dom.express_type > DataType.ENT_EXT_EXT_SELECT) {
//System.out.println(" !!!DOMAIN: " + dom.getClass().getName());
//
			throw new SdaiException(SdaiException.SY_ERR);
		}
		StaticFields staticFields = StaticFields.get();
		staticFields.def_count = 0;
		sel_number = -1;
		return getMixedInternal(staticFields, attr, (CDefined_type)domain, inst, (SelectType)dom, this, true);
	}
	private Object getMixedInternal(StaticFields staticFields, CExplicit_attribute attr, CDefined_type def, CEntity inst, 
			SelectType sel, Value root, boolean first) throws SdaiException {
		Object value_returned = null;
if (SdaiSession.debug2) System.out.println("  IN getMixedInternal   tag = " + tag);
if (SdaiSession.debug2) System.out.println("   DEFINED TYPE: " + def.getName(null));
		switch (tag) {
			case PhFileReader.ENTITY_REFERENCE:
					sel_number = 1;
					if (reference instanceof CEntity && inst != null) {
						CEntity ref = (CEntity)reference;
						if (!(ref.owning_model != null && ref.owning_model.optimized)) {
							inst.addToInverseList(ref);
						}
					}
					return reference;
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
					sel_number = 1;
					CLateBindingEntity late_ref = (CLateBindingEntity)reference;
					if (late_ref != null) {
						late_ref.inverseAdd(inst);
					}
					return late_ref;
//					return reference;
			case INDETERMINATE:
					sel_number = 0;
					return null;
			case PhFileReader.REDEFINE:
					boolean redecl = false;
					if (attr != null) {
						CEntityDefinition inst_def = (CEntityDefinition)inst.getInstanceType();
						redecl = inst_def.checkIfDerived(attr);
					}
					if (!redecl && inst != null) {
						EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_SERE, 
							inst.instance_identifier);
					}
					sel_number = 0;
					return null;
			case PhFileReader.INTEGER:
					value_returned = new Integer(integer);
					break;
			case PhFileReader.REAL:
					value_returned = new Double(real);
					break;
			case PhFileReader.LOGICAL:
			case PhFileReader.BOOLEAN:
					value_returned = new Integer(integer + 1);
					break;
			case PhFileReader.ENUM:
					int res = findEnumeration(string, def, inst);
					if (res > 0) {
						value_returned = new Integer(res);
					} else {
						String message;
						if (res == 0) {
							message = AdditionalMessages.RD_BEN1;
						} else {
							CDefined_type rec_def = recoverTyped(tag, sel, true, inst.owning_model.repository.session.sdai_context);
							if (rec_def != null) {
								res = findEnumeration(string, rec_def, inst);
								if (res > 0) {
									value_returned = new Integer(res);
									if (inst != null) {
										EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_TPRE,
											inst.instance_identifier, staticFields.current_attribute);
									}
									break;
								}
								if (res == 0) {
									message = AdditionalMessages.RD_BEN1;
								} else {
									message = AdditionalMessages.RD_BEN2;
								}
							} else {
								message = AdditionalMessages.RD_BEN2;
							}
						}
						if (inst != null) {
							EntityValue.printWarningToLogo(inst.owning_model.repository.session, message, 
								inst.instance_identifier, staticFields.current_attribute);
						}
//						if (staticFields.importing) {
//							unset_wrong_references(staticFields.c_instance);
//						}
						sel_number = 0;
						return null;
					}
					break;
			case PhFileReader.STRING:
					value_returned = string;
					break;
			case PhFileReader.BINARY:
					value_returned = (Binary)reference;
					break;
			case PhFileReader.TYPED_PARAMETER:
if (SdaiSession.debug2) System.out.println("  BEFORE findTyped");
					value_returned = findTyped(staticFields, attr, def, inst, sel, root);
if (SdaiSession.debug2) System.out.println("  IN TYPED_PARAMETER sel_number = " + sel_number);
					break;
			case PhFileReader.EMBEDDED_LIST:
					if (staticFields.def_count > 0) {
						root.sel_number = sel.giveSelectNumber(staticFields.route, staticFields.def_count);
					} else {
						root.sel_number = 0;
					}
					value_returned = findTypedAggregate(staticFields, def, sel, root, inst);
					break;
		}
		if (first && sel_number < 0) {
			if (staticFields.def_count > 0) {
				sel_number = sel.giveSelectNumber(staticFields.route, staticFields.def_count);
if (SdaiSession.debug2) System.out.println(" GET MIXED  " + sel_number + "   value_returned = " + value_returned.getClass().getName());
if (SdaiSession.debug2) System.out.print(" Route:");
if (SdaiSession.debug2) if (inst.instance_identifier == 2281 || inst.instance_identifier == 2284)
{for (int i = 0; i < staticFields.def_count; i++) System.out.print("  " + staticFields.route[i].getName(null));
System.out.println();}
			} else {
				if (recoverTyped(tag, sel, true, inst.owning_model.repository.session.sdai_context) != null) {
					if (inst != null) {
						EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_TPRE, 
							inst.instance_identifier, staticFields.current_attribute);
					}
					sel_number = sel.giveSelectNumber(staticFields.route, staticFields.def_count);
//System.out.println("  Value    type recovered  sel_number = " + sel_number +
//" inst: #" + inst.instance_identifier);
				} else {
					if (inst != null) {
						EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BTYP, 
							inst.instance_identifier, staticFields.current_attribute);
					}
//					if (staticFields.importing) {
//						unset_wrong_references(staticFields.c_instance);
//					}
					sel_number = 0;
					return null;
				}
			}
		}
//if (inst.instance_identifier == 2281 || inst.instance_identifier == 2284)
//System.out.println(" Value  sel_number: " + sel_number + 
//"   value_returned = " + value_returned.getClass().getName() + 
//"tag = " + tag);
		return value_returned;
	}
	private int findEnumeration(String str, CDefined_type def_type, CEntity inst) 
			throws SdaiException {
		DataType domain = (DataType)def_type.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type >= DataType.ENUMERATION && domain.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			A_string	elements;
			if (domain.express_type == DataType.EXTENSIBLE_ENUM || domain.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
				elements = ((EExtensible_enumeration_type)domain).getElements(null, 
					inst.owning_model.repository.session.sdai_context);
			} else {
				elements = ((EEnumeration_type)domain).getElements(null);
			}
			for (int i = 1; i <= elements.myLength; i++) {
				String item = elements.getByIndex(i);
				if (str.equalsIgnoreCase(item)) {
					return i;
				}
			}
		} else {
			return -1;
		}
		return 0;
	}


	private Object findTyped(StaticFields staticFields, CExplicit_attribute attr, CDefined_type def_type, CEntity inst, 
			SelectType sel, Value root) throws SdaiException {
		if (tag == PhFileReader.TYPED_PARAMETER) {
			CDefined_type def = findDefinedType(string.toLowerCase(), def_type, inst.owning_model.repository.session.sdai_context);
			if (def != null) {
				if (staticFields.route == null) {
					staticFields.route = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
				} else if (staticFields.def_count >= staticFields.route.length) {
					enlargeRoute(staticFields);
				}
				staticFields.route[staticFields.def_count] = def;
				staticFields.def_count++;
			} else {
				String type_name = string.toLowerCase();
				String def_type_name = def_type.getName(null);
				long inst_id = inst.instance_identifier;
				String instance_type = inst.getInstanceType().getName(null);
				DataType domain = (DataType)def_type.getDomain(null);
				String domain_name = ((EData_type)domain).getName(null);
				int domain_express_type = domain.express_type;
				String base = SdaiSession.line_separator + "   Instance id: #" + inst_id + 
					SdaiSession.line_separator + "   Instance type: " + instance_type + 
					SdaiSession.line_separator + "   Wanted defined type: " + type_name +
					SdaiSession.line_separator + "   Search started from the defined type: " + def_type_name +
					SdaiSession.line_separator + "   Domain name: " + domain_name +
					SdaiSession.line_separator + "   Domain Express type: " + domain_express_type;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			return nested_values[0].findTyped(staticFields, attr, def, inst, sel, root);
		} else {
			return getMixedInternal(staticFields, attr, def_type, inst, sel, root, false);
		}
	}


	private CDefined_type findDefinedType(String def_name, CDefined_type def, SdaiContext context) throws SdaiException {
if (SdaiSession.debug2) System.out.println("  IN findDefinedType  def_name = " + def_name + "   defined type name = " + def.getName(null));
		if (def_name.equals(def.getName(null))) {
			return def;
		}
		CDefined_type res;
		DataType domain = (DataType)def.getDomain(null);
		if (domain.express_type >= DataType.SELECT && domain.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			ANamed_type sels = ((ESelect_type)domain).getSelections(null, context);
if (SdaiSession.debug2) System.out.println("  IN findDefinedType domain is SELECT type   " + ((AEntity)sels).myLength);
			if (((AEntity)sels).myLength < 0) {
				((AEntity)sels).resolveAllConnectors();
			}
			int ln = ((AEntity)sels).myLength;
			DataType alternative;
			if (ln == 1) {
				alternative = (DataType)((AEntity)sels).myData;
				if (alternative.express_type == DataType.DEFINED_TYPE) {
					return findDefinedType(def_name, (CDefined_type)alternative, context);
				}
			} else {
				Object [] myDataA = (Object [])((AEntity)sels).myData;
				for (int i = 0; i < ln; i++) {
					alternative = (DataType)myDataA[i];
					if (alternative.express_type == DataType.DEFINED_TYPE) {
						res = findDefinedType(def_name, (CDefined_type)alternative, context);
						if (res != null) {
							return res;
						}
					}
				}
			}
		} else if (domain.express_type == DataType.DEFINED_TYPE) {
if (SdaiSession.debug2) System.out.println("  IN findDefinedType domain is defined type" + ((CDefined_type)domain).getName(null));
			return findDefinedType(def_name, (CDefined_type)domain, context);
		}
if (SdaiSession.debug2) System.out.println("  IN findDefinedType domain is of type: " + domain.getClass().getName());
		return null;
	}


	private CDefined_type recoverTyped(int code, SelectType sel, boolean first, SdaiContext context) throws SdaiException {
		ANamed_type sels = ((ESelect_type)sel).getSelections(null, context);
		if (((AEntity)sels).myLength < 0) {
			((AEntity)sels).resolveAllConnectors();
		}
		CDefined_type rec_def = null;
		int ln = ((AEntity)sels).myLength;
		DataType alternative;
		if (ln == 1) {
			alternative = (DataType)((AEntity)sels).myData;
			if (alternative.express_type == DataType.DEFINED_TYPE) {
				rec_def = recoverDefinedType(code, (CDefined_type)alternative, context);
			}
		} else {
			for (int i = 0; i < ln; i++) {
				Object [] myDataA = (Object [])((AEntity)sels).myData;
				alternative = (DataType)myDataA[i];
				if (alternative.express_type == DataType.DEFINED_TYPE) {
					rec_def = recoverDefinedType(code, (CDefined_type)alternative, context);
					if (rec_def != null) {
						break;
					}
				}
			}
		}
		if (rec_def != null && first) {
			StaticFields staticFields = StaticFields.get();
			if (staticFields.route == null) {
				staticFields.route = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
			} else if (staticFields.def_count >= staticFields.route.length) {
				enlargeRoute(staticFields);
			}
			staticFields.route[staticFields.def_count] = rec_def;
			staticFields.def_count++;
		}
		return rec_def;
	}


	private CDefined_type recoverDefinedType(int code, CDefined_type def, SdaiContext context) throws SdaiException {
		DataType domain = (DataType)def.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type >= DataType.ENUMERATION && domain.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			if (code == PhFileReader.ENUM) {
				return def;
			}
		} else if (domain.express_type >= DataType.LIST && domain.express_type <= DataType.AGGREGATE) {
			if (code == PhFileReader.EMBEDDED_LIST) {
				return def;
			}
		} else if (domain.express_type >= DataType.SELECT && domain.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			CDefined_type rec_def = recoverTyped(code, (SelectType)domain, false, context);
			if (rec_def != null) {
				return rec_def;
			}
		} else {
			switch(domain.express_type) {
				case DataType.INTEGER:
					if (code == PhFileReader.INTEGER) {
						return def;
					}
					break;
				case DataType.REAL:
				case DataType.NUMBER:
					if (code == PhFileReader.REAL) {
						return def;
					} else if (code == PhFileReader.INTEGER) {
						code = PhFileReader.REAL;
						tag = PhFileReader.REAL;
						real = integer;
						return def;
					}
					break;
				case DataType.STRING:
					if (code == PhFileReader.STRING) {
						return def;
					}
					break;
				case DataType.LOGICAL:
					if (code == PhFileReader.LOGICAL) {
						return def;
					}
					break;
				case DataType.BOOLEAN:
					if (code == PhFileReader.LOGICAL) {
						return def;
					}
					break;
				case DataType.BINARY:
					if (code == PhFileReader.BINARY) {
						return def;
					}
					break;
				case DataType.ENTITY:
					if (code == PhFileReader.ENTITY_REFERENCE || code == PhFileReader.ENTITY_REFERENCE_SPECIAL) {
						return def;
					}
					break;
			}
		}
		return null;
	}


	private Aggregate findTypedAggregate(StaticFields staticFields, CDefined_type def_type, SelectType sel, Value root, CEntity inst) 
			throws SdaiException {
		DataType domain = (DataType)def_type.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		AggregationType aggr_type;
if (SdaiSession.debug2) System.out.println("  Value  domain type: " + domain.getClass().getName() +
"   inst: #" + inst.instance_identifier);
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			if (domain.express_type >= DataType.SELECT && domain.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				aggr_type = recoverTypedAggregate(staticFields, (SelectType)domain, inst.owning_model.repository.session.sdai_context);
			} else {
				aggr_type = null;
			}
			if (aggr_type == null) {
				if (inst != null) {
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BVAT, 
						inst.instance_identifier, staticFields.current_attribute);
				}
//				if (staticFields.importing) {
//					unset_wrong_references(staticFields.c_instance);
//				}
				return null;
			} else {
				root.sel_number = sel.giveSelectNumber(staticFields.route, staticFields.def_count);
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_TPRE, 
					inst.instance_identifier, staticFields.current_attribute);
			}
		} else {
			aggr_type = (AggregationType)domain;
		}
		Aggregate aggr;
		try {
			if (aggr_type.shift == aggr_type.ADOUBLE3_AGGR && length > 3) {
				aggr = (Aggregate)A_double.class.newInstance();
				inst.owning_model.repository.session.a_double3_overflow = true;
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_ELEX, 
					inst.instance_identifier, staticFields.current_attribute);
			} else {
				aggr = (Aggregate)aggr_type.getAggregateClass().newInstance();
			}
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		boolean mixed;
		SelectType select = aggr_type.select;
		if (select != null && select.is_mixed > 0) {
			mixed = true;
		} else {
			mixed = false;
		}
		if (aggr != null) {
			if (aggr instanceof CAggregate) {
				((CAggregate)aggr).setValue(aggr_type, inst, this, mixed, true);
			} else {
				((A_primitive)aggr).setValue(aggr_type, inst, this, mixed, true);
			}
		}
		return aggr;
	}
	private AggregationType recoverTypedAggregate(StaticFields staticFields, SelectType sel, SdaiContext context) throws SdaiException {
		ANamed_type sels = ((ESelect_type)sel).getSelections(null, context);
		if (((AEntity)sels).myLength < 0) {
			((AEntity)sels).resolveAllConnectors();
		}
		AggregationType aggr_type = null;
		CDefined_type rec_def = null;
		DataType domain;
		int ln = ((AEntity)sels).myLength;
		if (ln == 1) {
			domain = (DataType)((AEntity)sels).myData;
			if (domain.express_type == DataType.DEFINED_TYPE) {
				rec_def = (CDefined_type)domain;
				while (domain.express_type == DataType.DEFINED_TYPE) {
					domain = (DataType)((CDefined_type)domain).getDomain(null);
				}
				if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
					return null;
				}
				aggr_type = (AggregationType)domain;
			}
		} else {
			for (int i = 0; i < ln; i++) {
				Object [] myDataA = (Object [])((AEntity)sels).myData;
				domain = (DataType)myDataA[i];
				if (domain.express_type == DataType.DEFINED_TYPE) {
					rec_def = (CDefined_type)domain;
					while (domain.express_type == DataType.DEFINED_TYPE) {
						domain = (DataType)((CDefined_type)domain).getDomain(null);
					}
					if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
						continue;
					}
					aggr_type = (AggregationType)domain;
					break;
				}
			}
		}
		if (aggr_type != null) {
			if (staticFields.route == null) {
				staticFields.route = new CDefined_type[SdaiSession.NUMBER_OF_DEFINED_TYPES];
			} else if (staticFields.def_count >= staticFields.route.length) {
				enlargeRoute(staticFields);
			}
			staticFields.route[staticFields.def_count] = rec_def;
			staticFields.def_count++;
		}
		return aggr_type;
	}


	Object getAggregateMixedValue(CDefined_type def, CEntity inst) 
			throws SdaiException {
		DataType dom = (DataType)def;
		while (dom.express_type == DataType.DEFINED_TYPE) {
			dom = (DataType)((CDefined_type)dom).getDomain(null);
		}
		if (dom.express_type < DataType.SELECT || dom.express_type > DataType.ENT_EXT_EXT_SELECT) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		StaticFields staticFields = StaticFields.get();
		staticFields.def_count = 0;
		sel_number = -1;
		return getMixedInternal(staticFields, null, def, inst, (SelectType)dom, this, true);
	}


	Object getValue(CEntity inst, CDefined_type def, boolean inverse) throws SdaiException {
		Object value_returned = null;
		switch (tag) {
			case PhFileReader.ENTITY_REFERENCE:
					if (inst != null && inverse && reference instanceof CEntity) {
						CEntity ref = (CEntity)reference;
						if (!(ref.owning_model != null && ref.owning_model.optimized)) {
							inst.addToInverseList(ref);
						}
					}
					return reference;
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
					CLateBindingEntity late_ref = (CLateBindingEntity)reference;
					if (late_ref != null) {
						late_ref.inverseAdd(inst);
					}
					return late_ref;
//					return reference;
			case INDETERMINATE:
					return null;
			case PhFileReader.REDEFINE:
					return null;
			case PhFileReader.INTEGER:
					value_returned = new Integer(integer);
					break;
			case PhFileReader.REAL:
					value_returned = new Double(real);
					break;
			case PhFileReader.LOGICAL:
					value_returned = new Integer(integer + 1);
					break;
			case PhFileReader.STRING:
					value_returned = string;
					break;
			case PhFileReader.BINARY:
					value_returned = (Binary)reference;
					break;
			case PhFileReader.ENUM:
					int res = findEnumeration(string, def, inst);
					if (res > 0) {
						value_returned = new Integer(res);
					} else {
						String message;
						if (res == 0) {
							message = AdditionalMessages.RD_BEN1;
						} else {
							message = AdditionalMessages.RD_BEN2;
						}
						if (inst != null) {
							StaticFields staticFields = StaticFields.get();
							EntityValue.printWarningToLogo(inst.owning_model.repository.session, message, inst.instance_identifier, 
								staticFields.current_attribute);
						}
						return null;
					}
					break;
		}
		return value_returned;
	}


	A_integer getIntegerAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_integer aggr = new A_integer(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int int_value = nested_values[i].getInteger();
			if ((aggr_type.express_type != DataType.ARRAY) && int_value == Integer.MIN_VALUE) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(int_value, count, Integer.MIN_VALUE, inst);
								break;
					case 1:  aggr.addAtTheEnd(int_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of integer values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of integers described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_integer getIntegerAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_INTEGER_AGGR;
			}
		}
		return getIntegerAggregate();
	}


/**
 * Returns an aggregate of integer values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of integers described by this class.
 */
	public A_integer getIntegerAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_integer aggr = new A_integer(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int int_value = nested_values[i].getInteger();
			if ((a_type.express_type != DataType.ARRAY) && int_value == Integer.MIN_VALUE) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(int_value, count, Integer.MIN_VALUE, inst);
								break;
					case 1:  aggr.addAtTheEnd(int_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	A_double getDoubleAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_double aggr;
		boolean aggr_class = aggr_type.check_aggregation_double();
		if (aggr_class && length > 3) {
			aggr_class = false;
			inst.owning_model.repository.session.a_double3_overflow = true;
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_ELEX, 
				inst.instance_identifier, attr);
		}
		if (aggr_class) {
//		if (aggr_type.check_aggregation_double()) {
			aggr = new A_double3(aggr_type, inst);
			((A_double3)aggr).setValueSimple(inst, this, true);
		} else {
			aggr = new A_double(aggr_type, inst);
			int type;
			if (aggr_type.express_type == DataType.LIST) {
				type = 1;
			} else {
				type = 0;
			}
			int count = 0;
			for (int i = 0; i < length; i++) {
				double double_value = nested_values[i].getDouble();
				if ((aggr_type.express_type != DataType.ARRAY) && Double.isNaN(double_value)) {
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, attr);
				} else {
					switch (type) {
						case 0:  aggr.setForNonList(double_value, count, inst);
									break;
						case 1:  aggr.addAtTheEnd(double_value);
									break;
					}
					count++;
				}
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of real values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of reals described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_double getDoubleAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_DOUBLE_AGGR;
			}
		}
		return getDoubleAggregate();
	}


/**
 * Returns an aggregate of real values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of reals described by this class.
 */
	public A_double getDoubleAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_double aggr;
		boolean aggr_class = a_type.check_aggregation_double();
		if (aggr_class && length > 3) {
			aggr_class = false;
			if (inst != null) {
				inst.owning_model.repository.session.a_double3_overflow = true;
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_ELEX, 
					inst.instance_identifier);
			} else {
				SdaiSession.getSession().a_double3_overflow = true;
			}
		}
		if (aggr_class) {
//		if (a_type.check_aggregation_double()) {
			aggr = new A_double3(a_type, inst);
			((A_double3)aggr).setValueSimple(inst, this, false);
		} else {
			aggr = new A_double(a_type, inst);
			int type;
			if (a_type.express_type == DataType.LIST) {
				type = 1;
			} else {
				type = 0;
			}
			int count = 0;
			for (int i = 0; i < length; i++) {
				double double_value = nested_values[i].getDouble();
				if ((a_type.express_type != DataType.ARRAY) && Double.isNaN(double_value)) {
					throw new SdaiException(SdaiException.AI_NVLD);
				} else {
					switch (type) {
						case 0:  aggr.setForNonList(double_value, count, inst);
									break;
						case 1:  aggr.addAtTheEnd(double_value);
									break;
					}
					count++;
				}
			}
		}
		return aggr;
	}


	A_string getStringAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
if (SdaiSession.debug2) System.out.println("  inst ident = " + inst.instance_identifier);
if (SdaiSession.debug2) System.out.println("  attr inst ident = " + attr.instance_identifier);
if (SdaiSession.debug2) System.out.println("  domain inst ident = " + 
((CEntity)((CExplicit_attribute)attr).getDomain(null)).instance_identifier);
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_string aggr = new A_string(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			String str = nested_values[i].getString();
			if ((aggr_type.express_type != DataType.ARRAY) && str == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(str, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(str, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of string values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of strings described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_string getStringAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_STRING_AGGR;
			}
		}
		return getStringAggregate();
	}


/**
 * Returns an aggregate of string values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of strings described by this class.
 */
	public A_string getStringAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_string aggr = new A_string(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			String str = nested_values[i].getString();
			if ((a_type.express_type != DataType.ARRAY) && str == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(str, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(str, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	A_enumeration getLogicalAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_enumeration aggr = new A_enumeration(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int log_value = nested_values[i].getLogical();
			if ((aggr_type.express_type != DataType.ARRAY) && log_value == 0) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(log_value, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(log_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of logical values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of logical values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_enumeration getLogicalAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_LOGICAL_AGGR;
			}
		}
		return getLogicalAggregate();
	}


/**
 * Returns an aggregate of logical values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of logical values described by this class.
 */
	public A_enumeration getLogicalAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_enumeration aggr = new A_enumeration(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int log_value = nested_values[i].getLogical();
			if ((a_type.express_type != DataType.ARRAY) && log_value == 0) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(log_value, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(log_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	A_boolean getBooleanAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_boolean aggr = new A_boolean(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int res = nested_values[i].getBoolean();
			if ((aggr_type.express_type != DataType.ARRAY) && res == 0) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(res, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(res);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of boolean values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of boolean values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_boolean getBooleanAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_BOOLEAN_AGGR;
			}
		}
		return getBooleanAggregate();
	}


/**
 * Returns an aggregate of boolean values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of boolean values described by this class.
 */
	public A_boolean getBooleanAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_boolean aggr = new A_boolean(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int res = nested_values[i].getBoolean();
			if ((a_type.express_type != DataType.ARRAY) && res == 0) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(res, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(res);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	A_enumeration getEnumerationAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_enumeration aggr = new A_enumeration(aggr_type, inst);
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type < DataType.ENUMERATION || el_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		SdaiContext context = null;
		if (el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			context = inst.owning_model.repository.session.sdai_context;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int enum_val = nested_values[i].getEnumeration((EEnumeration_type)el_type, null, 
				context, inst.owning_model.repository.session);
			if ((aggr_type.express_type != DataType.ARRAY) && enum_val == 0) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(enum_val, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(enum_val);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of enumeration values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of enumeration values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_enumeration getEnumerationAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_ENUM_AGGR;
			}
		}
		return getEnumerationAggregate();
	}


/**
 * Returns an aggregate of enumeration values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of enumeration values described by this class.
 */
	public A_enumeration getEnumerationAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_enumeration aggr = new A_enumeration(a_type, inst);
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type < DataType.ENUMERATION || el_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		SdaiContext context = null;
		if (inst != null && 
				(el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) ) {
			context = inst.owning_model.repository.session.sdai_context;
		}
		SdaiSession session = null;
		if (inst != null) {
			session = inst.owning_model.repository.session;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int enum_val = nested_values[i].getEnumeration((EEnumeration_type)el_type, null, 
				context, session);
			if ((a_type.express_type != DataType.ARRAY) && enum_val == 0) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(enum_val, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(enum_val);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	A_binary getBinaryAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		A_binary aggr = new A_binary(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			Binary binar = nested_values[i].getBinary();
			if ((aggr_type.express_type != DataType.ARRAY) && binar == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(binar, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(binar, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns an aggregate of binary values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of binary values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public A_binary getBinaryAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_BINARY_AGGR;
			}
		}
		return getBinaryAggregate();
	}


/**
 * Returns an aggregate of binary values wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of binary values described by this class.
 */
	public A_binary getBinaryAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		A_binary aggr = new A_binary(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			Binary binar = nested_values[i].getBinary();
			if ((a_type.express_type != DataType.ARRAY) && binar == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(binar, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(binar, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/* 
	Returns an aggregate of entity instances that is a value of an attribute 
	of the simple entity data type represented by an instance of class 
	EntityValue.
*/
	Aggregate getInstanceAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		CAggregate aggr = getInstanceAggregateInternal(attr, inst);
		if (aggr != null) {
			aggr.setValue((AggregationType)d_type, inst, this, false, true);
		}
		return aggr;
	}


/* 
	Returns an empty aggregate prepared to contain entity instances which 
	later is used as a value of an attribute of the simple entity data type 
	represented by an instance of class EntityValue. The elements are 
	assigned/added to the aggregate using <code>CAggregate</code> method 
	<code>setValue</code> (see <code>getInstanceAggregate</code> where the 
	current method is invoked).
*/
	private CAggregate getInstanceAggregateInternal(CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
//System.out.println(" EntityValue  count = " + count + "   instance: #" + inst.instance_identifier + 
//"   model: " + inst.owning_model.name);
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			StaticFields staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		if (inst.owning_model == SdaiSession.baseDictionaryModel && attr == null) {
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
//System.out.println(" !!  domain = " + domain.getClass().getName());
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		d_type = aggr_type;
if (SdaiSession.debug2) System.out.println("  inst ident = " + inst.instance_identifier);
if (SdaiSession.debug2) System.out.println("  attr inst ident = " + attr.instance_identifier);
if (SdaiSession.debug2) System.out.println("  aggr_type inst ident = " + aggr_type.instance_identifier);

if (SdaiSession.debug2) if (aggr_type == null) System.out.println("  returned NULL aggr_type" );
else System.out.println("  returned POSITIVE aggr_type");
if (SdaiSession.debug2) if (aggr_type.getAggregateClass() == null) System.out.println("  returned NULL aggr_type.aggregateClass" );
else System.out.println("  returned POSITIVE aggr_type.aggregateClass");
		CAggregate aggr;
		try {
			aggr = (CAggregate)aggr_type.getAggregateClass().newInstance();
//System.out.println("    AGGREGATE: " + aggr_type.getAggregateClass().getName());
//System.out.println(" In EntityValue aggr_type inst ident = " + aggr_type.instance_identifier);
//System.out.println(" In EntityValue aggregation_type inst ident = " + aggregation_type.instance_identifier);
//if (aggr_type.aggMemberImplClass == null) System.out.println("  In EntityValue aggr_type.aggMemberImplClass is NULL");
//else {
//System.out.println(" In EntityValue aggr_type.aggMemberImplClass is POS");
//System.out.println("    AGGREGATE MEMBER: " + aggr_type.aggMemberImplClass.getName());}
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return aggr;
	}


/**
 * Returns an aggregate of entity instances that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of entity instances described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aggregate getInstanceAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_INST_AGGR;
			}
		}
		return getInstanceAggregate();
	}


/**
 * Returns an aggregate of entity instances wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return aggregate of entity instances described by this class.
 */
	public Aggregate getInstanceAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		AEntity aggr;
		CEntity inst = null;
//		AggregationType a_type = (AggregationType)d_type;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else if (a_type != null) {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		try {
			if (a_type != null) {
				if (a_type == ExpressTypes.LIST_GENERIC_TYPE || a_type == ExpressTypes.SET_GENERIC_TYPE || 
						a_type == ExpressTypes.BAG_GENERIC_TYPE || a_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
					if (remedy_a_type()) {
						a_type = (AggregationType)d_type;
						aggr = (AEntity)a_type.getAggregateClass().newInstance();
					} else {
						aggr = new AEntity();
					}
					aggr.myType = a_type;
				} else if (a_type == ExpressTypes.SET_STRING_TYPE) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
					aggr = (AEntity)a_type.getAggregateClass().newInstance();
				}
			} else {
				aggr = new AEntity();
			}
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (aggr != null) {
			aggr.setValue(a_type, inst, this, false, false);
		}
		return aggr;
	}


	private boolean remedy_a_type() throws SdaiException {
		AggregationType ag_type = (AggregationType)d_type;
		if (ag_type == null || ag_type == ExpressTypes.LIST_GENERIC_TYPE || ag_type == ExpressTypes.SET_GENERIC_TYPE || 
						ag_type == ExpressTypes.BAG_GENERIC_TYPE || ag_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			return false;
		}
		EData_type el_type = ((EAggregation_type)d_type).getElement_type(null);
		for (int i = 0; i < length; i++) {
			boolean res = nested_values[i].isConforming(el_type);
			if (!res) {
				return res;
			}
		}
		if (agg_owner instanceof CEntity) {
			ag_type.shift = SdaiSession.EXPRESSIONS_INST_AGGR;
		} else {
			ag_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		return true;
	}


	private boolean isConforming(EData_type dtp) throws SdaiException {
		if (v_type == null || dtp == null) {
			return false;
		}
		DataType tp = (DataType)dtp;
		if (tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) {
			return isConformingSimple((DataType)v_type, tp);
		} else if (tp.express_type == DataType.ENTITY) {
			return isConformingEntityDef((DataType)v_type, tp);
		} else if (tp.express_type == DataType.DATA_TYPE) {
			if (tp.getName(null).equals("_ENTITY")) {
				DataType val_tp = (DataType)v_type;
				while (val_tp.express_type == DataType.DEFINED_TYPE) {
					val_tp = (DataType)((CDefined_type)val_tp).getDomain(null);
				}
				if (val_tp.express_type == DataType.ENTITY) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			if (d_type == dtp || v_type == dtp) {
				return true;
			} else {
				return false;
			}
		}
	}
	private boolean isConformingSimple(DataType val_tp, DataType tp) throws SdaiException {
		while (val_tp.express_type == DataType.DEFINED_TYPE) {
			val_tp = (DataType)((CDefined_type)val_tp).getDomain(null);
		}
		if (val_tp.express_type < DataType.NUMBER || val_tp.express_type > DataType.BINARY) {
			return false;
		}
		switch (tp.express_type) {
			case DataType.NUMBER:
				if (val_tp.express_type >= DataType.NUMBER && val_tp.express_type <= DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.INTEGER:
				if (val_tp.express_type == DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.REAL:
				if (val_tp.express_type == DataType.REAL || val_tp.express_type == DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.BOOLEAN:
				if (val_tp.express_type == DataType.BOOLEAN) {
					return true;
				}
				break;
			case DataType.LOGICAL:
				if (val_tp.express_type == DataType.LOGICAL || val_tp.express_type == DataType.BOOLEAN) {
					return true;
				}
				break;
			case DataType.BINARY:
				if (val_tp.express_type == DataType.BINARY) {
					return true;
				}
				break;
			case DataType.STRING:
				if (val_tp.express_type == DataType.STRING) {
					return true;
				}
				break;
		}
		return false;
	}
	private boolean isConformingEntityDef(DataType val_tp, DataType tp) throws SdaiException {
		while (val_tp.express_type == DataType.DEFINED_TYPE) {
			val_tp = (DataType)((CDefined_type)val_tp).getDomain(null);
		}
		if (val_tp.express_type == DataType.ENTITY && 
				((EEntity_definition)val_tp).isSubtypeOf((EEntity_definition)tp)) {
			return true;
		}
		return false;
	}


/* 
	Returns a general aggregate that is a value of an attribute of the simple 
	entity data type represented by an instance of class EntityValue. 
	This method is applied when the aggregate members are of the select type. 
*/
	Aggregate getMixedAggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		CAggregate aggr = getInstanceAggregateInternal(attr, inst);
if (SdaiSession.debug2) System.out.println("  OWNING inst ident = " + inst.instance_identifier +
"   inst name: " + ((CEntity_definition)inst.getInstanceType()).getCorrectName());
if (SdaiSession.debug2) System.out.println("  attr inst ident = " + attr.instance_identifier);
if (SdaiSession.debug2) System.out.println("  attr name = " + attr.getName(null));
		if (aggr != null) {
if (SdaiSession.debug2) System.out.println(" In getMixedAggregate aggregation_type inst ident = " + ((CEntity)d_type).instance_identifier);
			aggr.setValue((AggregationType)d_type, inst, this, true, true);
		}
		return aggr;
	}


/**
 * Returns an aggregate of values of select data type that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return aggregate of values of select data type described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aggregate getMixedAggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_MIXED_AGGR;
			}
		}
		return getMixedAggregate();
	}


/**
 * Returns an aggregate of values of select data type wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return aggregate of values of select data type described by this class.
 */
	public Aggregate getMixedAggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CAggregate aggr;
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else if (a_type != null) {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		try {
			if (a_type != null) {
				if (a_type == ExpressTypes.LIST_GENERIC_TYPE || a_type == ExpressTypes.SET_GENERIC_TYPE || 
						a_type == ExpressTypes.BAG_GENERIC_TYPE || a_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
					if (remedy_mixed_type()) {
						a_type = (AggregationType)d_type;
						aggr = (CAggregate)a_type.getAggregateClass().newInstance();
					} else {
						aggr = new CAggregate(a_type);
					}
				} else if (a_type == ExpressTypes.SET_STRING_TYPE) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
					aggr = (CAggregate)a_type.getAggregateClass().newInstance();
				}
			} else {
				aggr = new CAggregate();
			}
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (aggr != null) {
			aggr.setValue(a_type, inst, this, true, false);
		}
		return aggr;
	}


	private boolean remedy_mixed_type() throws SdaiException {
		AggregationType ag_type = (AggregationType)d_type;
		if (ag_type == null || ag_type == ExpressTypes.LIST_GENERIC_TYPE || ag_type == ExpressTypes.SET_GENERIC_TYPE || 
						ag_type == ExpressTypes.BAG_GENERIC_TYPE || ag_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			return false;
		}
		EData_type el_type = ((EAggregation_type)d_type).getElement_type(null);
		for (int i = 0; i < length; i++) {
			boolean res = nested_values[i].isMixedConforming(el_type);
			if (!res) {
				return res;
			}
		}
		if (agg_owner instanceof CEntity) {
			ag_type.shift = SdaiSession.EXPRESSIONS_MIXED_AGGR;
		} else {
			ag_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		return true;
	}


	private boolean isMixedConforming(EData_type dtp) throws SdaiException {
		if (v_type == null || dtp == null) {
			return false;
		}
		DataType tp = (DataType)dtp;
		if ((tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) || tp.express_type == DataType.ENTITY) {
			return false;
		} else if (tp.express_type == DataType.DATA_TYPE) {
			if (tp.getName(null).equals("_ENTITY")) {
				DataType val_tp = (DataType)v_type;
				while (val_tp.express_type == DataType.DEFINED_TYPE) {
					val_tp = (DataType)((CDefined_type)val_tp).getDomain(null);
				}
				if (val_tp.express_type == DataType.ENTITY) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			if (d_type == dtp || v_type == dtp) {
				return true;
			} else {
				return false;
			}
		}
	}


	Aa_integer getInteger2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aa_integer aggr = new Aa_integer(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_integer for_adding = nested_values[i].getIntegerAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a double-nesting aggregate of integer values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of integer values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_integer getInteger2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_INTEGER2_AGGR;
			}
		}
		return getInteger2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of integer values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of integer values described by this class.
 */
	public Aa_integer getInteger2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aa_integer aggr = new Aa_integer(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_integer for_adding = nested_values[i].getIntegerAggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	Aa_double getDouble2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		return prepareDouble2Aggregate((AggregationType)domain, inst);
	}


/**
 * Returns a double-nesting aggregate of real values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of real values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_double getDouble2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_DOUBLE2_AGGR;
			}
		}
		return getDouble2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of real values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of real values described by this class.
 */
	public Aa_double getDouble2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		return prepareDouble2Aggregate(a_type, inst);
	}


	Aa_string getString2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aa_string aggr = new Aa_string(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_string for_adding = nested_values[i].getStringAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a double-nesting aggregate of string values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of string values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_string getString2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_STRING2_AGGR;
			}
		}
		return getString2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of string values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of string values described by this class.
 */
	public Aa_string getString2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aa_string aggr = new Aa_string(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_string for_adding = nested_values[i].getStringAggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	Aa_integer getLogical2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aa_integer aggr = new Aa_integer(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_integer for_adding = nested_values[i].getLogicalAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a double-nesting aggregate of logical values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of logical values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_integer getLogical2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_LOGICAL2_AGGR;
			}
		}
		return getLogical2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of logical values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of logical values described by this class.
 */
	public Aa_integer getLogical2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aa_integer aggr = new Aa_integer(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_integer for_adding = nested_values[i].getLogicalAggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	Aa_boolean getBoolean2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aa_boolean aggr = new Aa_boolean(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_boolean for_adding = nested_values[i].getBooleanAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a double-nesting aggregate of boolean values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of boolean values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_boolean getBoolean2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_BOOLEAN2_AGGR;
			}
		}
		return getBoolean2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of boolean values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of boolean values described by this class.
 */
	public Aa_boolean getBoolean2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aa_boolean aggr = new Aa_boolean(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_boolean for_adding = nested_values[i].getBooleanAggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	Aa_enumeration getEnumeration2Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aa_enumeration aggr = new Aa_enumeration(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_enumeration for_adding = nested_values[i].getEnumerationAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a double-nesting aggregate of enumeration values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return double-nesting aggregate of enumeration values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aa_enumeration getEnumeration2Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_ENUM2_AGGR;
			}
		}
		return getEnumeration2Aggregate();
	}


/**
 * Returns a double-nesting aggregate of enumeration values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return double-nesting aggregate of enumeration values described by this class.
 */
	public Aa_enumeration getEnumeration2Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aa_enumeration aggr = new Aa_enumeration(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_enumeration for_adding = nested_values[i].getEnumerationAggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_integer getIntegerAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_integer aggr = new A_integer(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int int_value = nested_values[i].getInteger();
			if ((aggr_type.express_type != DataType.ARRAY) && int_value == Integer.MIN_VALUE) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(int_value, count, Integer.MIN_VALUE, inst);
								break;
					case 1:  aggr.addAtTheEnd(int_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_double getDoubleAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_double aggr = new A_double(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			double double_value = nested_values[i].getDouble();
			if ((aggr_type.express_type != DataType.ARRAY) && Double.isNaN(double_value)) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(double_value, count, inst);
								break;
					case 1:  aggr.addAtTheEnd(double_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_string getStringAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_string aggr = new A_string(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			String str = nested_values[i].getString();
			if ((aggr_type.express_type != DataType.ARRAY) && str == null) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(str, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(str, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_integer getLogicalAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_integer aggr = new A_integer(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int log_value = nested_values[i].getLogical();
			if ((aggr_type.express_type != DataType.ARRAY) && log_value == 0) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(log_value, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(log_value);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_boolean getBooleanAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_boolean aggr = new A_boolean(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int res = nested_values[i].getBoolean();
			if ((aggr_type.express_type != DataType.ARRAY) && res == 0) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(res, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(res);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private A_enumeration getEnumerationAggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		StaticFields staticFields;
		if (tag != PhFileReader.EMBEDDED_LIST) {
			staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		A_enumeration aggr = new A_enumeration(aggr_type, inst);
		DataType element_type = (DataType)aggr_type.getElement_type(null);
		if (element_type.express_type < DataType.ENUMERATION || element_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		SdaiContext context = null;
		if (inst != null && 
				(element_type.express_type == DataType.EXTENSIBLE_ENUM || element_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM)) {
			context = inst.owning_model.repository.session.sdai_context;
		}
		SdaiSession session = null;
		if (inst != null) {
			session = inst.owning_model.repository.session;
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			int enum_val = nested_values[i].getEnumeration((EEnumeration_type)element_type, null, context, session);
			if ((aggr_type.express_type != DataType.ARRAY) && enum_val == 0) {
				if (inst != null) {
					staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(enum_val, count, 0, inst);
								break;
					case 1:  aggr.addAtTheEnd(enum_val);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	Aggregate getInstanceAggregate(int index, AggregationType aggr_type, 
			CEntity inst, boolean mixed) throws SdaiException {
		Value val = nested_values[index];
		if (val.tag == INDETERMINATE) {
			return null;
		}
		if (val.tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		CAggregate aggr;
		try {
//if (aggr_type == null) System.out.println("  aggr_type is NULL");
//else System.out.println("  aggr_type is POS");
//if (aggr_type.aggMemberImplClass == null) System.out.println("  aggr_type.aggMemberImplClass is NULL");
//else System.out.println("  aggr_type.aggMemberImplClass is POS");
			aggr = (CAggregate)aggr_type.getAggregateClass().newInstance();
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		aggr.setValue(aggr_type, inst, val, mixed, true);
		return aggr;
	}


	Aaa_double getDouble3Aggregate(CExplicit_attribute attr, CEntity inst) throws SdaiException {
		StaticFields staticFields;
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BAGG, 
				inst.instance_identifier, attr);
//			staticFields = StaticFields.get();
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.LIST || domain.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		AggregationType aggr_type = (AggregationType)domain;
		Aaa_double aggr = new Aaa_double(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		int count = 0;
		for (int i = 0; i < length; i++) {
			Aa_double for_adding = nested_values[i].getDouble2Aggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, attr);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


/**
 * Returns a triple-nesting aggregate of real values that is wrapped in an object of this class.
 * In the case of unset, null is returned.
 * <p> This method is used when dealing with Express expressions. 
 *	It is invoked in Express compiler generated classes.
 * @param owner the entity instance owning the returned aggregate.
 * @return triple-nesting aggregate of real values described by this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 3.6.0
 */
	public Aaa_double getDouble3Aggregate(EEntity owner) throws SdaiException {
		if (owner != null) {
			agg_owner = (CEntity)owner;
			AggregationType a_type = (AggregationType)v_type;
			if (a_type != null) {
				a_type.shift = SdaiSession.EXPRESSIONS_DOUBLE3_AGGR;
			}
		}
		return getDouble3Aggregate();
	}


/**
 * Returns a triple-nesting aggregate of real values wrapped in an object 
 * of this class. In the case of unset, null is returned.
 * @return triple-nesting aggregate of real values described by this class.
 */
	public Aaa_double getDouble3Aggregate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		CEntity inst = null;
		AggregationType a_type = (AggregationType)v_type;
		if (agg_owner instanceof CEntity) {
			inst = (CEntity)agg_owner;
		} else {
			a_type.shift = SdaiSession.PRIVATE_AGGR;
		}
		Aaa_double aggr = new Aaa_double(a_type, inst);
		int type;
		if (a_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)a_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			Aa_double for_adding = nested_values[i].getDouble2Aggregate(next_type, inst);
			if ((a_type.express_type != DataType.ARRAY) && for_adding == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}


	private Aa_double getDouble2Aggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		if (tag == INDETERMINATE) {
			return null;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			StaticFields staticFields = StaticFields.get();
			if (inst != null) {
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_BNAG, 
					inst.instance_identifier, staticFields.current_attribute);
			}
//			if (staticFields.importing) {
//				unset_wrong_references(staticFields.c_instance);
//			}
			return null;
		}
		return prepareDouble2Aggregate(aggr_type, inst);
	}


	private Aa_double prepareDouble2Aggregate(AggregationType aggr_type, CEntity inst) 
			throws SdaiException {
		Aa_double aggr = new Aa_double(aggr_type, inst);
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			next_type = (AggregationType)ExpressTypes.LIST_GENERIC_TYPE;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int count = 0;
		for (int i = 0; i < length; i++) {
			A_double for_adding = nested_values[i].getDoubleAggregate(next_type, inst);
			if ((aggr_type.express_type != DataType.ARRAY) && for_adding == null) {
				if (inst != null) {
					StaticFields staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				} else {
//print some message
				}
			} else {
				switch (type) {
					case 0:  aggr.setForNonList(for_adding, count, null, inst);
								break;
					case 1:  aggr.addAtTheEnd(for_adding, null);
								break;
				}
				count++;
			}
		}
		return aggr;
	}



//--------------------------------  set methods  ------------------------------
/* 
	Stores an integer encoding an enumeration item to this instance of 
	<code>Value</code>. 
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	void setEnumeration(int value, CExplicit_attribute attr) throws SdaiException {
		DataType dt = (DataType)attr.getDomain(null);
		while (dt.express_type == DataType.DEFINED_TYPE) {
			v_type = dt;
			dt = (DataType)((CDefined_type)dt).getDomain(null);
		}
		if (dt.express_type < DataType.ENUMERATION || dt.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EEnumeration_type et = (EEnumeration_type)dt;
		A_string ee = et.getElements(null, null);
		if ((value > ee.myLength) || (value < 1)) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.ENUM;
			string = ((String)ee.getByIndex(value)).toUpperCase();
		}
//		v_type = et;
	}


	void setTyped(Object value, int tag, CDefined_type [] path, int sel_number) throws SdaiException {
		SelectType select = null;
		if (aux < 0) {
			select = (SelectType)v_type;
		}

		Value val = this;
		for (int i = 0; i < path.length; i++) {
			val.tag = PhFileReader.TYPED_PARAMETER;
			String name = path[i].getName(null);
			val.string = name.toUpperCase();
			if (val.nested_values == null) {
				val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			}
			if (val.nested_values[0] == null) {
				val.nested_values[0] = new Value();
			}

			if (aux < 0) {
				val.v_type = path[i];
				val.nested_values[0].d_type = path[i];
			}

			val = val.nested_values[0];
		}
		if (value == null) {
			val.tag = INDETERMINATE;
			return;
		}

		if (aux < 0) {
			val.v_type = (EData_type)select.types[sel_number];
		}

		val.tag = tag;
		switch (tag) {
		case PhFileReader.REAL: // or NUMBER
			if (value instanceof Double) {
				Double d = (Double)value;
				if (d.isNaN()) {
					val.tag = INDETERMINATE;
				} else {
					val.real = d.doubleValue();
				}
			} else if (value instanceof Integer) {
				int integ = ((Integer)value).intValue();
				if (integ == Integer.MIN_VALUE) {
					val.tag = INDETERMINATE;
				} else {
					val.real = integ;
				}
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			break;
		case PhFileReader.INTEGER:
			if (!(value instanceof Integer)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			int integ = ((Integer)value).intValue();
			if (integ == Integer.MIN_VALUE) {
				val.tag = INDETERMINATE;
			} else {
				val.integer = integ;
			}
			break;
		case PhFileReader.STRING:
			if (!(value instanceof String)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			val.string = (String)value;
			break;
		case PhFileReader.BINARY:
			if (!(value instanceof Binary)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			val.reference = value;
		case PhFileReader.BOOLEAN:
		case PhFileReader.LOGICAL:
			if (!(value instanceof Integer)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			integ = ((Integer)value).intValue();
			if (integ == 0) {
				val.tag = INDETERMINATE;
			} else {
				val.integer = integ - 1;
			}
			break;
		case PhFileReader.ENUM:
			if (!(value instanceof Integer)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			integ = ((Integer)value).intValue();
			String str_value;
			DataType dt = (DataType)path[path.length - 1];
			while (dt.express_type == DataType.DEFINED_TYPE) {
				dt = (DataType)((CDefined_type)dt).getDomain(null);
			}
			if (dt.express_type < DataType.ENUMERATION || dt.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			A_string ee = ((EEnumeration_type)dt).getElements(null, null);
			if (integ < 0 || integ > ee.myLength) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (integ == 0) {
				val.tag = INDETERMINATE;
			} else {
				val.tag = PhFileReader.ENUM;
				str_value = (String)ee.getByIndex(integ);
				val.string = str_value.toUpperCase();
			} 
			break;
		case PhFileReader.EMBEDDED_LIST:
			if (!(value instanceof Aggregate)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			val.reference = value;

			if (aux < 0) {
				val.aux = -1;
			}

			val.setTypedAggregate((Aggregate)value);

			if (aux < 0) {
				val.aux = 0;
			}

			break;
		}
	}


	private void setTypedAggregate(Aggregate agg) throws SdaiException {
		SelectType select = null;
		CAggregate cagg = null;
		boolean has_con = false;
		if (agg instanceof CAggregate) {
			cagg = (CAggregate)agg;
			select = cagg.myType.select;
			if (cagg.myLength < 0) {
				cagg.myLength = -cagg.myLength;
				has_con = true;
			}
		}
		if (select == null || select.is_mixed <= 0) {
			DataType el_type = (DataType)agg.getAggregationType().getElement_type(null);

			EData_type dt_type = null;
			if (aux < 0) {
				dt_type = (EData_type)el_type;
			}

			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}

			if (el_type.express_type >= DataType.NUMBER && el_type.express_type <= DataType.BINARY) {
				switch(el_type.express_type) {
					case DataType.INTEGER:
						if (!(agg instanceof A_integer)) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						setAggrInteger((A_integer)agg);
						break;
					case DataType.REAL:
					case DataType.NUMBER:
						if (!(agg instanceof A_double)) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						A_double a_double = (A_double)agg;
//						if (a_double.myType != null && a_double.myType.check_aggregation_double()) {
						if (a_double.myType != null && !a_double.myType.check_aggregation_double()) {
							setAggrDouble(a_double);
						} else {
							CEntity owner = a_double.getOwningInstance();
							if ((owner != null && !owner.owning_model.repository.session.a_double3_overflow && 
									!SdaiSession.getSession().a_double3_overflow) || a_double instanceof A_double3) {
								setAggrDouble((A_double3)a_double);
							} else {
								setAggrDouble(a_double);
							}
						}
						break;
					case DataType.STRING:
						if (!(agg instanceof A_string)) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						setAggrString((A_string)agg);
						break;
					case DataType.LOGICAL:
						if (!(agg instanceof A_enumeration)) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						setAggrLogical((A_enumeration)agg);
						break;
					case DataType.BOOLEAN:
						if (!(agg instanceof A_boolean)) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						setAggrBoolean((A_boolean)agg);
						break;
					case DataType.BINARY:
						throw new SdaiException(SdaiException.FN_NAVL);
				}
			} else if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				if (!(agg instanceof A_enumeration)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				A_string elements;
				if (el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
					CEntity owning_inst = ((A_enumeration)agg).getOwningInstance();
					if (owning_inst == null) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					SdaiModel owning_mod = owning_inst.owning_model;
					if (owning_mod == null) {
						throw new SdaiException(SdaiException.AI_NVLD);
					}
					elements = ((EExtensible_enumeration_type)el_type).getElements(null, owning_mod.repository.session.sdai_context);
				} else {
					elements = ((EEnumeration_type)el_type).getElements(null);
				}
				setAggrEnumeration((A_enumeration)agg, elements);
			} else if (el_type.express_type == DataType.ENTITY || 
							(el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT)) {
				setAggrElements((CAggregate)agg);
			} else if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				if (cagg == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (nested_values == null || cagg.myLength > nested_values.length) {
					enlarge_nested_values(cagg.myLength);
				}
				length = cagg.myLength;
				int i;
				Object [] myDataA;
				if (cagg.myType.express_type == DataType.LIST) {
					if (cagg.myData == null) {
						return;
					}
					if (length == 1) {
						set_typed_list_value(cagg.myData, 0, dt_type, (EData_type)el_type);
					} else if (length == 2) {
						myDataA = (Object [])cagg.myData;
						for (i = 0; i < 2; i++) {
							set_typed_list_value(myDataA[i], i, dt_type, (EData_type)el_type);
						}
					} else {
						ListElement element;
						if (length <= CAggregate.SHORT_AGGR) {
							element = (ListElement)cagg.myData;
						} else {
							myDataA = (Object [])cagg.myData;
							element = (ListElement)myDataA[0];
						}
						i = 0;
						while (element != null) {
							set_typed_list_value(element.object, i, dt_type, (EData_type)el_type);
							element = element.next;
							i++;
						}
					}
				} else {
					if (length == 1) {
						set_typed_set_value(cagg.myData, cagg, 0, dt_type, (EData_type)el_type);
					} else {
						myDataA = (Object [])cagg.myData;
						for (i = 0; i < length; i++) {
							if (cagg.myData == null) {
								set_typed_set_value(null, cagg, i, dt_type, (EData_type)el_type);
							} else {
								set_typed_set_value(myDataA[i], cagg, i, dt_type, (EData_type)el_type);
							}
						}
					}
				}
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		} else {
			setMixedAggrElements((CAggregate)agg);
		}
		if (has_con) {
			cagg.myLength = -cagg.myLength;
		}
	}
	void set_typed_list_value(Object member, int ind, EData_type dt_type, EData_type el_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof Aggregate)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = el_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setTypedAggregate((Aggregate)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_typed_set_value(Object member, CAggregate cagg, int ind, EData_type dt_type, EData_type el_type) 
			throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (cagg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
			if (aux < 0) {
				nested_values[ind].d_type = dt_type;
			}
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
				if (aux < 0) {
					nested_values[ind].d_type = dt_type;
				}
			} else {
				if (!(member instanceof Aggregate)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				if (aux < 0) {
					nested_values[ind].d_type = dt_type;
					nested_values[ind].v_type = el_type;
					nested_values[ind].aux = -1;
				}
				nested_values[ind].setTypedAggregate((Aggregate)member);
				if (aux < 0) {
					nested_values[ind].aux = 0;
				}
			}
		}
	}


	void setAggrInteger(A_integer agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		int member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.INTEGER) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			i = 0;
			ListElementInteger [] myDataList = (ListElementInteger [])agg.myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				member = element.value;
				if (member == Integer.MIN_VALUE) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[i].tag = PhFileReader.INTEGER;

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				nested_values[i].integer = member;
				element = element.next;
				i++;
			}
		} else {
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				int [] myDataArray = (int [])agg.myData;
				if (myDataArray == null) {
					nested_values[i].tag = INDETERMINATE;
				} else {
					member = myDataArray[i];
//System.out.println("   Value    myLength: " + length + "   agg.length: " + agg.myData.length);
//				if (!(member instanceof Integer)) {
//System.out.println("   Value    member type: " + member.getClass().getName());
//					throw new SdaiException(SdaiException.SY_ERR);
//				}
					if (member == Integer.MIN_VALUE) {
						nested_values[i].tag = INDETERMINATE;
					} else {
						nested_values[i].tag = PhFileReader.INTEGER;
						nested_values[i].integer = member;
					}
				}
			}
		}
	}


	void setAggrDouble(A_double agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		double member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.REAL || el_type.express_type == DataType.NUMBER || 
					el_type.express_type == DataType.INTEGER) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			i = 0;
			ListElementDouble [] myDataList = (ListElementDouble [])agg.myData;
			ListElementDouble element = myDataList[0];
			while (element != null) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				member = element.value;
				if (Double.isNaN(member)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[i].tag = PhFileReader.REAL;

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				nested_values[i].real = member;
				element = element.next;
				i++;
			}
		} else {
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				double [] myDataArray = (double [])agg.myData;
				if (myDataArray == null) {
					nested_values[i].tag = INDETERMINATE;
				} else {
					member = myDataArray[i];
					if (Double.isNaN(member)) {
						nested_values[i].tag = INDETERMINATE;
					} else {
						nested_values[i].tag = PhFileReader.REAL;
						nested_values[i].real = member;
					}
				}
			}
		}
	}


	void setAggrDouble(A_double3 agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.REAL || el_type.express_type == DataType.NUMBER || 
					el_type.express_type == DataType.INTEGER) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		for (int i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			if (aux < 0) {
				nested_values[i].d_type = dt_type;
				nested_values[i].v_type = vl_type;
			}
		}
		switch(length) {
			case 3:
				if (Double.isNaN(agg.double3)) {
					if (agg.myType.express_type != DataType.ARRAY) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					nested_values[2].tag = INDETERMINATE;
				} else {
					nested_values[2].tag = PhFileReader.REAL;
					nested_values[2].real = agg.double3;
				}
			case 2:
				if (Double.isNaN(agg.double2)) {
					nested_values[1].tag = INDETERMINATE;
					if (agg.myType.express_type != DataType.ARRAY) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				} else {
					nested_values[1].tag = PhFileReader.REAL;
					nested_values[1].real = agg.double2;
				}
			case 1:
				if (Double.isNaN(agg.double1)) {
					nested_values[0].tag = INDETERMINATE;
					if (agg.myType.express_type != DataType.ARRAY) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				} else {
					nested_values[0].tag = PhFileReader.REAL;
					nested_values[0].real = agg.double1;
				}
		}
	}


	void setAggrString(A_string agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.STRING) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_string(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_string(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_string(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_string(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_string(null, agg, i, dt_type, vl_type);
					} else {
						set_set_string(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_string(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof String)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].tag = PhFileReader.STRING;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
		}
		nested_values[ind].string = (String)member;
	}
	void set_set_string(Object member, A_string agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof String)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].tag = PhFileReader.STRING;
				nested_values[ind].string = (String)member;
			}
		}
	}


	void setAggrLogical(A_enumeration agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		int member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.LOGICAL || el_type.express_type == DataType.BOOLEAN) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			i = 0;
			ListElementInteger [] myDataList = (ListElementInteger [])agg.myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				member = element.value;
				if (member < 1 || member > 3) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[i].tag = PhFileReader.LOGICAL;

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				nested_values[i].integer = member - 1;
				element = element.next;
				i++;
			}
		} else {
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				int [] myDataArray = (int [])agg.myData;
				if (myDataArray == null) {
					nested_values[i].tag = INDETERMINATE;
				} else {
					member = myDataArray[i];
					if (member < 0 || member > 3) {
						throw new SdaiException(SdaiException.SY_ERR);
					} 
					if (member == 0) {
						nested_values[i].tag = INDETERMINATE;
					} else {
						nested_values[i].tag = PhFileReader.LOGICAL;
						nested_values[i].integer = member - 1;
					}
				}
			}
		}
	}


	void setAggrBoolean(A_boolean agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		int member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.BOOLEAN) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			i = 0;
			ListElementInteger [] myDataList = (ListElementInteger [])agg.myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				member = element.value;
				if (member < 1 || member > 2) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[i].tag = PhFileReader.BOOLEAN;

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				nested_values[i].integer = member - 1;
				element = element.next;
				i++;
			}
		} else {
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				int [] myDataArray = (int [])agg.myData;
				if (myDataArray == null) {
					nested_values[i].tag = INDETERMINATE;
				} else {
					member = myDataArray[i];
					if (member < 0 || member > 2) {
						throw new SdaiException(SdaiException.SY_ERR);
					} 
					if (member == 0) {
						nested_values[i].tag = INDETERMINATE;
					} else {
						nested_values[i].tag = PhFileReader.BOOLEAN;
						nested_values[i].integer = member - 1;
					}
				}
			}
		}
	}


	void setAggrEnumeration(A_enumeration agg, A_string elements) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		String str_value;
		int member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			i = 0;
			ListElementInteger [] myDataList = (ListElementInteger [])agg.myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				member = element.value;
				if (member < 1 || member > elements.myLength) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[i].tag = PhFileReader.ENUM;

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				str_value = (String)elements.getByIndex(member);
				nested_values[i].string = str_value.toUpperCase();
				element = element.next;
				i++;
			}
		} else {
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}

				if (aux < 0) {
					nested_values[i].d_type = dt_type;
					nested_values[i].v_type = vl_type;
				}

				int [] myDataArray = (int [])agg.myData;
				if (myDataArray == null) {
					nested_values[i].tag = INDETERMINATE;
				} else {
					member = myDataArray[i];
					if (member < 0 || member > elements.myLength) {
						throw new SdaiException(SdaiException.SY_ERR);
					} 
					if (member == 0) {
						nested_values[i].tag = INDETERMINATE;
					} else {
						nested_values[i].tag = PhFileReader.ENUM;
						str_value = (String)elements.getByIndex(member);
						nested_values[i].string = str_value.toUpperCase();
					}
				}
			}
		}
	}


	void setAggrBinary(A_binary agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.BINARY) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_binary(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_binary(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_binary(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_binary(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_binary(null, agg, i, dt_type, vl_type);
					} else {
						set_set_binary(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_binary(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof Binary)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].tag = PhFileReader.BINARY;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
		}
		nested_values[ind].reference = (Binary)member;
	}
	void set_set_binary(Object member, A_binary agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof Binary)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].tag = PhFileReader.BINARY;
				nested_values[ind].reference = (Binary)member;
			}
		}
	}


	void setAggrElements(CAggregate agg) throws SdaiException {
		boolean has_con = false;
		if (agg.myLength < 0) {
			agg.myLength = -agg.myLength;
			has_con = true;
		}
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null;
		if (aux < 0 && agg.myType != null) {
//if (agg == null) System.out.println("Value setAggrElements agg is NULL");
//if (agg.myType == null) System.out.println("Value setAggrElements agg.myType is NULL   agg: " + agg);
			dt_type = (EData_type)agg.myType.getElement_type(null);
		}

		Object [] myDataA;
		if ((aux < 0 && agg.myType == null) || agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_elements(agg.myData, agg, 0, null, dt_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_elements(myDataA[i], agg, i, null, dt_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_elements(element.object, agg, i, element, dt_type);
					element = element.next;
					i++;
				}
			}
//CEntity instan = agg.owning_instance;
//System.out.println("   In VALUE instance: #" + instan.instance_identifier);
		} else {
			if (length == 1) {
				set_set_elements(agg.myData, agg, 0, dt_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_elements(null, agg, i, dt_type);
					} else {
						set_set_elements(myDataA[i], agg, i, dt_type);
					}
				}
			}
		}
		if (has_con) {
			agg.myLength = -agg.myLength;
		}
	}
	void set_list_elements(Object member, CAggregate agg, int ind, ListElement element, 
			EData_type dt_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		nested_values[ind].reference = member;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
		}
		if (member instanceof CAggregate) {
			nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
			SelectType select = ((CAggregate)member).myType.select;
			if (aux < 0) {
				nested_values[ind].aux = -1;
				nested_values[ind].v_type = ((CAggregate)member).myType;
			}
			if (select != null && select.is_mixed > 0) {
				nested_values[ind].setMixedAggrElements((CAggregate)member);
			} else {
				nested_values[ind].setAggrElements((CAggregate)member);
			}
			if (aux < 0) {
				nested_values[ind].aux = 0;
			}
		} else {
			if (!(member instanceof CEntity || member instanceof SdaiModel.Connector)) {
				String base;
				if (member == null) {
					base = SdaiSession.line_separator + AdditionalMessages.UR_NUME;
				} else {
					base = SdaiSession.line_separator + AdditionalMessages.UR_WRME + member.getClass().getName();
				}
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
			if (aux < 0) {
				if (member instanceof SdaiModel.Connector) {
					CEntity inst_el = agg.resolveConnector(element, false, (SdaiModel.Connector)member, ind);
					if (inst_el == null) {
						throw new SdaiException(SdaiException.SY_ERR);
					} else {
						nested_values[ind].reference = inst_el;
						nested_values[ind].v_type = inst_el.getInstanceType();
					}
				} else {
					nested_values[ind].v_type = ((CEntity)member).getInstanceType();
				}
				if (agg.myType == null) {
					nested_values[ind].d_type = nested_values[ind].v_type;
				}
			}
		}
	}
	void set_set_elements(Object member, CAggregate agg, int ind, EData_type dt_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else if (member instanceof CAggregate) {
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				SelectType select = ((CAggregate)member).myType.select;
				if (aux < 0) {
					nested_values[ind].aux = -1;
					nested_values[ind].v_type = ((CAggregate)member).myType;
				}
				if (select != null && select.is_mixed > 0) {
					nested_values[ind].setMixedAggrElements((CAggregate)member);
				} else {
					nested_values[ind].setAggrElements((CAggregate)member);
				}
				if (aux < 0) {
					nested_values[ind].aux = 0;
				}
			} else {
				if (!(member instanceof CEntity || member instanceof SdaiModel.Connector)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
				nested_values[ind].reference = member;
				if (aux < 0) {
					if (member instanceof SdaiModel.Connector) {
						CEntity inst_elm = agg.resolveConnector(false, (SdaiModel.Connector)member, ind);
						if (inst_elm == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						} else {
							nested_values[ind].reference = inst_elm;
							nested_values[ind].v_type = inst_elm.getInstanceType();
						}
					} else {
						nested_values[ind].v_type = ((CEntity)member).getInstanceType();
					}
				}
			}
		}
	}


	void setMixedAggrElements(CAggregate agg) throws SdaiException {
		SelectType select = agg.myType.select;
		if (select == null) {
			setAggrElements(agg);
			return;
		}

		EData_type dt_type = null;
		if (aux < 0) {
			dt_type = (EData_type)((EAggregation_type)v_type).getElement_type(null);
		}

		boolean has_con = false;
		if (agg.myLength < 0) {
			agg.myLength = -agg.myLength;
			has_con = true;
		}
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		int sel_number = 0;
		boolean take_select;
		if (select != null && select.is_mixed > 0) {
			take_select = true;
		} else {
			take_select = false;
			if (select != null) {
				sel_number = 1;
			}
		}
		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (!take_select) {
				if (length == 1) {
					set_list_mixed_elements(agg.myData, null, 0, select, take_select, sel_number, dt_type);
				} else if (length == 2) {
					myDataA = (Object [])agg.myData;
					for (i = 0; i < 2; i++) {
						set_list_mixed_elements(myDataA[i], null, i, select, take_select, sel_number, dt_type);
					}
				} else {
					ListElement element;
					if (agg.myLength <= CAggregate.SHORT_AGGR) {
						element = (ListElement)agg.myData;
					} else {
						myDataA = (Object [])agg.myData;
						element = (ListElement)myDataA[0];
					}
					i = 0;
					while (element != null) {
						set_list_mixed_elements(element.object, null, i, select, take_select, sel_number, dt_type);
						element = element.next;
						i++;
					}
				}
			} else {
				if (length == 1) {
					myDataA = (Object [])agg.myData;
					set_list_mixed_elements(myDataA[0], myDataA[1], 0, select, take_select, sel_number, dt_type);
				} else {
					ListElement element;
					if (agg.myLength <= CAggregate.SHORT_AGGR_SELECT) {
						element = (ListElement)agg.myData;
					} else {
						myDataA = (Object [])agg.myData;
						element = (ListElement)myDataA[0];
					}
					i = 0;
					while (element != null) {
						set_list_mixed_elements(element.object, element.next.object, i, select, take_select, sel_number, dt_type);
						element = element.next.next;
						i++;
					}
				}
			}
//CEntity instan = agg.owning_instance;
//System.out.println("   In VALUE mixed instance: #" + instan.instance_identifier);
		} else {
			if (!take_select) {
				if (length == 1) {
					set_set_mixed_elements(agg.myData, null, agg, 0, select, take_select, sel_number, dt_type);
				} else {
					myDataA = (Object [])agg.myData;
					for (i = 0; i < length; i++) {
						if (agg.myData == null) {
							set_set_mixed_elements(null, null, agg, i, select, take_select, sel_number, dt_type);
						} else {
							set_set_mixed_elements(myDataA[i], null, agg, i, select, take_select, sel_number, dt_type);
						}
					}
				}
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (myDataA == null) {
						set_set_mixed_elements(null, null, agg, i, select, take_select, sel_number, dt_type);
					} else {
						set_set_mixed_elements(myDataA[i * 2], myDataA[i * 2 + 1], agg, i, select, take_select, sel_number, dt_type);
					}
				}
			}
		}
		if (has_con) {
			agg.myLength = -agg.myLength;
		}
	}
	void set_list_mixed_elements(Object member, Object next, int ind, SelectType select, 
			boolean take_select, int sel_number, EData_type dt_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (member == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (take_select) {
			sel_number = ((Integer)next).intValue();
		}
		if (member instanceof SdaiModel.Connector) {
			nested_values[ind].reference = member;
			nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
		} else if (select != null) {
			if (sel_number == 1) {
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
				if (aux < 0) {
					if (member instanceof EEntity) {
						nested_values[ind].d_type = dt_type;
						nested_values[ind].v_type = ((CEntity)member).getInstanceType();
					} else {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
			} else {
				sel_number -= 2;
				if (sel_number < 0) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (aux < 0) {
					nested_values[ind].d_type = dt_type;
					nested_values[ind].v_type = select;
					nested_values[ind].aux = -1;
				}
				nested_values[ind].setTyped(member, select.tags[sel_number], select.paths[sel_number], sel_number);
				if (aux < 0) {
					nested_values[ind].aux = 0;
				}
			}
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}
	void set_set_mixed_elements(Object member, Object next, CAggregate agg, int ind, SelectType select, 
			boolean take_select, int sel_number, EData_type dt_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (take_select) {
				if (next == null) {
					sel_number = -1;
				} else {
					sel_number = ((Integer)next).intValue();
				}
			}
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
				if (aux < 0) {
					nested_values[ind].d_type = dt_type;
				}
			} else if (member instanceof SdaiModel.Connector) {
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
			} else if (select != null) {
				if (sel_number < 0) {
					nested_values[ind].tag = INDETERMINATE;
					if (aux < 0) {
						nested_values[ind].d_type = dt_type;
					}
				} else if (sel_number == 1) {
					nested_values[ind].reference = member;
					nested_values[ind].tag = PhFileReader.ENTITY_REFERENCE;
					if (aux < 0) {
						if (member instanceof EEntity) {
							nested_values[ind].d_type = dt_type;
							nested_values[ind].v_type = ((CEntity)member).getInstanceType();
						} else {
							throw new SdaiException(SdaiException.SY_ERR);
						}
					}
				} else {
					sel_number -= 2;
					if (sel_number < 0) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					if (aux < 0) {
						nested_values[ind].d_type = dt_type;
						nested_values[ind].v_type = select;
						nested_values[ind].aux = -1;
					}
					nested_values[ind].setTyped(member, select.tags[sel_number], select.paths[sel_number], sel_number);
					if (aux < 0) {
						nested_values[ind].aux = 0;
					}
				}
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}	
	}


	void setAggrIntegerNested(Aa_integer agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_integer_nested(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_integer_nested(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_integer_nested(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_integer_nested(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_integer_nested(null, agg, i, dt_type, vl_type);
					} else {
						set_set_integer_nested(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_integer_nested(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_integer)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrInteger((A_integer)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_integer_nested(Object member, Aa_integer agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_integer)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrInteger((A_integer)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	void setAggrDoubleNested(Aa_double agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_double_nested(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_double_nested(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_double_nested(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_double_nested(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_double_nested(null, agg, i, dt_type, vl_type);
					} else {
						set_set_double_nested(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_double_nested(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_double)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrDouble((A_double)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_double_nested(Object member, Aa_double agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_double)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrDouble((A_double)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	void setAggrStringNested(Aa_string agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		Object member;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_string_nested(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_string_nested(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_string_nested(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_string_nested(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_string_nested(null, agg, i, dt_type, vl_type);
					} else {
						set_set_string_nested(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_string_nested(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_string)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrString((A_string)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_string_nested(Object member, Aa_string agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_string)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrString((A_string)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	void setAggrLogicalNested(Aa_enumeration agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_logical_nested(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_logical_nested(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_logical_nested(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_logical_nested(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_logical_nested(null, agg, i, dt_type, vl_type);
					} else {
						set_set_logical_nested(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_logical_nested(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_enumeration)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrLogical((A_enumeration)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_logical_nested(Object member, Aa_enumeration agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_enumeration)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrLogical((A_enumeration)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	void setAggrBooleanNested(Aa_boolean agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_boolean_nested(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_boolean_nested(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_boolean_nested(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_boolean_nested(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_boolean_nested(null, agg, i, dt_type, vl_type);
					} else {
						set_set_boolean_nested(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_boolean_nested(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_boolean)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrBoolean((A_boolean)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_boolean_nested(Object member, Aa_boolean agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_boolean)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrBoolean((A_boolean)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	void setAggrEnumerationNested(Aa_enumeration agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;
		A_string elements = null;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType e_type = (DataType)agg.myType.getElement_type(null);
			dt_type = e_type;
			while (e_type.express_type == DataType.DEFINED_TYPE) {
				e_type = (DataType)((CDefined_type)e_type).getDomain(null);
			}
			if (e_type.express_type >= DataType.LIST && e_type.express_type <= DataType.AGGREGATE) {
				vl_type = e_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_enumeration_nested(agg.myData, 0, elements, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					elements = set_list_enumeration_nested(myDataA[i], i, elements, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					elements = set_list_enumeration_nested(element.object, i, elements, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_enumeration_nested(agg.myData, agg, 0, elements, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						elements = set_set_enumeration_nested(null, agg, i, elements, dt_type, vl_type);
					} else {
						elements = set_set_enumeration_nested(myDataA[i], agg, i, elements, dt_type, vl_type);
					}
				}
			}
		}
	}
	A_string set_list_enumeration_nested(Object member, int ind, A_string elements, EData_type dt_type, 
			EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof A_enumeration)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (ind == 0) {
			DataType el_type = (DataType)((A_enumeration)member).myType.getElement_type(null);
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type < DataType.ENUMERATION || el_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
				CEntity owning_inst = ((A_enumeration)member).getOwningInstance();
				if (owning_inst == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				SdaiModel owning_mod = owning_inst.owning_model;
				if (owning_mod == null) {
					throw new SdaiException(SdaiException.AI_NVLD);
				}
				elements = ((EExtensible_enumeration_type)el_type).getElements(null, owning_mod.repository.session.sdai_context);
			} else {
				elements = ((EEnumeration_type)el_type).getElements(null);
			}
		}
		nested_values[ind].setAggrEnumeration((A_enumeration)member, elements);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
		return elements;
	}
	A_string set_set_enumeration_nested(Object member, Aa_enumeration agg, int ind, A_string elements, 
			EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof A_enumeration)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				if (ind == 0) {
					DataType el_type = (DataType)((A_enumeration)member).myType.getElement_type(null);
					while (el_type.express_type == DataType.DEFINED_TYPE) {
						el_type = (DataType)((CDefined_type)el_type).getDomain(null);
					}
					if (el_type.express_type < DataType.ENUMERATION || el_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
					if (el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						CEntity owning_inst = ((A_enumeration)member).getOwningInstance();
						if (owning_inst == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						SdaiModel owning_mod = owning_inst.owning_model;
						if (owning_mod == null) {
							throw new SdaiException(SdaiException.AI_NVLD);
						}
						elements = ((EExtensible_enumeration_type)el_type).getElements(null, owning_mod.repository.session.sdai_context);
					} else {
						elements = ((EEnumeration_type)el_type).getElements(null);
					}
				}
				nested_values[ind].setAggrEnumeration((A_enumeration)member, elements);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
		return elements;
	}


	void setAggrDoubleNested2(Aaa_double agg) throws SdaiException {
		if (nested_values == null || agg.myLength > nested_values.length) {
			enlarge_nested_values(agg.myLength);
		}
		length = agg.myLength;
		int i;

		EData_type dt_type = null, vl_type = null;
		if (aux < 0) {
			DataType el_type = (DataType)agg.myType.getElement_type(null);
			dt_type = el_type;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				vl_type = el_type;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}

		Object [] myDataA;
		if (agg.myType.express_type == DataType.LIST) {
			if (agg.myData == null) {
				return;
			}
			if (length == 1) {
				set_list_double_nested2(agg.myData, 0, dt_type, vl_type);
			} else if (length == 2) {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < 2; i++) {
					set_list_double_nested2(myDataA[i], i, dt_type, vl_type);
				}
			} else {
				ListElement element;
				if (agg.myLength <= CAggregate.SHORT_AGGR) {
					element = (ListElement)agg.myData;
				} else {
					myDataA = (Object [])agg.myData;
					element = (ListElement)myDataA[0];
				}
				i = 0;
				while (element != null) {
					set_list_double_nested2(element.object, i, dt_type, vl_type);
					element = element.next;
					i++;
				}
			}
		} else {
			if (length == 1) {
				set_set_double_nested2(agg.myData, agg, 0, dt_type, vl_type);
			} else {
				myDataA = (Object [])agg.myData;
				for (i = 0; i < length; i++) {
					if (agg.myData == null) {
						set_set_double_nested2(null, agg, i, dt_type, vl_type);
					} else {
						set_set_double_nested2(myDataA[i], agg, i, dt_type, vl_type);
					}
				}
			}
		}
	}
	void set_list_double_nested2(Object member, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (!(member instanceof Aa_double)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		nested_values[ind].reference = member;
		nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		nested_values[ind].setAggrDoubleNested((Aa_double)member);
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}
	void set_set_double_nested2(Object member, Aaa_double agg, int ind, EData_type dt_type, EData_type vl_type) throws SdaiException {
		if (nested_values[ind] == null) {
			nested_values[ind] = new Value();
		}
		if (aux < 0) {
			nested_values[ind].d_type = dt_type;
			nested_values[ind].v_type = vl_type;
			nested_values[ind].aux = -1;
		}
		if (agg.myData == null) {
			nested_values[ind].tag = INDETERMINATE;
		} else {
			if (member == null) {
				nested_values[ind].tag = INDETERMINATE;
			} else {
				if (!(member instanceof Aa_double)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				nested_values[ind].reference = member;
				nested_values[ind].tag = PhFileReader.EMBEDDED_LIST;
				nested_values[ind].setAggrDoubleNested((Aa_double)member);
			}
		}
		if (aux < 0) {
			nested_values[ind].aux = 0;
		}
	}


	boolean check_references(StaticFields staticFields, AEntity inst_aggr, SdaiModel mod, CEntity owning_instance) throws SdaiException {
		boolean con_created = false;
		switch (tag) {
			case PhFileReader.TYPED_PARAMETER:
				nested_values[0].check_references(staticFields, inst_aggr, mod, owning_instance);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (reference instanceof SdaiModel.Connector) {
					reference = ((SdaiModel.Connector)reference).copyConnector(owning_instance);
					con_created = true;
					break;
				}
				if (inst_aggr == null) {
					break;
				}
				int index = mod.find_instance(staticFields, ((CEntity)reference).instance_identifier);
				if (index >= 0) {
					//int true_index = ((CEntity)reference).instance_position;
                    int true_index = ((CEntity)reference).instance_position & CEntity.POS_MASK;    //--VV--
//					reference = inst_aggr.getByIndexEntity(index + 1);
					reference = staticFields.for_instances_sorting[true_index];
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				boolean con_found = false;
				for (int i = 0; i < length; i++) {
					boolean res = nested_values[i].check_references(staticFields, inst_aggr, mod, owning_instance);
					if (res) {
						con_found = true;
					}
				}
				if (con_found) {
					real = Double.NaN;
				}
				break;
			default:
		}
		return con_created;
	}


/**
 * Produces an object of this class.
 * @return an object of this class.
 */
	public static Value alloc() {
		return new Value();
	}


/**
 * Produces an object of this class prepared to wrap values of the 
 * specified data type.
 * @param type data type values of which (or of which specializations) are allowed 
 * to be described by the created object of this class.
 * @return an object of this class.
 * @see #alloc(Value)
 * @see #init(EData_type)
 */
	public static Value alloc(EData_type type) {
		Value val = new Value();
		val.init(type);
		return val;
	}


/**
 * Produces an object of this class prepared to wrap values of the 
 * specified data type (or its specializations).
 * @param v an object of <code>Value</code> the data type attached to which 
 * is borrowed to specify values which are allowed to be described by the 
 * created object of this class.
 * @return an object of this class.
 * @see #alloc(EData_type)
 */
	public static Value alloc(Value v) {
		Value val = new Value();
		val.init(v.d_type);
		return val;
	}


/**
 * Returns an entity instance wrapped in an object of this class.
 * In the case of unset, null is returned.
 * @return instance of the entity data type described by this class.
 */
	public EEntity getInstance() throws SdaiException {
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			if (reference instanceof EEntity) {
//				inst.addToInverseList(ref);  it is not clear about owner of this reference
				return (EEntity)reference;
			} else {
//		Print warning message
				return null;
			}
		} else if (tag == INDETERMINATE) {
			return null;
		}
		printWarningToLogoValidate(AdditionalMessages.EE_NINS);
		return null;
	}


/**
 * Initializes the current object of this class to wrap values of the 
 * specified data type.
 * @param type data type values of which (or of which specializations) are allowed 
 * to be described by this object of <code>Value</code>.
 * @return an object of this class with the data type attached.
 * @see #alloc(EData_type)
 */
	public Value init(EData_type type) {
		d_type = type;
		v_type = null;
		tag = INDETERMINATE;
		if (d_type != null) {
			int tp = ((DataType)d_type).express_type;
			if (tp >= DataType.LIST && tp <= DataType.AGGREGATE) {
				agg_owner = null;
			}
		}
		return this;
	}


/**
 * Initializes the current object of this class to wrap aggregates of the 
 * specified aggregation type.
 * @param type aggregation type specifying aggregates which are allowed 
 * to be described by this object of <code>Value</code>.
 * @param owner the instance (if null, then none) owning the aggregate wrapped.
 * @return an object of this class with the aggregation type and, possibly, 
 * the aggregate owner attached.
 */
	public Value initAggregate(EAggregation_type type, EEntity owner) throws SdaiException {
		init(type);
		agg_owner = (SdaiCommon)owner;
		return this;
	}


/**
 * Initializes an object of this class for the member of the aggregate represented 
 * by the current object of <code>Value</code> to wrap values of the 
 * specified aggregation type.
 * @param index the index for the member of interest within the aggregate wrapped.
 * @param type aggregation type specifying aggregates which are allowed 
 * to be in the role of the member of the aggregate at the specified position.
 * @return an object of this class prepared to describe the required member of 
 * the aggregate. 
 */
	public Value initAggregateMember(int index, EAggregation_type type) throws SdaiException {
		if (tag != PhFileReader.EMBEDDED_LIST) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAGG);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (nested_values == null || index > nested_values.length) {
			enlarge_and_save_nested_values(index);
		}
		index--;
		if (nested_values[index] == null) {
			nested_values[index] = new Value();
		}
		nested_values[index].init(type);
		return nested_values[index];
	}


/**
 * Initializes the current object of this class to represent an empty aggregate.
 * @return this object of <code>Value</code> with empty aggregate wrapped. 
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value create() throws SdaiException {
		EData_type type;
		if (d_type != null) {
//		if (v_type != null) {
			int tp = ((DataType)d_type).express_type;
//			int tp = ((DataType)v_type).express_type;
			if (tp < DataType.LIST || tp > DataType.AGGREGATE) {
				if (tp == DataType.DEFINED_TYPE) {
					type = get_aggregation_type(d_type);
//					type = get_aggregation_type(v_type);
					if (type == null) {
						printWarningToLogoValidate(AdditionalMessages.EE_NATY);
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					printWarningToLogoValidate(AdditionalMessages.EE_NATY);
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				type = d_type;
//				type = v_type;
			}
		} else {
			type = d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
		}
		tag = PhFileReader.EMBEDDED_LIST;
		v_type = type;
		length = 0;
		reference = null;
		return this;
	}


	private EData_type get_aggregation_type(EData_type type) throws SdaiException {
		DataType tp = (DataType)type;
		while (tp.express_type == DataType.DEFINED_TYPE) {
			tp = (DataType)((CDefined_type)tp).getDomain(null);
		}
		if (tp.express_type >= DataType.LIST || tp.express_type <= DataType.AGGREGATE) {
			return (EData_type)tp;
		}
		return null;
	}


/**
 * Returns the number of elements in an aggregate wrapped in an object of this class.
 * @return the size of the aggregate. 
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public int getMemberCount() throws SdaiException {
		if (v_type != null) {
			int tp = ((DataType)v_type).express_type;
			if (tp < DataType.LIST || tp > DataType.AGGREGATE) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_NAGG, v_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_NVAL);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return length;
	}


/**
 * Returns the value (of type <code>Value</code>) of the member at the specified index
 * position in an aggregate wrapped in an object of this class. Indexing starts with 1.
 * @param index the index or position from which the value is asked.
 * @return the value at the specified position in the aggregate.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value getByIndex(int index) throws SdaiException {
		if (v_type != null) {
			int tp = ((DataType)v_type).express_type;
			if (tp < DataType.LIST || tp > DataType.AGGREGATE) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_NAGG, v_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_NVAL);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (index <= 0 || index > length) {
			printWarningToLogoValidate(AdditionalMessages.EE_AIOB);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return nested_values[index - 1];
	}


/**
 * Returns an object of this class wrapping the value of the specified 
 * attribute. In the case when the attribute is a derived one, for 
 * calculation of its value the default context of the session is used.
 * @param attr an attribute the value of which is asked.
 * @return the value of the attribute. 
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value getAttribute(EAttribute attr) throws SdaiException {
		return getAttribute(attr, null);
	}


/**
 * Returns an object of this class wrapping the value of the specified 
 * attribute. 
 * @param attr an attribute the value of which is asked.
 * @param context context in which the value of the attribute is calculated 
 * (provided this attribute is a derived one).
 * @return the value of the attribute. 
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value getAttribute(EAttribute attr, SdaiContext context) throws SdaiException {
		if (attr == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NOAT);
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		CEntity inst;
		Value val_attr;
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			inst = (CEntity)reference;
//Value vvv = inst.get(attr, context);
//System.out.println("Value  ***** Case 1  vvv.tag: " + vvv.tag + "   attr: " + attr);
//return vvv;
			val_attr = inst.get(attr, context);
			if (val_attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
				} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
					val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
				}
			}
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
					!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
				return correctInverse(val_attr, (EInverse_attribute)attr);
			}
			return val_attr;
		}
		if (tag == ENTITY_VALUE) {
			val_attr = ((EntityValue)reference).findAttribute(attr, context);
			if (val_attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
				} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
					val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
				}
			}
			return val_attr;
		}
		if (tag == COMPLEX_ENTITY_VALUE) {
			EntityValue [] ev_arr = (EntityValue [])reference;
			for (int i = 0; i < length; i++) {
				val_attr = ev_arr[i].findAttribute(attr, context);
				if (val_attr != null) {
					return val_attr;
				}
			}
			val_attr = new Value();
			val_attr.tag = INDETERMINATE;
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
				val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
			} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
				val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
			}
			return val_attr;
		}
		if (tag == PhFileReader.MISSING) {
			val_attr = new Value();
			val_attr.tag = INDETERMINATE;
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
				val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
			} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
				val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
			}
			return val_attr;
		}
		if (tag == PhFileReader.EMBEDDED_LIST) {
			if (length == 1 && nested_values[0].tag == PhFileReader.ENTITY_REFERENCE) {
				inst = (CEntity)nested_values[0].reference;
				val_attr = inst.get(attr, context);
				if (val_attr == null) {
					val_attr = new Value();
					val_attr.tag = INDETERMINATE;
					if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
						val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
					} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
						val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
					}
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
						!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
					return correctInverse(val_attr, (EInverse_attribute)attr);
				}
				return val_attr;
			}
		}
		printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_NOAD, tag);
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	private Value correctInverse(Value val_attr, EInverse_attribute attr) throws SdaiException {
		if (val_attr.length <= 0) {
			val_attr.tag = INDETERMINATE;
			val_attr.d_type = attr.getDomain(null);
		} else {
			val_attr.tag = PhFileReader.ENTITY_REFERENCE;
			val_attr.reference = val_attr.nested_values[0].reference;
			val_attr.v_type = val_attr.d_type = val_attr.nested_values[0].v_type;
		}
		return val_attr;
	}


/**
 * Returns an object of this class wrapping the value of the attribute 
 * specified by its name. 
 * @param attrName the name of an attribute the value of which is asked.
 * @param context context in which the value of the attribute is calculated 
 * (provided this attribute is a derived one).
 * @return the value of the attribute.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value getAttribute(String attrName, SdaiContext context) throws SdaiException {
		if (attrName == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NOAT);
			throw new SdaiException(SdaiException.AT_NDEF);
		}
//if (attrName.equals("a1")) 
//System.out.println("Value ========  tag: " + tag + "   d_type: " + d_type + "   v_type: " + v_type);
		Value val_attr;
		CEntity inst;
		CEntityDefinition def;
		EAttribute attr;
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			inst = (CEntity)reference;
			def = (CEntityDefinition)inst.getInstanceType();
			attr = def.find_attribute(attrName);
			if (attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				return val_attr;
			}
			val_attr = inst.get(attr, context);
			if (val_attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
				} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
					val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
				}
			}
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
					!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
				return correctInverse(val_attr, (EInverse_attribute)attr);
			}
			return val_attr;
		} else if (tag == ENTITY_VALUE) {
			def = (CEntityDefinition)((EntityValue)reference).def;
			attr = def.find_attribute(attrName);
			if (attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				return val_attr;
			}
			val_attr = ((EntityValue)reference).findAttribute(attr, context);
			if (val_attr == null) {
				val_attr = new Value();
				val_attr.tag = INDETERMINATE;
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
					val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
				} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
					val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
				}
			}
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
					!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
				return correctInverse(val_attr, (EInverse_attribute)attr);
			}
			return val_attr;
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			EntityValue [] ev_arr = (EntityValue [])reference;
			EAttribute attr_found = null;
			for (int i = 0; i < length; i++) {
				def = (CEntityDefinition)ev_arr[i].def;
				attr = def.find_attribute(attrName);
				if (attr == null) {
					continue;
				}
				attr_found = attr;
				val_attr = ev_arr[i].findAttribute(attr, context);
				if (val_attr != null) {
					if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
							!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
						return correctInverse(val_attr, (EInverse_attribute)attr);
					}
					return val_attr;
				}
			}
			val_attr = new Value();
			val_attr.tag = INDETERMINATE;
			if (attr_found != null) {
				if (((AttributeDefinition)attr_found).attr_tp == AttributeDefinition.EXPLICIT) {
					val_attr.d_type = (EData_type)((EExplicit_attribute)attr_found).getDomain(null);
				} else if (((AttributeDefinition)attr_found).attr_tp == AttributeDefinition.DERIVED) {
					val_attr.d_type = (EData_type)((EDerived_attribute)attr_found).getDomain(null);
				}
			}
			return val_attr;
		} else if (tag == PhFileReader.MISSING) {
			val_attr = new Value();
			val_attr.tag = INDETERMINATE;
			return val_attr;
		} else if (tag == PhFileReader.EMBEDDED_LIST) {
			if (length == 1 && nested_values[0].tag == PhFileReader.ENTITY_REFERENCE) {
				inst = (CEntity)nested_values[0].reference;
				def = (CEntityDefinition)inst.getInstanceType();
				attr = def.find_attribute(attrName);
				if (attr == null) {
					val_attr = new Value();
					val_attr.tag = INDETERMINATE;
					return val_attr;
				}
				val_attr = inst.get(attr, context);
				if (val_attr == null) {
					val_attr = new Value();
					val_attr.tag = INDETERMINATE;
					if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.EXPLICIT) {
						val_attr.d_type = (EData_type)((EExplicit_attribute)attr).getDomain(null);
					} else if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.DERIVED) {
						val_attr.d_type = (EData_type)((EDerived_attribute)attr).getDomain(null);
					}
				}
				if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE && 
						!((EInverse_attribute)attr).testMin_cardinality(null) && val_attr.tag != PhFileReader.ENTITY_REFERENCE) {
					return correctInverse(val_attr, (EInverse_attribute)attr);
				}
				return val_attr;
			}
		}
		printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_NOAD, tag);
		throw new SdaiException(SdaiException.VT_NVLD);
	}


/**
 * Assigns the submitted value to the specified attribute of the 
 * entity instance wrapped in this object of type <code>Value</code>.
 * @param attr an attribute the value to which is assigned.
 * @param val the value to be assigned.
 * @return an object of this class describing an entity instance to the 
 * specified attribute of which the value is assigned.
 * @throws SdaiException AT_NDEF, attribute not defined.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value setAttribute(EExplicit_attribute attr, Value val) throws SdaiException {
		if (attr == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NOAT);
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NOVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			CEntity inst = (CEntity)reference;
			inst.set(attr, val);
			return this;
		}
		boolean failure;
		if (tag == ENTITY_VALUE) {
			EntityValue eval = (EntityValue)reference;
			failure = eval.setAttribute(attr, val);
//System.out.println("Value    val = " + val.getInteger() + "   failure = " + failure);
			if (failure) {
				printWarningToLogoValidateGiveAttribute(AdditionalMessages.EE_ATNE, attr);
				throw new SdaiException(SdaiException.AT_NDEF);
			}
			if (eval.owner != null) {
				((CEntity)eval.owner).set(attr, val);
			}
			return this;
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			EntityValue [] ev_arr = (EntityValue [])reference;
			for (int i = 0; i < length; i++) {
				failure = ev_arr[i].setAttribute(attr, val);
				if (!failure) {
					if (ev_arr[i].owner != null) {
						((CEntity)ev_arr[i].owner).set(attr, val);
					}
					return this;
				}
			}
			printWarningToLogoValidateGiveAttribute(AdditionalMessages.EE_ATNE, attr);
			throw new SdaiException(SdaiException.AT_NDEF);
		}
		printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_NOAD, tag);
		throw new SdaiException(SdaiException.VT_NVLD);
	}


/**
 * Verifies if the value wrapped in the object of this class is of 
 * type (or of its specialization) specified by the method's second parameter. 
 * In the case of the negative verdict, SdaiException SY_ERR with an 
 * additional explanation will be thrown. 
 * @param context context in which the value is checked. 
 * @param type data type against which the value is checked.
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value check(SdaiContext context, EData_type type) throws SdaiException {
		if (type == null || d_type == null) {
//System.out.println(" Value ~~~~~~~~~~~~~~~  Types: " + type + "    v_type: " + v_type + "   d_type: " + d_type  + "   tag: " + tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (type == v_type) {
			return this;
		}
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			int tp = ((DataType)type).express_type;
			if (tp != DataType.DEFINED_TYPE && (tp < DataType.SELECT || tp > DataType.ENT_EXT_EXT_SELECT) ) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (tag == INDETERMINATE) {
				return this;
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			Value res;
			if (sels.myLength == 1) {
				res = check_selection(context, (EData_type)sels.myData);
				if (res != null) {
					return this;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					res = check_selection(context, (EData_type)myDataA[i]);
					if (res != null) {
						return this;
					}
				}
			}
//			for (int i = 1; i <= sels.myLength; i++) {
//				EData_type part_type = (EData_type)sels.getByIndexEntity(i);
//				Value res = check_selection(context, part_type);
//				if (res != null) {
//					return this;
//				}
//			}
//System.out.println(" Value ~~~  Types: " + type + "    v_type: " + v_type + "   tag: " + tag + "   sel_type: " + sel_type);
			String base = AdditionalMessages.EC_INET;
			printWarningToLogoValidateGiveTypes(base, type, v_type);
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (analyse_Value(type, context)) {
			return this;
		}
//System.out.println(" Value ~~~~~~  Types: " + type + "    v_type: " + v_type + "   d_type: " + d_type + "   tag: " + tag);
		String base = AdditionalMessages.EC_INET;
		printWarningToLogoValidateGiveTypes(base, type, v_type);
		throw new SdaiException(SdaiException.SY_ERR, base);
	}

	int check_and_restore(SdaiContext context, EData_type type) throws SdaiException {
		if (type == null || d_type == null) {
//System.out.println(" Value ~~~~~~~~~~~~~~~  Types: " + type + "    v_type: " + v_type + "   d_type: " + d_type  + "   tag: " + tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (type == v_type) {
			return -1;
		}
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			int tp = ((DataType)type).express_type;
			if (tp != DataType.DEFINED_TYPE && (tp < DataType.SELECT || tp > DataType.ENT_EXT_EXT_SELECT) ) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (tag == INDETERMINATE) {
				return -1;
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			EData_type part_type;
			Value res;
			if (sels.myLength == 1) {
				part_type = (EData_type)sels.myData;
				res = check_selection(context, part_type);
				if (res != null) {
					return restore_def_types((SelectType)sel_type);
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					part_type = (EData_type)myDataA[i];
					res = check_selection(context, part_type);
					if (res != null) {
						return restore_def_types((SelectType)sel_type);
					}
				}
			}
//			for (int i = 1; i <= sels.myLength; i++) {
//				EData_type part_type = (EData_type)sels.getByIndexEntity(i);
//				Value res = check_selection(context, part_type);
//				if (res != null) {
//					return restore_def_types((SelectType)sel_type);
//				}
//			}
//System.out.println(" Value ~~~  Types: " + type + "    v_type: " + v_type + "   d_type: " + d_type + 
//"   tag: " + tag + "   sel_type: " + sel_type);
			String base = AdditionalMessages.EC_INET;
			printWarningToLogoValidateGiveTypes(base, type, v_type);
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (analyse_Value(type, context)) {
			return -1;
		}
//System.out.println(" Value ~~~~~~  Types: " + type + "    v_type: " + v_type + "   d_type: " + d_type + "   tag: " + tag +
//"   sel_type: " + sel_type);

//if (nested_values[0]!=null) 
//System.out.println(" Value ~~~~~~     nested_values[0].v_type: " + nested_values[0].v_type + 
//"   nested_values[0].d_type: " + nested_values[0].d_type + "   nested_values[0].tag: " + nested_values[0].tag);
		String base = AdditionalMessages.EC_INET;
		printWarningToLogoValidateGiveTypes(base, type, v_type);
		throw new SdaiException(SdaiException.SY_ERR, base);
	}

	int only_restore(EData_type type) throws SdaiException {
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			int tp = ((DataType)type).express_type;
			if (tp != DataType.DEFINED_TYPE && (tp < DataType.SELECT || tp > DataType.ENT_EXT_EXT_SELECT) ) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, null);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			EData_type part_type;
			Value res;
			if (sels.myLength == 1) {
				part_type = (EData_type)sels.myData;
				res = check_selection(null, part_type);
				if (res != null) {
					return restore_def_types((SelectType)sel_type);
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					part_type = (EData_type)myDataA[i];
					res = check_selection(null, part_type);
					if (res != null) {
						return restore_def_types((SelectType)sel_type);
					}
				}
			}
		}
		return -1;
	}

	int restore_def_types(SelectType sel_type) throws SdaiException {
		for (int i = 0; i < sel_type.count; i++) {
			if (sel_type.tags[i] == tag) {
				return i;
			}
		}
		return -1;
	}


/**
 * Verifies if the value wrapped in the object of this class is of 
 * type (or of its specialization) specified by the method's second parameter. 
 * In the case of the negative verdict null is returned. 
 * @param context context in which the value is checked. 
 * @param type data type against which the value is checked.
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value check_selection(SdaiContext context, EData_type type) throws SdaiException {
		if (type == v_type) {
			return this;
		}
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			int tp = ((DataType)type).express_type;
			if (tp != DataType.DEFINED_TYPE && (tp < DataType.SELECT || tp > DataType.ENT_EXT_EXT_SELECT) ) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
//			int d_tp = ((DataType)d_type).express_type;
//			if (tag != INDETERMINATE && 
//					(d_tp == DataType.DEFINED_TYPE || (d_tp >= DataType.SELECT && d_tp <= DataType.ENT_EXT_EXT_SELECT) )) {
//				if (analyse_defined_type(type)) {
//					return this;
//				} else {
//					return null;
//				}
//			}
			if (tag == INDETERMINATE) {
				return this;
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			EData_type part_type;
			Value res;
			if (sels.myLength == 1) {
				part_type = (EData_type)sels.myData;
				res = check_selection(context, part_type);
				if (res != null) {
					return this;
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					part_type = (EData_type)myDataA[i];
					res = check_selection(context, part_type);
					if (res != null) {
						return this;
					}
				}
			}
//			for (int i = 1; i <= sels.myLength; i++) {
//				EData_type part_type = (EData_type)sels.getByIndexEntity(i);
//				Value res = check_selection(context, part_type);
//				if (res != null) {
//					return this;
//				}
//			}
			return null;
		}
		if (analyse_Value(type, context)) {
			return this;
		}
		return null;
	}

	Value check_for_set(EData_type type, SdaiContext context) throws SdaiException {
		if (type == v_type) {
			analyse_Value(type, context);
		}
		return check(context, type);
	}

	private ESelect_type getSelect(EData_type type) throws SdaiException {
		DataType tp = (DataType)type;
		if (tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			return (ESelect_type)type;
		} else if (tp.express_type != DataType.DEFINED_TYPE) {
			return null;
		}
		while (tp.express_type == DataType.DEFINED_TYPE) {
			tp = (DataType)((CDefined_type)tp).getDomain(null);
			if (tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return (ESelect_type)tp;
			}
		}
		return null;
	}

	private boolean analyse_Value(EData_type type, SdaiContext context) throws SdaiException {
		if (type == null) {
			return false;
		}
		DataType tp = (DataType)type;
		if (tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) {
			return analyse_simple_Value(tp);
		} else if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
			return take_aggr_method((EAggregation_type)type, context);
		} else if (tp.express_type == DataType.ENTITY) {
			aux = PhFileReader.ENTITY_REFERENCE;
			if (tag == PhFileReader.ENTITY_REFERENCE) {
//if (prnt) {
//if (v_type == null)
//int ident = -1;
//if (reference instanceof CEntity) ident = (int)((CEntity)reference).instance_identifier;
//System.out.println(" Value ############# Inside analyse_Value()  v_type is NULL   : ");
//}
//else System.out.println(" Value ############# Inside analyse_Value()  v_type: " + v_type.getPersistentLabel());
//}
				return ((EEntity_definition)v_type).isSubtypeOf((EEntity_definition)type);
			} else if (tag == INDETERMINATE) {
				return true;
			}
			return false;
		} else if (tp.express_type == DataType.DATA_TYPE) {
			if (((EData_type)type).getName(null).equals("_ENTITY")) {
				if (tag == PhFileReader.ENTITY_REFERENCE || tag == INDETERMINATE) {
					return true;
				}
				return false;
			}
			return true;
		} else if (tp.express_type != DataType.DEFINED_TYPE) {
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
//		if (tag != INDETERMINATE && d_type != null && ((DataType)d_type).express_type == DataType.DEFINED_TYPE) {
//			if (!analyse_defined_type((EDefined_type)type)) {
//				return false;
//			}
//		}
		while (tp.express_type == DataType.DEFINED_TYPE) {
			tp = (DataType)((EDefined_type)tp).getDomain(null);
			if (tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			} else if (tp.express_type >= DataType.ENUMERATION && tp.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				aux = PhFileReader.ENUM;
				if (tag == PhFileReader.ENUM) {
					if (tp == v_type) {
						return true;
					}
				} else if (tag == INDETERMINATE) {
					return true;
				}
				return false;
			} else if (tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) {
				return analyse_simple_Value(tp);
			} else if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
				return take_aggr_method((EAggregation_type)tp, context);
			} else if (tp.express_type != DataType.DEFINED_TYPE) {
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
		}
		return false;
	}

	private boolean analyse_defined_type(EData_type dt) throws SdaiException {
		DataType type = (DataType)dt;
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				if (d_type == type) {
					return true;
				}
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		type = (DataType)d_type;
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				if (dt == type) {
					return true;
				}
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		int tp = ((DataType)dt).express_type;
		if ( (tp >= DataType.SELECT && tp <= DataType.ENT_EXT_EXT_SELECT) && dt == type) {
			return true;
		}
		return false;
	}

	private boolean analyse_simple_Value(DataType simple) throws SdaiException {
		if (v_type == null) {
			return true;
		}
		if (tag == PhFileReader.TYPED_PARAMETER) {
			return nested_values[0].analyse_simple_Value(simple);
		}
		int v_tp = ((DataType)v_type).express_type;
		switch (simple.express_type) {
			case DataType.NUMBER:
				if (tag == INDETERMINATE || v_tp == DataType.NUMBER || 
						v_tp == DataType.REAL || v_tp == DataType.INTEGER) {
					aux = PhFileReader.REAL;
					return true;
				}
				break;
			case DataType.INTEGER:
				if (tag == INDETERMINATE || v_tp == DataType.INTEGER) {
					aux = PhFileReader.INTEGER;
					return true;
				}
				break;
			case DataType.REAL:
				if (tag == INDETERMINATE || v_tp == DataType.REAL || v_tp == DataType.INTEGER) {
					aux = PhFileReader.REAL;
					return true;
				}
				break;
			case DataType.BOOLEAN:
				if (tag == INDETERMINATE || v_tp == DataType.BOOLEAN || 
						(v_tp == DataType.LOGICAL && integer != 2) ) {
					aux = PhFileReader.BOOLEAN;
					return true;
				}
				break;
			case DataType.LOGICAL:
				if (tag == INDETERMINATE || v_tp == DataType.LOGICAL || v_tp == DataType.BOOLEAN) {
					aux = PhFileReader.LOGICAL;
					return true;
				}
				break;
			case DataType.BINARY:
				if (tag == INDETERMINATE || v_type == simple) {
					aux = PhFileReader.BINARY;
					return true;
				}
				break;
			case DataType.STRING:
				if (tag == INDETERMINATE || v_type == simple) {
					aux = PhFileReader.STRING;
					return true;
				}
				break;
		}
		return false;
	}

	private boolean check_array_compatibility(EArray_type left_type, EArray_type right_type, 
			SdaiContext context) throws SdaiException {
		int i;
		int left_size = left_type.getUpper_index(null).getBound_value(null) - 
			left_type.getLower_index(null).getBound_value(null);
		int right_size = right_type.getUpper_index(null).getBound_value(null) - 
			right_type.getLower_index(null).getBound_value(null);
		if (left_size != right_size) {
			return false;
		}
		if (right_type.getOptional_flag(null) && !left_type.getOptional_flag(null)) {
			boolean unset_found = false;
			for (i = 0; i < length; i++) {
				if (nested_values[i].tag == INDETERMINATE) {
					unset_found = true;
					break;
				}
			}
			if (unset_found) {
				return false;
			}
		}
		if (!right_type.getUnique_flag(null) && left_type.getUnique_flag(null)) {
			boolean identical_found = false;
			for (i = 0; i < length - 1; i++) {
				for (int j = i + 1; j < length; j++) {
					int res = nested_values[i].compareValues(nested_values[j], true, false, true, context);
					if (res == 0) {
						identical_found = true;
						break;
					}
				}
				if (identical_found) {
					break;
				}
			}
			if (identical_found) {
				return false;
			}
		}
		return true;
	}

	private boolean take_aggr_method(EAggregation_type aggr_type, SdaiContext context) throws SdaiException {
		if (tag == PhFileReader.EMBEDDED_LIST) {
//System.out.println(" Value   aggr_type: " + aggr_type + 
//"   v_type: " + v_type);
			if (v_type == null) {
				printWarningToLogoValidate(AdditionalMessages.EE_NTVA);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			int v_tp = ((DataType)v_type).express_type;
			if (aggr_type != v_type) {
				if ((aggr_type == ExpressTypes.LIST_GENERIC_TYPE && v_tp == DataType.LIST) || 
						(aggr_type == ExpressTypes.SET_GENERIC_TYPE && v_tp == DataType.SET) || 
						(aggr_type == ExpressTypes.BAG_GENERIC_TYPE && (v_tp == DataType.SET || v_tp == DataType.BAG)) || 
						(aggr_type == ExpressTypes.AGGREGATE_GENERIC_TYPE && (v_tp >= DataType.LIST && v_tp <= DataType.AGGREGATE))) {
					return true;
				}
				if (v_tp < DataType.LIST || v_tp > DataType.AGGREGATE) {
					return false;
				}
				int a_tp = ((DataType)aggr_type).express_type;
				if (v_tp == DataType.ARRAY) {
					if (a_tp != DataType.ARRAY) {
						return false;
					}
					boolean res = check_array_compatibility((EArray_type)aggr_type, (EArray_type)v_type, context);
					if (!res) {
						return false;
					}
				} else if (a_tp != DataType.AGGREGATE && v_tp != DataType.AGGREGATE) {
					if ( !((a_tp == DataType.LIST && v_tp == DataType.LIST) || (a_tp == DataType.SET && v_tp == DataType.SET) || 
							(a_tp == DataType.BAG && (v_tp == DataType.SET || v_tp == DataType.BAG)) ) ) {
						return false;
					}
				}
//				return false;
			}
		} else if (tag == INDETERMINATE) {
			aux = PhFileReader.EMBEDDED_LIST;
			return true;
		} else {
			return false;
		}
		AggregationType next_type = null;
		SdaiSession ss = ((CEntity)aggr_type).owning_model.repository.session;
		DataType el_type = (DataType)aggr_type.getElement_type(null);
		DataType v_tp_el_type = (DataType)((EAggregation_type)v_type).getElement_type(null);
		if (!oneCompatibleToAnother(el_type, (DataType)((EAggregation_type)v_type).getElement_type(null), context, false)) {
			if (!elementsCompatibleToAggr((EAggregation_type)aggr_type, context, true)) {
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.BAG && 
						(v_tp_el_type.express_type == DataType.DATA_TYPE || v_tp_el_type.express_type == DataType.AGGREGATE)) {
					for (int i = 0; i < length; i++) {
						if (nested_values[i].tag != PhFileReader.EMBEDDED_LIST) {
							return false;
						}
						DataType member_v_tp = (DataType)nested_values[i].v_type;
						if (!oneCompatibleToAnother(el_type, member_v_tp, context, false)) {
							if (!nested_values[i].elementsCompatibleToAggr((EAggregation_type)el_type, context, true)) {
								return false;
							}
						}
					}
//System.out.println(" Value +++++++++++++++++++++++++++  length: " + length);
				} else {
					return false;
				}
			}
		}
//System.out.println(" SchemaData   el_type: " + el_type);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.NUMBER && el_type.express_type <= DataType.BINARY) {
			take_aggr_method_simple(el_type, 1);
			return true;
		} else if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type.express_type == DataType.ENTITY) {
			aux = AENTITY;
			return true;
		} else if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			aux =A_ENUMERATION;
			return true;
		} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else if (el_type.express_type == DataType.DATA_TYPE) {
			if (((EData_type)el_type).getName(null).equals("_ENTITY")) {
				aux =AENTITY;
				return true;
			}
			return true;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		el_type = (DataType)next_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.NUMBER && el_type.express_type <= DataType.BINARY) {
			take_aggr_method_simple(el_type, 2);
			return true;
		} else if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type.express_type == DataType.ENTITY) {
			aux =AENTITY;
			return true;
		} else if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			aux =AA_ENUMERATION;
			return true;
		} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else if (el_type == ExpressTypes.GENERIC_TYPE) {
			return true;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		String base;
		el_type = (DataType)next_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) {
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.NUMBER && el_type.express_type <= DataType.BINARY) {
			take_aggr_method_simple(el_type, 3);
			return true;
		} else if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (el_type.express_type == DataType.ENTITY) {
			aux =AENTITY;
			return true;
		} else if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
			return false;
		} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		while (next_type != null) {
			el_type = (DataType)next_type.getElement_type(null);
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			next_type = null;
			if (el_type.express_type >= DataType.NUMBER && el_type.express_type <= DataType.BINARY) {
				printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
				return false;
			} else if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				next_type = (AggregationType)el_type;
			} else if (el_type.express_type == DataType.ENTITY) {
				aux =AENTITY;
				return true;
			} else if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
				return false;
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				aux =AMIXED;
				return true;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		return true;
	}

	private void take_aggr_method_simple(DataType simple, int nesting) 
			throws SdaiException {
		switch (simple.express_type) {
			case DataType.NUMBER:
				if (nesting == 1) {
					aux =A_DOUBLE;
				} else if (nesting == 2) {
					aux =AA_DOUBLE;
				} else if (nesting == 3) {
					aux =AAA_DOUBLE;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.INTEGER:
				if (nesting == 1) {
					aux =A_INTEGER;
				} else if (nesting == 2) {
					aux =AA_INTEGER;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.REAL:
				if (nesting == 1) {
					aux =A_DOUBLE;
				} else if (nesting == 2) {
					aux =AA_DOUBLE;
				} else if (nesting == 3) {
					aux =AAA_DOUBLE;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.BOOLEAN:
				if (nesting == 1) {
					aux =A_BOOLEAN;
				} else if (nesting == 2) {
					aux =AA_BOOLEAN;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.LOGICAL:
				if (nesting == 1) {
					aux =A_LOGICAL;
				} else if (nesting == 2) {
					aux =AA_LOGICAL;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.BINARY:
				if (nesting == 1) {
					aux =A_BINARY;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
			case DataType.STRING:
				if (nesting == 1) {
					aux =A_STRING;
				} else if (nesting == 2) {
					aux =AA_STRING;
				} else {
					String base = AdditionalMessages.EE_NASA;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				break;
		}
	}


/**
 * Writes the value wrapped in the object of <code>Value</code> submitted 
 * as a method's second parameter to the current object of this class provided 
 * the value to be written conforms to the data type declared, using, 
 * for example, {@link #alloc(EData_type) alloc(EData_type)} method, 
 * in the current object.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be written.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(SdaiContext, Object)
 * @see #set(Value)
 */
	public Value set(SdaiContext context, Value val) throws SdaiException {
//System.out.println("Value   val.v_type: " + val.v_type + 
//"   d_type: " + d_type + "   val.tag: " + val.tag);
//if (val.v_type==null &&  val.tag==54)
//System.out.println("Value  !!!!! val.getActualType(): " + val.getActualType() +
//"    val.getActualJavaType(): " + val.getActualJavaType() + 
//"    getDeclaredType(): " + getDeclaredType());
		if (val == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (aux != -999) {
			if (val.tag == COMPLEX_ENTITY_VALUE || val.tag == ENTITY_VALUE) {
				val = val.makeInstance(context);
			}
			int aux_sel = val.check_and_restore(context, d_type);
			if (aux_sel >= 0 && val.tag != PhFileReader.TYPED_PARAMETER) {
				SelectType sel_type = (SelectType)getSelect(d_type);
				CDefined_type [] row = sel_type.paths[aux_sel];
				Value current_val = this;
				for (int i = 0; i < row.length; i++) {
					current_val.d_type = row[i];
					current_val.v_type = row[i];
					current_val.string = row[i].getNameUpperCase();
					current_val.length = 1;
					current_val.tag = PhFileReader.TYPED_PARAMETER;
					if (current_val.nested_values == null) {
						current_val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
					}
					if (current_val.nested_values[0] == null) {
						current_val.nested_values[0] = new Value();
					}
					current_val = current_val.nested_values[0];
				}
				current_val.aux = -999;
				current_val.set(context, val);
				current_val.aux = 0;
				return this;
			}
		}

		tag = val.tag;
		v_type = val.v_type;
		switch (tag) {
			case PhFileReader.INTEGER:
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				integer = val.integer;
				break;
			case PhFileReader.REAL:
				real = val.real;
				break;
			case PhFileReader.ENUM:
			case PhFileReader.STRING:
				string = val.string;
				reference = val.reference;
			case PhFileReader.BINARY:
				reference = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				string = val.string;
				length = 1;
				if (nested_values == null) {
					nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (nested_values[0] == null) {
					nested_values[0] = new Value();
				}
				nested_values[0].d_type = val.nested_values[0].d_type;
				nested_values[0].v_type = val.nested_values[0].v_type;
//System.out.println("Value IIIIIIIII val.d_type: " + val.d_type);
//System.out.println("Value IIIIIIIII val.v_type: " + val.v_type + "    d_type: " + d_type);
//System.out.println("Value IIIIIIIII val.nested_values[0].d_type: " + val.nested_values[0].d_type);
//System.out.println("Value IIIIIIIII val.nested_values[0].v_type: " + val.nested_values[0].v_type);
				nested_values[0].set(context, val.nested_values[0]);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				reference = val.reference;
				break;
			case PhFileReader.EMBEDDED_LIST:
/*if (prnt) System.out.println("Value IIIIIIIII old v_type: " + v_type +
"  new v_type: " + d_type);*/
/*if (prnt)*/ //v_type = d_type; // inserted 2007-03-16, GP
				agg_owner = val.agg_owner;
				if (nested_values == null || val.length > nested_values.length) {
					enlarge_nested_values(val.length);
				}
				length = val.length;
				for (int i = 0; i < val.length; i++) {
					if (nested_values[i] == null) {
						nested_values[i] = new Value();
					}
					if (v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || v_type == ExpressTypes.LIST_GENERIC_TYPE || 
							v_type == ExpressTypes.SET_GENERIC_TYPE || v_type == ExpressTypes.BAG_GENERIC_TYPE) {
//						nested_values[i].d_type = (EData_type)((EAggregation_type)d_type).getElement_type(null);
						nested_values[i].d_type = (EData_type)((EAggregation_type)v_type).getElement_type(null);
					} else {
						nested_values[i].d_type = (EData_type)((EAggregation_type)v_type).getElement_type(null);
					}
					nested_values[i].v_type = val.nested_values[i].v_type;
					nested_values[i].set(context, val.nested_values[i]);
				}
				break;
			default:
		}
		return this;
	}


/**
 * Writes the value wrapped in the object of <code>Value</code> submitted 
 * as a method's parameter to the current object of this class without 
 * checking whether the value to be written conforms to the data type 
 * declared for the current <code>Value</code> object. Upon termination of the method's 
 * run the declared and actual data types of the latter become equivalent to those of the 
 * supplied parameter. 
 * <p> This method is used in special cases when dealing with Express functions. 
 *	It is invoked in Express compiler generated classes.
 * @param val the value to be written.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(SdaiContext, Value)
 * @see #set(SdaiContext, Object)
 * @since 3.6.0
 */
	public Value set(Value val) throws SdaiException {
		if (val == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (aux != -999 && tag != INDETERMINATE) {
			int aux_sel = val.only_restore(d_type);
			if (aux_sel >= 0 && val.tag != PhFileReader.TYPED_PARAMETER) {
				SelectType sel_type = (SelectType)getSelect(d_type);
				CDefined_type [] row = sel_type.paths[aux_sel];
				Value current_val = this;
				for (int i = 0; i < row.length; i++) {
					current_val.d_type = row[i];
					current_val.v_type = row[i];
					current_val.string = row[i].getNameUpperCase();
					current_val.length = 1;
					current_val.tag = PhFileReader.TYPED_PARAMETER;
					if (current_val.nested_values == null) {
						current_val.nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
					}
					if (current_val.nested_values[0] == null) {
						current_val.nested_values[0] = new Value();
					}
					current_val = current_val.nested_values[0];
				}
				current_val.aux = -999;
				current_val.set(val);
				current_val.aux = 0;
				return this;
			}
		}

		tag = val.tag;
		v_type = val.v_type;
		switch (tag) {
			case PhFileReader.INTEGER:
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				integer = val.integer;
				break;
			case PhFileReader.REAL:
				real = val.real;
				break;
			case PhFileReader.ENUM:
			case PhFileReader.STRING:
				string = val.string;
				reference = val.reference;
			case PhFileReader.BINARY:
				reference = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				string = val.string;
				length = 1;
				if (nested_values == null) {
					nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (nested_values[0] == null) {
					nested_values[0] = new Value();
				}
				nested_values[0].d_type = val.nested_values[0].d_type;
				nested_values[0].v_type = val.nested_values[0].v_type;
				nested_values[0].set(val.nested_values[0]);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				reference = val.reference;
				break;
			case PhFileReader.EMBEDDED_LIST:
//				v_type = d_type; // inserted 2007-03-16, GP
				agg_owner = val.agg_owner;
				if (nested_values == null || val.length > nested_values.length) {
					enlarge_nested_values(val.length);
				}
				length = val.length;
				for (int i = 0; i < val.length; i++) {
					if (nested_values[i] == null) {
						nested_values[i] = new Value();
					}
					if (v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || v_type == ExpressTypes.LIST_GENERIC_TYPE || 
							v_type == ExpressTypes.SET_GENERIC_TYPE || v_type == ExpressTypes.BAG_GENERIC_TYPE) {
						nested_values[i].d_type = (EData_type)((EAggregation_type)d_type).getElement_type(null);
					} else {
						nested_values[i].d_type = (EData_type)((EAggregation_type)v_type).getElement_type(null);
					}
//					nested_values[i].d_type = val.nested_values[i].d_type;
					nested_values[i].v_type = val.nested_values[i].v_type;
					nested_values[i].set(val.nested_values[i]);
				}
				break;
			case COMPLEX_ENTITY_VALUE:
			case ENTITY_VALUE:
				reference = val.reference;
				break;
			default:
		}
		return this;
	}


/**
 * Writes the value submitted as a method's second parameter to the 
 * current object of this class provided 
 * the value to be written conforms to the data type declared, using, 
 * for example, {@link #alloc(EData_type) alloc(EData_type)} method, 
 * in the current object.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be written.
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #set(SdaiContext, Value)
 * @see #set(Value)
 */
	public Value set(SdaiContext context, Object val) throws SdaiException {
		if (val instanceof EEntity) {
			return set(context, (EEntity) val);
		} else
		if (val instanceof Aggregate) {
			return set(context, (Aggregate) val);
		} else
		if (val instanceof String) {
			return set(context, (String) val);
		} else
		if (val instanceof Binary) {
			return set(context, (Binary) val);
		} else
		if (val instanceof Integer) {
			return set(context, ((Integer)val).intValue());
		} else
		if (val instanceof Double) {
			return set(context, ((Double)val).doubleValue());
		} else
		if (val instanceof Value) {
			// not in select, but why not?
			return set(context, (Value)val);
		} else {
			// what is missing? - perhaps just val = null - could be checked additionally
			return unset();
		}
	}



/**
 * Wraps a value of integer type in an object of this class. 
 * Unset value to be supplied to the method is also allowed. 
 * The type of the value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, int val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		boolean int_val = true;
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_integer_in_select((ESelect_type)type)) {
				printWarningToLogoValidate(AdditionalMessages.EE_INNF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (type.express_type == DataType.INTEGER) {
			v_type = type;
		} else if (type.express_type == DataType.REAL) {
			v_type = type;
			tag = PhFileReader.REAL;
			real = val;
			int_val = false;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_INNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (int_val) {
			if (val == Integer.MIN_VALUE) {
				tag = INDETERMINATE;
			} else {
				tag = PhFileReader.INTEGER;
				integer = val;
			}
		}
		return this;
	}
	private boolean check_integer_in_select(ESelect_type type) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.INTEGER) {
				v_type = type;
				return true;
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_integer_in_select((ESelect_type)el_type);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.INTEGER) {
					v_type = type;
					return true;
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_integer_in_select((ESelect_type)el_type);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.INTEGER) {
//				v_type = type;
//				return true;
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_integer_in_select((ESelect_type)el_type);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps a value of real type in an object of this class.
 * Unset value to be supplied to the method is also allowed. 
 * The type of the value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, double val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_real_in_select((ESelect_type)type)) {
				printWarningToLogoValidate(AdditionalMessages.EE_DONF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.REAL_TYPE;
		} else if (type.express_type != DataType.REAL && type.express_type != DataType.NUMBER) {
			printWarningToLogoValidate(AdditionalMessages.EE_DONF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			v_type = type;
		}
		if (Double.isNaN(val)) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.REAL;
			real = val;
		}
		return this;
	}
	private boolean check_real_in_select(ESelect_type type) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.REAL || el_type.express_type == DataType.NUMBER) {
				v_type = type;
				return true;
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_real_in_select((ESelect_type)el_type);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.REAL || el_type.express_type == DataType.NUMBER) {
					v_type = type;
					return true;
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_real_in_select((ESelect_type)el_type);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.REAL || el_type.express_type == DataType.NUMBER) {
//				v_type = type;
//				return true;
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_real_in_select((ESelect_type)el_type);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps a value of string type in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, String val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_string_in_select((ESelect_type)type, val)) {
				printWarningToLogoValidate(AdditionalMessages.EE_STNF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.STRING_TYPE;
		} else if (type.express_type != DataType.STRING) {
			printWarningToLogoValidate(AdditionalMessages.EE_STNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			if (val != null && !checkStringValue((EString_type)type, val)) {
				printWarningToLogoValidate(AdditionalMessages.EE_STNF);
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			v_type = type;
		}
		if (val == null) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.STRING;
			string = val;
		}
		return this;
	}


/**
 * Wraps a value of string type in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * This method also stores the name of the schema in which 
 * the string (for example, the name of an entity data type) is defined.
 * The method is invoked in compiler generated code implementing Express expressions.
 * @param context an object of type <code>SdaiContext</code>.
 * @param val string value to be assigned.
 * @param schema_name the name of the schema.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, String val, String schema_name) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;

		if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.STRING_TYPE;
		} else if (type.express_type != DataType.STRING) {
			printWarningToLogoValidate(AdditionalMessages.EE_STNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			v_type = type;
		}
		if (val == null) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.STRING;
			string = val;
		}
		reference = schema_name;
		return this;
	}


	private boolean check_string_in_select(ESelect_type type, String val) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.STRING) {
				if (val == null || checkStringValue((EString_type)el_type, val)) {
					v_type = type;
					return true;
				}
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_string_in_select((ESelect_type)el_type, val);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.STRING) {
					if (val == null || checkStringValue((EString_type)el_type, val)) {
						v_type = type;
						return true;
					}
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_string_in_select((ESelect_type)el_type, val);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.STRING) {
//				if (val == null || checkStringValue((EString_type)el_type, val)) {
//					v_type = type;
//					return true;
//				}
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_string_in_select((ESelect_type)el_type, val);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}
	private boolean checkStringValue(EString_type str_type, String val) throws SdaiException {
		if (str_type.testWidth(null)) {
			EBound bound = str_type.getWidth(null);
			int sym_bound = bound.getBound_value(null);
			int sym_count = val.length();
			if (str_type.getFixed_width(null)) {
				if (sym_count == sym_bound) {
					return true;
				} else {
					return false;
				}
			} else {
				if (sym_count <= sym_bound) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}


/**
 * Wraps a value of either logical or boolean type in an object of this class.
 * Unset value to be supplied to the method is also allowed. 
 * The following integers represent values of logical or boolean types:
 * <ul><li> 0 for unset case; 
 * <li> 1 for FALSE;
 * <li> 2 for TRUE;
 * <li> 3 for UNKNOWN.</ul>
 * The type of the value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @see #setLB(SdaiContext, Value)
 */
	public Value setLB(SdaiContext context, int val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_LB_in_select((ESelect_type)type)) {
				printWarningToLogoValidate(AdditionalMessages.EE_LONF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.LOGICAL_TYPE;
		} else if (type.express_type != DataType.LOGICAL && type.express_type != DataType.BOOLEAN) {
			printWarningToLogoValidate(AdditionalMessages.EE_LONF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			v_type = type;
		}
		if (val == 0) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.LOGICAL;
			integer = val - 1;
		}
		return this;
	}
	private boolean check_LB_in_select(ESelect_type type) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.LOGICAL || el_type.express_type == DataType.BOOLEAN) {
				v_type = type;
				return true;
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_LB_in_select((ESelect_type)el_type);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.LOGICAL || el_type.express_type == DataType.BOOLEAN) {
					v_type = type;
					return true;
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_LB_in_select((ESelect_type)el_type);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.LOGICAL || el_type.express_type == DataType.BOOLEAN) {
//				v_type = type;
//				return true;
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_LB_in_select((ESelect_type)el_type);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps a value of either logical or boolean type in an object of this class.
 * Unset value to be supplied to the method is also allowed. 
 * The following integers represent values of logical or boolean types:
 * <ul><li> 0 for unset case; 
 * <li> 1 for FALSE;
 * <li> 2 for TRUE;
 * <li> 3 for UNKNOWN.</ul>
 * The type of the value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @see #setLB(SdaiContext, int)
 */
	public Value setLB(SdaiContext context, Value val) throws SdaiException {
		if (val == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == INDETERMINATE) {
			return setLB(context, 0);
		} else if (val.tag == PhFileReader.LOGICAL || val.tag == PhFileReader.BOOLEAN || val.tag == PhFileReader.INTEGER) {
			if (val.integer < 0 || val.integer >2) {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_VNLO, val.tag);
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			return setLB(context, val.integer + 1);
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_VNLO, val.tag);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


/**
 * Wraps a value of enumeration type in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value setEnum(SdaiContext context, String val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_enum_in_select((ESelect_type)type, val, context)) {
				printWarningToLogoValidate(AdditionalMessages.EE_ENNF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type < DataType.ENUMERATION || type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			printWarningToLogoValidate(AdditionalMessages.EE_ENNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			if (val != null && !check_enumeration((EEnumeration_type)type, val, context)) {
				printWarningToLogoValidate(AdditionalMessages.EE_ENNF);
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			v_type = type;
		}
		if (val == null) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.ENUM;
			string = val;
		}
		return this;
	}
	private boolean check_enum_in_select(ESelect_type type, String val, SdaiContext context) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				if (val == null || check_enumeration((EEnumeration_type)el_type, val, context)) {
					v_type = type;
					return true;
				}
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_enum_in_select((ESelect_type)el_type, val, context);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
					if (val == null || check_enumeration((EEnumeration_type)el_type, val, context)) {
						v_type = type;
						return true;
					}
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_enum_in_select((ESelect_type)el_type, val, context);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
//				if (val == null || check_enumeration((EEnumeration_type)el_type, val, context)) {
//					v_type = type;
//					return true;
//				}
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_enum_in_select((ESelect_type)el_type, val, context);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}
	private boolean check_enumeration(EEnumeration_type enum_type, String val, SdaiContext context) throws SdaiException {
		A_string elements = enum_type.getElements(null, context);
		for (int i = 1; i <= elements.myLength; i++) {
			String item = elements.getByIndex(i);
			if (val.equalsIgnoreCase(item)) {
				return true;
			}
		}
		return false;
	}


/**
 * Wraps a value of binary type in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The value is checked against the data type attached to 
 * this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the value to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, Binary val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_binary_in_select((ESelect_type)type, val)) {
				printWarningToLogoValidate(AdditionalMessages.EE_BINF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE && 
				((EData_type)type).getName(null).equals("_GENERIC")) {
			v_type = ExpressTypes.BINARY_TYPE;
		} else if (type.express_type != DataType.BINARY) {
			printWarningToLogoValidate(AdditionalMessages.EE_BINF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			if (val != null && !val.checkBinaryValue((EBinary_type)type)) {
				printWarningToLogoValidate(AdditionalMessages.EE_BINF);
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			v_type = type;
		}
		if (val == null) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.BINARY;
			reference = val;
		}
		return this;
	}
	private boolean check_binary_in_select(ESelect_type type, Binary val) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.BINARY) {
				if (val == null || !val.checkBinaryValue((EBinary_type)el_type)) {
					v_type = type;
					return true;
				}
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_binary_in_select((ESelect_type)el_type, val);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.BINARY) {
					if (val == null || !val.checkBinaryValue((EBinary_type)el_type)) {
						v_type = type;
						return true;
					}
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_binary_in_select((ESelect_type)el_type, val);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.BINARY) {
//				if (val == null || !val.checkBinaryValue((EBinary_type)el_type)) {
//					v_type = type;
//					return true;
//				}
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_binary_in_select((ESelect_type)el_type, val);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps an entity instance in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The entity definition of the instance is checked against the data type 
 * attached to this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param val the entity instance to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, EEntity val) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_entity_in_select((ESelect_type)type, val, context)) {
				printWarningToLogoValidate(AdditionalMessages.EE_EINF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (type.express_type == DataType.DATA_TYPE) {
			v_type = val.getInstanceType();
//			v_type = ExpressTypes.GENERIC_ENTITY_TYPE;
		} else if (type.express_type != DataType.ENTITY) {
			printWarningToLogoValidate(AdditionalMessages.EE_EINF);
			throw new SdaiException(SdaiException.VT_NVLD);
		} else {
			if (val != null && !val.getInstanceType().isSubtypeOf((EEntity_definition)type)) {
				printWarningToLogoValidate(AdditionalMessages.EE_EINF);
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			v_type = val.getInstanceType();
		}
		if (val == null) {
			tag = INDETERMINATE;
		} else {
			tag = PhFileReader.ENTITY_REFERENCE;
			reference = val;
		}
		return this;
	}
	private boolean check_entity_in_select(ESelect_type type, EEntity val, SdaiContext context) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, context);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.ENTITY) {
				if (val == null || !val.getInstanceType().isSubtypeOf((EEntity_definition)el_type)) {
					v_type = val.getInstanceType();
					return true;
				}
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_entity_in_select((ESelect_type)el_type, val, context);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type == DataType.ENTITY) {
					if (val == null || !val.getInstanceType().isSubtypeOf((EEntity_definition)el_type)) {
						v_type = val.getInstanceType();
						return true;
					}
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_entity_in_select((ESelect_type)el_type, val, context);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type == DataType.ENTITY) {
//				if (val == null || !val.getInstanceType().isSubtypeOf((EEntity_definition)el_type)) {
//					v_type = val.getInstanceType();
//					return true;
//				}
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_entity_in_select((ESelect_type)el_type, val, context);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps an aggregate (also nested) in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The aggregation type of the aggregate submitted is checked against the data 
 * type attached to this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param aggr the aggregate to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public Value set(SdaiContext context, Aggregate aggr) throws SdaiException {
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		AggregationType tp = null;
		if (aggr != null) {
			tp = (AggregationType)aggr.getAggregationType();
		}
		DataType type = (DataType)d_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			if (!check_aggregate_in_select((ESelect_type)type, tp)) {
				printWarningToLogoValidate(AdditionalMessages.EE_AGNF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else if (!(type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE && 
				(tp == null || type == tp || 
					(type == ExpressTypes.LIST_GENERIC_TYPE && (tp.express_type == DataType.LIST)) || 
					(type == ExpressTypes.SET_GENERIC_TYPE && (tp.express_type == DataType.SET)) || 
					(type == ExpressTypes.BAG_GENERIC_TYPE && ((tp.express_type == DataType.BAG) || (tp.express_type == DataType.SET))) || 
					(type == ExpressTypes.AGGREGATE_GENERIC_TYPE) ))) {
//System.out.println("Value    d_type: " + d_type + "    type: " + type +
//"   tp: " + tp);
			printWarningToLogoValidate(AdditionalMessages.EE_AGNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		v_type = tp;
		if (aggr == null || tp == null) {
			tag = INDETERMINATE;
			return this;
		}
//		v_type = d_type; // inserted 2007-03-16, GP
		agg_owner = ((A_basis)aggr).getOwner();
		tag = PhFileReader.EMBEDDED_LIST;
		length = aggr.getMemberCount();
		if (nested_values == null || length > nested_values.length) {
			enlarge_nested_values(length);
		}
		EData_type el_type;
		if (tp.shift == SdaiSession.ASDAIMODEL_INST) {
			el_type = ExpressTypes.GENERIC_TYPE;
		} else {
			el_type = (EData_type)((EAggregation_type)tp).getElement_type(null);
		}
//		EData_type el_type = (EData_type)((EAggregation_type)tp).getElement_type(null);
		for (int i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
				nested_values[i].init(el_type);
			}
		}
		reference = aggr;
		if (aggr instanceof CAggregate) {
			CAggregate agg = (CAggregate)aggr;
			if (agg instanceof A_string) {
				((A_string)agg).getValue(this, tp, context);
			} else if (agg instanceof A_binary) {
				((A_binary)agg).getValue(this, tp, context);
			} else if (tp.select != null) {
				if (tp.select.is_mixed == 0) {
					agg.getValue(this, tp, 1);
				} else {
					agg.getValue(this, tp, 2);
				}
			} else {
				agg.getValue(this, tp, 0);
			}
		} else if (aggr instanceof A_integer) {
			((A_integer)aggr).getValue(this, tp, context);
		} else if (aggr instanceof A_boolean) {
			((A_boolean)aggr).getValue(this, tp, context);
		} else if (aggr instanceof A_enumeration) {
			((A_enumeration)aggr).getValue(this, tp, context);
		} else if (aggr instanceof A_double3) {
			((A_double3)aggr).getValue(this, tp, context);
		} else if (aggr instanceof A_double) {
			((A_double)aggr).getValue(this, tp, context);
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_AGNF);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return this;
	}
	private boolean check_aggregate_in_select(ESelect_type type, AggregationType tp) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, null);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE && (tp == null || el_type == tp)) {
				return true;
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return check_aggregate_in_select((ESelect_type)el_type, tp);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE && (tp == null || el_type == tp)) {
					return true;
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					boolean res = check_aggregate_in_select((ESelect_type)el_type, tp);
					if (res) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE && (tp == null || el_type == tp)) {
//				return true;
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				boolean res = check_aggregate_in_select((ESelect_type)el_type, tp);
//				if (res) {
//					return res;
//				}
//			}
//		}
		return false;
	}


/**
 * Wraps an aggregate of entity instances in an object of this class.
 * <code>null</code> as an unset value is also allowed. 
 * The aggregation type of the aggregate submitted is checked against the data 
 * type attached to this object of class <code>Value</code>.
 * @param context context in which the value is checked for conformance. 
 * @param aggr the aggregate to be assigned.
 * @return the current object of this class.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public Value setInstancesAggregate(SdaiContext context, AEntity aggr) throws SdaiException {
		if (aggr == null) {
			tag = INDETERMINATE;
			return this;
		}
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_UNVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int d_tp = ((DataType)d_type).express_type;
		if (d_tp < DataType.LIST || d_tp > DataType.AGGREGATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_IANF);
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		DataType el_type = (DataType)((EAggregation_type)d_type).getElement_type(null);
		if (el_type.express_type != DataType.ENTITY && el_type != ExpressTypes.GENERIC_TYPE) {
			printWarningToLogoValidate(AdditionalMessages.EE_IANF);
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		v_type = d_type;
		agg_owner = ((A_basis)aggr).getOwner();
		tag = PhFileReader.EMBEDDED_LIST;
		length = aggr.getMemberCount();
		if (nested_values == null || length > nested_values.length) {
			enlarge_nested_values(length);
		}
		for (int i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
				nested_values[i].init(el_type);
			}
		}
		reference = aggr;
		aggr.getValueEntity(this, context);
		return this;
	}


/**
 * Makes an object of this class representing no value, that is, 
 * attaches INDETERMINATE to it.
 * @return the current object of this class.
 */
	public Value unset() {
		tag = INDETERMINATE;
		v_type = null;
		return this;
	}


/**
 * Implements <code>Aggregate indexing</code>, <code>String indexing</code> and 
 * <code>Binary indexing</code> operators of Express language. 
 * An aggregate or string or binary, depending on the operator, is given 
 * by the current object of type <code>Value</code>, while indices are given by 
 * objects of type <code>Value</code> submitted through parameters. 
 * For <code>Aggregate indexing</code>, the member of the aggregate at the 
 * position specified by the first method's parameter is returned. The second 
 * parameter is ignored.
 * For <code>String indexing</code> and <code>Binary indexing</code>, the 
 * sequence of characters or, respectively, bits at position <code>index1</code> 
 * through <code>index2</code> inclusive is taken as a result.
 * The value to be returned is wrapped in a newly created object of this class.
 * @param index1 index of the aggregate member asked or position of the first 
 * character or bit in the case of string and, respectively, binary indexing.
 * @param index2 position of the last character or bit in string and, 
 * respectively, binary indexing.
 * @return the object of this class wrapping either aggregate member or substring 
 * or subbinary.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.3.1 Binary indexing"
 * @see "ISO 10303-11::12.5.1 String indexing"
 * @see "ISO 10303-11::12.6.1 Aggregate indexing"
 * @see #indexing(int, int)
 */
	public Value indexing(Value index1, Value index2) throws SdaiException {
		if (index1 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_INNP);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		int ind;
		Value val;
		if (index1.tag != PhFileReader.INTEGER) {
	 		val = Value.alloc();
			val.tag = INDETERMINATE;
			if (tag == PhFileReader.EMBEDDED_LIST) {
				val.d_type = ((EAggregation_type)v_type).getElement_type(null);
			} else if (tag == PhFileReader.MISSING && d_type != null && 
					((DataType)d_type).express_type >= DataType.LIST && ((DataType)d_type).express_type <= DataType.AGGREGATE) {
				val.d_type = ((EAggregation_type)d_type).getElement_type(null);
			}
			return val;
		}
		if (index2 == null) {
			ind = Integer.MIN_VALUE;
		} else {
			if (index2.tag != PhFileReader.INTEGER) {
	 			val = Value.alloc();
				val.tag = INDETERMINATE;
				if (tag == PhFileReader.EMBEDDED_LIST) {
					val.d_type = ((EAggregation_type)v_type).getElement_type(null);
				} else if (tag == PhFileReader.MISSING && d_type != null && 
						((DataType)d_type).express_type >= DataType.LIST && ((DataType)d_type).express_type <= DataType.AGGREGATE) {
					val.d_type = ((EAggregation_type)d_type).getElement_type(null);
				}
				return val;
			}
			ind = index2.integer;
		}
		return indexing(index1.integer, ind);
	}


/**
 * Implements <code>Aggregate indexing</code>, <code>String indexing</code> and 
 * <code>Binary indexing</code> operators of Express language. 
 * An aggregate or string or binary, depending on the operator, is given 
 * by the current object of type <code>Value</code>. 
 * For <code>Aggregate indexing</code>, the member of the aggregate at the 
 * position specified by the first method's parameter is returned. The second 
 * parameter is ignored.
 * For <code>String indexing</code> and <code>Binary indexing</code>, the 
 * sequence of characters or, respectively, bits at position <code>index1</code> 
 * through <code>index2</code> inclusive is taken as a result.
 * The value to be returned is wrapped in a newly created object of this class.
 * @param index1 index of the aggregate member asked or position of the first 
 * character or bit in the case of string and, respectively, binary indexing.
 * @param index2 position of the last character or bit in string and, 
 * respectively, binary indexing.
 * @return the object of this class wrapping either aggregate member or substring 
 * or subbinary.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.3.1 Binary indexing"
 * @see "ISO 10303-11::12.5.1 String indexing"
 * @see "ISO 10303-11::12.6.1 Aggregate indexing"
 * @see #indexing(Value, Value)
 */
	public Value indexing(int index1, int index2) throws SdaiException {
		Value val;
		if (tag == PhFileReader.TYPED_PARAMETER) {
			return nested_values[0].indexing(index1, index2);
		}
		if (tag == PhFileReader.EMBEDDED_LIST) {
			if (index2 != Integer.MIN_VALUE) {
	 			val = Value.alloc();
				val.d_type = ((EAggregation_type)v_type).getElement_type(null);
				val.tag = INDETERMINATE;
				return val;
			}
			if (v_type == null) {
				printWarningToLogoValidate(AdditionalMessages.EE_INNA);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			int v_tp = ((DataType)v_type).express_type;
			if (v_tp < DataType.LIST || v_tp > DataType.AGGREGATE) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_INNA, v_type);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			int ind;
			if (v_tp == DataType.ARRAY) {
				EArray_type arr_tp = (EArray_type)v_type;
				int lb = arr_tp.getLower_index(null).getBound_value(null);
				int ub = arr_tp.getUpper_index(null).getBound_value(null);
				if (index1 < lb || index1 > ub) {
	 				val = Value.alloc();
					val.d_type = ((EAggregation_type)v_type).getElement_type(null);
					val.tag = INDETERMINATE;
					return val;
				}
				ind = index1 - lb;
				if (ind >= length) {
	 				val = Value.alloc();
					val.d_type = ((EAggregation_type)v_type).getElement_type(null);
					val.tag = INDETERMINATE;
					return val;
				}
				return nested_values[ind];
			}
			if (index1 > length || index1 <= 0) {
	 			val = Value.alloc();
				val.d_type = ((EAggregation_type)v_type).getElement_type(null);
				val.tag = INDETERMINATE;
				return val;
			}
			return nested_values[index1 - 1];
		} else if (tag == PhFileReader.STRING) {
	 		val = Value.alloc();
			val.d_type = d_type;
			if (index2 == Integer.MIN_VALUE) {
				val.substring(this, index1);
			} else {
				val.substring(this, index1, index2);
			}
			return val;
		} else if (tag == PhFileReader.BINARY) {
	 		val = Value.alloc();
			val.d_type = d_type;
			val.subbinary(this, index1, index2);
			return val;
		} else {
	 		val = Value.alloc();
			val.tag = INDETERMINATE;
			if (d_type != null && ((DataType)d_type).express_type >= DataType.LIST && 
					((DataType)d_type).express_type <= DataType.AGGREGATE) {
				val.d_type = ((EAggregation_type)d_type).getElement_type(null);
			}
			return val;
		}
	}


/**
 * Implements assignment statement when the object being assigned to is 
 * range qualified. This case is possible only for Express STRING and 
 * BINARY data types (see ISO 10303-11::13.3.2 Assignment compatibility). 
 * The string or binary to the left of the range qualifier is given 
 * by the current object of type <code>Value</code>, while indices are given by 
 * objects of type <code>Value</code> submitted through the first two parameters. 
 * The value of the expression to the right of the assignment statement 
 * is submitted through the last parameter. This value replaces the 
 * elements originally between specified indices (inclusively). 
 * For example, the result of the expression 'max_noteq_sat'[4:10] := '2' is 'max2sat'.
 * The result of the statement execution is wrapped into the current object of this class.
 * If the type of the data represented by this object differs from 
 * STRING and BINARY, then the method ignores the last parameter and 
 * makes a call to {@link #indexing(Value, Value)}. 
 * @param index1 index of the first element of STRING or BINARY to be replaced.
 * @param index2 index of the last element of STRING or BINARY to be replaced. 
 * @param repl value which replaces the specified part of STRING or BINARY.
 * @return this object of <code>Value</code> wrapping the result of the replacement operation. 
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::13.3.2 Assignment compatibility"
 * @see #replaceRange(int, int, Value)
 * @since 4.1.2
 */
	public Value replaceRange(Value index1, Value index2, Value repl) throws SdaiException {
		if (tag == INDETERMINATE) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag != PhFileReader.STRING && tag != PhFileReader.BINARY && tag != PhFileReader.TYPED_PARAMETER) {
			return indexing(index1, index2);
		}
		if (tag == PhFileReader.TYPED_PARAMETER) {
			return nested_values[0].replaceRange(index1, index2, repl);
		}
		if (index1 == null || index2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_INNP);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (index1.tag != PhFileReader.INTEGER || index2.tag != PhFileReader.INTEGER) {
	 		Value val = Value.alloc();
			val.tag = INDETERMINATE;
			return val;
		}
		return replaceRange(index1.integer, index2.integer, repl);
	}


/**
 * Implements assignment statement when the object being assigned to is 
 * range qualified. This case is possible only for Express STRING and 
 * BINARY data types (see ISO 10303-11::13.3.2 Assignment compatibility). 
 * The string or binary to the left of the range qualifier is given 
 * by the current object of type <code>Value</code>, while indices are given by 
 * the first two parameters. 
 * The value of the expression to the right of the assignment statement 
 * is submitted through the last parameter. This value replaces the 
 * elements originally between specified indices (inclusively). 
 * For example, the result of the expression 'max_noteq_sat'[4:10] := '2' is 'max2sat'.
 * The result of the statement execution is wrapped into the current object of this class.
 * If the type of the data represented by this object differs from 
 * STRING and BINARY, then the method ignores the last parameter and 
 * makes a call to {@link #indexing(int, int)}. 
 * @param index1 index of the first element of STRING or BINARY to be replaced.
 * @param index2 index of the last element of STRING or BINARY to be replaced. 
 * @param repl value which replaces the specified part of STRING or BINARY.
 * @return this object of <code>Value</code> wrapping the result of the replacement operation. 
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::13.3.2 Assignment compatibility"
 * @see #replaceRange(Value, Value, Value)
 * @since 4.1.2
 */
	public Value replaceRange(int index1, int index2, Value repl) throws SdaiException {
		if (tag == INDETERMINATE) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag != PhFileReader.STRING && tag != PhFileReader.BINARY && tag != PhFileReader.TYPED_PARAMETER) {
			return indexing(index1, index2);
		}
		if (tag == PhFileReader.TYPED_PARAMETER) {
			return nested_values[0].replaceRange(index1, index2, repl);
		}
		if (tag == PhFileReader.STRING) {
			if (repl.tag != PhFileReader.STRING) {
				return this;
			}
			String str;
			if (this.string==SdaiSession.asterisk && reference instanceof String) {
				str = (String)reference;
			} else {
				str = this.string;
			}
			int ln = str.length();
			if (index1 < 1 || index2 < index1 || index2 > ln) {
				tag = INDETERMINATE;
				if (d_type == null) {
					d_type = ExpressTypes.STRING_TYPE;
				}
				return this;
			}
			String pref = str.substring(0,index1 - 1);
			String suff = str.substring(index2);
			string = pref + repl.string + suff;
			return this;
		} else if (tag == PhFileReader.BINARY) {
			if (repl.tag != PhFileReader.BINARY) {
				return this;
			}
			Binary bin = (Binary)reference;
			int ln_b = bin.getSize();
			StaticFields staticFields = StaticFields.get();
			int count = bin.subbinary(staticFields, 1, index1 - 1);
			if (count < 0) {
				tag = INDETERMINATE;
				if (d_type == null) {
					d_type = ExpressTypes.BINARY_TYPE;
				}
				return this;
			}
			Binary pref_b = new Binary(staticFields.result, count);
			count = bin.subbinary(staticFields, index1 + 1, ln_b);
			if (count < 0) {
				tag = INDETERMINATE;
				if (d_type == null) {
					d_type = ExpressTypes.BINARY_TYPE;
				}
				return this;
			}
			Binary suff_b = new Binary(staticFields.result, count);
			count = pref_b.concatenation(staticFields, (Binary)repl.reference);
			Binary res = new Binary(staticFields.result, count);
			count = res.concatenation(staticFields, suff_b);
			reference =  new Binary(staticFields.result, count);
			return this;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_DTNA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


/**
 * Increments the value of integer type wrapped by an object of this class by a 
 * specified integer.
 * @param delta positive integer value (given by an object of type <code>Value</code>) by 
 * which a wrapped integer is enlarged.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #inc
 */
	public Value inc(Value delta) throws SdaiException {
		if (tag == PhFileReader.TYPED_PARAMETER) {
			nested_values[0].inc(delta);
			return this;
		}
		if (delta == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_INZE);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag == PhFileReader.INTEGER) {
			if (delta.tag == PhFileReader.INTEGER) {
				integer += delta.integer;
			} else if (delta.tag == PhFileReader.REAL) {
				integer += delta.real;
			} else {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_INTI, delta.tag);
				String base = "Method inc is allowed only for values of numeric type.";
				throw new SdaiException(SdaiException.VT_NVLD, base);
			}
		} else if (tag == PhFileReader.REAL) {
			if (delta.tag == PhFileReader.INTEGER) {
				real += delta.integer;
			} else if (delta.tag == PhFileReader.REAL) {
				real += delta.real;
			} else {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_INTI, delta.tag);
				String base = "Method inc is allowed only for values of numeric type.";
				throw new SdaiException(SdaiException.VT_NVLD, base);
			}
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_IVTI, tag);
			String base = "Method inc is allowed only for values of numeric type.";
			throw new SdaiException(SdaiException.VT_NVLD, base);
		}
		return this;
	}



/**
 * Increments the value of integer type wrapped by an object of this class by 1. 
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #inc(Value)
 */
	public Value inc() throws SdaiException {
		if (tag != PhFileReader.INTEGER) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_IVTI, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		integer++;
		return this;
	}


/**
 * Decreases the value of integer type wrapped by an object of this class by a 
 * specified integer.
 * @param delta positive integer value (given by an object of type <code>Value</code>) by 
 * which a wrapped integer is diminished.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #dec
 */
	public Value dec(Value delta) throws SdaiException {
		if (delta == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_DEZE);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag != PhFileReader.INTEGER || delta.tag != PhFileReader.INTEGER) {
			if (tag != PhFileReader.INTEGER) {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_DVTI, tag);
			} else {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_DETI, delta.tag);
			}
			throw new SdaiException(SdaiException.SY_ERR);
		}
		integer -= delta.integer;
		return this;
	}

/**
 * Decreases the value of integer type wrapped by an object of this class by 1. 
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #dec(Value)
 */
	public Value dec() throws SdaiException {
		if (tag != PhFileReader.INTEGER) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_DVTI, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		integer--;
		return this;
	}


/**
 * Converts logical value represented by an integer to boolean 
 * according to the following rule:
 * if TRUE (integer value 2), then <code>true</code>, 
 * if FALSE or UNKNOWN (integer values 1 and 3, respectively), then 
 * <code>false</code> is returned.
 * This method is disallowed for data types different than LOGICAL 
 * and BOOLEAN.
 * @return boolean value for integer representation of LOGICAL.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public boolean getAsBoolean() throws SdaiException {
		if (tag != PhFileReader.LOGICAL && tag != PhFileReader.BOOLEAN) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_VNLO, tag);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (integer == 1) {
			return true;
		} else {
			return false;
		}
	}


/**
 * Adds a new member to the aggregate represented by an object of this class. 
 * Its position is at the end of the aggregate.
 * @param context context in which the new member is checked for conformance. 
 * @param val an element (wrapped in an object of type <code>Value</code>) to  
 * be added.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public void addMember(SdaiContext context, Value val) throws SdaiException {
		setMember(context, val, length + 1);
	}


/**
 * Assigns a new value to a member of the aggregate represented by an object of this 
 * class located at the specified index position. The indexing of members starts from 1.
 * @param context context in which the new value is checked for conformance. 
 * @param val a value (wrapped in an object of type <code>Value</code>) to  
 * be set.
 * @param index a position for the member whose value is set.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public void setMember(SdaiContext context, Value val, int index) throws SdaiException {
		if (tag != PhFileReader.EMBEDDED_LIST) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_VNAG, tag);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == COMPLEX_ENTITY_VALUE || val.tag == ENTITY_VALUE) {
			val = val.makeInstance(context);
		}
		if (v_type != null) {
			if (!check_if_fits((EAggregation_type)v_type, val, context)) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_VANF, v_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else {
			v_type = find_best_fit_for_value(d_type, val, context);
			if (v_type == null) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_VANF, d_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		}
		if (nested_values == null || index > nested_values.length) {
			enlarge_and_save_nested_values(index);
		}
		if (index > length) {
			length = index;
		}
		index--;
		nested_values[index] = val;
	}
	private boolean check_if_fits(EAggregation_type aggr_type, Value val, SdaiContext context) throws SdaiException {
/*System.out.println("  Value    aggregate: #" + ((CEntity)aggr_type).instance_identifier);
CEntity eee = (CEntity)aggr_type;
System.out.println("  Value    aggregate: " + eee);
System.out.println("  Value    aggregate: " + aggr_type.getName(null));*/
		EData_type type = (EData_type)aggr_type.getElement_type(null);
		if (type == ExpressTypes.GENERIC_TYPE) {
			return true;
		}
		boolean res = val.check_type_to_fit(type, context);
		if (res) {
			val.d_type = type;
		}
		return res;
	}
	private boolean check_type_to_fit(EData_type type, SdaiContext context) throws SdaiException {
		if (type == null) {
			return false;
		}
		if (type == v_type) {
			return true;
		}
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			int tp = ((DataType)type).express_type;
			if (tp != DataType.DEFINED_TYPE && (tp < DataType.SELECT || tp > DataType.ENT_EXT_EXT_SELECT) ) {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_VANF, tp);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			if (d_type == null) {
				printWarningToLogoValidate(AdditionalMessages.EE_VANF);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			int d_tp = ((DataType)d_type).express_type;
			if (tag != INDETERMINATE && 
					(d_tp == DataType.DEFINED_TYPE || (d_tp >= DataType.SELECT && d_tp <= DataType.ENT_EXT_EXT_SELECT)) ) {
				return analyse_defined_type(type);
			}
			if (tag == INDETERMINATE) {
				return true;
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			EData_type part_type;
			if (sels.myLength == 1) {
				part_type = (EData_type)sels.myData;
				return check_type_to_fit(part_type, context);
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					part_type = (EData_type)myDataA[i];
					boolean res = check_type_to_fit(part_type, context);
					if (res) {
						return true;
					}
				}
			}
//			for (int i = 1; i <= sels.myLength; i++) {
//				EData_type part_type = (EData_type)sels.getByIndexEntity(i);
//				boolean res = check_type_to_fit(part_type, context);
//				if (res) {
//					return true;
//				}
//			}
			return false;
		}
		return analyse_Value(type, context);
	}
	private EAggregation_type find_best_fit_for_value(EData_type type, Value val, SdaiContext context) 
			throws SdaiException {
		if (type == null) {
			return null;
		}
		DataType tp = (DataType)type;
		while (tp.express_type == DataType.DEFINED_TYPE) {
			tp = (DataType)((CDefined_type)tp).getDomain(null);
		}
		if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
			if (check_if_fits((EAggregation_type)tp, val, context)) {
				return (EAggregation_type)tp;
			}
		} else if (tp.express_type >= DataType.SELECT && tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			return find_best_fit_in_select((ESelect_type)tp, val, context);
		}
		return null;
	}
	private EAggregation_type find_best_fit_in_select(ESelect_type type, Value val, SdaiContext context) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, context);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		DataType el_type;
		if (sels.myLength == 1) {
			el_type = (DataType)sels.myData;
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
				if (check_if_fits((EAggregation_type)el_type, val, context)) {
					return (EAggregation_type)el_type;
				}
			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				return find_best_fit_in_select((ESelect_type)el_type, val, context);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				el_type = (DataType)myDataA[i];
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					if (check_if_fits((EAggregation_type)el_type, val, context)) {
						return (EAggregation_type)el_type;
					}
				} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					EAggregation_type res = find_best_fit_in_select((ESelect_type)el_type, val, context);
					if (res != null) {
						return res;
					}
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			DataType el_type = (DataType)sels.getByIndexEntity(i);
//			while (el_type.express_type == DataType.DEFINED_TYPE) {
//				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
//			}
//			if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
//				if (check_if_fits((EAggregation_type)el_type, val, context)) {
//					return (EAggregation_type)el_type;
//				}
//			} else if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				EAggregation_type res = find_best_fit_in_select((ESelect_type)el_type, val, context);
//				if (res != null) {
//					return res;
//				}
//			}
//		}
		return null;
	}


/*	private EData_type areCompatible(EData_type tp1, EData_type tp2, SdaiContext context) throws SdaiException {
		if (tp1 == tp2) {
			return tp1;
		}
		if (oneCompatibleToAnother(tp1, tp2, context)) {
			return tp1;
		} else if (oneCompatibleToAnother(tp2, tp1, context)) {
			return tp2;
		}
		return null;
	}*/
	private boolean baseTypeCompatibleToAggr(EData_type tp1, EData_type tp2, SdaiContext context) throws SdaiException {
//System.out.println("Value +++++++ tp1: " + tp1 + "     tp2: " + tp2);
		if (tp1 == null) {
			return false;
		}
		if (tp2 == null) {
			return true;
		}
		DataType _tp1 = (DataType)tp1;
		DataType _tp2 = (DataType)tp2;
		while (_tp1.express_type == DataType.DEFINED_TYPE) {
			_tp1 = (DataType)((CDefined_type)_tp1).getDomain(null);
		}
		if (_tp1.express_type < DataType.LIST || _tp1.express_type > DataType.AGGREGATE) {
			return false;
		}
		if (_tp1 == ExpressTypes.LIST_GENERIC_TYPE || _tp1 == ExpressTypes.SET_GENERIC_TYPE || 
				_tp1 == ExpressTypes.BAG_GENERIC_TYPE || _tp1 == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			return true;
		}
		EData_type el_tp1 = ((EAggregation_type)_tp1).getElement_type(null);
		while (_tp2.express_type == DataType.DEFINED_TYPE) {
			_tp2 = (DataType)((CDefined_type)_tp2).getDomain(null);
		}
		EData_type el_tp2;
		if (_tp2.express_type >= DataType.LIST && _tp2.express_type <= DataType.AGGREGATE) {
			el_tp2 = ((EAggregation_type)_tp2).getElement_type(null);
		} else {
			el_tp2 = _tp2;
		}
		if (((DataType)el_tp2).express_type == DataType.STRING && _tp1 == ExpressTypes.SET_STRING_TYPE) {
			return true;
		}
//System.out.println("Value ------ el_tp1: " + el_tp1 + "     el_tp2: " + el_tp2);
		return oneCompatibleToAnother(el_tp1, el_tp2, context, false);
	}
	private boolean oneCompatibleToAnother(EData_type tp1, EData_type tp2, SdaiContext context, boolean overrid) throws SdaiException {
		if (tp1 == null) {
			return false;
		}
		if (tp2 == null) {
			return true;
		}
		DataType _tp1 = (DataType)tp1;
		DataType _tp2 = (DataType)tp2;
		if (_tp1.express_type >= DataType.NUMBER && _tp1.express_type <= DataType.BINARY) {
			return isCompatibleSimple(_tp1, _tp2);
		} else if (_tp1.express_type >= DataType.LIST && _tp1.express_type <= DataType.AGGREGATE) {
			return isCompatibleAggregation(_tp1, _tp2, context, overrid);
		} else if (_tp1.express_type == DataType.DEFINED_TYPE) {
			return isCompatibleDefinedType(_tp1, _tp2, context);
		} else if (_tp1.express_type >= DataType.ENUMERATION && _tp1.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			return isCompatibleEnumeration(_tp1, _tp2);
		} else if (_tp1.express_type == DataType.ENTITY) {
			return isCompatibleEntityDef(_tp1, _tp2);
		} else if (_tp1.express_type >= DataType.SELECT && _tp1.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			return isCompatibleSelect(_tp1, _tp2, context);
		} else if (_tp1.express_type == DataType.DATA_TYPE) {
			if (tp1.getName(null).equals("_ENTITY")) {
//				if (tp2 instanceof CData_type) {
				while (_tp2.express_type == DataType.DEFINED_TYPE) {
					_tp2 = (DataType)((CDefined_type)_tp2).getDomain(null);
				}
				if (_tp2.express_type == DataType.ENTITY) {
					return true;
				} else {
					return false;
				}
			} else {
//				if (((EData_type)tp2).getName(null).equals("_GENERIC")) {
//					return true;
//				} else {
//					return false;
//				}
				return true;
			}
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}
	private boolean isCompatibleSimple(DataType tp1, DataType tp2) throws SdaiException {
		while (tp2.express_type == DataType.DEFINED_TYPE) {
			tp2 = (DataType)((CDefined_type)tp2).getDomain(null);
		}
		if (tp2.express_type < DataType.NUMBER || tp2.express_type > DataType.BINARY) {
			return false;
		}
		switch (tp1.express_type) {
			case DataType.NUMBER:
				if (tp2.express_type >= DataType.NUMBER && tp2.express_type <= DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.INTEGER:
				if (tp2.express_type == DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.REAL:
				if (tp2.express_type == DataType.REAL || tp2.express_type == DataType.INTEGER) {
					return true;
				}
				break;
			case DataType.BOOLEAN:
				if (tp2.express_type == DataType.BOOLEAN) {
					return true;
				}
				break;
			case DataType.LOGICAL:
				if (tp2.express_type == DataType.LOGICAL || tp2.express_type == DataType.BOOLEAN) {
					return true;
				}
				break;
			case DataType.BINARY:
				if (tp2.express_type == DataType.BINARY) {
					return true;
				}
				break;
			case DataType.STRING:
				if (tp2.express_type == DataType.STRING) {
					return true;
				}
				break;
		}
		return false;
	}
	private boolean isCompatibleAggregation(DataType tp1, DataType tp2, SdaiContext context, boolean overrid) throws SdaiException {
		while (tp2.express_type == DataType.DEFINED_TYPE) {
			tp2 = (DataType)((CDefined_type)tp2).getDomain(null);
		}
		if (tp2.express_type < DataType.LIST && tp2.express_type > DataType.AGGREGATE) {
			return false;
		}
		if (overrid) {
		} else if (tp1.express_type == DataType.ARRAY) {
			if (tp2.express_type != DataType.ARRAY) {
				return false;
			}
			EArray_type arr_tp1 = (EArray_type)tp1;
			EArray_type arr_tp2 = (EArray_type)tp2;
			if ((arr_tp1.getLower_index(null) != arr_tp2.getLower_index(null)) || 
					(arr_tp1.getUpper_index(null) != arr_tp2.getUpper_index(null)) || 
					(arr_tp1.getUnique_flag(null) != arr_tp2.getUnique_flag(null)) || 
					(arr_tp1.getOptional_flag(null) != arr_tp2.getOptional_flag(null))) {
				return false;
			}
		} else if (tp1.express_type == DataType.LIST) {
			if (tp2.express_type != DataType.LIST) {
				return false;
			}
			if (tp1 == ExpressTypes.LIST_GENERIC_TYPE) {
				return true;
			}
		} else if (tp1.express_type == DataType.SET) {
			if (tp2.express_type != DataType.SET) {
				return false;
			}
			if (tp1 == ExpressTypes.SET_GENERIC_TYPE) {
				return true;
			}
		} else if (tp1.express_type == DataType.BAG) {
			if (tp2.express_type != DataType.SET && tp2.express_type != DataType.BAG) {
				return false;
			}
			if (tp1 == ExpressTypes.BAG_GENERIC_TYPE) {
				return true;
			}
		} else if (tp1.express_type == DataType.AGGREGATE) {
		} else if (tp1.express_type >= DataType.LIST && tp1.express_type <= DataType.AGGREGATE) {
			if (tp1 == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
				return true;
			}
			return false;
		} else {
			return false;
		}
		return oneCompatibleToAnother(((EAggregation_type)tp1).getElement_type(null), 
			((EAggregation_type)tp2).getElement_type(null), context, overrid);
	}
	private boolean isCompatibleEnumeration(DataType tp1, DataType tp2) throws SdaiException {
		while (tp2.express_type == DataType.DEFINED_TYPE) {
			tp2 = (DataType)((CDefined_type)tp2).getDomain(null);
		}
		if (tp1 == tp2) {
			return true;
		}
		return false;
	}
	private boolean isCompatibleEntityDef(DataType tp1, DataType tp2) throws SdaiException {
		while (tp2.express_type == DataType.DEFINED_TYPE) {
			tp2 = (DataType)((CDefined_type)tp2).getDomain(null);
		}
		if (tp2.express_type == DataType.ENTITY && 
				((EEntity_definition)tp2).isSubtypeOf((EEntity_definition)tp1)) {
			return true;
		}
		return false;
	}
	private boolean isCompatibleSelect(DataType tp1, DataType tp2, SdaiContext context) throws SdaiException {
		while (tp2.express_type == DataType.DEFINED_TYPE) {
			tp2 = (DataType)((CDefined_type)tp2).getDomain(null);
		}
		AEntity sels = (AEntity)((ESelect_type)tp1).getSelections(null, context);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		if (sels.myLength == 1) {
			if (oneCompatibleToAnother((DataType)sels.myData, tp2, context, false)) {
				return true;
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				if (oneCompatibleToAnother((DataType)myDataA[i], tp2, context, false)) {
					return true;
				}
			}
		}
		return false;
	}
	private boolean isCompatibleDefinedType(DataType tp1, DataType tp2, SdaiContext context) throws SdaiException {
		while (tp1.express_type == DataType.DEFINED_TYPE) {
			tp1 = (DataType)((CDefined_type)tp1).getDomain(null);
		}
		return oneCompatibleToAnother(tp1, tp2, context, false);
	}


/**
 * Implements addition, union, string concatenation and binary concatenation
 * operators of Express language. The operands are wrapped in objects of 
 * type <code>Value</code> submitted through the parameters of the method. 
 * The allowed types of the operands and the type of the result 
 * for each of the above operators are defined in "ISO 10303-11". 
 * If any operand is indeterminate, then as a result of the operator unset 
 * value is taken. The result of an operator is stored in the current 
 * object of this class.
 * @param context context in which an operation is performed. 
 * @param val1 the first operand of an operator: of numeric type in the case 
 * of addition; an aggregate or a new element in the case of union; string or 
 * binary in the case of string and binary concatenation, respectively.
 * @param val2 the second operand of an operator (of the same type definition 
 * as for the first one).
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 * @see "ISO 10303-11::12.3.2 Binary concatenation operator"
 * @see "ISO 10303-11::12.5.2 String concatenation operator"
 * @see "ISO 10303-11::12.6.3 Union operator"
 */
	public Value addOrUnionOrConcatenate(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAAG);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.EMBEDDED_LIST || val2.tag == PhFileReader.EMBEDDED_LIST) {
			union(val1, val2, context);
		} else if (val1.tag == PhFileReader.STRING || val2.tag == PhFileReader.STRING) {
			stringConcatenation(val1, val2);
		} else if (val1.tag == PhFileReader.BINARY || val2.tag == PhFileReader.BINARY) {
			binaryConcatenation(val1, val2);
		} else {
			addition(val1, val2);
		}
		return this;
	}


	private void union(Value val1, Value val2, SdaiContext context) throws SdaiException {
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type != null) {
				return;
			}
			boolean cs = false;
			boolean comp;
			int tp1 = -1, tp2 = -1;
			if (val1.d_type != null) {
				tp1 = ((DataType)val1.d_type).express_type;
			}
			if (val2.d_type != null) {
				tp2 = ((DataType)val2.d_type).express_type;
			}
			if (tp1 == DataType.SET || val1.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp1 == DataType.AGGREGATE) {
				if (tp2 == DataType.SET || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else if (tp1 == DataType.LIST) {
				if (tp2 == DataType.LIST || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else if (tp1 == DataType.BAG) {
				if (tp2 == DataType.BAG || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else if (val1.d_type != null) {
				if ((tp2 >= DataType.LIST &&  tp2 <= DataType.BAG) || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE 
              	|| tp2 == DataType.AGGREGATE) {
					d_type = val2.d_type;
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else {
				if (val2.d_type != null) {
					d_type = val2.d_type;
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			}
		} else if (val1.tag != PhFileReader.EMBEDDED_LIST) {
			unionFirstElement(val1, val2, context);
// element case
		} else if (val1.v_type == null) {
			unionFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.SET || val1.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			unionFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.LIST) {
			unionFirstList(val1, val2, context);
// list type
		} else if (((DataType)val1.v_type).express_type == DataType.BAG) {
			unionFirstBag(val1, val2, context);
// bag type
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


	private void set_simple(SdaiContext context, Value val) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		tag = val.tag;
		v_type = val.v_type;
		switch (tag) {
			case PhFileReader.INTEGER:
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				integer = val.integer;
				break;
			case PhFileReader.REAL:
				real = val.real;
				break;
			case PhFileReader.ENUM:
			case PhFileReader.STRING:
				string = val.string;
				reference = val.reference;
			case PhFileReader.BINARY:
				reference = val.reference;
				break;
			case PhFileReader.TYPED_PARAMETER:
				string = val.string;
				length = 1;
				if (nested_values == null) {
					nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (nested_values[0] == null) {
					nested_values[0] = new Value();
				}
				nested_values[0].d_type = val.nested_values[0].d_type;
				nested_values[0].set_simple(context, val.nested_values[0]);
				break;
			case PhFileReader.ENTITY_REFERENCE:
				reference = val.reference;
				break;
			case COMPLEX_ENTITY_VALUE:
			case ENTITY_VALUE:
				reference = val.reference;
				break;
			case PhFileReader.EMBEDDED_LIST:
				agg_owner = val.agg_owner;
				if (nested_values == null || val.length > nested_values.length) {
					enlarge_nested_values(val.length);
				}
				length = val.length;
				for (int i = 0; i < val.length; i++) {
					if (nested_values[i] == null) {
						nested_values[i] = new Value();
					}
					nested_values[i].d_type = val.nested_values[i].d_type;
					nested_values[i].set_simple(context, val.nested_values[i]);
				}
				break;
			default:
				throw new SdaiException(SdaiException.SY_ERR);
		}
	}


/**
 * Implements conversion of an aggregate of Express BAG OF GENERIC type to an 
 * aggregate of Express SET OF GENERIC type. 
 * Both aggregates are wrapped in objects of type <code>Value</code>. 
 * If such value for BAG is Express INDETERMINATE, then this method 
 * returns INDETERMINATE.
 * @param context context in which an operation is performed. 
 * @param bag the bag to be converted to a set. 
 * @return the current object of this class that wraps the resulting set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 4.1.0
 */
	public Value bagToSet(SdaiContext context, Value bag) throws SdaiException {
		if (d_type == null || ((DataType)d_type).express_type != DataType.SET || bag == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		agg_owner = bag.agg_owner;
		if (bag.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			return this;
		}
		if (bag.v_type == null || ((DataType)bag.v_type).express_type != DataType.BAG) {
			if (((DataType)bag.v_type).express_type == DataType.SET) {
				String text = AdditionalMessages.EE_NBAG + SdaiSession.line_separator + "   Argument type: " + bag.v_type;
				printWarningToLogoValidate(text);
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NBAG);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NBAG);
			}
		}
		v_type = d_type;
		tag = PhFileReader.EMBEDDED_LIST;
		if (bag.length == 0) {
			length = 0;
			return this;
		}
		int i;
		CEntity inst;
		boolean all_inst = true;
		for (i = 0; i < bag.length; i++) {
			if (bag.nested_values[i].tag != PhFileReader.ENTITY_REFERENCE) {
				all_inst = false;
				break;
			}
			inst = (CEntity)bag.nested_values[i].reference;
			inst.instance_position = (inst.instance_position & CEntity.FLG_MASK) | CEntity.POS_MASK;
		}
		length = 0;
		if (nested_values == null) {
			if (bag.length > SdaiSession.NUMBER_OF_VALUES) {
				nested_values = new Value[bag.length];
			} else {
				nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			}
		} else if (bag.length > nested_values.length) {
			enlarge_nested_values(bag.length);
		}
		if (all_inst) {
			for (i = 0; i < bag.length; i++) {
				inst = (CEntity)bag.nested_values[i].reference;
				if ((inst.instance_position & CEntity.POS_MASK) != CEntity.POS_MASK) {
					continue;
				}
				if (nested_values[length] == null) {
					nested_values[length] = new Value();
				}
				nested_values[length].tag = bag.nested_values[i].tag;
				nested_values[length].d_type = bag.nested_values[i].d_type;
				nested_values[length].v_type = bag.nested_values[i].v_type;
				nested_values[length].reference = inst;
				length++;
				inst.instance_position = inst.instance_position & CEntity.FLG_MASK;
			}
			return this;
		} else {
			for (i = 0; i < bag.length; i++) {
				Value val = bag.nested_values[i];
				if (checkIfInAggregate(val, context) == -1) {
					if (nested_values[length] == null) {
						nested_values[length] = new Value();
					}
					nested_values[length].d_type = bag.nested_values[i].d_type;
					nested_values[length].set_simple(context, val);
					length++;
				}
			}
			return this;
		}
	}


	private void unionFirstSet(Value val1, Value val2, SdaiContext context) throws SdaiException {
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.SET) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			if (fits) {
				v_type = d_type;
				comp = true;
			}
		}
		if (!comp) {
//System.out.println("Value ~~~~~~~~~~~~~~~   val1.v_type: " + val1.v_type + 
//"   val1.d_type: " + val1.d_type  + "   val1.tag: " + val1.tag);
//Aggregate set = (Aggregate)val1.reference;
//System.out.println("Value ~~~~~~~~~~~~~~~   set: " + set);
//System.out.println("Value ~~~~~~~~~~~~~~~   val2.v_type: " + val2.v_type + 
//"   val2.d_type: " + val2.d_type  + "   val2.tag: " + val2.tag);
//CEntity adjoined = (CEntity)val2.reference;
//System.out.println("Value ~~~~~~~~~~~~~~~   adjoined: " + adjoined);
			printWarningToLogoValidate(AdditionalMessages.EE_OPUO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_OPUO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		length = val1.length;
		if (nested_values == null || length > nested_values.length) {
			enlarge_nested_values(length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (int i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
			nested_values[i].set(context, val1.nested_values[i]);
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST) {
			if (checkIfInAggregate(val2, context) == -1) {
				if (length + 1 > nested_values.length) {
					enlarge(length + 1);
				}
				if (nested_values[length] == null) {
					nested_values[length] = new Value();
				}
				nested_values[length].d_type = dtp;
				nested_values[length].set(context, val2);
				length++;
			}
		} else if (cs || ((DataType)val2.v_type).express_type == DataType.BAG || 
				((DataType)val2.v_type).express_type == DataType.LIST) {
			for (int j = 0; j < val2.length; j++) {
				if (val1.checkIfInAggregate(val2.nested_values[j], context) == -1) {
					if (length + 1 > nested_values.length) {
						enlarge(length + 1);
					}
					if (nested_values[length] == null) {
						nested_values[length] = new Value();
					}
					nested_values[length].d_type = dtp;
					nested_values[length].set(context, val2.nested_values[j]);
					length++;
				}
			}
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


	private void unionFirstList(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.LIST || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.LIST) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			printWarningToLogoValidate(AdditionalMessages.EE_OPUO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_OPUO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (val2.tag == PhFileReader.EMBEDDED_LIST) {
			if (!cs) {
				printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			length = val1.length + val2.length;
		} else {
			length = val1.length + 1;
		}
		if (nested_values == null || length > nested_values.length) {
			enlarge_nested_values(length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		for (i = 0; i < val1.length; i++) {
			nested_values[i].set(context, val1.nested_values[i]);
		}
		if (val2.tag == PhFileReader.EMBEDDED_LIST) {
			for (i = 0; i < val2.length; i++) {
				nested_values[val1.length + i].set(context, val2.nested_values[i]);
			}
		} else {
			nested_values[length - 1].set(context, val2);
		}
	}


	private void unionFirstBag(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.BAG || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.BAG) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
//System.out.println(" Value $$$$$   val1.v_type: " + val1.v_type + 
//"   val1.d_type: " + val1.d_type  + "   val1.tag: " + val1.tag);
//System.out.println(" Value $$$$$   val2.v_type: " + val2.v_type + 
//"   val2.d_type: " + val2.d_type  + "   val2.tag: " + val2.tag);
			printWarningToLogoValidate(AdditionalMessages.EE_OPUO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_OPUO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (val2.tag == PhFileReader.EMBEDDED_LIST) {
			if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, v_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			length = val1.length + val2.length;
		} else {
			length = val1.length + 1;
		}
		if (nested_values == null || length > nested_values.length) {
			enlarge_nested_values(length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		for (i = 0; i < val1.length; i++) {
			nested_values[i].set(context, val1.nested_values[i]);
		}
		if (val2.tag == PhFileReader.EMBEDDED_LIST) {
			for (i = 0; i < val2.length; i++) {
				nested_values[val1.length + i].set(context, val2.nested_values[i]);
			}
		} else {
			nested_values[length - 1].set(context, val2);
		}
	}


	private void unionFirstElement(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		boolean fits = false;
		if (val2.tag != PhFileReader.EMBEDDED_LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_VNAG, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			unionFirstSet(val2, val1, context);
		} else if (((DataType)val2.v_type).express_type == DataType.BAG) {
			unionFirstBag(val2, val1, context);
		} else {
			boolean comp;
			if (d_type != null) {
				boolean comp1 = oneCompatibleToAnother(d_type, val2.v_type, context, false);
				boolean comp2 = baseTypeCompatibleToAggr(d_type, val1.v_type, context);
				if (!comp1 || !comp2) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				} else {
					fits = true;
				}
			} else {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
			if (!comp) {
				comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
				if (comp) {
					v_type = val2.v_type;
					if (!fits) {
						d_type = val2.d_type;
					}
				}
			}
			if (!comp) {
				printWarningToLogoValidate(AdditionalMessages.EE_OPUO);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_OPUO);
			}

			tag = PhFileReader.EMBEDDED_LIST;
			length = val2.length + 1;
			if (nested_values == null || length > nested_values.length) {
				enlarge_nested_values(length);
			}
			if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
			for (i = 0; i < length; i++) {
				if (nested_values[i] == null) {
					nested_values[i] = new Value();
				}
				nested_values[i].d_type = dtp;
			}
			nested_values[0].set(context, val1);
			for (i = 0; i < val2.length; i++) {
				nested_values[i + 1].set(context, val2.nested_values[i]);
			}
		}
	}


/**
 * Implements Express language operation A:=A+e, where A is an aggregate, 
 * e is an element, and plus stands for the aggregate union operator. 
 * The operands of the union operator are wrapped in objects of 
 * type <code>Value</code>. The aggregate is represented by this <code>Value</code>
 * object, whereas the element being added by the first parameter submitted to the method. 
 * If any operand is indeterminate, then as a result of the operation indeterminate 
 * value is returned. 
 * @param val the element to be added to the aggregate. 
 * @param context context in which operation is performed. 
 * @return the aggregate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.6.3 Union operator"
 * @since 4.1.1
 */
	public Value unionEnlarge(Value val, SdaiContext context) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag == INDETERMINATE || val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			return this;
		}
		if (tag != PhFileReader.EMBEDDED_LIST) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_NATY, tag);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NTVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (((DataType)v_type).express_type == DataType.SET || v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			unionEnlargeSet(val, context);
// set case
		} else if (((DataType)v_type).express_type == DataType.LIST) {
			unionEnlargeList(val, context);
// list type
		} else if (((DataType)v_type).express_type == DataType.BAG) {
			unionEnlargeBag(val, context);
// bag type
		} else {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return this;
	}


	private void unionEnlargeSet(Value val, SdaiContext context) throws SdaiException {
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		boolean comp = baseTypeCompatibleToAggr(v_type, val.v_type, context);
		if (!comp) {
/*System.out.println("Value ~~~~~~~~~~~~~~~   v_type: " + v_type + 
"   d_type: " + d_type  + "   tag: " + tag);
Aggregate set = (Aggregate)reference;
System.out.println("Value ~~~~~~~~~~~~~~~   set: " + set);
System.out.println("Value ~~~~~~~~~~~~~~~   val.v_type: " + val.v_type + 
"   val.d_type: " + val.d_type  + "   val.tag: " + val.tag);
CEntity adjoined = (CEntity)val.reference;
System.out.println("Value ~~~~~~~~~~~~~~~   adjoined: " + adjoined);*/
			printWarningToLogoValidate(AdditionalMessages.EE_OPUO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_OPUO);
		}
		if (checkIfInAggregate(val, context) == -1) {
			if (nested_values == null) {
				enlarge_nested_values(1);
			} else if (length + 1 > nested_values.length) {
				enlarge(length + 1);
			}
			if (nested_values[length] == null) {
				nested_values[length] = new Value();
			}
			EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
			nested_values[length].d_type = dtp;
			nested_values[length].set(context, val);
			length++;
		}
	}


	private void unionEnlargeList(Value val, SdaiContext context) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	private void unionEnlargeBag(Value val, SdaiContext context) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * Implements arithmetic negation (-) operator of Express language. 
 * The operand is wrapped in the current object of type <code>Value</code> 
 * which during execution of the operation is updated to contain its result. 
 * The allowed types of the operand are NUMBER, INTEGER and REAL. 
 * If the operand is indeterminate, then as a result of the operator unset 
 * value is taken. 
 * @return the current object of this class.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #negation(Value)
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
/*	public Value negation() throws SdaiException {
		if (tag == PhFileReader.INTEGER) {
			integer = -integer;
		} else if (tag == PhFileReader.REAL) {
			real = -real;
		} else if (tag != INDETERMINATE) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return this;
	}*/


/**
 * Implements arithmetic negation (-) operator of Express language. 
 * The operand is wrapped in an object of type <code>Value</code> 
 * submitted through the parameter of the method.
 * The result is stored in the current object of this class. 
 * The allowed types of the operand are NUMBER, INTEGER and REAL. 
 * If the operand is indeterminate, then as a result of the operator unset 
 * value is taken.
 * @param val the number whose sign has to be changed.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
	public Value negation(Value val) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return negation(val.nested_values[0]);
		}
		if (val.tag == PhFileReader.INTEGER) {
			tag = PhFileReader.INTEGER;
			if (d_type == null) {
				d_type = val.d_type;
			}
			v_type = val.v_type;
			integer = -val.integer;
		} else if (val.tag == PhFileReader.REAL) {
			tag = PhFileReader.REAL;
			if (d_type == null) {
				d_type = val.d_type;
			}
			v_type = val.v_type;
			real = -val.real;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = val.d_type;
			}
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return this;
	}


	private void addition(Value val1, Value val2) throws SdaiException {
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			addition(val1.nested_values[0], val2);
			return;
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			addition(val1, val2.nested_values[0]);
			return;
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.REAL_TYPE;
			}
			return;
		}
		int altern;
		if (val1.tag == PhFileReader.INTEGER) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 0;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 1;
			} else {
				altern = -1;
			}
		} else if (val1.tag == PhFileReader.REAL) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 2;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 3;
			} else {
				altern = -1;
			}
		} else {
			altern = -1;
		}
		switch (altern) {
			case -1:
				String base = AdditionalMessages.EE_ILOP;
				printWarningToLogoValidate(base);
				throw new SdaiException(SdaiException.VT_NVLD, base);
			case 0:
				tag = PhFileReader.INTEGER;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				integer = val1.integer + val2.integer;
				break;
			case 1:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val2.d_type;
				}
				v_type = val2.v_type;
				real = val1.integer + val2.real;
				break;
			case 2:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real + val2.integer;
				break;
			case 3:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real + val2.real;
				break;
		}
	}


	private void stringConcatenation(Value val1, Value val2) throws SdaiException {
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			stringConcatenation(val1.nested_values[0], val2);
			return;
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			stringConcatenation(val1, val2.nested_values[0]);
			return;
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.STRING_TYPE;
			}
			return;
		}
		if (val1.tag != PhFileReader.STRING || val2.tag != PhFileReader.STRING) {
			String base = AdditionalMessages.EE_ILOP;
			printWarningToLogoValidate(base);
			throw new SdaiException(SdaiException.VT_NVLD, base);
		}
		tag = PhFileReader.STRING;
		d_type = val1.d_type;
		v_type = val1.v_type;
		string = val1.string + val2.string;
		if (val1.reference instanceof String) {
			reference = val1.reference;
		} else if (val2.reference instanceof String) {
			reference = val2.reference;
		}
	}


	private void binaryConcatenation(Value val1, Value val2) throws SdaiException {
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			binaryConcatenation(val1.nested_values[0], val2);
			return;
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			binaryConcatenation(val1, val2.nested_values[0]);
			return;
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.BINARY_TYPE;
			}
			return;
		}
		if (val1.tag != PhFileReader.BINARY || val2.tag != PhFileReader.BINARY) {
			String base = AdditionalMessages.EE_ILOP;
			printWarningToLogoValidate(base);
			throw new SdaiException(SdaiException.VT_NVLD, base);
		}
		tag = PhFileReader.BINARY;
		d_type = val1.d_type;
		v_type = val1.v_type;
		StaticFields staticFields = StaticFields.get();
		int count = ((Binary)val1.reference).concatenation(staticFields, (Binary)val2.reference);
		reference = new Binary(staticFields.result, count);
	}


/**
 * Implements subtraction and aggregate difference operators of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. The allowed types of the 
 * operands and the type of the result for 
 * each of above two operators are defined in "ISO 10303-11". If any operand is 
 * indeterminate, then as a result of the operator unset value is taken. 
 * The result of an operator is stored in the current object of this class.
 * @param context context in which an operation is performed.
 * @param val1 the first operand of an operator: of numeric type in the case 
 * of subtraction; SET or BAG in the case of difference.
 * @param val2 the second operand of an operator: of numeric type in the case 
 * of subtraction; SET, BAG or a new element in the case of difference.
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 * @see "ISO 10303-11::12.6.4 Difference operator"
 */
	public Value substractOrDifference(SdaiContext context, Value val1, Value val2) throws SdaiException {
//		if (val1.tag == PhFileReader.EMBEDDED_LIST || val2.tag == PhFileReader.EMBEDDED_LIST) {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.d_type == null) {
			if (val2.d_type == null) {
				subtraction(val1, val2);
			} else {
				if (((DataType)val2.d_type).express_type >= DataType.LIST &&  ((DataType)val2.d_type).express_type <= DataType.AGGREGATE) {
					difference(val1, val2, context);
				} else {
					subtraction(val1, val2);
				}
			}
		} else if (val2.d_type == null) {
			if (((DataType)val1.d_type).express_type >= DataType.LIST &&  ((DataType)val1.d_type).express_type <= DataType.AGGREGATE) {
				difference(val1, val2, context);
			} else {
				subtraction(val1, val2);
			}
		} else if ( (((DataType)val1.d_type).express_type >= DataType.LIST &&  ((DataType)val1.d_type).express_type <= DataType.AGGREGATE)
			|| (((DataType)val2.d_type).express_type >= DataType.LIST &&  ((DataType)val2.d_type).express_type <= DataType.AGGREGATE) ) {
			difference(val1, val2, context);
		} else {
			subtraction(val1, val2);
		}
		return this;
	}


	private void difference(Value val1, Value val2, SdaiContext context) throws SdaiException {
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type != null) {
				return;
			}
			boolean cs = false;
			boolean comp;
			int tp1 = -1, tp2 = -1;
			if (val1.d_type != null) {
				tp1 = ((DataType)val1.d_type).express_type;
			}
			if (val2.d_type != null) {
				tp2 = ((DataType)val2.d_type).express_type;
			}
			if (tp1 == DataType.SET || tp1 == DataType.AGGREGATE || val1.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
				if (tp2 == DataType.SET || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else if (tp1 == DataType.BAG) {
				if (tp2 == DataType.BAG || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else {
				d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
			}
		} else if (val1.v_type == null) {
			differenceFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.SET || val1.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			differenceFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.BAG) {
			differenceFirstBag(val1, val2, context);
// bag type
		} else {
			String base = AdditionalMessages.EE_ILOP;
			printWarningToLogoValidate(base);
			throw new SdaiException(SdaiException.VT_NVLD, base);
		}
	}


	private void differenceFirstSet(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		int ind;
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.tag == PhFileReader.EMBEDDED_LIST && 
				(((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY)) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_SODO, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_SODO);
		}
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.SET) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			printWarningToLogoValidate(AdditionalMessages.EE_ITDO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ITDO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (nested_values == null || val1.length > nested_values.length) {
			enlarge_nested_values(val1.length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < val1.length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST) {
			ind = val1.checkIfInAggregate(val2, context);
			if (ind < 0) {
				for (i = 0; i < val1.length; i++) {
					nested_values[i].set(context, val1.nested_values[i]);
				}
				length = val1.length;
			} else {
				for (i = 0; i < ind; i++) {
					nested_values[i].set(context, val1.nested_values[i]);
				}
				for (i = ind + 1; i < val1.length; i++) {
					nested_values[i - 1].set(context, val1.nested_values[i]);
				}
				length = val1.length - 1;
			}
		} else {
			for (i = 0; i < val1.length; i++) {
				val1.nested_values[i].aux = 0;
			}
			for (int j = 0; j < val2.length; j++) {
				ind = val1.checkIfInAggregate(val2.nested_values[j], context);
				if (ind >= 0) {
					val1.nested_values[ind].aux = 1;
				}
			}
			length = 0;
			for (i = 0; i < val1.length; i++) {
				if (val1.nested_values[i].aux == 0) {
					nested_values[length++].set(context, val1.nested_values[i]);
				}
			}
		}
	}


	private void differenceFirstBag(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		int ind;
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.tag == PhFileReader.EMBEDDED_LIST && 
				(((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY)) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_SODO, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_SODO);
		}
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.BAG || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.BAG) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			printWarningToLogoValidate(AdditionalMessages.EE_ITDO);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ITDO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (nested_values == null || val1.length > nested_values.length) {
			enlarge_nested_values(val1.length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < val1.length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST) {
			ind = val1.checkIfInAggregate(val2, context);
			if (ind < 0) {
				for (i = 0; i < val1.length; i++) {
					nested_values[i].set(context, val1.nested_values[i]);
				}
				length = val1.length;
			} else {
				for (i = 0; i < ind; i++) {
					nested_values[i].set(context, val1.nested_values[i]);
				}
				for (i = ind + 1; i < val1.length; i++) {
					nested_values[i - 1].set(context, val1.nested_values[i]);
				}
				length = val1.length - 1;
			}
		} else {
			for (i = 0; i < val1.length; i++) {
				val1.nested_values[i].aux = val1.nested_values[i].tag;
			}
			for (int j = 0; j < val2.length; j++) {
				ind = val1.checkIfInAggregate(val2.nested_values[j], context);
				if (ind >= 0) {
					val1.nested_values[ind].tag = -1;
				}
			}
			length = 0;
			for (i = 0; i < val1.length; i++) {
				if (val1.nested_values[i].tag == -1) {
					val1.nested_values[i].tag = val1.nested_values[i].aux;
				} else {
					nested_values[length++].set(context, val1.nested_values[i]);
				}
			}
		}
	}


	private void subtraction(Value val1, Value val2) throws SdaiException {
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			subtraction(val1.nested_values[0], val2);
			return;
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			subtraction(val1, val2.nested_values[0]);
			return;
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.REAL_TYPE;
			}
			return;
		}
		int altern;
		if (val1.tag == PhFileReader.INTEGER) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 0;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 1;
			} else {
				altern = -1;
			}
		} else if (val1.tag == PhFileReader.REAL) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 2;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 3;
			} else {
				altern = -1;
			}
		} else {
			altern = -1;
		}
		switch (altern) {
			case -1:
				String base = AdditionalMessages.EE_ILOP;
				printWarningToLogoValidate(base);
				throw new SdaiException(SdaiException.VT_NVLD, base);
			case 0:
				tag = PhFileReader.INTEGER;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				integer = val1.integer - val2.integer;
				break;
			case 1:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val2.d_type;
				}
				v_type = val2.v_type;
				real = val1.integer - val2.real;
				break;
			case 2:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real - val2.integer;
				break;
			case 3:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real - val2.real;
				break;
		}
	}


/**
 * Implements multiplication and aggregate intersection operators of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. The allowed types of the operands 
 * and the type of the result for each of above two operators are defined in 
 * "ISO 10303-11". If any operand is indeterminate, then as a result of the 
 * operator unset value is taken.
 * The result of an operator is stored in the current object of this class.
 * @param context context in which an operation is performed.
 * @param val1 the first operand of an operator: of numeric type in the case 
 * of multiplication; SET or BAG in the case of intersection.
 * @param val2 the second operand of an operator (of the same type definition 
 * as for the first one).
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 * @see "ISO 10303-11::12.6.2 Intersection operator"
 */
	public Value mulOrIntersect(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.EMBEDDED_LIST || val2.tag == PhFileReader.EMBEDDED_LIST) {
			intersection(val1, val2, context);
		} else {
			multiplication(val1, val2);
		}
		return this;
	}


	private void intersection(Value val1, Value val2, SdaiContext context) throws SdaiException {
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type != null) {
				return;
			}
			boolean cs = false;
			boolean comp;
			int tp1 = -1, tp2 = -1;
			if (val1.d_type != null) {
				tp1 = ((DataType)val1.d_type).express_type;
			}
			if (val2.d_type != null) {
				tp2 = ((DataType)val2.d_type).express_type;
			}
			if (tp1 == DataType.SET || tp1 == DataType.AGGREGATE || val1.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
				if (tp2 == DataType.SET || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else if (tp1 == DataType.BAG) {
				if (tp2 == DataType.SET) {
					d_type = val2.d_type;
					return;
				}
				if (tp2 == DataType.BAG || val2.d_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || tp2 == DataType.AGGREGATE) {
					cs = true;
				}
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.d_type, context);
				if (comp) {
					d_type = val1.d_type;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.d_type, context);
					if (comp) {
						d_type = val2.d_type;
					}
				} else {
					d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
				}
			} else {
				d_type = ExpressTypes.AGGREGATE_GENERIC_TYPE;
			}
		} else if (val1.v_type == null) {
			intersectionFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.SET || val1.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			intersectionFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.BAG) {
			intersectionFirstBag(val1, val2, context);
// bag type
		} else {
			String base = AdditionalMessages.EE_ILOP;
			printWarningToLogoValidate(base);
			throw new SdaiException(SdaiException.VT_NVLD, base);
		}
	}


	private void intersectionFirstSet(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		int ind;
		boolean comp;
		boolean fits = false;
		boolean cs = false;
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			cs = true;
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST || 
				((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_SOIO, val2.v_type);
			throw new SdaiException(SdaiException.SY_ERR, AdditionalMessages.EE_SOIO);
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else if (cs) {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else if (cs) {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else if (cs) {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp && ((DataType)val2.v_type).express_type == DataType.SET) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			printWarningToLogoValidateGiveValueTypes(AdditionalMessages.EE_ITIO, val1.v_type, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ITIO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (nested_values == null || val1.length > nested_values.length) {
			enlarge_nested_values(val1.length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < val1.length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		for (i = 0; i < val1.length; i++) {
			val1.nested_values[i].aux = 0;
		}
		for (i = 0; i < val1.length; i++) {
			ind = val2.checkIfInAggregate(val1.nested_values[i], context);
			if (ind >= 0) {
				val1.nested_values[i].aux = 1;
			}
		}
		length = 0;
		for (i = 0; i < val1.length; i++) {
			if (val1.nested_values[i].aux == 1) {
				nested_values[length++].set(context, val1.nested_values[i]);
			}
		}
	}


	private void intersectionFirstBag(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		int ind;
		boolean comp;
		boolean fits = false;
		if (val2.tag != PhFileReader.EMBEDDED_LIST || 
				((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_SOIO, val2.v_type);
			throw new SdaiException(SdaiException.SY_ERR, AdditionalMessages.EE_SOIO);
		}
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			intersectionFirstSet(val2, val1, context);
			return;
		}
		if (d_type != null) {
			boolean comp1 = oneCompatibleToAnother(d_type, val1.v_type, context, false);
			boolean comp2 = baseTypeCompatibleToAggr(d_type, val2.v_type, context);
			if (!comp1 || !comp2) {
				comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
				if (comp) {
					d_type = val1.d_type;
					fits = true;
				} else {
					comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
					if (comp) {
						d_type = val2.d_type;
						fits = true;
					}
				}
			} else {
				fits = true;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val1.d_type, val2.v_type, context);
			if (comp) {
				d_type = val1.d_type;
				fits = true;
			} else {
				comp = baseTypeCompatibleToAggr(val2.d_type, val1.v_type, context);
				if (comp) {
					d_type = val2.d_type;
					fits = true;
				}
			}
		}
		comp = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		if (comp) {
			v_type = val1.v_type;
			if (!fits) {
				d_type = val1.d_type;
			}
		} else {
			comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			comp = val2.elementsCompatibleToAggr((EAggregation_type)val1.v_type, context, false);
			if (comp) {
				v_type = val1.v_type;
				if (!fits) {
					d_type = val1.d_type;
				}
			}
		}
		if (!comp) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
			if (comp) {
				v_type = val2.v_type;
				if (!fits) {
					d_type = val2.d_type;
				}
			}
		}
		if (!comp) {
			printWarningToLogoValidateGiveValueTypes(AdditionalMessages.EE_ITIO, val1.v_type, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ITIO);
		}

		tag = PhFileReader.EMBEDDED_LIST;
		if (nested_values == null || val1.length > nested_values.length) {
			enlarge_nested_values(val1.length);
		}
		if (((DataType)d_type).express_type < DataType.LIST || ((DataType)d_type).express_type > DataType.AGGREGATE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_NATY, d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EData_type dtp = ((EAggregation_type)d_type).getElement_type(null);
		for (i = 0; i < val1.length; i++) {
			if (nested_values[i] == null) {
				nested_values[i] = new Value();
			}
			nested_values[i].d_type = dtp;
		}
		for (i = 0; i < val1.length; i++) {
			val1.nested_values[i].aux = 0;
		}
		for (i = 0; i < val2.length; i++) {
			val2.nested_values[i].aux = val2.nested_values[i].tag;
		}
		for (i = 0; i < val1.length; i++) {
			ind = val2.checkIfInAggregate(val1.nested_values[i], context);
			if (ind >= 0) {
				val1.nested_values[i].aux = 1;
				val2.nested_values[ind].tag = -1;
			}
		}
		for (i = 0; i < val2.length; i++) {
			if (val2.nested_values[i].tag == -1) {
				val2.nested_values[i].tag = val2.nested_values[i].aux;
			}
		}
		length = 0;
		for (i = 0; i < val1.length; i++) {
			if (val1.nested_values[i].aux == 1) {
				nested_values[length++].set(context, val1.nested_values[i]);
			}
		}
	}


	private void multiplication(Value val1, Value val2) throws SdaiException {
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			multiplication(val1.nested_values[0], val2);
			return;
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			multiplication(val1, val2.nested_values[0]);
			return;
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.REAL_TYPE;
			}
			return;
		}
		int altern;
		if (val1.tag == PhFileReader.INTEGER) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 0;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 1;
			} else {
				altern = -1;
			}
		} else if (val1.tag == PhFileReader.REAL) {
			if (val2.tag == PhFileReader.INTEGER) {
				altern = 2;
			} else if (val2.tag == PhFileReader.REAL) {
				altern = 3;
			} else {
				altern = -1;
			}
		} else {
			altern = -1;
		}
		switch (altern) {
			case -1:
				printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
			case 0:
				tag = PhFileReader.INTEGER;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				integer = val1.integer * val2.integer;
				break;
			case 1:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val2.d_type;
				}
				v_type = val2.v_type;
				real = val1.integer * val2.real;
				break;
			case 2:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real * val2.integer;
				break;
			case 3:
				tag = PhFileReader.REAL;
				if (d_type == null) {
					d_type = val1.d_type;
				}
				v_type = val1.v_type;
				real = val1.real * val2.real;
				break;
		}
	}


/**
 * Implements real division operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are NUMBER, INTEGER and REAL. 
 * If any operand is indeterminate, then as a result of the operator unset 
 * value is taken.
 * The result of the operator is stored in the current object of this class.
 * @param context context in which the operation is performed.
 * @param numerator the numerator in the real division expression.
 * @param denominator the denominator in the real division expression. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
	public Value divide(SdaiContext context, Value numerator, Value denominator) throws SdaiException {
		if (numerator == null || denominator == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (numerator.tag == PhFileReader.TYPED_PARAMETER) {
			return divide(context, numerator.nested_values[0], denominator);
		}
		if (denominator.tag == PhFileReader.TYPED_PARAMETER) {
			return divide(context, numerator, denominator.nested_values[0]);
		}
		if (numerator.tag == INDETERMINATE || denominator.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.REAL_TYPE;
			}
			return this;
		}
		int altern;
		if (numerator.tag == PhFileReader.INTEGER) {
			if (denominator.tag == PhFileReader.INTEGER) {
				altern = 0;
			} else if (denominator.tag == PhFileReader.REAL) {
				altern = 1;
			} else {
				altern = -1;
			}
		} else if (numerator.tag == PhFileReader.REAL) {
			if (denominator.tag == PhFileReader.INTEGER) {
				altern = 2;
			} else if (denominator.tag == PhFileReader.REAL) {
				altern = 3;
			} else {
				altern = -1;
			}
		} else {
			altern = -1;
		}
		if (altern >= 0) {
			tag = PhFileReader.REAL;
		}
		switch (altern) {
			case -1:
				printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
			case 0:
				if (denominator.integer == 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_DIVZ);
					throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_DIVZ);
				}
				real = ((double)numerator.integer) / denominator.integer;
				break;
			case 1:
				if (denominator.real == 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_DIVZ);
					throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_DIVZ);
				}
				real = numerator.integer / denominator.real;
				break;
			case 2:
				if (denominator.integer == 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_DIVZ);
					throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_DIVZ);
				}
				real = numerator.real / denominator.integer;
				break;
			case 3:
				if (denominator.real == 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_DIVZ);
					throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_DIVZ);
				}
				real = numerator.real / denominator.real;
				break;
		}
		d_type = v_type = ExpressTypes.REAL_TYPE;
		return this;
	}


/**
 * Implements exponentiation operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are NUMBER, INTEGER and REAL. 
 * If any operand is indeterminate, then as a result of the operator unset 
 * value is taken.
 * The result of the operator is stored in the current object of this class.
 * @param context context in which the operation is performed.
 * @param base the base in the exponentiation expression.
 * @param power the power in the exponentiation expression. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
	public Value exponent(SdaiContext context, Value base, Value power) throws SdaiException {
		if (base == null || power == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (base.tag == PhFileReader.TYPED_PARAMETER) {
			return exponent(context, base.nested_values[0], power);
		}
		if (power.tag == PhFileReader.TYPED_PARAMETER) {
			return exponent(context, base, power.nested_values[0]);
		}
		if (base.tag == INDETERMINATE || power.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.REAL_TYPE;
			}
			return this;
		}
		double base_ = 0., power_ = 0.;
		int integer_value = 0;
		if (base.tag == PhFileReader.INTEGER) {
			integer_value++;
			base_ = base.integer;
		} else if (base.tag == PhFileReader.REAL) {
			base_ = base.real;
		} else {
			integer_value = -5;
		}
		if (power.tag == PhFileReader.INTEGER) {
			integer_value++;
			power_ = power.integer;
		} else if (power.tag == PhFileReader.REAL) {
			power_ = power.real;
		} else {
			integer_value = -5;
		}
		if (integer_value < 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (base_ == 0 && power_ <= 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_BNUL);
			throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_BNUL);
		}
		if (base_ <= 0 && power.tag != PhFileReader.INTEGER) {
			printWarningToLogoValidate(AdditionalMessages.EE_BNPO);
			throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_BNPO);
		}
		if (integer_value >= 2) {
			tag = PhFileReader.INTEGER;
			if (d_type == null) {
				d_type = base.d_type;
			}
			v_type = base.v_type;
			integer = (int)Math.pow(base_, power_);
		} else {
			tag = PhFileReader.REAL;
			if (d_type == null) {
				if (base.tag == PhFileReader.REAL) {
					d_type = base.d_type;
				} else {
					d_type = power.d_type;
				}
			}
			if (base.tag == PhFileReader.REAL) {
				v_type = base.v_type;
			} else {
				v_type = power.v_type;
			}
			real = Math.pow(base_, power_);
		}
		return this;
	}


/**
 * Implements integer division operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are NUMBER, INTEGER and REAL. 
 * If any operand is indeterminate, then as a result of the operator unset 
 * value is taken.
 * The result of the operator is stored in the current object of this class.
 * @param context context in which the operation is performed.
 * @param numerator the numerator in the integer division expression.
 * @param denominator the denominator in the integer division expression. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
	public Value DIV(SdaiContext context, Value numerator, Value denominator) throws SdaiException {
		if (numerator == null || denominator == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (numerator.tag == PhFileReader.TYPED_PARAMETER) {
			return DIV(context, numerator.nested_values[0], denominator);
		}
		if (denominator.tag == PhFileReader.TYPED_PARAMETER) {
			return DIV(context, numerator, denominator.nested_values[0]);
		}
		if (numerator.tag == INDETERMINATE || denominator.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.INTEGER_TYPE;
			}
			return this;
		}
		boolean bad_type = false;
		int num = 0, den = 1;
		if (numerator.tag == PhFileReader.INTEGER) {
			num = numerator.integer;
		} else if (numerator.tag == PhFileReader.REAL) {
			num = (int)numerator.real;
		} else {
			bad_type = true;
		}
		if (denominator.tag == PhFileReader.INTEGER) {
			den = denominator.integer;
		} else if (denominator.tag == PhFileReader.REAL) {
			den = (int)denominator.real;
		} else {
			bad_type = true;
		}
		if (bad_type) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (den == 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_DIVZ);
			throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_DIVZ);
		}
		tag = PhFileReader.INTEGER;
		integer = num / den;
		d_type = v_type = ExpressTypes.INTEGER_TYPE;
		return this;
	}


/**
 * Implements modulo operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are NUMBER, INTEGER and REAL. 
 * If any operand is indeterminate, then as a result of the operator unset 
 * value is taken.
 * The result of the operator is stored in the current object of this class.
 * @param context context in which the operation is performed.
 * @param val1 the first operand in the modulo expression.
 * @param val2 the second operand in the modulo expression. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.1 Arithmetic operators"
 */
	public Value MOD(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			return MOD(context, val1.nested_values[0], val2);
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			return MOD(context, val1, val2.nested_values[0]);
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.INTEGER_TYPE;
			}
			return this;
		}
		boolean bad_type = false;
		int arg1 = 0, arg2 = 0;
		if (val1.tag == PhFileReader.INTEGER) {
			arg1 = val1.integer;
		} else if (val1.tag == PhFileReader.REAL) {
			arg1 = (int)val1.real;
		} else {
			bad_type = true;
		}
		if (val2.tag == PhFileReader.INTEGER) {
			arg2 = val2.integer;
		} else if (val2.tag == PhFileReader.REAL) {
			arg2 = (int)val2.real;
		} else {
			bad_type = true;
		}
		if (bad_type) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		tag = PhFileReader.INTEGER;
		integer = arg1 % arg2;
		if (arg1 > 0) {
			if (arg2 < 0) {
				integer += arg2;
			}
		} else if (arg1 < 0) {
			if (arg2 > 0) {
				integer += arg2;
			}
		}
		d_type = v_type = ExpressTypes.INTEGER_TYPE;
		return this;
	}


/**
 * Implements "equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #equal
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int equalInt(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator =
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
//			if (val1.string.equals("*") && val1.reference instanceof String) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
//			if (val2.string.equals("*") && val2.reference instanceof String) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, false, true, context);
		if (res == 0) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of a value comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #equalInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public Value equal(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator =
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
//		boolean hidden_str1 = false, hidden_str2 = false;
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
//				hidden_str1 = true;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
//				hidden_str2 = true;
			}
		}
		int res = val1.compareValues(val2, true, false, true, context);
//		if (hidden_str1) {
//			val1.string = "*";
//		}
//		if (hidden_str2) {
//			val2.string = "*";
//		}
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 0) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "not equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken. 
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #nequal
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int nequalInt(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator <>
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, false, true, context);
		if (res == 0) {
			return 1;
		} else if (res == 2) {
			return 3;
		} else {
			return 2;
		}
	}


/**
 * Implements "not equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of a value comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #nequalInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public Value nequal(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator <>
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, false, true, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 0) {
			integer = 0;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 1;
		}
		return this;
	}


/**
 * Implements "greater than" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #greater
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int greaterInt(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator >
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		if (res == 1) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "greater than" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #greaterInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public Value greater(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator >
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 1) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "less than" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #less
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int lessInt(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator <
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		if (res == -1) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "less than" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #lessInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public Value less(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator <
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == -1) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "greater than or equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #gequal
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int gequalInt(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator >=
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		if (res == 0 || res == 1) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "greater than or equal" - a value comparison operator and also superset 
 * operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration in the case of value comparison and Express SET or BAG 
 * in the case of superset operator. 
 * The operands shall be of compatible types.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first operand.
 * @param val2 the second operand. 
 * @return the current object of this class representing logical value being a 
 * result of comparison or superset operator.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #gequalInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 * @see "ISO 10303-11::12.6.6 Superset operator"
 * @since 4.0.0
 */
	public Value gequalOrSuperset(SdaiContext context, Value val1, Value val2) 
			throws SdaiException {	// operator >= in the contexts: 1) greater than or equal 2) superset
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.EMBEDDED_LIST || val2.tag == PhFileReader.EMBEDDED_LIST) {
			return superset(context, val1, val2);
		} else {
			return gequal_internal(context, val1, val2);
		}
	}

/**
 * It is a shortcut to 
 * {@link #gequalOrSuperset gequalOrSuperset} method.
 */
	public Value gequal(SdaiContext context, Value val1, Value val2) 
			throws SdaiException {	// operator >= in the contexts: 1) greater than or equal 2) superset
		return gequalOrSuperset(context, val1, val2);
	}


	Value gequal_internal(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator >=
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 0 || res == 1) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "less than or equal" - a value comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration. 
 * The two operands of a value comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #lequal
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 */
	public static int lequalInt(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator <=
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		if (res <= 0) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "less than or equal" - a value comparison operator and also subset 
 * operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary 
 * and enumeration in the case of value comparison and Express SET or BAG 
 * in the case of subset operator. 
 * The operands shall be of compatible types.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first operand.
 * @param val2 the second operand. 
 * @return the current object of this class representing logical value being a 
 * result of comparison or subset operator.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #lequalInt
 * @see "ISO 10303-11::12.2.1 Value comparison operators"
 * @see "ISO 10303-11::12.6.5 Subset operator"
 * @since 4.0.0
 */
	public Value lequalOrSubset(SdaiContext context, Value val1, Value val2) 
			throws SdaiException {	// operator <= in the contexts: 1) less than or equal 2) subset
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.EMBEDDED_LIST || val2.tag == PhFileReader.EMBEDDED_LIST) {
			return subset(context, val1, val2);
		} else {
			return lequal_internal(context, val1, val2);
		}
	}

/**
 * It is a shortcut to 
 * {@link #lequalOrSubset lequalOrSubset} method.
 */
	public Value lequal(SdaiContext context, Value val1, Value val2) 
			throws SdaiException {	// operator <= in the contexts: 1) less than or equal 2) subset
		return lequalOrSubset(context, val1, val2);
	}


	Value lequal_internal(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator <=
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, false, false, false, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res <= 0) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "instance equal" - an instance comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of an instance comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #instanceEqual
 * @see "ISO 10303-11::12.2.2 Instance comparison operators"
 */
	public static int instanceEqualInt(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator :=:
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, true, true, context);
		if (res == 0) {
			return 2;
		} else if (res == 2) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements "instance equal" - an instance comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of an instance comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #instanceEqualInt
 * @see "ISO 10303-11::12.2.2 Instance comparison operators"
 */
	public Value instanceEqual(SdaiContext context, Value val1, Value val2) throws SdaiException {		// operator :=:
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, true, true, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 0) {
			integer = 1;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements "instance not equal" - an instance comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of an instance comparison operator shall be data type compatible.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return 1 if the comparison expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #instanceNotEqual
 * @see "ISO 10303-11::12.2.2 Instance comparison operators"
 */
	public static int instanceNotEqualInt(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator :<>:
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, true, true, context);
		if (res == 0) {
			return 1;
		} else if (res == 2) {
			return 3;
		} else {
			return 2;
		}
	}


/**
 * Implements "instance not equal" - an instance comparison operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The allowed types of the operands are numeric, logical, string, binary, 
 * enumeration, aggregate and entity data types. 
 * The two operands of an instance comparison operator shall be data type compatible.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the first value to be compared.
 * @param val2 the second value to be compared. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #instanceNotEqualInt
 * @see "ISO 10303-11::12.2.2 Instance comparison operators"
 */
	public Value instanceNotEqual(SdaiContext context, Value val1, Value val2) throws SdaiException {	// operator :<>:
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		}
		if (val2.tag == COMPLEX_ENTITY_VALUE || val2.tag == ENTITY_VALUE) {
			val2 = val2.makeInstance(context);
		}
		if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		if (val2.tag == PhFileReader.STRING) {
			if (val2.string==SdaiSession.asterisk && val2.reference instanceof String) {
				val2.string = (String)val2.reference;
			}
		}
		int res = val1.compareValues(val2, true, true, true, context);
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (res == 0) {
			integer = 0;
		} else if (res == 2) {
			integer = 2;
		} else {
			integer = 1;
		}
		return this;
	}


/**
 * Implements membership operator IN of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The right-hand operand shall be a value of an aggregation data type. 
 * The left-hand operand shall be compatible with the base type of this 
 * aggregation type.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the left-hand operand (an element to be tested for membership 
 * in an aggregate submitted through the second parameter).
 * @param val2 the right-hand operand (aggregate value). 
 * @return 1 if the membership expression evaluates to FALSE, 2 if it evaluates 
 * to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #IN
 * @see "ISO 10303-11::12.2.3 Membership operator"
 */
	public static int INInt(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST && val2.tag != INDETERMINATE) {
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			return 3;
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		} else if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		boolean unknown = false;
		for (int i = 0; i < val2.length; i++) {
			if (val2.nested_values[i].tag == INDETERMINATE) {
				unknown = true;
				continue;
			}
			int res = val2.nested_values[i].compareValues(val1, true, true, true, context);
			if (res == 0) {
				return 2;
			}
		}
		if (unknown) {
			return 3;
		} else {
			return 1;
		}
	}


/**
 * Implements membership operator IN of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The right-hand operand shall be a value of an aggregation data type. 
 * The left-hand operand shall be compatible with the base type of this 
 * aggregation type.
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the left-hand operand (an element to be tested for membership 
 * in an aggregate submitted through the second parameter).
 * @param val2 the right-hand operand (aggregate value). 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #INInt
 * @see "ISO 10303-11::12.2.3 Membership operator"
 */
	public Value IN(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST && val2.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			integer = 2;
			return this;
		}
		if (val1.tag == COMPLEX_ENTITY_VALUE || val1.tag == ENTITY_VALUE) {
			val1 = val1.makeInstance(context);
		} else if (val1.tag == PhFileReader.STRING) {
			if (val1.string==SdaiSession.asterisk && val1.reference instanceof String) {
				val1.string = (String)val1.reference;
			}
		}
		boolean unknown = false;
		for (int i = 0; i < val2.length; i++) {
			if (val2.nested_values[i].tag == INDETERMINATE) {
				unknown = true;
				continue;
			}
			int res = val2.nested_values[i].compareValues(val1, true, true, true, context);
			if (res == 0) {
				integer = 1;
				return this;
			}
		}
		if (unknown) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements LIKE operator of Express language. 
 * The operands are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The left-hand operand (first parameter) is the target string, 
 * whereas the right-hand operand (second parameter) is the pattern string. 
 * If either operand evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param context context in which the operation is performed.
 * @param val1 the target string. 
 * @param val2 the pattern string. 
 * @return 1 if comparison of pattern and target strings evaluates to FALSE, 
 * 2 if it evaluates to TRUE, and 3 if it evaluates to indeterminate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.2.5 Like operator"
 */
	public Value LIKE(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if ((val1.tag != PhFileReader.STRING && val1.tag != INDETERMINATE) || 
				(val2.tag != PhFileReader.STRING && val2.tag != INDETERMINATE)) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			integer = 2;
			return this;
		}
		char [] target = val1.string.toCharArray();
		char [] pattern = val2.string.toCharArray();
		if (pattern_matching(target, pattern, 0, 0)) {
			integer = 1;
		} else {
			integer = 0;
		}
		return this;
	}


	private boolean pattern_matching(char [] target, char [] pattern, int start_t, 
			int start_p) throws SdaiException {
		int i;
		int k = start_p;
		int l = start_t;
		char sym;
		boolean negate = false;
		boolean match_empty = true;
		if (start_t >= target.length) {
			for (i = start_p; i < pattern.length; i++) {
				if (pattern[i] != CONJUNCTION && pattern[i] != DOLLAR_SIGN && pattern[i] != ASTERISK) {
					match_empty = false;
					break;
				}
			}
			if (match_empty) {
				return true;
			} else {
				return false;
			}
		}
		while (k < pattern.length) {
			sym = pattern[k];
			if (sym == BACKSLASH) {
				k++;
				if (pattern[k] == BACKSLASH) {
					if (target[l] != BACKSLASH) {
						if (!negate) {
							return false;
						}
					} else {
						if (negate) {
							return false;
						}
					}
					negate = false;
					k++;
					l++;
					if (l >= target.length && k < pattern.length) {
						return false;
					}
					continue;
				}
				boolean matched = true;
				switch (pattern[k]) {
					case AT:
						if (target[l] != AT) {
							matched = false;
						}
						break;
					case UPPER:
						if (target[l] != UPPER) {
							matched = false;
						}
						break;
					case QUESTION_MARK:
						if (target[l] != QUESTION_MARK) {
							matched = false;
						}
						break;
					case CONJUNCTION:
						if (target[l] != CONJUNCTION) {
							matched = false;
						}
						break;
					case ANY:
						if (target[l] != ANY) {
							matched = false;
						}
						break;
					case DOLLAR_SIGN:
						if (target[l] != DOLLAR_SIGN) {
							matched = false;
						}
						break;
					case ASTERISK:
						if (target[l] != ASTERISK) {
							matched = false;
						}
						break;
					case NEGATION:
						if (target[l] != NEGATION) {
							matched = false;
						}
						break;
					default:
						printWarningToLogo(null, SdaiSession.line_separator + AdditionalMessages.EE_WRCH);
						return false;
				}
				if (matched) {
					if (negate) {
						return false;
					}
				} else if (!negate) {
					return false;
				}
				negate = false;
				k++;
				l++;
				if (l >= target.length && k < pattern.length) {
					return false;
				}
				continue;
			}
			switch (sym) {
				case AT:
					if (target[l] < 'A' || (target[l] > 'Z' && target[l] < 'a') || 
						target[l] > 'z') {
						return false;
					}
					break;
				case UPPER:
					if (target[l] < 'A' || target[l] > 'Z') {
						return false;
					}
					break;
				case QUESTION_MARK:
					break;
				case CONJUNCTION:
					return true;
				case ANY:
					if (target[l] < '0' || target[l] > '9') {
						return false;
					}
					break;
				case DOLLAR_SIGN:
					boolean found_space = false;
					for (i = l; i < target.length; i++) {
						if (target[l] != SPACE) {
							found_space = true;
						}
					}
					if (!found_space) {
						return true;
					}
					break;
				case ASTERISK:
					if (k == pattern.length - 1) {
						return true;
					}
					for (i = target.length; i >= l; i--) {
						boolean res = pattern_matching(target, pattern, i, k + 1);
						if (res) {
							return true;
						}
					}
					return false;
				case NEGATION:
					negate = true;
					l--;
					break;
				default:
					if (target[l] != sym) {
						if (!negate) {
							return false;
						}
					} else if (negate) {
						return false;
					}
					negate = false;
					break;
			}
			k++;
			l++;
			if (l >= target.length && k < pattern.length) {
				return false;
			}
		}
		return true;
	}


	private int compareValues(Value val, boolean advanced, boolean inst_comparison, boolean ignore, 
			SdaiContext context) throws SdaiException {
		int i;
		int arg1, arg2;
		if (tag == INDETERMINATE || val.tag == INDETERMINATE) {
			return 2;
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			if (tag != PhFileReader.TYPED_PARAMETER) {
				return -1;
			}
			if (v_type != val.v_type) {
				return -1;
			}
//			return compareValues(val.nested_values[0], advanced, inst_comparison, ignore, context);
			return nested_values[0].compareValues(val.nested_values[0], advanced, inst_comparison, ignore, context);
		}
		switch (tag) {
			case PhFileReader.INTEGER:
				if (val.tag == PhFileReader.INTEGER) {
					if (integer == val.integer) {
						return 0;
					} else if (integer < val.integer) {
						return -1;
					} else {
						return 1;
					}
				} else if (val.tag == PhFileReader.REAL) {
					if (integer == val.real) {
						return 0;
					} else if (integer < val.real) {
						return -1;
					} else {
						return 1;
					}
				} else {
					if (ignore) {
						return -1;
					}
//System.out.println("Value IIIIIIIII tag: " + tag + "    val.tag: " + val.tag + 
//"   d_type: " + d_type + "   v_type: " + v_type + "   val.d_type: " + val.d_type + "   val.v_type: " + val.v_type);
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				if (val.tag != PhFileReader.LOGICAL && val.tag != PhFileReader.BOOLEAN) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
				arg1 = integer;
				if (arg1 == 1) {
					arg1 = 3;
				}
				arg2 = val.integer;
				if (arg2 == 1) {
					arg2 = 3;
				}
				if (arg1 == arg2) {
					return 0;
				} else if (arg1 < arg2) {
					return -1;
				} else {
					return 1;
				}
			case PhFileReader.REAL:
				if (val.tag == PhFileReader.INTEGER) {
					if (real == val.integer) {
						return 0;
					} else if (real < val.integer) {
						return -1;
					} else {
						return 1;
					}
				} else if (val.tag == PhFileReader.REAL) {
					if (real == val.real) {
						return 0;
					} else if (real < val.real) {
						return -1;
					} else {
						return 1;
					}
				} else {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
			case PhFileReader.ENUM:
				EEnumeration_type enum_tp;
				if (((DataType)v_type).express_type == DataType.DEFINED_TYPE) {
					enum_tp = (EEnumeration_type)((CDefined_type)v_type).getDomain(null);
				} else {
					enum_tp = (EEnumeration_type)v_type;
				}
				if (val.tag != PhFileReader.ENUM) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
				EEnumeration_type val_enum_tp;
				if (((DataType)val.v_type).express_type == DataType.DEFINED_TYPE) {
					val_enum_tp = (EEnumeration_type)((CDefined_type)val.v_type).getDomain(null);
				} else {
					val_enum_tp = (EEnumeration_type)val.v_type;
				}

//				if (val.tag != PhFileReader.ENUM || v_type != val.v_type) {
				if (enum_tp != val_enum_tp) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
				String item;
				arg1 = -1;
//				A_string elements = ((EEnumeration_type)v_type).getElements(null, context);
				A_string elements = enum_tp.getElements(null, context);
				for (i = 1; i <= elements.myLength; i++) {
					item = elements.getByIndex(i);
					if (string.equalsIgnoreCase(item)) {
						arg1 = i;
						break;
					}
				}
				arg2 = -1;
				for (i = 1; i <= elements.myLength; i++) {
					item = elements.getByIndex(i);
					if (val.string.equalsIgnoreCase(item)) {
						arg2 = i;
						break;
					}
				}
				if (arg1 < 0 || arg2 < 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_WRST);
					throw new SdaiException(SdaiException.VA_NVLD, AdditionalMessages.EE_WRST);

				}
				if (arg1 == arg2) {
					return 0;
				} else if (arg1 < arg2) {
					return -1;
				} else {
					return 1;
				}
			case PhFileReader.STRING:
				if (val.tag != PhFileReader.STRING) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
				int comp_res = string.compareTo(val.string);
				if (comp_res == 0) {
					return 0;
				} else if (comp_res < 0) {
					return -1;
				} else {
					return 1;
				}
			case PhFileReader.BINARY:
				if (val.tag != PhFileReader.BINARY) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_INTY);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
				}
				Binary bin = (Binary)reference;
				return bin.compare((Binary)val.reference);
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].compareValues(val, advanced, inst_comparison, ignore, context);
			case PhFileReader.ENTITY_REFERENCE:
				if (!advanced) {
					printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
				}
				if (inst_comparison) {
					if (val.tag == PhFileReader.ENTITY_REFERENCE) {
						if (reference == val.reference) {
							return 0;
						} else {
							return -1;
						}
					} else {
						if (ignore) {
							return -1;
						}
						printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
						throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
					}
				} else {
/*int comp_ent = compareEntityValues(val);
if ( ((CEntity)reference).instance_identifier == 170) {
CEntity th = (CEntity)reference;
CEntity cc = (CEntity)val.reference;
System.out.println("Value HHHHHHH  comp_ent: " + comp_ent + 
"   this: " + th + 
"  cc: " + cc);
}
return comp_ent;*/
					return compareEntityValues(val, context);
				}
			case ENTITY_VALUE:
				if (!advanced) {
					printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
				}
				if (inst_comparison) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
				} else {
					return compareEntityValues(val, context);
				}
			case PhFileReader.EMBEDDED_LIST:
				if (!advanced) {
					printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
				}
				if (val.tag != PhFileReader.EMBEDDED_LIST) {
					if (ignore) {
						return -1;
					}
					printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
					throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
				}
//int ret_res = compareAggregates(val, inst_comparison, ignore, context);
//if (v_type != null && ((CEntity)v_type).instance_identifier == 28510)
//System.out.println("Value --------  ret_res: " + ret_res);
//return ret_res;
				return compareAggregates(val, inst_comparison, ignore, context); // return value: 0 means equal; -1 means not equal
			default:
		}
		return -1;
	}


	private int compareAggregates(Value val, boolean inst_comparison, boolean ignore, SdaiContext context) 
			throws SdaiException {
//if (make_out) {
//if (v_type != null) System.out.println("Value ^^^^^  v_type: " + v_type.getClass().getName());
//if (val.v_type != null) System.out.println("Value ^^^^^  val.v_type: " + val.v_type.getClass().getName());
//}
		int i;
		boolean compatible;
		if (length != val.length) {
			return -1;
		}
		int tp1 = ((DataType)v_type).express_type;
		int tp2 = ((DataType)val.v_type).express_type;
//		if (((DataType)v_type).express_type == DataType.ARRAY && ((DataType)val.v_type).express_type != DataType.ARRAY) {
		if (tp1 == DataType.ARRAY || tp2 == DataType.ARRAY) {
			compatible = true;
			if (tp1 == DataType.ARRAY) {
				if (tp2 != DataType.ARRAY && tp2 != DataType.AGGREGATE) {
					compatible = false;
				}
			} else {
				if (tp1 != DataType.AGGREGATE) {
					compatible = false;
				}
			}
			if (compatible) {
				return compareOrderedCollection(val, inst_comparison, ignore, context);
			}
			if (ignore) {
				return -1;
			}
			printWarningToLogoValidate(AdditionalMessages.EE_INTY);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
		}
//		if (((DataType)v_type).express_type == DataType.LIST && ((DataType)val.v_type).express_type != DataType.LIST) {
		if (tp1 == DataType.LIST || tp2 == DataType.LIST) {
			compatible = true;
			if (tp1 == DataType.LIST) {
				if (tp2 != DataType.LIST && tp2 != DataType.AGGREGATE) {
					compatible = false;
				}
			} else {
				if (tp1 != DataType.AGGREGATE) {
					compatible = false;
				}
			}
			if (compatible) {
				return compareOrderedCollection(val, inst_comparison, ignore, context);
			}
			if (ignore) {
				return -1;
			}
			printWarningToLogoValidate(AdditionalMessages.EE_INTY);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
		}
		for (i = 0; i < length; i++) {
			nested_values[i].aux = nested_values[i].tag;
		}
		int ret_value = 0;
		for (i = 0; i < val.length; i++) {
			int res = find_in_bag_or_set(val.nested_values[i], inst_comparison, ignore, context);
//if (val.nested_values[i].tag == PhFileReader.ENTITY_REFERENCE) {
//CEntity iin = (CEntity)val.nested_values[i].reference;
//System.out.println("Value compare Aggregates*****   inst: " + iin +
//"  i = " + i + "   res = " + res);
//}
			if (res < 0) {
				ret_value = -1;
				break;
			}
		}
		for (i = 0; i < length; i++) {
			nested_values[i].tag = nested_values[i].aux;
		}
		return ret_value;
	}


	private int compareOrderedCollection(Value val, boolean inst_comparison, boolean ignore, SdaiContext context) 
			throws SdaiException {
		boolean unknown = false;
		for (int i = 0; i < length; i++) {
			int res = nested_values[i].compareValues(val.nested_values[i], true, 
				inst_comparison, ignore, context);
//if (res < 0)
//System.out.println("  Value  res = " + res + "  i = " + i + 
//"  nested_values[i].real = " + nested_values[i].real + 
//"   nested_values[i]: " + nested_values[i] + "   nested_values[i].tag = " + nested_values[i].tag + 
//"  val.nested_values[i].real = " + val.nested_values[i].real + 
//"   val.nested_values[i]: " + val.nested_values[i] + 
//"   val.nested_values[i].tag = " + val.nested_values[i].tag);
			if (res == -1 || res == 1) {
				return -1;
			} else if (res == 2) {
				unknown = true;
			}
		}
		if (unknown) {
			return 2;
		}
		return 0;
	}


	private int find_in_bag_or_set(Value val, boolean inst_comparison, boolean ignore, SdaiContext context)
			throws SdaiException {
//System.out.println("Value RRRRRRR  ((CEntity)val.reference).instance_identifier: " + ((CEntity)val.v_type).instance_identifier + 
//"   length: " + length);
		for (int i = 0; i < length; i++) {
			int res = nested_values[i].compareValues(val, true, inst_comparison, ignore, context);
/*if ( ((CEntity)val.reference).instance_identifier == 170)
System.out.println("Value $$$$$$$  i: " + i + 
"   nested_values[i].tag: " + nested_values[i].tag + 
"  checked inst: " + ((CEntity)nested_values[i].reference).instance_identifier + 
"   res: " + res);*/
			if (res == 0) {
				nested_values[i].tag = -1;
				return res;
			}
		}
		return -1;
	}


	private int compareEntityValues(Value val, SdaiContext context) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (staticFields.plist1 == null) {
			staticFields.plist1 = new CEntity[PAIRS_LIST_LENGTH];
			staticFields.plist2 = new CEntity[PAIRS_LIST_LENGTH];
		}
		staticFields.plist_count = 0;
		return deepEquality(staticFields, val, context);
	}


	private int deepEquality(StaticFields staticFields, Value val, SdaiContext context) throws SdaiException {
		int i;
		EntityValue pval1, pval2;
		boolean unknown = false;
		if (tag == PhFileReader.ENTITY_REFERENCE || val.tag == PhFileReader.ENTITY_REFERENCE) {
			if (tag == INDETERMINATE || val.tag == INDETERMINATE) {
				return 2;
			} else if (tag != PhFileReader.ENTITY_REFERENCE || val.tag != PhFileReader.ENTITY_REFERENCE) {
				return -1;
			}
			if (reference == val.reference) {
				return 0;
			}
			CEntity_definition def1 = (CEntity_definition)((CEntity)reference).getInstanceType();
			CEntity_definition def2 = (CEntity_definition)((CEntity)val.reference).getInstanceType();
			if (def1 != def2) {
				return -1;
			}
			boolean found = false;
			for (i = 0; i < staticFields.plist_count; i++) {
				if (staticFields.plist1[i] == reference && staticFields.plist2[i] == val.reference) {
					found = true;
					break;
				}
			}
			if (found) {
				return 0;
			}
			if (staticFields.plist_count >= staticFields.plist1.length) {
				int new_length = staticFields.plist1.length * 2;
				CEntity [] new_plist = new CEntity[new_length];
				System.arraycopy(staticFields.plist1, 0, new_plist, 0, staticFields.plist1.length);
				staticFields.plist1 = new_plist;
				new_plist = new CEntity[new_length];
				System.arraycopy(staticFields.plist2, 0, new_plist, 0, staticFields.plist2.length);
				staticFields.plist2 = new_plist;
			}
			staticFields.plist1[staticFields.plist_count] = (CEntity)reference;
			staticFields.plist2[staticFields.plist_count] = (CEntity)val.reference;
			staticFields.plist_count++;
			prepareComplexInstance(def1, context);
			val.prepareComplexInstance(def2, context);
//if ( ((CEntity)reference).instance_identifier == 501 && ((CEntity)val.reference).instance_identifier == 502) {
//SdaiModel md = ((CEntity)reference).owning_model;
//md.print_entity_values(compl_ent_values, 501);
//md.print_entity_values(val.compl_ent_values, 502);
//}
			for (i = 0; i < def1.noOfPartialEntityTypes; i++) {
				pval1 = compl_ent_values.entityValues[i];
				pval2 = val.compl_ent_values.entityValues[i];
				for (int j = 0; j < pval1.count; j++) {
					Value val1 = pval1.values[j];
					Value val2 = pval2.values[j];
					int res = val1.deepEquality(staticFields, val2, context);
					if (res == -1 || res == 1) {
/*if (val1.tag == PhFileReader.ENTITY_REFERENCE && val2.tag == PhFileReader.ENTITY_REFERENCE) {
CEntity_definition val1_inst_def = (CEntity_definition)((CEntity)val1.reference).getInstanceType();
CEntity_definition val2_inst_def = (CEntity_definition)((CEntity)val2.reference).getInstanceType();
System.out.println("Value CASE OF NEGATIVE  val1.v_type: " + val1.v_type + 
"   val2.v_type: " + val2.v_type + "   res: " + res + 
"   <<<<<>>>>>  inst1: " + ((CEntity)val1.reference).instance_identifier + 
"   <<<<<>>>>>  inst2: " + ((CEntity)val2.reference).instance_identifier + 
"    val1_inst_def: " + val1_inst_def + 
"    val2_inst_def: " + val2_inst_def);
}*/
						return -1;
					} else if (res == 2) {
						unknown = true;
					}
				}
			}
			if (unknown) {
				return 2;
			} else {
				return 0;
			}
		} else if (tag == ENTITY_VALUE && val.tag == ENTITY_VALUE) {
			pval1 = (EntityValue)reference;
			pval2 = (EntityValue)val.reference;
			for (int j = 0; j < pval1.count; j++) {
				int res = pval1.values[j].deepEquality(staticFields, pval2.values[j], context);
				if (res == -1 || res == 1) {
					return -1;
				} else if (res == 2) {
					unknown = true;
				}
			}
			if (unknown) {
				return 2;
			} else {
				return 0;
			}
		} else {
			return compareValues(val, true, false, true, context);
		}
	}


	private void prepareComplexInstance(CEntity_definition def, SdaiContext context) throws SdaiException {
		if (compl_ent_values == null) {
			compl_ent_values = new ComplexEntityValue();
		}
//System.out.println("Value   given def: " + def.getName(null));
		CEntity inst = (CEntity)reference;
		inst.owning_model.prepareAll(compl_ent_values, def);
		inst.getAll(compl_ent_values);

		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			EntityValue pval = compl_ent_values.entityValues[i];
			if (pval == null) {
				continue;
			}
			CEntity_definition p_def = pval.def;
//System.out.println("Value   partial def: " + p_def.getName(null));
			CExplicit_attribute[] expl_attrs = ((CEntityDefinition)p_def).takeExplicit_attributes();
			for (int j = 0; j < p_def.noOfPartialAttributes; j++) {
				Value val = pval.values[j];
//System.out.println("Value   j = " + j + "   attribute: " + expl_attrs[j].getName(null) + 
//"   value tag: " + val.tag);
				EData_type domain = (EData_type)expl_attrs[j].getDomain(null);
				val.d_type = domain;
				val.get_value_type(domain, context);
			}
		}
	}
	private void get_value_type(EData_type domain, SdaiContext context) throws SdaiException {
		Value value_next;
		boolean first_next;

		if (tag == INDETERMINATE || tag == PhFileReader.REDEFINE) {
			return;
		}
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			v_type = ((CEntity)reference).getInstanceType();
			return;
		}
		if (tag == PhFileReader.EMBEDDED_LIST) {
			EAggregation_type aggr_type = ((Aggregate)reference).getAggregationType();
			v_type = aggr_type;
			EData_type el_type = (EData_type)aggr_type.getElement_type(null);
			for (int i = 0; i < length; i++) {
				nested_values[i].d_type = el_type;
				nested_values[i].get_value_type(el_type, context);
			}
			return;
		}
		if (tag == PhFileReader.TYPED_PARAMETER) {
			if (((DataType)domain).express_type != DataType.DEFINED_TYPE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			CDefined_type def = getDefType(string.toLowerCase(), (CDefined_type)domain, context);
			if (def == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			v_type = def;
			nested_values[0].d_type = def;
			EData_type new_domain = (EData_type)def.getDomain(null);
//System.out.println("Value   nested_values[0].tag: " + nested_values[0].tag + "   def: " + def);
			nested_values[0].get_value_type(new_domain, context);
			return;
		}
		while (((DataType)domain).express_type == DataType.DEFINED_TYPE) {
			domain = (EData_type)((CDefined_type)domain).getDomain(null);
		}
		DataType dom = (DataType)domain;
		switch (tag) {
			case PhFileReader.INTEGER:
				if (dom.express_type == DataType.INTEGER) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.REAL:
				if (dom.express_type == DataType.REAL || dom.express_type == DataType.NUMBER) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.BOOLEAN:
				if (dom.express_type == DataType.BOOLEAN) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.LOGICAL:
				if (dom.express_type == DataType.LOGICAL) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.ENUM:
				if (dom.express_type >= DataType.ENUMERATION && dom.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.STRING:
				if (dom.express_type == DataType.STRING) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case PhFileReader.BINARY:
				if (dom.express_type == DataType.BINARY) {
					v_type = domain;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
		}
	}
	private CDefined_type getDefType(String def_name, CDefined_type def, SdaiContext context) throws SdaiException {
		if (def_name.equals(def.getName(null))) {
			return def;
		}
		CDefined_type res;
		EEntity domain = def.getDomain(null);
		DataType dom = (DataType)domain;
		if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//			ANamed_type sels = ((ESelect_type)domain).getSelections(null, context);
			AEntity sels = ((ESelect_type)domain).getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			DataType alternative;
			if (sels.myLength == 1) {
				alternative = (DataType)sels.myData;
				if (alternative.express_type == DataType.DEFINED_TYPE) {
					return getDefType(def_name, (CDefined_type)alternative, context);
				}
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					alternative = (DataType)myDataA[i];
					if (alternative.express_type == DataType.DEFINED_TYPE) {
						res = getDefType(def_name, (CDefined_type)alternative, context);
						if (res != null) {
							return res;
						}
					}
				}
			}
//			if (((AEntity)sels).myLength < 0) {
//				((AEntity)sels).resolveAllConnectors();
//			}
//			for (int i = 1; i <= ((AEntity)sels).myLength; i++) {
//				ENamed_type alternative = (ENamed_type)((AEntity)sels).getByIndexEntity(i);
//				if (((DataType)alternative).express_type == DataType.DEFINED_TYPE) {
//					res = getDefType(def_name, (CDefined_type)alternative, context);
//					if (res != null) {
//						return res;
//					}
//				}
//			}
		} else if (dom.express_type == DataType.DEFINED_TYPE) {
			return getDefType(def_name, (CDefined_type)domain, context);
		}
		return null;
	}


	private int checkIfInAggregate(Value val, SdaiContext context) throws SdaiException {
		if (val.tag == INDETERMINATE) {
			return -2;
		}
		boolean unknown = false;
		for (int i = 0; i < length; i++) {
			if (nested_values[i].tag == INDETERMINATE) {
				unknown = true;
				continue;
			}
			int res = nested_values[i].compareValues(val, true, true, true, context);
			if (res == 0) {
				return i;
			}
		}
		if (unknown) {
			return -2;
		} else {
			return -1;
		}
	}


/**
 * Implements binary indexing operator of Express language. 
 * The binary value is wrapped in an object of type <code>Value</code> submitted 
 * through the first parameter of the method. 
 * The indices specifying a sequence of bits to be returned shall be supplied 
 * explicitly as positive integers.
 * If either of the indices evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param val the binary value. 
 * @param index1 position of the first bit in the binary value being indexed. 
 * @param index2 position of the last bit in the binary value being indexed. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.3.1 Binary indexing"
 */
	public Value subbinary(Value val, int index1, int index2) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		if (index2 == Integer.MIN_VALUE) {
			index2 = index1;
		}
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		return subbinary(staticFields, val, index1, index2);
	}

	private Value subbinary(StaticFields staticFields, Value val, int index1, int index2) throws SdaiException {
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return subbinary(staticFields, val.nested_values[0], index1, index2);
		}
		if (val.tag != PhFileReader.BINARY) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		Binary bin = (Binary)val.reference;
		int count = bin.subbinary(staticFields, index1, index2);
		if (count < 0) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.BINARY_TYPE;
			}
			return this;
		}
		tag = PhFileReader.BINARY;
		reference = new Binary(staticFields.result, count);
//		v_type = ExpressTypes.BINARY_TYPE;
		d_type = val.d_type;
		v_type = val.v_type;
		return this;
	}


/**
 * Implements AND operator of Express language. 
 * The operands, both logical, are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either of the operands evaluates to indeterminate, that operand is 
 * dealt with as if it were logical value UNKNOWN.
 * @param context context in which the operation is performed.
 * @param val1 the first logical operand. 
 * @param val2 the second logical operand. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.4.2 AND operator"
 */
	public Value AND(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			return AND(context, val1.nested_values[0], val2);
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			return AND(context, val1, val2.nested_values[0]);
		}
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE && d_type != ExpressTypes.GENERIC_TYPE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_ITVR, d_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val1.tag != PhFileReader.LOGICAL && val1.tag != PhFileReader.BOOLEAN && 
				val1.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val2.tag != PhFileReader.LOGICAL && val2.tag != PhFileReader.BOOLEAN && 
				val2.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val1.tag == INDETERMINATE) {
			val1.integer = 2;
		}
		if (val2.tag == INDETERMINATE) {
			val2.integer = 2;
		}
		tag = PhFileReader.LOGICAL;
		switch (val1.integer) {
			case 0: 
				integer = 0;
				break;
			case 1:
				integer = val2.integer;
				break;
			case 2:
				if (val2.integer == 0) {
					integer = 0;
				} else {
					integer = 2;
				}
				break;
		}
		v_type = ExpressTypes.LOGICAL_TYPE;
		return this;
	}


/**
 * Implements OR operator of Express language. 
 * The operands, both logical, are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either of the operands evaluates to indeterminate, that operand is 
 * dealt with as if it were logical value UNKNOWN.
 * @param context context in which the operation is performed.
 * @param val1 the first logical operand. 
 * @param val2 the second logical operand. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.4.3 OR operator"
 */
	public Value OR(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			return OR(context, val1.nested_values[0], val2);
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			return OR(context, val1, val2.nested_values[0]);
		}
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE && d_type != ExpressTypes.GENERIC_TYPE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_ITVR, d_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val1.tag != PhFileReader.LOGICAL && val1.tag != PhFileReader.BOOLEAN && 
				val1.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val2.tag != PhFileReader.LOGICAL && val2.tag != PhFileReader.BOOLEAN && 
				val2.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val1.tag == INDETERMINATE) {
			val1.integer = 2;
		}
		if (val2.tag == INDETERMINATE) {
			val2.integer = 2;
		}
		tag = PhFileReader.LOGICAL;
		switch (val1.integer) {
			case 0: 
				integer = val2.integer;
				break;
			case 1:
				integer = 1;
				break;
			case 2:
				if (val2.integer == 1) {
					integer = 1;
				} else {
					integer = 2;
				}
				break;
		}
		v_type = ExpressTypes.LOGICAL_TYPE;
		return this;
	}


/**
 * Implements XOR operator of Express language. 
 * The operands, both logical, are wrapped in objects of type <code>Value</code> submitted 
 * through the parameters of the method. 
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If either of the operands evaluates to indeterminate, that operand is 
 * dealt with as if it were logical value UNKNOWN.
 * @param context context in which the operation is performed.
 * @param val1 the first logical operand. 
 * @param val2 the second logical operand. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.4.4 XOR operator"
 */
	public Value XOR(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			return XOR(context, val1.nested_values[0], val2);
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			return XOR(context, val1, val2.nested_values[0]);
		}
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE && d_type != ExpressTypes.GENERIC_TYPE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_ITVR, d_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val1.tag != PhFileReader.LOGICAL && val1.tag != PhFileReader.BOOLEAN && 
				val1.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val2.tag != PhFileReader.LOGICAL && val2.tag != PhFileReader.BOOLEAN && 
				val2.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val1.tag == INDETERMINATE) {
			val1.integer = 2;
		}
		if (val2.tag == INDETERMINATE) {
			val2.integer = 2;
		}
		tag = PhFileReader.LOGICAL;
		switch (val1.integer) {
			case 0:
				integer = val2.integer;
				break;
			case 1:
				if (val2.integer == 0) {
					integer = 1;
				} else if (val2.integer == 1) {
					integer = 0;
				} else {
					integer = 2;
				}
				break;
			case 2:
				integer = 2;
				break;
		}
		v_type = ExpressTypes.LOGICAL_TYPE;
		return this;
	}


/**
 * Implements NOT operator of Express language. 
 * The logical operand is wrapped in an object of type <code>Value</code> submitted 
 * through the parameter of the method. 
 * The result evaluates to the logical value represented by the current object 
 * of type <code>Value</code>.
 * If the operand evaluates to indeterminate, it is dealt with as if 
 * it were logical value UNKNOWN.
 * @param val the logical operand. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.4.1 NOT operator"
 */
	public Value NOT(Value val) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return NOT(val.nested_values[0]);
		}
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE && d_type != ExpressTypes.GENERIC_TYPE) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_ITVR, d_type);
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (val.tag != PhFileReader.LOGICAL && val.tag != PhFileReader.BOOLEAN && 
				val.tag != INDETERMINATE) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val.tag == INDETERMINATE) {
			val.integer = 2;
		}
		tag = PhFileReader.LOGICAL;
		switch (val.integer) {
			case 0:
				integer = 1;
				break;
			case 1:
				integer = 0;
				break;
			case 2:
				integer = 2;
				break;
		}
		v_type = ExpressTypes.LOGICAL_TYPE;
		return this;
	}


/**
 * Implements string indexing operator of Express language. 
 * The string value is wrapped in an object of type <code>Value</code> submitted 
 * through the first parameter of the method. 
 * The indices specifying a sequence of characters to be returned shall be supplied 
 * explicitly as positive integers.
 * If either of the indices evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param val the string value. 
 * @param index1 position of the first character in the string being indexed. 
 * @param index2 position of the last character in the string being indexed. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #substring(Value, int)
 * @see "ISO 10303-11::12.5.1 String indexing"
 */
	public Value substring(Value val, int index1, int index2) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return substring(val.nested_values[0], index1, index2);
		}
		if (val.tag != PhFileReader.STRING) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val.string==SdaiSession.asterisk && val.reference instanceof String) {
			val.string = (String)val.reference;
		}
		String str = val.string;
		int ln = str.length();
		if (index1 < 1 || index1 > ln || index2 < index1 || index2 > ln) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = ExpressTypes.STRING_TYPE;
			}
			return this;
		}
		tag = PhFileReader.STRING;
		string = str.substring(index1 - 1, index2);
		v_type = val.v_type;
		if (d_type == null) {
			d_type = val.d_type;
		}
		return this;
	}


/**
 * Implements string indexing operator of Express language. 
 * The string value is wrapped in an object of type <code>Value</code> submitted 
 * through the first parameter of the method. 
 * The index specifying a character asked shall be supplied explicitly as 
 * a positive integer.
 * If this index evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param val the string value. 
 * @param index position of the character asked. 
 * @return the current object of this class.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #substring(Value, int, int)
 * @see "ISO 10303-11::12.5.1 String indexing"
 */
	public Value substring(Value val, int index) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return substring(val.nested_values[0], index);
		}
		if (val.tag != PhFileReader.STRING) {
			printWarningToLogoValidate(AdditionalMessages.EE_ILOP);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_ILOP);
		}
		if (val.string==SdaiSession.asterisk && val.reference instanceof String) {
			val.string = (String)val.reference;
		}
		String str = val.string;
		int ln = str.length();
		if (index < 1 || index > ln) {
			if (d_type == null) {
				d_type = ExpressTypes.STRING_TYPE;
			}
			tag = INDETERMINATE;
			return this;
		}
		tag = PhFileReader.STRING;
		string = str.substring(index - 1, index);
		v_type = val.v_type;
		if (d_type == null) {
			d_type = val.d_type;
		}
		return this;
	}


/**
 * Implements subset operator of Express language. 
 * The operands (Express SET or BAG) are wrapped in objects of type <code>Value</code> 
 * submitted through the parameters of the method. 
 * The operands shall be of compatible types. 
 * If either of the operands evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param val1 the first operand (aggregate).
 * @param val2 the second operand (aggregate). 
 * @return the current object of this class representing logical value being a 
 * result of subset operator.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.6.5 Subset operator"
 */
	Value subset(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1.tag != PhFileReader.EMBEDDED_LIST && val1.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_WOSO, val1.tag);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST && val2.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_WOSO, val2.tag);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = PhFileReader.LOGICAL;
			integer = 2;
			return this;
		}
		if (val1.v_type == null || ((DataType)val1.v_type).express_type == DataType.SET || 
				val1.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val1.v_type).express_type == DataType.AGGREGATE) {
			subsetFirstSet(val1, val2, context);
// set case
		} else if (((DataType)val1.v_type).express_type == DataType.BAG) {
			subsetFirstBag(val1, val2, context);
// bag type
		} else {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_WOSO, val1.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		return this;
	}


	private void subsetFirstSet(Value val1, Value val2, SdaiContext context) throws SdaiException {
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		}
		if (((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_WOSO, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
//		boolean comp1 = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		boolean comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
		if (!comp) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
		}
		if (!comp) {
			printWarningToLogoValidate(AdditionalMessages.EE_INTY);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
		}

		tag = PhFileReader.LOGICAL;
		v_type = ExpressTypes.LOGICAL_TYPE;
		for (int i = 0; i < val1.length; i++) {
			if (val2.checkIfInAggregate(val1.nested_values[i], context) < 0) {
				integer = 0;
				return;
			}
		}
		integer = 1;
	}


	private void subsetFirstBag(Value val1, Value val2, SdaiContext context) throws SdaiException {
		int i;
		if (d_type == null) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		} else if (d_type != ExpressTypes.LOGICAL_TYPE) {
			d_type = ExpressTypes.LOGICAL_TYPE;
		}
		if (((DataType)val2.v_type).express_type == DataType.LIST || ((DataType)val2.v_type).express_type == DataType.ARRAY) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_WOSO, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
//		boolean comp1 = baseTypeCompatibleToAggr(val1.v_type, val2.v_type, context);
		boolean comp = baseTypeCompatibleToAggr(val2.v_type, val1.v_type, context);
		if (!comp) {
			comp = val1.elementsCompatibleToAggr((EAggregation_type)val2.v_type, context, false);
		}
		if (!comp) {
			printWarningToLogoValidate(AdditionalMessages.EE_INTY);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_INTY);
		}
		tag = PhFileReader.LOGICAL;
		v_type = ExpressTypes.LOGICAL_TYPE;
		int res = 1;
		for (i = 0; i < val2.length; i++) {
			val2.nested_values[i].aux = val2.nested_values[i].tag;
		}
		for (i = 0; i < val1.length; i++) {
			int ind = val2.checkIfInAggregate(val1.nested_values[i], context);
			if (ind >= 0) {
				val2.nested_values[ind].tag = -1;
			} else {
				res = 0;
				break;
			}
		}
		for (i = 0; i < val2.length; i++) {
			if (val2.nested_values[i].tag == -1) {
				val2.nested_values[i].tag = val2.nested_values[i].aux;
			}
		}
		integer = res;
	}


/**
 * Implements superset operator of Express language. 
 * The operands (Express SET or BAG) are wrapped in objects of type <code>Value</code> 
 * submitted through the parameters of the method. 
 * The operands shall be of compatible types. 
 * If either of the operands evaluates to indeterminate, then as a result of 
 * the operator unset value is taken.
 * @param val1 the first operand (aggregate).
 * @param val2 the second operand (aggregate). 
 * @return the current object of this class representing logical value being a 
 * result of superset operator.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.6.6 Superset operator"
 */
	Value superset(SdaiContext context, Value val1, Value val2) throws SdaiException {
		if (val1.tag != PhFileReader.EMBEDDED_LIST && val1.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_WOSO, val1.tag);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		if (val2.tag != PhFileReader.EMBEDDED_LIST && val2.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_WOSO, val2.tag);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = PhFileReader.LOGICAL;
			integer = 2;
			return this;
		}
		if (val2.v_type == null || ((DataType)val2.v_type).express_type == DataType.SET || 
				val2.v_type == ExpressTypes.AGGREGATE_GENERIC_TYPE || ((DataType)val2.v_type).express_type == DataType.AGGREGATE) {
			subsetFirstSet(val2, val1, context);
// set case
		} else if (((DataType)val2.v_type).express_type == DataType.BAG) {
			subsetFirstBag(val2, val1, context);
// bag type
		} else {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_WOSO, val2.v_type);
			throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_WOSO);
		}
		return this;
	}


	Value addComplexInstance(Value val) throws SdaiException {
		Value val_converted = convertToComplexEntityValue((CEntity)val.reference);
		EntityValue [] ev_arr = (EntityValue [])val_converted.reference;
		int ln = val_converted.length;
		for (int i = 0; i < ln; i++) {
			Value e_val = new Value();
			e_val.tag = ENTITY_VALUE;
			e_val.reference = ev_arr[i];
			e_val.integer = 0;
			this.addComplex(e_val);
		}
		return this;
	}


/**
 * Adds a partial complex entity value to already existing complex entity 
 * instance represented by the current object of this class. 
 * Thus, an instance of the entity containing one more simple entity 
 * data type is obtained.
 * The values of the attributes of this simple entity are supplied 
 * by an object of type <code>Value</code> submitted through the parameter 
 * of the method. 
 * This method is related to an implementation of complex entity instance 
 * construction operator of Express language. 
 * @param val partial complex entity value.
 * @return the current object of this class representing extended 
 * complex entity instance.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.10 Complex entity instance construction operator"
 */
	public Value addComplex(Value val) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.ENTITY_REFERENCE) {
			return addComplexInstance(val);
		} else if (val.tag != ENTITY_VALUE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EntityValue new_ent_val = (EntityValue)val.reference;
		EntityValue [] ev_arr;
		String new_name= new_ent_val.def.getNameUpperCase();
		String n;
		if (tag == COMPLEX_ENTITY_VALUE) {
			ev_arr = (EntityValue [])reference;
			if (length >= ev_arr.length) {
				ev_arr = enlarge_entity_value_arr(ev_arr);
				reference = ev_arr;
			}
			int index = -2;
			int i;
			for (i = 0; i < length; i++) {
				if (new_ent_val.def == ev_arr[i].def) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				n = ev_arr[i].def.getNameUpperCase();
				if (new_name.compareTo(n) < 0) {
					index = i - 1;
					break;
				}
			}
			if (index == -2) {
				index = length - 1;
			}
			int ln = length - 1;
			for (i = ln; i > index; i--) {
				ev_arr[i + 1] = ev_arr[i];
			}
			ev_arr[index + 1] = new_ent_val;
			length++;
		} else if (tag == ENTITY_VALUE) {
			ev_arr = new EntityValue[SdaiSession.NUMBER_OF_ITEMS_IN_COMPLEX_ENTITY];
			EntityValue this_ent_val = (EntityValue)reference;
			if (new_ent_val.def == this_ent_val.def) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			n = this_ent_val.def.getNameUpperCase();
			if (new_name.compareTo(n) < 0) {
				ev_arr[0] = new_ent_val;
				ev_arr[1] = this_ent_val;
			} else {
				ev_arr[0] = this_ent_val;
				ev_arr[1] = new_ent_val;
			}
			reference = ev_arr;
			tag = COMPLEX_ENTITY_VALUE;
			length = 2;
		} else if (tag == PhFileReader.ENTITY_REFERENCE) {
			Value val_converted = convertToComplexEntityValue((CEntity)reference);
			return val_converted.addComplex(val);
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVR, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Returns a partial complex entity value within a complex entity 
 * instance represented by the current object of this class. 
 * This partial value is also wrapped in an object of type <code>Value</code>.
 * The method is related to an implementation of group references  
 * operator of Express language. 
 * @param context context in which the operation is performed.
 * @param edef simple entity data type of which a partial complex entity value 
 * is asked.
 * @return partial complex entity value.  
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #groupReference(SdaiContext, Class)
 * @see "ISO 10303-11::12.7.4 Group references"
 */
	public Value groupReference(SdaiContext context, EEntity_definition edef) throws SdaiException {
		int i;
		Value res;
		if (tag == ENTITY_VALUE) {
			if (((EntityValue)reference).def == edef) {
				return this;
			} else {
				res = new Value();
				res.tag = INDETERMINATE;
				return res;
			}
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			EntityValue [] ev_arr = (EntityValue [])reference;
			for (i = 0; i < length; i++) {
				if (ev_arr[i].def == edef) {
					res = new Value();
					res.tag = ENTITY_VALUE;
					res.reference = ev_arr[i];
					res.integer = 0;
					return res;
				}
			}
		} else if (tag == PhFileReader.ENTITY_REFERENCE) {
			CEntity inst = (CEntity)getInstance();
			ComplexEntityValue compl = inst.toValue();
			EntityValue ev = compl.getEntityValue(edef);
			if (ev == null) {
	 			res = new Value();
				res.tag = INDETERMINATE;
			} else {
				EntityValue evc = new EntityValue(inst.owning_model.repository.session);
				evc.copyEntityValue(ev);
				res = new Value();
				res.tag = ENTITY_VALUE;
				CExplicit_attribute[] expl_attrs = ((CEntityDefinition)edef).takeExplicit_attributes();
				for (i = 0; i < ((CEntityDefinition)edef).noOfPartialAttributes; i++) {
					Value val = evc.values[i];
					EData_type domain = (EData_type)expl_attrs[i].getDomain(null);
					val.d_type = domain;
					val.get_value_type(domain, context);
				}
				res.reference = evc;
				res.integer = 0;
			}
			return res;
		} else if (tag == PhFileReader.MISSING) {
			res = new Value();
			res.tag = INDETERMINATE;
			return res;
		} else {
			res = new Value();
			res.tag = INDETERMINATE;
			return res;
//			throw new SdaiException(SdaiException.SY_ERR);
		}
		return null;
	}


	private Value convertToComplexEntityValue(CEntity inst) throws SdaiException {
		ComplexEntityValue compl = inst.toValue();
		EntityValue ev = new EntityValue(inst.owning_model.repository.session);
		ev.copyEntityValue(compl.entityValues[0]);
		Value val_basis = new Value(ev);
		for (int i = 1; i < compl.def.noOfPartialEntityTypes; i++) {
			ev = new EntityValue(inst.owning_model.repository.session);
			ev.copyEntityValue(compl.entityValues[i]);
			Value val = new Value(ev);
			val_basis.addComplex(val);
		}
		return val_basis;
	}


/**
 * Returns a partial complex entity value within a complex entity 
 * instance represented by the current object of this class. 
 * This partial value is also wrapped in an object of type <code>Value</code>.
 * The method is related to an implementation of group references  
 * operator of Express language. 
 * @param context context in which the operation is performed.
 * @param provided_class Java class for the simple entity data type of which 
 * a partial complex entity value is asked.
 * @return partial complex entity value.  
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #groupReference(SdaiContext, EEntity_definition)
 * @see "ISO 10303-11::12.7.4 Group references"
 */
	public Value groupReference(SdaiContext context, Class provided_class) throws SdaiException {
		String cl_name = provided_class.getName();
		int index2 = cl_name.lastIndexOf(PhFileReader.DOT);
		String entity_name = cl_name.substring(index2 + 2).toUpperCase();
		int index1 = cl_name.lastIndexOf(PhFileReader.DOT, index2 - 1);
		String dict_name = cl_name.substring(index1 + 2, index2).toUpperCase() + 
			SdaiSession.DICTIONARY_NAME_SUFIX;
		SchemaData sch_data = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name).schemaData;
		index1 = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, entity_name);
		CEntityDefinition edef;
		if (index1 >= 0) {
			edef = sch_data.entities[index1];
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return groupReference(context, edef);
	}


/**
 * Creates an object of this class and attaches simple entity data type 
 * to it. The latter is submitted through the parameter of the constructor. 
 * This constructor is related to an implementation of complex entity instance 
 * construction operator of Express language. 
 * @param edef simple entity data type for which an object of type <code>Value</code> 
 * is asked.
 * @see "ISO 10303-11::12.10 Complex entity instance construction operator"
 */
	public Value(EEntity_definition edef) {
		tag = ENTITY_VALUE;
		integer = 0;
		reference = new EntityValue(edef);
	}


/**
 * Creates an object of this class and attaches simple entity data type 
 * to it. The latter is submitted through the parameter of the constructor. 
 * This constructor is related to an implementation of complex entity instance 
 * construction operator of Express language. 
 * @param provided_class Java class for the simple entity data type for which an 
 * object of type <code>Value</code> is asked.
 * @see "ISO 10303-11::12.10 Complex entity instance construction operator"
 */
	public Value(Class provided_class) throws SdaiException {
		tag = ENTITY_VALUE;
		integer = 0;
		String cl_name = provided_class.getName();
		int index2 = cl_name.lastIndexOf(PhFileReader.DOT);
		String entity_name = cl_name.substring(index2 + 2).toUpperCase();
		int index1 = cl_name.lastIndexOf(PhFileReader.DOT, index2 - 1);
		String dict_name = cl_name.substring(index1 + 2, index2).toUpperCase() + 
			SdaiSession.DICTIONARY_NAME_SUFIX;
		SchemaData sch_data = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name).schemaData;
		index1 = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, entity_name);
		CEntityDefinition edef;
		if (index1 >= 0) {
			edef = sch_data.entities[index1];
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		reference = new EntityValue(edef);
	}


	public Value(EntityValue eval) {
		tag = ENTITY_VALUE;
		reference = eval;
	}


/**
 * Adjoins value of an attribute to a partial complex entity value 
 * being formed (and represented by the current object of this class). 
 * To construct a partial complex entity value, this method 
 * shall be applied repeatedly, once for each attribute of the 
 * corresponding simple entity data type. 
 * The order of invocations of the method shall strictly conform the 
 * order of appearance of attributes in the definition of the entity in an Express schema. 
 * The method is related to an implementation of complex entity instance 
 * construction operator of Express language. 
 * @param val value of an attribute of the simple entity data type.
 * @return the current object of this class representing partial complex 
 * entity value.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.10 Complex entity instance construction operator"
 */
	public Value addParameter(Value val) throws SdaiException {
		if (val == null) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag != ENTITY_VALUE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EntityValue ent_val = (EntityValue)reference;
		if (ent_val.count <= integer) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
/*		if (((DataType)v_type).express_type != DataType.ENTITY) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		CEntityDefinition edef = (CEntityDefinition)v_type;
		CExplicit_attribute[] partial_attrs = edef.takeExplicit_attributes();*/
		ent_val.values[integer] = val;
		integer++;
		return this;
	}


/**
 * Creates an instance of the entity described by the current object of 
 * this class. The values of the attributes of the entity are also set.
 * The created instance is wrapped in an object of type <code>Value</code>. 
 * The parameter of the method is used to get an <code>SdaiModel</code> 
 * to make it owning for the instance created. 
 * The method is related to an implementation of complex entity instance 
 * construction operator of Express language. 
 * @param context an object of type <code>SdaiContext</code>.
 * @return an object of this class representing instance created. 
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::12.10 Complex entity instance construction operator"
 */
	public Value makeInstance(SdaiContext context) throws SdaiException {
		if (tag != ENTITY_VALUE && tag != COMPLEX_ENTITY_VALUE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVR, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (context == null) {
			printWarningToLogoValidate(AdditionalMessages.EC_CONR);
			String base = SdaiSession.line_separator + AdditionalMessages.EC_CONR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiModel model_app = context.working_model;
		if (model_app == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_WMOD);
			String base2 = SdaiSession.line_separator + AdditionalMessages.EE_WMOD;
			throw new SdaiException(SdaiException.SY_ERR, base2);
		}
		SdaiModel model_dic = model_app.underlying_schema.modelDictionary;
		int number_of_items_in_entity_name = 0;
		int byte_count_entity_name = 0;
		if (compl_ent_values == null) {
			compl_ent_values = new ComplexEntityValue();
		}
		int i;
		StaticFields staticFields = StaticFields.get();
		if (tag == ENTITY_VALUE) {
			EntityValue ev = (EntityValue)reference;
			ev.expr = true;
			compl_ent_values.entityValues[0] = ev;
			staticFields.constr_entity_name = fromString(ev.def.getNameUpperCase());
			byte_count_entity_name = staticFields.constr_entity_name.length;
			number_of_items_in_entity_name = 1;
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			if (staticFields.entity_name_components == null) {
				if (length <= SdaiSession.NUMBER_OF_ITEMS_IN_COMPLEX_ENTITY) {
					staticFields.entity_name_components = new PartialEntityName[SdaiSession.NUMBER_OF_ITEMS_IN_COMPLEX_ENTITY];
				} else {
					staticFields.entity_name_components = new PartialEntityName[length];
				}
			} else if (length > staticFields.entity_name_components.length) {
				staticFields.entity_name_components = new PartialEntityName[length];
			}
			if (length > compl_ent_values.entityValues.length) {
				compl_ent_values.enlarge(length);
			}
			EntityValue [] ev_arr = (EntityValue [])reference;
			byte_count_entity_name = 0;
			for (i = 0; i < length; i++) {
				compl_ent_values.entityValues[i] = ev_arr[i];
				compl_ent_values.entityValues[i].expr = true;
				byte [] partial_name = fromString(ev_arr[i].def.getNameUpperCase());
				if (staticFields.entity_name_components[i] == null) {
					staticFields.entity_name_components[i] = new PartialEntityName();
				}
				staticFields.entity_name_components[i].entity_name = partial_name;
				staticFields.entity_name_components[i].length_of_entity_name = partial_name.length;
				byte_count_entity_name += partial_name.length;
			}
			number_of_items_in_entity_name = length;
			byte_count_entity_name += (length - 1);
			construct_name(staticFields, length, byte_count_entity_name);
		}
		if (staticFields.create == null) {
			staticFields.create = new Create_instance(this);
		}
		SdaiRepository expr_repo = model_app.repository;
		//int inst_ident = (int)expr_repo.largest_identifier + 1;    //--VV--030310--
		long inst_ident = expr_repo.incPersistentLabel(model_app);
		Value val = new Value();
		CEntity created_instance;
		try {
			created_instance = staticFields.create.create_inst_expr(staticFields.constr_entity_name, 
				byte_count_entity_name, model_dic, model_app, inst_ident, number_of_items_in_entity_name, val);
		} catch (java.lang.IllegalAccessException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		} catch (java.lang.InstantiationException ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		if (created_instance==null) {
			String base = AdditionalMessages.RD_CONF + 
				SdaiSession.line_separator + AdditionalMessages.RD_ENT + 
				new String(staticFields.constr_entity_name, 0, byte_count_entity_name);
			printWarningToLogoValidate(base);
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		val.d_type = created_instance.getInstanceType();
		created_instance.instance_identifier = inst_ident;
		//expr_repo.largest_identifier = inst_ident;  //--VV--030310--
		compl_ent_values.def = (CEntity_definition)val.d_type;
		if (check_instance_construction(compl_ent_values)) {
			printWarningToLogoValidateGiveType(AdditionalMessages.EE_IDIN, val.d_type);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		created_instance.setAll(compl_ent_values);
		for (i = 0; i < number_of_items_in_entity_name; i++) {
			compl_ent_values.entityValues[i].expr = false;
		}
		val.v_type = val.d_type;
		val.tag = PhFileReader.ENTITY_REFERENCE;
		val.reference = created_instance;
		return val;
	}


	boolean check_instance_construction(ComplexEntityValue entity_vals) throws SdaiException {
		int count = entity_vals.def.noOfPartialEntityTypes;
		if (entity_vals.entityValues.length < count) {
			return true;
		}
		for (int i = 0; i < count; i++) {
			EntityValue partval = entity_vals.entityValues[i];
			if (partval == null) {
				return true;
			}
			if (partval.values.length < partval.count) {
				return true;
			}
			for (int j = 0; j < partval.count; j++) {
				if (partval.values[j] == null) {
					return true;
				}
			}
		}
		return false;
	}


	private void construct_name(StaticFields staticFields, int number_of_items_in_entity_name, int byte_count_entity_name) 
			throws SdaiException {
		if (staticFields.constr_entity_name == null) {
			if (byte_count_entity_name <= BYTES_COUNT_IN_ENTITY_NAME) {
				staticFields.constr_entity_name = new byte[BYTES_COUNT_IN_ENTITY_NAME];
			} else {
				staticFields.constr_entity_name = new byte[byte_count_entity_name];
			}
		} else if (byte_count_entity_name > staticFields.constr_entity_name.length) {
			staticFields.constr_entity_name = new byte[byte_count_entity_name];
		}
		int k = -1;
		for (int i = 0; i < number_of_items_in_entity_name; i++) {
			for (int j = 0; j < staticFields.entity_name_components[i].length_of_entity_name; j++) {
				k++;
				staticFields.constr_entity_name[k] = staticFields.entity_name_components[i].entity_name[j];
			}
			if (i < number_of_items_in_entity_name - 1) {
				k++;
				staticFields.constr_entity_name[k] = (byte)'$';
			}
		}
	}


	PartialEntityName [] get_complex_name(StaticFields staticFields) {
		return staticFields.entity_name_components;
	}


	byte [] fromString(String str) {
		byte bytes[] = new byte[str.length()];
		for (int l = 0; l < str.length(); l++) {
			bytes[l] = (byte)str.charAt(l);
		}
		return bytes;
	}


/*	public void scanInstances(Express expr, SdaiModel model) throws SdaiException {
		if (model.lengths == null) {
			return;
		}
		Get_instance get_inst = new Get_instance();
		ComplexEntityValue entity_values = new ComplexEntityValue();
		get_inst.put_entity_types(model);
		CEntity app_instance = get_inst.get_next();
		while (app_instance != null) {
			CEntity_definition def = (CEntity_definition)app_instance.getInstanceType();
			SchemaData schemaData = null;
			if (!(app_instance instanceof CEntity)) {
				continue;
			}
			app_instance.owning_model.prepareAll(entity_values, def);
			app_instance.getAll(entity_values);
			Value val_basis = new Value(entity_values.entityValues[0]);
			for (int i = 1; i < entity_values.def.noOfPartialEntityTypes; i++) {
				Value val = new Value(entity_values.entityValues[i]);
				val_basis.addComplex(val);
			}
			Value wrapped_inst = val_basis.makeInstance(expr);
			CEntity res_inst = (CEntity)wrapped_inst.getInstance();
			System.out.println(res_inst.toString());
			app_instance = get_inst.get_next();
		}
	}*/


	void setMixed(Object value, SelectType sel, int sel_number) throws SdaiException {
		if (value == null) {
			tag = INDETERMINATE;
			if (d_type == null) {
				d_type = sel;
			}
			return;
		}
		if (value instanceof SdaiModel.Connector) {
			tag = PhFileReader.ENTITY_REFERENCE;
			reference = value;
			return;
		}
		if (sel_number == 1) {
			if (!(value instanceof CEntity)) {
				printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			tag = PhFileReader.ENTITY_REFERENCE;
			v_type = ((CEntity)value).getInstanceType();
			reference = value;
			return;
		}
		sel_number -= 2;
		if (sel_number < 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		v_type = sel;
		aux = -1;
		setTyped(value, sel.tags[sel_number], sel.paths[sel_number], sel_number);
		aux = 0;
	}


/**
*/
	boolean getValueType(CExplicit_attribute attribute, CEntity inst) throws SdaiException {
		EData_type type = (EData_type)attribute.getDomain(null);
		d_type = type;
		DataType tp = (DataType)type;
		if (tp.express_type >= DataType.NUMBER && tp.express_type <= DataType.BINARY) {
			return getSimpleValueType((ESimple_type)type);
		} else if (tp.express_type >= DataType.LIST && tp.express_type <= DataType.AGGREGATE) {
			return getAggrType((EAggregation_type)type, inst);
		} else if (tp.express_type == DataType.ENTITY) {
			v_type = type;
			aux = PhFileReader.ENTITY_REFERENCE;
			return true;
		} else if (tp.express_type != DataType.DEFINED_TYPE) {
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		while (((DataType)type).express_type == DataType.DEFINED_TYPE) {
			EData_type underlying_type = (EData_type)((EDefined_type)type).getDomain(null);
			DataType und_tp = (DataType)underlying_type;
			if (und_tp.express_type >= DataType.SELECT && und_tp.express_type <= DataType.ENT_EXT_EXT_SELECT) {
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			} else if (und_tp.express_type >= DataType.ENUMERATION && und_tp.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				v_type = underlying_type;
				aux = PhFileReader.ENUM;
				return true;
			} else if (und_tp.express_type >= DataType.NUMBER && und_tp.express_type <= DataType.BINARY) {
				return getSimpleValueType((ESimple_type)underlying_type);
			} else if (und_tp.express_type >= DataType.LIST && und_tp.express_type <= DataType.AGGREGATE) {
				return getAggrType((EAggregation_type)underlying_type, inst);
			} else if (und_tp.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
		}
		return false;
	}


/**
*/
	private boolean getSimpleValueType(ESimple_type simple) {
		v_type = simple;
		DataType dt = (DataType)simple;
		if (dt.express_type == DataType.NUMBER) {
			aux = PhFileReader.REAL;
			return true;
		} else if (dt.express_type == DataType.INTEGER) {
			aux = PhFileReader.INTEGER;
			return true;
		} else if (dt.express_type == DataType.REAL) {
			aux = PhFileReader.REAL;
			return true;
		} else if (dt.express_type == DataType.BOOLEAN) {
			aux = PhFileReader.BOOLEAN;
			return true;
		} else if (dt.express_type == DataType.LOGICAL) {
			aux = PhFileReader.LOGICAL;
			return true;
		} else if (dt.express_type == DataType.BINARY) {
			aux = PhFileReader.BINARY;
			return true;
		} else if (dt.express_type == DataType.STRING) {
			aux = PhFileReader.STRING;
			return true;
		}
		return false;
	}


	private boolean getAggrType(EAggregation_type aggr_type, CEntity inst) throws SdaiException {
		v_type = aggr_type;
		SdaiSession ss = inst.owning_model.repository.session;
		AggregationType next_type = null;
		EData_type el_type = (EData_type)aggr_type.getElement_type(null);
		while (((DataType)el_type).express_type == DataType.DEFINED_TYPE) {
			el_type = (EData_type)((CDefined_type)el_type).getDomain(null);
		}
		DataType dt = (DataType)el_type;
		if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
			getSimpleAggrType(dt, 1);
			return true;
		} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (dt.express_type == DataType.ENTITY) {
			aux =AENTITY;
			return true;
		} else if (dt.express_type >= DataType.ENUMERATION && dt.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			if (dt.express_type >= DataType.EXTENSIBLE_ENUM) {
				reference = ((EExtensible_enumeration_type)el_type).getElements(null, 
					inst.owning_model.repository.session.sdai_context);
			} else {
				reference = ((EEnumeration_type)el_type).getElements(null);
			}
			aux =A_ENUMERATION;
			return true;
		} else if (dt.express_type >= DataType.SELECT && dt.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		el_type = (EData_type)next_type.getElement_type(null);
		while (((DataType)el_type).express_type == DataType.DEFINED_TYPE) {
			el_type = (EData_type)((CDefined_type)el_type).getDomain(null);
		}
		dt = (DataType)el_type;
		if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
			getSimpleAggrType(dt, 2);
			return true;
		} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (dt.express_type == DataType.ENTITY) {
			aux =AENTITY;
			return true;
		} else if (dt.express_type >= DataType.ENUMERATION && dt.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			aux =AA_ENUMERATION;
			return true;
		} else if (dt.express_type >= DataType.SELECT && dt.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		el_type = (EData_type)next_type.getElement_type(null);
		while (((DataType)el_type).express_type == DataType.DEFINED_TYPE) {
			el_type = (EData_type)((CDefined_type)el_type).getDomain(null);
		}
		dt = (DataType)el_type;
		if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
			getSimpleAggrType(dt, 3);
			return true;
		} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)el_type;
		} else if (dt.express_type == DataType.ENTITY) {
			aux =AENTITY;
			return true;
		} else if (dt.express_type >= DataType.ENUMERATION && dt.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
			return false;
		} else if (dt.express_type >= DataType.SELECT && dt.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			aux =AMIXED;
			return true;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		while (next_type != null) {
			el_type = (EData_type)next_type.getElement_type(null);
			while (((DataType)el_type).express_type == DataType.DEFINED_TYPE) {
				el_type = (EData_type)((CDefined_type)el_type).getDomain(null);
			}
			next_type = null;
			dt = (DataType)el_type;
			if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
				printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
				return false;
			} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
				next_type = (AggregationType)el_type;
			} else if (dt.express_type == DataType.ENTITY) {
				aux =AENTITY;
				return true;
			} else if (dt.express_type >= DataType.ENUMERATION && dt.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				printWarningToLogo(ss, SdaiSession.line_separator + AdditionalMessages.EE_NASA);
				return false;
			} else if (dt.express_type >= DataType.SELECT && dt.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				aux =AMIXED;
				return true;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		return true;
	}


	private void printWarningToLogo(SdaiSession session, String text) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text);
		} else {
			SdaiSession.println(text);
		}
	}


	private void getSimpleAggrType(DataType simple, int nesting) 
			throws SdaiException {
		if (simple.express_type == DataType.NUMBER) {
			if (nesting == 1) {
				aux =A_DOUBLE;
			} else if (nesting == 2) {
				aux =AA_DOUBLE;
			} else if (nesting == 3) {
				aux =AAA_DOUBLE;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.INTEGER) {
			if (nesting == 1) {
				aux =A_INTEGER;
			} else if (nesting == 2) {
				aux =AA_INTEGER;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.REAL) {
			if (nesting == 1) {
				aux =A_DOUBLE;
			} else if (nesting == 2) {
				aux =AA_DOUBLE;
			} else if (nesting == 3) {
				aux =AAA_DOUBLE;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.BOOLEAN) {
			if (nesting == 1) {
				aux =A_BOOLEAN;
			} else if (nesting == 2) {
				aux =AA_BOOLEAN;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.LOGICAL) {
			if (nesting == 1) {
				aux =A_LOGICAL;
			} else if (nesting == 2) {
				aux =AA_LOGICAL;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.BINARY) {
			if (nesting == 1) {
				aux =A_BINARY;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		} else if (simple.express_type == DataType.STRING) {
			if (nesting == 1) {
				aux =A_STRING;
			} else if (nesting == 2) {
				aux =AA_STRING;
			} else {
				printWarningToLogoValidate(AdditionalMessages.EE_NASA);
				throw new SdaiException(SdaiException.VT_NVLD, AdditionalMessages.EE_NASA);
			}
		}
	}




/**
 * Implements built-in function "Abs" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val the number the absolute value of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.1 Abs - arithmetic function"
 */
	public Value abs(Value val) throws SdaiException { // 15.1
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return abs(val.nested_values[0]);
		}
		d_type = ExpressTypes.NUMBER_TYPE;
		if (val.tag == PhFileReader.INTEGER) {
			integer = Math.abs(val.integer);
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == PhFileReader.REAL) {
			real = Math.abs(val.real);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "ACos" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a cosine the angle of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.2 ACos - arithmetic function"
 */
	public Value aCos(Value val) throws SdaiException { // 15.2
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return aCos(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb < -1.0 || numb > 1.0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.acos(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "ASin" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a sine the angle of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.3 ASin - arithmetic function"
 */
	public Value aSin(Value val) throws SdaiException { // 15.3
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return aSin(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb < -1.0 || numb > 1.0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.asin(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "ATan" of Express language. 
 * The parameters (of type NUMBER) are wrapped in the objects of type <code>Value</code> 
 * submitted to the method. If either of the parameters is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val1 numerator in the ratio specifying tangent the angle of which is asked.
 * @param val2 denominator in the ratio specifying tangent the angle of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.4 ATan - arithmetic function"
 */
	public Value aTan(Value val1, Value val2) throws SdaiException { // 15.4
		if (val1 == null || val2 == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val1.tag == PhFileReader.TYPED_PARAMETER) {
			return aTan(val1.nested_values[0], val2);
		}
		if (val2.tag == PhFileReader.TYPED_PARAMETER) {
			return aTan(val1, val2.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if ((val1.tag == PhFileReader.REAL || val1.tag == PhFileReader.INTEGER) && 
				(val2.tag == PhFileReader.REAL || val2.tag == PhFileReader.INTEGER)) {
			double numb1, numb2;
			if (val1.tag == PhFileReader.REAL) {
				numb1 = val1.real;
			} else {
				numb1 = val1.integer;
			}
			if (val2.tag == PhFileReader.REAL) {
				numb2 = val2.real;
			} else {
				numb2 = val2.integer;
			}
			if (numb2 == 0) {
				if (numb1 == 0) {
					printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (numb1 > 0) {
					real = Math.PI/2.0;
				} else {
					real = -Math.PI/2.0;
				}
			} else {
				real = Math.atan(numb1/numb2);
			}
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val1.tag == INDETERMINATE || val2.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val1.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "BLength" of Express language. 
 * The parameter (of type BINARY) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val binary the number of bits in which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.5 BLength - binary function"
 */
	public Value bLength(Value val) throws SdaiException { // 15.5
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return bLength(val.nested_values[0]);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.BINARY) {
			integer = ((Binary)val.reference).getSize();
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Cos" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an angle the cosine of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.6 Cos - arithmetic function"
 */
	public Value cos(Value val) throws SdaiException { // 15.6
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return cos(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			real = Math.cos(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Exists" of Express language. 
 * The parameter (of Express type GENERIC) is wrapped in an object of type 
 * <code>Value</code> submitted to the method. 
 * @param val a variable or the result of an expression of any type.
 * @return <code>true</code> if a value exists for the input parameter, 
 * and <code>false</code> otherwise.
 * @see "ISO 10303-11::15.7 Exists - general function"
 */
/*	public boolean existsBoolean(Value val) { // 15.7
		if (val.tag == INDETERMINATE) {
			return false;
		} 
		return true;
	}*/


/**
 * Implements built-in function "Exists" of Express language. 
 * The parameter (of Express type GENERIC) is wrapped in an object of type 
 * <code>Value</code> submitted to the method. 
 * @param val a variable or the result of an expression of any type.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist (since JSDAI 4.1.0).
 * @see "ISO 10303-11::15.7 Exists - general function"
 */
	public Value exists(Value val) throws SdaiException { // 15.7
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = v_type = ExpressTypes.BOOLEAN_TYPE;
		if (val.tag == INDETERMINATE) {
			integer = 0;
		} else {
			integer = 1;
		}
		tag = PhFileReader.LOGICAL;
		return this;
	}


/**
 * Implements built-in function "Exp" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val the power of the exponent.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.8 Exp - arithmetic function"
 */
	public Value exp(Value val) throws SdaiException { // 15.8
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return exp(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			real = Math.exp(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Format" of Express language. 
 * The parameters (of type NUMBER and STRING, respectively) are wrapped in the 
 * objects of type <code>Value</code> submitted to the method. 
 * If either of the parameters is indeterminate, then as a result of the 
 * function unset value is taken. 
 * @param number an integer or real number to be formatted.
 * @param form_str a string containing formatting commands. 
 * @return the current object of this class wrapping the result of the function 
 *  - a string representation of the number.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.9 Format - general function"
 */
	public Value format(Value number, Value form_str) throws SdaiException { // 15.9
		if (number == null || form_str == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (number.tag == PhFileReader.TYPED_PARAMETER) {
			return format(number.nested_values[0], form_str);
		}
		d_type = ExpressTypes.STRING_TYPE;
		if (number.tag == INDETERMINATE || form_str.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			return this;
		}
		if (number.tag != PhFileReader.REAL && number.tag != PhFileReader.INTEGER) {
			printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (form_str.tag != PhFileReader.STRING) {
			printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		String pattern = form_str.string;
		tag = PhFileReader.STRING;
		v_type = ExpressTypes.STRING_TYPE;
		if (pattern.length() == 0) {
			string = number.standard_repr();
		} else if (!recognize_picture(pattern)) {
			string = number.symbolic_repr(pattern);
		} else {
			string = number.picture_repr(pattern);
		}
		return this;
	}


	boolean recognize_picture(String str) throws SdaiException {
		char ch = str.charAt(0);
		if (ch == PhFileReader.PLUS || ch == PhFileReader.MINUS) {
			if (str.length() <= 1) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			ch = str.charAt(1);
		}
		if (ch == ANY || ch == PhFileReader.COMMA_b || 
				ch == PhFileReader.DOT || ch == PhFileReader.LEFT_PARENTHESIS) {
			return true;
		}
		return false;
	}


	String symbolic_repr(String pattern) throws SdaiException {
		int format_sign;
		int width;
		int decimals;
		int type;
		boolean proc_zero;

		int ln = pattern.length();
		char ch = pattern.charAt(0);
		int pos;
		if (ch == PhFileReader.PLUS) {
			pos = format_sign = 1;
		} else if (ch == PhFileReader.MINUS) {
			format_sign = -1;
			pos = 1;
		} else {
			pos = format_sign = 0;
		}
		int dot = pattern.indexOf(PhFileReader.DOT);
		String str_width;
		String str_decimals = null;
		if (dot > 0) {
			str_width = pattern.substring(pos, dot);
			str_decimals = pattern.substring(dot + 1, ln - 1);
		} else {
			str_width = pattern.substring(pos, ln - 1);
		}
		ch = pattern.charAt(ln - 1);
		if (ch == CAPITAL_I) {
			type = 1;
		} else if (ch == CAPITAL_F) {
			type = 2;
		} else if (ch == CAPITAL_E) {
			type = 3;
		} else {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		ch = str_width.charAt(0);
		if (ch == ZERO) {
			proc_zero = true;
		} else {
			proc_zero = false;
		}
		width = Integer.parseInt(str_width);
		if (str_decimals != null) {
			decimals = Integer.parseInt(str_decimals);
		} else {
			decimals = 0;
		}

		boolean whole_number;
		char numb_sign_char;
		char exp_sign;
		char sym = PhFileReader.DOT;
		int numb;
		int numb_sign;
		int sym_count;
		double real_numb;
		double norm_numb = 0.0;
		double fraction = 0.0;
		String s_numb;
		String s_fraction;
		String s_power;
		String str, res;
		switch (type) {
			case 1:
				if (tag == PhFileReader.INTEGER) {
					numb = this.integer;
				} else {
					numb = (int)Math.round(this.real);
				}
				if (numb > 0) {
					numb_sign = 1;
				} else if (numb < 0) {
					numb = -numb;
					numb_sign = -1;
				} else {
					numb_sign = 0;
				}
				s_numb = Integer.toString(numb);
				numb_sign_char = get_sign(format_sign, numb_sign);
				sym_count = s_numb.length() + 1;
				if (width < sym_count) {
					if (numb_sign_char == SPACE) {
						return s_numb;
					} else {
						return numb_sign_char + s_numb;
					}
				} else if (width > sym_count) {
					StaticFields staticFields = StaticFields.get();
					str = get_standard_chars(staticFields, proc_zero, width - sym_count);
					if (proc_zero) {
						res = numb_sign_char + str + s_numb;
					} else {
						res = str + numb_sign_char + s_numb;
					}
					return res;
				} else {
					return numb_sign_char + s_numb;
				}
			case 2: {
				if (tag == PhFileReader.INTEGER) {
					if (this.integer > 0) {
						numb = this.integer;
						numb_sign = 1;
					} else if (this.integer < 0) {
						numb = -this.integer;
						numb_sign = -1;
					} else {
						numb_sign = numb = 0;
					}
					whole_number = true;
				} else {
					numb = (int)this.real;
					if (this.real > 0) {
						numb_sign = 1;
						fraction = this.real - numb;
						whole_number = false;
					} else if (this.real < 0) {
						numb = -numb;
						numb_sign = -1;
						real_numb = -this.real;
						fraction = real_numb - numb;
						whole_number = false;
					} else {
						numb_sign = 0;
						whole_number = true;
					}
				}
				s_numb = Integer.toString(numb);
				if (decimals == 0) {
					decimals = 2;
				}
				StaticFields staticFields = StaticFields.get();
				if (whole_number) {
					s_fraction = get_standard_chars(staticFields, true, decimals);
				} else {
					s_fraction = get_fraction(staticFields, fraction, decimals);
					if (staticFields.number_vanishes && numb == 0) {
						numb_sign = 0;
					}
				}
				numb_sign_char = get_sign(format_sign, numb_sign);
				sym_count = s_numb.length() + decimals + 2;
				if (width < sym_count) {
					if (numb_sign_char == SPACE) {
						return s_numb + sym + s_fraction;
					} else {
						return numb_sign_char + s_numb + sym + s_fraction;
					}
				} else if (width > sym_count) {
					str = get_standard_chars(staticFields, proc_zero, width - sym_count);
					if (proc_zero) {
						res = numb_sign_char + str + s_numb + sym + s_fraction;
					} else {
						res = str + numb_sign_char + s_numb + sym + s_fraction;
					}
					return res;
				} else {
					return numb_sign_char + s_numb + sym + s_fraction;
				}
			}
			case 3: {
				if (decimals == 0) {
//					throw new SdaiException(SdaiException.SY_ERR);
					decimals = width - 7;
					if (decimals <= 0) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
				if (tag == PhFileReader.INTEGER) {
					real_numb = this.integer;
				} else {
					real_numb = this.real;
				}
				if (real_numb > 0) {
					numb_sign = 1;
				} else if (real_numb < 0) {
					real_numb = -real_numb;
					numb_sign = -1;
				} else {
					numb_sign = 0;
				}
				StaticFields staticFields = StaticFields.get();
				if (real_numb >= 1.0) {
					norm_numb = normalize_large(staticFields, real_numb, proc_zero);
					numb = (int)norm_numb;
					exp_sign = PhFileReader.PLUS;
				} else if (real_numb != 0.0) {
					norm_numb = normalize_small(staticFields, real_numb, proc_zero);
					numb = (int)norm_numb;
					if (staticFields.power > 0) {
						exp_sign = PhFileReader.MINUS;
					} else {
						exp_sign = PhFileReader.PLUS;
					}
				} else {
					numb = 0;
					exp_sign = PhFileReader.PLUS;
				}
				s_numb = Integer.toString(numb) + sym;
				if (numb_sign == 0) {
					s_fraction = get_standard_chars(staticFields, true, decimals);
				} else {
					if (!proc_zero) {
						norm_numb -= numb;
					}
					s_fraction = get_fraction(staticFields, norm_numb, decimals);
				}
				numb_sign_char = get_sign(format_sign, numb_sign);
				s_power = Integer.toString(staticFields.power);
				if (s_power.length() < 2) {
					s_power = ZERO + s_power;
				}
				res = s_numb + s_fraction + CAPITAL_E + exp_sign + s_power;
				sym_count = res.length() + 1;
				if (width < sym_count) {
					if (numb_sign_char == SPACE) {
						return res;
					} else {
						return numb_sign_char + res;
					}
				} else if (width > sym_count) {
					str = get_standard_chars(staticFields, false, width - sym_count);
					res = str + numb_sign_char + res;
					return res;
				} else {
					return numb_sign_char + res;
				}
			}
		}
		return null;
	}


	char get_sign(int format_sign, int numb_sign) throws SdaiException {
		if (format_sign < 1) {
			if (numb_sign == -1) {
				return PhFileReader.MINUS;
			} else {
				return SPACE;
			}
		} else {
			if (numb_sign == -1) {
				return PhFileReader.MINUS;
			} else if (numb_sign == 1) {
				return PhFileReader.PLUS;
			} else {
				return SPACE;
			}
		}
	}


	private String get_standard_chars(StaticFields staticFields, boolean proc_zero, int rem) throws SdaiException {
		if (proc_zero) {
			if (staticFields.preceding_zeros == null) {
				init_preceding_zeros(staticFields);
			} else if (rem > staticFields.preceding_zeros.length) {
				enlarge_preceding_zeros(staticFields, rem);
			}
			return new String(staticFields.preceding_zeros, 0, rem);
		} else {
			if (staticFields.preceding_spaces == null) {
				init_preceding_spaces(staticFields);
			} else if (rem > staticFields.preceding_spaces.length) {
				enlarge_preceding_spaces(staticFields, rem);
			}
			return new String(staticFields.preceding_spaces, 0, rem);
		}
	}


	private String get_fraction(StaticFields staticFields, double fraction, int decimals) throws SdaiException {
		double db = fraction;
		for (int i = 0; i < decimals; i++) {
			db *= 10;
		}
		int i_fraction = (int)Math.round(db);
		if (i_fraction == 0) {
			staticFields.number_vanishes = true;
		} else {
			staticFields.number_vanishes = false;
		}
		String s_fraction = Integer.toString(i_fraction);
		int ln = s_fraction.length();
		if (ln < decimals) {
			String str = get_standard_chars(staticFields, true, decimals - ln);
			return s_fraction + str;
		}
		return s_fraction;
	}


	double normalize_large(StaticFields staticFields, double d_numb, boolean proc_zero) throws SdaiException {
		staticFields.power = 0;
		double norm_numb = d_numb;
		for (int i = 0; i < MAX_POWER; i++) {
			if (norm_numb < 10.0) {
				break;
			}
			norm_numb /= 10;
			staticFields.power++;
		}
		if (proc_zero) {
			norm_numb /= 10;
			staticFields.power++;
		}
		return norm_numb;
	}


	private double normalize_small(StaticFields staticFields, double d_numb, boolean proc_zero) throws SdaiException {
		staticFields.power = 0;
		double norm_numb = d_numb;
		for (int i = 0; i < MAX_POWER; i++) {
			if (proc_zero) {
				if (norm_numb >= 0.1) {
					break;
				}
			} else {
				if (norm_numb > 1.0) {
					break;
				}
			}
			norm_numb *= 10;
			staticFields.power++;
		}
		return norm_numb;
	}


	String picture_repr(String pattern) throws SdaiException {
		int dot = pattern.lastIndexOf(PhFileReader.DOT);
		int comma = pattern.lastIndexOf(PhFileReader.COMMA_b);
		if (dot >= 0 || comma >= 0) {
			return with_separator(pattern, dot, comma);
		}
		return without_separator(pattern);
	}


	String with_separator(String pattern, int dot, int comma) throws SdaiException {
		int i;
		int last_sep;
		int numb_sign;
		int format_sign = 0;
		int ind;
		int numb;
		double fraction;
		if (dot < comma) {
			last_sep = comma;
		} else {
			last_sep = dot;
		}
		char [] copy = pattern.toCharArray();

		if (tag == PhFileReader.INTEGER) {
			if (this.integer > 0) {
				numb = this.integer;
				numb_sign = 1;
			} else if (this.integer < 0) {
				numb = -this.integer;
				numb_sign = -1;
			} else {
				numb_sign = numb = 0;
			}
			fraction = 0;
		} else {
			numb = (int)this.real;
			if (this.real > 0) {
				numb_sign = 1;
				fraction = this.real - numb;
			} else if (this.real < 0) {
				numb = -numb;
				numb_sign = -1;
				double real_numb = -this.real;
				fraction = real_numb - numb;
			} else {
				numb_sign = 0;
				fraction = 0;
			}
		}
		String s_numb = Integer.toString(numb);
		ind = s_numb.length() - 1;
		boolean still_number = true;
		for (i = last_sep - 1; i >= 0; i--) {
			if (still_number) {
				if (copy[i] == ANY) {
					copy[i] = s_numb.charAt(ind);
					ind--;
					if (ind < 0) {
						still_number = false;
					}
				}
			} else {
				if (copy[i] == ANY || copy[i] == PhFileReader.DOT || copy[i] == PhFileReader.COMMA_b) {
					copy[i] = SPACE;
				}
			}
		}
		int fr_sym_count = 0;
		for (i = last_sep + 1; i < copy.length; i++) {
			if (copy[i] == ANY) {
				fr_sym_count++;
			}
		}
		StaticFields staticFields = StaticFields.get();
		if (fraction != 0) {
			s_numb = get_fraction(staticFields, fraction, fr_sym_count);
			if (staticFields.number_vanishes && numb == 0) {
				numb_sign = 0;
			}
		} else {
			s_numb = get_standard_chars(staticFields, true, fr_sym_count);
		}
		ind = 0;
		for (i = last_sep + 1; i < copy.length; i++) {
			if (copy[i] == ANY) {
				copy[i] = s_numb.charAt(ind);
				ind++;
			}
		}
		int l_par = pattern.indexOf(PhFileReader.LEFT_PARENTHESIS);
		int r_par = pattern.lastIndexOf(PhFileReader.RIGHT_PARENTHESIS);
		if (l_par >= 0 && r_par >= 0 && numb_sign >= 0) {
			copy[l_par] = SPACE;
			copy[r_par] = SPACE;
		}
		ind = pattern.indexOf(PhFileReader.PLUS);
		i = pattern.indexOf(PhFileReader.MINUS);
		if (ind >= 0 && i < 0) {
			format_sign = 1;
		} else if (ind < 0 && i >= 0) {
			format_sign = -1;
			ind = i;
		} else if (ind >= 0 && i >= 0) {
			if (ind < i) {
				format_sign = 1;
			} else {
				format_sign = -1;
				ind = i;
			}
		} else {
			ind = -1;
		}
		if (ind >= 0) {
			copy[ind] = get_sign(format_sign, numb_sign);
		}
		return new String(copy);
	}


	String without_separator(String pattern) throws SdaiException {
		int i;
		int numb_sign;
		int format_sign = 0;
		int ind;
		int numb;
		char [] copy = pattern.toCharArray();
		if (tag == PhFileReader.INTEGER) {
			numb = this.integer;
		} else {
			numb = (int)Math.round(this.real);
		}
		if (numb > 0) {
			numb_sign = 1;
		} else if (numb < 0) {
			numb = -numb;
			numb_sign = -1;
		} else {
			numb_sign = 0;
		}
		String s_numb = Integer.toString(numb);
		ind = s_numb.length() - 1;
		boolean still_number = true;
		for (i = copy.length - 1; i >= 0; i--) {
			if (still_number) {
				if (copy[i] == ANY) {
					copy[i] = s_numb.charAt(ind);
					ind--;
					if (ind < 0) {
						still_number = false;
					}
				}
			} else {
				if (copy[i] == ANY) {
					copy[i] = SPACE;
				}
			}
		}
		int l_par = pattern.indexOf(PhFileReader.LEFT_PARENTHESIS);
		int r_par = pattern.lastIndexOf(PhFileReader.RIGHT_PARENTHESIS);
		if (l_par >= 0 && r_par >= 0 && numb_sign >= 0) {
			copy[l_par] = SPACE;
			copy[r_par] = SPACE;
		}
		ind = pattern.indexOf(PhFileReader.PLUS);
		i = pattern.indexOf(PhFileReader.MINUS);
		if (ind >= 0 && i < 0) {
			format_sign = 1;
		} else if (ind < 0 && i >= 0) {
			format_sign = -1;
			ind = i;
		} else if (ind >= 0 && i >= 0) {
			if (ind < i) {
				format_sign = 1;
			} else {
				format_sign = -1;
				ind = i;
			}
		} else {
			ind = -1;
		}
		if (ind >= 0) {
			copy[ind] = get_sign(format_sign, numb_sign);
		}
		return new String(copy);
	}


	String standard_repr() throws SdaiException {
		if (tag == PhFileReader.INTEGER) {
			return symbolic_repr(INT_FORMAT);
		} else {
			return symbolic_repr(REAL_FORMAT);
		}
	}


/**
 * Implements built-in function "HiBound" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object 
 * of type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an aggregate the upper index (if ARRAY) or upper bound 
 * (if BAG, LIST or SET) of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.10 HiBound - arithmetic function"
 */
	public Value hiBound(Value val) throws SdaiException { // 15.10
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.EMBEDDED_LIST) {
			if (((DataType)val.v_type).express_type == DataType.ARRAY) {
				CArray_type arr_type = (CArray_type)val.v_type;
				integer = arr_type.getUpper_index(null).getBound_value(null);
				tag = PhFileReader.INTEGER;
				v_type = ExpressTypes.INTEGER_TYPE;
			} else {
				EVariable_size_aggregation_type var_aggr_type = (EVariable_size_aggregation_type)val.v_type;
				if (var_aggr_type.testUpper_bound(null)) {
					integer = var_aggr_type.getUpper_bound(null).getBound_value(null);
					tag = PhFileReader.INTEGER;
					v_type = ExpressTypes.INTEGER_TYPE;
				} else {
					tag = INDETERMINATE;
				}
			}
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "HiIndex" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object 
 * of type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an aggregate the upper index (if ARRAY) or the number of elements 
 * (if BAG, LIST or SET) of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.11 HiIndex - arithmetic function"
 */
	public Value hiIndex(Value val) throws SdaiException { // 15.11
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.EMBEDDED_LIST) {
			if (((DataType)val.v_type).express_type == DataType.ARRAY) {
				CArray_type arr_type = (CArray_type)val.v_type;
				integer = arr_type.getUpper_index(null).getBound_value(null);
			} else {
				integer = val.length;
			}
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Length" of Express language. 
 * The parameter (of type STRING) is wrapped in an object 
 * of type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a string the the number of characters in which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.12 Length - string function"
 */
	public Value length(Value val) throws SdaiException { // 15.12
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return length(val.nested_values[0]);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.STRING) {
			if (val.string==SdaiSession.asterisk && val.reference instanceof String) {
				val.string = (String)val.reference;
			}
			integer = val.string.length();
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "LoBound" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object 
 * of type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an aggregate the lower index (if ARRAY) or lower bound 
 * (if BAG, LIST or SET) of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.13 LoBound - arithmetic function"
 */
	public Value loBound(Value val) throws SdaiException { // 15.13
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.EMBEDDED_LIST) {
			if (((DataType)val.v_type).express_type == DataType.ARRAY) {
				CArray_type arr_type = (CArray_type)val.v_type;
				integer = arr_type.getLower_index(null).getBound_value(null);
			} else {
				EVariable_size_aggregation_type var_aggr_type = (EVariable_size_aggregation_type)val.v_type;
				integer = var_aggr_type.getLower_bound(null).getBound_value(null);
			}
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Log" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a number the natural logarithm of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.14 Log - arithmetic function"
 */
	public Value log(Value val) throws SdaiException { // 15.14
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return log(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb <= 0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.log(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Log2" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a number the base two logarithm of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.15 Log2 - arithmetic function"
 */
	public Value log2(Value val) throws SdaiException { // 15.15
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return log2(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb <= 0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.log(numb) / Math.log(2.0);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Log10" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a number the base ten logarithm of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.16 Log10 - arithmetic function"
 */
	public Value log10(Value val) throws SdaiException { // 15.16
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return log10(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb <= 0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.log(numb) / Math.log(10.0);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "LoIndex" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object 
 * of type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an aggregate the lower index (if ARRAY) of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.17 LoIndex - arithmetic function"
 */
	public Value loIndex(Value val) throws SdaiException { // 15.17
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == PhFileReader.EMBEDDED_LIST) {
			if (((DataType)val.v_type).express_type == DataType.ARRAY) {
				CArray_type arr_type = (CArray_type)val.v_type;
				integer = arr_type.getLower_index(null).getBound_value(null);
			} else {
				integer = 1;
			}
			tag = PhFileReader.INTEGER;
			v_type = ExpressTypes.INTEGER_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "NVL" of Express language. 
 * The parameters (of Express type GENERIC) are wrapped in the objects of type 
 * <code>Value</code> submitted to the method. If both parameters are 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param context context in which the function is considered.
 * @param val the main input value (which is returned by the function provided this value exists).
 * @param substitute an alternate value (which is returned if the main value is unset).
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.18 NVL - null value function"
 */
	public Value NVL(SdaiContext context, Value val, Value substitute) throws SdaiException { // 15.18
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag != INDETERMINATE) {
			d_type = val.v_type;
			set(context, val);
			d_type = val.d_type;
			v_type = val.v_type;
		} else {
			if (substitute == null) {
				printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
				throw new SdaiException(SdaiException.VA_NEXS);
			}
			if (substitute.tag == INDETERMINATE) {
				if (val.d_type != null) {
					d_type = val.d_type;
				} else if (substitute.d_type != null) {
					d_type = substitute.d_type;
				}
				tag = INDETERMINATE;
			} else {
				d_type = substitute.v_type;
				boolean temp = false;
				if (d_type == null) {
					substitute.v_type = d_type = ExpressTypes.GENERIC_TYPE;
					temp = true;
				}
				set(context, substitute);
				if (temp) {
					d_type = v_type;
					substitute.v_type = null;
				} else {
					d_type = substitute.d_type;
				}
			}
		}
		return this;
	}


/**
 * Implements built-in function "Odd" of Express language. 
 * The parameter (of type INTEGER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function UNKNOWN value is returned. 
 * @param val a number which is checked whether it is odd.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.19 Odd - arithmetic function"
 */
	public Value odd(Value val) throws SdaiException { // 15.19
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return odd(val.nested_values[0]);
		}
		if (val.tag != PhFileReader.INTEGER && val.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		v_type = ExpressTypes.LOGICAL_TYPE;
		if (val.tag == PhFileReader.INTEGER) {
			if (val.integer % 2 != 0) {
				integer = 1;
			} else {
				integer = 0;
			}
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		}
		return this;
	}


/**
 * Implements built-in function "RolesOf" of Express language. 
 * The parameter (of Express type GENERIC) is wrapped in an object of type 
 * <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an entity instance the names of the roles played by which are asked. 
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.20 RolesOf - general function"
 */
	public Value rolesOf(Value val) throws SdaiException { // 15.20
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.SET_STRING_TYPE;
		if (val.tag == PhFileReader.ENTITY_REFERENCE) {
			CEntity inst = (CEntity)val.reference;
			tag = PhFileReader.EMBEDDED_LIST;
			inst.rolesOfExpress(this);
			v_type = ExpressTypes.SET_STRING_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = PhFileReader.EMBEDDED_LIST;
			v_type = ExpressTypes.SET_STRING_TYPE;
			length = 0;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


	void add_qualified_name(String name) throws SdaiException {
		boolean appears = false;
		for (int i = 0; i < length; i++) {
			if (nested_values[i].string.equals(name)) {
				appears = true;
				break;
			}
		}
		if (!appears) {
			addNewString(name);
		}
	}


/**
 * Implements built-in function "Sin" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an angle the sine of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.21 Sin - arithmetic function"
 */
	public Value sin(Value val) throws SdaiException { // 15.21
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return sin(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			real = Math.sin(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "SizeOf" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object of 
 * type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then indeterminate value of size is returned. 
 * @param val an aggregate the number of elements of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #sizeOfExt
 * @see #sizeOfExt0
 * @see "ISO 10303-11::15.22 SizeOf - aggregate function"
 */
	public Value sizeOf(Value val) throws SdaiException { // 15.22
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			return this;
		}
		DataType dt = (DataType)val.v_type;
		if (dt.express_type < DataType.LIST || dt.express_type > DataType.AGGREGATE) {
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		if (((DataType)val.v_type).express_type == DataType.ARRAY) {
			EArray_type arr_type = (EArray_type)val.v_type;
			integer = arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		} else {
//			integer = val.getMemberCount();
			integer = val.length;
		}
		tag = PhFileReader.INTEGER;
		v_type = ExpressTypes.INTEGER_TYPE;
		return this;
	}


/**
 * Implements built-in function "SizeOf" of Express language. 
 * The aggregate (of Express type AGGREGATE OF GENERIC) is wrapped in an object of 
 * type <code>Value</code> submitted to the method as the second parameter. 
 * If this parameter is indeterminate, then indeterminate value of size is returned. 
 * Through the <code>context</code> parameter, this method transfers some 
 * data to the where rule validation methods. 
 * @param context context in which the function is considered.
 * @param val an aggregate the number of elements of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #sizeOf
 * @see #sizeOfExt0
 * @see "ISO 10303-11::15.22 SizeOf - aggregate function"
 * @since 4.1.0
 */
 	public Value sizeOfExt(SdaiContext context, Value val) throws SdaiException { // 15.22
 		if (context != null) {
 			context.empty_aggr = false;
 		}
 		return sizeOfExtCommon(context, val);
 	}


/**
 * Implements built-in function "SizeOf" of Express language. 
 * The aggregate (of Express type AGGREGATE OF GENERIC) is wrapped in an object of 
 * type <code>Value</code> submitted to the method as the second parameter. 
 * If this parameter is indeterminate, then indeterminate value of size is returned. 
 * Through the <code>context</code> parameter, this method transfers some 
 * data to the where rule validation methods. 
 * @param context context in which the function is considered.
 * @param val an aggregate the number of elements of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #sizeOf
 * @see #sizeOfExt
 * @see "ISO 10303-11::15.22 SizeOf - aggregate function"
 * @since 4.1.0
 */
 	public Value sizeOfExt0(SdaiContext context, Value val) throws SdaiException { // 15.22
 		if (context != null) {
 			context.empty_aggr = true;
 		}
 		return sizeOfExtCommon(context, val);
 	}


	private Value sizeOfExtCommon(SdaiContext context, Value val) throws SdaiException {
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.INTEGER_TYPE;
		if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
			return this;
		}
		DataType dt = (DataType)val.v_type;
		if (dt.express_type < DataType.LIST || dt.express_type > DataType.AGGREGATE) {
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		if (((DataType)val.v_type).express_type == DataType.ARRAY) {
			EArray_type arr_type = (EArray_type)val.v_type;
			integer = arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		} else {
			integer = val.length;
		}
		tag = PhFileReader.INTEGER;
		v_type = ExpressTypes.INTEGER_TYPE;
		if (context == null) {
			return this;
		}
		if (val.length == 0) {
			context.aggr_size = 0;
			return this;
		}
		if (context.ent_instances == null) {
			if (val.length <= context.NUMBER_OF_INSTANCES) {
				context.ent_instances = new CEntity[context.NUMBER_OF_INSTANCES];
			} else {
				context.ent_instances = new CEntity[val.length];
			}
		} else if (context.ent_instances.length < val.length) {
			enlarge_instances_array(context, val.length);
		}
		int i;
		DataType el_tp = (DataType)((EAggregation_type)val.v_type).getElement_type(null);
		if (el_tp.express_type == DataType.ENTITY) {
			for (i = 0; i < val.length; i++) {
				context.ent_instances[i] = (CEntity)val.nested_values[i].reference;
			}
			context.aggr_size = val.length;
		} else {
			boolean failed = false;
			for (i = 0; i < val.length; i++) {
				Value eval = val.nested_values[i];
				DataType eltp = (DataType)eval.v_type;
				if (eltp.express_type == DataType.ENTITY) {
					context.ent_instances[i] = (CEntity)eval.reference;
				} else {
					failed = true;
					break;
				}
			}
			if (failed) {
				context.aggr_size = 0;
			} else {
				context.aggr_size = val.length;
			}
		}
		return this;
	}


	private void enlarge_instances_array(SdaiContext context, int demand) {
		int new_length = context.ent_instances.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		CEntity new_ent_instances[] = new CEntity[new_length];
		context.ent_instances = new_ent_instances;
	}


/**
 * Implements built-in function "Sqrt" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a number the non-negative square root of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.23 Sqrt - arithmetic function"
 */
	public Value sqrt(Value val) throws SdaiException { // 15.23
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return sqrt(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			if (numb < 0) {
				printWarningToLogoValidate(AdditionalMessages.EE_EFNE);
				throw new SdaiException(SdaiException.SY_ERR);
			}
			real = Math.sqrt(numb);
			tag = PhFileReader.REAL;
			v_type = ExpressTypes.REAL_TYPE;
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Tan" of Express language. 
 * The parameter (of type NUMBER) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val an angle the tangent of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.24 Tan - arithmetic function"
 */
	public Value tan(Value val) throws SdaiException { // 15.24
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == PhFileReader.TYPED_PARAMETER) {
			return tan(val.nested_values[0]);
		}
		d_type = ExpressTypes.REAL_TYPE;
		if (val.tag == PhFileReader.REAL || val.tag == PhFileReader.INTEGER) {
			double numb;
			if (val.tag == PhFileReader.REAL) {
				numb = val.real;
			} else {
				numb = val.integer;
			}
			double db = Math.PI / 2.0;
			db = numb / db;
			int i = (int)db;
			if (i == db && i % 2 == 1) {
				tag = INDETERMINATE;
			} else {
				real = Math.tan(numb);
				tag = PhFileReader.REAL;
				v_type = ExpressTypes.REAL_TYPE;
			}
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "TypeOf" of Express language. 
 * The parameter (a value of Express type GENERIC) of this function is wrapped in 
 * the current object of this class. 
 * If the parameter is indeterminate, then as a result of the function an 
 * empty set is returned. 
 * @param context context in which the function is considered.
 * @return a set of the names of all types the specified value is a member of.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #typeOfV
 * @see "ISO 10303-11::15.25 TypeOf - general function"
 */
	public A_string typeOf(SdaiContext context) throws SdaiException { // 15.25
//long time1=0, time2=0, time3, time4, time5, time6, time7, time8, time9, time10, time11;
//time1 = System.currentTimeMillis();
		if (context == null) {
			printWarningToLogoValidate(AdditionalMessages.EC_CONR);
			throw new SdaiException(SdaiException.SY_ERR, AdditionalMessages.EC_CONR);
		}
		if (tag == COMPLEX_ENTITY_VALUE || tag == ENTITY_VALUE) {
			Value v = makeInstance(context);
			d_type = v.d_type;
			set(v);
//			set(makeInstance(context));
		}
		int i;
		if (tag == INDETERMINATE) {
			return new A_string();
		}
		DataType vtp = (DataType)v_type;

		if (context.schema != null && vtp.typeof_schema == context.schema) {
			return vtp.typeof_aggr;
		}

		if (types == null) {
			types = new Object[NUMBER_OF_TYPES];
		}
		if (is_included == null) {
			is_included = new boolean[NUMBER_OF_UNQUALIFIED_TYPES];
		}
		for (i = 0; i < NUMBER_OF_UNQUALIFIED_TYPES; i++) {
			is_included[i] = false;
		}
		types[0] = take_data_type(null);
		if (aux >= 0) {
			is_included[aux] = true;
		}
		types_count = 1;
		int search_ind = 0;
		Object item;
		while (search_ind < types_count) {
//if (prnt)
//System.out.println("Value <<<<<<<<   search_ind = " + search_ind + 
//"   types[search_ind]: " + types[search_ind]);
			if (types[search_ind] instanceof String || 
					types[search_ind] instanceof EEnumeration_type) {
				search_ind++;
				continue;
			}
			if (((DataType)types[search_ind]).express_type >= DataType.SELECT && 
					((DataType)types[search_ind]).express_type <= DataType.ENT_EXT_EXT_SELECT) {
				examine_select((ESelect_type)types[search_ind], context);
			} else if (((DataType)types[search_ind]).express_type == DataType.DEFINED_TYPE) {
				DataType dt = (DataType)((CDefined_type)types[search_ind]).getDomain(null);
				if ( (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) || 
						(dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) ) {
					item = take_data_type(dt);
					if_new_then_add(item, aux);
				} else {
					if_new_then_add(dt, -1);
				}
			} else if (((DataType)types[search_ind]).express_type == DataType.ENTITY) {
				CSchema_definition schm = (CSchema_definition)context.schema;
//if (prnt) System.out.println("Value VVVVVVVVVVVVVVVVV    schema: " + schm.getName(null));
				if (schm != null) {
					check_entity((CEntity_definition)types[search_ind], schm);
				}
			} else {
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
			search_ind++;
		}

		search_ind = 0;
		while (search_ind < types_count) {
			if (types[search_ind] instanceof EEntity_definition) {
				AEntity_definition super_types = 
					((CEntity_definition)types[search_ind]).getSupertypes(null);
				SdaiIterator it_st = super_types.createIterator();
				while (it_st.next()) {
					CEntity_definition entity = (CEntity_definition)super_types.getCurrentMember(it_st);
					if_new_then_add(entity, -1);
				}
			} 
			search_ind++;
		}
		if ((is_included[1] || is_included[2]) && !is_included[0]) {
			is_included[0] = true;
			if (types_count >= types.length) {
				enlarge_types();
			}
// check if array sufficient
			types[types_count++] = "NUMBER";
		}
		if (is_included[1] && !is_included[2]) {
			is_included[2] = true;
			if (types_count >= types.length) {
				enlarge_types();
			}
// check if array sufficient
			types[types_count++] = "REAL";
		}
		if (is_included[3] && !is_included[4]) {
			is_included[4] = true;
			if (types_count >= types.length) {
				enlarge_types();
			}
// check if array sufficient
			types[types_count++] = "LOGICAL";
		}
		if (is_included[8] && !is_included[9]) {
			is_included[9] = true;
			if (types_count >= types.length) {
				enlarge_types();
			}
// check if array sufficient
			types[types_count++] = "BAG";
		}

//		SdaiModel owner = ((CEntity)v_type).owning_model;
		SchemaData schd = null;
		CSchema_definition value_schema = (CSchema_definition)context.schema;
		String value_schema_name = null;
		if (value_schema != null) {
			value_schema_name = value_schema.getName(null).toUpperCase();
			schd = value_schema.modelDictionary.schemaData;
		}
		if ( (vtp.express_type < DataType.LIST || vtp.express_type > DataType.AGGREGATE) && schd != null) {
//			if (((CEntity)v_type).owning_model.described_schema == null) {
//				throw new SdaiException(SdaiException.SY_ERR);
//			}
//			SchemaData schd_type = ((CEntity)v_type).owning_model.schemaData;
			search_ind = 0;
			while (search_ind < types_count) {
//				schd_type.check_if_typeOf(types[search_ind], this, context);
				if (!(types[search_ind] instanceof String)) {
					DataType dtp = (DataType)types[search_ind];
//					if (dtp.express_type == DataType.ENTITY || dtp.express_type == DataType.DEFINED_TYPE) {
//						schd.check_if_typeOf(dtp, this, context);
//					}
					if (dtp.express_type == DataType.ENTITY) {
						schd.check_entity_if_typeOf((CEntityDefinition)dtp, this, context);
					} else if (dtp.express_type == DataType.DEFINED_TYPE) {
						schd.check_def_type_if_typeOf((DefinedType)dtp, this, context);
					}
//					schd.check_if_typeOf(types[search_ind], this, context);
				}
				search_ind++;
			}
		}

		A_string type_names = new A_string();
		SdaiModel type_owner;
//		String value_schema = null;
//		if (!(v_type instanceof EAggregation_type)) {
//			value_schema = owner.described_schema.getName(null).toUpperCase();
//		}
		CSchema_definition type_schema;
		String type_schema_name;
		String type_name;
		String str;
		int index;
		search_ind = 0;
		while (search_ind < types_count) {
			if (types[search_ind] instanceof String) {
				type_names.addAtTheEnd(types[search_ind], null);
			} else if ( ((DataType)types[search_ind]).express_type == DataType.ENTITY ) {
				CEntity_definition edef = (CEntity_definition)types[search_ind];
				type_owner = ((CEntity)edef).owning_model;
				type_schema = type_owner.described_schema;
				if (type_schema == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				String type_name_JSDAI = edef.getNameUpperCase();
				type_name = edef.getNameExpress();
				if (type_schema == value_schema) {
					if (use_asterisk) {
						str = "*." + type_name;
					} else {
						str = value_schema_name + "." + type_name;
					}
					type_names.addAtTheEnd(str, null);
				} else {
					if (use_asterisk) {
						str = "*." + type_name;
					} else {
						type_schema_name = type_schema.getName(null).toUpperCase();
						str = type_schema_name + "." + type_name;
					}
					type_names.addAtTheEnd(str, null);
//					if (value_schema != null) {
					if (schd == null) {
						String base = SdaiSession.line_separator + AdditionalMessages.EE_SCEM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
						index = schd.find_entity(0, schd.sNames.length - 1, type_name_JSDAI);
						if (index < 0) {
							String base = SdaiSession.line_separator + AdditionalMessages.EC_TOF1 +
								value_schema.getName(null) + AdditionalMessages.EC_TOF2 + 
								edef.getName(null) + AdditionalMessages.EC_TOF3;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						if (schd.e_aliases == null) {
							schd.take_aliases_of_entity_defs();
						}
						if (schd.e_aliases[index] != null) {
							if (use_asterisk) {
								str = "*." + schd.e_aliases[index];
							} else {
								str = value_schema_name + "." + schd.e_aliases[index];
							}
							type_names.addAtTheEnd(str, null);
						} else if (!use_asterisk) {
							str = value_schema_name + "." + type_name;
							type_names.addAtTheEnd(str, null);
						}
//					}
				}
			} else if ( ((DataType)types[search_ind]).express_type == DataType.DEFINED_TYPE ) {
				CDefined_type tdef = (CDefined_type)types[search_ind];
				type_owner = ((CEntity)tdef).owning_model;
				type_schema = type_owner.described_schema;
				if (type_schema == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				type_name = tdef.getNameUpperCase();
//System.out.println(" Value ^^^^^^^^^^^^^^^^^^^^^^^^^^^^  search_ind = " + search_ind + 
//"   type_schema.getName(null).toUpperCase(): " + type_schema.getName(null).toUpperCase() + 
//"   value_schema_name: " + value_schema_name);
				if (type_schema == value_schema) {
					if (use_asterisk) {
						str = "*." + type_name;
					} else {
						str = value_schema_name + "." + type_name;
					}
					type_names.addAtTheEnd(str, null);
				} else {
					if (use_asterisk) {
						str = "*." + type_name;
					} else {
						type_schema_name = type_schema.getName(null).toUpperCase();
						str = type_schema_name + "." + type_name;
					}
					type_names.addAtTheEnd(str, null);
//					if (value_schema != null) {
					if (schd == null) {
						String base = SdaiSession.line_separator + AdditionalMessages.EE_SCEM;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
						index = schd.find_type(0, schd.defTypesCount - 1, type_name);
						if (index < 0) {
							String base = SdaiSession.line_separator + AdditionalMessages.EC_TOF1 +
								value_schema.getName(null) + AdditionalMessages.EC_TOF4 + 
								tdef.getName(null) + AdditionalMessages.EC_TOF3;
							throw new SdaiException(SdaiException.SY_ERR, base);
						}
						if (schd.dt_aliases == null) {
							schd.take_aliases_of_defined_types();
						}
						if (schd.dt_aliases[index] != null) {
							if (use_asterisk) {
								str = "*." + schd.dt_aliases[index];
							} else {
								str = value_schema_name + "." + schd.dt_aliases[index];
							}
							type_names.addAtTheEnd(str, null);
						} else if (!use_asterisk) {
							str = value_schema_name + "." + type_name;
							type_names.addAtTheEnd(str, null);
						}
//					}
				}
			} else if (((DataType)types[search_ind]).express_type >= DataType.SELECT && 
					((DataType)types[search_ind]).express_type <= DataType.ENT_EXT_EXT_SELECT) {
// The code excerpt below was excluded, 2005-08-22
/*				SelectType sel = (SelectType)types[search_ind];
				type_owner = ((CEntity)sel).owning_model;
				type_schema = type_owner.described_schema;
				type_name = sel.getNameUpperCase();
				if (type_schema == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (use_asterisk) {
					str = "*." + type_name;
				} else {
					type_schema_name = type_schema.getName(null).toUpperCase();
					str = type_schema_name + "." + type_name;
				}
				type_names.addAtTheEnd(str, null);*/
			} else if (((DataType)types[search_ind]).express_type >= DataType.ENUMERATION && 
					((DataType)types[search_ind]).express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
// The code excerpt below was excluded, 2005-11-14
/*				EEnumeration_type enum = (EEnumeration_type)types[search_ind];
				type_owner = ((CEntity)enum).owning_model;
				type_schema = type_owner.described_schema;
				type_name = ((EnumerationType)enum).getNameUpperCase();
				if (type_schema == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (use_asterisk) {
					str = "*." + type_name;
				} else {
					type_schema_name = type_schema.getName(null).toUpperCase();
					str = type_schema_name + "." + type_name;
				}
				type_names.addAtTheEnd(str, null);*/
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			search_ind++;
		}
//if (prnt) System.out.println(" Value !!!  type_names: " + type_names);
		vtp.typeof_schema = context.schema;
		vtp.typeof_aggr = type_names;
		vtp.typeof_value = null;
		return type_names;
	}


/**
 * Implements built-in function "TypeOf" of Express language. 
 * The parameter (a value of Express type GENERIC) of this function is wrapped in 
 * the current object of this class. 
 * The result evaluates to the list of strings represented by the newly created 
 * object of type <code>Value</code>.
 * If the parameter is indeterminate, then as a result of the function an 
 * empty list is returned. 
 * @param context context in which the function is considered.
 * @return a list of the names of all types the specified value is a member of.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #typeOf
 * @see "ISO 10303-11::15.25 TypeOf - general function"
 */
	public Value typeOfV(SdaiContext context) throws SdaiException { // 15.25
		A_string str_aggr = typeOf(context);
		DataType vtp = (DataType)v_type;
		if (vtp != null && vtp.typeof_value != null) {
			return vtp.typeof_value;
		}
		Value val = alloc(ExpressTypes.SET_STRING_TYPE);
		val.v_type = ExpressTypes.SET_STRING_TYPE;
		val.tag = PhFileReader.EMBEDDED_LIST;
		if (tag == INDETERMINATE) {
			val.length = 0;
			return val;
		}
		val.enlarge_nested_values(str_aggr.myLength);
		for (int i = 0; i < str_aggr.myLength; i++) {
			val.nested_values[i] = alloc(ExpressTypes.STRING_TYPE);
		}
		str_aggr.getValue(val, str_aggr.myType, context);
		val.length = str_aggr.myLength;
		val.reference = str_aggr;
		if (vtp != null) {
			vtp.typeof_value = val;
		}
		return val;
	}


	boolean if_new_then_add(Object item, int index) throws SdaiException {
		if (item instanceof String) {
			if (is_included[index]) {
				return false;
			}
			is_included[index] = true;
// check if array sufficient
			if (types_count >= types.length) {
				enlarge_types();
			}
			types[types_count++] = item;
			return true;
		} else {
			boolean found = false;
			for (int i = 0; i < types_count; i++) {
				if (types[i] == item) {
					found = true;
					break;
				}
			}
			if (!found) {
				if (types_count >= types.length) {
					enlarge_types();
				}
				types[types_count++] = item;
				return true;
			}
			return false;
		}
	}


	Object take_data_type(DataType dt) throws SdaiException {
		if (dt == null) {
			dt = (DataType)v_type;
//			dt = d_type;
		}
		if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
			return take_data_type_simple(dt);
		} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
			if (dt.express_type == DataType.ARRAY) {
				aux = 7;
				return "ARRAY";
			} else if (dt.express_type == DataType.SET) {
				aux = 8;
				return "SET";
			} else if (dt.express_type == DataType.BAG) {
				aux = 9;
				return "BAG";
			} else if (dt.express_type == DataType.LIST) {
				aux = 10;
				return "LIST";
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		} else if (dt.express_type == DataType.ENTITY || dt.express_type == DataType.DEFINED_TYPE || 
				(dt.express_type >= DataType.ENUMERATION && dt.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM)) {
			aux = -1;
			return dt;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


	String take_data_type_simple(DataType type) throws SdaiException {
		if (type.express_type == DataType.NUMBER) {
			aux = 0;
			return "NUMBER";
		} else if (type.express_type == DataType.INTEGER) {
			aux = 1;
			return "INTEGER";
		} else if (type.express_type == DataType.REAL) {
			aux = 2;
			return "REAL";
		} else if (type.express_type == DataType.BOOLEAN) {
			aux = 3;
			return "BOOLEAN";
		} else if (type.express_type == DataType.LOGICAL) {
			aux = 4;
			return "LOGICAL";
		} else if (type.express_type == DataType.BINARY) {
			aux = 5;
			return "BINARY";
		} else if (type.express_type == DataType.STRING) {
			aux = 6;
			return "STRING";
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


	private void examine_select(ESelect_type type, SdaiContext context) throws SdaiException {
		AEntity sels = (AEntity)type.getSelections(null, context);
		if (sels.myLength < 0) {
			sels.resolveAllConnectors();
		}
		EData_type selection;
		if (sels.myLength == 1) {
			selection = (EData_type)sels.myData;
			if (check_type(selection, context)) {
				if_new_then_add(selection, -1);
			}
		} else {
			Object [] myDataA = (Object [])sels.myData;
			for (int i = 0; i < sels.myLength; i++) {
				selection = (EData_type)myDataA[i];
				if (check_type(selection, context)) {
					if_new_then_add(selection, -1);
				}
			}
		}
//		for (int i = 1; i <= sels.myLength; i++) {
//			EData_type selection = (EData_type)sels.getByIndexEntity(i);
//			if (check_type(selection, context)) {
//				if_new_then_add(selection, -1);
//			}
//		}
	}


	private boolean check_type(EData_type type, SdaiContext context) throws SdaiException {
		if (type == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (type == v_type) {
			return true;
		}
		boolean res;
		ESelect_type sel_type = getSelect(type);
		if (sel_type != null) {
			res = check_defined_type(type);
			if (res) {
				return true;
			}
			AEntity sels = (AEntity)sel_type.getSelections(null, context);
			if (sels.myLength < 0) {
				sels.resolveAllConnectors();
			}
			if (sels.myLength == 1) {
				return check_type((EData_type)sels.myData, context);
			} else {
				Object [] myDataA = (Object [])sels.myData;
				for (int i = 0; i < sels.myLength; i++) {
					res = check_type((EData_type)myDataA[i], context);
					if (res) {
						return true;
					}
				}
			}
//			for (int i = 1; i <= sels.myLength; i++) {
//				EData_type part_type = (EData_type)sels.getByIndexEntity(i);
//				res = check_type(part_type, context);
//				if (res) {
//					return true;
//				}
//			}
			return false;
		}
		return check_non_select(type);
	}

	private boolean check_non_select(EData_type type) throws SdaiException {
		DataType dt = (DataType)type;
		if (dt.express_type >= DataType.NUMBER && dt.express_type <= DataType.BINARY) {
			return check_simple_type(dt);
		} else if (dt.express_type >= DataType.LIST && dt.express_type <= DataType.AGGREGATE) {
			return check_aggr((EAggregation_type)type);
		} else if (dt.express_type == DataType.ENTITY) {
			if (tag == PhFileReader.ENTITY_REFERENCE) {
				return ((EEntity_definition)v_type).isSubtypeOf((EEntity_definition)type);
			}
			return false;
		} else if (type instanceof CData_type) {
			if (((EData_type)type).getName(null).equals("_ENTITY")) {
				if (tag == PhFileReader.ENTITY_REFERENCE) {
					return true;
				}
				return false;
			}
			return true;
		} else if (dt.express_type != DataType.DEFINED_TYPE) {
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		if (((DataType)v_type).express_type == DataType.DEFINED_TYPE) {
			if (check_defined_type(type)) {
				return true;
			}
		}
		return false;
/*		EData_type dt = type;
		while (dt instanceof CDefined_type) {
			dt = (EData_type)((CDefined_type)dt).getDomain(null);
		}
		if (dt instanceof ESelect_type) {
			throw new SdaiException(SdaiException.SY_ERR);
		} else if (dt instanceof EEnumeration_type) {
			if (tag == PhFileReader.ENUM && dt == v_type) {
				return true;
			}
			return false;
		} else if (dt instanceof ESimple_type) {
			return check_simple_type((ESimple_type)dt);
		} else if (dt instanceof EAggregation_type) {
			return check_aggr((EAggregation_type)dt);
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}*/
	}

/*	private boolean check_defined_type(EData_type dt) throws SdaiException {
		EData_type type = dt;
		while (type instanceof EDefined_type) {
			if (v_type == type) {
				return true;
			}
			type = (EData_type)((CDefined_type)type).getDomain(null);
		}
		return false;
	}*/
	private boolean check_defined_type(EData_type dt) throws SdaiException {
		DataType type = (DataType)v_type;
		while (type.express_type == DataType.DEFINED_TYPE) {
			if (dt == type) {
				return true;
			}
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		return false;
	}

	private boolean check_simple_type(DataType simple) throws SdaiException {
		if (simple.express_type == DataType.NUMBER) {
			if (tag == PhFileReader.REAL || tag == PhFileReader.INTEGER) {
				return true;
			}
		} else if (simple.express_type == DataType.INTEGER) {
			if (tag == PhFileReader.INTEGER) {
				return true;
			}
		} else if (simple.express_type == DataType.REAL) {
			if (tag == PhFileReader.REAL || tag == PhFileReader.INTEGER) {
				return true;
			}
		} else if (simple.express_type == DataType.BOOLEAN) {
			if (tag == PhFileReader.BOOLEAN) {
				return true;
			}
		} else if (simple.express_type == DataType.LOGICAL) {
			if (tag == PhFileReader.LOGICAL || tag == PhFileReader.BOOLEAN) {
				return true;
			}
		} else if (simple.express_type == DataType.BINARY || simple.express_type == DataType.STRING) {
			if (v_type == simple) {
				return true;
			}
		}
		return false;
	}

	private boolean check_aggr(EAggregation_type aggr_type) throws SdaiException {
		if (tag == PhFileReader.EMBEDDED_LIST) {
			if (aggr_type == v_type || aggr_type == ExpressTypes.LIST_GENERIC_TYPE) {
				return true;
			}
		}
		return false;
	}

	void check_entity(CEntity_definition edef, CSchema_definition schdef) throws SdaiException {
//		CSchema_definition schdef = (CSchema_definition)edef.owning_model.described_schema;
		SchemaData sch_data = schdef.modelDictionary.schemaData;
		int index_to_entity = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1,
			(CEntityDefinition)edef);
		if (index_to_entity < 0) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int [] subtypes = schdef.getSubtypes(index_to_entity);
		if (subtypes != null) {
			for (int i = 0; i < subtypes.length; i++) {
				CEntity_definition subtype = (CEntity_definition)sch_data.entities[subtypes[i]];
				if (((EEntity_definition)v_type).isSubtypeOf(subtype)) {
					if_new_then_add(subtype, -1);
				}
			}
		}
	}


/**
 * Implements built-in function "UsedIn" of Express language. 
 * The parameters (of Express type GENERIC and STRING, respectively) are wrapped 
 * in the objects of type <code>Value</code> submitted to the method. 
 * If either of the parameters is indeterminate, then as a result of the function 
 * unset value is taken. 
 * @param instance an entity instance the usage of which is reported.
 * @param role the name of the attribute playing the role in which the specified 
 * instance is used.
 * @return the current object of this class representing the result of the function 
 *  - a bag of instances that use the specified instance in the specified role.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.26 UsedIn - general function"
 */
	public Value usedIn(Value instance, Value role) throws SdaiException { // 15.26
		if (instance == null || role == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		return usedIn(null, instance, role);
	}
	Value usedIn(SdaiContext context, Value instance, Value role) throws SdaiException { // 15.26
		if (instance.tag == INDETERMINATE || role.tag == INDETERMINATE) {
			v_type = d_type = ExpressTypes.BAG_GENERIC_TYPE;
			tag = PhFileReader.EMBEDDED_LIST;
			length = 0;
			return this;
		}
		if (instance.tag != PhFileReader.ENTITY_REFERENCE || role.tag != PhFileReader.STRING) {
			if (instance.tag != PhFileReader.ENTITY_REFERENCE) {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, instance.tag);
			} else {
				printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, role.tag);
			}
			throw new SdaiException(SdaiException.SY_ERR);
		}
		CEntity inst = (CEntity)instance.reference;
		v_type = d_type = ExpressTypes.BAG_GENERIC_TYPE;
		tag = PhFileReader.EMBEDDED_LIST;
		boolean all_refs;
		EAttribute attr = null;
		if (role.string.length() == 0) {
			all_refs = true;
		} else {
			all_refs = false;
			String sch_name = null;
			if (role.reference instanceof String) {
				sch_name = (String)role.reference;
			}
			attr = extract_attribute(role.string, sch_name);
			if (attr == null) {
				length = 0;
				return this;
			}
		}
		inst.makeUsedinExpress(attr, this, all_refs, context);
		return this;
	}


	private EAttribute extract_attribute(String qual_name, String sch_name) throws SdaiException {
		int index2 = qual_name.lastIndexOf('.');
		if (index2 < 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_INQN);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		String attr_name = qual_name.substring(index2 + 1).toLowerCase();
		int index1 = qual_name.indexOf('.');
		if (index1 < 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_INQN);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (index1 >= index2) {
			String base = SdaiSession.line_separator + AdditionalMessages.EE_QNRO + qual_name;
			printWarningToLogo(null, base);
			return null;
		}
		String prefix = qual_name.substring(0, index1);
		if (prefix.equals("*") && sch_name != null) {
			prefix = sch_name;
		}
//		String dict_name = qual_name.substring(0, index1) + SdaiSession.DICTIONARY_NAME_SUFIX;
		String dict_name = prefix + SdaiSession.DICTIONARY_NAME_SUFIX;
		String ent_name = qual_name.substring(index1 + 1, index2);
		SdaiModel dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dict_model.getMode() == SdaiModel.NO_ACCESS) {
			dict_model.startReadOnlyAccess();
		}
		SchemaData sch_data = dict_model.schemaData;
		int index = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, ent_name);
		CEntityDefinition entity_def;
		if (index >= 0) {
			entity_def = sch_data.entities[index];
		} else {
			return null;
		}
		Class c = entity_def.getEntityClass();
		return entity_def.extract_attribute(attr_name);
	}


/**
 * Implements built-in function "Value" of Express language. 
 * The parameter (of type STRING) is wrapped in an object of type <code>Value</code> 
 * submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function unset value is taken. 
 * @param val a string the numeric representation of which is asked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.27 Value - arithmetic function"
 */
	public Value value(Value val) throws SdaiException { // 15.27
		if (val == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		d_type = ExpressTypes.NUMBER_TYPE;
		if (val.tag == PhFileReader.STRING) {
			if (val.string==SdaiSession.asterisk && val.reference instanceof String) {
				val.string = (String)val.reference;
			}
			int ex = -1;
			int exl = val.string.indexOf('e');
			if (exl >= 0) {
				ex = exl;
			}
			int exu = val.string.indexOf('E');
			if (exu >= 0 && (ex < 0 || exu < ex)) {
				ex = exu;
			}
			int pt = val.string.indexOf('.');
			if (ex >= 0) {
				if (pt < 0 || pt > ex) {
					tag = INDETERMINATE;
					return this;
				}
			}
			if (pt == 0) {
				tag = INDETERMINATE;
				return this;
			}
			try {
				Integer int_obj = Integer.valueOf(val.string);
				integer = int_obj.intValue();
				tag = PhFileReader.INTEGER;
				v_type = ExpressTypes.INTEGER_TYPE;
			} catch (NumberFormatException ex1) {
				try {
					Double doub_obj = Double.valueOf(val.string);
					real = doub_obj.doubleValue();
					tag = PhFileReader.REAL;
					v_type = ExpressTypes.REAL_TYPE;
				} catch (NumberFormatException ex2) {
					tag = INDETERMINATE;
				}
			}
		} else if (val.tag == INDETERMINATE) {
			tag = INDETERMINATE;
		} else {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, val.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return this;
	}


/**
 * Implements built-in function "Value_in" of Express language. 
 * The parameters (of Express type AGGREGATE OF GENERIC and GENERIC, respectively) 
 * are wrapped in the objects of type <code>Value</code> submitted to the method. 
 * If either of the parameters is indeterminate, then as a result of the function 
 * UNKNOWN value is returned. 
 * @param context context in which the function is considered.
 * @param aggr an aggregate.
 * @param el a value whose membership to the specified aggregate is checked. 
 * @return the current object of this class representing the result of the function 
 *  - a logical value depending on whether or not the specified value is a member 
 * of the specified aggregate.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.28 Value_in - membership function"
 */
	public Value value_in(SdaiContext context, Value aggr, Value el) throws SdaiException { // 15.28
		if (aggr == null || el == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (aggr.tag != PhFileReader.EMBEDDED_LIST && aggr.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, aggr.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (aggr.tag == INDETERMINATE || el.tag == INDETERMINATE) {
			integer = 2;
			return this;
		}
		boolean unknown = false;
		for (int i = 0; i < aggr.length; i++) {
			if (aggr.nested_values[i].tag == INDETERMINATE) {
				unknown = true;
				continue;
			}
			int res = aggr.nested_values[i].compareValues(el, true, true, true, context);
			if (res == 0) {
				integer = 1;
				return this;
			}
		}
		if (unknown) {
			integer = 2;
		} else {
			integer = 0;
		}
		return this;
	}


/**
 * Implements built-in function "Value_unique" of Express language. 
 * The parameter (of Express type AGGREGATE OF GENERIC) is wrapped in an object of 
 * type <code>Value</code> submitted to the method. If the parameter is 
 * indeterminate, then as a result of the function UNKNOWN value is returned. 
 * @param context context in which the function is considered.
 * @param aggr an aggregate the uniqueness of which elements is checked.
 * @return the current object of this class representing the result of the function.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-11::15.29 Value_unique - uniqueness function"
 */
	public Value value_unique(SdaiContext context, Value aggr) throws SdaiException { // 15.29
		if (aggr == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (aggr.tag != PhFileReader.EMBEDDED_LIST && aggr.tag != INDETERMINATE) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, aggr.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		v_type = d_type = ExpressTypes.LOGICAL_TYPE;
		tag = PhFileReader.LOGICAL;
		if (aggr.tag == INDETERMINATE) {
			integer = 2;
			return this;
		}
		boolean unknown = false;
		for (int i = 0; i < aggr.length; i++) {
			if (aggr.nested_values[i].tag == INDETERMINATE) {
				unknown = true;
				continue;
			}
			for (int j = i + 1; j < aggr.length; j++) {
				if (aggr.nested_values[j].tag == INDETERMINATE) {
					unknown = true;
					continue;
				}
				int res = aggr.nested_values[i].compareValues(aggr.nested_values[j], true, false, true, context);
				if (res == 0) {
					integer = 0;
					return this;
				}
			}
		}
		if (unknown) {
			integer = 2;
		} else {
			integer = 1;
		}
		return this;
	}


/**
 * Implements built-in procedure "Insert" of Express language. 
 * The parameters (of Express type GENERIC and INTEGER, respectively) 
 * are wrapped in the objects of type <code>Value</code> submitted to the method. 
 * The aggregate itself (of Express type LIST OF GENERIC) is represented by the 
 * current object of <code>Value</code> class. 
 * @param context context in which the procedure is considered.
 * @param val an element to be inserted into the list.
 * @param index an integer giving the position in the list at which the specified 
 * element is to be inserted.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #insert(SdaiContext, Value, Value, Value)
 * @see "ISO 10303-11::16.1 Insert"
 */
	public void insert(SdaiContext context, Value val, Value index) throws SdaiException { // 16.1
		if (val == null || index == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (val.tag == COMPLEX_ENTITY_VALUE || val.tag == ENTITY_VALUE) {
			val = val.makeInstance(context);
		}
		if (tag == INDETERMINATE || val.tag == INDETERMINATE || index.tag == INDETERMINATE) {
			return;
		}
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAAG);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (tag != PhFileReader.EMBEDDED_LIST || ((DataType)d_type).express_type != DataType.LIST) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVR, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (index.tag != PhFileReader.INTEGER) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, index.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int ind = index.integer;
		if (ind > length || ind < 0) {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (v_type != null) {
			if (!check_if_fits((EAggregation_type)v_type, val, context)) {
				printWarningToLogoValidateGiveType(AdditionalMessages.EE_ITVR, v_type);
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		} else {
			v_type = find_best_fit_for_value(d_type, val, context);
			if (v_type == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		}
		if (nested_values == null || length >= nested_values.length) {
			enlarge_and_save_nested_values(length + 1);
		}
		ind--;
		for (int i = length - 1; i > ind; i--) {
			nested_values[i + 1] = nested_values[i];
		}
		nested_values[ind + 1] = val;
		length++;
	}


/**
 * Implements built-in procedure "Insert" of Express language. 
 * The parameters (of Express type GENERIC and INTEGER, respectively) 
 * are wrapped in the objects of type <code>Value</code> submitted to the method. 
 * The aggregate itself (of Express type LIST OF GENERIC) is also represented by the 
 * object of <code>Value</code> class identified as the second parameter of the method. 
 * @param context context in which the procedure is considered.
 * @param list a list into which the element is to be inserted.
 * @param new_member an element to be inserted into the list.
 * @param index an integer giving the position in the list at which the specified 
 * element is to be inserted.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #insert(SdaiContext, Value, Value)
 * @see "ISO 10303-11::16.1 Insert"
 */
	public void insert(SdaiContext context, Value list, Value new_member, Value index) throws SdaiException { // 16.1
		if (list == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		list.insert(context, new_member, index);
	}


/**
 * Implements built-in procedure "Remove" of Express language. 
 * The parameter (of Express type INTEGER) is wrapped in an object of 
 * type <code>Value</code> submitted to the method.
 * The aggregate itself (of Express type LIST OF GENERIC) is represented by the 
 * current object of <code>Value</code> class. 
 * @param index an integer giving a position of an element in the list 
 * to be removed.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #remove(Value, Value)
 * @see "ISO 10303-11::16.2 Remove"
 */
	public void remove(Value index) throws SdaiException { // 16.2
		if (index == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (tag == INDETERMINATE || index.tag == INDETERMINATE) {
			return;
		}
		if (d_type == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAAG);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (tag != PhFileReader.EMBEDDED_LIST || ((DataType)d_type).express_type != DataType.LIST) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVR, tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (index.tag != PhFileReader.INTEGER) {
			printWarningToLogoValidateGiveJavaType(AdditionalMessages.EE_ITVA, index.tag);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int ind = index.integer;
		if (ind > length || ind < 1) {
			printWarningToLogoValidate(AdditionalMessages.EE_ITVA);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		ind--;
		for (int i = ind + 1; i < length; i++) {
			nested_values[i - 1] = nested_values[i];
		}
		length--;
	}


/**
 * Implements built-in procedure "Remove" of Express language. 
 * The parameter (of Express type INTEGER) is wrapped in an object of 
 * type <code>Value</code> submitted to the method.
 * The aggregate itself (of Express type LIST OF GENERIC) is also represented by the 
 * object of <code>Value</code> class identified as the first parameter of the method.
 * @param list a list from which the element has to be removed.
 * @param index an integer giving a position of an element in the list 
 * to be removed.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #remove(Value)
 * @see "ISO 10303-11::16.2 Remove"
 */
	public void remove(Value list, Value index) throws SdaiException { // 16.2
		if (list == null) {
			printWarningToLogoValidate(AdditionalMessages.EE_NAVA);
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		list.remove(index);
	}


/**
 * Returns <code>true</code> if and only if the current object of 
 * <code>Value</code> class wraps Express indeterminate value ("?"). 
 * @return <code>true</code> if current <code>Value</code> represents Express "?", 
 * and <code>false</code> otherwise.
 */
	public boolean isIndeterminate() throws SdaiException {
		if (tag == INDETERMINATE) {
			return true;
		}
		return false;
	}


/**
 * Checks if the specified entity instance is value-equal to another entity instance in the 
 * specified <code>SdaiModel</code>. If such an entity instance is found, then the 
 * submitted instance is deleted and the found instance is returned. 
 * Otherwise, the replacement operation is not performed, and the method returns 
 * the submitted entity instance.
 * The input entity instance is wrapped in the current object of <code>Value</code>. 
 * @param context context in which the replacement operation is considered.
 * @param entity_extent Java class for the entity of which instances are searched. 
 * @param entity_model <code>SdaiModel</code> whose content is searched.
 * @return entity instance that is either the replacement of the specified instance or specified instance itself.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 4.1.2
 */
	public Value replaceByAlreadyExistingAndDelete(SdaiContext _context, Class entity_extent, 
			SdaiModel entity_model) throws SdaiException {
		if (tag != PhFileReader.ENTITY_REFERENCE) {
			return this;
		}
		CEntity rep_inst = (CEntity)reference;
		SchemaData sch_data = entity_model.underlying_schema.modelDictionary.schemaData;
		CEntity_definition extent_def = sch_data.findEntity(entity_extent);
		if (extent_def == null) {
			return this;
		}
		if (!rep_inst.getInstanceType().isSubtypeOf(extent_def)) {
			return this;
		}

		Value instv = alloc();
		instv.tag = PhFileReader.ENTITY_REFERENCE;
		AEntity ae = entity_model.getInstances(entity_extent);
		SdaiIterator aei = ae.createIterator();
		boolean found = false;
		while (aei.next()) {
			EEntity inst = ae.getCurrentMemberEntity(aei);
			// skip the same instance, we are interested in value equal instances only
			if (inst == rep_inst) continue; 
			instv.v_type = inst.getInstanceType();
			instv.d_type = instv.v_type;
			instv.reference = inst;
			if (Value.equalInt(_context, this, instv) == 2) {
				found = true;
				// if this method is applied consistently, 
				// there should be no more than one older value equal instance
				break; 
			}
		}
		if (found) {
			rep_inst.deleteApplicationInstance();
			return instv;
		} else {
			return this;
		}
	}


/**
 * Performs a conversion of the nested aggregate to a one-dimensional aggregate of entity 
 * instances. Entity instances of all types found in the nested aggregate are stored 
 * in the result aggregate. Elements which are not entity instances are bypassed. 
 * The Express type of the resulting aggregate is AGGREGATE OF GENERIC. 
 * @return the aggregate of entity instances.
 * @throws SdaiException SY_ERR, underlying system error.
 * @since 4.1.2
 */
    public Value unnest() throws SdaiException {
		Value res_aggr = Value.alloc(ExpressTypes.AGGREGATE_GENERIC_TYPE).create();
		res_aggr.tag = PhFileReader.EMBEDDED_LIST;
		if (tag == PhFileReader.EMBEDDED_LIST) {
			for (int i = 0; i < length; i++) {
				if (nested_values[i].tag == INDETERMINATE) {
					continue;
				}
				Value val = nested_values[i].unnest();
				for (int j = 0; j < val.length; j++) {
					if (res_aggr.nested_values == null || res_aggr.length >= res_aggr.nested_values.length) {
						res_aggr.enlarge_and_save_nested_values(res_aggr.length + 1);
					}
					res_aggr.nested_values[res_aggr.length] = val.nested_values[j];
					res_aggr.length++;
				}
			}
		} else {
			if (tag == PhFileReader.ENTITY_REFERENCE) {
				res_aggr.nested_values = new Value[1];
				res_aggr.nested_values[0] = this;
				res_aggr.length = 1;
			}
		}
		return res_aggr;
    }


/**
 * Implements built-in function "Extent" of Express-X language. 
 * The parameter (of Express type STRING) is wrapped in an object of 
 * type <code>Value</code> submitted to the method.
 * The resulting aggregate (of Express type SET OF GENERIC) is represented by the 
 * current object of <code>Value</code> class. 
 * @param context context in which the function is considered.
 * @param data_type the string representing the name of an entity data type. 
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-14::11.1 Extent - general function"
 * @since 4.1.2
 */
	public Value extent(SdaiContext context, Value data_type) throws SdaiException {
		if (context == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.EC_CONR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int j;
		if (d_type != ExpressTypes.SET_GENERIC_TYPE) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (data_type.tag == INDETERMINATE) {
			v_type = d_type;
			tag = PhFileReader.EMBEDDED_LIST;
			length = 0;
			return this;
		}
		if (data_type.tag != PhFileReader.STRING) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		v_type = d_type;
		tag = PhFileReader.EMBEDDED_LIST;
		String sch_name = null;
		if (data_type.reference instanceof String) {
			sch_name = (String)data_type.reference;
		}
		SchemaData sch_data = extract_entity(data_type.string, sch_name);
		if (sch_data == null) {
			length = 0;
			return this;
		}

		SdaiModel mod = context.src_model;
		if (mod.underlying_schema != sch_data.model.described_schema) {
			mod = context.tar_model;
			if (mod.underlying_schema != sch_data.model.described_schema) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		int index = this.integer;
		int counter = 0;
		int true_index;
		if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
			true_index = mod.find_entityRO(mod.dictionary.schemaData.entities[index]);
		} else {
			true_index = index;
		}
		CEntity inst;
		CEntity_definition type = null;
		if (true_index >= 0) {
			if (mod.lengths[true_index] > 0 && nested_values == null) {
				nested_values = new Value[SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE];
			}
			for (j = 0; j < mod.lengths[true_index]; j++) {
				inst = mod.instances_sim[true_index][j];
				if (counter >= nested_values.length) {
					enlarge_nested_values_mild();
				}
				if (nested_values[counter] == null) {
					nested_values[counter] = new Value();
				}
				if (type == null) {
					type = (CEntity_definition)inst.getInstanceType();
				}
				nested_values[counter].d_type = type;
				nested_values[counter].v_type = type;
				nested_values[counter].tag = PhFileReader.ENTITY_REFERENCE;
				nested_values[counter].reference = inst;
				counter++;
			}
		}
		int [] subtypes = mod.underlying_schema.getSubtypes(index);
		if (subtypes == null) {
			length = counter;
			return this;
		}
		for (int i = 0; i < subtypes.length; i++) {
			CEntity_definition sub_type = null;
			if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
				true_index = mod.find_entityRO(mod.dictionary.schemaData.entities[subtypes[i]]);
			} else {
				true_index = subtypes[i];
			}
			if (true_index >= 0) {
				if (mod.lengths[true_index] > 0 && nested_values == null) {
					nested_values = new Value[SdaiModel.INSTANCE_ARRAY_INITIAL_SIZE];
				}
				for (j = 0; j < mod.lengths[true_index]; j++) {
					inst = mod.instances_sim[true_index][j];
					if (counter >= nested_values.length) {
						enlarge_nested_values_mild();
					}
					if (nested_values[counter] == null) {
						nested_values[counter] = new Value();
					}
					if (sub_type == null) {
						sub_type = (CEntity_definition)inst.getInstanceType();
					}
					if (type != null) {
						nested_values[counter].d_type = type;
					} else {
						nested_values[counter].d_type = sub_type;
					}
					nested_values[counter].v_type = sub_type;
					nested_values[counter].tag = PhFileReader.ENTITY_REFERENCE;
					nested_values[counter].reference = inst;
					counter++;
				}
			}
		}
		length = counter;
		return this;
	}


	private SchemaData extract_entity(String qual_name, String sch_name) throws SdaiException {
		int index = qual_name.lastIndexOf('.');
		if (index < 0) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		String ent_name = qual_name.substring(index + 1);
		String prefix = qual_name.substring(0, index);
		if (prefix.equals("*") && sch_name != null) {
			prefix = sch_name;
		}
		String dict_name = prefix.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
		SdaiModel dict_model = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
		if (dict_model.getMode() == SdaiModel.NO_ACCESS) {
			dict_model.startReadOnlyAccess();
		}
		SchemaData sch_data = dict_model.schemaData;
		int index2type = sch_data.find_entity(0, sch_data.noOfEntityDataTypes - 1, ent_name);
		if (index2type >= 0) {
			integer = index2type;
			return sch_data;
		} else {
			return null;
		}
	}


	Value examine_value(CEntity ref_owner, CEntity old, CEntity_definition new_def, 
			DataType dtype, CExplicit_attribute attr) throws SdaiException {
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			if (reference == old && !((CEntity)dtype).analyse_entity(ref_owner, new_def)) {
				Value res = new Value();
				res.tag = EXCPT;
				res.reference = attr;
				res.agg_owner = ref_owner;
				return res;
			}
		} else if (tag == PhFileReader.EMBEDDED_LIST) {
			return examine_aggr_value(ref_owner, old, new_def, dtype, attr);
		} else if (tag == PhFileReader.TYPED_PARAMETER) {
			String name = string.toLowerCase();
			if (dtype.express_type != DataType.DEFINED_TYPE) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			CDefined_type tp = find_type_by_name(name, (CDefined_type)dtype, 
				ref_owner.owning_model.repository.session.sdai_context);
			if (tp == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			return nested_values[0].examine_value(ref_owner, old, new_def, tp, attr);
		}
		return null;
	}


	private Value examine_aggr_value(CEntity ref_owner, CEntity old, CEntity_definition new_def, 
			DataType dtype, CExplicit_attribute attr) throws SdaiException {
		DataType type = dtype;
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type < DataType.LIST || type.express_type > DataType.AGGREGATE) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		type = (DataType)((EAggregation_type)type).getElement_type(null);
		for (int i = 0; i < length; i++) {
			Value res = nested_values[i].examine_value(ref_owner, old, new_def, type, attr);
			if (res != null) {
				return res;
			}
		}
		return null;
	}


	private CDefined_type find_type_by_name(String name, CDefined_type def, SdaiContext context) throws SdaiException {
		if (name.equals(def.getName(null))) {
			return def;
		}
		DataType type = (DataType)def.getDomain(null);
		while (type.express_type == DataType.DEFINED_TYPE) {
			if (name.equals(type.getName(null))) {
				return (CDefined_type)type;
			}
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type >= DataType.SELECT && type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			ANamed_type sels = ((ESelect_type)type).getSelections(null, context);
			if (((AEntity)sels).myLength < 0) {
				((AEntity)sels).resolveAllConnectors();
			}
			int ln = ((AEntity)sels).myLength;
			DataType alternative;
			if (ln == 1) {
				alternative = (DataType)((AEntity)sels).myData;
				if (alternative.express_type == DataType.DEFINED_TYPE) {
					return find_type_by_name(name, (CDefined_type)alternative, context);
				}
			} else {
				Object [] myDataA = (Object [])((AEntity)sels).myData;
				for (int i = 0; i < ln; i++) {
					alternative = (DataType)myDataA[i];
					if (alternative.express_type == DataType.DEFINED_TYPE) {
						CDefined_type res = find_type_by_name(name, (CDefined_type)alternative, context);
						if (res != null) {
							return res;
						}
					}
				}
			}
		}
		return null;
	}


/**
 * Returns a description of this value as a <code>String</code>.
 * @return a description of the current object of <code>Value</code> class. 
 */
	public String toString() {
//		synchronized (SdaiCommon.syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
//e.printStackTrace();
			SdaiSession.printStackTraceToLogWriter(e);
			return super.toString();
		}
//		} // syncObject
	}


/**
	This method is doing a job of the public method <code>toString</code>.
*/
	private String getAsString() throws SdaiException {
		int i;
		int str_index = 0;
		String str;
		int str_length;

		StaticFields staticFields = StaticFields.get();
		if (staticFields.value_as_string == null) {
			staticFields.value_as_string = new byte[NUMBER_OF_CHARACTERS_IN_VALUE];
		}
		if (d_type != null) {
			str = d_type.getName(null).toUpperCase();
			str_length = str.length();
			if (str_length + 4 >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, 0, str_length + 4);
			}
			for (i = 0; i < str_length; i++) {
				staticFields.value_as_string[str_index++] = (byte)str.charAt(i);
			}
			staticFields.value_as_string[str_index++] = PhFileReader.SPACE;
			staticFields.value_as_string[str_index++] = PhFileReader.COLON;
			staticFields.value_as_string[str_index++] = PhFileReader.EQUAL;
			staticFields.value_as_string[str_index++] = PhFileReader.SPACE;
		}
		if (tag == PhFileReader.ENTITY_REFERENCE) {
			CEntity inst = (CEntity)reference;
			str = inst.getAsString(staticFields);
			str_length = str.length() - 1;
			if (str_index + str_length >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, str_index, str_index + str_length);
			}
			for (i = 0; i < str_length; i++) {
				staticFields.value_as_string[str_index++] = (byte)str.charAt(i);
			}
		} else if (tag == PhFileReader.TYPED_PARAMETER) {
			str_index = get_value_to_string(staticFields, true, str_index);
		} else if (tag == ENTITY_VALUE) {
			str_length = ((EntityValue)reference).getAsByteArray(staticFields);
			if (str_index + str_length >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, str_index, str_index + str_length);
			}
			for (i = 0; i < str_length; i++) {
				staticFields.value_as_string[str_index++] = staticFields.ent_value_as_string[i];
			}
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			if (staticFields.value_as_string_complex == null && str_index < NUMBER_OF_CHARACTERS_IN_VALUE) {
				staticFields.value_as_string_complex = new byte[NUMBER_OF_CHARACTERS_IN_VALUE];
			} else {
				staticFields.value_as_string_complex = new byte[str_index + 1];
			}
			EntityValue [] ev_arr = (EntityValue [])reference;
			staticFields.value_as_string_complex[str_index++] = PhFileReader.LEFT_PARENTHESIS;
			for (i = 0; i < length; i++) {
				str_length = ev_arr[i].getAsByteArray(staticFields);
				if (str_index + str_length + 1 >= staticFields.value_as_string_complex.length) {
					staticFields.value_as_string_complex = enlarge_value_string(staticFields.value_as_string_complex, str_index, str_index + str_length + 1);
				}
				for (int j = 0; j < str_length; j++) {
					staticFields.value_as_string_complex[str_index++] = staticFields.ent_value_as_string[j];
				}
			}
			staticFields.value_as_string_complex[str_index++] = PhFileReader.RIGHT_PARENTHESIS;
			return new String(staticFields.value_as_string_complex, 0, str_index);
		} else {
			boolean parenth = false;
			if (v_type != null) {
				str = v_type.getName(null).toUpperCase();
				str_length = str.length();
				if (str_length + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, 0, str_length + 1);
				}
				for (i = 0; i < str_length; i++) {
					staticFields.value_as_string[str_index++] = (byte)str.charAt(i);
				}
				parenth = true;
				staticFields.value_as_string[str_index++] = PhFileReader.LEFT_PARENTHESIS;
			}
			str_index = get_value_to_string(staticFields, true, str_index);
			if (parenth) {
				if (str_index + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, str_index, str_index + 1);
				}
				staticFields.value_as_string[str_index++] = PhFileReader.RIGHT_PARENTHESIS;
			}
		}
		return new String(staticFields.value_as_string, 0, str_index);
	}


	int getAsByteArray(StaticFields staticFields) throws SdaiException {
		int str_index = 0;

		if (staticFields.value_as_string == null) {
			staticFields.value_as_string = new byte[NUMBER_OF_CHARACTERS_IN_VALUE];
		}
		if (tag == ENTITY_VALUE) {
			int str_length = ((EntityValue)reference).getAsByteArray(staticFields);
			if (str_length >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, 0, str_length);
			}
			for (int i = 0; i < str_length; i++) {
				staticFields.value_as_string[str_index++] = staticFields.ent_value_as_string[i];
			}
			return str_index;
		} else if (tag == COMPLEX_ENTITY_VALUE) {
			return 0;
		} else {
			return get_value_to_string(staticFields, true, str_index);
		}
	}


	private int get_value_to_string(StaticFields staticFields, boolean first, int index) throws SdaiException {
		int i;
		boolean first_next;
		switch (tag) {
			case PhFileReader.MISSING:
				if (index + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 2);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				staticFields.value_as_string[index++] = PhFileReader.DOLLAR_SIGN;
				break;
			case PhFileReader.REDEFINE:
				if (index + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 2);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				staticFields.value_as_string[index++] = PhFileReader.ASTERISK;
				break;
			case PhFileReader.INTEGER:
				if (!first) {
					if (index + 1 >= staticFields.value_as_string.length) {
						staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
					}
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				if (integer == Integer.MIN_VALUE) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				index = whole_to_byte_array(staticFields, integer, index);
				break;
			case PhFileReader.REAL:
				String str = Double.toString(real);
				if (index + str.length() + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + str.length() + 1);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				for (i = 0; i < str.length(); i++) {
					staticFields.value_as_string[index++] = (byte)str.charAt(i);
				}
				break;
			case PhFileReader.BOOLEAN:
				if (index + 4 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 4);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				if (integer == 0) {
					for (i = 0; i < 3; i++) {
						staticFields.value_as_string[index++] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (integer == 1) {
					for (i = 0; i < 3; i++) {
						staticFields.value_as_string[index++] = PhFileWriter.LOG_TRUE[i];
					}
				}
				break;
			case PhFileReader.LOGICAL:
				if (index + 4 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 4);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				if (integer == 0) {
					for (i = 0; i < 3; i++) {
						staticFields.value_as_string[index++] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (integer == 1) {
					for (i = 0; i < 3; i++) {
						staticFields.value_as_string[index++] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					for (i = 0; i < 3; i++) {
						staticFields.value_as_string[index++] = PhFileWriter.LOG_UNKNOWN[i];
					}
				}
				break;
			case PhFileReader.ENUM:
				if (!first) {
					if (index + 1 >= staticFields.value_as_string.length) {
						staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
					}
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				SdaiSession.printer.fromStringBasicLatin(string);
				if (index + staticFields.string_length + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + staticFields.string_length + 2);
				}
				staticFields.value_as_string[index++] = PhFileReader.DOT;
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.value_as_string[index++] = staticFields.string[i];
				}
				staticFields.value_as_string[index++] = PhFileReader.DOT;
				break;
			case PhFileReader.STRING:
				if (!first) {
					if (index + 1 >= staticFields.value_as_string.length) {
						staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
					}
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				if (SdaiSession.isToStringUnicode()) {
					SdaiSession.printer.fromStringBasicLatin(string);
				} else {
					SdaiSession.printer.fromString(string);
				}
				int ln = 2 * staticFields.string_length;
				if (index + ln + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + ln + 2);
				}
				staticFields.value_as_string[index++] = PhFileReader.APOSTROPHE;
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.value_as_string[index++] = staticFields.string[i];
					if (staticFields.string[i] == PhFileReader.APOSTROPHE) {
						staticFields.value_as_string[index++] = PhFileReader.APOSTROPHE;
					}
				}
				staticFields.value_as_string[index++] = PhFileReader.APOSTROPHE;
				break;
			case PhFileReader.BINARY:
				if (!first) {
					if (index + 1 >= staticFields.value_as_string.length) {
						staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
					}
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				SdaiSession.printer.convertBinary((Binary)reference);
				if (index + staticFields.string_length + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + staticFields.string_length + 2);
				}
				staticFields.value_as_string[index++] = PhFileReader.QUOTATION_MARK;
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.value_as_string[index++] = staticFields.string[i];
				}
				staticFields.value_as_string[index++] = PhFileReader.QUOTATION_MARK;
				break;
			case PhFileReader.TYPED_PARAMETER:
				if (!first) {
					if (index + 1 >= staticFields.value_as_string.length) {
						staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
					}
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				SdaiSession.printer.fromStringBasicLatin(string);
				if (index + staticFields.string_length + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + staticFields.string_length + 1);
				}
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.value_as_string[index++] = staticFields.string[i];
				}
				staticFields.value_as_string[index++] = PhFileReader.LEFT_PARENTHESIS;
				first_next = true;
				index = nested_values[0].get_value_to_string(staticFields, first_next, index);
				if (index + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
				}
				staticFields.value_as_string[index++] = PhFileReader.RIGHT_PARENTHESIS;
				break;
			case PhFileReader.ENTITY_REFERENCE:
				if (index + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 2);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				staticFields.value_as_string[index++] = PhFileReader.SPECIAL;
				index = whole_to_byte_array(staticFields, ((CEntity)reference).instance_identifier, index);
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (index + 2 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 2);
				}
				if (!first) {
					staticFields.value_as_string[index++] = PhFileReader.COMMA_b;
				}
				staticFields.value_as_string[index++] = PhFileReader.LEFT_PARENTHESIS;
				first_next = true;
				for (i = 0; i < length; i++) {
					index = nested_values[i].get_value_to_string(staticFields, first_next, index);
					first_next = false;
				}
				if (index + 1 >= staticFields.value_as_string.length) {
					staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
				}
				staticFields.value_as_string[index++] = PhFileReader.RIGHT_PARENTHESIS;
				break;
		}
		return index;
	}


	private int whole_to_byte_array(StaticFields staticFields, long lo, int index) throws SdaiException {
		boolean neg;
		long number, next_number;
		if (lo < 0) {
			neg = true;
			number = -lo;
		} else if (lo > 0) {
			neg = false;
			number = lo;
		} else {
			if (index + 1 >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
			}
			staticFields.value_as_string[index++] = PhFileWriter.DIGITS[0];
			return index;
		}
		int initial_index = index;
		while (number != 0) {
			next_number = number / 10;
			if (index + 1 >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
			}
			staticFields.value_as_string[index++] = PhFileWriter.DIGITS[(int)(number - next_number * 10)];
			number = next_number;
		}
		if (neg) {
			if (index + 1 >= staticFields.value_as_string.length) {
				staticFields.value_as_string = enlarge_value_string(staticFields.value_as_string, index, index + 1);
			}
			staticFields.value_as_string[index++] = PhFileReader.MINUS;
		}
		for (int i = initial_index;
				i <= initial_index + (index - initial_index) / 2 - 1; i++) {
			byte sym = staticFields.value_as_string[i];
			staticFields.value_as_string[i] = staticFields.value_as_string[index - i + initial_index - 1];
			staticFields.value_as_string[index - i + initial_index - 1] = sym;
		}
		return index;
	}




	void addNewMember(CEntity inst, CEntity_definition def) throws SdaiException {
		Value val = alloc(def);
		val.v_type = def;
		val.tag = PhFileReader.ENTITY_REFERENCE;
		val.reference = inst;

		if (nested_values == null || length >= nested_values.length) {
			enlarge_and_save_nested_values(length + 1);
		}
		nested_values[length] = val;
		length++;
	}


	void addNewString(String str) throws SdaiException {
		Value val = alloc(ExpressTypes.STRING_TYPE);
		val.v_type = ExpressTypes.STRING_TYPE;
		val.tag = PhFileReader.STRING;
		val.string = str;

		if (nested_values == null || length >= nested_values.length) {
			enlarge_and_save_nested_values(length + 1);
		}
		nested_values[length] = val;
		length++;
	}






	void enlarge() {
		int new_length = nested_values.length * 2;
		Value new_nested_values[] = new Value[new_length];
		System.arraycopy(nested_values, 0, new_nested_values, 0, nested_values.length);
		nested_values = new_nested_values;
	}

	void enlarge(int count) {
		int new_length = nested_values.length * 2;
		if (new_length < count) {
			new_length = count;
		}
		Value new_nested_values[] = new Value[new_length];
		System.arraycopy(nested_values, 0, new_nested_values, 0, nested_values.length);
		nested_values = new_nested_values;
	}

	void enlarge_nested_values(int count) {
		if (nested_values == null) {
			if (count <= SdaiSession.NUMBER_OF_VALUES) {
				nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			} else {
				nested_values = new Value[count];
			}
			return;
		}
		int new_length = nested_values.length * 2;
		if (new_length < count) {
			new_length = count;
		}
		nested_values = new Value[new_length];
	}

	void enlarge_nested_values_mild() {
		int new_length;
		if (nested_values.length < SdaiModel.INSTANCE_ARRAY_SIZE_STEP) {
			new_length = nested_values.length * 2;
		} else {
			new_length = nested_values.length + SdaiModel.INSTANCE_ARRAY_SIZE_STEP;
		}
		Value new_nested_values[] = new Value[new_length];
		System.arraycopy(nested_values, 0, new_nested_values, 0, nested_values.length);
		nested_values = new_nested_values;
	}

	private void enlarge_and_save_nested_values(int count) {
		if (nested_values == null) {
			if (count <= SdaiSession.NUMBER_OF_VALUES) {
				nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
			} else {
				nested_values = new Value[count];
			}
			return;
		}
		int new_length = nested_values.length * 2;
		if (new_length < count) {
			new_length = count;
		}
		Value new_nested_values[] = new Value[new_length];
		System.arraycopy(nested_values, 0, new_nested_values, 0, nested_values.length);
		nested_values = new_nested_values;
	}

	private void enlarge_types() {
		int new_length = types.length * 2;
		Object new_types[] = new Object[new_length];
		System.arraycopy(types, 0, new_types, 0, types.length);
		types = new_types;
	}

	private static void enlargeRoute(StaticFields staticFields) {
		int new_length = staticFields.route.length * 2;
		CDefined_type [] new_route = new CDefined_type[new_length];
		System.arraycopy(staticFields.route, 0, new_route, 0, staticFields.route.length);
		staticFields.route = new_route;
	}

	private static void init_preceding_spaces(StaticFields staticFields) {
		staticFields.preceding_spaces = new char[ZEROS_COUNT];
		for (int i = 0; i < ZEROS_COUNT; i++) {
			staticFields.preceding_spaces[i] = SPACE;
		}
	}

	private static void enlarge_preceding_spaces(StaticFields staticFields, int demand) {
		int new_length = staticFields.preceding_spaces.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.preceding_spaces = new char[new_length];
		for (int i = 0; i < new_length; i++) {
			staticFields.preceding_spaces[i] = SPACE;
		}
	}

	private static void init_preceding_zeros(StaticFields staticFields) {
		staticFields.preceding_zeros = new char[ZEROS_COUNT];
		for (int i = 0; i < ZEROS_COUNT; i++) {
			staticFields.preceding_zeros[i] = ZERO;
		}
	}

	private static void enlarge_preceding_zeros(StaticFields staticFields, int demand) {
		int new_length = staticFields.preceding_zeros.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		staticFields.preceding_zeros = new char[new_length];
		for (int i = 0; i < new_length; i++) {
			staticFields.preceding_zeros[i] = ZERO;
		}
	}

	EntityValue [] enlarge_entity_value_arr(EntityValue [] ev_arr) {
		int new_length = ev_arr.length * 2;
		EntityValue new_ev_arr[] = new EntityValue[new_length];
		System.arraycopy(ev_arr, 0, new_ev_arr, 0, ev_arr.length);
		reference = new_ev_arr;
		return new_ev_arr;
	}

	private byte [] enlarge_value_string(byte [] byte_arr, int str_length, int demand) {
		int new_length = byte_arr.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_byte_arr[] = new byte[new_length];
		System.arraycopy(byte_arr, 0, new_byte_arr, 0, str_length);
		return new_byte_arr;
	}


	private boolean elementsCompatibleToAggr(EAggregation_type aggr_tp, SdaiContext context, boolean overrid) throws SdaiException {
		if (aggr_tp == null) {
			return false;
		}
		DataType tp = (DataType)aggr_tp;
		if (tp == ExpressTypes.LIST_GENERIC_TYPE || tp == ExpressTypes.SET_GENERIC_TYPE || 
				tp == ExpressTypes.BAG_GENERIC_TYPE || tp == ExpressTypes.AGGREGATE_GENERIC_TYPE) {
			return true;
		}
		EData_type el_tp1 = aggr_tp.getElement_type(null);
		if (tag != PhFileReader.EMBEDDED_LIST) {
			if (tp == ExpressTypes.SET_STRING_TYPE) {
				if (((DataType)v_type).express_type != DataType.STRING) {
					return false;
				}
			} else {
				if (!oneCompatibleToAnother(el_tp1, v_type, context, false)) {
					return false;
				}
			}
			return true;
		}

		for (int i = 0; i < length; i++) {
			Value val = nested_values[i];
			if (tp == ExpressTypes.SET_STRING_TYPE) {
				if (((DataType)val.v_type).express_type != DataType.STRING) {
					return false;
				}
			} else {
				if (!oneCompatibleToAnother(el_tp1, val.v_type, context, overrid)) {
					return false;
				}
			}
		}
		return true;
	}


	boolean explore_value_for_undo(SdaiSession se, RandomAccessFile ur_f, SdaiModel owners_mod, SchemaData sch_data) 
			throws java.io.IOException, SdaiException {
		Value value_next;
		int index;
		switch (tag) {
			case PhFileReader.MISSING:
				ur_f.writeByte('$');
				return true;
			case PhFileReader.REDEFINE:
				ur_f.writeByte('*');
				return true;
			case PhFileReader.INTEGER:
				ur_f.writeByte('i');
				ur_f.writeInt(integer);
				return true;
			case PhFileReader.REAL:
				ur_f.writeByte('r');
				ur_f.writeDouble(real);
				return true;
			case PhFileReader.BOOLEAN:
				if (integer == 0) {
					ur_f.writeByte('f');
				} else if (integer == 1) {
					ur_f.writeByte('t');
				}
				return true;
			case PhFileReader.LOGICAL:
				if (integer == 0) {
					ur_f.writeByte('f');
				} else if (integer == 1) {
					ur_f.writeByte('t');
				} else {
					ur_f.writeByte('u');
				}
				return true;
			case PhFileReader.ENUM:
				ur_f.writeByte('e');
				ur_f.writeUTF(string);
				return true;
			case PhFileReader.STRING:
				ur_f.writeByte('s');
				ur_f.writeUTF(string);
				return true;
			case PhFileReader.BINARY:
				ur_f.writeByte('b');
//System.out.println("Value  BEFORE ???   ");System.out.flush();
//				ur_f.writeUTF(((Binary)reference).toString());
				Binary bnr = (Binary)reference;
				long bin_count = (long)bnr.value.length;
				ur_f.writeLong(bin_count + 1);
				ur_f.writeByte((byte)bnr.unused);
				for (int i = 0; i < bin_count; i++) {
					ur_f.writeByte(bnr.value[i]);
				}
//System.out.println("Value  AFTER ???   ");System.out.flush();
				return true;
			case PhFileReader.TYPED_PARAMETER:
				ur_f.writeByte('p');
				if (sch_data.defTypesCount < 0) {
					sch_data.initializeDefinedTypes();
				}
				index = sch_data.find_type(0, sch_data.defTypesCount - 1, string);
				if (index < 0) {
					throw new SdaiException(SdaiException.VA_NVLD);
				}
				ur_f.writeShort((short)index);
				return nested_values[0].explore_value_for_undo(se, ur_f, owners_mod, sch_data);
			case PhFileReader.ENTITY_REFERENCE:
				SchemaData schd;
				String entity_name;
				if (reference instanceof SdaiModel.Connector) {
					SdaiModel.Connector con = (SdaiModel.Connector)reference;
//					SdaiModel mod_in = con.resolveModelIn();
					SdaiModel mod_in = (SdaiModel)nested_values[0].reference;
					if(mod_in != null) {
						//FIXME ConnectorLocalImpl can not be used here!!
						//      What if ConnectorRemoteImpl comes here!!
						if (con.getEntityNameUpperCase() != null) {
							SdaiModel dict = mod_in.dictionary;
							entity_name = ((SdaiModelLocalImpl.ConnectorLocalImpl)con).entity_name.toUpperCase();
							if (dict != null && (dict.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
								schd = dict.schemaData;
								ur_f.writeByte('3');
								ur_f.writeLong(con.instance_identifier);
								ur_f.writeShort(se.find_model_undo(mod_in));
								index = schd.find_entity(0, schd.sNames.length - 1, entity_name);
								if (index < 0) {
									throw new SdaiException(SdaiException.SY_ERR);
								}
								ur_f.writeShort(index);
							} else {
								ur_f.writeByte('4');
								ur_f.writeLong(con.instance_identifier);
								ur_f.writeShort(se.find_model_undo(mod_in));
								ur_f.writeShort(se.find_entity_undo(entity_name));
							}
						} else {
							ur_f.writeByte('3');
							ur_f.writeLong(con.instance_identifier);
							ur_f.writeShort(se.find_model_undo(mod_in));
							ur_f.writeShort(-1);
						}
					} else {
						ur_f.writeByte('$');
					}
				} else {
					CEntity inst = (CEntity)reference;
					CEntity_definition def = (CEntity_definition)inst.getInstanceType();
					entity_name = ((CEntityDefinition)def).getNameUpperCase();
					if (owners_mod == inst.owning_model) {
						ur_f.writeByte('1');
						ur_f.writeLong(inst.instance_identifier);
						index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
						if (index < 0) {
							throw new SdaiException(SdaiException.SY_ERR/*, base*/);
						}
						ur_f.writeShort(index);
						index = inst.find_instance(index);
						if (index < 0) {
//System.out.println("Value    its model = " + owners_mod.name + 
//"   inst.instance_identifier: " + inst.instance_identifier + "   index: " + index);
							throw new SdaiException(SdaiException.SY_ERR/*, base*/);
						}
						ur_f.writeInt(index);
					} else {
						ur_f.writeByte('2');
						ur_f.writeLong(inst.instance_identifier);
						ur_f.writeShort(se.find_model_undo(inst.owning_model));
						schd = inst.owning_model.underlying_schema.modelDictionary.schemaData;
						index = schd.find_entity(0, schd.sNames.length - 1, entity_name);
						if (index < 0) {
							throw new SdaiException(SdaiException.SY_ERR/*, base*/);
						}
						ur_f.writeShort(index);
						if ((inst.owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY) {
							index = inst.owning_model.find_entityRO(schd.entities[index]);
							if (index < 0) {
								throw new SdaiException(SdaiException.SY_ERR);
							}
						}
						index = inst.find_instance(index);
//						index = inst.owning_model.find_instance_id(inst.instance_identifier);
						if (index < 0) {
							throw new SdaiException(SdaiException.SY_ERR/*, base*/);
						}
						ur_f.writeInt(index);
					}
				}
				return true;
			case PhFileReader.EMBEDDED_LIST:
				ur_f.writeByte('(');
				for (int i = 0; i < length; i++) {
					boolean result = nested_values[i].explore_value_for_undo(se, ur_f, owners_mod, sch_data);
					if (!result) {
						return false;
					}
				}
				ur_f.writeByte(')');
				return true;
		}
		return false;
	}


	final void check_aggregate(CExplicit_attribute eattr, long instance_identifier, 
			String ent_name_except, SdaiSession session) throws SdaiException, java.io.IOException {

		DataType type = (DataType)eattr.getDomain(null);
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type < DataType.LIST || type.express_type > DataType.AGGREGATE) {
			return;
		}
		EAggregation_type aggr_type = (EAggregation_type)type;
		check_aggr_elements(aggr_type, instance_identifier, ent_name_except, session);
	}


	private final boolean check_aggr_elements(EAggregation_type aggr_type, long instance_identifier, 
			String ent_name_except, SdaiSession session)
			throws SdaiException, java.io.IOException {
		int i;
		boolean repaired = false;

		DataType el_type = (DataType)aggr_type.getElement_type(null);
		while (el_type.express_type == DataType.DEFINED_TYPE) 	{
			el_type = (DataType)((CDefined_type)el_type).getDomain(null);
		}
		if (el_type.express_type >= DataType.SELECT && el_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			return repaired;
		}
		if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
			for (i = 0; i < length; i++) {
				boolean res = nested_values[i].check_aggr_elements((EAggregation_type)el_type, instance_identifier, 
					ent_name_except, session);
				if (res) {
					repaired = true;
				}
			}
			return repaired;
		}
		boolean viol_found = false;
		for (i = 0; i < length; i++) {
			if (nested_values[i].compare_against_type(el_type)) {
				continue;
			}
			viol_found = true;
			nested_values[i].sel_number = WRONG_VALUE_TAG;
		}
		if (!viol_found) {
			return repaired;
		}
		String element_type = el_type.get_data_type_name();
		int k = 0;
		for (i = 0; i < length; i++) {
			if (nested_values[i].sel_number != WRONG_VALUE_TAG) {
				nested_values[k] = nested_values[i];
				k++;
				continue;
			}
			nested_values[i].sel_number = 0;
         String value_type = nested_values[i].get_value_type_name();
         int index = i + 1;
			String text = AdditionalMessages.RD_IMAG + 
					SdaiSession.line_separator + AdditionalMessages.RD_INST + instance_identifier +
					SdaiSession.line_separator + AdditionalMessages.RD_ENT + ent_name_except.toUpperCase() + 
					SdaiSession.line_separator + AdditionalMessages.RD_MEIN + index + 
					SdaiSession.line_separator + AdditionalMessages.RD_MEAT + value_type + 
					SdaiSession.line_separator + AdditionalMessages.RD_MEDT + element_type; 
			if (session != null && session.logWriterSession != null) {
				session.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		}
		length = k;
		return true;
	}


	private final boolean compare_against_type(DataType el_type) throws SdaiException, java.io.IOException {
		switch (tag) {
			case PhFileReader.MISSING:
			case PhFileReader.REDEFINE:
				return true;
			case PhFileReader.INTEGER:
				if (el_type.express_type == DataType.INTEGER || el_type.express_type == DataType.REAL) {
					return true;
				}
				return false;
			case PhFileReader.REAL:
				if (el_type.express_type == DataType.REAL) {
					return true;
				}
				return false;
			case PhFileReader.LOGICAL:
				if (el_type.express_type == DataType.LOGICAL) {
					return true;
				}
				return false;
			case PhFileReader.ENUM:
				if (el_type.express_type >= DataType.ENUMERATION && el_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
					return true;
				}
				return false;
			case PhFileReader.STRING:
				if (el_type.express_type == DataType.STRING) {
					return true;
				}
				return false;
			case PhFileReader.BINARY:
				if (el_type.express_type == DataType.BINARY) {
					return true;
				}
				return false;
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].compare_against_type(el_type);
			case PhFileReader.ENTITY_REFERENCE:
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
				if (el_type.express_type == DataType.ENTITY) {
					return true;
				}
				return false;
			case PhFileReader.EMBEDDED_LIST:
				return false;
		}
		return true;
	}


	final String get_value_type_name() throws SdaiException, java.io.IOException {
		switch (tag) {
			case PhFileReader.INTEGER:
				return "integer";
			case PhFileReader.REAL:
				return "real";
			case PhFileReader.LOGICAL:
				return "logical";
			case PhFileReader.ENUM:
				return "enumeration";
			case PhFileReader.STRING:
				return "string";
			case PhFileReader.BINARY:
				return "binary";
			case PhFileReader.TYPED_PARAMETER:
				return nested_values[0].get_value_type_name();
			case PhFileReader.ENTITY_REFERENCE:
			case PhFileReader.ENTITY_REFERENCE_SPECIAL:
				return "entity instance";
			case PhFileReader.EMBEDDED_LIST:
				return "aggregate";
		}
		return "";
	}


	private void printWarningToLogoValidate(String message) throws SdaiException {
		String text = message;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoValidateGiveType(String message, EData_type v_type) throws SdaiException {
		String text = message + 
		SdaiSession.line_separator + AdditionalMessages.EE_VATP + v_type.getName(null);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoValidateGiveValueTypes(String message, EData_type v_type1, EData_type v_type2) throws SdaiException {
		String text = message + 
		SdaiSession.line_separator + AdditionalMessages.EE_VATP + v_type1.getName(null) + 
		SdaiSession.line_separator + AdditionalMessages.EE_VATP + v_type2.getName(null);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoValidateGiveJavaType(String message, int java_type) throws SdaiException {
		String text = message + 
		SdaiSession.line_separator + AdditionalMessages.EE_VAJT + java_type;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoValidateGiveAttribute(String message, EAttribute attr) throws SdaiException {
		String text = message + 
		SdaiSession.line_separator + AdditionalMessages.RD_ATTR + attr.getName(null);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


	private void printWarningToLogoValidateGiveTypes(String message, EData_type variable_type, 
			EData_type val_type) throws SdaiException {
		String text = message + 
		SdaiSession.line_separator + AdditionalMessages.EE_VADT + variable_type.getName(null) + 
		SdaiSession.line_separator + AdditionalMessages.EE_VAAT + val_type.getName(null);
		StaticFields staticFields = StaticFields.get();
		if (staticFields.inst_under_valid != null) {
			text = text + SdaiSession.line_separator + AdditionalMessages.RD_VENT + 
			staticFields.inst_under_valid.getInstanceType().getName(null) +
			SdaiSession.line_separator + AdditionalMessages.RD_VINS + 
			staticFields.inst_under_valid.instance_identifier;
			SdaiSession ss = staticFields.inst_under_valid.owning_model.repository.session;
			if (ss != null && ss.logWriterSession != null) {
				ss.printlnSession(text);
			} else {
				SdaiSession.println(text);
			}
		} else {
			SdaiSession.println(text);
		}
	}


}
