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

package jsdai.SLayered_interconnect_module_design_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_xim.EPath_area_with_parameters_armx;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SGeometry_schema.EGeometric_representation_context;
import jsdai.SLayered_interconnect_module_design_mim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.CShape_representation;
import jsdai.SProduct_property_representation_schema.EItem_identified_representation_usage;
import jsdai.SProduct_property_representation_schema.EShape_representation;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxConductive_interconnect_element_with_pre_defined_transitions_armx
		extends
			CConductive_interconnect_element_with_pre_defined_transitions_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from IIRU
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EItem_identified_representation_usage type) throws SdaiException {
		return test_string(a17);
	}
	public String getName(EItem_identified_representation_usage type) throws SdaiException {
		return get_string(a17);
	}*/
	public void setName(EItem_identified_representation_usage type, String value) throws SdaiException {
		a17 = set_string(value);
	}
	public void unsetName(EItem_identified_representation_usage type) throws SdaiException {
		a17 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EItem_identified_representation_usage type) throws SdaiException {
		return a17$;
	}
	
	// attribute (current explicit or supertype explicit) : used_representation, base type: entity representation
/*	public static int usedinUsed_representation(EItem_identified_representation_usage type, jsdai.SRepresentation_schema.ERepresentation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a20$, domain, result);
	}
	public boolean testUsed_representation(EItem_identified_representation_usage type) throws SdaiException {
		return test_instance(a20);
	}*/
	public jsdai.SRepresentation_schema.ERepresentation getUsed_representation(EItem_identified_representation_usage type) throws SdaiException {
		return (jsdai.SRepresentation_schema.ERepresentation)get_instance(a20);
	}
	public void setUsed_representation(EItem_identified_representation_usage type, jsdai.SRepresentation_schema.ERepresentation value) throws SdaiException {
		a20 = set_instanceX(a20, value);
	}
	public void unsetUsed_representation(EItem_identified_representation_usage type) throws SdaiException {
		a20 = unset_instance(a20);
	}
	public static jsdai.dictionary.EAttribute attributeUsed_representation(EItem_identified_representation_usage type) throws SdaiException {
		return a20$;
	}
	
	// ENDOF from IIRU
	
	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a10 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a10 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a10$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a13$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a13);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a13);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a13 = set_instanceX(a13, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a13 = unset_instance(a13);
	}
/*	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a13$;
	}*/
	// ENDOF taken from PDR

	// Product_view_definition
	
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
/*	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}*/

	// methods for derived attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getName(EProduct_definition type, SdaiContext _context) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public String getName(EProduct_definition type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition type) throws SdaiException {
		return d0$;
	}*/

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_formation)a2;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}
/*	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a2$;
	}*/

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = get_instance(a3);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a3 = set_instanceX(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
/*	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}*/
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a7);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a7 = unset_string();
	}
/*	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a7$;
	}
	// -2- methods for SELECT attribute: definition
	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a9);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a9 = get_instance_select(a9);
		return (EEntity)a9;
	}*/

	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a9 = set_instanceX(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

/*	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}*/

	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CConductive_interconnect_element_with_pre_defined_transitions.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
//			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization - this is derived in laminate_component
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			
			
			

			setDerived_from(context, this);
			
			// composed_conductor 
			setImplementation_or_resident_stratum(context, this);
			
			// centreline_shape 
			// setCentreline_shape(context, this);
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
//			unsetId_x(null);
			unsetImplementation_or_resident_stratum(null);

			// centreline_shape 
			// unsetCentreline_shape(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Kind of AIM gap
			// unsetDefinition(null);

			//********** "design_discipline_item_definition" attributes

			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is derived in laminate_component
			// unsetAdditional_characterization(context, this);

			unsetDerived_from(context, this);
			
//			unsetId_x(context, this);
			
			// composed_conductor 
			unsetImplementation_or_resident_stratum(context, this);

			// centreline_shape 
			// unsetCentreline_shape(context, this);
			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  (product_definition.description = 'conductive interconnect element with pre defined transitions')
	 *  (product_definition.description = 'join 2 physical connectivity definition supporting')
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// Do nothing as it is abstract
	public static void setMappingConstraints(SdaiContext context,
			EConductive_interconnect_element_with_pre_defined_transitions_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxConductive_interconnect_element_armx.setMappingConstraints(context, armEntity);
		armEntity.setName((EItem_identified_representation_usage)null, "");
		if(armEntity.testCentreline_shape(null)){
			EPath_area_with_parameters_armx curve = armEntity.getCentreline_shape(null);
			ARepresentation ar = new ARepresentation();
			CRepresentation.usedinItems(null, curve, context.domain, ar);
			ERepresentation er = null;
			if(ar.getMemberCount() == 1){
				er = ar.getByIndex(1);
			}else if(ar.getMemberCount() == 0){
				EGeometric_representation_context egrc = CxAP210ARMUtilities.createGeometric_representation_context(context, "", "", 2, false);
				er = (EShape_representation) context.working_model.createEntityInstance(CShape_representation.definition);
				er.setName(null, "");
				er.setContext_of_items(null, egrc);
				er.createItems(null).addUnordered(curve);
			}else{
				SdaiSession.getSession().printlnSession(" Reps for curve in CIEWPDT > 1 "+ar.getMemberCount());				
			}
			if(er != null){
				armEntity.setUsed_representation(null, er);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EConductive_interconnect_element_with_pre_defined_transitions_armx armEntity) throws SdaiException {
		CxConductive_interconnect_element_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName((EItem_identified_representation_usage)null);
	}

	//********** "design_discipline_item_definition" attributes

	/**
	 * Sets/creates data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_contexts(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for additional_context attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_contexts(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);		
	}
// Product_occurrence attributes	
	
	/**
	 * Sets/creates data for derived_from attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PDU -> PD
	public static void setDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.setDerived_from(context, armEntity);
	}

	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.unsetDerived_from(context, armEntity);		
	}
	
///// Item_shape
	/**
	 * Sets/creates data for id_x attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
	}
*/
	/**
	 * Unsets/deletes data for Quantity_criterion attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);		
	}
*/
   //********** "conductive_interconnect_element" attributes

   /**
    * Sets/creates data for composed_conductor attribute.
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setImplementation_or_resident_stratum(SdaiContext context, EConductive_interconnect_element_armx armEntity) throws
      SdaiException {
		CxStratum_feature_template_component_armx.setImplementation_or_resident_stratum(context, armEntity);
   }

  /**
   * Unsets/deletes data for composed_conductor attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetImplementation_or_resident_stratum(SdaiContext context, EConductive_interconnect_element_armx armEntity) throws
     SdaiException {
	  CxStratum_feature_template_component_armx.unsetImplementation_or_resident_stratum(context, armEntity);  	
  }

  //********** "conductive_interconnect_element_with_pre_defined_transitions" attributes

  /**
   * Sets/creates data for centreline_shape attribute.
   *
   *  attribute_mapping centreline_shape(centreline_shape, $PATH, Planar_path_shape_model_with_parameters);
conductive_interconnect_element_with_pre_defined_transitions <=
conductive_interconnect_element <=
stratum_feature_template_component <=
laminate_component <=
assembly_component <=
component_definition <=
product_definition
characterized_product_definition = product_definition
characterized_product_definition
characterized_definition = characterized_product_definition
characterized_definition <-
property_definition.definition
property_definition
{property_definition
property_definition.name = 'centreline shape'}
property_definition <-
property_definition_representation.definition
property_definition_representation
property_definition_representation.used_representation ->
representation =>
shape_representation =>
csg_2d_shape_representation =>
single_area_csg_2d_shape_representation

end_attribute_mapping;

   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  // CIEWPDT <- PD <- PDR -> SAC2SR
  /* Became redeclared no need to do anything  
  public static void setCentreline_shape(SdaiContext context,
                                         EConductive_interconnect_element_with_pre_defined_transitions_armx armEntity) throws
     SdaiException {
     if (armEntity.testCentreline_shape(null)) {
			// CURVE stuff
        EPlanar_path_shape_model_with_parameters armCentreline_shape =  
           armCentreline_shape = armEntity.getCentreline_shape(null);
        // EPD
        EProperty_definition epd = (EProperty_definition)
        	context.working_model.createEntityInstance(CProperty_definition.definition);
        epd.setDefinition(null, armEntity);
        epd.setName(null, "centreline shape");
        // PDR
        EProperty_definition_representation epdr = (EProperty_definition_representation)
    		context.working_model.createEntityInstance(CProperty_definition_representation.definition);
        epdr.setDefinition(null, epd);
        epdr.setUsed_representation(null, armCentreline_shape);
     }
  }
*/
 /**
  * Unsets/deletes data for centreline_shape attribute.
  *
  * 	component_shape_aspect <=
  * 	shape_aspect
  * 	shape_definition = shape_aspect
  * 	shape_definition
  * 	characterized_definition = shape_definition
  * 	characterized_definition <-
  * 	property_definition.definition
  * 	property_definition
  * 	{property_definition
  * 	property_definition.name = `centreline shape'}
  * 	property_definition <-
  * 	property_definition_representation.definition
  * 	property_definition_representation
  * 	property_definition_representation.used_representation ->
  * 	{representation =>
  * 	shape_representation}
  * 	representation
  * 	representation.items[i] ->
  * 	representation_item
  * 	{representation_item
  * 	representation_item.name = `centreline'}
  * 	representation_item =>
  * 	geometric_representation_item =>
  * 	curve
  *  end_attribute_mapping;
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
  /* Became redeclared no need to do anything  
 public static void unsetCentreline_shape(SdaiContext context,
                                          EConductive_interconnect_element_with_pre_defined_transitions_armx armEntity) throws
    SdaiException {

    AEntity results = new AEntity();
    CProperty_definition.usedinDefinition(null, armEntity, context.domain, results);
    EProperty_definition property_definition = null;
    String name = null;
    final String KEYWORD1 = "centreline shape";
    for (int i = 1; i <= results.getMemberCount(); i++) {
       property_definition = (jsdai.SProduct_property_definition_schema.EProperty_definition)
          results.getByIndexEntity(i);

       if (property_definition.testName(null)) {
          if (!name.equals(KEYWORD1)) {
             property_definition = null;
             continue;
          }
          else {
             AProperty_definition_representation results2 = new AProperty_definition_representation();
             EProperty_definition_representation property_definition_representation;
             CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, results2);
             for (int j = 1; j <= results2.getMemberCount();j++) {
                property_definition_representation = (EProperty_definition_representation)
                   results2.getByIndexEntity(i);
                property_definition_representation.deleteApplicationInstance();
             }
          }
          property_definition.deleteApplicationInstance();
       }
    }
 }
*/
}