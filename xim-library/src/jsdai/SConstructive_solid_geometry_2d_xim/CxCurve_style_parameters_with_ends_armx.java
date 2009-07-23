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
import jsdai.util.LangUtils;
import jsdai.SConstructive_solid_geometry_2d_mim.CCurve_style_parameters_with_ends;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SRepresentation_schema.CGlobal_uncertainty_assigned_context$parametric_representation_context;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$measure_representation_item;
import jsdai.SMixed_complex_types.CLength_measure_with_unit$uncertainty_measure_with_unit;
import jsdai.SRepresentation_schema.*;

public class CxCurve_style_parameters_with_ends_armx extends CCurve_style_parameters_with_ends_armx implements EMappedXIMEntity
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

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}*/
	public boolean testContext_of_items2(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	public ERepresentation_context getContext_of_items2(ERepresentation type) throws SdaiException {
		a2 = get_instance(a2);
		return (ERepresentation_context)a2;
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
	// ENDOF Taken from Representations
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCurve_style_parameters_with_ends.definition);

		setMappingConstraints(context, this);

		// name_x
		setName_x(context, this);
      
		// corner_style
		setCorner_style(context, this);
		
      // curve_width
		setCurve_width(context, this);
		
      // end_extension
		setEnd_extension(context, this);
		
      // end_style
		setEnd_style(context, this);
		
      // width_uncertainty		
		setWidth_uncertainty(context, this);
		
		// clean ARM
		// name_x
		unsetName_x(null);
      
		// corner_style
		unsetCorner_style(null);
		
      // curve_width
		unsetCurve_width(null);
		
      // end_extension
		unsetEnd_extension(null);
		
      // end_style
		unsetEnd_style(null);
		
      // width_uncertainty		
		unsetWidth_uncertainty(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		// name_x
		unsetName_x(context, this);
      
		// corner_style
		unsetCorner_style(context, this);
		
      // curve_width
		unsetCurve_width(context, this);
		
      // end_extension
		unsetEnd_extension(context, this);
		
      // end_style
		unsetEnd_style(context, this);
		
      // width_uncertainty		
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
	public static void setMappingConstraints(SdaiContext context, ECurve_style_parameters_with_ends_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxCurve_style_parameters.setMappingConstraints(context, armEntity);
		// armEntity.setName(null, "curve style parameters with ends");
	}

	public static void unsetMappingConstraints(SdaiContext context, ECurve_style_parameters_with_ends_armx armEntity) throws SdaiException
	{
		CxCurve_style_parameters.unsetMappingConstraints(context, armEntity);
		// armEntity.unsetName(null);
	}
	
   //********** "curve_style" attributes

   /**
    * Sets/creates data for name attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setName_x(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
      if (armEntity.testName_x(null)) {

         java.lang.String armName = armEntity.getName_x(null);
         //unset old values
         unsetName_x(context, armEntity);
         jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = null;
         jsdai.SQualified_measure_schema.EDescriptive_representation_item neededDri = null;

         if (armEntity.testItems2(null)) {
            jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
               ARepresentation_item();
            items = armEntity.getItems2(null);
            for (int i = 1; i <= items.getMemberCount(); i++) {
               dri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item) items.getByIndex(i);
               if (dri.testName(null) && dri.getName(null).equals("curve style name")) {
                  neededDri = dri;
                  break;
               }
            }
            if (neededDri == null) {
               neededDri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item)
                  context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.
                                                             CDescriptive_representation_item.class);
               neededDri.setName(null, "curve style name");
               items.addUnordered(neededDri);
            }

         }
         else {
            neededDri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item)
               context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.
                                                          CDescriptive_representation_item.class);
            neededDri.setName(null, "curve style name");
            armEntity.createItems(null).addUnordered(neededDri);
         }
         neededDri.setDescription(null, armName);

      }
   }

 /**
  * Unsets/deletes data for name attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetName_x(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = null;

       if (armEntity.testItems2(null)) {
          jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
             ARepresentation_item();
          items = armEntity.getItems2(null);
          for (int i = 1; i <= items.getMemberCount(); i++) {
             dri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item) items.getByIndex(i);
             if (dri.testName(null) && dri.getName(null).equals("curve style name")) {
                items.removeUnordered(dri);
             }
          }
       }
 }

 /**
  * Sets/creates data for corner_style attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setCorner_style(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testCorner_style(null)) {

       //unset old values
       unsetCorner_style(context, armEntity);
       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = (jsdai.SQualified_measure_schema.
                                                                               EDescriptive_representation_item)
          context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.CDescriptive_representation_item.definition);

       int result = armEntity.getCorner_style(null);
       if (result == EExtend_or_chord_2_extend_or_truncate_or_round.TRUNCATE) {
          dri.setDescription(null, "truncate");
       }
       else if (result == EExtend_or_chord_2_extend_or_truncate_or_round.CHORD_2_EXTEND) {
          dri.setDescription(null, "chord 2 extend");
       }
       else if (result == EExtend_or_chord_2_extend_or_truncate_or_round.ROUND) {
          dri.setDescription(null, "round");
       }
       else if (result == EExtend_or_chord_2_extend_or_truncate_or_round.EXTEND) {
          dri.setDescription(null, "extend");

       }
       dri.setName(null, "corner style");
       // R -> DRI
       jsdai.SRepresentation_schema.ARepresentation_item items;
       if (armEntity.testItems2(null)) {
          items = armEntity.getItems2(null);
       }
       else {
          items = armEntity.createItems(null);
       }
       items.addUnordered(dri);
    }
 }

 /**
  * Unsets/deletes data for corner_style attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetCorner_style(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testCorner_style(null)) {

       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = null;
       String desc = null;
       if (armEntity.testItems2(null)) {
          jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
             ARepresentation_item();
          items = armEntity.getItems2(null);
          for (int i = 1; i <= items.getMemberCount(); i++) {
             dri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item) items.getByIndex(i);
             if (dri.testDescription(null)) {
                desc = dri.getDescription(null);
                if (desc.equals("corner style") || desc.equals("chord 2 extend") || desc.equals("extend") ||
                    desc.equals("round") || desc.equals("truncate")) {
                   items.removeUnordered(dri);
                }
             }
          }
       }
    }
 }

 /**
  * Sets/creates data for curve_width attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setCurve_width(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    //unset old values
    unsetCurve_width(context, armEntity);

    if (armEntity.testCurve_width(null)) {

       jsdai.SMeasure_schema.ELength_measure_with_unit armLdea = armEntity.
			    getCurve_width(null);
//EA         armLdea.createAimData(context);
       CLength_measure_with_unit$measure_representation_item lmwu =
          (CLength_measure_with_unit$measure_representation_item)
          context.working_model.substituteInstance(armLdea, CLength_measure_with_unit$measure_representation_item.class);
       lmwu.setName(null, "curve width");
//EA         armLdea.setAimInstance(lmwu);
       if (armEntity.testItems2(null)) {
          AEntity items = armEntity.getItems2(null);
          if (!items.isMember(lmwu)) {
             items.addUnordered(lmwu);
          }
       }
       else {
          armEntity.createItems(null).addUnordered(lmwu);
       }
       // FILL AIM gap
       /* No longer needed as agreed in issue #2237 in SEDSZILLA
       ERepresentation_context erc = armEntity.getContext_of_items(null);
       CGlobal_uncertainty_assigned_context$parametric_representation_context
       	aimContext;
       if(erc instanceof CGlobal_uncertainty_assigned_context$parametric_representation_context)
       	aimContext = (CGlobal_uncertainty_assigned_context$parametric_representation_context)
				erc;
       else{
       	aimContext = (CGlobal_uncertainty_assigned_context$parametric_representation_context)
				context.working_model.substituteInstance(erc, 
						CGlobal_uncertainty_assigned_context$parametric_representation_context.definition);
       }
          
       if (!aimContext.testUnits(null)) {
          aimContext.createUnits(null);
       }
       AUnit lengthUnits = aimContext.getUnits(null);
       if (lengthUnits.getMemberCount() == 0) {
          lengthUnits.addUnordered(lmwu.getUnit_component(null));
       }*/

    }
 }

 /**
  * Unsets/deletes data for curve_width attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetCurve_width(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testCurve_width(null)) {

       jsdai.SRepresentation_schema.ERepresentation_item item = null;
       //String desc = null;
       if (armEntity.testItems2(null)) {
          jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
             ARepresentation_item();
          items = armEntity.getItems2(null);
          for (int i = 1; i <= items.getMemberCount(); i++) {
             item = items.getByIndex(i);
             if (item instanceof CLength_measure_with_unit$measure_representation_item && item.testName(null) &&
                 item.getName(null).equals("curve width")) {
                items.removeUnordered(item);
             }
          }
       }
    }
 }

 /**
  * Sets/creates data for end_extension attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setEnd_extension(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testEnd_extension(null)) {

       //int armEnd_extension = (int) armEntity.getEnd_extension(null);
       //unset old values
       unsetEnd_extension(context, armEntity);
       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = (jsdai.SQualified_measure_schema.
                                                                               EDescriptive_representation_item)
          context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.CDescriptive_representation_item.class);

       int result = armEntity.getEnd_extension(null);
       if (result == EExtend_or_truncate.TRUNCATE) {
          dri.setDescription(null, "truncate");
       }
       else if (result == EExtend_or_truncate.EXTEND) {
          dri.setDescription(null, "extend");
       }
       dri.setName(null, "end extension");
       // R -> DRI
       jsdai.SRepresentation_schema.ARepresentation_item items;
       if (armEntity.testItems2(null)) {
          items = armEntity.getItems2(null);
       }
       else {
          items = armEntity.createItems(null);
       }
       items.addUnordered(dri);

    }
 }

 /**
  * Unsets/deletes data for end_extension attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetEnd_extension(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testEnd_extension(null)) {

       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = null;
       EEntity tempItem = null;
       String desc = null;
       if (armEntity.testItems2(null)) {
          jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
             ARepresentation_item();
          items = armEntity.getItems2(null);
          for (int i = 1; i <= items.getMemberCount(); i++) {
             tempItem = items.getByIndex(i);
             if (tempItem instanceof jsdai.SQualified_measure_schema.EDescriptive_representation_item) {
                dri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item) tempItem;
                if (dri.testDescription(null)) {
                   if (!dri.testName(null)) {
                      continue;
                   }
                   desc = dri.getDescription(null);
                   if (dri.getName(null).equals("end extension") && (desc.equals("extend") || desc.equals("truncate"))) {
                      items.removeUnordered(dri);
                   }
                }
             }
          }
       }
    }
 }

 /**
  * Sets/creates data for end_style attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setEnd_style(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    //unset old values
    unsetEnd_style(context, armEntity);

    if (armEntity.testEnd_style(null)) {

       //int armEnd_style = (int) armEntity.getEnd_style(null);
       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = (jsdai.SQualified_measure_schema.
                                                                               EDescriptive_representation_item)
          context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.CDescriptive_representation_item.definition);

       int result = armEntity.getEnd_style(null);
       if (result == ESquare_or_round.ROUND) {
          dri.setDescription(null, "round");
       }
       else if (result == ESquare_or_round.SQUARE) {
          dri.setDescription(null, "square");
       }
       dri.setName(null, "end style");
       // R -> DRI
       jsdai.SRepresentation_schema.ARepresentation_item items;
       if (armEntity.testItems2(null)) {
          items = armEntity.getItems2(null);
       }
       else {
          items = armEntity.createItems(null);
       }
       items.addUnordered(dri);
    }
 }

 /**
  * Unsets/deletes data for end_style attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetEnd_style(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    if (armEntity.testEnd_style(null)) {

       jsdai.SQualified_measure_schema.EDescriptive_representation_item dri = null;
       EEntity tempItem = null;
       String desc = null;
       if (armEntity.testItems2(null)) {
          jsdai.SRepresentation_schema.ARepresentation_item items = new jsdai.SRepresentation_schema.
             ARepresentation_item();
          items = armEntity.getItems2(null);
          for (int i = 1; i <= items.getMemberCount(); i++) {
             tempItem = items.getByIndex(i);
             if (tempItem instanceof jsdai.SQualified_measure_schema.EDescriptive_representation_item) {
                dri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item) tempItem;
                if (dri.testDescription(null)) {
                   if (!dri.testName(null)) {
                      continue;
                   }
                   desc = dri.getDescription(null);
                   if (dri.getName(null).equals("end style") && (desc.equals("round") || desc.equals("square"))) {
                      items.removeUnordered(dri);
                   }
                }
             }
          }
       }
    }
 }

 // SINCE WD34
 /**
  * Sets/creates data for width_uncertainty attribute.
  attribute_mapping width_uncertainty_length_data_element (width_uncertainty
   , (*PATH*), length_data_element);
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
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setWidth_uncertainty(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    //unset old values
    unsetWidth_uncertainty(context, armEntity);

    if (armEntity.testWidth_uncertainty(null)) {
       ELength_measure_with_unit elde = armEntity.getWidth_uncertainty(null);
//EA         elde.createAimData(context);
       if(!(elde instanceof EUncertainty_measure_with_unit)){
          elde = (ELength_measure_with_unit)
             context.working_model.substituteInstance(elde, CLength_measure_with_unit$uncertainty_measure_with_unit.definition);

			 ((EUncertainty_measure_with_unit)elde).setName(null, "");
//EA            elde.setAimInstance(elmwu);
       }
       // 1) Get context
       CGlobal_uncertainty_assigned_context$parametric_representation_context aimContext;
       ERepresentation_context erc = armEntity.getContext_of_items2(null);
       if(erc instanceof CGlobal_uncertainty_assigned_context$parametric_representation_context){
       	aimContext = (CGlobal_uncertainty_assigned_context$parametric_representation_context)
          erc;
       }else{
       	// if reference is invalid
       	if((erc == null)||(erc.findEntityInstanceSdaiModel() == null)){
            LangUtils.Attribute_and_value_structure[] structure = {
                  new LangUtils.Attribute_and_value_structure(
                  CRepresentation_context.attributeContext_type(null), "curve style parametric context")};
            aimContext = (CGlobal_uncertainty_assigned_context$parametric_representation_context)
                 LangUtils.createInstanceIfNeeded(context, CGlobal_uncertainty_assigned_context$parametric_representation_context.definition, structure);
            if (!aimContext.testContext_identifier(null)) {
            	aimContext.setContext_identifier(null, "");
            }
            armEntity.setContext_of_items(null, aimContext);   
       	}
       	else{
       		aimContext = (CGlobal_uncertainty_assigned_context$parametric_representation_context) 
       			context.working_model.substituteInstance(erc, CGlobal_uncertainty_assigned_context$parametric_representation_context.definition);
       		// We have to set it, as it is special one and is not updated after substitution
       		armEntity.setContext_of_items(null, aimContext);
       	}
       }
       // 2) Uncertainty
       if (!aimContext.testUncertainty(null)) {
          aimContext.createUncertainty(null);
       }
       AUncertainty_measure_with_unit accuraciesHolder = aimContext.
          getUncertainty(null);
       if (!accuraciesHolder.isMember(elde)) {
          accuraciesHolder.addUnordered(elde);
       }
    }
 }

 /**
  * Unsets/deletes data for width_uncertainty attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetWidth_uncertainty(SdaiContext context, CxCurve_style_parameters_with_ends_armx armEntity) throws SdaiException {
    // 1) Get context
    if (!armEntity.testContext_of_items2(null)) {
       return;
    }
    if(!(armEntity.getContext_of_items2(null) instanceof CGlobal_uncertainty_assigned_context$parametric_representation_context))
    	return;
    CGlobal_uncertainty_assigned_context$parametric_representation_context
       aimContext = (
       CGlobal_uncertainty_assigned_context$parametric_representation_context)
       armEntity.getContext_of_items2(null);
    if(aimContext == null){
    	return;
    }
    // 2) Uncertainty
    if (!aimContext.testUncertainty(null)) {
       return;
    }
    AUncertainty_measure_with_unit accuraciesHolder = aimContext.
       getUncertainty(null);
    accuraciesHolder.clear();
 }	
	
}
