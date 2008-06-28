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
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;

public class CxSelected_instance
		extends
			CSelected_instance implements EMappedXIMEntity{

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
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setQuantity_criterion(context, this);
			
			setOccurrence_quantity(context, this);
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);

			unsetQuantity_criterion(null);
			unsetOccurrence_quantity(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization - this is DERIVED to some magic string
			// unsetAdditional_characterization(context, this);

			unsetQuantity_criterion(context, this);
			
			unsetOccurrence_quantity(context, this);
			
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; product_definition
	 *  product_definition
	 *  {product_definition.name='quantified instance'}
	 *  {product_definition.frame_of_reference -&gt; product_definition_context
	 *  product_definition_context &lt;= application_context_element 
	 *  application_context_element.name = 'part occurrence'}	 
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
			ESelected_instance armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxProduct_occurrence_with_quantity.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "selected instance");
		// PDC
/*      LangUtils.Attribute_and_value_structure[] pdcStructure = {
            new LangUtils.Attribute_and_value_structure(
            CProduct_definition_context.attributeName(null), "part occurrence"),
         };
      EProduct_definition_context epdc = (EProduct_definition_context) 
			LangUtils.createInstanceIfNeeded(context,
			CProduct_definition_context.definition, pdcStructure);
      armEntity.setFrame_of_reference(null, epdc);*/   
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ESelected_instance armEntity) throws SdaiException {
		CxProduct_occurrence.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "");
		// armEntity.unsetFrame_of_reference(null);
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
	 * Sets/creates data for Occurrence_quantity attribute.
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PD <- PDR -> R
	public static void setOccurrence_quantity(SdaiContext context,
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		CxProduct_occurrence_with_quantity.setOccurrence_quantity(context, armEntity);		
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
	public static void unsetOccurrence_quantity(SdaiContext context,
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		CxProduct_occurrence_with_quantity.unsetOccurrence_quantity(context, armEntity);		
	}
	
	/**
	 * Sets/creates data for name attribute.
		// NOT IMPLEMENTED ALTERNATIVE	
		assembly_component_usage &lt;= product_definition_usage 
		product_definition_usage &lt;= product_definition_relationship
		characterized_product_definition = product_definition_relationship
		characterized_definition = characterized_product_definition
		characterized_definition &lt;- property_definition.definition
		property_definition 
		{property_definition.name = 'occurrence selection'}
		represented_definition = property_definition
		represented_definition &lt;- property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation -&gt; representation
		{representation.name = 'selection criteria'}
		representation.items[i] -&gt; representation_item
		representation_item =&gt; descriptive_representation_item
		{representation_item.name = 'selection control'}
		descriptive_representation_item.description
		// IMPLEMENTED
		product_definition = characterized_product_definition
		characterized_product_definition = characterized_definition
		characterized_definition &lt;- property_definition.definition
		property_definition 
		{property_definition.name = 'occurrence selection'}
		represented_definition = property_definition
		represented_definition &lt;- property_definition_representation.definition
		property_definition_representation
		property_definition_representation.used_representation -&gt; representation
		{representation.name = 'selection criteria'}
		representation.items[i] -&gt; representation_item
		representation_item =&gt; descriptive_representation_item
		{representation_item.name = 'selection control'}
		descriptive_representation_item.description
		
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PropD <- PDR -> R -> DRI
	
	public static void setQuantity_criterion(SdaiContext context,
			ESelected_instance armEntity) throws SdaiException {
		unsetQuantity_criterion(context, armEntity);
		if(armEntity.testQuantity_criterion(null)){
			String criterion = armEntity.getQuantity_criterion(null);
			ERepresentation er = 
				CxAP210ARMUtilities.findRepresentation(context, armEntity, "occurrence selection", "selection criteria");
			ARepresentation_item items;
			if(er.testItems(null))
				items = er.getItems(null);
			else
				items = er.createItems(null);
			EDescriptive_representation_item edri = (EDescriptive_representation_item)
				context.working_model.createEntityInstance(CDescriptive_representation_item.definition);
			edri.setName(null, "selection control");
			edri.setDescription(null, criterion);
			items.addUnordered(edri);
				
		}
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

	public static void unsetQuantity_criterion(SdaiContext context,
			ESelected_instance armEntity) throws SdaiException {
		ERepresentation er = CxAP210ARMUtilities.findRepresentation(context, armEntity, "occurrence selection", "selection criteria");
		if(er == null)
			return;
		if(!er.testItems(null))
			return;
		ARepresentation_item items = er.getItems(null);
		SdaiIterator iter = items.createIterator();
		while(iter.next()){
			ERepresentation_item item = items.getCurrentMember(iter);
			if((item instanceof EDescriptive_representation_item)&&
				(item.testName(null))&&(item.getName(null).equals("selection control")))
				item.deleteApplicationInstance();
		}
	}
	
	

}