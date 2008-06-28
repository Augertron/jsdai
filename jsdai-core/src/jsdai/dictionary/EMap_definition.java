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

// Java interface for entity map_definition

package jsdai.dictionary;
import jsdai.lang.*;

public interface EMap_definition extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EMap_definition type) throws SdaiException;
	public String getName(EMap_definition type) throws SdaiException;
	public void setName(EMap_definition type, String value) throws SdaiException;
	public void unsetName(EMap_definition type) throws SdaiException;

	// attribute:super_type, base type: entity map_definition
	public boolean testSuper_type(EMap_definition type) throws SdaiException;
	public EMap_definition getSuper_type(EMap_definition type) throws SdaiException;
	public void setSuper_type(EMap_definition type, EMap_definition value) throws SdaiException;
	public void unsetSuper_type(EMap_definition type) throws SdaiException;

	// Inverse attribute - partitions : SET[1:-2147483648] OF map_partition FOR parent
	public AMap_partition getPartitions(EMap_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - target_parameters : SET[0:-2147483648] OF target_parameter FOR parent
	public ATarget_parameter getTarget_parameters(EMap_definition type, ASdaiModel domain) throws SdaiException;
}
