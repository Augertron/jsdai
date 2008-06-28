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

// Java class implementing entity schema_view_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CSchema_view_definition extends CGeneric_schema_definition implements ESchema_view_definition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CSchema_view_definition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// name: protected String a0;   name - java inheritance - STRING
	// identification: protected String a1;   identification - java inheritance - STRING
	// view_declarations: protected Object  - inverse - current -  ENTITY view_declaration
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// Inverse attribute - view_declarations : SET[0:-2147483648] OF view_declaration FOR parent
	public AView_declaration getView_declarations(ESchema_view_definition type, ASdaiModel domain) throws SdaiException {
		AView_declaration result = (AView_declaration)get_inverse_aggregate(i0$);
		CView_declaration.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeView_declarations(ESchema_view_definition type) throws SdaiException {
		return i0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = null;
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getString(0);
		a1 = av.entityValues[0].getString(1);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: generic_schema_definition
		av.entityValues[0].setString(0, a0);
		av.entityValues[0].setString(1, a1);
		// partial entity: schema_view_definition
	}
}
