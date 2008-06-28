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
import jsdai.SGeneric_product_occurrence_xim.EDefinition_based_product_occurrence;
import jsdai.SPhysical_unit_design_view_mim.CNext_assembly_usage_occurrence_relationship;
import jsdai.SProduct_definition_schema.AProduct_definition_relationship;
import jsdai.SProduct_definition_schema.CProduct_definition_relationship;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_structure_schema.EProduct_definition_occurrence_relationship;

public class CxNext_assembly_usage_occurrence_relationship_armx extends CNext_assembly_usage_occurrence_relationship_armx implements EMappedXIMEntity
{

	// Taken from Product_definition_occurrence_relationship
	// attribute (current explicit or supertype explicit) : occurrence, base type: entity product_definition
/*	public static int usedinOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_instance(a8);
	}
	public jsdai.SProduct_definition_schema.EProduct_definition getOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return (jsdai.SProduct_definition_schema.EProduct_definition)get_instance(a8);
	}*/
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a8 = set_instance(a8, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a8$;
	}
	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(EProduct_definition_occurrence_relationship type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a6$;
	}
	// END of taken from PDOR
	
	public int attributeState = ATTRIBUTES_MODIFIED;	


	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CNext_assembly_usage_occurrence_relationship.definition);

		setMappingConstraints(context, this);
		
		processAssemblyTrick(context, this);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EProduct_definition_occurrence_relationship)null)){
			armEntity.setName((EProduct_definition_occurrence_relationship)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
	}
	
	public static void processAssemblyTrick(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity)throws SdaiException{
//		if(context.working_model.getUnderlyingSchemaString().equalsIgnoreCase("ap210_electronic_assembly_interconnect_and_packaging_design_mim")){
//			return;
//		}
		EProduct_definition epd = armEntity.getRelated_product_definition(null);
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
		}
	}
}
