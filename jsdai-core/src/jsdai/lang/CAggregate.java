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
import jsdai.query.SerializableRef;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 This class is for internal JSDAI use only. Applications shall use only 
 <code>Aggregate</code> or one of the specialized extensions of <code>CAggregate</code>.
*/

/* Implementation of Aggregate as a variable-size array or a list */
public class CAggregate extends A_basis implements Aggregate {

/**
	The count of elements in this aggregate.
*/
	int myLength;

/**
	The elements of this aggregate. They are represented in one of the following 
	two ways:
	 1) for EXPRESS SET, BAG and ARRAY types the aggregate members are stored in 
		java array;
	 2) for EXPRESS LIST type a chain of objects of class <code>ListElement</code> 
		is created; in the java array <code>myData</code> only the first (head) and 
		the last (tail) objects of this chain are stored.
*/
	Object myData;

/**
	The aggregation type of this aggregate.
*/
	AggregationType myType;

	private static final int NUMBER_OF_DEFINED_TYPES_IN_SELECT = 30;

	static final int SHORT_AGGR = 4;

	static final int SHORT_AGGR_SELECT = 2;

	static final int INIT_SIZE_SET = 2;

	static final int INIT_SIZE_SET_SELECT = 4;

/**
	Value used to set an initial size for the array 'numbers'.
*/
	private static final int COUNT_OF_INTEGER_NUMBERS = 32;

/**
	Array of positive integers 1,2,3,...
	These integers are used as select numbers written to an aggregate
	together with values in the cases when the element type of the
	aggregate is (mixed) select type.
	The array is applied in many methods in class CAggregate.
*/
	private static Integer[] numbers = init_numbers();



/**
	Returns entity instance of which attribute this aggregate (possibly through 
	one or more other aggregates in the case of nesting) is a value. 
	If this aggregate is not created to be a value of an entity instance, then 
	<code>null</code> is returned.
*/
/*	CEntity getOwningInstance() {
		SdaiCommon owner = this.owner;
		while (owner instanceof Aggregate || owner instanceof SdaiListenerElement) {
			owner = owner.getOwner();
		}
		return (owner instanceof CEntity) ? ((CEntity)owner) : null;
	}*/
	CEntity getOwningInstance() {
		SdaiCommon owner = this.owner;
		while (!(owner instanceof CEntity) && owner != null) {
			owner = owner.getOwner();
		}
		return (CEntity)owner;
	}


/**
	Informs the owner that some data in this aggregate were modified. 
*/
	void modified() throws SdaiException {
		owner.modified();
	}



/**
	The constructor of this class. It is used indirectly to create instances of 
	extensions of this class, for example <code>ALine</code>, using java 
	<code>newInstance</code> method. 
	The statement "new CAggregate()" in JSDAI lang package nowhere appears. 
*/
	protected CAggregate() {
	}


/**
	The constructor used to create special aggregate of listeners in classes 
	<code>CEntity</code> and <code>SdaiRepository</code>. 
*/
	protected CAggregate(AggregationType provided_type) {
		myType = provided_type;
	}


/**
	The constructor used to create extensions of this class, for example, 
	aggregates for simple data types, that is, <code>A_integer</code>, 
	<code>A_string</code> and so on.
*/
	protected CAggregate(AggregationType provided_type, SdaiCommon aggr_owner) {
		myType = provided_type;
		owner = aggr_owner;
	}



/**
	Supplies the aggregate with the type and owner information. 
	The method is used in the cases when an aggregate is created by 
	applying <code>newInstance</code> method. This occurs in 
	<code>CEntity</code> class.
*/
	void attach(EAggregation_type provided_type, CEntity instance) {
		myType = (AggregationType)provided_type;
		owner = instance;
	}


/* 10.12.1 Get member count */
	public int getMemberCount() throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		Object [] myDataA;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			int sum;
			int index;
			if (myType.shift == SdaiSession.INST_AGGR) {
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
				} else {
					index = myLength;
				}
				if (index >= 0) {
					sum = model.lengths[index];
				} else {
					sum = 0;
				}
				CSchema_definition schema = (CSchema_definition)myDataA[1];
				if (schema == null) {
					return sum;
				}
				int subtypes[] = schema.getSubtypes(myLength);
				if (subtypes != null) {
					for (int i = 0; i < subtypes.length; i++) {
						if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								model.repository != SdaiSession.systemRepository) {
							index = model.find_entityRO(model.dictionary.schemaData.entities[subtypes[i]]);
						} else {
							index = subtypes[i];
						}
						if (index >= 0) {
							sum += model.lengths[index];
						}
					}
				}
				return sum;
			} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
				} else {
					index = myLength;
				}
				if (index >= 0) {
					return model.lengths[index];
				} else {
					return 0;
				}
			} else {
				sum = 0;
				for (int i = 0; i < model.lengths.length; i++) {
					sum += model.lengths[i];
				}
				return sum;
			}
		}
		if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			return myLength; 
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			return getMemberCountListOfModelsAll();
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			return getMemberCountListOfModels();
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			return getMemberCountListOfModelsExact();
		}
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		if (myType != null && myType.express_type == DataType.ARRAY && myData == null) {
			CArray_type arr_type = (CArray_type)myType;
			return arr_type.getUpper_index(null).getBound_value(null) - 
				arr_type.getLower_index(null).getBound_value(null) + 1;
		} else {
			if (myLength < 0) {
				resolveAllConnectors();
			}
			return myLength;
		}
//		} // syncObject
	}


	private int getMemberCountListOfModelsAll() throws SdaiException {
		int i;
		int count = 0;
		ASdaiModel amod = (ASdaiModel)myData;
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					for (i = 0; i < model.lengths.length; i++) {
						count += model.lengths[i];
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					for (i = 0; i < model.lengths.length; i++) {
						count += model.lengths[i];
					}
				}
			}
		}
		return count;
	}


/**
	The number of elements in the set which consists of all instances of a 
	specified entity data type and all instances of all its subtypes collected 
	going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) instances 
	of which are collected. 
*/
	private int getMemberCountListOfModels() throws SdaiException {
		int count = 0;
		SdaiModel model;
		int true_extent_index;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					element = element.next;
					continue;
				}
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					count += model.countEntityInstancesRO(true_extent_index);
				} else {
					count += model.countEntityInstances(true_extent_index);
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					continue;
				}
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					count += model.countEntityInstancesRO(true_extent_index);
				} else {
					count += model.countEntityInstances(true_extent_index);
				}
			}
		}
		return count;
	}


/**
	The number of elements in the set which consists of all instances of a 
	specified entity data type (but not instances of its subtypes) collected 
	going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
*/
	private int getMemberCountListOfModelsExact() throws SdaiException {
		int count = 0;
		SdaiModel model;
		int true_extent_index;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					element = element.next;
					continue;
				}
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					true_extent_index = model.find_entityRO(model.dictionary.schemaData.entities[true_extent_index]);
				}
				if (true_extent_index >= 0) {
					count += model.lengths[true_extent_index];
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					continue;
				}
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					true_extent_index = model.find_entityRO(model.dictionary.schemaData.entities[true_extent_index]);
				}
				if (true_extent_index >= 0) {
					count += model.lengths[true_extent_index];
				}
			}
		}
		return count;
	}


	public boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
		int i;
//		synchronized (syncObject) {
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		Object [] myDataA;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (!(value instanceof EEntity)) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			if (myType.shift == SdaiSession.INST_AGGR) {
				return isMemberInstances((EEntity)value);
			} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
				return isMemberInstancesExact((EEntity)value);
			} else {
				return isMemberInstancesAll((EEntity)value);
			}
		}
		if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			for (i = 0; i < myLength; i++) {
				if (myDataA[i] == value) {
					return true;
				}
			}
			return false; 
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			if (!(value instanceof EEntity)) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return isMemberListOfModelsAll((EEntity)value);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			if (!(value instanceof EEntity)) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return isMemberListOfModels((EEntity)value);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			if (!(value instanceof EEntity)) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return isMemberListOfModelsExact((EEntity)value);
		}
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}

		if (myLength == 0) {
			return false;
		}
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				if (value instanceof CEntity) {
					sel_number = 1;
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				sel_number = myType.select.giveSelectNumber(select);
				if (sel_number == -1) {
					String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
					throw new SdaiException(SdaiException.VA_NVLD, base);
				} else if (sel_number == -2) {
					String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (checkValue(value, myData, sel_number)) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					if (checkValue(value, myDataA[0], sel_number) || checkValue(value, myDataA[1], sel_number)) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (checkValue(value, element.object, sel_number)) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (checkValue(value, myDataA[0], sel_number) && ((Integer)myDataA[1]).intValue() == sel_number) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (checkValue(value, element.object, sel_number)) {
							element = element.next;
							if (((Integer)element.object).intValue() == sel_number) {
								if (has_con) {
									myLength = -myLength;
								}
								return true;
							}
						} else {
							element = element.next;
						}
						element = element.next;
					}
				}
			}
			if (has_con) {
				myLength = -myLength;
			}
			return false;
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (checkValue(value, myData, sel_number)) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (checkValue(value, myDataA[i], sel_number)) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i*2;
					if (checkValue(value, myDataA[index], sel_number) && ((Integer)myDataA[index + 1]).intValue() == sel_number) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				}
			}
			if (has_con) {
				myLength = -myLength;
			}
			return false;
		}
//		} // syncObject
	}


/**
	Compares two values for equality. For data of types integer, double, string 
	and binary <code>true</code> is returned when values coincide and does not 
	mean unset case. Otherwise, a pointer comparison is performed.
	Invoked in the public method <code>isMember</code>.
*/
	private boolean checkValue(Object value, Object agg_value, int sel_number) throws SdaiException {
		if (value instanceof Integer) {
			if (!(agg_value instanceof Integer)) {
				return false;
			}
			int i = ((Integer)value).intValue();
			int agg_i = ((Integer)agg_value).intValue();
			if (i != agg_i) {
				return false;
			}
			if (sel_number <= 0) {
				if (analyse_value(value)) {
					return true;
				}
			} else if (analyse_select_value(myType.select.tags[sel_number - 2], myType.select, sel_number, value)) {
				return true;
			}
			return false;
		}
		if (value instanceof Double) {
			if (!(agg_value instanceof Double)) {
				return false;
			}
			double d = ((Double)value).doubleValue();
			double agg_d = ((Double)agg_value).doubleValue();
			if (d == agg_d && !((Double)value).isNaN()) {
				return true;
			}
			return false;
		}
		if (value instanceof String) {
			if (!(agg_value instanceof String)) {
				return false;
			}
			if (value != null && ((String)value).equals((String)agg_value)) {
				return true;
			}
			return false;
		}
		if (value instanceof Binary) {
			if (!(agg_value instanceof Binary)) {
				return false;
			}
			if (value != null && ((Binary)value).equals((Binary)agg_value)) {
				return true;
			}
			return false;
		}
		if (value != null && value == agg_value) {
			return true;
		}
		return false;
	}


/**
	Checks if a submitted value with select path attached belongs to this aggregate. 
	The method is invoked in compiler generated early binding methods.
*/
	protected boolean pIsMember(Object value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return isMember(value, select);
//		} // syncObject
	}


/**
	Checks if a submitted value belongs to the set which consists of instances 
	of a specified entity data type and all its subtypes in an 
	<code>SdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations with parameter specifying an entity) 
	in class <code>SdaiModel</code>. The set is represented by an aggregate in 
	which the array <code>myData</code> contains two elements: the first is an 
	<code>SdaiModel</code> containing instances, whereas the second is the 
	underlying schema of this model.
*/
	boolean isMemberInstances(EEntity value) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		int index = myLength;
		boolean first_time = true;
		int i = -1;
		int subtypes[] = null;
		while (index >= 0) {
			if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
					model.repository != SdaiSession.systemRepository) {
				index = model.find_entityRO(model.dictionary.schemaData.entities[index]);
			}
			if (index >= 0) {
				//if (model.sorted[index]) {
				if ((model.sim_status[index] & SdaiModel.SIM_SORTED) != 0) {
					int ind = model.find_instance(0, model.lengths[index] - 1, index, 
						((CEntity)value).instance_identifier);
					if (ind >= 0 && model.instances_sim[index][ind] == value) {
						return true;
					}
				} else {
					for (int j = 0; j < model.lengths[index]; j++) {
						if (model.instances_sim[index][j] == value) {
							return true;
						}
					}
				}
			}
			i++;
			if (first_time) {
				if (myDataA[1] == null) {
					return false;
				}
				subtypes = ((CSchema_definition)myDataA[1]).getSubtypes(myLength);
				first_time = false;
			}
			if (i < subtypes.length) {
				index = subtypes[i];
			} else {
				index = -1;
			}
		}
		return false;
	}


/**
	Checks if a submitted value belongs to the set which consists of instances 
	of a specified entity data type (but not its subtypes) in an 
	<code>SdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>SdaiModel</code>. 
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first is an <code>SdaiModel</code> containing 
	instances, whereas the second is the underlying schema of this model.
*/
	boolean isMemberInstancesExact(EEntity value) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		int index;
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
		} else {
			index = myLength;
		}
		if (index >= 0) {
			//if (model.sorted[index]) {
            if ((model.sim_status[index] & SdaiModel.SIM_SORTED) != 0) {
				int ind = model.find_instance(0, model.lengths[index] - 1, index, 
					((CEntity)value).instance_identifier);
				if (ind >= 0 && model.instances_sim[index][ind] == value) {
					return true;
				}
			} else {
				for (int j = 0; j < model.lengths[index]; j++) {
					if (model.instances_sim[index][j] == value) {
						return true;
					}
				}
			}
		}
		return false;
	}


/**
	Checks if a submitted value belongs to the set which consists of all instances 
	of an <code>SdaiModel</code>. This set is returned by <code>getInstances</code> 
	method in class <code>SdaiModel</code>. 
	The set is represented by an aggregate in which the first element of the 
	array <code>myData</code> is an <code>SdaiModel</code> whose instances are 
	considered.
*/
	boolean isMemberInstancesAll(EEntity value) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		Class cl = value.getClass();
		for (int i = 0; i < model.lengths.length; i++) {
			if (model.lengths[i] <= 0) continue;
			if (!(cl.isInstance(model.instances_sim[i][0]))) continue;
			//if (model.sorted[i]) {
            if ((model.sim_status[i] & SdaiModel.SIM_SORTED) != 0) {
				int ind = model.find_instance(0, model.lengths[i] - 1, i, 
					((CEntity)value).instance_identifier);
				if (ind >= 0 && model.instances_sim[i][ind] == value) {
					return true;
				}
			} else {
				for (int j = 0; j < model.lengths[i]; j++) {
					if (model.instances_sim[i][j] == value) {
						return true;
					}
				}
			}
		}
		return false;
	}


	boolean isMemberListOfModelsAll(EEntity value) throws SdaiException {
		ASdaiModel amod = (ASdaiModel)myData;
		SdaiModel model;
		boolean is;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					is = model.isMemberListOfModelsAll((CEntity)value);
					if (is) {
						return true;
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					is = model.isMemberListOfModelsAll((CEntity)value);
					if (is) {
						return true;
					}
				}
			}
		}
		return false;
	}


/**
	Checks if a submitted value belongs to the set which consists of all instances of a 
	specified entity data type and all instances of all its subtypes collected 
	going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) instances 
	of which are collected. 
*/
	boolean isMemberListOfModels(EEntity value) throws SdaiException {
		SdaiModel model;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				boolean is = model.isMemberListOfModels((CEntity)value, myDataA[1]);
				if (is) {
					return true;
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				boolean is = model.isMemberListOfModels((CEntity)value, myDataA[1]);
				if (is) {
					return true;
				}
			}
		}
		return false;
	}


/**
	Checks if a submitted value belongs to the set which consists of all instances of a 
	specified entity data type (but not instances of its subtypes) collected 
	going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
*/
	boolean isMemberListOfModelsExact(EEntity value) throws SdaiException {
		SdaiModel model;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				boolean is = model.isMemberListOfModelsExact((CEntity)value, myDataA[1]);
				if (is) {
					return true;
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				boolean is = model.isMemberListOfModelsExact((CEntity)value, myDataA[1]);
				if (is) {
					return true;
				}
			}
		}
		return false;
	}


	public boolean isMember(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int type = myType.select.getType(select);
		if (type < 0) {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (type == PhFileReader.INTEGER) {
			if (value == Integer.MIN_VALUE) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
		} else if (value == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return isMember(new Integer(value), select);
//		} // syncObject
	}


/**
	Checks if a submitted value of integer type with select path attached 
	belongs to this aggregate. The method is invoked in compiler generated 
	early binding methods.
*/
	protected boolean pIsMember(int value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return isMember(value, select);
//		} // syncObject
	}


	public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return isMember(new Double(value), select);
//		} // syncObject
	}


/**
	Checks if a submitted value of double type with select path attached 
	belongs to this aggregate. The method is invoked in compiler generated 
	early binding methods.
*/
	protected boolean pIsMember(double value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return isMember(value, select);
//		} // syncObject
	}


	public boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int i;
		if (value) {
			i = 2;
		} else {
			i = 1;
		}
		return isMember(new Integer(i), select);
//		} // syncObject
	}


/**
	Checks if a submitted value of boolean type with select path attached 
	belongs to this aggregate. The method is invoked in compiler generated 
	early binding methods.
*/
	protected boolean pIsMember(boolean value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return isMember(value, select);
//		} // syncObject
	}


	public SdaiIterator createIterator() throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		Object [] myDataA;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			return new SdaiIterator(this, -1);
		}
		if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			return new SdaiIterator(this);
		}
		if (myType != null && (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL || 
				myType.shift == SdaiSession.ASDAIMODEL_INST || myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT)) {
			ASdaiModel amod;
			if (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
				amod = (ASdaiModel)myData;
			} else {
				myDataA = (Object [])myData;
				amod = (ASdaiModel)myDataA[0];
			}
			Object obj =null;
			if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
				obj = amod.myData[0];
			}
			return new SdaiIterator(this, obj, -1);
		}
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		return new SdaiIterator(this);
//		} // syncObject
	}

	public void attachIterator(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (iter == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		iter.myAggregate = this;
		Object [] myDataA;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (myType.shift == SdaiSession.INST_AGGR) {
				iter.myIndex = -1;
				iter.myOuterIndex = -1;
				iter.AggregationType = SdaiIterator.INSTANCES;
			} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
				iter.myIndex = -1;
				iter.AggregationType = SdaiIterator.INSTANCES_EXACT;
			} else {
				iter.myIndex = 0;
				iter.myOuterIndex = -1;
				iter.AggregationType = SdaiIterator.ALL_INSTANCES;
			}
		} else if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			iter.myIndex = -1;
			iter.behind = false;
			iter.AggregationType = SdaiIterator.INSTANCES_COMPL;
		} else if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			iter.myIndex = -1;
			iter.myOuterIndex = -1;
			ASdaiModel amod = (ASdaiModel)myData;
			if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
				iter.myElement = amod.myData[0];
			} else {
				iter.subtypeIndex = 0;
			}
			iter.AggregationType = SdaiIterator.ASDAIMODEL_INST_ALL;
			return;
		} else if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			iter.myIndex = -1;
			iter.myOuterIndex = -1;
			myDataA = (Object [])myData;
			ASdaiModel amod = (ASdaiModel)myDataA[0];
			if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
				iter.myElement = amod.myData[0];
			} else {
				iter.memberIndex = 0;
			}
			iter.AggregationType = SdaiIterator.ASDAIMODEL_INST;
			return;
		} else if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			iter.myIndex = -1;
			myDataA = (Object [])myData;
			ASdaiModel amod = (ASdaiModel)myDataA[0];
			if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
				iter.myElement = amod.myData[0];
			} else {
				iter.memberIndex = 0;
			}
			iter.AggregationType = SdaiIterator.ASDAIMODEL_INST_EXACT;
			return;
		} else {
			if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
			}
			if (myLength < 0) {
				resolveAllConnectors();
			}
			iter.myIndex = -1;
			iter.behind = false;
			iter.AggregationType = SdaiIterator.ENTITY_AGGR;
		}
		iter.myElement = null;
//		} // syncObject
	}


	public int getLowerBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int getUpperBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public int query(String where, EEntity entity, AEntity result) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public ASdaiModel getQuerySourceDomain() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
// 		SdaiModel sm = this.getByIndexObject(1).findEntityInstanceSdaiModel();
// 		ASdaiModel domain = new ASdaiModel();
// 		domain.addByIndex(1, sm, null);
	}

	public AEntity getQuerySourceInstances() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

    /**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

    /**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public Object getByIndexObject(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		Object [] myDataA = null;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (myType.shift == SdaiSession.INST_AGGR) {
				return getByIndexInstance(index, model);
			} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
				return getByIndexInstanceExact(index, model);
			} else {
				return getByIndexInstanceAll(index, model);
			}
		}
		if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myDataA[index] == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return myDataA[index];
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			return getByIndexListOfModelsAll(index);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			return getByIndexListOfModels(index);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			return getByIndexListOfModelsExact(index);
		}
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		Object obj;
		ListElement el_value = null;
		boolean sel = false;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElement element;
			if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
				sel = true;
				if (myLength == 1) {
					myDataA = (Object [])myData;
					obj = myDataA[0];
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					index *= 2;
					while (index-- > 0) {
						element = element.next;
					}
					el_value = element;
					obj = element.object;
				}
			} else {
				if (myLength == 1) {
					obj = myData;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					el_value = element;
					obj = element.object;
				}
			}
			if (obj == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (obj instanceof Integer) {
				if (!sel) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				Object obj_next;
				if (myLength == 1) {
					obj_next = myDataA[1];
				} else {
					obj_next = el_value.next.object;
				}
				checkInteger((Integer)obj, obj_next);
				return obj;
//			} else if (obj instanceof SdaiModel.Connector) {
//				return resolveConnector(el_value, sel, (SdaiModel.Connector)obj, index);
			} else {
				return obj;
			}
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myType.select != null && myType.select.is_mixed > 0) {
				sel = true;
				myDataA = (Object [])myData;
				obj = myDataA[index*2];
			} else {
				if (myLength == 1) {
					obj = myData;
				} else {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				}
			}
			if (obj == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (obj instanceof Integer) {
				if (!sel) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				checkInteger((Integer)obj, myDataA[index*2 + 1]);
				return obj;
//			} else if (obj instanceof SdaiModel.Connector) {
//				return resolveConnector(sel, (SdaiModel.Connector)obj, index);
			} else {
				return obj;
			}
		}
//		} // syncObject
	}


/**
	Given an index, returns the element with this index in the set which consists 
	of instances of a specified entity data type and all its subtypes in an 
	<code>SdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations with parameter specifying an entity) 
	in class <code>SdaiModel</code>. The set is represented by an aggregate in 
	which the array <code>myData</code> contains two elements: the first is an 
	<code>SdaiModel</code> containing instances, whereas the second is the 
	underlying schema of this model.
	An order in which elements in the set appear is a subject of implementation: 
	first, instances of the provided entity type in increasing order of their identifiers 
	are listed, then in the same manner instances of its subtypes are enumerated.
*/
	Object getByIndexInstance(int index, SdaiModel model) throws SdaiException {
		int index_to_type = -1;
		int index_inside = -1;
		index--;
		if (index < 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int index_to_type_local;
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index_to_type_local = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
		} else {
			index_to_type_local = myLength;
		}
		int sum;
		if (index_to_type_local >= 0) {
			sum = model.lengths[index_to_type_local];
		} else {
			sum = 0;
		}
		if (index < sum) {
			index_to_type = index_to_type_local;
			index_inside = index;
		} else {
			Object [] myDataA = (Object [])myData;
			if (myDataA[1] != null) {
				int subtypes[] = ((CSchema_definition)myDataA[1]).getSubtypes(myLength);
				if (subtypes != null) {
					for (int i = 0; i < subtypes.length; i++) {
						int old_sum = sum;
						if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								model.repository != SdaiSession.systemRepository) {
							index_to_type_local = model.find_entityRO(model.dictionary.schemaData.entities[subtypes[i]]);
						} else {
							index_to_type_local = subtypes[i];
						}
						if (index_to_type_local >= 0) {
							sum += model.lengths[index_to_type_local];
						}
						if (index < sum) {
							index_to_type = index_to_type_local;
							index_inside = index - old_sum;
							break;
						}
					}
				}
			}
		}
		if (index_to_type < 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		return model.instances_sim[index_to_type][index_inside];
	}


/**
	Given an index, returns the element with this index in the set which consists 
	of instances of a specified entity data type (but not its subtypes) in an 
	<code>SdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>SdaiModel</code>. 
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first is an <code>SdaiModel</code> containing 
	instances, whereas the second is the underlying schema of this model.
	Instances in the set appear in increasing order of their identifiers. 
*/
	Object getByIndexInstanceExact(int index, SdaiModel model) throws SdaiException {
		index--;
		int index_to_type_local;
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index_to_type_local = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
			if (index_to_type_local < 0) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
		} else {
			index_to_type_local = myLength;
		}
		if (index < model.lengths[index_to_type_local] && index >= 0) {
			return model.instances_sim[index_to_type_local][index];
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


/**
	Given an index, returns the element with this index in the set which consists 
	of all instances of an <code>SdaiModel</code>. This set is returned by 
	<code>getInstances</code> method in class <code>SdaiModel</code>. 
	The set is represented by an aggregate in which the first element of the 
	array <code>myData</code> is an <code>SdaiModel</code> whose instances are 
	considered.
	An order in which elements in the set appear is a subject of implementation: 
	entity types are considered alphabetically according to their names, and for 
	each entity type instances in the set are enumerated in increasing order 
	of their identifiers. 
*/
	Object getByIndexInstanceAll(int index, SdaiModel model) throws SdaiException {
		index--;
		if (index < 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int sum = 0;
		for (int i = 0; i <  model.lengths.length; i++) {
			if (sum + model.lengths[i] > index) {
				return model.instances_sim[i][index - sum];
			}
			sum += model.lengths[i];
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


	Object getByIndexListOfModelsAll(int index) throws SdaiException {
		if (index <= 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		ASdaiModel amod = (ASdaiModel)myData;
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement start_el = amod.to_mod;
			if (start_el == null) {
				model = getModelWithEntity(amod, (ListElement)amod.myData[0], 0, 0, index);
			} else if (index <= amod.count) {
				amod.to_mod = null;
				amod.ext_index = 0;
				amod.count = 0;
				model = getModelWithEntity(amod, (ListElement)amod.myData[0], 0, 0, index);
			} else {
				SdaiModel start_mod = (SdaiModel)start_el.object;
				if (amod.ext_index < start_mod.lengths.length - 1) {
					model = getModelWithEntity(amod, start_el, 0, amod.ext_index + 1, index - amod.count);
				} else {
					model = getModelWithEntity(amod, start_el.next, 0, 0, index - amod.count);
				}
			}
		} else {
			int start_ind = amod.to_mod_set;
			if (start_ind < 0) {
				model = getModelWithEntity(amod, null, 0, 0, index);
			} else if (index <= amod.count) {
				amod.to_mod_set = -1;
				amod.ext_index = 0;
				amod.count = 0;
				model = getModelWithEntity(amod, null, 0, 0, index);
			} else {
				SdaiModel start_mod = (SdaiModel)amod.myData[amod.to_mod_set];
				if (amod.ext_index < start_mod.lengths.length - 1) {
					model = getModelWithEntity(amod, null, amod.to_mod_set, amod.ext_index + 1, index - amod.count);
				} else {
					model = getModelWithEntity(amod, null, amod.to_mod_set + 1, 0, index - amod.count);
				}
			}
		}

		if (model != null) {
			return model.instances_sim[model.ext_index][index - amod.count - 1];
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


	private SdaiModel getModelWithEntity(ASdaiModel amod, ListElement st_element, int mod_index, int st_extent, int count) 
			throws SdaiException {
		SdaiModel model;
		int saved_ext = amod.ext_index;
		int saved_count = amod.count;
		int sum = 0;
		int st_index;
		boolean first = true;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement saved_el = amod.to_mod;
			ListElement element = st_element;
			while (element != null) {
				model = (SdaiModel)element.object;
				if (first) {
					st_index = st_extent;
					first = false;
				} else {
					st_index = 0;
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					for (int i = st_index; i <  model.lengths.length; i++) {
						if (sum + model.lengths[i] >= count) {
							amod.to_mod = saved_el;
							amod.ext_index = saved_ext;
							amod.count = saved_count;
							model.ext_index = i;
							return model;
						}
						sum += model.lengths[i];
						saved_el = element;
						saved_ext = i;
						saved_count += model.lengths[i];
					}
				}
				element = element.next;
			}
		} else {
			int saved_model_index = amod.to_mod_set;
			for (int j = mod_index; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if (first) {
					st_index = st_extent;
					first = false;
				} else {
					st_index = 0;
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) > 0) {
					for (int i = st_index; i <  model.lengths.length; i++) {
						if (sum + model.lengths[i] >= count) {
							amod.to_mod_set = saved_model_index;
							amod.ext_index = saved_ext;
							amod.count = saved_count;
							model.ext_index = i;
							return model;
						}
						sum += model.lengths[i];
						saved_model_index = j;
						saved_ext = i;
						saved_count += model.lengths[i];
					}
				}
			}
		}
		return null;
	}


/**
	Given an index, returns the element with this index in the set which consists 
	of all instances of a specified entity data type and all instances of all its 
	subtypes collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) instances 
	of which are collected. 
	An order in which elements in the set appear is a subject of implementation: 
	models are taken as they are listed in <code>ASdaiModel</code>; for each 
	model, instances of the provided entity type are enumerated in increasing order 
	of their identifiers, then in the same manner instances of its subtypes are 
	considered.
*/
	Object getByIndexListOfModels(int index) throws SdaiException {
		if (index <= 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int count = 0;
		int old_count;
		int true_extent_index;
		SdaiModel model;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					element = element.next;
					continue;
				}
				old_count = count;
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					count += model.countEntityInstancesRO(true_extent_index);
				} else {
					count += model.countEntityInstances(true_extent_index);
				}
				if (count >= index) {
					return model.getByIndexListOfModels(index - old_count, myDataA[1]);
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					continue;
				}
				old_count = count;
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					count += model.countEntityInstancesRO(true_extent_index);
				} else {
					count += model.countEntityInstances(true_extent_index);
				}
				if (count >= index) {
					return model.getByIndexListOfModels(index - old_count, myDataA[1]);
				}
			}
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


/**
	Given an index, returns the element with this index in the set which consists 
	of all instances of a specified entity data type (but not instances of its 
	subtypes) collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
	An order in which elements in the set appear is a subject of implementation: 
	models are taken as they are listed in <code>ASdaiModel</code>; for each 
	model, instances of the provided entity type are enumerated in increasing order 
	of their identifiers. 
*/
	Object getByIndexListOfModelsExact(int index) throws SdaiException {
		if (index <= 0) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int count = 0;
		int old_count;
		int true_extent_index;
		SdaiModel model;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					element = element.next;
					continue;
				}
				old_count = count;
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						element = element.next;
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					true_extent_index = 
						model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[true_extent_index]);
				}
				if (true_extent_index >= 0) {
					count += model.lengths[true_extent_index];
				}
				if (count >= index) {
					return model.getByIndexListOfModelsExact(index - old_count, myDataA[1]);
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
					continue;
				}
				old_count = count;
				if (myDataA[1] == model.extent_type) {
					true_extent_index = model.extent_index;
				} else if (myDataA[1] instanceof CEntity_definition) {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				} else {
					true_extent_index = 
						model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
					if (true_extent_index < 0) {
						continue;
					}
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					true_extent_index = 
						model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[true_extent_index]);
				}
				if (true_extent_index >= 0) {
					count += model.lengths[true_extent_index];
				}
				if (count >= index) {
					return model.getByIndexListOfModelsExact(index - old_count, myDataA[1]);
				}
			}
		}
		throw new SdaiException(SdaiException.IX_NVLD, this);
	}


/**
	Checks if the submitted aggregate value of integer type, which belongs to 
	the list of selections of a select type, means unset case. If so, then 
	SdaiException VA_NSET is thrown. 
*/
	private void checkInteger(Integer value, Object sel_obj) throws SdaiException {
		int value_int = value.intValue();
		if (!(sel_obj instanceof Integer)) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int sel = ((Integer)sel_obj).intValue();
		if (myType.select.tags[sel - 2] == PhFileReader.INTEGER) {
			if (value_int == Integer.MIN_VALUE) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
		} else {
			if (value_int == 0) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
		}
	}


/**
	Given an index and select path number, returns the aggregate element with this index 
	provided the path number attached to it is equal to the specified one.
	The method is invoked in compiler generated early binding methods.
*/
	protected Object pGetByIndexObject(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrByIndex(index)) {
			return getByIndexObject(index);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public int getByIndexInt(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getByIndexObject(index);
		if (value instanceof Integer) {
			return ((Integer)value).intValue();
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an index and select path number, returns the element of integer type 
	with this index within the aggregate provided the path number attached to this 
	element is equal to the specified one. The method is invoked in compiler 
	generated early binding methods.
*/
	protected int pGetByIndexInt(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrByIndex(index)) {
			return getByIndexInt(index);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public double getByIndexDouble(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getByIndexObject(index);
		if (value instanceof Double) {
			Double value_double = (Double)value;
			if (value_double.isNaN()) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return value_double.doubleValue();
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an index and select path number, returns the element of double type 
	with this index within the aggregate provided the path number attached to this 
	element is equal to the specified one. The method is invoked in compiler 
	generated early binding methods.
*/
	protected double pGetByIndexDouble(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrByIndex(index)) {
			return getByIndexDouble(index);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public boolean getByIndexBoolean(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getByIndexObject(index);
		if (value instanceof Integer) {
			int value_int = ((Integer)value).intValue();
			if (value_int == 1) {
				return false;
			} else if (value_int == 2) {
				return true;
			} else {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an index and select path number, returns the element of boolean type 
	with this index within the aggregate provided the path number attached to this 
	element is equal to the specified one. The method is invoked in compiler 
	generated early binding methods.
*/
	protected boolean pGetByIndexBoolean(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrByIndex(index)) {
			return getByIndexBoolean(index);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public int getValueBoundByIndex(int index) throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null && !SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity aggr_owner = null;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR) {
			if (!(getOwner() instanceof SdaiRepository)) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
		} else {
			if (myType != null) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				aggr_owner = (CEntity)owner;
				if (aggr_owner == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = aggr_owner.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof Aggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		if (aggr_owner != null) {
			aggr_owner.owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
		}
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myType != null) {
						updateInverseList(myData, value, aggr_owner);
					}
					myData = value;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					if (myType != null) {
						updateInverseList(myDataA[index], value, aggr_owner);
					}
					myDataA[index] = value;
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					if (myType != null) {
						updateInverseList(element.object, value, aggr_owner);
					}
					element.object = value;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myType != null) {
						updateInverseList(myDataA[0], value, aggr_owner);
					}
					myDataA[0] = value;
					myDataA[1] = getIntegerWithValue(sel_number);
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					index *= 2;
					while (index-- > 0) {
						element = element.next;
					}
					if (myType != null) {
						updateInverseList(element.object, value, aggr_owner);
					}
					element.object = value;
					element = element.next;
					element.object = getIntegerWithValue(sel_number);
				}
			}
		} else if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			if (myData == null) {
				EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
				initializeData(upper_bound.getBound_value(null) - lower_index + 1);
			}
			index -= lower_index;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (sel_number <= 0) {
				if (myLength == 1) {
					updateInverseList(myData, value, aggr_owner);
					myData = value;
				} else {
					myDataA = (Object [])myData;
					updateInverseList(myDataA[index], value, aggr_owner);
					myDataA[index] = value;
				}
			} else {
				myDataA = (Object [])myData;
				int first_index = index * 2;
				updateInverseList(myDataA[first_index], value, aggr_owner);
				myDataA[first_index] = value;
				myDataA[first_index + 1] = getIntegerWithValue(sel_number);
			}
		} else {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


/**
	Puts a submitted value into the aggregate at the specified index position. 
	The select information is represented by the select path number 
	corresponding to the value being assigned. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pSetByIndex(int index, Object value, int path) throws SdaiException {
//		synchronized (syncObject) {
		CDefined_type select[] = myType.select.getSelectArray(path);
		setByIndex(index, value, select);
//		} // syncObject
	}


	public void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		setByIndex(index, new Integer(value), select);
//		} // syncObject
	}


/**
	Puts a submitted value of integer type into the aggregate at the specified 
	index position. The select information is represented by the select path number 
	corresponding to the value being assigned. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pSetByIndex(int index, int value, int path) throws SdaiException {
//		synchronized (syncObject) {
		CDefined_type select[] = myType.select.getSelectArray(path);
		setByIndex(index, value, select);
//		} // syncObject
	}


	public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		setByIndex(index, new Double(value), select);
//		} // syncObject
	}


/**
	Puts a submitted value of double type into the aggregate at the specified 
	index position. The select information is represented by the select path number 
	corresponding to the value being assigned. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pSetByIndex(int index, double value, int path) throws SdaiException {
//		synchronized (syncObject) {
		CDefined_type select[] = myType.select.getSelectArray(path);
		setByIndex(index, value, select);
//		} // syncObject
	}


	public void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val = (value) ? 2 : 1;
		setByIndex(index, new Integer(int_val), select);
//		} // syncObject
	}


/**
	Puts a submitted value of boolean type into the aggregate at the specified 
	index position. The select information is represented by the select path number 
	corresponding to the value being assigned. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pSetByIndex(int index, boolean value, int path) throws SdaiException {
//		synchronized (syncObject) {
		CDefined_type select[] = myType.select.getSelectArray(path);
		setByIndex(index, value, select);
//		} // syncObject
	}


	public Aggregate createAggregateByIndex(int index, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		Aggregate aggr_member;
		if (myType == null || myType.shift <= SdaiSession.PRIVATE_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
if (SdaiSession.debug2) System.out.println("   CAggregate  sel_number = " + sel_number +
"    aggr_member: " + aggr_member.getClass().getName());
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		StaticFields staticFields;
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
// aggr_member is put at position index
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					removeFromInverseList(myData);
					myData = aggr_member;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[index]);
					myDataA[index] = aggr_member;
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					removeFromInverseList(element.object);
					element.object = aggr_member;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[0]);
					myDataA[0] = aggr_member;
					myDataA[1] = getIntegerWithValue(sel_number);
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					index *= 2;
					while (index-- > 0) {
						element = element.next;
					}
					removeFromInverseList(element.object);
					element.object = aggr_member;
					element = element.next;
					element.object = getIntegerWithValue(sel_number);
				}
			}
		} else if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			if (myData == null) {
				EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
				initializeData(upper_bound.getBound_value(null) - lower_index + 1);
			}
			index -= lower_index;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
// aggr_member is put at position index
			if (sel_number <= 0) {
				if (myLength == 1) {
					removeFromInverseList(myData);
					myData = aggr_member;
				} else {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[index]);
					myDataA[index] = aggr_member;
				}
			} else {
				myDataA = (Object [])myData;
				int first_index = index * 2;
				removeFromInverseList(myDataA[first_index]);
				myDataA[first_index] = aggr_member;
				myDataA[first_index + 1] = getIntegerWithValue(sel_number);
			}
		} else {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate as a value at the specified index position of the 
	aggregate represented by this class. The select information is provided by 
	the select path number corresponding to the aggregate-member being created. 
	The method is invoked in compiler generated early binding methods.
*/
	protected Aggregate pCreateAggregateByIndex(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
		CDefined_type select[] = myType.select.getSelectArray(path);
		return createAggregateByIndex(index, select);
//		} // syncObject
	}


	public int testByIndex(int index, EDefined_type select[]) throws SdaiException {
		int integ;
		double doub;
//		synchronized (syncObject) {
		int sel_number = 0;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.AI_NVLD);
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
			}
			if (myType.select != null && myType.select.is_mixed > 0) {
				if (select == null) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				sel_number = 1;
			}
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		int tag = -1;
		Object obj;
		ListElement el_value = null;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElement element;
			if (sel_number <= 0) {
				if (select != null && select.length > 0) {
					select[0] = null;
				}
				if (myLength == 1) {
					obj = myData;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					el_value = element;
					obj = element.object;
				}
				if (obj == null) {
					return 0;
				} else if (obj instanceof Integer || obj instanceof Double) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
//					if (obj instanceof SdaiModel.Connector) {
//						resolveConnector(el_value, false, (SdaiModel.Connector)obj, index);
//					}
					return 1;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					obj = myDataA[0];
					integ = ((Integer)myDataA[1]).intValue();
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					index *= 2;
					while (index-- > 0) {
						element = element.next;
					}
					el_value = element;
					obj = element.object;
					integ = ((Integer)element.next.object).intValue();
				}
				if (integ == 1) {
					if (select != null && select.length > 0) {
						select[0] = null;
					}
				} else {
					int result = myType.select.giveDefinedTypes(integ, select);
					if (result == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INSI;
						throw new SdaiException(SdaiException.SY_ERR, base);
					} else if (result == -1) {
						throw new SdaiException(SdaiException.VA_NVLD);
					}
					tag = myType.select.tags[integ - 2];
				}
				if (obj == null) {
					return 0;
				} else if (obj instanceof Integer) {
					integ = ((Integer)obj).intValue();
					if (tag == PhFileReader.BOOLEAN) {
						if (integ == 0) {
							return 0;
						} else {
							return 4;
						}
					} else if (tag == PhFileReader.INTEGER) {
						if (integ == Integer.MIN_VALUE) {
							return 0;
						} else {
							return 2;
						}
					} else {
						if (integ == 0) {
							return 0;
						} else {
							return 2;
						}
					}
				} else if (obj instanceof Double) {
					doub = ((Double)obj).doubleValue();
					if (Double.isNaN(doub)) {
						return 0;
					} else {
						return 3;
					}
				} else {
//					if (obj instanceof SdaiModel.Connector) {
//						resolveConnector(el_value, true, (SdaiModel.Connector)obj, index);
//					}
					return 1;
				}
			}
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (sel_number <= 0) {
				if (select != null && select.length > 0) {
					select[0] = null;
				}
				if (myLength == 1) {
					obj = myData;
				} else {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				}
				if (obj == null) {
					return 0;
				} else if (obj instanceof Integer || obj instanceof Double) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
//					if (obj instanceof SdaiModel.Connector) {
//						resolveConnector(false, (SdaiModel.Connector)obj, index);
//					}
					return 1;
				}
			} else {
				myDataA = (Object [])myData;
				index *= 2;
				obj = myDataA[index];
				integ = ((Integer)myDataA[index + 1]).intValue();
				if (integ == 1) {
					if (select != null && select.length > 0) {
						select[0] = null;
					}
				} else {
					int result = myType.select.giveDefinedTypes(integ, select);
					if (result == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INSI;
						throw new SdaiException(SdaiException.SY_ERR, base);
					} else if (result == -1) {
						throw new SdaiException(SdaiException.VA_NVLD);
					}
					tag = myType.select.tags[integ - 2];
				}
				if (obj == null) {
					return 0;
				} else if (obj instanceof Integer) {
					integ = ((Integer)obj).intValue();
					if (tag == PhFileReader.BOOLEAN) {
						if (integ == 0) {
							return 0;
						} else {
							return 4;
						}
					} else if (tag == PhFileReader.INTEGER) {
						if (integ == Integer.MIN_VALUE) {
							return 0;
						} else {
							return 2;
						}
					} else {
						if (integ == 0) {
							return 0;
						} else {
							return 2;
						}
					}
				} else if (obj instanceof Double) {
					doub = ((Double)obj).doubleValue();
					if (Double.isNaN(doub)) {
						return 0;
					} else {
						return 3;
					}
				} else {
//					if (obj instanceof SdaiModel.Connector) {
//						resolveConnector(true, (SdaiModel.Connector)obj, index);
//					}
					return 1;
				}
			}
		}
//		} // syncObject
	}


	public int testByIndex(int index) throws SdaiException {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select != null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		return testByIndex(index, null);
	}


/**
	Returns select path number for the aggregate element specified by the index. 
	The method is invoked in compiler generated early binding methods. 
*/
	protected int pTestByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		int value = getCurrentSelectionNrByIndex(index);
		if (value < 0) {
			String base = SdaiSession.line_separator + "Failed to get select path number";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return value;
//		} // syncObject
	}


	public int getLowerIndex() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public int getUpperIndex() throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift == SdaiSession.PRIVATE_AGGR || 
				myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (!(myType.express_type == DataType.ARRAY)) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myData == null) {
			return;
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		EBound bound = ((CArray_type)myType).getLower_index(null);
		int lower_index = bound.getBound_value(null);
		index -= lower_index;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		if (myType.select != null && myType.select.is_mixed > 0) {
			myDataA = (Object [])myData;
			int first_index = index * 2;
			removeFromInverseList(myDataA[first_index]);
			myDataA[first_index] = null;
			myDataA[first_index + 1] = null;
		} else {
			if (myLength == 1) {
				removeFromInverseList(myData);
				myData = null;
			} else {
				myDataA = (Object [])myData;
				removeFromInverseList(myDataA[index]);
				myDataA[index] = null;
			}
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}


	public void reindexArray() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


	public void resetArrayIndex(int lower, int upper) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity owning_instance = null;
		if (myType != null &&  myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			owning_instance = getOwningInstance();
			SdaiModel owning_model = owning_instance.owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if (owning_model.repository != SdaiSession.systemRepository) {
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
			}
			SdaiSession session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} else if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof CAggregate || value instanceof SessionAggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (myType != null && myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		index--;
		EBound bound = null;
		if (myType != null) {
			CList_type list_type = (CList_type)myType;
			if (list_type.testUpper_bound(null)) {
				bound = list_type.getUpper_bound(null);
			}
		}
		if (bound != null && myLength >= bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (owning_instance != null) {
			owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
			if (value instanceof CEntity) {
				CEntity new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
			}
		}
		Object [] myDataA;
		ListElement element = null;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = value;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				myDataA[index] = value;
				myDataA[1 - index] = myData;
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				switch(index) {
					case 0:
						element = new ListElement(value);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						break;
					case 1:
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(value);
						element.next.next = new ListElement(myDataA[1]);
						break;
					case 2:
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
						break;
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				if (index == 0) {
					new_element.next = element;
					myData = new_element;
				} else {
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						new_element.next = element.next;
					}
					element.next = new_element;
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				if (index == myLength) {
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = new_element;
				} else if (index == 0) {
					new_element.next = (ListElement)myDataA[0];
					myDataA[0] = new_element;
				}	else {
					element = (ListElement)myDataA[0];
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						new_element.next = element.next;
					}
					element.next = new_element;
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = value;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (index == 0) {
					element = new ListElement(value);
					element.next = new ListElement(getIntegerWithValue(sel_number));
					element.next.next = new ListElement(myDataA[0]);
					element.next.next.next = new ListElement(myDataA[1]);
				} else {
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(value);
					element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (index == 0) {
					add_element.next = element;
					myData = new_element;
				} else {
					index *= 2;
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						add_element.next = element.next;
					}
					element.next = new_element;
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (index == myLength) {
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = add_element;
				} else if (index == 0) {
					add_element.next = (ListElement)myDataA[0];
					myDataA[0] = new_element;
				}	else {
					element = (ListElement)myDataA[0];
					index *= 2;
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						add_element.next = element.next;
					}
					element.next = new_element;
				}
			}
		}
		myLength++;
		if (myType != null &&  
				(myType.shift != SdaiSession.PRIVATE_AGGR || getOwner() instanceof SdaiRepository)) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else if (myType == null) {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		myLength++;
//		} // syncObject
	}


/**
	Adds a new member of type <code>Object</code> at the specified index position to this
	aggregate, provided its type is LIST. The select information is represented by the 
	select path number corresponding to the value being added. The method is invoked in 
	compiler generated early binding methods.
*/
	protected void pAddByIndex(int index, Object value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addByIndex(index, value, select);
//		} // syncObject
	}


	public void addByIndex(int index, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addByIndex(index, new Integer(value), select);
//		} // syncObject
	}


/**
	Adds a new member of integer type at the specified index position to this
	aggregate, provided its type is LIST. The select information is represented by the 
	select path number corresponding to the value being added. The method is invoked in 
	compiler generated early binding methods.
*/
	protected void pAddByIndex(int index, int value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addByIndex(index, value, select);
//		} // syncObject
	}


	public void addByIndex(int index, double value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addByIndex(index, new Double(value), select);
//		} // syncObject
	}


/**
	Adds a new member of double type at the specified index position to this
	aggregate, provided its type is LIST. The select information is represented by the 
	select path number corresponding to the value being added. The method is invoked in 
	compiler generated early binding methods.
*/
	protected void pAddByIndex(int index, double value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addByIndex(index, value, select);
//		} // syncObject
	}


	public void addByIndex(int index, boolean value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		addByIndex(index, new Integer(int_val), select);
//		} // syncObject
	}


/**
	Adds a new member of boolean type at the specified index position to this
	aggregate, provided its type is LIST. The select information is represented by the 
	select path number corresponding to the value being added. The method is invoked in 
	compiler generated early binding methods.
*/
	protected void pAddByIndex(int index, boolean value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addByIndex(index, value, select);
//		} // syncObject
	}


	public Aggregate addAggregateByIndex(int index, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift == SdaiSession.PRIVATE_AGGR || 
				myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		index--;
		EBound bound;
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			bound = list_type.getUpper_bound(null);
		} else {
			bound = null;
		}
		if (bound != null && myLength >= bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Aggregate aggr_member;
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
//CDefined_type ddd = (CDefined_type)myType.getElement_type(null);
//System.out.println("  CAggregate    element_type: " + ddd.getName(null));
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		ListElement element = null;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = aggr_member;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				myDataA[index] = aggr_member;
				myDataA[1 - index] = myData;
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				switch(index) {
					case 0:
						element = new ListElement(aggr_member);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						break;
					case 1:
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(aggr_member);
						element.next.next = new ListElement(myDataA[1]);
						break;
					case 2:
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
						break;
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				if (index == 0) {
					new_element.next = element;
					myData = new_element;
				} else {
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						new_element.next = element.next;
					}
					element.next = new_element;
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				if (index == myLength) {
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = new_element;
				} else if (index == 0) {
					new_element.next = (ListElement)myDataA[0];
					myDataA[0] = new_element;
				}	else {
					element = (ListElement)myDataA[0];
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						new_element.next = element.next;
					}
					element.next = new_element;
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = aggr_member;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (index == 0) {
					element = new ListElement(aggr_member);
					element.next = new ListElement(getIntegerWithValue(sel_number));
					element.next.next = new ListElement(myDataA[0]);
					element.next.next.next = new ListElement(myDataA[1]);
				} else {
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(aggr_member);
					element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (index == 0) {
					add_element.next = element;
					myData = new_element;
				} else {
					index *= 2;
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						add_element.next = element.next;
					}
					element.next = new_element;
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (index == myLength) {
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = add_element;
				} else if (index == 0) {
					add_element.next = (ListElement)myDataA[0];
					myDataA[0] = new_element;
				}	else {
					element = (ListElement)myDataA[0];
					index *= 2;
					while (--index > 0) {
						element = element.next;
					}
					if (element.next != null) {
						add_element.next = element.next;
					}
					element.next = new_element;
				}
			}
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate and adds it at the specified index position to the
	aggregate represented by this class. The select information is provided by the 
	select path number corresponding to the aggregate-member being created. 
	The method is invoked in compiler generated early binding methods.
*/
	protected Aggregate pAddAggregateByIndex(int index, int path) throws SdaiException {
//		synchronized (syncObject) {
    	EDefined_type select[] = myType.select.getSelectArray(path);
		return addAggregateByIndex(index, select);
//		} // syncObject
	}


	public void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			aggr_owner = getOwningInstance();
			owning_model = aggr_owner.owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
				throw new SdaiException(SdaiException.MX_NRW, owning_model);
			}
			SdaiSession session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} else if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		if (myType != null && myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		index--;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (aggr_owner != null) {
			owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
		}
		Object [] myDataA;
		ListElement element;
		int first_index;
		if (sel_number <= 0) {
			if (myLength == 1) {
				if (myType != null) {
					removeFromInverseList(myData);
				}
				myData = null;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (myType != null) {
					removeFromInverseList(myDataA[index]);
				}
				myData = myDataA[1 - index];
				myDataA = null;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				if (myLength == 3) {
					myDataA = new Object[2];
					switch(index) {
						case 0:
							if (myType != null) {
								removeFromInverseList(element.object);
							}
							myDataA[0] = element.next.object;
							myDataA[1] = element.next.next.object;
							break;
						case 1:
							if (myType != null) {
								removeFromInverseList(element.next.object);
							}
							myDataA[0] = element.object;
							myDataA[1] = element.next.next.object;
							break;
						case 2:
							if (myType != null) {
								removeFromInverseList(element.next.next.object);
							}
							myDataA[0] = element.object;
							myDataA[1] = element.next.object;
							break;
					}
					myData = myDataA;
				} else {
					if (index == 0) {
						if (myType != null) {
							removeFromInverseList(element.object);
						}
						myData = element.next;
					} else {
						first_index = index;
						while (--first_index > 0) {
							element = element.next;
						}
						if (myType != null) {
							removeFromInverseList(element.next.object);
						}
						if (index == myLength - 1) {
							element.next = null;
						} else {
							element.next = element.next.next;
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				element = (ListElement)myDataA[0];
				if (index == 0) {
					if (myType != null) {
						removeFromInverseList(element.object);
					}
					myDataA[0] = element.next;
				}	else {
					first_index = index;
					while (--first_index > 0) {
						element = element.next;
					}
					if (myType != null) {
						removeFromInverseList(element.next.object);
					}
					if (index == myLength - 1) {
						element.next = null;
						myDataA[1] = element;
					} else {
						element.next = element.next.next;
					}
				}
				if (myLength == SHORT_AGGR + 1) {
					myData = myDataA[0];
				}
			}
		} else {
			ListElement before_element;
			if (myLength == 1) {
				myDataA = (Object [])myData;
				if (myType != null) {
					removeFromInverseList(myDataA[0]);
				}
				myData = null;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				if (myLength == 2) {
					myDataA = new Object[2];
					if (index == 0) {
						if (myType != null) {
							removeFromInverseList(element.object);
						}
						myDataA[0] = element.next.next.object;
						myDataA[1] = element.next.next.next.object;
					} else {
						if (myType != null) {
							removeFromInverseList(element.next.next.object);
						}
						myDataA[0] = element.object;
						myDataA[1] = element.next.object;
					}
					myData = myDataA;
				} else {
					if (index == 0) {
						if (myType != null) {
							removeFromInverseList(element.object);
						}
						myData = element.next.next;
					} else {
						first_index = index * 2;
						while (--first_index > 0) {
							element = element.next;
						}
						before_element = element;
						element = element.next.next;
						if (myType != null) {
							removeFromInverseList(before_element.next.object);
						}
						if (index == myLength - 1) {
							before_element.next = null;
						} else {
							before_element.next = element.next;
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				element = (ListElement)myDataA[0];
				if (index == 0) {
					if (myType != null) {
						removeFromInverseList(element.object);
					}
					myDataA[0] = element.next.next;
				}	else {
					first_index = index * 2;
					while (--first_index > 0) {
						element = element.next;
					}
					before_element = element;
					element = element.next.next;
					if (myType != null) {
						removeFromInverseList(before_element.next.object);
					}
					if (index == myLength - 1) {
						before_element.next = null;
						myDataA[1] = before_element;
					} else {
						before_element.next = element.next;
					}
				}
				if (myLength == SHORT_AGGR_SELECT + 1) {
					myData = myDataA[0];
				}
			}
		}
		myLength--;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
		if (myType != null) {
			if (myType.shift != SdaiSession.PRIVATE_AGGR || (getOwner() instanceof SdaiRepository)) {
				owner.modified();
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);				
			}
		} else if (myType == null) {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		SdaiModel owning_model = null;
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			owning_instance = getOwningInstance();
			owning_model = owning_instance.owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if (owning_model.repository != SdaiSession.systemRepository) {
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
			}
			SdaiSession session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} else if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		if (this instanceof A_string && !(value instanceof String)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (this instanceof A_binary && !(value instanceof Binary)) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (value instanceof CAggregate || value instanceof SessionAggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (myType != null && myType.express_type == DataType.ARRAY) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		EBound upper_bound = null;
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		}

		if (myLength < 0) {
			resolveAllConnectors();
		}
		if (owning_instance != null) {
			if (owning_model.repository.session.undo_redo_file != null && 
					owning_model.repository != SdaiSession.systemRepository) {
				owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
		}
		CEntity new_member;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (myType != null) {
				CList_type list_type = (CList_type)myType;
				if (list_type.testUpper_bound(null)) {
					upper_bound = list_type.getUpper_bound(null);
				}
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}

			ListElement element;
			ListElement new_element;
			if (sel_number <= 0) {
				if (myLength == 0) {
					myData = value;
				} else if (myLength == 1) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					myDataA[1] = value;
					myData = myDataA;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(value);
					myData = element;
				} else if (myLength <= SHORT_AGGR) {
					element = (ListElement)myData;
					new_element = new ListElement(value);
					while (element.next != null) {
						element = element.next;
					}
					element.next = new_element;
					if (myLength == SHORT_AGGR) {
						myDataA = new Object[2];
						myDataA[0] = myData;
						myDataA[1] = new_element;
						myData = myDataA;
					}
				} else {
					myDataA = (Object [])myData;
					new_element = new ListElement(value);
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = new_element;
				}
			} else {
				ListElement add_element;
				if (myLength == 0) {
					myDataA = new Object[2];
					myDataA[0] = value;
					myDataA[1] = getIntegerWithValue(sel_number);
					myData = myDataA;
				} else if (myLength == 1) {
					myDataA = (Object [])myData;
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(value);
					element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
					myData = element;
				} else if (myLength <= SHORT_AGGR_SELECT) {
					element = (ListElement)myData;
					new_element = new ListElement(value);
					add_element = new ListElement(getIntegerWithValue(sel_number));
					new_element.next = add_element;
					while (element.next != null) {
						element = element.next;
					}
					element.next = new_element;
					if (myLength == SHORT_AGGR_SELECT) {
						myDataA = new Object[2];
						myDataA[0] = myData;
						myDataA[1] = add_element;
						myData = myDataA;
					}
				} else {
					myDataA = (Object [])myData;
					new_element = new ListElement(value);
					add_element = new ListElement(getIntegerWithValue(sel_number));
					new_element.next = add_element;
					((ListElement)myDataA[1]).next = new_element;
					myDataA[1] = add_element;
				}
			}
			myLength++;
			if (myType != null && value instanceof CEntity) {
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
			}
			if (myType != null &&  
					(myType.shift != SdaiSession.PRIVATE_AGGR || getOwner() instanceof SdaiRepository)) {
				owner.modified();
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);				
			} else if (myType == null) {
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
			}
		} else {
			EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
			if (aggr_type.testUpper_bound(null)) {
				upper_bound = aggr_type.getUpper_bound(null);
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}

			int ln;
			if (sel_number <= 0) {
				if (myLength == 0) {
					 myData = value;
				} else if (myLength == 1) {
					if (upper_bound == null) {
						ln = Integer.MAX_VALUE;
					} else {
						ln = upper_bound.getBound_value(null);
					}
					Object obj = myData;
					if (ln <= 1) {
						ln = 2;
					}
					initializeData(ln);
					myDataA = (Object [])myData;
					myDataA[0] = obj;
					myDataA[1] = value;
					myLength = 1;
					myData = myDataA;
				} else {
					myDataA = (Object [])myData;
					if (myLength >= myDataA.length) {
						ensureCapacity(myLength);
						myDataA = (Object [])myData;
					}
					myDataA[myLength] = value;
				}
			} else {
				if (myLength == 0) {
					if (upper_bound == null) {
						ln = Integer.MAX_VALUE;
					} else {
						ln = upper_bound.getBound_value(null);
					}
					initializeData(ln);
				} else if (myLength * 2 >= ((Object [])myData).length) {						
					ensureCapacity(myLength);
				}
				myDataA = (Object [])myData;
				int first_index = myLength * 2;
				myDataA[first_index] = value;
				myDataA[first_index + 1] = getIntegerWithValue(sel_number);
			}
			myLength++;
			if (value instanceof CEntity) {
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR && owning_model.repository != SdaiSession.systemRepository) {
				owner.modified();
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
			}
		}
//		myLength++;
//		if (myType != null && value instanceof CEntity) {
//			((CEntity)value).inverseAdd(owning_instance);
//		}
//		} // syncObject
	}


/**
	Adds a new member of type <code>Object</code> to this aggregate. The select 
	information is represented by the select path number corresponding to the 
	value being added. The method is invoked in compiler generated early 
	binding methods.
*/
	protected void pAddUnordered(Object value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addUnordered(value, select);
//		} // syncObject
	}


	public void addUnordered(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addUnordered(new Integer(value), select);
//		} // syncObject
	}


/**
	Adds a new member of integer type to this aggregate. The select 
	information is represented by the select path number corresponding to the 
	value being added. The method is invoked in compiler generated early 
	binding methods.
*/
	protected void pAddUnordered(int value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addUnordered(value, select);
//		} // syncObject
	}


	public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addUnordered(new Double(value), select);
//		} // syncObject
	}


/**
	Adds a new member of double type to this aggregate. The select 
	information is represented by the select path number corresponding to the 
	value being added. The method is invoked in compiler generated early 
	binding methods.
*/
	protected void pAddUnordered(double value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addUnordered(value, select);
//		} // syncObject
	}


	public void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		addUnordered(new Integer(int_val), select);
//		} // syncObject
	}


/**
	Adds a new member of boolean type to this aggregate. The select 
	information is represented by the select path number corresponding to the 
	value being added. The method is invoked in compiler generated early 
	binding methods.
*/
	protected void pAddUnordered(boolean value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addUnordered(value, select);
//		} // syncObject
	}


/**
	It is a simplified version of <code>addUnordered</code> method, which 
	is used to build <code>bootEntityAggregate</code> in class SdaiSession.
*/
	void addUnorderedSY(Object value) throws SdaiException {
		EBound upper_bound = null;
		EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
		if (aggr_type.testUpper_bound(null)) {
			upper_bound = aggr_type.getUpper_bound(null);
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CEntity owning_instance = getOwningInstance();
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CEntity || value instanceof SdaiModel.Connector || value instanceof CLateBindingEntity) {
				sel_number = 1;
			} else {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
		}
		Object [] myDataA;
		int ln;
		if (sel_number <= 0) {
			if (myLength == 0) {
				 myData = value;
			} else if (myLength == 1) {
				if (upper_bound == null) {
					ln = Integer.MAX_VALUE;
				} else {
					ln = upper_bound.getBound_value(null);
				}
				Object obj = myData;
				if (ln <= 1) {
					ln = 2;
				}
				initializeData(ln);
				myDataA = (Object [])myData;
				myDataA[0] = obj;
				myDataA[1] = value;
				myLength = 1;
				myData = myDataA;
			} else {
				myDataA = (Object [])myData;
				if (myLength >= myDataA.length) {
					ensureCapacity(myLength);
					myDataA = (Object [])myData;
				}
				myDataA[myLength] = value;
			}
		} else {
			if (myLength == 0) {
				if (upper_bound == null) {
					ln = Integer.MAX_VALUE;
				} else {
					ln = upper_bound.getBound_value(null);
				}
				initializeData(ln);
			} else if (myLength * 2 >= ((Object [])myData).length) {						
				ensureCapacity(myLength);
			}
			myDataA = (Object [])myData;
			int first_index = myLength * 2;
			myDataA[first_index] = value;
			myDataA[first_index + 1] = getIntegerWithValue(sel_number);
		}
		if (value instanceof CEntity) {
			((CEntity)value).inverseAdd(owning_instance);
		}
		myLength++;
	}


	public Aggregate createAggregateUnordered(EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity owning_instance = getOwningInstance();
		SdaiModel owning_model = owning_instance.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		EBound upper_bound;
		EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
		if (aggr_type.testUpper_bound(null)) {
			upper_bound = aggr_type.getUpper_bound(null);
		} else {
			upper_bound = null;
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Aggregate aggr_member;
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Object [] myDataA;
		session.undoRedoModifyPrepare(owning_instance);
		int ln;
		if (sel_number <= 0) {
			if (myLength == 0) {
				 myData = aggr_member;
			} else if (myLength == 1) {
				if (upper_bound == null) {
					ln = Integer.MAX_VALUE;
				} else {
					ln = upper_bound.getBound_value(null);
				}
				Object obj = myData;
				if (ln <= 1) {
					ln = 2;
				}
				initializeData(ln);
				myDataA = (Object [])myData;
				myDataA[0] = obj;
				myDataA[1] = aggr_member;
				myLength = 1;
				myData = myDataA;
			} else {
				myDataA = (Object [])myData;
				if (myLength >= myDataA.length) {
					ensureCapacity(myLength);
					myDataA = (Object [])myData;
				}
				myDataA[myLength] = aggr_member;
			}
		} else {
			if (myData == null) {
				if (upper_bound == null) {
					ln = Integer.MAX_VALUE;
				} else {
					ln = upper_bound.getBound_value(null);
				}
				initializeData(ln);
				myDataA = (Object [])myData;
			} else {
				myDataA = (Object [])myData;
				if (myLength * 2 >= myDataA.length) {						
					ensureCapacity(myLength);
					myDataA = (Object [])myData;
				}
			}
			int first_index = myLength * 2;
			myDataA[first_index] = aggr_member;
			myDataA[first_index + 1] = getIntegerWithValue(sel_number);
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate and adds it to the aggregate represented by this class. 
	The select information is provided by the select path number corresponding 
	to the aggregate-member being created. The method is invoked in compiler 
	generated early binding methods.
*/
	protected Aggregate pCreateAggregateUnordered(int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return createAggregateUnordered(select);
//		} // syncObject
	}


	public void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		SdaiModel owning_model = null;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			if(getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			owning_instance = getOwningInstance();
			owning_model = owning_instance.owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
				throw new SdaiException(SdaiException.MX_NRW, owning_model);
			}
			SdaiSession session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		int i, index = -1;
		if (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				if (value instanceof CEntity) {
					sel_number = 1;
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				sel_number = myType.select.giveSelectNumber(select);
				if (sel_number == -1) {
					String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
					throw new SdaiException(SdaiException.VA_NVLD, base);
				} else if (sel_number == -2) {
					String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		if (myLength == 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		if(owning_instance != null && owning_model != null) {
			owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		}
		Object [] myDataA;
		if (sel_number <= 0) {
			if (myLength == 1) {
				if (myData == value) {
					index = 0;
					removeFromInverseList(myData);
					myData = null;
				}
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (myDataA[0] == value) {
					index = 0;
					removeFromInverseList(myDataA[0]);
					myData = myDataA[1];
				} else if (myDataA[1] == value) {
					index = 1;
					removeFromInverseList(myDataA[1]);
					myData = myDataA[0];
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					if (myDataA[i] == value) {
						index = i;
						removeFromInverseList(myDataA[i]);
						myDataA[i] = myDataA[myLength - 1];
						myDataA[myLength - 1] = null;
						break;
					}
				}
			}
			if (index < 0) {
				throw new SdaiException(SdaiException.VA_NEXS);
			} else {
				myLength--;
			}
		} else {
			boolean mark;
			int numb;
			myDataA = (Object [])myData;
			for (i = 0; i < myLength; i++) {
				int first_index = i * 2;
//System.out.println("CAggregate ???  myDataA[first_index] = " + myDataA[first_index] + 
//"   value: " + value + "  its class: " + value_class + "   member_class: " + member_class);
				boolean coincide = false;
				if (value instanceof Integer) {
					if (myDataA[first_index] instanceof Integer) {
						int integ = ((Integer)value).intValue();
						numb = ((Integer)myDataA[first_index + 1]).intValue();
						int tag = myType.select.tags[numb - 2];
						if (tag == PhFileReader.INTEGER) {
							if (integ == Integer.MIN_VALUE) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
						} else {
							if (integ == 0) {
								throw new SdaiException(SdaiException.VA_NSET);
							}
						}
						int int_memb = ((Integer)myDataA[first_index]).intValue();
						if (int_memb == integ) {
							coincide = true;
						}
					}
				} else if (value instanceof Double) {
					if (myDataA[first_index] instanceof Double) {
						double doub = ((Double)value).doubleValue();
						if (Double.isNaN(doub)) {
							throw new SdaiException(SdaiException.VA_NSET);
						}
						double doub_memb = ((Double)myDataA[first_index]).doubleValue();
						if (doub_memb == doub) {
							coincide = true;
						}
					}
				} else if (myDataA[first_index] == value) {
					coincide = true;
				}
//				if (myDataA[first_index] == value) {
				if (coincide) {
					numb = ((Integer)myDataA[first_index + 1]).intValue();
					if (numb == sel_number) {
						mark = true;
					} else {
						mark = false;
					}
					if (mark) {
						index = i;
						removeFromInverseList(myDataA[index * 2]);
						for (int j = 0; j < 2; j++) {
							myDataA[index * 2 + j] = myDataA[(myLength - 1) * 2 + j];
							myDataA[(myLength - 1) * 2 + j] = null;
						}
						myLength--;
						break;
					}
				}
			}
			if (index < 0) {
				throw new SdaiException(SdaiException.VA_NEXS);
			}
		}
		if(owner != null) {
			owner.modified();
		}
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}


/**
	Removes the specified value of type <code>Object</code> from the aggregate 
	represented by this class. The select information is provided by the select 
	path number corresponding to the value being removed. The method is invoked 
	in compiler generated early binding methods.
*/
	protected void pRemoveUnordered(Object value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		removeUnordered(value, select);
//		} // syncObject
	}


	public void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		removeUnordered(new Integer(value), select);
//		} // syncObject
	}


/**
	Removes the specified value of integer type from the aggregate represented 
	by this class. The select information is provided by the select path number 
	corresponding to the value being removed. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pRemoveUnordered(int value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		removeUnordered(value, select);
//		} // syncObject
	}


	public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		removeUnordered(new Double(value), select);
//		} // syncObject
	}


/**
	Removes the specified value of double type from the aggregate represented 
	by this class. The select information is provided by the select path number 
	corresponding to the value being removed. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pRemoveUnordered(double value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		removeUnordered(value, select);
//		} // syncObject
	}


	public void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		removeUnordered(new Integer(int_val), select);
//		} // syncObject
	}


/**
	Removes the specified value of boolean type from the aggregate represented 
	by this class. The select information is provided by the select path number 
	corresponding to the value being removed. The method is invoked in compiler 
	generated early binding methods.
*/
	protected void pRemoveUnordered(boolean value, int path) throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		removeUnordered(value, select);
//		} // syncObject
	}


	public int testCurrentMember(SdaiIterator iter, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (iter == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (iter.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Object [] myDataA;
		if (myType.shift <= SdaiSession.INST_AGGR && myType.shift >= SdaiSession.ALL_INST_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (select != null && select.length > 0) {
				select[0] = null;
			}
			if (myType.shift == SdaiSession.ALL_INST_AGGR) {
				if (iter.myOuterIndex < 0 || iter.myOuterIndex >= model.lengths.length || 
						model.lengths[iter.myOuterIndex] <= iter.myIndex) {
					throw new SdaiException(SdaiException.IR_NSET, iter);
				}
				return 1;
			}
			int index;
			if (myType.shift == SdaiSession.INST_AGGR) {
				if (iter.myOuterIndex == -1) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
							model.repository != SdaiSession.systemRepository) {
						index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
					} else {
						index = myLength;
					}
				} else {
					int subtypes[] = ((CSchema_definition)myDataA[1]).getSubtypes(myLength);
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
							model.repository != SdaiSession.systemRepository) {
						index = model.find_entityRO(model.dictionary.schemaData.entities[subtypes[iter.myOuterIndex]]);
					} else {
						index = subtypes[iter.myOuterIndex];
					}
				}
			} else {
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
				} else {
					index = myLength;
				}
			}
			int inst_count;
			if (index >= 0) {
				inst_count =  model.lengths[index];
			} else {
				inst_count = 0;
			}
			if (iter.myIndex < 0 || iter.myIndex >= inst_count) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			return 1;
		}
		if (myType.shift == SdaiSession.INST_COMPL_AGGR) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (select != null && select.length > 0) {
				select[0] = null;
			}
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myDataA[iter.myIndex] == null) {
				return 0;
			} else {
				return 1;
			}
		}
		if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}

		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		int tag = -1;
		int integ;
		double doub;
		Object value;
		int sel_number = 0;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null && (iter.behind || myLength == 0 || iter.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myType.select != null && myType.select.is_mixed > 0) {
				if (select == null) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				sel_number = 1;
			}
			ListElement element = null;
			if (sel_number <= 0) {
				if (select != null && select.length > 0) {
					select[0] = null;
				}
				if (myLength == 1) {
					value = myData;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					value = myDataA[iter.myIndex];
				} else {
					element = (ListElement)iter.myElement;
					value = element.object;
				}
				if (value == null) {
					return 0;
				} else if (value instanceof Integer || value instanceof Double) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
					if (value instanceof SdaiModel.Connector) {
						resolveConnector(element, false, (SdaiModel.Connector)value, iter.myIndex);
					}
					return 1;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					value = myDataA[0];
					integ = ((Integer)myDataA[1]).intValue();
				} else {
					element = (ListElement)iter.myElement;
					value = element.object;
					integ = ((Integer)element.next.object).intValue();
				}
				if (integ == 1) {
					if (select != null && select.length > 0) {
						select[0] = null;
					}
				} else {
					int result = myType.select.giveDefinedTypes(integ, select);
					if (result == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INSI;
						throw new SdaiException(SdaiException.SY_ERR, base);
					} else if (result == -1) {
						throw new SdaiException(SdaiException.VA_NVLD);
					}
					tag = myType.select.tags[integ - 2];
				}
				if (value == null) {
					return 0;
				} else if (value instanceof Integer) {
					integ = ((Integer)value).intValue();
					if (tag == PhFileReader.BOOLEAN) {
						if (integ == 0) {
							return 0;
						} else {
							return 4;
						}
					} else if (tag == PhFileReader.INTEGER) {
						if (integ == Integer.MIN_VALUE) {
							return 0;
						} else {
							return 2;
						}
					} else {
						if (integ == 0) {
							return 0;
						} else {
							return 2;
						}
					}
				} else if (value instanceof Double) {
					doub = ((Double)value).doubleValue();
					if (Double.isNaN(doub)) {
						return 0;
					} else {
						return 3;
					}
				} else {
					if (value instanceof SdaiModel.Connector) {
						resolveConnector(element, true, (SdaiModel.Connector)value, 0);
					}
					return 1;
				}
			}
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myType.select != null && myType.select.is_mixed > 0) {
				if (select == null) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				sel_number = 1;
			}
			if (sel_number <= 0) {
				if (select != null && select.length > 0) {
					select[0] = null;
				}
				if (myLength == 1) {
					value = myData;
				} else {
					myDataA = (Object [])myData;
					value = myDataA[iter.myIndex];
				}
				if (value == null) {
					return 0;
				} else if (value instanceof Integer || value instanceof Double) {
					throw new SdaiException(SdaiException.SY_ERR);
				} else {
					if (value instanceof SdaiModel.Connector) {
						resolveConnector(false, (SdaiModel.Connector)value, iter.myIndex);
					}
					return 1;
				}
			} else {
				myDataA = (Object [])myData;
				int true_index = iter.myIndex * 2;
				value = myDataA[true_index];
				integ = ((Integer)myDataA[true_index + 1]).intValue();
				if (integ == 1) {
					if (select != null && select.length > 0) {
						select[0] = null;
					}
				} else {
					int result = myType.select.giveDefinedTypes(integ, select);
					if (result == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INSI;
						throw new SdaiException(SdaiException.SY_ERR, base);
					} else if (result == -1) {
						throw new SdaiException(SdaiException.VA_NVLD);
					}
					tag = myType.select.tags[integ - 2];
				}
				if (value == null) {
					return 0;
				} else if (value instanceof Integer) {
					integ = ((Integer)value).intValue();
					if (tag == PhFileReader.BOOLEAN) {
						if (integ == 0) {
							return 0;
						} else {
							return 4;
						}
					} else if (tag == PhFileReader.INTEGER) {
						if (integ == Integer.MIN_VALUE) {
							return 0;
						} else {
							return 2;
						}
					} else {
						if (integ == 0) {
							return 0;
						} else {
							return 2;
						}
					}
				} else if (value instanceof Double) {
					doub = ((Double)value).doubleValue();
					if (Double.isNaN(doub)) {
						return 0;
					} else {
						return 3;
					}
				} else {
					if (value instanceof SdaiModel.Connector) {
						resolveConnector(true, (SdaiModel.Connector)value, iter.myIndex);
					}
					return 1;
				}
			}
		}
//		} // syncObject
	}


	public int testCurrentMember(SdaiIterator iter) throws SdaiException {
		if (myType == null || myType.select != null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		return testCurrentMember(iter, null);
	}


/**
	Returns select path number for the aggregate element referenced by the 
	iterator. The method is invoked in compiler generated early binding methods. 
*/
	protected int pTestCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		int value = getCurrentSelectionNrAsCurrentMember(iter, true);
		if (value < 0) {
			String base = SdaiSession.line_separator + "Failed to get select path number";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return value;
//		} // syncObject
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public int testCurrentMemberI(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		int value = getCurrentSelectionNrAsCurrentMember(iter, false);
		if (value < 0) {
			String base = SdaiSession.line_separator + "Failed to get select path number";
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		return value;
//		} // syncObject
	}


	public Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (iter == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (iter.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		Object [] myDataA = null;
		if (myType != null && (myType.shift <= SdaiSession.INST_AGGR && 
				myType.shift >= SdaiSession.ALL_INST_AGGR)) {
			myDataA = (Object [])myData;
			SdaiModel model = (SdaiModel)myDataA[0];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (myType.shift == SdaiSession.ALL_INST_AGGR) {
				if (iter.myOuterIndex < 0 || iter.myOuterIndex >= model.lengths.length || 
						model.lengths[iter.myOuterIndex] <= iter.myIndex) {
					throw new SdaiException(SdaiException.IR_NSET, iter);
				}
				return model.instances_sim[iter.myOuterIndex][iter.myIndex];
			}
			int index;
			if (myType.shift == SdaiSession.INST_AGGR) {
				if (iter.myOuterIndex == -1) {
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
							model.repository != SdaiSession.systemRepository) {
						index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
					} else {
						index = myLength;
					}
				} else {
					int subtypes[] = ((CSchema_definition)myDataA[1]).getSubtypes(myLength);
					if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
							model.repository != SdaiSession.systemRepository) {
						index = model.find_entityRO(model.dictionary.schemaData.entities[subtypes[iter.myOuterIndex]]);
					} else {
						index = subtypes[iter.myOuterIndex];
					}
				}
			} else {
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
				} else {
					index = myLength;
				}
			}
			int inst_count;
			if (index >= 0) {
				inst_count =  model.lengths[index];
			} else {
				inst_count = 0;
			}
			if (iter.myIndex >= 0 && iter.myIndex < inst_count) {
				return model.instances_sim[index][iter.myIndex];
			}
			throw new SdaiException(SdaiException.IR_NSET, iter);
		}
		if (myType != null && myType.shift == SdaiSession.INST_COMPL_AGGR) {
			SdaiModel model = (SdaiModel)myDataA[myLength];
			if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myDataA[iter.myIndex] == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return myDataA[iter.myIndex];
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			return getCurrentMemberListOfModelsAll(iter);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			return getCurrentMemberListOfModels(iter);
		}
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			return getCurrentMemberListOfModelsExact(iter);
		}
		if (myType != null && myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = getOwningInstance().owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}
		Object return_value;
		boolean sel = false;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (iter.myElement == null && (iter.behind || myLength == 0 || iter.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			ListElement element = null;
			if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
				sel = true;
				if (myLength == 1) {
					myDataA = (Object [])myData;
					return_value = myDataA[0];
				} else {
					element = (ListElement)iter.myElement;
					return_value = element.object;
				}
			} else {
				if (myLength == 1) {
					return_value = myData;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					return_value = myDataA[iter.myIndex];
				} else {
					element = (ListElement)iter.myElement;
					return_value = element.object;
				}
			}
			if (return_value == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (return_value instanceof Integer) {
				Object obj_next;
				if (myLength == 1) {
					obj_next = myDataA[1];
				} else {
					obj_next = element.next.object;
				}
				checkInteger((Integer)return_value, obj_next);
				return return_value;
			} else if (return_value instanceof SdaiModel.Connector) {
				return resolveConnector(element, sel, (SdaiModel.Connector)return_value, iter.myIndex);
			} else {
				return return_value;
			}
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myType.select == null || myType.select.is_mixed == 0) {
				if (myLength == 1) {
					return_value = myData;
				} else {
					myDataA = (Object [])myData;
					return_value = myDataA[iter.myIndex];
				}
			} else {
				sel = true;
				myDataA = (Object [])myData;
				return_value = myDataA[iter.myIndex * 2];
			}
			if (return_value == null) {
//System.out.println();
//System.out.println("  CAggregate   myType: " + myType.getClass().getName() +
//"   index = " + index + "   iter.myIndex = " + iter.myIndex + "  myLength = " + myLength +
//"  owning_instance: #" + owning_instance.instance_identifier + 
//"  owning model: " + owning_instance.owning_model.name);
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if (return_value instanceof Integer) {
				if (!sel) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				checkInteger((Integer)return_value, myDataA[iter.myIndex*2 + 1]);
				return return_value;
			} else if (return_value instanceof SdaiModel.Connector) {
				return resolveConnector(sel, (SdaiModel.Connector)return_value, iter.myIndex);
			} else {
				return return_value;
			}
		}
//		} // syncObject
	}


	Object getCurrentMemberListOfModelsAll(SdaiIterator iter) throws SdaiException {
		ASdaiModel amod = (ASdaiModel)myData;
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)iter.myElement;
			if (element == null) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			model = (SdaiModel)element.object;
		} else {
			if (iter.subtypeIndex < 0 || iter.subtypeIndex >= amod.myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			model = (SdaiModel)amod.myData[iter.subtypeIndex];
		}
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (iter.myOuterIndex < 0 || iter.myOuterIndex >= model.lengths.length || 
				model.lengths[iter.myOuterIndex] <= iter.myIndex) {
			throw new SdaiException(SdaiException.IR_NSET, iter);
		}
		return model.instances_sim[iter.myOuterIndex][iter.myIndex];
	}


/**
	Returns the element referenced by the iterator in the set which consists 
	of all instances of a specified entity data type and all instances of all its 
	subtypes collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) instances 
	of which are collected. 
	An order in which elements in the set appear is a subject of implementation: 
	models are taken as they are listed in <code>ASdaiModel</code>; for each 
	model, instances of the provided entity type are enumerated in increasing order 
	of their identifiers, then in the same manner instances of its subtypes are 
	considered.
*/
	Object getCurrentMemberListOfModels(SdaiIterator iter) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)iter.myElement;
			model = (SdaiModel)element.object;
		} else {
			if (iter.memberIndex < 0 || iter.memberIndex >= amod.myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			model = (SdaiModel)amod.myData[iter.memberIndex];
		}
		int index;
		if (myDataA[1] == model.extent_type) {
			index = model.extent_index;
		} else if (myDataA[1] instanceof CEntity_definition) {
			index = 
				model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
			if (index < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)myDataA[1]);
			}
		} else {
			index = 
				model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
			if (index == -1) {
				throw new SdaiException(SdaiException.ED_NDEF);
			} else if (index == -2) {
				throw new SdaiException(SdaiException.ED_NVLD);
			}
		}
		if (iter.myOuterIndex >= 0) {
			if (model.underlying_schema == null) {
				model.getUnderlyingSchema();
			}
			int subtypes[] = model.underlying_schema.getSubtypes(index);
			index = subtypes[iter.myOuterIndex];
		}
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index = model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[index]);
		}
		int inst_count;
		if (index >= 0) {
			inst_count =  model.lengths[index];
		} else {
			inst_count = 0;
		}
		if (iter.myIndex >= 0 && iter.myIndex < inst_count) {
			return model.instances_sim[index][iter.myIndex];
		}
		throw new SdaiException(SdaiException.IR_NSET, iter);
	}


/**
	Returns the element referenced by the iterator in the set which consists 
	of all instances of a specified entity data type (but not instances of its 
	subtypes) collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>.
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
	An order in which elements in the set appear is a subject of implementation: 
	models are taken as they are listed in <code>ASdaiModel</code>; for each 
	model, instances of the provided entity type are enumerated in increasing order 
	of their identifiers. 
*/
	Object getCurrentMemberListOfModelsExact(SdaiIterator iter) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)iter.myElement;
			model = (SdaiModel)element.object;
		} else {
			if (iter.memberIndex < 0 || iter.memberIndex >= amod.myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			model = (SdaiModel)amod.myData[iter.memberIndex];
		}
		int index;
		if (myDataA[1] == model.extent_type) {
			index = model.extent_index;
		} else if (myDataA[1] instanceof CEntity_definition) {
			index = 
				model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
			if (index < 0) {
				throw new SdaiException(SdaiException.ED_NVLD, (CEntity_definition)myDataA[1]);
			}
		} else {
			index = 
				model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
			if (index == -1) {
				throw new SdaiException(SdaiException.ED_NDEF);
			}  else if (index == -2) {
				throw new SdaiException(SdaiException.ED_NVLD);
			}
		}
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index = model.find_entityRO(model.underlying_schema.owning_model.schemaData.entities[index]);
		}
		int inst_count;
		if (index >= 0) {
			inst_count =  model.lengths[index];
		} else {
			inst_count = 0;
		}
		if (iter.myIndex >= 0 && iter.myIndex < inst_count) {
			return model.instances_sim[index][iter.myIndex];
		}
		throw new SdaiException(SdaiException.IR_NSET, iter);
	}


/**
	Given an iterator and select path number, returns the aggregate element 
	referenced by the iterator provided the path number attached to the 
	element is equal to the specified one. The method is invoked in compiler 
	generated early binding methods.
*/
	protected Object pGetCurrentMemberObject(SdaiIterator iter, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrAsCurrentMember(iter, true)) {
			return getCurrentMemberObject(iter);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getCurrentMemberObject(iter);
		if (value instanceof Integer) {
			return ((Integer)value).intValue();
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an iterator and select path number, returns the aggregate element 
	of integer type referenced by the iterator provided the path number attached 
	to the element is equal to the specified one. The method is invoked in 
	compiler generated early binding methods.
*/
	protected int pGetCurrentMemberInt(SdaiIterator iter, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrAsCurrentMember(iter, true)) {
			return getCurrentMemberInt(iter);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getCurrentMemberObject(iter);
		if (value instanceof Double) {
			Double value_double = (Double)value;
			if (value_double.isNaN()) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return value_double.doubleValue();
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an iterator and select path number, returns the aggregate element 
	of double type referenced by the iterator provided the path number attached 
	to the element is equal to the specified one. The method is invoked in 
	compiler generated early binding methods.
*/
	protected double pGetCurrentMemberDouble(SdaiIterator iter, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrAsCurrentMember(iter, true)) {
			return getCurrentMemberDouble(iter);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public boolean getCurrentMemberBoolean(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		Object value = getCurrentMemberObject(iter);
		if (value instanceof Integer) {
			int value_int = ((Integer)value).intValue();
			if (value_int == 1) {
				return false;
			} else if (value_int == 2) {
				return true;
			}
		}
		throw new SdaiException(SdaiException.VT_NVLD);
//		} // syncObject
	}


/**
	Given an iterator and select path number, returns the aggregate element 
	of boolean type referenced by the iterator provided the path number attached 
	to the element is equal to the specified one. The method is invoked in 
	compiler generated early binding methods.
*/
	protected boolean pGetCurrentMemberBoolean(SdaiIterator iter, int path) throws SdaiException {
//		synchronized (syncObject) {
		if (path == getCurrentSelectionNrAsCurrentMember(iter, true)) {
			return getCurrentMemberBoolean(iter);
		}
		String base = SdaiSession.line_separator + AdditionalMessages.EB_SE_NC;
		throw new SdaiException(SdaiException.SY_ERR, base);
//		} // syncObject
	}


	public Aggregate createAggregateAsCurrentMember(SdaiIterator it,
			EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null || myType.shift <= SdaiSession.PRIVATE_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Aggregate aggr_member;
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			if (it.myElement == null && (it.behind || myLength == 0 || it.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (sel_number <= 0) {
				if (myLength == 1) {
					removeFromInverseList(myData);
					myData = aggr_member;
					it.myElement = aggr_member;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[it.myIndex]);
					myDataA[it.myIndex] = aggr_member;
					it.myElement = aggr_member;
				} else {
					removeFromInverseList(((ListElement)it.myElement).object);
					((ListElement)it.myElement).object = aggr_member;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[0]);
					myDataA[0] = aggr_member;
					myDataA[1] = getIntegerWithValue(sel_number);
					it.myElement = aggr_member;
				} else {
					ListElement element = (ListElement)it.myElement;
					removeFromInverseList(element.object);
					element.object = aggr_member;
					element = element.next;
					element.object = getIntegerWithValue(sel_number);
				}
			}
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (sel_number <= 0) {
				if (myLength == 1) {
					removeFromInverseList(myData);
					myData = aggr_member;
				} else {
					myDataA = (Object [])myData;
					removeFromInverseList(myDataA[it.myIndex]);
					myDataA[it.myIndex] = aggr_member;
				}
			} else {
				myDataA = (Object [])myData;
				int first_index = it.myIndex * 2;
				removeFromInverseList(myDataA[first_index]);
				myDataA[first_index] = aggr_member;
				myDataA[first_index + 1] = getIntegerWithValue(sel_number);
			}
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate at the position specified by the iterator in the  
	aggregate represented by this class. The select information is provided by 
	the select path number corresponding to the aggregate-member being created. 
	The method is invoked in compiler generated early binding methods.
*/
	protected Aggregate pCreateAggregateAsCurrentMember(SdaiIterator iter, int path) 
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return createAggregateAsCurrentMember(iter, select);
//		} // syncObject
	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null) {
			if (owner == null) {
				if (!SdaiSession.isSessionAvailable()) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			} else {
				SdaiCommon own = getOwner();
				if (own == null) {
					if (!SdaiSession.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				} else if (own instanceof SdaiSession && !((SdaiSession)own).opened) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
			}
		}
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity aggr_owner = null;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR) {
			if (!(getOwner() instanceof SdaiRepository)) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
		} else {
			if (myType != null) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				aggr_owner = getOwningInstance();
				SdaiModel owning_model = aggr_owner.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof CAggregate || value instanceof SessionAggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		}
		if (aggr_owner != null) {
			aggr_owner.owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
		}
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (it.myElement == null && (it.behind || myLength == 0 || it.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myType != null) {
						updateInverseList(myData, value, aggr_owner);
					}
					myData = value;
					it.myElement = value;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					if (myType != null) {
						updateInverseList(myDataA[it.myIndex], value, aggr_owner);
					}
					myDataA[it.myIndex] = value;
					it.myElement = value;
				} else {
					if (myType != null) {
						updateInverseList(((ListElement)it.myElement).object, value, aggr_owner);
					}
					((ListElement)it.myElement).object = value;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myType != null) {
						updateInverseList(myDataA[0], value, aggr_owner);
					}
					myDataA[0] = value;
					myDataA[1] = getIntegerWithValue(sel_number);
					it.myElement = value;
				} else {
					ListElement element = (ListElement)it.myElement;
					if (myType != null) {
						updateInverseList(element.object, value, aggr_owner);
					}
					element.object = value;
					element = element.next;
					element.object = getIntegerWithValue(sel_number);
				}
			}
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (sel_number <= 0) {
				if (myLength == 1) {
					updateInverseList(myData, value, aggr_owner);
					myData = value;
				} else {
					myDataA = (Object [])myData;
					updateInverseList(myDataA[it.myIndex], value, aggr_owner);
					myDataA[it.myIndex] = value;
				}
			} else {
				myDataA = (Object [])myData;
				int first_index = it.myIndex * 2;
				updateInverseList(myDataA[first_index], value, aggr_owner);
				myDataA[first_index] = value;
				myDataA[first_index + 1] = getIntegerWithValue(sel_number);
			}
		}
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


/**
	Puts a submitted value of type <code>Object</code> into the aggregate at 
	the position specified by the iterator. The select information is 
	represented by the select path number corresponding to the value being 
	assigned. The method is invoked in compiler generated early binding methods.
*/
	protected void pSetCurrentMember(SdaiIterator iter, Object value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		setCurrentMember(iter, value, select);
//		} // syncObject
	}


	public void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		setCurrentMember(iter, new Integer(value), select);
//		} // syncObject
	}


/**
	Puts a submitted value of integer type into the aggregate at the position 
	specified by the iterator. The select information is represented by the 
	select path number corresponding to the value being assigned. The method is 
	invoked in compiler generated early binding methods.
*/
	protected void pSetCurrentMember(SdaiIterator iter, int value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		setCurrentMember(iter, value, select);
//		} // syncObject
	}


	public void setCurrentMember(SdaiIterator iter, double value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		setCurrentMember(iter, new Double(value), select);
//		} // syncObject
	}


/**
	Puts a submitted value of double type into the aggregate at the position 
	specified by the iterator. The select information is represented by the 
	select path number corresponding to the value being assigned. The method is 
	invoked in compiler generated early binding methods.
*/
	protected void pSetCurrentMember(SdaiIterator iter, double value, int path)
			throws SdaiException {
//		synchronized (syncObject) {	
		EDefined_type select[] = myType.select.getSelectArray(path);
		setCurrentMember(iter, value, select);
//		} // syncObject
	}


	public void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		setCurrentMember(iter, new Integer(int_val), select);
//		} // syncObject
	}


/**
	Puts a submitted value of boolean type into the aggregate at the position 
	specified by the iterator. The select information is represented by the 
	select path number corresponding to the value being assigned. The method is 
	invoked in compiler generated early binding methods.
*/
	protected void pSetCurrentMember(SdaiIterator iter, boolean value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		setCurrentMember(iter, value, select);
//		} // syncObject
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null && !SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity owning_instance = null;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR) {
			if (!(getOwner() instanceof SdaiRepository)) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
		} else {
			if (myType != null) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				owning_instance = getOwningInstance();
				SdaiModel owning_model = owning_instance.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof CAggregate || value instanceof SessionAggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (myType != null && myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		EBound upper_bound = null;
		if (myType != null) {
			CList_type list_type = (CList_type)myType;
			if (list_type.testUpper_bound(null)) {
				upper_bound = list_type.getUpper_bound(null);
			}
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (owning_instance != null) {
			owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
			if (value instanceof CEntity) {
				CEntity new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
			}
		}
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = value;
				it.myElement = null;
				it.behind = true;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				if (it.myElement != null) {
					myDataA[0] = value;
					myDataA[1] = myData;
					it.myIndex = 1;
				} else {
					if (it.behind) {
						myDataA[0] = myData;
						myDataA[1] = value;
					} else {
						myDataA[0] = value;
						myDataA[1] = myData;
						it.myElement = value;
						it.myIndex = 0;
					}
				}
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					if (it.myIndex == 0) {
						element = new ListElement(value);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element.next;
					} else {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(value);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element.next.next;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
					} else {
						element = new ListElement(value);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
						it.myIndex = 0;
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				if (it.myElement != null) {
					if (it.myElement == element) {
						new_element.next = element;
						myData = new_element;
					} else {
						while (element.next != it.myElement) {
							element=element.next;
						}
						new_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
					} else {
						new_element.next = element;
						myData = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				if (it.myElement != null) {
					if (it.myElement == myDataA[0]) {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					} else {
						element = (ListElement)myDataA[0];
						while (element.next != it.myElement) {
							element=element.next;
						}
						new_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = new_element;
					} else {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = value;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
				it.myElement = null;
				it.behind = true;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					element = new ListElement(value);
					element.next = new ListElement(getIntegerWithValue(sel_number));
					element.next.next = new ListElement(myDataA[0]);
					element.next.next.next = new ListElement(myDataA[1]);
					it.myElement = element.next.next;
					it.myIndex = 1;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
						element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
					} else {
						element = new ListElement(value);
						element.next = new ListElement(getIntegerWithValue(sel_number));
						element.next.next = new ListElement(myDataA[0]);
						element.next.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
						it.myIndex = 0;
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (it.myElement == element) {
						add_element.next = element;
						myData = new_element;
					} else {
						while (element.next != it.myElement) {
							element=element.next;
						}
						add_element.next = element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
					} else {
						add_element.next = element;
						myData = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (it.myElement == myDataA[0]) {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					} else {
						element = (ListElement)myDataA[0];
						while (element.next != it.myElement) {
							element=element.next;
						}
						add_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
					} else {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
			}
		}
		myLength++;
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


/**
	Adds a new member of type <code>Object</code> to this aggregate before the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddBefore(SdaiIterator iter, Object value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addBefore(iter, value, select);
//		} // syncObject
	}


	public void addBefore(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addBefore(iter, new Integer(value), select);
//		} // syncObject
	}


/**
	Adds a new member of integer type to this aggregate before the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddBefore(SdaiIterator iter, int value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addBefore(iter, value, select);
//		} // syncObject
	}


	public void addBefore(SdaiIterator iter, double value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addBefore(iter, new Double(value), select);
//		} // syncObject
	}


/**
	Adds a new member of double type to this aggregate before the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddBefore(SdaiIterator iter, double value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addBefore(iter, value, select);
//		} // syncObject
	}


	public void addBefore(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		addBefore(iter, new Integer(int_val), select);
//		} // syncObject
	}


/**
	Adds a new member of boolean type to this aggregate before the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddBefore(SdaiIterator iter, boolean value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addBefore(iter, value, select);
//		} // syncObject
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null && !SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity owning_instance = null;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR) {
			if (!(getOwner() instanceof SdaiRepository)) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
		} else {
			if (myType != null) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				owning_instance = getOwningInstance();
				SdaiModel owning_model = owning_instance.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if (owning_model.repository != SdaiSession.systemRepository) {
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (value instanceof CAggregate || value instanceof SessionAggregate) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		if (myType != null && myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		EBound upper_bound = null;
		if (myType != null) {
			CList_type list_type = (CList_type)myType;
			if (list_type.testUpper_bound(null)) {
				upper_bound = list_type.getUpper_bound(null);
			}
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		int tag = PhFileReader.ENTITY_REFERENCE;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			if (value instanceof CLateBindingEntity || value instanceof SdaiModel.Connector) {
				sel_number = 1;
			} else {
				if (select == null || select.length == 0 || select[0] == null) {
					if (value instanceof CEntity) {
						sel_number = 1;
					} else {
						throw new SdaiException(SdaiException.VT_NVLD);
					}
				} else {
					sel_number = myType.select.giveSelectNumber(select);
					if (sel_number == -1) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
						throw new SdaiException(SdaiException.VA_NVLD, base);
					} else if (sel_number == -2) {
						String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
					tag = myType.select.tags[sel_number - 2];
				}
			}
		}
		if (owning_instance != null) {
			if (owning_instance.owning_model.repository.session.undo_redo_file != null && 
					owning_instance.owning_model.repository != SdaiSession.systemRepository) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value(tag, myType.select, sel_number, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
			if (value instanceof CEntity) {
				CEntity new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
			}
		}
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = value;
				it.myElement = null;
				it.myIndex = -1;
				it.behind = false;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				if (it.myElement != null) {
					myDataA[0] = myData;
					myDataA[1] = value;
				} else {
					if (it.behind) {
						myDataA[0] = myData;
						myDataA[1] = value;
						it.myElement = value;
						it.myIndex = 1;
					} else {
						myDataA[0] = value;
						myDataA[1] = myData;
					}
				}
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					if (it.myIndex == 0) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(value);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
					} else {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
						it.myElement = element.next;
					}
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
						it.myElement = element.next.next;
						it.myIndex = 2;
					} else {
						element = new ListElement(value);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				if (it.myElement != null) {
					new_element.next = ((ListElement)it.myElement).next;
					((ListElement)it.myElement).next = new_element;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						new_element.next = element;
						myData = new_element;
					}
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				if (it.myElement != null) {
					new_element.next = ((ListElement)it.myElement).next;
					((ListElement)it.myElement).next = new_element;
					if (it.myElement == myDataA[1]) {
						myDataA[1] = new_element;
					}
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					}
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = value;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
				it.myElement = null;
				it.myIndex = -1;
				it.behind = false;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(value);
					element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
					it.myElement = element;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(value);
						element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
						it.myElement = element.next.next;
						it.myIndex = 1;
					} else {
						element = new ListElement(value);
						element.next = new ListElement(getIntegerWithValue(sel_number));
						element.next.next = new ListElement(myDataA[0]);
						element.next.next.next = new ListElement(myDataA[1]);
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					element = ((ListElement)it.myElement).next;
					add_element.next = element.next;
					element.next = new_element;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						add_element.next = element;
						myData = new_element;
					}
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (((ListElement)it.myElement).next == myDataA[1]) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
					} else {
						element = ((ListElement)it.myElement).next;
						add_element.next = element.next;
						element.next = new_element;
					}
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					}
				}
			}
		}
		myLength++;
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


/**
	Adds a new member of type <code>Object</code> to this aggregate after the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddAfter(SdaiIterator iter, Object value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addAfter(iter, value, select);
//		} // syncObject
	}


	public void addAfter(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == Integer.MIN_VALUE) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addAfter(iter, new Integer(value), select);
//		} // syncObject
	}


/**
	Adds a new member of integer type to this aggregate after the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddAfter(SdaiIterator iter, int value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addAfter(iter, value, select);
//		} // syncObject
	}


	public void addAfter(SdaiIterator iter, double value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		addAfter(iter, new Double(value), select);
//		} // syncObject
	}


/**
	Adds a new member of double type to this aggregate after the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddAfter(SdaiIterator iter, double value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addAfter(iter, value, select);
//		} // syncObject
	}


	public void addAfter(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (myType.select == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		int int_val;
		if (value) {
			int_val = 2;
		} else {
			int_val = 1;
		}
		addAfter(iter, new Integer(int_val), select);
//		} // syncObject
	}


/**
	Adds a new member of boolean type to this aggregate after the 
	position referenced by the iterator. The select information is represented 
	by the select path number corresponding to the value being added. The method 
	is invoked in compiler generated early binding methods.
*/
	protected void pAddAfter(SdaiIterator iter, boolean value, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		addAfter(iter, value, select);
//		} // syncObject
	}


	public Aggregate createAggregateBefore(SdaiIterator it, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null || myType.shift == SdaiSession.PRIVATE_AGGR || 
				myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		EBound upper_bound;
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			upper_bound = list_type.getUpper_bound(null);
		} else {
			upper_bound = null;
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Aggregate aggr_member;
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = aggr_member;
				it.myElement = null;
				it.behind = true;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				if (it.myElement != null) {
					myDataA[0] = aggr_member;
					myDataA[1] = myData;
					it.myIndex = 1;
				} else {
					if (it.behind) {
						myDataA[0] = myData;
						myDataA[1] = aggr_member;
					} else {
						myDataA[0] = aggr_member;
						myDataA[1] = myData;
						it.myElement = aggr_member;
						it.myIndex = 0;
					}
				}
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					if (it.myIndex == 0) {
						element = new ListElement(aggr_member);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element.next;
					} else {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(aggr_member);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element.next.next;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
					} else {
						element = new ListElement(aggr_member);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
						it.myIndex = 0;
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				if (it.myElement != null) {
					if (it.myElement == element) {
						new_element.next = element;
						myData = new_element;
					} else {
						while (element.next != it.myElement) {
							element=element.next;
						}
						new_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
					} else {
						new_element.next = element;
						myData = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				if (it.myElement != null) {
					if (it.myElement == myDataA[0]) {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					} else {
						element = (ListElement)myDataA[0];
						while (element.next != it.myElement) {
							element=element.next;
						}
						new_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = new_element;
					} else {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = aggr_member;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
				it.myElement = null;
				it.behind = true;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					element = new ListElement(aggr_member);
					element.next = new ListElement(getIntegerWithValue(sel_number));
					element.next.next = new ListElement(myDataA[0]);
					element.next.next.next = new ListElement(myDataA[1]);
					it.myElement = element.next.next;
					it.myIndex = 1;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
						element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
					} else {
						element = new ListElement(aggr_member);
						element.next = new ListElement(getIntegerWithValue(sel_number));
						element.next.next = new ListElement(myDataA[0]);
						element.next.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
						it.myIndex = 0;
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (it.myElement == element) {
						add_element.next = element;
						myData = new_element;
					} else {
						while (element.next != it.myElement) {
							element=element.next;
						}
						add_element.next = element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
					} else {
						add_element.next = element;
						myData = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (it.myElement == myDataA[0]) {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					} else {
						element = (ListElement)myDataA[0];
						while (element.next != it.myElement) {
							element=element.next;
						}
						add_element.next=element.next;
						element.next = new_element;
					}
					it.myIndex++;
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
					} else {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
						it.myElement = new_element;
						it.myIndex = 0;
					}
				}
			}
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate before the position specified by the iterator in the  
	aggregate represented by this class. The select information is provided by 
	the select path number corresponding to the aggregate-member being created. 
	The method is invoked in compiler generated early binding methods.
*/
	protected Aggregate pCreateAggregateBefore(SdaiIterator iter, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return createAggregateBefore(iter, select);
//		} // syncObject
	}


	public Aggregate createAggregateAfter(SdaiIterator it, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (myType == null || myType.shift == SdaiSession.PRIVATE_AGGR || 
				myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
		CEntity aggr_owner = getOwningInstance();
		SdaiModel owning_model = aggr_owner.owning_model;
		if (owning_model == null) {
			throw new SdaiException(SdaiException.EI_NEXS);
		}
		if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
			throw new SdaiException(SdaiException.MX_NRW, owning_model);
		}
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		EBound upper_bound;
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			upper_bound = list_type.getUpper_bound(null);
		} else {
			upper_bound = null;
		}
		if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			if (select == null || select.length == 0 || select[0] == null) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			sel_number = myType.select.giveSelectNumber(select);
			if (sel_number == -1) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_NUNF;
				throw new SdaiException(SdaiException.VA_NVLD, base);
			} else if (sel_number == -2) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_INCP;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		Aggregate aggr_member;
		AggregationType new_type;
		try {
			if (sel_number > 0) {
				DataType a_type = (DataType)myType.select.types[sel_number - 2];
				if (a_type.express_type >= DataType.LIST && a_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)a_type;
					aggr_member = (Aggregate)new_type.getAggregateClass().newInstance();
				} else {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
			} else {
				aggr_member = (Aggregate)myType.getAggMemberImplClass().newInstance();
				DataType el_type = (DataType)myType.getElement_type(null);
				while (el_type.express_type == DataType.DEFINED_TYPE) {
					el_type = (DataType)((CDefined_type)el_type).getDomain(null);
				}
				if (el_type.express_type >= DataType.LIST && el_type.express_type <= DataType.AGGREGATE) {
					new_type = (AggregationType)el_type;
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
			if (aggr_member instanceof CAggregate) {
				((CAggregate)aggr_member).myType = new_type;
				((CAggregate)aggr_member).owner = this;
			} else {
				((A_primitive)aggr_member).myType = new_type;
				((A_primitive)aggr_member).owner = this;
			}
		} catch (java.lang.IllegalAccessException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_CREF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (sel_number <= 0) {
			if (myLength == 0) {
				myData = aggr_member;
				it.myElement = null;
				it.myIndex = -1;
				it.behind = false;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				if (it.myElement != null) {
					myDataA[0] = myData;
					myDataA[1] = aggr_member;
				} else {
					if (it.behind) {
						myDataA[0] = myData;
						myDataA[1] = aggr_member;
						it.myElement = aggr_member;
						it.myIndex = 1;
					} else {
						myDataA[0] = aggr_member;
						myDataA[1] = myData;
					}
				}
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					if (it.myIndex == 0) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(aggr_member);
						element.next.next = new ListElement(myDataA[1]);
						it.myElement = element;
					} else {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
						it.myElement = element.next;
					}
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
						it.myElement = element.next.next;
						it.myIndex = 2;
					} else {
						element = new ListElement(aggr_member);
						element.next = new ListElement(myDataA[0]);
						element.next.next = new ListElement(myDataA[1]);
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				if (it.myElement != null) {
					new_element.next = ((ListElement)it.myElement).next;
					((ListElement)it.myElement).next = new_element;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						new_element.next = element;
						myData = new_element;
					}
				}
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (new_element.next != null) {
						new_element = new_element.next;
					}
					myDataA[1] = new_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				if (it.myElement != null) {
					new_element.next = ((ListElement)it.myElement).next;
					((ListElement)it.myElement).next = new_element;
					if (it.myElement == myDataA[1]) {
						myDataA[1] = new_element;
					}
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						new_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					}
				}
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = aggr_member;
				myDataA[1] = getIntegerWithValue(sel_number);
				myData = myDataA;
				it.myElement = null;
				it.myIndex = -1;
				it.behind = false;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				if (it.myElement != null) {
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(aggr_member);
					element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
					it.myElement = element;
				} else {
					if (it.behind) {
						element = new ListElement(myDataA[0]);
						element.next = new ListElement(myDataA[1]);
						element.next.next = new ListElement(aggr_member);
						element.next.next.next = new ListElement(getIntegerWithValue(sel_number));
						it.myElement = element.next.next;
						it.myIndex = 1;
					} else {
						element = new ListElement(aggr_member);
						element.next = new ListElement(getIntegerWithValue(sel_number));
						element.next.next = new ListElement(myDataA[0]);
						element.next.next.next = new ListElement(myDataA[1]);
					}
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					element = ((ListElement)it.myElement).next;
					add_element.next = element.next;
					element.next = new_element;
				} else {
					if (it.behind) {
						while (element.next != null) {
							element = element.next;
						}
						element.next = new_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						add_element.next = element;
						myData = new_element;
					}
				}
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					while (add_element.next != null) {
						add_element = add_element.next;
					}
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(aggr_member);
				add_element = new ListElement(getIntegerWithValue(sel_number));
				new_element.next = add_element;
				if (it.myElement != null) {
					if (((ListElement)it.myElement).next == myDataA[1]) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
					} else {
						element = ((ListElement)it.myElement).next;
						add_element.next = element.next;
						element.next = new_element;
					}
				} else {
					if (it.behind) {
						((ListElement)myDataA[1]).next = new_element;
						myDataA[1] = add_element;
						it.myElement = new_element;
						it.myIndex = myLength;
					} else {
						add_element.next = (ListElement)myDataA[0];
						myDataA[0] = new_element;
					}
				}
			}
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
		return aggr_member;
//		} // syncObject
	}


/**
	Creates an aggregate after the position specified by the iterator in the  
	aggregate represented by this class. The select information is provided by 
	the select path number corresponding to the aggregate-member being created. 
	The method is invoked in compiler generated early binding methods.
*/
	protected Aggregate pCreateAggregateAfter(SdaiIterator iter, int path)
			throws SdaiException {
//		synchronized (syncObject) {
		EDefined_type select[] = myType.select.getSelectArray(path);
		return createAggregateAfter(iter, select);
//		} // syncObject
	}


	public void clear() throws SdaiException {
//		synchronized (syncObject) {
		int init_ln = myLength;
		CEntity owning_instance = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				owning_instance = getOwningInstance();
				SdaiModel owning_model = owning_instance.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				SdaiSession session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				session.undoRedoModifyPrepare(owning_instance);
				unsetAllByRef(owning_instance);
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		myData = null;
		myLength = 0;
		if (myType != null && myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial && init_ln > 0) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
		if (myType != null && (myType.shift != SdaiSession.PRIVATE_AGGR || 
				getOwner() instanceof SdaiRepository) && init_ln > 0) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else if (myType == null) {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}


	public EAggregation_type getAggregationType() throws SdaiException {
		if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		return myType;
	}



/**
	Initializes this aggregate: allocates memory for its elements and fixes 
	its length. Invoked in this class, in <code>SdaiIterator</code> and in 
	<code>SchemaDefinition</code>.
*/
	void initializeData(int bound) {
		myLength = 0;
		int shift;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			shift = 2;
		} else {
			shift = 1;
		}
		if (bound == Integer.MAX_VALUE) {
			if (shift <= 1) {
				myData = new Object[INIT_SIZE_SET];
			} else {
				myData = new Object[INIT_SIZE_SET_SELECT];
			}
		} else {
			if (myType.express_type == DataType.ARRAY) {
				myLength = bound;
				if (bound <= 1 && shift <= 1) {
					return;
				}
			}
			if (shift <= 1) {
				myData = new Object[bound];
			} else {
				myData = new Object[bound * 2];
			}
		}
	}


/**
	Enlarges the memory allocated for aggregate members either twice 
	or to satisfy the required demand (if the latter exceeds the doubled length of 
	the aggregate). Invoked in this class and in <code>SchemaDefinition</code>.
*/
	void ensureCapacity(int demand) {
		Object [] myDataA = (Object [])myData;
		int new_length = myDataA.length * 2;
		int shift = 1;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			shift = 2;
		}
		if (shift <= 1) {
			while (new_length < demand+1) {
				new_length *= 2;
			}
		} else {
			while (new_length < (demand + 1) * shift) {
				new_length *= 2;
			}
		}
		Object[] new_array = new Object[new_length];
		System.arraycopy(myDataA, 0, new_array, 0, myLength * shift);
		myData = new_array;
	}


/**
	Returns select path number for the aggregate element specified by the 
	index. If the base type of the aggregate is not a select type, then -1 is 
	returned. This method is invoked in get methods like 
	<code>pGetByIndexObject</code> used by compiler generated early binding 
	methods and in <code>pTestByIndex</code>. 
*/
	private int getCurrentSelectionNrByIndex(int index) throws SdaiException {
		if (myData == null) {
			return -1;
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		Object [] myDataA = null;
		Object obj;
		boolean sel = false;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myType != null && myType.select != null) {
				if (myType.select.is_mixed > 0) {
					index *= 2;
					sel = true;
				}
			} else {
				return -1;
			}
			ListElement element = null;
			Object obj_next = null;
			if (!sel) {
				if (myLength == 1) {
					obj = myData;
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					obj = element.object;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					obj = myDataA[0];
					obj_next = myDataA[1];
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (index-- > 0) {
						element = element.next;
					}
					obj = element.object;
					obj_next = element.next.object;
				}
			}
			if (obj instanceof SdaiModel.Connector) {
				resolveConnector(element, sel, (SdaiModel.Connector)obj, index);
			}
			if (sel) {
				return ((Integer)obj_next).intValue();
			} else {
				return 1;
			}
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				index -= bound.getBound_value(null);
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (myType.select != null) {
				if (myType.select.is_mixed > 0) {
					sel = true;
				}
			} else {
				return -1;
			}
			int true_index = -1;
			if (!sel) {
				if (myLength == 1) {
					obj = myData;
				} else {
					myDataA = (Object [])myData;
					obj = myDataA[index];
				}
			} else {
				true_index = index * 2;
				myDataA = (Object [])myData;
				obj = myDataA[true_index];
			}
			if (obj instanceof SdaiModel.Connector) {
				resolveConnector(sel, (SdaiModel.Connector)obj, index);
			}
			if (sel) {
				if (checkIfIsUnset(myDataA[true_index])) {
					if (myType.express_type == DataType.ARRAY) {
						return 0;
					} else {
						return -1;
					}
				}
				return ((Integer)myDataA[true_index + 1]).intValue();
			} else {
				return 1;
			}
		}
	}


/**
	Returns select path number for the aggregate element referenced by the 
	iterator. If the base type of the aggregate is not a select type, then -1 is 
	returned. This method is invoked in get methods like 
	<code>pGetCurrentMemberObject</code> used by compiler generated early binding 
	methods and also in <code>pTestCurrentMember</code> and 
	<code>testCurrentMemberI</code>. 
*/
	private int getCurrentSelectionNrAsCurrentMember(SdaiIterator it, boolean resolve_connector) 
			throws SdaiException {
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
    	}
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			return -1;
		}
		Object [] myDataA = null;
		Object obj;
		boolean sel = false;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (myType != null && myType.select != null) {
				if (it.myElement == null && (it.behind || myLength == 0 || it.myIndex < 0)) {
					throw new SdaiException(SdaiException.IR_NSET, it);
				}
				Object obj_next = null;
				if (myType.select.is_mixed > 0) {
					sel = true;
					if (myLength == 1) {
						myDataA = (Object [])myData;
						obj = myDataA[0];
						obj_next = myDataA[1];
					} else {
						obj = ((ListElement)it.myElement).object;
						obj_next = ((ListElement)it.myElement).next.object;
					}
				} else {
					if (myLength == 1) {
						obj = myData;
					} else if (myLength == 2) {
						myDataA = (Object [])myData;
						obj = myDataA[it.myIndex];
					} else {
						obj = ((ListElement)it.myElement).object;
					}
				}
				if (obj instanceof SdaiModel.Connector) {
					if (!resolve_connector) {
						return -1;
					}
					resolveConnector((ListElement)it.myElement, sel, (SdaiModel.Connector)obj, it.myIndex);
				}
				if (sel) {
					return ((Integer)obj_next).intValue();
				} else {
					return 1;
				}
			} else {
				return -1;
			}
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (myType.select == null) {
				return -1;
			}
			if (myType.select.is_mixed > 0) {
				sel = true;
				myDataA = (Object [])myData;
				obj = myDataA[it.myIndex * 2];
			} else {
				if (myLength == 1) {
					obj = myData;
				} else {
					myDataA = (Object [])myData;
					obj = myDataA[it.myIndex];
				}
			}
			if (obj instanceof SdaiModel.Connector) {
				if (!resolve_connector) {
					return -1;
				}
				resolveConnector(sel, (SdaiModel.Connector)obj, it.myIndex);
			}
			if (sel) {
				if (checkIfIsUnset(obj)) {
					if (myType.express_type == DataType.ARRAY) {
						return 0;
					} else {
						return -1;
					}
				}
				return ((Integer)myDataA[it.myIndex * 2 + 1]).intValue();
			} else {
				return 1;
			}
		}
  }


/**
	Returns <code>true</code> if the submitted value means unset case. 
	Method is invoked in <code>getCurrentSelectionNrByIndex</code> and in 
	<code>getCurrentSelectionNrAsCurrentMember</code>.
*/
	private boolean checkIfIsUnset(Object value) {
		if (value == null) {
			return true;
		} else if (value instanceof Integer) {
			int integ = ((Integer)value).intValue();
			if (integ == Integer.MIN_VALUE) {
				return true;
			}
		} else if (value instanceof Double) {
			double doub = ((Double)value).doubleValue();
			if (Double.isNaN(doub)) {
				return true;
			}
		}
		return false;
	}


/**
	Updates the inverse list for instances submitted through the parameters 
	of this method. The first parameter represents an old value of the aggregate. 
	If its type is entity instance, then the owner of the current aggregate 
	is removed from the inverse list of this entity instance. If, however, 
	the first parameter is an aggregate, then this procedure is applied 
	recursively for its elements by invoking <code>updateInverseListAggregate</code> 
	method. The second parameter specifies the new value of the aggregate. 
	If its type is entity instance, then the owner of the current aggregate 
	is added to the inverse list of this entity instance.
*/
	private void updateInverseList(Object current_value, Object new_value)
			throws SdaiException {
		CEntity owning_instance = getOwningInstance();
		if (current_value instanceof CEntity) {
			CEntity ex_member = (CEntity)current_value;
			if (!ex_member.owning_model.optimized) {
				ex_member.inverseRemove(owning_instance);
			}
		} else if (current_value instanceof CAggregate) {
			((CAggregate)current_value).updateInverseListAggregate(owning_instance);
		}
		if (new_value instanceof CEntity) {
			CEntity new_member = (CEntity)new_value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
		}
	}


	private void updateInverseList(Object current_value, Object new_value, CEntity owning_instance)
			throws SdaiException {
		if (current_value instanceof CEntity) {
			CEntity ex_member = (CEntity)current_value;
			if (!ex_member.owning_model.optimized) {
				ex_member.inverseRemove(owning_instance);
			}
		} else if (current_value instanceof CAggregate) {
			((CAggregate)current_value).updateInverseListAggregate(owning_instance);
		}
		if (new_value instanceof CEntity) {
			CEntity new_member = (CEntity)new_value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
		}
	}


/**
	Removes the owner of the current aggregate from the inverse list of each 
	instance contained in the current aggregate or in aggregates being its 
	(possibly nested) members. In the latter case the method is applied 
	recursively. 
*/
	private void updateInverseListAggregate() throws SdaiException {
		CEntity owning_instance = getOwningInstance();
		updateInverseListAggregateInternal(owning_instance);
	}

	private void updateInverseListAggregate(CEntity owning_instance) throws SdaiException {
		updateInverseListAggregateInternal(owning_instance);
	}


	private void updateInverseListAggregateInternal(CEntity owning_instance) throws SdaiException {
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return;
		}
		CEntity ex_member;
		Object [] myDataA;
		int i;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						ex_member = (CEntity)myData;
						if (!ex_member.owning_model.optimized) {
							ex_member.inverseRemove(owning_instance);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).updateInverseListAggregate(owning_instance);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							ex_member = (CEntity)myDataA[i];
							if (!ex_member.owning_model.optimized) {
								ex_member.inverseRemove(owning_instance);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).updateInverseListAggregate(owning_instance);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							ex_member = (CEntity)element.object;
							if (!ex_member.owning_model.optimized) {
								ex_member.inverseRemove(owning_instance);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).updateInverseListAggregate(owning_instance);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						ex_member = (CEntity)myDataA[0];
						if (!ex_member.owning_model.optimized) {
							ex_member.inverseRemove(owning_instance);
						}
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).updateInverseListAggregate(owning_instance);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							ex_member = (CEntity)element.object;
							if (!ex_member.owning_model.optimized) {
								ex_member.inverseRemove(owning_instance);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).updateInverseListAggregate(owning_instance);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						ex_member = (CEntity)myData;
						if (!ex_member.owning_model.optimized) {
							ex_member.inverseRemove(owning_instance);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).updateInverseListAggregate(owning_instance);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof CEntity) {
							ex_member = (CEntity)myDataA[i];
							if (!ex_member.owning_model.optimized) {
								ex_member.inverseRemove(owning_instance);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).updateInverseListAggregate(owning_instance);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] instanceof CEntity) {
						ex_member = (CEntity)myDataA[index];
						if (!ex_member.owning_model.optimized) {
							ex_member.inverseRemove(owning_instance);
						}
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).updateInverseListAggregate(owning_instance);
					}
				}
			}
		}
	}


	int validate_instances_aggregate() throws SdaiException {
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return 1;
		}
		DataType type = (DataType)((EAggregation_type)myType).getElement_type(null);
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		Object [] myDataA;
		int return_res = 1, res;
		int i;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						if (!((CEntity)myData).validate_entity(type, myType.select)) {
							return 0;
						}
					} else if (myData instanceof CAggregate) {
						res = ((CAggregate)myData).validate_instances_aggregate();
						if (res == 0) {
							return 0;
						} else if (res == 2) {
							return_res = 2;
						}
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							if (!((CEntity)myDataA[i]).validate_entity(type, myType.select)) {
								return 0;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							res = ((CAggregate)myDataA[i]).validate_instances_aggregate();
							if (res == 0) {
								return 0;
							} else if (res == 2) {
								return_res = 2;
							}
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							if (!((CEntity)element.object).validate_entity(type, myType.select)) {
								return 0;
							}
						} else if (element.object instanceof CAggregate) {
							res = ((CAggregate)element.object).validate_instances_aggregate();
							if (res == 0) {
								return 0;
							} else if (res == 2) {
								return_res = 2;
							}
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						if (!((CEntity)myDataA[0]).validate_entity(type, myType.select)) {
							return 0;
						}
					} else if (myDataA[0] instanceof CAggregate) {
						res = ((CAggregate)myDataA[0]).validate_instances_aggregate();
						if (res == 0) {
							return 0;
						} else if (res == 2) {
							return_res = 2;
						}
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							if (!((CEntity)element.object).validate_entity(type, myType.select)) {
								return 0;
							}
						} else if (element.object instanceof CAggregate) {
							res = ((CAggregate)element.object).validate_instances_aggregate();
							if (res == 0) {
								return 0;
							} else if (res == 2) {
								return_res = 2;
							}
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData == null) {
						if (myType.express_type == DataType.ARRAY && !((CArray_type)myType).getOptional_flag(null)) {
							if (return_res == 1 && myType.allow_entity_aggregate((EAggregation_type)myType)) {
								return_res = 2;
							}
						}
					} else if (myData instanceof CEntity) {
						if (!((CEntity)myData).validate_entity(type, myType.select)) {
							return 0;
						}
					} else if (myData instanceof CAggregate) {
						res = ((CAggregate)myData).validate_instances_aggregate();
						if (res == 0) {
							return 0;
						} else if (res == 2) {
							return_res = 2;
						}
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] == null) {
							if (myType.express_type == DataType.ARRAY && !((CArray_type)myType).getOptional_flag(null)) {
								if (return_res == 1 && myType.allow_entity_aggregate((EAggregation_type)myType)) {
									return_res = 2;
								}
							}
						} else if (myDataA[i] instanceof CEntity) {
							if (!((CEntity)myDataA[i]).validate_entity(type, myType.select)) {
								return 0;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							res = ((CAggregate)myDataA[i]).validate_instances_aggregate();
							if (res == 0) {
								return 0;
							} else if (res == 2) {
								return_res = 2;
							}
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] == null) {
						if (myType.express_type == DataType.ARRAY && !((CArray_type)myType).getOptional_flag(null)) {
							if (return_res == 1 && myType.allow_entity_aggregate((EAggregation_type)myType)) {
								return_res = 2;
							}
						}
					} else if (myDataA[index] instanceof CEntity) {
						if (!((CEntity)myDataA[index]).validate_entity(type, myType.select)) {
							return 0;
						}
					} else if (myDataA[index] instanceof CAggregate) {
						res = ((CAggregate)myDataA[index]).validate_instances_aggregate();
						if (res == 0) {
							return 0;
						} else if (res == 2) {
							return_res = 2;
						}
					}
				}
			}
		}
		return return_res;
	}


/**
	Removes the owner of the current aggregate from the inverse list of 
	instances submitted through the parameter of this method. The value of the 
	parameter may be an instance or an aggregate. In the latter case the 
	procedure of removal is applied recursively for the elements of the 
	aggregate by invoking <code>updateInverseListAggregate</code> method. 
	The method is used in this and <code>SdaiIterator</code> classes. 
*/
	void removeFromInverseList(Object value) throws SdaiException {
		if (value instanceof CEntity) {
			CEntity ex_member = (CEntity)value;
			if (!ex_member.owning_model.optimized) {
				ex_member.inverseRemove(getOwningInstance());
			}
		} else if (value instanceof CAggregate) {
			((CAggregate)value).updateInverseListAggregate();
		}
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void unsetAll() throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity owning_instance = getOwningInstance();
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		CEntity to_ent;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(owning_instance);
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAll(owning_instance);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(owning_instance);
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAll(owning_instance);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(owning_instance);
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAll(owning_instance);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						to_ent = (CEntity)myDataA[0];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(owning_instance);
						}
					} else if (myDataA[0] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[0]).disconnect();
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).unsetAll(owning_instance);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(owning_instance);
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAll(owning_instance);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(owning_instance);
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAll(owning_instance);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(owning_instance);
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAll(owning_instance);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] instanceof CEntity) {
						to_ent = (CEntity)myDataA[index];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(owning_instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(owning_instance);
						}
					} else if (myDataA[index] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[index]).disconnect();
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).unsetAll(owning_instance);
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
//		} // syncObject
	}


/** This method is for internal JSDAI use only. Applications shall not use it. */
	public void unsetAll(CEntity instance) throws SdaiException {
//		synchronized (syncObject) {
		if (myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		CEntity to_ent;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(instance);
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAll(instance);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(instance);
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAll(instance);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(instance);
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAll(instance);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						to_ent = (CEntity)myDataA[0];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(instance);
						}
					} else if (myDataA[0] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[0]).disconnect();
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).unsetAll(instance);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(instance);
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAll(instance);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(instance);
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAll(instance);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
								!to_ent.owning_model.optimized && 
								!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
								to_ent.inverseRemoveAll(instance);
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAll(instance);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] instanceof CEntity) {
						to_ent = (CEntity)myDataA[index];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll && 
							!to_ent.owning_model.optimized && 
							!(instance.owning_model.refresh_in_abort && to_ent.owning_model.modified)) {
							to_ent.inverseRemoveAll(instance);
						}
					} else if (myDataA[index] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[index]).disconnect();
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).unsetAll(instance);
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
//		} // syncObject
	}


	void unsetAllByRef(CEntity owning_instance) throws SdaiException {
//		synchronized (syncObject) {
		if (owning_instance == null) {
			owning_instance = getOwningInstance();
		}
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		CEntity to_ent;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
							if (!to_ent.owning_model.optimized) {
								to_ent.inverseRemove(owning_instance);
							}
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAllByRef(owning_instance);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
								if (!to_ent.owning_model.optimized) {
									to_ent.inverseRemove(owning_instance);
								}
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAllByRef(owning_instance);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
								if (!to_ent.owning_model.optimized) {
									to_ent.inverseRemove(owning_instance);
								}
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAllByRef(owning_instance);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						to_ent = (CEntity)myDataA[0];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
							if (!to_ent.owning_model.optimized) {
								to_ent.inverseRemove(owning_instance);
							}
						}
					} else if (myDataA[0] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[0]).disconnect();
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).unsetAllByRef(owning_instance);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							to_ent = (CEntity)element.object;
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
								if (!to_ent.owning_model.optimized) {
									to_ent.inverseRemove(owning_instance);
								}
							}
						} else if (element.object instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)element.object).disconnect();
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).unsetAllByRef(owning_instance);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						to_ent = (CEntity)myData;
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
							if (!to_ent.owning_model.optimized) {
								to_ent.inverseRemove(owning_instance);
							}
						}
					} else if (myData instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myData).disconnect();
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).unsetAllByRef(owning_instance);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof CEntity) {
							to_ent = (CEntity)myDataA[i];
							if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
								if (!to_ent.owning_model.optimized) {
									to_ent.inverseRemove(owning_instance);
								}
							}
						} else if (myDataA[i] instanceof SdaiModel.Connector) {
							((SdaiModel.Connector)myDataA[i]).disconnect();
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).unsetAllByRef(owning_instance);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] instanceof CEntity) {
						to_ent = (CEntity)myDataA[index];
						if (to_ent.owning_model != null && !to_ent.owning_model.closingAll) {
							if (!to_ent.owning_model.optimized) {
								to_ent.inverseRemove(owning_instance);
							}
						}
					} else if (myDataA[index] instanceof SdaiModel.Connector) {
						((SdaiModel.Connector)myDataA[index]).disconnect();
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).unsetAllByRef(owning_instance);
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
//		} // syncObject
	}


	boolean scanAggregate(CEntity ref_owner, CEntity old, CEntity_definition new_def) throws SdaiException {
		int sel_number = 0;
		if (myType == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		CEntity base_type = (CEntity)((EAggregation_type)myType).getElement_type(null);
		if (myLength == 0) {
			return true;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData == old) {
						if (!base_type.analyse_entity(ref_owner, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					} else if (myData instanceof CAggregate) {
						if (!((CAggregate)myData).scanAggregate(ref_owner, old, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] == old) {
							if (!base_type.analyse_entity(ref_owner, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							if (!((CAggregate)myDataA[i]).scanAggregate(ref_owner, old, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == old) {
							if (!base_type.analyse_entity(ref_owner, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						} else if (element.object instanceof CAggregate) {
							if (!((CAggregate)element.object).scanAggregate(ref_owner, old, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] == old) {
						if (!base_type.analyse_entity(ref_owner, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					} else if (myDataA[0] instanceof CAggregate) {
						if (!((CAggregate)myDataA[0]).scanAggregate(ref_owner, old, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == old) {
							if (!base_type.analyse_entity(ref_owner, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						} else if (element.object instanceof CAggregate) {
							if (!((CAggregate)element.object).scanAggregate(ref_owner, old, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData == old) {
						if (!base_type.analyse_entity(ref_owner, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					} else if (myData instanceof CAggregate) {
						if (!((CAggregate)myData).scanAggregate(ref_owner, old, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] == old) {
							if (!base_type.analyse_entity(ref_owner, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							if (!((CAggregate)myDataA[i]).scanAggregate(ref_owner, old, new_def)) {
								if (has_con) {
									myLength = -myLength;
								}
								return false;
							}
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] == old) {
						if (!base_type.analyse_entity(ref_owner, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					} else if (myDataA[index] instanceof CAggregate) {
						if (!((CAggregate)myDataA[index]).scanAggregate(ref_owner, old, new_def)) {
							if (has_con) {
								myLength = -myLength;
							}
							return false;
						}
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
		return true;
	}


	boolean is_referencing(CEntity instance) throws SdaiException {
		int sel_number = 0;
		if (myType != null && myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return false;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		boolean res;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData == instance) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					} else if (myData instanceof CAggregate) {
						res = ((CAggregate)myData).is_referencing(instance);
						if (res) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] == instance) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						} else if (myDataA[i] instanceof CAggregate) {
							res = ((CAggregate)myDataA[i]).is_referencing(instance);
							if (res) {
								if (has_con) {
									myLength = -myLength;
								}
								return true;
							}
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == instance) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						} else if (element.object instanceof CAggregate) {
							res = ((CAggregate)element.object).is_referencing(instance);
							if (res) {
								if (has_con) {
									myLength = -myLength;
								}
								return true;
							}
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] == instance) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					} else if (myDataA[0] instanceof CAggregate) {
						res = ((CAggregate)myDataA[0]).is_referencing(instance);
						if (res) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == instance) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						} else if (element.object instanceof CAggregate) {
							res = ((CAggregate)element.object).is_referencing(instance);
							if (res) {
								if (has_con) {
									myLength = -myLength;
								}
								return true;
							}
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData == instance) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					} else if (myData instanceof CAggregate) {
						res = ((CAggregate)myData).is_referencing(instance);
						if (res) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] == instance) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						} else if (myDataA[i] instanceof CAggregate) {
							res = ((CAggregate)myDataA[i]).is_referencing(instance);
							if (res) {
								if (has_con) {
									myLength = -myLength;
								}
								return true;
							}
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] == instance) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					} else if (myDataA[index] instanceof CAggregate) {
						res = ((CAggregate)myDataA[index]).is_referencing(instance);
						if (res) {
							if (has_con) {
								myLength = -myLength;
							}
							return true;
						}
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
		return false;
	}


/**
	All appearances (even in the nested aggregates) of the instance specified 
	by the first parameter within this aggregate are replaced by the value 
	given as a second parameter. Particularly, this value may be <code>null</code>. 
	In this case, if the aggregation type is different than ARRAY, then the 
	aggregate may become smaller because unset values are not kept in it.
*/
	void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException {
		if (myType == null || myType.shift == -7 || myType.shift == -8) {
			String base = SdaiSession.line_separator + AdditionalMessages.RD_ERRF;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		if (newer != null) {
			boolean changed = changeReferencesInstance(old, newer);
			if (changed && (newer instanceof SdaiModel.Connector)) {
				myLength = -myLength;
			}
		} else {
			changeReferencesUnset(old);
		}
		if (has_con) {
			myLength = -myLength;
		}
	}

	boolean changeReferencesInstance(InverseEntity old, InverseEntity newer) throws SdaiException {
		boolean changed = false;
		int sel;
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel = 2;
		} else {
			sel = 1;
		}
		int i;
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel <= 1) {
				if (myLength == 1) {
					if (myType.getAggMemberImplClass() != null) {
						((CAggregate)myData).changeReferences(old, newer);
					} else {
						if (myData == old) {
							myData = newer;
							changed = true;
						}
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myType.getAggMemberImplClass() != null) {
							((CAggregate)myDataA[i]).changeReferences(old, newer);
						} else {
							if (myDataA[i] == old) {
								myDataA[i] = newer;
								changed = true;
							}
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					if (myType.getAggMemberImplClass() != null) {
						while (element != null) {
							((CAggregate)element.object).changeReferences(old, newer);
							element = element.next;
						}
					} else {
						while (element != null) {
							if (element.object == old) {
								element.object = newer;
								changed = true;
							}
							element = element.next;
						}
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (!(myDataA[0] instanceof CAggregate)) {
						if (myDataA[0] == old) {
							myDataA[0] = newer;
							changed = true;
						}
					} else {
						((CAggregate)myDataA[0]).changeReferences(old, newer);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (!(element.object instanceof CAggregate)) {
							if (element.object == old) {
								element.object = newer;
								changed = true;
							}
						} else {
							((CAggregate)element.object).changeReferences(old, newer);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			boolean move_case = false;
			CEntity old_inst = null;
			CEntity newer_inst = null;
			if (old instanceof CEntity && newer instanceof CEntity) {
				newer_inst = (CEntity)newer;
				if (newer_inst.instance_identifier<0) {
					move_case = true;
				}
			}
			if (move_case) {
				old_inst = (CEntity)old;
			}
			if (move_case && old_inst.instance_identifier>0) {
				return changed;
			}
			boolean set_tp = false;
			if (move_case && myType.express_type == DataType.SET) {
				set_tp = true;
			}
			boolean found;
			int ind;
			if (sel <= 1) {
				if (myLength == 1) {
					if (myType.getAggMemberImplClass() != null) {
						((CAggregate)myData).changeReferences(old, newer);
					} else {
						if (myData == old) {
							myData = newer;
							changed = true;
						}
					}
				} else {
					myDataA = (Object [])myData;
					if (myType.getAggMemberImplClass() != null) {
						for (i = 0; i < myLength; i++) {
							if (myDataA[i] != null) {
								((CAggregate)myDataA[i]).changeReferences(old, newer);
								if (move_case && old_inst.instance_identifier > 0) {
									break;
								}
							}
						}
					} else {
						if (set_tp) {
							found = false;
							for (i = 0; i < myLength; i++) {
								if (myDataA[i] == newer) {
									found = true;
									break;
								}
							}
							if (found) {
								ind = -1;
								for (i = 0; i < myLength; i++) {
									if (myDataA[i] == old) {
										ind = i;
										break;
									}
								}
								if (ind >= 0) {
									for (i = ind + 1; i < myLength; i++) {
										myDataA[i - 1] = myDataA[i];
									}
									myLength--;
									if (myLength == 1) {
										myData = myDataA[0];
									}
									old_inst.instance_identifier = -old_inst.instance_identifier;
								}
							} else {
								for (i = 0; i < myLength; i++) {
									if (myDataA[i] == old) {
										myDataA[i] = newer;
										changed = true;
									}
								}
							}
						} else {
							for (i = 0; i < myLength; i++) {
								if (myDataA[i] == old) {
									myDataA[i] = newer;
									changed = true;
								}
							}
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				int true_ind;
				if (set_tp) {
					found = false;
					for (i = 0; i < myLength; i++) {
						true_ind = i * 2;
						if (!(myDataA[true_ind] instanceof CAggregate)) {
							if (myDataA[true_ind] == newer) {
								found = true;
							}
						} else {
							((CAggregate)myDataA[true_ind]).changeReferences(old, newer);
							if (old_inst.instance_identifier > 0) {
								break;
							}
						}
					}
					if (old_inst.instance_identifier > 0) {
						return changed;
					}
					if (found) {
						ind = -1;
						for (i = 0; i < myLength; i++) {
							true_ind = i * 2;
							if (myDataA[true_ind] == old) {
								ind = i;
								break;
							}
						}
						if (ind >= 0) {
							for (i = ind + 1; i < myLength; i++) {
								true_ind = i * 2;
								myDataA[true_ind - 2] = myDataA[true_ind];
								myDataA[true_ind - 1] = myDataA[true_ind + 1];
							}
							myLength--;
							old_inst.instance_identifier = -old_inst.instance_identifier;
						}
					} else {
						for (i = 0; i < myLength; i++) {
							true_ind = i * 2;
							if (myDataA[true_ind] == old) {
								myDataA[true_ind] = newer;
								changed = true;
							}
						}
					}
				} else {
					for (i = 0; i < myLength; i++) {
						true_ind = i * 2;
						if (!(myDataA[true_ind] instanceof CAggregate)) {
							if (myDataA[true_ind] == old) {
								myDataA[true_ind] = newer;
								changed = true;
							}
						} else {
							((CAggregate)myDataA[true_ind]).changeReferences(old, newer);
						}
					}
				}
			}
		}
		return changed;
	}


	void changeReferencesUnset(InverseEntity old) throws SdaiException {
		int sel;
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel = 2;
		} else {
			sel = 1;
		}
		int count = 0;
		int i;
		Object [] myDataA = null;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			ListElement last_element = null;
			ListElement head = null;
			if (sel <= 1) {
				if (myLength == 1) {
					if (!(myData instanceof CAggregate)) {
						if (myData == old) {
							myData = null;
							myLength = 0;
						}
					} else {
						((CAggregate)myData).changeReferences(old, null);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					boolean first_survive = true;
					if (!(myDataA[0] instanceof CAggregate)) {
						if (myDataA[0] == old) {
							myDataA[0] = null;
							myLength--;
							first_survive = false;
						}
					} else {
						((CAggregate)myDataA[0]).changeReferences(old, null);
					}
					if (!(myDataA[1] instanceof CAggregate)) {
						if (myDataA[1] == old) {
							myDataA[1] = null;
							myLength--;
						}
					} else {
						((CAggregate)myDataA[1]).changeReferences(old, null);
					}
					if (myLength == 0) {
						myData = null;
					} else if (myLength == 1) {
						if (first_survive) {
							myData = myDataA[0];
						} else {
							myData = myDataA[1];
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (!(element.object instanceof CAggregate)) {
							if (element.object == old) {
								element.object = null;
							}
						} else {
							((CAggregate)element.object).changeReferences(old, null);
						}
						if (element.object != null) {
							count++;
							if (last_element == null) {
								head = element;
							} else {
								last_element.next = element;
							}
							last_element = element;
						}
						element = element.next;
					}
					if (last_element != null) {
						last_element.next = null;
					}
					if (last_element == null) {
						myData = null;
					} else if (count == 1) {
						myData = head.object;
					} else if (count == 2) {
						if (myLength <= SHORT_AGGR) {
							myDataA = new Object[2];
						}
						myDataA[0] = head.object;
						myDataA[1] = head.next.object;
						myData = myDataA;
					} else if (count <= SHORT_AGGR) {
						myData = head;
					} else {
						myDataA[0] = head;
						myDataA[1] = last_element;
					}
					myLength = count;
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (!(myDataA[0] instanceof CAggregate)) {
						if (myDataA[0] == old) {
							myData = null;
							myLength = 0;
						}
					} else {
						((CAggregate)myDataA[0]).changeReferences(old, null);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (!(element.object instanceof CAggregate)) {
							if (element.object == old) {
								element.object = null;
							}
						} else {
							((CAggregate)element.object).changeReferences(old, null);
						}
						if (element.object != null) {
							count++;
							if (last_element == null) {
								head = element;
							} else {
								last_element.next = element;
							}
							last_element = element.next;
						}
						element = element.next.next;
					}
					if (last_element != null) {
						last_element.next = null;
					}
					if (last_element == null) {
						myData = null;
					} else if (count == 1) {
						if (myLength <= SHORT_AGGR_SELECT) {
							myDataA = new Object[2];
						}
						myDataA[0] = head.object;
						myDataA[1] = head.next.object;
						myData = myDataA;
					} else if (count <= SHORT_AGGR_SELECT) {
						myData = head;
					} else {
						myDataA[0] = head;
						myDataA[1] = last_element;
					}
					myLength = count;
				}
			}
		} else {
			boolean unset = false;
			if (sel <= 1) {
				if (myLength == 1) {
					if (!(myData instanceof CAggregate)) {
						if (myData == old) {
							myData = null;
							if (myType.express_type != DataType.ARRAY) {
								myLength = 0;
							}
						}
					} else {
						((CAggregate)myData).changeReferences(old, null);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (!(myDataA[i] instanceof CAggregate)) {
							if (myDataA[i] == old) {
								myDataA[i] = null;
								unset = true;
							}
						} else {
							((CAggregate)myDataA[i]).changeReferences(old, null);
						}
					}
					if (myType.express_type == DataType.ARRAY || !unset) {
						return;
					}
					for (i = 0, count = 0; i < myLength; i++) {
						if (myDataA[i] != null) {
							myDataA[count] = myDataA[i];
							count++;
						}
					}
					myLength = count;
					if (myLength == 1) {
						myData = myDataA[0];
					} else if (myLength == 0) {
						myData = null;
					}
				}
			} else {
				int true_ind;
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					true_ind = 2 * i;
					if (!(myDataA[true_ind] instanceof CAggregate)) {
						if (myDataA[true_ind] == old) {
							myDataA[true_ind] = null;
							unset = true;
						}
					} else {
						((CAggregate)myDataA[true_ind]).changeReferences(old, null);
					}
				}
				if (myType.express_type == DataType.ARRAY || !unset) {
					return;
				}
				for (i = 0, count = 0; i < myLength; i++) {
					true_ind = 2 * i;
					if (myDataA[true_ind] != null) {
						int new_ind = 2 * count;
						myDataA[new_ind] = myDataA[true_ind];
						myDataA[new_ind + 1] = myDataA[true_ind + 1];
						count++;
					}
				}
				myLength = count;
				if (myLength == 0) {
					myData = null;
				}
			}
		}
	}


/**
	Returns entity instance obtained as a result of resolving a reference 
	represented by a connector. To this end, <code>resolveConnector</code> method in
	<code>CEntity</code> class is invoked. An element of the aggregate to be 
	resolved is specified by the parameters. If the aggregate is of LIST type, 
	then the first parameter representing an object of <code>ListElement</code> class 
	is used. Otherwise, the element is specified by <code>SdaiIterator</code> (first 
	parameter) if the current method is applied in methods using an iterator or by 
	index (second parameter) if the current method is applied in methods accessing 
	elements by index.
*/
	CEntity resolveConnector(ListElement element, boolean sel, SdaiModel.Connector obj, int index) throws SdaiException {
		CEntity app_inst = obj.resolveConnector(true, true, false);
		Object [] myDataA;
		if (sel) {
			if (myLength == 1) {
				myDataA = (Object [])myData;
				myDataA[0] = app_inst;
			} else {
				element.object = app_inst;
			}
		} else {
			if (myLength == 1) {
				myData = app_inst;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				myDataA[index] = app_inst;
			} else {
				element.object = app_inst;
			}
		}
		return app_inst;
	}

	CEntity resolveConnector(boolean sel, SdaiModel.Connector obj, int index) throws SdaiException {
		CEntity app_inst = obj.resolveConnector(true, true, false);
		Object [] myDataA;
		if (sel) {
			myDataA = (Object [])myData;
			myDataA[index*2] = app_inst;
		} else {
			if (myLength == 1) {
				myData = app_inst;
			} else {
				myDataA = (Object [])myData;
				myDataA[index] = app_inst;
			}
		}
		return app_inst;
	}


/**
	Adds the submitted instance (second parameter) to the resulting set (third 
	parameter) for each appearance of the referenced instance (first parameter) 
	in this aggregate or aggregates which are (possibly, nested) members of 
	this aggregate. This method is invoked in <code>makeUsedin2</code> method 
	in class <code>CEntity</code>. 
*/
	void usedin(CEntity referenced, CEntity referencing, AEntity result) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		int sel_number = 1;
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 2;
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 1) {
				if (myLength == 1) {
					if (myData == referenced) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(referencing, null);
						} else {
							result.setForNonList(referencing, result.myLength, null, null);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).usedin(referenced, referencing, result);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] == referenced) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(referencing, null);
							} else {
								result.setForNonList(referencing, result.myLength, null, null);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).usedin(referenced, referencing, result);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == referenced) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(referencing, null);
							} else {
								result.setForNonList(referencing, result.myLength, null, null);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).usedin(referenced, referencing, result);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] == referenced) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(referencing, null);
						} else {
							result.setForNonList(referencing, result.myLength, null, null);
						}
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).usedin(referenced, referencing, result);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object == referenced) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(referencing, null);
							} else {
								result.setForNonList(referencing, result.myLength, null, null);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).usedin(referenced, referencing, result);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 1) {
				if (myLength == 1) {
					if (myData == referenced) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(referencing, null);
						} else {
							result.setForNonList(referencing, result.myLength, null, null);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).usedin(referenced, referencing, result);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] == referenced) {
							if (result.myType == null || result.myType.express_type == DataType.LIST) {
								result.addAtTheEnd(referencing, null);
							} else {
								result.setForNonList(referencing, result.myLength, null, null);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).usedin(referenced, referencing, result);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] == referenced) {
						if (result.myType == null || result.myType.express_type == DataType.LIST) {
							result.addAtTheEnd(referencing, null);
						} else {
							result.setForNonList(referencing, result.myLength, null, null);
						}
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).usedin(referenced, referencing, result);
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
	}


/**
	Puts values described by the object of class <code>Value</code> (third 
	parameter) to the current aggregate. An element is included into the 
	aggregate using <code>addAtTheEnd</code> (if aggregation type is LIST) 
	or <code>setForNonList</code> (if aggregation type is ARRAY, SET or BAG). 
	The method is invoked in classes <code>EntityValue</code> and 
	<code>Value</code>. It is used when reading either an exchange structure 
	or a JSDAI binary file.
*/
	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
		int sel_number = 0;
		boolean typed = mixed;
		boolean write_select = false;
		boolean bug_found;
		myType = aggr_type;
		owner = inst;
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
		}
		AggregationType next_type;
		CDefined_type start_def = null;
		DataType element_type = (DataType)aggr_type.getElement_type(null);
		if (element_type.express_type >= DataType.LIST && element_type.express_type <= DataType.AGGREGATE) {
			next_type = (AggregationType)element_type;
		} else {
			next_type = null;
			if (element_type.express_type == DataType.DEFINED_TYPE) {
				start_def = (CDefined_type)element_type;
				while (element_type.express_type == DataType.DEFINED_TYPE) {
					element_type = (DataType)((CDefined_type)element_type).getDomain(null);
				}
				if (element_type.express_type >= DataType.SELECT && element_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
					if (((SelectType)element_type).is_mixed > 0) {
						write_select = true;
					}
					typed = true;
				} else if (element_type.express_type >= DataType.LIST && element_type.express_type <= DataType.AGGREGATE) {
					next_type = (AggregationType)element_type;
				}
			}
		}
		int count = 0;
		for (int i = 0; i < val.length; i++) {
			Object for_adding;
			bug_found = false;
			if (next_type == null) {
				Value val_for_element = val.nested_values[i];
				if (typed) {
if (SdaiSession.debug2) System.out.println(" !!  element_type = " + element_type.getClass().getName());
					for_adding = val_for_element.getAggregateMixedValue(start_def, inst);
					sel_number = val_for_element.sel_number;
if (SdaiSession.debug2) System.out.println("  IN AGGREGATE sel_number = " + sel_number);
					if (sel_number <= 0) {
						for_adding = null;
						bug_found = true;
					}
				} else {
					for_adding = val_for_element.getValue(inst, start_def, inverse);
				}
			} else {
if (SdaiSession.debug2) System.out.println("    In CAggregate: " + next_type.getAggregateClass().getName());
if (SdaiSession.debug2) System.out.println(" In CAggregate aggr_type inst ident = " + next_type.instance_identifier);
				for_adding = val.getInstanceAggregate(i, next_type, inst, mixed);
			}
//System.out.println(" CAggregate:  type: " + type + "   mixed: " + mixed +
//"   bug_found: " + bug_found + "  sel_number: " + sel_number + 
//"   typed: " + typed);
			if (isValueCorrect(for_adding, inst)) {
				switch (type) {
				case 0:  
					if (!write_select) {
						setForNonList(for_adding, count, null, inst);
					} else {
						setForNonList(for_adding, count,
									  getIntegerWithValue(bug_found ? 1 : sel_number), inst);
					}
					break;
				case 1:  
					if (for_adding == null) {
						break;
					}
					if (!write_select) {
						addAtTheEnd(for_adding, null);
					} else {
						addAtTheEnd(for_adding, getIntegerWithValue(bug_found ? 1 : sel_number));
					}
					break;
				}
				count++;
			} else {
				if (val.aux == 1001) {
					((CEntity)owner).modified_sleeping();
					fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
			}
		}
	}


/**
	Puts a submitted value into the aggregate at the specified index position. 
	The aggregation type is either ARRAY or SET or BAG. The select information, 
	if any, is represented by the third parameter. The last parameter gives 
	the owner of the aggregate. The method is invoked in <code>setValue</code> 
	method of this class and in classes <code>EntityValue</code> and 
	<code>Value</code>.
*/
	void setForNonList(Object value, int index, Integer sel_number, CEntity inst) throws SdaiException {
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		Object [] myDataA;
		int t_index;
		Object obj = null;
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
			int upper_index = upper_bound.getBound_value(null);
			if (myData == null) {
				initializeData(upper_index - lower_index + 1);
			}
			if (index > upper_index - lower_index) {
				if (inst != null) {
					StaticFields staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_EXAR, 
						inst.instance_identifier, staticFields.current_attribute);
				}
				return;
			}
			if (sel_number == null) {
				if (myLength == 1) {
					 myData = value;
				} else {
					myDataA = (Object [])myData;
					myDataA[index] = value;
				}
			} else {
				myDataA = (Object [])myData;
				t_index = index * 2;
				myDataA[t_index] = value;
				if (value == null) {
					myDataA[t_index + 1] = null;
				} else {
					myDataA[t_index + 1] = sel_number;
				}
			}
			return;
		} else {
			if ((sel_number == null && myLength == 1) || (sel_number != null && myLength == 0)) {
				int ln;
				if (myType.express_type == DataType.AGGREGATE) {
					ln = Integer.MAX_VALUE;
				} else {
					EVariable_size_aggregation_type size_aggr_type = (EVariable_size_aggregation_type)myType;
					if (size_aggr_type.testUpper_bound(null)) {
						EBound upper_bound = size_aggr_type.getUpper_bound(null);
						ln = upper_bound.getBound_value(null);
					} else {
						ln = Integer.MAX_VALUE;
					}
				}
				if (sel_number == null) {
					obj = myData;
					if (ln <= 1) {
						ln = 2;
					}
					initializeData(ln);
					myLength = 1;
				} else {
					initializeData(ln);
				}
			} else if (myLength > 0) {
				myDataA = (Object [])myData;
				if (sel_number == null) {
					t_index = index;
				} else {
					t_index = index * 2;
				}
				if (t_index >= myDataA.length) {
					 ensureCapacity(myLength);
				}
			}
		}
		if (sel_number == null) {
			if (myLength == 0) {
				 myData = value;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				myDataA[0] = obj;
				myDataA[1] = value;
			} else {
				myDataA = (Object [])myData;
				myDataA[index] = value;
			}
		} else {
			myDataA = (Object [])myData;
			t_index = index * 2;
			myDataA[t_index] = value;
			if (value == null) {
				myDataA[t_index + 1] = null;
			} else {
				myDataA[t_index + 1] = sel_number;
			}
		}
		myLength++;
		if (has_con) {
			myLength = -myLength;
		}
	}


/**
	Puts a submitted value into the aggregate which type is LIST. The value is 
	added at the end of the aggregate. The select information, if any, is 
	represented by the second parameter. The method is invoked in 
	<code>setValue</code> method of this class and in classes 
	<code>EntityValue</code> and <code>Value</code>.
*/
	void addAtTheEnd(Object value, Integer sel_number) throws SdaiException {
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (sel_number == null) {
			if (myLength == 0) {
				myData = value;
			} else if (myLength == 1) {
				myDataA = new Object[2];
				myDataA[0] = myData;
				myDataA[1] = value;
				myData = myDataA;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				element = new ListElement(myDataA[0]);
				element.next = new ListElement(myDataA[1]);
				element.next.next = new ListElement(value);
				myData = element;
			} else if (myLength <= SHORT_AGGR) {
				element = (ListElement)myData;
				while (element.next != null) {
					element = element.next;
				}
				element.next = new ListElement(value);
				if (myLength == SHORT_AGGR) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					myDataA[1] = element.next;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				((ListElement)myDataA[1]).next = new_element;
				myDataA[1] = new_element;
			}
		} else {
			ListElement add_element;
			if (myLength == 0) {
				myDataA = new Object[2];
				myDataA[0] = value;
				if (value == null) {
					myDataA[1] = null;
				} else {
					myDataA[1] = sel_number;
				}
				myData = myDataA;
			} else if (myLength == 1) {
				myDataA = (Object [])myData;
				element = new ListElement(myDataA[0]);
				element.next = new ListElement(myDataA[1]);
				element.next.next = new ListElement(value);
				if (value == null) {
					element.next.next.next = new ListElement(null);
				} else {
					element.next.next.next = new ListElement(sel_number);
				}
				myData = element;
			} else if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
				new_element = new ListElement(value);
				if (value == null) {
					add_element = new ListElement(null);
				} else {
					add_element = new ListElement(sel_number);
				}
				new_element.next = add_element;
				while (element.next != null) {
					element = element.next;
				}
				element.next = new_element;
				if (myLength == SHORT_AGGR_SELECT) {
					myDataA = new Object[2];
					myDataA[0] = myData;
					myDataA[1] = add_element;
					myData = myDataA;
				}
			} else {
				myDataA = (Object [])myData;
				new_element = new ListElement(value);
				if (value == null) {
					add_element = new ListElement(null);
				} else {
					add_element = new ListElement(sel_number);
				}
				new_element.next = add_element;
				((ListElement)myDataA[1]).next = new_element;
				myDataA[1] = add_element;
			}
		}
		myLength++;
		if (has_con) {
			myLength = -myLength;
		}
	}


/**
	Returns <code>true</code> if value can be included into the aggregate. 
	The method is invoked in <code>setValue</code> method of this class. 
*/
	private boolean isValueCorrect(Object value, CEntity inst) throws SdaiException {
		if (myType.express_type == DataType.ARRAY) {
			return true;
		}
		if (value == null) {
			if (inst != null) {
				StaticFields staticFields = StaticFields.get();
				EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
					inst.instance_identifier, staticFields.current_attribute);
			}
			return false;
		}
		return true;
	}


/**
	Returns <code>true</code> if elements uniqueness requirement for the current 
	aggregate is violated. 
	The method is invoked in <code>validateAggregatesUniqueness</code> method in 
	<code>CEntity</code> class. 
*/
	boolean checkUniquenessViolation(AggregationType aggr_type) throws SdaiException {
		if (aggr_type.express_type == DataType.BAG) {
			return false;
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		int j, k;
		int sel_number;
		if (aggr_type.select != null && aggr_type.select.is_mixed > 0) {
			sel_number = 2;
		} else {
			sel_number = 1;
		}
		if (myLength <= 1) {
			return false;
		}
		Object [] myDataA;
		if (aggr_type.express_type == DataType.LIST) {
			if (!((CList_type)aggr_type).getUnique_flag(null)) {
				return false;
			}
			ListElement element;
			int a_type;
			if (sel_number <= 1) {
				if (myLength == 2) {
					myDataA = (Object [])myData;
					return check_list_for_uniqueness(myDataA[1], null, 1, 0, 1);
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
						a_type = 2;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
						a_type = 3;
					}
					j = 0;
					element = element.next;
					while (element != null) {
						if (element.object != null && check_list_for_uniqueness(element.object, null, 1, j, a_type)) {
							return true;
						}
						j++;
						element = element.next;
					}
				}
			} else {
				if (myLength <= SHORT_AGGR_SELECT) {
					element = (ListElement)myData;
					a_type = 2;
				} else {
					myDataA = (Object [])myData;
					element = (ListElement)myDataA[0];
					a_type = 3;
				}
				j = 0;
				element = element.next.next;
				while (element != null) {
					if (element.object != null && 
							check_list_for_uniqueness(element.object, element.next.object, 2, j, a_type)) {
						return true;
					}
					j++;
					element = element.next.next;
				}
			}
		} else {
			if ((aggr_type.express_type == DataType.ARRAY) && 
				(!((CArray_type)aggr_type).getUnique_flag(null))) {
				return false;
			}
			int val_type;
			long long_number = 0;
			double doub_number = 0.;
			String str = null;
			myDataA = (Object [])myData;
			for (j = 1; j < myLength; j++) {
				int index = j * sel_number;
				if (myDataA[index] == null) {
					continue;
				}
				if (myDataA[index] instanceof CEntity) {
					val_type = 1;
					long_number = ((CEntity)myDataA[index]).instance_identifier;
				} else if (myDataA[index] instanceof Integer) {
					val_type = 2;
					long_number = ((Integer)myDataA[index]).intValue();
				} else if (myDataA[index] instanceof Double) {
					val_type = 3;
					doub_number = ((Double)myDataA[index]).doubleValue();
				} else if (myDataA[index] instanceof String) {
					val_type = 4;
					str = (String)myDataA[index];
				} else {
					val_type = 0;
				}
				for (k = 0; k < j; k++) {
					int current_index = k * sel_number;
					if (sel_number <= 1) {
						if (compare_values(myDataA[index], myDataA[current_index], val_type, 
								long_number, doub_number, str)) {
							return true;
						}
					} else {
						if (compare_values(myDataA[index], myDataA[current_index], val_type, 
								long_number, doub_number, str) && 
								myDataA[current_index + 1] == myDataA[index + 1]) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}


/**
	Returns <code>true</code> if the submitted value coincides with some 
	element of this aggregate of type LIST in the interval from the beginning to 
	the element with index specified by the last parameter. The third parameter 
	equals 2 if the type of the value is select type, and 1 otherwise. In the 
	former case the select path number is given by the second parameter. 
	The method is invoked in <code>checkUniquenessViolation</code> method of 
	this class. 
*/
	private boolean check_list_for_uniqueness(Object value, Object selection, 
			int sel_number, int last_index, int a_type) {
		int val_type;
		long long_number = 0;
		double doub_number = 0.;
		String str = null;
		if (value instanceof CEntity) {
			val_type = 1;
			long_number = ((CEntity)value).instance_identifier;
		} else if (value instanceof Integer) {
			val_type = 2;
			long_number = ((Integer)value).intValue();
		} else if (value instanceof Double) {
			val_type = 3;
			doub_number = ((Double)value).doubleValue();
		} else if (value instanceof String) {
			val_type = 4;
			str = (String)value;
		} else {
			val_type = 0;
		}
		Object [] myDataA;
		if (a_type == 1) {
			myDataA = (Object [])myData;
			return compare_values(value, myDataA[0], val_type, long_number, doub_number, str);
		}
		int i = 0;
		ListElement element;
		if (a_type == 2) {
			element = (ListElement)myData;
		} else {
			myDataA = (Object [])myData;
			element = (ListElement)myDataA[0];
		}
		if (sel_number <= 1) {
			while (i <= last_index) {
				if (compare_values(value, element.object, val_type, long_number, doub_number, str)) {
					return true;
				}
				i++;
				element = element.next;
			}
		} else {
			while (i <= last_index) {
				if (compare_values(value, element.object, val_type, long_number, doub_number, str)) {
					element = element.next;
					if (element.object == selection) {
						return true;
					}
				} else {
					element = element.next;
				}
				i++;
				element = element.next;
			}
		}
		return false;
	}


/**
	Returns <code>true</code> if the two submitted values coincide. The type of 
	the value is specified by the third parameter. The rest of the parameters 
	describe the values to be compared.
	The method is invoked in <code>checkUniquenessViolation</code> and 
	<code>check_list_for_uniqueness</code> methods of this class. 
*/
	private boolean compare_values(Object value1, Object value2, int val_type, 
			long long_number, double doub_number, String str) {
		switch (val_type) {
			case 0: 	if (value1 == value2) {
							return true;
						}
						break;
			case 1:	if (value2 instanceof CEntity) {
							if (long_number == ((CEntity)value2).instance_identifier) {
								return true;
							}
						}
						break;
			case 2:	if (value2 instanceof Integer) {
							if (long_number == ((Integer)value2).intValue()) {
								return true;
							}
						}
						break;
			case 3:	if (value2 instanceof Double) {
							if (doub_number == ((Double)value2).doubleValue()) {
								return true;
							}
						}
						break;
			case 4:	if (value2 instanceof String) {
							if (str.equals((String)value2)) {
								return true;
							}
						}
						break;
		}
		return false;
	}


/**
	Returns <code>false</code> if either this array has values at all positions 
	or optional elements in the array are allowed, and <code>true</code> if 
	both these conditions are violated. 
	The method is invoked in <code>validateArrayNotOptional</code> method in 
	<code>CEntity</code> class. 
*/
	boolean checkOptionalMissing(AggregationType aggr_type) throws SdaiException {

		EBound lindex = ((EArray_type)aggr_type).getLower_index(null);
		EBound uindex = ((EArray_type)aggr_type).getUpper_index(null);
		if (lindex instanceof EInteger_bound && uindex instanceof EInteger_bound) {
			int differ = uindex.getBound_value(null) - lindex.getBound_value(null);
			if (myLength > differ + 1) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		if (((CArray_type)aggr_type).getOptional_flag(null)) {
			return false;
		}
		int sel_number;
		if (aggr_type.select != null && aggr_type.select.is_mixed > 0) {
			sel_number = 2;
		} else {
			sel_number = 1;
		}
		if (myLength <= 0) {
			return false;
		}
		int i;
		Object [] myDataA;
		if (sel_number <= 1) {
			if (myLength == 1) {
				if (myData == null) {
					return true;
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					if (myDataA[i] == null) {
						return true;
					}
				}
			}
		} else {
			myDataA = (Object [])myData;
			for (i = 0; i < myLength; i++) {
				if (myDataA[i * 2] == null) {
					return true;
				}
			}
		}
		return false;
	}


/**
 * Returns a <code>String</code> representing this aggregate instance.
 * It, in fact, gives values of all members of this aggregate
 * including nested aggregates.
 * This representation adheres all mapping rules defined
 * in "ISO 10303-21: Clear text encoding of the exchange structure".

 * <P><B>Example:</B>
 * <P><TT><pre>    Aggregate agg = ...;
 *    System.out.println("aggregate: " + agg);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    aggregate: (#1008,PARAMETER_VALUE(1.5),(#1523,#36,#9912));</pre>

 * @return the <code>String</code> representing this aggregate instance.
 * @see "ISO 10303-21: Implementation methods: Clear text encoding 
 * of the exchange structure."
 */
	public String toString() {
//		synchronized (syncObject) {
		try {
			if (myLength < 0) {
				resolveAllConnectors();
			}
			return getAsString();
		} catch (SdaiException e) {
//e.printStackTrace();
			return super.toString();
		}
//		} // syncObject
	}


/**
	This method is doing a job of the public method <code>toString</code>.
*/
	String getAsString() throws SdaiException {
		int i, j;
		int str_index = -1;
		if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		StaticFields staticFields = StaticFields.get();
		if (myType == null || (myType.shift == 0 && this instanceof AExplicit_attribute)) {
			return toStringNonPersistent(staticFields, str_index);
		}
		Object [] myDataA;
		if (myType.shift <= SdaiSession.INST_AGGR) {
			if (staticFields.instance_as_string == null) {
				staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
			}
			staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
			if (myType.shift == SdaiSession.INST_AGGR) {
				str_index = toStringInstances(staticFields, str_index);
			} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
				str_index = toStringInstancesExact(staticFields, str_index);
			} else if (myType.shift == SdaiSession.ALL_INST_AGGR) {
				str_index = toStringInstancesAll(staticFields, str_index);
			} else if (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
				str_index = toStringListOfModelsAll(staticFields, str_index);
			} else if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
				str_index = toStringListOfModels(staticFields, str_index);
			} else if (myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
				str_index = toStringListOfModelsExact(staticFields, str_index);
			} else {
				boolean first = true;
				for (i = 0; i < myLength; i++) {
					myDataA = (Object [])myData;
					CEntity inst = (CEntity)myDataA[i];
					if (str_index + 2 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
					}
					if (!first) {
						staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
					}
					first = false;
					staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
					str_index = long_to_byte_array(staticFields, inst.instance_identifier, str_index);
				}
			}
			if (str_index + 1 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
			}
			staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
			return new String(staticFields.instance_as_string, 0, str_index + 1);
		}
		CEntity owning_instance = getOwningInstance();

		if (owning_instance == null) {
			if (this instanceof Aa_integer) {
				return toStringExpressAggregate(staticFields, str_index, SdaiSession.EXPRESSIONS_INTEGER2_AGGR);
			} else {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
		}

		if (myType.shift >= SdaiSession.EXPRESSIONS_INST_AGGR && myType.shift <= SdaiSession.EXPRESSIONS_DOUBLE3_AGGR) {
			return toStringExpressAggregate(staticFields, str_index, myType.shift);
		}

		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			if (getOwner() == null) {
				throw new SdaiException(SdaiException.AI_NEXS);
			}
			SdaiModel owning_model = owning_instance.owning_model;
			if (owning_model == null) {
				throw new SdaiException(SdaiException.EI_NEXS);
			}
			if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
				throw new SdaiException(SdaiException.MX_NDEF, owning_model);
			}
		}

		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		if (staticFields.entity_values == null) {
			staticFields.entity_values = new ComplexEntityValue();
		}
		CEntity_definition def = (CEntity_definition)owning_instance.getInstanceType();
		owning_instance.owning_model.prepareAll(staticFields.entity_values, def);
		owning_instance.getAll(staticFields.entity_values);
		EntityValue pval;
		Value val;
		SdaiSession ss = owning_instance.owning_model.repository.session;

//System.out.println("  CAggregate  def.noOfPartialEntityTypes = " + def.noOfPartialEntityTypes); 
		for (i = 0; i < def.noOfPartialEntityTypes; i++) {
			if (def.complex == 2) {
				pval = staticFields.entity_values.entityValues[i];
			} else {
				int map_index = def.externalMappingIndexing[i];
				pval = staticFields.entity_values.entityValues[map_index];
			}
//System.out.println("  CAggregate  pval.count = " + pval.count);
			for (j = 0; j < pval.count; j++) {
				val = pval.values[j];
				str_index = get_value(staticFields, true, false, val, str_index, ss);
//System.out.println("  CAggregate  str_index = " + str_index + "  val.tag = " + val.tag);
				if (str_index > -1) {
					break;
				}
			}
		}
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}


/**
	Writes a long number to a byte array. The last index value in this 
	array used for writing a digit is returned.
	This method is invoked in this class and in <code>SdaiModel</code>. 
*/
	int long_to_byte_array(StaticFields staticFields, long lo, int index) throws SdaiException {
		boolean neg;
		long number, next_number;
		if (lo < 0) {
			neg = true;
			number = -lo;
		} else if (lo > 0) {
			neg = false;
			number = lo;
		} else {
			if (index + 1 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.instance_as_string[++index] = PhFileWriter.DIGITS[0];
			return index;
		}
		int initial_index = index;
		while (number != 0) {
			next_number = number / 10;
			if (index + 1 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.instance_as_string[++index] = PhFileWriter.DIGITS[(int)(number - next_number * 10)];
			number = next_number;
		}
		if (neg) {
			if (index + 1 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 2);
			}
			staticFields.instance_as_string[++index] = PhFileReader.MINUS;
		}
		for (int i = initial_index + 1;
				i <= initial_index + (index - initial_index) / 2; i++) {
			byte sym = staticFields.instance_as_string[i];
			staticFields.instance_as_string[i] = staticFields.instance_as_string[index - i + initial_index + 1];
			staticFields.instance_as_string[index - i + initial_index + 1] = sym;
		}
		return index;
	}


/**
	Writes the value being an element of the aggregate to a byte array. The 
	last index value in this array used for writing the data is returned.
	This method is invoked in <code>getAsString</code>. 
*/
	private int get_value(StaticFields staticFields, boolean first, boolean agg_found, Value val, int index, SdaiSession ss)
			throws SdaiException {
		Value value_next;
		boolean first_next;
		if (!agg_found && val.tag != PhFileReader.EMBEDDED_LIST) {
			return index;
		}
		switch (val.tag) {
			case PhFileReader.MISSING:
				if (index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++index] = PhFileReader.DOLLAR_SIGN;
				break;
			case PhFileReader.REDEFINE:
				if (index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++index] = PhFileReader.ASTERISK;
				break;
			case PhFileReader.INTEGER:
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == Integer.MIN_VALUE) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				index = long_to_byte_array(staticFields, val.integer, index);
				break;
			case PhFileReader.REAL:
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				String str = Double.toString(val.real);
				if (index + str.length() >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + str.length() + 1);
				}
				for (int i = 0; i < str.length(); i++) {
					staticFields.instance_as_string[++index] = (byte)str.charAt(i);
				}
				break;
			case PhFileReader.BOOLEAN:
				if (index + 4 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 5);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == 0) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (val.integer == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				}
				break;
			case PhFileReader.LOGICAL:
				if (index + 4 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 5);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.integer == 0) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (val.integer == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_UNKNOWN[i];
					}
				}
				break;
			case PhFileReader.ENUM:
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.fromStringBasicLatin(val.string);
				if (index + staticFields.string_length + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 3);
				}
				staticFields.instance_as_string[++index] = PhFileReader.DOT;
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.instance_as_string[++index] = PhFileReader.DOT;
				break;
			case PhFileReader.STRING:
				if (index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++index] = PhFileReader.APOSTROPHE;
				if (SdaiSession.isToStringUnicode()) {
					ss.printer.fromStringBasicLatin(val.string);
				} else {
					ss.printer.fromString(val.string);
				}
				int ln = 2 * staticFields.string_length;
				if (index + ln + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + ln + 2);
				}
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++index] = staticFields.string[i];
					if (staticFields.string[i] == PhFileReader.APOSTROPHE) {
						staticFields.instance_as_string[++index] = PhFileReader.APOSTROPHE;
					}
				}
				staticFields.instance_as_string[++index] = PhFileReader.APOSTROPHE;
				break;
			case PhFileReader.BINARY:
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.convertBinary((Binary)val.reference);
				if (index + staticFields.string_length + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 3);
				}
				staticFields.instance_as_string[++index] = PhFileReader.QUOTATION_MARK;
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.instance_as_string[++index] = PhFileReader.QUOTATION_MARK;
				break;
			case PhFileReader.TYPED_PARAMETER:
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				ss.printer.fromStringBasicLatin(val.string);
				if (index + staticFields.string_length + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + staticFields.string_length + 2);
				}
				for (int i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++index] = staticFields.string[i];
				}
				staticFields.instance_as_string[++index] = PhFileReader.LEFT_PARENTHESIS;
				value_next = val.nested_values[0];
				first_next = true;
				index = get_value(staticFields, first_next, agg_found, value_next, index, ss);
				if (index + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.instance_as_string[++index] = PhFileReader.RIGHT_PARENTHESIS;
				break;
			case PhFileReader.ENTITY_REFERENCE:
				CEntity base_inst = null;
				if (!(val.reference instanceof SdaiModel.Connector)) {
					base_inst = (CEntity)val.reference;
				}
				if (index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (val.reference instanceof SdaiModel.Connector) {
					staticFields.instance_as_string[++index] = PhFileReader.QUESTION_MARK;
				} else {
					staticFields.instance_as_string[++index] = PhFileReader.SPECIAL;
					index = long_to_byte_array(staticFields, base_inst.instance_identifier, index);
				}
				break;
			case PhFileReader.EMBEDDED_LIST:
				if (!agg_found && val.reference != this) {
					for (int i = 0; i < val.length; i++) {
						value_next = val.nested_values[i];
						index = get_value(staticFields, true, false, value_next, index, ss);
						if (index > -1) {
							break;
						}
					}
					break;
				}
				if (index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++index] = PhFileReader.LEFT_PARENTHESIS;
				first_next = true;
				for (int i = 0; i < val.length; i++) {
					value_next = val.nested_values[i];
					index = get_value(staticFields, first_next, true, value_next, index, ss);
					first_next = false;
				}
				if (index + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 2);
				}
				staticFields.instance_as_string[++index] = PhFileReader.RIGHT_PARENTHESIS;
				break;
		}
		return index;
	}


/**
	Writes to a byte array the elements of the set which consists of instances 
	of a specified entity data type and all its subtypes in an 
	<code>SdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations with parameter specifying an entity) 
	in class <code>SdaiModel</code>. The set is represented by an aggregate in 
	which the array <code>myData</code> contains two elements: the first is an 
	<code>SdaiModel</code> containing instances, whereas the second is the 
	underlying schema of this model. The method returns the last index value 
	in the byte array used for writing the names of the instances in the above set.
	This method is invoked in <code>getAsString</code> in this class.
*/
	private int toStringInstances(StaticFields staticFields, int str_index) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		int index = myLength;
		boolean first_time = true;
		boolean first = true;
		int i = -1;
		int subtypes[] = null;
		while (index >= 0) {
			if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
					model.repository != SdaiSession.systemRepository) {
				index = model.find_entityRO(model.dictionary.schemaData.entities[index]);
			}
			if (index >= 0) {
				for (int j = 0; j < model.lengths[index]; j++) {
					CEntity inst = model.instances_sim[index][j];
					if (str_index + 2 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
					}
					if (!first) {
						staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
					}
					first = false;
					staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
					str_index = long_to_byte_array(staticFields, inst.instance_identifier, str_index);
				}
			}
			i++;
			if (first_time) {
				if (myDataA[1] == null) {
					return str_index;
				}
				subtypes = ((CSchema_definition)myDataA[1]).getSubtypes(myLength);
				first_time = false;
			}
			if (i < subtypes.length) {
				index = subtypes[i];
			} else {
				index = -1;
			}
		}
		return str_index;
	}


/**
	Writes to a byte array the elements of the set which consists of instances 
	of a specified entity data type (but not its subtypes) in an 
	<code>SdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>SdaiModel</code>. The set 
	is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first is an <code>SdaiModel</code> containing 
	instances, whereas the second is the underlying schema of this model. The 
	method returns the last index value in the byte array used for writing the 
	names of the instances in the above set.
	This method is invoked in <code>getAsString</code> in this class.
*/
	private int toStringInstancesExact(StaticFields staticFields, int str_index) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		int index;
		if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
				model.repository != SdaiSession.systemRepository) {
			index = model.find_entityRO(model.dictionary.schemaData.entities[myLength]);
		} else {
			index = myLength;
		}
		boolean first = true;
		if (index >= 0) {
			for (int j = 0; j < model.lengths[index]; j++) {
				CEntity inst = model.instances_sim[index][j];
				if (str_index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				first = false;
				staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
				str_index = long_to_byte_array(staticFields, inst.instance_identifier, str_index);
			}
		}
		return str_index;
	}


/**
	Writes to a byte array the elements of the set which consists of all 
	instances of an <code>SdaiModel</code>. This set is returned by 
	<code>getInstances</code> method in class <code>SdaiModel</code>. The set 
	is represented by an aggregate in which the first element of the 
	array <code>myData</code> is an <code>SdaiModel</code> whose instances are 
	considered. The method returns the last index value in the byte array used 
	for writing the names of the instances in the above set.
	This method is invoked in <code>getAsString</code> in this class.
*/
	private int toStringInstancesAll(StaticFields staticFields, int str_index) throws SdaiException {
		Object [] myDataA = (Object [])myData;
		SdaiModel model = (SdaiModel)myDataA[0];
		boolean first = true;
		for (int i = 0; i < model.lengths.length; i++) {
			if (model.lengths[i] <= 0) continue;
			for (int j = 0; j < model.lengths[i]; j++) {
				CEntity inst = model.instances_sim[i][j];
				if (str_index + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
				}
				if (!first) {
					staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				first = false;
				staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
				str_index = long_to_byte_array(staticFields, inst.instance_identifier, str_index);
			}
		}
		return str_index;
	}


/**
	Returns the string representation of a non-persistent list. 
	This method is invoked in <code>getAsString</code> in this class.
*/
	private String toStringNonPersistent(StaticFields staticFields, int str_index) throws SdaiException {
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		boolean first = true;
		if (myLength < 1) {
		} else if (myLength == 1) {
			str_index = write_list_element(staticFields, myData, str_index, first);
		} else if (myLength == 2) {
			Object [] myDataA = (Object [])myData;
			for (int i = 0; i < 2; i++) {
				str_index = write_list_element(staticFields, myDataA[i], str_index, first);
				first = false;
			}
		} else {
			if (staticFields.agg_it1 == null) {
				staticFields.agg_it1 = createIterator();
			} else {
				attachIterator(staticFields.agg_it1);
			}
			while (staticFields.agg_it1.next()) {
				str_index = write_list_element(staticFields, ((ListElement)staticFields.agg_it1.myElement).object, str_index, first);
				first = false;
			}
		}
		if (str_index + 1 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}
	private int write_list_element(StaticFields staticFields, Object obj, int str_index, boolean first) throws SdaiException {
		if (str_index + 2 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
		}
		if (!first) {
			staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
		}
		if (obj instanceof CEntity) {
			staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
			str_index = long_to_byte_array(staticFields, ((CEntity)obj).instance_identifier, str_index);
		} else if (obj instanceof String) {
			String str = (String)obj;
			int str_ln = str.length();
			if (str_index + str_ln >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, str_index + 1, str_index + str_ln + 1);
			}
			for (int i = 0; i < str_ln; i++) {
				staticFields.instance_as_string[++str_index] = (byte)str.charAt(i);
			}
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return str_index;
	}


	private String toStringExpressAggregate(StaticFields staticFields, int str_index, int tag) throws SdaiException {
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		boolean first = true;
		int true_tag;
		if (staticFields.aggr_value == null) {
			staticFields.aggr_value = new Value();
		}

		if (staticFields.agg_it1 == null) {
			staticFields.agg_it1 = createIterator();
		} else {
			attachIterator(staticFields.agg_it1);
		}
		while (staticFields.agg_it1.next()) {
			if (tag >= SdaiSession.EXPRESSIONS_INTEGER2_AGGR && tag <= SdaiSession.EXPRESSIONS_ENUM2_AGGR) {
				str_index = writeExpressNested2AggrValue(staticFields, str_index, (Aggregate)getCurrentMemberObject(staticFields.agg_it1), tag, first);
				first = false;
				continue;
			} else if (tag == SdaiSession.EXPRESSIONS_DOUBLE3_AGGR) {
				str_index = writeExpressNested3AggrValue(staticFields, str_index, (Aggregate)getCurrentMemberObject(staticFields.agg_it1), first);
				first = false;
				continue;
			}
			Object member = null;
			if (tag >= SdaiSession.EXPRESSIONS_INST_AGGR && tag <= SdaiSession.EXPRESSIONS_BINARY_AGGR) {
				member = getCurrentMemberObject(staticFields.agg_it1);
			}
			true_tag = tag;
			switch (tag) {
				case SdaiSession.EXPRESSIONS_INTEGER_AGGR:
					staticFields.aggr_value.integer = ((Integer)member).intValue();
					break;
				case SdaiSession.EXPRESSIONS_DOUBLE_AGGR:
					staticFields.aggr_value.string = ((Double)member).toString();
					break;
				case SdaiSession.EXPRESSIONS_STRING_AGGR:
					staticFields.aggr_value.string = (String)member;
					break;
				case SdaiSession.EXPRESSIONS_LOGICAL_AGGR:
					staticFields.aggr_value.integer = ((Integer)member).intValue();
					break;
				case SdaiSession.EXPRESSIONS_BOOLEAN_AGGR:
					if (((Boolean)member).booleanValue()) {
						staticFields.aggr_value.integer = 2;
					} else {
						staticFields.aggr_value.integer = 1;
					}
					break;
				case SdaiSession.EXPRESSIONS_ENUM_AGGR:
					staticFields.aggr_value.integer = ((Integer)member).intValue();
					break;
				case SdaiSession.EXPRESSIONS_BINARY_AGGR:
					staticFields.aggr_value.reference = member;
					break;
				case SdaiSession.EXPRESSIONS_INST_AGGR:
					staticFields.aggr_value.reference = member;
					break;
				case SdaiSession.EXPRESSIONS_MIXED_AGGR:
					if (myType.select.is_mixed == 0) {
						staticFields.aggr_value.reference = getCurrentMemberObject(staticFields.agg_it1);
						true_tag = SdaiSession.EXPRESSIONS_INST_AGGR;
						break;
					}
					if (staticFields.sel_array == null) {
						staticFields.sel_array = new CDefined_type[NUMBER_OF_DEFINED_TYPES_IN_SELECT];
					}
					int test_res = testCurrentMember(staticFields.agg_it1, staticFields.sel_array);
					int sel_tag = myType.select.getType(staticFields.sel_array);
					switch (test_res) {
						case 0:
							true_tag = PhFileReader.MISSING;
							break;
						case 1:
							member = getCurrentMemberObject(staticFields.agg_it1);
							if (staticFields.sel_array[0] == null) {
								staticFields.aggr_value.reference = member;
								true_tag = SdaiSession.EXPRESSIONS_INST_AGGR;
							} else if (sel_tag == PhFileReader.STRING) {
								staticFields.aggr_value.string = (String)member;
								true_tag = SdaiSession.EXPRESSIONS_STRING_AGGR;
							} else if (sel_tag == PhFileReader.BINARY) {
								staticFields.aggr_value.reference = member;
								true_tag = SdaiSession.EXPRESSIONS_BINARY_AGGR;
							} else {
								throw new SdaiException(SdaiException.SY_ERR);
							}
							break;
						case 2:
							staticFields.aggr_value.integer = getCurrentMemberInt(staticFields.agg_it1);
							if (sel_tag == PhFileReader.INTEGER) {
								true_tag = SdaiSession.EXPRESSIONS_INTEGER_AGGR;
							} else if (sel_tag == PhFileReader.LOGICAL) {
								true_tag = SdaiSession.EXPRESSIONS_LOGICAL_AGGR;
							} else if (sel_tag == PhFileReader.ENUM) {
								true_tag = SdaiSession.EXPRESSIONS_ENUM_AGGR;
							} else {
								throw new SdaiException(SdaiException.SY_ERR);
							}
							break;
						case 3:
							staticFields.aggr_value.string = ((Double)getCurrentMemberObject(staticFields.agg_it1)).toString();
							true_tag = SdaiSession.EXPRESSIONS_DOUBLE_AGGR;
							break;
						case 4:
							if (getCurrentMemberBoolean(staticFields.agg_it1)) {
								staticFields.aggr_value.integer = 2;
							} else {
								staticFields.aggr_value.integer = 1;
							}
							true_tag = SdaiSession.EXPRESSIONS_BOOLEAN_AGGR;
							break;
						default:
							throw new SdaiException(SdaiException.VT_NVLD);
					}
				default:
					throw new SdaiException(SdaiException.VT_NVLD);
			}
			str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, true_tag, first);
			first = false;
		}
		if (str_index + 1 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}


	private int writeExpressNested2AggrValue(StaticFields staticFields, int str_index, Aggregate aggr, int tag, boolean first) throws SdaiException {
		if (str_index + 2 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
		}
		if (!first) {
			staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		SdaiIterator it2 = aggr.createIterator();
		boolean first_inside = true;
		while (it2.next()) {
			switch(tag) {
				case SdaiSession.EXPRESSIONS_INTEGER2_AGGR:
					staticFields.aggr_value.integer = ((A_integer)aggr).getCurrentMember(it2);
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_INTEGER_AGGR, first_inside);
					break;
				case SdaiSession.EXPRESSIONS_DOUBLE2_AGGR:
					staticFields.aggr_value.real = ((A_double)aggr).getCurrentMember(it2);
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_DOUBLE_AGGR, first_inside);
					break;
				case SdaiSession.EXPRESSIONS_STRING2_AGGR:
					staticFields.aggr_value.string = ((A_string)aggr).getCurrentMember(it2);
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_STRING_AGGR, first_inside);
					break;
				case SdaiSession.EXPRESSIONS_LOGICAL2_AGGR:
					staticFields.aggr_value.integer = ((A_enumeration)aggr).getCurrentMember(it2);
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_LOGICAL_AGGR, first_inside);
					break;
				case SdaiSession.EXPRESSIONS_BOOLEAN2_AGGR:
					if (((A_boolean)aggr).getCurrentMember(it2)) {
						staticFields.aggr_value.integer = 2;
					} else {
						staticFields.aggr_value.integer = 1;
					}
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_BOOLEAN_AGGR, first_inside);
					break;
				case SdaiSession.EXPRESSIONS_ENUM2_AGGR:
					staticFields.aggr_value.integer = ((A_enumeration)aggr).getCurrentMember(it2);
					str_index = writeExpressAggrValue(staticFields, str_index, staticFields.aggr_value, SdaiSession.EXPRESSIONS_ENUM_AGGR, first_inside);
					break;
				default:
					throw new SdaiException(SdaiException.VT_NVLD);
			}
			first_inside = false;
		}
		if (str_index + 1 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return str_index;
	}


	private int writeExpressNested3AggrValue(StaticFields staticFields, int str_index, Aggregate aggr, boolean first) throws SdaiException {
		if (str_index + 2 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
		}
		if (!first) {
			staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		SdaiIterator it3 = aggr.createIterator();
		boolean first_inside = true;
		while (it3.next()) {
			str_index = writeExpressNested2AggrValue(staticFields, str_index, ((Aa_double)aggr).getCurrentMember(it3), 
				SdaiSession.EXPRESSIONS_DOUBLE2_AGGR, first_inside);
			first_inside = false;
		}
		if (str_index + 1 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return str_index;
	}



	private int writeExpressAggrValue(StaticFields staticFields, int str_index, Value aggr_value, int tag, boolean first) throws SdaiException {
		int i;
		String str;
		if (str_index + 2 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
		}
		if (!first) {
			staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
		}
		switch(tag) {
			case PhFileReader.MISSING:
				staticFields.instance_as_string[++str_index] = PhFileReader.DOLLAR_SIGN;
				break;
			case SdaiSession.EXPRESSIONS_INTEGER_AGGR:
				str_index = long_to_byte_array(staticFields, aggr_value.integer, str_index);
				break;
			case SdaiSession.EXPRESSIONS_DOUBLE_AGGR:
				str = aggr_value.string;
				if (str_index + str.length() >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + str.length() + 1);
				}
				for (i = 0; i < str.length(); i++) {
					staticFields.instance_as_string[++str_index] = (byte)str.charAt(i);
				}
				break;
			case SdaiSession.EXPRESSIONS_STRING_AGGR:
				staticFields.instance_as_string[++str_index] = PhFileReader.APOSTROPHE;
				boolean uni = SdaiSession.isToStringUnicode();
				if (uni) {
//				if (SdaiSession.toStringUnicode) {
					SdaiSession.printer.fromStringBasicLatin(aggr_value.string);
				} else {
					SdaiSession.printer.fromString(aggr_value.string);
				}
				int ln = 2 * staticFields.string_length;
				if (str_index + ln + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + ln + 2);
				}
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++str_index] = staticFields.string[i];
					if (staticFields.string[i] == PhFileReader.APOSTROPHE) {
						staticFields.instance_as_string[++str_index] = PhFileReader.APOSTROPHE;
					}
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.APOSTROPHE;
				break;
			case SdaiSession.EXPRESSIONS_LOGICAL_AGGR:
				if (str_index + 3 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + 4);
				}
				if (aggr_value.integer == 1) {
					for (i = 0; i < 3; i++) {
						staticFields.instance_as_string[++str_index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (aggr_value.integer == 2) {
					for (i = 0; i < 3; i++) {
						staticFields.instance_as_string[++str_index] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					for (i = 0; i < 3; i++) {
						staticFields.instance_as_string[++str_index] = PhFileWriter.LOG_UNKNOWN[i];
					}
				}
				break;
			case SdaiSession.EXPRESSIONS_BOOLEAN_AGGR:
				if (str_index + 3 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + 3);
				}
				if (aggr_value.integer == 1) {
					for (i = 0; i < 3; i++) {
						staticFields.instance_as_string[++str_index] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					for (i = 0; i < 3; i++) {
						staticFields.instance_as_string[++str_index] = PhFileWriter.LOG_FALSE[i];
					}
				}
				break;
			case SdaiSession.EXPRESSIONS_ENUM_AGGR:
				str = getEnumValue(aggr_value.integer, (DataType)myType.getElement_type(null));
				SdaiSession.printer.fromStringBasicLatin(str);
				staticFields.instance_as_string[++str_index] = PhFileReader.DOT;
				if (str_index + staticFields.string_length + 1 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + staticFields.string_length + 2);
				}
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++str_index] = staticFields.string[i];
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.DOT;
				break;
			case SdaiSession.EXPRESSIONS_BINARY_AGGR:
				staticFields.instance_as_string[++str_index] = PhFileReader.QUOTATION_MARK;
				SdaiSession.printer.convertBinary((Binary)aggr_value.reference);
				if (str_index + staticFields.string_length + 2 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, str_index + 1, str_index + staticFields.string_length + 2);
				}
				for (i = 0; i < staticFields.string_length; i++) {
					staticFields.instance_as_string[++str_index] = staticFields.string[i];
				}
				staticFields.instance_as_string[++str_index] = PhFileReader.QUOTATION_MARK;
				break;
			case SdaiSession.EXPRESSIONS_INST_AGGR:
				CEntity inst = (CEntity)aggr_value.reference;
				staticFields.instance_as_string[++str_index] = PhFileReader.SPECIAL;
				str_index = long_to_byte_array(staticFields, inst.instance_identifier, str_index);
				break;
			default:
				throw new SdaiException(SdaiException.VT_NVLD);
		}
		return str_index;
	}


	String getEnumValue(int value, DataType type) throws SdaiException {
		if (type.express_type == DataType.LOGICAL) {
			return null;
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			type = (DataType)((CDefined_type)type).getDomain(null);
		}
		if (type.express_type < DataType.ENUMERATION || type.express_type > DataType.EXTENDED_EXTENSIBLE_ENUM) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		EEnumeration_type enum_type = (EEnumeration_type)type;
		A_string elements;
		if (type.express_type == DataType.EXTENSIBLE_ENUM || type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
			CEntity owning_inst = getOwningInstance();
			if (owning_inst == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			SdaiModel owning_mod = owning_inst.owning_model;
			if (owning_mod == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			}
			elements = enum_type.getElements(null, owning_mod.repository.session.sdai_context);
		} else {
			elements = enum_type.getElements(null);
		}
		if (value <= 0 || value > elements.myLength) {
			return null;
		}
		if (elements.testByIndex(value) == 0) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return elements.getByIndex(value);
	}


	private int toStringListOfModelsAll(StaticFields staticFields, int str_index) throws SdaiException {
		boolean first = true;
		int old_str_index;
		ASdaiModel amod = (ASdaiModel)myData;
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					old_str_index = str_index;
					str_index = model.toStringListOfModelsAll(staticFields, str_index, this, first);
					if (str_index > old_str_index) {
						first = false;
					}
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				if ((model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.NO_ACCESS) {
					old_str_index = str_index;
					str_index = model.toStringListOfModelsAll(staticFields, str_index, this, first);
					if (str_index > old_str_index) {
						first = false;
					}
				}
			}
		}
		return str_index;
	}


/**
	Writes to a byte array the elements of the set which consists of all 
	instances of a specified entity data type and all instances of all its 
	subtypes collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>. 
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
	The method returns the last index value in the byte array used for writing 
	the names of the instances in the above set.
	This method is invoked in <code>getAsString</code> in this class.
*/
	private int toStringListOfModels(StaticFields staticFields, int str_index) throws SdaiException {
		boolean first = true;
		int old_str_index;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				old_str_index = str_index;
				str_index = model.toStringListOfModels(staticFields, str_index, this, first, myDataA[1]);
				if (str_index > old_str_index) {
					first = false;
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				old_str_index = str_index;
				str_index = model.toStringListOfModels(staticFields, str_index, this, first, myDataA[1]);
				if (str_index > old_str_index) {
					first = false;
				}
			}
		}
		return str_index;
	}


/**
	Writes to a byte array the elements of the set which consists of all 
	instances of a specified entity data type (but not instances of its 
	subtypes) collected going through all started <code>SdaiModel</code>s in an 
	<code>ASdaiModel</code>. This set is returned by <code>getExactInstances</code> 
	method (one of two its variations) in class <code>ASdaiModel</code>. 
	The set is represented by an aggregate in which the array <code>myData</code> 
	contains two elements: the first coincides with the corresponding element 
	in the list <code>ASdaiModel</code> from which this set was constructed, 
	whereas the second represents an entity (either definition or class) 
	instances of which are collected. 
	The method returns the last index value in the byte array used for writing 
	the names of the instances in the above set.
	This method is invoked in <code>getAsString</code> in this class.
*/
	private int toStringListOfModelsExact(StaticFields staticFields, int str_index) throws SdaiException {
		boolean first = true;
		int old_str_index;
		Object [] myDataA = (Object [])myData;
		ASdaiModel amod = (ASdaiModel)myDataA[0];
		SdaiModel model;
		if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
			ListElement element = (ListElement)amod.myData[0];
			while (element != null) {
				model = (SdaiModel)element.object;
				old_str_index = str_index;
				str_index = model.toStringListOfModelsExact(staticFields, str_index, this, first, myDataA[1]);
				if (str_index > old_str_index) {
					first = false;
				}
				element = element.next;
			}
		} else {
			for (int j = 0; j < amod.myLength; j++) {
				model = (SdaiModel)amod.myData[j];
				old_str_index = str_index;
				str_index = model.toStringListOfModelsExact(staticFields, str_index, this, first, myDataA[1]);
				if (str_index > old_str_index) {
					first = false;
				}
			}
		}
		return str_index;
	}


/**
	Increases the size of the auxiliary array 'instance_as_string' either 
	twice or to satisfy the demand requested whichever of the alternatives 
	leads to a bigger array. This method besides this class is also used in 
	<code>SdaiModel</code>.
*/
	static void enlarge_instance_string(StaticFields staticFields, int str_length, int demand) {
		int new_length = staticFields.instance_as_string.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		byte new_instance_as_string[] = new byte[new_length];
		System.arraycopy(staticFields.instance_as_string, 0, new_instance_as_string, 0, str_length);
		staticFields.instance_as_string = new_instance_as_string;
	}


/**
	Returns <code>true</code> if the submitted value is allowed to be put into 
	the aggregate (using set/add methods). To verify this, dictionary data is 
	used. This method is applied when members of the aggregate are not of select 
	type. 
*/
	private boolean analyse_value(Object value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type >= DataType.NUMBER && type.express_type <= DataType.BINARY) {
			return analyse_simple_value((ESimple_type)type, value);
		} else if (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) {
			return false;
		} else if (type.express_type == DataType.ENTITY) {
			if (!(value instanceof CEntity) && !(value instanceof SdaiModel.Connector)) {
				return false;
			}
			if (value instanceof SdaiModel.Connector) {
				return true;
			}
			CEntity_definition value_type = (CEntity_definition)((CEntity)value).getInstanceType();
			return value_type.isSubtypeOf((CEntity_definition)type);
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.SELECT && underlying_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			} else if (underlying_type.express_type >= DataType.ENUMERATION && underlying_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
				if (value instanceof Integer) {
					int int_value = ((Integer)value).intValue();
					A_string ee;
					if (underlying_type.express_type == DataType.EXTENSIBLE_ENUM || underlying_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						CEntity owning_inst = getOwningInstance();
						if (owning_inst == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						SdaiModel owning_mod = owning_inst.owning_model;
						if (owning_mod == null) {
							throw new SdaiException(SdaiException.AI_NVLD);
						}
						ee = ((EExtensible_enumeration_type)underlying_type).getElements(null, 
							owning_mod.repository.session.sdai_context);
					} else {
						ee = ((EEnumeration_type)underlying_type).getElements(null);
					}
					if (int_value <= ee.myLength && int_value > 0) {
						return true;
					}
				}
				return false;
			} else if (underlying_type.express_type >= DataType.NUMBER && underlying_type.express_type <= DataType.BINARY) {
				return analyse_simple_value((ESimple_type)underlying_type, value);
			} else if (underlying_type.express_type >= DataType.LIST && underlying_type.express_type <= DataType.AGGREGATE) {
				return false;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		return false;
	}


/**
	Returns <code>true</code> if the submitted value is allowed to be put into 
	the aggregate (using set/add methods) the type of which members is simple 
	type (specified by the first parameter). This method is applied (in 
	<code>analyse_value</code>) when members of the aggregate are not of select 
	type. 
*/
	private boolean analyse_simple_value(ESimple_type simple, Object value) throws SdaiException {
		int int_value;
		int width;
		EBound bound;
		DataType dt = (DataType)simple;
		if (dt.express_type == DataType.NUMBER) {
			if ((value instanceof Double && !((Double)value).isNaN()) ||
					(value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE)) {
				return true;
			}
		} else if (dt.express_type == DataType.INTEGER) {
			if (value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE) {
				return true;
			}
		} else if (dt.express_type == DataType.REAL) {
			if (value instanceof Double && !((Double)value).isNaN()) {
				return true;
			}
		} else if (dt.express_type == DataType.BOOLEAN) {
			if (value instanceof Integer) {
				int_value = ((Integer)value).intValue();
				if (int_value > 0 && int_value < 3) {
					return true;
				}
			}
		} else if (dt.express_type == DataType.LOGICAL) {
			if (value instanceof Integer) {
				int_value = ((Integer)value).intValue();
				if (int_value > 0 && int_value < 4) {
					return true;
				}
			}
		} else if (dt.express_type == DataType.BINARY) {
			if (value instanceof Binary) {
				CBinary_type bin_type = (CBinary_type)simple;
				if (bin_type.testWidth(null)) {
					int bit_count = ((Binary)value).getSize();
					bound = bin_type.getWidth(null);
					if (!(bound instanceof EInteger_bound)) {
						return true;
					}
					width = bound.getBound_value(null);
					if (bin_type.getFixed_width(null)) {
						if (bit_count == width) {
							return true;
						}
					} else {
						if (bit_count <= width) {
							return true;
						}
					}
				} else {
					return true;
				}
			}
		} else if (dt.express_type == DataType.STRING) {
			if (value instanceof String) {
				CString_type str_type = (CString_type)simple;
				if (str_type.testWidth(null)) {
					bound = str_type.getWidth(null);
					if (!(bound instanceof EInteger_bound)) {
						return true;
					}
					width = bound.getBound_value(null);
					if (str_type.getFixed_width(null)) {
						if (((String)value).length() == width) {
							return true;
						}
					} else {
						if (((String)value).length() <= width) {
							return true;
						}
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}


/**
	Returns <code>true</code> if the submitted value is allowed to be put into 
	the aggregate (using set/add methods). To verify this, dictionary data are 
	used. This method is applied when members of the aggregate are of select 
	type. The first three parameters provide a select information.
*/
	private boolean analyse_select_value(int tag, SelectType sel_type, int sel_number, Object value) 
			throws SdaiException {
		int int_value;
		int width;
		EBound bound;
		switch(tag) {
			case PhFileReader.ENTITY_REFERENCE:
				if (value instanceof CEntity) {
					CEntity_definition value_type = (CEntity_definition)((CEntity)value).getInstanceType();
					return analyse_entity_in_select(sel_type, value_type);
				}
				if (value instanceof SdaiModel.Connector || value instanceof CLateBindingEntity) {
					return true;
				}
				break;
			case PhFileReader.INTEGER:
				if (value instanceof Integer && ((Integer)value).intValue() != Integer.MIN_VALUE) {
					return true;
				}
				break;
			case PhFileReader.REAL:
				if (value instanceof Double && !((Double)value).isNaN()) {
					return true;
				}
				break;
			case PhFileReader.LOGICAL:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					if (int_value > 0 && int_value < 4) {
						return true;
					}
				}
				break;
			case PhFileReader.BOOLEAN:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					if (int_value > 0 && int_value < 3) {
						return true;
					}
				}
				break;
			case PhFileReader.ENUM:
				if (value instanceof Integer) {
					int_value = ((Integer)value).intValue();
					EEnumeration_type enumer = (EEnumeration_type)sel_type.types[sel_number - 2];
					A_string ee;
					DataType dt = (DataType)enumer;
					if (dt.express_type == DataType.EXTENSIBLE_ENUM || dt.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						CEntity owning_inst = getOwningInstance();
						if (owning_inst == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						SdaiModel owning_mod = owning_inst.owning_model;
						if (owning_mod == null) {
							throw new SdaiException(SdaiException.AI_NVLD);
						}
						ee = enumer.getElements(null, owning_mod.repository.session.sdai_context);
					} else {
						ee = enumer.getElements(null);
					}
					if (int_value <= ee.myLength && int_value > 0) {
						return true;
					}
				}
				break;
			case PhFileReader.BINARY:
				if (value instanceof Binary) {
					CBinary_type bin_type = (CBinary_type)sel_type.types[sel_number - 2];
					if (bin_type.testWidth(null)) {
						int bit_count = ((Binary)value).getSize();
						bound = bin_type.getWidth(null);
						if (!(bound instanceof EInteger_bound)) {
							return true;
						}
						width = bound.getBound_value(null);
						if (bin_type.getFixed_width(null)) {
							if (bit_count == width) {
								return true;
							}
						} else {
							if (bit_count <= width) {
								return true;
							}
						}
					} else {
						return true;
					}
				}
				break;
			case PhFileReader.STRING:
				if (value instanceof String) {
					CString_type str_type = (CString_type)sel_type.types[sel_number - 2];
					if (str_type.testWidth(null)) {
						bound = str_type.getWidth(null);
						if (!(bound instanceof EInteger_bound)) {
							return true;
						}
						width = bound.getBound_value(null);
						if (str_type.getFixed_width(null)) {
							if (((String)value).length() == width) {
								return true;
							}
						} else {
							if (((String)value).length() <= width) {
								return true;
							}
						}
					} else {
						return true;
					}
				}
				break;
			default:
			}
		return false;
	}


/**
	Returns <code>true</code> if the submitted entity is in conformance with 
	the specified select type, that is, instances of this entity can be assigned 
	to objects having the specified select type. 
	This method is applied (in <code>analyse_select_value</code>) when members 
	of the aggregate are of select type. 
	
*/
	boolean analyse_entity_in_select(SelectType sel_type, CEntity_definition value_type) 
			throws SdaiException {
		ANamed_type sels;
		if (sel_type.express_type >= DataType.EXTENSIBLE_SELECT && sel_type.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			CEntity owning_inst = getOwningInstance();
			if (owning_inst == null) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			SdaiModel owning_mod = owning_inst.owning_model;
			if (owning_mod == null) {
				throw new SdaiException(SdaiException.AI_NVLD);
			}
			SdaiSession ss = owning_mod.repository.session;
			if (ss.sdai_context == null) {
				StaticFields staticFields = StaticFields.get();
				staticFields.context_schema = owning_mod.underlying_schema;
				if (!ss.sdai_context_missing) {
					ss.sdai_context_missing = true;
					ss.printWarningToLogoSdaiContext(AdditionalMessages.SS_SCMI);
				}
				sels = ((EExtensible_select_type)sel_type).getSelections(null, null);
				staticFields.context_schema = null;
			} else {
				sels = ((EExtensible_select_type)sel_type).getSelections(null, ss.sdai_context);
			}
		} else {
			sels = ((ESelect_type)sel_type).getSelections(null);
		}
		if (((AEntity)sels).myLength < 0) {
			((AEntity)sels).resolveAllConnectors();
		}
		if (((AEntity)sels).myLength == 1) {
			return examine_alternative((EData_type)((AEntity)sels).myData, value_type);
		}
		Object [] myDataA = (Object [])((AEntity)sels).myData;
		for (int i = 0; i < ((AEntity)sels).myLength; i++) {
			boolean res = examine_alternative((EData_type)myDataA[i], value_type);
			if (res) {
				return true;
			}
		}
		return false;
	}

	private boolean examine_alternative(EData_type alternative, CEntity_definition value_type) throws SdaiException {
		if (((DataType)alternative).express_type == DataType.ENTITY) {
			return value_type.isSubtypeOf((CEntity_definition)alternative);
		}
		DataType dom = (DataType)alternative;
		while (dom.express_type == DataType.DEFINED_TYPE) {
			dom = (DataType)((CDefined_type)dom).getDomain(null);
		}
		if (dom.express_type >= DataType.SELECT && dom.express_type <= DataType.ENT_EXT_EXT_SELECT) {
			return analyse_entity_in_select((SelectType)dom, value_type);
		}
		return false;
	}


	void getValue(Value val, AggregationType aggr_type, int select) throws SdaiException {
		Object value;
//		EAggregation_type a_type = null;
		int sel_number;
		int i;
		if (select <= 0) {
			if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
				select = 1;
			} else {
				DataType member_type = analyse_member_type(aggr_type);
				if (member_type == null) {
// Print message !!!!!!!!!!!!!!!!!! 
// Such a case is processed by special aggregate java classes, not CAggregate
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
//			throw new SdaiException(SdaiException.SY_ERR, base);
				} else if (member_type.express_type >= DataType.LIST && member_type.express_type <= DataType.AGGREGATE) {
//				a_type = (EAggregation_type)member_type;
				} else {
					select = 1;
				}
			}
		}
		if (myLength == 0) {
			return;
		}
		Object [] myDataA;
		if (aggr_type.express_type == DataType.LIST) {
			ListElement element;
			if (select <= 1) {
				if (myLength == 1) {
					if (select == 1) {
						val.nested_values[0].set(null, (CEntity)myData);
					} else {
						val.nested_values[0].set(null, (Aggregate)myData);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (select == 1) {
							val.nested_values[i].set(null, (CEntity)myDataA[i]);
						} else {
							val.nested_values[i].set(null, (Aggregate)myDataA[i]);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					i = 0;
					while (element != null) {
						value = element.object;
						if (select == 1) {
							val.nested_values[i].set(null, (CEntity)value);
						} else {
							val.nested_values[i].set(null, (Aggregate)value);
						}
						i++;
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					sel_number = ((Integer)myDataA[1]).intValue();
					setAggrMember(val, 0, myDataA[0], sel_number, aggr_type);
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					i = 0;
					while (element != null) {
						element = element.next;
						sel_number = ((Integer)element.object).intValue();
						setAggrMember(val, i, element.object, sel_number, aggr_type);
						i++;
						element = element.next;
					}
				}
			}
		} else {
			if (select <= 1) {
				int ln;
				if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
					ln = getMemberCountListOfModels();
				} else if (myType.express_type == DataType.ARRAY && myData == null) {
					CArray_type arr_type = (CArray_type)myType;
					ln = arr_type.getUpper_index(null).getBound_value(null) - 
						arr_type.getLower_index(null).getBound_value(null) + 1;
				} else {
					ln = myLength;
				}
				if (ln == 1) {
					if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
						value = getByIndexObject(1);
					} else {
						value = myData;
					}
					if (select == 1) {
						val.nested_values[0].set(null, (CEntity)value);
					} else {
						val.nested_values[0].set(null, (Aggregate)value);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < ln; i++) {
						if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
							value = getByIndexObject(i + 1);
						} else {
							value = myDataA[i];
						}
						if (select == 1) {
							val.nested_values[i].set(null, (CEntity)value);
						} else {
							val.nested_values[i].set(null, (Aggregate)value);
						}
					}
				}
			} else {
				int index = 0;
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					sel_number = ((Integer)myDataA[index + 1]).intValue();
					setAggrMember(val, i, myDataA[index], sel_number, aggr_type);
					index += 2;
				}
			}
		}
	}


	private void setAggrMember(Value val, int index, Object value, int sel_number, 
			AggregationType aggr_type) throws SdaiException {
		if (value == null) {
			val.nested_values[index].set(null, (String)null);
			return;
		}
		int value_type = aggr_type.select.tags[sel_number - 2];
		switch (value_type) {
			case PhFileReader.INTEGER:
				val.nested_values[index].set(null, ((Integer)value).intValue());
				break;
			case PhFileReader.BOOLEAN:
			case PhFileReader.LOGICAL:
				val.nested_values[index].setLB(null, ((Integer)value).intValue());
				break;
			case PhFileReader.REAL:
				val.nested_values[index].set(null, ((Double)value).doubleValue());
				break;
			case PhFileReader.ENUM:
				int int_value = ((Integer)value).intValue();
				EEnumeration_type enumer = (EEnumeration_type)aggr_type.select.types[sel_number - 2];
				if (int_value == 0) {
					val.nested_values[index].setEnum(null, null);
				} else {
					A_string ee;
					SdaiContext context = null;
					DataType dt = (DataType)enumer;
					if (dt.express_type == DataType.EXTENSIBLE_ENUM || dt.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
						CEntity owning_inst = getOwningInstance();
						if (owning_inst == null) {
							throw new SdaiException(SdaiException.SY_ERR);
						}
						SdaiModel owning_mod = owning_inst.owning_model;
						if (owning_mod == null) {
							throw new SdaiException(SdaiException.AI_NVLD);
						}
						context = owning_mod.repository.session.sdai_context;
						ee = enumer.getElements(null, context);
					} else {
						ee = enumer.getElements(null);
					}
					val.nested_values[index].setEnum(context, ee.getByIndex(int_value));
				}
				break;
			case PhFileReader.STRING:
				val.nested_values[index].set(null, (String)value);
				break;
			case PhFileReader.BINARY:
				val.nested_values[index].set(null, (Binary)value);
				break;
			case PhFileReader.EMBEDDED_LIST:
//				AggregationType a_type = (AggregationType)aggr_type.select.types[sel_number - 2];
//				val.nested_values[index].init(a_type);
				val.nested_values[index].set(null, (Aggregate)value);
				break;
			default:
		}
	}


	private DataType analyse_member_type(AggregationType aggr_type) throws SdaiException {
		DataType type = (DataType)aggr_type.getElement_type(null);
		if ( (type.express_type >= DataType.LIST && type.express_type <= DataType.AGGREGATE) || 
				type.express_type == DataType.ENTITY) {
			return type;
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			return null;
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if (underlying_type.express_type >= DataType.LIST && underlying_type.express_type <= DataType.AGGREGATE) {
				return underlying_type;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				return null;
			}
		}
		return null;
	}


	void addByIndexAttribute(int index, Object value) throws SdaiException {
		if (value == null) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		index--;
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.SY_ERR, this);
		}
		Object [] myDataA;
		ListElement element = null;
		ListElement new_element;
		if (myLength == 0) {
			myData = value;
		} else if (myLength == 1) {
			myDataA = new Object[2];
			if (index == 0) {
				myDataA[0] = value;
				myDataA[1] = myData;
			} else {
				myDataA[0] = myData;
				myDataA[1] = value;
			}
			myData = myDataA;
		} else if (myLength == 2) {
			myDataA = (Object [])myData;
			switch(index) {
				case 0:
					element = new ListElement(value);
					element.next = new ListElement(myDataA[0]);
					element.next.next = new ListElement(myDataA[1]);
					break;
				case 1:
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(value);
					element.next.next = new ListElement(myDataA[1]);
					break;
				case 2:
					element = new ListElement(myDataA[0]);
					element.next = new ListElement(myDataA[1]);
					element.next.next = new ListElement(value);
					break;
			}
			myData = element;
		} else if (myLength <= SHORT_AGGR) {
			element = (ListElement)myData;
			new_element = new ListElement(value);
			if (index == 0) {
				new_element.next = element;
				myData = new_element;
			} else {
				while (--index > 0) {
					element = element.next;
				}
				if (element.next != null) {
					new_element.next = element.next;
				}
				element.next = new_element;
			}
			if (myLength == SHORT_AGGR) {
				myDataA = new Object[2];
				myDataA[0] = myData;
				while (new_element.next != null) {
					new_element = new_element.next;
				}
				myDataA[1] = new_element;
				myData = myDataA;
			}
		} else {
			myDataA = (Object [])myData;
			new_element = new ListElement(value);
			if (index == myLength) {
				((ListElement)myDataA[1]).next = new_element;
				myDataA[1] = new_element;
			} else if (index == 0) {
				new_element.next = (ListElement)myDataA[0];
				myDataA[0] = new_element;
			}	else {
				element = (ListElement)myDataA[0];
				while (--index > 0) {
					element = element.next;
				}
				if (element.next != null) {
					new_element.next = element.next;
				}
				element.next = new_element;
			}
		}
		myLength++;
	}


	void usedin(CEntity referenced, CEntity referencing, CEntity_definition entityDef, 
			Value result) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		int sel_number = 1;
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 2;
		}
		if (myLength == 0) {
			return;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int i;
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 1) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						if (myData == referenced) {
							result.addNewMember(referencing, entityDef);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).usedin(referenced, referencing, entityDef, result);
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof CEntity) {
							if (myDataA[i] == referenced) {
								result.addNewMember(referencing, entityDef);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).usedin(referenced, referencing, entityDef, result);
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							if (element.object == referenced) {
								result.addNewMember(referencing, entityDef);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).usedin(referenced, referencing, entityDef, result);
						}
						element = element.next;
					}
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof CEntity) {
						if (myDataA[0] == referenced) {
							result.addNewMember(referencing, entityDef);
						}
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).usedin(referenced, referencing, entityDef, result);
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof CEntity) {
							if (element.object == referenced) {
								result.addNewMember(referencing, entityDef);
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).usedin(referenced, referencing, entityDef, result);
						}
						element = element.next.next;
					}
				}
			}
		} else {
			if (sel_number <= 1) {
				if (myLength == 1) {
					if (myData instanceof CEntity) {
						if (myData == referenced) {
							result.addNewMember(referencing, entityDef);
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).usedin(referenced, referencing, entityDef, result);
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof CEntity) {
							if (myDataA[i] == referenced) {
								result.addNewMember(referencing, entityDef);
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).usedin(referenced, referencing, entityDef, result);
						}
					}
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					int index = i * 2;
					if (myDataA[index] instanceof CEntity) {
						if (myDataA[index] == referenced) {
							result.addNewMember(referencing, entityDef);
						}
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).usedin(referenced, referencing, entityDef, result);
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
	}


	boolean check_string_in_aggregate() throws SdaiException {
		if (myType == null || myType.select == null || myType.select.is_mixed == 0) {
			return true;
		}
		Object value;
		if (myLength == 0) {
			return true;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		Object [] myDataA;
		if (myType.express_type == DataType.LIST) {
			if (myLength == 1) {
				myDataA = (Object [])myData;
				if (!check_string_value_in_aggr(myDataA[0], myDataA[1])) {
					if (has_con) {
						myLength = -myLength;
					}
					return false;
				}
			} else {
				ListElement element;
				if (myLength <= SHORT_AGGR_SELECT) {
					element = (ListElement)myData;
				} else {
					myDataA = (Object [])myData;
					element = (ListElement)myDataA[0];
				}
				while (element != null) {
					value = element.object;
					element = element.next;
					if (!check_string_value_in_aggr(value, element.object)) {
						if (has_con) {
							myLength = -myLength;
						}
						return false;
					}
					element = element.next;
				}
			}
		} else {
			myDataA = (Object [])myData;
			for (int i = 0; i < myLength * 2; i += 2) {
				value = myDataA[i];
				if (!check_string_value_in_aggr(value, myDataA[i+1])) {
					if (has_con) {
						myLength = -myLength;
					}
					return false;
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
		return true;
	}
	boolean check_string_value_in_aggr(Object value, Object sel_value) throws SdaiException {
		if (value instanceof String) {
			int sel_number = ((Integer)sel_value).intValue();
			if (sel_number <= 1) {
				throw new SdaiException(SdaiException.SY_ERR);
			}
			DataType type = (DataType)myType.select.types[sel_number - 2];
			if (type.express_type != DataType.STRING) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			if (!((StringType)type).check_width((String)value)) {
				return false;
			}
		} else if (value instanceof A_string) {
			if (!((A_string)value).check_A_string()) {
				return false;
			}
		} else if (value instanceof Aa_string) {
			if (!((Aa_string)value).check_Aa_string()) {
				return false;
			}
		} else if (value instanceof Aaa_string) {
			if (!((Aaa_string)value).check_Aaa_string()) {
				return false;
			}
		} else if (value instanceof CAggregate) {
			if (!((CAggregate)value).check_string_in_aggregate()) {
				return false;
			}
		}
		return true;
	}


	void resolveAllConnectors() throws SdaiException {
		if (myType == null || myType != null && myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		int sel_number = 0;
		if (myType.select != null && myType.select.is_mixed > 0) {
			sel_number = 1;
		}
		if (myLength == 0) {
			return;
		}
		if (myLength < 0) {
			myLength = -myLength;
		}
		int i;
		Object [] myDataA = null;
		int count = 0;
		if (myType.express_type == DataType.LIST) {
			ListElement element;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof SdaiModel.Connector) {
						if (resolveConnector(null, false, (SdaiModel.Connector)myData, 0) != null) {
							count++;
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).resolveAllConnectors();
						count++;
					} else {
						count++;
					}
				} else if (myLength == 2) {
					myDataA = (Object [])myData;
					for (i = 0; i < 2; i++) {
						if (myDataA[i] instanceof SdaiModel.Connector) {
							if (resolveConnector(null, false, (SdaiModel.Connector)myDataA[i], i) != null) {
								count++;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).resolveAllConnectors();
							count++;
						} else {
							count++;
						}
					}
				} else {
					if (myLength <= SHORT_AGGR) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof SdaiModel.Connector) {
							if (resolveConnector(element, false, (SdaiModel.Connector)element.object, 0) != null) {
								count++;
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).resolveAllConnectors();
							count++;
						} else {
							count++;
						}
						element = element.next;
					}
				}
				if (count >= myLength) {
					return;
				}
				if (count == 0) {
					myData = null;
				} else if (count <= 2) {
					contractShortList(count, false);
				} else {
					contractLongList(count, false);
				}
			} else {
				if (myLength == 1) {
					myDataA = (Object [])myData;
					if (myDataA[0] instanceof SdaiModel.Connector) {
						if (resolveConnector(null, true, (SdaiModel.Connector)myDataA[0], 0) != null) {
							count++;
						}
					} else if (myDataA[0] instanceof CAggregate) {
						((CAggregate)myDataA[0]).resolveAllConnectors();
						count++;
					} else {
						count++;
					}
				} else {
					if (myLength <= SHORT_AGGR_SELECT) {
						element = (ListElement)myData;
					} else {
						myDataA = (Object [])myData;
						element = (ListElement)myDataA[0];
					}
					while (element != null) {
						if (element.object instanceof SdaiModel.Connector) {
							if (resolveConnector(element, true, (SdaiModel.Connector)element.object, 0) != null) {
								count++;
							}
						} else if (element.object instanceof CAggregate) {
							((CAggregate)element.object).resolveAllConnectors();
							count++;
						} else {
							count++;
						}
						element = element.next.next;
					}
				}
				if (count >= myLength) {
					return;
				}
				if (count == 0) {
					myData = null;
				} else if (count <= 2) {
					contractShortList(count, true);
				} else {
					contractLongList(count, true);
				}
			}
		} else {
			int index = 0;
			if (sel_number <= 0) {
				if (myLength == 1) {
					if (myData instanceof SdaiModel.Connector) {
						if (resolveConnector(false, (SdaiModel.Connector)myData, 0) != null) {
							count++;
						}
					} else if (myData instanceof CAggregate) {
						((CAggregate)myData).resolveAllConnectors();
						count++;
					} else {
						count++;
					}
				} else {
					myDataA = (Object [])myData;
					for (i = 0; i < myLength; i++) {
						if (myDataA[i] instanceof SdaiModel.Connector) {
							if (resolveConnector(false, (SdaiModel.Connector)myDataA[i], i) != null) {
								index = i;
								count++;
							}
						} else if (myDataA[i] instanceof CAggregate) {
							((CAggregate)myDataA[i]).resolveAllConnectors();
							index = i;
							count++;
						} else {
							index = i;
							count++;
						}
					}
				}
				if (count >= myLength) {
					return;
				}
				if (count == 0) {
					myData = null;
				} else if (count == 1) {
					myData = myDataA[index];
				} else {
					contractLongSet(count, false);
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					index = i * 2;
					if (myDataA[index] instanceof SdaiModel.Connector) {
						if (resolveConnector(true, (SdaiModel.Connector)myDataA[index], i) != null) {
							count++;
						}
					} else if (myDataA[index] instanceof CAggregate) {
						((CAggregate)myDataA[index]).resolveAllConnectors();
						count++;
					} else {
						count++;
					}
				}
				if (count >= myLength) {
					return;
				}
				if (count == 0) {
					myData = null;
				} else {
					contractLongSet(count, true);
				}
			}
		}
		myLength = count;
//		owner.modified();
//		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
	}


	void contractShortList(int size, boolean sel) throws SdaiException {
		Object [] myDataA = null;
		ListElement element;
		if (!sel) {
			if (myLength == 2) {
				myDataA = (Object [])myData;
				for (int i = 0; i < 2; i++) {
					if (myDataA[i] != null) {
						myData = myDataA[i];
						return;
					}
				}
			} else {
				if (myLength <= SHORT_AGGR) {
					element = (ListElement)myData;
				} else {
					myDataA = (Object [])myData;
					element = (ListElement)myDataA[0];
				}
				int count = 0;
				while (element != null) {
					if (element.object != null) {
						if (size == 1) {
							myData = element.object;
							return;
						} else {
							if (count == 0) {
								myDataA = new Object[2];
								myDataA[0] = element.object;
								count++;
							} else {
								myDataA[1] = element.object;
								myData = myDataA;
								return;
							}
						}
					}
					element = element.next;
				}
			}
		} else {
			if (myLength <= SHORT_AGGR_SELECT) {
				element = (ListElement)myData;
			} else {
				myDataA = (Object [])myData;
				element = (ListElement)myDataA[0];
			}
			while (element != null) {
				if (element.object != null) {
					myDataA = new Object[2];
					myDataA[0] = element.object;
					myDataA[1] = element.next.object;
					myData = myDataA;
					return;
				}
				element = element.next.next;
			}
		}
	}


	void contractLongList(int size, boolean sel) throws SdaiException {
		int count = 0;
		Object [] myDataA;
		ListElement element, write_el, last_el = null, first_el;
		if (!sel) {
			if (myLength <= SHORT_AGGR) {
				first_el = write_el = element = (ListElement)myData;
			} else {
				myDataA = (Object [])myData;
				first_el = write_el = element = (ListElement)myDataA[0];
			}
			while (element != null) {
				if (element.object != null) {
					write_el.object = element.object;
					last_el = write_el;
					write_el = write_el.next;
					count++;
					if (count >= size) {
						break;
					}
				}
				element = element.next;
			}
			last_el.next = null;
			if (size <= SHORT_AGGR) {
				myData = first_el;
			} else {
				myDataA = new Object[2];
				myDataA[0] = first_el;
				myDataA[1] = last_el;
				myData = myDataA;
			}
		} else {
			if (myLength <= SHORT_AGGR_SELECT) {
				first_el = write_el = element = (ListElement)myData;
			} else {
				myDataA = (Object [])myData;
				first_el = write_el = element = (ListElement)myDataA[0];
			}
			while (element != null) {
				if (element.object != null) {
					write_el.object = element.object;
					write_el = write_el.next;
					write_el.object = element.next.object;
					last_el = write_el;
					write_el = write_el.next;
					count++;
					if (count >= size) {
						break;
					}
				}
				element = element.next.next;
			}
			last_el.next = null;
			if (size <= SHORT_AGGR_SELECT) {
				myData = first_el;
			} else {
				myDataA = new Object[2];
				myDataA[0] = first_el;
				myDataA[1] = last_el;
				myData = myDataA;
			}
		}
	}


	void contractLongSet(int size, boolean sel) throws SdaiException {
		int i;
		Object [] myDataA;
		int write_ind = 0;
		if (!sel) {
			myDataA = (Object [])myData;
			for (i = 0; i < myLength; i++) {
				if (myDataA[i] != null) {
					myDataA[write_ind++] = myDataA[i];
					if (write_ind >= size) {
						break;
					}
				}
			}
			for (i = write_ind; i < myLength; i++) {
				myDataA[i] = null;
			}
			myData = myDataA;
		} else {
			myDataA = (Object [])myData;
			int wr_i = 0;
			int index;
			for (i = 0; i < myLength; i++) {
				index = i * 2;
				if (myDataA[index] != null) {
					myDataA[wr_i++] = myDataA[index];
					myDataA[wr_i++] = myDataA[index + 1];
					write_ind++;
					if (write_ind >= size) {
						break;
					}
				}
			}
			for (i = write_ind; i < myLength; i++) {
				index = i * 2;
				myDataA[index] = myDataA[index + 1] = null;
			}
			myData = myDataA;
		}
	}

	private static Integer getIntegerWithValue(int value) {
		if(numbers.length < value) {
			synchronized(numbers) {
				if(numbers.length < value) {
					enlarge_numbers(value);
				}
			}
		}
		return numbers[value - 1];
	}

/**
	Increases the size of the array 'numbers' either twice or according to some demand.
	The elements of this array are used as select numbers written to an aggregate
	together with values in the cases when the element type of the
	aggregate is (mixed) select type.
	The method is applied in class CAggregate.
*/
	private static void enlarge_numbers(int demand) {
		int new_length = numbers.length * 2;
		if (new_length < demand) {
			new_length = demand;
		}
		Integer new_numbers[] = new Integer[new_length];
		System.arraycopy(numbers, 0, new_numbers, 0, numbers.length);
		for (int i = numbers.length; i <= new_length; i++) {
			new_numbers[i] = new Integer(i + 1);
		}
		numbers = new_numbers;
	}


	private static Integer[] init_numbers() {
		Integer[] numbers = new Integer[COUNT_OF_INTEGER_NUMBERS];
		for (int k = 0; k < COUNT_OF_INTEGER_NUMBERS; k++) {
			numbers[k] = new Integer(k + 1);
		}
		return numbers;
	}


// Shortcuts for aggregates of mixed selects

/**
 * It is a specialization of 
 * {@link #isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public boolean isMember(EEntity value) throws SdaiException {
		return isMember(value, null);
	}


/**
 * It is {@link #getByIndexObject getByIndexObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 */
	public EEntity getByIndexEntity(int index) throws SdaiException {
		return (EEntity)getByIndexObject(index);
	}


/**
 * It is a specialization of 
 * {@link #setByIndex(int, Object, EDefined_type []) setByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void setByIndex(int index, EEntity value) throws SdaiException {
		setByIndex(index, value, null);
	}


/**
 * It is a specialization of 
 * {@link #addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addByIndex(int index, EEntity value) throws SdaiException {
		addByIndex(index, value, null);
	}


/**
 * It is a specialization of 
 * {@link #addUnordered(Object, EDefined_type []) addUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public void addUnordered(EEntity value) throws SdaiException {
		addUnordered(value, null);
	}


/**
 * It is a specialization of 
 * {@link #removeUnordered(Object, EDefined_type []) removeUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public void removeUnordered(EEntity value) throws SdaiException {
		removeUnordered(value, null);
	}


/**
 * It is a specialization of 
 * {@link #setCurrentMember(SdaiIterator, Object, EDefined_type []) setCurrentMember(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void setCurrentMember(SdaiIterator iter, EEntity value) throws SdaiException {
		setCurrentMember(iter, value, null);
	}


/**
 * It is a specialization of 
 * {@link #addBefore(SdaiIterator, Object, EDefined_type []) addBefore(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addBefore(SdaiIterator iter, EEntity value) throws SdaiException {
		addBefore(iter, value, null);
	}


/**
 * It is a specialization of 
 * {@link #addAfter(SdaiIterator, Object, EDefined_type []) addAfter(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addAfter(SdaiIterator iter, EEntity value) throws SdaiException {
		addAfter(iter, value, null);
	}


/**
 * It is {@link #getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 */
	public EEntity getCurrentMemberEntity(jsdai.lang.SdaiIterator iter) throws jsdai.lang.SdaiException {
		return (EEntity)getCurrentMemberObject(iter);
	}

	/**
	 * Returns true if this aggregate equals to other aggregate.
	 * <b>Notice:</b> current implementation returns reliable result only
	 * for simulated aggregates obtained through <code>getInstances</code> methods.
	 *
	 * @param obj the object to compare to.
	 * @return true if other object is of <code>CAggregate</code> type and is equal to this aggregate.
     * @since 3.6.0
	 */
	public boolean equals(Object obj) {
		if(obj instanceof CAggregate) {
			CAggregate other = (CAggregate)obj;
			int length = myLength + 1;
			int otherLength = other.myLength + 1;
			boolean isEquals = new EqualsBuilder()
				.append(owner, other.owner)
				.append(length, otherLength)
				.append(myType, other.myType)
				.isEquals();
			if(isEquals) {
				if(myData != other.myData) {
					if (myData == null || other.myData == null) {
						return false;
					}
					if(myData instanceof Object []) {
						if(!(other.myData instanceof Object [])) {
							return false;
						}

						Object [] myDataA = (Object [])myData;
						Object [] myDataB = (Object [])other.myData;
						if(myDataA.length < length) {
							length = myDataA.length;
						}
						if(myDataB.length < length) {
							length = myDataB.length;
						}
						for(int i = 0; i < length; ++i) {
							if(myDataA[i] != myDataB[i]) {
								if (myDataA[i] == null || myDataB[i] == null) {
									return false;
								}
								Class lhsClass = myDataA[i].getClass();
								if (!lhsClass.isInstance(myDataB[i])) {
									return false;
								}
								if(myDataA[i] != myDataB[i]) {
									return false;
								}
							}
						}
					} else {
						if(myData != other.myData) {
							return false;
						}
					}
				}
			}
			return isEquals;
		}
		return false;
	}


	boolean checkAggr() throws SdaiException {
		int ln = myLength;
		if (myLength < 0) {
			ln = -myLength;
		}
		int i;

		Object [] myDataA;
		if (!(myType == null || myType.express_type == DataType.LIST)) {
			return false;
		}
		if (myData == null) {
			return false;
		}
		if (ln == 1) {
			if (myData == null) {
//System.out.println("CAggregate ??????????? 1  member: " + myData);
				return true;
			}
		} else if (ln == 2) {
			myDataA = (Object [])myData;
			for (i = 0; i < 2; i++) {
				if (myDataA[i] == null) {
//System.out.println("CAggregate ??????????? 2  member: " + myDataA[i]);
					return true;
				}
			}
		} else {
			ListElement element;
			if (ln <= SHORT_AGGR) {
				element = (ListElement)myData;
			} else {
				myDataA = (Object [])myData;
				element = (ListElement)myDataA[0];
			}
			i = 0;
			while (element != null) {
				if (element.object == null) {
/*System.out.println("CAggregate ??????????? 3  member: " + element.object + "  i: " + i);
ListElement lel;
if (ln <= SHORT_AGGR) lel = (ListElement)myData;
else {myDataA = (Object [])myData;lel = (ListElement)myDataA[0];}
while (lel != null) {
System.out.println("CAggregate +++++++++++++  lel: " + lel.object);
if (lel == element) break;
lel = lel.next;
}*/
					return true;
				}
				element = element.next;
				i++;
			}
		}
		return false;
	}


	/**
	 * Returns a hash code value of this aggregate.
	 * <b>Notice:</b> current implementation returns reliable result only
	 * for simulated aggregates obtained through <code>getInstances</code> methods.
	 *
	 * @return the hash code value
     * @since 3.6.0
	 */
	public int hashCode() {
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		int hashCode =
			(owner == null ? 0 : owner.hashCode()) ^ myLength ^ (myType == null ? 0 : myType.hashCode());
		if(myData != null) {
			if(myData instanceof Object []) {
				Object [] myDataA = (Object [])myData;
				int length = myLength + 1;
				if(myDataA.length < length) {
					length = myDataA.length;
				}
				for(int i = 0; i < length; ++i) {
					if(myDataA[i] != null) {
						hashCode += myDataA[i].getClass().hashCode() ^ myDataA[i].hashCode();
					}
				}
			} else {
				hashCode += myData.getClass().hashCode() ^ myData.hashCode();
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
		return hashCode;
	}
}
