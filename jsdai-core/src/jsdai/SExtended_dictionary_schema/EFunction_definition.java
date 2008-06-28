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

// Java interface for entity function_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EFunction_definition extends EAlgorithm_definition {

	// attribute:return_type, base type: entity data_type
	public boolean testReturn_type(EFunction_definition type) throws SdaiException;
	public EData_type getReturn_type(EFunction_definition type) throws SdaiException;
	public void setReturn_type(EFunction_definition type, EData_type value) throws SdaiException;
	public void unsetReturn_type(EFunction_definition type) throws SdaiException;

	/// methods for attribute:return_type_label, base type: STRING
	public boolean testReturn_type_label(EFunction_definition type) throws SdaiException;
	public String getReturn_type_label(EFunction_definition type) throws SdaiException;
	public void setReturn_type_label(EFunction_definition type, String value) throws SdaiException;
	public void unsetReturn_type_label(EFunction_definition type) throws SdaiException;

	// methods for attribute: return_type_labels, base type: LIST OF STRING
	public boolean testReturn_type_labels(EFunction_definition type) throws SdaiException;
	public A_string getReturn_type_labels(EFunction_definition type) throws SdaiException;
	public A_string createReturn_type_labels(EFunction_definition type) throws SdaiException;
	public void unsetReturn_type_labels(EFunction_definition type) throws SdaiException;

}
