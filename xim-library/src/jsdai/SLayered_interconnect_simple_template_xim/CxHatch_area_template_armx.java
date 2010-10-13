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
import jsdai.SConstructive_solid_geometry_2d_mim.EClosed_curve_style_parameters;
import jsdai.SConstructive_solid_geometry_2d_xim.ECurve_style_parameters_with_ends_armx;
import jsdai.SGeometry_schema.EGeometric_representation_context;
import jsdai.SLayered_interconnect_simple_template_mim.*;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SPart_template_xim.*;
import jsdai.SProduct_definition_schema.EProduct_definition;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SMixed_complex_types.*;

public class CxHatch_area_template_armx extends CHatch_area_template_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	
	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a2);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a2 = get_instance_select(a2);
		return (EEntity)a2;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a2 = set_instanceX(a2, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a2$;
	}

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a0$;
	}
	
	// END OF Property_definition
	
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a4);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a4$;
	}

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}*/
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CHatch_area_template.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		
		setPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		setId_x(context, this);

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		// fill_area_style 
		// setFill_area_style(context, this);
		
		// outer_boundary_style
		setOuter_boundary_style(context, this);

		// inner_boundary_style
		setInner_boundary_style(context, this);
		
	    // fill_boundary_style : curve_style_parameters_with_ends_armx;
		setFill_boundary_style(context, this);
		
        // hatch_pattern : SET [1:4] OF hatch_line_element_armx;
		setHatch_pattern(context, this);
		
        // min_feature : OPTIONAL length_measure_with_unit;
		setMin_feature(context, this);
		
		// Clean ARM specific attributes
//		unsetId_x(null);
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetPhysical_characteristic(null);

		// fill_area_style 
		// unsetFill_area_style(null);
		
		// outer_boundary_style
		unsetOuter_boundary_style(null);

		// inner_boundary_style
		unsetInner_boundary_style(null);
	
	    // fill_boundary_style : curve_style_parameters_with_ends_armx;
		unsetFill_boundary_style(null);
		
        // hatch_pattern : SET [1:4] OF hatch_line_element_armx;
		unsetHatch_pattern(null);
		
        // min_feature : OPTIONAL length_measure_with_unit;
		unsetMin_feature(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		unsetPhysical_characteristic(context, this);
		//********** "managed_design_object" attributes

		//********** "item_shape" attributes
//		unsetId_x(context, this);

		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);
		
		// fill_area_style 
		// unsetFill_area_style(context, this);
		
		// outer_boundary_style
		unsetOuter_boundary_style(context, this);

		// inner_boundary_style
		unsetInner_boundary_style(context, this);

	    // fill_boundary_style : curve_style_parameters_with_ends_armx;
		unsetFill_boundary_style(context, this);
		
        // hatch_pattern : SET [1:4] OF hatch_line_element_armx;
		unsetHatch_pattern(context, this);
		
        // min_feature : OPTIONAL length_measure_with_unit;
		unsetMin_feature(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  part_template_definition &lt;=
	*  {product_definition
	*  (product_definition.description = 'trace template')
	*  (product_definition.description = 'default trace template')}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		
		CxFill_area_template_armx.setMappingConstraints(context, armEntity);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException
	{
		CxFill_area_template_armx.unsetMappingConstraints(context, armEntity);
	}	
	//********** "managed_design_object" attributes

	//********** "item_shape" attributes
    /**
     * Sets/creates data for Id_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
/* Removed from XIM - see bug #3610
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }
*/
  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
/* Removed from XIM - see bug #3610
    public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.unsetId_x(context, armEntity);
   }
*/
 	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_characterization(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
   }

    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_contexts(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

    // Template_definition, originally developed in Part_template_definition
 	/**
 	* Sets/creates data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
    // PTD <- PD <- PDR -> R -> RI <- AGA -> CT
 	public static void setPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
 	{
 		CxTemplate_definition.setPhysical_characteristic(context, armEntity);
 	}


 	/**
 	* Unsets/deletes data for physical_characteristic attribute.
 	*
 	* @param context SdaiContext.
 	* @param armEntity arm entity.
 	* @throws SdaiException
 	*/
 	public static void unsetPhysical_characteristic(SdaiContext context, ETemplate_definition armEntity) throws SdaiException
   {
 		CxTemplate_definition.unsetPhysical_characteristic(context, armEntity); 		
 	}
 	

	//********** "fill_area_template" attributes
	
	/**
	 * Sets/creates data for trace_style attribute.
	 *
	 * <p>
		attribute_mapping fill_area_style(fill_area_style, $PATH, Hatch_style/Tile_style);
			fill_area_template <=
			parametric_template <=
			part_template_definition <=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			{property_definition
			property_definition.name = 'fill area style'}
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation
			representation.items[i] ->
			representation_item =>
			geometric_representation_item =>
			(fill_area_style_hatching)
			(externally_defined_hatch_style)
			(fill_area_style_tiles) 
			(externally_defined_tile_style) 
		end_attribute_mapping;
	 * </p>
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// FAT <- PD <- PDR -> R -> GRI
/* 	
	public static void setFill_area_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException
	{
		unsetFill_area_style(context, armEntity);
		if (armEntity.testFill_area_style(null)){
			ERepresentation_item style = (ERepresentation_item)armEntity.getFill_area_style(null);
			//1) R -> GRI
			ARepresentation representations = new ARepresentation();
			CRepresentation.usedinItems(null, style, context.domain, representations);
			SdaiIterator iter = representations.createIterator();
			ERepresentation suitableRepresentation = null;
			top: while(iter.next()){
				ERepresentation rep = representations.getCurrentMember(iter);
				ARepresentation_item items = rep.getItems(null);
				int count = items.getMemberCount();
				// Make sure the path is unambigous
				if(count == 1){
					suitableRepresentation = rep;
					break;
				}
				for(int i=1; i<=count; i++){
					ERepresentation_item item = items.getByIndex(i);
					// rep makes path ambigous - skip it
					if((item instanceof EFill_area_style_hatching)||
						(item instanceof EExternally_defined_hatch_style)||
						(item instanceof EFill_area_style_tiles)||
						(item instanceof EExternally_defined_tile_style)){
						if(item != style){
							continue top;
						}
					}
				}
				suitableRepresentation = rep;
				break;
			}
			if(suitableRepresentation == null){
				suitableRepresentation = CxAP210ARMUtilities.createRepresentation(context, "", false);
				suitableRepresentation.getItems(null).addUnordered(style);
			}
			// 2) FAT <- PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "fill area style");
			// PD <- PDR -> R
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, epd);
			epdr.setUsed_representation(null, suitableRepresentation);
		}
	}
*/


	/**
	 * Unsets/deletes data for trace_style attribute.
	 *
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
/*	public static void unsetFill_area_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException
	{
			jsdai.SProduct_property_definition_schema.AProperty_definition aPd =
				new jsdai.SProduct_property_definition_schema.AProperty_definition();
			jsdai.SProduct_property_definition_schema.EProperty_definition pd = null;
			jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armEntity,context.domain,aPd);
			if (aPd.getMemberCount()==0)
			{
				return;
			}
			for(int j=1;j<=aPd.getMemberCount();j++){
				pd = aPd.getByIndex(j);
				if((!pd.testName(null))||(!pd.getName(null).equals("fill area style"))){
					continue;
				}
				AProperty_definition_representation aPdr = new  AProperty_definition_representation();
				EProperty_definition_representation pdr = null;
				CProperty_definition_representation.usedinDefinition(null,pd,context.domain,aPdr);
				if (aPdr.getMemberCount()>=1)
				{
					for(int i=1;i<=aPdr.getMemberCount();i++)
					{
						pdr = aPdr.getByIndex(i);
						pdr.deleteApplicationInstance();
					}
				}
				pd.deleteApplicationInstance();
			}
	}
*/
	/**
	 * Sets/creates data for outer_boundary_style attribute.
	 *
	 * <p>
		attribute_mapping outer_boundary_style(outer_boundary_style, $PATH, Closed_curve_style_parameters_armx);
			fill_area_template <=
			parametric_template <=
			part_template_definition <=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			{property_definition
			property_definition.name = 'outer boundary style'}
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation =>
			curve_style_parameters_representation =>
			closed_curve_style_parameters
		end_attribute_mapping;
	 * </p>
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// FAT <- PD <- PDR -> CCSP
	public static void setOuter_boundary_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException
	{
		if (armEntity.testOuter_boundary_style(null)){
			EClosed_curve_style_parameters style = armEntity.getOuter_boundary_style(null);
			// 2) FAT <- PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "outer boundary style");
			// PD <- PDR -> R
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, epd);
			epdr.setUsed_representation(null, style);
		}
	}



	/**
	 * Unsets/deletes data for outer_boundary_style attribute.
	 *
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	public static void unsetOuter_boundary_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException
	{
			jsdai.SProduct_property_definition_schema.AProperty_definition aPd =
				new jsdai.SProduct_property_definition_schema.AProperty_definition();
			jsdai.SProduct_property_definition_schema.EProperty_definition pd = null;
			jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armEntity,context.domain,aPd);
			if (aPd.getMemberCount()==0)
			{
				return;
			}
			for(int j=1;j<=aPd.getMemberCount();j++){
				pd = aPd.getByIndex(j);
				if((!pd.testName(null))||(!pd.getName(null).equals("outer boundary style"))){
					continue;
				}
				AProperty_definition_representation aPdr = new  AProperty_definition_representation();
				EProperty_definition_representation pdr = null;
				CProperty_definition_representation.usedinDefinition(null,pd,context.domain,aPdr);
				if (aPdr.getMemberCount()>=1)
				{
					for(int i=1;i<=aPdr.getMemberCount();i++)
					{
						pdr = aPdr.getByIndex(i);
						pdr.deleteApplicationInstance();
					}
				}
				pd.deleteApplicationInstance();
			}
	}

	/**
	 * Sets/creates data for inner_boundary_style attribute.
	 *
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// FAT <- PD <- PDR -> CCSP
	public static void setInner_boundary_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException{
		CxFill_area_template_armx.setInner_boundary_style(context, armEntity);
	}

	public static void unsetInner_boundary_style(SdaiContext context, EFill_area_template_armx armEntity) throws SdaiException{
		CxFill_area_template_armx.unsetInner_boundary_style(context, armEntity);
	}

	/**
	 * Sets/creates data for Fill_boundary_style attribute.
	 *
		attribute_mapping fill_boundary_style(fill_boundary_style, $PATH, Curve_style_parameters_with_ends_armx);
			hatch_area_template <=
			fill_area_template <=
			parametric_template <=					
			part_template_definition <=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			{property_definition
			property_definition.name = 'fill boundary style'}
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation =>
			curve_style_parameters_representation =>
			curve_style_parameters_with_ends
		end_attribute_mapping;
	 *
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// HAT <- PD <- PDR -> CSPWE
	public static void setFill_boundary_style(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		unsetFill_boundary_style(context, armEntity);
		if (armEntity.testFill_boundary_style(null)){
			ECurve_style_parameters_with_ends_armx style = armEntity.getFill_boundary_style(null);
			// 2) FAT <- PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "fill boundary style");
			// PD <- PDR -> R
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, epd);
			epdr.setUsed_representation(null, style);
		}
	}

	public static void unsetFill_boundary_style(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		jsdai.SProduct_property_definition_schema.AProperty_definition aPd =
			new jsdai.SProduct_property_definition_schema.AProperty_definition();
		jsdai.SProduct_property_definition_schema.EProperty_definition pd = null;
		jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armEntity,context.domain,aPd);
		if (aPd.getMemberCount()==0)
		{
			return;
		}
		for(int j=1;j<=aPd.getMemberCount();j++){
			pd = aPd.getByIndex(j);
			if((!pd.testName(null))||(!pd.getName(null).equals("fill boundary style"))){
				continue;
			}
			AProperty_definition_representation aPdr = new  AProperty_definition_representation();
			EProperty_definition_representation pdr = null;
			CProperty_definition_representation.usedinDefinition(null,pd,context.domain,aPdr);
			if (aPdr.getMemberCount()>=1)
			{
				for(int i=1;i<=aPdr.getMemberCount();i++)
				{
					pdr = aPdr.getByIndex(i);
					pdr.deleteApplicationInstance();
				}
			}
			pd.deleteApplicationInstance();
		}
	}

	/**
	 * Sets/creates data for hatch_pattern attribute.
	 *
		attribute_mapping hatch_pattern(hatch_pattern, $PATH, Hatch_line_element_armx);
			hatch_area_template <=
			fill_area_template <=
			parametric_template <=					
			part_template_definition <=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			{property_definition
			property_definition.name = 'hatch_pattern'}
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation
			representation.items[i] ->
			representation_item =>
			geometric_representation_item =>
			hatch_line_element
		end_attribute_mapping;
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// HAT <- PD <- PDR -> R -> HLE
	public static void setHatch_pattern(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		unsetHatch_pattern(context, armEntity);
		if (armEntity.testHatch_pattern(null)){
			AHatch_line_element_armx hles = armEntity.getHatch_pattern(null);
			for(int i=1,count=hles.getMemberCount(); i<=count; i++){
				// 2) FAT <- PD
				EProperty_definition epd = (EProperty_definition)
					context.working_model.createEntityInstance(CProperty_definition.definition);
				epd.setDefinition(null, armEntity);
				epd.setName(null, "hatch pattern");
				// HLE and rep
				EHatch_line_element_armx hle = hles.getByIndex(i);
				ARepresentation are = new ARepresentation();
				CRepresentation.usedinItems(null, hle, context.domain, are);
				ERepresentation er;
				if(are.getMemberCount() > 0){
					er = are.getByIndex(1);
				}else{
					EGeometric_representation_context egrc = CxAP210ARMUtilities.createGeometric_representation_context(context, "", "", 2, true);
					EShape_representation esr = (EShape_representation)context.working_model.createEntityInstance(CShape_representation.definition);
					esr.setName(null, "");
					esr.setContext_of_items(null, egrc);
					esr.createItems(null).addUnordered(hle);
					er = esr;
				}
				// PD <- PDR -> R
				EProperty_definition_representation epdr = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				epdr.setDefinition(null, epd);
				epdr.setUsed_representation(null, er);
			}
		}
	}

	public static void unsetHatch_pattern(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		jsdai.SProduct_property_definition_schema.AProperty_definition aPd =
			new jsdai.SProduct_property_definition_schema.AProperty_definition();
		jsdai.SProduct_property_definition_schema.EProperty_definition pd = null;
		jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armEntity,context.domain,aPd);
		if (aPd.getMemberCount()==0)
		{
			return;
		}
		for(int j=1;j<=aPd.getMemberCount();j++){
			pd = aPd.getByIndex(j);
			if((!pd.testName(null))||(!pd.getName(null).equals("hatch pattern"))){
				continue;
			}
			AProperty_definition_representation aPdr = new  AProperty_definition_representation();
			EProperty_definition_representation pdr = null;
			CProperty_definition_representation.usedinDefinition(null,pd,context.domain,aPdr);
			if (aPdr.getMemberCount()>=1)
			{
				for(int i=1;i<=aPdr.getMemberCount();i++)
				{
					pdr = aPdr.getByIndex(i);
					pdr.deleteApplicationInstance();
				}
			}
			pd.deleteApplicationInstance();
		}
	}

	/**
	 * Sets/creates data for min_feature attribute.
	 *
		attribute_mapping min_feature(min_feature, $PATH, length_measure_with_unit);
			hatch_area_template <=
			fill_area_template <=
			parametric_template <=					
			part_template_definition <=
			product_definition
			characterized_product_definition = product_definition
			characterized_product_definition
			characterized_definition = characterized_product_definition
			characterized_definition <-
			property_definition.definition
			property_definition <-
			{property_definition
			property_definition.name = 'min feature'}
			property_definition_representation.definition
			property_definition_representation
			property_definition_representation.used_representation ->
			representation
			representation.items[i] ->
			representation_item =>
			measure_representation_item <=
			measure_with_unit =>
			length_measure_with_unit
		end_attribute_mapping;
	 * @param context SdaiContext.
	 * @param armEntity arm entity.
	 * @throws SdaiException
	 */
	// HAT <- PD <- PDR -> R -> LMWU
	public static void setMin_feature(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		unsetMin_feature(context, armEntity);
		if (armEntity.testMin_feature(null)){
			ELength_measure_with_unit lmwu = armEntity.getMin_feature(null);
			ERepresentation_item eri;
			if(lmwu instanceof ERepresentation_item){
				eri = (ERepresentation_item)lmwu;
			}else{
				eri = (ERepresentation_item)
					context.working_model.substituteInstance(lmwu, CLength_measure_with_unit$measure_representation_item.definition);
				eri.setName(null, "");
			}
			// 2) FAT <- PD
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setDefinition(null, armEntity);
			epd.setName(null, "min feature");
			// LMWU and rep
			ARepresentation are = new ARepresentation();
			CRepresentation.usedinItems(null, eri, context.domain, are);
			ERepresentation er;
			if(are.getMemberCount() > 0){
				er = are.getByIndex(1);
			}else{
				EGeometric_representation_context egrc = CxAP210ARMUtilities.createGeometric_representation_context(context, "", "", 2, true);
				EShape_representation esr = (EShape_representation)context.working_model.createEntityInstance(CShape_representation.definition);
				esr.setName(null, "");
				esr.setContext_of_items(null, egrc);
				esr.createItems(null).addUnordered(eri);
				er = esr;
			}
			// PD <- PDR -> R
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, epd);
			epdr.setUsed_representation(null, er);
		}
	}

	public static void unsetMin_feature(SdaiContext context, EHatch_area_template_armx armEntity) throws SdaiException{
		jsdai.SProduct_property_definition_schema.AProperty_definition aPd =
			new jsdai.SProduct_property_definition_schema.AProperty_definition();
		jsdai.SProduct_property_definition_schema.EProperty_definition pd = null;
		jsdai.SProduct_property_definition_schema.CProperty_definition.usedinDefinition(null,armEntity,context.domain,aPd);
		if (aPd.getMemberCount()==0)
		{
			return;
		}
		for(int j=1;j<=aPd.getMemberCount();j++){
			pd = aPd.getByIndex(j);
			if((!pd.testName(null))||(!pd.getName(null).equals("min feature"))){
				continue;
			}
			AProperty_definition_representation aPdr = new  AProperty_definition_representation();
			EProperty_definition_representation pdr = null;
			CProperty_definition_representation.usedinDefinition(null,pd,context.domain,aPdr);
			if (aPdr.getMemberCount()>=1)
			{
				for(int i=1;i<=aPdr.getMemberCount();i++)
				{
					pdr = aPdr.getByIndex(i);
					pdr.deleteApplicationInstance();
				}
			}
			pd.deleteApplicationInstance();
		}
	}
	
	
}
