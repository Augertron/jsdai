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

// Java class implementing entity enumeration_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CEnumeration_type extends CData_type implements EEnumeration_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CEnumeration_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	protected A_string a1; // local_elements - current entity - LIST OF STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// elements: protected A_string  - derived - current -  LIST OF STRING
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: local_elements, base type: LIST OF STRING
	public boolean testLocal_elements(EEnumeration_type type) throws SdaiException {
		return test_aggregate(a1);
	}
	public A_string getLocal_elements(EEnumeration_type type) throws SdaiException {
		return (A_string)get_aggregate(a1);
	}
	public A_string createLocal_elements(EEnumeration_type type) throws SdaiException {
		a1 = create_aggregate_string(a1, a1$, 0);
		return a1;
	}
	public void unsetLocal_elements(EEnumeration_type type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeLocal_elements(EEnumeration_type type) throws SdaiException {
		return a1$;
	}

	// methods for attribute: elements, base type: LIST OF STRING
	public boolean testElements(EEnumeration_type type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public A_string getElements(EEnumeration_type type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
		return getElements((EEnumeration_type)null, _context).getStringAggregate(this);
	}
	public Value getElements(EEnumeration_type type, SdaiContext _context) throws SdaiException {



				return (Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id).set(_context, get(a1$)));
	}
	public static jsdai.dictionary.EAttribute attributeElements(EEnumeration_type type) throws SdaiException {
		return d0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getStringAggregate(0, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: enumeration_type
		av.entityValues[1].setStringAggregate(0, a1);
	}

	/*---------------------- methods to validate WHERE rules --------------------*/

	public int rEnumeration_typeWr1(SdaiContext _context) throws SdaiException {
	



		return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).OR(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).greater(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id).set(_context, get(a1$))), Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0)), Value.alloc(ExpressTypes.LOGICAL_TYPE).IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_ENUMERATION_TYPE", "EXTENDED_DICTIONARY_SCHEMA"), Value.alloc(jsdai.SExtended_dictionary_schema.CEnumeration_type.definition).set(_context, this).typeOfV(_context)))).getLogical());
	}
}
