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

// Java interface for entity declaration

package jsdai.dictionary;
import jsdai.lang.*;

public interface EDeclaration extends EEntity {

	// attribute:parent, base type: entity generic_schema_definition
	public boolean testParent(EDeclaration type) throws SdaiException;
	public EGeneric_schema_definition getParent(EDeclaration type) throws SdaiException;
	public void setParent(EDeclaration type, EGeneric_schema_definition value) throws SdaiException;
	public void unsetParent(EDeclaration type) throws SdaiException;

	// constants and methods for SELECT attribute: definition
	boolean testDefinition(EDeclaration type) throws SdaiException;

	EEntity getDefinition(EDeclaration type) throws SdaiException; // case 1

	void setDefinition(EDeclaration type, EEntity value) throws SdaiException; // case 1

	void unsetDefinition(EDeclaration type) throws SdaiException;

	// attribute:parent_schema, base type: entity schema_definition
	public boolean testParent_schema(EDeclaration type) throws SdaiException;
	public ESchema_definition getParent_schema(EDeclaration type) throws SdaiException;

}
