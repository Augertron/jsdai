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

package jsdai.SDimension_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SShape_dimension_schema.ADimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CShape_dimension_representation;
import jsdai.SShape_dimension_schema.EDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.EShape_dimension_representation;
import jsdai.SShape_tolerance_schema.CPlus_minus_tolerance;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxDimension_value_with_limitation extends CDimension_value_with_limitation implements EMappedXIMEntity
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlus_minus_tolerance.definition);

		setMappingConstraints(context, this);

		// limited_value : measure_representation_item;
		setLimited_value(context, this);

		// clean ARM
		// defined_in : geometric_representation_context;
		unsetLimited_value(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// limited_value : measure_representation_item;
			unsetLimited_value(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EDimension_value_with_limitation armEntity) throws SdaiException {
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
			EDimension_value_with_limitation armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for defined_in attribute.
	 * 
	 * <p>
	attribute_mapping limited_value(limited_value, ($PATH)($PATH), measure_representation_item);
		((plus_minus_tolerance
		plus_minus_tolerance.toleranced_dimension ->
		dimensional_characteristic
		dimensional_characteristic = dimensional_size
		dimensional_size
		dimensional_characteristic = dimensional_size
		dimensional_characteristic <-
		dimensional_characteristic_representation.dimension
		dimensional_characteristic_representation
		dimensional_characteristic_representation.representation ->
		shape_dimension_representation <=
		shape_representation <=
		representation
		representation.items[i] ->
		representation_item =>
		measure_representation_item))((plus_minus_tolerance
		plus_minus_tolerance.toleranced_dimension ->
		dimensional_characteristic
		dimensional_characteristic = dimensional_location
		dimensional_location
		dimensional_characteristic = dimensional_location
		dimensional_characteristic <-
		dimensional_characteristic_representation.dimension
		dimensional_characteristic_representation
		dimensional_characteristic_representation.representation ->
		shape_dimension_representation <=
		shape_representation <=
		representation
		representation.items[i] ->
		representation_item =>
		measure_representation_item))
	end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PMT -> DS/DC <- DCR -> SDR -> MRI 
	public static void setLimited_value(SdaiContext context,
			EDimension_value_with_limitation armEntity) throws SdaiException {
		unsetLimited_value(context, armEntity);
		if(armEntity.testLimited_value(null)){
			EMeasure_representation_item emri = armEntity.getLimited_value(null);
			EEntity ee = armEntity.getToleranced_dimension(null);
			// SDR
			LangUtils.Attribute_and_value_structure[] srStructure = {
					new LangUtils.Attribute_and_value_structure(
							CShape_dimension_representation.attributeItems(null), emri)
			};
			EShape_dimension_representation esdr = (EShape_dimension_representation) 
				LangUtils.createInstanceIfNeeded(context,
						CShape_dimension_representation.definition, srStructure);
			if(!esdr.testName(null)){
				esdr.setName(null, "");
			}
			// EDCR
			EDimensional_characteristic_representation edcr = (EDimensional_characteristic_representation)
				context.working_model.createEntityInstance(CDimensional_characteristic_representation.definition);
			edcr.setDimension(null, ee);
			edcr.setRepresentation(null, esdr);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLimited_value(SdaiContext context,
			EDimension_value_with_limitation armEntity) throws SdaiException {
		if(armEntity.testToleranced_dimension(null)){
			EEntity ee = armEntity.getToleranced_dimension(null);
			ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
			CDimensional_characteristic_representation.usedinDimension(null, ee, context.domain, adcr);
			for(int i=1,n=adcr.getMemberCount(); i<=n; i++){
				EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
				edcr.deleteApplicationInstance();
			}
		}
	}
	
}