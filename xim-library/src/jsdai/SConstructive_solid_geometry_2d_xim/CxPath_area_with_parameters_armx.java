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

package jsdai.SConstructive_solid_geometry_2d_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SConstructive_solid_geometry_2d_mim.CPath_area_with_parameters;
import jsdai.SMeasure_schema.ELength_measure_with_unit;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.ARepresentation_map;
import jsdai.SRepresentation_schema.CRepresentation_map;
import jsdai.SRepresentation_schema.EMapped_item;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SRepresentation_schema.ERepresentation_map;
import jsdai.SMixed_complex_types.*;

public class CxPath_area_with_parameters_armx extends CPath_area_with_parameters_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;
	
	private static Object parametersMap = new Object();


	// Taken from Mapped_item
	// attribute (current explicit or supertype explicit) : mapping_source, base type: entity representation_map
/*	public static int usedinMapping_source(EMapped_item type, ERepresentation_map instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testMapping_source(EMapped_item type) throws SdaiException {
		return test_instance(a1);
	}
	public ERepresentation_map getMapping_source(EMapped_item type) throws SdaiException {
		a1 = get_instance(a1);
		return (ERepresentation_map)a1;
	}*/
	public void setMapping_source(EMapped_item type, ERepresentation_map value) throws SdaiException {
		a1 = set_instanceX(a1, value);
	}
	public void unsetMapping_source(EMapped_item type) throws SdaiException {
		a1 = unset_instance(a1);
	}
	public static jsdai.dictionary.EAttribute attributeMapping_source(EMapped_item type) throws SdaiException {
		return a1$;
	}
	// ENDOF Taken from Mapped_item

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPath_area_with_parameters.definition);

		setMappingConstraints(context, this);
		
	    setParameters(context, this);

	    unsetParameters(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetParameters(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
	}

	/**
	* Sets/creates data for parameters constraints.
	*
	* <p>
		attribute_mapping parameters(parameters, curve_style_parameters);
			path_area_with_parameters <=
			mapped_item
			mapped_item.mapping_source ->
			representation_map
			representation_map.mapped_representation ->
			representation =>
			curve_style_parameters_representation =>
			curve_style_parameters
		end_attribute_mapping;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setParameters(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
		unsetParameters(context, armEntity);
		if(armEntity.testParameters(null)){
			ECurve_style_parameters parameters = armEntity.getParameters(null);
			ERepresentation_map erm = (ERepresentation_map)parameters.getTemp(parametersMap);
			if(erm == null){
				ARepresentation_map rep_maps = new ARepresentation_map(); 
				CRepresentation_map.usedinMapped_representation(null, parameters, context.domain, rep_maps);
				if(rep_maps.getMemberCount() > 0){
					erm = rep_maps.getByIndex(1);
				}else{
					erm = (ERepresentation_map)
						context.working_model.createEntityInstance(CRepresentation_map.definition);
					erm.setMapped_representation(null, parameters);
				}
				parameters.setTemp(parametersMap, erm);
			}
			armEntity.setMapping_source(null, erm);
	        if (erm.testMapping_origin(null))
	        	return;
	        // AIM gaps
        	// According recommendation from SEDS-2254
	        ERepresentation_item item = null;
	        // ARM is still present, Cx class is not invoked yet
	        if(parameters.testCurve_width(null)){
	        	ELength_measure_with_unit curveWidth = parameters.getCurve_width(null);
	        	if(!(curveWidth instanceof ERepresentation_item)){
	        		item = (ERepresentation_item)
	        			context.working_model.substituteInstance(curveWidth, CLength_measure_with_unit$measure_representation_item.definition);
	        	}else{
	        		item = (ERepresentation_item)curveWidth;
	        	}
	        // ARM is cleaned need to go via AIM	
	        }else{
	        	ARepresentation_item items = CxCurve_style_parameters.getItems(parameters);
	        	SdaiIterator iter = items.createIterator();
	        	while(iter.next()){
	        		ERepresentation_item temp = items.getCurrentMember(iter);
	        		if((temp.testName(null))&&(temp.getName(null).equals("curve width"))){
	        			item = temp;
	        			break;
	        		}
	        	}
	        }
        	erm.setMapping_origin(null, item);
			
		}
	}

	public static void unsetParameters(SdaiContext context, EPath_area_with_parameters_armx armEntity) throws SdaiException
	{
		armEntity.unsetMapping_source(null);
	}

	
}
