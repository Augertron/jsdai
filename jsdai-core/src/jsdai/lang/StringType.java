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

package jsdai.lang;

import jsdai.dictionary.*;

/**
 * This class is a supertype of <code>CString_type</code> contained in the 
 * <code>jsdai.dictionary</code> package.
 * It was designed primarily to check restrictions imposed on the size of string values.
 * The class is for internal JSDAI use only.
 * @since 3.6.0
 */
public abstract class StringType extends DataType implements EString_type {

	protected StringType() { }


	boolean check_width(String value) throws SdaiException {
		CString_type str_type = (CString_type)this;
		if (str_type.testWidth(null)) {
			EBound bound = str_type.getWidth(null);
			int sym_bound = bound.getBound_value(null);
			int sym_count = value.length();
			if (str_type.getFixed_width(null)) {
				if (sym_count == sym_bound) {
					return true;
				} else {
					return false;
				}
			} else {
				if (sym_count <= sym_bound) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}


}
