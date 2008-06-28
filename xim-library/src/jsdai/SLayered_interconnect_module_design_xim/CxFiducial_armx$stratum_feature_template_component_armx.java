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
import jsdai.util.LangUtils;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SLayered_interconnect_module_design_mim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxFiducial_armx$stratum_feature_template_component_armx
		extends
			CFiducial_armx$stratum_feature_template_component_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  
	// Taken from PDR
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a9);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a9);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a9 = set_instance(a9, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a9 = unset_instance(a9);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a9$;
	}
	// ENDOF taken from PDR
	
	// Product_view_definition

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a3);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a3$;
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
		return d7$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a4);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a4 = get_instance(a4);
		return (EProduct_definition_formation)a4;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a4 = set_instance(a4, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a4 = unset_instance(a4);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a4$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a5$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a5);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a5 = get_instance(a5);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a5;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a5 = set_instance(a5, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a5 = unset_instance(a5);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a5$;
	}
	
	// From CProperty_definition.java
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProperty_definition type) throws SdaiException {
		return test_string(a13);
	}
	public String getName(EProperty_definition type) throws SdaiException {
		return get_string(a13);
	}*/
	public void setName(EProperty_definition type, String value) throws SdaiException {
		a13 = set_string(value);
	}
	public void unsetName(EProperty_definition type) throws SdaiException {
		a13 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a13$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a15$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a15);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a15 = get_instance_select(a15);
		return (EEntity)a15;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a15 = set_instance(a15, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a15 = unset_instance(a15);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a15$;
	}
	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CFiducial$stratum_feature_template_component.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
			//Id
			setId_x(context, this);
			
			//id - goes directly into AIM
			
			//additional_characterization - this is derived in laminate_component
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			
			
			

			setDerived_from(context, this);
			
			// stratum_feature_implementation
			setImplementation_or_resident_stratum(context, this);
			
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
			unsetId_x(null);

			// stratum_feature_implementation
			unsetImplementation_or_resident_stratum(null);
			
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
			
			unsetId_x(context, this);			

			// stratum_feature_implementation
			unsetImplementation_or_resident_stratum(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  (assembly_component &lt;=
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.description = 'stratum feature template component']
	 *  [product_definition
	 *  product_definition.name = 'laminate component']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'layout occurrence']})
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
			CFiducial_armx$stratum_feature_template_component_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxFiducial_armx.setMappingConstraints(context, armEntity);
		CxStratum_feature_template_component_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			CFiducial_armx$stratum_feature_template_component_armx armEntity) throws SdaiException {
		CxFiducial_armx.unsetMappingConstraints(context, armEntity);
		CxStratum_feature_template_component_armx.unsetMappingConstraints(context, armEntity);
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
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.setId_x(context, armEntity);
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
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);		
	}

	//********** "special_symbol_laminate_component" attributes

	/**
	* Sets/creates data for stratum_feature_implementation attribute.
	*
	* <p>
	*  attribute_mapping stratum_feature_implementation_stratum_feature (stratum_feature_implementation
	* , (*PATH*), stratum_feature);
	* 	assembly_component &lt;=
	*  product_definition_shape &lt;-
	*  shape_aspect.of_shape
	*  shape_aspect &lt;-
	*  shape_aspect_relationship.related_shape_aspect
	*  shape_aspect_relationship
	*  {shape_aspect_relationship
	*  shape_aspect_relationship.name = 'stratum feature implementation'}
	*  shape_aspect_relationship.relating_shape_aspect -&gt;
	*  shape_aspect =&gt;
	*  stratum_feature
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetImplementation_or_resident_stratum(context, armEntity);

		if (armEntity.testImplementation_or_resident_stratum(null)){
			EEntity armAttribute = armEntity.getImplementation_or_resident_stratum(null);
			if(armAttribute instanceof EStratum_feature_armx){
				// SA
				LangUtils.Attribute_and_value_structure[] saStructure =
	            {new LangUtils.Attribute_and_value_structure(
	            		CShape_aspect.attributeOf_shape(null),
							armEntity)};
				EShape_aspect
	            	esa = (EShape_aspect)
	            LangUtils.createInstanceIfNeeded(context,
	                                             CShape_aspect.definition,
	                                             saStructure);
				if(!esa.testProduct_definitional(null))
					esa.setProduct_definitional(null, ELogical.UNKNOWN);
				if(!esa.testName(null))
					esa.setName(null, "");
				
				//shape_aspect_relationship
				EShape_aspect_relationship shape_aspect_relationship = (EShape_aspect_relationship) 
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
					shape_aspect_relationship.setName(null, "implementation");
					shape_aspect_relationship.setDescription(null, "");
					shape_aspect_relationship.setRelating_shape_aspect(null, (EShape_aspect)armAttribute);
					shape_aspect_relationship.setRelated_shape_aspect(null, esa);
			}else{
				//pdr
				EProduct_definition_relationship pdr = (EProduct_definition_relationship) 
					context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
				pdr.setName(null, "resident stratum");
				pdr.setDescription(null, "");
				pdr.setRelating_product_definition(null, (EStratum_armx)armAttribute);
				pdr.setRelated_product_definition(null, armEntity);
			}
		}
	}


	/**
	* Unsets/deletes data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplementation_or_resident_stratum(SdaiContext context, EStratum_feature_template_component_armx armEntity) throws SdaiException
	{
		EShape_aspect esa = null;
		AShape_aspect asa = new AShape_aspect();
		CShape_aspect.usedinOf_shape(null, armEntity, context.domain, asa);

		for (int j = 1; j <= asa.getMemberCount(); j++) {
			esa = asa.getByIndex(j);

			EShape_aspect_relationship relationship = null;
			AShape_aspect_relationship aRelationship = new AShape_aspect_relationship();
			CShape_aspect_relationship.usedinRelated_shape_aspect(null, esa, context.domain, aRelationship);
	
			for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
				relationship = aRelationship.getByIndex(i);
				if (relationship.testName(null) && relationship.getName(null).equals("implementation")) {
					relationship.deleteApplicationInstance();
				}
			}
		}

		EProduct_definition_relationship relationship = null;
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("resident stratum")) {
				relationship.deleteApplicationInstance();
			}
		}
		
	}

}