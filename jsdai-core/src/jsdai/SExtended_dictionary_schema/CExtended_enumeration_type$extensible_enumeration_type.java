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

// Java class implementing entity extended_enumeration_type$extensible_enumeration_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CExtended_enumeration_type$extensible_enumeration_type extends CEntity implements EExtended_enumeration_type, EExtensible_enumeration_type {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CExtended_enumeration_type$extensible_enumeration_type.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - non-java inheritance - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected A_string a1; // local_elements - non-java inheritance - LIST OF STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// elements: protected A_string  - derived - non-java inheritance -  LIST OF STRING
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected Object a2; // is_based_on - non-java inheritance - ENTITY defined_type
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a2 == old) {
			a2 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EData_type type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EData_type type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EData_type type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EData_type type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EData_type type) throws SdaiException {
		return a0$;
	}

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



				return (Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id).set(_context, get(jsdai.SExtended_dictionary_schema.CEnumeration_type.attributeLocal_elements(null))));
	}
	public static jsdai.dictionary.EAttribute attributeElements(EEnumeration_type type) throws SdaiException {
		return d0$;
	}

	// attribute: is_based_on, base type: entity defined_type
	public static int usedinIs_based_on(EExtended_enumeration_type type, EDefined_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testIs_based_on(EExtended_enumeration_type type) throws SdaiException {
		return test_instance(a2);
	}
	public EDefined_type getIs_based_on(EExtended_enumeration_type type) throws SdaiException {
		a2 = get_instance(a2);
		return (EDefined_type)a2;
	}
	public void setIs_based_on(EExtended_enumeration_type type, EDefined_type value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetIs_based_on(EExtended_enumeration_type type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeIs_based_on(EExtended_enumeration_type type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[1].getStringAggregate(0, a1$, this);
		a2 = av.entityValues[2].getInstance(0, this, a2$);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: data_type
		av.entityValues[0].setString(0, a0);
		// partial entity: enumeration_type
		av.entityValues[1].setStringAggregate(0, a1);
		// partial entity: extended_enumeration_type
		av.entityValues[2].setInstance(0, a2);
		// partial entity: extensible_enumeration_type
	}

	/*---------------------- methods to validate WHERE rules --------------------*/

	public int rEnumeration_typeWr1(SdaiContext _context) throws SdaiException {
	



		return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).OR(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).greater(_context, Value.alloc(ExpressTypes.INTEGER_TYPE).sizeOf(Value.alloc(jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema._st_list_0_unique_express_id).set(_context, get(jsdai.SExtended_dictionary_schema.CEnumeration_type.attributeLocal_elements(null)))), Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0)), Value.alloc(ExpressTypes.LOGICAL_TYPE).IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_ENUMERATION_TYPE", "EXTENDED_DICTIONARY_SCHEMA"), Value.alloc(jsdai.SExtended_dictionary_schema.CExtended_enumeration_type$extensible_enumeration_type.definition).set(_context, this).typeOfV(_context)))).getLogical());
	}
	public int rExtended_enumeration_typeWr1(SdaiContext _context) throws SdaiException {
	




		return (Value.alloc(ExpressTypes.LOGICAL_TYPE).set(_context, Value.alloc(ExpressTypes.LOGICAL_TYPE).IN(_context, Value.alloc(ExpressTypes.STRING_TYPE).set(_context, "*.EXTENSIBLE_ENUMERATION_TYPE", "EXTENDED_DICTIONARY_SCHEMA"), Value.alloc(jsdai.SExtended_dictionary_schema.CDefined_type.definition).set(_context, get(jsdai.SExtended_dictionary_schema.CExtended_enumeration_type.attributeIs_based_on(null))).getAttribute(jsdai.SExtended_dictionary_schema.CDefined_type.attributeDomain(null), _context).typeOfV(_context))).getLogical());
	}
}
