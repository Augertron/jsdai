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

// Java class implementing entity uof_mapping

package jsdai.mapping;
import jsdai.lang.*;

public class CUof_mapping extends CEntity implements EUof_mapping {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CUof_mapping.class, SMapping.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected AEntity_mapping a1; // mappings - current entity - SET OF ENTITY
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a1, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EUof_mapping type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EUof_mapping type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EUof_mapping type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EUof_mapping type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EUof_mapping type) throws SdaiException {
		return a0$;
	}

	// methods for attribute: mappings, base type: SET OF ENTITY
	public static int usedinMappings(EUof_mapping type, EEntity_mapping instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testMappings(EUof_mapping type) throws SdaiException {
		return test_aggregate(a1);
	}
	public AEntity_mapping getMappings(EUof_mapping type) throws SdaiException {
		return (AEntity_mapping)get_aggregate(a1);
	}
	public AEntity_mapping createMappings(EUof_mapping type) throws SdaiException {
		a1 = (AEntity_mapping)create_aggregate_class(a1, a1$,  AEntity_mapping.class, 0);
		return a1;
	}
	public void unsetMappings(EUof_mapping type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeMappings(EUof_mapping type) throws SdaiException {
		return a1$;
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
		a1 = (AEntity_mapping)av.entityValues[0].getInstanceAggregate(1, a1$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: uof_mapping
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstanceAggregate(1, a1);
	}
}
