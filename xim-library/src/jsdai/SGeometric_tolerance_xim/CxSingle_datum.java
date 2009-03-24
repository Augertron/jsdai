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

package jsdai.SGeometric_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeometric_tolerance_xim.CSingle_datum;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_aspect_definition_schema.AReferenced_modified_datum;
import jsdai.SShape_aspect_definition_schema.CDatum;
import jsdai.SShape_aspect_definition_schema.CReferenced_modified_datum;
import jsdai.SShape_aspect_definition_schema.EDatum;
import jsdai.SShape_aspect_definition_schema.EReferenced_modified_datum;

public class CxSingle_datum extends CSingle_datum implements EMappedXIMEntity
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
		a2 = set_instance(a2, value);
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
	// ENDOF From CShape_aspect.java

	// DATUM
	/// methods for attribute: identification, base type: STRING
/*	public boolean testIdentification(EDatum type) throws SdaiException {
		return test_string(a4);
	}
	public String getIdentification(EDatum type) throws SdaiException {
		return get_string(a4);
	}*/
	public void setIdentification(EDatum type, String value) throws SdaiException {
		a4 = set_string(value);
	}
	public void unsetIdentification(EDatum type) throws SdaiException {
		a4 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeIdentification(EDatum type) throws SdaiException {
		return a4$;
	}
	// ENDOF DATUM
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CDatum.definition);

		setMappingConstraints(context, this);

		setModification(context, this);
		// clean ARM
		unsetModification(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetModification(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		physical_unit_datum <=
		shape_aspect
		{shape_aspect
		shape_aspect.description = 'plane'}
	end_mapping_constraints;	
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
		ESingle_datum armEntity) throws SdaiException {
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
			ESingle_datum armEntity) throws SdaiException {
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	attribute_mapping modification(modification, $PATH, limit_condition);
		datum <- 
		datum_reference.referenced_datum 
		datum_reference 
		datum_reference => 
		referenced_modified_datum 
		referenced_modified_datum.modifier ->
		limit_condition
		(limit_condition = .MAXIMUM_MATERIAL_CONDITION.)
		(limit_condition = .LEAST_MATERIAL_CONDITION.)
		(limit_condition = .REGARDLESS_OF_FEATURE_SIZE.)
	end_attribute_mapping;

	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setModification(SdaiContext context,
		ESingle_datum armEntity) throws SdaiException {
		unsetModification(context, armEntity);
		if(armEntity.testModification(null)){
			int modification = armEntity.getModification(null);
			EReferenced_modified_datum ermd = (EReferenced_modified_datum)
				context.working_model.createEntityInstance(CReferenced_modified_datum.definition);
			ermd.setReferenced_datum(null, armEntity);
			ermd.setModifier(null, modification);
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetModification(SdaiContext context,
			ESingle_datum armEntity) throws SdaiException {
		AReferenced_modified_datum armd = new AReferenced_modified_datum();
		CReferenced_modified_datum.usedinReferenced_datum(null, armEntity, context.domain, armd);
		for(int i=1,count=armd.getMemberCount(); i<=count; i++){
			armd.getByIndex(i).deleteApplicationInstance();
		}
	}
	
}