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

package jsdai.SFabrication_technology_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SFeature_and_connection_zone_xim.CxShape_feature;
import jsdai.SProduct_property_definition_schema.*;

public class CxComponent_termination_passage_template_terminal extends CComponent_termination_passage_template_terminal implements EMappedXIMEntity{

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

		setTemp("AIM", CShape_aspect.definition);

		setMappingConstraints(context, this);
		
		// Connection_area
		setConnection_area(context, this);
		
		// Clean ARM
		unsetConnection_area(null);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

			// Connection_area
			unsetConnection_area(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 * {[shape_aspect
	 *  (shape_aspect.description =  'component termination passage template interface terminal')
	 *  (shape_aspect.description =  'component termination passage template join terminal')]
	 *  [shape_aspect
	 *  shape_aspect.of_shape -&gt;
	 *  product_definition_shape &lt;=
	 *  property_definition
	 *  property_definition.definition -&gt;
	 *  characterized_definition
	 *  characterized_definition = characterized_product_definition
	 *  characterized_product_definition
	 *  characterized_product_definition = product_definition
	 *  product_definition
	 *  [product_definition
	 *  product_definition.formation -&gt;
	 *  product_definition_formation
	 *  product_definition_formation.of_product -&gt;
	 *  product &lt;-
	 *  product_related_product_category.products[i]
	 *  product_related_product_category &lt;=
	 *  product_category
	 *  product_category.name = 'template model']
	 *  [product_definition.frame_of_reference -&gt;
	 *  product_definition_context &lt;=
	 *  application_context_element
	 *  application_context_element.name = 'template definition']]}
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
			EComponent_termination_passage_template_terminal armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		CxShape_feature.setMappingConstraints(context, armEntity);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EComponent_termination_passage_template_terminal armEntity) throws SdaiException {
		CxShape_feature.unsetMappingConstraints(context, armEntity);
	}


	//********** "component_termination_passage_template_terminal" attributes

	/**
	* Sets/creates data for Connection_area attribute.
	*
	*/
	public static void setConnection_area(SdaiContext context, EComponent_termination_passage_template_terminal armEntity) throws SdaiException
	{
		CxShape_feature.setConnection_area(context, armEntity);
	}


	/**
	* Unsets/deletes data for Connection_area attribute.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EComponent_termination_passage_template_terminal armEntity) throws SdaiException
	{
		CxShape_feature.unsetConnection_area(context, armEntity);
	}

	
}