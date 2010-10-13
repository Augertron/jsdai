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

package jsdai.SLayered_interconnect_module_design_xim;

/**
* @author Giedrius Liutkus
* @version $
* $Id$
*/

import jsdai.SMaterial_property_definition_schema.AProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.CProperty_definition_relationship;
import jsdai.SMaterial_property_definition_schema.EProperty_definition_relationship;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxInter_stratum_extent extends CInter_stratum_extent implements EMappedXIMEntity
{
	// Taken from CProduct_definition_relationship
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF Taken from CProduct_definition_relationship
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		// 		commonForAIMInstanceCreation(context);
		setTemp("AIM", CProduct_definition_relationship.definition);

		setMappingConstraints(context, this);

		setIncluded_stratum(context, this);
		
		unsetIncluded_stratum(null);
		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetIncluded_stratum(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	 {product_definition_relationship
	*   product_definition_relationship.name = 'inter stratum extent'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EInter_stratum_extent armEntity) throws SdaiException
	{
		armEntity.setName(null, "inter stratum extent");
		// AIM GAP
//		if(!armEntity.testId(null))
			armEntity.setId(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EInter_stratum_extent armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}
	
	
	//********** "managed_design_object" attributes

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
		attribute_mapping included_stratum(included_stratum, $PATH, Stratum_armx);
			product_definition_relationship
			characterized_product_definition = product_definition_relationship
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			property_definition_relationship.related_property_definition
			property_definition_relationship
			{property_definition_relationship.name = 'included stratum'}
			property_definition_relationship.relating_property_definition ->
			property_definition =>
			product_definition_shape =>
			stratum
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// PDR <- PD <- PDR -> S
	public static void setIncluded_stratum(SdaiContext context, EInter_stratum_extent armEntity) throws SdaiException
	{
		unsetIncluded_stratum(context, armEntity);
		if(armEntity.testIncluded_stratum(null)){
			AStratum_armx stratums = armEntity.getIncluded_stratum(null);
			for(int i=1,count=stratums.getMemberCount(); i<=count; i++){
				EStratum_armx stratum = stratums.getByIndex(i);
		        // PD
		        LangUtils.Attribute_and_value_structure[] pdStructure = {
		           new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), 
		           		armEntity),
		        };
	
		        EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
		           context,
		           CProperty_definition.definition,
		           pdStructure);
	
		        if (!property_definition.testName(null)) {
		           property_definition.setName(null, "");
		        }
				// PDR
		        EProperty_definition_relationship property_definition_relationship = (EProperty_definition_relationship)
		        	context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
		        property_definition_relationship.setRelated_property_definition(null, property_definition);
		        property_definition_relationship.setName(null, "included stratum");
		        property_definition_relationship.setRelating_property_definition(null, stratum);
			}
		}
	}

	public static void unsetIncluded_stratum(SdaiContext context, EInter_stratum_extent armEntity) throws SdaiException
	{
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int p=1,countP=apd.getMemberCount(); p<=countP; p++){
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, apd.getByIndex(p), context.domain, apdr);	
			for(int r=1,countR=apdr.getMemberCount(); r<=countR; r++){
				EProperty_definition_relationship epdr = apdr.getByIndex(r);
				if((epdr.testName(null))&&(epdr.getName(null).equals("included stratum"))){
					epdr.deleteApplicationInstance();
				}
			}
		}
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
	}
	
}
