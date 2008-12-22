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

package jsdai.SInterconnect_placement_requirements_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SGroup_schema.*;
import jsdai.SInterconnect_placement_requirements_mim.CLayer_qualified_layout_spacing_requirement;
import jsdai.SLayered_interconnect_module_design_xim.*;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SRequirement_decomposition_xim.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxLayer_qualified_layout_spacing_requirement_armx extends CLayer_qualified_layout_spacing_requirement_armx implements EMappedXIMEntity
{

	
	// Taken from property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProduct_definition type) throws SdaiException {
		return a2$;
	}
	// END OF taken from property_definition
	
	public static jsdai.dictionary.EAttribute attributeName(EGroup type) throws SdaiException {
		return a10$;
	}
	
	// ENDOF taken from CGroup	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CLayer_qualified_layout_spacing_requirement.definition);

			setMappingConstraints(context, this);

			//********** "Requirement_definition_property" attributes

			//required_analytical_representation
			setRequired_analytical_representation(context, this);

			//order improtant set of requirement_specification must be before required_specification
			//requirement_specification - made derived
			// setRequirement_specification(context, this);

			//required_specification
	      // Removed since WD34
			// setRequired_specification(context, this);

			//required_part
			// setRequired_part(context, this);

			//required_coordinated_characteristic - removed during modularization
			// setRequired_coordinated_characteristic(context, this);


			//required_characteristic
			setRequired_characteristic(context, this);

			//required_material - INVERSE
			// setRequired_material(context, this);

	      //********** "layout_spacing_requirement_occurrence" attributes
	      //reference_design_object_category
	      setDesign_object_category_1(context, this);

	      //dependent_design_object_category
	      setDesign_object_category_2(context, this);

	      // New since WD26
	      setOf_spacing_type(context, this);

	      setLayer_context(context, this);
	      
	      // Clean ARM
	      // unsetAssociated_definition(null);
	      unsetRequired_analytical_representation(null);
	      unsetRequired_functional_specification(null);
	      // unsetRequired_part(null);
	      unsetRequired_characteristic(null);

	      unsetDesign_object_category_1(null);
	      unsetDesign_object_category_2(null);
	      unsetOf_spacing_type(null);
	      
	      unsetLayer_context(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes

		//required_analytical_representation
		unsetRequired_analytical_representation(context, this);

		//order improtant set of requirement_specification must be before required_specification
		//requirement_specification - made derived
		// unsetRequirement_specification(context, this);

		//required_specification
      // Removed since WD34
		// setRequired_specification(context, this);

		//required_part
		// unsetRequired_part(context, this);

		//required_coordinated_characteristic - removed during modularization
		// unsetRequired_coordinated_characteristic(context, this);


		//required_characteristic
		unsetRequired_characteristic(context, this);

		//required_material - INVERSE
		// setRequired_material(context, this);

      //********** "layout_spacing_requirement_occurrence" attributes
      //reference_design_object_category
      unsetDesign_object_category_1(context, this);

      //dependent_design_object_category
      unsetDesign_object_category_2(context, this);

      // New since WD26
      unsetOf_spacing_type(context, this);
      
      unsetLayer_context(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*
	*  mapping_constraints;
	* 	grouped_requirements_property &lt;=
	*  [group
	*  {group
	*  group.name = 'layer qualified layout spacing requirements property'}]
	*  [requirements_property &lt;=
	*  property_definition]
	*  end_mapping_constraints;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ELayer_qualified_layout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, ELayer_qualified_layout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.unsetMappingConstraints(context, armEntity);

	}
	
	
	//********** "managed_design_object" attributes

	//********** "Requirement_definition_property" attributes

	/**
	* Sets/creates data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_analytical_representation(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_analytical_representation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_analytical_representation(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_analytical_representation(context, armEntity);
	}


	/**
	* Sets/creates data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void setRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.setRequired_specification(context, armEntity);
	}
*/

	/**
	* Unsets/deletes data for required_specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
// removed since WD32
/*	public static void unsetRequired_specification(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_specification(context, armEntity);
	}
*/

	/**
	* Sets/creates data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_characteristic(context, armEntity);
	}


	/**
	* Unsets/deletes data for required_characteristic attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRequired_characteristic(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.unsetRequired_characteristic(context, armEntity);
	}

/* This is now inverse attribute
	/
	* Sets/creates data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void setRequired_material(SdaiContext context, EPredefined_requirement_view_definition_armx armEntity) throws SdaiException
	{
		CxPredefined_requirement_view_definition_armx.setRequired_material(context, armEntity);
	}


	/
	* Unsets/deletes data for required_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void unsetRequired_material(SdaiContext context, EEe_requirement_occurrence armEntity) throws SdaiException
	{
		CxEe_requirement_occurrence.unsetRequired_material(context, armEntity);
	}
*/
	//********** "layout_spacing_requirement_occurrence" attributes

	/**
	* Sets/creates data for reference_design_object_category attribute.
	*
	*
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, where DO should be created/deleted always
	public static void setDesign_object_category_1(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.setDesign_object_category_1(context, armEntity);		
   }


	/**
	* Unsets/deletes data for reference_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// GRP <- AGA -> DO, remove DO
	public static void unsetDesign_object_category_1(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.unsetDesign_object_category_1(context, armEntity);		
	}


	/**
	* Sets/creates data for dependent_design_object_category attribute.
	*
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDesign_object_category_2(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.setDesign_object_category_2(context, armEntity);		
   }


	/**
	* Unsets/deletes data for dependent_design_object_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDesign_object_category_2(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
	{
		CxLayout_spacing_requirement_armx.unsetDesign_object_category_2(context, armEntity);		
	}

// New since WD26
   /**
   * Sets/creates data for reference_design_object_category attribute.
    grouped_requirements_property <=
    group
    {(group.description = `nearest boundary')
    (group.description = `centroid')
    (group.description = `furthest boundary')}
    group.description
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */

   public static void setOf_spacing_type(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
   {
   	CxLayout_spacing_requirement_armx.setOf_spacing_type(context, armEntity);   	
   }


   /**
   * Unsets/deletes data for reference_design_object_category attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
   public static void unsetOf_spacing_type(SdaiContext context, ELayout_spacing_requirement_armx armEntity) throws SdaiException
   {
   	CxLayout_spacing_requirement_armx.unsetOf_spacing_type(context, armEntity);
   }

	//********** "layer_qualified_layout_spacing_requirement_occurrence" attributes

	/**
	* Sets/creates data for layer_context attribute.
	*
	* <p>
	*  attribute_mapping layer_context_inter_stratum_extent (layer_context
	* , (*PATH*), inter_stratum_extent);
	* 	grouped_requirements_property <=
	* 	property_definition <-
	* 	property_definition_relationship.related_property_definition
	* 	{property_definition_relationship
	* 	property_definition_relationship.name = `qualified extent'}
	* 	property_definition_relationship.relating_property_definition ->
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `inter stratum extent'}
	* 	product_definition_relationship
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLayer_context(SdaiContext context, ELayer_qualified_layout_spacing_requirement_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetLayer_context(context, armEntity);

		if (armEntity.testLayer_context(null))
		{

			AInter_stratum_extent aArmLayer_context = armEntity.getLayer_context(null);
			EInter_stratum_extent armLayer_context = null;
			for (int i = 1; i <= aArmLayer_context.getMemberCount(); i++) {
				armLayer_context = aArmLayer_context.getByIndex(i);

				//property_definition_relationship
			    EProperty_definition_relationship property_definition_relationship = (EProperty_definition_relationship) context.working_model.createEntityInstance(EProperty_definition_relationship.class);
				property_definition_relationship.setName(null, "layered context");
				property_definition_relationship.setDescription(null, "");
            // PD -> MRA
            LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), armEntity)};
            EProperty_definition epd = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context,
                                                CProperty_definition.definition,
                                                pdStructure);
            if (!epd.testName(null))
               epd.setName(null, "");
				
				property_definition_relationship.setRelated_property_definition(null, epd);

				//property_definition
				EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "", null, armLayer_context, false);
				property_definition_relationship.setRelating_property_definition(null, property_definition);
			}
		}
	}


	/**
	* Unsets/deletes data for layer_context attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLayer_context(SdaiContext context, ELayer_qualified_layout_spacing_requirement_armx armEntity) throws SdaiException
	{
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int j = 1; j <= aProperty_definition.getMemberCount(); j++) {
			EProperty_definition epd = aProperty_definition.getByIndex(j); 
			//property_definition_relationship
			EProperty_definition_relationship property_definition_relationship = null;
			AProperty_definition_relationship aProperty_definition_relationship = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, aProperty_definition_relationship);
			for (int i = 1; i <= aProperty_definition_relationship.getMemberCount(); i++) {
				property_definition_relationship = aProperty_definition_relationship.getByIndex(i);
				if (property_definition_relationship.testName(null) && property_definition_relationship.getName(null).equals("layer context")) {
	
					//property_definition
					if (property_definition_relationship.testRelating_property_definition(null)) {
						property_definition = property_definition_relationship.getRelating_property_definition(null);
	
						if (CxAP210ARMUtilities.countEntityUsers(context, property_definition) <= 1) {
							property_definition.deleteApplicationInstance();
						}
					}
					property_definition_relationship.deleteApplicationInstance();
				}
			}
		}
	}
   

}
