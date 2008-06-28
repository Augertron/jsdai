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

// Java class implementing entity oneof_subtype_expression

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class COneof_subtype_expression extends CSubtype_expression implements EOneof_subtype_expression {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(COneof_subtype_expression.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	// generic_operands: protected AEntity_or_view_or_subtype_expression a0;   generic_operands - java inheritance - SET OF SELECT
	// operands: protected AEntity_or_subtype_expression  - derived - java inheritance -  SET OF SELECT
	protected static final jsdai.dictionary.CDerived_attribute d0$ = CEntity.initDerivedAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		super.changeReferences(old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_operands, base type: SET OF SELECT
	public static int usedinGeneric_operands(ESubtype_expression type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}

	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			if (a0 instanceof CAggregate)
				a0.unsetAll();
			a0 = null;
			return;
		}
		a0 = (AEntity_or_view_or_subtype_expression)av.entityValues[1].getInstanceAggregate(0, a0$, this);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: oneof_subtype_expression
		// partial entity: subtype_expression
		av.entityValues[1].setInstanceAggregate(0, a0);
	}
}
