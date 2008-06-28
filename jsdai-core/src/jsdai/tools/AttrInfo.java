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

package jsdai.tools;

import java.io.*;
import java.util.*;
import jsdai.lang.*;
import jsdai.util.*;
import jsdai.SMapping_schema.*;
import jsdai.SExtended_dictionary_schema.*;

public class AttrInfo {
/*	public static final int	SINGLE_AIM 		= 1;
	public static final int MULTIPLE_AIM 	= 2;
	public static final int PATH 			= 3;
	public static final int IDENTICAL 		= 4;
*/
	
	protected String armEntityName = "";
	protected String aimTypeName = "";
	protected String attributeName = "";
	protected String isOptional = "";
	protected boolean isSelectType = false;
	
	protected String aimElementColumn = "";
	
	protected Object refPath = new String();
	protected int	 refPathType = -1;
	
	public AttrInfo() {}
	
	public boolean isSelectTypeInside() {
		return isSelectType;
	}
	
	public void setIsSelectType() {
		isSelectType = true;
	}
	
	public void setIsOptional() {
		isOptional = "OPTIONAL "; // this space is required to form 'nice' output.
	}
	
	public void setArmEntityName(String s) {
		armEntityName = s;
	}
	
	public void setAimTypeName(String s) {
		aimTypeName = s;
	}
	
	public void setAttrName(String s) {
		attributeName = s;
	}
	
	public void setAimColumn(String s) {
		aimElementColumn = s;
	}
	
	public void setRefPath(Object o) {
		refPath = o;
	}
	
	public String getArmColumn() throws IOException {
/*		if ((armEntityName.length() == 0) && (aimTypeName.length() == 0)) {
			if (attributeName.length() == 0)
				throw new IOException("attribute name is not specified!");
//			return "<B>"+attributeName+":</B> "+ isOptional;
		} 
		else if ((armEntityName.length() == 0) || (aimTypeName.length() == 0) || 
					(attributeName.length() == 0)) {
						throw new IOException("armEntityName or aimTypeName or attribute name"+
											" are empty! This is not allowed here.");
		}
//		return armEntityName + " to " + aimTypeName + "\n(as " + 
//					attributeName + ")";
*/
		// do not print word 'to' if aimElement column is empty: this
		// makes output better for select_type attributes:
		String result = "";
		if (aimElementColumn.length() == 0)
			result = "<B>"+attributeName+":</B> "+isOptional+ "<B>"+
							 aimTypeName+"</B>";
		else
			result = "<B>"+attributeName+":</B> "+isOptional+ "<B>"+
							 aimTypeName+"</B> (to "+aimElementColumn+")";
		return result;
	}
	
	public String getAimColumn() {
		return aimElementColumn;
	}
	
	public String getRefPath() {
		return refPath.toString();
	}
	
	public boolean shortRefPath() {
		// implementation of this method is expected to be more complex,
		// when refpath object will be designed..
		if (refPath.toString().length() == 0)
			return true;
		else
			return false;
	}
}