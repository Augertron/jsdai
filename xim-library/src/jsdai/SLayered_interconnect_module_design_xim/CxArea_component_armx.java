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
import jsdai.SLayered_interconnect_module_design_mim.CArea_component;
import jsdai.SLayered_interconnect_module_design_xim.CxStratum_feature_template_component_armx;
import jsdai.SGeneric_product_occurrence_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;
import jsdai.SShape_property_assignment_xim.*;

public class CxArea_component_armx
		extends
			CArea_component_armx implements EMappedXIMEntity{

	// Flags, which are used to determine the type of assembly, which has to be generated 
	public static final int AP203 = 1;
	
	public static final int AP21x = 2;
	
	public static int apFlag = AP21x; // Default is this style  

	// Taken from PDR
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

		setTemp("AIM", CArea_component.definition);

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

			// Implementation_or_resident_stratum
			setImplementation_or_resident_stratum(context, this);
			
			// Eplcitly_created
			setExplicitly_created(context, this);
			
			// is_base  
			setIs_base(context, this); 

			// Replaced_component  
			setReplaced_component(context, this); 
			
			// Clean ARM specific attributes - this is derived in laminate_component
			// unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
			
			
			unsetDerived_from(null);
			unsetId_x(null);
			unsetImplementation_or_resident_stratum(null);
			unsetExplicitly_created(null);
			unsetIs_base(null); 
			unsetReplaced_component(null); 
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

			// Eplcitly_created
			unsetExplicitly_created(context, this);
			
			// is_base  
			unsetIs_base(context, this); 

			// Replaced_component  
			unsetReplaced_component(context, this); 
			
			// Implementation_or_resident_stratum
			unsetImplementation_or_resident_stratum(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EArea_component_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
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
			EArea_component_armx armEntity) throws SdaiException {
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
	public static void setImplementation_or_resident_stratum(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.setImplementation_or_resident_stratum(context, armEntity);
	}


	/**
	* Unsets/deletes data for stratum_feature_implementation attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplementation_or_resident_stratum(SdaiContext context, EArea_component_armx armEntity) throws SdaiException
	{
		CxStratum_feature_template_component_armx.unsetImplementation_or_resident_stratum(context, armEntity);
	}

   //********** "area_component" attributes

   /**
    * Sets/creates data for Explicitly_created attribute.
    *
    *
		attribute_mapping explicitly_created(explicitly_created, $PATH);
		area_component <=
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
		{property_definition.name = 'explicitly created'}
		property_definition.description
		{(property_definition.description = 'true')
		(property_definition.description = 'false')}
		
		end_attribute_mapping;
    *
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setExplicitly_created(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {
      //unset old values
      unsetExplicitly_created(context, armEntity);
      if (armEntity.testExplicitly_created(null)) {
         boolean explicitlyCreated = armEntity.getExplicitly_created(null);
         // pd
         EProperty_definition epd = (EProperty_definition)
			context.working_model.createEntityInstance(CProperty_definition.definition);
         epd.setDefinition(null, armEntity);
         epd.setName(null, "explicitly created");
         if(explicitlyCreated)
        	 epd.setDescription(null, "true");
         else
        	 epd.setDescription(null, "false");
      }
   }

  /**
   * Unsets/deletes data for Explicitly_created attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void unsetExplicitly_created(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {

  		EProperty_definition epdr = null;
  		AProperty_definition apdr = new AProperty_definition();
  		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apdr);

     for(int i = 1; i <= apdr.getMemberCount(); i++) {
        epdr = apdr.getByIndex(i);
        if (epdr.testName(null) && epdr.getName(null).equals("explicitly created")) {
           epdr.deleteApplicationInstance();
        }
     }
  }

  /**
   * Sets/creates data for is_base attribute.
   *
   *
		attribute_mapping is_base(is_base, $PATH);
		area_component <=
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
		{property_definition.name = 'is base'}
		property_definition.description
		{(property_definition.description = 'true')
		(property_definition.description = 'false')}
		
		end_attribute_mapping;
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
  public static void setIs_base(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {
     //unset old values
     unsetIs_base(context, armEntity);
     if (armEntity.testIs_base(null)) {
        boolean explicitlyCreated = armEntity.getIs_base(null);
        // pd
        EProperty_definition epd = (EProperty_definition)
			context.working_model.createEntityInstance(CProperty_definition.definition);
        epd.setDefinition(null, armEntity);
        epd.setName(null, "is base");
        if(explicitlyCreated)
       	 epd.setDescription(null, "true");
        else
       	 epd.setDescription(null, "false");
     }
  }

 /**
  * Unsets/deletes data for Is_base attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetIs_base(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {

 		EProperty_definition epdr = null;
 		AProperty_definition apdr = new AProperty_definition();
 		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apdr);

    for(int i = 1; i <= apdr.getMemberCount(); i++) {
       epdr = apdr.getByIndex(i);
       if (epdr.testName(null) && epdr.getName(null).equals("is base")) {
          epdr.deleteApplicationInstance();
       }
    }
 }

 /**
  * Sets/creates data for replaced_component attribute.
  *
  *
		attribute_mapping replaced_component(replaced_component, $PATH, Area_component_armx);
		area_component <=
		stratum_feature_template_component <=
		laminate_component <=
		assembly_component <=
		component_definition <=
		product_definition <-
		product_definition_relationship.related_product_definition
		product_definition_relationship
		{product_definition_relationship
		product_definition_relationship.name = 'replaced component'}
		product_definition_relationship.relating_product_definition ->
		product_definition =>
		component_definition =>
		assembly_component =>
		laminate_component =>
		stratum_feature_template_component =>
		area_component
		
		end_attribute_mapping;
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setReplaced_component(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {
    //unset old values
    unsetReplaced_component(context, armEntity);
    if (armEntity.testReplaced_component(null)) {
       EArea_component_armx replacedComponent = armEntity.getReplaced_component(null);
       // pdr
       EProduct_definition_relationship epdr = (EProduct_definition_relationship)
			context.working_model.createEntityInstance(CProduct_definition_relationship.definition);
       epdr.setRelated_product_definition(null, armEntity);
       epdr.setRelating_product_definition(null, replacedComponent);
       epdr.setName(null, "replaced component");
       epdr.setId(null, "replaced component");
    }
 }

/**
 * Unsets/deletes data for replaced_component attribute.
 *
 * @param context SdaiContext.
 * @param armEntity arm entity.
 * @throws SdaiException
 */
public static void unsetReplaced_component(SdaiContext context, EArea_component_armx armEntity) throws SdaiException {

	EProduct_definition_relationship epdr = null;
	AProduct_definition_relationship apdr = new AProduct_definition_relationship();
	CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, apdr);

   for(int i = 1; i <= apdr.getMemberCount(); i++) {
      epdr = apdr.getByIndex(i);
      if (epdr.testName(null) && epdr.getName(null).equals("replaced component")) {
         epdr.deleteApplicationInstance();
      }
   }
}
 
}