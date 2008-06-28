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

package jsdai.SLocation_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SLocation_mim.AApplied_location_representation_assignment;
import jsdai.SLocation_mim.ALocation_representation_item;
import jsdai.SLocation_mim.CApplied_location_representation_assignment;
import jsdai.SLocation_mim.EApplied_location_representation_assignment;
import jsdai.SLocation_schema.CLocation;
import jsdai.SManagement_resources_schema.CLocation_representation_role;
import jsdai.SManagement_resources_schema.ELocation_representation_role;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxLocation_armx extends CLocation_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLocation.definition);

		// alternative_location_representations : OPTIONAL SET [1:?] OF location_representation;
		setAlternative_location_representations(context, this);

		unsetAlternative_location_representations(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// alternative_location_representations : OPTIONAL SET [1:?] OF location_representation;
		unsetAlternative_location_representations(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ELocation_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, ELocation_armx armEntity) throws SdaiException
	{
	}
	
	/**
	* Sets/creates data for alternative_location_representations attribute.
	*
	* <p>
	attribute_mapping alternative_location_representations(alternative_location_representations, ($PATH)($PATH)($PATH)($PATH)($PATH), Location_representation);
	(
						location
						location <-
						location_representation_assignment.represented_location
						location_representation_assignment =>
						applied_location_representation_assignment
						applied_location_representation_assignment.items[i] ->
						location_representation_item =
						representation
						{representation.name='global location representation'}
					)( 
						location <-
						location_representation_assignment.represented_location
						location_representation_assignment =>
						applied_location_representation_assignment
						{applied_location_representation_assignment
						applied_location_representation_assignment.items[i] ->
						location_representation_item =
						organization}					
					)(
						location <-
						location_representation_assignment.represented_location
						location_representation_assignment =>
						applied_location_representation_assignment
						applied_location_representation_assignment.items[i] ->
						location_representation_item =
						address
					)(
						location <-
						location_representation_assignment.represented_location
						location_representation_assignment =>
						applied_location_representation_assignment
						{applied_location_representation_assignment
						applied_location_representation_assignment.items[i] ->
						location_representation_item =
						product}
					)(
						location =>
					characterized_location_object <=
					characterized_object =
					characterized_definition <-
					property_definition.definition
					property_definition =
					represented_definition <-
					property_definition_representation.definition
					property_definition_representation.used_representation ->
					representation
					{representation.name='regional grid location representation'}
					)
	end_attribute_mapping; 
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// L <- ALRA -> R 
	// L <- ALRA -> O
	// L <- ALRA -> A
	// L <- ALRA -> P
	// L <- PD <- PDR -> R
	public static void setAlternative_location_representations(SdaiContext context, ELocation_armx armEntity) throws SdaiException
	{
		unsetAlternative_location_representations(context, armEntity);
		if(armEntity.testAlternative_location_representations(null)){
			ALocation_representation alr = armEntity.getAlternative_location_representations(null);
			ALocation_representation_item items = null;
			for(int i=1,count=alr.getMemberCount(); i<=count; i++){
				EEntity elr = alr.getByIndex(i);
				if(elr instanceof ERegional_grid_location_representation){
					// PD
					LangUtils.Attribute_and_value_structure[] pdStructure =
					{new LangUtils.Attribute_and_value_structure(
						CProperty_definition.attributeDefinition(null),
						armEntity)
					};
					EProperty_definition epd = (EProperty_definition)
						LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure);
					if(!epd.testName(null)){
						epd.setName(null, "");
					}
					// PDR
					EProperty_definition_representation epdr = (EProperty_definition_representation)
						context.working_model.createEntityInstance(CProperty_definition_representation.definition);
					epdr.setDefinition(null, epd);
					epdr.setUsed_representation(null, (ERepresentation)elr);
				}else{
					if(items == null){
						EApplied_location_representation_assignment ealra = (EApplied_location_representation_assignment)
							context.working_model.createEntityInstance(CApplied_location_representation_assignment.definition);
						ealra.setRepresented_location(null, armEntity);
						items = ealra.createItems(null);
						// Role - AIM gap
						LangUtils.Attribute_and_value_structure[] roleStructure =
						{new LangUtils.Attribute_and_value_structure(
							CLocation_representation_role.attributeId(null),
							""),
						 new LangUtils.Attribute_and_value_structure(
							CLocation_representation_role.attributeName(null),
							"")							
						};
						ELocation_representation_role elrr = (CLocation_representation_role)
							LangUtils.createInstanceIfNeeded(context, CLocation_representation_role.definition, roleStructure);
						ealra.setRole(null, elrr);
					}
					items.addUnordered(elr);
				}
			}
		}
	}

	public static void unsetAlternative_location_representations(SdaiContext context, ELocation_armx armEntity) throws SdaiException
	{
		// Case 1 
		AApplied_location_representation_assignment aalra = new AApplied_location_representation_assignment();
		CApplied_location_representation_assignment.usedinRepresented_location(null, armEntity, context.domain, aalra);
		for(int i=1,count=aalra.getMemberCount(); i<= count; i++){
			EApplied_location_representation_assignment ealra = aalra.getByIndex(i);
			ealra.deleteApplicationInstance();
		}
		// Case 2
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int i=1,count=apd.getMemberCount(); i<= count; i++){
			EProperty_definition epd = apd.getByIndex(i);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			for(int j=1,count2=apdr.getMemberCount(); j<= count2; j++){
				EProperty_definition_representation epdr = apdr.getByIndex(j);
				epdr.deleteApplicationInstance();
			}
		}		
	}
	
}
