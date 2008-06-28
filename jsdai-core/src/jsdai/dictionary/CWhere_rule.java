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

// Java class implementing entity where_rule

package jsdai.dictionary;
import jsdai.lang.*;

public class CWhere_rule extends WhereRule implements EWhere_rule {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected String a0; // label - current entity - STRING
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected Object a1; // parent_item - current entity - SELECT type_or_rule
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected int a2; // order - current entity - INTEGER
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a1 == old) {
			a1 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: label, base type: STRING
	public boolean testLabel(EWhere_rule type) throws SdaiException {
		return test_string(a0);
	}
	public String getLabel(EWhere_rule type) throws SdaiException {
		return get_string(a0);
	}
	public void setLabel(EWhere_rule type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetLabel(EWhere_rule type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeLabel(EWhere_rule type) throws SdaiException {
		return a0$;
	}

	// methods for SELECT attribute: parent_item
	public static int usedinParent_item(EWhere_rule type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testParent_item(EWhere_rule type) throws SdaiException {
		return test_instance(a1);
	}

	public jsdai.lang.EEntity getParent_item(EWhere_rule type) throws jsdai.lang.SdaiException { // case 1
		a1 = get_instance_select(a1);
		return (jsdai.lang.EEntity)a1;
	}

	public void setParent_item(EWhere_rule type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException { // case 1
		a1 = set_instance(a1, value);
	}

	public void unsetParent_item(EWhere_rule type) throws SdaiException {
		a1 = unset_instance(a1);
	}

	public static jsdai.dictionary.EAttribute attributeParent_item(EWhere_rule type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: order, base type: INTEGER
	public boolean testOrder(EWhere_rule type) throws SdaiException {
		return test_integer(a2);
	}
	public int getOrder(EWhere_rule type) throws SdaiException {
		return get_integer(a2);
	}
	public void setOrder(EWhere_rule type, int value) throws SdaiException {
		a2 = set_integer(value);
	}
	public void unsetOrder(EWhere_rule type) throws SdaiException {
		a2 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeOrder(EWhere_rule type) throws SdaiException {
		return a2$;
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
		a1 = av.entityValues[0].getInstance(1, this);
		a2 = av.entityValues[0].getInteger(2);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: where_rule
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setInstance(1, a1);
		av.entityValues[0].setInteger(2, a2);
	}
}
