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

package jsdai.SGeneric_material_aspects_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SMaterial_property_definition_schema.*;

public class CxMaterial_property_association extends CMaterial_property_association implements EMappedXIMEntity
{
	
	// Taken from Material_designation_characterization
/*	public boolean testName(EMaterial_designation_characterization type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EMaterial_designation_characterization type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EMaterial_designation_characterization type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EMaterial_designation_characterization type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EMaterial_designation_characterization type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EMaterial_designation_characterization type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EMaterial_designation_characterization type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EMaterial_designation_characterization type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EMaterial_designation_characterization type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EMaterial_designation_characterization type) throws SdaiException {
		return a1$;
	}


	// -2- methods for SELECT attribute: property
/*	public static int usedinProperty(EMaterial_designation_characterization type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testProperty(EMaterial_designation_characterization type) throws SdaiException {
		return test_instance(a3);
	}

	public EEntity getProperty(EMaterial_designation_characterization type) throws SdaiException { // case 1
		a3 = get_instance_select(a3);
		return (EEntity)a3;
	}
*/
	public void setProperty(EMaterial_designation_characterization type, EEntity value) throws SdaiException { // case 1
		a3 = set_instance(a3, value);
	}

	public void unsetProperty(EMaterial_designation_characterization type) throws SdaiException {
		a3 = unset_instance(a3);
	}

	public static jsdai.dictionary.EAttribute attributeProperty(EMaterial_designation_characterization type) throws SdaiException {
		return a3$;
	}
	
	// ENDOF FROM Material_designation_characterization 

	public int attributeState = ATTRIBUTES_MODIFIED;	

	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CMaterial_designation_characterization.definition);

		setMappingConstraints(context, this);

	    // associated_property_value : material_property_value_representation;
		setAssociated_property_value(context, this);
		
        // definitional : OPTIONAL BOOLEAN;
		setDefinitional(context, this);
		
		
	    // associated_property_value : material_property_value_representation;
		unsetAssociated_property_value(null);
		
        // definitional : OPTIONAL BOOLEAN;
		unsetDefinitional(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

	    // associated_property_value : material_property_value_representation;
		unsetAssociated_property_value(context, this);
		
        // definitional : OPTIONAL BOOLEAN;
		unsetDefinitional(context, this);
		
	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		// AIM gap
		armEntity.setDescription(null, "");
	}

	public static void unsetMappingConstraints(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		// AIM gap
		armEntity.unsetDescription(null);
	}


	/**
	* Sets/creates data for Associated_property_value attribute.
	*
	attribute_mapping associated_property_value(associated_property_value, $PATH, Material_property_value_representation);
		material_designation_characterization.property -> characterized_material_property
		characterized_material_property = material_property_representation
		material_property_representation <= property_definition_representation
		property_definition_representation.used_representation -> representation
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MDC -> MPR -> R
	public static void setAssociated_property_value(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		unsetAssociated_property_value(context, armEntity);
		if(armEntity.testAssociated_property_value(null)){
			EMaterial_property_value_representation representation = armEntity.getAssociated_property_value(null);
           armEntity.setProperty(null, representation);
		}
	}

	public static void unsetAssociated_property_value(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		armEntity.unsetProperty(null);
	}

	
	/**
	* Sets/creates data for Associated_property_value attribute.
	*
	attribute_mapping definitional(definitional, material_designation_characterization.name);
		material_designation_characterization.name
		{(material_designation_characterization.name = 'non definitional')
		(material_designation_characterization.name = 'definitional')
		(material_designation_characterization.name = '')}
	end_attribute_mapping;
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	// MDC -> MPR -> R
	public static void setDefinitional(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		unsetDefinitional(context, armEntity);
		if(armEntity.testDefinitional(null)){
			boolean definitional = armEntity.getDefinitional(null);
			if(definitional){
				armEntity.setName(null, "definitional");
			}else{
				armEntity.setName(null, "non definitional");				
			}
		}
	}

	public static void unsetDefinitional(SdaiContext context, EMaterial_property_association armEntity) throws SdaiException
	{
		armEntity.setName(null, "");
	}
	
}
