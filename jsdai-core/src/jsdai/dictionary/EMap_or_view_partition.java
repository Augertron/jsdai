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

// Java interface for entity map_or_view_partition

package jsdai.dictionary;
import jsdai.lang.*;

public interface EMap_or_view_partition extends EEntity {

	// constants and methods for SELECT attribute: parent
	boolean testParent(EMap_or_view_partition type) throws SdaiException;

	jsdai.lang.EEntity getParent(EMap_or_view_partition type) throws jsdai.lang.SdaiException; // case 1

	void setParent(EMap_or_view_partition type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException; // case 1

	void unsetParent(EMap_or_view_partition type) throws SdaiException;

	/// methods for attribute:name, base type: STRING
	public boolean testName(EMap_or_view_partition type) throws SdaiException;
	public String getName(EMap_or_view_partition type) throws SdaiException;
	public void setName(EMap_or_view_partition type, String value) throws SdaiException;
	public void unsetName(EMap_or_view_partition type) throws SdaiException;

	// Inverse attribute - source_parameters : SET[0:-2147483648] OF source_parameter FOR parent
	public ASource_parameter getSource_parameters(EMap_or_view_partition type, ASdaiModel domain) throws SdaiException;
}
