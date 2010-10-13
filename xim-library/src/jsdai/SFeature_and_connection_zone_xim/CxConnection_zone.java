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

package jsdai.SFeature_and_connection_zone_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $Id$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_definition_schema.*;

public class CxConnection_zone extends CConnection_zone implements EMappedXIMEntity
{

	// From CShape_aspect.java
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

		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	*  mapping_constraints;
	* 	{shape_aspect
	* 	shape_aspect.description = `connection zone'}
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EConnection_zone armEntity) throws SdaiException
	{
//		CxShape_element.setMappingConstraints(context, armEntity);
		
		jsdai.SProduct_property_definition_schema.EShape_aspect aimEntity = armEntity;
		aimEntity.setDescription(null, "connection zone");
		// The only way see to set "of_shape" attribute
		AEntity users = new AEntity();
		armEntity.findEntityInstanceUsers(null, users);
		for(int i=1;i<=users.getMemberCount();i++){
			EMappedARMEntity user = (EMappedARMEntity)users.getByIndexEntity(i);
			user.createAimData(context);
			EEntity aimUser = user.getAimInstance();
			if((aimUser instanceof ECharacterized_object)||
				(aimUser instanceof EProduct_definition)||
				(aimUser instanceof EProduct_definition_relationship)){
// Some WR do not allow this
//				(aimUser instanceof EProduct_definition_shape)||
//				(aimUser instanceof EShape_aspect_relationship)){
				EProduct_definition_shape epds = (EProduct_definition_shape)
					context.working_model.createEntityInstance(CProduct_definition_shape.class);
				epds.setDefinition(null, aimUser);
				epds.setName(null, "");
				aimEntity.setOf_shape(null, epds);
				break;
			}
			if(aimUser instanceof EShape_aspect)
				aimEntity.setOf_shape(null, ((EShape_aspect)(aimUser)).getOf_shape(null));
		}
		if(!aimEntity.testOf_shape(null)){
			SdaiSession.getSession().printlnSession(aimEntity+" connection zone without a valid user " +users.getMemberCount()+" "+armEntity);
		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EConnection_zone armEntity) throws SdaiException
	{
//		CxShape_element.unsetMappingConstraints(context, armEntity);
		
		jsdai.SProduct_property_definition_schema.EShape_aspect aimEntity = armEntity;
		aimEntity.unsetDescription(null);
	}
	
	
}
