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

// Java class implementing entity domain_equivalent_type

package jsdai.dictionary;
import jsdai.lang.*;

public class CDomain_equivalent_type extends CEntity implements EDomain_equivalent_type {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected Object a0; // external_type - current entity - ENTITY named_type
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // native_type - current entity - ENTITY named_type
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected Object a2; // owner - current entity - ENTITY external_schema
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
		if (a1 == old) {
			a1 = newer;
		}
		if (a2 == old) {
			a2 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// attribute: external_type, base type: entity named_type
	public static int usedinExternal_type(EDomain_equivalent_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testExternal_type(EDomain_equivalent_type type) throws SdaiException {
		return test_instance(a0);
	}
	public ENamed_type getExternal_type(EDomain_equivalent_type type) throws SdaiException {
		a0 = get_instance(a0);
		return (ENamed_type)a0;
	}
	public void setExternal_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException {
		a0 = set_instance(a0, value);
	}
	public void unsetExternal_type(EDomain_equivalent_type type) throws SdaiException {
		a0 = unset_instance(a0);
	}
	public static jsdai.dictionary.EAttribute attributeExternal_type(EDomain_equivalent_type type) throws SdaiException {
		return a0$;
	}

	// attribute: native_type, base type: entity named_type
	public static int usedinNative_type(EDomain_equivalent_type type, ENamed_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testNative_type(EDomain_equivalent_type type) throws SdaiException {
		return test_instance(a1);
	}
	public ENamed_type getNative_type(EDomain_equivalent_type type) throws SdaiException {
		a1 = get_instance(a1);
		return (ENamed_type)a1;
	}
	public void setNative_type(EDomain_equivalent_type type, ENamed_type value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetNative_type(EDomain_equivalent_type type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeNative_type(EDomain_equivalent_type type) throws SdaiException {
		return a1$;
	}

	// attribute: owner, base type: entity external_schema
	public static int usedinOwner(EDomain_equivalent_type type, EExternal_schema instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOwner(EDomain_equivalent_type type) throws SdaiException {
		return test_instance(a2);
	}
	public EExternal_schema getOwner(EDomain_equivalent_type type) throws SdaiException {
		a2 = get_instance(a2);
		return (EExternal_schema)a2;
	}
	public void setOwner(EDomain_equivalent_type type, EExternal_schema value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetOwner(EDomain_equivalent_type type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOwner(EDomain_equivalent_type type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = unset_instance(a1);
			a2 = unset_instance(a2);
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this);
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getInstance(2, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: domain_equivalent_type
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInstance(2, a2);
	}
}
