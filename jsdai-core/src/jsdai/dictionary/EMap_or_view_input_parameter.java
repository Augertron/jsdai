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

// Java interface for entity map_or_view_input_parameter

package jsdai.dictionary;
import jsdai.lang.*;

public interface EMap_or_view_input_parameter extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EMap_or_view_input_parameter type) throws SdaiException;
	public String getName(EMap_or_view_input_parameter type) throws SdaiException;
	public void setName(EMap_or_view_input_parameter type, String value) throws SdaiException;
	public void unsetName(EMap_or_view_input_parameter type) throws SdaiException;

	// attribute:parent, base type: entity map_or_view_partition
	public boolean testParent(EMap_or_view_input_parameter type) throws SdaiException;
	public EMap_or_view_partition getParent(EMap_or_view_input_parameter type) throws SdaiException;
	public void setParent(EMap_or_view_input_parameter type, EMap_or_view_partition value) throws SdaiException;
	public void unsetParent(EMap_or_view_input_parameter type) throws SdaiException;

	// attribute:extent, base type: entity data_type
	public boolean testExtent(EMap_or_view_input_parameter type) throws SdaiException;
	public EData_type getExtent(EMap_or_view_input_parameter type) throws SdaiException;
	public void setExtent(EMap_or_view_input_parameter type, EData_type value) throws SdaiException;
	public void unsetExtent(EMap_or_view_input_parameter type) throws SdaiException;

	/// methods for attribute:order, base type: INTEGER
	public boolean testOrder(EMap_or_view_input_parameter type) throws SdaiException;
	public int getOrder(EMap_or_view_input_parameter type) throws SdaiException;
	public void setOrder(EMap_or_view_input_parameter type, int value) throws SdaiException;
	public void unsetOrder(EMap_or_view_input_parameter type) throws SdaiException;

}
