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
import jsdai.SLayered_interconnect_module_design_mim.CDielectric_material_passage;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRequirement_decomposition_xim.EPredefined_requirement_view_definition_armx;
import jsdai.SShape_property_assignment_xim.*;

public class CxDielectric_material_passage_armx
		extends
			CDielectric_material_passage_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a11 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a11 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a11$;
	}

	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a12 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a12 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a12$;
	}
	
	// attribute (current explicit or supertype explicit) : relating_product_definition, base type: entity product_definition
/*	public static int usedinRelating_product_definition(EProduct_definition_relationship type, EProduct_definition instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a14$, domain, result);
	}
	public boolean testRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return test_instance(a14);
	}
	public EProduct_definition getRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return (EProduct_definition)get_instance(a14);
	}*/
	public void setRelating_product_definition(EProduct_definition_relationship type, EProduct_definition value) throws SdaiException {
		a14 = set_instance(a14, value);
	}
	public void unsetRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		a14 = unset_instance(a14);
	}
	public static jsdai.dictionary.EAttribute attributeRelating_product_definition(EProduct_definition_relationship type) throws SdaiException {
		return a14$;
	}
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
		a2 = set_instance(a2, value);
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
		a3 = set_instance(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	
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
	public static jsdai.dictionary.EAttribute attributeName(EProperty_definition type) throws SdaiException {
		return a7$;
	}
	// -2- methods for SELECT attribute: definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a9$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a9);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a9 = get_instance_select(a9);
		return (EEntity)a9;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a9 = set_instance(a9, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a9 = unset_instance(a9);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a9$;
	}
	
	// ENDOF From CProperty_definition.java
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDielectric_material_passage.definition);

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
			
			// feature_of_size
			setFeature_of_size(context, this);
			
			// feature_material
			setFeature_material(context, this);
			
			// precedent_passage 			
			setPrecedent_passage(context, this);
			
			setVertical_extent(context, this);
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
			unsetId_x(null);
			unsetFeature_of_size(null);

			// feature_material
			unsetFeature_material(null);
			
			// precedent_passage 			
			unsetPrecedent_passage(null);
			unsetVertical_extent(null);			
			
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
			
			// feature_of_size
			unsetFeature_of_size(context, this);
			
			// feature_material
			unsetFeature_material(context, this);
			
			// precedent_passage 			
			unsetPrecedent_passage(context, this);
			
			unsetVertical_extent(context, this);			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
	 *  inter_stratum_feature &lt;=
	 *  assembly_component &lt;=			
	 *  component_definition &lt;=
	 *  product_definition
	 *  {product_definition
	 *  product_definition.description = 'dielectric material passage'} 
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
			EDielectric_material_passage_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxInter_stratum_feature_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EDielectric_material_passage_armx armEntity) throws SdaiException {
		CxInter_stratum_feature_armx.unsetMappingConstraints(context, armEntity);
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

	//********** "inter_stratum_feature" attributes

	/**
	* Sets/creates data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setFeature_of_size(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_feature_armx.setFeature_of_size(context, armEntity);		
	}


	/**
	* Unsets/deletes data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetFeature_of_size(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_feature_armx.unsetFeature_of_size(context, armEntity);
	}
	
	//********** "dielectric_material_passage" attributes

	/**
	* Sets/creates data for feature_material attribute.
	*
	* <p>
            <aa attribute="feature_material" assertion_to="Material_identification">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">inter_stratum_feature &lt;=
assembly_component &lt;=			
component_definition &lt;=
product_definition 
characterized_product_definition = product_definition
characterized_product_definition
characterized_definition = characterized_product_definition
characterized_definition &lt;-
material_designation.definitions[i]
material_designation
</refpath>
            </aa>
            <aa attribute="feature_material" assertion_to="Requirement_definition_property">
                <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">inter_stratum_feature &lt;=
assembly_component &lt;=			
component_definition &lt;=
product_definition 
characterized_product_definition = product_definition
characterized_product_definition
characterized_definition = characterized_product_definition
characterized_definition &lt;-
property_definition.definition
{property_definition
property_definition.name = 'feature material'}
property_definition &lt;- 
property_definition_relationship.related_property_definition
{property_definition_relationship
property_definition_relationship.name = 'feature material'}
property_definition_relationship.relating_property_definition -&gt;
property_definition =&gt;
requirements_property
</refpath>
            </aa>	
	* </p>
	* end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// AC <- MD    OR   AC <- PD <- PDR -> RP
	public static void setFeature_material(SdaiContext context, EDielectric_material_passage_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetFeature_material(context, armEntity);

		if (armEntity.testFeature_material(null))
		{

			EMappedARMEntity armFeature_material = (EMappedARMEntity) armEntity.getFeature_material(null);

			//EEe_requirement_occurrence
			if (armFeature_material instanceof EPredefined_requirement_view_definition_armx) {
				//property_definition
				EProperty_definition property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "feature material", null, armEntity, true);

				//property_definition_relationship
				EProperty_definition_relationship property_definition_relationship = (EProperty_definition_relationship) context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
				property_definition_relationship.setName(null, "feature material");
				property_definition_relationship.setDescription(null, "");
				property_definition_relationship.setRelated_property_definition(null, property_definition);
            // PD -> MRA
            LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(
               CProperty_definition.attributeDefinition(null), armFeature_material)};
            EProperty_definition epd = (EProperty_definition)
               LangUtils.createInstanceIfNeeded(context,
                                                CProperty_definition.definition,
                                                pdStructure);
            if (!epd.testName(null))
               epd.setName(null, "");
				
				property_definition_relationship.setRelating_property_definition(null, epd);

			//EEe_material
			} else if (armFeature_material instanceof EMaterial_designation) {
				EMaterial_designation aimFeature_material = (EMaterial_designation) armFeature_material;
				if (!aimFeature_material.testDefinitions(null)) {
					aimFeature_material.createDefinitions(null);
				}
				aimFeature_material.getDefinitions(null).addUnordered(armEntity);
			}
		}
	}


	/**
	* Unsets/deletes data for feature_material attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetFeature_material(SdaiContext context, EDielectric_material_passage_armx armEntity) throws SdaiException
	{
		//EEe_requirement_occurrence
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);
			if (property_definition.testName(null) && property_definition.getName(null).equals("feature material")) {

				EProperty_definition_relationship property_definition_relationship = null;
				AProperty_definition_relationship aRelationship = new AProperty_definition_relationship();
				CProperty_definition_relationship.usedinRelated_property_definition(null, property_definition, context.domain, aRelationship);
				for (int j = 1; j <= aRelationship.getMemberCount(); j++) {
					property_definition_relationship = aRelationship.getByIndex(j);
					if (property_definition_relationship.testName(null) && property_definition_relationship.getName(null).equals("feature material")) {
						property_definition_relationship.deleteApplicationInstance();
					}
				}
			}
		}

		//EEe_material
		EMaterial_designation material_designation = null;
		AMaterial_designation aMaterial_designation = new AMaterial_designation();
		CMaterial_designation.usedinDefinitions(null, armEntity, context.domain, aMaterial_designation);
		for (int i = 1; i <= aMaterial_designation.getMemberCount(); i++) {
			material_designation = aMaterial_designation.getByIndex(i);
			while (material_designation.getDefinitions(null).isMember(armEntity)) {
				material_designation.getDefinitions(null).removeUnordered(armEntity);
			}
		}
	}


	/**
	* Sets/creates data for precedent_passage attribute.
	*
	* <p>
	*  attribute_mapping precedent_passage_cutout (precedent_passage
	* , (*PATH*), cutout);
	* 	inter_stratum_feature &lt;=
	*  assembly_component &lt;=			
	*  component_definition &lt;=
	*  product_definition &lt;-
	*  product_definition_relationship.related_product_definition
	*  product_definition_relationship
	*  {product_definition_relationship
	*  product_definition_relationship.name = 'precedent passage'}
	*  product_definition_relationship.relating_product_definition -&gt;
	*  {product_definition
	*  (product_definition.description = 'cutout')
	*  (product_definition.description = 'partially plated cutout')
	*  (product_definition.description = 'plated cutout')
	*  (product_definition.description = 'physical connectivity interrupting cutout')}
	*  product_definition =&gt;
	*  component_definition =&gt;
	*  assembly_component =&gt;			
	*  inter_stratum_feature
	* end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setPrecedent_passage(SdaiContext context, EDielectric_material_passage_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetPrecedent_passage(context, armEntity);

		if (armEntity.testPrecedent_passage(null))
		{
			ECutout_armx armPrecedent_passage = armEntity.getPrecedent_passage(null);
		  //shape_aspect_relationship
			EProperty_definition_relationship property_definition_relationship = (EProperty_definition_relationship) context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
			property_definition_relationship.setName(null, "precedent passage");
			property_definition_relationship.setDescription(null, "");
			property_definition_relationship.setRelated_property_definition(null, armEntity);
			property_definition_relationship.setRelating_property_definition(null, 
					armPrecedent_passage);
		}
	}


	/**
	* Unsets/deletes data for precedent_passage attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetPrecedent_passage(SdaiContext context, EDielectric_material_passage_armx armEntity) throws SdaiException
	{
		EProperty_definition_relationship relationship = null;
		AProperty_definition_relationship aRelationship = new AProperty_definition_relationship();
		CProperty_definition_relationship.usedinRelated_property_definition(null, armEntity, context.domain, aRelationship);

		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);
			if (relationship.testName(null) && relationship.getName(null).equals("precedent passage")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	
	/**
	* Sets/creates data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setVertical_extent(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_feature_armx.setVertical_extent(context, armEntity);		
	}


	/**
	* Unsets/deletes data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetVertical_extent(SdaiContext context, EInter_stratum_feature_armx armEntity) throws SdaiException
	{
		CxInter_stratum_feature_armx.unsetVertical_extent(context, armEntity);
	}
	
}