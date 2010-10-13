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

import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SShape_aspect_definition_schema.CComposite_shape_aspect$datum_feature;
import jsdai.SShape_composition_xim.CxComposite_shape_aspect_armx;
import jsdai.SShape_composition_xim.EComposite_shape_aspect_armx;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxComposite_shape_aspect_armx$datum_feature extends CComposite_shape_aspect_armx$datum_feature implements EMappedXIMEntity{

	// From CShape_aspect.java
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

		setTemp("AIM", CComposite_shape_aspect$datum_feature.definition);

		setMappingConstraints(context, this);
		
		setElements(context, this);
		
//		setId_x(context, this);
		
//		unsetId_x(null);
		
		unsetElements(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetElements(context, this);
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
			CComposite_shape_aspect_armx$datum_feature armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxComposite_shape_aspect_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CComposite_shape_aspect_armx$datum_feature armEntity) throws SdaiException {
		
		CxComposite_shape_aspect_armx.unsetMappingConstraints(context, armEntity);
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
	public static void setElements(SdaiContext context, EComposite_shape_aspect_armx armEntity) throws SdaiException {
		CxComposite_shape_aspect_armx.setElements(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetElements(SdaiContext context, EComposite_shape_aspect_armx armEntity) throws SdaiException {
		CxComposite_shape_aspect_armx.unsetElements(context, armEntity);
	}
}