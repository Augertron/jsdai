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

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SAssembly_module_with_interconnect_component_xim.CxInterconnect_module_component_surface_feature_armx;
import jsdai.SComponent_feature_xim.CxComponent_feature_armx;
import jsdai.SComponent_feature_xim.EComponent_feature_armx;
import jsdai.SGeometric_tolerance_xim.CxDatum_feature_shape_element_xim;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element;
import jsdai.SShape_property_assignment_xim.EShape_element;

public class CxDatum_feature_shape_element_xim$interconnect_module_component_surface_feature_armx extends CDatum_feature_shape_element_xim$interconnect_module_component_surface_feature_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
	/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
			return test_string(a2);
		}
		public String getDescription(EShape_aspect type) throws SdaiException {
			return get_string(a2);
		}*/
		public void setDescription(EShape_aspect type, String value) throws SdaiException {
			a2 = set_string(value);
		}
		public void unsetDescription(EShape_aspect type) throws SdaiException {
			a2 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
			return a2$;
		}
	
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a3);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a3 = get_instance(a3);
		return (EProduct_definition_shape)a3;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a3 = set_instanceX(a3, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a4);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a4);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a4 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a4 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a4$;
	}
	// ENDOF From CShape_aspect.java

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDatum_feature$interconnect_module_component_surface_feature.definition);

		setMappingConstraints(context, this);

		// Definition
		setDefinition (context, this);

		// id_x
		setId_x (context, this);

		// Definition
		unsetDefinition(null);
		
		// id_x
		unsetId_x (null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Definition
			unsetDefinition (context, this);
			
			// id_x
			unsetId_x (context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  group_shape_aspect &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'interconnect module constraint region'}
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
			CDatum_feature_shape_element_xim$interconnect_module_component_surface_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxDatum_feature_shape_element_xim.setMappingConstraints(context, armEntity);
		CxInterconnect_module_component_surface_feature_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CDatum_feature_shape_element_xim$interconnect_module_component_surface_feature_armx armEntity) throws SdaiException {
		CxDatum_feature_shape_element_xim.unsetMappingConstraints(context, armEntity);
		CxInterconnect_module_component_surface_feature_armx.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping definition.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
	public static void setDefinition(SdaiContext context,
			EComponent_feature_armx armEntity) throws SdaiException {
		CxComponent_feature_armx.setDefinition(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute definition.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	
	public static void unsetDefinition(SdaiContext context,
			EComponent_feature_armx armEntity) throws SdaiException {
		CxComponent_feature_armx.unsetDefinition(context, armEntity);
	}
	
	
	/**
	 * Sets/creates data for mapping id_x.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	
	public static void setId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		CxShape_element.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping for attribute id_x.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	
	public static void unsetId_x(SdaiContext context,
			EShape_element armEntity) throws SdaiException {
		CxShape_element.unsetId_x(context, armEntity);
	}

}