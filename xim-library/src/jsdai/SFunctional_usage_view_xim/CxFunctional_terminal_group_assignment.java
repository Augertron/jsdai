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

package jsdai.SFunctional_usage_view_xim;

import jsdai.lang.*;
import jsdai.libutil.*;
// import jsdai.dictionary.*;
// import jsdai.MAp210.*;
import jsdai.SGroup_mim.*;

/**
* @author Valdas Zigas, Giedrius Liutkus
* @version $Revision$
*/

public class CxFunctional_terminal_group_assignment extends CFunctional_terminal_group_assignment implements EMappedXIMEntity
{


	// Taken from Applied_group_assignment
	// methods for attribute: items, base type: SET OF SELECT
/*	public static int usedinItems(EApplied_group_assignment type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a1$, domain, result);
	}
	public boolean testItems(EApplied_group_assignment type) throws SdaiException {
		return test_aggregate(a1);
	}
	public AGroupable_item getItems(EApplied_group_assignment type) throws SdaiException {
		if (a1 == null)
			throw new SdaiException(SdaiException.VA_NSET);
		return a1;
	}*/
	public AGroupable_item createItems(EApplied_group_assignment type) throws SdaiException {
		a1 = (AGroupable_item)create_aggregate_class(a1, a1$, AGroupable_item.class, 0);
		return a1;
	}
	public void unsetItems(EApplied_group_assignment type) throws SdaiException {
		unset_aggregate(a1);
		a1 = null;
	}
	public static jsdai.dictionary.EAttribute attributeItems(EApplied_group_assignment type) throws SdaiException {
		return a1$;
	}
	// end of taken from 
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
			if (attributeState == ATTRIBUTES_MODIFIED) {
				attributeState = ATTRIBUTES_UNMODIFIED;
			} else {
				return;
			}

			setTemp("AIM", CApplied_group_assignment.definition);

			setMappingConstraints(context, this);

			// functional_usage_view_terminal 
			setFunctional_usage_view_terminal (context, this);

	      // Clean ARM
	      unsetFunctional_usage_view_terminal(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.libutil.EMappedXIMEntity#removeAimData(jsdai.lang.SdaiContext)
	 */
	public void removeAimData(SdaiContext context) throws SdaiException {
		unsetMappingConstraints(context, this);

		// functional_usage_view_terminal 
		unsetFunctional_usage_view_terminal (context, this);

	}
	
	
	/**
	* Sets/creates data for mapping constraints.
	*
	*  {applied_group_assignment
	*  applied_group_assignment &lt;=
	*  group_assignment
	*  group_assignment.assigned_group -&gt;
	*  group =&gt;
	*  functional_terminal_group}	
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EFunctional_terminal_group_assignment armEntity) throws SdaiException
	{
	}

	public static void unsetMappingConstraints(SdaiContext context, EFunctional_terminal_group_assignment armEntity) throws SdaiException
	{
	}
	
	/**
	   * Sets/creates data for Functional_usage_view_terminal attribute.
	   *
	   *
	   *  applied_group_assignment
	   *  applied_group_assignment.items[i] -&gt;
	   *  groupable_item 
	   *  groupable_item = shape_aspect
	   *  {shape_aspect
	   *  shape_aspect.description = 'scalar terminal'}
	   *  shape_aspect =&gt;
	   *  functional_unit_terminal_definition
	   *
	   * @param context SdaiContext.
	   * @param armEntity arm entity.
	   * @throws SdaiException
	   */
	  // MI -> RM -> SR
	  public static void setFunctional_usage_view_terminal(SdaiContext context,
	                                                            EFunctional_terminal_group_assignment armEntity) throws
	     SdaiException {
	     unsetFunctional_usage_view_terminal(context, armEntity);

	     if (armEntity.testFunctional_usage_view_terminal(null)) {
	        EScalar_terminal_definition terminal = armEntity.getFunctional_usage_view_terminal(null);
	        armEntity.createItems(null).addUnordered(terminal);
	     }
	  }

	  /**
	   * Unsets/deletes data for associated_feature_shape_definition attribute.
	   *
	   * @param context SdaiContext.
	   * @param armEntity arm entity.
	   * @throws SdaiException
	   */
	  // MI - RM -> SR, break here
	  public static void unsetFunctional_usage_view_terminal(SdaiContext context,
	  		EFunctional_terminal_group_assignment armEntity) throws
	     SdaiException {
	     if (armEntity.testFunctional_usage_view_terminal(null)) {
	     	armEntity.unsetItems(null);
	     }
	  }
			

}
