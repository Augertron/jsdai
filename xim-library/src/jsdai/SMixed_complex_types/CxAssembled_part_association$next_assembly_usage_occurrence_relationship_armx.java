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

package jsdai.SMixed_complex_types;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SItem_definition_structure_xim.CxAssembled_part_association;
import jsdai.SAssembly_structure_xim.CxNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.CxProduct_occurrence_definition_relationship_armx;
import jsdai.SAssembly_structure_xim.ENext_assembly_usage_occurrence_relationship_armx;
import jsdai.SPhysical_unit_design_view_mim.CNext_assembly_usage_occurrence_relationship;
import jsdai.SPhysical_unit_design_view_mim.ENext_assembly_usage_occurrence_relationship;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.libutil.XimEntityStandalone;

public class CxAssembled_part_association$next_assembly_usage_occurrence_relationship_armx extends CAssembled_part_association$next_assembly_usage_occurrence_relationship_armx implements EMappedXIMEntity, XimEntityStandalone
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Product_definition_occurrence_relationship
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return get_string(a1);
	}
	public void setName(EProduct_definition_occurrence_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a1$;
	}
*/	
	// attribute (current explicit or supertype explicit) : occurrence, base type: entity product_definition
/*	public static int usedinOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SProduct_definition_schema.EProduct_definition getOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return (jsdai.SProduct_definition_schema.EProduct_definition)get_instance(a3);
	}
	public void setOccurrence(EProduct_definition_occurrence_relationship type, jsdai.SProduct_definition_schema.EProduct_definition value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeOccurrence(EProduct_definition_occurrence_relationship type) throws SdaiException {
		return a3$;
	}
*/	
	// END of taken from PDOR
	// Taken from PDR
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a5);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a5);
	}
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a5$;
	}
*/	
	// ENDOF taken from PDR

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CNext_assembly_usage_occurrence_relationship.definition);

		setMappingConstraints(context, this);

		// relating_view : product_view_definition;
        setRelating_view(context, this);
		
		// related_view : product_occurrence;		
        setRelated_view(context, this);
        
		// reference_designator
		setReference_designator(context, this);		
		
		// CxNext_assembly_usage_occurrence_relationship_armx.processAssemblyTrick(context, this);
		//********** "managed_design_object" attributes

		// relating_view : product_view_definition;
        unsetRelating_view(null);
		
		// related_view : product_occurrence;		
        unsetRelated_view(null);
        
		// reference_designator
		unsetReference_designator(null);		
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// relating_view : product_view_definition;
        unsetRelating_view(context, this);
		
		// related_view : product_occurrence;		
        unsetRelated_view(context, this);
        
		// reference_designator
		unsetReference_designator(context, this);		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	 product_definition_relationship
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CAssembled_part_association$next_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxAssembled_part_association.setMappingConstraints(context, armEntity);
		CxNext_assembly_usage_occurrence_relationship_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CAssembled_part_association$next_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxAssembled_part_association.setMappingConstraints(context, armEntity);
		CxNext_assembly_usage_occurrence_relationship_armx.unsetMappingConstraints(context, armEntity);
	}
	
	
	public static void setRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.setRelated_view(context, armEntity);
	}
	
	public static void unsetRelated_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.unsetRelated_view(context, armEntity);
	}

	public static void setReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.setReference_designator(context, armEntity);
	}
	
	public static void unsetReference_designator(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxNext_assembly_usage_occurrence_relationship_armx.unsetReference_designator(context, armEntity);
	}

	public static void setRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.setRelating_view(context, armEntity);
	}
	
	public static void unsetRelating_view(SdaiContext context, ENext_assembly_usage_occurrence_relationship_armx armEntity) throws SdaiException
	{
		CxProduct_occurrence_definition_relationship_armx.unsetRelating_view(context, armEntity);
	}
	
	ENext_assembly_usage_occurrence_relationship aimInstance;
	
	public EEntity getAimInstance(SdaiContext context) throws SdaiException {
		if(aimInstance == null){
			aimInstance = (ENext_assembly_usage_occurrence_relationship)
				context.working_model.createEntityInstance(CNext_assembly_usage_occurrence_relationship.definition);
		}
		return aimInstance;
	}
	
	public void unsetAimInstance(SdaiContext context) throws SdaiException{
		aimInstance = null;
	}

}
