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

// Java class implementing entity map_or_view_partition

package jsdai.SExtended_dictionary_schema;
import jsdai.lang.*;

public class CMap_or_view_partition extends CEntity implements EMap_or_view_partition {
	public static final jsdai.dictionary.CEntity_definition definition = initEntityDefinition(CMap_or_view_partition.class, SExtended_dictionary_schema.ss);

	/*----------------------------- Attributes -----------*/

	protected Object a0; // parent - current entity - SELECT map_or_view_definition_select
	protected static final jsdai.dictionary.CExplicit_attribute a0$ = CEntity.initExplicitAttribute(definition, 0);
	protected String a1; // name - current entity - STRING
	protected static final jsdai.dictionary.CExplicit_attribute a1$ = CEntity.initExplicitAttribute(definition, 1);
	// source_parameters: protected Object  - inverse - current -  ENTITY source_parameter
	protected static final jsdai.dictionary.CInverse_attribute i0$ = CEntity.initInverseAttribute(definition, 0);

	public jsdai.dictionary.EEntity_definition getInstanceType() {
		return definition;
	}

	protected void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (a0 == old) {
			a0 = newer;
		}
	}

	/*----------- Methods for attribute access -----------*/

	// methods for SELECT attribute: parent
	public static int usedinParent(EMap_or_view_partition type, EEntity instance, ASdaiModel domain, AEntity result) throws SdaiException {
		return ((CEntity)instance).makeUsedin(definition, a0$, domain, result);
	}
	public boolean testParent(EMap_or_view_partition type) throws SdaiException {
		return test_instance(a0);
	}

	public EEntity getParent(EMap_or_view_partition type) throws SdaiException { // case 1
		a0 = get_instance_select(a0);
		return (EEntity)a0;
	}

	public void setParent(EMap_or_view_partition type, EEntity value) throws SdaiException { // case 1
		a0 = set_instance(a0, value);
	}

	public void unsetParent(EMap_or_view_partition type) throws SdaiException {
		a0 = unset_instance(a0);
	}

	public static jsdai.dictionary.EAttribute attributeParent(EMap_or_view_partition type) throws SdaiException {
		return a0$;
	}

	/// methods for attribute: name, base type: STRING
	public boolean testName(EMap_or_view_partition type) throws SdaiException {
		return test_string(a1);
	}
	public String getName(EMap_or_view_partition type) throws SdaiException {
		return get_string(a1);
	}
	public void setName(EMap_or_view_partition type, String value) throws SdaiException {
		a1 = set_string(value);
	}
	public void unsetName(EMap_or_view_partition type) throws SdaiException {
		a1 = unset_string();
	}
	public static jsdai.dictionary.EAttribute attributeName(EMap_or_view_partition type) throws SdaiException {
		return a1$;
	}

	// Inverse attribute - source_parameters : SET[0:-2147483648] OF source_parameter FOR parent
	public ASource_parameter getSource_parameters(EMap_or_view_partition type, ASdaiModel domain) throws SdaiException {
		ASource_parameter result = (ASource_parameter)get_inverse_aggregate(i0$);
		CSource_parameter.usedinParent(null, this, domain, result);
		return result;
	}
	public static jsdai.dictionary.EAttribute attributeSource_parameters(EMap_or_view_partition type) throws SdaiException {
		return i0$;
	}


	/*---------------------- setAll() --------------------*/

	protected void setAll(ComplexEntityValue av) throws SdaiException {
		if (av == null) {
			a0 = unset_instance(a0);
			a1 = null;
			return;
		}
		a0 = av.entityValues[0].getInstance(0, this, a0$);
		a1 = av.entityValues[0].getString(1);
	}

	/*---------------------- getAll() --------------------*/

	protected void getAll(ComplexEntityValue av) throws SdaiException {
		// partial entity: map_or_view_partition
		av.entityValues[0].setInstance(0, a0);
		av.entityValues[0].setString(1, a1);
	}
}
