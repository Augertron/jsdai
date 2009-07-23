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

package jsdai.SPhysical_connectivity_definition_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPhysical_component_feature_xim.*;
import jsdai.SPhysical_connectivity_definition_mim.CPhysical_connectivity_definition;
import jsdai.SProduct_property_definition_schema.*;

public class CxPhysical_connectivity_definition_armx extends CPhysical_connectivity_definition_armx implements EMappedXIMEntity
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
	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_shape)a2;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a2$;
	}
	// ENDOF Taken from Shape_aspect
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPhysical_connectivity_definition.definition);

		setMappingConstraints(context, this);

		// associated_definition - made DERIVED
		// setAssociated_definition(context, this);
		
      // associated_terminals 
		setAssociated_terminals(context, this);
		
		// clean ARM
		// associated_definition - made derived
		// unsetAssociated_definition(null);
		
      // associated_terminals 
		unsetAssociated_terminals(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// associated_definition - made DERIVED
		// unsetAssociated_definition(context, this);
		
      // associated_terminals 
		unsetAssociated_terminals(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	physical_connectivity_definition &lt;=
	*  shape_aspect
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
//		if(!armEntity.testProduct_definitional(null))
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
	}
	

	//********** "physical_connectivity_definition" attributes

	/**
	*	Sets/creates data for associated_definition attribute.
	*
	*  (physical_unit_network_definition as associated_definition);
	*
	* 	physical_connectivity_definition <=
	* 	shape_aspect
	* 	shape_aspect.of_shape ->
	* 	product_definition_shape <=
	* 	property_definition
	* 	property_definition.definition ->
	* 	characterized_definition
	* 	characterized_definition = characterized_product_definition
	* 	characterized_product_definition
	* 	characterized_product_definition = product_definition
	* 	product_definition =>
	* 	physical_unit_network_definition
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* - made DERIVED
	public static void setAssociated_definition(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		if (armEntity.testAssociated_definition(null))
		{
			unsetAssociated_definition(context, armEntity);

			EPhysical_unit_network_definition_armx armAssociated_definition = 
							armEntity.getAssociated_definition(null);

				// PDS -> PUND
            LangUtils.Attribute_and_value_structure[] pdS = {new LangUtils.Attribute_and_value_structure(
                    jsdai.SProduct_property_definition_schema.CProperty_definition.attributeDefinition(null), armAssociated_definition)
            };
            jsdai.SProduct_property_definition_schema.EProduct_definition_shape product_definition_shape =
                (jsdai.SProduct_property_definition_schema.EProduct_definition_shape)
                LangUtils.createInstanceIfNeeded(context,jsdai.SProduct_property_definition_schema.CProduct_definition_shape.definition,pdS);
				if(!product_definition_shape.testName(null))
					product_definition_shape.setName(null, "");
				// PCD -> PDS
				armEntity.setOf_shape(null, product_definition_shape);
		}
	}
*/

	/**
	* Unsets/deletes data for associated_definition attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	/* made DERIVED
	public static void unsetAssociated_definition(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{

		if (armEntity.testOf_shape(null))
		{
			//EProduct_definition_shape product_definition_shape = aimEntity.getOf_shape(null);

			armEntity.unsetOf_shape(null);
			// product_definition_shape.unsetDefinition(null);
			// LangUtils.deleteInstanceIfUnused(context.domain, product_definition_shape);
		}
	}
*/

	/**
	* Sets/creates data for associated_terminals attribute.
	*
	*  attribute_mapping associated_terminals_physical_component_terminal
	* (associated_terminals, (*PATH*), physical_component_terminal);
	* 	physical_connectivity_definition <=
	* 	shape_aspect <-
	* 	shape_aspect_relationship.relating_shape_aspect
	* 	shape_aspect_relationship
	* 	{shape_aspect_relationship
	* 	shape_aspect_relationship.name = `associated terminals'}
	* 	shape_aspect_relationship.related_shape_aspect ->
	* 	{shape_aspect
	* 	(shape_aspect.description = `assembly module component terminal')
	* 	(shape_aspect.description = `bare die component terminal')
	* 	(shape_aspect.description = `interconnect component join terminal')
	* 	(shape_aspect.description = `interconnect module component terminal')
	* 	(shape_aspect.description = `packaged component join terminal')
	* 	}
	* 	shape_aspect =>
	* 	component_terminal
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAssociated_terminals(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		if (armEntity.testAssociated_terminals(null))
		{
			APhysical_component_terminal_armx armAssociated_terminals = armEntity.getAssociated_terminals(null);

			//unset old values
			unsetAssociated_terminals(context, armEntity);

			for (int i = 1; i <= armAssociated_terminals.getMemberCount(); i++)
			{
				EPhysical_component_terminal_armx armTerminal = armAssociated_terminals.getByIndex(i);

				EShape_aspect_relationship sar = (EShape_aspect_relationship)
								(context.working_model.createEntityInstance(CShape_aspect_relationship.definition));

				sar.setName(null, "associated terminals");
				sar.setRelating_shape_aspect(null, armEntity);
				sar.setRelated_shape_aspect(null, armTerminal);
			}
		}
	}


	/**
	* Unsets/deletes data for associated_terminals attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetAssociated_terminals(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		AEntity results = new AEntity();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, results);
		EShape_aspect_relationship sar = null;

		final String KEYWORD = "associated terminals";

		for (int i = 1; i <= results.getMemberCount(); i++)
		{
			sar = (jsdai.SProduct_property_definition_schema.EShape_aspect_relationship)
					results.getByIndexEntity(i);

			if (sar.testName(null)){
				if (!KEYWORD.equals(sar.getName(null))){
					continue;
				}
			}

			if (sar.testRelated_shape_aspect(null)){
				sar.deleteApplicationInstance();
			} else {
				continue;
			}

		}
	}
	
	
}
