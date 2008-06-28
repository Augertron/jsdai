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

package jsdai.SShape_feature_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SShape_feature_mim.CPlaced_feature;
import jsdai.SMaterial_property_definition_schema.*;
import jsdai.SProduct_property_definition_schema.*;

public class CxPlaced_feature_armx extends CPlaced_feature_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CPlaced_feature.definition);

		setMappingConstraints(context, this);
		
		setDefinition(context, this);
		
		// clean ARM
		unsetDefinition(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

		unsetDefinition(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  component_feature &lt;=
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
			EPlaced_feature_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_feature_occurrence.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EPlaced_feature_armx armEntity) throws SdaiException {
		CxShape_feature_occurrence.unsetMappingConstraints(context, armEntity);
	}

	// Placed_feature
	/**
	 * Sets/creates data for attribute definition.
	 * 
	 * placed_feature <=
		shape_aspect
		shape_definition = shape_aspect
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition 
		{property_definition_relationship.name = 'definition'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition = characterized_object
		characterized_object =>
		feature_definition
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// PF <- PD <- PDR -> PD -> FD
	public static void setDefinition(SdaiContext context,
			EPlaced_feature_armx armEntity) throws SdaiException {
		unsetDefinition(context, armEntity);
		if(armEntity.testDefinition(null)){
         // PF <- PD
         LangUtils.Attribute_and_value_structure[] pdStructure1 = {
            new LangUtils.Attribute_and_value_structure(
            CProperty_definition.attributeDefinition(null),
				  armEntity)
         };
         EProperty_definition epd1 = (EProperty_definition)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure1);
         if (!epd1.testName(null)) {
            epd1.setName(null, "");
         }
         // FD
         EShape_feature_definition_armx efd = armEntity.getDefinition(null);
         // PD -> FD
         LangUtils.Attribute_and_value_structure[] pdStructure2 = {
            new LangUtils.Attribute_and_value_structure(
            CProperty_definition.attributeDefinition(null),
				  efd)
         };
         EProperty_definition epd2 = (EProperty_definition)
            LangUtils.createInstanceIfNeeded(context, CProperty_definition.definition, pdStructure2);
         if (!epd2.testName(null)) {
            epd2.setName(null, "");
         }
         // PDR
         EProperty_definition_relationship epdr = (EProperty_definition_relationship)
				context.working_model.createEntityInstance(CProperty_definition_relationship.definition);
         epdr.setName(null, "definition");
         epdr.setRelated_property_definition(null, epd1);
         epdr.setRelating_property_definition(null, epd2);
         epdr.setDescription(null, "");
         // System.err.println(" SETTING definition "+epdr);
		}
	}

	/**
	 * Unsets/deletes mapping for attribute definition.
	 * placed_feature <=
		shape_aspect
		shape_definition = shape_aspect
		characterized_definition = shape_definition
		characterized_definition <-
		property_definition.definition
		property_definition <-
		property_definition_relationship.related_property_definition 
		{property_definition_relationship.name = 'definition'}
		property_definition_relationship.relating_property_definition ->
		property_definition
		property_definition.definition ->
		characterized_definition = characterized_object
		characterized_object =>
		feature_definition
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	// PF <- PD <- PDR -> PD -> FD
	public static void unsetDefinition(SdaiContext context,
			EPlaced_feature_armx armEntity) throws SdaiException {
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int i=1,count=apd.getMemberCount();i<=count;i++){
			EProperty_definition epd = apd.getByIndex(i);
			AProperty_definition_relationship apdr = new AProperty_definition_relationship();
			CProperty_definition_relationship.usedinRelated_property_definition(null, epd, context.domain, apdr);
			for(int j=1,count2=apdr.getMemberCount();j<=count2;j++){
				EProperty_definition_relationship epdr = apdr.getByIndex(j);
				if(epdr.getName(null).equals("definition"))
					epdr.deleteApplicationInstance();
			}			
		}
	}
	
	
}