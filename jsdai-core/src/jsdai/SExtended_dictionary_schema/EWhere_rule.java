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

// Java interface for entity where_rule

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EWhere_rule extends EEntity {

	/// methods for attribute:label, base type: STRING
	public boolean testLabel(EWhere_rule type) throws SdaiException;
	public String getLabel(EWhere_rule type) throws SdaiException;
	public void setLabel(EWhere_rule type, String value) throws SdaiException;
	public void unsetLabel(EWhere_rule type) throws SdaiException;

	// constants and methods for SELECT attribute: parent_item
	boolean testParent_item(EWhere_rule type) throws SdaiException;

	EEntity getParent_item(EWhere_rule type) throws SdaiException; // case 1

	void setParent_item(EWhere_rule type, EEntity value) throws SdaiException; // case 1

	void unsetParent_item(EWhere_rule type) throws SdaiException;

	/// methods for attribute:order, base type: INTEGER
	public boolean testOrder(EWhere_rule type) throws SdaiException;
	public int getOrder(EWhere_rule type) throws SdaiException;
	public void setOrder(EWhere_rule type, int value) throws SdaiException;
	public void unsetOrder(EWhere_rule type) throws SdaiException;

}
