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

package jsdai.SLayered_interconnect_simple_template_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_mim.CPath_area_with_parameters;
import jsdai.SConstructive_solid_geometry_2d_xim.CxPath_area_with_parameters_armx;
import jsdai.SConstructive_solid_geometry_2d_xim.EPath_area_with_parameters_armx;
import jsdai.SRepresentation_schema.EMapped_item;
import jsdai.SRepresentation_schema.ERepresentation_map;

public class CxClosed_path_area_with_parameters extends CClosed_path_area_with_parameters implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;
	
	private static Object parametersMap = new Object();


	// Taken from Mapped_item
	// attribute (current explicit or supertype explicit) : mapping_source, base type: entity representation_map
/*	public static int usedinMapping_source(EMapped_item type, ERepresentation_map instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testMapping_source(EMapped_item type) throws SdaiException {
		return test_instance(a1);
	}
	public ERepresentation_map getMapping_source(EMapped_item type) throws SdaiException {
		a1 = get_instance(a1);
		return (ERepresentation_map)a1;
	}*/

	public void setMapping_source(EMapped_item type, ERepresentation_map value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}

	public void unsetMapping_source(EMapped_item type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeMapping_source(EMapped_item type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Mapped_item

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPath_area_with_parameters.definition);

		setMappingConstraints(context, this);
		
	    setParameters(context, this);

	    unsetParameters(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetParameters(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClosed_path_area_with_parameters armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPath_area_with_parameters_armx.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EClosed_path_area_with_parameters armEntity) throws SdaiException
	{
		CxPath_area_with_parameters_armx.setMappingConstraints(context, armEntity);
	}

	/**
	* Sets/creates data for parameters constraints.
	*
	* <p>
		attribute_mapping parameters(parameters, curve_style_parameters);
			path_area_with_parameters <=
			mapped_item
			mapped_item.mapping_source ->
			representation_map
			representation_map.mapped_representation ->
			representation =>
			curve_style_parameters_representation =>
			curve_style_parameters
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setParameters(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
		CxPath_area_with_parameters_armx.setParameters(context, armEntity);		
	}

	public static void unsetParameters(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
		CxPath_area_with_parameters_armx.unsetParameters(context, armEntity);
	}

	
}
