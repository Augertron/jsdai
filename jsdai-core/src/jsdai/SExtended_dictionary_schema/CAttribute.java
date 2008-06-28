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

// Java class implementing entity attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CAttribute extends CEntity implements EAttribute {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CAttribute.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected String a0; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected Object a1; // parent - current entity - ENTITY entity_or_view_definition
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	protected int a2; // order - current entity - INTEGER
	protected static final jsdai.dictionary.CExplicit_attribute a2$ = CEntity.initExplicitAttribute(definition, 2);
	// parent_entity: protected Object  - derived - current -  ENTITY entity_definition
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: name, base type: STRING
	public boolean testName(EAttribute type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EAttribute type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EAttribute type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EAttribute type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EAttribute type) throws SdaiException {
		return a0$;
	}

	// attribute: parent, base type: entity entity_or_view_definition
	public static int usedinParent(EAttribute type, EEntity_or_view_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParent(EAttribute type) throws SdaiException {
		return test_instance(a1);
	}
	public EEntity_or_view_definition getParent(EAttribute type) throws SdaiException {
		a1 = get_instance(a1);
		return (EEntity_or_view_definition)a1;
	}
	public void setParent(EAttribute type, EEntity_or_view_definition value) throws SdaiException {
		a1 = set_instance(a1, value);
	}
	public void unsetParent(EAttribute type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeParent(EAttribute type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: order, base type: INTEGER
	public boolean testOrder(EAttribute type) throws SdaiException {
		return test_integer(a2);
	}
	public int getOrder(EAttribute type) throws SdaiException {
		return get_integer(a2);
	}
	public void setOrder(EAttribute type, int value) throws SdaiException {
		a2 = set_integer(value);
	}
	public void unsetOrder(EAttribute type) throws SdaiException {
		a2 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeOrder(EAttribute type) throws SdaiException {
		return a2$;
	}

	// derived attribute: parent_entity, base type: entity entity_definition
	public boolean testParent_entity(EAttribute type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getParent_entity(EAttribute type, SdaiContext _context) throws SdaiException {



				return (jsdai.SExtended_dictionary_schema.FGet_entity_definition.run(_context, Value.alloc(jsdai.SExtended_dictionary_schema.CEntity_or_view_definition.definition).set(_context, get(a1$))));
	}
	public EEntity_definition getParent_entity(EAttribute type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
			return (EEntity_definition) getParent_entity(null, _context).getInstance();
	}
	public static jsdai.dictionary.EAttribute attributeParent_entity(EAttribute type) throws SdaiException {
		return d0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = unset_instance(a1);
			a2 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getInstance(1, this, a1$);
		a2 = av.entityValues[0].getInteger(2);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: attribute
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
	}
}
