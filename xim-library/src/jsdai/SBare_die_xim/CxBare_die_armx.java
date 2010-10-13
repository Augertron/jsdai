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

package jsdai.SBare_die_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SBare_die_mim.CBare_die;
import jsdai.SGeometry_schema.*;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SFunctional_usage_view_xim.EFunctional_unit_usage_view;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;

public class CxBare_die_armx
		extends
		CBare_die_armx implements EMappedXIMEntity {

	
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

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF Taken from Physical_unit - Property_definition
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CBare_die.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// From Item_shape
//			setId_x(context, this);

			// SETTING DERIVED
			// setDefinition(null, this);
			
	      // least_material_condition_centroid_location
			setLeast_material_condition_centroid_location(context, this);
			
	      // maximum_height_above_seating_plane
			setMaximum_height_above_seating_plane(context, this);
			
	      // maximum_material_condition_centroid_location
			setMaximum_material_condition_centroid_location(context, this);
			
	      // implemented_function
			setImplemented_function(context, this);

			
			// Clean ARM specific attributes
			unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
//			unsetId_x(null);
						
			unsetLeast_material_condition_centroid_location(null);
			unsetMaximum_height_above_seating_plane(null);
			unsetMaximum_material_condition_centroid_location(null);
			unsetImplemented_function(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			unsetAdditional_characterization(context, this);

			//id_x
//			unsetId_x(context, this);
			
			// unsetDefinition(null);
			
	      // least_material_condition_centroid_location
			unsetLeast_material_condition_centroid_location(context, this);
			
	      // maximum_height_above_seating_plane
			unsetMaximum_height_above_seating_plane(context, this);
			
	      // maximum_material_condition_centroid_location
			unsetMaximum_material_condition_centroid_location(context, this);
			
	      // implemented_function
			unsetImplemented_function(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  bare_die &lt;=
	 *  physical_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.formation -&gt;
	 *  product_definition_formation
	 *  product_definition_formation.of_product -&gt;
	 *  product &lt;-
	 *  product_related_product_category.products[i]
	 *  product_related_product_category &lt;=
	 *  product_category
	 *  product_category.name = 'bare die']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical design usage']}
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
			EPart_usage_view armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_usage_view.setMappingConstraints(context, armEntity);
		
      LangUtils.Attribute_and_value_structure[] pcStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProduct_related_product_category.attributeName(null), "bare die")
         };
      
         EProduct_related_product_category eprpc = (EProduct_related_product_category)
				LangUtils.createInstanceIfNeeded(context,
         		CProduct_related_product_category.definition, pcStructure);
      EProduct product = armEntity.getDefined_version(null).getOf_product(null);
      if(!eprpc.testProducts(null))
      	eprpc.createProducts(null).addUnordered(product);
      else{
      	AProduct products = eprpc.getProducts(null);
      	if(!products.isMember(product))
      		products.addUnordered(product);
      }
		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPart_usage_view armEntity) throws SdaiException {
		CxPart_usage_view.unsetMappingConstraints(context, armEntity);

		if(!armEntity.testDefined_version(null))
			return;
		EProduct_definition_formation version = armEntity.getDefined_version(null);
		if(!version.testOf_product(null))
			return;
		EProduct product = version.getOf_product(null);
		
		AProduct_related_product_category categories = new AProduct_related_product_category();
		CProduct_related_product_category.usedinProducts(null, product, context.domain, categories);
		
		SdaiIterator iter = categories.createIterator();
		while(iter.next()){
			EProduct_related_product_category category = categories.getCurrentMember(iter);
			if(category.getName(null).equals("bare die"))
				category.getProducts(null).removeUnordered(product);
			if(category.getProducts(null).getMemberCount() == 0)
				category.deleteApplicationInstance();
		}
		
	}

	//********** "design_discipline_item_definition" attributes
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		//unset old values
		CxItem_shape.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);	
	}
*/

	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
	}

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
	}

	//********** "bare_die" attributes

	/**
	* Sets/creates data for least_material_condition_centroid_location attribute.
	*
	* <p>
	*  attribute_mapping least_material_condition_centroid_location_cartesian_point (least_material_condition_centroid_location
	* , (*PATH*), cartesian_point);
	* 	(bare_die <=)
	* 	(externally_defined_bare_die <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_bare_die <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	{representation
	* 	representation.name = `material property data'}
	* 	representation
	* 	representation.items[i] ->
	* 	representation_item =>
	* 	{cartesian_point
	* 	cartesian_point.name = `least material condition centroid location'}
	* 	cartesian_point
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLeast_material_condition_centroid_location(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetLeast_material_condition_centroid_location(context, armEntity);

		if (armEntity.testLeast_material_condition_centroid_location(null))
		{

			ECartesian_point armLeast_material_condition_centroid_location = armEntity.getLeast_material_condition_centroid_location(null);
			//EA armLeast_material_condition_centroid_location.createAimData(context);

			//representation
			jsdai.SRepresentation_schema.ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, "material property data");
			armLeast_material_condition_centroid_location.setName(null, "least material condition centroid location");
			representation.getItems(null).addUnordered(armLeast_material_condition_centroid_location);
		}
	}


	/**
	* Unsets/deletes data for least_material_condition_centroid_location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLeast_material_condition_centroid_location(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			//property_definition_representation
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
			for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
				property_definition_representation = aProperty_definition_representation.getByIndex(j);
				//representation
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation representation = property_definition_representation.getUsed_representation(null);
					if (representation.testName(null) && representation.getName(null).equals("material property data")) {
						if (representation.testItems(null)) {
							ARepresentation_item aItem = representation.getItems(null);
							ERepresentation_item item = null;

							int k = 1;
							while (k <= aItem.getMemberCount()) {
								item = aItem.getByIndex(k);

								if (item.testName(null)
									&& item.getName(null).equals("least material condition centroid location")) {
									aItem.removeUnordered(item);
								} else {
									k++;
								}
							}
						}
					}
				}
			}
		}
	}


	/**
	* Sets/creates data for maximum_height_above_seating_plane attribute.
	*
	* <p>
	*  attribute_mapping maximum_height_above_seating_plane_length_data_element (maximum_height_above_seating_plane
	* , (*PATH*), length_data_element);
	* 	(bare_die <=)
	* 	(externally_defined_bare_die <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_bare_die <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	{representation.name = `mounting data'}
	* 	representation.items[i] ->
	* 	representation_item
	* 	{representation_item.name = `maximum height above seating plane'}
	* 	representation_item =>
	* 	measure_representation_item <=
	* 	measure_with_unit =>
	* 	length_measure_with_unit
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_height_above_seating_plane(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_height_above_seating_plane(context, armEntity);

		if (armEntity.testMaximum_height_above_seating_plane(null))
		{

			ELength_measure_with_unit armHeight = armEntity.getMaximum_height_above_seating_plane(null);
			//EA armMaximum_height_above_seating_plane.createAimData(context);
			//representation
			jsdai.SRepresentation_schema.ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, "mounting data");
			ERepresentation_item item;
			if(!(armHeight instanceof ERepresentation_item))
				item = (ERepresentation_item)
					context.working_model.substituteInstance(armHeight, CLength_measure_with_unit$measure_representation_item.definition);
			else
				item = (ERepresentation_item)armHeight;
			item.setName(null, "maximum height above seating plane");
			representation.getItems(null).addUnordered(item);
		}
	}


	/**
	* Unsets/deletes data for maximum_height_above_seating_plane attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_height_above_seating_plane(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			//property_definition_representation
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
			for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
				property_definition_representation = aProperty_definition_representation.getByIndex(j);
				//representation
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation representation = property_definition_representation.getUsed_representation(null);
					if (representation.testName(null) && representation.getName(null).equals("mounting data")) {
						if (representation.testItems(null)) {
							ARepresentation_item aItem = representation.getItems(null);
							ERepresentation_item item = null;

							int k = 1;
							while (k <= aItem.getMemberCount()) {
								item = aItem.getByIndex(k);

								if (item.testName(null)
									&& item.getName(null).equals("maximum height above seating plane")) {
									aItem.removeUnordered(item);
								} else {
									k++;
								}
							}
						}
					}
				}
			}
		}
	}


	/**
	* Sets/creates data for maximum_material_condition_centroid_location attribute.
	*
	* <p>
	*  attribute_mapping maximum_material_condition_centroid_location_cartesian_point (maximum_material_condition_centroid_location
	* , (*PATH*), cartesian_point);
	* 	(bare_die <=)
	* 	(externally_defined_bare_die <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_bare_die <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	{representation
	* 	representation.name = `material property data'}
	* 	representation
	* 	representation.items[i] ->
	* 	representation_item =>
	* 	{cartesian_point
	* 	cartesian_point.name = `maximum material condition centroid location'}
	* 	cartesian_point
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_material_condition_centroid_location(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_material_condition_centroid_location(context, armEntity);

		if (armEntity.testMaximum_material_condition_centroid_location(null))
		{

			ECartesian_point armLocation = armEntity.getMaximum_material_condition_centroid_location(null);
			//EA armMaximum_material_condition_centroid_location.createAimData(context);

			//representation
			jsdai.SRepresentation_schema.ERepresentation representation = CxAP210ARMUtilities.findRepresentation(context, armEntity, "material property data");
			armLocation.setName(null, "maximum material condition centroid location");
			representation.getItems(null).addUnordered(armLocation);
		}
	}


	/**
	* Unsets/deletes data for maximum_material_condition_centroid_location attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_material_condition_centroid_location(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//property_definition
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			//property_definition_representation
			EProperty_definition_representation property_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
			for (int j = 1; j <= aProperty_definition_representation.getMemberCount(); j++) {
				property_definition_representation = aProperty_definition_representation.getByIndex(j);
				//representation
				if (property_definition_representation.testUsed_representation(null)) {
					ERepresentation representation = property_definition_representation.getUsed_representation(null);
					if (representation.testName(null) && representation.getName(null).equals("material property data")) {
						if (representation.testItems(null)) {
							ARepresentation_item aItem = representation.getItems(null);
							ERepresentation_item item = null;

							int k = 1;
							while (k <= aItem.getMemberCount()) {
								item = aItem.getByIndex(k);

								if (item.testName(null)
									&& item.getName(null).equals("maximum material condition centroid location")) {
									aItem.removeUnordered(item);
								} else {
									k++;
								}
							}
						}
					}
				}
			}
		}
	}


	/**
	* Sets/creates data for die_seating_plane attribute.
	*
	* <p>
	*  attribute_mapping die_seating_plane_seating_plane (die_seating_plane
	* , (*PATH*), seating_plane);
	* 	(bare_die <=)
	* 	(externally_defined_bare_die <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_bare_die <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition
	* 	characterized_product_definition = product_definition
	* 	characterized_product_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition =>
	* 	product_definition_shape <-
	* 	shape_aspect.of_shape
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `die seating plane'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect =>
	* 	seating_plane
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  // Removed since WD26
	/* public static void setDie_seating_plane(SdaiContext context, EBare_die armEntity) throws SdaiException
	{
		//unset old values
		unsetDie_seating_plane(context, armEntity);

		if (armEntity.testDie_seating_plane(null))
		{

			jsdai.SElectronic_assembly_interconnect_and_packaging_design.EBare_die aimEntity = (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EBare_die) armEntity.getAimInstance();
			jsdai.SAp210_arm_extended.ESeating_plane armDie_seating_plane = (jsdai.SAp210_arm_extended.ESeating_plane) armEntity.getDie_seating_plane(null);
			armDie_seating_plane.createAimData(context);
			armDie_seating_plane.getAimInstance();
			jsdai.SElectronic_assembly_interconnect_and_packaging_design.ESeating_plane aimDie_seating_plane = (jsdai.SElectronic_assembly_interconnect_and_packaging_design.ESeating_plane)
																						armDie_seating_plane.getAimInstance();


			//property definition must be created during creation of Ee_product_definition.context_description
			AProperty_definition aProperty_definition = new AProperty_definition();
			CProperty_definition.usedinDefinition(null, aimEntity, context.domain, aProperty_definition);
			EProperty_definition property_definition = null;
			if (aProperty_definition.getMemberCount() > 0) {
				property_definition = aProperty_definition.getByIndex(1);
			} else {
				property_definition = jsdai.SAp210_arm.CxAP210ARMUtilities.createProperty_definition(context, CProduct_definition_shape.definition , "", null, aimEntity, false);
			}

			EProduct_definition_shape product_definition_shape = null;
			//change entity type
			if (property_definition instanceof EProduct_definition_shape) {
				product_definition_shape = (EProduct_definition_shape) property_definition;
			} else {
				product_definition_shape = (EProduct_definition_shape) context.working_model.substituteInstance(property_definition, EProduct_definition_shape.class);
			}

			//shape_aspect
			EShape_aspect shape_aspect = null;
			AShape_aspect aShape_aspect = new AShape_aspect();
			CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);

			if (aShape_aspect.getMemberCount() > 0) {
				shape_aspect = aShape_aspect.getByIndex(1);
			} else {
				shape_aspect = (EShape_aspect) context.working_model.createEntityInstance(jsdai.SProduct_property_definition_schema.EShape_aspect.class);
				shape_aspect.setOf_shape(null, product_definition_shape);
			}

			if (!shape_aspect.testName(null)) {
				shape_aspect.setName(null, "");
			}

			if (!shape_aspect.testProduct_definitional(null)) {
				shape_aspect.setProduct_definitional(null, ELogical.UNKNOWN);
			}

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setRelated_shape_aspect(null, shape_aspect);
			shape_aspect_relationship.setRelating_shape_aspect(null, aimDie_seating_plane);
			shape_aspect_relationship.setName(null, "die seating plane");
		}
	}
*/

	/**
	* Unsets/deletes data for die_seating_plane attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* public static void unsetDie_seating_plane(SdaiContext context, EBare_die armEntity) throws SdaiException
	{
		jsdai.SElectronic_assembly_interconnect_and_packaging_design.EBare_die aimEntity = (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EBare_die) armEntity.getAimInstance();

		//product_definition_shape
		EProduct_definition_shape product_definition_shape = null;
		AProduct_definition_shape aProduct_definition_shape = new AProduct_definition_shape();
		CProduct_definition_shape.usedinDefinition(null, aimEntity, context.domain, aProduct_definition_shape);

		for (int i = 1; i <= aProduct_definition_shape.getMemberCount(); i++) {
			product_definition_shape = aProduct_definition_shape.getByIndex(i);

			//shape_aspect
			AShape_aspect aShape_aspect = new AShape_aspect();
			EShape_aspect shape_aspect = null;
			CShape_aspect.usedinOf_shape(null, product_definition_shape, context.domain, aShape_aspect);
			for (int j = 1; j <= aShape_aspect.getMemberCount(); j++) {
				shape_aspect = aShape_aspect.getByIndex(j);

				//shape_aspect_relationship
				EShape_aspect_relationship shape_aspect_relationship = null;
				AShape_aspect_relationship aShape_aspect_relationship = new AShape_aspect_relationship();
				CShape_aspect_relationship.usedinRelated_shape_aspect(null, shape_aspect, context.domain, aShape_aspect_relationship);
				for (int k = 1; k <= aShape_aspect_relationship.getMemberCount(); k++) {
					shape_aspect_relationship = aShape_aspect_relationship.getByIndex(k);
					if (shape_aspect_relationship.testName(null)
					   && shape_aspect_relationship.getName(null).equals("die seating plane"))
					{

						shape_aspect_relationship.deleteApplicationInstance();
					}
				}
			}
		}
	}
  */

	/**
	* Sets/creates data for implemented_function attribute.
	*
	* <p>
	*  attribute_mapping implemented_function_functional_unit_usage_view (implemented_function
	* , (*PATH*), functional_unit_usage_view);
	* 	(bare_die <=)
	* 	(externally_defined_bare_die <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_bare_die <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `implemented function'}
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `functional design usage'}
	* 	product_definition =>
	* 	(functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit =>
	* 	library_defined_functional_unit)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplemented_function(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetImplemented_function(context, armEntity);

		if (armEntity.testImplemented_function(null))
		{

			EFunctional_unit_usage_view armImplemented_function = armEntity.getImplemented_function(null);

			//product_definition_relationship
			jsdai.SProduct_definition_schema.EProduct_definition_relationship relationship = (jsdai.SProduct_definition_schema.EProduct_definition_relationship) context.working_model.createEntityInstance(jsdai.SProduct_definition_schema.EProduct_definition_relationship.class);
			relationship.setId(null, "");
			relationship.setName(null, "implemented function");
			relationship.setRelated_product_definition(null, armEntity);
			relationship.setRelating_product_definition(null, armImplemented_function);
		}
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, EBare_die_armx armEntity) throws SdaiException
	{
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		EProduct_definition_relationship relationship = null;
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);

			if (relationship.testName(null) && relationship.getName(null).equals("implemented function")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	

}