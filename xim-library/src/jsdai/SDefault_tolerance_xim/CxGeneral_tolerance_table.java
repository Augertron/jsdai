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

package jsdai.SDefault_tolerance_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.SDefault_tolerance_mim.CDefault_tolerance_table;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_context;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.libutil.EMappedXIMEntity;

public class CxGeneral_tolerance_table extends CGeneral_tolerance_table implements EMappedXIMEntity
{
	public int attributeState = ATTRIBUTES_MODIFIED;	

	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
	/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
			return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
		}
		public boolean testContext_of_items(ERepresentation type) throws SdaiException {
			return test_instance(a2);
		}
		public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
			a2 = get_instance(a2);
			return (ERepresentation_context)a2;
		}*/
		public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
			a2 = set_instanceX(a2, value);
		}
		public void unsetContext_of_items(ERepresentation type) throws SdaiException {
			a2 = unset_instance(a2);
		}
		public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
			return a2$;
		}
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}
		setTemp("AIM", CDefault_tolerance_table.definition);
		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	mapping_constraints;
		{property_definition_representation 
		property_definition_representation.name = 'default setting association'} 
	end_mapping_constraints;
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context, EGeneral_tolerance_table armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		ERepresentation_context erc = 
			CxAP210ARMUtilities.createRepresentation_context(context, "", "", true);
		armEntity.setContext_of_items(null, erc);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context, EGeneral_tolerance_table armEntity) throws SdaiException {
	}
}