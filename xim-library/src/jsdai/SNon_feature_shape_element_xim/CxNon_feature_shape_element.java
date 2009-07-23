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

package jsdai.SNon_feature_shape_element_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element;

public class CxNon_feature_shape_element extends CNon_feature_shape_element implements EMappedXIMEntity{

	// From CShape_aspect.java
/*	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_shape)a2;
	}
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a2$;
	}
*/
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

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);
		
//		setScope(context, this);
		// Clean ARM
//		unsetScope(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

//			unsetScope(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  {shape_aspect
	 *  shape_aspect.product_definitional = .FALSE.}
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
			ENon_feature_shape_element armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_element.setMappingConstraints(context, armEntity);
		armEntity.setProduct_definitional(null, ELogical.FALSE);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ENon_feature_shape_element armEntity) throws SdaiException {
		CxShape_element.unsetMappingConstraints(context, armEntity);
		armEntity.unsetProduct_definitional(null);
	}
	

	//********** "non_feature_shape_element" attributes

	/**
	* Sets/creates data for scope attribute.
	*  Mapping is not exact - just average, since it is not available
	* 	shape_aspect
	*  shape_aspect.of_shape ->
	*  product_definition_shape <=
	*  property_definition
	*  property_definition.definition ->
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA -> PDS -> PD
/* Now made direct redeclaration of "of_shape"	
	public static void setScope(SdaiContext context, ENon_feature_shape_element armEntity) throws SdaiException
	{
		//unset old values
		unsetScope(context, armEntity);

		if (armEntity.testScope(null))
		{

			EEntity armScope = armEntity.getScope(null);
			if(armScope instanceof EProduct_definition_shape){
				armEntity.setOf_shape(null, (EProduct_definition_shape)armScope);
			}
			else{
				// PDS
				LangUtils.Attribute_and_value_structure[] pdsStructure =
					{new LangUtils.Attribute_and_value_structure(
						CProduct_definition_shape.attributeDefinition(null),
						armScope)
					};
				EProduct_definition_shape epds = (EProduct_definition_shape)
					LangUtils.createInstanceIfNeeded(context, CProduct_definition_shape.definition, pdsStructure);
				// SA -> PDS
				armEntity.setOf_shape(null, epds);
				if(!epds.testName(null))
					epds.setName(null, "");
			}
		}
	}

*/
	/**
	* Unsets/deletes data for scope attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
/*	
	public static void unsetScope(SdaiContext context, ENon_feature_shape_element armEntity) throws SdaiException
	{
		armEntity.unsetOf_shape(null);
	}	
*/	
}