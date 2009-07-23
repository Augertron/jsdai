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

package jsdai.SAssembly_component_placement_requirements_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SAssembly_component_placement_requirements_mim.CMounting_restriction_volume;
import jsdai.SAssembly_module_with_interconnect_component_xim.EInterconnect_module_component_surface_feature_armx;
import jsdai.SNon_feature_shape_element_xim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxMounting_restriction_volume_armx extends CMounting_restriction_volume_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_shape)a2;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a2$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMounting_restriction_volume.definition);

		setMappingConstraints(context, this);

//		setScope(context, this);
		
		// Mounting_surface
		setMounting_surface (context, this);
		
		// Clean ARM
//		unsetScope(null);
		
		// Mounting_surface
		unsetMounting_surface(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
//			unsetScope(context, this);
			
			// Mounting_surface
			unsetMounting_surface (context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  group_shape_aspect &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'interconnect module constraint region'}
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
			EMounting_restriction_volume_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxNon_feature_shape_element.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EMounting_restriction_volume_armx armEntity) throws SdaiException {
		CxNon_feature_shape_element.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping scope.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void setScope(SdaiContext context,
			ENon_feature_shape_element armEntity) throws SdaiException {
		CxNon_feature_shape_element.setScope(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/*	
	public static void unsetScope(SdaiContext context,
			ENon_feature_shape_element armEntity) throws SdaiException {
		CxNon_feature_shape_element.unsetScope(context, armEntity);
	}
*/	
	
	/**
	* Sets/creates data for mounting_surface attribute.
	*
	* <p>
	*  attribute_mapping mounting_surface_interconnect_module_component_surface_feature (mounting_surface
	* , (*PATH*), interconnect_module_component_surface_feature);
	* 	mounting_restriction_volume &lt;=
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.relating_shape_aspect
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'mounting surface'}
	*  shape_aspect_relationship
	*  shape_aspect_relationship.related_shape_aspect -&gt;
	*  {shape_aspect.description = 'interconnect module component surface feature'}
	*  shape_aspect =&gt;
	*  component_feature =&gt;				
	*  physical_component_feature =&gt;
	*  interconnect_module_component_surface_feature 
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> IMCSF
	public static void setMounting_surface(SdaiContext context, EMounting_restriction_volume_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMounting_surface(context, armEntity);

		if (armEntity.testMounting_surface(null))
		{
			EInterconnect_module_component_surface_feature_armx armMounting_surface = armEntity.getMounting_surface(null);
			String keyword = "mounting surface";
		   // IMCSF in AIM
			// SAR
			EShape_aspect_relationship relationship = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			relationship.setName(null, keyword);
			relationship.setRelating_shape_aspect(null, armEntity);
			relationship.setRelated_shape_aspect(null, armMounting_surface);
		}
	}


	/**
	* Unsets/deletes data for mounting_surface attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> IMCSF, remove all SARs
	public static void unsetMounting_surface(SdaiContext context, EMounting_restriction_volume_armx armEntity) throws SdaiException
	{
			AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		String keyword = "mounting surface";
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
		for(int i=1; i<=relationships.getMemberCount();){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
			if(relationship.getName(null).equals(keyword)){
				relationships.removeByIndex(i);
				relationship.deleteApplicationInstance();
			}
		}
	}
	

}