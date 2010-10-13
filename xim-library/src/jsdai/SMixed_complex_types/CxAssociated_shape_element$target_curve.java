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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.SDimension_tolerance_xim.CxAssociated_shape_element;
import jsdai.SGeometric_tolerance_xim.CxTarget_curve;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SShape_aspect_definition_schema.CDatum_target;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxAssociated_shape_element$target_curve extends CAssociated_shape_element$target_curve implements EMappedXIMEntity{

	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a8);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a8 = get_instance(a8);
		return (EProduct_definition_shape)a8;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a8 = set_instanceX(a8, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a8$;
	}
	
	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a6);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a6);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a6 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a6 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a6$;
		}
		
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a7);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a7 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a7$;
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
	
	// From CShape_aspect.java
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a9);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a9);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a9 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a9 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a9$;
	}
	// ENDOF From CShape_aspect.java

	/* Taken from EItem_identified_representation_usage
	//going through all the attributes: #5629499534233780=EXPLICIT_ATTRIBUTE('definition',#5629499534233776,2,#5629499534233768,$,.F.);
	//<01> generating methods for consolidated attribute:  definition
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// -2- methods for SELECT attribute: definition
	public static int usedinDefinition(EItem_identified_representation_usage type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testDefinition(EItem_identified_representation_usage type) throws SdaiException {
		return test_instance(a3);
	}

	public EEntity getDefinition(EItem_identified_representation_usage type) throws SdaiException { // case 1
		return get_instance_select(a3);
	}*/

	public void setDefinition(EItem_identified_representation_usage type, EEntity value) throws SdaiException { // case 1
		a3 = set_instance(a3, value);
	}

	public void unsetDefinition(EItem_identified_representation_usage type) throws SdaiException {
		a3 = unset_instance(a3);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EItem_identified_representation_usage type) throws SdaiException {
		return a3$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDatum_target.definition);

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
			CAssociated_shape_element$target_curve armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxAssociated_shape_element.setMappingConstraints(context, armEntity);
		CxTarget_curve.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CAssociated_shape_element$target_curve armEntity) throws SdaiException {
		CxAssociated_shape_element.unsetMappingConstraints(context, armEntity);
		CxTarget_curve.unsetMappingConstraints(context, armEntity);
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