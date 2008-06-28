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
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_2d_shape_xim.CxPlanar_projected_shape_model;
import jsdai.SPart_template_extension_mim.CPlanar_path_shape_representation_with_parameters;
import jsdai.SPart_template_extension_xim.CxPlanar_path_shape_model_with_parameters;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxPlanar_path_shape_model_with_parameters$planar_projected_shape_model extends CPlanar_path_shape_model_with_parameters$planar_projected_shape_model implements EMappedXIMEntity
{


	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

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
	* <p>
	* mapping_constraints;
	* 	{shape_representation &lt;=
	*  representation				
	*  [representation.id = 'ac2ds']
	*  [representation.name = 'planar projected shape']
	*  [representation &lt;-
	*  property_definition_representation.used_representation
	*  property_definition_representation
	*  property_definition_representation.definition -&gt;
	*  {property_definition =&gt;
	*  product_definition_shape}
	*  property_definition
	*  property_definition.definition -&gt;
	*  characterized_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_product_definition
	*  characterized_product_definition = product_definition
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component]}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, CPlanar_path_shape_model_with_parameters$planar_projected_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxPlanar_projected_shape_model.setMappingConstraints(context, armEntity);
		CxPlanar_path_shape_model_with_parameters.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, CPlanar_path_shape_model_with_parameters$planar_projected_shape_model armEntity) throws SdaiException
	{
		CxPlanar_projected_shape_model.setMappingConstraints(context, armEntity);
		CxPlanar_path_shape_model_with_parameters.setMappingConstraints(context, armEntity);
	}

}
