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
import jsdai.SPhysical_connectivity_definition_mim.CPhysical_connectivity_definition;
import jsdai.SProduct_property_definition_schema.*;

public class CxPhysical_connectivity_structure_definition extends CPhysical_connectivity_structure_definition implements EMappedXIMEntity
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
		// associated_definition - made DERIVED
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
	public static void setMappingConstraints(SdaiContext context, EPhysical_connectivity_structure_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPhysical_connectivity_definition_armx.setMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		CxPhysical_connectivity_definition_armx.unsetMappingConstraints(context, armEntity);
	}
	

	//********** "physical_connectivity_definition" attributes

	/**
	* Sets/creates data for associated_terminals attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setAssociated_terminals(SdaiContext context, EPhysical_connectivity_definition_armx armEntity) throws SdaiException
	{
		CxPhysical_connectivity_definition_armx.setAssociated_terminals(context, armEntity);
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
		CxPhysical_connectivity_definition_armx.unsetAssociated_terminals(context, armEntity);		
	}
	
	
}
