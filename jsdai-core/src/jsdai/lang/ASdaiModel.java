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

import java.io.OutputStream;
import jsdai.client.*;
import jsdai.dictionary.*;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.xml.InstanceReader;
import jsdai.xml.SdaiInputSource;


/**
 Specialized class implementing <code>Aggregate</code> for members of
 type <code>SdaiModel</code>. See <a href="Aggregate.html">Aggregate</a> for detailed
 description of methods whose specializations are given here.
 */
public class ASdaiModel extends SessionAggregate {

	ListElement to_mod;
	int to_mod_set;
	int ext_index;
	int count;


/**
 * Constructs a new object of aggregate <code>ASdaiModel</code>
 * without aggregation type. The aggregates of such kind are used
 * as non-persistent lists.
 * @see Aggregate#getAggregationType
 */
	public ASdaiModel() {
		super();
	}


	ASdaiModel(EAggregation_type provided_type, SdaiCommon instance) {
		super(provided_type, instance);
	}



/**
 * It is a specialization of
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>SdaiModel</code> and the
 * second parameter is dropped.
 */
	public boolean isMember(SdaiModel value) throws SdaiException {
		return isMember(value, null);
	}


/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type <code>SdaiModel</code> instead of <code>Object</code>.
 */
  public SdaiModel getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				SdaiIterator iter = createIterator();
				for (int i = 0; i < index; i++) {
					iter.next();
				}
				return getCurrentMember(iter);
			}
		}
		return (SdaiModel)getByIndexObject(index);
//		} // syncObject
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			setByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			addByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


/**
 * It is a specialization of
 * {@link Aggregate#addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>SdaiModel</code>
 * and the third parameter is dropped.
 */
	public void addByIndex(int index, SdaiModel value) throws SdaiException {
		addByIndexCommon(index, value);
	}


	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			addUnorderedCommon(value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


//	public SdaiModel getCurrentMember(SdaiIterator iter) throws SdaiException {
//		return (SdaiModel)getCurrentMemberObject(iter);
//	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			setCurrentMemberCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			addBeforeCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (value instanceof SdaiModel) {
			addAfterCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}


/**
 * Returns a read-only aggregate containing all instances of all started
 * <code>SdaiModel</code>s in this <code>ASdaiModel</code>.
 * The models whose access is ended at the time of invocation
 * of this method are bypassed.
 * @return aggregate containing all instances of this <code>ASdaiModel</code>.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 */
	public AEntity getInstances() throws SdaiException {
//		synchronized (syncObject) {
		AEntity allInstancesInModels = new AEntity();
		allInstancesInModels.myData = this;
		to_mod = null;
		to_mod_set = -1;
		allInstancesInModels.myLength = myLength;
		allInstancesInModels.myType = SdaiSession.setTypeForInstancesListOfModelAll;
		// Ensure correctness for partial models
		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
						model.provideAllInstancesIfNeeded();
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
						model.provideAllInstancesIfNeeded();
					}
				}
			}
		}
		return allInstancesInModels;
//		} // syncObject
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type and all instances of all its subtypes
 * collected going through all started <code>SdaiModel</code>s in this
 * <code>ASdaiModel</code>.
 * If this entity type is not defined or declared in the
 * EXPRESS schema whose definition is underlying for a model, then
 * such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel maggr = ...;
 *    EEntity_definition cp_type = ...;  // cartesian point
 *    ACartesian_point aggr = (ACartesian_point)maggr.getInstances(cp_type);</pre></TT>
 * @param type definition of the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type taken from all started <code>SdaiModel</code>s
 * in this <code>ASdaiModel</code>.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(Class type)
 * @see SdaiModel#getInstances(EEntity_definition type)
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 */
/*	public AEntity getInstances(EEntity_definition type) throws SdaiException {
// Simulated: getMemberCount, isMember, createIterator, attachIterator,
//            getByIndexObject, getCurrentMemberObject, getAsString.
//            In SdaiIterator: constructor, beginning, next.
		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
		getListAggregate();
		ListElement element = (ListElement)mod_list.myData[0];

		while (element != null) {
			SdaiModel model = (SdaiModel)element.object;
			if (model.underlying_schema != null) {
				int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
				if (index_to_entity >= 0) {
					if (model.mode > 0) {
						model.extent_index = index_to_entity;
						model.extent_type = type;
					}
					if (schema_data == null) {
						schema_data = model.underlying_schema.owning_model.schemaData;
						some_index = index_to_entity;
					}
				}
			}
			element = element.next;
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModel);
		} // syncObject
	}*/


	public AEntity getInstances(EEntity_definition type) throws SdaiException {
// Simulated: getMemberCount, isMember, createIterator, attachIterator,
//            getByIndexObject, getCurrentMemberObject, getAsString.
//            In SdaiIterator: constructor, beginning, next.
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
//		getListAggregate();
		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
			}
		}

		if (schema_data == null) {
			SdaiModel mod_found = ((CEntity)type).owning_model;
			if (mod_found != null) {
				if (mod_found.schemaData.model.lengths == null) {
					if (mod_found.getMode() == mod_found.NO_ACCESS) {
						mod_found.startReadOnlyAccess();
					} else {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
				schema_data = mod_found.schemaData;
				some_index = schema_data.findEntityExtentIndex((CEntityDefinition)type);
			}
		}

		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModel);
//		} // syncObject
	}

/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type and all instances of all its subtypes
 * collected going through all started <code>SdaiModel</code>s in this
 * <code>ASdaiModel</code>.
 * If this entity type (represented by the method's parameter) is not defined
 * or declared in the EXPRESS schema whose definition is underlying for a model,
 * then such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * A parameter value submitted to the method shall be in the form EXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel maggr = ...;
 *    ACartesian_point aggr = (ACartesian_point)maggr.getInstances(ECartesian_point.class);</pre></TT>
 * @param type Java class for the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type taken from all started <code>SdaiModel</code>s
 * in this <code>ASdaiModel</code>.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see SdaiModel#getInstances(Class type)
 * @see #getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 */
/*	public AEntity getInstances(Class type) throws SdaiException {
		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
		getListAggregate();
		ListElement element = (ListElement)mod_list.myData[0];

//		ListElement element = (ListElement)myData[0];
		while (element != null) {
			SdaiModel model = (SdaiModel)element.object;
			if (model.underlying_schema != null) {
				int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
				if (index_to_entity >= 0) {
					if (model.mode > 0) {
						model.extent_index = index_to_entity;
						model.extent_type = type;
					}
					if (schema_data == null) {
						schema_data = model.underlying_schema.owning_model.schemaData;
						some_index = index_to_entity;
					}
				}
			}
			element = element.next;
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModel);
		} // syncObject
	}*/


	public AEntity getInstances(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
//		getListAggregate();
//		ListElement element = (ListElement)mod_list.myData[0];

		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
			}
		}

		if (schema_data == null) {
			String name = type.getName();
			int last_dot = name.lastIndexOf(".");
			String dict_name = name.substring(SdaiSession.SCHEMA_PREFIX_LENGTH, last_dot).toUpperCase() +
				SdaiSession.DICTIONARY_NAME_SUFIX;
//			SdaiModel mod_found = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
			SdaiModel mod_found = SdaiSession.systemRepository.findExistingDictionarySdaiModel(dict_name);
			if (mod_found != null) {
				if (mod_found.schemaData.model.lengths == null) {
					if (mod_found.getMode() == mod_found.NO_ACCESS) {
						mod_found.startReadOnlyAccess();
					} else {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				}
				schema_data = mod_found.schemaData;
				some_index = schema_data.findEntityExtentIndex(type);
			} else {
				String base = SdaiSession.line_separator + "Wrong entity data type: " + name;
				throw new SdaiException(SdaiException.ED_NVLD, base);
			}
		}

		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModel);
//		} // syncObject
	}
	private AEntity getInstancesInternal(SchemaData sch_data, int index, Object type, CSet_type set_type)
			throws SdaiException {
		AEntity instancesInModels;
		if (sch_data == null) {
			instancesInModels = new AEntity();
		} else {
			try {
				instancesInModels = (AEntity)sch_data.getAggregateClassByIndex(index).newInstance();
			} catch (java.lang.IllegalAccessException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} catch (java.lang.InstantiationException ex) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Object [] myDataA = new Object[2];
// Temporary solution
		myDataA[0] = this;
		myDataA[1] = type;
		instancesInModels.myData = myDataA;
		instancesInModels.myLength = myLength;
		instancesInModels.myType = set_type;
		return instancesInModels;
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type
 * collected going through all started <code>SdaiModel</code>s in this
 * <code>ASdaiModel</code>.
 * The instances of any subtype of the specified entity data type
 * are not included into this aggregate.
 * If the given entity type is not defined or declared in the
 * EXPRESS schema whose definition is underlying for a model, then
 * such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel maggr = ...;
 *    EEntity_definition cp_type = ...;  // cartesian point
 *    ACartesian_point aggr = (ACartesian_point)maggr.getExactInstances(cp_type);</pre></TT>
 * @param type definition of the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type taken from all started <code>SdaiModel</code>s
 * in this <code>ASdaiModel</code>.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see SdaiModel#getExactInstances(EEntity_definition type)
 * @see #getExactInstances(Class type)
 */
/*	public AEntity getExactInstances(EEntity_definition type) throws SdaiException {
		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
		getListAggregate();
		ListElement element = (ListElement)mod_list.myData[0];

//		ListElement element = (ListElement)myData[0];
		while (element != null) {
			SdaiModel model = (SdaiModel)element.object;
			if (model.underlying_schema != null) {
				int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
				if (index_to_entity >= 0) {
					if (model.mode > 0) {
						model.extent_index = index_to_entity;
						model.extent_type = type;
					}
					if (schema_data == null) {
						schema_data = model.underlying_schema.owning_model.schemaData;
						some_index = index_to_entity;
					}
				}
			}
			element = element.next;
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModelExact);
		} // syncObject
	}*/


	public AEntity getExactInstances(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
//		getListAggregate();
//		ListElement element = (ListElement)mod_list.myData[0];

		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForExactTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForExactTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
			}
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModelExact);
//		} // syncObject
	}


/**
 * Returns a read-only aggregate containing all instances
 * of the specified entity data type
 * collected going through all started <code>SdaiModel</code>s in this
 * <code>ASdaiModel</code>.
 * The instances of any subtype of the specified entity data type
 * are not included into this aggregate.
 * If the given entity type (represented by the method's parameter) is not defined
 * or declared in the EXPRESS schema whose definition is underlying for a model,
 * then such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * The aggregate can be cast to the aggregate specific
 * for the given entity type.
 * A parameter value submitted to the method shall be in the form CXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel maggr = ...;
 *    ACartesian_point aggr = (ACartesian_point)maggr.getExactInstances(CCartesian_point.class);</pre></TT>
 * @param type Java class for the entity of which instances are required.
 * @return aggregate containing all instances of the specified
 * entity data type taken from all started <code>SdaiModel</code>s
 * in this <code>ASdaiModel</code>.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getInstances(EEntity_definition type)
 * @see #getInstances(Class type)
 * @see SdaiModel#getExactInstances(Class type)
 * @see #getExactInstances(EEntity_definition type)
 */
/*	public AEntity getExactInstances(Class type) throws SdaiException {
		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
		getListAggregate();
		ListElement element = (ListElement)mod_list.myData[0];

//		ListElement element = (ListElement)myData[0];
		while (element != null) {
			SdaiModel model = (SdaiModel)element.object;
			if (model.underlying_schema != null) {
				int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
				if (index_to_entity >= 0) {
					if (model.mode > 0) {
						model.extent_index = index_to_entity;
						model.extent_type = type;
					}
					if (schema_data == null) {
						schema_data = model.underlying_schema.owning_model.schemaData;
						some_index = index_to_entity;
					}
				}
			}
			element = element.next;
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModelExact);
		} // syncObject
	}*/


	public AEntity getExactInstances(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SchemaData schema_data = null;
		int some_index = -1;

// Temporary solution
//		getListAggregate();
//		ListElement element = (ListElement)mod_list.myData[0];

		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForExactTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null) {
					int index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
							model.provideInstancesForExactTypeIfNeeded(index_to_entity);
							model.extent_index = index_to_entity;
							model.extent_type = type;
						}
						if (schema_data == null) {
							schema_data = model.underlying_schema.owning_model.schemaData;
							some_index = index_to_entity;
						}
					}
				}
			}
		}
		return getInstancesInternal(schema_data, some_index, type, SdaiSession.setTypeForInstancesListOfModelExact);
//		} // syncObject
	}


/**
 * Returns the number of instances contained in started <code>SdaiModel</code>s of
 * this <code>ASdaiModel</code>.
 * The models whose access is ended at the time of invocation
 * of this method are ignored.
 * @return the number of instances.
 * @see SdaiModel#getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public int getInstanceCount() throws SdaiException {
//		synchronized (syncObject) {
		int i;
		SdaiModel model;
		int count = 0;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					model.provideAllInstancesIfNeeded();
					for (i = 0; i < model.lengths.length; i++) {
						count += model.lengths[i];
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					model.provideAllInstancesIfNeeded();
					for (i = 0; i < model.lengths.length; i++) {
						count += model.lengths[i];
					}
				}
			}
		}
		return count;
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * and of all its subtypes contained in started <code>SdaiModel</code>s of
 * this <code>ASdaiModel</code>.
 * If this entity type is not defined or declared in the
 * EXPRESS schema whose definition is underlying for a model, then
 * such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * @param type definition of the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @see #getInstanceCount()
 * @see SdaiModel#getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public int getInstanceCount(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SdaiModel model;
		int index_to_entity;
		int count = 0;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity =
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						count += model.getInstanceCountInternal(index_to_entity);
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					if (index_to_entity >= 0) {
						count += model.getInstanceCountInternal(index_to_entity);
					}
				}
			}
		}
		return count;
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * and of all its subtypes contained in started <code>SdaiModel</code>s of
 * this <code>ASdaiModel</code>.
 * If this entity type (represented by the method's parameter) is not defined
 * or declared in the EXPRESS schema whose definition is underlying for a model,
 * then such a model is ignored.
 * The models whose access is ended at the time of invocation
 * of this method are also bypassed.
 * A parameter value submitted to the method shall be in the form EXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param type Java class for the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see SdaiModel#getInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public int getInstanceCount(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiModel model;
		int index_to_entity;
		int count = 0;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						count += model.getInstanceCountInternal(index_to_entity);
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if (index_to_entity >= 0) {
						count += model.getInstanceCountInternal(index_to_entity);
					}
				}
			}
		}
		return count;
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * contained in started <code>SdaiModel</code>s of
 * this <code>ASdaiModel</code>.
 * If this entity type is not defined or declared in the
 * EXPRESS schema whose definition is underlying for a model, then
 * such a model is ignored. The models whose access is ended
 * at the time of invocation of this method are also bypassed.
 * The instances of any subtype of the specified entity type are not counted.
 * Passing null value to the method's
 * parameter results in SdaiException ED_NDEF.
 * @param type definition of the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException ED_NDEF, entity definition not defined.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see SdaiModel#getExactInstanceCount(EEntity_definition type)
 * @see #getExactInstanceCount(Class type)
 */
	public int getExactInstanceCount(EEntity_definition type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.ED_NDEF);
		}
		SdaiModel model;
		int index_to_entity;
		int count = 0;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
							model.repository != SdaiSession.systemRepository) {
						index_to_entity = model.find_entityRO((CEntityDefinition)type);
					} else {
						index_to_entity =
							model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					}
					if (index_to_entity >= 0) {
						count += model.lengths[index_to_entity];
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
							model.repository != SdaiSession.systemRepository) {
						index_to_entity = model.find_entityRO((CEntityDefinition)type);
					} else {
						index_to_entity =
							model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)type);
					}
					if (index_to_entity >= 0) {
						count += model.lengths[index_to_entity];
					}
				}
			}
		}
		return count;
//		} // syncObject
	}


/**
 * Returns the number of instances of the specified entity data type
 * contained in started <code>SdaiModel</code>s of
 * this <code>ASdaiModel</code>.
 * If this entity type (represented by the method's parameter) is not defined
 * or declared in the EXPRESS schema whose definition is underlying for a model,
 * then such a model is ignored. The models whose access is ended
 * at the time of invocation of this method are also bypassed.
 * The instances of any subtype of the specified entity type are not counted.
 * A parameter value submitted to the method shall be in the form CXxx.class,
 * where xxx is the name of the entity.
 * Passing null value to the method's
 * parameter results in SdaiException VA_NSET.
 * @param type Java class for the entity of which the instance count is required.
 * @return the number of instances of the specified entity data type.
 * @throws SdaiException VA_NSET, value not set.
 * @see #getInstanceCount()
 * @see #getInstanceCount(EEntity_definition type)
 * @see #getInstanceCount(Class type)
 * @see #getExactInstanceCount(EEntity_definition type)
 * @see SdaiModel#getExactInstanceCount(Class type)
 */
	public int getExactInstanceCount(Class type) throws SdaiException {
//		synchronized (syncObject) {
		if (type == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiModel model;
		int index_to_entity;
		int count = 0;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
							model.repository != SdaiSession.systemRepository) {
						index_to_entity =
							model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[index_to_entity]);
					}
					if (index_to_entity >= 0) {
						count += model.lengths[index_to_entity];
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				if (model.underlying_schema != null && (model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					index_to_entity = model.underlying_schema.owning_model.schemaData.findEntityExtentIndex(type);
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY &&
							model.repository != SdaiSession.systemRepository) {
						index_to_entity =
							model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[index_to_entity]);
					}
					if (index_to_entity >= 0) {
						count += model.lengths[index_to_entity];
					}
				}
			}
		}
		return count;
//		} // syncObject
	}


	void removeUnorderedKeepSorted(Object value) throws SdaiException {
		int index =-1;
		for (int i = 0; i < myLength; i++) {
			if (myData[i] == value) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
//		for (int i = index; i < myLength - 1; i++) {
//			myData[i] = myData[i + 1];
//		}
		System.arraycopy(myData, index + 1, myData, index, myLength-index-1);



		myData[myLength - 1] = null;
		myLength--;
	}


/**
 * Returns a description of this <code>ASdaiModel</code> as a <code>String</code>.
 * It includes constant string "SdaiModels: " and a list of the names
 * of all models contained in this aggregate.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel mod_aggr = ...;
 *    System.out.println(mod_aggr);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    SdaiModels: mod, model2, model3, mod_special </pre>
 * @return the <code>String</code> representing this <code>ASdaiModel</code>.
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
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		for (i = 0; i < MODELS_LENGTH; i++) {
			staticFields.instance_as_string[++str_index] = MODELS[i];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.COLON;
		boolean first = true;
		SdaiModel mod;
		int ln;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (staticFields.it_sa == null) {
				staticFields.it_sa = createIterator();
			} else {
				attachIterator(staticFields.it_sa);
			}
			while (staticFields.it_sa.next()) {
				mod = (SdaiModel)((ListElement)staticFields.it_sa.myElement).object;
				ln = mod.name.length();
				if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(str_index + 1, str_index + ln + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
				first = false;
				str_index = write_string(mod.name, str_index);
			}
		} else {
			for (i = 0; i < myLength; i++) {
				mod = (SdaiModel)myData[i];
				ln = mod.name.length();
				if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(str_index + 1, str_index + ln + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
				first = false;
				str_index = write_string(mod.name, str_index);
			}
		}
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}


	public int getMemberCount() throws SdaiException {
//		synchronized (syncObject) {
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				SdaiTransaction trans = repo.session.active_transaction;
				if (trans == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				int del_count = 0;
				for (int i = 0; i < trans.stack_length; i++) {
					if (trans.stack_del_mods_rep[i] == repo && !trans.stack_del_mods[i].checkModel(null)) {
						del_count++;
					}
				}
//int rem = repo.getModCount();
//System.out.println("   ASdaiModel   repo name: " + repo.name +
//"   repo: " + repo + "   del_count: " + del_count +
//"   rem: " + rem + "   repo.unresolved_mod_count: " + repo.unresolved_mod_count);
//return rem - del_count + repo.unresolved_mod_count;
				return repo.getModCount() - del_count + repo.unresolved_mod_count;
			}
		}
		return super.getMemberCount();
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>SdaiModel</code> instead of <code>Object</code>.
 */
	public SdaiModel getCurrentMember(SdaiIterator it) throws SdaiException {
//		synchronized (syncObject) {
		boolean virtual = false;
		Object owning_obj = getOwner();
		if (owning_obj instanceof SdaiRepository) {
			SdaiRepository repo = (SdaiRepository)owning_obj;
			if (repo.isRemote()) {
				if (it.myAggregate != this) {
					String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (it.myElement == null && (it.myIndex < 0 || it.behind)) {
					throw new SdaiException(SdaiException.IR_NSET, it);
				}
				if (it.myIndex >= 0) {
					return (SdaiModel)repo.models.myData[it.myIndex];
				}
				SdaiModel model = null;
				for (int i = 0; i < repo.models.myLength; i++) {
					model = (SdaiModel)repo.models.myData[i];
//System.out.println("ASdaiModel @@@@@@@@@  i: " + i + "    model: " + model.name + "   modRemote: " + model.modRemote);
//System.out.println("ASdaiModel   it.myElement: " + it.myElement);
					if (model.checkModel(it.myElement)) {
//System.out.println("ASdaiModel   model.modRemote is equal to modRemote");
						if (model.underlying_schema == null && model.dictionary == null) {
							virtual = true;
							break;
						} else {
							return model;
						}
					}
				}
				SdaiModelHeader modelHeader = repo.takeModelHeader(it.myElement);
				String dict_name = modelHeader.schema.toUpperCase() + SdaiSession.DICTIONARY_NAME_SUFIX;
				SdaiModel dictionary = SdaiSession.systemRepository.findDictionarySdaiModel(dict_name);
				if (dictionary.getMode() == SdaiModel.NO_ACCESS) {
					dictionary.startReadOnlyAccess();
				}

				if (virtual) {
					model.promoteVirtualToOrdinary(dictionary);
					if (model.inst_idents == null) {
						model.loadInstanceIdentifiersRemoteModel();
					}
				} else {
					model = repo.createNewModel(modelHeader.name, dictionary.described_schema, it.myElement);
				}
				model.created = false;
				model.modified_outside_contents = false;
				model.fromSdaiModelHeader(modelHeader);
				model.committed = true;
				return model;
			}
		}
		return (SdaiModel)getCurrentMemberObject(it);
//		} // syncObject
	}


	protected boolean try_to_addUnordered(SdaiModel value) throws SdaiException {
		boolean found = false;
		for (int i = 0; i < myLength; i++) {
			if (myData[i] == value) {
				found = true;
			}
		}
		if (found) {
			return false;
		}
		myData[myLength] = value;
		myLength++;
		return true;
	}


/**
 * Finds instances that are mappings of given source entity.
 * Instances that satisfies mapping constraints must be in this aggregate of models, but other instances required by mapping constraints may be in other models within given data domain.
 * Read only or read/write access for suplied models must be started before using this method.
 * This method first finds all possible mappings.
 * These mappings should be defined in specified mapping domain.
 * Then it finds instances for every mapping.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel m = ...;
 *    EEntity_definition sourceEntity = ...;  // definition of source entity
 *    AEntity instances = m.findMappingInstances(sourceEntity,  targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param sourceEntity this method searches instances that are mappings of this entity
 * @param dataDomain an application domain where to search instances to satisfy mapping constraints.
 * It can be null; then this aggregate of models is used as data domain.
 * @param mappingDomain a domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY MOST_SPECIFC_ENTITY} - returned mappings are restricted to most specific,
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instanciated are also not included.
 * @return list of instances that satisfy requirements.
 * Every instance is mentioned only once in this list (elements of list are unique).
 * It will return null if there are no instances that are valid mappings of given entity.
 * @see #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see SdaiModel#findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
 * @see EEntity#findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		return Mapping.findMappingInstances(this, sourceEntity, dataDomain, mappingDomain, null, mode);
	}

	/**
	 * Finds instances that are mappings of given source entity.
	 * This method provides the similar functionality as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * and adds extra possibility to get most specific mappings of the instances.
	 * <p>This method is part of the mapping extensions of JSDAI.
	 * @param sourceEntity this method searches instances that are mappings of this entity
	 * @param dataDomain the domain where to search instances to satisfy mapping constraints.
	 * It can be null.
	 * @param mappingDomain the domain for mapping constraints and dictionary data.
	 * @param instanceMappings if not null should be an empty aggregate. The most specific entity
	 * mappings are returned in this aggregate. The member indexes are synchronized with instance
	 * aggregate which is returned from this method. If null then the method works as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * @param mode Should always be {@link EEntity#NO_RESTRICTIONS} - no restrictions.
	 * @return list of instances that satisfy requirements. If <code>instanceMappings</code> is
	 * not null then member indexes in both aggregates make pairs. Every pair
	 * (instance, instanceMapping) is mentioned in both aggregates only once. If
	 * <code>instanceMappings</code> is null then every instance is mentioned
	 * only once in this aggregate.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
	 * @see SdaiModel#findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, AEntity_mapping, int)
	 * @see EEntity#findMostSpecificMappings
	 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		return Mapping.findMappingInstances(this, sourceEntity, dataDomain, mappingDomain, instanceMappings, mode);
	}

/**
 * Finds instances that are mappings of given entity mapping.
 * This method is more specific than {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}, because it searches instances that meets requirements only for specified mapping.
 * Instances that satisfy mapping constraints must be in supplied aggregate of model, but other instances required by mapping constraints may be in other models within data domain.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiModel m = ...;
 *    EEntity_mapping mapping = ...;  // mapping of source entity
 *    AEntity instances = m.findMappingInstances(sourceEntity, targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param entityMapping this method searches instances that satisfies constraints for this entity mapping.
 * @param dataDomain an application domain where to search instances to satisfy mapping constraints.
 * It can be null; then this aggregate of models is used as data domain.
 * @param mappingDomain a domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY MOST_SPECIFC_ENTITY} - returned mappings are restricted to most specific,
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instantiated are also not included.
 * @return list of instances that satisfy requirements.
 * Every instance is mentioned only once in this list (elements of list are unique).
 * It will return null if there are no instances that are valid mappings of given entity.
 * @see #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
 * @see SdaiModel#findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see EEntity#findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
 */
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel dataDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, dataDomain,
												mappingDomain, null, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
	 * Finds instances that are mappings of given source entity.
	 * This method provides the similar functionality as
	 * {@link #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)}
	 * and adds extra possibility to get most specific mappings of the instances.
	 * <p>This method is part of the mapping extensions of JSDAI.
	 * @param entityMapping this method searches instances that satisfies constraints
	 *                      for this entity mapping.
	 * @param dataDomain the domain where to search instances to satisfy mapping constraints.
	 * It can be null.
	 * @param mappingDomain the domain for mapping constraints and dictionary data.
	 * @param instanceMappings if not null should be an empty aggregate. The most specific entity
	 * mappings are returned in this aggregate. The member indexes are synchronized with instance
	 * aggregate which is returned from this method. If null then the method works as
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * @param mode Should always be {@link EEntity#NO_RESTRICTIONS} - no restrictions.
	 * @return list of instances that satisfy requirements. If <code>instanceMappings</code> is
	 * not null then member indexes in both aggregates make pairs. Every pair
	 * (instance, instanceMapping) is mentioned in both aggregates only once. If
	 * <code>instanceMappings</code> is null then every instance is mentioned
	 * only once in this aggregate.
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
	 * @see SdaiModel#findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, AEntity_mapping, int)
	 * @see EEntity#findMostSpecificMappings
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel dataDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, dataDomain, mappingDomain,
												instanceMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/**
	 * Writes this aggregate of <code>SdaiModels</code> in XML representation to specified stream.
	 * Output format is controlled by <code>instanceReader</code> parameter.
	 *
	 * @param location an <code>OutputStream</code> to write XML to
	 * @param instanceReader an {@link jsdai.xml.InstanceReader} describing output format
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @see jsdai.xml.InstanceReader jsdai.xml.InstanceReader
	 * @see jsdai.xml.LateBindingReader jsdai.xml.LateBindingReader
	 */
	public void exportXml(OutputStream location, InstanceReader instanceReader) throws SdaiException {
		try {
			instanceReader.serialize(location, new SdaiInputSource(this));
		} catch(SdaiException e) {
			throw e;
		} catch(Exception e) {
			throw new SdaiException(SdaiException.SY_ERR, e);
		}
	}

	/**
	 * Makes the contents of member <code>SdaiModels</code> available for
	 * read-only access. Models in read-only or read-write mode prior to
	 * invoking this method are skipped from the operation and remain in
	 * their corresponding modes.
	 * @throws SdaiException TR_NEXS, transaction does not exist.
	 * @throws SdaiException TR_NAVL, transaction currently not available.
	 * @throws SdaiException TR_EAB, transaction ended abnormally.
	 * @throws SdaiException RP_NOPN, repository is not open.
	 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
	 * @throws SdaiException MX_RO, SDAI-model access read-only.
	 * @throws SdaiException MX_RW, SDAI-model access read-write.
	 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
	 * @throws SdaiException SY_ERR, underlying system error.
	 * @see SdaiModel#startReadOnlyAccess()
	 * @since 4.2.0
	 */
	public void startReadOnlyAccess() throws SdaiException {
		SdaiModel[] remoteModels = new SdaiModel[getMemberCount()];
		SessionRemote bridgeSession = null;
		int j = 0;
		for(SdaiIterator i = createIterator(); i.next(); ) {
			SdaiModel model = getCurrentMember(i);
			if(model.getMode() == SdaiModel.NO_ACCESS) {
				if(bridgeSession == null) {
					bridgeSession = model.repository.session.bridgeSession;
				} else if(bridgeSession != model.repository.session.bridgeSession) {
					throw new SdaiException(SdaiException.SS_NAVL, "Model " + model +
							" belongs to a different bridge session");
				}
				if(bridgeSession != null && bridgeSession.isStartMultiModelAccessAvailable()
						&& model.getModRemote() != null) {
					remoteModels[j++] = model;
				} else {
					model.startReadOnlyAccess();
				}
			}
		}
		if(j > 0) {
			bridgeSession.startMultiModelAccess(remoteModels, j, SdaiModel.READ_ONLY);
		}
	}

	/**
	 * Makes the contents of member <code>SdaiModels</code> available for
	 * read-write access. Models in read-only mode prior to invoking this
	 * method are promoted to read-write mode during the operation. Models
	 * in read-write mode prior to invoking this method are skipped from
	 * the operation and remain in read-write mode.
	 * @throws SdaiException TR_NEXS, transaction does not exist.
	 * @throws SdaiException TR_NAVL, transaction currently not available.
	 * @throws SdaiException TR_EAB, transaction ended abnormally.
	 * @throws SdaiException RP_NOPN, repository is not open.
	 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
	 * @throws SdaiException MX_RO, SDAI-model access read-only.
	 * @throws SdaiException MX_RW, SDAI-model access read-write.
	 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
	 * @throws SdaiException SY_ERR, underlying system error.
	 * @see SdaiModel#startReadWriteAccess()
	 * @since 4.2.0
	 */
	public void startReadWriteAccess() throws SdaiException {
		SdaiModel[] remoteModels = null;
		SessionRemote bridgeSession = null;
		int j = 0;
		for(SdaiIterator i = createIterator(); i.next(); ) {
			SdaiModel model = getCurrentMember(i);
			int modelMode = model.getMode();
			if(modelMode == SdaiModel.NO_ACCESS) {
				if(bridgeSession == null) {
					bridgeSession = model.repository.session.bridgeSession;
				} else if(bridgeSession != model.repository.session.bridgeSession) {
					throw new SdaiException(SdaiException.SS_NAVL, "Model " + model +
							" belongs to a different bridge session");
				}
				if(bridgeSession != null && bridgeSession.isStartMultiModelAccessAvailable()
						&& model.getModRemote() != null) {
					if(j == 0) {
						remoteModels = new SdaiModel[getMemberCount()];
					}
					remoteModels[j++] = model;
				} else {
					model.startReadWriteAccess();
				}
			} else if(modelMode == SdaiModel.READ_ONLY) {
				model.promoteSdaiModelToRW();
			}
		}
		if(j > 0) {
			bridgeSession.startMultiModelAccess(remoteModels, j, SdaiModel.READ_WRITE);
		}
	}


/**
 * Terminates any access, either read-only or read-write, for each
 * <code>SdaiModel</code> in this aggregate. As a result, the contents of all
 * models becomes inaccessable. Each model of the aggregate is removed from
 * the set "active_models" of <code>SdaiSession</code>.
 * If some entity instance within an <code>SdaiModel</code>, which is being
 * processed by the current method, has been created, deleted or modified
 * since the most recent commit, abort or start transaction read-write access
 * operation was performed, then SdaiException TR_RW is thrown.
 * @throws SdaiException TR_RW, transaction read-write.
 * @throws SdaiException RP_NOPN, repository is not open.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @throws SdaiException MX_NDEF, SDAI-model access not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #startReadOnlyAccess
 * @see #startReadWriteAccess
 * @see SdaiModel#endReadOnlyAccess()
 * @see SdaiModel#endReadWriteAccess()
 * @since 4.2.0
 */
	public void endAccess() throws SdaiException {
		int j;
		SdaiModel model;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				model.closingAll = true;
				element = element.next;
			}
			element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				model.endAccessAll();
				element = element.next;
			}
			element = (ListElement)myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				model.closingAll = false;
				element = element.next;
			}
		} else {
			for (j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				model.closingAll = true;
			}
			for (j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				model.endAccessAll();
			}
			for (j = 0; j < myLength; j++) {
				model = (SdaiModel)myData[j];
				model.closingAll = false;
			}
		}
	}

}
