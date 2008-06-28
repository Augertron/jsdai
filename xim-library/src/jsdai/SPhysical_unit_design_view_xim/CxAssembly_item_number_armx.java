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

package jsdai.SPhysical_unit_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SPhysical_unit_design_view_mim.CAssembly_item_number;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_structure_schema.*;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SRepresentation_schema.*;

public class CxAssembly_item_number_armx extends CAssembly_item_number_armx implements EMappedXIMEntity
{

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a0$;
	}

	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_item_number.definition);

		setMappingConstraints(context, this);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{[descriptive_representation_item &lt;=
	*  representation_item				
	*  representation_item.name = 'item find number']
	*  [descriptive_representation_item &lt;=
	*  representation_item &lt;-
	*  representation.items[i]
	*  representation.context_of_items -&gt;
	*  representation_context.context_type = 'item find number representation context']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	
	public static void setMappingConstraints(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		/* Made redeclared after changed in SEDSZILLA #2368		
		armEntity.setName(null, "item find number");
		// Create representation only once
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, armEntity, context.domain, ar);
		// No further checking is done
		if(ar.getMemberCount() > 0)
			return;
		// create new Representation
		// New Context
		  LangUtils.Attribute_and_value_structure[] rcStructure =
			 {
				new LangUtils.Attribute_and_value_structure(
						jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), 
						"item find number representation context")
			};

		  ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
											context,
											jsdai.SRepresentation_schema.CRepresentation_context.definition,
											rcStructure);
		if(!representation_context.testContext_identifier(null))
			representation_context.setContext_identifier(null, "");
		ERepresentation er = (ERepresentation)context.working_model.createEntityInstance(CRepresentation.definition);
		er.setContext_of_items(null, representation_context);
		er.createItems(null).addUnordered(armEntity);
		er.setName(null, "");*/
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
	//	armEntity.unsetName(null);
	}


	//********** "assembly_item_number" attributes

	/**
	* Sets/creates data for assembly_usage attribute.
	*
	* <p>
	*  attribute_mapping assembly_usage_assembly_composition_relationship (assembly_usage
	* , (*PATH*), assembly_composition_relationship);
	* 	descriptive_representation_item <= 
	* 	representation_item <- 
	* 	representation.items[i] 
	* 	representation <- 
	* 	property_definition_representation.used_representation 
	* 	property_definition_representation.definition -> 
	* 	property_definition 
	* 	{property_definition.name = `item find number'} 
	* 	property_definition.definition -> 
	* 	product_definition_relationship => 
	* 	{product_definition_relationship 
	* 	product_definition_usage => 
	* 	assembly_component_usage 
	*  end_attribute_mapping; 
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	/* Made redeclared after changed in SEDSZILLA #2368	
	public static void setAssembly_usage(SdaiContext context, EAssembly_item_number armEntity) throws SdaiException
	{
		//unset old values
		unsetAssembly_usage(context, armEntity);
		
		if (armEntity.testAssembly_usage(null))
		{ 
			// GET ER
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, armEntity, context.domain, ar);
			SdaiIterator iterR = ar.createIterator();
			ERepresentation er = null; 
			while(iterR.next()){
				ERepresentation erCandidate = ar.getCurrentMember(iterR);
				ERepresentation_context erc = erCandidate.getContext_of_items(null);
				if(erc.getContext_type(null).equals("item find number representation context")){
					er = erCandidate;
					break;
				}
			}
			if(er == null)
				throw new SdaiException(SdaiException.EI_NVLD, "DRI must have representation with context name /'item find number representation context/' pointing to it "+armEntity);
			AAssembly_component_usage armAssembly_usages = armEntity.getAssembly_usage(null);
			SdaiIterator iter = armAssembly_usages.createIterator();
			while(iter.next()){
				EAssembly_component_usage armAssembly_usage = armAssembly_usages.getCurrentMember(iter);
				// PD -> ACU
				LangUtils.Attribute_and_value_structure[] pdS = {
					new LangUtils.Attribute_and_value_structure(
						jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null),
						armAssembly_usage),
					new LangUtils.Attribute_and_value_structure(
						jsdai.SProduct_property_definition_schema.CProperty_definition.attributeName(null),
						"item find number")						
				};
				jsdai.SProduct_property_definition_schema.EProperty_definition pd =
					(jsdai.SProduct_property_definition_schema.EProperty_definition)
				LangUtils.createInstanceIfNeeded(context, jsdai.SProduct_property_definition_schema.CProperty_definition.definition,pdS);
				//PDR->PD
				jsdai.SProduct_property_representation_schema.EProperty_definition_representation 
					pdr = (jsdai.SProduct_property_representation_schema.EProperty_definition_representation)
					context.working_model.createEntityInstance(jsdai.SProduct_property_representation_schema.CProperty_definition_representation.definition);
				pdr.setDefinition(null,pd);
				pdr.setUsed_representation(null, er);
			}
		}
	}
*/

	/**
	* Unsets/deletes data for assembly_usage attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
/* Made redeclared after changed in SEDSZILLA #2368	
	public static void unsetAssembly_usage(SdaiContext context, EAssembly_item_number armEntity) throws SdaiException
	{
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, armEntity, context.domain, ar);
		SdaiIterator iterR = ar.createIterator();
		ERepresentation er = null; 
		while(iterR.next()){
			ERepresentation erCandidate = ar.getCurrentMember(iterR);
			ERepresentation_context erc = erCandidate.getContext_of_items(null);
			if(erc.getContext_type(null).equals("item find number representation context")){
				er = erCandidate;
				break;
			}
		}
		if(er == null)
			return;
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinUsed_representation(null, er, context.domain, apdr);
		for(int i=1;i<=apdr.getMemberCount();i++){
			EProperty_definition_representation epdr = apdr.getByIndex(i);
			EEntity definition = epdr.getDefinition(null);
			if(definition instanceof EProperty_definition){
				EProperty_definition epd = (EProperty_definition)definition;
				if(epd.getName(null).equals("item find number")){
					epdr.deleteApplicationInstance();
				}
			}
		}
	}
*/	
	
}
