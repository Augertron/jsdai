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

// Java interface for entity entity_mapping

package jsdai.mapping;
import jsdai.lang.*;

public interface EEntity_mapping extends EEntity {

	// attribute:source, base type: entity entity_definition
	public boolean testSource(EEntity_mapping type) throws SdaiException;
	public jsdai.dictionary.EEntity_definition getSource(EEntity_mapping type) throws SdaiException;
	public void setSource(EEntity_mapping type, jsdai.dictionary.EEntity_definition value) throws SdaiException;
	public void unsetSource(EEntity_mapping type) throws SdaiException;

	// constants and methods for SELECT attribute: target
	boolean testTarget(EEntity_mapping type) throws SdaiException;

	jsdai.lang.EEntity getTarget(EEntity_mapping type) throws jsdai.lang.SdaiException; // case 1

	void setTarget(EEntity_mapping type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException; // case 1

	void unsetTarget(EEntity_mapping type) throws SdaiException;

	// constants and methods for SELECT attribute: constraints
	boolean testConstraints(EEntity_mapping type) throws SdaiException;

	jsdai.lang.EEntity getConstraints(EEntity_mapping type) throws jsdai.lang.SdaiException; // case 1

	void setConstraints(EEntity_mapping type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException; // case 1

	void unsetConstraints(EEntity_mapping type) throws SdaiException;

	/// methods for attribute:entry_point, base type: BOOLEAN
	public boolean testEntry_point(EEntity_mapping type) throws SdaiException;
	public boolean getEntry_point(EEntity_mapping type) throws SdaiException;
	public void setEntry_point(EEntity_mapping type, boolean value) throws SdaiException;
	public void unsetEntry_point(EEntity_mapping type) throws SdaiException;

	/// methods for attribute:strong_users, base type: BOOLEAN
	public boolean testStrong_users(EEntity_mapping type) throws SdaiException;
	public boolean getStrong_users(EEntity_mapping type) throws SdaiException;
	public void setStrong_users(EEntity_mapping type, boolean value) throws SdaiException;
	public void unsetStrong_users(EEntity_mapping type) throws SdaiException;

}
