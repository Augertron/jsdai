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

import jsdai.SProduct_property_definition_schema.AShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SShape_tolerance_schema.CTolerance_zone;
import jsdai.SShape_tolerance_schema.CTolerance_zone_form;
import jsdai.SShape_tolerance_schema.ETolerance_zone_form;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxTolerance_zone_armx extends CTolerance_zone_armx implements EMappedXIMEntity
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

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CTolerance_zone.definition);

		setMappingConstraints(context, this);

	    // affected_plane : OPTIONAL Axis_placement_shape_element;
		setAffected_plane(context, this);
		
	    // model_coordinate_system : OPTIONAL Axis_placement_shape_element;
		setModel_coordinate_system(context, this);
		
	    // form_type : OPTIONAL tolerance_zone_type; 
		setForm_type(context, this);
		
	    // affected_plane : OPTIONAL Axis_placement_shape_element;
		unsetAffected_plane(null);
		
	    // model_coordinate_system : OPTIONAL Axis_placement_shape_element;
		unsetModel_coordinate_system(null);
		
	    // form_type : OPTIONAL tolerance_zone_type; 
		unsetForm_type(null);		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
		    // affected_plane : OPTIONAL Axis_placement_shape_element;
			unsetAffected_plane(context, this);
			
		    // model_coordinate_system : OPTIONAL Axis_placement_shape_element;
			unsetModel_coordinate_system(context, this);
			
		    // form_type : OPTIONAL tolerance_zone_type; 
			unsetForm_type(context, this);					
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
			ETolerance_zone_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
        // SELF\shape_aspect.of_shape : product_definition_shape := ?;
        // SELF\shape_aspect.product_definitional : LOGICAL := ?;
		if(!armEntity.testProduct_definitional(null)){
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		}
        // SELF\shape_aspect.name : label := ?;
		if(!armEntity.testName((EShape_aspect)null)){
			armEntity.setName((EShape_aspect)null, "");
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
			ETolerance_zone_armx armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for affected_plane attribute.
	 * 
	 * <p>
	attribute_mapping affected_plane(affected_plane, $PATH, Axis_placement_shape_element);
		tolerance_zone <=
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship
		{shape_aspect_relationship.name = 'affected plane association'} 
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setAffected_plane(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		unsetAffected_plane(context, armEntity);
		if(armEntity.testAffected_plane(null)){
			EAxis_placement_shape_element eapse = armEntity.getAffected_plane(null);
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, armEntity);
			esar.setName(null, "affected plane association");
			esar.setRelating_shape_aspect(null, eapse);
		}
	}

	public static void unsetAffected_plane(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount();i<=count;i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if((esar.testName(null))&&((esar.getName(null).equals("affected plane association")))){
				esar.deleteApplicationInstance();
			}
		}
	}	
	
	/**
	 * Sets/creates data for model_coordinate_system attribute.
	 * 
	 * <p>
	attribute_mapping model_coordinate_system(model_coordinate_system, $PATH, Axis_placement_shape_element);
		tolerance_zone <=
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship
		{shape_aspect_relationship.name = 'model coordinate system'}
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setModel_coordinate_system(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		unsetModel_coordinate_system(context, armEntity);
		if(armEntity.testAffected_plane(null)){
			EAxis_placement_shape_element eapse = armEntity.getAffected_plane(null);
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, armEntity);
			esar.setName(null, "model coordinate system");
			esar.setRelating_shape_aspect(null, eapse);
		}
	}

	public static void unsetModel_coordinate_system(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount();i<=count;i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if((esar.testName(null))&&((esar.getName(null).equals("model coordinate system")))){
				esar.deleteApplicationInstance();
			}
		}
	}		
	
	/**
	 * Sets/creates data for model_coordinate_system attribute.
	 * 
	 * <p>
	attribute_mapping form_type(form_type, $PATH, tolerance_zone_form.name);
		tolerance_zone.form ->
		tolerance_zone_form
		tolerance_zone_form.name
		{(tolerance_zone_form.name = 'cylindrical')
		(tolerance_zone_form.name = 'spherical')
		(tolerance_zone_form.name = 'parallelepiped')
		(tolerance_zone_form.name = 'conical')
		(tolerance_zone_form.name = 'non uniform')
		(tolerance_zone_form.name != '').UNKOWN.}
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setForm_type(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		unsetForm_type(context, armEntity);
		if(armEntity.testForm_type(null)){
			int form = armEntity.getForm_type(null);
			String name;
//			if(form == ETolerance_zone_type.UNKOWN){
//				name = "";
//			}else{
				name = ETolerance_zone_type.toString(form).replace("_", " ").toLowerCase();
//			}
			// TZF
			LangUtils.Attribute_and_value_structure[] tzfStructure = {
					new LangUtils.Attribute_and_value_structure(
							CTolerance_zone_form.attributeName(null), name)
			};
			ETolerance_zone_form etzf = (ETolerance_zone_form) 
				LangUtils.createInstanceIfNeeded(context,
						CTolerance_zone_form.definition, tzfStructure);
			armEntity.setForm(null, etzf);
		}
	}

	public static void unsetForm_type(SdaiContext context, ETolerance_zone_armx armEntity) throws SdaiException {
		armEntity.unsetForm(null);
	}			
}