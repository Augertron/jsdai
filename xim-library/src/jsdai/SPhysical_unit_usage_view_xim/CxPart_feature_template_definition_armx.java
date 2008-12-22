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
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SGroup_schema.EGroup;
import jsdai.SPhysical_unit_usage_view_mim.CPart_feature_template_definition;
import jsdai.SShape_feature_xim.CxShape_feature_definition_armx;

public class CxPart_feature_template_definition_armx extends CPart_feature_template_definition_armx implements EMappedXIMEntity{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CGroup
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EGroup type) throws SdaiException {
		return test_string(a2);
	}
	public String getName(EGroup type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setName(EGroup type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetName(EGroup type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EGroup type) throws SdaiException {
		return a3$;
	}
	// ENDOF taken from CGroup
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPart_feature_template_definition.definition);

		setMappingConstraints(context, this);
		
		// setFeature_model(context, this);
		
		// unsetFeature_model(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// unsetFeature_model(context, this);

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
			EPart_feature_template_definition_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_feature_definition_armx.setMappingConstraints(context, armEntity);
//		if(!armEntity.testName((EGroup)null)){
			armEntity.setName((EGroup)null, "");
//		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPart_feature_template_definition_armx armEntity) throws SdaiException {
		CxShape_feature_definition_armx.unsetMappingConstraints(context, armEntity);
	}

// Feature_definition_armx
	/**
	 * Sets/creates data for attribute Feature_model.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Made derived	
	public static void setFeature_model(SdaiContext context,
			EPart_feature_template_definition_armx armEntity) throws SdaiException {
		CxFeature_definition_armx.setFeature_model(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes mapping for attribute Feature_model.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/* Made derived	
	public static void unsetFeature_model(SdaiContext context,
			EPart_feature_template_definition_armx armEntity) throws SdaiException {
		CxFeature_definition_armx.unsetFeature_model(context, armEntity);
	}
*/
}