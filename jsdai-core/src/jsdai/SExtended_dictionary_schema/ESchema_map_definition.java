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

// Java interface for entity schema_map_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface ESchema_map_definition extends EGeneric_schema_definition {

	// Inverse attribute - view_declarations : SET[0:-2147483648] OF view_declaration FOR parent
	public AView_declaration getView_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - map_declarations : SET[0:-2147483648] OF map_declaration FOR parent
	public AMap_declaration getMap_declarations(ESchema_map_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - source_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_source FOR current_schema
	public AReference_from_specification_as_source getSource_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - target_schema_specifications : SET[1:-2147483648] OF reference_from_specification_as_target FOR current_schema
	public AReference_from_specification_as_target getTarget_schema_specifications(ESchema_map_definition type, ASdaiModel domain) throws SdaiException;
}
