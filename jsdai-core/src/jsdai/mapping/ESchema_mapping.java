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

// Java interface for entity schema_mapping

package jsdai.mapping;
import jsdai.lang.*;

public interface ESchema_mapping extends EEntity {

	// attribute:source, base type: entity schema_definition
	public boolean testSource(ESchema_mapping type) throws SdaiException;
	public jsdai.dictionary.ESchema_definition getSource(ESchema_mapping type) throws SdaiException;
	public void setSource(ESchema_mapping type, jsdai.dictionary.ESchema_definition value) throws SdaiException;
	public void unsetSource(ESchema_mapping type) throws SdaiException;

	// attribute:target, base type: entity schema_definition
	public boolean testTarget(ESchema_mapping type) throws SdaiException;
	public jsdai.dictionary.ESchema_definition getTarget(ESchema_mapping type) throws SdaiException;
	public void setTarget(ESchema_mapping type, jsdai.dictionary.ESchema_definition value) throws SdaiException;
	public void unsetTarget(ESchema_mapping type) throws SdaiException;

	// methods for attribute: uofs, base type: SET OF ENTITY
	public boolean testUofs(ESchema_mapping type) throws SdaiException;
	public AUof_mapping getUofs(ESchema_mapping type) throws SdaiException;
	public AUof_mapping createUofs(ESchema_mapping type) throws SdaiException;
	public void unsetUofs(ESchema_mapping type) throws SdaiException;

	/// methods for attribute:id, base type: STRING
	public boolean testId(ESchema_mapping type) throws SdaiException;
	public String getId(ESchema_mapping type) throws SdaiException;
	public void setId(ESchema_mapping type, String value) throws SdaiException;
	public void unsetId(ESchema_mapping type) throws SdaiException;

	// methods for attribute: components, base type: SET OF ENTITY
	public boolean testComponents(ESchema_mapping type) throws SdaiException;
	public ASchema_mapping getComponents(ESchema_mapping type) throws SdaiException;
	public ASchema_mapping createComponents(ESchema_mapping type) throws SdaiException;
	public void unsetComponents(ESchema_mapping type) throws SdaiException;

}
