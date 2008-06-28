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

// Java interface for entity external_schema

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EExternal_schema extends EEntity {

	// attribute:definition, base type: entity schema_definition
	public boolean testDefinition(EExternal_schema type) throws SdaiException;
	public ESchema_definition getDefinition(EExternal_schema type) throws SdaiException;
	public void setDefinition(EExternal_schema type, ESchema_definition value) throws SdaiException;
	public void unsetDefinition(EExternal_schema type) throws SdaiException;

	// attribute:native_schema, base type: entity schema_definition
	public boolean testNative_schema(EExternal_schema type) throws SdaiException;
	public ESchema_definition getNative_schema(EExternal_schema type) throws SdaiException;
	public void setNative_schema(EExternal_schema type, ESchema_definition value) throws SdaiException;
	public void unsetNative_schema(EExternal_schema type) throws SdaiException;

	// Inverse attribute - for_types : SET[1:-2147483648] OF domain_equivalent_type FOR owner
	public ADomain_equivalent_type getFor_types(EExternal_schema type, ASdaiModel domain) throws SdaiException;
}
