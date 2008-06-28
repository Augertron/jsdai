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

package jsdai.SModel_parameter_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SModel_parameter_mim.CCategory_model_parameter;

public class CxCategory_model_parameter_armx extends CCategory_model_parameter_armx implements EMappedXIMEntity {

	
	public int attributeState = ATTRIBUTES_MODIFIED;	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCategory_model_parameter.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			// Valid_range
			setValid_range(context, this);

			// Default_value
			setDefault_value(context, this);

			
			// Clean ARM specific attributes - this is DERIVED to some magic string

			// Valid_range
			unsetValid_range(null);

			// Default_value
			unsetDefault_value(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			// Valid_range
			unsetValid_range(context, this);

			// Default_value
			unsetDefault_value(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
		product_specific_parameter_value_assignment <=
		[characterized_object]
		[product_related_product_category]
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
			ECategory_model_parameter_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxModel_parameter_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ECategory_model_parameter_armx armEntity) throws SdaiException {
		CxModel_parameter_armx.unsetMappingConstraints(context, armEntity);
	}


	//********** "model_parameter_armx" attributes

	/**
	* Sets/creates data for default_value attribute.
	*
	* <p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MP <- DVPDR -> R -> RI {<- AGA -> CT}
	public static void setDefault_value(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		CxModel_parameter_armx.setDefault_value(context, armEntity);
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetDefault_value(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		CxModel_parameter_armx.unsetDefault_value(context, armEntity);
	}


	/**
	* Sets/creates data for default_value attribute.
	*
	*/
	// MP <- VRPDR -> R -> RI
	public static void setValid_range(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		CxModel_parameter_armx.setValid_range(context, armEntity);
	}


	/**
	* Unsets/deletes data for minimum_thickness_over_metal_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetValid_range(SdaiContext context, EModel_parameter_armx armEntity) throws SdaiException
	{
		CxModel_parameter_armx.unsetValid_range(context, armEntity);
	}
}