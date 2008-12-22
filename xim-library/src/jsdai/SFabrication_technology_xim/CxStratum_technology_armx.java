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
import jsdai.SCharacteristic_xim.*;
import jsdai.SFabrication_technology_mim.CStratum_technology;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.CxItem_shape;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_property_definition_schema.*;


public class CxStratum_technology_armx extends CStratum_technology_armx implements EMappedXIMEntity
{

	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a0$;
	}
	// ENDOF From CProperty_definition.java

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

        // minimum_aspect_ratio : OPTIONAL REAL;
		// TODO setMinimum_aspect_ratio(context, this);		
		
		// Added after modularization - made derived
		// setSurface_specification(context, this);

		// Clean ARM
		unsetStratum_thickness(null);
		unsetMinimum_finished_feature_size(null);
		unsetLaminate_stiffness_class(null);
		unsetMinimum_finished_feature_spacing(null);
		unsetMaximum_feature_size_requirement(null);
		unsetLayer_position(null);
        // minimum_aspect_ratio : OPTIONAL REAL;
		unsetMinimum_aspect_ratio(null);		
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

        // minimum_aspect_ratio : OPTIONAL REAL;
		// TODO unsetMinimum_aspect_ratio(context, this);		
		
		// Added after modularization - made derived
		// unsetSurface_specification(context, this);

		
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
	public static void setMappingConstraints(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		//CxManaged_design_object.setMappingConstraints(context, armEntity);
		unsetMappingConstraints(context, armEntity);
		CxItem_shape.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		CxItem_shape.unsetMappingConstraints(context, armEntity);
	}
	
	
	//********** "managed_design_object" attributes

	//********** "stratum_technology" attributes
/* This is now inverse
	
	 Sets/creates data for stratum_material attribute.
	*
	* <p>
	*  attribute_mapping stratum_material_ee_material (stratum_material
	* , (*PATH*), ee_material);
	* 	stratum_technology <=
	* 	characterized_object
	* 	characterized_definition = characterized_object
	* 	characterized_definition <-
	* 	material_designation.definitions[i]
	* 	material_designation
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	/
	public static void setStratum_material(SdaiContext context, EStratum_technology armEntity) throws SdaiException
	{
		//unset old values
		unsetStratum_material(context, armEntity);

		if (armEntity.testStratum_material(null))
		{

			jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology aimEntity =
                    (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology) armEntity.getAimInstance();
			jsdai.SAp210_arm_extended.EEe_material armEm = (jsdai.SAp210_arm_extended.EEe_material) armEntity.getStratum_material(null);
            armEm.createAimData(context);
            jsdai.SMaterial_property_definition_schema.EMaterial_designation md = (jsdai.SMaterial_property_definition_schema.EMaterial_designation) armEm.getAimInstance();
				ACharacterized_definition acd;
				if(md.testDefinitions(null))
					acd = md.getDefinitions(null);
				else
               acd = md.createDefinitions(null);
				acd.addUnordered(aimEntity);
		}
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
		if (armEntity.testStratum_material(null))
		{

			jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology aimEntity =
                    (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EStratum_technology) armEntity.getAimInstance();

            jsdai.SMaterial_property_definition_schema.AMaterial_designation aMd = new jsdai.SMaterial_property_definition_schema.AMaterial_designation();
            jsdai.SMaterial_property_definition_schema.EMaterial_designation md = null;
            jsdai.SMaterial_property_definition_schema.CMaterial_designation.usedinDefinitions(null,aimEntity,context.domain,aMd);
            if (aMd.getMemberCount()>=1)
                md = aMd.getByIndex(1);
            else
                return;
            if (!md.testDefinitions(null))
                return;

            AEntity items = md.getDefinitions(null);
            EEntity item = null;
            int i = 1;
            while (i <= items.getMemberCount())
            {
                item = items.getByIndexEntity(i);
                if (item.equals(aimEntity))
                {
                    items.removeUnordered(item);
                }
                else
                {
                    i++;
                }
            }
		}
	}
*/

	/**
	* Sets/creates data for stratum_thickness attribute.
	*
	* <p>
	attribute_mapping stratum_thickness(stratum_thickness, $PATH, Length_tolerance_characteristic);
		stratum_technology <=
		product_definition_shape <=
		property_definition <-
		property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation ->
		representation
		{representation
		representation.description = 'length tolerance'}
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setStratum_thickness(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		  unsetStratum_thickness(context, armEntity);
       if (armEntity.testStratum_thickness(null)){

	      ETolerance_characteristic characteristic = armEntity.getStratum_thickness(null);
	      
	      // CxAP210ARMUtilities.setProperty_definitionToRepresentationPath(context, armEntity, null, "stratum thickness", characteristic);
			
			//property_definition_representation
	      EProperty_definition epd = setPropertDefinition(armEntity, "stratum thickness", null);
		  EProperty_definition_representation property_definition_representation  = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
		   
		  property_definition_representation.setDefinition(null, epd);
		  property_definition_representation.setUsed_representation(null, characteristic);

		}
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
		// CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "allowed component terminal extent");
		// MIGHT be too strong - need to test
		unsetPropertDefinitionRepresentation(context.domain, armEntity, "stratum thickness");
	}

	/**
	* Sets/creates data for minimum_finished_feature_size attribute.
	*
	* <p>
	*  attribute_mapping minimum_finished_feature_size_length_data_element (minimum_finished_feature_size
	* , (*PATH*), length_data_element);
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
	* 	{representation
	* 	representation.name = `physical characteristics representation'}
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum finished feature size'}
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
	public static void setMinimum_finished_feature_size(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
         //unset old values
        unsetMinimum_finished_feature_size(context, armEntity);
        if (armEntity.testMinimum_finished_feature_size(null)){

			jsdai.SMeasure_schema.ELength_measure_with_unit armLde = armEntity.getMinimum_finished_feature_size(null);
				// RI - Giedrius' code
			//EA armLde.createAimData(context);
				CLength_measure_with_unit$measure_representation_item newInstance;
				if(!(armLde instanceof CLength_measure_with_unit$measure_representation_item)){
					newInstance = (CLength_measure_with_unit$measure_representation_item)
						context.working_model.substituteInstance(armLde, jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item.class);
					if(!newInstance.testName(null)){
						newInstance.setName(null, "");
					}
				}
				else{
					newInstance = (CLength_measure_with_unit$measure_representation_item)armLde;
				}
				setRepresentation_item(context, armEntity, newInstance, "minimum finished feature size");
		}
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
	 CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "minimum finished feature size");
	}


	/**
	* Sets/creates data for laminate_stiffness_class attribute.
	*
	* <p>
	*  attribute_mapping laminate_stiffness_class (laminate_stiffness_class
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
	* 	{representation
	* 	representation.name = `physical characteristics representation'}
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `laminate stiffness class'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `fluid like')
	* 	(descriptive_representation_item.description = `conformal coat')
	* 	(descriptive_representation_item.description = `stiff laminate')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLaminate_stiffness_class(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetLaminate_stiffness_class(context, armEntity);

		if (armEntity.testLaminate_stiffness_class(null))
		{
			int armValue = armEntity.getLaminate_stiffness_class(null);
            String description = EStiffness_class.toString(armValue).toLowerCase().replace('_', ' ');
            setPropertDefinition(armEntity, "laminate stiffness class", description);
		}
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
		unsetPropertDefinition(context.domain, armEntity, "laminate stiffness class");		
	}


	/**
	* Sets/creates data for minimum_finished_feature_spacing attribute.
	*
	* <p>
	*  attribute_mapping minimum_finished_feature_spacing_length_data_element (minimum_finished_feature_spacing
	* , (*PATH*), length_data_element);
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
	* 	{representation
	* 	representation.name = `physical characteristics representation'}
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `minimum finished feature spacing'}
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
	public static void setMinimum_finished_feature_spacing(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException{
      //unset old values
      unsetMinimum_finished_feature_spacing(context, armEntity);

      if (armEntity.testMinimum_finished_feature_spacing(null)){

			jsdai.SMeasure_schema.ELength_measure_with_unit armLde = armEntity.getMinimum_finished_feature_spacing(null);

				// RI - Giedrius' code
			//EA armLde.createAimData(context);
				CLength_measure_with_unit$measure_representation_item newInstance;
				if(!(armLde instanceof CLength_measure_with_unit$measure_representation_item)){
				   newInstance = (CLength_measure_with_unit$measure_representation_item)
						context.working_model.substituteInstance(armLde, jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item.class);
					if(!newInstance.testName(null)){
						newInstance.setName(null, "");
					}
				}
				else{
					newInstance = (CLength_measure_with_unit$measure_representation_item)armLde;
				}
				setRepresentation_item(context, armEntity, newInstance, "minimum finished feature spacing");
		}
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
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "minimum finished feature spacing");
	}


	/**
	* Sets/creates data for maximum_feature_size_requirement attribute.
	*
	* <p>
	*  attribute_mapping maximum_feature_size_requirement_length_data_element (maximum_feature_size_requirement
	* , (*PATH*), length_data_element);
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
	* 	{representation
	* 	representation.name = `physical characteristics representation'}
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `maximum feature size requirement'}
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
	public static void setMaximum_feature_size_requirement(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
        //unset old values
        unsetMaximum_feature_size_requirement(context, armEntity);
        if (armEntity.testMaximum_feature_size_requirement(null)){

			jsdai.SMeasure_schema.ELength_measure_with_unit armLde = armEntity.getMaximum_feature_size_requirement(null);

				// RI - Giedrius' code
			//EA armLde.createAimData(context);
				CLength_measure_with_unit$measure_representation_item newInstance;
				ELength_measure_with_unit instance = (ELength_measure_with_unit)armLde.getTemp("AIM");//EA .getAimInstance();
				if(!(instance instanceof CLength_measure_with_unit$measure_representation_item)){
				   newInstance = (CLength_measure_with_unit$measure_representation_item)
						context.working_model.substituteInstance(instance, jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item.class);
					armLde.setTemp("AIM", newInstance);
					if(!newInstance.testName(null)){
						newInstance.setName(null, "");
					}
					//EA armLde.setAimInstance(newInstance);
				}
				else{
					newInstance = (CLength_measure_with_unit$measure_representation_item)instance;
				}
				setRepresentation_item(context, armEntity, newInstance, "maximum feature size requirement");
		}
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
		CxAP210ARMUtilities.unsetProperty_definitionToRepresentationPath(context, armEntity, "maximum feature size requirement");
	}


	/**
	* Sets/creates data for specification attribute.
	*
	* <p>
	*  attribute_mapping specification_process_specification (specification
	* , (*PATH*), process_specification);
	* 	stratum_technology &lt;=
		characterized_object
		document_reference_item = characterized_object
		document_reference_item &lt;-
		applied_document_reference.items[i]
		applied_document_reference
		applied_document_reference &lt;=
		document_reference
		document_reference.assigned_document -&gt;
		document &lt;-
		document_product_association.relating_document
		{document_product_association.name = 'equivalence'}
		document_product_association.related_product -&gt;
		product_or_formation_or_definition = product_definition
		product_definition
		{product_definition.name = 'process specification'}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* made derived
	public static void setSpecification(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
        //unset old values
        unsetSpecification(context, armEntity);

        if (armEntity.testSpecification(null))
		{

            EProcess_specification armPs = armEntity.getSpecification(null);
            CxAP210ARMUtilities.assignDocument_definition(context, armPs, armEntity);
		}
	}
*/

	/**
	* Unsets/deletes data for specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* made derived
	public static void unsetSpecification(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		if (armEntity.testSpecification(null))
        {
			CxAP210ARMUtilities.unAssignDocument_definition(context, armEntity, "process specification");			
        }
	}
*/
    private static void setRepresentation_item(SdaiContext context, 
    		EStratum_technology_armx armEntity,
            jsdai.SRepresentation_schema.ERepresentation_item item,
            String name)throws SdaiException{

    	String repName = "physical characteristics representation";
    	ARepresentation representations = new ARepresentation();
    	CRepresentation.usedinItems(null, item, context.domain, representations);
    	jsdai.SRepresentation_schema.ERepresentation suitableRepresentation = null;
    	if(representations.getMemberCount() > 0){
    		for(int i=1,count=representations.getMemberCount(); i<=count; i++){
    			ERepresentation rep = representations.getByIndex(i);
    			if((rep.testName(null))&&(rep.getName(null).equals(repName))){
    				suitableRepresentation = rep;
    				break;
    			}
    		}
    	}
    	if(suitableRepresentation == null){
    		suitableRepresentation = (jsdai.SRepresentation_schema.ERepresentation)
            	context.working_model.createEntityInstance(jsdai.SRepresentation_schema.CRepresentation.definition);
    		suitableRepresentation.setName(null, repName);
    		// Context
    		jsdai.SRepresentation_schema.ERepresentation_context representation_context =
    			CxAP210ARMUtilities.createRepresentation_context(context,
                                                                       "", "", true);
    		suitableRepresentation.setContext_of_items(null, representation_context);
    		suitableRepresentation.createItems(null).addUnordered(item);
    	}
    	// PD
        EProperty_definition propDef = (EProperty_definition)
    		context.working_model.createEntityInstance(CProperty_definition.definition);
        propDef.setDefinition(null, armEntity);
        propDef.setName(null, name);
    	// PDR
        EProperty_definition_representation propDefRep = (EProperty_definition_representation)
        	context.working_model.createEntityInstance(CProperty_definition_representation.class);
        propDefRep.setDefinition(null, propDef);
        propDefRep.setUsed_representation(null, suitableRepresentation);
    }

	public static jsdai.SRepresentation_schema.ERepresentation getSuitable_representation(SdaiContext context, EEntity aimEntity, String keyword)throws SdaiException{
		AProperty_definition props = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, aimEntity, context.domain, props);
		for(int i=1;i<=props.getMemberCount();i++){
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, props.getByIndex(i), context.domain, apdr);
			for(int j=1;j<=apdr.getMemberCount();j++){
				if(apdr.getByIndex(j).testUsed_representation(null)){
					ERepresentation er = apdr.getByIndex(j).getUsed_representation(null);
					if((er.testName(null))&&(er.getName(null).equals(keyword))){
						return er;
					}
				}
			}
		}
		return null;
	}

////////////////////
// WD20 - moved from design_layer_technology
////////////////////
	public static void setLayer_position(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetLayer_position(context, armEntity);

		if (armEntity.testLayer_position(null))
		{
			int armDesign_layer_position = armEntity.getLayer_position(null);
			String value = ELayer_position_type.toString(armDesign_layer_position).toLowerCase().replace('_', ' ');
			setPropertDefinition(armEntity, "layer position", value);
		}
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
		String keyword = "layer position";
		unsetPropertDefinition(context.domain, armEntity, keyword);
	}

	// Added since modularization
	/**
	* Sets/creates data for surface_specification attribute.
	*
	* <p>
	*  attribute_mapping surface_specification_surface_finish_specification (surface_specification
	* , (*PATH*), surface_finish_specification);
	* 	stratum_technology &lt;=
		characterized_object
		document_reference_item = characterized_object
		document_reference_item &lt;-
		applied_document_reference.items[i]
		applied_document_reference
		applied_document_reference &lt;=
		document_reference
		document_reference.assigned_document -&gt;
		document &lt;-
		document_product_association.relating_document
		{document_product_association.name = 'equivalence'}
		document_product_association.related_product -&gt;
		product_or_formation_or_definition = product_definition
		product_definition
		{product_definition.name = 'surface finish specification'}	
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// ST <- ADR -> D <- DPA -> PD
	/* made derived
	public static void setSurface_specification(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
        //unset old values
        unsetSurface_specification(context, armEntity);

        if (armEntity.testSpecification(null))
		{
            ESurface_finish_specification armSfs = armEntity.getSurface_specification(null);
            CxAP210ARMUtilities.assignDocument_definition(context, armSfs, armEntity);
		}
	}
*/

	/**
	* Unsets/deletes data for specification attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* made derived
	public static void unsetSurface_specification(SdaiContext context, EStratum_technology_armx armEntity) throws SdaiException
	{
		if (armEntity.testSurface_specification(null))
        {
			CxAP210ARMUtilities.unAssignDocument_definition(context, armEntity, "surface finish specification");
        }
	}
	*/
	
	static EProperty_definition setPropertDefinition(EEntity definition, String name, String description)throws SdaiException{
		SdaiModel model = definition.findEntityInstanceSdaiModel();
		EProperty_definition epd = (EProperty_definition)model.createEntityInstance(CProperty_definition.definition);
		epd.setDefinition(null, definition);
		epd.setName(null, name);
		if(description != null){
			epd.setDescription(null, description);
		}
		return epd;
	}

	static void unsetPropertDefinition(ASdaiModel domain, EEntity definition, String name)throws SdaiException{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, definition, domain, apd);
		for(int i=1,count=apd.getMemberCount(); i<=count; i++){
			EProperty_definition epd = apd.getByIndex(i);
			if((epd.testName(null))&&(epd.getName(null).equals(name))){
				epd.deleteApplicationInstance();
			}
		}
	}

	private static void unsetPropertDefinitionRepresentation(ASdaiModel domain, EEntity definition, String name)throws SdaiException{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, definition, domain, apd);
		for(int i=1,count=apd.getMemberCount(); i<=count; i++){
			EProperty_definition epd = apd.getByIndex(i);
			if((epd.testName(null))&&(epd.getName(null).equals(name))){
				AProperty_definition_representation apdr = new AProperty_definition_representation();
				CProperty_definition_representation.usedinDefinition(null, epd, domain, apdr);
				for(int j=1,count2=apdr.getMemberCount(); j<=count2; j++){
					apdr.getByIndex(j).deleteApplicationInstance();
				}
				epd.deleteApplicationInstance();
			}
		}
	}
	
	
}
