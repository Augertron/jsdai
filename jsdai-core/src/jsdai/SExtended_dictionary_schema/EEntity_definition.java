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

// Java interface for entity entity_definition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EEntity_definition extends EEntity_or_view_definition {

	/// methods for attribute:complex, base type: BOOLEAN
	public boolean testComplex(EEntity_definition type) throws SdaiException;
	public boolean getComplex(EEntity_definition type) throws SdaiException;
	public void setComplex(EEntity_definition type, boolean value) throws SdaiException;
	public void unsetComplex(EEntity_definition type) throws SdaiException;

	/// methods for attribute:instantiable, base type: BOOLEAN
	public boolean testInstantiable(EEntity_definition type) throws SdaiException;
	public boolean getInstantiable(EEntity_definition type) throws SdaiException;
	public void setInstantiable(EEntity_definition type, boolean value) throws SdaiException;
	public void unsetInstantiable(EEntity_definition type) throws SdaiException;

	/// methods for attribute:independent, base type: BOOLEAN
	public boolean testIndependent(EEntity_definition type) throws SdaiException;
	public boolean getIndependent(EEntity_definition type) throws SdaiException;
	public void setIndependent(EEntity_definition type, boolean value) throws SdaiException;
	public void unsetIndependent(EEntity_definition type) throws SdaiException;

	/// methods for attribute:abstract_entity, base type: BOOLEAN
	public boolean testAbstract_entity(EEntity_definition type) throws SdaiException;
	public boolean getAbstract_entity(EEntity_definition type) throws SdaiException;
	public void setAbstract_entity(EEntity_definition type, boolean value) throws SdaiException;
	public void unsetAbstract_entity(EEntity_definition type) throws SdaiException;

	/// methods for attribute:connotational_subtype, base type: BOOLEAN
	public boolean testConnotational_subtype(EEntity_definition type) throws SdaiException;
	public boolean getConnotational_subtype(EEntity_definition type) throws SdaiException;
	public void setConnotational_subtype(EEntity_definition type, boolean value) throws SdaiException;
	public void unsetConnotational_subtype(EEntity_definition type) throws SdaiException;

	// methods for attribute: supertypes, base type: LIST OF ENTITY
	public boolean testSupertypes(EEntity_definition type) throws SdaiException;
	public Value getSupertypes(EEntity_definition type, SdaiContext _context) throws SdaiException;
	public AEntity_definition getSupertypes(EEntity_definition type) throws SdaiException;

	// Inverse attribute - attributes : SET[0:-2147483648] OF attribute FOR parent
	public AAttribute getAttributes(EEntity_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - uniqueness_rules : SET[0:-2147483648] OF uniqueness_rule FOR parent_entity
	public AUniqueness_rule getUniqueness_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException;
	// Inverse attribute - global_rules : SET[0:-2147483648] OF global_rule FOR entities
	public AGlobal_rule getGlobal_rules(EEntity_definition type, ASdaiModel domain) throws SdaiException;
}
