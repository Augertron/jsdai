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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SLayered_interconnect_module_design_mim.CLayer_connection_point;
import jsdai.SProduct_property_definition_schema.*;

public class CxExplicitly_located_layer_connection_point extends CExplicitly_located_layer_connection_point implements EMappedXIMEntity{

	// From CShape_aspect.java
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

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
	
	// ENDOF From CShape_aspect.java
	

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLayer_connection_point.definition);

		setMappingConstraints(context, this);
		
		// location_2d
		setLocation_2d(context, this);
		
      // location_3d
		setLocation_3d(context, this);
		
		// Clean ARM

		// location_2d
		unsetLocation_2d(null);
		
      // location_3d
		unsetLocation_3d(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// location_2d
			unsetLocation_2d(context, this);
			
	      // location_3d
			unsetLocation_3d(context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; layer_connection_point &lt;=
	 *  layer_connection_point &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'explicitly located'}
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
			EExplicitly_located_layer_connection_point armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxLayer_connection_point_armx.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "explicitly located");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EExplicitly_located_layer_connection_point armEntity) throws SdaiException {
		CxLayer_connection_point_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}


	//********** "layer_connection_point" attributes

	/**
	* Sets/creates data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// LCP <- PD <- PDR -> R -> CP
	public static void setLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.setLocation_2d(context, armEntity);		
	}


	/**
	* Unsets/deletes data for location_2d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_2d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.unsetLocation_2d(context, armEntity);
	}

	/**
	* Sets/creates data for location_3d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.setLocation_3d(context, armEntity);		
	}


	/**
	* Unsets/deletes data for location_3d attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetLocation_3d(SdaiContext context, ELayer_connection_point_armx armEntity) throws SdaiException
	{
		CxLayer_connection_point_armx.unsetLocation_3d(context, armEntity);		
	}



	
}