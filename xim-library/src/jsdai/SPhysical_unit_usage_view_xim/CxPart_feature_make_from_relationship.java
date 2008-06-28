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

package jsdai.SPhysical_unit_usage_view_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SPhysical_unit_usage_view_mim.CMake_from_part_feature_relationship;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element_relationship;

public class CxPart_feature_make_from_relationship extends CPart_feature_make_from_relationship implements EMappedXIMEntity{

	EEntity getAimInstance(){
		return this;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMake_from_part_feature_relationship.definition);

		setMappingConstraints(context, this);

		// associated_make_from
		setAssociated_make_from(context, this);
		
		// clean ARM
		// associated_make_from
		unsetAssociated_make_from(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// associated_make_from
			unsetAssociated_make_from(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  {[make_from_part_feature_relationship &lt;=
	 *  shape_aspect_relationship]
	 *  [make_from_part_feature_relationship &lt;=
	 *  shape_aspect]}
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
			EPart_feature_make_from_relationship armEntity) throws SdaiException {
		CxShape_element_relationship.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPart_feature_make_from_relationship armEntity) throws SdaiException {
		CxShape_element_relationship.unsetMappingConstraints(context, armEntity);
	}


	//********** "part_feature_make_from_relationship" attributes

	/**
	* Sets/creates data for associated_make_from attribute.
	*
	* <p>
	*  attribute_mapping associated_make_from_engineering_make_from (associated_make_from
	* , (*PATH*), engineering_make_from);
	* 	make_from_part_feature_relationship <= 
	* 	shape_aspect 
	* 	shape_aspect.of_shape -> 
	* 	product_definition_shape <= 
	* 	property_definition 
	* 	property_definition.definition -> 
	* 	characterized_definition 
	* 	characterized_definition = characterized_product_definition 
	* 	characterized_product_definition 
	* 	characterized_product_definition = product_definition_relationship 
	* 	product_definition_relationship => 
	* 	design_make_from_relationship 
	*  end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	// MFPFR -> PDS -> DMFR
	public static void setAssociated_make_from(SdaiContext context, EPart_feature_make_from_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetAssociated_make_from(context, armEntity);
		
		if (armEntity.testAssociated_make_from(null))
		{ 
			EProduct_definition_relationship armAssociated_make_from = armEntity.getAssociated_make_from(null);
			// PDS -> DMFR
	      LangUtils.Attribute_and_value_structure[] pdsStructure =
			{new LangUtils.Attribute_and_value_structure(CProduct_definition_shape.attributeDefinition(null), armAssociated_make_from)};
	      EProduct_definition_shape property_definition = (EProduct_definition_shape)
				LangUtils.createInstanceIfNeeded(
				context, CProduct_definition_shape.definition, pdsStructure);
	      if(!property_definition.testName(null))
	      	property_definition.setName(null, "");
			armEntity.setOf_shape(null, property_definition);
		}
	}


	/**
	* Unsets/deletes data for associated_make_from attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	public static void unsetAssociated_make_from(SdaiContext context, EPart_feature_make_from_relationship armEntity) throws SdaiException
	{
		armEntity.unsetOf_shape(null);
	}	
	
}