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

// Java interface for entity annotation

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EAnnotation extends EEntity {

	// constants and methods for SELECT attribute: target
	boolean testTarget(EAnnotation type) throws SdaiException;

	EEntity getTarget(EAnnotation type) throws SdaiException; // case 1

	void setTarget(EAnnotation type, EEntity value) throws SdaiException; // case 1

	void unsetTarget(EAnnotation type) throws SdaiException;

	// methods for attribute: values, base type: LIST OF STRING
	public boolean testValues(EAnnotation type) throws SdaiException;
	public A_string getValues(EAnnotation type) throws SdaiException;
	public A_string createValues(EAnnotation type) throws SdaiException;
	public void unsetValues(EAnnotation type) throws SdaiException;

}
