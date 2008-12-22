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
import jsdai.SLayered_interconnect_module_design_mim.EProbe_access_area;
import jsdai.SLayered_interconnect_simple_template_mim.EDefault_attachment_size_based_land_physical_template;
import jsdai.SProduct_property_definition_schema.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SShape_property_assignment_xim.CxShape_element;

public class CxShape_feature extends CShape_feature implements EMappedXIMEntity
{

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
		
		setConnection_area(context, this);
		
		unsetConnection_area(null);

		//********** "managed_design_object" attributes

	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);
		
		unsetConnection_area(context, this);
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
	public static void setMappingConstraints(SdaiContext context, EShape_feature armEntity) throws SdaiException
	{
		CxShape_element.setMappingConstraints(context, armEntity);
		jsdai.SProduct_property_definition_schema.EShape_aspect aimEntity = armEntity;
		// aimEntity.setDescription(null, "shape feature");
		aimEntity.setProduct_definitional(null, ELogical.TRUE);
		// Security checks where 'of_shape' is redeclared as DERIVED
		if(aimEntity instanceof EProbe_access_area){
			return;
		}
		if(aimEntity instanceof EDefault_attachment_size_based_land_physical_template){
			return;
		}
		
		// Likely it is redeclared in XIM to be something meaningful in subtype - so leave it
		if(aimEntity.testOf_shape(null)){
			return;
		}
		// The only way see to set "of_shape" attribute
		AEntity users = new AEntity();
		armEntity.findEntityInstanceUsers(null, users);
		for(int i=1;i<=users.getMemberCount();i++){
			EEntity aimUser = users.getByIndexEntity(i);
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
			System.err.println(aimEntity+" shape_feature without a valid user " +users.getMemberCount()+" "+armEntity);
		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EShape_feature armEntity) throws SdaiException
	{
		CxShape_element.unsetMappingConstraints(context, armEntity);
		jsdai.SProduct_property_definition_schema.EShape_aspect aimEntity = armEntity;
		// aimEntity.unsetDescription(null);
		aimEntity.unsetProduct_definitional(null);
	}

	/**
	* Sets/creates data for connection_area.
	*
	* <p>
	*  mapping_constraints;
	* 	shape_aspect
	*  shape_aspect &lt;- 
	*  shape_aspect_relationship.related_shape_aspect 
	*  {shape_aspect_relationship 
	*  shape_aspect_relationship.name = 'connection area'} 
	*  shape_aspect_relationship.relating_shape_aspect -&gt;  
	*  shape_aspect 
	*  {shape_aspect.description = 'connection zone'} 
	*  end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setConnection_area(SdaiContext context, EShape_feature armEntity) throws SdaiException
	{
		unsetConnection_area(context, armEntity);
		if(armEntity.testConnection_area(null)){
			AConnection_zone zones = armEntity.getConnection_area(null);
			for(int i=1, count=zones.getMemberCount();i<=count;i++){
				EConnection_zone zone = zones.getByIndex(i);
				EShape_aspect_relationship relationship = (EShape_aspect_relationship) 
					context.working_model.createEntityInstance(CShape_aspect_relationship.definition);
				relationship.setRelated_shape_aspect(null, armEntity);
				relationship.setName(null, "connection area");
				relationship.setRelating_shape_aspect(null, zone);
			}
		}
	}

	/**
	* Unsets/deletes data for connection_area.
	*
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void unsetConnection_area(SdaiContext context, EShape_feature armEntity) throws SdaiException
	{
			AShape_aspect_relationship asar = new AShape_aspect_relationship();
			CShape_aspect_relationship.usedinRelated_shape_aspect(null, armEntity, context.domain, asar);
			
			for(int i=1, count=asar.getMemberCount(); i<=count; i++){
				EShape_aspect_relationship esar = asar.getByIndex(i);
				// Safety check for derived attributes
/*				if(((CEntity)esar).testAttributeFast(CShape_aspect_relationship.attributeName(null), null) < 0){
					continue;
				}*/
				if((esar.testName(null))&&(esar.getName(null).equals("connection area"))){
					esar.deleteApplicationInstance();
				}
			}
	}
	
}
