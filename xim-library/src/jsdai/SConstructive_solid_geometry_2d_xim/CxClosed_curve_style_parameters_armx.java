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

package jsdai.SConstructive_solid_geometry_2d_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_mim.CClosed_curve_style_parameters;
import jsdai.SRepresentation_schema.*;

public class CxClosed_curve_style_parameters_armx extends CClosed_curve_style_parameters_armx implements EMappedXIMEntity
{

	// Taken from Representations
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

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testContext_of_items(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
		return (ERepresentation_context)get_instance(a2);
	}*/
	boolean testContext_of_items2(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	ERepresentation_context getContext_of_items2(ERepresentation type) throws SdaiException {
		return (ERepresentation_context)get_instance(a2);
	}
	
	public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetContext_of_items(ERepresentation type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
		return a2$;
	}

	// methods for attribute: items, base type: SET OF ENTITY
/*	public static int usedinItems(ERepresentation type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}*/
	boolean testItems2(ERepresentation type) throws SdaiException {
		return test_aggregate(a1);
	}
	public ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
		return (ARepresentation_item)get_aggregate(a1);
	}
	public ARepresentation_item createItems(ERepresentation type) throws SdaiException {
		a1 = (ARepresentation_item)create_aggregate_class(a1, a1$,  ARepresentation_item.class, 0);
		return a1;
	}
	public void unsetItems(ERepresentation type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(ERepresentation type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Representations
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CClosed_curve_style_parameters.definition);

		setMappingConstraints(context, this);

        // name_x : STRING;
		setName_x(context, this);
        
		// corner_style : extend_or_chord_2_extend_or_truncate_or_round;
		setCorner_style(context, this);
        
		// curve_width : length_measure_with_unit;
		setCurve_width(context, this);
        
		// width_uncertainty : length_measure_with_unit;		
		setWidth_uncertainty(context, this);
		
		// Clean up ARM
        // name_x : STRING;
		unsetName_x(null);
        
		// corner_style : extend_or_chord_2_extend_or_truncate_or_round;
		unsetCorner_style(null);
        
		// curve_width : length_measure_with_unit;
		unsetCurve_width(null);
        
		// width_uncertainty : length_measure_with_unit;		
		unsetWidth_uncertainty(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
        // name_x : STRING;
		unsetName_x(context, this);
        
		// corner_style : extend_or_chord_2_extend_or_truncate_or_round;
		unsetCorner_style(context, this);
        
		// curve_width : length_measure_with_unit;
		unsetCurve_width(context, this);
        
		// width_uncertainty : length_measure_with_unit;		
		unsetWidth_uncertainty(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		closed_curve_style_parameters <=
		curve_style_parameters_representation <=
		representation
		[representation
		representation.context_of_items ->
		representation_context
		[representation_context
		representation_context =>
		global_uncertainty_assigned_context]
		[representation_context
		representation_context =>
		parametric_representation_context]]
		[representation
		representation.context_of_items ->
		representation_context
		representation_context.context_type
		representation_context.context_type = 'curve style parametric context']

	end_mapping_constraints;	
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxCurve_style_parameters.setMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setName(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetMappingConstraints(context, armEntity);
	}
	
   //********** "curve_style" attributes

	public static void setName_x(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.setName_x(context, armEntity);
	}

	public static void unsetName_x(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetName_x(context, armEntity);
	}

	public static void setCorner_style(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.setCorner_style(context, armEntity);
	}

	public static void unsetCorner_style(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetCorner_style(context, armEntity);
	}

	public static void setCurve_width(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.setCurve_width(context, armEntity);
	}

	public static void unsetCurve_width(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetCurve_width(context, armEntity);
	}

	public static void setWidth_uncertainty(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.setWidth_uncertainty(context, armEntity);
	}

	public static void unsetWidth_uncertainty(SdaiContext context, EClosed_curve_style_parameters_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetWidth_uncertainty(context, armEntity);
	}
	
	
	
}
