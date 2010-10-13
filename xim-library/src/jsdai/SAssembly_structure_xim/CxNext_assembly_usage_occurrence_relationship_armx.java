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

import jsdai.SGeneric_product_occurrence_xim.EDefinition_based_product_occurrence;
import jsdai.SGeneric_product_occurrence_xim.EProduct_occurrence;
import jsdai.SLayered_interconnect_module_design_mim.EStructured_layout_component_sub_assembly_relationship_with_component;
import jsdai.SLksoft_extensions_xim.EStructured_layout_component_sub_assembly_relationship_with_component_xim;
import jsdai.SPhysical_unit_design_view_mim.CNext_assembly_usage_occurrence_relationship;
import jsdai.SPhysical_unit_design_view_mim.ENext_assembly_usage_occurrence_relationship;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_structure_schema.EProduct_definition_occurrence_relationship;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiSession;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.libutil.XimEntityStandalone;

public class CxNext_assembly_usage_occurrence_relationship_armx extends CNext_assembly_usage_occurrence_relationship_armx implements EMappedXIMEntity, XimEntityStandalone
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
	}
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a8 = set_instanceX(a8, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a8$;
	}
*/	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a6);
	}
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
*/	
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
        setRelated_view(context, this);
        
        // reference_designator
        setReference_designator(context, this);
		
		//processAssemblyTrick(context, this);
		
		// relating_view : product_view_definition;
        unsetRelating_view(null);
		
		// related_view : product_occurrence;		
        unsetRelated_view(null);
		
        // reference_designator
        unsetReference_designator(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// relating_view : product_view_definition;
        unsetRelating_view(context, this);
		
		// related_view : product_occurrence;		
        unsetRelated_view(context, this);
	
        // reference_designator
        unsetReference_designator(context, this);
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
		CxProduct_occurrence_definition_relationship_armx.setMappingConstraints(context, armEntity);
		// AIM gap
		ENext_assembly_usage_occurrence_relationship aimInstance = (ENext_assembly_usage_occurrence_relationship) 
			((XimEntityStandalone)(armEntity)).getAimInstance(context);
		aimInstance.setName((EProduct_definition_occurrence_relationship)null, "");
//		if(!armEntity.testName((EProduct_definition_occurrence_relationship)null)){
//OPT			armEntity.setName((EProduct_definition_occurrence_relationship)null, "");
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.unsetMappingConstraints(context, armEntity);
	}
	
	public static void processAssemblyTrick(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity,
			ENext_assembly_usage_occurrence_relationship aimEntity)throws SdaiException{
//		if(context.working_model.getUnderlyingSchemaString().equalsIgnoreCase("ap210_electronic_assembly_interconnect_and_packaging_design_mim")){
//			return;
//		}
		EProduct_occurrence epd = armEntity.getRelated_view(null);
		// unset for easier further processing (esp. in USEDIN)
		//?? armEntity.unsetRelated_product_definition(null);
		// if here we do not have product_occurrence - it means we already made a trick
		if(epd instanceof EDefinition_based_product_occurrence){
			EDefinition_based_product_occurrence component = (EDefinition_based_product_occurrence)epd;
			// ARM entity is not processed yet, so we can get what we want directly
			EProduct_definition definition = null;
			if(component.testDerived_from(null)){
				definition = component.getDerived_from(null);
			}else{
/*				AProduct_definition_relationship apdr = new AProduct_definition_relationship();
				CProduct_definition_relationship.usedinRelated_product_definition(null, component, context.domain, apdr);
				for(int i=1,count=apdr.getMemberCount(); i<=count; i++){
					EProduct_definition_relationship epdr = apdr.getByIndex(i);
					if((epdr.testName(null))&&(epdr.getName(null).equals("definition usage"))){
						definition = epdr.getRelating_product_definition(null);
						break;
					}
				}
				if(definition == null){
					if(epd instanceof EAssembly_component){
						EAssembly_component eac = (EAssembly_component)epd;
						System.err.println(" EAC "+eac);
						definition = aimEntity.getRelating_product_definition(null);
						if(definition == null){
							System.err.println(" Unsupported case - neither definition available nor mapping is satisfied "+component+" "+apdr.getMemberCount()+" "+aimEntity);
						}
					}
					//armEntity.setOccurrence(null, component);
					return;
				}*/
				SdaiSession.getSession().printlnSession(" Unsupported case - neither definition available nor mapping is satisfied "+component+" "+aimEntity);
				return;
			}
			if(!(aimEntity instanceof EStructured_layout_component_sub_assembly_relationship_with_component)){
				aimEntity.setRelated_product_definition(null, definition);
			}
			//armEntity.setOccurrence(null, component);
		}
	}
	
	public static void setRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		unsetRelated_view(context, armEntity);
		EProduct_occurrence epo = null;
		if(armEntity instanceof EStructured_layout_component_sub_assembly_relationship_with_component_xim){
			epo = (EProduct_occurrence)armEntity;
		}else{
			if(armEntity.testRelated_view(null)){
				epo = armEntity.getRelated_view(null);
			}
		}
		if(epo == null){
			return;
		}
		XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
		ENext_assembly_usage_occurrence_relationship aimInstance = (ENext_assembly_usage_occurrence_relationship)
			ximEntity.getAimInstance(context);
		aimInstance.setOccurrence(null, epo);
		processAssemblyTrick(context, armEntity, aimInstance);
	}
	
	public static void unsetRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
		ENext_assembly_usage_occurrence_relationship aimInstance = (ENext_assembly_usage_occurrence_relationship)
			ximEntity.getAimInstance(context);
		aimInstance.unsetOccurrence(null);
	}


	public static void setRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.setRelating_view(context, armEntity);
	}
	
	public static void unsetRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.unsetRelating_view(context, armEntity);
	}

	ENext_assembly_usage_occurrence_relationship aimInstance;
	
	public EEntity getAimInstance(SdaiContext context) throws SdaiException {
		if(aimInstance == null){
			aimInstance = (ENext_assembly_usage_occurrence_relationship)
				context.working_model.createEntityInstance(CNext_assembly_usage_occurrence_relationship.definition);
		}
		return aimInstance;
	}

	public void unsetAimInstance(SdaiContext context) throws SdaiException{
		aimInstance = null;
	}
	
	public static void setReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		unsetReference_designator(context, armEntity);
		if(armEntity.testReference_designator(null)){
			String designator = armEntity.getReference_designator(null);
			XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
			ENext_assembly_usage_occurrence_relationship aimInstance = (ENext_assembly_usage_occurrence_relationship)
				ximEntity.getAimInstance(context);
			aimInstance.setReference_designator(null, designator);
			aimInstance.setId(null, designator);
		}
	}
	
	public static void unsetReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		XimEntityStandalone ximEntity = (XimEntityStandalone)armEntity;
		ENext_assembly_usage_occurrence_relationship aimInstance = (ENext_assembly_usage_occurrence_relationship)
			ximEntity.getAimInstance(context);
		aimInstance.unsetReference_designator(null);
	}

	
}
