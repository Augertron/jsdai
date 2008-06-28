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

// Java interface for entity view_attribute

package jsdai.dictionary;
import jsdai.lang.*;

public interface EView_attribute extends EAttribute {

	// constants and methods for SELECT attribute: domain
	boolean testDomain(EView_attribute type) throws SdaiException;

	jsdai.lang.EEntity getDomain(EView_attribute type) throws jsdai.lang.SdaiException; // case 1

	void setDomain(EView_attribute type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException; // case 1

	void unsetDomain(EView_attribute type) throws SdaiException;

	/// methods for attribute:optional_flag, base type: BOOLEAN
	public boolean testOptional_flag(EView_attribute type) throws SdaiException;
	public boolean getOptional_flag(EView_attribute type) throws SdaiException;
	public void setOptional_flag(EView_attribute type, boolean value) throws SdaiException;
	public void unsetOptional_flag(EView_attribute type) throws SdaiException;

}
