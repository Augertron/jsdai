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

// Java class implementing entity documentation_with_element

package jsdai.dictionary;
import jsdai.lang.*;

public class CDocumentation_with_element extends CEntity implements EDocumentation_with_element {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected Object a0; // target - non-java inheritance - SELECT documentation_object
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	protected A_string a1; // values - non-java inheritance - LIST OF STRING
	protected static jsdai.dictionary.CExplicit_attribute a1$;
	protected String a2; // element - current entity - STRING
	protected static jsdai.dictionary.CExplicit_attribute a2$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// methods for SELECT attribute: target
	public static int usedinTarget(EAnnotation type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testTarget(EAnnotation type) throws SdaiException {
		return test_instance(a0);
	}

	public EEntity getTarget(EAnnotation type) throws SdaiException { // case 1
		a0 = get_instance_select(a0);
		return (EEntity)a0;
	}

	public void setTarget(EAnnotation type, EEntity value) throws SdaiException { // case 1
		a0 = set_instance(a0, value);
	}

	public void unsetTarget(EAnnotation type) throws SdaiException {
		a0 = unset_instance(a0);
	}

	public static jsdai.dictionary.EAttribute attributeTarget(EAnnotation type) throws SdaiException {
		return a0$;
	}

	// methods for attribute: values, base type: LIST OF STRING
	public boolean testValues(EAnnotation type) throws SdaiException {
		return test_aggregate(a1);
	}
	public A_string getValues(EAnnotation type) throws SdaiException {
		return (A_string)get_aggregate(a1);
	}
	public A_string createValues(EAnnotation type) throws SdaiException {
		a1 = create_aggregate_string(a1, a1$, 0);
		return a1;
	}
	public void unsetValues(EAnnotation type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeValues(EAnnotation type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: element, base type: STRING
	public boolean testElement(EDocumentation_with_element type) throws SdaiException {
		return test_string(a2);
	}
	public String getElement(EDocumentation_with_element type) throws SdaiException {
		return get_string(a2);
	}
	public void setElement(EDocumentation_with_element type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetElement(EDocumentation_with_element type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeElement(EDocumentation_with_element type) throws SdaiException {
		return a2$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			if (a1 instanceof CAggregate)
				a1.unsetAll();
			a1 = null;
			a2 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getStringAggregate(1, a1$, this);
		a2 = av.entityValues[2].getString(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: annotation
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setStringAggregate(1, a1);
		// partial entity: documentation
		// partial entity: documentation_with_element
		av.entityValues[2].setString(0, a2);
	}
}
