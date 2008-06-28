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

package jsdai.dictionary;
import jsdai.lang.*;

public class COneof_subtype_expression extends CEntity implements EOneof_subtype_expression {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected AEntity_or_view_or_subtype_expression a0; // generic_operands - non-java inheritance - SET OF SELECT
	protected static jsdai.dictionary.CExplicit_attribute a0$;
	// operands: protected AEntity_or_subtype_expression  - derived - non-java inheritance -  SET OF SELECT
	protected static jsdai.dictionary.CDerived_attribute d0$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		changeReferencesAggregate(a0, old, newer);
	}

	/*----------- Methods for attribute access -----------*/

	// methods for attribute: generic_operands, base type: SET OF SELECT
	public static int usedinGeneric_operands(ESubtype_expression type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testGeneric_operands(ESubtype_expression type) throws SdaiException {
		return test_aggregate(a0);
	}
	public AEntity_or_view_or_subtype_expression getGeneric_operands(ESubtype_expression type) throws SdaiException {
		if (a0 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a0;
	}
	public AEntity_or_view_or_subtype_expression createGeneric_operands(ESubtype_expression type) throws SdaiException {
		a0 = (AEntity_or_view_or_subtype_expression)create_aggregate_class(a0, a0$, AEntity_or_view_or_subtype_expression.class, 0);
		return a0;
	}
	public void unsetGeneric_operands(ESubtype_expression type) throws SdaiException {
		unset_aggregate(a0);
		a0 = null;
	}
	public static jsdai.dictionary.EAttribute attributeGeneric_operands(ESubtype_expression type) throws SdaiException {
		return a0$;
	}

	// methods for attribute: operands, base type: SET OF SELECT
	public static int usedinOperands(ESubtype_expression type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testOperands(ESubtype_expression type) throws SdaiException {
		return test_aggregate(a0);
	}
	public AEntity_or_subtype_expression getOperands(ESubtype_expression type) throws SdaiException {
		return getOperandsInternal((AEntity_or_view_or_subtype_expression)a0);
	}
	public static jsdai.dictionary.EAttribute attributeOperands(ESubtype_expression type) throws SdaiException {
		return d0$;
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
