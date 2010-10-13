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

package jsdai.SDerived_shape_element_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_aspect_definition_schema.CDerived_shape_aspect;
import jsdai.SShape_aspect_definition_schema.CShape_aspect_deriving_relationship;
import jsdai.SShape_aspect_definition_schema.EShape_aspect_deriving_relationship;

public class CxDerived_shape_element extends CDerived_shape_element implements EMappedXIMEntity{

	// From CShape_aspect.java
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

		setTemp("AIM", CDerived_shape_aspect.definition);

		setMappingConstraints(context, this);
		
		setDerived_from(context, this);
		
//		setId_x(context, this);
		
		// Clean ARM
		unsetDerived_from(null);
		
//		unsetId_x(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetDerived_from(context, this);
			
//			unsetId_x(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
		mapping_constraints;
			(|derived_shape_aspect|)
			(
			(derived_shape_aspect 
			{derived_shape_aspect => apex})
			)(
			({derived_shape_aspect => centre_of_symmetry})
			)(
			({derived_shape_aspect => geometric_alignment})
			)(
			({derived_shape_aspect => perpendicular_to})
			)(
			({derived_shape_aspect => extension})
			)(
			({derived_shape_aspect => tangent})
			)(
			({derived_shape_aspect => geometric_intersection})
			)
		end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EDerived_shape_element armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

//		CxShape_element.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EDerived_shape_element armEntity) throws SdaiException {
//		CxShape_element.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for id_x constraints.
	 *
	 * <p>
	 * </p>
	 *
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
		CxShape_element.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 *
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);
	}
*/	
	//********** "component_feature" attributes

	/**
	* Sets/creates data for Derived_from attribute.
	*
	* <p>
	attribute_mapping derived_from(derived_from, $PATH, Shape_element);
		shape_aspect <- 
		shape_aspect_relationship.relating_shape_aspect
		shape_aspect_relationship 
		{shape_aspect_relationship => 
		shape_aspect_deriving_relationship} 
		shape_aspect_relationship.related_shape_aspect -> 
		shape_aspect
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	public static void setDerived_from(SdaiContext context, EDerived_shape_element armEntity) throws SdaiException
	{
		//unset old values
		unsetDerived_from(context, armEntity);

		if (armEntity.testDerived_from(null))
		{
			AShape_aspect armDefinitions = (AShape_aspect) armEntity.getDerived_from(null);
			for(SdaiIterator iter = armDefinitions.createIterator(); iter.next();){
				// SA (template)
				// System.err.println(" ARM "+armDefinition);
			   // SAR
				EShape_aspect_relationship relationship = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_deriving_relationship.definition);
			   relationship.setName(null, "");
				// SA <- SAR -> SA
				relationship.setRelating_shape_aspect(null, armEntity);
				relationship.setRelated_shape_aspect(null, armDefinitions.getCurrentMember(iter));
			}
		}
	}


	/**
	* Unsets/deletes data for Derived_from attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDerived_from(SdaiContext context, EDerived_shape_element armEntity) throws SdaiException
	{
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null,
			armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
		   if(relationship instanceof EShape_aspect_deriving_relationship){
				relationships.removeByIndex(i);
				relationship.deleteApplicationInstance();
		   }
			else
				i++;
		}
	}


	
}