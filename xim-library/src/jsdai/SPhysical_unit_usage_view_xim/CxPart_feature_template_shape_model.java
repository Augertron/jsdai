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

package jsdai.SPhysical_unit_usage_view_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_representation_schema.*;

public class CxPart_feature_template_shape_model extends CPart_feature_template_shape_model implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_representation.definition);

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
		{shape_representation &lt;=
		[representation
		representation.description = 'part feature template shape model']
		[representation &lt;-
		property_definition_representation.used_representation
		property_definition_representation
		property_definition_representation.definition -&gt;
		property_definition
		property_definition.definition -&gt;
		characterized_definition
		characterized_definition = characterized_object
		characterized_object =&gt;
		feature_definition =&gt;
		part_feature_template_definition]}	
	</p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SA will be set via NFSE
	// All the constraints are captured by setting attributes.
	// Some magic strings will come only at subtypes as this is ABSTRACT entity.
	public static void setMappingConstraints(SdaiContext context, EPart_feature_template_shape_model armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setRepresentationDescription(context, armEntity, "part feature template shape model");
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart_feature_template_shape_model armEntity) throws SdaiException
	{
		CxAP210ARMUtilities.unsetDerviedDescription(context, armEntity);
	}

	
}
