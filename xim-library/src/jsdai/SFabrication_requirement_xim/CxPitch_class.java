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

package jsdai.SFabrication_requirement_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SCharacteristic_xim.ERange_characteristic_armx;
import jsdai.SFabrication_requirement_xim.CPitch_class;
import jsdai.SFabrication_requirement_xim.EPitch_class;
import jsdai.SRepresentation_schema.*;

public class CxPitch_class extends CPitch_class implements EMappedXIMEntity
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// FROM Representation
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

	//<01> generating methods for consolidated attribute:  context_of_items
	//<01-0> current entity
	//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
	// attribute (current explicit or supertype explicit) : context_of_items, base type: entity representation_context
/*	public static int usedinContext_of_items(ERepresentation type, ERepresentation_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testContext_of_items(ERepresentation type) throws SdaiException {
		return test_instance(a2);
	}
	public ERepresentation_context getContext_of_items(ERepresentation type) throws SdaiException {
		return (ERepresentation_context)get_instance(a2);
	}*/
	public void setContext_of_items(ERepresentation type, ERepresentation_context value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetContext_of_items(ERepresentation type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeContext_of_items(ERepresentation type) throws SdaiException {
		return a2$;
	}

	//<01> generating methods for consolidated attribute:  description
	//<01-0> current entity
	//<01-0-1> derived attribute
	//<01-0-1-1> NOT explicit-to-derived - generateDerivedCurrentEntityMethodsX()
	// methods for derived attribute: description, base type: STRING
/*	public boolean testDescription(ERepresentation type) throws SdaiException {
			throw new SdaiException(SdaiException.FN_NAVL);
	}
	public Value getDescription(ERepresentation type, SdaiContext _context) throws SdaiException {


				return ((new jsdai.SBasic_attribute_schema.FGet_description_value()).run(_context, Value.alloc(jsdai.SRepresentation_schema.CRepresentation.definition).set(_context, this)));
	}
	public String getDescription(ERepresentation type) throws SdaiException {
		SdaiContext _context = this.findEntityInstanceSdaiModel().getRepository().getSession().getSdaiContext();
		return getDescription((ERepresentation)null, _context).getString();
	}*/
	public static jsdai.dictionary.EAttribute attributeDescription(ERepresentation type) throws SdaiException {
		return d1$;
	}
	// ENDOF FROM Representation	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CRepresentation.definition);

		setMappingConstraints(context, this);
		
        // Class_member
		setClass_member(context, this);
      
		// Class_name
		setClass_name(context, this);
		
		// Clean ARM
		// as_finished_inter_stratum_extent 		
		// unsetAs_finished_inter_stratum_extent(null);

      // plated_passage
		unsetClass_member(null);
      
		// as_finished_passage_extent
		unsetClass_name(null);
		
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

        // Class_member
		unsetClass_member(context, this);
      
		// Class_name
		unsetClass_name(context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
		{representation
		representation.name = 'pitch class'}
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPitch_class armEntity) throws SdaiException
	{
		unsetMappingConstraints(context, armEntity);
		armEntity.setName(null, "pitch class");
		// AIM GAP
//		if(!armEntity.testContext_of_items(null)){
		ERepresentation_context repContext = CxAP210ARMUtilities.createRepresentation_context(context, "", "", true);
		armEntity.setContext_of_items(null, repContext);
//		}
	}

	public static void unsetMappingConstraints(SdaiContext context, EPitch_class armEntity) throws SdaiException
	{
		armEntity.unsetName(null);
	}

 //********** "pitch_class" attributes

 /**
  * Sets/creates data for class_name attribute.
  *
  * <p>
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setClass_name(SdaiContext context, EPitch_class armEntity) throws SdaiException {
    //unset old values
    unsetClass_name(context, armEntity);

    if (armEntity.testClass_name(null)) {
       String name = armEntity.getClass_name(null);
       CxAP210ARMUtilities.setRepresentationDescription(context,armEntity, name);
    }
 }

 /**
  * Unsets/deletes data for class_name attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetClass_name(SdaiContext context, EPitch_class armEntity) throws SdaiException {
	 CxAP210ARMUtilities.unsetDerviedDescription(context, armEntity);
 }

 /**
  * Sets/creates data for class_name attribute.
  *
  * <p>
  * </p>
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void setClass_member(SdaiContext context, EPitch_class armEntity) throws SdaiException {
    //unset old values
    unsetClass_member(context, armEntity);

    if (armEntity.testClass_member(null)) {
       ERange_characteristic_armx member = armEntity.getClass_member(null);
       armEntity.createItems(null).addUnordered(member);
    }
 }

 /**
  * Unsets/deletes data for class_name attribute.
  *
  * @param context SdaiContext.
  * @param armEntity arm entity.
  * @throws SdaiException
  */
 public static void unsetClass_member(SdaiContext context, EPitch_class armEntity) throws SdaiException {
	 armEntity.unsetItems(null);
 }
 
 	
}
