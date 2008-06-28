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

package jsdai.SFabrication_joint_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SComponent_feature_xim.CxComponent_feature_joint_armx;
import jsdai.SFabrication_joint_mim.CFabrication_joint;
import jsdai.SLayered_interconnect_module_design_xim.EStratum_feature_template_component_armx;
import jsdai.SProduct_property_definition_schema.AShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.CShape_aspect;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;

public class CxFabrication_joint_armx extends CFabrication_joint_armx implements EMappedXIMEntity{

	EEntity getAimInstance(){
		return this;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CFabrication_joint.definition);

		setMappingConstraints(context, this);

		setAuxiliary_joint_material(context, this);
		
		unsetAuxiliary_joint_material(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetAuxiliary_joint_material(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  make_from_feature_relationship &lt;=
	 *  component_feature_relationship &lt;=
	 *  shape_aspect_relationship
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
			EFabrication_joint_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxComponent_feature_joint_armx.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EFabrication_joint_armx armEntity) throws SdaiException {
		CxComponent_feature_joint_armx.unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
		attribute_mapping auxiliary_joint_material(auxiliary_joint_material, $PATH, Stratum_feature_template_component);
			fabrication_joint <=
			component_feature_joint <=
			shape_aspect <-
			shape_aspect_relationship.relating_shape_aspect
			shape_aspect_relationship
			{shape_aspect_relationship.name = 'auxiliary joint material'}
			shape_aspect_relationship.related_shape_aspect ->
			shape_aspect
			shape_aspect.of_shape ->
			product_definition_shape =>
			assembly_component =>
			laminate_component =>
			stratum_feature_template_component
		end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// FJ <- SAR -> SA -> SFTC
	public static void setAuxiliary_joint_material(SdaiContext context,
			EFabrication_joint_armx armEntity) throws SdaiException {
		unsetAuxiliary_joint_material(context, armEntity);
		if(armEntity.testAuxiliary_joint_material(null)){
			EStratum_feature_template_component_armx auxMaterial = armEntity.getAuxiliary_joint_material(null);
			// SA
			LangUtils.Attribute_and_value_structure[] saStructure = {new LangUtils.Attribute_and_value_structure(
					CShape_aspect.attributeOf_shape(null), auxMaterial)};
			EShape_aspect esa = (EShape_aspect)
				LangUtils.createInstanceIfNeeded(context, CShape_aspect.definition,
						saStructure);
			if(!esa.testName(null))
				esa.setName(null, "");
			if(!esa.testProduct_definitional(null))
				esa.setProduct_definitional(null, ELogical.UNKNOWN);
			
			// SAR
			EShape_aspect_relationship esar = (EShape_aspect_relationship)
				context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
			esar.setRelated_shape_aspect(null, esa);
			esar.setRelating_shape_aspect(null, armEntity);
			esar.setName(null, "auxiliary joint material");
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAuxiliary_joint_material(SdaiContext context,
			EFabrication_joint_armx armEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount(); i<= count; i++){
			EShape_aspect_relationship esar = asar.getByIndex(i);
			if((esar.testName(null))&&(esar.getName(null).equals("auxiliary joint material"))){
				esar.deleteApplicationInstance();
			}
		}
	}
	
}