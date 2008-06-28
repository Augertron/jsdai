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

// Java interface for entity array_type

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EArray_type extends EAggregation_type {

	// attribute:lower_index, base type: entity bound
	public boolean testLower_index(EArray_type type) throws SdaiException;
	public EBound getLower_index(EArray_type type) throws SdaiException;
	public void setLower_index(EArray_type type, EBound value) throws SdaiException;
	public void unsetLower_index(EArray_type type) throws SdaiException;

	// attribute:upper_index, base type: entity bound
	public boolean testUpper_index(EArray_type type) throws SdaiException;
	public EBound getUpper_index(EArray_type type) throws SdaiException;
	public void setUpper_index(EArray_type type, EBound value) throws SdaiException;
	public void unsetUpper_index(EArray_type type) throws SdaiException;

	/// methods for attribute:unique_flag, base type: BOOLEAN
	public boolean testUnique_flag(EArray_type type) throws SdaiException;
	public boolean getUnique_flag(EArray_type type) throws SdaiException;
	public void setUnique_flag(EArray_type type, boolean value) throws SdaiException;
	public void unsetUnique_flag(EArray_type type) throws SdaiException;

	/// methods for attribute:optional_flag, base type: BOOLEAN
	public boolean testOptional_flag(EArray_type type) throws SdaiException;
	public boolean getOptional_flag(EArray_type type) throws SdaiException;
	public void setOptional_flag(EArray_type type, boolean value) throws SdaiException;
	public void unsetOptional_flag(EArray_type type) throws SdaiException;

}
