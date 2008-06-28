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
import jsdai.SPrinted_physical_layout_template_mim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPrinted_connector_template_terminal_relationship_armx extends CPrinted_connector_template_terminal_relationship_armx implements EMappedXIMEntity{

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
	// ENDOF From CShape_aspect.java

	// Taken from SAR
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a4);
	}
	public String getName(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setName(EShape_aspect_relationship type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetName(EShape_aspect_relationship type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect_relationship type) throws SdaiException {
		return a4$;
	}
	
	// ENDOF taken from SAR
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPrinted_connector_template_terminal_relationship.definition);

		setMappingConstraints(context, this);
		
		// Connector
		// setConnector(context, this);
		
		// Clean ARM
		// Connection_area
		// unsetConnector(null);
		
      // connection_zone_category 
		// unsetConnector(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			// unsetConnector(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EPrinted_connector_template_terminal_relationship_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EShape_aspect)null)){
			armEntity.setName((EShape_aspect)null, "");
//		}
//		if(!armEntity.testName((EShape_aspect_relationship)null)){
			armEntity.setName((EShape_aspect_relationship)null, "");
//		}
//		if(!armEntity.testProduct_definitional(null)){
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
//		}
		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_connector_template_terminal_relationship_armx armEntity) throws SdaiException {
	}

	//********** "printed_part_template_terminal" attributes

	/**
	* Sets/creates data for Connector attribute.
	*
	* <p>
		attribute_mapping connector(connector, $PATH, Printed_connector_template_armx);
			printed_connector_template_terminal_relationship <=
			shape_aspect <-
			shape_aspect_relationship.related_shape_aspect
			{shape_aspect_relationship
			shape_aspect_relationship.name = 'connector'}
			shape_aspect_relationship.relating_shape_aspect ->
			shape_aspect.of_shape ->
			product_definition_shape =>
			part_template_definition =>
			printed_part_template =>
			printed_connector_template

		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PCTTR <- SAR -> SA -> PCT
	/* No need for it - now we have it redeclared
	public static void setConnector(SdaiContext context, EPrinted_connector_template_terminal_relationship_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetConnector(context, armEntity);
		// System.err.println(armEntity+" Category set "+armEntity.testConnection_zone_category(null));
		if (armEntity.testConnector(null))
		{
			EPrinted_connector_template_armx connector = armEntity.getConnector(null);
			armEntity.setOf_shape(null, connector);
		   // Find suitable SA
			LangUtils.Attribute_and_value_structure[] saStructure = {new LangUtils.Attribute_and_value_structure(
				CShape_aspect.attributeOf_shape(null), connector)};
			EShape_aspect esa = (EShape_aspect)
				LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition,
				saStructure);
			if(!esa.testName(null))
				esa.setName(null, "");
			if(!esa.testProduct_definitional(null))
				esa.setProduct_definitional(null, ELogical.UNKNOWN);
			
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, armEntity);
			esar.setRelating_shape_aspect(null, esa);
			esar.setName(null, "connector");
		}
	}
*/
	/**
	* Unsets/deletes data for connector attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/*	
	public static void unsetConnector(SdaiContext context, EPrinted_connector_template_terminal_relationship_armx armEntity) throws SdaiException
	{
		//applied_group_assignment
		EShape_aspect_relationship esar = null;
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		SdaiIterator iter = asar.createIterator();
		while(iter.next()){
			esar = asar.getCurrentMember(iter);
			if((esar.testName(null))&&(esar.getName(null).equals("connector"))){
				esar.deleteApplicationInstance();
			}
		}
	}
*/	
}