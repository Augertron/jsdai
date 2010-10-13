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

package jsdai.SPackaged_part_black_box_model_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFunctional_usage_view_xim.EFunctional_unit_usage_view;
import jsdai.SPackaged_part_black_box_model_mim.*;
import jsdai.SPhysical_unit_usage_view_xim.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_view_definition_xim.*;

public class CxPackaged_part_armx
		extends
		CPackaged_part_armx implements EMappedXIMEntity {

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

	// From property_definition
/*	public static int usedinDefinition(EProperty_definition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testDefinition(EProperty_definition type) throws SdaiException {
		return test_instance(a8);
	}

	public EEntity getDefinition(EProperty_definition type) throws SdaiException { // case 1
		a8 = get_instance_select(a8);
		return (EEntity)a8;
	}
*/
	public void setDefinition(EProperty_definition type, EEntity value) throws SdaiException { // case 1
		a8 = set_instanceX(a8, value);
	}

	public void unsetDefinition(EProperty_definition type) throws SdaiException {
		a8 = unset_instance(a8);
	}

	public static jsdai.dictionary.EAttribute attributeDefinition(EProperty_definition type) throws SdaiException {
		return a8$;
	}
	
	
	// Taken from Physical_unit - Property_definition
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(jsdai.SProduct_property_definition_schema.EProperty_definition type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(jsdai.SProduct_property_definition_schema.EProperty_definition type) throws SdaiException {
		return a6$;
	}
	// ENDOF Taken from Physical_unit - Property_definition
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPackaged_part.definition);

			setMappingConstraints(context, this);

			//********** "design_discipline_item_definition" attributes
			//id - goes directly into AIM
			
			//additional_characterization
			setAdditional_characterization(context, this);

			//additional_context
			setAdditional_contexts(context, this);

			// From Item_shape
//			setId_x(context, this);

			// SETTING DERIVED
			// setDefinition(null, this);
			
			// used_package
		   setUsed_package(context, this);
		   
		   // implemented_function
	      setImplemented_function(context, this);
	      
			// Clean ARM specific attributes
			unsetAdditional_characterization(null);
			unsetAdditional_contexts(null);
//			unsetId_x(null);
			
		   unsetUsed_package(null);
	      unsetImplemented_function(null);
			
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			//********** "design_discipline_item_definition" attributes
			//associated_item_version - goes directly into AIM

			//additional_context
			unsetAdditional_contexts(context, this);

			//additional_characterization
			unsetAdditional_characterization(context, this);

			//id_x
//			unsetId_x(context, this);
			
			// unsetDefinition(null);

			// used_package
		   unsetUsed_package(context, this);
		   
		   // implemented_function
	      unsetImplemented_function(context, this);
			
			// this.deleteApplicationInstance();
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; packaged_part &lt;=
	 *  physical_unit &lt;=
	 *  product_definition
	 *  {product_definition
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'physical design usage']}
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
			EPackaged_part_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPart_usage_view.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPackaged_part_armx armEntity) throws SdaiException {
		CxPart_usage_view.unsetMappingConstraints(context, armEntity);
	}

	//********** "design_discipline_item_definition" attributes
	/* Removed from XIM - see bug #3610
	public static void setId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		//unset old values
		CxItem_shape.setId_x(context, armEntity);
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
	/* Removed from XIM - see bug #3610	
	public static void unsetId_x(SdaiContext context,
			EItem_shape armEntity) throws SdaiException {
		CxItem_shape.unsetId_x(context, armEntity);	
	}
*/

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

	//********** "packaged_part" attributes

	/**
	* Sets/creates data for used_package attribute.
	*
	* <p>
	*  attribute_mapping used_package_package (used_package
	* , (*PATH*), package);
	* 	(packaged_part <=)
	* 	(externally_defined_packaged_part <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_packaged_part <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `used package'}
	* 	product_definition_relationship
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `physical design usage'}
	* 	product_definition =>
	* 	physical_unit =>
	* 	(package)
	* 	(externally_defined_physical_unit =>
	* 	externally_defined_package)
	* 	(externally_defined_physical_unit =>
	* 	library_defined_physical_unit =>
	* 	library_defined_package)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
  public static void setUsed_package(SdaiContext context, EPackaged_part_armx armEntity) throws SdaiException {
     //unset old values
     unsetUsed_package(context, armEntity);

     if (armEntity.testUsed_package(null)) {

        AEntity armUsed_packages = armEntity.getUsed_package(null);
        for (int i = 1; i <= armUsed_packages.getMemberCount(); i++) {
           EProduct_definition armUsed_package = (EProduct_definition)armUsed_packages.getByIndexEntity(i);

           //product_definition_relationship
           jsdai.SProduct_definition_schema.EProduct_definition_relationship relationship = (jsdai.
              SProduct_definition_schema.EProduct_definition_relationship) context.working_model.
              createEntityInstance(jsdai.SProduct_definition_schema.CProduct_definition_relationship.definition);
           relationship.setId(null, "");
           relationship.setName(null, "used package");
           relationship.setRelated_product_definition(null, armEntity);
           relationship.setRelating_product_definition(null, armUsed_package);
        }
     }
  }


	/**
	* Unsets/deletes data for used_package attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetUsed_package(SdaiContext context, EPackaged_part_armx armEntity) throws SdaiException
	{
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		EProduct_definition_relationship relationship = null;
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		for (int i = 1; i <= aRelationship.getMemberCount();) {
			relationship = aRelationship.getByIndex(i);

			if (relationship.testName(null) && relationship.getName(null).equals("used package")) {
            aRelationship.removeByIndex(i);
				relationship.deleteApplicationInstance();
			}
         else
            i++;
		}
	}

	/**
	* Sets/creates data for implemented_function attribute.
	*
	* <p>
	*  attribute_mapping implemented_function_functional_unit_usage_view (implemented_function
	* , (*PATH*), functional_unit_usage_view);
	* 	(packaged_part <=)
	* 	(externally_defined_packaged_part <=
	* 	externally_defined_physical_unit <=)
	* 	(library_defined_packaged_part <=
	* 	library_defined_physical_unit <=
	* 	externally_defined_physical_unit <=)
	* 	physical_unit <=
	* 	product_definition <-
	* 	product_definition_relationship.related_product_definition
	* 	product_definition_relationship
	* 	{product_definition_relationship
	* 	product_definition_relationship.name = `implemented function'}
	* 	product_definition_relationship.relating_product_definition ->
	* 	{product_definition
	* 	product_definition.frame_of_reference ->
	* 	product_definition_context <=
	* 	application_context_element
	* 	application_context_element.name = `functional design usage'}
	* 	product_definition =>
	* 	(functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit)
	* 	(functional_unit =>
	* 	externally_defined_functional_unit =>
	* 	library_defined_functional_unit)
	*  end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setImplemented_function(SdaiContext context, EPackaged_part_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetImplemented_function(context, armEntity);

		if (armEntity.testImplemented_function(null))
		{

			EFunctional_unit_usage_view armImplemented_function = armEntity.getImplemented_function(null);

			//product_definition_relationship
			jsdai.SProduct_definition_schema.EProduct_definition_relationship relationship = (jsdai.SProduct_definition_schema.EProduct_definition_relationship) context.working_model.createEntityInstance(jsdai.SProduct_definition_schema.CProduct_definition_relationship.definition);
			relationship.setId(null, "");
			relationship.setName(null, "implemented function");
			relationship.setRelated_product_definition(null, armEntity);
			relationship.setRelating_product_definition(null, armImplemented_function);
		}
	}


	/**
	* Unsets/deletes data for implemented_function attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetImplemented_function(SdaiContext context, EPackaged_part armEntity) throws SdaiException
	{
		AProduct_definition_relationship aRelationship = new AProduct_definition_relationship();
		EProduct_definition_relationship relationship = null;
		CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, aRelationship);
		for (int i = 1; i <= aRelationship.getMemberCount(); i++) {
			relationship = aRelationship.getByIndex(i);

			if (relationship.testName(null) && relationship.getName(null).equals("implemented function")) {
				relationship.deleteApplicationInstance();
			}
		}
	}
	

}