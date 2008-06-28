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

// Java interface for entity generic_schema_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EGeneric_schema_definition extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EGeneric_schema_definition type) throws SdaiException;
	public String getName(EGeneric_schema_definition type) throws SdaiException;
	public void setName(EGeneric_schema_definition type, String value) throws SdaiException;
	public void unsetName(EGeneric_schema_definition type) throws SdaiException;

	/// methods for attribute:identification, base type: STRING
	public boolean testIdentification(EGeneric_schema_definition type) throws SdaiException;
	public String getIdentification(EGeneric_schema_definition type) throws SdaiException;
	public void setIdentification(EGeneric_schema_definition type, String value) throws SdaiException;
	public void unsetIdentification(EGeneric_schema_definition type) throws SdaiException;

}
