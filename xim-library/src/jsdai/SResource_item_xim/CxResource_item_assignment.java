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

package jsdai.SResource_item_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SAction_schema.*;
import jsdai.SActivity_method_assignment_mim.*;
import jsdai.SManagement_resources_schema.*;

public class CxResource_item_assignment extends CResource_item_assignment implements EMappedXIMEntity{


	// Taken from Action_method
	/// methods for attribute: name, base type: STRING
/*	public boolean testName(EAction_method type) throws SdaiException {
		return test_string(a0);
	}
	public String getName(EAction_method type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setName(EAction_method type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetName(EAction_method type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EAction_method type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: consequence, base type: STRING
/*	public boolean testConsequence(EAction_method type) throws SdaiException {
		return test_string(a2);
	}
	public String getConsequence(EAction_method type) throws SdaiException {
		return get_string(a2);
	}*/
	public void setConsequence(EAction_method type, String value) throws SdaiException {
		a2 = set_string(value);
	}
	public void unsetConsequence(EAction_method type) throws SdaiException {
		a2 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeConsequence(EAction_method type) throws SdaiException {
		return a2$;
	}

	/// methods for attribute: purpose, base type: STRING
/*	public boolean testPurpose(EAction_method type) throws SdaiException {
		return test_string(a3);
	}
	public String getPurpose(EAction_method type) throws SdaiException {
		return get_string(a3);
	}*/
	public void setPurpose(EAction_method type, String value) throws SdaiException {
		a3 = set_string(value);
	}
	public void unsetPurpose(EAction_method type) throws SdaiException {
		a3 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributePurpose(EAction_method type) throws SdaiException {
		return a3$;
	}
	// ENDOF taken from Action_method
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAction_method.definition);

		setMappingConstraints(context, this);

		// assigned_resource
		setAssigned_resource(context, this);
		
      // item
		setItem(context, this);
		
		// clean ARM
		// assigned_resource
		unsetAssigned_resource(null);
		
      // item
		unsetItem(null);
		
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// assigned_resource
			unsetAssigned_resource(context, this);
			
	      // item
			unsetItem(context, this);
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 *  [{action_method.name = 'resource management'}
	 *  {action_method.consequence = 'resource item assignment'}
	 *  {action_method.purpose = 'standard action method'}]
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
			EResource_item_assignment armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		armEntity.setName(null, "resource management");
		armEntity.setConsequence(null, "resource item assignment");
		armEntity.setPurpose(null, "standard action method");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EResource_item_assignment armEntity) throws SdaiException {
		armEntity.unsetName(null);
		armEntity.unsetConsequence(null);
		armEntity.unsetPurpose(null);
	}

	/**
	 * Sets/creates data for Assigned_resource.
	 * 
	 * <p>
	 *  action_method
	 *  supported_item = action_method
	 *  supported_item
	 *  supported_item &lt;- action_resource.usage[i]
	 *  action_resource	 
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// AR -> AM <- AAMA -> AMI
	public static void setAssigned_resource(SdaiContext context,
			EResource_item_assignment armEntity) throws SdaiException {
		unsetAssigned_resource(context, armEntity);
		if(armEntity.testAssigned_resource(null)){
			EResource_item item = armEntity.getAssigned_resource(null);
			ASupported_item items;
			if(item.testUsage(null))
				items = item.getUsage(null);
			else
				items = item.createUsage(null);
			if(!items.isMember(armEntity))
				items.addUnordered(armEntity);
		}
	}

	/**
	 * Unsets/deletes mapping Resource_items.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetAssigned_resource(SdaiContext context,
			EResource_item_assignment armEntity) throws SdaiException {
		AResource_item ari = new AResource_item();
		CResource_item.usedinUsage(null, armEntity, context.domain, ari);
		SdaiIterator iter = ari.createIterator();
		while(iter.next()){
			EResource_item eri = ari.getCurrentMember(iter);
			eri.getUsage(null).removeUnordered(armEntity);
		}
	}

	/**
	 * Sets/creates data for Item.
	 * 
	 * <p>
    	<aa attribute="item" assertion_to="resource_assignment_item">
    	<aimelt>PATH</aimelt>
      <source>ISO 10303-1249</source>
      <refpath>	
      	action_method &lt;- action_method_assignment.assigned_action_method
      	action_method_assignment
      	{action_method_assignment.role -&gt; action_method_role
      	action_method_role
      	action_method_role.name = 'resource item assignment'}
      	action_method_assignment =&gt; applied_action_method_assignment
      	applied_action_method_assignment
      	applied_action_method_assignment.items[i] -&gt; action_method_items
      </refpath>
    </aa>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// AM <- AAMA -> AMI
	public static void setItem(SdaiContext context,
			EResource_item_assignment armEntity) throws SdaiException {
		unsetItem(context, armEntity);
		if(armEntity.testItem(null)){
			// Role
       	LangUtils.Attribute_and_value_structure[] structure = {
   			   new LangUtils.Attribute_and_value_structure(
   			   	CAction_method_role.attributeName(null), 
   			   	"resource item assignment")
   			   };
       	EAction_method_role role = (EAction_method_role)  
   		   LangUtils.createInstanceIfNeeded(context, CAction_method_role.definition, structure);
       	// AAMA
       	EApplied_action_method_assignment eaama = (EApplied_action_method_assignment)
				context.working_model.createEntityInstance(CApplied_action_method_assignment.definition);
       	eaama.setRole(null, role);
//TODO  	eaama.createItems(null).addUnordered(armEntity.getItem(null));
       	eaama.setAssigned_action_method(null, armEntity);
		}
	}

	/**
	 * Unsets/deletes mapping Resource_items.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetItem(SdaiContext context,
			EResource_item_assignment armEntity) throws SdaiException {
		if(armEntity.testItem(null)){
			AApplied_action_method_assignment aaama = new AApplied_action_method_assignment();
			CApplied_action_method_assignment.usedinAssigned_action_method(null, armEntity, context.domain, aaama);
			SdaiIterator iter = aaama.createIterator();
			while(iter.next()){
				aaama.getCurrentMember(iter).deleteApplicationInstance();
			}
		}
	}
	
	
}