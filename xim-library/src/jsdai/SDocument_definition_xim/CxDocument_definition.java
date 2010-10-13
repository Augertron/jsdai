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

package jsdai.SDocument_definition_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.util.LangUtils;
import jsdai.SApplication_context_schema.CApplication_context_element;
import jsdai.SApplication_context_schema.CProduct_definition_context;
import jsdai.SApplication_context_schema.EProduct_definition_context;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_view_definition_xim.*;

public class CxDocument_definition extends CDocument_definition implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

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
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct_definition.definition);

		setMappingConstraints(context, this);

		// SETTING DERIVED
		// setDefinition(null, this);

		//********** "managed_design_object" attributes

		//********** "item_shape" attributes

		// Clean ARM specific attributes
		
		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		setAdditional_characterization(context, this);

		//additional_context
		setAdditional_contexts(context, this);

		//description_x
		setDescription_x(context, this);

		// Clean ARM specific attributes
		unsetAdditional_characterization(null);
		unsetAdditional_contexts(null);
		unsetDescription_x(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		// unsetDefinition(null);

		//********** "managed_design_object" attributes


		//	********** "product_view_definition" attributes

		//id - goes directly into AIM
		
		//additional_characterization
		unsetAdditional_characterization(context, this);

		//additional_context
		unsetAdditional_contexts(context, this);

		//description_x
		unsetDescription_x(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  {product_definition
	*  product_definition.frame_of_reference -&gt;
	*  product_definition_context &lt;=
	*  application_context_element 
	*  (application_context_element.name = 'digital document definition') 
	*  (application_context_element.name = 'physical document definition')
	*  (application_context_element.name = 'generic document definition')}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EDocument_definition armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		String name = "generic document definition";
		CxProduct_view_definition.setMappingConstraints(context, armEntity);
		// Check - maybe we do have the correct context already
		if(armEntity.testFrame_of_reference(null)){
			EProduct_definition_context epdc = armEntity.getFrame_of_reference(null);
			if(epdc.testName(null)){
				if(epdc.getName(null).equals(name)){
					return;
				}
			}else{
				epdc.setName(null, name);
				return;
			}
		}
		
// None of subtype constraints should be set here - SEDS written		
      LangUtils.Attribute_and_value_structure[] pdcStructure = {
            new LangUtils.Attribute_and_value_structure(
            CApplication_context_element.attributeName(null), name)
         };
         EProduct_definition_context epdc = (EProduct_definition_context)
            LangUtils.createInstanceIfNeeded(context,
            CProduct_definition_context.definition, pdcStructure);
		
		armEntity.setFrame_of_reference(null, epdc);
		
	}

	public static void unsetMappingConstraints(SdaiContext context, EDocument_definition armEntity) throws SdaiException
	{
		CxProduct_view_definition.unsetMappingConstraints(context, armEntity);
		
		// armEntity.unsetFrame_of_reference(null);
	}	
	//********** "managed_design_object" attributes

	//********** "item_shape" attributes
    /**
     * Sets/creates data for Id_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
/* Removed from XIM - see bug #3610	
    public static void setId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
       CxItem_shape.setId_x(context, armEntity);
    }
*/
  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
/* Removed from XIM - see bug #3610	
    public static void unsetId_x(SdaiContext context, EItem_shape armEntity) throws SdaiException {
      CxItem_shape.unsetId_x(context, armEntity);
   }
*/
 	//********** "product_view_definition" attributes
    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_characterization(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_characterization(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_characterization(context, armEntity);
   }

    /**
     * Sets/creates data for name_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
       CxProduct_view_definition.setAdditional_contexts(context, armEntity);
    }

  /**
   * Unsets/deletes data for Id_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetAdditional_contexts(SdaiContext context, EProduct_view_definition armEntity) throws SdaiException {
      CxProduct_view_definition.unsetAdditional_contexts(context, armEntity);
   }

    /**
     * Sets/creates data for description_x attribute.
     *
     * @param context SdaiContext.
     * @param armEntity arm entity.
     * @throws SdaiException
     */
    public static void setDescription_x(SdaiContext context, EDocument_definition armEntity) throws SdaiException {
    	unsetDescription_x(context, armEntity);
    	if(armEntity.testDescription_x(null)){
    		String value = armEntity.getDescription_x(null);
    		armEntity.setDescription(null, value);
    	}
    }

  /**
   * Unsets/deletes data for description_x attribute.
   *
   * @param context SdaiContext.
   * @param armEntity arm entity.
   * @throws SdaiException
   */
    public static void unsetDescription_x(SdaiContext context, EDocument_definition armEntity) throws SdaiException {
      armEntity.unsetDescription(null);
   }
    
}
