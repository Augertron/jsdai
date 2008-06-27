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

// Java interface for entity attribute_mapping_int_value

package jsdai.mapping;
import jsdai.lang.*;

public interface EAttribute_mapping_int_value extends EAttribute_mapping_value {

	/// methods for attribute:mapped_value, base type: INTEGER
	public boolean testMapped_value(EAttribute_mapping_int_value type) throws SdaiException;
	public int getMapped_value(EAttribute_mapping_int_value type) throws SdaiException;
	public void setMapped_value(EAttribute_mapping_int_value type, int value) throws SdaiException;
	public void unsetMapped_value(EAttribute_mapping_int_value type) throws SdaiException;

}
