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
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SLayered_interconnect_module_design_mim.CStratum_feature;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_property_representation_schema.*;
import jsdai.SQualified_measure_schema.*;
import jsdai.SRepresentation_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element;

public class CxStratum_feature_armx extends CStratum_feature_armx implements EMappedXIMEntity{

	// From CShape_aspect.java
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
		
		// Clean ARM
		// setFeature_of_size(context, this);
		unsetFeature_of_size(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Feature_of_size
			unsetFeature_of_size(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  stratum_feature &lt;=
	 *  shape_aspect
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
			EStratum_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_element.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EStratum_feature_armx armEntity) throws SdaiException {
		CxShape_element.unsetMappingConstraints(context, armEntity);
	}

	//********** "stratum_feature" attributes

	/**
	* Sets/creates data for feature_of_size attribute.
	*
	*
	*  attribute_mapping feature_of_size (feature_of_size
	* , descriptive_representation_item);
	* 	stratum_feature <=
	* 	shape_aspect
	* 	shape_definition = shape_aspect
	* 	shape_definition
	* 	characterized_definition = shape_definition
	* 	characterized_definition <-
	* 	property_definition.definition
	* 	property_definition <-
	* 	property_definition_representation.definition
	* 	property_definition_representation
	* 	property_definition_representation.used_representation ->
	* 	representation
	* 	representation.items[i] ->
	* 	{representation_item
	* 	representation_item.name = `feature of size'}
	* 	representation_item =>
	* 	descriptive_representation_item
	* 	{descriptive_representation_item
	* 	(descriptive_representation_item.description = `true')
	* 	(descriptive_representation_item.description = `false')}
	*  end_attribute_mapping;
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	* // SF <- PD <- PDR -> R -> DRI
	*/
	public static void setFeature_of_size(SdaiContext context, EStratum_feature_armx armEntity) throws SdaiException
	{
		//unset old values
		unsetFeature_of_size(context, armEntity);

		if (armEntity.testFeature_of_size(null))
		{
			boolean armFeature_of_size = armEntity.getFeature_of_size(null);

			//property_definition
			EProperty_definition property_definition = null;
			AProperty_definition aProperty_definition = new AProperty_definition();
			CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);
			if (aProperty_definition.getMemberCount() > 0) {
			   property_definition = aProperty_definition.getByIndex(1);
			} else {
				property_definition = CxAP210ARMUtilities.createProperty_definition(context, null, "", "", armEntity, true);
			}

			//property_definition_representation
			AProperty_definition_representation aPdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aPdr);

			EProperty_definition_representation property_definition_representation = null;
			jsdai.SRepresentation_schema.ERepresentation representation = null;
			boolean found = false;
			for (int i = 1; i <= aPdr.getMemberCount(); i++) {
				property_definition_representation = aPdr.getByIndex(i);
				if (property_definition_representation.testUsed_representation(null)) {
					 representation = property_definition_representation.getUsed_representation(null);
					 found = true;
					 break;
				}
			}
			if (!found) {
				representation = CxAP210ARMUtilities.createRepresentation(context, "", false);
				representation.setName(null, "");
				//EProperty_definition_representation
				property_definition_representation = (EProperty_definition_representation) context.working_model.createEntityInstance(EProperty_definition_representation.class);
				property_definition_representation.setDefinition(null, property_definition);
				property_definition_representation.setUsed_representation(null, representation);
			}

			if (!representation.testItems(null)) {
				representation.createItems(null);
			}
         String keyword;
			if(armFeature_of_size)
				keyword = "true";
			else
				keyword = "false";

         String keyword2 = "dri_feature_of_size_"+keyword;
         Object temp = CxAP210ARMUtilities.mostlyUsedInstances.get(keyword2);
         jsdai.SQualified_measure_schema.EDescriptive_representation_item edri;
         if(temp == null){
            edri = (jsdai.SQualified_measure_schema.EDescriptive_representation_item)
            	context.working_model.createEntityInstance(jsdai.SQualified_measure_schema.CDescriptive_representation_item.definition);
            edri.setName(null, "feature of size");
            edri.setDescription(null, keyword);
            CxAP210ARMUtilities.mostlyUsedInstances.put(keyword2, edri);
         }
         else{
            edri = (EDescriptive_representation_item)temp;
         }

			representation.getItems(null).addUnordered(edri);
		}
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
		EProperty_definition property_definition = null;
		AProperty_definition aProperty_definition = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, aProperty_definition);

		for (int i = 1; i <= aProperty_definition.getMemberCount(); i++) {
			property_definition = aProperty_definition.getByIndex(i);

			//property_definition_representation
			AProperty_definition_representation aPdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, property_definition, context.domain, aPdr);

			EProperty_definition_representation property_definition_representation = null;
			jsdai.SRepresentation_schema.ERepresentation representation = null;
			boolean found = false;
			for (int j = 1; j <= aPdr.getMemberCount(); j++) {
				property_definition_representation = aPdr.getByIndex(j);
				if (property_definition_representation.testUsed_representation(null)) {
					representation = property_definition_representation.getUsed_representation(null);
					if (representation.testItems(null)) {
						ARepresentation_item items = representation.getItems(null);
						ERepresentation_item item = null;

						//EDescriptive_representation_item
						int k = 1;
						while (k <= items.getMemberCount()) {
							item = items.getByIndex(k);
							if (item instanceof EDescriptive_representation_item) {
								EDescriptive_representation_item drItem = (EDescriptive_representation_item) item;
								if (drItem.testName(null) && drItem.getName(null).equals("feature of size")) {
									items.removeUnordered(drItem);
									if (CxAP210ARMUtilities.countEntityUsers(context, drItem) == 0) {
										item.deleteApplicationInstance();
									}
								} else {
									k++;
								}
							} else {
							  k++;
							}
						}
                  // REPRESENTATION
                  if(representation.getItems(null).getMemberCount() == 0){
                     // The only usage is PDR
                     if (CxAP210ARMUtilities.countEntityUsers(context, representation) == 1) {
                        representation.deleteApplicationInstance();
                        property_definition_representation.deleteApplicationInstance();
                     }
                  }

					}
				 }
			}
		}
	}
	

}