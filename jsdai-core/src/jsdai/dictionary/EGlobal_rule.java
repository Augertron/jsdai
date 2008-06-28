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

// Java interface for entity global_rule

package jsdai.dictionary;
import jsdai.lang.*;

public interface EGlobal_rule extends EEntity {

	/// methods for attribute:name, base type: STRING
	public boolean testName(EGlobal_rule type) throws SdaiException;
	public String getName(EGlobal_rule type) throws SdaiException;
	public void setName(EGlobal_rule type, String value) throws SdaiException;
	public void unsetName(EGlobal_rule type) throws SdaiException;

	// methods for attribute: entities, base type: LIST OF ENTITY
	public boolean testEntities(EGlobal_rule type) throws SdaiException;
	public AEntity_definition getEntities(EGlobal_rule type) throws SdaiException;
	public AEntity_definition createEntities(EGlobal_rule type) throws SdaiException;
	public void unsetEntities(EGlobal_rule type) throws SdaiException;

	// Inverse attribute - where_rules : SET[1:-2147483648] OF where_rule FOR parent_item
	public AWhere_rule getWhere_rules(EGlobal_rule type, ASdaiModel domain) throws SdaiException;
}
