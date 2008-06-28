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

// Java interface for entity view_partition_attribute

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public interface EView_partition_attribute extends EEntity {

	// attribute:parent_view_attribute, base type: entity view_attribute
	public boolean testParent_view_attribute(EView_partition_attribute type) throws SdaiException;
	public EView_attribute getParent_view_attribute(EView_partition_attribute type) throws SdaiException;
	public void setParent_view_attribute(EView_partition_attribute type, EView_attribute value) throws SdaiException;
	public void unsetParent_view_attribute(EView_partition_attribute type) throws SdaiException;

	// attribute:related_partition, base type: entity view_partition
	public boolean testRelated_partition(EView_partition_attribute type) throws SdaiException;
	public EView_partition getRelated_partition(EView_partition_attribute type) throws SdaiException;
	public void setRelated_partition(EView_partition_attribute type, EView_partition value) throws SdaiException;
	public void unsetRelated_partition(EView_partition_attribute type) throws SdaiException;

}
