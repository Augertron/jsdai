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

package jsdai.SProcess_property_assignment_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProcess_property_representation_schema.AAction_property_representation;
import jsdai.SProcess_property_representation_schema.CAction_property_representation;
import jsdai.SProcess_property_representation_schema.EAction_property_representation;
import jsdai.SProcess_property_schema.CAction_property;
import jsdai.SProduct_property_definition_schema.AGeneral_property_association;
import jsdai.SProduct_property_definition_schema.CGeneral_property_association;
import jsdai.SProduct_property_definition_schema.EGeneral_property;
import jsdai.SProduct_property_definition_schema.EGeneral_property_association;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SSupport_resource_schema.EText;

public class CxApplied_independent_activity_property extends CApplied_independent_activity_property implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAction_property.definition);

			setMappingConstraints(context, this);

			// base_element_property : general_property;
			setBase_element_property(context, this);

			// Clean ARM specific attributes - this is DERIVED to some magic string
			setRepresentation(context, this);
			
			// base_element_property : general_property;
			unsetBase_element_property(null);

			// Clean ARM specific attributes - this is DERIVED to some magic string
			unsetRepresentation(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// base_element_property : general_property;
			unsetBase_element_property(context, this);

			// Clean ARM specific attributes - this is DERIVED to some magic string
			unsetRepresentation(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
			action_property
			{action_property = derived_property_select
			derived_property_select <- 
			general_property_association.derived_definition
			general_property_association}
	   end_mapping_constraints;
	   These constraints are satisfied in base_element_property attribute mapping.
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EApplied_independent_activity_property armEntity) throws SdaiException {
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
			EApplied_independent_activity_property armEntity) throws SdaiException {
	}


	//********** "interconnect_module_usage_view" attributes

	/**
	* Sets/creates data for Base_element_property attribute.
	*
	* <p>
		attribute_mapping base_element_property(base_element_property, $PATH, general_property);
			action_property
			action_property = derived_property_select
			derived_property_select <- 
			general_property_association.derived_definition
			general_property_association
			general_property_association.base_definition ->
			general_property
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// AP <- GPA -> GP
	public static void setBase_element_property(SdaiContext context, EApplied_independent_activity_property armEntity) throws SdaiException
	{
		//unset old values
		unsetBase_element_property(context, armEntity);

		if (armEntity.testBase_element_property(null))
		{
	      EGeneral_property egp = armEntity.getBase_element_property(null);
	      // GPA
	      EGeneral_property_association egpa = (EGeneral_property_association)
	      	context.working_model.createEntityInstance(CGeneral_property_association.definition);
	      egpa.setDerived_definition(null, armEntity);
	      egpa.setBase_definition(null, egp);
	      egpa.setName(null, "");
		}
	}


	/**
	* Unsets/deletes data for considered_instance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetBase_element_property(SdaiContext context, EApplied_independent_activity_property armEntity) throws SdaiException
	{
		AGeneral_property_association agpa = new AGeneral_property_association();
		CGeneral_property_association.usedinDerived_definition(null, armEntity, context.domain, agpa);
		SdaiIterator iter = agpa.createIterator();
		while(iter.next()){
			EGeneral_property_association egpa = agpa.getCurrentMember(iter);
			egpa.deleteApplicationInstance();
		}
	}

	/**
	* Sets/creates data for representation attribute.
	*
	* <p>
	(* XIM specific add-on *)
	attribute_mapping representation(representation, $PATH, measure_with_unit);
		action_property <-
		action_property_representation.property
		action_property_representation
		action_property_representation.representation -> 
		representation
		representation.items[i] ->
		representation_item
		representation_item =>
		measure_representation_item <=
		measure_with_unit
	end_attribute_mapping;
	attribute_mapping representation(representation, $PATH);
		action_property <-
		action_property_representation.property
		action_property_representation
		action_property_representation.representation -> 
		representation
		representation.items[i] ->
		representation_item
		{representation_item =>
		descriptive_representation_item}
		representation_item.name
	end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// AP <- APR -> R -> RI
	public static void setRepresentation(SdaiContext context, EApplied_independent_activity_property armEntity) throws SdaiException
	{
		//unset old values
		unsetRepresentation(context, armEntity);

		if(armEntity.testRepresentation(null) != 0){
			ERepresentation er;
			if(armEntity.testRepresentation(null) == 1){
				ERepresentation_item eri = (ERepresentation_item)armEntity.getRepresentation(null);
				ARepresentation ar = new ARepresentation();
				CRepresentation.usedinItems(null, eri, context.domain, ar);
				if(ar.getMemberCount() > 0){
					er = ar.getByIndex(1);
				}else{
					er = CxAP210ARMUtilities.createRepresentation(context, "", false);
					er.createItems(null).addUnordered(eri);
				}
			// simple text - need to create representation	
			}else{
				EDescriptive_representation_item edri = (EDescriptive_representation_item)
					context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
				String value = armEntity.getRepresentation(null, (EText)null);
				edri.setName(null, value);
				edri.setDescription(null, "");
				er = CxAP210ARMUtilities.createRepresentation(context, "", false);
				er.createItems(null).addUnordered(edri);
			}
	      // APR
	      EAction_property_representation epr = (EAction_property_representation)
	      	context.working_model.createEntityInstance(CAction_property_representation.definition);
	      epr.setProperty(null, armEntity);
	      epr.setRepresentation(null, er);
	      epr.setName(null, "");
	      epr.setDescription(null, "");
		}
	}


	/**
	* Unsets/deletes data for considered_instance attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetRepresentation(SdaiContext context, EApplied_independent_activity_property armEntity) throws SdaiException
	{
		AAction_property_representation apr = new AAction_property_representation();
		CAction_property_representation.usedinProperty(null, armEntity, context.domain, apr);
		SdaiIterator iter = apr.createIterator();
		while(iter.next()){
			EAction_property_representation epr = apr.getCurrentMember(iter);
			// Should be sufficiently constraint to be able to delete it.
			epr.deleteApplicationInstance();
		}
	}
	
}