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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SGeometry_schema.ECartesian_point;
import jsdai.SLayered_interconnect_module_design_mim.CLayout_junction;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxLayout_junction_armx extends CLayout_junction_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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
	
	// ENDOF From CShape_aspect.java
	

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLayout_junction.definition);

		setMappingConstraints(context, this);
		
		// Net
		setNet(context, this);
		
		// Location
		setLocation(context, this);
	
		// Clean ARM
		unsetNet(null);
		unsetLocation(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Net
			unsetNet(context, this);
			
			// Location
			unsetLocation(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; layout_junction &lt;=
	 *  shape_aspect
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ELayout_junction_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
//		if(!armEntity.testProduct_definitional(null))
		armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELayout_junction_armx armEntity) throws SdaiException {
	}


	//********** "layout_junction_armx" attributes

	/**
	* Sets/creates data for net attribute.
	*
	attribute_mapping net(net, $PATH, Generic_physical_network);
		layout_junction <=
		shape_aspect <-
		shape_aspect_relationship.related_shape_aspect
		shape_aspect_relationship
		{shape_aspect_relationship
		shape_aspect_relationship.name = 'net'}
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect =>
		physical_network
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// LJ <- SAR -> PN
	public static void setNet(SdaiContext context, ELayout_junction_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetNet(context, armEntity);
		if (armEntity.testNet(null))
		{
			EGeneric_physical_network net = armEntity.getNet(null);
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, armEntity);
			esar.setRelating_shape_aspect(null, net);
			esar.setName(null, "net");
		}
	}


	 private static void unsetNet(SdaiContext context, ELayout_junction_armx armEntity) throws
	         SdaiException {
		 AShape_aspect_relationship asar = new AShape_aspect_relationship();
		 CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		 for(int i=1,count=asar.getMemberCount(); i<= count; i++){
			 EShape_aspect_relationship esar = asar.getByIndex(i);
			 if((esar.testName(null))&&(esar.getName(null).equals("net"))){
				 esar.deleteApplicationInstance();
			 }
		 }
	 }


		/**
		* Sets/creates data for net attribute.
		*
		attribute_mapping location(location, $PATH, Cartesian_point);
			layout_junction <=
			shape_aspect
			shape_definition = shape_aspect
			shape_definition
			characterized_definition = shape_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			{[representation =>
			shape_representation]
			[representation
			representation.name = 'layout junction location']}
			representation
			representation.items[i] ->
			representation_item =>
			geometric_representation_item =>
			point =>
			cartesian_point
		end_attribute_mapping;
		* @param context SdaiContext.
		* @param armEntity arm entity.
		* @throws SdaiException
		*/
		// LJ <- PD <- PDR -> SR -> CP
		public static void setLocation(SdaiContext context, ELayout_junction_armx armEntity) throws SdaiException
		{
			//unset old values
			unsetLocation(context, armEntity);
			if (armEntity.testLocation(null))
			{
				ECartesian_point point = armEntity.getLocation(null);
				// PD
		         LangUtils.Attribute_and_value_structure[] pdStructure =
		           {new LangUtils.Attribute_and_value_structure(
		           CProperty_definition.attributeDefinition(null),
		           armEntity)};
		         EProperty_definition epd = (EProperty_definition)
		            LangUtils.createInstanceIfNeeded(context,
		            		CProperty_definition.definition,
		                    pdStructure);
		         if(!epd.testName(null))
		            epd.setName(null, "");
		         // R
					LangUtils.Attribute_and_value_structure[] repStructure = {
							new LangUtils.Attribute_and_value_structure(
									CShape_representation.attributeItems(null), point),
							new LangUtils.Attribute_and_value_structure(
									CShape_representation.attributeName(null), "layout junction location")};
					EShape_representation rep = (EShape_representation)
						LangUtils.createInstanceIfNeeded(context, CShape_representation.definition,
							repStructure);
					if(!rep.testContext_of_items(null)){
						  LangUtils.Attribute_and_value_structure[] rcStructure =
							 {
								new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_identifier(null), ""),
								new LangUtils.Attribute_and_value_structure(jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), "")
							};

						  	ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
															context,
															jsdai.SRepresentation_schema.CRepresentation_context.definition,
															rcStructure);
						  	rep.setContext_of_items(null, representation_context);
					}
				// PDR
				EProperty_definition_representation epdr = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				epdr.setDefinition(null, epd);
				epdr.setUsed_representation(null, rep);
			}
		}
	 
	
	/**
	* Unsets/deletes data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
		// LJ <- PD <- PDR -> SR -> CP		
	public static void unsetLocation(SdaiContext context, ELayout_junction_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition(); 
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int k=1;k<=apd.getMemberCount();k++){
			EProperty_definition property_definition = apd.getByIndex(k);
			AProperty_definition_representation apdr = new AProperty_definition_representation(); 
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, apdr);
			for(int v=1;v<=apdr.getMemberCount();v++){				
				EProperty_definition_representation property_definition_representation = apdr.getByIndex(v);
				ERepresentation representation = 
					property_definition_representation.getUsed_representation(null);
				if(!(representation.getName(null).equals("layout junction location")))
					continue;
				property_definition_representation.deleteApplicationInstance();
			}
		}
	}
}