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

package jsdai.SNetwork_functional_design_view_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SModel_parameter_xim.*;
import jsdai.SNetwork_functional_design_view_mim.CComponent_functional_unit;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.ERepresentation;

public class CxFunctional_unit_armx
		extends
			CFunctional_unit_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  
	
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
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
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
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a2$;
	}

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
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CComponent_functional_unit.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			
			setFunctional_property(context, this);
			
			// Clean ARM specific attributes
			unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
			
			unsetFunctional_property(null);			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			unsetAdditional_characterization(context, this);

			unsetDerived_from(context, this);
			
			unsetFunctional_property(context, this);			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 *  component_functional_unit &lt;= 
	 *  product_definition
	 *  {product_definition
	 *  product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'functional occurrence'}
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
			EFunctional_unit_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxDefinition_based_product_occurrence.setMappingConstraints(context, armEntity);
		// Removed constraint according SEDS-1491, comment #39
		// CxAP210ARMUtilities.assignPart_definition_type(context, armEntity, "functional occurrence");		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EFunctional_unit_armx armEntity) throws SdaiException {
		CxDefinition_based_product_occurrence.unsetMappingConstraints(context, armEntity);
		// CxAP210ARMUtilities.deassignPart_definition_type(context, armEntity);		
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

	/**
	 * Sets/creates data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);		
	}

	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);		
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

	/**
	* Sets/creates data for functional_property attribute.
	* @param context SdaiContext.
	* @param armEntity arm entity.
	*
	*  attribute_mapping functional_property_characteristic (functional_property
	* , (*PATH*), characteristic);
	* 	component_functional_unit &lt;=
	*  product_definition
	*  characterized_product_definition = product_definition
	*  characterized_product_definition
	*  characterized_definition = characterized_product_definition
	*  characterized_definition &lt;-
	*  property_definition.definition
	*  property_definition &lt;-
	*  property_definition_representation.definition
	*  property_definition_representation
	*  property_definition_representation.used_representation -&gt;
	*  representation =&gt;
	*  parameter_assignment_override
	* end_attribute_mapping;
	*
	*/
	// PD <- PD <- PDR -> R
	public static void setFunctional_property(SdaiContext context, EFunctional_unit_armx armEntity) throws SdaiException {
		if (armEntity.testFunctional_property(null)) {
			// PD <- PD
			LangUtils.Attribute_and_value_structure[] pdcrStructure = new LangUtils.Attribute_and_value_structure[]{new LangUtils.Attribute_and_value_structure(
					CProperty_definition.attributeDefinition(null),
					armEntity),};

			EProperty_definition epd = (EProperty_definition) LangUtils
					.createInstanceIfNeeded(context, CProperty_definition.definition,
							pdcrStructure);
			ANfdv_pa_or_parameter_assignment_override properties = armEntity.getFunctional_property(null);
			SdaiIterator iter = properties.createIterator();
			while(iter.next()){
				ERepresentation property = (ERepresentation)properties.getCurrentMember(iter); 
				EProperty_definition_representation epdr = (EProperty_definition_representation)
					context.working_model.createEntityInstance(CProperty_definition_representation.definition);
				epdr.setDefinition(null, epd);
				epdr.setUsed_representation(null, property);
			}
		}
	}
	
	public static void unsetFunctional_property(SdaiContext context, EFunctional_unit_armx armEntity) throws SdaiException {
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		if(apd.getMemberCount() == 0)
			return;
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			SdaiIterator iterPDR = apdr.createIterator();
			while(iterPDR.next()){
				EProperty_definition_representation epdr = apdr.getCurrentMember(iterPDR);
				if(epdr.getUsed_representation(null) instanceof EParameter_assignment_override_armx)
					epdr.deleteApplicationInstance();
			}
		}
	}
	
	
}