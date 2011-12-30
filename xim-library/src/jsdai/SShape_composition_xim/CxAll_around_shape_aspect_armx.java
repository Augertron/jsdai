/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2011, LKSoftWare GmbH, Germany
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
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SProduct_property_definition_schema.EProduct_definition_shape;
import jsdai.SProduct_property_definition_schema.EShape_aspect;
import jsdai.SShape_aspect_definition_schema.CAll_around_shape_aspect;
import jsdai.lang.ELogical;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.EMappedXIMEntity;

public class CxAll_around_shape_aspect_armx extends CAll_around_shape_aspect_armx implements EMappedXIMEntity
{
	// From CShape_aspect.java
	// attribute (current explicit or supertype explicit) : of_shape, base type: entity product_definition_shape
/*	public static int usedinOf_shape(EShape_aspect type, EProduct_definition_shape instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testOf_shape(EShape_aspect type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_shape getOf_shape(EShape_aspect type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_shape)a2;
	}*/
	public void setOf_shape(EShape_aspect type, EProduct_definition_shape value) throws SdaiException {
		a2 = set_instanceX(a2, value);
	}
	public void unsetOf_shape(EShape_aspect type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeOf_shape(EShape_aspect type) throws SdaiException {
		return a2$;
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
	
	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(EShape_aspect type) throws SdaiException {
			return test_string(a0);
		}
		public String getName(EShape_aspect type) throws SdaiException {
			return get_string(a0);
		}*/
		public void setName(EShape_aspect type, String value) throws SdaiException {
			a0 = set_string(value);
		}
		public void unsetName(EShape_aspect type) throws SdaiException {
			a0 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(EShape_aspect type) throws SdaiException {
			return a0$;
		}
	
	// ENDOF From CShape_aspect.java

	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAll_around_shape_aspect.definition);

		setMappingConstraints(context, this);

		setElements(context, this);
		
		unsetElements(null);
		// made_up_by : SET[2:?] OF Single_datum;
//		setMade_up_by(context, this);
		
//		// clean ARM

		// made_up_by : SET[2:?] OF Single_datum;
//		unsetMade_up_by(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetElements(context, this);
			// made_up_by : SET[2:?] OF Single_datum;
//			unsetMade_up_by(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		shape_aspect
		{shape_aspect.description = 'all around'}
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EAll_around_shape_aspect_armx armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxComposite_shape_aspect_armx.setMappingConstraints(context, armEntity);
		// SELF\shape_aspect.product_definitional : LOGICAL := ?;
		if(!armEntity.testProduct_definitional(null)){
			armEntity.setProduct_definitional(null, ELogical.UNKNOWN);
		}
		
		// SELF\shape_aspect.name : label := ?;
		if(!armEntity.testName(null)){
			armEntity.setName(null, "");
		}
		
		armEntity.setDescription(null, "all around");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EAll_around_shape_aspect_armx armEntity) throws SdaiException {
		CxComposite_shape_aspect_armx.unsetMappingConstraints(context, armEntity);
		armEntity.unsetDescription(null);
	}

	public static void setElements(SdaiContext context, EComposite_shape_aspect_armx armEntity) throws SdaiException {
		CxComposite_shape_aspect_armx.setElements(context, armEntity);
	}
	
	public static void unsetElements(SdaiContext context, EComposite_shape_aspect_armx armEntity) throws SdaiException {
		CxComposite_shape_aspect_armx.unsetElements(context, armEntity);
	}	
	
	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	attribute_mapping made_up_by(made_up_by, $PATH, Single_datum);
		common_datum <= 
		composite_shape_aspect <=
		shape_aspect <-
		shape_aspect_relationship.relating_shape_aspect
		shape_aspect_relationship
		shape_aspect_relationship.related_shape_aspect ->
		shape_aspect =>
		datum
	end_attribute_mapping;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
/* Removed
	public static void setMade_up_by(SdaiContext context, ECommon_datum_armx armEntity) throws SdaiException {
		unsetMade_up_by(context, armEntity);
		if(armEntity.testMade_up_by(null)){
			ASingle_datum datums = armEntity.getMade_up_by(null);
			for(int i=1,count=datums.getMemberCount();i<=count;i++){
				ESingle_datum datum = datums.getByIndex(i);
				EShape_aspect_relationship esar = (EShape_aspect_relationship)
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				esar.setName(null, "");
				esar.setRelated_shape_aspect(null, datum);
				esar.setRelating_shape_aspect(null, armEntity);
			}
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
/*	
	public static void unsetMade_up_by(SdaiContext context,
			ECommon_datum_armx armEntity) throws SdaiException {
		AShape_aspect_relationship asar = new AShape_aspect_relationship();
		CShape_aspect_relationship.usedinRelating_shape_aspect(null, armEntity, context.domain, asar);
		for(int i=1,count=asar.getMemberCount(); i<=count; i++){
			asar.getByIndex(i).deleteApplicationInstance();
		}
	}
*/	
}