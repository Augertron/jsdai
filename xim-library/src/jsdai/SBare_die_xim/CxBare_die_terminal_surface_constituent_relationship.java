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

package jsdai.SBare_die_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SShape_composition_xim.CxShape_element_constituent_relationship;

public class CxBare_die_terminal_surface_constituent_relationship extends CBare_die_terminal_surface_constituent_relationship implements EMappedXIMEntity{

	EEntity getAimInstance(){
		return this;
	}
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CShape_aspect_relationship.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);

	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * mapping_constraints; 
	 *  {shape_aspect_relationship
	 *  (shape_aspect_relationship.name = 'bare die terminal surface constituent relationship')
	 *  (shape_aspect_relationship.name = 'composing')
	 *  (shape_aspect_relationship.name = 'constituent')
	 *  (shape_aspect_relationship.name = 'interconnect module terminal surface constituent relationship')
	 *  (shape_aspect_relationship.name = 'package terminal surface constituent relationship')}
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
			EBare_die_terminal_surface_constituent_relationship armEntity) throws SdaiException {
		CxShape_element_constituent_relationship.setMappingConstraints(context, armEntity);
		armEntity.setName(null, "bare die terminal surface constituent relationship");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EBare_die_terminal_surface_constituent_relationship armEntity) throws SdaiException {
		CxShape_element_constituent_relationship.unsetMappingConstraints(context, armEntity);
		armEntity.unsetName(null);
	}
	
}