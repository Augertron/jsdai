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

package jsdai.SBasic_curve_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SGeometry_schema.ECurve;
import jsdai.SRepresentation_schema.*;

public class CxMeasurement_path extends CMeasurement_path implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from Representation
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(ERepresentation type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(ERepresentation type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(ERepresentation type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(ERepresentation type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(ERepresentation type) throws SdaiException {
		return a0$;
	}
	
	//going through all the attributes: #5629499534229266=EXPLICIT_ATTRIBUTE('items',#5629499534229263,1,#5629499534667020,$,.F.);
	//<01> generating methods for consolidated attribute:  items
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// methods for attribute: items, base type: SET OF ENTITY
/*	public static int usedinItems(ERepresentation type, ERepresentation_item instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testItems(ERepresentation type) throws SdaiException {
		return test_aggregate(a1);
	}
	public ARepresentation_item getItems(ERepresentation type) throws SdaiException {
		return (ARepresentation_item)get_aggregate(a1);
	}*/
	public ARepresentation_item createItems(ERepresentation type) throws SdaiException {
		a1 = (ARepresentation_item)create_aggregate_class(a1, a1$,  ARepresentation_item.class, 0);
		return a1;
	}
	public void unsetItems(ERepresentation type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(ERepresentation type) throws SdaiException {
		return a1$;
	}	
	// ENDOF taken from Representation
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CRepresentation.definition);

		setMappingConstraints(context, this);

		setDefined_by(context, this);
		
		unsetDefined_by(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		unsetDefined_by(context, this);
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		{representation.name = 'measuring direction'}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EMeasurement_path armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "measuring direction");
	}

	public static void unsetMappingConstraints(SdaiContext context, EMeasurement_path armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}

	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	mapping_constraints;
		{representation.name = 'measuring direction'}
	end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setDefined_by(SdaiContext context, EMeasurement_path armEntity) throws SdaiException
	{
		unsetDefined_by(context, armEntity);
		if(armEntity.testDefined_by(null)){
			ECurve curve = armEntity.getDefined_by(null);
			armEntity.createItems(null).addUnordered(curve);
		}
	}

	public static void unsetDefined_by(SdaiContext context, EMeasurement_path armEntity) throws SdaiException
	{
		armEntity.unsetItems(null);
	}
	
}
