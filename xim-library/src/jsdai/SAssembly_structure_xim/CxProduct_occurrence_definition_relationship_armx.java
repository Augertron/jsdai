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

package jsdai.SAssembly_structure_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPhysical_unit_design_view_mim.CNext_assembly_usage_occurrence_relationship;
import jsdai.SProduct_structure_schema.CProduct_definition_usage;
import jsdai.SProduct_structure_schema.EAssembly_component_usage;
import jsdai.SProduct_structure_schema.EProduct_definition_usage;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;

public class CxProduct_occurrence_definition_relationship_armx extends CProduct_occurrence_definition_relationship_armx implements EMappedXIMEntity, XimEntityStandalone
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CNext_assembly_usage_occurrence_relationship.definition);

		setMappingConstraints(context, this);
		
		// relating_view : product_view_definition;
        setRelating_view(context, this);
		
		// related_view : product_occurrence;		
        // setRelated_view(context, this);
        
//		processAssemblyTrick(context, this);
		
		// relating_view : product_view_definition;
		unsetRelating_view(null);
				
		// related_view : product_occurrence;		
		unsetRelated_view(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// relating_view : product_view_definition;
		unsetRelating_view(context, this);
				
		// related_view : product_occurrence;		
		// unsetRelated_view(context, this);
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		EAssembly_component_usage aimInstance = (EAssembly_component_usage) 
			((XimEntityStandalone)(armEntity)).getAimInstance(context);
		aimInstance.setId(null, "");
		aimInstance.setName(null, "");
		
//		if(!armEntity.testName((EProduct_definition_occurrence_relationship)null)){
//OPT			armEntity.setName((EProduct_definition_occurrence_relationship)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
	}
	
//	public static void processAssemblyTrick(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity)throws SdaiException{
//		if(context.working_model.getUnderlyingSchemaString().equalsIgnoreCase("ap210_electronic_assembly_interconnect_and_packaging_design_mim")){
//			return;
//		}
/*OPT		EProduct_definition epd = armEntity.getRelated_product_definition(null);
		// unset for easier further processing (esp. in USEDIN)
		armEntity.unsetRelated_product_definition(null);
		// if here we do not have product_occurrence - it means we already made a trick
		if(epd instanceof EDefinition_based_product_occurrence){
			EDefinition_based_product_occurrence component = (EDefinition_based_product_occurrence)epd;
			// ARM entity is not processed yet, so we can get what we want directly
			EProduct_definition definition = null;
			if(component.testDerived_from(null)){
				definition = component.getDerived_from(null);
			}else{
				AProduct_definition_relationship apdr = new AProduct_definition_relationship();
				CProduct_definition_relationship.usedinRelated_product_definition(null, component, context.domain, apdr);
				for(int i=1,count=apdr.getMemberCount(); i<=count; i++){
					EProduct_definition_relationship epdr = apdr.getByIndex(i);
					if((epdr.testName(null))&&(epdr.getName(null).equals("definition usage"))){
						definition = epdr.getRelating_product_definition(null);
						break;
					}
				}
				if(definition == null){
					System.err.println(" Unsupported case - neither definition available nor mapping is satisfied "+component+" "+apdr.getMemberCount());
					armEntity.setOccurrence(null, component);
					return;
				}
			}
			armEntity.setRelated_product_definition(null, definition);
			armEntity.setOccurrence(null, component);
		} OPT*/ 
//	}

	private EProduct_definition_usage aimInstance;
	
	public EEntity getAimInstance(SdaiContext context) throws SdaiException {
		if(aimInstance == null){
			aimInstance = (EProduct_definition_usage)
				context.working_model.createEntityInstance(CProduct_definition_usage.definition);
		}
		return aimInstance;
	}
	
	public void unsetAimInstance(SdaiContext context) throws SdaiException{
		aimInstance = null;
	}
	
/*	
	public static void setRelated_view(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
		unsetRelated_view(context, armEntity);
		if(armEntity.testRelated_view(null)){
			EProduct_occurrence epo = armEntity.getRelated_view(null);
			XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
			EProduct_definition_usage aimInstance = (EProduct_definition_usage)ximEntity.getAimInstance(context);
			aimInstance.setRelated_product_definition(null, epo);
		}
	}

	public static void unsetRelated_view(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
		XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
		EProduct_definition_usage aimInstance = (EProduct_definition_usage)ximEntity.getAimInstance(context);
		aimInstance.unsetRelated_product_definition(null);
	}
*/
	public static void setRelating_view(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
		unsetRelating_view(context, armEntity);
		if(armEntity.testRelating_view(null)){
			EProduct_view_definition epvd = armEntity.getRelating_view(null);
			XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
			EProduct_definition_usage aimInstance = (EProduct_definition_usage)ximEntity.getAimInstance(context);
			aimInstance.setRelating_product_definition(null, epvd);
		}
	}

	public static void unsetRelating_view(SdaiContext context, EProduct_occurrence_definition_relationship_armx armEntity) throws SdaiException
	{
		XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
		EProduct_definition_usage aimInstance = (EProduct_definition_usage)ximEntity.getAimInstance(context);
		aimInstance.unsetRelating_product_definition(null);
	}
	
	
}
