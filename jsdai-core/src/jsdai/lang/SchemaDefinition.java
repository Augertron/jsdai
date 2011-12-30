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

/** It is a supertype of the class <code>CSchema_definition</code> contained in the 
 * <code>jsdai.dictionary</code> package. This class is designed primarily 
 * to hold some non-public data fields. Also, the public method 
 * {@link #getEntityDefinition getEntityDefinition} defined in
 * "ISO 10303-22: Product data representation and exchange: Standard data access interface"
 * and additional public method {@link #getDefinedType getDefinedType}, 
 * which is not a part of the standard, are implemented here. 
 */
public abstract class SchemaDefinition extends CEntity {
	private int [] subtypes []; 
	protected SdaiModel modelDictionary;

	private int [] temp [];
	private int [] count;
	private boolean [] resolved;
	private AEntity_definition entities;

	static final int NUMBER_OF_SUBTYPES = 8;


	protected SchemaDefinition() {
		super();
	}



/**
 * Checks whether there exists entity definition with the specified name.
 * This method searches the set of entities that are encountered in the
 * EXPRESS schema represented by this <code>ESchema_definition</code>.
 * Method's parameter is some string giving the name of an entity. 
 * If this entity is complex, then its constituents (simple entity data 
 * types) within this string are separated by the '+' character.
 * For example, "length_unit+si_unit". Both lower case letters and 
 * upper case letters are acceptable. If the entity is not known for this schema,
 * then <code>false</code> value is returned. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param entity_name the name of the entity whose definition is checked for inclusion in the schema.
 * @return <code>true</code> if the specified entity definition was found, <code>false</code> otherwise.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getEntityDefinition
 */
	public boolean testEntityDefinition(String entity_name) throws SdaiException {
		if (entity_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		entity_name = entity_name.replace('+', '$').toUpperCase();
		SchemaData sch_data = modelDictionary.schemaData;
		int index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
		if (index >= 0) {
			return true;
		}
		return false;
	}

/**
 * Returns entity definition for the entity with the specified name.
 * This method searches the set of entities that are encountered in the
 * EXPRESS schema represented by this <code>ESchema_definition</code>.
 * Method's parameter is some string giving the name of an entity. 
 * If this entity is complex, then its constituents (simple entity data 
 * types) within this string are separated by '+' character.
 * For example, "length_unit+si_unit". Both lower case letters and 
 * upper case letters are acceptable. If this parameter does not
 * allow to identify an entity that is known for this schema,
 * then SdaiException ED_NDEF is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param entity_name the name of the entity whose definition is requested.
 * @return definition of the specified entity.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see SdaiModel#getEntityDefinition
 * @see "ISO 10303-22::10.7.8 Get entity definition"
 */
	public EEntity_definition getEntityDefinition(String entity_name) throws SdaiException {
//		synchronized (syncObject) {
		if (entity_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		entity_name = entity_name.replace('+', '$').toUpperCase();
		if (entities == null) {
			getEntities();
		}
		SchemaData sch_data = modelDictionary.schemaData;
		int index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
		if (index >= 0) {
			if (((AEntity)entities).myLength == 1) {
				return (CEntity_definition)((AEntity)entities).myData;
			} else {
				Object [] myDataA = (Object [])((AEntity)entities).myData;
				return (CEntity_definition)myDataA[index];
			}
		} else {
			throw new SdaiException(SdaiException.ED_NDEF, "Entity name: " + entity_name);
		}
//		} // syncObject
	}

	EEntity_definition getEntityDefinitionFast(String entity_name) throws SdaiException {
//		synchronized (syncObject) {
		entity_name = entity_name.replace('+', '$').toUpperCase();
		if (entities == null) {
			getEntities();
		}
		SchemaData sch_data = modelDictionary.schemaData;
		int index = sch_data.find_entity(0, sch_data.sNames.length - 1, entity_name);
		if (((AEntity)entities).myLength == 1) {
			return index >= 0 ? (EEntity_definition)((AEntity)entities).myData : null;
		} else {
			Object [] myDataA = (Object [])((AEntity)entities).myData;
			return index >= 0 ? (EEntity_definition)myDataA[index] : null;
		}
//		} // syncObject
	}

	EEntity_definition getEntityDefinition(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (entities == null) {
			getEntities();
		}
		if (((AEntity)entities).myLength == 1) {
			return index >= 0 ? (EEntity_definition)((AEntity)entities).myData : null;
		} else {
			Object [] myDataA = (Object [])((AEntity)entities).myData;
			return index >= 0 ? (EEntity_definition)myDataA[index] : null;
		}
//		} // syncObject
	}


/**
 * Returns defined type with the specified name.
 * This method searches the set of defined types that are encountered in the
 * EXPRESS schema represented by this <code>ESchema_definition</code>.
 * Method's parameter is some string giving the name of a defined type. 
 * Both lower case letters and upper case letters are acceptable. 
 * If this parameter does not
 * allow to identify a defined type that is known for this schema,
 * then SdaiException VA_NVLD is thrown. Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param type_name the name of the requested defined type.
 * @return defined type.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 */
	public EDefined_type getDefinedType(String type_name) throws SdaiException {
//		synchronized (syncObject) {
		if (type_name == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		type_name = type_name.toUpperCase();
		SchemaData sch_data = modelDictionary.schemaData;
		if (sch_data.defTypesCount < 0) {
			sch_data.initializeDefinedTypes();
		}
		int index = sch_data.find_type(0, sch_data.defTypesCount - 1, type_name);
		if (index >= 0) {
			return sch_data.def_types[index];
		} else {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
//		} // syncObject
	}

	EDefined_type getDefinedTypeFast(String type_name) throws SdaiException {
//		synchronized (syncObject) {
		type_name = type_name.toUpperCase();
		SchemaData sch_data = modelDictionary.schemaData;
		if (sch_data.defTypesCount < 0) {
			sch_data.initializeDefinedTypes();
		}
		int index = sch_data.find_type(0, sch_data.defTypesCount - 1, type_name);
		return index >= 0 ? sch_data.def_types[index] : null;
//		} // syncObject
	}


	protected AEntity_definition getEntities() throws jsdai.lang.SdaiException {
		if (entities == null) {
			entities = new AEntity_definition();
			AEntity ents = (AEntity)entities;
			ents.myType = SdaiSession.setType0toN;
			ents.owner = this;
if (SdaiSession.debug2) System.out.println("  SchemaDefinition    schema: " + ((ESchema_definition)this).getName(null));
if (SdaiSession.debug2) System.out.println("  In SchemaDefinition    modelDictionary: " + modelDictionary.name);
			SchemaData data = modelDictionary.schemaData;
			if (data.entities.length == 1) {
				ents.myData = data.entities[0];
				ents.myLength = 1;
				return entities;
			}
			if (ents.myData == null) {
				ents.myData = new Object[data.entities.length];
			}
			Object [] myDataA = (Object [])ents.myData;
			System.arraycopy(data.entities, 0, myDataA, 0, data.entities.length);
			ents.myLength = data.entities.length;
		}
		return entities;
	}


	protected int[] getSubtypes(int index) throws SdaiException {
		synchronized(this) {
			if (subtypes == null) {
				prepareSubtypesDelayed();
			}
			if (subtypes[index] == null) {
				SchemaData data = modelDictionary.schemaData;
				if (data.entities[index].complex != 2) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				int n_def = data.getSubtypesComplex(index);
				subtypes[index] = new int[n_def];
				for (int i = 0; i < n_def; i++) {
					subtypes[index][i] = data.aux[i];
				}
			}
		}
		return subtypes[index];
	}


	private void prepareSubtypesDelayed() throws SdaiException {
		int i, j;
		int index;
//System.out.println("  SchemaDefinition   modelDictionary: " + modelDictionary.name);
		SchemaData data = modelDictionary.schemaData;
		if (modelDictionary.name.equals("MIXED_COMPLEX_TYPES_DICTIONARY_DATA")) {
			subtypes = new int[data.entities.length][];
//for (i = 0; i < data.entities.length; i++) {
//CEntity_definition entt = (CEntity_definition)data.entities[i];
//data.getSubtypesComplex(i);}
			return;
		}
		temp = new int[data.entities.length][];
		count = new int[data.entities.length];
		resolved = new boolean[data.entities.length];
		for (i = 0; i < data.entities.length; i++) {
			CEntity_definition entity = (CEntity_definition)data.entities[i];
			AEntity_definition supertypes = entity.getSupertypes(null);
			for (j = 1; j <= ((AEntity)supertypes).myLength; j++) {
				CEntityDefinition super_entity = (CEntityDefinition)supertypes.getByIndex(j);
				SchemaData sch_data = super_entity.owning_model.schemaData;
				if (sch_data == data) {
					index = super_entity.index;
				} else {
					index = data.find_entity(0, data.sNames.length - 1, 
						super_entity);

//System.out.println(" SchemaDefinition   case2   index = " + index + 
//"  name searched: " + super_entity.getNameUpperCase()); 
//if (index < 0) {System.out.println("  SchemaDefinition    index = " + i + 
//"   !!!!entity: " + entity.getCorrectName() + 
//"   supertypes.myLength: " + supertypes.myLength);
//System.out.println("*****MODEL: " + modelDictionary.name/* + "    SCHEMA: " + schema.getName(null)*/);
/*if (index < 0) {
System.out.println("modelDictionary: " + modelDictionary.name);
System.out.println("entity: " + entity + "  i: " + i);
System.out.println("name original: " + super_entity.getCorrectName());
System.out.println("name searched: " + super_entity.getNameUpperCase());
System.out.println("*****Schema: " + data.schema.getName(null));
System.out.println();
for (int s = 0; s < data.noOfEntityDataTypes; s++) {
System.out.println("s=" + s + "    *****NORMAL NAME: " + data.sNames[s] + 
"   LONG NAME: " + data.sLongNames[s]);
}System.out.println(); }*/

				}
				if (index < 0) {
						String base = SdaiSession.line_separator + "Entity not found: " +
							super_entity.getNameUpperCase();
						throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (temp[index] == null) {
					temp[index] = new int[NUMBER_OF_SUBTYPES];
				}
				if (contained(index, i)) {
					continue;
				}
				if (count[index] >= temp[index].length) {
					enlarge_temp(index);
				}
				temp[index][count[index]] = i;
				count[index]++;
			}
		}
		subtypes = new int[data.entities.length][];
		for (i = 0; i < data.entities.length; i++) {
if (SdaiSession.debug2) 
System.out.println(" ind = " + i + "   ENTITY: " + data.entities[i].getCorrectName()
+ "    data.model.name = " + data.model.name);
			if (data.entities[i].complex == 2) {
				continue;
			}
			for (j = 0; j < count[i]; j++) {
				goDown(i, temp[i][j]);
			}
			resolved[i] = true;
			boolean mark = true;
			int k = count[i] - 2;
			while (mark) {
				mark = false;
				for (j = 0; j <= k; j++) {
					if (temp[i][j] > temp[i][j + 1]) {
						index = temp[i][j];
						temp[i][j] = temp[i][j + 1];
						temp[i][j + 1] = index;
						mark = true;
					}
				}
				k--;
			}
			subtypes[i] = new int[count[i]];
if (SdaiSession.debug2) System.out.println(" index = " + i + "   !!!!entity: " + data.entities[i].getCorrectName());
if (SdaiSession.debug) System.out.print(" subtypes (count = " + count[i] + ") : ");
			for (j = 0; j < count[i]; j++) {
				subtypes[i][j] = temp[i][j];
if (SdaiSession.debug) System.out.print("  " + subtypes[i][j]);
			}
if (SdaiSession.debug) System.out.println();
		}
		temp = null;
		count = null;
		resolved = null;
	}


	private boolean contained(int index, int candidate) throws SdaiException {
		for (int i = 0; i < count[index]; i++) {
			if (temp[index][i] == candidate) {
				return true;
			}
		}
		return false;
	}
	private void goDown(int root, int node) throws SdaiException {
		for (int i = 0; i < count[node]; i++) {
			if (contained(root, temp[node][i])) {
				continue;
			}
			if (count[root] >= temp[root].length) {
				enlarge_temp(root);
			}
			temp[root][count[root]] = temp[node][i];
			count[root]++;
			if (!(resolved[temp[node][i]])) {
				goDown(root, temp[node][i]);
			}
		}
	}


	void enlarge_temp(int index) {
		int new_length = temp[index].length * 2;
		int [] new_array = new int[new_length];
		System.arraycopy(temp[index], 0, new_array, 0, temp[index].length);
		temp[index] = new_array;
	}

}

