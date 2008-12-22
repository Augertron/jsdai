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

package jsdai.SPhysical_unit_design_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SAssembly_structure_xim.ANext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SGroup_mim.AGroupable_item;
import jsdai.SPhysical_unit_design_view_mim.CAssembly_item_number;
import jsdai.SRepresentation_schema.*;

public class CxAssembly_item_number_armx extends CAssembly_item_number_armx implements EMappedXIMEntity
{

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation_item type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation_item type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation_item type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation_item type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation_item type) throws SdaiException {
		return a0$;
	}

    // Attribute from jsdai.SPhysical_unit_design_view_mim.CAssembly_item_number
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(jsdai.SGroup_mim.EApplied_group_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testItems(jsdai.SGroup_mim.EApplied_group_assignment type) throws SdaiException {
		return test_aggregate(a3);
	}
	public jsdai.SGroup_mim.AGroupable_item getItems(jsdai.SGroup_mim.EApplied_group_assignment type) throws SdaiException {
		if (a3 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a3;
	}*/
	public jsdai.SGroup_mim.AGroupable_item createItems(jsdai.SGroup_mim.EApplied_group_assignment type) throws SdaiException {
		a3 = (jsdai.SGroup_mim.AGroupable_item)create_aggregate_class(a3, a3$, jsdai.SGroup_mim.AGroupable_item.class, 0);
		return a3;
	}
	public void unsetItems(jsdai.SGroup_mim.EApplied_group_assignment type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(jsdai.SGroup_mim.EApplied_group_assignment type) throws SdaiException {
		return a3$;
	}
    // ENDOF Attribute from jsdai.SPhysical_unit_design_view_mim.CAssembly_item_number
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAssembly_item_number.definition);

		setMappingConstraints(context, this);
		
		setAssembly_usage(context, this);
		
		unsetAssembly_usage(null); 
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		setAssembly_usage(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	* 	{[descriptive_representation_item &lt;=
	*  representation_item				
	*  representation_item.name = 'item find number']
	*  [descriptive_representation_item &lt;=
	*  representation_item &lt;-
	*  representation.items[i]
	*  representation.context_of_items -&gt;
	*  representation_context.context_type = 'item find number representation context']}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	
	public static void setMappingConstraints(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		/* Made redeclared after changed in SEDSZILLA #2368		
		armEntity.setName(null, "item find number");
		// Create representation only once
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, armEntity, context.domain, ar);
		// No further checking is done
		if(ar.getMemberCount() > 0)
			return;
		// create new Representation
		// New Context
		  LangUtils.Attribute_and_value_structure[] rcStructure =
			 {
				new LangUtils.Attribute_and_value_structure(
						jsdai.SRepresentation_schema.CRepresentation_context.attributeContext_type(null), 
						"item find number representation context")
			};

		  ERepresentation_context representation_context = (ERepresentation_context) LangUtils.createInstanceIfNeeded(
											context,
											jsdai.SRepresentation_schema.CRepresentation_context.definition,
											rcStructure);
		if(!representation_context.testContext_identifier(null))
			representation_context.setContext_identifier(null, "");
		ERepresentation er = (ERepresentation)context.working_model.createEntityInstance(CRepresentation.definition);
		er.setContext_of_items(null, representation_context);
		er.createItems(null).addUnordered(armEntity);
		er.setName(null, "");*/
	}

	public static void unsetMappingConstraints(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
	//	armEntity.unsetName(null);
	}


	//********** "assembly_item_number" attributes

	/**
	* Sets/creates data for assembly_usage attribute.
	*
	* <p>
	attribute_mapping assembly_usage(assembly_usage, $PATH, assembly_component_usage);
		assembly_item_number <=
		applied_group_assignment
		applied_group_assignment.items[i] ->
		groupable_item *> pudv_groupable_item
		pudv_groupable_item = assembly_component_usage
	end_attribute_mapping;
	* </p> 
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	/* Made redeclared after changed in SEDSZILLA #2368; Made explicit again after restructuring in assembly */	
	public static void setAssembly_usage(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetAssembly_usage(context, armEntity);
		
		if (armEntity.testAssembly_usage(null))
		{
			AGroupable_item items = armEntity.createItems(null);
			ANext_assembly_usage_occurrence_relationship_armx links = armEntity.getAssembly_usage(null);
			for(int i=1,count=links.getMemberCount(); i<=count; i++){
				ENext_assembly_usage_occurrence_relationship_armx link = links.getByIndex(i); 
				items.addUnordered(link);
			}
		}
	}


	/**
	* Unsets/deletes data for assembly_usage attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/	
	/* Made redeclared after changed in SEDSZILLA #2368; Made explicit again after restructuring in assembly */
	public static void unsetAssembly_usage(SdaiContext context, EAssembly_item_number_armx armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}
	
}
