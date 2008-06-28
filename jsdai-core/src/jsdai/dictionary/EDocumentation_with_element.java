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

// Java interface for entity documentation_with_element

package jsdai.dictionary;
import jsdai.lang.*;

public interface EDocumentation_with_element extends EDocumentation {

	/// methods for attribute:element, base type: STRING
	public boolean testElement(EDocumentation_with_element type) throws SdaiException;
	public String getElement(EDocumentation_with_element type) throws SdaiException;
	public void setElement(EDocumentation_with_element type, String value) throws SdaiException;
	public void unsetElement(EDocumentation_with_element type) throws SdaiException;

}
