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

package jsdai.SGeneric_product_occurrence_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SAssembly_structure_xim.AProduct_occurrence_definition_relationship_armx;
import jsdai.SAssembly_structure_xim.CNext_assembly_usage_occurrence_relationship_armx;
import jsdai.SAssembly_structure_xim.CProduct_occurrence_definition_relationship_armx;
import jsdai.SPhysical_unit_design_view_mim.ANext_assembly_usage_occurrence_relationship;
import jsdai.SPhysical_unit_design_view_mim.CNext_assembly_usage_occurrence_relationship;
import jsdai.SPhysical_unit_design_view_mim.EAssembly_component;
import jsdai.SPhysical_unit_design_view_mim.ENext_assembly_usage_occurrence_relationship;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_structure_schema.*;
import jsdai.SProduct_view_definition_xim.*;

public class CxDefinition_based_product_occurrence
		extends
			CDefinition_based_product_occurrence implements EMappedXIMEntity{

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

		setTemp("AIM", CProduct_definition.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setDerived_from(context, this);
			// Cleaning is done insude this method
			finalize_assembly_structure(context, this);
			
			
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
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
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
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxProduct_occurrence.setMappingConstraints(context, armEntity);
		if(armEntity.testDerived_from(null)){
			// Implement a derived attribute
			EProduct_view_definition epvd = armEntity.getDerived_from(null);
			if(epvd.testFormation(null)){
				armEntity.setFormation(null, epvd.getFormation(null));	
			}else{
				SdaiSession.getSession().printlnSession(" WARNING definition without version "+epvd);
			}
	      
		}else{
			SdaiSession.getSession().printlnSession(" WARNING component without definition "+armEntity);
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
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		CxProduct_occurrence.unsetMappingConstraints(context, armEntity);
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
// Removed attributes after re-structuring	
	/**
	 * Sets/creates data for Occurrence_quantity attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PD <- PDR -> R
/*	
	public static void setOccurrence_quantity(SdaiContext context,
			EProduct_occurrence armEntity) throws SdaiException {
		CxProduct_occurrence.setOccurrence_quantity(context, armEntity);		
	}
*/
	/**
	 * Unsets/deletes data for name attribute.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/*	
	public static void unsetOccurrence_quantity(SdaiContext context,
			EProduct_occurrence armEntity) throws SdaiException {
		CxProduct_occurrence.unsetOccurrence_quantity(context, armEntity);		
	}
*/
	/**
	 * Sets/creates data for Quantity_criterion attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PD <- PDR -> R
/*	
	public static void setQuantity_criterion(SdaiContext context,
			EProduct_occurrence armEntity) throws SdaiException {
		CxProduct_occurrence.setQuantity_criterion(context, armEntity);		
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
/*	
	public static void unsetQuantity_criterion(SdaiContext context,
			EProduct_occurrence armEntity) throws SdaiException {
		CxProduct_occurrence.unsetQuantity_criterion(context, armEntity);		
	}
*/
	/**
	 * Sets/creates data for derived_from attribute.
	 * 	<description>If the Definition_based_part_occurrence is mapped only onto assembly_component_usage.</description>
				<aimelt xml:space="preserve">IDENTICAL MAPPING</aimelt>
			</alt_map>
			<alt_map id="2">
				<description>If the Definition_based_part_occurrence is mapped only onto product_definition.</description>
				<aimelt xml:space="preserve">PATH</aimelt>
				<refpath xml:space="preserve">
			product_definition
			{product_definition.frame_of_reference -&gt; product_definition_context
			product_definition_context &lt;= application_context_element 
			application_context_element.name = 'part occurrence'}
			product_definition &lt;- product_definition_relationship.related_product_definition
			product_definition_relationship
			{product_definition_relationship.name = 'definition usage'}
			{product_definition_relationship =&gt; product_definition_usage}
			product_definition_relationship.relating_product_definition -&gt; product_definition
			{product_definition.frame_of_reference -&gt; product_definition_context
			product_definition_context &lt;= application_context_element 
			application_context_element.name = 'part definition'}
				
			product_definition
			{product_definition.frame_of_reference -&gt; product_definition_context
			product_definition_context &lt;= application_context_element 
			application_context_element.name = 'functional occurrence'}
			product_definition &lt;- product_definition_relationship.related_product_definition
			product_definition_relationship
			{product_definition_relationship.name = 'definition usage'}
			{product_definition_relationship =&gt; product_definition_usage}
			product_definition_relationship.relating_product_definition -&gt; product_definition
			{product_definition.frame_of_reference -&gt; product_definition_context
			product_definition_context &lt;= application_context_element 
			application_context_element.name = 'functional definition'}
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PDU -> PD
	public static void setDerived_from(SdaiContext context,
			EDefinition_based_product_occurrence armEntity) throws SdaiException {
		// in AP203 style mapping is IDENTICAL, so do nothing
		if(apFlag == AP203)
			return;
		// We reuse the same instance as it is PDR, so don't need to create one more PDR as it is done for DBPO
		if(armEntity instanceof EAssembly_component){
			return;
		}
		unsetDerived_from(context, armEntity);
		// ALTERNATIVE 2, if it is ALTERNATIVE 1 - DO NOTHING
		if(armEntity.testDerived_from(null)){
			EProduct_definition definition = armEntity.getDerived_from(null);
			EProduct_definition_usage epdr = (EProduct_definition_usage)
				context.working_model.createEntityInstance(CProduct_definition_usage.definition);
			epdr.setRelated_product_definition(null, armEntity);
			epdr.setRelating_product_definition(null, definition);
			epdr.setName(null, "definition usage");
			epdr.setId(null, "");
		}
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
		AProduct_definition_relationship apdr = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProduct_definition_relationship epdr = apdr.getCurrentMember(iter);
			if(epdr instanceof EProduct_definition_usage){
				// Safety check for derived attributes
/*				if(((CEntity)epdr).testAttributeFast(CProduct_definition_relationship.attributeName(null), null) < 0){
					continue;
				}*/
				if((epdr.testName(null))&&(epdr.getName(null).equals("definition usage")))
					epdr.deleteApplicationInstance();
			}
		}
	}
	
	// Special method, which determines which AP is output generated for - which assembly structure has to be generated.
	// Options:
	// 1) AP203 (or AP214) - it is used if (0 or 1 assembly_component_usage is pointing to this occurrence) AND (it is not subtype of Assembly_component)
	// 2) AP214 S7 - it is used if (2 or more assembly_component_usages are pointing to this occurrence) AND (it is not subtype of Assembly_component)
	// 3) AP210 - it is subtype of Assembly_component. For AP210 - Assembly_components - simply do nothing and do not invoke this method
	public static void finalize_assembly_structure(SdaiContext context, EDefinition_based_product_occurrence component)throws SdaiException{
		// Last chance to skip 1) or 2) patterns
		// System.err.println(" Component "+component+" "+context.working_model.getUnderlyingSchemaString());
		if(context.working_model.getUnderlyingSchemaString().equalsIgnoreCase("ap210_electronic_assembly_interconnect_and_packaging_design_mim")){
			component.unsetDerived_from(null);
			return;
		}
		// if new structure is used - do nothing
		AProduct_occurrence_definition_relationship_armx apodr = new AProduct_occurrence_definition_relationship_armx();
		CProduct_occurrence_definition_relationship_armx.usedinRelated_view(null, component, null, apodr);
		if(apodr.getMemberCount() > 0){
			// component.unsetDerived_from(null);
//			System.err.println(" ComponentA "+component+" "+apodr.getByIndex(1));
			return;
		}
		// Maybe it is already implemented on AIM, but even than schema will not be compatible, so need to delete them
		ANext_assembly_usage_occurrence_relationship anauor = new ANext_assembly_usage_occurrence_relationship();
		CNext_assembly_usage_occurrence_relationship.usedinOccurrence(null, component, context.domain, anauor);
		if(anauor.getMemberCount() > 0){
//			for(int i=1, count=anauor.getMemberCount(); i<=count; i++){
//				anauor.getByIndex(i).deleteApplicationInstance();
//			}
			component.unsetDerived_from(null);
//			System.err.println(" ComponentB "+component+" "+anauor.getByIndex(1));
			return;
		}
		// System.err.println(" Component1 "+component);
		AProduct_definition_relationship apdr = new AProduct_definition_relationship();
		CProduct_definition_relationship.usedinRelated_product_definition(null, component, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		int count = 0;
		AAssembly_component_usage aacu = new AAssembly_component_usage(); 
		while(iter.next()){
			EProduct_definition_relationship epdr = apdr.getCurrentMember(iter);
			if(epdr instanceof EAssembly_component_usage){
				count++;
				aacu.addUnordered(epdr);
			}
		}
		EProduct_definition definition = null;
		if(component.testDerived_from(null)){
			// Get definition
			definition = component.getDerived_from(null);
			component.unsetDerived_from(null);
		}
		// System.err.println(" Component2 "+component+" count "+count);
		// 1) Just delete this occurrence
		if(count < 2){
			// Clean ARM specific attributes
			// component.unsetAdditional_characterization(null);
			component.unsetAdditional_contexts(null);
			if(count == 1){
				aacu.getByIndex(1).deleteApplicationInstance();
			}
			if(definition != null){
				EEntity newComponent = component.findEntityInstanceSdaiModel().substituteInstance(component, definition.getInstanceType());
				definition.moveUsersFrom(newComponent);
				newComponent.deleteApplicationInstance();
			}
		}
		// 2) Create few needed entities - AP214 S7
		else if(definition != null){
			EProduct_definition_relationship epdr = (EProduct_definition_relationship)
				context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
			epdr.setRelated_product_definition(null, component);
			epdr.setRelating_product_definition(null, definition);
			
			SdaiIterator iterACU = aacu.createIterator();
			while(iterACU.next()){
				EAssembly_component_usage eacu = aacu.getCurrentMember(iterACU);
				EProduct_definition_occurrence_relationship epdor = (EProduct_definition_occurrence_relationship)
					context.working_model.createEntityInstance(CProduct_definition_occurrence_relationship.definition);
				epdor.setOccurrence(null, component);
				epdor.setOccurrence_usage(null, eacu);
				
				eacu.setRelated_product_definition(null, definition);
			}
		}
	}
	
	
}