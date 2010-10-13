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

package jsdai.SGeometric_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SDimension_tolerance_xim.CxAssociated_shape_element;
import jsdai.SProduct_property_definition_schema.CShape_aspect;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxAxis_placement_shape_element extends CAxis_placement_shape_element implements EMappedXIMEntity
{
	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a7$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a7);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a7 = get_instance(a7);
		return (EProduct_definition_shape)a7;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a7 = set_instanceX(a7, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a7 = unset_instance(a7);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a7$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a8);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a8);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a8 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a8 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a8$;
	}

	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a5);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a5);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a5 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a5 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a5$;
		}
		
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a6);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a6$;
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

		// property - used for both attributes
//		unsetProperty(context, this);

		// defined_in : geometric_representation_context;
//		setDefined_in(context, this);
		
        // parameter_reference : axis2_placement;
//		setParameter_reference(context, this);
		
		// clean ARM
		// defined_in : geometric_representation_context;
//		unsetDefined_in(null);
		
        // parameter_reference : axis2_placement;
//		unsetParameter_reference(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// property - used for both attributes
//			unsetProperty(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EAxis_placement_shape_element armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxAssociated_shape_element.setMappingConstraints(context, armEntity);
        // SELF\shape_aspect.of_shape : product_definition_shape := ?;
        // SELF\shape_aspect.product_definitional : LOGICAL := ?;
		if(!armEntity.testProduct_definitional(null)){
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		}
        // SELF\shape_aspect.name : label := ?;
		if(!armEntity.testName((EShape_aspect)null)){
			armEntity.setName((EShape_aspect)null, "");
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
			EAxis_placement_shape_element armEntity) throws SdaiException {
		CxAssociated_shape_element.unsetMappingConstraints(context, armEntity);
	}

}