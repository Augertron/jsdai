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

package jsdai.SPart_and_version_identification_xim;

/**
* @author Giedrius Liutkus
* @version $$
* $$
*/

import jsdai.lang.*;
import jsdai.libutil.*;
import jsdai.SProduct_definition_schema.*;

public class CxRaw_material extends CRaw_material implements EMappedXIMEntity, PartImplementer
{

	public int attributeState = ATTRIBUTES_MODIFIED;	

	// Taken from CProduct
	// methods for attribute: frame_of_reference, base type: SET OF ENTITY
/*	public static int usedinFrame_of_reference(EProduct type, jsdai.SApplication_context_schema.EProduct_context instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a3$, domain, result);
	}
	private jsdai.SApplication_context_schema.AProduct_context getFrame_of_reference2(EProduct type) throws SdaiException {
		return (jsdai.SApplication_context_schema.AProduct_context)get_aggregate(a3);
	}*/
	
	public boolean testFrame_of_reference2(EProduct type) throws SdaiException {
		return test_aggregate(a3);
	}
	public jsdai.SApplication_context_schema.AProduct_context createFrame_of_reference(EProduct type) throws SdaiException {
		a3 = (jsdai.SApplication_context_schema.AProduct_context)create_aggregate_class(a3, a3$,  jsdai.SApplication_context_schema.AProduct_context.class, 0);
		return a3;
	}
	public void unsetFrame_of_reference(EProduct type) throws SdaiException {
		unset_aggregate(a3);
		a3 = null;
	}
	public static jsdai.dictionary.EAttribute attributeFrame_of_reference(EProduct type) throws SdaiException {
		return a3$;
	}
	// ENDOF taken from CProduct
	
	
	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CProduct.definition);

		setMappingConstraints(context, this);
		
		fillAIMGaps(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {

		unsetMappingConstraints(context, this);
		
		unfillAIMGaps(this);
		
	}
	
	
	public void fillAIMGaps(SdaiContext context, EPart armEntity)throws SdaiException{
		CxPart.fillAIMGaps(context, armEntity);
	}
	
	public void unfillAIMGaps(EPart armEntity)throws SdaiException{
		CxPart.unfillAIMGaps(armEntity);
	}
	
	/**
	* Sets/creates data for mapping constraints.
	*
	* <p>
	* mapping_constraints;
	*  {product &lt;-
	*  product_related_product_category.products[i]
	*  product_related_product_category &lt;=
	*  product_category
	*  (product_category.name='part')
	*  (product_category.name='raw material')}	
	* end_mapping_constraints;
	* </p>
	* @param context SdaiContext.
	* @param armEntity arm entity.
	* @throws SdaiException
	*/
	public static void setMappingConstraints(SdaiContext context, EPart armEntity) throws SdaiException
	{
		CxPart.setMappingConstraints(context, armEntity);
	}

	public static void unsetMappingConstraints(SdaiContext context, EPart armEntity) throws SdaiException
	{
		CxPart.unsetMappingConstraints(context, armEntity);		
	}	
	
}
