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

package jsdai.SProduct_class_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SIdentification_assignment_mim.AApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.AIdentification_item;
import jsdai.SIdentification_assignment_mim.CApplied_identification_assignment;
import jsdai.SIdentification_assignment_mim.EApplied_identification_assignment;
import jsdai.SManagement_resources_schema.CIdentification_role;
import jsdai.SManagement_resources_schema.EIdentification_role;
import jsdai.SProduct_class_mim.CProduct_class;

public class CxProduct_class_armx extends CProduct_class_armx implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_class.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		//********** "managed_design_object" attributes

		//********** "item_shape" attributes

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes
		//id - goes directly into AIM
		
		//additional_characterization
		setVersion_id(context, this);

		// Clean ARM specific attributes
		unsetVersion_id(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		//additional_characterization
		unsetVersion_id(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  library_defined_product_definition &lt;=
	*  externally_defined_product_definition &lt;=
	*  [product_definition]
	*  [externally_defined_item])	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EProduct_class_armx armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EProduct_class_armx armEntity) throws SdaiException
	{
	}	
	//********** "managed_design_object" attributes

	//********** "item_shape" attributes
    /**
     * Sets/creates data for version_d attribute.
     *
	attribute_mapping version_id(version_id, identification_assignment.assigned_id);
		id_for_class = product_class 
		id_for_class <* identification_item 
		identification_item <- applied_identification_assignment.items[i] 
		applied_identification_assignment <= identification_assignment 
		{identification_assignment.role -> identification_role identification_role.name = 'version'} 
		identification_assignment.assigned_id 
	end_attribute_mapping;
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
	// PC <- AIA.assigned_id
    public static void setVersion_id(SdaiContext context, EProduct_class_armx armEntity) throws SdaiException {
       unsetVersion_id(context, armEntity);
       if(armEntity.testVersion_id(null)){
    	   String value = armEntity.getVersion_id(null);
    	   // Find role
			LangUtils.Attribute_and_value_structure[] irStructure =
			{new LangUtils.Attribute_and_value_structure(
				CIdentification_role.attributeName(null),
				"version")
			};
			EIdentification_role eir = (EIdentification_role)
				LangUtils.createInstanceIfNeeded(context, CIdentification_role.definition, irStructure);
			// AIA
			EApplied_identification_assignment eaia = (EApplied_identification_assignment)
				context.working_model.createEntityInstance(CApplied_identification_assignment.definition);
			eaia.setRole(null, eir);
			eaia.createItems(null).addUnordered(armEntity);
			eaia.setAssigned_id(null, value);
       }
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetVersion_id(SdaiContext context, EProduct_class_armx armEntity) throws SdaiException {
    	AApplied_identification_assignment aaia = new AApplied_identification_assignment();
    	CApplied_identification_assignment.usedinItems(null, armEntity, context.domain, aaia);
    	for(int i=1,count=aaia.getMemberCount(); i<= count; i++){
    		EApplied_identification_assignment eaia = aaia.getByIndex(i);
    		AIdentification_item items = eaia.getItems(null);
    		items.removeUnordered(armEntity);
    		if(items.getMemberCount() == 0){
    			eaia.deleteApplicationInstance();
    		}
    	}
    }
}
