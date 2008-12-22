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

package jsdai.SPre_defined_product_data_management_specializations_xim;

/**
 * 
 * @author Giedrius Liutkus
 * @version $$
 */

import jsdai.lang.*;
import jsdai.libutil.EMappedXIMEntity;
import jsdai.util.LangUtils;
import jsdai.SSecurity_classification_schema.CSecurity_classification;
import jsdai.SSecurity_classification_schema.CSecurity_classification_level;
import jsdai.SSecurity_classification_schema.ESecurity_classification;
import jsdai.SSecurity_classification_schema.ESecurity_classification_level;

public class CxSecurity_classification__unclassified  extends CSecurity_classification__unclassified implements EMappedXIMEntity{


	// Taken from SC
	/// methods for attribute: name, base type: STRING
	/*	public boolean testName(ESecurity_classification type) throws SdaiException {
			return test_string(a0);
		}
		public String getName(ESecurity_classification type) throws SdaiException {
			return get_string(a0);
		} */
		public void setName(ESecurity_classification type, String value) throws SdaiException {
			a0 = set_string(value);
		}
		public void unsetName(ESecurity_classification type) throws SdaiException {
			a0 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributeName(ESecurity_classification type) throws SdaiException {
			return a0$;
		}

		//going through all the attributes: #5629499534230883=EXPLICIT_ATTRIBUTE('purpose',#5629499534230880,1,#5629499534229328,$,.F.);
		//<01> generating methods for consolidated attribute:  purpose
		//<01-0> current entity
		//<01-0-0> explicit attribute - generateExplicitCurrentEntityMethodsX()
		/// methods for attribute: purpose, base type: STRING
	/*	public boolean testPurpose(ESecurity_classification type) throws SdaiException {
			return test_string(a1);
		}
		public String getPurpose(ESecurity_classification type) throws SdaiException {
			return get_string(a1);
		} */
		public void setPurpose(ESecurity_classification type, String value) throws SdaiException {
			a1 = set_string(value);
		}
		public void unsetPurpose(ESecurity_classification type) throws SdaiException {
			a1 = unset_string();
		}
		public static jsdai.dictionary.EAttribute attributePurpose(ESecurity_classification type) throws SdaiException {
			return a1$;
		}
		
		// attribute (current explicit or supertype explicit) : security_level, base type: entity security_classification_level
	/*	public static int usedinSecurity_level(ESecurity_classification type, ESecurity_classification_level instance, ASdaiModel domain, AEntity result) throws SdaiException {
			return ((CEntity)instance).makeUsedin(definition, a2$, domain, result);
		}
		public boolean testSecurity_level(ESecurity_classification type) throws SdaiException {
			return test_instance(a2);
		}
		public ESecurity_classification_level getSecurity_level(ESecurity_classification type) throws SdaiException {
			return (ESecurity_classification_level)get_instance(a2);
		} */
		public void setSecurity_level(ESecurity_classification type, ESecurity_classification_level value) throws SdaiException {
			a2 = set_instance(a2, value);
		}
		public void unsetSecurity_level(ESecurity_classification type) throws SdaiException {
			a2 = unset_instance(a2);
		}
		public static jsdai.dictionary.EAttribute attributeSecurity_level(ESecurity_classification type) throws SdaiException {
			return a2$;
		}

	// ENDOF taken from SC
	
	public int attributeState = ATTRIBUTES_MODIFIED;	

	public void createAimData(SdaiContext context) throws SdaiException {
		if (attributeState == ATTRIBUTES_MODIFIED) {
			attributeState = ATTRIBUTES_UNMODIFIED;
		} else {
			return;
		}

		setTemp("AIM", CSecurity_classification.definition);

		setMappingConstraints(context, this);

	}

	public void removeAimData(SdaiContext context) throws SdaiException {
			unsetMappingConstraints(context, this);
			
	}

	/**
	 * Sets/creates data for mapping constraints.
	 * 
	 * <p>
	 * 		security_classification
			{security_classification.security_level ->
			security_classification_level
			security_classification_level.name = 'unclassified'}
	 * </p>
	 * 
	 * @param context
	 *            SdaiContext.
	 * @param armEntity
	 *            arm entity.
	 * @throws SdaiException
	 */
	public static void setMappingConstraints(SdaiContext context,
			ESecurity_classification__unclassified armEntity) throws SdaiException {
		unsetMappingConstraints(context, armEntity);
		CxPre_defined_security_classification.setMappingConstraints(context, armEntity);
	      // SCL
	      LangUtils.Attribute_and_value_structure[] sclStructure = {
	         new LangUtils.Attribute_and_value_structure(
	         CSecurity_classification_level.attributeName(null),
	         "unclassified")
	      };
	      ESecurity_classification_level escl = (ESecurity_classification_level)
	         LangUtils.createInstanceIfNeeded(context, CSecurity_classification_level.definition, sclStructure);
	      armEntity.setSecurity_level(null, escl);
	}

	/**
	 * Unsets/deletes mapping constraint data.
	 * 
	 * @param context
	 * @param armEntity
	 * @throws SdaiException
	 */
	public static void unsetMappingConstraints(SdaiContext context,
			ESecurity_classification__unclassified armEntity) throws SdaiException {
		CxPre_defined_security_classification.setMappingConstraints(context, armEntity);
		armEntity.unsetSecurity_level(null);
	}

	
}