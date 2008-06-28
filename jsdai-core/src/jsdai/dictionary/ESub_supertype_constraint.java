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

// Java interface for entity sub_supertype_constraint

package jsdai.dictionary;
import jsdai.lang.*;

public interface ESub_supertype_constraint extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(ESub_supertype_constraint type) throws SdaiException;
	public String getName(ESub_supertype_constraint type) throws SdaiException;
	public void setName(ESub_supertype_constraint type, String value) throws SdaiException;
	public void unsetName(ESub_supertype_constraint type) throws SdaiException;

	// attribute:generic_supertype, base type: entity entity_or_view_definition
	public boolean testGeneric_supertype(ESub_supertype_constraint type) throws SdaiException;
	public EEntity_or_view_definition getGeneric_supertype(ESub_supertype_constraint type) throws SdaiException;
	public void setGeneric_supertype(ESub_supertype_constraint type, EEntity_or_view_definition value) throws SdaiException;
	public void unsetGeneric_supertype(ESub_supertype_constraint type) throws SdaiException;

	// methods for attribute: total_cover, base type: SET OF ENTITY
	public boolean testTotal_cover(ESub_supertype_constraint type) throws SdaiException;
	public AEntity_definition getTotal_cover(ESub_supertype_constraint type) throws SdaiException;
	public AEntity_definition createTotal_cover(ESub_supertype_constraint type) throws SdaiException;
	public void unsetTotal_cover(ESub_supertype_constraint type) throws SdaiException;

	// attribute:constraint, base type: entity subtype_expression
	public boolean testConstraint(ESub_supertype_constraint type) throws SdaiException;
	public ESubtype_expression getConstraint(ESub_supertype_constraint type) throws SdaiException;
	public void setConstraint(ESub_supertype_constraint type, ESubtype_expression value) throws SdaiException;
	public void unsetConstraint(ESub_supertype_constraint type) throws SdaiException;

	/// methods for attribute:abstract_supertype, base type: BOOLEAN
	public boolean testAbstract_supertype(ESub_supertype_constraint type) throws SdaiException;
	public boolean getAbstract_supertype(ESub_supertype_constraint type) throws SdaiException;
	public void setAbstract_supertype(ESub_supertype_constraint type, boolean value) throws SdaiException;
	public void unsetAbstract_supertype(ESub_supertype_constraint type) throws SdaiException;

	// attribute:super_type, base type: entity entity_definition
	public boolean testSuper_type(ESub_supertype_constraint type) throws SdaiException;
	public EEntity_definition getSuper_type(ESub_supertype_constraint type) throws SdaiException;

}
