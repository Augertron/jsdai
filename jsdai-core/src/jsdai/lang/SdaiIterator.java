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
import jsdai.client.*;

/**
 * The instances of the class <code>SdaiIterator</code> are used to iterate
 * through the members of an <code>Aggregate</code>. For aggregates of the
 * EXPRESS type SET or BAG, the only possible access procedure is to start
 * from the beginning of the aggregate and go through all aggregate members 
 * using {@link #next next} method. For aggregates of the EXPRESS type LIST
 * or ARRAY, examination of their members is possible in both directions: 
 * forward, using methods {@link #beginning beginning} and {@link #next next}
 * and backward, using methods {@link #end end} and {@link #previous previous}.
 * @see "ISO 10303-22::9.4.1 iterator"
 */
public class SdaiIterator {

/**
	The aggregate with which this iterator is associated.
*/
	Object myAggregate;

/**
	Usually, the index to the element referenced by the iterator. 
	In the cases when an aggregate (in fact, a set consisting of entity 
	instances) is simulated, <code>myIndex</code> points to an instance of the 
	specified entity data type, or more precisely, to some element of a row in 
	the 2-dimensional array (matrix) <code>instances_sim</code> of the 
	considered <code>SdaiModel</code>. 
*/
	int myIndex;

/**
	An auxiliary index used in the cases when an aggregate (in fact, a set 
	consisting of entity instances) is simulated. The following cases are 
	possible.
	  1. The set consists of all instances of an <code>SdaiModel</code>. 
	It is returned by <code>getInstances</code> method in class 
	<code>SdaiModel</code>. The set is represented by an aggregate in which 
	the first element of the array <code>myData</code> is an 
	<code>SdaiModel</code> whose instances are considered. In this case 
	<code>myOuterIndex</code> points to the row in the 2-dimensional array 
	(matrix) <code>instances_sim</code> of the considered <code>SdaiModel</code>.
	  2. The set consists of instances of a specified entity data type and all 
	its subtypes in an <code>SdaiModel</code>. It is returned by 
	<code>getInstances</code> method (one of two its variations with parameter 
	specifying an entity) in class <code>SdaiModel</code>. The set is 
	represented by an aggregate in which the array <code>myData</code> contains 
	two elements: the first is an <code>SdaiModel</code> containing instances, 
	whereas the second is the underlying schema of this model. In this case 
	<code>myOuterIndex</code>, if nonnegative, points to the element of the 
	array <code>subtypes</code> returned by <code>getSubtypes</code> method 
	of the class <code>SchemaDefinition</code> invoked for the specified entity. 
	In this array all subtypes of this entity are listed. In fact, the mentioned 
	set consists of all instances of the given entity and all instances of each 
	subtype in <code>subtypes</code> array. The value -1 of 
	<code>myOuterIndex</code> means that iterator is positioned on instances 
	of entity itself but not those of its subtypes.
	  3. The set consists of all instances of a specified entity data type and 
	all instances of all its subtypes collected going through all started 
	<code>SdaiModel</code>s in an <code>ASdaiModel</code>. It is returned 
	by <code>getInstances</code> method (one of two its variations) in class 
	<code>ASdaiModel</code>. The set is represented by an aggregate in which 
	the array <code>myData</code> contains two elements: the first coincides 
	with the corresponding element in the list <code>ASdaiModel</code> from 
	which this set was constructed, whereas the second represents an entity 
	(either definition or class) instances of which are collected.
	In this case <code>myOuterIndex</code> has the same meaning as in the 
	case 2 above.
*/
	int myOuterIndex;

/**
	Gives the index of the element in the array <code>subtypes</code> returned 
	by <code>getSubtypes</code> method of the class <code>SchemaDefinition</code> 
	invoked for the specified entity. This information is used in the sets 
	returned by <code>getInstances</code> method (one of two its variations 
	with parameter specifying an entity) in class <code>SdaiModel</code> and by 
	<code>getInstances</code> method (one of two its variations) in class 
	<code>ASdaiModel</code>. The field is set with value in 
	<code>make_current_member</code> method. 
*/
	int subtypeIndex;

	int memberIndex;

	long id;

/**
	Usually, the element in the associated aggregate referenced by the iterator. 
	In the case of an aggregate (in fact, a set consisting of entity instances) 
	returned either by <code>getInstances</code> or by <code>getExactInstances</code> 
	method in class <code>ASdaiModel</code> the field <code>myElement</code> 
	points to an <code>SdaiModel</code> on which instances the iterator is 
	currently positioned.
*/
	Object myElement;

/**
	Has value 'true' if the iterator is positioned after the last element of 
	the associated aggregate.
*/
	boolean behind;

/**
	The type indicator for the aggregate associated with this iterator.
*/
	int AggregationType;

/**
	The type indicator for an aggregate which is different than aggregate 
	allowed to consist of numbers only one type (integer, double, logical, 
	boolean, enumeration) and which is a value, maybe through a chain of nested 
	aggregates, of an attribute of an entity instance. 
	For entity aggregate whose elements are only of integer (logical, boolean, 
	enumeration) type (respectively, double type) the indicator INTEGER_AGGR 
	(respectively, DOUBLE_AGGR) is used.
*/
	static final int ENTITY_AGGR = 1;

/**
	The type indicator for session aggregates: ASdaiRepository, ASchemaInstance, 
	ASdaiModel, and AEntityExtent.
*/
	static final int SESSION_AGGR = 2;

/**
	The type indicator for the SET of all instances of an <code>SdaiModel</code>. 
	It is returned by <code>getInstances</code> method in class 
	<code>SdaiModel</code>.
*/
	static final int ALL_INSTANCES = 3;

/**
	The type indicator for the SET of instances of an entity data type and all 
	its subtypes in an <code>SdaiModel</code>. It is returned by 
	<code>getInstances</code> method (one of two its variations with parameter 
	specifying an entity) in class <code>SdaiModel</code>.
*/
	static final int INSTANCES = 4;

/**
	The type indicator for the SET of instances of an entity data type (but not 
	its subtypes) in an <code>SdaiModel</code>. This set is returned by 
	<code>getExactInstances</code> method (one of two its variations) in class 
	<code>SdaiModel</code>.
*/
	static final int INSTANCES_EXACT = 5;

/**
	The type indicator for the SET consisting of all instances (within an 
	<code>SdaiModel</code>) of those entities which contain specified simple 
	entity data types. This set is returned by <code>getInstances</code> method 
	(one of two its variations in class <code>SdaiModel</code>) with parameter 
	being an array of entity definitions.
*/
	static final int INSTANCES_COMPL = 6;

/**
	The type indicator for an aggregate whose elements are only of integer 
	(logical, boolean, enumeration) type and which is a value, maybe through a 
	chain of nested aggregates, of an attribute of an entity instance. 
	For general entity aggregate the type indicator ENTITY_AGGR is used.
*/
	static final int INTEGER_AGGR = 7;

/**
	The type indicator for an aggregate whose elements are only of double type 
	and which is a value, maybe through a chain of nested aggregates, of an 
	attribute of an entity instance. 
	For general entity aggregate the type indicator ENTITY_AGGR is used.
*/
	static final int DOUBLE_AGGR = 8;

	static final int DOUBLE3_AGGR = 9;

/**
	The type indicator for the SET of instances of an entity data type and all 
	instances of all its subtypes collected going through all started 
	<code>SdaiModel</code>s in an <code>ASdaiModel</code>. It is returned 
	by <code>getInstances</code> method (one of two its variations) in class 
	<code>ASdaiModel</code>.
*/
	static final int ASDAIMODEL_INST = 10;

/**
	The type indicator for the SET of instances of an entity data type (but not 
	instances of its subtypes) collected going through all started 
	<code>SdaiModel</code>s in an <code>ASdaiModel</code>. This set is returned 
	by <code>getExactInstances</code> method (one of two its variations) in 
	class <code>ASdaiModel</code>.
*/
	static final int ASDAIMODEL_INST_EXACT = 11;

	static final int SESSION_AGGR_MODELS = 12;

	static final int SESSION_AGGR_SCHEMAS = 13;

	static final int ASDAIMODEL_INST_ALL = 14;




/**
	Used to create an iterator when the type indicator of an aggregate to be 
	associated is one of the following: ENTITY_AGGR, INTEGER_AGGR, DOUBLE_AGGR, 
	SESSION_AGGR, INSTANCES_COMPL.
*/
	SdaiIterator(Object provided_aggregate) throws SdaiException {
		myAggregate = provided_aggregate;
		myIndex = -1;
		behind = false;
		Class cls = provided_aggregate.getClass();
//System.out.println(" SdaiIterator   provided_aggregate: " + cls.getName());
		if (CAggregate.class.isAssignableFrom(cls)) {
			CAggregate agg = (CAggregate)provided_aggregate;
			if (agg.myType != null && agg.myType.shift == SdaiSession.INST_COMPL_AGGR) {
				AggregationType = INSTANCES_COMPL;
			} else {
				AggregationType = ENTITY_AGGR;
			}
		} else if (SessionAggregate.class.isAssignableFrom(cls)){
			if (provided_aggregate instanceof ASdaiModel) {
				ASdaiModel aggr_mod = (ASdaiModel)provided_aggregate;
				Object owning_obj = aggr_mod.getOwner();
				if (owning_obj instanceof SdaiRepository && 
						((SdaiRepository)owning_obj).isRemote()) {
					AggregationType = SESSION_AGGR_MODELS;
				} else {
					AggregationType = SESSION_AGGR;
				}
			} else if (provided_aggregate instanceof ASchemaInstance) {
				ASchemaInstance aggr_sch = (ASchemaInstance)provided_aggregate;
				Object owning_obj = aggr_sch.getOwner();
				if (owning_obj instanceof SdaiRepository && 
						((SdaiRepository)owning_obj).isRemote()) {
					AggregationType = SESSION_AGGR_SCHEMAS;
				} else {
					AggregationType = SESSION_AGGR;
				}
			} else {
				AggregationType = SESSION_AGGR;
			}
		} else if (provided_aggregate instanceof A_integerPrimitive) {
			AggregationType = INTEGER_AGGR;
		} else if (provided_aggregate instanceof A_double3) {
			AggregationType = DOUBLE3_AGGR;
			myIndex = A_double3.NO_ELEMENT_BEFORE;
		} else if (provided_aggregate instanceof A_double) {
			AggregationType = DOUBLE_AGGR;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


/**
	Used to create an iterator when the type indicator of an aggregate to be 
	associated is one of the following: ALL_INSTANCES, INSTANCES, INSTANCES_EXACT.
*/
	SdaiIterator(Object provided_aggregate, int provided_outer_index)
			throws SdaiException {
		myAggregate = provided_aggregate;
		myOuterIndex = provided_outer_index;
		CAggregate agg = (CAggregate)provided_aggregate;
		if (agg.myType.shift == SdaiSession.INST_AGGR) {
			myIndex = -1;
			subtypeIndex = -1;
			AggregationType = INSTANCES;
		} else if (agg.myType.shift == SdaiSession.INST_EXACT_AGGR) {
			myIndex = -1;
			AggregationType = INSTANCES_EXACT;
		} else if (agg.myType.shift == SdaiSession.ALL_INST_AGGR) {
			myIndex = 0;
			AggregationType = ALL_INSTANCES;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}


/**
	Used to create an iterator when the type indicator of an aggregate to be 
	associated is either ASDAIMODEL_INST or ASDAIMODEL_INST_EXACT.
*/
	SdaiIterator(Object provided_aggregate, Object first_model, int provided_outer_index)
			throws SdaiException {
		myAggregate = provided_aggregate;
		myElement = first_model;
		myOuterIndex = provided_outer_index;
		myIndex = -1;
		if (((CAggregate)provided_aggregate).myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
			subtypeIndex = 0;
			AggregationType = ASDAIMODEL_INST_ALL;
		} else if (((CAggregate)provided_aggregate).myType.shift == SdaiSession.ASDAIMODEL_INST) {
			subtypeIndex = -1;
			memberIndex = 0;
			AggregationType = ASDAIMODEL_INST;
		} else if (((CAggregate)provided_aggregate).myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
			memberIndex = 0;
			AggregationType = ASDAIMODEL_INST_EXACT;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
	}



/**
 * Positions this <code>SdaiIterator</code> before the first member of the
 * aggregate to which this <code>SdaiIterator</code> is attached.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @see "ISO 10303-22::10.12.5 Beginning"
 */
	public void beginning() throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		SdaiModel owning_model;
		ASdaiModel amod;
		Object [] myDataA;
		switch (AggregationType) {
			case ENTITY_AGGR:
				CAggregate agg = (CAggregate)myAggregate;
				if (agg.myType != null && agg.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					owning_model = agg.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (agg.myType == null || agg.myType.express_type == DataType.LIST) {
					myElement = null;
					behind = false;
				}
				myIndex = -1;
				break;
			case SESSION_AGGR:
				SessionAggregate agg_dic = (SessionAggregate)myAggregate;
				if (agg_dic.myType != null) {
					if (agg_dic.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
//					if (SdaiSession.session == null) {
					if (!agg_dic.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				}
				if (agg_dic.myType == null || agg_dic.myType.express_type == DataType.LIST) {
					myElement = null;
					behind = false;
				} else {
					myIndex = -1;
				}
				break;
			case SESSION_AGGR_MODELS:
				Object owning_obj = ((ASdaiModel)myAggregate).getOwner();
				if (owning_obj == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				if (!(owning_obj instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				SdaiRepository repo = (SdaiRepository)owning_obj;
				if (repo.session == null) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (!repo.isRemote()) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				myElement = null;
				myIndex = -1;
				behind = false;
				break;
			case SESSION_AGGR_SCHEMAS:
				owning_obj = ((ASchemaInstance)myAggregate).getOwner();
				if (owning_obj == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				if (!(owning_obj instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				repo = (SdaiRepository)owning_obj;
				if (repo.session == null) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (!repo.isRemote()) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				myElement = null;
				myIndex = -1;
				behind = false;
				break;
			case ALL_INSTANCES:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				SdaiModel mod = (SdaiModel)myDataA[0];
				if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, mod);
				}
				myOuterIndex = -1;
				myIndex = 0;
				break;
			case INSTANCES:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				mod = (SdaiModel)myDataA[0];
				if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, mod);
				}
				myOuterIndex = -1;
				myIndex = -1;
				break;
			case INSTANCES_EXACT:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				mod = (SdaiModel)myDataA[0];
				if ((mod.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, mod);
				}
				myIndex = -1;
				break;
			case INSTANCES_COMPL:
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				owning_model = (SdaiModel)myDataA[agg.myLength];
				if (owning_model == null) {
					throw new SdaiException(SdaiException.MO_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				myIndex = -1;
				break;
			case ASDAIMODEL_INST_ALL:
				myOuterIndex = -1;
				myIndex = -1;
				agg = (CAggregate)myAggregate;
				amod = (ASdaiModel)agg.myData;
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					myElement = amod.myData[0];
				} else {
					subtypeIndex = 0;
				}
				break;
			case ASDAIMODEL_INST:
				myOuterIndex = -1;
				myIndex = -1;
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				amod = (ASdaiModel)myDataA[0];
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					myElement = amod.myData[0];
				} else {
					memberIndex = 0;
				}
				break;
			case ASDAIMODEL_INST_EXACT:
				myIndex = -1;
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				amod = (ASdaiModel)myDataA[0];
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					myElement = amod.myData[0];
				} else {
					memberIndex = 0;
				}
				break;
			case INTEGER_AGGR:
			case DOUBLE_AGGR:
				A_primitive agg_prim = (A_primitive)myAggregate;
				if (agg_prim.myType == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_prim);
				}
				if (agg_prim.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_prim.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					owning_model = agg_prim.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (agg_prim.myType.express_type == DataType.LIST) {
					myElement = null;
					behind = false;
				} else {
					myIndex = -1;
				}
				break;
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				if (agg_double3.myType == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				owning_model = agg_double3.getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				myIndex = A_double3.NO_ELEMENT_BEFORE;
				break;
		}
//		} // syncObject
	}


/**
 * Positions this <code>SdaiIterator</code> to reference the next member of the
 * aggregate to which this <code>SdaiIterator</code> is attached.
 * If the iterator currently references the last element, or is at the end of 
 * the related aggregate and thus has no current member, or this aggregate is
 * empty, then the iterator is positioned to the end of the aggregate
 * without referencing any aggregate member.
 * This method is valid for aggregates of any EXPRESS type: ARRAY, LIST, SET, BAG.
 * @return <code>true</code> if iterator has a new current member, 
 * <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException MO_NEXS, SDAI-model does not exist.
 * @see #previous
 * @see "ISO 10303-22::10.12.6 Next"
 */
	public boolean next() throws SdaiException {
		SdaiModel owning_model;
		ASdaiModel amod;
		SdaiTransaction trans;
		int new_ind;
		int count_inst;
		boolean res;
		boolean del;
//		synchronized (SdaiCommon.syncObject) {
		switch (AggregationType) {
			case ENTITY_AGGR:
				CAggregate agg = (CAggregate)myAggregate;
				if (agg.myType != null && agg.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					owning_model = agg.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				Object [] myDataA;
				int shift = 1;
				if (agg.myType != null && agg.myType.select != null && agg.myType.select.is_mixed > 0) {
					shift = 2;
				}
				if (agg.myType == null || agg.myType.express_type == DataType.LIST) {
					if (agg.myLength <= 0 || (myElement != null && myIndex >= agg.myLength-1)) {
						myElement = null;
						behind = true;
						return false;
					}
					if (myElement == null && behind) {
						return false;
					}
					if (shift <= 1) {
						if (myElement == null) {
							if (agg.myLength == 1) {
								myElement = agg.myData;
							} else if (agg.myLength == 2) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							} else if (agg.myLength <= CAggregate.SHORT_AGGR) {
								myElement = agg.myData;
							} else {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							}
							myIndex = 0;
						} else {
							if (agg.myLength == 2) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[1];
							} else {
								myElement = ((ListElement)myElement).next;
							}
							myIndex++;
						}
						return true;
					} else {
						if (myElement == null) {
							if (agg.myLength == 1) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							} else if (agg.myLength <= CAggregate.SHORT_AGGR_SELECT) {
								myElement = agg.myData;
							} else {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							}
							myIndex = 0;
						} else {
							myElement = ((ListElement)myElement).next.next;
							myIndex++;
						}
						return true;
					}
				} else {
					if (agg.myType.express_type == DataType.ARRAY && agg.myData == null) {
						EBound bound = ((CArray_type)agg.myType).getLower_index(null);
						EBound upper_bound = ((CArray_type)agg.myType).getUpper_index(null);
						agg.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
					}
					if (myIndex >= agg.myLength) {
						return false;
					}
					myIndex++;
					if (myIndex >= agg.myLength) {
						return false;
					} else {
						return true;
					}
				}
			case SESSION_AGGR:
				SessionAggregate agg_dic = (SessionAggregate)myAggregate;
				if (agg_dic.myType != null) {
					if (agg_dic.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
//					if (SdaiSession.session == null) {
					if (!agg_dic.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				}
				if (agg_dic.myType == null || agg_dic.myType.express_type == DataType.LIST) {
					if (agg_dic.myLength <= 0) {
						myElement = null;
						behind = true;
						return false;
					}
					if (myElement == null && behind) {
						return false;
					}	else if (myElement == null) {
						myElement = agg_dic.myData[0];
						myIndex = 0;
						return true;
					} else {
						myElement = ((ListElement)myElement).next;
						if (myElement == null) {
							behind = true;
							return false;
						} else {
							myIndex++;
							return true;
						}
					}
				} else {
					if (agg_dic.myType.express_type == DataType.ARRAY && agg_dic.myData == null) {
						EBound bound = ((CArray_type)agg_dic.myType).getLower_index(null);
						EBound upper_bound = ((CArray_type)agg_dic.myType).getUpper_index(null);
						agg_dic.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
					}
					if (myIndex >= agg_dic.myLength) {
						return false;
					}
					myIndex++;
					int delta = 0;
					if (myAggregate instanceof AEntityExtent &&
							agg_dic.myType != null && agg_dic.myType.shift == -1) {
						delta = 1;
					}
					if (myIndex >= agg_dic.myLength - delta) {
						return false;
					} else {
						return true;
					}
				}
			case SESSION_AGGR_MODELS:
				Object owning_obj = ((ASdaiModel)myAggregate).getOwner();
				if (owning_obj == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				if (!(owning_obj instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				SdaiRepository repo = (SdaiRepository)owning_obj;
				if (repo.session == null) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (!repo.isRemote()) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (myIndex >= 0) {
					if (behind) {
						return false;
					}
					res = get_next_model(repo);
					if (!res) {
						behind = true;
						return false;
					}
					return true;
				}
				if (myElement == null && behind) {
					return false;
				}
				trans = repo.session.active_transaction;
				if (trans == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				while (true) {
					del = false;
//System.out.println("  SdaiIterator ============    myElement: " + myElement + 
//"   repo: " + repo);
					myElement = repo.getNextModel(myElement);
//System.out.println("  SdaiIterator ============  AFTER  myElement: " + myElement);
/*SdaiModelRemote mrem = (SdaiModelRemote)myElement;
SdaiModelHeader mhead;
if (mrem != null) {
mhead = mrem.getHeader();
System.out.println("  SdaiIterator ============    myElement: " + myElement + 
"   repo: " + repo.name + "   model name: " + mhead.name);
}*/
					if (myElement == null) {
						break;
					}
					for (int i = 0; i < trans.stack_length; i++) {
						if (trans.stack_del_mods_rep[i] == repo && trans.stack_del_mods[i].checkModel(myElement)) {
							del = true;
							break;
						}
					}
					if (!del) {
						break;
					}
				}
				if (myElement == null) {
					if (repo.unresolved_mod_count > 0) {
						res = get_next_model(repo);
						if (res) {
//System.out.println("  SdaiIterator ============  TRUE returned  res: " + res);
							return true;
						}
					}
					behind = true;
//System.out.println("  SdaiIterator ============  FALSE returned  behind: " + behind);
					return false;
				}
				return true;
			case SESSION_AGGR_SCHEMAS:
				owning_obj = ((ASchemaInstance)myAggregate).getOwner();
				if (owning_obj == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				if (!(owning_obj instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				repo = (SdaiRepository)owning_obj;
				if (repo.session == null) {
					throw new SdaiException(SdaiException.SS_NOPN);
				}
				if (!repo.isRemote()) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				if (myIndex >= 0) {
					if (behind) {
						return false;
					}
					res = get_next_schema_inst(repo);
					if (!res) {
						behind = true;
						return false;
					}
					return true;
				}
				if (myElement == null && behind) {
					return false;
				}
				trans = repo.session.active_transaction;
				if (trans == null) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				while (true) {
					del = false;
					myElement = repo.getNextSchInstance(myElement);
					if (myElement == null) {
						break;
					}
					for (int i = 0; i < trans.stack_length_sch_insts; i++) {
						if (trans.stack_del_sch_insts[i].checkSchInstance(myElement)) {
							del = true;
							break;
						}
					}
					if (!del) {
						break;
					}
				}
				if (myElement == null) {
					if (repo.unresolved_sch_count > 0) {
						res = get_next_schema_inst(repo);
						if (res) {
							return true;
						}
					}
					behind = true;
					return false;
				}
				return true;
			case ALL_INSTANCES:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				owning_model = (SdaiModel)myDataA[0];
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				if (myOuterIndex >= owning_model.lengths.length) {
					return false;
				}
				if (myOuterIndex < 0) {
					myOuterIndex = 0;
				} else {
					if (myIndex < owning_model.lengths[myOuterIndex]) {
						if (owning_model.instances_sim[myOuterIndex][myIndex].instance_identifier == id) {
							myIndex++;
						}
					}
				}
				while (myIndex >= owning_model.lengths[myOuterIndex]) {
					myOuterIndex++;
					if (myOuterIndex >= owning_model.lengths.length) {
						return false;
					}
					if (owning_model.lengths[myOuterIndex] > 0) {
						myIndex = 0;
						id=owning_model.instances_sim[myOuterIndex][0].instance_identifier;
						return true;
					}
				}
				id=owning_model.instances_sim[myOuterIndex][myIndex].instance_identifier;
				return true;
			case INSTANCES:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				owning_model = (SdaiModel)myDataA[0];
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				int ind_to_ent = ((CAggregate)myAggregate).myLength;
				CSchema_definition schema =
					(CSchema_definition)myDataA[1];
				int index;
				if (myOuterIndex == -1) {
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
							owning_model.repository != SdaiSession.systemRepository) {
						index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[ind_to_ent]);
					} else {
						index = ind_to_ent;
					}
				} else {
					if (subtypeIndex < 0) {
						return false;
					} else {
						if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								owning_model.repository != SdaiSession.systemRepository) {
							index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[subtypeIndex]);
						} else {
							index = subtypeIndex;
						}
					}
				}
				new_ind = myIndex;
				if (index >= 0) {
					count_inst = owning_model.lengths[index];
				} else {
					count_inst = 0;
				}
				if (myIndex < 0) {
					new_ind++;
				} else if (myIndex < count_inst) {
					if (owning_model.instances_sim[index][myIndex].instance_identifier == id) {
						new_ind++;
					}
				}
//				if (myIndex + 1 >= owning_model.lengths[index]) {
				if (new_ind >= count_inst) {
					if (schema == null) {
						return false;
					} else {
						int subtypes [] = schema.getSubtypes(ind_to_ent);
						if	(myOuterIndex + 1 < subtypes.length) {
//							return make_current_member(owning_model, subtypes);
							res = make_current_member(owning_model, subtypes);
							if (res) {
								int subtypeIndexCorrected;
								if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
										owning_model.repository != SdaiSession.systemRepository) {
									subtypeIndexCorrected = 
										owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[subtypeIndex]);
								} else {
									subtypeIndexCorrected = subtypeIndex;
								}
								id=owning_model.instances_sim[subtypeIndexCorrected][myIndex].instance_identifier;
							}
							return res;
						} else {
							if (myIndex + 1 == count_inst) {
								myIndex++;
							}
							return false;
						}
					}
				} else {
					myIndex = new_ind;
					id=owning_model.instances_sim[index][myIndex].instance_identifier;
					return true;
				}
			case INSTANCES_EXACT:
				myDataA = (Object [])((CAggregate)myAggregate).myData;
				owning_model = (SdaiModel)myDataA[0];
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				ind_to_ent = ((CAggregate)myAggregate).myLength;
				int ind_to_ent_corrected;
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						owning_model.repository != SdaiSession.systemRepository) {
					ind_to_ent_corrected = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[ind_to_ent]);
				} else {
					ind_to_ent_corrected = ind_to_ent;
				}
				new_ind = myIndex;
				if (ind_to_ent_corrected >= 0) {
					count_inst = owning_model.lengths[ind_to_ent_corrected];
				} else {
					count_inst = 0;
				}
				if (myIndex < 0) {
					new_ind++;
				} else if (myIndex < count_inst) {
					if (owning_model.instances_sim[ind_to_ent_corrected][myIndex].instance_identifier == id) {
						new_ind++;
					}
				}
//				if (myIndex + 1 >= owning_model.lengths[ind_to_ent]) {
				if (new_ind >= count_inst) {
					if (myIndex + 1 == count_inst) {
						myIndex++;
					}
					return false;
				} else {
					myIndex = new_ind;
					id=owning_model.instances_sim[ind_to_ent_corrected][myIndex].instance_identifier;
					return true;
				}
			case INSTANCES_COMPL:
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				owning_model = (SdaiModel)myDataA[agg.myLength];
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				if (myIndex >= agg.myLength) {
					return false;
				}
				myIndex++;
				if (myIndex >= agg.myLength) {
					return false;
				} else {
					return true;
				}
			case ASDAIMODEL_INST_ALL:
				agg = (CAggregate)myAggregate;
				amod = (ASdaiModel)agg.myData;
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					owning_model = getModelAllList(false);
					while (owning_model != null) {
						if (myOuterIndex < 0) {
							myOuterIndex = 0;
							myIndex = 0;
						} else {
							myIndex++;
						}
						if (myIndex < owning_model.lengths[myOuterIndex]) {
							return true;
						}
						while (myIndex >= owning_model.lengths[myOuterIndex]) {
							myOuterIndex++;
							if (myOuterIndex >= owning_model.lengths.length) {
								owning_model = getModelAllList(true);
								break;
							}
							myIndex = 0;
							if (owning_model.lengths[myOuterIndex] > 0) {
								return true;
							}
						}
					}
					return false;
				} else {
					owning_model = getModelAllSet(amod, false);
					while (owning_model != null) {
						if (myOuterIndex < 0) {
							myOuterIndex = 0;
							myIndex = 0;
						} else {
							myIndex++;
						}
						if (myIndex < owning_model.lengths[myOuterIndex]) {
							return true;
						}
						while (myIndex >= owning_model.lengths[myOuterIndex]) {
							myOuterIndex++;
							if (myOuterIndex >= owning_model.lengths.length) {
								owning_model = getModelAllSet(amod, true);
								break;
							}
							myIndex = 0;
							if (owning_model.lengths[myOuterIndex] > 0) {
								return true;
							}
						}
					}
					return false;
				}
			case ASDAIMODEL_INST:
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				amod = (ASdaiModel)myDataA[0];
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					owning_model = getModelList(false);
					while (owning_model != null) {
						if (myDataA[1] == owning_model.extent_type) {
							ind_to_ent = owning_model.extent_index;
						} else if (myDataA[1] instanceof CEntity_definition) {
							ind_to_ent = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
							if (ind_to_ent < 0) {
								owning_model = getModelList(true);
								continue;
							}
						} else {
							ind_to_ent = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
							if (ind_to_ent < 0) {
								owning_model = getModelList(true);
								continue;
							}
						}
						schema = owning_model.underlying_schema;
						if (myOuterIndex == -1) {
							index = ind_to_ent;
						} else {
							if (subtypeIndex < 0) {
								return false;
							} else {
								index = subtypeIndex;
							}
						}
						if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								owning_model.repository != SdaiSession.systemRepository) {
							index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[index]);
						}
						if (index >= 0) {
							count_inst = owning_model.lengths[index];
						} else {
							count_inst = 0;
						}
						if (myIndex + 1 >= count_inst) {
							if (schema == null) {
								return false;
							}
							int subtypes [] = schema.getSubtypes(ind_to_ent);
							boolean member_found = make_current_member(owning_model, subtypes);
							if (member_found) {
//System.out.println(" SdaiIterator  member_found  return   myOuterIndex: " + myOuterIndex +
//"    myIndex: " + myIndex + "   subtypeIndex: " + subtypeIndex);
								return true;
							}
						} else {
							myIndex++;
//System.out.println(" SdaiIterator  myIndex++ return   myOuterIndex: " + myOuterIndex +
//"    myIndex: " + myIndex + "   subtypeIndex: " + subtypeIndex);
							return true;
						}
						owning_model = getModelList(true);
//System.out.println(" SdaiIterator  true model: " + owning_model.name +
//"   in repo: " + owning_model.repository.name);
					}
				} else {
					owning_model = getModelSet(amod, false);
					while (owning_model != null) {
						if (myDataA[1] == owning_model.extent_type) {
							ind_to_ent = owning_model.extent_index;
						} else if (myDataA[1] instanceof CEntity_definition) {
							ind_to_ent = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
							if (ind_to_ent < 0) {
								owning_model = getModelSet(amod, true);
								continue;
							}
						} else {
							ind_to_ent = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
							if (ind_to_ent < 0) {
								owning_model = getModelSet(amod, true);
								continue;
							}
						}
						schema = owning_model.underlying_schema;
						if (myOuterIndex == -1) {
							index = ind_to_ent;
						} else {
							if (subtypeIndex < 0) {
								return false;
							} else {
								index = subtypeIndex;
							}
						}
						if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								owning_model.repository != SdaiSession.systemRepository) {
							index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[index]);
						}
						if (index >= 0) {
							count_inst = owning_model.lengths[index];
						} else {
							count_inst = 0;
						}
						if (myIndex + 1 >= count_inst) {
							if (schema == null) {
								return false;
							}
							int subtypes [] = schema.getSubtypes(ind_to_ent);
							boolean member_found = make_current_member(owning_model, subtypes);
							if (member_found) {
								return true;
							}
						} else {
							myIndex++;
							return true;
						}
						owning_model = getModelSet(amod, true);
					}
				}
				return false;
			case ASDAIMODEL_INST_EXACT:
				agg = (CAggregate)myAggregate;
				myDataA = (Object [])agg.myData;
				amod = (ASdaiModel)myDataA[0];
				if (amod.myType == null || amod.myType.express_type == DataType.LIST) {
					owning_model = getModelExactList(false);
					while (owning_model != null) {
						if (myDataA[1] == owning_model.extent_type) {
							index = owning_model.extent_index;
						} else if (myDataA[1] instanceof CEntity_definition) {
							index = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
							if (index < 0) {
								owning_model = getModelExactList(true);
								continue;
							}
						} else {
							index = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
							if (index < 0) {
								owning_model = getModelExactList(true);
								continue;
							}
						}
						if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								owning_model.repository != SdaiSession.systemRepository) {
							index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[index]);
						}
						if (index >= 0) {
							count_inst = owning_model.lengths[index];
						} else {
							count_inst = 0;
						}
						if (myIndex + 1 < count_inst) {
							myIndex++;
							return true;
						}
						owning_model = getModelExactList(true);
					}
				} else {
					owning_model = getModelExactSet(amod, false);
					while (owning_model != null) {
						if (myDataA[1] == owning_model.extent_type) {
							index = owning_model.extent_index;
						} else if (myDataA[1] instanceof CEntity_definition) {
							index = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((CEntity_definition)myDataA[1]);
							if (index < 0) {
								owning_model = getModelExactSet(amod, true);
								continue;
							}
						} else {
							index = 
								owning_model.underlying_schema.owning_model.schemaData.findEntityExtentIndex((Class)myDataA[1]);
							if (index < 0) {
								owning_model = getModelExactSet(amod, true);
								continue;
							}
						}
						if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
								owning_model.repository != SdaiSession.systemRepository) {
							index = owning_model.find_entityRO(owning_model.dictionary.schemaData.entities[index]);
						}
						if (index >= 0) {
							count_inst = owning_model.lengths[index];
						} else {
							count_inst = 0;
						}
						if (myIndex + 1 < count_inst) {
							myIndex++;
							return true;
						}
						owning_model = getModelExactSet(amod, true);
					}
				}
				myIndex++;
				return false;
			case INTEGER_AGGR:
				A_integerPrimitive agg_int = (A_integerPrimitive)myAggregate;
				if (agg_int.myType == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_int);
				}
				if (agg_int.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_int.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					owning_model = agg_int.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (agg_int.myType.express_type == DataType.LIST) {
					if (agg_int.myLength <= 0) {
						myElement = null;
						behind = true;
						return false;
					}
					if (myElement == null && behind) {
						return false;
					}	else if (myElement == null) {
						ListElementInteger [] myDataList = (ListElementInteger [])agg_int.myData;
						myElement = myDataList[0];
						myIndex = 0;
						return true;
					} else {
						myElement = ((ListElementInteger)myElement).next;
						if (myElement == null) {
							behind = true;
							return false;
						} else {
							myIndex++;
							return true;
						}
					}
				} else {
					if (agg_int.myType.express_type == DataType.ARRAY && agg_int.myData == null) {
						EBound bound = ((CArray_type)agg_int.myType).getLower_index(null);
						EBound upper_bound = ((CArray_type)agg_int.myType).getUpper_index(null);
						if (agg_int instanceof A_integer) {
							agg_int.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1, Integer.MIN_VALUE);
						} else {
							agg_int.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1, 0);
						}
					}
					if (myIndex >= agg_int.myLength) {
						return false;
					}
					myIndex++;
					if (myIndex >= agg_int.myLength) {
						return false;
					} else {
						return true;
					}
				}
			case DOUBLE_AGGR:
				A_double agg_double = (A_double)myAggregate;
				if (agg_double.myType == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
				if (agg_double.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_double.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					owning_model = agg_double.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (agg_double.myType.express_type == DataType.LIST) {
					if (agg_double.myLength <= 0) {
						myElement = null;
						behind = true;
						return false;
					}
					if (myElement == null && behind) {
						return false;
					}	else if (myElement == null) {
						ListElementDouble [] myDataList = (ListElementDouble [])agg_double.myData;
						myElement = myDataList[0];
						myIndex = 0;
						return true;
					} else {
						myElement = ((ListElementDouble)myElement).next;
						if (myElement == null) {
							behind = true;
							return false;
						} else {
							myIndex++;
							return true;
						}
					}
				} else {
					if (agg_double.myType.express_type == DataType.ARRAY && agg_double.myData == null) {
						EBound bound = ((CArray_type)agg_double.myType).getLower_index(null);
						EBound upper_bound = ((CArray_type)agg_double.myType).getUpper_index(null);
						agg_double.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
					}
					if (myIndex >= agg_double.myLength) {
						return false;
					}
					myIndex++;
					if (myIndex >= agg_double.myLength) {
						return false;
					} else {
						return true;
					}
				}
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				if (agg_double3.myType == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				owning_model = agg_double3.getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				if (myIndex > agg_double3.myLength) {
					return false;
				}
				myIndex++;
				if (myIndex > agg_double3.myLength) {
					return false;
				} else {
					return true;
				}
		}
		return false;
//		} // syncObject
	}


	private boolean get_next_model(SdaiRepository repo) throws SdaiException {
		myIndex++;
		for (int i = myIndex; i < repo.models.myLength; i++) {
			SdaiModel model = (SdaiModel)repo.models.myData[i];
			if (!model.isRemote()) {
				myIndex = i;
				return true;
			}
		}
		return false;
	}


	private boolean get_next_schema_inst(SdaiRepository repo) throws SdaiException {
		myIndex++;
		for (int i = myIndex; i < repo.schemas.myLength; i++) {
			SchemaInstance sch = (SchemaInstance)repo.schemas.myData[i];
			if (!sch.isRemote()) {
				myIndex = i;
				return true;
			}
		}
		return false;
	}


/**
	Positions the iterator at the next element (provided the iterator is not at 
	the end of the associated aggregate) in the case of an aggregate having type 
	indicator either INSTANCES or ASDAIMODEL_INST. If the next element exists, 
	the method returns <code>true</code>. The first parameter specifies an 
	<code>SdaiModel</code> instances of which compose the aggregate. The 
	second parameter gives the array of subtypes of an entity type considered 
	(the aggregate consists of the instances of this type and all its subtypes).
	The current index in <code>subtypes</code> is stored as 
	<code>subtypeIndex</code>.
*/
	private boolean make_current_member(SdaiModel model, int [] subtypes) 
			throws SdaiException {
		boolean exit = false;
		while (!exit) {
			if (myOuterIndex + 1 < subtypes.length) {
				myOuterIndex++;
				subtypeIndex = subtypes[myOuterIndex];
				myIndex = -1;
				int index;
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.READ_ONLY && 
						model.repository != SdaiSession.systemRepository) {
					index = model.find_entityRO(model.dictionary.schemaData.entities[subtypeIndex]);
				} else {
					index = subtypeIndex;
				}
				int count;
				if (index >= 0) {
					count = model.lengths[index];
				} else {
					count = 0;
				}
				if (myIndex < count - 1) {
					myIndex++;
					return true;
				}
			} else {
				myIndex++;
				exit = true;
			}
		}
		return false;
	}


	private SdaiModel getModelAllList(boolean next) throws SdaiException {
		ListElement element = (ListElement)myElement;
		if (next) {
			element = element.next;
			myElement = element;
		}
		if (element == null) {
			return null;
		}
		SdaiModel model = (SdaiModel)element.object;
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			element = element.next;
			myElement = element;
			if (element == null) {
				return null;
			}
			model = (SdaiModel)element.object;
			next = true;
		}
		if (next) {
			myIndex = -1;
			myOuterIndex = -1;
		}
		return model;
	}


	private SdaiModel getModelAllSet(ASdaiModel amod, boolean next) throws SdaiException {
		if (next) {
			subtypeIndex++;
		}
		if (subtypeIndex >= ((CAggregate)myAggregate).myLength) {
			return null;
		}
		SdaiModel model = (SdaiModel)amod.myData[subtypeIndex];
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			subtypeIndex++;
			if (subtypeIndex >= ((CAggregate)myAggregate).myLength) {
				return null;
			}
			model = (SdaiModel)amod.myData[subtypeIndex];
			next = true;
		}
		if (next) {
			myIndex = -1;
			myOuterIndex = -1;
		}
		return model;
	}


/**
	Returns a started (either in read-only or read-write mode) 
	<code>SdaiModel</code> in which an entity instance next to the current 
	position of the iterator will be searched. This method is invoked for an 
	iterator whose associated aggregate has type indicator ASDAIMODEL_INST. 
	An <code>SdaiModel</code> is taken from the list of models defined by an 
	<code>ASdaiModel</code> whose method <code>getInstances</code> has created 
	the aggregate associated with the iterator. Only started (in any mode) 
	models are considered. If the parameter of the method has value 
	<code>true</code>, then a search for a started model begins from the model 
	which is next to that represented by the current value of the field 
	<code>myElement</code>.
*/
	private SdaiModel getModelList(boolean next) throws SdaiException {
		ListElement element = (ListElement)myElement;
		if (next) {
			element = element.next;
			myElement = element;
		}
		if (element == null) {
			return null;
		}
		SdaiModel model = (SdaiModel)element.object;
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			element = element.next;
			myElement = element;
			if (element == null) {
				return null;
			}
			model = (SdaiModel)element.object;
			next = true;
		}
		if (next) {
			myIndex = -1;
			myOuterIndex = -1;
		}
		if (model.underlying_schema == null) {
			model.getUnderlyingSchema();
		}
		return model;
	}


	private SdaiModel getModelSet(ASdaiModel amod, boolean next) throws SdaiException {
		if (next) {
			memberIndex++;
		}
		if (memberIndex >= ((CAggregate)myAggregate).myLength) {
			return null;
		}
		SdaiModel model = (SdaiModel)amod.myData[memberIndex];
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			memberIndex++;
			if (memberIndex >= ((CAggregate)myAggregate).myLength) {
				return null;
			}
			model = (SdaiModel)amod.myData[memberIndex];
			next = true;
		}
		if (next) {
			myIndex = -1;
			myOuterIndex = -1;
		}
		if (model.underlying_schema == null) {
			model.getUnderlyingSchema();
		}
		return model;
	}


/**
	Returns a started (either in read-only or read-write mode) 
	<code>SdaiModel</code> in which an entity instance next to the current 
	position of the iterator will be searched. This method is invoked for an 
	iterator whose associated aggregate has type indicator ASDAIMODEL_INST_EXACT. 
	An <code>SdaiModel</code> is taken from the list of models defined by an 
	<code>ASdaiModel</code> whose method <code>getExactInstances</code> has created 
	the aggregate associated with the iterator. Only started (in any mode) 
	models are considered. If the parameter of the method has value 
	<code>true</code>, then a search for a started model begins from the model 
	which is next to that represented by the current value of the field 
	<code>myElement</code>.
*/
	private SdaiModel getModelExactList(boolean next) throws SdaiException {
		ListElement element = (ListElement)myElement;
		if (next) {
			element = element.next;
			myElement = element;
		}
		if (element == null) {
			return null;
		}
		SdaiModel model = (SdaiModel)element.object;
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			element = element.next;
			myElement = element;
			if (element == null) {
				return null;
			}
			model = (SdaiModel)element.object;
			next = true;
		}
		if (next) {
			myIndex = -1;
		}
		return model;
	}


	private SdaiModel getModelExactSet(ASdaiModel amod, boolean next) throws SdaiException {
		if (next) {
			memberIndex++;
		}
		if (memberIndex >= ((CAggregate)myAggregate).myLength) {
			return null;
		}
		SdaiModel model = (SdaiModel)amod.myData[memberIndex];
		while ((model.mode & SdaiModel.MODE_MODE_MASK) <= 0) {
			memberIndex++;
			if (memberIndex >= ((CAggregate)myAggregate).myLength) {
				return null;
			}
			model = (SdaiModel)amod.myData[memberIndex];
			next = true;
		}
		if (next) {
			myIndex = -1;
		}
		return model;
	}


/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException EX_NSUP will be thrown if this method is invoked.
 * @return the value bound for the aggregate member at the position specified
 * by this <code>SdaiIterator</code>.
 * @throws SdaiException EX_NSUP, expression evaluation not supported.
 * @see "ISO 10303-22::10.12.8 Get value bound by iterator"
 */
	public int getValueBound() throws SdaiException {
		throw new SdaiException(SdaiException.EX_NSUP);
	}


/**
 * Removes the member referenced by this <code>SdaiIterator</code> from the
 * aggregate to which this <code>SdaiIterator</code> is attached.
 * If the specified member is an aggregate instance, then that aggregate
 * instance is deleted along with all nested aggregate instances contained
 * by it. After execution of this method the iterator is positioned as if
 * {@link #next next} method was applied. The method is valid for aggregates
 * of only EXPRESS types different than ARRAY.

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is ARRAY;
 * <li> AI_NSET if the aggregate to which this <code>SdaiIterator</code>
 * is attached is empty;
 * <li> FN_NAVL if the aggregate to which this <code>SdaiIterator</code>
 * is attached is read-only; such aggregates, for example, are in data
 * dictionary.
 * </ul>
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException AI_NSET, aggregate instance is empty.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @see #next
 * @see "ISO 10303-22::10.13.3 Remove current member"
 */
	public boolean remove() throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session;
		boolean has_member = true;
		switch (AggregationType) {
			case ENTITY_AGGR:
				Object [] myDataA;
				CAggregate agg = (CAggregate)myAggregate;
				AggregationType type = agg.myType;
				if (type != null && type.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				int i;
				if (myIndex == agg.myLength - 1) {
					has_member = false;
				}
				if (type == null || type.express_type == DataType.LIST) {
					if (agg.myLength <= 0) {
						throw new SdaiException(SdaiException.AI_NSET, agg);
					}
					if (myElement == null && (behind || agg.myLength == 0 || myIndex < 0)) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					int shift = 1;
					if (type != null && type.select != null && type.select.is_mixed > 0) {
						shift = 2;
					}
					ListElement element;
					int index;
					if (shift <= 1) {
						if (agg.myLength == 1) {
							if (type != null) {
								agg.removeFromInverseList(agg.myData);
							}
							agg.myData = null;
							myElement = null;
							behind = true;
						} else if (agg.myLength == 2) {
							myDataA = (Object [])agg.myData;
							if (myIndex == 0) {
								if (type != null) {
									agg.removeFromInverseList(myDataA[0]);
								}
								agg.myData = myDataA[1];
								myElement = agg.myData;
							} else {
								if (type != null) {
									agg.removeFromInverseList(myDataA[1]);
								}
								agg.myData = myDataA[0];
								myElement = null;
								behind = true;
							}
						} else if (agg.myLength <= CAggregate.SHORT_AGGR) {
							element = (ListElement)agg.myData;
							if (agg.myLength == 3) {
								myDataA = new Object[2];
								switch(myIndex) {
									case 0:
										if (type != null) {
											agg.removeFromInverseList(element.object);
										}
										myDataA[0] = element.next.object;
										myDataA[1] = element.next.next.object;
										myElement = myDataA[0];
										break;
									case 1:
										if (type != null) {
											agg.removeFromInverseList(element.next.object);
										}
										myDataA[0] = element.object;
										myDataA[1] = element.next.next.object;
										myElement = myDataA[1];
										break;
									case 2:
										if (type != null) {
											agg.removeFromInverseList(element.next.next.object);
										}
										myDataA[0] = element.object;
										myDataA[1] = element.next.object;
										myElement = null;
										behind = true;
										break;
								}
								agg.myData = myDataA;
							} else {
								if (myIndex == 0) {
									if (type != null) {
										agg.removeFromInverseList(element.object);
									}
									agg.myData = element.next;
									myElement = agg.myData;
								} else {
									index = myIndex;
									while (--index > 0) {
										element = element.next;
									}
									if (type != null) {
										agg.removeFromInverseList(element.next.object);
									}
									if (myIndex == agg.myLength - 1) {
										element.next = null;
										myElement = null;
										behind = true;
									} else {
										element.next = element.next.next;
										myElement = element.next;
									}
								}
							}
						} else {
							myDataA = (Object [])agg.myData;
							element = (ListElement)myDataA[0];
							if (myIndex == 0) {
								if (type != null) {
									agg.removeFromInverseList(element.object);
								}
								myDataA[0] = element.next;
								myElement = myDataA[0];
							}	else {
								index = myIndex;
								while (--index > 0) {
									element = element.next;
								}
								if (type != null) {
									agg.removeFromInverseList(element.next.object);
								}
								if (myIndex == agg.myLength - 1) {
									element.next = null;
									myDataA[1] = element;
									myElement = null;
									behind = true;
								} else {
									element.next = element.next.next;
									myElement = element.next;
								}
							}
							if (agg.myLength == CAggregate.SHORT_AGGR + 1) {
								agg.myData = myDataA[0];
							} else {
								agg.myData = myDataA;
							}
						}
					} else {
						ListElement before_element;
						if (agg.myLength == 1) {
							myDataA = (Object [])agg.myData;
							if (type != null) {
								agg.removeFromInverseList(myDataA[0]);
							}
							agg.myData = null;
							myElement = null;
							behind = true;
						} else if (agg.myLength <= CAggregate.SHORT_AGGR_SELECT) {
							element = (ListElement)agg.myData;
							if (agg.myLength == 2) {
								myDataA = new Object[2];
								if (myIndex == 0) {
									if (type != null) {
										agg.removeFromInverseList(element.object);
									}
									myDataA[0] = element.next.next.object;
									myDataA[1] = element.next.next.next.object;
									myElement = myDataA[0];
								} else {
									if (type != null) {
										agg.removeFromInverseList(element.next.next.object);
									}
									myDataA[0] = element.object;
									myDataA[1] = element.next.object;
									myElement = null;
									behind = true;
								}
								agg.myData = myDataA;
							} else {
								if (myIndex == 0) {
									if (type != null) {
										agg.removeFromInverseList(element.object);
									}
									agg.myData = element.next.next;
									myElement = agg.myData;
								} else {
									index = myIndex * 2;
									while (--index > 0) {
										element = element.next;
									}
									before_element = element;
									element = element.next.next;
									if (type != null) {
										agg.removeFromInverseList(before_element.next.object);
									}
									if (myIndex == agg.myLength - 1) {
										before_element.next = null;
										myElement = null;
										behind = true;
									} else {
										before_element.next = element.next;
										myElement = element.next;
									}
								}
							}
						} else {
							myDataA = (Object [])agg.myData;
							element = (ListElement)myDataA[0];
							if (myIndex == 0) {
								if (type != null) {
									agg.removeFromInverseList(element.object);
								}
								myDataA[0] = element.next.next;
								myElement = myDataA[0];
							}	else {
								index = myIndex * 2;
								while (--index > 0) {
									element = element.next;
								}
								before_element = element;
								element = element.next.next;
								if (type != null) {
									agg.removeFromInverseList(before_element.next.object);
								}
								if (myIndex == agg.myLength - 1) {
									before_element.next = null;
									myDataA[1] = before_element;
									myElement = null;
									behind = true;
								} else {
									before_element.next = element.next;
									myElement = element.next;
								}
							}
							if (agg.myLength == CAggregate.SHORT_AGGR_SELECT + 1) {
								agg.myData = myDataA[0];
							} else {
								agg.myData = myDataA;
							}
						}
					}
				} else {
					if (type.express_type == DataType.ARRAY) {
						throw new SdaiException(SdaiException.AI_NVLD, agg);
					}
					if (myIndex < 0 || myIndex >= agg.myLength) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					int shift = 1;
					if (type.select != null && type.select.is_mixed > 0) {
						shift = 2;
					}
					if (shift <= 1) {
						if (agg.myLength == 1) {
							agg.removeFromInverseList(agg.myData);
							agg.myData = null;
						} else if (agg.myLength == 2) {
							myDataA = (Object [])agg.myData;
							if (myIndex == 0) {
								agg.removeFromInverseList(myDataA[0]);
								agg.myData = myDataA[1];
							} else {
								agg.removeFromInverseList(myDataA[1]);
								agg.myData = myDataA[0];
							}
						} else {
							myDataA = (Object [])agg.myData;
							agg.removeFromInverseList(myDataA[myIndex]);
							if (myIndex < agg.myLength - 1) {
								myDataA[myIndex] = myDataA[agg.myLength - 1];
								myDataA[agg.myLength - 1] = null;
							} else {
								myDataA[agg.myLength - 1] = null;
							}
						}
					} else {
						myDataA = (Object [])agg.myData;
						agg.removeFromInverseList(myDataA[myIndex*2]);
						if (myIndex < agg.myLength - 1) {
							myDataA[myIndex*2] = myDataA[(agg.myLength - 1)*2];
							myDataA[myIndex*2 + 1] = myDataA[(agg.myLength - 1)*2 + 1];
						} else {
							myDataA[(agg.myLength - 1)*2] = null;
							myDataA[(agg.myLength - 1)*2 + 1] = null;
						}
					}
				}
				agg.myLength--;
				if (type != null && type.shift == SdaiSession.PRIVATE_AGGR && type == SdaiSession.listTypeSpecial) {
					SdaiCommon own = agg.getOwner();
					if (own instanceof SdaiRepository) {
						((SdaiRepository)own).modified = true;
					} else if (own instanceof SdaiModel) {
						((SdaiModel)own).modified_outside_contents = true;
					} else if (own instanceof SchemaInstance) {
						((SchemaInstance)own).modified = true;
					}
				}
				if (type != null && type.shift != SdaiSession.PRIVATE_AGGR) {
					agg.owner.modified();
					agg.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				} else if (type == null) {
					agg.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case SESSION_AGGR:
				SessionAggregate agg_dic = (SessionAggregate)myAggregate;
				AggregationType type_dic = agg_dic.myType;
				if (type_dic != null) {
					throw new SdaiException(SdaiException.FN_NAVL);
				}
				if (agg_dic.myLength <= 0) {
					throw new SdaiException(SdaiException.AI_NSET, agg_dic);
				}
				if (myElement == null) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				ListElement element;
				if (agg_dic.myLength == 1) {
					agg_dic.myData[0] = agg_dic.myData[1] = null;
					myElement = null;
					behind = true;
					has_member = false;
				} else if (myIndex == 0) {
					agg_dic.myData[0] = ((ListElement)agg_dic.myData[0]).next;
					myElement = agg_dic.myData[0];
				} else if (myIndex == agg_dic.myLength - 1) {
					element = (ListElement)agg_dic.myData[0];
					for (i = 0; i < agg_dic.myLength - 2; i++) {
						element = element.next;
					}
					element.next = null;
					agg_dic.myData[1] = element;
					myElement = null;
					behind = true;
					has_member = false;
				} else {
					element = (ListElement)agg_dic.myData[0];
					for (i = 0; i < myIndex - 1; i++) {
						element = element.next;
					}
					element.next = element.next.next;
					myElement = element.next;
				}
				agg_dic.myLength--;
				agg_dic.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				break;
			case SESSION_AGGR_MODELS:
			case ALL_INSTANCES:
			case INSTANCES:
			case INSTANCES_EXACT:
			case INSTANCES_COMPL:
			case ASDAIMODEL_INST:
			case ASDAIMODEL_INST_EXACT:
				throw new SdaiException(SdaiException.FN_NAVL);
			case INTEGER_AGGR:
				A_integerPrimitive agg_int = (A_integerPrimitive)myAggregate;
				AggregationType type_int = agg_int.myType;
				if (type_int == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_int);
				}
				if (type_int.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_int.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg_int.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				if (type_int.express_type == DataType.LIST) {
					if (agg_int.myLength <= 0) {
						throw new SdaiException(SdaiException.AI_NSET, agg_int);
					}
					if (myElement == null) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					ListElementInteger element_int;
					ListElementInteger [] myDataList = (ListElementInteger [])agg_int.myData;
					if (agg_int.myLength == 1) {
						((ListElementInteger [])agg_int.myData)[0] = ((ListElementInteger [])agg_int.myData)[1] = null;
						myElement = null;
						behind = true;
						has_member = false;
					} else if (myIndex == 0) {
						((ListElementInteger [])agg_int.myData)[0] = myDataList[0].next;
						myElement = ((ListElementInteger [])agg_int.myData)[0];
					} else if (myIndex == agg_int.myLength - 1) {
						element_int = myDataList[0];
						for (i = 0; i < agg_int.myLength - 2; i++) {
							element_int = element_int.next;
						}
						element_int.next = null;
						((ListElementInteger [])agg_int.myData)[1] = element_int;
						myElement = null;
						behind = true;
						has_member = false;
					} else {
						element_int = myDataList[0];
						for (i = 0; i < myIndex - 1; i++) {
							element_int = element_int.next;
						}
						element_int.next = element_int.next.next;
						myElement = element_int.next;
					}
				} else {
					if (type_int.express_type == DataType.ARRAY) {
						throw new SdaiException(SdaiException.AI_NVLD, agg_int);
					}
					if (myIndex < 0 || myIndex >= agg_int.myLength) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					int [] myDataArray = (int [])agg_int.myData;
					myDataArray[myIndex] = myDataArray[agg_int.myLength - 1];
					if (myIndex == agg_int.myLength - 1) {
						has_member = false;
					}
				}
				agg_int.myLength--;
				if (type_int.shift != SdaiSession.PRIVATE_AGGR) {
					agg_int.owner.modified();
					agg_int.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case DOUBLE_AGGR:
				A_double agg_double = (A_double)myAggregate;
				AggregationType type_double = agg_double.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
				if (type_double.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_double.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg_double.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				if (type_double.express_type == DataType.LIST) {
					if (agg_double.myLength <= 0) {
						throw new SdaiException(SdaiException.AI_NSET, agg_double);
					}
					if (myElement == null) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					ListElementDouble element_double;
					ListElementDouble [] myDataList = (ListElementDouble [])agg_double.myData;
					if (agg_double.myLength == 1) {
						((ListElementDouble [])agg_double.myData)[0] = ((ListElementDouble [])agg_double.myData)[1] = null;
						myElement = null;
						behind = true;
						has_member = false;
					} else if (myIndex == 0) {
						((ListElementDouble [])agg_double.myData)[0] = myDataList[0].next;
						myElement = ((ListElementDouble [])agg_double.myData)[0];
					} else if (myIndex == agg_double.myLength - 1) {
						element_double = myDataList[0];
						for (i = 0; i < agg_double.myLength - 2; i++) {
							element_double = element_double.next;
						}
						element_double.next = null;
						((ListElementDouble [])agg_double.myData)[1] = element_double;
						myElement = null;
						behind = true;
						has_member = false;
					} else {
						element_double = myDataList[0];
						for (i = 0; i < myIndex - 1; i++) {
							element_double = element_double.next;
						}
						element_double.next = element_double.next.next;
						myElement = element_double.next;
					}
				} else {
					if (type_double.express_type == DataType.ARRAY) {
						throw new SdaiException(SdaiException.AI_NVLD, agg_double);
					}
					if (myIndex < 0 || myIndex >= agg_double.myLength) {
						throw new SdaiException(SdaiException.IR_NSET, this);
					}
					if (aggr_owner != null) {
						owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
					}
					double [] myDataArray = (double [])agg_double.myData;
					myDataArray[myIndex] = myDataArray[agg_double.myLength - 1];
					if (myIndex == agg_double.myLength - 1) {
						has_member = false;
					}
				}
				agg_double.myLength--;
				if (type_double.shift != SdaiSession.PRIVATE_AGGR) {
					agg_double.owner.modified();
					agg_double.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				type_double = agg_double3.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				aggr_owner = agg_double3.getOwningInstance();
				owning_model = aggr_owner.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				if (type_double.express_type == DataType.ARRAY) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (myIndex <= 0 || myIndex > agg_double3.myLength) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (myIndex == agg_double3.myLength) {
					has_member = false;
				} else {
					if (aggr_owner != null) {
						session.undoRedoModifyPrepare(aggr_owner);
					}
					switch(agg_double3.myLength) {
						case 2:
							agg_double3.double1 = agg_double3.double2;
							break;
						case 3:
							if (myIndex == 1) {
								agg_double3.double1 = agg_double3.double2;
								agg_double3.double2 = agg_double3.double3;
							} else {
								agg_double3.double2 = agg_double3.double3;
							}
							break;
					}
					has_member = true;
				}
				agg_double3.myLength--;
				agg_double3.owner.modified();
				agg_double3.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				break;
		}
		return has_member;
//		} // syncObject
	}


/**
 * Positions this <code>SdaiIterator</code> after the last member of the
 * aggregate to which this <code>SdaiIterator</code> is attached.
 * The method is valid only when the EXPRESS type of the attached aggregate
 * is either ARRAY or LIST.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @see "ISO 10303-22::10.15.2 end"
 */
	public void end() throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		switch (AggregationType) {
			case ENTITY_AGGR:
				CAggregate agg = (CAggregate)myAggregate;
				AggregationType type = agg.myType;
				if (type != null && type.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					SdaiModel owning_model = agg.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (type == null || type.express_type == DataType.LIST) {
					myElement = null;
					behind = true;
				} else if (type.express_type == DataType.ARRAY) {
					if (agg.myData == null) {
						EBound bound = ((CArray_type)type).getLower_index(null);
						EBound upper_bound = ((CArray_type)type).getUpper_index(null);
						agg.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
					}
					myIndex = agg.myLength;
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg);
				}
				break;
			case SESSION_AGGR:
				SessionAggregate agg_dic = (SessionAggregate)myAggregate;
				AggregationType type_ses = agg_dic.myType;
				if (type_ses != null) {
					if (agg_dic.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
//					if (SdaiSession.session == null) {
					if (!agg_dic.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				}
				if (type_ses == null || type_ses.express_type == DataType.LIST) {
					myElement = null;
					behind = true;
				} else if (type_ses.express_type == DataType.ARRAY) {
					if (agg_dic.myData == null) {
						EBound bound = ((CArray_type)type_ses).getLower_index(null);
						EBound upper_bound = ((CArray_type)type_ses).getUpper_index(null);
						agg_dic.initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
					}
					myIndex = agg_dic.myLength;
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg_dic);
				}
				break;
			case SESSION_AGGR_MODELS:
			case ALL_INSTANCES:
			case INSTANCES:
			case INSTANCES_EXACT:
			case INSTANCES_COMPL:
			case ASDAIMODEL_INST:
			case ASDAIMODEL_INST_EXACT:
				throw new SdaiException(SdaiException.AI_NVLD, myAggregate);
			case INTEGER_AGGR:
			case DOUBLE_AGGR:
				A_primitive agg_prim = (A_primitive)myAggregate;
				AggregationType type_prim = agg_prim.myType;
				if (type_prim == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_prim);
				}
				if (type_prim.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_prim.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					SdaiModel owning_model = agg_prim.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (type_prim.express_type == DataType.LIST) {
					myElement = null;
					behind = true;
				} else if (type_prim.express_type == DataType.ARRAY) {
					if (agg_prim.myData == null) {
						EBound bound = ((CArray_type)type_prim).getLower_index(null);
						EBound upper_bound = ((CArray_type)type_prim).getUpper_index(null);
						if (agg_prim instanceof A_integer) {
							((A_integer)agg_prim).initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1, Integer.MIN_VALUE);
						} else if (agg_prim instanceof A_integerPrimitive) {
							((A_integerPrimitive)agg_prim).initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1, 0);
						} else if (agg_prim instanceof A_double) {
							((A_double)agg_prim).initializeData(upper_bound.getBound_value(null) - bound.getBound_value(null) + 1);
						}
					}
					myIndex = agg_prim.myLength;
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg_prim);
				}
				break;
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				AggregationType type_double = agg_double3.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = agg_double3.getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				if (type_double.express_type == DataType.LIST || type_double.express_type == DataType.ARRAY) {
					myIndex = agg_double3.myLength + 1;
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				break;
		}
//		} // syncObject
	}


/**
 * Positions this <code>SdaiIterator</code> to reference the preceding member of the
 * aggregate to which this <code>SdaiIterator</code> is attached.
 * If the iterator currently references the first element, or is at the beginning of 
 * the related aggregate and thus has no current member, or this aggregate is
 * empty, then the iterator is positioned to the beginning of the aggregate
 * without referencing any aggregate member.
 * The method is valid only when the EXPRESS type of the attached aggregate
 * is either ARRAY or LIST.
 * @return <code>true</code> if iterator has a new current member, 
 * <code>false</code> otherwise.
 * @throws SdaiException SS_NOPN, session is not open.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @see #next
 * @see "ISO 10303-22::10.15.3 Previous"
 */
	public boolean previous() throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		switch (AggregationType) {
			case ENTITY_AGGR:
				CAggregate agg = (CAggregate)myAggregate;
				if (agg.myType != null && agg.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					SdaiModel owning_model = agg.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				int i;
				Object [] myDataA;
				if (agg.myType == null || agg.myType.express_type == DataType.LIST) {
					if (agg.myLength <= 0 || (myElement != null && myIndex <= 0)) {
						myElement = null;
						behind = false;
						myIndex = -1;
						return false;
					}
					int shift = 1;
					if (agg.myType != null && agg.myType.select != null 
							&& agg.myType.select.is_mixed > 0) {
						shift = 2;
					}
					ListElement element;
					if (myElement == null && !behind) {
						return false;
					}
					if (shift <= 1) {
						if (myElement == null) {
							if (agg.myLength == 1) {
								myElement = agg.myData;
							} else if (agg.myLength == 2) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[1];
							} else if (agg.myLength <= CAggregate.SHORT_AGGR) {
								element = (ListElement)agg.myData;
								while (element.next != null) {
									element = element.next;
								}
								myElement = element;
							} else {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[1];
							}
							behind = false;
							myIndex = agg.myLength - 1;
							return true;
						} else {
							if (agg.myLength == 2) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							} else {
								if (agg.myLength <= CAggregate.SHORT_AGGR) {
									element = (ListElement)agg.myData;
								} else {
									myDataA = (Object [])agg.myData;
									element = (ListElement)myDataA[0];
								}
								for (i = 0; i < myIndex - 1; i++) {
									element = element.next;
								}
								myElement = element;
							}
							myIndex--;
							return true;
						}
					} else {
						if (myElement == null) {
							if (agg.myLength == 1) {
								myDataA = (Object [])agg.myData;
								myElement = myDataA[0];
							} else if (agg.myLength <= CAggregate.SHORT_AGGR_SELECT) {
								element = (ListElement)agg.myData;
								while (element.next.next != null) {
									element = element.next.next;
								}
								myElement = element;
							} else {
								myDataA = (Object [])agg.myData;
								element = (ListElement)myDataA[0];
								for (i = 0; i < 2 * (agg.myLength - 1); i++) {
									element = element.next;
								}
								myElement = element;
							}
							behind = false;
							myIndex = agg.myLength - 1;
							return true;
						} else {
							if (agg.myLength <= CAggregate.SHORT_AGGR_SELECT) {
								element = (ListElement)agg.myData;
							} else {
								myDataA = (Object [])agg.myData;
								element = (ListElement)myDataA[0];
							}
							for (i = 0; i < 2 * (myIndex - 1); i++) {
								element = element.next;
							}
							myElement = element;
							myIndex--;
							return true;
						}
					}
				} else if (agg.myType.express_type == DataType.ARRAY) {
					if (myIndex < 0) {
						return false;
					}
					myIndex--;
					if (myIndex < 0) {
						return false;
					} else {
						return true;
					}
				} else {
					throw new SdaiException(SdaiException.AI_NVLD);
				}
			case SESSION_AGGR:
				SessionAggregate agg_dic = (SessionAggregate)myAggregate;
				if (agg_dic.myType != null) {
					if (agg_dic.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
//					if (SdaiSession.session == null) {
					if (!agg_dic.isSessionAvailable()) {
						throw new SdaiException(SdaiException.SS_NOPN);
					}
				}
				if (agg_dic.myType == null || agg_dic.myType.express_type == DataType.LIST) {
					if (agg_dic.myLength <= 0) {
						myElement = null;
						behind = false;
						return false;
					}
					if (myElement == null && !behind) {
						return false;
					} else if (myElement == null) {
						myElement = agg_dic.myData[1];
						behind = false;
						myIndex = agg_dic.myLength - 1;
						return true;
					} else if (myIndex == 0) {
						myElement = null;
						behind = false;
						return false;
					} else {
						ListElement element = (ListElement)agg_dic.myData[0];
						while (element.next != myElement) {
							element = element.next;
						}
						myElement = element;
						myIndex--;
						return true;
					}
//				} else if (agg_dic.myType.express_type == DataType.ARRAY) {
				} else {
					if (myIndex < 0) {
						return false;
					} else {	
						myIndex--;
						if (myIndex < 0) {
							return false;
						} else {
							return true;
						}
					}
				}// else {
//					throw new SdaiException(SdaiException.AI_NVLD);
//				}
			case SESSION_AGGR_MODELS:
			case ALL_INSTANCES:
			case INSTANCES:
			case INSTANCES_EXACT:
			case INSTANCES_COMPL:
			case ASDAIMODEL_INST:
			case ASDAIMODEL_INST_EXACT:
				throw new SdaiException(SdaiException.AI_NVLD, myAggregate);
			case INTEGER_AGGR:
				A_integerPrimitive agg_int = (A_integerPrimitive)myAggregate;
				AggregationType type_int = agg_int.myType;
				if (type_int == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_int);
				}
				if (type_int.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_int.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					SdaiModel owning_model = agg_int.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (type_int.express_type == DataType.LIST) {
					if (agg_int.myLength <= 0) {
						myElement = null;
						behind = false;
						return false;
					}
					ListElementInteger element;
					ListElementInteger [] myDataList = (ListElementInteger [])agg_int.myData;
					if (myElement == null && !behind) {
						return false;
					} else if (myElement == null) {
						myElement = myDataList[1];
						behind = false;
						myIndex = agg_int.myLength - 1;
						return true;
					} else if (myIndex == 0) {
						myElement = null;
						behind = false;
						return false;
					} else {
						element = myDataList[0];
						for (i = 0; i < myIndex - 1; i++) {
							element = element.next;
						}
						myElement = element;
						myIndex--;
						return true;
					}
				} else if (type_int.express_type == DataType.ARRAY) {
					if (myIndex < 0) {
						return false;
					} else {	
						myIndex--;
						if (myIndex < 0) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					throw new SdaiException(SdaiException.AI_NVLD);
				}
			case DOUBLE_AGGR:
				A_double agg_double = (A_double)myAggregate;
				AggregationType type_double = agg_double.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
				if (type_double.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_double.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					SdaiModel owning_model = agg_double.getOwningInstance().owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.MX_NDEF, owning_model);
					}
				}
				if (type_double.express_type == DataType.LIST) {
					if (agg_double.myLength <= 0) {
						myElement = null;
						behind = false;
						return false;
					}
					ListElementDouble element;
					ListElementDouble [] myDataList = (ListElementDouble [])agg_double.myData;
					if (myElement == null && !behind) {
						return false;
					} else if (myElement == null) {
						myElement = myDataList[1];
						behind = false;
						myIndex = agg_double.myLength - 1;
						return true;
					} else if (myIndex == 0) {
						myElement = null;
						behind = false;
						return false;
					} else {
						element = myDataList[0];
						for (i = 0; i < myIndex - 1; i++) {
							element = element.next;
						}
						myElement = element;
						myIndex--;
						return true;
					}
				} else if (type_double.express_type == DataType.ARRAY) {
					if (myIndex < 0) {
						return false;
					} else {	
						myIndex--;
						if (myIndex < 0) {
							return false;
						} else {
							return true;
						}
					}
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				type_double = agg_double3.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = agg_double3.getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
				if (type_double.express_type == DataType.LIST || type_double.express_type == DataType.ARRAY) {
					if (myIndex <= 0) {
						return false;
					}
					myIndex--;
					if (myIndex <= 0) {
						return false;
					} else {
						return true;
					}
				} else {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
		}
		return false;
//		} // syncObject
	}


/**
 * Changes the value of a member of the attached aggregate at the position 
 * specified by this <code>SdaiIterator</code> so that this aggregate has no
 * value at that position. A subsequent application of <code>testByIndex</code>
 * methods at this index position will return value 0. If the old value is
 * an aggregate instance, then it is deleted along with any nested aggregate
 * instances contained by it. The method is valid only if the type of the
 * aggregate attached to this <code>SdaiIterator</code> is ARRAY. If this
 * condition is violated, then <code>SdaiException</code> AI_NVLD is thrown.

 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.18.2 Unset value current member"
 */
	public void unset() throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session;
		switch (AggregationType) {
			case ENTITY_AGGR:
				CAggregate agg = (CAggregate)myAggregate;
				if (agg.myType == null || agg.myType.express_type != DataType.ARRAY) {
					throw new SdaiException(SdaiException.AI_NVLD, agg);
				}
				if (agg.myType.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				if (myIndex < 0 || myIndex >= agg.myLength) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				int shift = 1;
				if (agg.myType.select != null && agg.myType.select.is_mixed > 0) {
					shift = 2;
				}
				if (aggr_owner != null) {
					owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
				}
				Object [] myDataA;
				if (shift <= 1) {
					if (agg.myLength == 1) {
						agg.removeFromInverseList(agg.myData);
						agg.myData = null;
					} else {
						myDataA = (Object [])agg.myData;
						agg.removeFromInverseList(myDataA[myIndex]);
						myDataA[myIndex] = null;
					}
				} else {
					myDataA = (Object [])agg.myData;
					int index = myIndex * 2;
					agg.removeFromInverseList(myDataA[index]);
					myDataA[index] = null;
					myDataA[index + 1] = null;
				}
				if (agg.myType.shift != SdaiSession.PRIVATE_AGGR) {
					agg.owner.modified();
					agg.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				} else if (agg.myType == null) {
					agg.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case SESSION_AGGR:
			case SESSION_AGGR_MODELS:
			case ALL_INSTANCES:
			case INSTANCES:
			case INSTANCES_EXACT:
			case INSTANCES_COMPL:
			case ASDAIMODEL_INST:
			case ASDAIMODEL_INST_EXACT:
				throw new SdaiException(SdaiException.FN_NAVL);
			case INTEGER_AGGR:
				A_integerPrimitive agg_int = (A_integerPrimitive)myAggregate;
				AggregationType type_int = agg_int.myType;
				if (type_int == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_int);
				}
				if (type_int.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_int.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg_int.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				if (type_int.express_type != DataType.ARRAY) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_int);
				}
				if (myIndex < 0 || myIndex >= agg_int.myLength) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				int [] myDataArrayInt = (int [])agg_int.myData;
				if (aggr_owner != null) {
					owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
				}
				if (agg_int instanceof A_integer) {
					myDataArrayInt[myIndex] = Integer.MIN_VALUE;
				} else {
					myDataArrayInt[myIndex] = 0;
				}
				if (type_int.shift != SdaiSession.PRIVATE_AGGR) {
					agg_int.owner.modified();
					agg_int.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case DOUBLE_AGGR:
				A_double agg_double = (A_double)myAggregate;
				AggregationType type_double = agg_double.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
				if (type_double.shift != SdaiSession.PRIVATE_AGGR) {
					if (agg_double.getOwner() == null) {
						throw new SdaiException(SdaiException.AI_NEXS);
					}
					aggr_owner = agg_double.getOwningInstance();
					owning_model = aggr_owner.owning_model;
					if (owning_model == null) {
						throw new SdaiException(SdaiException.EI_NEXS);
					}
					if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
						throw new SdaiException(SdaiException.MX_NRW, owning_model);
					}
					session = owning_model.repository.session;
					if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
						String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
						throw new SdaiException(SdaiException.SY_ERR, base);
					}
				}
				if (type_double.express_type != DataType.ARRAY) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double);
				}
				if (myIndex < 0 || myIndex >= agg_double.myLength) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				double [] myDataArrayDouble = (double [])agg_double.myData;
				if (aggr_owner != null) {
					owning_model.repository.session.undoRedoModifyPrepare(aggr_owner);
				}
				myDataArrayDouble[myIndex] = Double.NaN;
				if (type_double.shift != SdaiSession.PRIVATE_AGGR) {
					agg_double.owner.modified();
					agg_double.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
				break;
			case DOUBLE3_AGGR:
				A_double3 agg_double3 = (A_double3)myAggregate;
				type_double = agg_double3.myType;
				if (type_double == null) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (agg_double3.getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				aggr_owner = agg_double3.getOwningInstance();
				owning_model = aggr_owner.owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, owning_model);
				}
				if (type_double.express_type != DataType.ARRAY) {
					throw new SdaiException(SdaiException.AI_NVLD, agg_double3);
				}
				if (myIndex <= 0 || myIndex > agg_double3.myLength) {
					throw new SdaiException(SdaiException.IR_NSET, this);
				}
				session = owning_model.repository.session;
				if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
					String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
				if (aggr_owner != null) {
					session.undoRedoModifyPrepare(aggr_owner);
				}
				switch(myIndex) {
					case 1:
						agg_double3.double1 = Double.NaN;
						break;
					case 2:
						agg_double3.double2 = Double.NaN;
						break;
					case 3:
						agg_double3.double3 = Double.NaN;
						break;
				}
				agg_double3.owner.modified();
				agg_double3.fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				break;
		}
//		} // syncObject
	}


}
