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

import jsdai.SMixed_complex_types.CDimensional_location_with_path$directed_dimensional_location;
import jsdai.SShape_dimension_schema.CDimensional_location_with_path;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxCurved_distance_dimension extends CCurved_distance_dimension implements EMappedXIMEntity
{

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
	/* Taken from EDimensional_location_with_path */
	// attribute (current explicit or supertype explicit) : path, base type: entity shape_aspect
/*	public static int usedinPath(EDimensional_location_with_path type, jsdai.SProduct_property_definition_schema.EShape_aspect instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testPath(EDimensional_location_with_path type) throws SdaiException {
		return test_instance(a4);
	}
	public jsdai.SProduct_property_definition_schema.EShape_aspect getPath(EDimensional_location_with_path type) throws SdaiException {
		return (jsdai.SProduct_property_definition_schema.EShape_aspect)get_instance(a4);
	}
	public void setPath(EDimensional_location_with_path type, jsdai.SProduct_property_definition_schema.EShape_aspect value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetPath(EDimensional_location_with_path type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributePath(EDimensional_location_with_path type) throws SdaiException {
		return a4$;
	}	*/
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		if((testDirected(null))&&(getDirected(null))){
			setTemp("AIM", CDimensional_location_with_path$directed_dimensional_location.definition);
		}else{
			setTemp("AIM", CDimensional_location_with_path.definition);
		}

		setMappingConstraints(context, this);

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

		
		// Unset attributes
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

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
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
	public static void setMappingConstraints(SdaiContext context, ECurved_distance_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxLocation_dimension.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, ECurved_distance_dimension armEntity) throws SdaiException {
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
	public static void setAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxLocation_dimension.setAuxiliary(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAuxiliary(SdaiContext context, ELocation_dimension armEntity) throws SdaiException {
		CxGDTCommon.unsetAuxiliary(context, armEntity);
	}
	
}