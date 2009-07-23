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

package jsdai.SContextual_shape_positioning_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeometry_schema.*;
import jsdai.SMixed_complex_types.*;
import jsdai.SRepresentation_schema.*;

public class CxGeometric_composition_with_placement_transformation extends CGeometric_composition_with_placement_transformation implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from representation_relationship_with_transformation
// -2- methods for SELECT attribute: transformation_operator
/*	public static int usedinTransformation_operator(ERepresentation_relationship_with_transformation type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a4$, domain, result);
	}*/
	private boolean testTransformation_operator2(ERepresentation_relationship_with_transformation type) throws SdaiException {
		return test_instance(a4);
	}

	private EEntity getTransformation_operator2(ERepresentation_relationship_with_transformation type) throws SdaiException { // case 1
		a4 = get_instance_select(a4);
		return (EEntity)a4;
	}

	public void setTransformation_operator(ERepresentation_relationship_with_transformation type, EEntity value) throws SdaiException { // case 1
		a4 = set_instanceX(a4, value);
	}

	public void unsetTransformation_operator(ERepresentation_relationship_with_transformation type) throws SdaiException {
		a4 = unset_instance(a4);
	}

	public static jsdai.dictionary.EAttribute attributeTransformation_operator(ERepresentation_relationship_with_transformation type) throws SdaiException {
		return a4$;
	}
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDefinitional_representation_relationship$representation_relationship_with_transformation$shape_representation_relationship.definition);
		
		setMappingConstraints(context, this);
		
		setSource(context, this);
		
		setTarget(context, this);

		unsetSource(null);
		
		unsetTarget(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetSource(context, this);
		
		unsetTarget(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		CxGeometric_relationship_with_placement_transformation.setMappingConstraints(context, armEntity);

	}

	public static void unsetMappingConstraints(SdaiContext context, EGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		CxGeometric_relationship_with_placement_transformation.unsetMappingConstraints(context, armEntity);
	}

	/**
	* Sets/creates data for source.
	*
	*			<aa attribute="source" assertion_to="Axis_placement">
				<aimelt>
					PATH
				</aimelt>
				<refpath>
					representation_relationship_with_transformation
					representation_relationship_with_transformation.transformation_operator -&gt;
					transformation = item_defined_transformation
					item_defined_transformation.transform_item_1 -&gt;
					representation_item =&gt;
					placement
					(placement =&gt;
					axis1_placement)
					(placement =&gt;
					axis2_placement_2d)
					(placement =&gt;
					axis2_placement_3d)
				</refpath>
			</aa>		
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setSource(SdaiContext context, CxGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		unsetSource(context, armEntity);
		if(armEntity.testSource(null)){
			EPlacement placement = (EPlacement)armEntity.getSource(null);
			EItem_defined_transformation eidt;
			if(armEntity.testTransformation_operator2(null)){
				eidt = (EItem_defined_transformation)armEntity.getTransformation_operator2(null);
			}
			else{
				eidt = (EItem_defined_transformation)context.working_model.createEntityInstance(CItem_defined_transformation.definition);
				eidt.setName(null, "");
				armEntity.setTransformation_operator(null, eidt);
			}
			eidt.setTransform_item_1(null, placement);
		}

	}

	public static void unsetSource(SdaiContext context, CxGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		unsetSource(context, armEntity);
		if(armEntity.testTransformation_operator2(null)){
			EItem_defined_transformation eidt = 
				(EItem_defined_transformation)armEntity.getTransformation_operator2(null);
			eidt.unsetTransform_item_1(null);
		}
	}

	/**
	* Sets/creates data for source.
	*
	*			<aa attribute="source" assertion_to="Axis_placement">
				<aimelt>
					PATH
				</aimelt>
				<refpath>
					representation_relationship_with_transformation
					representation_relationship_with_transformation.transformation_operator -&gt;
					transformation = item_defined_transformation
					item_defined_transformation.transform_item_2 -&gt;
					representation_item =&gt;
					placement
					(placement =&gt;
					axis1_placement)
					(placement =&gt;
					axis2_placement_2d)
					(placement =&gt;
					axis2_placement_3d)
				</refpath>
			</aa>		
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setTarget(SdaiContext context, CxGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		unsetTarget(context, armEntity);
		if(armEntity.testTarget(null)){
			EPlacement placement = (EPlacement)armEntity.getTarget(null);
			EItem_defined_transformation eidt;
			if(armEntity.testTransformation_operator2(null)){
				eidt = (EItem_defined_transformation)armEntity.getTransformation_operator2(null);
			}
			else{
				eidt = (EItem_defined_transformation)context.working_model.createEntityInstance(CItem_defined_transformation.definition);
				eidt.setName(null, "");
				armEntity.setTransformation_operator(null, eidt);
			}
			eidt.setTransform_item_2(null, placement);
		}

	}

	public static void unsetTarget(SdaiContext context, CxGeometric_composition_with_placement_transformation armEntity) throws SdaiException
	{
		unsetTarget(context, armEntity);
		if(armEntity.testTransformation_operator2(null)){
			EItem_defined_transformation eidt = 
				(EItem_defined_transformation)armEntity.getTransformation_operator2(null);
			eidt.unsetTransform_item_2(null);
		}
	}
	
}
