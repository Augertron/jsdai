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
import jsdai.SGeometric_tolerance_xim.CxTarget_curve;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SShape_aspect_definition_schema.CDatum_target$derived_shape_aspect;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxContact$target_curve extends CContact$target_curve implements EMappedXIMEntity
{
	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a4);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a4 = get_instance(a4);
		return (EProduct_definition_shape)a4;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a4 = set_instanceX(a4, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a4$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a5);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a5);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a5 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a5 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a5$;
	}

	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a2);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a2);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a2 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a2 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a2$;
		}
		
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a3);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a3$;
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

		setTemp("AIM", CDatum_target$derived_shape_aspect.definition);

		setMappingConstraints(context, this);

		setDerived_from(context, this);
		
		unsetDerived_from(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
	
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
	public static void setMappingConstraints(SdaiContext context, CContact$target_curve armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxTarget_curve.setMappingConstraints(context, armEntity);
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
	public static void unsetMappingConstraints(SdaiContext context, CContact$target_curve armEntity) throws SdaiException {
		CxTarget_curve.unsetMappingConstraints(context, armEntity);
		CxContact.unsetMappingConstraints(context, armEntity);
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