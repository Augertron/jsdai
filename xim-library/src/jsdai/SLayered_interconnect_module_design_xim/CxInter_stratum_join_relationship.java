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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_interconnect_module_design_mim.CJoin_shape_aspect;
import jsdai.SProduct_property_definition_schema.*;

public class CxInter_stratum_join_relationship extends CInter_stratum_join_relationship implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Shape_aspect
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a0$;
	}

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

		setTemp("AIM", CJoin_shape_aspect.definition);

		setMappingConstraints(context, this);
		
		// Points_to_be_connected
		setPoints_to_be_connected(context, this);
		
		// Clean ARM
		// Points_to_be_connected
		unsetPoints_to_be_connected(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// Points_to_be_connected
		unsetPoints_to_be_connected(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	join_shape_aspect &lt;=
	*  shape_aspect
	*  {shape_aspect
	*  (shape_aspect.name = 'unrouted join')
	*  (shape_aspect.name = 'constrained intra layer join')
	*  (shape_aspect.name = 'inter stratum join')
	*  (shape_aspect.name = 'stratum embedded component join')
	*  (shape_aspect.name = 'intra stratum join')}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EInter_stratum_join_relationship armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxJoin_relationship.setMappingConstraints(context, armEntity);
		armEntity.setName((EShape_aspect)null, "inter stratum join");
	}

	public static void unsetMappingConstraints(SdaiContext context, EInter_stratum_join_relationship armEntity) throws SdaiException
	{
		CxJoin_relationship.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName((EShape_aspect)null);
	}


	//********** "inter_stratum_join_relationship" attributes

	/**
	* Sets/creates data for points_to_be_connected attribute.
	*
	* <p>
	*  attribute_mapping points_to_be_connected_layer_connection_point (points_to_be_connected
	* , (*PATH*), layer_connection_point);
	* 	join_shape_aspect <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `connected point'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	shape_aspect =>
	* 	layer_connection_point
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// JSA <- SAR -> LCP
	public static void setPoints_to_be_connected(SdaiContext context, EInter_stratum_join_relationship armEntity) throws SdaiException
	{
		//unset old values
		unsetPoints_to_be_connected(context, armEntity);

		if (armEntity.testPoints_to_be_connected(null))
		{

			ALayer_connection_point_armx armPoints_to_be_connected = armEntity.getPoints_to_be_connected(null);
			for (int i = 1; i <= armPoints_to_be_connected.getMemberCount(); i++){
				// LCP
				ELayer_connection_point_armx armPoint = armPoints_to_be_connected.getByIndex(i);
				// SAR
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.class);
				sar.setName(null, "connected point");
				sar.setRelated_shape_aspect(null, armPoint);
				sar.setRelating_shape_aspect(null, armEntity);
			}
			// Fill AIM GAPS
			// This is now solved at join_relationship.associated_layout
/*			if(armPoints_to_be_connected.getMemberCount() > 0){
		      EStratum stratum = armPoints_to_be_connected.getByIndex(1).getResident_design_layer_stratum(null);
				// Get PCB
				AInterconnect_module_stratum_assembly_relationship relationships =
					new AInterconnect_module_stratum_assembly_relationship();
				CInterconnect_module_stratum_assembly_relationship.usedinComponent(null,
					stratum, null, relationships);
				if(relationships.getMemberCount() > 0){
					EInterconnect_module pcb = relationships.getByIndex(1).getAssembly(null);
					pcb.createAimData(context);
					jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit
					   pcbAIM = (jsdai.SElectronic_assembly_interconnect_and_packaging_design.EPhysical_unit)
					   pcb.getAimInstance();
					LangUtils.Attribute_and_value_structure[] structure = {new LangUtils.Attribute_and_value_structure(
						CProduct_definition_shape.attributeDefinition(null), pcbAIM)};
					EProduct_definition_shape epds = (EProduct_definition_shape)
						LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition, structure);
				   if(!epds.testName(null))
						epds.setName(null, "");
					// Finally the link we wanted - JSA -> PDS -> PCB
					aimEntity.setOf_shape(null, epds);
				}
			}*/

		}
	}


	/**
	* Unsets/deletes data for points_to_be_connected attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPoints_to_be_connected(SdaiContext context, EInter_stratum_join_relationship armEntity) throws SdaiException
	{
		AShape_aspect_relationship relationships = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, relationships);
		for(int i=1;i<=relationships.getMemberCount();i++){
			EShape_aspect_relationship relationship = relationships.getByIndex(i);
			if((relationship.testName(null))&&(relationship.getName(null).equals("connected point")))
				relationship.deleteApplicationInstance();
		}

	}
	
	
}
