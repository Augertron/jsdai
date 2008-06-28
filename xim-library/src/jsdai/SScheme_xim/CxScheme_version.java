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

package jsdai.SScheme_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.SAction_schema.*;

public class CxScheme_version extends CScheme_version implements EMappedXIMEntity{


	// Taken from Action_method

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
		
		// Of_scheme
		setOf_scheme(context, this);
		
		// clean ARM
		unsetOf_scheme(null);		

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
			// Of_scheme
			unsetOf_scheme(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 *  {action_method.purpose='scheme version'}
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
			EScheme_version armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);

		armEntity.setPurpose(null, "scheme version");
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			EScheme_version armEntity) throws SdaiException {
		armEntity.unsetPurpose(null);
	}

	/**
	 * Sets/creates data for of_scheme.
	 * 
	 * <p>
	 		<aa attribute="of_scheme" assertion_to="Scheme">
				<aimelt>PATH</aimelt>
				<source/>
				<refpath>action_method &lt;-
					action_method_relationship.related_method
					action_method_relationship
					action_method_relationship.description='scheme version of scheme'
					action_method_relationship.relating_method -&gt;
					action_method
					{action_method.purpose='scheme'}
	   </refpath>
			</aa>	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	// AM <- AMR -> AM
	public static void setOf_scheme(SdaiContext context,
			EScheme_version armEntity) throws SdaiException {
		unsetOf_scheme(context, armEntity);
		if(armEntity.testOf_scheme(null)){
			EScheme version = armEntity.getOf_scheme(null);
       	// AMR
       	EAction_method_relationship emr = (EAction_method_relationship)
				context.working_model.createEntityInstance(CAction_method_relationship.definition);
       	emr.setRelated_method(null, armEntity);
       	emr.setRelating_method(null, version);
       	emr.setDescription(null, "scheme version of scheme");
       	emr.setName(null, "");
		}
	}

	/**
	 * Unsets/deletes mapping Resource_items.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetOf_scheme(SdaiContext context,
			EScheme_version armEntity) throws SdaiException {
			AAction_method_relationship amr = new AAction_method_relationship();
			CAction_method_relationship.usedinRelated_method(null, armEntity, context.domain, amr);
			SdaiIterator iter = amr.createIterator();
			while(iter.next()){
				EAction_method_relationship emr = amr.getCurrentMember(iter);
				if((emr.testDescription(null))&&(emr.getDescription(null).equals("scheme version of scheme")))
					emr.deleteApplicationInstance();
			}
	}
	
	
	
}