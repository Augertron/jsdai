/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2011, LKSoftWare GmbH, Germany
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

package jsdai.SDimension_tolerance_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.CChain_based_item_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EChain_based_item_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;

public class CxChain_based_associated_shape_element extends CChain_based_associated_shape_element implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a8);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a8);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a10 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a10 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a10$;
	}
	// ENDOF From CShape_aspect.java

	/* Taken from EItem_identified_representation_usage
	//going through all the attributes: #5629499534233780=EXPLICIT_ATTRIBUTE('definition',#5629499534233776,2,#5629499534233768,$,.F.);
	//<01> generating methods for consolidated attribute:  definition
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// -2- methods for SELECT attribute: definition
	public static int usedinDefinition(EItem_identified_representation_usage type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDefinition(EItem_identified_representation_usage type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getDefinition(EItem_identified_representation_usage type) throws SdaiException { // case 1
		return get_instance_select(a2);
	}*/

	public void setDefinition(EItem_identified_representation_usage type, EEntity value) throws SdaiException { // case 1
		a2 = set_instance(a2, value);
	}

	public void unsetDefinition(EItem_identified_representation_usage type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EItem_identified_representation_usage type) throws SdaiException {
		return a2$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);
		
//		setId_x(context, this);
		
//		unsetId_x(null);
		
		unsetName((EItem_identified_representation_usage)null);
		
		unsetDescription((EItem_identified_representation_usage)null);
		
		unsetDefinition(null);
		
		unsetUsed_representation(null);
		
		unsetIdentified_item(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

//			unsetId_x(context, this);			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  component_feature &lt;=
	 *  shape_aspect
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
			EChain_based_associated_shape_element armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

//		CxShape_element.setMappingConstraints(context, armEntity);
		
		EChain_based_item_identified_representation_usage ecbiiru = (EChain_based_item_identified_representation_usage)
			context.working_model.createEntityInstance(CChain_based_item_identified_representation_usage.definition);
		if(armEntity.testName((EItem_identified_representation_usage)null)){
			ecbiiru.setName(null, armEntity.getName((EItem_identified_representation_usage)null));
		}
		if(armEntity.testDescription((EItem_identified_representation_usage)null)){
			ecbiiru.setDescription(null, armEntity.getDescription((EItem_identified_representation_usage)null));
		}
		ecbiiru.setDefinition(null, armEntity);
		if(armEntity.testUsed_representation(null)){
			ecbiiru.setUsed_representation(null, armEntity.getUsed_representation(null));
		}
		if(armEntity.testIdentified_item(null)){
			ecbiiru.setIdentified_item(null, armEntity.getIdentified_item(null));
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EChain_based_associated_shape_element armEntity) throws SdaiException {
//		CxShape_element.unsetMappingConstraints(context, armEntity);
	}

	//********** "shape_element" attributes

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  component_feature &lt;=
	 *  shape_aspect
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
		CxShape_element.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EShape_element armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);
	}
*/	
}