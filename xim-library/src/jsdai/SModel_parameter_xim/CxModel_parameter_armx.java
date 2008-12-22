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

package jsdai.SModel_parameter_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SExtended_measure_representation_xim.AValue_range_armx;
import jsdai.SExtended_measure_representation_xim.EValue_range_armx;
import jsdai.SModel_parameter_mim.CDefault_value_property_definition_representation;
import jsdai.SModel_parameter_mim.CModel_parameter;
import jsdai.SModel_parameter_mim.CValid_range_property_definition_representation;
import jsdai.SModel_parameter_mim.EDefault_value_property_definition_representation;
import jsdai.SModel_parameter_mim.EValid_range_property_definition_representation;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;

public class CxModel_parameter_armx extends CModel_parameter_armx implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CModel_parameter.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			// Valid_range
			setValid_range(context, this);

			// Default_value
			setDefault_value(context, this);

			
			// Clean ARM specific attributes - this is DERIVED to some magic string

			// Valid_range
			unsetValid_range(null);

			// Default_value
			unsetDefault_value(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// Valid_range
			unsetValid_range(context, this);

			// Default_value
			unsetDefault_value(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
		product_specific_parameter_value_assignment <=
		[characterized_object]
		[product_related_product_category]
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
			EModel_parameter_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EModel_parameter_armx armEntity) throws SdaiException {
	}


	//********** "model_parameter_armx" attributes

	/**
	* Sets/creates data for default_value attribute.
	*
	* <p>
	attribute_mapping default_value(default_value, $PATH, scalar_or_coordinated_characteristics);
		(model_parameter <=
		general_property
		represented_definition = general_property 
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation
		{property_definition_representation =>
		default_value_property_definition_representation}
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item
		{representation_item
		groupable_item = representation_item
		groupable_item <-
		applied_group_assignment.items[i]
		applied_group_assignment <=
		group_assignment
		group_assignment.assigned_group ->
		group =>
		characteristic_type})
		(model_parameter <=
		general_property
		represented_definition = general_property 
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation
		{property_definition_representation =>
		default_value_property_definition_representation}
		property_definition_representation.used_representation ->
		representation)
	end_attribute_mapping;	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MP <- DVPDR -> R -> RI {<- AGA -> CT}
	public static void setDefault_value(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetDefault_value(context, armEntity);

		if (armEntity.testDefault_value(null))
		{
	      EEntity defaultValue = armEntity.getDefault_value(null);
	      // R
	      ERepresentation er;
	      if(defaultValue instanceof ERepresentation){
	    	  er = (ERepresentation)defaultValue;
	      }else if(defaultValue instanceof ERepresentation_item){
				LangUtils.Attribute_and_value_structure[] repStructure = {new LangUtils.Attribute_and_value_structure(
						CRepresentation.attributeItems(null), defaultValue)};
				er = (ERepresentation)
					LangUtils.createInstanceIfNeeded(context, CRepresentation.definition,
						repStructure);
				if(!er.testName(null)){
					er.setName(null, "");
				}
				if(!er.testContext_of_items(null)){
					  LangUtils.Attribute_and_value_structure[] rcStructure =
						 {
							new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
							new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "")
						};

					  	ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
														context,
														jsdai.SRepresentation_schema.CRepresentation_context.definition,
														rcStructure);
					  	er.setContext_of_items(null, representation_context);
				}
	       // Exception here mean we have unsupported case	    	  
	      }else{
	    	  throw new SdaiException(SdaiException.ED_NVLD, "not valid type of default_value "+defaultValue+" for "+armEntity);
	      }
	      EDefault_value_property_definition_representation edvpdr = (EDefault_value_property_definition_representation)
	      	context.working_model.createEntityInstance(CDefault_value_property_definition_representation.definition);
	      
	      edvpdr.setDefinition(null, armEntity);
	      edvpdr.setUsed_representation(null, er);
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefault_value(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
		SdaiIterator iterAPDR = apdr.createIterator();
		while(iterAPDR.next()){
			EProperty_definition_representation epdr = apdr.getCurrentMember(iterAPDR);
			if(epdr instanceof EDefault_value_property_definition_representation){
				epdr.deleteApplicationInstance();
			}
		}				
	}


	/**
	* Sets/creates data for default_value attribute.
	*
	* <p>
	attribute_mapping valid_range(valid_range, $PATH, Value_range);
		model_parameter <=
		general_property
		represented_definition = general_property 
		represented_definition <-
		property_definition_representation.definition
		property_definition_representation
		{property_definition_representation =>
		valid_range_property_definition_representation}
		property_definition_representation.used_representation ->
		representation
		representation.items[i] ->
		representation_item =>
		compound_representation_item

	end_attribute_mapping;	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MP <- VRPDR -> R -> RI
	public static void setValid_range(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetValid_range(context, armEntity);

		if (armEntity.testValid_range(null))
		{
	      AValue_range_armx valueRanges = armEntity.getValid_range(null);
	      for(int i=1,count=valueRanges.getMemberCount(); i<=count; i++){
	    	  EValue_range_armx valueRange = valueRanges.getByIndex(i); 
		      // R
				LangUtils.Attribute_and_value_structure[] repStructure = {new LangUtils.Attribute_and_value_structure(
						CRepresentation.attributeItems(null), valueRange)};
				ERepresentation er = (ERepresentation)
					LangUtils.createInstanceIfNeeded(context, CRepresentation.definition,
						repStructure);
				if(!er.testName(null)){
					er.setName(null, "");
				}
				if(!er.testContext_of_items(null)){
					  LangUtils.Attribute_and_value_structure[] rcStructure =
						 {
							new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
							new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "")
						};

					  	ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
														context,
														jsdai.SRepresentation_schema.CRepresentation_context.definition,
														rcStructure);
					  	er.setContext_of_items(null, representation_context);
				}
			// VRPDR	
		      EValid_range_property_definition_representation evrpdr = (EValid_range_property_definition_representation)
		      	context.working_model.createEntityInstance(CValid_range_property_definition_representation.definition);
		      
		      evrpdr.setDefinition(null, armEntity);
		      evrpdr.setUsed_representation(null, er);
			}
		}
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetValid_range(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
		SdaiIterator iterAPDR = apdr.createIterator();
		while(iterAPDR.next()){
			EProperty_definition_representation epdr = apdr.getCurrentMember(iterAPDR);
			if(epdr instanceof EValid_range_property_definition_representation){
				epdr.deleteApplicationInstance();
			}
		}				
	}

}