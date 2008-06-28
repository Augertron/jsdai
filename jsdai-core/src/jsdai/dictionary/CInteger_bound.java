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

// Java class implementing entity integer_bound

package jsdai.dictionary;
import jsdai.lang.*;

public class CInteger_bound extends CEntity implements EInteger_bound {
	static jsdai.dictionary.CEntity_definition definition;

	/*----------------------------- Attributes -----------*/

	protected int a0; // bound_value - non-java inheritance - INTEGER
	protected static jsdai.dictionary.CExplicit_attribute a0$;

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
	}

	/*----------- Methods for attribute access -----------*/

	/// methods for attribute: bound_value, base type: INTEGER
	public boolean testBound_value(EBound type) throws SdaiException {
		return test_integer(a0);
	}
	public int getBound_value(EBound type, SdaiContext context) throws SdaiException {
		return get_integer(a0);
	}
	public int getBound_value(EBound type) throws SdaiException {
		return get_integer(a0);
	}
	public void setBound_value(EBound type, int value) throws SdaiException {
		a0 = set_integer(value);
	}
	public void unsetBound_value(EBound type) throws SdaiException {
		a0 = unset_integer();
	}
	public static jsdai.dictionary.EAttribute attributeBound_value(EBound type) throws SdaiException {
		return a0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = Integer.MIN_VALUE;
			return;
		}
		a0 = av.entityValues[0].getInteger(0);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: bound
		av.entityValues[0].setInteger(0, a0);
		// partial entity: integer_bound
	}
}
