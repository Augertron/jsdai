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

package jsdai.SAssembly_component_placement_requirements_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPhysical_unit_design_view_xim.EAssembly_component_armx;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SRequirement_view_definition_xim.CxRequirement_view_definition;
import jsdai.SAssembly_component_placement_requirements_mim.CGroup_product_definition;
import jsdai.SAssembly_physical_requirement_allocation_xim.AComponent_or_feature;
import jsdai.SComponent_feature_xim.EComponent_feature_armx;

/**
* @author Giedrius Liutkus, Valdas Zigas
* @version $Revision$
*/

public class CxPlacement_group_requirement_definition extends CPlacement_group_requirement_definition implements EMappedXIMEntity
{

	// Taken from property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}
	public void setName(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}*/
	public void unsetName(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a2$;
	}

	// END OF taken from property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CGroup_product_definition.definition);

		setMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		//********** "Requirement_definition_property" attributes
	}

/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	group_product_definition <=
		component_definition <=
		product_definition
		{product_definition
		product_definition.description = 'placement group'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	//  All constraints are anyway set in "associated_definition"
	public static void setMappingConstraints(SdaiContext context, EPlacement_group_requirement_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxRequirement_view_definition.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EProduct_definition)null, "placement group");
		// AIM gap
		AComponent_group_assignment composition = armEntity.getComposition(null, context.domain);
		if(composition.getMemberCount() == 0){
			SdaiSession.getSession().printlnSession("Group does not have a single member via coposition INVERSE "+armEntity);
			return;
		}
		EEntity component_or_feature = composition.getByIndex(1).getAssigned_component(null);
		EAssembly_component_armx component;
		if(component_or_feature instanceof EAssembly_component_armx){
			component = (EAssembly_component_armx)component_or_feature; 
		}else{
			EComponent_feature_armx feature = (EComponent_feature_armx)component_or_feature;
			component = feature.getAssociated_component(null);
		}
		EProduct_definition assembly = component.getOccurrence_contexts(null, null).getByIndex(1).getRelating_view(null);
		armEntity.setRelating_product_definition(null, assembly);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPlacement_group_requirement_definition armEntity) throws SdaiException
	{
		CxRequirement_view_definition.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EProduct_definition)null);
	}
	
	//********** "managed_design_object" attributes

	//********** "requirement_definition_property" attributes

}
