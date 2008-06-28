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

// Java interface for entity parameter

package jsdai.dictionary;
import jsdai.lang.*;

public interface EParameter extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EParameter type) throws SdaiException;
	public String getName(EParameter type) throws SdaiException;
	public void setName(EParameter type, String value) throws SdaiException;
	public void unsetName(EParameter type) throws SdaiException;

	// attribute:parameter_type, base type: entity data_type
	public boolean testParameter_type(EParameter type) throws SdaiException;
	public EData_type getParameter_type(EParameter type) throws SdaiException;
	public void setParameter_type(EParameter type, EData_type value) throws SdaiException;
	public void unsetParameter_type(EParameter type) throws SdaiException;

	/// methods for attribute:var_type, base type: BOOLEAN
	public boolean testVar_type(EParameter type) throws SdaiException;
	public boolean getVar_type(EParameter type) throws SdaiException;
	public void setVar_type(EParameter type, boolean value) throws SdaiException;
	public void unsetVar_type(EParameter type) throws SdaiException;

	// methods for attribute: type_labels, base type: LIST OF STRING
	public boolean testType_labels(EParameter type) throws SdaiException;
	public A_string getType_labels(EParameter type) throws SdaiException;
	public A_string createType_labels(EParameter type) throws SdaiException;
	public void unsetType_labels(EParameter type) throws SdaiException;

}
