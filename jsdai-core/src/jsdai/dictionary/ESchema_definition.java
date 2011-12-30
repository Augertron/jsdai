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

// Java interface for entity schema_definition

package jsdai.dictionary;
import jsdai.lang.*;

public interface ESchema_definition extends EGeneric_schema_definition {

	boolean testEntityDefinition(String entity_name) throws jsdai.lang.SdaiException;
	EEntity_definition getEntityDefinition(String entity_name) throws jsdai.lang.SdaiException;
	EDefined_type getDefinedType(String type_name) throws jsdai.lang.SdaiException;

	/// methods for attribute:name, base type: STRING
/*	public boolean testName(ESchema_definition type) throws SdaiException;
	public String getName(ESchema_definition type) throws SdaiException;
	public void setName(ESchema_definition type, String value) throws SdaiException;
	public void unsetName(ESchema_definition type) throws SdaiException;
*/

	/// methods for attribute:identification, base type: STRING
/*	public boolean testIdentification(ESchema_definition type) throws SdaiException;
	public String getIdentification(ESchema_definition type) throws SdaiException;
	public void setIdentification(ESchema_definition type, String value) throws SdaiException;
	public void unsetIdentification(ESchema_definition type) throws SdaiException;
*/

	// Inverse attribute - entity_declarations : SET[0:-2147483648] OF entity_declaration FOR parent
	public AEntity_declaration getEntity_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - type_declarations : SET[0:-2147483648] OF type_declaration FOR parent
	public AType_declaration getType_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - rule_declarations : SET[0:-2147483648] OF rule_declaration FOR parent
	public ARule_declaration getRule_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - algorithm_declarations : SET[0:-2147483648] OF algorithm_declaration FOR parent
	public AAlgorithm_declaration getAlgorithm_declarations(ESchema_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - external_schemas : SET[1:-2147483648] OF external_schema FOR native_schema
	public AExternal_schema getExternal_schemas(ESchema_definition type, ASdaiModel domain) throws SdaiException;
}
