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

package jsdai.SAssembly_module_usage_view_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SAssembly_module_usage_view_mim.CLayered_assembly_module_usage_view;
import jsdai.SExtended_geometric_tolerance_xim.EDatum_based_length_measure;
import jsdai.SPackage_xim.CxPackage_armx;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxLayered_assembly_module_usage_view_armx
		extends
		CLayered_assembly_module_usage_view_armx implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition

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
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}

	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}*/

	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instanceX(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
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
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLayered_assembly_module_usage_view.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// From Item_shape
			setId_x(context, this);

			// SETTING DERIVED
			// setDefinition(null, this);
			
			// implemented_function
			setImplemented_function(context, this);

	      // maximum_negative_component_height
			setMaximum_negative_component_height(context, this);
			
	      // maximum_positive_component_height
			setMaximum_positive_component_height(context, this);
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			unsetId_x(null);
			unsetImplemented_function(null);
			
			unsetMaximum_negative_component_height(null);
			unsetMaximum_positive_component_height(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes

			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is DERIVED to some magic string
			// unsetAdditional_characterization(context, this);

			//id_x
			unsetId_x(context, this);
			
			// unsetDefinition(null);
			
			unsetImplemented_function(context, this);

	      // maximum_negative_component_height
			unsetMaximum_negative_component_height(context, this);
			
	      // maximum_positive_component_height
			unsetMaximum_positive_component_height(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; physical_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.name = 'assembly module']
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
			ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxAssembly_module_usage_view_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException {
		CxAssembly_module_usage_view_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "design_discipline_item_definition" attributes
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		//unset old values
		CxItem_shape.setId_x(context, armEntity);
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
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);	
	}


	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * <p>
	 * attribute_mapping additional_context_application_context
	 * (additional_context , $PATH, application_context); product_definition <-
	 * product_definition_context_association.definition
	 * product_definition_context_association
	 * {product_definition_context_association.role -> (* Modified by Audronis
	 * Gustas *) product_definition_context_role
	 * product_definition_context_role.name = 'additional context'}
	 * product_definition_context_association.frame_of_reference ->
	 * product_definition_context end_attribute_mapping;
	 * 
	 * </p>
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
	//********** "assembly_module_usage_view" attributes

	/**
	* Sets/creates data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplemented_function(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		CxAssembly_module_usage_view_armx.setImplemented_function(context, armEntity);		
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		CxAssembly_module_usage_view_armx.unsetImplemented_function(context, armEntity);		
	}

	/**
	* Sets/creates data for maximum_negative_component_height attribute.
	*
	* <p>
	*  attribute_mapping maximum_negative_component_height_datum_based_length_measure (maximum_negative_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	(physical_unit <=
	* 	product_definition)
	* 	(externally_defined_physical_unit <=
	* 	[externally_defined_product_definition]
	* 	[physical_unit <=
	* 	product_definition])
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	[externally_defined_product_definition]
	* 	[physical_unit <=
	* 	product_definition])
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
	* 	{representation
	* 	representation.name = `maximum negative component height'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_negative_component_height(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_negative_component_height(context, armEntity);

		if (armEntity.testMaximum_negative_component_height(null))
		{
			EDatum_based_length_measure armMaximum_negative_component_height = armEntity.getMaximum_negative_component_height(null);
			CxPackage_armx.setRepresentation_characteristic(context, armEntity, "maximum negative component height", armMaximum_negative_component_height);
		}
	}


	/**
	* Unsets/deletes data for maximum_negative_component_height attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_negative_component_height(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		CxPackage_armx.unsetRepresentation_characteristic(context, armEntity, "maximum negative component height");
/*		//property_definition
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
					if(representation instanceof EDatum_based_length_measure){
						continue;
					}
					if (representation.testName(null) && representation.getName(null).equals("maximum negative component height")) {
						property_definition_representation.deleteApplicationInstance();
					}
				}
			}
		}*/
	}


	/**
	* Sets/creates data for maximum_positive_component_height attribute.
	*
	* <p>
	*  attribute_mapping maximum_positive_component_height_datum_based_length_measure (maximum_positive_component_height
	* , (*PATH*), datum_based_length_measure);
	* 	(physical_unit <=
	* 	product_definition)
	* 	(externally_defined_physical_unit <=
	* 	[externally_defined_product_definition]
	* 	[physical_unit <=
	* 	product_definition])
	* 	(library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=
	* 	[externally_defined_product_definition]
	* 	[physical_unit <=
	* 	product_definition])
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
	* 	{representation.name = `maximum positive component height'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_positive_component_height(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetMaximum_positive_component_height(context, armEntity);

		if (armEntity.testMaximum_positive_component_height(null))
		{
			EDatum_based_length_measure armMaximum_positive_component_height = armEntity.getMaximum_positive_component_height(null);
			CxPackage_armx.setRepresentation_characteristic(context, armEntity, "maximum positive component height", armMaximum_positive_component_height);
		}
	}


	/**
	* Unsets/deletes data for maximum_positive_component_height attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_positive_component_height(SdaiContext context, ELayered_assembly_module_usage_view_armx armEntity) throws SdaiException
	{
		CxPackage_armx.unsetRepresentation_characteristic(context, armEntity, "maximum positive component height");		
	}

	/**
	 * Relates representation with product_definition in following path:
	 * product_defintition <- property_definition <- property_definition_representation -> representation
	 * @param product_defintion
	 * @param representation ERepresentation.
	 * @return found representation.
	 * @throws SdaiException
	 */
	public static void relateRepresentation(SdaiContext context, jsdai.SProduct_definition_schema.EProduct_definition product_definition, jsdai.SRepresentation_schema.ERepresentation representation) throws SdaiException {

		//property_definition
	   LangUtils.Attribute_and_value_structure[] propertyStructure =
												 {new LangUtils.Attribute_and_value_structure(
													  jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null), product_definition),
												};
	   jsdai.SProduct_property_definition_schema.EProperty_definition property_definition  = (jsdai.SProduct_property_definition_schema.EProperty_definition)
																 LangUtils.createInstanceIfNeeded(context, jsdai.SProduct_property_definition_schema.CProperty_definition.definition, propertyStructure);
		if (!property_definition.testName(null)) {
			property_definition.setName(null, "");
		}

		//property_definition_representation
	   jsdai.SProduct_property_representation_schema.EProperty_definition_representation property_definition_representation  = (jsdai.SProduct_property_representation_schema.EProperty_definition_representation) context.working_model.createEntityInstance(jsdai.SProduct_property_representation_schema.EProperty_definition_representation.class);
	   property_definition_representation.setDefinition(null, property_definition);
	   property_definition_representation.setUsed_representation(null, representation);
	}
	
	

}