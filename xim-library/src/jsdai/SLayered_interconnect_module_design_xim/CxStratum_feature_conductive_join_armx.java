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
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SInterconnect_module_connection_routing_mim.*;
import jsdai.SLayered_interconnect_module_design_mim.CStratum_feature_conductive_join;
import jsdai.SProduct_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
public class CxStratum_feature_conductive_join_armx
		extends
			CStratum_feature_conductive_join_armx implements EMappedXIMEntity{

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
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
			setTemp("AIM", CStratum_feature_conductive_join.definition);

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
	 *  stratum_feature_conductive_join &lt;=
	 *  product_definition_relationship 
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
			EStratum_feature_conductive_join_armx armEntity) throws SdaiException {
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
			EStratum_feature_conductive_join_armx armEntity) throws SdaiException {
	}


	//********** "stratum_concept_relationship_armx" attributes

	/**
	* Sets/creates data for resulting_shape attribute.
	*  <aa attribute="resulting_shape" assertion_to="Geometric_model">
          <aimelt xml:space="preserve">PATH</aimelt>
                <refpath xml:space="preserve">stratum_feature_conductive_join &lt;=
						product_definition_relationship
						characterized_product_definition = product_definition_relationship
						characterized_product_definition
						characterized_definition = characterized_product_definition
						characterized_definition &lt;-
						property_definition.definition
						property_definition &lt;-
						property_definition_representation.definition
						property_definition_representation
						property_definition_representation.used_representation -&gt;
						representation
					</refpath>
            </aa>
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// SFCJ <- PDR -> R
	public static void setResulting_shape(SdaiContext context, EStratum_feature_conductive_join_armx armEntity) throws SdaiException
	{
		unsetResulting_shape(context, armEntity);
		if(armEntity.testResulting_shape(null)){
			EShape_representation shape = armEntity.getResulting_shape(null);
			// PDR
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setDefinition(null, armEntity);
			epdr.setUsed_representation(null, shape);
		}
	}


	/**
	* Unsets/deletes data for setTopological_requirement attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetResulting_shape(SdaiContext context, EStratum_feature_conductive_join_armx armEntity) throws SdaiException
	{
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinDefinition(null, armEntity, context.domain, apdr);
		SdaiIterator iter = apdr.createIterator();
		while(iter.next()){
			EProperty_definition_representation epdr = apdr.getCurrentMember(iter);
			if(epdr.getUsed_representation(null) instanceof EShape_representation)
				epdr.deleteApplicationInstance();
		}
	}
	
	
}