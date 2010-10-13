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

package jsdai.SShape_composition_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.AShape_aspect;
import jsdai.SProduct_property_definition_schema.AShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.CShape_aspect_relationship;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SProduct_property_definition_schema.EShape_aspect_relationship;
import jsdai.SShape_aspect_definition_schema.CComposite_shape_aspect;

public class CxComposite_shape_aspect_armx extends CComposite_shape_aspect_armx implements EMappedXIMEntity{

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

		setTemp("AIM", CComposite_shape_aspect.definition);

		setMappingConstraints(context, this);
		
		setElements(context, this);
		
		unsetElements(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			unsetElements(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  !{composite_shape_aspect &lt;=
	 *  shape_aspect &lt;-
	 *  shape_aspect_relationship.relating_shape_aspect
	 *  shape_aspect_relationship
	 *  {shape_aspect_relationship
	 *  shape_aspect_relationship.name = 'composing'}}	 
	 * end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// Constraint is repeating what is guaranteed via inverse anyway
	public static void setMappingConstraints(SdaiContext context,
			EComposite_shape_aspect_armx armEntity) throws SdaiException {
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
			EComposite_shape_aspect_armx armEntity) throws SdaiException {
	}

	public static void setElements(SdaiContext context, 
			EComposite_shape_aspect_armx armEntity) throws SdaiException {
		unsetElements(context, armEntity);
		if(armEntity.testElements(null)){
			AShape_aspect asa = armEntity.getElements(null);
			for(int i=1,count=asa.getMemberCount();i<=count;i++){
				EShape_aspect esa = asa.getByIndex(i);
				EShape_aspect_relationship esar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				esar.setRelating_shape_aspect(null, armEntity);
				esar.setName(null, "");
				esar.setRelated_shape_aspect(null, esa);
			}
		}
	}

	public static void unsetElements(SdaiContext context,
			EComposite_shape_aspect_armx armEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount();i<=count;i++){
			asar.getByIndex(i).deleteApplicationInstance();
		}
	}
}