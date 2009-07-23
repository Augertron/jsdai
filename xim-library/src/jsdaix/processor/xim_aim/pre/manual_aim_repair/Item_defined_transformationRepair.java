/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2009, LKSoftWare GmbH, Germany
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

package jsdaix.processor.xim_aim.pre.manual_aim_repair;


import jsdai.SGeometry_schema.EAxis2_placement_2d;
import jsdai.SGeometry_schema.EAxis2_placement_3d;
import jsdai.SGeometry_schema.ECartesian_point;
import jsdai.SGeometry_schema.ECartesian_transformation_operator;
import jsdai.SGeometry_schema.EDirection;
import jsdai.SGeometry_schema.EPlacement;
import jsdai.SRepresentation_schema.AItem_defined_transformation;
import jsdai.SRepresentation_schema.ARepresentation_relationship_with_transformation;
import jsdai.SRepresentation_schema.CItem_defined_transformation;
import jsdai.SRepresentation_schema.CRepresentation_relationship_with_transformation;
import jsdai.SRepresentation_schema.EItem_defined_transformation;
import jsdai.SRepresentation_schema.ERepresentation_relationship_with_transformation;
import jsdai.lang.ASdaiModel;
import jsdai.lang.A_double;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiSession;
import jsdaix.processor.xim_aim.pre.Importer;

/**
 * In stepmod we basically support only the pattern that item_defined_transformation points to 2 axis_placements.
 * This repairer should resolve the situation when one of the ends is cartesian_transformation_operator and
 * convert it to the pattern mentioned above.
 * After this repair entity should map to Geometric_relationship_with_placement_transformation & co
 * TODO - in the future we may need to more fixes here to recreate minimal assemblies, etc.
 * See also bug #2158 in bugzilla.
 */
public final class Item_defined_transformationRepair {
	
	private Item_defined_transformationRepair() { }
	
	private final static float precision = 0.00001f; // precision with which we treat that number is close enough to 0 to be treated as 0

	public static void run(ASdaiModel models, Importer importer)
		throws SdaiException {
		
		for (SdaiIterator i = models.createIterator(); i.next();) {
			SdaiModel model = models.getCurrentMember(i);
			AItem_defined_transformation transformations = (AItem_defined_transformation) model.getInstances(CItem_defined_transformation.definition);
			for (SdaiIterator j = transformations.createIterator(); j.next();) {
				EItem_defined_transformation transformation = transformations.getCurrentMember(j);
				ARepresentation_relationship_with_transformation arrwt = getItemsToRepair(transformation, models, importer);
				if(arrwt == null){
					continue;
				}
				// Now we will really make some repairs
				ECartesian_transformation_operator cto = getCTO(transformation, models, importer);
				for(int r=1, rCount=arrwt.getMemberCount(); r<=rCount; r++){
					ERepresentation_relationship_with_transformation errwt = arrwt.getByIndex(r);
					errwt.setTransformation_operator(null, cto);
				}
			}
		}
	}
	
	private static ARepresentation_relationship_with_transformation getItemsToRepair(EItem_defined_transformation transformation, ASdaiModel models, Importer importer)throws SdaiException {
		boolean needToSearchForInverses = false;
		if((transformation.testTransform_item_1(null))&&(transformation.getTransform_item_1(null) instanceof ECartesian_transformation_operator)){
			if((transformation.testTransform_item_2(null))&&(transformation.getTransform_item_2(null) instanceof EPlacement)){			
				needToSearchForInverses = true;
			}else{
				importer.logMessage(" Unsupported combination of item_1 and item_2 for "+transformation);
			}
		}else if((transformation.testTransform_item_2(null))&&(transformation.getTransform_item_2(null) instanceof ECartesian_transformation_operator)){
			if((transformation.testTransform_item_1(null))&&(transformation.getTransform_item_1(null) instanceof EPlacement)){			
				needToSearchForInverses = true;
			}else{
				importer.logMessage(" Unsupported combination of item_1 and item_2 for "+transformation);
			}
		}
		if(needToSearchForInverses){
			ARepresentation_relationship_with_transformation result = new ARepresentation_relationship_with_transformation();
			CRepresentation_relationship_with_transformation.usedinTransformation_operator(null, transformation, models, result);
			return result;
		}
		return null;
	}

	private static ECartesian_transformation_operator getCTO(EItem_defined_transformation transformation, ASdaiModel domain, Importer importer)
		throws SdaiException {
		if((transformation.testTransform_item_1(null))&&(transformation.getTransform_item_1(null) instanceof ECartesian_transformation_operator)){
			ECartesian_transformation_operator ecto = (ECartesian_transformation_operator)transformation.getTransform_item_1(null);
			if((transformation.testTransform_item_2(null))&&(transformation.getTransform_item_2(null) instanceof EPlacement)){
				EPlacement placement = (EPlacement)transformation.getTransform_item_2(null);
				raiseErrorOrWarning(placement, transformation, importer);
				return ecto;
			}
		}
		if((transformation.testTransform_item_2(null))&&(transformation.getTransform_item_2(null) instanceof ECartesian_transformation_operator)){
			ECartesian_transformation_operator ecto = (ECartesian_transformation_operator)transformation.getTransform_item_2(null);
			if((transformation.testTransform_item_1(null))&&(transformation.getTransform_item_1(null) instanceof EPlacement)){
				EPlacement placement = (EPlacement)transformation.getTransform_item_1(null);
				raiseErrorOrWarning(placement, transformation, importer);
				return ecto;
			}
		}
		return null;
	}

	// Check if axis_placement has now transformation encoded - if so - raise ERROR, otherwise - warning
	private static void raiseErrorOrWarning(EPlacement placement, EItem_defined_transformation transformation, Importer importer)throws SdaiException {
		String ERROR_MESSAGE = "Item_definition_transformation has Axis_placment with non zero transformation AND Cartesian_transformation_operation "+transformation;  
		String WARNING_MESSAGE = "Item_definition_transformation has Axis_placment AND Cartesian_transformation_operation - transforming to geometric_relationship_with_operator_transformation"+transformation;
		// is no transformation encoded?
		if(placement instanceof EAxis2_placement_2d){
			EAxis2_placement_2d eap2d = (EAxis2_placement_2d)placement;
			if(eap2d.testRef_direction(null)){
				EDirection dir = eap2d.getRef_direction(null);
				if(dir.testDirection_ratios(null)){
					A_double ratios = dir.getDirection_ratios(null);
					double x = ratios.getByIndex(1);
					double y = ratios.getByIndex(2);
					if((Math.abs(x-1)>precision)||(Math.abs(y)>precision)){
						importer.errorMessage(ERROR_MESSAGE);
						return;
					}
				}
			}
			if(eap2d.testLocation(null)){
				ECartesian_point location = eap2d.getLocation(null);
				A_double coords = location.getCoordinates(null);
				double x = coords.getByIndex(1);
				double y = coords.getByIndex(2);
				if((Math.abs(x)>precision)||(Math.abs(y)>precision)){
					importer.errorMessage(ERROR_MESSAGE);
					return;
				}
			}
		}else if(placement instanceof EAxis2_placement_3d){
				EAxis2_placement_3d eap3d = (EAxis2_placement_3d)placement;
				if(eap3d.testRef_direction(null)){
					EDirection dir = eap3d.getRef_direction(null);
					if(dir.testDirection_ratios(null)){
						A_double ratios = dir.getDirection_ratios(null);
						double x = ratios.getByIndex(1);
						double y = ratios.getByIndex(2);
						double z = ratios.getByIndex(3);
						if((Math.abs(x-1)>precision)||(Math.abs(y)>precision)||(Math.abs(z)>precision)){
							importer.errorMessage(ERROR_MESSAGE);
							return;
						}
					}
				}
				if(eap3d.testAxis(null)){
					EDirection dir = eap3d.getAxis(null);
					if(dir.testDirection_ratios(null)){
						A_double ratios = dir.getDirection_ratios(null);
						double x = ratios.getByIndex(1);
						double y = ratios.getByIndex(2);
						double z = ratios.getByIndex(3);
						if((Math.abs(x)>precision)||(Math.abs(y)>precision)||(Math.abs(z-1)>precision)){
							importer.errorMessage(ERROR_MESSAGE);
							return;
						}
					}
				}
				if(eap3d.testLocation(null)){
					ECartesian_point location = eap3d.getLocation(null);
					A_double coords = location.getCoordinates(null);
					double x = coords.getByIndex(1);
					double y = coords.getByIndex(2);
					double z = coords.getByIndex(2);
					if((Math.abs(x)>precision)||(Math.abs(y)>precision)||(Math.abs(z)>precision)){
						importer.errorMessage(ERROR_MESSAGE);
						return;
					}
				}
			}
		importer.logMessage(WARNING_MESSAGE);			
	}
}
