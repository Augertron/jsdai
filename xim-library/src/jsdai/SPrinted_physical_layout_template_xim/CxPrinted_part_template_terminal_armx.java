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
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SGroup_mim.*;
import jsdai.SGroup_schema.*;
import jsdai.SPrinted_physical_layout_template_mim.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPrinted_part_template_terminal_armx extends CPrinted_part_template_terminal_armx implements EMappedXIMEntity{

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
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPrinted_part_template_terminal.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
      // connection_zone_category 
		setConnection_zone_category(context, this);
		
		// Clean ARM
		// Connection_area
		unsetConnection_area(null);
		
      // connection_zone_category 
		unsetConnection_zone_category(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
			
	      // connection_zone_category 
			unsetConnection_zone_category(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  printed_part_cross_section_template_terminal &lt;=
	 *  {[printed_part_template_terminal
	 *  groupable_item = printed_part_template_terminal
	 *  groupable_item &lt;-
	 *  applied_group_assignment.items[i]
	 *  applied_group_assignment &lt;=
	 *  group_assignment
	 *  group_assignment.assigned_group -&gt;
	 *  {group =&gt;
	 *  printed_part_template_Connection_area_category}
	 *  group
	 *  group.name
	 *  {(group.name = 'area edge segment')
	 *  (group.name = 'curve edge segment')}]
	 *  [printed_part_template_terminal &lt;=
	 *  shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  [product_definition
	 *  product_definition.formation -&gt;
	 *  product_definition_formation
	 *  product_definition_formation.of_product -&gt;
	 *  product &lt;-
	 *  product_related_product_category.products[i]
	 *  product_related_product_category &lt;=
	 *  product_category
	 *  product_category.name = 'template model']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'template definition']]}
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
			EPrinted_part_template_terminal_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPrinted_part_template_terminal_armx armEntity) throws SdaiException {
		CxShape_feature.unsetMappingConstraints(context, armEntity);
	}

	//********** "printed_part_template_terminal" attributes

	/**
	* Sets/creates data for connection_zone_category attribute.
	*
	* <p>
	*  attribute_mapping connection_zone_category (connection_zone_category
	* , group.name);
	* 	printed_part_template_terminal
	* 	group_assigned_item = printed_part_template_terminal
	* 	group_assigned_item <-
	* 	applied_group_assignment.items[i]
	* 	applied_group_assignment <=
	* 	group_assignment
	* 	group_assignment.assigned_group ->
	* 	{group =>
	* 	printed_part_template_terminal_connection_zone_category}
	* 	group
	* 	group.name
	* 	{(group.name = `area edge segment')
	* 	(group.name = `curve edge segment')
	* 	(group.name = `surface area')
	* 	(group.name = `surface point')}
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_zone_category(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetConnection_zone_category(context, armEntity);
		// System.err.println(armEntity+" Category set "+armEntity.testConnection_zone_category(null));
		if (armEntity.testConnection_zone_category(null))
		{

			int armConnection_zone_category = armEntity.getConnection_zone_category(null);
			String categoryString = EPrinted_part_template_terminal_class.toString(armConnection_zone_category).toLowerCase().replace('_',' '); 

			//applied_group_assignment
		   // Find suitable group
			LangUtils.Attribute_and_value_structure[] groupStructure = {new LangUtils.Attribute_and_value_structure(
				CPrinted_part_template_terminal_connection_zone_category.attributeName(null), categoryString)};
			EPrinted_part_template_terminal_connection_zone_category group = (EPrinted_part_template_terminal_connection_zone_category)
				LangUtils.createInstanceIfNeeded(context, CPrinted_part_template_terminal_connection_zone_category.definition,
				groupStructure);
			
		   // G <- AGA
			LangUtils.Attribute_and_value_structure[] structure = {new LangUtils.Attribute_and_value_structure(
				CApplied_group_assignment.attributeAssigned_group(null), group)};
			EApplied_group_assignment aga = (EApplied_group_assignment)
				LangUtils.createInstanceIfNeeded(context, CApplied_group_assignment.definition,
				structure);
			AGroupable_item items;
			if(aga.testItems(null))
				items = aga.getItems(null);
			else
				items = aga.createItems(null);
			// AGA -> SA
			items.addUnordered(armEntity);

			// System.err.println(aimEntity+" <- "+aga+" -> "+group);
		}
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
		//applied_group_assignment
		EApplied_group_assignment applied_group_assignment = null;
		AApplied_group_assignment aApplied_group_assignment = new AApplied_group_assignment();
		CApplied_group_assignment.usedinItems(null, armEntity, context.domain, aApplied_group_assignment);
		for (int i = 1; i <= aApplied_group_assignment.getMemberCount(); i++) {
			applied_group_assignment = aApplied_group_assignment.getByIndex(i);
			if (applied_group_assignment.testAssigned_group(null)
			   && applied_group_assignment.getAssigned_group(null) instanceof EPrinted_part_template_terminal_connection_zone_category) {
			   EGroup group = applied_group_assignment.getAssigned_group(null);

				while (applied_group_assignment.getItems(null).isMember(armEntity)) {
					applied_group_assignment.getItems(null).removeUnordered(armEntity);
				}

				if (applied_group_assignment.getItems(null).getMemberCount() == 0) {
					applied_group_assignment.deleteApplicationInstance();

					if (CxAP210ARMUtilities.countEntityUsers(context, group) == 0) {
						group.deleteApplicationInstance();
					}
				}
			}
		}
	}


	/**
	* Sets/creates data for Connection_area attribute.
	*
	*/
	public static void setConnection_area(SdaiContext context, EPrinted_part_template_terminal_armx armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
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
		CxShape_feature.unsetConnection_area(context, armEntity);
	}



}