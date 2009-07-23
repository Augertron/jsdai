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
import jsdai.SMeasure_schema.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SRepresentation_schema.*;

public class CxProduct_occurrence_with_quantity
		extends
			CProduct_occurrence_with_quantity implements EMappedXIMEntity{

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
			
			//additional_characterization - this is DERIVED to some magic string
			// setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			setOccurrence_quantity(context, this);
			
			// Clean ARM specific attributes - this is DERIVED to some magic string
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);

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
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxProduct_occurrence.setMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "quantified instance");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		CxProduct_occurrence.unsetMappingConstraints(context, armEntity);
		CxAP210ARMUtilities.setProduct_definitionName(context, armEntity, "");
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
	/* - this is DERIVED to some magic string
	public static void setAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);		
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
/* - this is DERIVED to some magic string	
	public static void unsetAdditional_characterization(SdaiContext context,
			EProduct_view_definition armEntity) throws SdaiException {
		CxProduct_view_definition.setAdditional_characterization(context, armEntity);		
	}
*/
// Product_occurrence attributes	
	
	/**
	 * Sets/creates data for name attribute.
	
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
	{representation_item.name = 'selection quantity'}
	representation_item =&gt; 
	([measure_representation_item]
	[qualified_representation_item])
	(compound_representation_item
	compound_representation_item =&gt; value_range)	
		
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PD <- PD <- PDR -> R

	public static void setOccurrence_quantity(SdaiContext context,
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		unsetOccurrence_quantity(context, armEntity);
		if(armEntity.testOccurrence_quantity(null)){
			EEntity quantity = armEntity.getOccurrence_quantity(null);
			ERepresentation_item item = CxAP210ARMUtilities.upgradeToMRI(context.working_model, (EMeasure_with_unit)quantity);
			ERepresentation er = 
				CxAP210ARMUtilities.findRepresentation(context, armEntity, "occurrence selection", "selection criteria");
			ARepresentation_item items;
			if(er.testItems(null))
				items = er.getItems(null);
			else
				items = er.createItems(null);
			if(!items.isMember(item))
				items.addUnordered(item);
				
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
	
	public static void unsetOccurrence_quantity(SdaiContext context,
			EProduct_occurrence_with_quantity armEntity) throws SdaiException {
		CxAP210ARMUtilities.clearRepresentationItems(context, armEntity, "occurrence selection", "selection criteria", "selection quantity");		
	}
	

}