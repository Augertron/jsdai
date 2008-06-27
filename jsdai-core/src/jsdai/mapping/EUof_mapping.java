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

// Java interface for entity uof_mapping

package jsdai.mapping;
import jsdai.lang.*;

public interface EUof_mapping extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EUof_mapping type) throws SdaiException;
	public String getName(EUof_mapping type) throws SdaiException;
	public void setName(EUof_mapping type, String value) throws SdaiException;
	public void unsetName(EUof_mapping type) throws SdaiException;

	// methods for attribute: mappings, base type: SET OF ENTITY
	public boolean testMappings(EUof_mapping type) throws SdaiException;
	public AEntity_mapping getMappings(EUof_mapping type) throws SdaiException;
	public AEntity_mapping createMappings(EUof_mapping type) throws SdaiException;
	public void unsetMappings(EUof_mapping type) throws SdaiException;

}
