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

public class CxResource_item extends CResource_item implements EMappedXIMEntity{


	// Taken from Action_resource
	// methods for attribute: usage, base type: SET OF SELECT
/*	public static int usedinUsage(EAction_resource type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testUsage(EAction_resource type) throws SdaiException {
		return test_aggregate(a2);
	}
	public ASupported_item getUsage(EAction_resource type) throws SdaiException {
		if (a2 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a2;
	}*/
	public ASupported_item createUsage(EAction_resource type) throws SdaiException {
		a2 = (ASupported_item)create_aggregate_class(a2, a2$, ASupported_item.class, 0);
		return a2;
	}
	public void unsetUsage(EAction_resource type) throws SdaiException {
		unset_aggregate(a2);
		a2 = null;
	}
	public static jsdai.dictionary.EAttribute attributeUsage(EAction_resource type) throws SdaiException {
		return a2$;
	}

	// attribute (current explicit or supertype explicit) : kind, base type: entity action_resource_type
/*	public static int usedinKind(EAction_resource type, EAction_resource_type instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testKind(EAction_resource type) throws SdaiException {
		return test_instance(a3);
	}
	public EAction_resource_type getKind(EAction_resource type) throws SdaiException {
		a3 = get_instance(a3);
		return (EAction_resource_type)a3;
	}*/
	public void setKind(EAction_resource type, EAction_resource_type value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetKind(EAction_resource type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeKind(EAction_resource type) throws SdaiException {
		return a3$;
	}


	
	// ENDOF taken from Action_resource
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CAction_relationship.definition);

		setMappingConstraints(context, this);

		// resource_items
		setResource_items(context, this);
		
		// clean ARM
		unsetResource_items(null);
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			unsetResource_items(context, this);			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 *  action_resource
	 *  {action_resource.kind -&gt;
	 *  action_resource_type
	 *  action_resource_type.name = 'resource item'}
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
			EResource_item armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

      LangUtils.Attribute_and_value_structure[] artStructure =
		{new LangUtils.Attribute_and_value_structure(
              CAction_resource_type.attributeName(null),
               "resource item")
		};
      EAction_resource_type eart = (EAction_resource_type)
			LangUtils.createInstanceIfNeeded(context, CAction_resource_type.definition, artStructure);
		armEntity.setKind(null, eart);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EResource_item armEntity) throws SdaiException {

		armEntity.unsetKind(null);
	}

	/**
	 * Sets/creates data for resource_items.
	 * 
	 * <p>
	 *  <aa attribute="resource_items" assertion_to="resource_item_select">
	 *  <aimelt>PATH</aimelt> 
	 *  <source>ISO 10303-1249</source> 
	 *  	<refpath>action_resource.usage[i] -&gt;
   		supported_item 
   		supported_item = action_method
   		action_method
   		[{action_method.name = 'resource management'}
   		{action_method.consequence = 'resource item'}
   		{action_method.purpose = 'standard action method'}]
   		action_method &lt;- action_method_assignment.assigned_action_method
   		action_method_assignment
   		{action_method_assignment.role -&gt; action_method_role
   		action_method_role
   		action_method_role.name = 'resource item'}
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
	// AR -> AM <- AAMA -> AMI
	public static void setResource_items(SdaiContext context,
			EResource_item armEntity) throws SdaiException {
		unsetResource_items(context, armEntity);
		if(armEntity.testResource_items(null)){
			throw new SdaiException(SdaiException.FN_NAVL, "setting method is not implemented for this attribute"+armEntity);
		}
	}

	/**
	 * Unsets/deletes mapping Resource_items.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetResource_items(SdaiContext context,
			EResource_item armEntity) throws SdaiException {
		if(armEntity.testResource_items(null)){
			throw new SdaiException(SdaiException.FN_NAVL, "unsetting method is not implemented for this attribute"+armEntity);
		}
	}
	
	
}