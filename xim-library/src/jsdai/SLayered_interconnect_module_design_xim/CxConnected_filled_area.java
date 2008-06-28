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
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SLayered_interconnect_module_design_mim.CStratum_feature;
import jsdai.SProduct_property_definition_schema.*;

public class CxConnected_filled_area extends CConnected_filled_area implements EMappedXIMEntity{

	// From CShape_aspect.java
	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a1$;
	}
	
	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a3);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a3);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a3 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a3 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a3$;
	}
	// ENDOF From CShape_aspect.java


	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CStratum_feature.definition);

		setMappingConstraints(context, this);
		
		// feature_of_size
		setFeature_of_size(context, this);
		
		// implementation_requirement 
		setImplementation_requirement(context, this);
		
		// Clean ARM
		// setFeature_of_size(context, this);
		unsetFeature_of_size(null);

		// implementation_requirement 
		unsetImplementation_requirement(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Feature_of_size
			unsetFeature_of_size(context, this);
			
			// implementation_requirement 
			unsetImplementation_requirement(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  stratum_feature &lt;=
	 *  shape_aspect
	 *  {shape_aspect
	 *  (shape_aspect.description = 'conductive filled area')
	 *  (shape_aspect.description = 'connected filled area')}
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
			EConnected_filled_area armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxConductive_filled_area.setMappingConstraints(context, armEntity);
		armEntity.setDescription(null, "connected filled area");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EConnected_filled_area armEntity) throws SdaiException {
		CxConductive_filled_area.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}

	//********** "stratum_feature" attributes

	/**
	* Sets/creates data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* // SF <- PD <- PDR -> R -> DRI
	*/
	public static void setFeature_of_size(SdaiContext context, EStratum_feature_armx armEntity) throws SdaiException
	{
		CxStratum_feature_armx.setFeature_of_size(context, armEntity);		
	}


	/**
	* Unsets/deletes data for feature_of_size attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetFeature_of_size(SdaiContext context, EStratum_feature_armx armEntity) throws SdaiException
	{
		CxStratum_feature_armx.unsetFeature_of_size(context, armEntity);		
	}
	

   //**********connected_filled_area attributes
    /**
     * Sets/creates data for implementation_requirement attribute.
     * @param context SdaiContext.
     * @param armEntity arm entity.
     *
     *  attribute_mapping implementation_requirement_intra_stratum_join_relationship (implementation_requirement
     * , (*PATH*), intra_stratum_join_relationship);
     * 	stratum_feature <=
     * 	shape_aspect <-
     * 	shape_aspect_relationship.related_shape_aspect
     * 	shape_aspect_relationship
     * 	{shape_aspect_relationship
     * 	shape_aspect_relationship.name = `join implementation'}
     * 	shape_aspect_relationship.relating_shape_aspect ->
     * 	{shape_aspect
     * 	shape_aspect.name = `intra stratum join'}
     * 	shape_aspect =>
     * 	join_shape_aspect
     *  end_attribute_mapping;
     *
     */
    // SF <- SAR -> JSA
    public static void setImplementation_requirement(SdaiContext context, EConnected_filled_area armEntity) throws
       SdaiException {
       unsetImplementation_requirement(context, armEntity);
       if (armEntity.testImplementation_requirement(null)) {
			AIntra_stratum_join_relationship armImplementation_requirements = armEntity.getImplementation_requirement(null);
			SdaiIterator iter = armImplementation_requirements.createIterator();
			while(iter.next()){
				EIntra_stratum_join_relationship armImplementation_requirement = armImplementation_requirements.getCurrentMember(iter); 
				// Create SAR
				EShape_aspect_relationship sar = (EShape_aspect_relationship)
		             context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
		        sar.setName(null, "join implementation");
		        // SAR -> JSA
		        sar.setRelating_shape_aspect(null, armImplementation_requirement);
		        // SF <- SAR
		        sar.setRelated_shape_aspect(null, armEntity);
			}
       }
    }

  public static void unsetImplementation_requirement(SdaiContext context, EConnected_filled_area armEntity) throws
     SdaiException {
     AShape_aspect_relationship asar = new AShape_aspect_relationship();
     CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity,
                                                           context.domain, asar);
     for (int i = 1; i <= asar.getMemberCount(); ) {
        EShape_aspect_relationship esar = asar.getByIndex(i);
        if (esar.getName(null).equals("join implementation")) {
           asar.removeByIndex(i);
           esar.deleteApplicationInstance();
        }
        else {
           i++;
        }
     }
  }
	

}