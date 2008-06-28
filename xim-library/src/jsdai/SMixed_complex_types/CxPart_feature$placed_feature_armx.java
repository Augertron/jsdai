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

package jsdai.SMixed_complex_types;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SPhysical_unit_usage_view_xim.CxPart_feature;
import jsdai.SPhysical_unit_usage_view_xim.EMaterial_state_change_enumeration;
import jsdai.SPhysical_unit_usage_view_xim.EPart_feature;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_feature_mim.CPlaced_feature;
import jsdai.SShape_feature_xim.CxPlaced_feature_armx;
import jsdai.SShape_feature_xim.EPlaced_feature_armx;
import jsdai.SShape_property_assignment_xim.CxShape_element;
import jsdai.SShape_property_assignment_xim.EShape_element;

public class CxPart_feature$placed_feature_armx extends CPart_feature$placed_feature_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a4$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a6);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a6);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a6 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a6 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a6$;
	}
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlaced_feature.definition);

		setMappingConstraints(context, this);
		
		setMaterial_state_change(context, this);
		
		setPrecedent_feature(context, this);
		
		setConnection_area(context, this);
		
		setDefinition(context, this);
		
		setId_x(context, this);
		// Clean ARM
		unsetMaterial_state_change(null);
		unsetPrecedent_feature(null);
		unsetConnection_area(null);
		unsetDefinition(null);
		
		unsetId_x(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetMaterial_state_change(context, this);
			
			unsetPrecedent_feature(context, this);
			
			unsetConnection_area(context, this);

			unsetDefinition(context, this);
			
			unsetId_x(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
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
			CPart_feature$placed_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPart_feature.setMappingConstraints(context, armEntity);
		CxPlaced_feature_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CPart_feature$placed_feature_armx armEntity) throws SdaiException {
		CxPart_feature.unsetMappingConstraints(context, armEntity);
		CxPlaced_feature_armx.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for material_state_change.
	 * 
	 * <p>
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.related_shape_aspect
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'precedent feature'}
	 *  shape_aspect_relationship.relating_shape_aspect -&gt;
	 *  shape_aspect
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
////////////////////////////////////////////////////////////////////
	/**
	* Sets/creates data for material_state_change attribute.
	*
	*
	*  attribute_mapping material_state_change (material_state_change
	* );
	*
	*  attribute_mapping material_state_change (material_state_change
	* );
	* 	shape_aspect
	*  ... (* general stuff from ENTITY mapping *)
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	{[representation
	* 	representation.name = `material state change']
	* 	[representation
	* 	representation.items[i] ->
	* 	representation_item
	* 	{(representation_item.name = `material addition')
	* 	(representation_item.name = `material removal')}]}
	* 
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetMaterial_state_change(context, armEntity);

		if (armEntity.testMaterial_state_change(null))
		{

			int armMaterial_state_change = armEntity.getMaterial_state_change(null);

			//property_definition
			LangUtils.Attribute_and_value_structure[] pdStructure ={
					new LangUtils.Attribute_and_value_structure(
							CProperty_definition.attributeDefinition(null), 
							armEntity)
			};

			EProperty_definition property_definition = (EProperty_definition) 
				LangUtils.createInstanceIfNeeded(context,CProperty_definition.definition, pdStructure);

			if (!property_definition.testName(null)) {
				property_definition.setName(null, "");
			}

			//property_definition_representation
			EProperty_definition_representation property_definition_representation = null;
			EProperty_definition_representation tempProperty_definition_representation = null;
			AProperty_definition_representation aProperty_definition_representation = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aProperty_definition_representation);
			ERepresentation representation = null;
			for (int i = 1; i <= aProperty_definition_representation.getMemberCount(); i++) {
				tempProperty_definition_representation = aProperty_definition_representation.getByIndex(i);
				if (tempProperty_definition_representation.testUsed_representation(null)) {
					representation = tempProperty_definition_representation.getUsed_representation(null);
					if (representation.testName(null) && representation.getName(null).equals("material state change")) {
						property_definition_representation = tempProperty_definition_representation;
						break;
					}
				} else {
					property_definition_representation = tempProperty_definition_representation;
				}
			}

			if (property_definition_representation == null) {
				property_definition_representation = (EProperty_definition_representation) context.working_model.createEntityInstance(EProperty_definition_representation.class);
				property_definition_representation.setDefinition(null, property_definition);
			}

			//representation
			if (property_definition_representation.testUsed_representation(null)) {
				representation = property_definition_representation.getUsed_representation(null);
			} else {
				representation = CxAP210ARMUtilities.createRepresentation(context, "material state change", false);
				property_definition_representation.setUsed_representation(null, representation);
			}

			if (!representation.testItems(null)) {
				representation.createItems(null);
			}

			ERepresentation_item representation_item = (ERepresentation_item) context.working_model.createEntityInstance(ERepresentation_item.class);
			representation_item.setName(null, EMaterial_state_change_enumeration.toString(armMaterial_state_change).toLowerCase().replace('_', ' '));
			representation.getItems(null).addUnordered(representation_item);
		}
	}


	/**
	* Unsets/deletes data for material_state_change attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaterial_state_change(SdaiContext context, EPart_feature armEntity) throws SdaiException
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
					if (representation.testName(null) && representation.getName(null).equals("material state change")) {
						if (representation.testItems(null)) {
							ARepresentation_item aItem = representation.getItems(null);
							ERepresentation_item item = null;

							int k = 1;
							while (k <= aItem.getMemberCount()) {
								item = aItem.getByIndex(k);

								if (item.testName(null) &&
								   (item.getName(null).equals("material addition") || item.getName(null).equals("material removal"))) {
									aItem.removeUnordered(item);
								} else {
									k++;
								}
							}

							if (aItem.getMemberCount() == 0 && CxAP210ARMUtilities.countEntityUsers(context, representation) <= 1) {
								representation.deleteApplicationInstance();
								property_definition_representation.deleteApplicationInstance();
							}
						}
					}
				}
			}
		}
	}


	/**
	* Sets/creates data for precedent_feature attribute.
	*
	*  attribute_mapping precedent_feature_part_feature (precedent_feature
	* , (*PATH*), part_feature);
	* 	shape_aspect
	* 	{([shape_aspect =>
	* 	composite_shape_aspect]
	* 	[shape_aspect
	* 	shape_aspect.description = `part group feature'])
	* 	(shape_aspect.description = `part generic feature')
	* 	(shape_aspect.description = `polarity indication feature')
	* 	(shape_aspect.description = `interconnect module cavity surface')
	* 	(shape_aspect.description = `interconnect module cutout surface')
	* 	(shape_aspect.description = `interconnect module edge segment surface')
	* 	(shape_aspect.description = `interconnect module edge surface')
	* 	(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')
	* 	(shape_aspect =>
	* 	physical_unit_datum_feature =>
	* 	primary_orientation_feature)
	* 	(shape_aspect =>
	* 	physical_unit_datum_feature =>
	* 	secondary_orientation_feature)
	* 	(shape_aspect =>
	* 	package_body)
	* 	(shape_aspect =>
	* 	part_tooling_feature)
	* 	(shape_aspect =>
	* 	thermal_feature)
	* 	(shape_aspect =>
	* 	part_mounting_feature)
	* 	(shape_aspect =>
	* 	package_terminal
	* 	{!{package_terminal =>
	* 	primary_reference_terminal}})
	* 	(shape_aspect =>
	* 	assembly_module_terminal)
	* 	(shape_aspect =>
	* 	interconnect_module_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal =>
	* 	bare_die_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal)
	* 	(shape_aspect =>
	* 	packaged_part_terminal)
	* 	(shape_aspect =>
	* 	package_body_surface)}
	* 	shape_aspect <-
	* 	shape_aspect_relationship.related_shape_aspect
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `precedent feature'}
	* 	shape_aspect_relationship.relating_shape_aspect ->
	* 	shape_aspect
	* 	{([shape_aspect =>
	* 	composite_shape_aspect]
	* 	[shape_aspect
	* 	shape_aspect.description = `part group feature'])
	* 	(shape_aspect.description = `part generic feature')
	* 	(shape_aspect.description = `polarity indication feature')
	* 	(shape_aspect.description = `interconnect module cavity surface')
	* 	(shape_aspect.description = `interconnect module cutout surface')
	* 	(shape_aspect.description = `interconnect module edge segment surface')
	* 	(shape_aspect.description = `interconnect module edge surface')
	* 	(shape_aspect.description = `interconnect module primary surface')
	* 	(shape_aspect.description = `interconnect module secondary surface')
	* 	(shape_aspect.description = `interconnect module surface feature')
	* 	(shape_aspect =>
	* 	physical_unit_datum_feature =>
	* 	primary_orientation_feature)
	* 	(shape_aspect =>
	* 	physical_unit_datum_feature =>
	* 	secondary_orientation_feature)
	* 	(shape_aspect =>
	* 	package_body)
	* 	(shape_aspect =>
	* 	part_tooling_feature)
	* 	(shape_aspect =>
	* 	thermal_feature)
	* 	(shape_aspect =>
	* 	part_mounting_feature)
	* 	(shape_aspect =>
	* 	package_terminal)
	* 	(shape_aspect =>
	* 	assembly_module_terminal)
	* 	(shape_aspect =>
	* 	interconnect_module_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal =>
	* 	bare_die_terminal)
	* 	(shape_aspect =>
	* 	minimally_defined_bare_die_terminal)
	* 	(shape_aspect =>
	* 	packaged_part_terminal)
	* 	(shape_aspect =>
	* 	package_body_surface)}
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		//unset old values
		unsetPrecedent_feature(context, armEntity);

		if (armEntity.testPrecedent_feature(null))
		{
			EPart_feature armPrecedent_feature = armEntity.getPrecedent_feature(null);

			//shape_aspect_relationship
			EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
			shape_aspect_relationship.setName(null, "precedent feature");
			shape_aspect_relationship.setRelated_shape_aspect(null, armEntity);
			shape_aspect_relationship.setRelating_shape_aspect(null, armPrecedent_feature);
		}
	}


	/**
	* Unsets/deletes data for precedent_feature attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_feature(SdaiContext context, EPart_feature armEntity) throws SdaiException
	{
		AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, aRelationship);
		EShape_aspect_relationship relationship = null;

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("precedent feature")) {
				relationship.deleteApplicationInstance();
			}
		}
	}

	/**
	 * Sets/creates data for connection_area.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setConnection_area(SdaiContext context,
			EPart_feature armEntity) throws SdaiException {
		CxShape_feature.setConnection_area(context, armEntity);
	}

	/**
	 * Unsets/deletes data for connection_area.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetConnection_area(SdaiContext context,
			EPart_feature armEntity) throws SdaiException {
		CxShape_feature.unsetConnection_area(context, armEntity);
	}
	
	/**
	 * Sets/creates data for definition.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDefinition(SdaiContext context,
			EPlaced_feature_armx armEntity) throws SdaiException {
		CxPlaced_feature_armx.setDefinition(context, armEntity);
	}

	/**
	 * Unsets/deletes data for definition.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetDefinition(SdaiContext context,
			EPlaced_feature_armx armEntity) throws SdaiException {
		CxPlaced_feature_armx.unsetDefinition(context, armEntity);
	}

	/**
	 * Sets/creates data for id_x.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		CxShape_element.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for definition.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);
	}
	
}