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

import jsdai.SShape_aspect_definition_schema.CDatum_reference;
import jsdai.SShape_aspect_definition_schema.CReferenced_modified_datum;
import jsdai.SShape_aspect_definition_schema.EReferenced_modified_datum;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxDatum_reference_armx extends CDatum_reference_armx implements EMappedXIMEntity
{
	//going through all the attributes: #5629499534230233=EXPLICIT_ATTRIBUTE('modifier',#5629499534230231,0,#5629499534230212,$,.F.);
	//<01> generating methods for consolidated attribute:  modifier
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute:modifier, base type: ENUMERATION
	public boolean testModifier(EReferenced_modified_datum type) throws SdaiException {
		return test_enumeration(a2);
	}
	public int getModifier(EReferenced_modified_datum type) throws SdaiException {
		return get_enumeration(a2);
	}
	public void setModifier(EReferenced_modified_datum type, int value) throws SdaiException {
		a2 = set_enumeration(value, a2$);
	}
	public void unsetModifier(EReferenced_modified_datum type) throws SdaiException {
		a2 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeModifier(EReferenced_modified_datum type) throws SdaiException {
		return a2$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		if(testModification(null)){
			setTemp("AIM", CReferenced_modified_datum.definition);
		}else{
			setTemp("AIM", CDatum_reference.definition);
		}
		setMappingConstraints(context, this);

		// modification : OPTIONAL limit_condition;
		setModification(context, this);
		
//		// clean ARM

		// modification : OPTIONAL limit_condition;
		unsetModification(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// modification : OPTIONAL limit_condition;
			unsetModification(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		physical_unit_datum <=
		shape_aspect
		{shape_aspect
		shape_aspect.description = 'plane'}
	end_mapping_constraints;	
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EDatum_reference_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EDatum_reference_armx armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	attribute_mapping modification(modification, $PATH, limit_condition);
		datum_reference 
	 	datum_reference => 
	 	referenced_modified_datum 
		referenced_modified_datum.modifier ->
		limit_condition
		(limit_condition = .MAXIMUM_MATERIAL_CONDITION.)
		(limit_condition = .LEAST_MATERIAL_CONDITION.)
		(limit_condition = .REGARDLESS_OF_FEATURE_SIZE.)
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */

	public static void setModification(SdaiContext context, EDatum_reference_armx armEntity) throws SdaiException {
		unsetModification(context, armEntity);
		if(armEntity.testModification(null)){
			int value = armEntity.getModification(null);
			EReferenced_modified_datum ermd = (EReferenced_modified_datum)armEntity;
			ermd.setModifier(null, value);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetModification(SdaiContext context, EDatum_reference_armx armEntity) throws SdaiException {
		if(armEntity instanceof EReferenced_modified_datum){
			EReferenced_modified_datum ermd = (EReferenced_modified_datum)armEntity;
			ermd.unsetModifier(null);
		}
	}
	
}