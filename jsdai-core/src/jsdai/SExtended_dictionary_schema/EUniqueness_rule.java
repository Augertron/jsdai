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

// Java interface for entity uniqueness_rule

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EUniqueness_rule extends EEntity {

	/// methods for attribute:label, base type: STRING
	public boolean testLabel(EUniqueness_rule type) throws SdaiException;
	public String getLabel(EUniqueness_rule type) throws SdaiException;
	public void setLabel(EUniqueness_rule type, String value) throws SdaiException;
	public void unsetLabel(EUniqueness_rule type) throws SdaiException;

	// methods for attribute: attributes, base type: LIST OF ENTITY
	public boolean testAttributes(EUniqueness_rule type) throws SdaiException;
	public AAttribute getAttributes(EUniqueness_rule type) throws SdaiException;
	public AAttribute createAttributes(EUniqueness_rule type) throws SdaiException;
	public void unsetAttributes(EUniqueness_rule type) throws SdaiException;

	// attribute:parent_entity, base type: entity entity_definition
	public boolean testParent_entity(EUniqueness_rule type) throws SdaiException;
	public EEntity_definition getParent_entity(EUniqueness_rule type) throws SdaiException;
	public void setParent_entity(EUniqueness_rule type, EEntity_definition value) throws SdaiException;
	public void unsetParent_entity(EUniqueness_rule type) throws SdaiException;

}
