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

package jsdai.SPart_template_extension_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SPart_template_extension_mim.CPlanar_path_shape_representation_with_parameters;

public class CxPlanar_closed_path_shape_model_with_parameters extends CPlanar_closed_path_shape_model_with_parameters implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	


	// Taken from Mapped_item
	// ENDOF Taken from Mapped_item

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlanar_path_shape_representation_with_parameters.definition);

		setMappingConstraints(context, this);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*	{planar_path_shape_representation_with_parameters <=
		single_area_csg_2d_shape_representation <=
		csg_2d_shape_representation <=
		shape_representation <=
		representation 
		representation.items[i] ->
		representation_item
		representation_item =>
		mapped_item
		mapped_item.mapping_source ->
		representation_map
		representation_map.mapped_representation ->
		representation =>
		curve_style_parameters_representation =>
		closed_curve_style_parameters}
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// Constraint should be ensured by correct setting of items list
	public static void setMappingConstraints(SdaiContext context, EPlanar_closed_path_shape_model_with_parameters armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPlanar_path_shape_model_with_parameters.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPlanar_closed_path_shape_model_with_parameters armEntity) throws SdaiException
	{
		CxPlanar_path_shape_model_with_parameters.unsetMappingConstraints(context, armEntity);
	}
}
