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

// Java interface for entity subtype_expression

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface ESubtype_expression extends EEntity {

	// methods for attribute: generic_operands, base type: SET OF SELECT
	public boolean testGeneric_operands(ESubtype_expression type) throws SdaiException;
	public AEntity_or_view_or_subtype_expression getGeneric_operands(ESubtype_expression type) throws SdaiException;
	public AEntity_or_view_or_subtype_expression createGeneric_operands(ESubtype_expression type) throws SdaiException;
	public void unsetGeneric_operands(ESubtype_expression type) throws SdaiException;

	// methods for attribute: operands, base type: SET OF SELECT
	public boolean testOperands(ESubtype_expression type) throws SdaiException;
	public Value getOperands(ESubtype_expression type, SdaiContext _context) throws SdaiException;
	public AEntity_or_subtype_expression getOperands(ESubtype_expression type) throws SdaiException;

}
