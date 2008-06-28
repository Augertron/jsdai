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

// Java interface for entity attribute

package jsdai.dictionary;
import jsdai.lang.*;

public interface EAttribute extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EAttribute type) throws SdaiException;
	public String getName(EAttribute type) throws SdaiException;
	public void setName(EAttribute type, String value) throws SdaiException;
	public void unsetName(EAttribute type) throws SdaiException;

	// attribute:parent, base type: entity entity_or_view_definition
	public boolean testParent(EAttribute type) throws SdaiException;
	public EEntity_or_view_definition getParent(EAttribute type) throws SdaiException;
	public void setParent(EAttribute type, EEntity_or_view_definition value) throws SdaiException;
	public void unsetParent(EAttribute type) throws SdaiException;

	/// methods for attribute:order, base type: INTEGER
	public boolean testOrder(EAttribute type) throws SdaiException;
	public int getOrder(EAttribute type) throws SdaiException;
	public void setOrder(EAttribute type, int value) throws SdaiException;
	public void unsetOrder(EAttribute type) throws SdaiException;

	// attribute:parent_entity, base type: entity entity_definition
	public boolean testParent_entity(EAttribute type) throws SdaiException;
	public EEntity_definition getParent_entity(EAttribute type) throws SdaiException;

}
