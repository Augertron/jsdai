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

package jsdai.express_g.common;

/**
 * @author Mantas Balnys
 *
 */
public final class StaticTools {

	/**
	 * 
	 */
	private StaticTools() {
	}
	
	/**
	 * compares two strings, can be null, if one string is null returns false, 
	 * if both strings are null returns true  
	 * @param s1 first comparable String
	 * @param s2 second comparable String
	 * @return rezult of comparison
	 */
	public static boolean equalStrings(String s1, String s2) {
		boolean eq;
		if (s1 == null) {
			if (s2 == null)
				eq = true;
			else
				eq = false;
		} else
		if (s2 == null) {
			eq = false;
		} else {
			eq = s1.compareTo(s2) == 0;
		}		
		return eq;
	}
	
	/**
	 * compares two strings ignoring case, can be null, if one string is null returns false, 
	 * if both strings are null returns true  
	 * @param s1 first comparable String
	 * @param s2 second comparable String
	 * @return rezult of comparison
	 */
	public static boolean equalStringsNoCase(String s1, String s2) {
		boolean eq;
		if (s1 == null) {
			if (s2 == null)
				eq = true;
			else
				eq = false;
		} else
		if (s2 == null) {
			eq = false;
		} else {
			eq = s1.compareToIgnoreCase(s2) == 0;
		}		
		return eq;
	}

}
