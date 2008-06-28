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

package jsdai.SShape_composition_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_composition_xim.CxShape_element_constituent_relationship;
import jsdai.SShape_property_assignment_xim.EShape_element_relationship;

public class CxShape_element_composing_relationship extends CShape_element_composing_relationship implements EMappedXIMEntity{

	EEntity getAimInstance(){
		return this;
	}
	
	// FROM CShape_element_relationship.java
	/// methods for attribute: relation_type, base type: STRING
/*	public boolean testRelation_type(EShape_element_relationship type) throws SdaiException {
		return testName((jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)null);
	}
	public String getRelation_type(EShape_element_relationship type) throws SdaiException {
		return getName((jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)null);
	}*/
	public void setRelation_type(EShape_element_relationship type, String value) throws SdaiException {
		setName((jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)null, value);
	}
	public void unsetRelation_type(EShape_element_relationship type) throws SdaiException {
		unsetName((jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)null);
	}
	public static jsdai.dictionary.EAttribute attributeRelation_type(EShape_element_relationship type) throws SdaiException {
		return attributeName((jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)null);
	}
	// Taken from Shape_aspect_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect_relationship type) throws SdaiException {
		return a0$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_aspect_relationship.definition);

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
	 *  {shape_aspect_relationship
	 *  (shape_aspect_relationship.name = 'bare die terminal surface constituent relationship')
	 *  (shape_aspect_relationship.name = 'composing')
	 *  (shape_aspect_relationship.name = 'constituent')
	 *  (shape_aspect_relationship.name = 'interconnect module terminal surface constituent relationship')
	 *  (shape_aspect_relationship.name = 'package terminal surface constituent relationship')}
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EShape_element_composing_relationship armEntity) throws SdaiException {
		CxShape_element_constituent_relationship.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "composing");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EShape_element_composing_relationship armEntity) throws SdaiException {
		CxShape_element_constituent_relationship.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}
	
}