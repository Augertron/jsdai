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

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SMeasure_schema.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SQualified_measure_schema.CDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SRepresentation_schema.*;

public class CxCurve_style_parameters extends CCurve_style_parameters implements EMappedXIMEntity
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
		a2 = set_instance(a2, value);
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
	ARepresentation_item getItems2(ERepresentation type) throws SdaiException {
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

		setTemp("AIM", CShape_representation.definition);

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
	* mapping_constraints;
	* 	representation
	*  [representation
	*  representation.context_of_items -&gt;
	*  representation_context
	*  [representation_context
	*  representation_context =&gt;
	*  global_uncertainty_assigned_context]
	*  [representation_context
	*  representation_context =&gt;
	*  parametric_representation_context]]
	*  [representation
	*  representation.context_of_items -&gt;
	*  representation_context
	*  representation_context.context_type
	*  representation_context.context_type = 'curve style parametric context']
	*  [representation
	*  (representation.name = 'closed curve style parameters')
	*  (representation.name = 'curve style parameters with ends')]	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// Tricky workarround to avoid get methods
		ERepresentation_context erc = getContext_of_items3(armEntity);
		if(erc != null){
			erc.setContext_type(null, "curve style parametric context");
		}
		else{
		 // Need to create a new Context - can't reuse it, because of uncertainty values
		 CGlobal_uncertainty_assigned_context$parametric_representation_context
		 	erc2 = (CGlobal_uncertainty_assigned_context$parametric_representation_context) 
		 	context.working_model.createEntityInstance(CGlobal_uncertainty_assigned_context$parametric_representation_context.definition);
		 erc2.setContext_type(null, "curve style parametric context");
       	 erc2.setContext_identifier(null, "");
         armEntity.setContext_of_items(null, erc2);   
		}
		// AIM gap
//		if(!armEntity.testName(null)){
			armEntity.setName(null, "");
//		}
	}
	private static ERepresentation_context getContext_of_items3(ECurve_style_parameters armEntity) throws SdaiException {
		ERepresentation_context erc = null;		
		if(armEntity instanceof CxCurve_style_parameters){
			CxCurve_style_parameters temp = (CxCurve_style_parameters)armEntity;
			if(temp.testContext_of_items2(null)){
				erc = temp.getContext_of_items2(null);
			}
		}else if(armEntity instanceof CxClosed_curve_style_parameters_armx){
			CxClosed_curve_style_parameters_armx temp = (CxClosed_curve_style_parameters_armx)armEntity;
			if(temp.testContext_of_items2(null)){
				erc = temp.getContext_of_items2(null);
			}
		}else if(armEntity instanceof CxCurve_style_parameters_with_ends_armx){
			CxCurve_style_parameters_with_ends_armx temp = (CxCurve_style_parameters_with_ends_armx)armEntity;
			if(temp.testContext_of_items2(null)){
				erc = temp.getContext_of_items2(null);
			}
		}else{
			System.err.println("Unsupported type of Cx class within CxCurve_style_parameters "+armEntity);
		}
		return erc;
	}

	public static void unsetMappingConstraints(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		ERepresentation_context erc = getContext_of_items3(armEntity);
		if(erc == null){
			return;
		}
		erc.unsetContext_type(null);
	}	
	
    // name_x : STRING;
	/**
	 * attribute_mapping name_x(name_x, descriptive_representation_item.description);
		curve_style_parameters_representation <=
		representation
		representation.items[i] ->
		representation_item
		{representation_item.name = 'curve style name'}
		representation_item =>
		descriptive_representation_item
		descriptive_representation_item.description
	  end_attribute_mapping;
	 */
	// CSPR -> DRI
	public static void setName_x(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		unsetName_x(context, armEntity);
		if(armEntity.testName_x(null)){
			String name_x = armEntity.getName_x(null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "curve style name");
			edri.setDescription(null, name_x);
			ARepresentation_item items = getItems(armEntity);
			if(items == null){
				items = armEntity.createItems(null);
			}
			items.addUnordered(edri);
		}
	}	

	public static void unsetName_x(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		ARepresentation_item items = getItems(armEntity);
		if(items == null){
			return;
		}
		SdaiIterator iter = items.createIterator();
		while(iter.next()){
			ERepresentation_item item = items.getCurrentMember(iter);
			if(item instanceof EDescriptive_representation_item){
				if((item.testName(null))&&(item.getName(null).equals("curve style name"))){
					item.deleteApplicationInstance();
				}
			}
		}
	}
	static ARepresentation_item getItems(ECurve_style_parameters armEntity) throws SdaiException {
		ARepresentation_item items;
		if(armEntity instanceof CxCurve_style_parameters){
			CxCurve_style_parameters temp = (CxCurve_style_parameters)armEntity;
			if(temp.testItems2(null)){
				items = temp.getItems2(null);
			}else{
				return null;
			}
		}else if(armEntity instanceof CxClosed_curve_style_parameters_armx){
			CxClosed_curve_style_parameters_armx temp = (CxClosed_curve_style_parameters_armx)armEntity;
			if(temp.testItems2(null)){
				items = temp.getItems2(null);
			}else{
				return null;
			}
		}else if(armEntity instanceof CxCurve_style_parameters_with_ends_armx){
			CxCurve_style_parameters_with_ends_armx temp = (CxCurve_style_parameters_with_ends_armx)armEntity;
			if(temp.testItems2(null)){
				items = temp.getItems2(null);
			}else{
				return null;
			}
		}else{
			return null;
		}
		return items;
	}	
	
	
	// corner_style : extend_or_chord_2_extend_or_truncate_or_round;
	/**
	 * attribute_mapping corner_style(corner_style, $PATH);
		curve_style_parameters_representation <=
		representation
		representation.items[i] ->
		representation_item
		{{representation_item.name = 'corner style'}
		representation_item =>
		descriptive_representation_item
		(descriptive_representation_item.description = 'chord 2 extend')
		(descriptive_representation_item.description = 'extend')
		(descriptive_representation_item.description = 'round')
		(descriptive_representation_item.description = 'truncate')
		}
	  end_attribute_mapping;
	 */
	// CSPR -> DRI
	public static void setCorner_style(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		unsetCorner_style(context, armEntity);
		if(armEntity.testCorner_style(null)){
			int cornerStyle = armEntity.getCorner_style(null);
			String cornerStyleName = 
				EExtend_or_chord_2_extend_or_truncate_or_round.toString(cornerStyle).toLowerCase().replace('_', ' ');
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "corner style");
			edri.setDescription(null, cornerStyleName);
			ARepresentation_item items = getItems(armEntity);
			if(items == null){
				items = armEntity.createItems(null);
			}
			items.addUnordered(edri);
		}
	}	

	public static void unsetCorner_style(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		ARepresentation_item items = getItems(armEntity);		
		if(items == null){
			return;
		}
		SdaiIterator iter = items.createIterator();
		while(iter.next()){
			ERepresentation_item item = items.getCurrentMember(iter);
			if(item instanceof EDescriptive_representation_item){
				if((item.testName(null))&&(item.getName(null).equals("corner style"))){
					item.deleteApplicationInstance();
				}
			}
		}
	}	
    
	// curve_width : length_measure_with_unit;
	/**
	 * attribute_mapping curve_width(curve_width, $PATH, length_measure_with_unit);
			curve_style_parameters_representation <=
			representation
			representation.items[i] ->
			representation_item
			{representation_item
			representation_item.name = 'curve width'}
			representation_item =>
			measure_representation_item <= 
			measure_with_unit =>
			length_measure_with_unit

	  end_attribute_mapping;
	 */
	// CSPR -> LMWU
	public static void setCurve_width(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		unsetCurve_width(context, armEntity);
		if(armEntity.testCurve_width(null)){
			ELength_measure_with_unit lmwu = armEntity.getCurve_width(null);
			ERepresentation_item eri;
			if(!(lmwu instanceof ERepresentation_item)){
				eri = (ERepresentation_item)
					context.working_model.substituteInstance(lmwu, CLength_measure_with_unit$measure_representation_item.definition);
			}else{
				eri = (ERepresentation_item)lmwu;
			}
			eri.setName(null, "curve width");
			ARepresentation_item items = getItems(armEntity);
			if(items == null){
				items = armEntity.createItems(null);
			}
			items.addUnordered(eri);
		}
	}	

	public static void unsetCurve_width(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		ARepresentation_item items = getItems(armEntity);		
		if(items == null){
			return;
		}
		for(int i=1;i<=items.getMemberCount();){
			ERepresentation_item item = items.getByIndex(i);
			if(item instanceof ELength_measure_with_unit){
				if((item.testName(null))&&(item.getName(null).equals("curve width"))){
					items.removeUnordered(item);
				}else{
					i++;
				}
			}else{
				i++;
			}
		}
	}	

	
	// width_uncertainty : length_measure_with_unit;		
	/**
	 * attribute_mapping width_uncertainty(width_uncertainty, $PATH, length_measure_with_unit);
		curve_style_parameters_representation <=
		representation
		representation.context_of_items ->
		representation_context
		representation_context =>
		global_uncertainty_assigned_context
		global_uncertainty_assigned_context.uncertainty[i] ->
		uncertainty_measure_with_unit <=
		measure_with_unit =>
		length_measure_with_unit

	  end_attribute_mapping;
	 */
	// CSPR -> LMWU
	public static void setWidth_uncertainty(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		unsetWidth_uncertainty(context, armEntity);
		if(armEntity.testWidth_uncertainty(null)){
			ELength_measure_with_unit lmwu = armEntity.getWidth_uncertainty(null);
			EUncertainty_measure_with_unit umwu;
			if(!(lmwu instanceof EUncertainty_measure_with_unit)){
				umwu = (EUncertainty_measure_with_unit)
					context.working_model.substituteInstance(lmwu, CLength_measure_with_unit$uncertainty_measure_with_unit.definition);
			}else{
				umwu = (EUncertainty_measure_with_unit)lmwu;
			}
			if(!umwu.testName(null)){
				umwu.setName(null, "");
			}
			EGlobal_uncertainty_assigned_context repContext = (EGlobal_uncertainty_assigned_context)
				getContext_of_items3(armEntity);
			repContext.createUncertainty(null).addUnordered(umwu);
			// System.err.println(repContext+" Adding "+umwu);
		}
	}	

	public static void unsetWidth_uncertainty(SdaiContext context, ECurve_style_parameters armEntity) throws SdaiException
	{
		EGlobal_uncertainty_assigned_context repContext = (EGlobal_uncertainty_assigned_context)
			getContext_of_items3(armEntity);
		if(repContext == null){
			return;
		}
        repContext.unsetUncertainty(null);
	}	
	
	
}
