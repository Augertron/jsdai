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

package jsdai.SClassification_with_attributes_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGroup_schema.CGroup_relationship;
import jsdai.SGroup_schema.EGroup_relationship;

public class CxClass_hierarchy extends CClass_hierarchy implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Group_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EGroup_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EGroup_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EGroup_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EGroup_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGroup_relationship type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
	public boolean testDescription(EGroup_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EGroup_relationship type) throws SdaiException {
		return get_string(a1);
	}
	public void setDescription(EGroup_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EGroup_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EGroup_relationship type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Group_relationship

	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CGroup_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	group_relationship
	*  {group_relationship.name = 'class hierarchy'}
	*  [group_relationship.related_group -&gt;]
	*  [group_relationship.relating_group -&gt;]
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClass_hierarchy armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "class hierarchy");
	}

	public static void unsetMappingConstraints(SdaiContext context, EClass_hierarchy armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
}
