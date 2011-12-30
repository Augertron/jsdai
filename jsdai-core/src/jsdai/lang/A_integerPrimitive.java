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

/** Specialized Aggregate for members of the following 
  * EXPRESS types: integer, enumeration and boolean 
  */
abstract class A_integerPrimitive extends A_primitive {

	A_integerPrimitive() {
		super();
	}

	A_integerPrimitive(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		super((AggregationType)provided_type, instance);
	}


	boolean isMemberInternal(int value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (myLength <= 0) {
			return false;
		}
		if (myType.express_type == DataType.LIST) {
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				if (element.value == value) {
					return true;
				}
				element = element.next;
			}
			return false;
		} else {
			int [] myDataArray = (int [])myData;
			for (int i = 0; i < myLength; i++) {
				if (myDataArray[i] == value) {
					return true;
				}
			}
			return false;
		}
	}


	int getByIndexInternal(int index, int unset_value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		int value;
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			value = element.value;
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1, unset_value);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			value = ((int [])myData)[index];
		}
		if (value <= unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
	}


	void setByIndexInternal(int index,  int value, int unset_value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myType.shift == SdaiSession.PRIVATE_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
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
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			element.value = value;
		}	else if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			if (myData == null) {
				EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
				initializeData(upper_bound.getBound_value(null) - lower_index + 1, unset_value);
			}
			index -= lower_index;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			((int [])myData)[index] = value;
		} else {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	int testByIndexInternal(int index, int unset_value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
//			if (index < 0 || index >= myLength) {
//				return 0;
//			}
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			if (element.value == unset_value) {
				return 0;
			} else {
				return 2;
			}
		} else {
			if (myType.express_type == DataType.ARRAY) {
				EBound bound = ((CArray_type)myType).getLower_index(null);
				int lower_index = bound.getBound_value(null);
				index -= lower_index;
				if (myData == null) {
					EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
					initializeData(upper_bound.getBound_value(null) - lower_index + 1, unset_value);
				}
			} else {
				index--;
			}
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			if (((int [])myData)[index] == unset_value) {
				return 0;
			} else {
				return 2;
			}
		}
	}


	void unsetValueByIndexInternal(int index, int unset_value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myType.shift == SdaiSession.PRIVATE_AGGR) {
			throw new SdaiException(SdaiException.FN_NAVL);
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
		if (myType.express_type != DataType.ARRAY) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myData == null) {
			return;
		}
		EBound bound = ((CArray_type)myType).getLower_index(null);
		int lower_index = bound.getBound_value(null);
		index -= lower_index;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		((int [])myData)[index] = unset_value;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	void addByIndexInternal(int index, int value, int unset_value) throws SdaiException {
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session = null;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
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
			session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		index--;
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			EBound bound = list_type.getUpper_bound(null);
			if (myLength >= bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		if (myData == null) {
			myData = new ListElementInteger[2];
		}
		ListElementInteger [] myDataList = (ListElementInteger [])myData;
		ListElementInteger new_element = new ListElementInteger(value);
		if (myLength == 0) {
			((ListElementInteger [])myData)[0] = ((ListElementInteger [])myData)[1] = new_element;
		} else if (index == myLength) {
			((ListElementInteger [])myData)[1].next = new_element;
			((ListElementInteger [])myData)[1] = new_element;
		} else if (index == 0) {
			new_element.next = myDataList[0];
			((ListElementInteger [])myData)[0] = new_element;
		}	else {
			ListElementInteger element = myDataList[0];
			while (--index > 0) {
				element = element.next;
			}
			if (element.next != null) {
				new_element.next = element.next;
			}
			element.next = new_element;
		}
		myLength++;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		}
//		myLength++;
	}


	void removeByIndexInternal(int index) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session = null;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
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
			session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		index--;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		ListElementInteger [] myDataList = (ListElementInteger [])myData;
		if (myLength == 1) {
			((ListElementInteger [])myData)[0] = ((ListElementInteger [])myData)[1] = null;
		} else if (index == 0) {
			((ListElementInteger [])myData)[0] = myDataList[0].next;
		} else {
			ListElementInteger element = myDataList[0];
			while (--index > 0) {
				element = element.next;
			}
			if (index == myLength - 1) {
				element.next = null;
				((ListElementInteger [])myData)[1] = element;
			} else {
				element.next = element.next.next;
			}
		}
		myLength--;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		}
	}


	void addUnorderedInternal(int value, int unset_value) throws SdaiException {
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session = null;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
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
			session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myType.express_type == DataType.SET && isMemberInternal(value)) {
			return;
		}
		EVariable_size_aggregation_type aggr_type = (EVariable_size_aggregation_type)myType;
		EBound upper_bound = null;
		if (aggr_type.testUpper_bound(null)) {
			upper_bound = aggr_type.getUpper_bound(null);
			if (myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myData == null) {
			int ln;
			if (upper_bound == null) {
				ln = Integer.MAX_VALUE;
			} else {
				ln = upper_bound.getBound_value(null);
			}
			initializeData(ln, -1);
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		int [] myDataArray = (int [])myData;
		if (myLength >= myDataArray.length) {						
			ensureCapacity(myLength + 1);
			myDataArray = (int [])myData;
		}
		myDataArray[myLength] = value;
		myLength++;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		}
	}


	void removeUnorderedInternal(int value, int unset_value) throws SdaiException {
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (myType.express_type == DataType.ARRAY || myType.express_type == DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		int index = -1;
		session.undoRedoModifyPrepare(aggr_owner);
		int [] myDataArray = (int [])myData;
		for (int i = 0; i < myLength; i++) {
			if (myDataArray[i] == value) {
				index = i;
				myDataArray[i] = myDataArray[myLength - 1];
				myLength--;
//				if (myType instanceof CSet_type) {
					break;
//				} else {
//					i--;
//				}
			}
		}
		if (index < 0) {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	int testCurrentMemberInternal(SdaiIterator iter, int unset_value) throws SdaiException {
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
		int value;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null) {
//				return 0;
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((ListElementInteger)iter.myElement).value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
//				return 0;
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((int [])myData)[iter.myIndex];
		}
		if (value == unset_value) {
			return 0;
		} else {
			return 2;
		}
	}


	int getCurrentMemberInternal(SdaiIterator iter, int unset_value) throws SdaiException {
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
		int value;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((ListElementInteger)iter.myElement).value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((int [])myData)[iter.myIndex];
		}
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
	}


	void setCurrentMemberInternal(SdaiIterator it, int value, int unset_value) throws SdaiException {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType.express_type == DataType.LIST) {
			if (it.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			((ListElementInteger)it.myElement).value = value;
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (myType.express_type == DataType.SET && isMemberInternal(value)) {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			((int [])myData)[it.myIndex] = value;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	void addBeforeInternal(SdaiIterator it, int value, int unset_value) throws SdaiException {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			EBound upper_bound = list_type.getUpper_bound(null);
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myData == null) {
			myData = new ListElementInteger[2];
		}
		session.undoRedoModifyPrepare(aggr_owner);
		ListElementInteger [] myDataList = (ListElementInteger [])myData;
		ListElementInteger new_element = new ListElementInteger(value);
		if (myLength == 0) {
			((ListElementInteger [])myData)[0] = ((ListElementInteger [])myData)[1] = new_element;
			it.myElement = null;
			it.behind = true;
		} else if (it.myElement == null && it.behind) {
			((ListElementInteger [])myData)[1].next = new_element;
			((ListElementInteger [])myData)[1] = new_element;
		} else if (it.myElement == null) {
			new_element.next = myDataList[0];
			((ListElementInteger [])myData)[0] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == myDataList[0]) {
			new_element.next = myDataList[0];
			((ListElementInteger [])myData)[0] = new_element;
		}	else {
			ListElementInteger element = myDataList[0];
			while (element.next != it.myElement) {
				element=element.next;
			}
			new_element.next=element.next;
			element.next = new_element;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	void addAfterInternal(SdaiIterator it, int value, int unset_value) throws SdaiException {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (value == unset_value) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
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
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		CList_type list_type = (CList_type)myType;
		if (list_type.testUpper_bound(null)) {
			EBound upper_bound = list_type.getUpper_bound(null);
			if (upper_bound != null && myLength >= upper_bound.getBound_value(null)) {
				String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
		}
		if (myData == null) {
			myData = new ListElementInteger[2];
		}
		session.undoRedoModifyPrepare(aggr_owner);
		ListElementInteger [] myDataList = (ListElementInteger [])myData;
		ListElementInteger new_element = new ListElementInteger(value);
		
		ListElement element;

		if (myLength == 0) {
			((ListElementInteger [])myData)[0] = ((ListElementInteger [])myData)[1] = new_element;
			it.myElement = null;
			it.behind = false;
		} else if (it.myElement == null && it.behind) {
			((ListElementInteger [])myData)[1].next = new_element;
			((ListElementInteger [])myData)[1] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == null) {
			new_element.next = myDataList[0];
			((ListElementInteger [])myData)[0] = new_element;
		} else if (it.myElement == myDataList[1]) {
			((ListElementInteger [])myData)[1].next = new_element;
			((ListElementInteger [])myData)[1] = new_element;
		}	else {
			new_element.next = ((ListElementInteger)it.myElement).next;
			((ListElementInteger)it.myElement).next = new_element;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);		
	}


	void clearInternal(int unset_value) throws SdaiException {
		if (myType == null) {
			throw new SdaiException(SdaiException.FN_NAVL);
		}
		CEntity aggr_owner = null;
		SdaiModel owning_model = null;
		SdaiSession session = null;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
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
			session = owning_model.repository.session;
			if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
				String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
//			owner.modified();
//			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		if (myType.express_type == DataType.LIST) {
			myLength = 0;
			if (myData != null) {
				((ListElementInteger [])myData)[0] = null;
				((ListElementInteger [])myData)[1] = null;
			}
		} else if (myType.express_type == DataType.ARRAY) {
			int [] myDataArray = (int [])myData;
			for (int i = 0; i < myLength; i++) {
				myDataArray[i] = unset_value;
			}
		} else {
			myLength = 0;
		}
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);	
		}
	}



	void initializeData(int bound, int unset_value) {
		if (myData != null) {
			return;
		}
		myLength = 0;
		if (bound == Integer.MAX_VALUE) {
			myData = new int[2];
		} else {
			myData = new int[bound];
			if (myType.express_type == DataType.ARRAY) {
				int [] myDataArray = (int [])myData;
				for (int i = 0; i < bound; i++) {
					myDataArray[i] = unset_value;
				}
				myLength = bound;
			}
		}
	}


	void ensureCapacity(int demand) {
		int [] myDataArray = (int [])myData;
		int new_length = myDataArray.length * 2;
		if (demand > new_length) {
			new_length = demand;
		}
		int [] new_array = new int[new_length];
		System.arraycopy(myDataArray, 0, new_array, 0, myLength);
		myData = new_array;
	}


	void setValueInternal(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, int unset_value) throws SdaiException {
		myType = aggr_type;
		owner = inst;
		int type;
		if (aggr_type.express_type == DataType.LIST) {
			type = 1;
		} else {
			type = 0;
			if (myType.express_type != DataType.ARRAY) {
				myLength = 0;
			}
		}
		DataType element_type = (DataType)aggr_type.getElement_type(null);
		while (element_type.express_type == DataType.DEFINED_TYPE) {
			element_type = (DataType)((CDefined_type)element_type).getDomain(null);
		}
		if (element_type.express_type != DataType.INTEGER) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		for (int i = 0; i < val.length; i++) {
			int for_adding = val.nested_values[i].integer;
			if (for_adding == unset_value) {
				continue;
			}
			switch (type) {
			case 0:  
				setForNonList(for_adding, i, unset_value, inst);
				break;
			case 1:  
				addAtTheEnd(for_adding);
				break;
			}
		}
	}


	void setForNonList(int value, int index, int unset_value, CEntity inst) throws SdaiException {
		int [] myDataArray;
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			EBound upper_bound = ((CArray_type)myType).getUpper_index(null);
			int upper_index = upper_bound.getBound_value(null);
			if (myData == null) {
				initializeData(upper_index - lower_index + 1, unset_value);
			}
			if (index > upper_index - lower_index) {
				if (inst != null) {
					StaticFields staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_EXAR, 
						inst.instance_identifier, staticFields.current_attribute);
				}
				return;
			}
//			index -= lower_index;
		} else {
			if (myData == null) {
				int ln;
				EVariable_size_aggregation_type size_aggr_type = (EVariable_size_aggregation_type)myType;
				if (size_aggr_type.testUpper_bound(null)) {
					EBound upper_bound = size_aggr_type.getUpper_bound(null);
					ln = upper_bound.getBound_value(null);
				} else {
					ln = Integer.MAX_VALUE;
				}
				initializeData(ln, -1);
			}
			myDataArray = (int [])myData;
			if (myLength >= myDataArray.length) {						
				ensureCapacity(myLength + 1);
			}
			index = myLength;
			myLength++;
		}
		myDataArray = (int [])myData;
		myDataArray[index] = value;
	}


	void addAtTheEnd(int value) throws SdaiException {
		if (myData == null) {
			myData = new ListElementInteger[2];
		}
		ListElementInteger new_element = new ListElementInteger(value);
		if (myLength == 0) {
			((ListElementInteger [])myData)[0] = ((ListElementInteger [])myData)[1] = new_element;
		} else {
			((ListElementInteger [])myData)[1].next = new_element;
			((ListElementInteger [])myData)[1] = new_element;
		}
		myLength++;
	}


	boolean checkUniquenessViolation(AggregationType aggr_type) throws SdaiException {
		if (aggr_type.express_type == DataType.BAG) {
			return false;
		}
		int j;
		if (aggr_type.express_type == DataType.LIST) {
			if (!((CList_type)aggr_type).getUnique_flag(null)) {
				return false;
			}
			if (myLength <= 1) {
				return false;
			}
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			j = 0;
			element = element.next;
			while (element != null) {
				if (check_if_set(element.value) && check_list_for_uniqueness(element.value, j)) {
					return true;
				}
				j++;
				element = element.next;
			}
		} else {
			if ((aggr_type.express_type == DataType.ARRAY) && 
				(!((CArray_type)aggr_type).getUnique_flag(null))) {
				return false;
			}
			int [] myDataArray = (int [])myData;
			for (j = 1; j < myLength; j++) {
				if (!check_if_set(myDataArray[j])) {
					continue;
				}
				for (int k = 0; k < j; k++) {
					if (myDataArray[k] == myDataArray[j]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private boolean check_if_set(int value) {
		if (this instanceof A_integer) {
			if (value == Integer.MIN_VALUE) {
				return false;
			}
		} else {
			if (value == 0) {
				return false;
			}
		}
		return true;
	}
	private boolean check_list_for_uniqueness(int value, int last_index) {
		int i = 0;
		ListElementInteger [] myDataList = (ListElementInteger [])myData;
		ListElementInteger element = myDataList[0];
		while (i <= last_index) {
			if (element.value == value) {
				return true;
			}
			i++;
			element = element.next;
		}
		return false;
	}


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
		int [] myDataArray = (int [])myData;
		for (int j = 0; j < myLength; j++) {
			if (!check_if_set(myDataArray[j])) {
				return true;
			}
		}
		return false;
	}


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
		if (myType == null) {
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

		boolean first = true;
		int str_index = -1;
		int type;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		if (myLength <= 0) {
			staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
			return new String(staticFields.instance_as_string, 0, str_index + 1);
		}
		if (this instanceof A_integer) {
			type = 1;
		} else if (this instanceof A_enumeration) {
			DataType el_type = (DataType)myType.getElement_type(null);
			while (el_type.express_type == DataType.DEFINED_TYPE) {
				el_type = (DataType)((CDefined_type)el_type).getDomain(null);
			}
			if (el_type.express_type == DataType.LOGICAL) {
				type = 3;
			} else {
				type = 2;
			}
		} else if (this instanceof A_boolean) {
			type = 4;
		} else {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		if (myType.express_type == DataType.LIST) {
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			while (element != null) {
				str_index = get_value(staticFields, first, element.value, type, str_index);
				first = false;
				element = element.next;
			}
		} else {
			int [] myDataArray = (int [])myData;
			for (int i = 0; i < myLength; i++) {
				if (myDataArray == null) {
					str_index = get_value(staticFields, first, 0, -1, str_index);
				} else {
					str_index = get_value(staticFields, first, myDataArray[i], type, str_index);
				}
				first = false;
			}
		}
		if (str_index + 1 >= staticFields.instance_as_string.length) {
			enlarge_instance_string(staticFields, str_index + 1, str_index + 2);
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}


	private int get_value(StaticFields staticFields, boolean first, int i_value, int type, int index) throws SdaiException {
		boolean missing = false;
		switch (type) {
			case -1:
				missing = true;
				break;
			case 1:
				if (i_value == Integer.MIN_VALUE) {
					missing = true;
					break;
				}
				if (!first) {
					if (index + 1 >= staticFields.instance_as_string.length) {
						enlarge_instance_string(staticFields, index + 1, index + 2);
					}
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				index = convert_integer(staticFields, i_value, index);
				break;
			case 2:
				if (i_value == 0) {
					missing = true;
					break;
				}
				String str = ((A_enumeration)this).getValue(i_value);
				if (index + str.length() + 3 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + str.length() + 4);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				staticFields.instance_as_string[++index] = PhFileReader.DOT;
				for (int i = 0; i < str.length(); i++) {
					staticFields.instance_as_string[++index] = (byte)str.charAt(i);
				}
				staticFields.instance_as_string[++index] = PhFileReader.DOT;
				break;
			case 3:
				if (i_value == 0) {
					missing = true;
					break;
				}
				if (index + 4 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 5);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (i_value == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (i_value == 2) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				} else if (i_value == 3) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_UNKNOWN[i];
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
			case 4:
				if (i_value == 0) {
					missing = true;
					break;
				}
				if (index + 4 >= staticFields.instance_as_string.length) {
					enlarge_instance_string(staticFields, index + 1, index + 5);
				}
				if (!first) {
					staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
				}
				if (i_value == 1) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_FALSE[i];
					}
				} else if (i_value == 2) {
					for (int i = 0; i < 3; i++) {
						staticFields.instance_as_string[++index] = PhFileWriter.LOG_TRUE[i];
					}
				} else {
					throw new SdaiException(SdaiException.SY_ERR);
				}
				break;
		}
		if (missing) {
			if (index + 2 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 3);
			}
			if (!first) {
				staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
			}
			staticFields.instance_as_string[++index] = PhFileReader.DOLLAR_SIGN;
		}
		return index;
	}


	private int convert_integer(StaticFields staticFields, int integ, int index) {
		boolean neg;
		int number, next_number;
		if (integ < 0) {
			neg = true;
			number = -integ;
		} else if (integ > 0) {
			neg = false;
			number = integ;
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
			staticFields.instance_as_string[++index] = PhFileWriter.DIGITS[number - next_number * 10];
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



}
