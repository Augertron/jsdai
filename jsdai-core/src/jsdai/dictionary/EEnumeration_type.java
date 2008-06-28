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

// Java interface for entity enumeration_type

package jsdai.dictionary;
import jsdai.lang.*;

public interface EEnumeration_type extends EData_type {

	// methods for attribute: local_elements, base type: LIST OF STRING
	public boolean testLocal_elements(EEnumeration_type type) throws SdaiException;
	public A_string getLocal_elements(EEnumeration_type type) throws SdaiException;
	public A_string createLocal_elements(EEnumeration_type type) throws SdaiException;
	public void unsetLocal_elements(EEnumeration_type type) throws SdaiException;

	// methods for attribute: elements, base type: LIST OF STRING
	public boolean testElements(EEnumeration_type type) throws SdaiException;
	public A_string getElements(EEnumeration_type type) throws SdaiException;
	public A_string getElements(EEnumeration_type type, SdaiContext _context) throws SdaiException;

}
