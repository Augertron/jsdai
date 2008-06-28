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

// Java interface for entity target_parameter

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface ETarget_parameter extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(ETarget_parameter type) throws SdaiException;
	public String getName(ETarget_parameter type) throws SdaiException;
	public void setName(ETarget_parameter type, String value) throws SdaiException;
	public void unsetName(ETarget_parameter type) throws SdaiException;

	// attribute:parent, base type: entity map_definition
	public boolean testParent(ETarget_parameter type) throws SdaiException;
	public EMap_definition getParent(ETarget_parameter type) throws SdaiException;
	public void setParent(ETarget_parameter type, EMap_definition value) throws SdaiException;
	public void unsetParent(ETarget_parameter type) throws SdaiException;

	// attribute:extent, base type: entity entity_definition
	public boolean testExtent(ETarget_parameter type) throws SdaiException;
	public EEntity_definition getExtent(ETarget_parameter type) throws SdaiException;
	public void setExtent(ETarget_parameter type, EEntity_definition value) throws SdaiException;
	public void unsetExtent(ETarget_parameter type) throws SdaiException;

	/// methods for attribute:order, base type: INTEGER
	public boolean testOrder(ETarget_parameter type) throws SdaiException;
	public int getOrder(ETarget_parameter type) throws SdaiException;
	public void setOrder(ETarget_parameter type, int value) throws SdaiException;
	public void unsetOrder(ETarget_parameter type) throws SdaiException;

}
