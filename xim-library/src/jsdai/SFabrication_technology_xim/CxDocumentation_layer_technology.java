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

package jsdai.SFabrication_technology_xim;

/**
* @author Vilius Kontrimas, Giedrius Liutkus
* @version $Revision$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFabrication_technology_mim.CStratum_technology;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_property_definition_schema.*;


public class CxDocumentation_layer_technology extends CDocumentation_layer_technology implements EMappedXIMEntity
{

	// Taken from Characterized_object
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(ECharacterized_object type) throws SdaiException {
		return test_string(a5);
	}
	public String getDescription(ECharacterized_object type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setDescription(ECharacterized_object type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetDescription(ECharacterized_object type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(ECharacterized_object type) throws SdaiException {
		return a5$;
	}
	// ENDOF Taken from Characterized_object	

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_technology.definition);

		setMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "stratum_technology" attributes
		//stratum_thickness
		setStratum_thickness(context, this);

		//minimum_finished_feature_size
		setMinimum_finished_feature_size(context, this);

		//laminate_stiffness_class
		setLaminate_stiffness_class(context, this);

		//minimum_finished_feature_spacing
		setMinimum_finished_feature_spacing(context, this);

		//maximum_feature_size_requirement
		setMaximum_feature_size_requirement(context, this);

		//specification - made derived
		// setSpecification(context, this);

		// Added since WD20
		setLayer_position(context, this);

		// Added after modularization - made derived
		// setSurface_specification(context, this);

		// design_layer_purpose
		setPre_defined_documentation_layer_purpose(context, this);
		
		// Clean ARM
		unsetStratum_thickness(null);
		unsetMinimum_finished_feature_size(null);
		unsetLaminate_stiffness_class(null);
		unsetMinimum_finished_feature_spacing(null);
		unsetMaximum_feature_size_requirement(null);
		unsetLayer_position(null);
		unsetPre_defined_documentation_layer_purpose(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "managed_design_object" attributes

		//********** "stratum_technology" attributes
		//stratum_thickness
		unsetStratum_thickness(context, this);

		//minimum_finished_feature_size
		unsetMinimum_finished_feature_size(context, this);

		//laminate_stiffness_class
		unsetLaminate_stiffness_class(context, this);

		//minimum_finished_feature_spacing
		unsetMinimum_finished_feature_spacing(context, this);

		//maximum_feature_size_requirement
		unsetMaximum_feature_size_requirement(context, this);

		//specification - made derived
		// unsetSpecification(context, this);

		// Added since WD20
		unsetLayer_position(context, this);

		// Added after modularization - made derived
		// unsetSurface_specification(context, this);

		unsetPre_defined_documentation_layer_purpose(context, this);
		
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	stratum_technology <=
	* 	characterized_object
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CxDocumentation_layer_technology armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription((ECharacterized_object)null, "documentation layer");
	}

	public static void unsetMappingConstraints(SdaiContext context, CxDocumentation_layer_technology armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((ECharacterized_object)null);
	}
	
	
	//********** "managed_design_object" attributes

	//********** "stratum_technology" attributes
/* This is now inverse
	
	 Sets/creates data for stratum_material attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void setStratum_material(SdaiContext context, EStratum_technology armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setStratum_material(context, armEntity); 
	}


	/
	* Unsets/deletes data for stratum_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	
	public static void unsetStratum_material(SdaiContext context, EStratum_technology armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetStratum_material(context, armEntity);	
	}
*/

	/**
	* Sets/creates data for stratum_thickness attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_thickness(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setStratum_thickness(context, armEntity);
	}


	/**
	* Unsets/deletes data for maximum_stratum_thickness attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetStratum_thickness(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetStratum_thickness(context, armEntity);		
	}

	/**
	* Sets/creates data for minimum_finished_feature_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMinimum_finished_feature_size(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setMinimum_finished_feature_size(context, armEntity);		
	}


	/**
	* Unsets/deletes data for minimum_finished_feature_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMinimum_finished_feature_size(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetMinimum_finished_feature_size(context, armEntity);		
	}


	/**
	* Sets/creates data for laminate_stiffness_class attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLaminate_stiffness_class(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setLaminate_stiffness_class(context, armEntity);		
	}


	/**
	* Unsets/deletes data for laminate_stiffness_class attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLaminate_stiffness_class(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetLaminate_stiffness_class(context, armEntity);		
	}


	/**
	* Sets/creates data for minimum_finished_feature_spacing attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMinimum_finished_feature_spacing(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException{
		CxStratum_technology_armx.setMinimum_finished_feature_spacing(context, armEntity);		
	}


	/**
	* Unsets/deletes data for minimum_finished_feature_spacing attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMinimum_finished_feature_spacing(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetMinimum_finished_feature_spacing(context, armEntity);		
	}


	/**
	* Sets/creates data for maximum_feature_size_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMaximum_feature_size_requirement(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setMaximum_feature_size_requirement(context, armEntity);
	}


	/**
	* Unsets/deletes data for maximum_feature_size_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetMaximum_feature_size_requirement(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetMaximum_feature_size_requirement(context, armEntity);		
	}

////////////////////
// WD20 - moved from design_layer_technology
////////////////////
	public static void setLayer_position(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.setLayer_position(context, armEntity);
	}
	/**
	* Unsets/deletes data for design_layer_position attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLayer_position(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxStratum_technology_armx.unsetLayer_position(context, armEntity);
	}

	// Design_layer_technology
	//********** "design_layer_technology" attributes

	/**
	* Sets/creates data for design_layer_purpose attribute.
	*
	* <p>
	*  attribute_mapping design_layer_purpose (design_layer_purpose
	* , descriptive_representation_item);
	* 	stratum_technology <=
	* 	characterized_object
	* 	characterized_definition = characterized_object
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `layer purpose'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `power or ground')
	* 	(descriptive_representation_item.description = `other signal')
	* 	(descriptive_representation_item.description = `lands only')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// ST <- PD <- PDR -> R -> DRI
	public static void setPre_defined_documentation_layer_purpose(SdaiContext context, EDocumentation_layer_technology armEntity) throws SdaiException
	{
		//unset old values
		unsetPre_defined_documentation_layer_purpose(context, armEntity);

		if (armEntity.testPre_defined_documentation_layer_purpose(null))
		{
			//jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology aimEntity = (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology) armEntity.getAimInstance();
			int armDesign_layer_purpose = armEntity.getPre_defined_documentation_layer_purpose(null);
		   String value = EPredefined_documentation_layer_purpose.toString(armDesign_layer_purpose).toLowerCase().replace('_',' ');
         setDescriptive_representation_item(context, armEntity, "layer purpose", value,
                                            "physical characteristics representation");
		}
	}


	/**
	* Unsets/deletes data for design_layer_purpose attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPre_defined_documentation_layer_purpose(SdaiContext context, EDocumentation_layer_technology armEntity) throws SdaiException
	{
		String keyword = "layer purpose";
		CxAP210ARMUtilities.unsetRepresentationItemFromPropertyDefinition(armEntity, context, null, keyword);
	}


	/**
	* Sets/creates data for pre_defined_documentation_layer_purpose attribute.
	*
	* <p>
	*  attribute_mapping design_layer_position (design_layer_position
	* , descriptive_representation_item);
	* 	characterized_object
	* 	characterized_definition = characterized_object
	* 	characterized_definition &lt;-
	* 	property_definition.definition
	* 	property_definition &lt;-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation -&gt;
	* 	representation
	* 	{representation
	* 	representation.name = 'physical characteristics representation'}
	* 	representation.items[i] -&gt;
	* 	{representation_item
	* 	representation_item.name = 'layer purpose'}
	* 	representation_item =&gt;
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = 'soldermask')
	* 	(descriptive_representation_item.description = 'solderpaste')
	* 	(descriptive_representation_item.description = 'silkscreen')
	* 	(descriptive_representation_item.description = 'generic layer')
	* 	(descriptive_representation_item.description = 'glue')
	* 	(descriptive_representation_item.description = 'gluemask')
	* 	(descriptive_representation_item.description = 'pastemask')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	static void setDescriptive_representation_item(SdaiContext context, EStratum_technology_armx armEntity, String keyword, String value, String representationName)throws SdaiException{
		// Get/create representation
		jsdai.SRepresentation_schema.ARepresentation representations =
			CxAP210ARMUtilities.getAllRepresentationsOfPropertyDefinition(armEntity, context, representationName);
		jsdai.SRepresentation_schema.ERepresentation representation;
		// Take first suitable, since geometry is not likely to happen for technology things
		if(representations.getMemberCount() > 0)
			representation = representations.getByIndex(1);
		else{
			// R
			representation = (jsdai.SRepresentation_schema.ERepresentation)
				context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.class);
         representation.setName(null, representationName);
			// Context
			jsdai.SRepresentation_schema.ERepresentation_context representation_context =
            CxAP210ARMUtilities.createRepresentation_context(context,
                                                                       "", "", true);
			representation.setContext_of_items(null, representation_context);
			// PD
/*			EProperty_definition propDef = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.class);
			propDef.setDefinition(null, armEntity);
			propDef.setName(null, "");*/
			// PDR
			EProperty_definition_representation propDefRep = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.class);
			propDefRep.setDefinition(null, armEntity);
			propDefRep.setUsed_representation(null, representation);
		}
		// Create DRI
		EDescriptive_representation_item edri = (EDescriptive_representation_item)
			context.working_model.createEntityInstance(CDescriptive_representation_item.class);
		edri.setName(null, keyword);
		edri.setDescription(null, value);
		// R -> DRI
		ARepresentation_item items;
		if(representation.testItems(null))
			items = representation.getItems(null);
		else
			items = representation.createItems(null);
		items.addUnordered(edri);
	}
	
}
