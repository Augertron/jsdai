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
*/

import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SShape_dimension_schema.CAngular_location$directed_dimensional_location;
import jsdai.SShape_dimension_schema.CAngular_location;
import jsdai.SShape_dimension_schema.EAngle_relator;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxAngular_location_dimension extends CAngular_location_dimension implements EMappedXIMEntity
{

	/* Taken from Shape_aspect_relationship */
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EShape_aspect_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EShape_aspect_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EShape_aspect_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect_relationship type) throws SdaiException {
		return a0$;
	}
	
	/* Taken from Angular_size */
	// attribute:angle_selection, base type: ENUMERATION
/*	public boolean testAngle_selection(EAngular_location type) throws SdaiException {
		return test_enumeration(a4);
	}
	public int getAngle_selection(EAngular_location type) throws SdaiException {
		return get_enumeration(a4);
	}
	public void setAngle_selection(EAngular_location type, int value) throws SdaiException {
		a4 = set_enumeration(value, a4$);
	}
	public void unsetAngle_selection(EAngular_location type) throws SdaiException {
		a4 = unset_enumeration();
	}
	public static jsdai.dictionary.EAttribute attributeAngle_selection(EAngular_location type) throws SdaiException {
		return a4$;
	}*/

	/* Taken from Shape_aspect_relationship */
/*	public static int usedinRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a2);
	}
	public EShape_aspect getRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return (EShape_aspect)get_instance(a2);
	}
	public void setRelating_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a2$;
	}*/

	//going through all the attributes: #5629499534229212=EXPLICIT_ATTRIBUTE('related_shape_aspect',#5629499534229207,3,#5629499534229200,$,.F.);
	//<01> generating methods for consolidated attribute:  related_shape_aspect
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : related_shape_aspect, base type: entity shape_aspect
/*	public static int usedinRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return test_instance(a3);
	}
	public EShape_aspect getRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return (EShape_aspect)get_instance(a3);
	}
	public void setRelated_shape_aspect(EShape_aspect_relationship type, EShape_aspect value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeRelated_shape_aspect(EShape_aspect_relationship type) throws SdaiException {
		return a3$;
	}*/
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		if((testDirected(null))&&(getDirected(null))){
			setTemp("AIM", CAngular_location$directed_dimensional_location.definition);
		}else{
			setTemp("AIM", CAngular_location.definition);
		}

		setMappingConstraints(context, this);

		// id : STRING;
		setId_x(context, this);

		// single_value : OPTIONAL measure_representation_item;
		setSingle_value(context, this);

		// lower_range : OPTIONAL measure_representation_item;
		setLower_range(context, this);

		// upper_range : OPTIONAL measure_representation_item;
		setUpper_range(context, this);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		setNotes(context, this); 
		
		// theoretical_exact : BOOLEAN;		
		setTheoretical_exact(context, this);

		// auxiliary         : BOOLEAN;	
		setAuxiliary(context, this);

		// orientation : OPTIONAL axis2_placement;	
		setOrientation(context, this);

		// envelope_principle : OPTIONAL BOOLEAN;
		setEnvelope_principle(context, this);
		
		// Unset attributes
		// id : STRING;
		unsetId_x(null);
		
		// single_value : OPTIONAL measure_representation_item;
		unsetSingle_value(null);

		// lower_range : OPTIONAL measure_representation_item;
		unsetLower_range(null);

		// upper_range : OPTIONAL measure_representation_item;
		unsetUpper_range(null);
		
		// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
		unsetNotes(null); 
		
		// theoretical_exact : BOOLEAN;		
		unsetTheoretical_exact(null);

		// auxiliary         : BOOLEAN;	
		unsetAuxiliary(null);

		// orientation : OPTIONAL axis2_placement;	
		unsetOrientation(null);

		// envelope_principle : OPTIONAL BOOLEAN;
		unsetEnvelope_principle(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// id : STRING;
			unsetId_x(context, this);

			// single_value : OPTIONAL measure_representation_item;
			unsetSingle_value(context, this);

			// lower_range : OPTIONAL measure_representation_item;
			unsetLower_range(context, this);

			// upper_range : OPTIONAL measure_representation_item;
			unsetUpper_range(context, this);
			
			// notes : OPTIONAL SET[1:?] OF STRING; (** Originally it is mandatory SET[0:?] **)
			unsetNotes(context, this); 
			
			// theoretical_exact : BOOLEAN;		
			unsetTheoretical_exact(context, this);

			// auxiliary         : BOOLEAN;	
			unsetAuxiliary(context, this);

			// orientation : OPTIONAL axis2_placement;	
			unsetOrientation(context, this);

			// envelope_principle : OPTIONAL BOOLEAN;
			unsetEnvelope_principle(context, this);
			
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
	public static void setMappingConstraints(SdaiContext context, EAngular_location_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxLocation_dimension.setMappingConstraints(context, armEntity);
		if(!armEntity.testAngle_selection(null)){
			armEntity.setAngle_selection(null, EAngle_relator.EQUAL);
		}
		armEntity.setName(null, "");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EAngular_location_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetMappingConstraints(context, armEntity);
	}


	/**
	 * Sets/creates data for Single_value attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setSingle_value(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setSingle_value(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetSingle_value(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetSingle_value(context, armEntity);
	}

	/**
	 * Sets/creates data for lower_range attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setLower_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setLower_range(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetLower_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetLower_range(context, armEntity);
	}

	/**
	 * Sets/creates data for Upper_range attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setUpper_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setUpper_range(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetUpper_range(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetUpper_range(context, armEntity);		
	}

	/**
	 * Sets/creates data for notes attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setNotes(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setNotes(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetNotes(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetNotes(context, armEntity);		
	}
	
	/**
	 * Sets/creates data for orientation attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setOrientation(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setOrientation(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetOrientation(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetOrientation(context, armEntity);
	}

	/**
	 * Sets/creates data for Theoretical_exact attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setTheoretical_exact(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setTheoretical_exact(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetTheoretical_exact(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetTheoretical_exact(context, armEntity);
	}

	/**
	 * Sets/creates data for Auxiliary attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setAuxiliary(context, armEntity);
	}

	/**
	 * Unsets/deletes data for Auxiliary attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetAuxiliary(context, armEntity);
	}

	/**
	 * Sets/creates data for id_x attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setId_x(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setId_x(context, armEntity);
	}

	/**
	 * Unsets/deletes data for id_x attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId_x(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetId_x(context, armEntity);
	}	
	
	/**
	 * Sets/creates data for envelope_principle attribute.
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setEnvelope_principle(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setEnvelope_principle(context, armEntity);
	}

	/**
	 * Unsets/deletes data for envelope_principle attribute.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetEnvelope_principle(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.unsetEnvelope_principle(context, armEntity);
	}	
}