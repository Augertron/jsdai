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

package jsdai.SProduct_breakdown_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SApplication_context_schema.EProduct_definition_context;
import jsdai.SProduct_breakdown_mim.CBreakdown_of;
import jsdai.SProduct_definition_schema.*;

public class CxBreakdown_of_armx extends CBreakdown_of_armx implements EMappedXIMEntity
{

	// Taken from CProduct_definition_relationship
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a3);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a3$;
	}
	// ENDOF taken from CProduct_definition_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CBreakdown_of.definition);

		setMappingConstraints(context, this);
		
		setBreakdown(context, this);
		
		unsetBreakdown(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		unsetBreakdown(context, this);
	}

/*	
	attribute_mapping breakdown(breakdown, $PATH, Breakdown_version);

		breakdown_of <=
		product_definition_relationship
		product_definition_relationship.relating_product_definition ->
		product_definition
		product_definition.formation ->
		product_definition_formation
	end_attribute_mapping; */
	
	public static void setBreakdown(SdaiContext context, EBreakdown_of_armx armEntity)throws SdaiException{
		unsetBreakdown(context, armEntity);
		if(armEntity.testBreakdown(null)){
			EBreakdown_version ebv = armEntity.getBreakdown(null);
			// PD
			LangUtils.Attribute_and_value_structure[] pdStructure = {new LangUtils.Attribute_and_value_structure(
				CProduct_definition.attributeFormation(null), 
	    		ebv)};
			EProduct_definition epd = (EProduct_definition) LangUtils.createInstanceIfNeeded(
				context,
				CProduct_definition.definition,
				pdStructure);
			if(!epd.testFrame_of_reference(null)){
				EProduct_definition_context epdc = CxAP210ARMUtilities.createProduct_definition_context(context, "", "", true);
				epd.setFrame_of_reference(null, epdc);
			}
			if(!epd.testId(null)){
				epd.setId(null, "");
			}
			
			armEntity.setRelating_product_definition(null, epd);
		}		
	}
	
	public static void unsetBreakdown(SdaiContext context, EBreakdown_of_armx armEntity)throws SdaiException{
		armEntity.unsetRelating_product_definition(null);
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EBreakdown_of_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EBreakdown_of_armx armEntity) throws SdaiException
	{
	}	
	
}
