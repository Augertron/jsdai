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
import java.lang.reflect.*;

/* This class is used for setAll(), getAll(), prepareAll(). An array of 
	objects of this class is declared in <code>ComplexEntityValue</code>. 
	It serves for direct representation of the external mapping in ISO 10303-21. 
*/
/** This class is for internal JSDAI use only. Applications shall not use it. */
public class EntityValue {

/**
	Definition of the simple entity data type the values of whose attributes 
	are represented by this class.
*/
	CEntity_definition def;

/* 
	An array each element of which represents a value assigned to the 
	corresponding attribute of the simple entity data type <code>def</code>. 
	The attributes within a simple entity data type appear in the same order 
	as in the Express definition. 
*/
/** This field is for internal JSDAI use only. Applications shall not use it. */
	public Value values[];

/**
	The count of elements in <code>values</code>. 
*/
	int count;

	CEntity owner;

/**
	The select number attached to the value of the last attribute processed by 
	a 'get' method in this class. This number is needed only for a short 
	time when executing <code>setAll</code> within a JSDAI library class. 
	To access the number, <code>getSelectNumber</code> method is used.
*/
	private int iSelectNumber;

/*
	The constant indicating that the value of an attribute is redefined. 
*/
/** This field is for internal JSDAI use only. Applications shall not use it. */
	public static final int REDEFINE = PhFileReader.REDEFINE;

/**
	The attribute whose value is currently prepared to be returned by a 
	'get' method of this class. This field is used when printing a warning 
	message to log file.
*/
	static CExplicit_attribute current_attribute;

	SdaiSession owning_session;

	static final int NUMBER_OF_CHARACTERS_IN_ENT_VALUE = 512;

	Class [] paramValue;

	Object[] argsValue;

	boolean expr;



/**
	The constructor of this class. 
*/
	public EntityValue(SdaiSession session) {
		owning_session = session;
	}


	public EntityValue(EEntity_definition edef) {
		def = (CEntity_definition)edef;
		CEntityDefinition def = (CEntityDefinition)edef;
		if (SdaiSession.NUMBER_OF_VALUES >= def.noOfPartialAttributes) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		} else {
			values = new Value[def.noOfPartialAttributes];
		}
		for (int i = 0; i < def.noOfPartialAttributes; i++) {
			values[i] = new Value();
			values[i].tag = PhFileReader.MISSING;
		}
		count = def.noOfPartialAttributes;
	}


	final void unset_EntityValue() throws SdaiException {
		def = null;
		owner = null;
		current_attribute = null;
		owning_session = null;
		if (values == null) {
			return;
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				values[i].unset_Value();
			}
		}
	}


// Method below is for debugging purposes
/*	final void check_references_EntityValue() throws SdaiException {
		if (values == null) {
			return;
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				values[i].check_references_Value();
			}
		}
	}*/



/*	public void init(Class c) throws SdaiException {
	}
	public void unset() throws SdaiException {
	}
	public Value getAttribute(int index) throws SdaiException {  // indexing 0,1,2,...
		return null;
	}*/

//--------------------------------  get methods  ------------------------------
/* 
	Returns an integer that is a value of the attribute (specified by the index) 
	of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int getInteger(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return Integer.MIN_VALUE;
		}
		return values[index].getInteger(owning_session);
	}


/* 
	Returns a double that is a value of the attribute (specified by the index) 
	of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public double getDouble(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return Double.NaN;
		}
		return values[index].getDouble(owning_session);
	}


/* 
	Returns a string that is a value of the attribute (specified by the index) 
	of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public String getString(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getString(owning_session);
	}


/* 
	Returns an integer encoding a logical number that is a value of the 
	attribute (specified by the index) of the simple entity data type 
	represented by this class. This returned value is used by <code>setAll</code> 
	method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int getLogical(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return 0;
		}
		return values[index].getLogical(owning_session);
	}


/* 
	Returns an integer encoding a boolean number that is a value of the 
	attribute (specified by the index) of the simple entity data type 
	represented by this class. This returned value is used by <code>setAll</code> 
	method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int getBoolean(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return 0;
		}
		return values[index].getBoolean(owning_session);
	}


/* 
	Returns an integer encoding an enumeration item that is a value of the 
	attribute (specified by the index) of the simple entity data type 
	represented by this class. This returned value is used by <code>setAll</code> 
	method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int getEnumeration(int index, CExplicit_attribute attrib) throws SdaiException {
		if (attrib == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier, attrib);
			return 0;
		}
		DataType domain = (DataType)attrib.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.ENUMERATION || domain.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return values[index].getEnumeration((EEnumeration_type)domain, attrib, null, owning_session);
	}


/* 
	Returns a <code>Binary</code> that is a value of the attribute (specified 
	by the index) of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Binary getBinary(int index) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getBinary(owning_session);
	}


/* 
	Returns an entity instance that is a value of the attribute (specified by 
	the index) of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public InverseEntity getInstance(int index, CEntity inst) throws SdaiException {
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		CExplicit_attribute attr = null;
		if (def != null && ((CEntityDefinition)def).attributesExpl != null)  {
			attr = ((CEntityDefinition)def).attributesExpl[index];
		}
//		return values[index].getInstance(inst);
		return values[index].getInstance(inst, attr);
	}


/* 
	Returns an entity instance that is a value of the attribute (specified by 
	the index) of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class. The parameters denote the following:
	 - index: the index position of the value of this <code>EntityValue</code> 
				to be read;
	 - inst: the instance to which attribute the returned value will be 
				assigned (speaking differently, owning instance);
	 - attr: an attribute to which the returned value will be assigned.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public InverseEntity getInstance(int index, CEntity inst, CExplicit_attribute attr) throws SdaiException {
		StaticFields staticFields;
		if (count <= index) {
			staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		Value val = values[index];
		if (val.tag == PhFileReader.ENTITY_REFERENCE) {
			if (val.reference instanceof CEntity) {
				CEntity ref = (CEntity)val.reference;
				DataType base = (DataType)attr.getDomain(null);
//CEntity_definition given = (CEntity_definition)ref.getInstanceType();
//CEntity_definition required = (CEntity_definition)base;
//System.out.println("  EntityValue    given: " + given.getName(null) +
//"    required: " + required.getName(null) + 
//"   owner: #" + inst.instance_identifier + 
//"   value: #" + ref.instance_identifier);
				if (base.express_type == DataType.ENTITY) {
					CEntity_definition given = (CEntity_definition)ref.getInstanceType();
					if (!given.isSubtypeOf((CEntity_definition)base)) {
						staticFields = StaticFields.get();
						printWarningToLogo(owning_session, AdditionalMessages.RD_WRRE, staticFields.current_instance_identifier,
							ref.instance_identifier);
						return null;
					}
//				} else {
//					printWarningToLogo(owning_session, AdditionalMessages.RD_BREF, staticFields.current_instance_identifier);
//					return null;
				}
				inst.addToInverseList(ref);
				return ref;
			} else {
				SdaiModel.Connector ref = (SdaiModel.Connector)val.reference;
				return ref;
			}
		} else if (val.tag == PhFileReader.ENTITY_REFERENCE_SPECIAL) {
			CLateBindingEntity late_ref = (CLateBindingEntity)val.reference;
			if (late_ref != null) {
				late_ref.inverseAdd(inst);
			}
			return late_ref;
//			return (CLateBindingEntity)val.reference;
		} else if (val.tag == PhFileReader.MISSING) {
			return null;
		} else if (val.tag == PhFileReader.REDEFINE) {
			boolean redecl = false;
			if (attr != null) {
				CEntityDefinition inst_def = (CEntityDefinition)inst.getInstanceType();
				redecl = inst_def.checkIfDerived(attr);
			}
			if (!redecl) {
				staticFields = StaticFields.get();
				printWarningToLogo(owning_session, AdditionalMessages.RD_RIRE, 
					staticFields.current_instance_identifier);
			}
			return null;
		}
		if (!expr) {
			printWarningToLogo(owning_session, AdditionalMessages.RD_BREF, inst.instance_identifier);
		}
		return null;
	}


/* 
	Returns a value of the attribute (specified by the index) of the simple 
	entity data type represented by this class. 
	This method is applied when the attribute is of the select data type. 
	The returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Object getMixed(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		StaticFields staticFields;
		if (count <= index) {
			staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		Value val = values[index];
		staticFields = StaticFields.get();
		staticFields.current_attribute = attr;
		Object obj = val.getMixedValue(attr, inst);
		iSelectNumber = val.sel_number;
		return obj;
	}


/* 
	Returns an aggregate of integers that is a value of the attribute (specified 
	by the index) of the simple entity data type represented by this class. 
	This returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_integer getIntegerAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getIntegerAggregate(attr, inst);
	}


/* 
	Returns an aggregate of real numbers that is a value of the attribute 
	(specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_double getDoubleAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getDoubleAggregate(attr, inst);
	}


/* 
	Returns an aggregate of strings that is a value of the attribute 
	(specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_string getStringAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getStringAggregate(attr, inst);
	}


/* 
	Returns an aggregate of enumeration items encoding logical numbers that is 
	a value of the attribute (specified by the index) of the simple entity data 
	type represented by this class. This returned value is used by 
	<code>setAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_enumeration getLogicalAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getLogicalAggregate(attr, inst);
	}


/* 
	Returns an aggregate of boolean numbers that is a value of the attribute 
	(specified by the index) of the simple entity data type represented by this 
	class. This returned value is used by <code>setAll</code> method within a 
	JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_boolean getBooleanAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getBooleanAggregate(attr, inst);
	}


/* 
	Returns an aggregate of enumeration items that is a value of the attribute 
	(specified by the index) of the simple entity data type represented by this 
	class. This returned value is used by <code>setAll</code> method within a 
	JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_enumeration getEnumerationAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getEnumerationAggregate(attr, inst);
	}


/* 
	Returns an aggregate of <code>Binary</code> objects that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public A_binary getBinaryAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getBinaryAggregate(attr, inst);
	}


/* 
	Returns an aggregate of entity instances that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aggregate getInstanceAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		if (!expr) {
			values[index].aux = 1001;
		}
		Aggregate aggr = values[index].getInstanceAggregate(attr, inst);
		if (!expr) {
			values[index].aux = 0;
		}
		if (Double.isNaN(values[index].real)) {
			((CAggregate)aggr).myLength = -((CAggregate)aggr).myLength;
			values[index].real = 0;
		}
		return aggr;
//		return values[index].getInstanceAggregate(attr, inst);
	}


/* 
	Returns a general aggregate that is a value of the attribute (specified 
	by the index) of the simple entity data type represented by this class. 
	This method is applied when the aggregate members are of the select type. 
	The returned value is used by <code>setAll</code> method within a JSDAI 
	library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aggregate getMixedAggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		if (!expr) {
			values[index].aux = 1001;
		}
		Aggregate aggr = values[index].getMixedAggregate(attr, inst);
		if (!expr) {
			values[index].aux = 0;
		}
		if (Double.isNaN(values[index].real)) {
			((CAggregate)aggr).myLength = -((CAggregate)aggr).myLength;
			values[index].real = 0;
		}
		return aggr;
//		return values[index].getMixedAggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of integer numbers that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_integer getInteger2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getInteger2Aggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of real numbers that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_double getDouble2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getDouble2Aggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of strings that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_string getString2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getString2Aggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of integers encoding logical numbers that 
	is a value of the attribute (specified by the index) of the simple entity 
	data type represented by this class. This returned value is used by 
	<code>setAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_integer getLogical2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getLogical2Aggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of boolean numbers that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_boolean getBoolean2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getBoolean2Aggregate(attr, inst);
	}


/* 
	Returns a double-nested aggregate of enumeration items that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aa_enumeration getEnumeration2Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getEnumeration2Aggregate(attr, inst);
	}


/* 
	Returns a triple-nested aggregate of real numbers that is a value of the 
	attribute (specified by the index) of the simple entity data type represented 
	by this class. This returned value is used by <code>setAll</code> method 
	within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Aaa_double getDouble3Aggregate(int index, CExplicit_attribute attr, CEntity inst) 
			throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (count <= index) {
			StaticFields staticFields = StaticFields.get();
			printWarningToLogo(owning_session, AdditionalMessages.RD_INSV, staticFields.current_instance_identifier);
			return null;
		}
		return values[index].getDouble3Aggregate(attr, inst);
	}



//--------------------------------  set methods  ------------------------------
/* 
	Stores an integer taken as a value of an attribute to an instance of 
	<code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setInteger(int index, int value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != Integer.MIN_VALUE) {
			values[index].tag = PhFileReader.INTEGER;
			values[index].integer = value;
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a real number taken as a value of an attribute to an instance of 
	<code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setDouble(int index, double value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (!Double.isNaN(value)) {
			values[index].tag = PhFileReader.REAL;
			values[index].real = value;
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a string taken as a value of an attribute to an instance of 
	<code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setString(int index, String value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.STRING;
			values[index].string = value;
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an integer encoding a logical number taken as a value of an 
	attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setLogical(int index, int value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value == 0) {
			values[index].tag = PhFileReader.MISSING;
			return;
		}
		values[index].tag = PhFileReader.LOGICAL;
		if (value == 2) {
			values[index].integer = 1;
		} else if (value == 1) {
			values[index].integer = 0;
		} else {
			values[index].integer = 2;
		}
	}


/* 
	Stores an integer encoding a boolean number taken as a value of an 
	attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setBoolean(int index, int value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value == 0) {
			values[index].tag = PhFileReader.MISSING;
			return;
		}
		values[index].tag = PhFileReader.BOOLEAN;
		if (value == 2) {
			values[index].integer = 1;
		} else {
			values[index].integer = 0;
		}
		values[index].length = 3;
	}


/* 
	Stores an integer encoding an enumeration item taken as a value of an 
	attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setEnumeration(int index, int value, CExplicit_attribute attr) throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		values[index].setEnumeration(value, attr);
	}


/* 
	Stores a <code>Binary</code> taken as a value of an attribute to an 
	instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setBinary(int index, Binary value) {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.BINARY;
			values[index].reference = value;
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an entity instance taken as a value of an attribute to an 
	instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setInstance(int index, Object value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
//			if (!(value instanceof CEntity || value instanceof SdaiModel.Connector)) {
//				throw new SdaiException(SdaiException.SY_ERR);
//			}
//			values[index].tag = PhFileReader.ENTITY_REFERENCE;
//			values[index].reference = value;
			if (value instanceof CEntity) {
				values[index].tag = PhFileReader.ENTITY_REFERENCE;
				values[index].reference = value;
			} else if (value instanceof SdaiModel.Connector) {
				values[index].tag = PhFileReader.ENTITY_REFERENCE;
				values[index].reference = value;
				if (values[index].nested_values == null) {
					values[index].nested_values = new Value[SdaiSession.NUMBER_OF_VALUES];
				}
				if (values[index].nested_values[0] == null) {
					values[index].nested_values[0] = new Value();
				}
				values[index].nested_values[0].reference = ((SdaiModel.Connector)value).resolveModelIn();
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a value of an attribute of the select data type to an 
	instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setMixed(int index, Object value, CExplicit_attribute attr, 
			int sel_number) throws SdaiException {
		if (attr == null) {
			String base = SdaiSession.line_separator + AdditionalMessages.DI_ATNS;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value == null) {
			values[index].tag = PhFileReader.MISSING;
			return;
		}
		if (value instanceof SdaiModel.Connector) {
			values[index].tag = PhFileReader.ENTITY_REFERENCE;
			values[index].reference = value;
			return;
		}
		if (sel_number <= 1) {
			if (!(value instanceof CEntity)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			values[index].tag = PhFileReader.ENTITY_REFERENCE;
			values[index].reference = value;
			return;
		}
		DataType domain = (DataType)attr.getDomain(null);
		while (domain.express_type == DataType.DEFINED_TYPE) {
			domain = (DataType)((CDefined_type)domain).getDomain(null);
		}
		if (domain.express_type < DataType.SELECT || domain.express_type > DataType.ENT_EXT_EXT_SELECT) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		SelectType sel = (SelectType)domain;
		sel_number -= 2;
		if (sel_number < 0) {
//System.out.println("EntityValue  sel_number = " + sel_number);
			throw new SdaiException(SdaiException.SY_ERR);
		}
		/*System.out.println("setMixed: "  // debug message for testing select of number
		 + value + " "                  //  4
		 + sel_number + " "             // 19
		 + index + " "                  //  0
		 + value + " "                  //  4
		 + sel.tags[sel_number]  + " "  // 23
		 + sel.paths[sel_number] );     // [Ljsdai.dictionary.CDefined_type;@af45acb8*/
		values[index].setTyped(value, sel.tags[sel_number], sel.paths[sel_number], sel_number);
	}


/* 
	Stores an aggregate of integer numbers taken as a value of an attribute to 
	an instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setIntegerAggregate(int index, A_integer value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrInteger(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of real numbers taken as a value of an attribute to 
	an instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setDoubleAggregate(int index, A_double value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
//			if (value.myType.check_aggregation_double()) {
			if (!value.myType.check_aggregation_double()) {
				values[index].setAggrDouble(value);
			} else {
				CEntity owner = value.getOwningInstance();
				if ((owner != null && !owner.owning_model.repository.session.a_double3_overflow && 
						!SdaiSession.getSession().a_double3_overflow) || value instanceof A_double3) {
					values[index].setAggrDouble((A_double3)value);
				} else {
					values[index].setAggrDouble(value);
				}
			}
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of strings taken as a value of an attribute to 
	an instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setStringAggregate(int index, A_string agg) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (agg != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = agg;
			values[index].setAggrString(agg);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of enumeration items encoding logical numbers taken as 
	a value of an attribute to an instance of <code>Value</code> (specified by 
	the index) within the array <code>values</code> defined in this class. 
	This method is invoked by <code>getAll</code> method within a JSDAI library 
	class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setLogicalAggregate(int index, A_enumeration value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrLogical(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of boolean numbers taken as a value of an attribute to 
	an instance of <code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setBooleanAggregate(int index, A_boolean value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrBoolean(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of enumeration items taken as a value of an attribute 
	to an instance of <code>Value</code> (specified by the index) within the 
	array <code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setEnumerationAggregate(int index, A_enumeration value, CExplicit_attribute attr) 
			throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			DataType el_type = (DataType)value.myType.getElement_type(null);
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type < DataType.ENUMERATION || el_type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			A_string elements;
			if (el_type.express_type == DataType.EXTENSIBLE_ENUM || el_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
				CEntity owning_inst = value.getOwningInstance();
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
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrEnumeration(value, elements);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of <code>Binary</code> objects taken as a value of an 
	attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setBinaryAggregate(int index, A_binary agg) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (agg != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = agg;
			values[index].setAggrBinary(agg);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of entity instances taken as a value of an 
	attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setInstanceAggregate(int index, CAggregate value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrElements(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores an aggregate of elements (whose type is the select data type) taken 
	as a value of an attribute to an instance of <code>Value</code> (specified 
	by the index) within the array <code>values</code> defined in this class. 
	This method is invoked by <code>getAll</code> method within a JSDAI library 
	class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setMixedAggregate(int index, CAggregate value, CExplicit_attribute attr) 
			throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setMixedAggrElements(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of integer numbers taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setInteger2Aggregate(int index, Aa_integer value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrIntegerNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of real numbers taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setDouble2Aggregate(int index, Aa_double value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrDoubleNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of strings taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setString2Aggregate(int index, Aa_string value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrStringNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of enumeration items encoding logical 
	numbers taken as a value of an attribute to an instance of 
	<code>Value</code> (specified by the index) within the array 
	<code>values</code> defined in this class. This method is invoked by 
	<code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setLogical2Aggregate(int index, Aa_enumeration value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrLogicalNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of boolean numbers taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setBoolean2Aggregate(int index, Aa_boolean value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrBooleanNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a double-nesting aggregate of enumeration items taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setEnumeration2Aggregate(int index, Aa_enumeration value, CExplicit_attribute attr) 
			throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrEnumerationNested(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


/* 
	Stores a triple-nesting aggregate of real numbers taken as a value of 
	an attribute to an instance of <code>Value</code> (specified by the index) 
	within the array <code>values</code> defined in this class. This method is 
	invoked by <code>getAll</code> method within a JSDAI library class.
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void setDouble3Aggregate(int index, Aaa_double value) throws SdaiException {
		if (values == null) {
			values = new Value[SdaiSession.NUMBER_OF_VALUES];
		}
		if (values.length <= index) {
			enlarge(index + 1);
		}
		if (values[index] == null) {
			values[index] = new Value();
		}
		if (value != null) {
			values[index].tag = PhFileReader.EMBEDDED_LIST;
			values[index].reference = value;
			values[index].setAggrDoubleNested2(value);
		} else {
			values[index].tag = PhFileReader.MISSING;
		}
	}


//--------------------------------  other methods  ----------------------------
/* 
	Returns the select number attached to the value of the last attribute 
	processed by a 'get' method in this class. This number is needed only for 
	a short time when executing <code>setAll</code> within a JSDAI library 
	class. 
*/
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int getSelectNumber() {
		return iSelectNumber;
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void init(EEntity_definition type) throws SdaiException {                     // Do we need this method?
		def = (CEntity_definition)type;
		count = 0;
	}


// This can be used for Express constructs like 
//  p\cartesian_point.coordinates[1] :=7.5
// More precisely, for the point ('.') in the example above.
/** This method is for internal JSDAI use only. Applications shall not use it. */
	public Value get(EAttribute attr) throws SdaiException {                             // Do we need this method?
		for (int i = 0; i < ((CEntityDefinition)def).attributes.length; i++) {
			if (((CEntityDefinition)def).attributes[i] == attr) {
				return values[i];
			}
		}  
// Print message !!!!!!!!!!!!!!!!!! the attribute is not of the underlying entity
//		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
		throw new SdaiException(SdaiException.SY_ERR/*, base*/);
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void set(EExplicit_attribute attr, Value val) throws SdaiException {          // Do we need this method?
		for (int i = 0; i < ((CEntityDefinition)def).attributes.length; i++) {
			if (((CEntityDefinition)def).attributes[i] == attr) {
				values[i] = val;
				return;
			}
		}
// Print message !!!!!!!!!!!!!!!!!! the attribute is not of the underlying entity
//		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
		throw new SdaiException(SdaiException.SY_ERR/*, base*/);
	}


	int getAttributeIndex(EAttribute attr) throws SdaiException {
		CExplicit_attribute [] attrs = ((CEntityDefinition)def).takeExplicit_attributes();
		int position = -1;
		for (int i = 0; i < attrs.length; i++) {
			CExplicit_attribute at = attrs[i];
			if (!at.testRedeclaring(null)) {
				position++;
				if (attr == at) {
					return position;
				}
			}
		}
		return -1;
	}


	Value findAttribute(EAttribute attr, SdaiContext context) throws SdaiException {
		int ind = getAttributeIndex(attr);
		CEntityDefinition edef;
		if (ind < 0) {
			if (!(owner instanceof CEntity)) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			edef = (CEntityDefinition)owner.getInstanceType();
			Value v;
			if (((AttributeDefinition)attr).attr_tp == AttributeDefinition.INVERSE) {
				EInverse_attribute inv_attrib = (EInverse_attribute)attr;
				AEntity inv_val = owner.get_inverse(inv_attrib, null);
				v = new Value();
				if (inv_attrib.testMin_cardinality(null)) {
					v.v_type = v.d_type = inv_val.myType;
					v.tag = PhFileReader.EMBEDDED_LIST;
					v.reference = inv_val;
					v.aux = -1;
					v.setAggrElements(inv_val);
				} else {
					EEntity_definition domain = inv_attrib.getDomain(null);
					v.d_type = domain;
					if (inv_val.myLength > 0) {
						v.v_type = domain;
						v.tag = PhFileReader.ENTITY_REFERENCE;
						v.reference = inv_val.getByIndexEntity(1);
					} else {
						v.tag = PhFileReader.MISSING;
					}
				}
				v.aux = 3;
			} else {
				v = get_derivedValue(edef, attr, context);
			}
			return v;
		}
		if (values[ind].tag != PhFileReader.REDEFINE) {
			return values[ind];
		}
		edef = (CEntityDefinition)owner.getInstanceType();
		CDerived_attribute der_attr = edef.getDerivedForExplicit((CExplicit_attribute)attr);
		return get_derivedValue(edef, der_attr, context);
	}


	private Value get_derivedValue(CEntityDefinition edef, EAttribute attr, SdaiContext context) throws SdaiException {
		if (edef.attributesDerived != null) {
			for (int i = 0; i < edef.attributesDerived.length; i++) {
				if (edef.attributesDerived[i] == attr) {
					Value v = get_derived_value(edef, i, context);
					if (v == null) {
						throw new SdaiException(SdaiException.VA_NSET);
					}
					return v;
				}
			}
		}
		return null;
	}


	private Value get_derived_value(CEntityDefinition edef, int index, SdaiContext context) throws SdaiException {
		if (argsValue == null) {
			argsValue = new Object [2];
			argsValue[0] = null;
		}
		if (context != null) {
			argsValue[1] = context;
		} else {
			argsValue[1] = new SdaiContext(owner);
		}
		Method meth;
		if (edef.attributesDerivedMethodValue[index] instanceof Method) {
			meth = (Method)edef.attributesDerivedMethodValue[index];
		} else {
			if (paramValue == null) {
				paramValue = new Class[2];
				paramValue[1] = SdaiContext.class;
			}
			paramValue[0] = (Class)edef.attributesDerivedMethodValue[index];
			Class cl = edef.attributesDerivedClass[index];
			try {
				meth = cl.getDeclaredMethod(edef.attributesDerivedMethodName[index], paramValue);
			} catch (java.lang.NoSuchMethodException ex) {
				throw new SdaiException(SdaiException.SY_ERR, ex);
			}
			edef.attributesDerivedMethodValue[index] = meth;
		}
		Object res;
		try {
			res = meth.invoke(owner, argsValue);
		} catch (Exception ex) {
//System.out.println("EntityValue  ***** meth: " + meth.getName() + "   in class: " + cl);
//ex.printStackTrace();
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return (Value) res;
	}


	boolean setAttribute(EAttribute attr, Value val) throws SdaiException {
		int ind = getAttributeIndex(attr);
		if (ind >= 0) {
			values[ind] = val;
			return false;
		}
		return true;
	}


	void copyEntityValue(EntityValue ev) throws SdaiException {
		def = ev.def;
		values = new Value[ev.count];
		for (int i = 0; i < ev.count; i++) {
			values[i] = new Value();
			values[i].copyValue(ev.values[i], ev.owner);
		}
		count = ev.count;
		owner = ev.owner;
	}


	public String toString() {
//		synchronized (SdaiCommon.syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return super.toString();
		}
//		} // syncObject
	}


/**
	This method is doing a job of the public method <code>toString</code>.
*/
	private String getAsString() throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		int str_length = getAsByteArray(staticFields);
		return new String(staticFields.ent_value_as_string, 0, str_length);
	}


	int getAsByteArray(StaticFields staticFields) throws SdaiException {
		int i;
		int str_index = 0;
		int str_length;

		if (staticFields.ent_value_as_string == null) {
			staticFields.ent_value_as_string = new byte[NUMBER_OF_CHARACTERS_IN_ENT_VALUE];
		}
		String str = def.getName(null).toUpperCase();
		str_length = str.length();
		if (str_length + 1 >= staticFields.ent_value_as_string.length) {
			enlarge_ent_value_string(staticFields, 0, str_length + 1);
		}
		for (i = 0; i < str_length; i++) {
			staticFields.ent_value_as_string[str_index++] = (byte)str.charAt(i);
		}
		staticFields.ent_value_as_string[str_index++] = PhFileReader.LEFT_PARENTHESIS;
		boolean first = true;
		for (i = 0; i < count; i++) {
			str_length = values[i].getAsByteArray(staticFields);
			if (str_index + str_length + 1 >= staticFields.ent_value_as_string.length) {
				enlarge_ent_value_string(staticFields, str_index, str_index + str_length + 1);
			}
			if (!first) {
				staticFields.ent_value_as_string[str_index++] = PhFileReader.COMMA_b;
			}
			first = false;
			for (int j = 0; j < str_length; j++) {
				staticFields.ent_value_as_string[str_index++] = staticFields.value_as_string[j];
			}
		}
		staticFields.ent_value_as_string[str_index++] = PhFileReader.RIGHT_PARENTHESIS;
		return str_index;
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, long inst_ident) throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident);
		}
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, CExplicit_attribute attr) 
			throws SdaiException {
		String str;
		if (attr == null) {
			str = "";
		} else {
			str = SdaiSession.line_separator + AdditionalMessages.RD_ATTR + attr.getName(null);
		}
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		}
	}


	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, String attr_name) 
			throws SdaiException {
		String str;
		if (attr_name == null) {
			str = "";
		} else {
			str = SdaiSession.line_separator + AdditionalMessages.RD_ATTR + attr_name;
		}
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		}
	}


	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, String entity_name, 
			String part_entity_name) throws SdaiException {
		String str = SdaiSession.line_separator + AdditionalMessages.RD_ENT + entity_name + 
			SdaiSession.line_separator + AdditionalMessages.RD_SENT + part_entity_name;
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + str);
		}
	}


/**
	Prints a warning message to logo file.
*/
	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, long referenced_inst) 
			throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator +
				AdditionalMessages.RD_IREF + referenced_inst);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INST + inst_ident + SdaiSession.line_separator +
				AdditionalMessages.RD_IREF + referenced_inst);
		}
	}


/**
	Increases the size of the array 'values' either twice or to satisfy 
	the required demand, whichever of these two values is larger. 
	Besides current class, this method is used in <code>SdaiModel</code> 
	and <code>PhFileReader</code>.
*/
	void enlarge(int demand) {
		int old_length = values.length;
		int new_length = old_length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		Value new_values[] = new Value[new_length];
		System.arraycopy(values, 0, new_values, 0, old_length);
		values = new_values;
	}


	private static void enlarge_ent_value_string(StaticFields staticFields, int str_length, int demand) {
		int new_length = staticFields.ent_value_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_ent_value_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.ent_value_as_string, 0, new_ent_value_as_string, 0, str_length);
		staticFields.ent_value_as_string = new_ent_value_as_string;
	}


}
