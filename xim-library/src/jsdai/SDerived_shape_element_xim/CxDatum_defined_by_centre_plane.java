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

package jsdai.SDerived_shape_element_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeometric_tolerance_xim.CxDatum_defined_by_derived_shape;
import jsdai.SGeometric_tolerance_xim.CxDatum_defined_by_feature;
import jsdai.SGeometric_tolerance_xim.EDatum_defined_by_feature;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_aspect_definition_schema.CCentre_of_symmetry$datum;
import jsdai.SShape_aspect_definition_schema.EDatum;

public class CxDatum_defined_by_centre_plane extends CDatum_defined_by_centre_plane implements EMappedXIMEntity
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

	// DATUM
	/// methods for attribute: identification, base type: STRING
/*	public boolean testIdentification(EDatum type) throws SdaiException {
		return test_string(a5);
	}
	public String getIdentification(EDatum type) throws SdaiException {
		return get_string(a5);
	}*/
	public void setIdentification(EDatum type, String value) throws SdaiException {
		a5 = set_string(value);
	}
	public void unsetIdentification(EDatum type) throws SdaiException {
		a5 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeIdentification(EDatum type) throws SdaiException {
		return a5$;
	}
	// ENDOF DATUM
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CCentre_of_symmetry$datum.definition);

		setMappingConstraints(context, this);

		setDerived_from(context, this);
		
		unsetDerived_from(null);
		
//		setModification(context, this);
		
		// clean ARM
//		unsetModification(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetDerived_from(context, this);
			
//			unsetModification(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			EDatum_defined_by_centre_plane armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxDatum_defined_by_derived_shape.setMappingConstraints(context, armEntity);
		CxCentre_plane.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EDatum_defined_by_centre_plane armEntity) throws SdaiException {
		CxDatum_defined_by_derived_shape.unsetMappingConstraints(context, armEntity);
		CxCentre_plane.unsetMappingConstraints(context, armEntity);
	}

	public static void setDerived_from(SdaiContext context,
			EDerived_shape_element armEntity) throws SdaiException {
		CxDerived_shape_element.setDerived_from(context, armEntity);
	}

	public static void unsetDerived_from(SdaiContext context,
			EDerived_shape_element armEntity) throws SdaiException {
		CxDerived_shape_element.unsetDerived_from(context, armEntity);
	}
	
	
	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	/* Removed in Milestone M6 of IDA-STEP	
	public static void setModification(SdaiContext context,
		ESingle_datum armEntity) throws SdaiException {
		CxSingle_datum.setModification(context, armEntity);
	}*/

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	/* Removed in Milestone M6 of IDA-STEP	
	public static void unsetModification(SdaiContext context,
			ESingle_datum armEntity) throws SdaiException {
		CxSingle_datum.unsetModification(context, armEntity);		
	}*/

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	attribute_mapping defined_by(defined_by, $PATH, Shape_element);
		datum <= 
		shape_aspect <- 
		shape_aspect_relationship.related_shape_aspect 
		shape_aspect_relationship
		shape_aspect_relationship.relating_shape_aspect ->
		shape_aspect
		{shape_aspect => 
		datum_feature}
	end_attribute_mapping;
 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setDefined_by(SdaiContext context, EDatum_defined_by_feature armEntity) throws SdaiException {
		CxDatum_defined_by_feature.setDefined_by(context, armEntity);		
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetDefined_by(SdaiContext context, EDatum_defined_by_feature armEntity) throws SdaiException {
		CxDatum_defined_by_feature.unsetDefined_by(context, armEntity);		
	}
	
}