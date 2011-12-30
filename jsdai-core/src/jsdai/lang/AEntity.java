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

import jsdai.client.SdaiModelRemote;
import jsdai.dictionary.*;
import jsdai.mapping.AEntity_mapping;
import jsdai.mapping.EEntity_mapping;
import jsdai.query.AEntityRef;
import jsdai.query.EEntityRef;
import jsdai.query.SerializableRef;

/**
 Specialized class implementing <code>Aggregate</code> for members of
 type <code>EEntity</code>. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class AEntity extends CAggregate {

/**
 * Constructs a new object of aggregate <code>AEntity</code>
 * without aggregation type. The aggregates of such kind are used
 * as non-persistent lists.
 * @see Aggregate#getAggregationType
 */
	public AEntity() {
		super();
	}


	/**
	 * Constructs a new object linked to the session of aggregate
	 * <code>AEntity</code> without aggregation type. The aggregates
	 * of such kind are used as non-persistent lists.
	 * @param owning_session a <code>SdaiSession</code> value
	 * @see Aggregate#getAggregationType
	 */
	public AEntity(SdaiSession owning_session) {
		super();
		owner = owning_session;
	}



/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public boolean isMember(EEntity value) throws SdaiException {
		int i;
		SdaiModel model;
//		synchronized (syncObject) {
		Object [] myDataA;
		if (myType == null) {
			if (!SdaiSession.isSessionAvailable()) {
				throw new SdaiException(SdaiException.SS_NOPN);
			}
		} else {
			if (myType.shift >= 0) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				CEntity ownr = (CEntity)owner;
				if (ownr == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				model = ownr.owning_model;
//				model = getOwningInstance().owning_model;
				if (model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, model);
				}
			} else {
				if (myType.shift <= SdaiSession.INST_AGGR && myType.shift >= SdaiSession.ALL_INST_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[0];
					if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.AI_NVLD, this);
					}
					if (myType.shift == SdaiSession.INST_AGGR) {
						return isMemberInstances(value);
					} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
						return isMemberInstancesExact(value);
					} else {
						return isMemberInstancesAll(value);
					}
				}
				if (myType.shift == SdaiSession.INST_COMPL_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[myLength];
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
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
					return isMemberListOfModelsAll(value);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
					return isMemberListOfModels(value);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
					return isMemberListOfModelsExact(value);
				}
				if (myType.shift != SdaiSession.PRIVATE_AGGR) {
					throw new SdaiException(SdaiException.SY_ERR);
				}
			}
		}

		if (myLength == 0 || value == null) {
			return false;
		}
		boolean has_con = false;
		if (myLength < 0) {
			myLength = -myLength;
			has_con = true;
		}
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (myLength == 1) {
				if (value == myData) {
					if (has_con) {
						myLength = -myLength;
					}
					return true;
				}
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (value == myDataA[0] || value == myDataA[1]) {
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
					if (value == element.object) {
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
				if (value == myData) {
					if (has_con) {
						myLength = -myLength;
					}
					return true;
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					if (value == myDataA[i]) {
						if (has_con) {
							myLength = -myLength;
						}
						return true;
					}
				}
			}
		}
		if (has_con) {
			myLength = -myLength;
		}
		return false;
//		} // syncObject
	}
//	public boolean isMember(EEntity value) throws SdaiException {
//		return isMember(value, null);
//	}

/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 */
	public EEntity getByIndexEntity(int index) throws SdaiException {
//		synchronized (syncObject) {
		Object [] myDataA;
		if (myType != null) {
			SdaiModel model;
			if (myType.shift <= SdaiSession.INST_AGGR) {
				if (myType.shift >= SdaiSession.ALL_INST_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[0];
					if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.AI_NVLD, this);
					}
					if (myType.shift == SdaiSession.INST_AGGR) {
						return (EEntity)getByIndexInstance(index, model);
					} else if (myType.shift == SdaiSession.INST_EXACT_AGGR) {
						return (EEntity)getByIndexInstanceExact(index, model);
					} else {
						return (EEntity)getByIndexInstanceAll(index, model);
					}
				}
				if (myType.shift == SdaiSession.INST_COMPL_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[myLength];
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
					return (EEntity)myDataA[index];
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
					return (EEntity)getByIndexListOfModelsAll(index);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
					return (EEntity)getByIndexListOfModels(index);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
					return (EEntity)getByIndexListOfModelsExact(index);
				}
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				CEntity ownr = (CEntity)owner;
				if (ownr == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = ownr.owning_model;
//				SdaiModel owning_model = getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		Object obj;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElement element = null;
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
			if (obj == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
//			if (obj instanceof SdaiModel.Connector) {
//				return resolveConnector(element, false, (SdaiModel.Connector)obj, index);
//			}
			return (EEntity)obj;
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
			if (myLength == 1) {
				obj = myData;
			} else {
				myDataA = (Object [])myData;
				obj = myDataA[index];
			}
			if (obj == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
//			if (obj instanceof SdaiModel.Connector) {
//				return resolveConnector(false, (SdaiModel.Connector)obj, index);
//			}
			return (EEntity)obj;
		}
//		} // syncObject
	}
//	public EEntity getByIndexEntity(int index) throws SdaiException {
//		return (EEntity)getByIndexObject(index);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, Object, EDefined_type []) setByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void setByIndex(int index, EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		CEntity owning_instance = null;
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift == SdaiSession.PRIVATE_AGGR) {
				if (!(getOwner() instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.FN_NAVL);
				}
			} else {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
				if (myType.select == null) {
					if (!analyse_value_entity(value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				} else if (!analyse_select_value_entity(myType.select, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		if (myLength < 0) {
			resolveAllConnectors();
		}
		Object [] myDataA;
		CEntity ex_member, new_member;
		if (myType == null || myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (owning_instance != null) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
			ListElement element;
			if (myLength == 1) {
				if (myType != null) {
					ex_member = (CEntity)myData;
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
				}
				myData = value;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (myType != null) {
					ex_member = (CEntity)myDataA[index];
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
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
					ex_member = (CEntity)element.object;
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
				}
				element.object = value;
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
			if (owning_instance != null) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
			if (myLength == 1) {
				ex_member = (CEntity)myData;
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
				myData = value;
			} else {
				myDataA = (Object [])myData;
				ex_member = (CEntity)myDataA[index];
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
				myDataA[index] = value;
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
//	public void setByIndex(int index, EEntity value) throws SdaiException {
//		setByIndex(index, value, null);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addByIndex(int index, EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		CEntity owning_instance = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
			if (myType.express_type != DataType.LIST) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
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
			if (bound != null && myLength >= bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		if (owning_instance != null) {
			owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		}
		if (myType != null) {
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value_entity(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else if (myType.shift > -5 && !analyse_select_value_entity(myType.select, value)) {
				throw new SdaiException(SdaiException.VT_NVLD); 
			}
			CEntity new_member = (CEntity)value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
		}
		Object [] myDataA;
		ListElement element = null;
		ListElement new_element;
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
		myLength++;
		if (myType != null) { 
			if	(myType.shift != SdaiSession.PRIVATE_AGGR || getOwner() instanceof SdaiRepository) {
				owner.modified();
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
			}
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		myLength++;
//		} // syncObject
	}
//	public void addByIndex(int index, EEntity value) throws SdaiException {
//		addByIndex(index, value, null);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(Object, EDefined_type []) addUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public void addUnordered(EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		SdaiModel owning_model = null;
		SdaiCommon aggr_owner = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
			}
			if (myType.express_type == DataType.ARRAY) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			if (myType.select == null) {
				if (myType.shift > -5 && !analyse_value_entity(value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			} else {
				if (myType.shift > -5 && !analyse_select_value_entity(myType.select, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		if (myLength < 0) {
			resolveAllConnectors();
		}
		CEntity new_member;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (myType != null) {
				EBound upper_bound = null;
				CList_type list_type = (CList_type)myType;
				if (list_type.testUpper_bound(null)) {
					upper_bound = list_type.getUpper_bound(null);
				}
				if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
					String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
					throw new SdaiException(SdaiException.SY_ERR, base);
				}
			}
			if (owning_instance != null) {
				if (owning_model.repository.session.undo_redo_file != null && 
						owning_model.repository != SdaiSession.systemRepository) {
					owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
				}
			}
			ListElement element;
			ListElement new_element;
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
			myLength++;
			if (myType != null) {
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
				if (myType.shift != SdaiSession.PRIVATE_AGGR || aggr_owner instanceof SdaiRepository) {
					owner.modified();
					fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
				}
			} else {
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
			}
		} else {
			EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
			EBound upper_bound = null;
			if (aggr_type.testUpper_bound(null)) {
				upper_bound = aggr_type.getUpper_bound(null);
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			if (owning_instance != null) {
				if (owning_model.repository.session.undo_redo_file != null && 
						owning_model.repository != SdaiSession.systemRepository) {
					owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
				}
			}
			if (myLength == 0) {
				 myData = value;
			} else if (myLength == 1) {
				int ln;
				if (upper_bound == null) {
					ln = Integer.MAX_VALUE;
				} else {
					ln = upper_bound.getBound_value(null);
				}
				Object obj = myData;
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
			myLength++;
			new_member = (CEntity)value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR && owning_model.repository != SdaiSession.systemRepository) {
				owner.modified();
				fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
			}
		}
//		myLength++;
//		if (myType != null) {
//			((CEntity)value).inverseAdd(owning_instance);
//		}
//		} // syncObject
	}

	private boolean analyse_value_entity(EEntity value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type == DataType.ENTITY) {
			CEntity_definition value_type = (CEntity_definition)value.getInstanceType();
			return value_type.isSubtypeOf((CEntity_definition)type);
		}
		return false;
	}

	private boolean analyse_select_value_entity(SelectType sel_type, EEntity value) throws SdaiException {
		return analyse_entity_in_select(sel_type, (CEntity_definition)value.getInstanceType());
	}

//	public void addUnordered(EEntity value) throws SdaiException {
//		addUnordered(value, null);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(Object, EDefined_type []) removeUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EEntity</code> and the second parameter is dropped.
 */
	public void removeUnordered(EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null || myType.shift <= SdaiSession.INST_AGGR) {
			throw new SdaiException(SdaiException.AI_NVLD);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiCommon owner = this.owner;
		while (!(owner instanceof CEntity) && owner != null) {
			owner = owner.getOwner();
		}
		CEntity owning_instance = (CEntity)owner;
		if (owning_instance == null) {
//		if (getOwner() == null) {
			throw new SdaiException(SdaiException.AI_NEXS);
		}
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
		Object [] myDataA;
		int index = -1;
		if (myLength == 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		CEntity ex_member;
		owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		if (myLength == 1) {
			if (myData == value) {
				index = 0;
				ex_member = (CEntity)myData;
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				myData = null;
			}
		} else if (myLength == 2) {
			myDataA = (Object [])myData;
			if (myDataA[0] == value) {
				index = 0;
				ex_member = (CEntity)myDataA[0];
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				myData = myDataA[1];
			} else if (myDataA[1] == value) {
				index = 1;
				ex_member = (CEntity)myDataA[1];
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				myData = myDataA[0];
			}
		} else {
			myDataA = (Object [])myData;
			for (int i = 0; i < myLength; i++) {
				if (myDataA[i] == value) {
					index = i;
					ex_member = (CEntity)myDataA[i];
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					myDataA[i] = myDataA[myLength - 1];
					myDataA[myLength - 1] = null;
					break;
				}
			}
		}
		if (index < 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		myLength--;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
//	public void removeUnordered(EEntity value) throws SdaiException {
//		removeUnordered(value, null);
//	}

					/*		Operations using an iterator			*/


/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, Object, EDefined_type []) setCurrentMember(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void setCurrentMember(SdaiIterator it, EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift == SdaiSession.PRIVATE_AGGR) {
				if (!(getOwner() instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.FN_NAVL);
				}
			} else {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
				if (myType.select == null) {
					if (!analyse_value_entity(value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				} else if (!analyse_select_value_entity(myType.select, value)) {
					throw new SdaiException(SdaiException.VT_NVLD); 
				}
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}

		CEntity ex_member, new_member;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (it.myElement == null && (it.behind || myLength == 0 || it.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (owning_instance != null) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
			if (myLength == 1) {
				if (myType != null) {
					ex_member = (CEntity)myData;
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
				}
				myData = value;
				it.myElement = value;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				if (myType != null) {
					ex_member = (CEntity)myDataA[it.myIndex];
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
				}
				myDataA[it.myIndex] = value;
				it.myElement = value;
			} else {
				if (myType != null) {
					ex_member = (CEntity)((ListElement)it.myElement).object;
					if (!ex_member.owning_model.optimized) {
						ex_member.inverseRemove(owning_instance);
					}
					new_member = (CEntity)value;
					if (!new_member.owning_model.optimized) {
						new_member.inverseAdd(owning_instance);
					}
				}
				((ListElement)it.myElement).object = value;
			}
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (owning_instance != null) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
			if (myLength == 1) {
				ex_member = (CEntity)myData;
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
				myData = value;
			} else {
				myDataA = (Object [])myData;
				ex_member = (CEntity)myDataA[it.myIndex];
				if (!ex_member.owning_model.optimized) {
					ex_member.inverseRemove(owning_instance);
				}
				new_member = (CEntity)value;
				if (!new_member.owning_model.optimized) {
					new_member.inverseAdd(owning_instance);
				}
				myDataA[it.myIndex] = value;
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
//	public void setCurrentMember(SdaiIterator iter, EEntity value) throws SdaiException {
//		setCurrentMember(iter, value, null);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#addBefore(SdaiIterator, Object, EDefined_type []) addBefore(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addBefore(SdaiIterator it, EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift == SdaiSession.PRIVATE_AGGR) {
				if (!(getOwner() instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.FN_NAVL);
				}
			} else {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
				if (myType.select == null) {
					if (!analyse_value_entity(value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				} else {
					if (!analyse_select_value_entity(myType.select, value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				}
			}
			if (myType.express_type != DataType.LIST) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			EBound upper_bound = null;
			CList_type list_type = (CList_type)myType;
			if (list_type.testUpper_bound(null)) {
				upper_bound = list_type.getUpper_bound(null);
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (owning_instance != null) {
			owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
		}
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (myType != null) {
			CEntity new_member = (CEntity)value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
		}
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
		myLength++;
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}
//	public void addBefore(SdaiIterator iter, EEntity value) throws SdaiException {
//		addBefore(iter, value, null);
//	}

/**
 * It is a specialization of 
 * {@link Aggregate#addAfter(SdaiIterator, Object, EDefined_type []) addAfter(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type <code>EEntity</code> and the third parameter is dropped.
 */
	public void addAfter(SdaiIterator it, EEntity value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		CEntity owning_instance = null;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				throw new SdaiException(SdaiException.FN_NAVL);
			}
			if (myType.shift == SdaiSession.PRIVATE_AGGR) {
				if (!(getOwner() instanceof SdaiRepository)) {
					throw new SdaiException(SdaiException.FN_NAVL);
				}
			} else {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				owning_instance = (CEntity)owner;
				if (owning_instance == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
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
				if (myType.select == null) {
					if (!analyse_value_entity(value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				} else {
					if (!analyse_select_value_entity(myType.select, value)) {
						throw new SdaiException(SdaiException.VT_NVLD); 
					}
				}
			}
			if (myType.express_type != DataType.LIST) {
				throw new SdaiException(SdaiException.AI_NVLD, this);
			}
			EBound upper_bound = null;
			CList_type list_type = (CList_type)myType;
			if (list_type.testUpper_bound(null)) {
				upper_bound = list_type.getUpper_bound(null);
			}
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (owning_instance != null) {
			if (owning_instance.owning_model.repository.session.undo_redo_file != null && 
					owning_instance.owning_model.repository != SdaiSession.systemRepository) {
				owning_instance.owning_model.repository.session.undoRedoModifyPrepare(owning_instance);
			}
		}
		Object [] myDataA;
		ListElement element;
		ListElement new_element;
		if (myType != null) {
			CEntity new_member = (CEntity)value;
			if (!new_member.owning_model.optimized) {
				new_member.inverseAdd(owning_instance);
			}
		}
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
		myLength++;
		if (myType != null) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		} else {
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}
//	public void addAfter(SdaiIterator iter, EEntity value) throws SdaiException {
//		addAfter(iter, value, null);
//	}

/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 */
	public EEntity getCurrentMemberEntity(SdaiIterator iter) throws jsdai.lang.SdaiException {
//		synchronized (syncObject) {
		if (iter == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (iter.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		Object [] myDataA;
		if (myType != null) {
			if (myType.shift <= SdaiSession.INST_AGGR) {
				SdaiModel model;
				if (myType.shift >= SdaiSession.ALL_INST_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[0];
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
				if (myType.shift == SdaiSession.INST_COMPL_AGGR) {
					myDataA = (Object [])myData;
					model = (SdaiModel)myDataA[myLength];
					if (model == null || (model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
						throw new SdaiException(SdaiException.AI_NVLD, this);
					}
					if (iter.myIndex < 0 || iter.myIndex >= myLength) {
						throw new SdaiException(SdaiException.IR_NSET, iter);
					}
					if (myDataA[iter.myIndex] == null) {
						throw new SdaiException(SdaiException.VA_NSET);
					}
					return (EEntity)myDataA[iter.myIndex];
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_ALL) {
					return (EEntity)getCurrentMemberListOfModelsAll(iter);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST) {
					return (EEntity)getCurrentMemberListOfModels(iter);
				}
				if (myType.shift == SdaiSession.ASDAIMODEL_INST_EXACT) {
					return (EEntity)getCurrentMemberListOfModelsExact(iter);
				}
			}
			if (myType.shift != SdaiSession.PRIVATE_AGGR) {
				SdaiCommon owner = this.owner;
				while (!(owner instanceof CEntity) && owner != null) {
					owner = owner.getOwner();
				}
				CEntity ownr = (CEntity)owner;
				if (ownr == null) {
//				if (getOwner() == null) {
					throw new SdaiException(SdaiException.AI_NEXS);
				}
				SdaiModel owning_model = ownr.owning_model;
//				SdaiModel owning_model = getOwningInstance().owning_model;
				if (owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((owning_model.mode & SdaiModel.MODE_MODE_MASK) == SdaiModel.NO_ACCESS) {
					throw new SdaiException(SdaiException.MX_NDEF, owning_model);
				}
			}
		} else if (!SdaiSession.isSessionAvailable()) {
			throw new SdaiException(SdaiException.SS_NOPN);
		}

		Object return_value;
		if (myType == null || myType.express_type == DataType.LIST) {
			if (iter.myElement == null && (iter.behind || myLength == 0 || iter.myIndex < 0)) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			ListElement element = null;
			if (myLength == 1) {
				return_value = myData;
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				return_value = myDataA[iter.myIndex];
			} else {
				element = (ListElement)iter.myElement;
				return_value = element.object;
			}
			if (return_value == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
//			if (return_value instanceof SdaiModel.Connector) {
//				return resolveConnector(element, false, (SdaiModel.Connector)return_value, iter.myIndex);
//			}
			return (EEntity)return_value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			if (myLength == 1) {
				return_value = myData;
			} else {
				myDataA = (Object [])myData;
				return_value = myDataA[iter.myIndex];
			}
			if (return_value == null) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
//			if (return_value instanceof SdaiModel.Connector) {
//				return resolveConnector(false, (SdaiModel.Connector)return_value, iter.myIndex);
//			}
			return (EEntity)return_value;
		}
//		} // syncObject
	}
//	public EEntity getCurrentMemberEntity(jsdai.lang.SdaiIterator iter) throws jsdai.lang.SdaiException {
//		return (EEntity)getCurrentMemberObject(iter);
//	}


/**
	Initializes the aggregate used to store all instances of those entities 
	which contain specified simple entity data types, that is, the aggregate 
	returned by <code>getInstances(Class types[])</code> method. The method 
	is invoked in <code>getInstancesInternal2</code> in class 
	<code>SdaiModel</code>. 
*/
	void initializeInstancesAggregate(int count) {
		myType = SdaiSession.setTypeForInstancesCompl;
		myData = new Object[count + 1];
		myLength = count;
	}


/**
 * Finds instances that are mappings of given source entity.
 * Instances that satisfies mapping constraints must be in this aggregate of instances, but other instances required by mapping constraints may be in other models within given data domain.
 * This method first finds all possible mappings.
 * These mappings should be defined in specified mapping domain.
 * Then it finds instances for every mapping.
 * <P><B>Example:</B>
 * <P><TT><pre>    AEntity ae = ...;
 *    EEntity_definition sourceEntity = ...;  // definition of source entity
 *    AEntity instances = ae.findMappingInstances(sourceEntity, targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param sourceEntity this method searches instances that are mappings of this entity
 * @param targetDomain an application domain where to search instances to satisfy mapping constraints.
 * It can be null; then this aggregate of models is used as data domain.
 * @param mappingDomain a domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY} - returned mappings are restricted to most specific,
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
 * If there is mapping of subtype entity then mapping of supertype entity is not included.
 * Entities that can not be instanciated are also not included.
 * @return list of instances that satisfy requirements.
 * Every instance is mentioned only once in this list (elements of list are unique).
 * It will return null if there are no instances that are valid mappings of given entity.
 * @see #findMappingInstances(EEntity_mapping, ASdaiModel, ASdaiModel, int)
 * @see SdaiModel#findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)
 * @see EEntity#findEntityMappings(ASdaiModel, ASdaiModel, AEntity_mapping, int)
 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		return Mapping.findMappingInstances(this, sourceEntity, targetDomain, mappingDomain, null, mode);
	}

	/**
	 * Finds instances that are mappings of given source entity.
	 * This method provides the similar functionality as 
	 * {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}
	 * and adds extra possibility to get most specific mappings of the instances.
	 * <p>This method is part of the mapping extensions of JSDAI.
	 * @param sourceEntity this method searches instances that are mappings of this entity
	 * @param targetDomain the domain where to search instances to satisfy mapping constraints.
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
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */
	public AEntity findMappingInstances(EEntity_definition sourceEntity, ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, sourceEntity, targetDomain, mappingDomain,
												instanceMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

/**
 * Finds instances that are mappings of given entity mapping.
 * This method is more specific than {@link #findMappingInstances(EEntity_definition, ASdaiModel, ASdaiModel, int)}, because it searches instances that meets requirements only for specified mapping.
 * Instances that satisfy mapping constraints must be in supplied this aggregate of instances, but other instances required by mapping constraints may be in other models within data domain.
 * <P><B>Example:</B>
 * <P><TT><pre>    AEntity ae = ...;
 *    EEntity_mapping mapping = ...;  // mapping of source entity
 *    AEntity instances = ae.findMappingInstances(sourceEntity, targetDomain, mappingDomain, EEntity.NO_RESTRICTIONS);</pre></TT>
 * <p>This method is part of the mapping extensions of JSDAI.
 * @param entityMapping this method searches instances that satisfies constraints for this entity mapping.
 * @param targetDomain an application domain where to search instances to satisfy mapping constraints.
 * It can be null; then this aggregate of models is used as data domain.
 * @param mappingDomain a domain for mapping constraints and dictionary data.
 * @param mode {@link EEntity#NO_RESTRICTIONS} - no restrictions,
 * {@link EEntity#MOST_SPECIFC_ENTITY} - returned mappings are restricted to most specific,
 * {@link EEntity#MANDATORY_ATTRIBUTES_SET} - in addition to MOST_SPECIFIC_ENTITY it also requires, that all explicit mandatory attributes are set.
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
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, targetDomain, mappingDomain,
												null, mode);
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
	 * @param targetDomain the domain where to search instances to satisfy mapping constraints.
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
	public AEntity findMappingInstances(EEntity_mapping entityMapping, ASdaiModel targetDomain, ASdaiModel mappingDomain, AEntity_mapping instanceMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMappingInstances(this, entityMapping, targetDomain, mappingDomain,
												instanceMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}

	/** Finds most specific ARM mappings for the instances in the aggregate.
	 * This is ARM (mapping) operations support method.
	 * It finds most specific mappings for entities in the aggregate.
	 * Most specific mapping can be viewed as means to define
	 * of which ARM type the AIM entity really is.
	 * @param dataDomain is domain which defines where to search instances that satisfy mapping constraints.
	 * 					In the case it is null owning model of target instance will be used as data domain.
	 * @param mappingDomain is domain for mapping constraints.
	 * @param baseMappings is an aggregate of mapings which define where to start looking for most specific mappings.
	 * The mappings returned will be mappings for subtypes of source enitity of base mappings.
	 * @param mode is mode for mapping operations. It can be one of:
	 * EEntity.NO_RESTRICTIONS
	 * EEntity.MOST_SPECIFC_ENTITY
	 * EEntity.MANDATORY_ATTRIBUTES_SET
	 *
	 * @throws SdaiException All variety of SdaiExceptions can be thrown.
	 * @return it returns an aggregate of entity_mappings that represent
	 * most specific mappings. If some instances have more than one
	 * most specific mapping all mappings are returned and corresponding
	 * instances are duplicates in the aggregate which this method is called for.
	 * Beware that number of members in the aggregate may change!
	 * Resulting entity_mapping aggregate is always synchronized with the members of the aggregate.
	 * @see <a href="package-summary.html#MappingExtension">JSDAI Mapping Extension</a>
	 */	
	public AEntity_mapping findMostSpecificMappings(ASdaiModel dataDomain, ASdaiModel mappingDomain,
	AEntity_mapping baseMappings, int mode) throws SdaiException {
		if(Implementation.mappingSupport) {
			return Mapping.findMostSpecificMappings(this, dataDomain, mappingDomain, baseMappings, mode);
		} else {
			throw new SdaiException(SdaiException.FN_NAVL, AdditionalMessages.MP_NAVL);
		}
	}


	void contract() throws SdaiException {
		if (myType != null) {
			return;
		}
		Object [] myDataA = null;
		if (myLength == 2) {
			myDataA = (Object [])myData;
			if (myDataA[0] == myDataA[1]) {
				myData = myDataA[0];
				myLength = 1;
			}
			return;
		}
		int count = 1;
		ListElement element;
		if (myLength <= SHORT_AGGR) {
			element = (ListElement)myData;
		} else {
			myDataA = (Object [])myData;
			element = (ListElement)myDataA[0];
		}
		ListElement before = element;
		element = element.next;
		while (element != null) {
			if (included(element.object, count)) {
				before.next = element.next;
				myLength--;
				element = before.next;
				if (element == null && myDataA != null) {
					myDataA[1] = before;
				}
			} else {
				before = element;
				count++;
				element = element.next;
			}
		}
		if (myLength == 1) {
			myData = before.object;
		} else if (myLength == 2) {
			if (myDataA != null) {
				myDataA[0] = ((ListElement)myDataA[0]).object;
				myDataA[1] = before.object;
			} else {
				myDataA = new Object[2];
				myDataA[0] = ((ListElement)myData).object;
				myDataA[1] = before.object;
			}
		} else if (myLength <= SHORT_AGGR && myDataA != null) {
			myData = myDataA[0];
		}
	}


	private boolean included(Object checked_inst, int count) throws SdaiException {
		Object [] myDataA;
		ListElement element;
		if (myLength <= SHORT_AGGR) {
			element = (ListElement)myData;
		} else {
			myDataA = (Object [])myData;
			element = (ListElement)myDataA[0];
		}
		for (int i = 0; i < count; i++) {
			if (element.object == checked_inst) {
				return true;
			}
			element = element.next;
		}
		return false;
	}


	void getValueEntity(Value val, SdaiContext context) throws SdaiException {
		if (myLength == 0) {
			return;
		}
		if (myLength < 0) {
			resolveAllConnectors();
		}
		int i;
		if (myType != null && myType.shift == SdaiSession.ASDAIMODEL_INST) {
			SdaiIterator it1 = this.createIterator();
			i = 0;
			while (it1.next()) {
				EEntity inst = (EEntity)getCurrentMemberListOfModels(it1);
				val.nested_values[i].set(context, inst);
				i++;
			}
			return;
		}
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (myLength == 1) {
				val.nested_values[0].set(context, (CEntity)myData);
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				for (i = 0; i < 2; i++) {
					val.nested_values[i].set(context, (CEntity)myDataA[i]);
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
					val.nested_values[i].set(context, (CEntity)element.object);
					i++;
					element = element.next;
				}
			}
		} else {
			if (myLength == 1) {
				val.nested_values[0].set(context, (CEntity)myData);
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					val.nested_values[i].set(context, (CEntity)myDataA[i]);
				}
			}
		}
	}


	public ASdaiModel getQuerySourceDomain() throws SdaiException {
		SdaiModel sm = this.getByIndexEntity(1).findEntityInstanceSdaiModel();
		ASdaiModel domain = new ASdaiModel();
		domain.addByIndex(1, sm, null);
		return domain;
	}

	public AEntity getQuerySourceInstances() throws SdaiException {
		return this;
	}

    /**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceInstanceRef() throws SdaiException {
		SdaiIterator instanceIter = createIterator();
		EEntityRef instanceRefs[] = new EEntityRef[getMemberCount()];
		int instanceRefsIdx = 0;
		while(instanceIter.next()) {
			CEntity instance = (CEntity)getCurrentMemberEntity(instanceIter);
			SdaiModel instanceModel = instance.owning_model;
			SdaiModelRemote instModRemote = instanceModel.getModRemote();
			if(instModRemote != null) {
				instanceRefs[instanceRefsIdx++] =
					new EEntityRef(instModRemote.getRemoteRepository().getRemoteId(),
								   instModRemote.getRemoteId(),
								   instance.instance_identifier);
			} else {
				throw new SdaiException(SdaiException.FN_NAVL, "Not a remote instance: " + instance);
			}
		}
		return new AEntityRef(instanceRefs);
	}

    /**
     * @since 3.6.0
     */
    public SerializableRef getQuerySourceDomainRef() throws SdaiException{
		return null;
	}

}
