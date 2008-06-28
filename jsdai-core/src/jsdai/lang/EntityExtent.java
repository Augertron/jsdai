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

/**
 * The EntityExtent class groups all entity instances of an <code>SdaiModel</code>
 * into folders. There is one instance of EntityExtent for each entity data type defined
 * or declared in the EXPRESS schema whose definition is underlying for the
 * <code>SdaiModel</code> of interest. A folder corresponding to an entity data
 * type, specified by its definition, contains all instances of this entity
 * data type from the model being an owner of this folder and also all instances
 * of all its subtypes from the same model.
 * <BR>For details please look at "ISO 10303-22::8.4.3 sdai_model_contents" and
 * "ISO 10303-22::8.4.4 entity_extent".
 */
public class EntityExtent extends SdaiCommon {
	CEntityDefinition definition;
	SdaiModel owned_by;
	int index_to_entity;



	SdaiCommon getOwner() {
		return owned_by;
	}

	void modified() throws SdaiException {
		// dummy, skip
	}



	EntityExtent(CEntity_definition definition_supplied, SdaiModel owner, int index) 
			throws  SdaiException {
		definition = definition_supplied;
		owned_by = owner;
		index_to_entity = index;
	}



/**
 * Returns the instance of <code>EEntity_definition</code> in
 * "SDAI_dictionary_schema", which represents 
 * the type of entity instances contained in this folder.
 * <P><B>Example:</B>
 * <P><TT><pre>    EntityExtent folder = ...;
 *    EEntity_definition def = folder.getDefinition();</TT></pre>
 * <P>
 * @return definition of entity whose instances are contained in this folder.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @see #getDefinitionString
 */
	public EEntity_definition getDefinition() throws SdaiException {
		if (owned_by == null) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return definition;
	}


/**
 * Returns the name of <code>EEntity_definition</code> that represents 
 * the type of entity instances contained in this folder.
 * <P><B>Example:</B>
 * <P><TT><pre>    EntityExtent folder = ...;
 *    String def_name = folder.getDefinitionString();</TT></pre>
 * <P>
 * @return the name of entity whose instances are contained in this folder.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @see #getDefinition
 */
	public String getDefinitionString() throws SdaiException {
		if (owned_by == null) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return definition.name;
	}


/**
 * Returns a read-only aggregate containing all instances from this folder.
 * The aggregate consists of all instances of the entity data type represented by
 * this <code>EntityExtent</code> and of all instances of all its subtypes.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * <P><B>Example:</B>
 * <P><TT><pre>    EntityExtent folder = ...;  // for "cartesian point"
 *    ACartesian_point aggr = (ACartesian_point)folder.getInstances();</pre></TT>
 * @return aggregate containing all instances of this <code>EntityExtent</code>.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiModel#getInstances(EEntity_definition type)
 */ 
	public AEntity getInstances() throws SdaiException {
//		synchronized (syncObject) {
		if (owned_by == null) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return owned_by.getInstances(definition);
//		} // syncObject
	}


/**
 * Returns <code>SdaiModel</code> this <code>EntityExtent</code> belongs to. 
 * The entity instances in an <code>EntityExtent</code> are only available
 * when model being an owner of this <code>EntityExtent</code>
 * is either in read-only or read-write access mode. 
 * <P><B>Example:</B>
 * <P><TT><pre>    EntityExtent folder = ...;
 *    SdaiModel owner = folder.getOwnedBy();</TT></pre>
 * <P>
 * @return <code>SdaiModel</code> that contains this <code>EntityExtent</code>.
 * @throws SdaiException ED_NVLD, entity definition invalid.
 */
	public SdaiModel getOwnedBy() throws SdaiException {
		if (owned_by == null) {
			throw new SdaiException(SdaiException.ED_NVLD);
		}
		return owned_by;
	}


/**
 * Returns a <code>String</code> representing this <code>EntityExtent</code>.
 * This string consists of the name of entity definition represented by
 * this <code>EntityExtent</code> and a list of persistent labels of all
 * instances contained in the folder (as returned by
 * <code>getInstances</code> method).
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiModel m = ...;
 *    AEntityExtent pop_folders = m.getPopulatedFolders();
 *    EntityExtent folder = (EntityExtent)pop_folders.getByIndexObject(1);
 *    System.out.println(folder);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    vector: #55,#97,#125,#378</pre>
 * @return the <code>String</code> representing this <code>EntityExtent</code>.
 * @see #getInstances
 * @see EEntity#getPersistentLabel
 */
	public String toString() {
//		synchronized (syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
			return super.toString();
		}
//		} // syncObject
	}

	String getAsString() throws SdaiException {
		int i;
		int str_index = -1;
		SdaiSession session = owned_by.repository.session;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instance_as_string_extent == null) {
			staticFields.instance_as_string_extent = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		String name = definition.name;
		int ln = name.length();
		if (ln + 2 > staticFields.instance_as_string_extent.length) {
			staticFields.instance_as_string_extent = enlarge_instance_string(staticFields.instance_as_string_extent, 0, ln + 2);
		}
		for (i = 0; i < ln; i++) {
			staticFields.instance_as_string_extent[++str_index] = (byte)name.charAt(i);
		}
		staticFields.instance_as_string_extent[++str_index] = PhFileReader.COLON;
		staticFields.instance_as_string_extent[++str_index] = PhFileReader.SPACE;
		boolean first = true;
		AEntity aggr = owned_by.getInstances(definition);
		for (i = 1; i <= aggr.getMemberCount(); i++) {
			CEntity inst = (CEntity)aggr.getByIndexObject(i);
			if (str_index + 2 >= staticFields.instance_as_string_extent.length) {
				staticFields.instance_as_string_extent = 
					enlarge_instance_string(staticFields.instance_as_string_extent, str_index + 1, str_index + 3);
			}
			if (!first) {
				staticFields.instance_as_string_extent[++str_index] = PhFileReader.COMMA_b;
			}
			first = false;
			staticFields.instance_as_string_extent[++str_index] = PhFileReader.SPECIAL;
			str_index = identifier_to_byte_array(staticFields, inst.instance_identifier, str_index);
		}
		return new String(staticFields.instance_as_string_extent, 0, str_index + 1);
	}


	private int identifier_to_byte_array(StaticFields staticFields, long lo, int index) throws SdaiException {
		long next_number;
		int initial_index = index;
		while (lo != 0) {
			next_number = lo / 10;
			if (index + 1 >= staticFields.instance_as_string_extent.length) {
				staticFields.instance_as_string_extent = 
					enlarge_instance_string(staticFields.instance_as_string_extent, index + 1, index + 2);
			}
			staticFields.instance_as_string_extent[++index] = PhFileWriter.DIGITS[(int)(lo - next_number * 10)];
			lo = next_number;
		}
		for (int i = initial_index + 1;
				i <= initial_index + (index - initial_index) / 2; i++) {
			byte sym = staticFields.instance_as_string_extent[i];
			staticFields.instance_as_string_extent[i] = staticFields.instance_as_string_extent[index - i + initial_index + 1];
			staticFields.instance_as_string_extent[index - i + initial_index + 1] = sym;
		}
		return index;
	}


	byte [] enlarge_instance_string(byte [] instance_as_string, int str_length, int demand) {
		int new_length = instance_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_as_string[] = new byte[new_length];
		System.arraycopy(instance_as_string, 0, new_instance_as_string, 0, str_length);
		return new_instance_as_string;
	}

}
