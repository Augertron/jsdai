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

package jsdai.SInterconnect_physical_requirement_allocation_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_definition_schema.CProduct_definition;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_view_definition_xim.CxProduct_view_definition;
import jsdai.SProduct_view_definition_xim.EProduct_view_definition;
import jsdai.SRequirement_view_definition_xim.CxRequirement_view_definition;

public class CxElectrical_isolation_requirement extends CElectrical_isolation_requirement implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Product_view_definition
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

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	// From CProduct_definition
	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = get_instance(a3);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	// END OF Property_definition
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_definition.definition);

		setMappingConstraints(context, this);

		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		CxRequirement_view_definition.fillAIMGaps(context, this);
		
		// Clean ARM specific attributes - this is DERIVED to some magic string
		// unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		//********** "managed_design_object" attributes

		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization - this is DERIVED to some magic string
		// unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		CxRequirement_view_definition.unfillAIMGaps(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  	product_definition
		{product_definition
		product_definition.formation ->
		product_definition_formation
		product_definition_formation.of_product ->
		product <- 
		product_related_product_category.products[i]
		product_related_product_category <=
		product_category 
		product_category.name='requirement'}
		{product_definition.name = 'electrical isolation requirement'}								
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EElectrical_isolation_requirement armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxRequirement_view_definition.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "electrical isolation requirement");
	}

	public static void unsetMappingConstraints(SdaiContext context, EElectrical_isolation_requirement armEntity) throws SdaiException
	{
		CxRequirement_view_definition.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "");
	}	


	
	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_contexts(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

}
