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
* @version $$
* $Id$
*/

import jsdai.SDerived_shape_element_xim.CxContact;
import jsdai.SDerived_shape_element_xim.CxDerived_shape_element;
import jsdai.SDerived_shape_element_xim.EDerived_shape_element;
import jsdai.SGeometric_tolerance_xim.CxTarget_circular_curve;
import jsdai.SGeometric_tolerance_xim.ETarget_circular_curve;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxContact$target_circular_curve extends CContact$target_circular_curve implements EMappedXIMEntity
{
	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a9);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a9 = get_instance(a9);
		return (EProduct_definition_shape)a9;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a9 = set_instanceX(a9, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a9 = unset_instance(a9);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a9$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a10);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a10);
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

	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a7);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a7);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a7 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a7 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a7$;
		}
		
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a8);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a8);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a8 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a8 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a8$;
	}
	// ENDOF From CShape_aspect.java

	/// methods for attribute: target_id, base type: STRING
/*	public boolean testTarget_id(EDatum_target type) throws SdaiException {
		return test_string(a0);
	}
	public String getTarget_id(EDatum_target type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setTarget_id(EDatum_target type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetTarget_id(EDatum_target type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeTarget_id(EDatum_target type) throws SdaiException {
		return a0$;
	}
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDerived_shape_aspect$placed_datum_target_feature.definition);

		setMappingConstraints(context, this);

		setDiameter(context, this);
		
		setDerived_from(context, this);
		
		unsetDiameter(null);
		
		unsetDerived_from(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetDiameter(context, this);		
			
			unsetDerived_from(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		datum_target
	 	{datum_target <= 
	 	shape_aspect 
	 	shape_aspect.description = 'area'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, CContact$target_circular_curve armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxTarget_circular_curve.setMappingConstraints(context, armEntity);
		CxContact.setMappingConstraints(context, armEntity);
//		armEntity.setName(null, "");
//		armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, CContact$target_circular_curve armEntity) throws SdaiException {
		CxTarget_circular_curve.unsetMappingConstraints(context, armEntity);
		CxContact.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for diameter attribute.
	 * 
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDiameter(SdaiContext context, ETarget_circular_curve armEntity) throws SdaiException {
		CxTarget_circular_curve.setDiameter(context, armEntity);
	}

	/**
	 * Unsets/deletes data for diameter attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetDiameter(SdaiContext context, ETarget_circular_curve armEntity) throws SdaiException {
		CxTarget_circular_curve.unsetProperty(context, armEntity);
	}
	
	/**
	* Sets/creates data for Derived_from attribute.
	*
	* <p>
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA <- SAR -> SA
	public static void setDerived_from(SdaiContext context, EDerived_shape_element armEntity) throws SdaiException
	{
		CxDerived_shape_element.setDerived_from(context, armEntity);
	}

	/**
	* Unsets/deletes data for Derived_from attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDerived_from(SdaiContext context, EDerived_shape_element armEntity) throws SdaiException
	{
		CxDerived_shape_element.unsetDerived_from(context, armEntity);
	}
	
}