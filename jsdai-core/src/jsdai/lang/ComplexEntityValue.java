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

package jsdai.lang; 

import jsdai.dictionary.*;

/* This class is used for setAll(), getAll(), prepareAll(). 
	It serves for direct representation of the external mapping in ISO 10303-21. 
*/
/** This class is for internal JSDAI use only. Applications shall not use it. */
public class ComplexEntityValue {

/**
	Definition of the entity data type whose instance is represented by this class.
*/
	CEntity_definition def;

/* An array each element of which represents values assigned to 
	attributes of the corresponding simple entity data type 
	of <code>def</code>. Simple entity data types within <code>def</code>
	are sorted alphabetically by their names.
*/
/** This field is for internal JSDAI use only. Applications shall not use it. */
	public EntityValue entityValues[];

/** This field is for internal JSDAI use only. Applications shall not use it.
 *  @since 4.0.0
 */
	public boolean xim_special_substitute_instance;

	static final int NUMBER_OF_CHARACTERS_IN_COMPL_VALUE = 512;



/**
	The constructor of this class. Used in <code>CAggregate</code>, 
	<code>CEntity</code>, <code>PhFileReader</code>, <code>PhFileWriter</code> 
	and <code>SdaiModel</code>.
*/
	public ComplexEntityValue() {
		entityValues = new EntityValue[SdaiSession.NUMBER_OF_ITEMS_IN_COMPLEX_ENTITY];
	}



/** This method is for internal JSDAI use only. Applications shall not use it. */
	public EntityValue getEntityValue(EEntity_definition part_def) throws SdaiException {
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			if (entityValues[i].def == part_def) {
				return entityValues[i];
			}
		}
// Print message !!!!!!!!!!!!!!!!!! partial entity data type is not found
//		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
//		throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		return null;
	}


	final void unset_ComplexEntityValue() throws SdaiException {
		def = null;
		if (entityValues == null) {
			return;
		}
		for (int i = 0; i < entityValues.length; i++) {
			if (entityValues[i] != null) {
				entityValues[i].unset_EntityValue();
			}
		}
	}


// Method below is for debugging purposes
/*	final void check_references_ComplexEntityValue() throws SdaiException {
		if (entityValues == null) {
			return;
		}
		for (int i = 0; i < entityValues.length; i++) {
			if (entityValues[i] != null) {
				entityValues[i].check_references_EntityValue();
			}
		}
	}*/


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void clear() throws SdaiException {
		def = null;
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public ANamed_type findDataTypes() throws SdaiException {
		ANamed_type types = new ANamed_type();
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			((AEntity)types).addAtTheEnd(def.partialEntityTypes[i], null);
//			types.addByIndex(i + 1, def.partialEntityTypes[i]);
		}
		return types;
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void addEntityValue(EntityValue eval) throws SdaiException {
		int i;
		String new_name = eval.def.getNameUpperCase();
		int ind = -1;
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			int res = new_name.compareTo(entityValues[i].def.getNameUpperCase());
			if (res < 0) {
				ind = i;
				break;
			} else if (res == 0) {
// Print message !!!!!!!!!!!!!!!!!! partial entity data type is included already
//				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR/*, base*/);
			}
		}
		if (ind < 0) {
			ind = def.noOfPartialEntityTypes;
		}
		for (i = def.noOfPartialEntityTypes; i > ind; i--) {
			entityValues[i] = entityValues[i - 1];
		}
		entityValues[ind] = eval;
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void removeEntityValue(EEntity_definition del_def) throws SdaiException {
		int i;
		int ind = -1;
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			if (entityValues[i].def == del_def) {
				ind = i;
				break;
			}
		}
		if (ind < 0) {
// Print message !!!!!!!!!!!!!!!!!! partial entity data type is included already
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR/*, base*/);
		}
		for (i = ind; i < def.noOfPartialEntityTypes - 1; i++) {
			entityValues[i] = entityValues[i + 1];
		}
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void attachEntity(CSchema_definition schema) throws SdaiException {
		SchemaData sch_data = schema.owning_model.schemaData;
		for (int i = 0; i < sch_data.noOfEntityDataTypes; i++) {
			CEntityDefinition def_ch = sch_data.entities[i];
			int start_index = 0;
			int res_index;
			boolean found = true;
			for (int j = 0; j < def.noOfPartialEntityTypes; j++) {
				res_index = def_ch.find_partial_entity(start_index, def_ch.noOfPartialEntityTypes - 1,
					entityValues[j].def.getCorrectName());
				if (res_index < 0) {
					found = false;
					break;
				}
				start_index = res_index + 1;
			}
			if (found) {
				def = (CEntity_definition)def_ch;
				return;
			}
		}
// Print message !!!!!!!!!!!!!!!!!! entity data type not found in the library
//		String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
		throw new SdaiException(SdaiException.SY_ERR/*, base*/);
	}


	public String toString() {
//		synchronized (SdaiCommon.syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
			SdaiSession.printStackTraceToLogWriter(e);
			return super.toString();
		}
//		} // syncObject
	}


/**
	This method is doing a job of the public method <code>toString</code>.
*/
	private String getAsString() throws SdaiException {
		int str_index = 0;

		StaticFields staticFields = StaticFields.get();
		if (staticFields.compl_value_as_string == null) {
			staticFields.compl_value_as_string = new byte[NUMBER_OF_CHARACTERS_IN_COMPL_VALUE];
		}
		staticFields.compl_value_as_string[str_index++] = PhFileReader.LEFT_PARENTHESIS;
		for (int i = 0; i < def.noOfPartialEntityTypes; i++) {
			int str_length = entityValues[i].getAsByteArray(staticFields);
			if (str_index + str_length >= staticFields.compl_value_as_string.length) {
				enlarge_compl_value_string(staticFields, str_index, str_index + str_length);
			}
			for (int j = 0; j < str_length; j++) {
				staticFields.compl_value_as_string[str_index++] = staticFields.ent_value_as_string[j];
			}
		}
		staticFields.compl_value_as_string[str_index++] = PhFileReader.RIGHT_PARENTHESIS;
		return new String(staticFields.compl_value_as_string, 0, str_index);
	}



/**
	Increases the size of the array 'entityValues' either twice or to satisfy 
	the required demand, whichever of these two values is larger. 
	Used in <code>SdaiModel</code> and <code>PhFileReader</code> classes.
*/
	void enlarge(int demand)	{
		int old_length = entityValues.length;
		int new_length = old_length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		SdaiSession session = null;
		if (entityValues[0] != null) {
			session = entityValues[0].owning_session;
		}
		EntityValue [] new_entityValues = new EntityValue[new_length];
		System.arraycopy(entityValues, 0, new_entityValues, 0, old_length);
		entityValues = new_entityValues;
		for (int i = 0; i < demand; i++) {
			if (entityValues[i] == null) {
				entityValues[i] = new EntityValue(session);
			}
		}
	}


	private void enlarge_compl_value_string(StaticFields staticFields, int str_length, int demand) {
		int new_length = staticFields.compl_value_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_compl_value_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.compl_value_as_string, 0, new_compl_value_as_string, 0, str_length);
		staticFields.compl_value_as_string = new_compl_value_as_string;
	}


}
