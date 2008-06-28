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

package jsdai.SPrinted_physical_layout_template_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SPart_template_2d_shape_xim.ETemplate_location_in_structured_template;
import jsdai.SPrinted_physical_layout_template_mim.*;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;

public class CxStructured_printed_part_template_terminal_armx extends CStructured_printed_part_template_terminal_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStructured_printed_part_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		setAssociated_composition_relationship(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);

		unsetAssociated_composition_relationship(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);

			unsetAssociated_composition_relationship(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_part_template_terminal &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'join terminal'}
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
			EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxPrinted_part_template_terminal_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException {
		CxPrinted_part_template_terminal_armx.unsetMappingConstraints(context, armEntity);
	}

	//********** "printed_part_template_terminal" attributes

	/**
	* Sets/creates data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_zone_category(context, armEntity);		
	}


	/**
	* Unsets/deletes data for connection_zone_category attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_zone_category(context, armEntity);
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.setConnection_area(context, armEntity);		
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxPrinted_part_template_terminal_armx.unsetConnection_area(context, armEntity);		
	}

	/**
	* Sets/creates data for Associated_composition_relationship attribute.
	*
	*attribute_mapping associated_composition_relationship(associated_composition_relationship, $PATH, Template_location_in_structured_template);
		structured_printed_part_template_terminal <=
		printed_part_template_terminal <=
		shape_aspect
		shape_definition = shape_aspect
		shape_definition
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition
		property_definition_relationship
		{property_definition_relationship
		property_definition_relationship.name = 'associated composition relationship'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition
		characterized_definition = characterized_product_definition
		characterized_product_definition
		characterized_product_definition = product_definition_relationship
		{product_definition_relationship
		product_definition_relationship.name = 'tlist'}
		product_definition_relationship =>
		product_definition_usage =>
		assembly_component_usage
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SPPTT <- PD <- PDR -> PD -> ACU
	public static void setAssociated_composition_relationship(SdaiContext context, EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException
	{
		unsetAssociated_composition_relationship(context, armEntity);
		if(armEntity.testAssociated_composition_relationship(null)){
			ETemplate_location_in_structured_template location = armEntity.getAssociated_composition_relationship(null);
			// PD -> ACU
			LangUtils.Attribute_and_value_structure[] pd1Structure =
			{
				new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), location)
			};
			EProperty_definition epd1 = (EProperty_definition) 
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pd1Structure);
			if(!epd1.testName(null)){
				epd1.setName(null, "");
			}
			// SPPTT <- PD
			LangUtils.Attribute_and_value_structure[] pd2Structure =
			{
				new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), armEntity)
			};
			EProperty_definition epd2 = (EProperty_definition) 
				LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pd2Structure);
			if(!epd2.testName(null)){
				epd2.setName(null, "");
			}
			// PDR
			EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			epdr.setRelated_property_definition(null, epd2);
			epdr.setRelating_property_definition(null, epd1);
			epdr.setName(null, "associated composition relationship");
			epdr.setDescription(null, "");
		}
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_composition_relationship(SdaiContext context, EStructured_printed_part_template_terminal_armx armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterAPD = apd.createIterator();
		while(iterAPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterAPD);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
			SdaiIterator iterAPDR = apdr.createIterator();
			while(iterAPDR.next()){
				EProperty_definition_relationship epdr = apdr.getCurrentMember(iterAPDR);
				if((epdr.testName(null))&&(epdr.getName(null).equals("associated composition relationship"))){
					epdr.deleteApplicationInstance();
				}
			}			
		}
				
	}
	
}