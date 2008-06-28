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

package jsdai.SComponent_grouping_xim;

/**
 * 
 * @author Valdas Zigas, Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SComponent_grouping_mim.CLinear_array_component_definition_link;
import jsdai.SComponent_grouping_mim.ERectangular_array_placement_group_component;
import jsdai.SProduct_definition_schema.*;

public class CxLinear_array_placement_group_component_link extends CLinear_array_placement_group_component_link implements EMappedXIMEntity{

	/// methods for attribute: description, base type: STRING
/*	public boolean testDescription(EProduct_definition type) throws SdaiException {
		return test_string(a1);
	}
	public String getDescription(EProduct_definition type) throws SdaiException {
		return get_string(a1);
	}*/
	public void setDescription(EProduct_definition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetDescription(EProduct_definition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeDescription(EProduct_definition type) throws SdaiException {
		return a1$;
	}

	/// methods for attribute: id, base type: STRING
/*	public boolean testId(EProduct_definition type) throws SdaiException {
		return test_string(a0);
	}
	public String getId(EProduct_definition type) throws SdaiException {
		return get_string(a0);
	}*/
	public void setId(EProduct_definition type, String value) throws SdaiException {
		a0 = set_string(value);
	}
	public void unsetId(EProduct_definition type) throws SdaiException {
		a0 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeId(EProduct_definition type) throws SdaiException {
		return a0$;
	}

	// attribute (current explicit or supertype explicit) : formation, base type: entity product_definition_formation
/*	public static int usedinFormation(EProduct_definition type, EProduct_definition_formation instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
	}
	public boolean testFormation(EProduct_definition type) throws SdaiException {
		return test_instance(a2);
	}
	public EProduct_definition_formation getFormation(EProduct_definition type) throws SdaiException {
		a2 = get_instance(a2);
		return (EProduct_definition_formation)a2;
	}*/
	public void setFormation(EProduct_definition type, EProduct_definition_formation value) throws SdaiException {
		a2 = set_instance(a2, value);
	}
	public void unsetFormation(EProduct_definition type) throws SdaiException {
		a2 = unset_instance(a2);
	}
	public static jsdai.dictionary.EAttribute attributeFormation(EProduct_definition type) throws SdaiException {
		return a2$;
	}

	// attribute (current explicit or supertype explicit) : frame_of_reference, base type: entity product_definition_context
/*	public static int usedinFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	public boolean testFrame_of_reference(EProduct_definition type) throws SdaiException {
		return test_instance(a3);
	}
	public jsdai.SApplication_context_schema.EProduct_definition_context getFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = get_instance(a3);
		return (jsdai.SApplication_context_schema.EProduct_definition_context)a3;
	}*/
	public void setFrame_of_reference(EProduct_definition type, jsdai.SApplication_context_schema.EProduct_definition_context value) throws SdaiException {
		a3 = set_instance(a3, value);
	}
	public void unsetFrame_of_reference(EProduct_definition type) throws SdaiException {
		a3 = unset_instance(a3);
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct_definition type) throws SdaiException {
		return a3$;
	}
	
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CLinear_array_component_definition_link.definition);

			setMappingConstraints(context, this);
			
			// Kind of AIM gap
			// setDefinition(null, this);

			//********** "design_discipline_item_definition" attributes
	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
	}

	//		************************************* AUTOMOTIVE_DESIGN
	// ***************************************************************

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ELinear_array_placement_group_component_link armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		// AIM gaps
		ARectangular_array_placement_group_component_armx users = armEntity.getReferenced_by(null, null);
		EProduct_definition user = null;
		if(users.getMemberCount() != 1){
			// Maybe on XIM it is already unset so go via AIM
			AProduct_definition_relationship apdr = new AProduct_definition_relationship();
			CProduct_definition_relationship.usedinRelated_product_definition(null, armEntity, context.domain, apdr);
			for(int i=1,count=apdr.getMemberCount(); i<=count; i++){
				EProduct_definition_relationship epdr = apdr.getByIndex(i);
				if(epdr.testName(null) && epdr.getName(null).equals("element")){
					user = (ERectangular_array_placement_group_component)epdr.getRelating_product_definition(null);
					break;
				}
			}
			if(user == null){
				System.err.println("WARNING: Linear_array_placement_group_component_link shall have exactly one user by inverse Referenced_by "+armEntity+" count = "+users.getMemberCount()+" "+apdr.getMemberCount());
				System.err.println(context.working_model.getRepository().getSessionIdentifier("#33013"));
			}
		}else{
			if(user == null){
				user = users.getByIndex(1);
				ERectangular_array_placement_group_component_armx erapgca = (ERectangular_array_placement_group_component_armx)
					user;
				user = erapgca.getDerived_from(null); 
			}
			armEntity.setFormation(null, user.getFormation(null));
			armEntity.setFrame_of_reference(null, user.getFrame_of_reference(null));
			armEntity.setId((EProduct_definition)null, user.getId((EProduct_definition)null));
		}
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ELinear_array_placement_group_component_link armEntity) throws SdaiException {
		// Unset AIM gaps
		armEntity.unsetFormation(null);
		armEntity.unsetFrame_of_reference(null);
		armEntity.unsetId((EProduct_definition)null);
		
	}

}