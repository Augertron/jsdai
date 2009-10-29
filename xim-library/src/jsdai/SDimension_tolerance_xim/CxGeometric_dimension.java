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

package jsdai.SDimension_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SIdentification_assignment_mim.AApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.AIdentification_item;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.EApplied_identification_assignment;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.EIdentification_role;
import jsdai.SShape_dimension_schema.CDimensional_location;
import jsdai.SShape_dimension_schema.EDimensional_location;
import jsdai.SShape_dimension_schema.EDimensional_size;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;

public class CxGeometric_dimension extends CGeometric_dimension implements EMappedXIMEntity
{
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDimensional_location.definition);

		setMappingConstraints(context, this);

	    // id : STRING;
		setId(context, this);
		
        // dimension_value : dimension_value_select;
//TODO		setDimension_value(context, this);
		
        // notes : OPTIONAL SET [1:?] OF STRING;
//TODO		setNotes(context, this);
		
		// clean ARM
	    // id : STRING;
		unsetId(null);
		
        // dimension_value : dimension_value_select;
		unsetDimension_value(null);
		
        // notes : OPTIONAL SET [1:?] OF STRING;
		unsetNotes(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
		    // id : STRING;
			unsetId(context, this);
			
	        // dimension_value : dimension_value_select;
//TODO			unsetDimension_value(context, this);
			
	        // notes : OPTIONAL SET [1:?] OF STRING;
//TODO			unsetNotes(context, this);
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
			EGeometric_dimension armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EGeometric_dimension armEntity) throws SdaiException {
	}
	
	/**
	 * Sets/creates data for id attribute.
	 * 
	 * <p>
	attribute_mapping id(id, ((shape_aspect_relationship.name))((identification_assignment.assigned_id)));
		((dimensional_location <= 
		shape_aspect_relationship 
		shape_aspect_relationship.name)  
		)((dimensional_size identification_item = dimensional_size 
		identification_item <- 
		applied_identification_assignment.items[i] 
		applied_identification_assignment <= 
		identification_assignment 
		{identification_assignment.role -> 
		identification_role 
		identification_role.name = 'size id'} 
		identification_assignment.assigned_id))
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// DL.name or DS <- AIA  
	public static void setId(SdaiContext context,
			EGeometric_dimension armEntity) throws SdaiException {
		unsetId(context, armEntity);
		if(armEntity.testId(null)){
			String id = armEntity.getId(null);
			if(armEntity instanceof EDimensional_location){
				EDimensional_location edl = (EDimensional_location)armEntity;
				edl.setName(null, id);
			}else if(armEntity instanceof EDimensional_size){
				EDimensional_size eds = (EDimensional_size)armEntity;
				EApplied_identification_assignment eaia = (EApplied_identification_assignment)
					context.working_model.createEntityInstance(CApplied_identification_assignment.definition);
				// Role
				LangUtils.Attribute_and_value_structure[] irStructure = {
						new LangUtils.Attribute_and_value_structure(
								CIdentification_role.attributeName(null), "size id")
				};
				EIdentification_role eir = (EIdentification_role) 
					LangUtils.createInstanceIfNeeded(context,
							CIdentification_role.definition, irStructure);
				eaia.setRole(null, eir);
				
				eaia.setAssigned_id(null, id);
				eaia.createItems(null).addUnordered(armEntity);
			}
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetId(SdaiContext context,
			EGeometric_dimension armEntity) throws SdaiException {
		if(armEntity instanceof EDimensional_location){
			EDimensional_location edl = (EDimensional_location)armEntity;
			edl.unsetName(null);
		}else if(armEntity instanceof EDimensional_size){
			AApplied_identification_assignment aaia = new AApplied_identification_assignment();
			CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
			for(int i=1,n=aaia.getMemberCount(); i<=n;i++){
				EApplied_identification_assignment eaia = (EApplied_identification_assignment)aaia.getByIndex(i);
				if(eaia.testRole(null)){
					EIdentification_role eir = eaia.getRole(null);
					if((eir.testName(null))&&(eir.equals("size id"))){
						AIdentification_item items = eaia.getItems(null);
						items.removeUnordered(armEntity);
						if(items.getMemberCount() == 0){
							eaia.deleteApplicationInstance();
							// TODO - not finished with code writing
						}
					}
				}
			}
		}		
	}
	
}