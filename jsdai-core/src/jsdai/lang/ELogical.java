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

/**
 * The ELogical class represents the EXPRESS data type LOGICAL and defines
 * its values FALSE, TRUE and UNKNOWN for its usage in Java similarly like
 * it is done for ENUMERATION. This class is used only statically.
 * There exist no instance of it.
 */
public final class ELogical {

/**
 * The value 1 that stands for the logical value "false".
 */
	public static final int FALSE = 1;

/**
 * The value 2 that stands for the logical value "true".
 */
	public static final int TRUE = 2;

/**
 * The value 3 that stands for the logical value "unknown".
 */
	public static final int UNKNOWN = 3;

/**
 * The array of <code>String</code>s {"FALSE", "TRUE", "UNKNOWN"} representing 
 * logical values "false", "true" and "unknown".
 */
	public static final String values[] = {"FALSE", "TRUE", "UNKNOWN"};

//-------- methods ---------
	private ELogical() {
	}


/**
 * Converts the <code>String</code> representation of the value of the logical type
 * to the corresponding integer constant.
 * If the string passed through the method's parameter is different from
 * each possible string representation of a logical value,
 * then zero having a meaning "unset value" is returned;
 * otherwise, method returns some integer from the interval [1,3].
 * @param str a string specifying the logical value.
 * @return  an integer representing the logical value.
 * @see #toString
 */
	public static int toInt(String str) {
//		synchronized (SdaiCommon.syncObject) {
		for (int i = 0; i < 3; i++) {
			if (values[i].equalsIgnoreCase(str)) return i + 1;
		}
		return 0;
//		} // syncObject
	}


/**
 * Converts the <code>int</code> representation of the value of the logical type
 * to the corresponding <code>String</code> constant.
 * If the integer passed through the method's parameter belongs to the
 * interval [1,3], then string counterpart of the logical value is returned;
 * otherwise, method returns string constant "unset".
 * @param value an integer specifying the logical value.
 * @return  a string representation of the logical value.
 * @see #toInt
 */
	public static String toString(int value) {
//		synchronized (SdaiCommon.syncObject) {
		if (value <= 0 || value > 3) return "unset";
		return values[value - 1];
//		} // syncObject
	}
}
