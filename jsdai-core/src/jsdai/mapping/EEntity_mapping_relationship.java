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

// Java interface for entity entity_mapping_relationship

package jsdai.mapping;
import jsdai.lang.*;

public interface EEntity_mapping_relationship extends EEntity {

	// attribute:relating, base type: entity entity_mapping
	public boolean testRelating(EEntity_mapping_relationship type) throws SdaiException;
	public EEntity_mapping getRelating(EEntity_mapping_relationship type) throws SdaiException;
	public void setRelating(EEntity_mapping_relationship type, EEntity_mapping value) throws SdaiException;
	public void unsetRelating(EEntity_mapping_relationship type) throws SdaiException;

	// attribute:related, base type: entity entity_mapping
	public boolean testRelated(EEntity_mapping_relationship type) throws SdaiException;
	public EEntity_mapping getRelated(EEntity_mapping_relationship type) throws SdaiException;
	public void setRelated(EEntity_mapping_relationship type, EEntity_mapping value) throws SdaiException;
	public void unsetRelated(EEntity_mapping_relationship type) throws SdaiException;

	// constants and methods for SELECT attribute: constraints
	boolean testConstraints(EEntity_mapping_relationship type) throws SdaiException;

	jsdai.lang.EEntity getConstraints(EEntity_mapping_relationship type) throws jsdai.lang.SdaiException; // case 1

	void setConstraints(EEntity_mapping_relationship type, jsdai.lang.EEntity value) throws jsdai.lang.SdaiException; // case 1

	void unsetConstraints(EEntity_mapping_relationship type) throws SdaiException;

	// methods for attribute: path, base type: LIST OF SELECT
	public boolean testPath(EEntity_mapping_relationship type) throws SdaiException;
	public AAttribute_mapping_path_select getPath(EEntity_mapping_relationship type) throws SdaiException;
	public AAttribute_mapping_path_select createPath(EEntity_mapping_relationship type) throws SdaiException;
	public void unsetPath(EEntity_mapping_relationship type) throws SdaiException;

}
