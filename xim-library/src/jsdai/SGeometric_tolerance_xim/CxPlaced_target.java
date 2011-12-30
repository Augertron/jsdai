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

package jsdai.SGeometric_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SDerived_shape_element_xim.EContact;
import jsdai.SDimension_tolerance_xim.CxAssociated_shape_element;
import jsdai.SGeometric_tolerance_mim.CPlaced_datum_target_feature;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CShape_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SShape_aspect_definition_schema.CShape_representation_with_parameters;
import jsdai.SShape_aspect_definition_schema.EDatum_target;
import jsdai.SShape_aspect_definition_schema.EShape_representation_with_parameters;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxPlaced_target extends CPlaced_target implements EMappedXIMEntity
{
	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a7$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a7);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a7 = get_instance(a7);
		return (EProduct_definition_shape)a7;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a7 = set_instanceX(a7, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a7 = unset_instance(a7);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a7$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a8);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a8);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a8 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a8 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a8$;
	}

	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a5);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a5);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a5 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a5 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a5$;
		}
		
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a6);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a6$;
	}
	// ENDOF From CShape_aspect.java

	/// methods for attribute: target_id, base type: STRING
/*	public boolean testTarget_id(EDatum_target type) throws SdaiException {
		return test_string(a9);
	}
	public String getTarget_id(EDatum_target type) throws SdaiException {
		return get_string(a9);
	}*/
	public void setTarget_id(EDatum_target type, String value) throws SdaiException {
		a9 = set_string(value);
	}
	public void unsetTarget_id(EDatum_target type) throws SdaiException {
		a9 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeTarget_id(EDatum_target type) throws SdaiException {
		return a9$;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlaced_datum_target_feature.definition);

		setMappingConstraints(context, this);

		// property - used for both attributes
//		unsetProperty(context, this);

		// defined_in : geometric_representation_context;
//		setDefined_in(context, this);
		
        // parameter_reference : axis2_placement;
//		setParameter_reference(context, this);
		
		// clean ARM
		// defined_in : geometric_representation_context;
//		unsetDefined_in(null);
		
        // parameter_reference : axis2_placement;
//		unsetParameter_reference(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// property - used for both attributes
//			unsetProperty(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EPlaced_target armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxAssociated_shape_element.setMappingConstraints(context, armEntity);
        // SELF\shape_aspect.of_shape : product_definition_shape := ?;
        // SELF\shape_aspect.product_definitional : LOGICAL := ?;
		// "wr2" in entity "datum_target" 
		armEntity.setProduct_definitional(null, ELogical.TRUE);
        // SELF\shape_aspect.description : text := ?;
        // SELF\datum_target.target_id : identifier := ?;
		if(!armEntity.testTarget_id(null)){
			armEntity.setTarget_id(null, "");
		}
        // SELF\shape_aspect.name : label := ?;
		if(!(armEntity instanceof EContact)){
			if(!armEntity.testName((EShape_aspect)null)){
				armEntity.setName((EShape_aspect)null, "");
			}
		}
		setOrientation(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPlaced_target armEntity) throws SdaiException {
		CxAssociated_shape_element.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for defined_in attribute.
	 * 
	 * <p>
	attribute_mapping defined_in(defined_in, $PATH, geometric_representation_context);
		placed_datum_target_feature <= 
		datum_target <= 
		shape_aspect 
		shape_definition = shape_aspect 
		shape_definition
		characterized_definition = shape_definition
		characterized_definition <- 
		property_definition.definition 
		property_definition 
		represented_definition = property_definition
		represented_definition <- 
		property_definition_representation.definition
		{property_definition_representation => 
		shape_definition_representation} 
		property_definition_representation
		property_definition_representation.used_representation ->
		{representation => 
		shape_representation => 
		shape_representation_with_parameters} 
		representation
		representation.context_of_items -> 
		representation_context => 
		geometric_representation_context
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PDTF <- PropD <- PDR -> SRWP -> GRC 
	/* Removed - issue 3610	
	public static void setDefined_in(SdaiContext context,
		EPlaced_target armEntity) throws SdaiException {
		if(armEntity.testDefined_in(null)){
			EGeometric_representation_context egrc = armEntity.getDefined_in(null);
			// SRWP
			LangUtils.Attribute_and_value_structure[] srStructure = {
					new LangUtils.Attribute_and_value_structure(
							CShape_representation_with_parameters.attributeContext_of_items(null), egrc)
			};
			EShape_representation_with_parameters esrwp = (EShape_representation_with_parameters) 
				LangUtils.createInstanceIfNeeded(context,
						CShape_representation_with_parameters.definition, srStructure);
			if(!esrwp.testName(null)){
				esrwp.setName(null, "");
			}
			// PD
			LangUtils.Attribute_and_value_structure[] pdStructure = {
					new LangUtils.Attribute_and_value_structure(
							CProperty_definition.attributeDefinition(null), armEntity)
			};
			EProperty_definition epd = (EProperty_definition) 
				LangUtils.createInstanceIfNeeded(context,
						CProperty_definition.definition, pdStructure);
			if(!epd.testName(null)){
				epd.setName(null, "");
			}
			// EPDR
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, epd);
			epdr.setUsed_representation(null, esrwp);
		}
	}
*/
	/**
	 * Sets/creates data for Parameter_reference attribute.
	 * 
	 * <p>
	attribute_mapping parameter_reference(parameter_reference, $PATH, axis2_placement);
		placed_datum_target_feature <= 
		datum_target <= 
		shape_aspect 
		shape_definition = shape_aspect
		shape_definition
		characterized_definition = shape_definition
		characterized_definition <- 
		property_definition.definition property_definition
		represented_definition = property_definition
		represented_definition <- 
		property_definition_representation.definition
		{property_definition_representation => 
		shape_definition_representation}
		property_definition_representation
		property_definition_representation.used_representation ->
		{representation => 
		shape_representation => 
		shape_representation_with_parameters}
		representation 
		representation.items[i] -> 
		representation_item
		{representation_item.name = 'orientation'}
		representation_item => geometric_representation_item
		geometric_representation_item => placement
		placement
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PDTF <- PropD <- SDR -> SRWP -> P 
/* Removed - bugzilla issue 3610	
	public static void setParameter_reference(SdaiContext context,
		EPlaced_target armEntity) throws SdaiException {
		if(armEntity.testParameter_reference(null)){
			EPlacement placement = (EPlacement)armEntity.getParameter_reference(null);
			placement.setName(null, "orientation");
			setParameter(context, armEntity, placement);
		}
	}
*/	
	public static EShape_representation_with_parameters setParameter(SdaiContext context, 
			EPlaced_target armEntity, ERepresentation_item eri)
			throws SdaiException {
		// SRWP
		LangUtils.Attribute_and_value_structure[] srStructure = {
				new LangUtils.Attribute_and_value_structure(
						CShape_representation_with_parameters.attributeItems(null), eri)
		};
		EShape_representation_with_parameters esrwp = (EShape_representation_with_parameters) 
			LangUtils.createInstanceIfNeeded(context,
					CShape_representation_with_parameters.definition, srStructure);
		if(!esrwp.testName(null)){
			esrwp.setName(null, "");
		}
		// PD
		LangUtils.Attribute_and_value_structure[] pdStructure = {
				new LangUtils.Attribute_and_value_structure(
						CProperty_definition.attributeDefinition(null), armEntity)
		};
		EProperty_definition epd = (EProperty_definition) 
			LangUtils.createInstanceIfNeeded(context,
					CProperty_definition.definition, pdStructure);
		if(!epd.testName(null)){
			epd.setName(null, "");
		}
		// EPDR
		EProperty_definition_representation epdr = (EProperty_definition_representation)
			context.working_model.createEntityInstance(CShape_definition_representation.definition);
		epdr.setDefinition(null, epd);
		epdr.setUsed_representation(null, esrwp);
		return esrwp;
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */

	public static void unsetProperty(SdaiContext context,
			EPlaced_target armEntity) throws SdaiException {
		// do nothing as orientation is already using this link
/*		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int i=1,n=apd.getMemberCount(); i<=n; i++){
			EProperty_definition epd = apd.getByIndex(i);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			for(int j=1,m=apdr.getMemberCount(); j<=m; j++){
				EProperty_definition_representation epdr = apdr.getByIndex(j);
				if((epdr.testUsed_representation(null))&&(epdr.getUsed_representation(null) instanceof EShape_representation_with_parameters)){
					epdr.deleteApplicationInstance();
				}
			}
		}*/
	}
	
	// Converting new pattern to old one - According Rec practices:
	// "CAx-IF Recommended Practices For the Representation and Presentation of Product Manufacturing Information (PMI)
	// Version 3.2a, 6/22/2011"
	public static void setOrientation(SdaiContext context, EPlaced_target armEntity) throws SdaiException {
		ERepresentation_item eri = armEntity.getIdentified_item(null);
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, eri, null, ar);
		ERepresentation_context erc = null;
		if(ar.getMemberCount() > 0){
			erc = ar.getByIndex(1).getContext_of_items(null);
		}
		eri.setName(null, "orientation");
		EShape_representation_with_parameters esrwp = CxPlaced_target.setParameter(context, armEntity, eri);
		if(erc != null){
			esrwp.setContext_of_items(null, erc);
		}
	}	
	
}