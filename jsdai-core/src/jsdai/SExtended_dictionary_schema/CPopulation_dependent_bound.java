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

// Java class implementing entity population_dependent_bound

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CPopulation_dependent_bound extends CBound implements EPopulation_dependent_bound {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CPopulation_dependent_bound.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// bound_value: protected int a0 -  explicit redeclared as derived - java inheritance -  INTEGER
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);
	protected String a1; // schema_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected String a2; // entity_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	protected String a3; // method_name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a3$ = CEntity.initExplicitAttribute(definition, 3);
	// bound_value: protected int  - derived redeclaring - current -  INTEGER

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: schema_name, base type: STRING
	public boolean testSchema_name(EPopulation_dependent_bound type) throws SdaiException {
		return test_string(a1);
	}
	public String getSchema_name(EPopulation_dependent_bound type) throws SdaiException {
		return get_string(a1);
	}
	public void setSchema_name(EPopulation_dependent_bound type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetSchema_name(EPopulation_dependent_bound type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeSchema_name(EPopulation_dependent_bound type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: entity_name, base type: STRING
	public boolean testEntity_name(EPopulation_dependent_bound type) throws SdaiException {
		return test_string(a2);
	}
	public String getEntity_name(EPopulation_dependent_bound type) throws SdaiException {
		return get_string(a2);
	}
	public void setEntity_name(EPopulation_dependent_bound type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetEntity_name(EPopulation_dependent_bound type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeEntity_name(EPopulation_dependent_bound type) throws SdaiException {
		return a2$;
	}

	/// methods for attribute: method_name, base type: STRING
	public boolean testMethod_name(EPopulation_dependent_bound type) throws SdaiException {
		return test_string(a3);
	}
	public String getMethod_name(EPopulation_dependent_bound type) throws SdaiException {
		return get_string(a3);
	}
	public void setMethod_name(EPopulation_dependent_bound type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetMethod_name(EPopulation_dependent_bound type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeMethod_name(EPopulation_dependent_bound type) throws SdaiException {
		return a3$;
	}

	// methods for explicit redeclared as derived attribute: bound_value, base type: INTEGER
	public boolean testBound_value(EBound type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getBound_value(EBound type, SdaiContext _context) throws SdaiException {



				return (Value.alloc(ExpressTypes.INTEGER_TYPE).set(_context, 0));
	}
	public int getBound_value(EBound type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
			return getBound_value((EBound)null, _context).getInteger();
	}
	public void setBound_value(EBound type, int value) throws SdaiException {
			throw new SdaiException(SdaiException.AT_NVLD);
	}
	public void unsetBound_value(EBound type) throws SdaiException {
			throw new SdaiException(SdaiException.AT_NVLD);
	}
	public static jsdai.dictionary.EAttribute attributeBound_value(EBound type) throws SdaiException {
		return d0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a1 = null;
			a2 = null;
			a3 = null;
			return;
		}
		a1 = av.entityValues[1].getString(0);
		a2 = av.entityValues[1].getString(1);
		a3 = av.entityValues[1].getString(2);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: bound
		av.entityValues[0].values[0].tag = EntityValue.REDEFINE;
		// partial entity: population_dependent_bound
		av.entityValues[1].setString(0, a1);
		av.entityValues[1].setString(1, a2);
		av.entityValues[1].setString(2, a3);
	}
}
