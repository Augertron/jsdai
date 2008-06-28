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

package jsdai.SInterconnect_module_connection_routing_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SLayered_interconnect_module_design_mim.CPlated_inter_stratum_feature;
import jsdai.SProduct_definition_schema.EProduct_definition_relationship;
import jsdai.SProduct_property_definition_schema.*;

public class CxPhysical_network_supporting_stratum_feature_conductive_join
		extends
			CPhysical_network_supporting_stratum_feature_conductive_join implements EMappedXIMEntity{

	// Product_definition_relationship attributes
	
	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition_relationship type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition_relationship type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition_relationship type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setName(EProduct_definition_relationship type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EProduct_definition_relationship type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EProduct_definition_relationship type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition_relationship type) throws SdaiException {
		return test_string(a2);
	}
	public String getDescription(EProduct_definition_relationship type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setDescription(EProduct_definition_relationship type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetDescription(EProduct_definition_relationship type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition_relationship type) throws SdaiException {
		return a2$;
	}

//////////////////	
	
	// Shape_aspect attributes
	
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EShape_aspect type) throws SdaiException {
		return test_string(a6);
	}
	public String getName(EShape_aspect type) throws SdaiException {
		return get_string(a6);
	}*/
	public void setName(EShape_aspect type, String value) throws SdaiException {
		a6 = set_string(value);
	}
	public void unsetName(EShape_aspect type) throws SdaiException {
		a6 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
		return a6$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EShape_aspect type) throws SdaiException {
		return test_string(a7);
	}
	public String getDescription(EShape_aspect type) throws SdaiException {
		return get_string(a7);
	}*/
	public void setDescription(EShape_aspect type, String value) throws SdaiException {
		a7 = set_string(value);
	}
	public void unsetDescription(EShape_aspect type) throws SdaiException {
		a7 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EShape_aspect type) throws SdaiException {
		return a7$;
	}

	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a8$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a8);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a8 = get_instance(a8);
		return (EProduct_definition_shape)a8;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a8 = set_instance(a8, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a8 = unset_instance(a8);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a8$;
	}

	/// methods for attribute: product_definitional, base type: LOGICAL
/*	public boolean testProduct_definitional(EShape_aspect type) throws SdaiException {
		return test_logical(a9);
	}
	public int getProduct_definitional(EShape_aspect type) throws SdaiException {
		return get_logical(a9);
	}*/
	public void setProduct_definitional(EShape_aspect type, int value) throws SdaiException {
		a9 = set_logical(value);
	}
	public void unsetProduct_definitional(EShape_aspect type) throws SdaiException {
		a9 = unset_logical();
	}
	public static jsdai.dictionary.EAttribute attributeProduct_definitional(EShape_aspect type) throws SdaiException {
		return a9$;
	}

	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CPlated_inter_stratum_feature.definition);

			setMappingConstraints(context, this);
			
			// resulting_shape 
			setResulting_shape(context, this);
			
			// Clean ARM
			unsetResulting_shape(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// resulting_shape 
			unsetResulting_shape(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints;
	 *  stratum_concept_relationship &lt;=
	 *  [shape_aspect
	 *  {shape_aspect
	 *  shape_aspect.description = 'physical net supporting stratum feature conductive join'}]
	 *  [shape_aspect_relationship
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'stratum feature conductive join'}] 
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// Do nothing as it is abstract
	public static void setMappingConstraints(SdaiContext context,
			EPhysical_network_supporting_stratum_feature_conductive_join armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxStratum_feature_conductive_join_armx.setMappingConstraints(context, armEntity);
		CxInter_stratum_join_implementation.setMappingConstraints(context, armEntity);
		
		armEntity.setDescription((EShape_aspect)null, "physical net supporting stratum feature conductive join");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPhysical_network_supporting_stratum_feature_conductive_join armEntity) throws SdaiException {
		CxStratum_feature_conductive_join_armx.unsetMappingConstraints(context, armEntity);
		CxInter_stratum_join_implementation.unsetMappingConstraints(context, armEntity);

		armEntity.unsetDescription((EShape_aspect)null);		
	}


	//********** "stratum_concept_relationship_armx" attributes

	/**
	* Sets/creates data for setTopological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setResulting_shape(SdaiContext context, EPhysical_network_supporting_stratum_feature_conductive_join armEntity) throws SdaiException
	{
		CxStratum_feature_conductive_join_armx.setResulting_shape(context, armEntity);		
	}


	/**
	* Unsets/deletes data for setTopological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetResulting_shape(SdaiContext context, EPhysical_network_supporting_stratum_feature_conductive_join armEntity) throws SdaiException
	{
		CxStratum_feature_conductive_join_armx.unsetResulting_shape(context, armEntity);
	}
	
	
}