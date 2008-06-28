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

// Java interface for entity interface_specification

package jsdai.dictionary;
import jsdai.lang.*;

public interface EInterface_specification extends EEntity {

	// attribute:foreign_schema, base type: entity generic_schema_definition
	public boolean testForeign_schema(EInterface_specification type) throws SdaiException;
	public EGeneric_schema_definition getForeign_schema(EInterface_specification type) throws SdaiException;
	public void setForeign_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException;
	public void unsetForeign_schema(EInterface_specification type) throws SdaiException;

	// attribute:current_schema, base type: entity generic_schema_definition
	public boolean testCurrent_schema(EInterface_specification type) throws SdaiException;
	public EGeneric_schema_definition getCurrent_schema(EInterface_specification type) throws SdaiException;
	public void setCurrent_schema(EInterface_specification type, EGeneric_schema_definition value) throws SdaiException;
	public void unsetCurrent_schema(EInterface_specification type) throws SdaiException;

	// methods for attribute: items, base type: SET OF ENTITY
	public boolean testItems(EInterface_specification type) throws SdaiException;
	public AInterfaced_declaration getItems(EInterface_specification type) throws SdaiException;
	public AInterfaced_declaration createItems(EInterface_specification type) throws SdaiException;
	public void unsetItems(EInterface_specification type) throws SdaiException;

}
