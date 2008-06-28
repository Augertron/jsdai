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
import jsdai.SShape_feature_mim.CShape_feature_definition;

public class CxShape_feature_definition_armx extends CShape_feature_definition_armx implements EMappedXIMEntity{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_feature_definition.definition);

		setMappingConstraints(context, this);
		
		// Made derived
		// setFeature_model(context, this);
		
		// Clean ARM
		// unsetFeature_model(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// unsetFeature_model(context, this);

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
			EShape_feature_definition_armx armEntity) throws SdaiException {
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
			EShape_feature_definition_armx armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for feature_model model.
	 * 	
	 * <p>
	 attribute_mapping feature_model(feature_model, $PATH, shape_representation);
		 feature_definition <=
		 characterized_object
		 characterized_definition = characterized_object
		 characterized_definition <-
		 property_definition.definition
		 property_definition <-
		 property_definition_representation.definition
		 property_definition_representation
		 property_definition_representation.used_representation ->
		 representation =>
		 shape_representation

	 end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Made derived	
	public static void setFeature_model(SdaiContext context,
			EFeature_definition_armx armEntity) throws SdaiException {
		unsetFeature_model(context, armEntity);
		if(armEntity.testFeature_model(null)){
			EShape_representation esr = armEntity.getFeature_model(null);
			// PD
         LangUtils.Attribute_and_value_structure[] pdStructure = {
               new LangUtils.Attribute_and_value_structure(CProperty_definition.attributeDefinition(null), 
               		armEntity)
            };

            EProperty_definition property_definition = (EProperty_definition) LangUtils.createInstanceIfNeeded(
               context,
               CProduct_definition_shape.definition,
               pdStructure);

            if (!property_definition.testName(null)) {
               property_definition.setName(null, "");
            }
			// PDR
         EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
         epdr.setDefinition(null, property_definition);
         epdr.setUsed_representation(null, esr);
		}
	}
*/
	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
/* Made derived	
	public static void unsetFeature_model(SdaiContext context,
			EFeature_definition_armx armEntity) throws SdaiException {
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		SdaiIterator iterPD = apd.createIterator();
		while(iterPD.next()){
			EProperty_definition epd = apd.getCurrentMember(iterPD);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			SdaiIterator iterPDR = apdr.createIterator();
			while(iterPDR.next()){
				EProperty_definition_representation epdr = apdr.getCurrentMember(iterPDR);
				if((epdr.testUsed_representation(null))&&(epdr.getUsed_representation(null) instanceof EShape_representation))
					epdr.deleteApplicationInstance();
			}
		}
	}
*/
}