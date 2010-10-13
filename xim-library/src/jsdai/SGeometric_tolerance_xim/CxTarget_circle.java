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

import jsdai.SGeometric_tolerance_mim.CPlaced_datum_target_feature;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxTarget_circle extends CTarget_circle implements EMappedXIMEntity
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

	/// methods for attribute: target_id, base type: STRING
/*	public boolean testTarget_id(EDatum_target type) throws SdaiException {
		return test_string(a9);
	}
	public String getTarget_id(EDatum_target type) throws SdaiException {
		return get_string(a9);
	}*/
	public void setTarget_id(EDatum_target type, String value) throws SdaiException {
		a9 = set_string(value);
	}
	public void unsetTarget_id(EDatum_target type) throws SdaiException {
		a9 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeTarget_id(EDatum_target type) throws SdaiException {
		return a9$;
	}
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlaced_datum_target_feature.definition);

		setMappingConstraints(context, this);

		// property - used for both attributes
		unsetProperty(context, this);

		// defined_in : geometric_representation_context;
//		setDefined_in(context, this);
		
        // parameter_reference : axis2_placement;
//		setParameter_reference(context, this);
		
		// diameter : measure_representation_item;
		setDiameter(context, this);
		
		// clean ARM
		// defined_in : geometric_representation_context;
//		unsetDefined_in(null);
		
        // parameter_reference : axis2_placement;
//		unsetParameter_reference(null);
		
		// diameter : measure_representation_item;
		unsetDiameter(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// property - used for 3 attributes
			unsetProperty(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
			placed_datum_target_feature <= 
			datum_target
			{datum_target <= 
			shape_aspect 
			shape_aspect.description = 'circle'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ETarget_circle armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPlaced_target.setMappingConstraints(context, armEntity);
		armEntity.setDescription((EShape_aspect)null, "circle");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ETarget_circle armEntity) throws SdaiException {
		CxPlaced_target.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription((EShape_aspect)null);
	}

	/**
	 * Sets/creates data for defined_in attribute.
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
	// PDTF <- PropD <- PDR -> SRWP -> GRC 
	/* Removed - issue 3610	
	public static void setDefined_in(SdaiContext context, EPlaced_target armEntity) throws SdaiException {
		CxPlaced_target.setDefined_in(context, armEntity);
	}
*/
	/**
	 * Sets/creates data for Parameter_reference attribute.
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
	/* Removed - issue 3610	
	// PDTF <- PropD <- SDR -> SRWP -> P 
	public static void setParameter_reference(SdaiContext context, EPlaced_target armEntity) throws SdaiException {
		CxPlaced_target.setParameter_reference(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	
	public static void unsetProperty(SdaiContext context, EPlaced_target armEntity) throws SdaiException {
		CxPlaced_target.unsetProperty(context, armEntity);
	}

	/**
	 * Sets/creates data for Parameter_reference attribute.
	 * 
	 * <p>
	attribute_mapping diameter(diameter, $PATH, measure_representation_item);
		placed_datum_target_feature <= 
		datum_target <= 
		shape_aspect 
		shape_definition = shape_aspect
		shape_definition 
		characterized_definition = shape_definition 
		characterized_definition <-
		property_definition.definition property_definition
		represented_definition = property_definition
		represented_definition <-
		property_definition_representation.definition
		{property_definition_representation => 
		shape_definition_representation} 
		property_definition_representation
		property_definition_representation.used_representation ->
		{representation => 
		shape_representation => 
		shape_representation_with_parameters} 
		representation representation.items[i] -> 
		representation_item
		{representation_item.name = 'target diameter'} 
		representation_item => measure_representation_item
		{measure_representation_item <= 
		measure_with_unit => 
		length_measure_with_unit}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PDTF <- PropD <- SDR -> SRWP -> LMWU 
	public static void setDiameter(SdaiContext context, ETarget_circle armEntity) throws SdaiException {
		if(armEntity.testDiameter(null)){
			ELength_measure_with_unit elmwu = armEntity.getDiameter(null);
			ERepresentation_item eri;
			if(!(elmwu instanceof ERepresentation_item)){
				eri = (ERepresentation_item)context.working_model.substituteInstance(elmwu, 
						CLength_measure_with_unit$measure_representation_item.definition);
			}else{
				eri = (ERepresentation_item)elmwu;
			}
			eri.setName(null, "target diameter");
			CxPlaced_target.setParameter(context, armEntity, eri);
		}
	}

}