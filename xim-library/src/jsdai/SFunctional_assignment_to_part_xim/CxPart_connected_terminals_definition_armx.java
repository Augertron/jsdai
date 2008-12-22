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

package jsdai.SFunctional_assignment_to_part_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SFunctional_assignment_to_part_mim.CPart_connected_terminals_definition;
import jsdai.SProduct_property_definition_schema.*;

public class CxPart_connected_terminals_definition_armx extends CPart_connected_terminals_definition_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Shape_aspect
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}

	// ENDOF Taken from Shape_aspect
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPart_connected_terminals_definition.definition);

		setMappingConstraints(context, this);
		
		// connected_terminals 
		setConnected_terminals(context, this);
		
		// Clean ARM
		//	 connected_terminals
		unsetConnected_terminals(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		//	 connected_terminals
		unsetConnected_terminals(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	part_connected_terminals_definition &lt;=
	*  shape_aspect
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPart_connected_terminals_definition_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
//		if(!armEntity.testProduct_definitional(null))
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_connected_terminals_definition_armx armEntity) throws SdaiException
	{
	}

	//********** "part_connected_terminals_definition" attributes

	/**
	* Sets/creates data for connected_terminals attribute.
	*
	* <p>
	*  attribute_mapping connected_terminals_part_terminal (connected_terminals
	* , (*PATH*), part_terminal);
	* 	part_connected_terminals_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name  = `member connected terminal'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	(assembly_module_terminal)
	* 	(minimally_defined_bare_die_terminal)
	* 	(interconnect_module_terminal)
	* 	(packaged_part_terminal)
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping connected_terminals_part_terminal (connected_terminals
	* , (*PATH*), part_terminal);
	* 	part_connected_terminals_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name  = `member connected terminal'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	(assembly_module_terminal)
	* 	(minimally_defined_bare_die_terminal)
	* 	(interconnect_module_terminal)
	* 	(packaged_part_terminal)
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping connected_terminals_part_terminal (connected_terminals
	* , (*PATH*), part_terminal);
	* 	part_connected_terminals_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name  = `member connected terminal'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	(assembly_module_terminal)
	* 	(minimally_defined_bare_die_terminal)
	* 	(interconnect_module_terminal)
	* 	(packaged_part_terminal)
	*  end_attribute_mapping;
	* </p>
	* <p>
	*  attribute_mapping connected_terminals_part_terminal (connected_terminals
	* , (*PATH*), part_terminal);
	* 	part_connected_terminals_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name  = `member connected terminal'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	(assembly_module_terminal)
	* 	(minimally_defined_bare_die_terminal)
	* 	(interconnect_module_terminal)
	* 	(packaged_part_terminal)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PCTD <- SAR -> SA
	public static void setConnected_terminals(SdaiContext context, EPart_connected_terminals_definition_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetConnected_terminals(context, armEntity);

		if (armEntity.testConnected_terminals(null))
		{
			APart_terminal armConnected_terminals = armEntity.getConnected_terminals(null);
			for(int i=1;i<=armConnected_terminals.getMemberCount();i++){
				// SA
				EPart_terminal armConnected_terminal = armConnected_terminals.getByIndex(i);
				// SAR
				jsdai.SProduct_property_definition_schema.EShape_aspect_relationship esar =
					(EShape_aspect_relationship) context.working_model.createEntityInstance(EShape_aspect_relationship.class);
				esar.setName(null, "member connected terminal");
				esar.setDescription(null, "");
				// SAR -> SA
				esar.setRelated_shape_aspect(null, armConnected_terminal);
				// PCTD <- SAR
				esar.setRelating_shape_aspect(null, armEntity);
			}
		}
	}


	/**
	* Unsets/deletes data for connected_terminals attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnected_terminals(SdaiContext context, EPart_connected_terminals_definition_armx armEntity) throws SdaiException
	{
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
			if((relationship.testName(null))&&(relationship.getName(null).equals("member connected terminal"))){
            relationships.removeByIndex(i);
            relationship.deleteApplicationInstance();
         }
         else
            i++;
		}

	}
	
}
