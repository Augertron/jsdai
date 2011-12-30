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
 Specialized class implementing <code>Aggregate</code> for members of the
 EXPRESS type real. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_double extends A_primitive {

	A_double() {
		super();
	}

	A_double(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		super((AggregationType)provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(double, EDefined_type []) isMember(double, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public boolean isMember(double value) throws SdaiException {
//		synchronized (syncObject) {
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
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (element != null) {
				if (element.value == value) {
					return true;
				}
				element = element.next;
			}
			return false;
		} else {
			double [] myDataArray = (double [])myData;
			for (int i = 0; i < myLength; i++) {
				if (myDataArray[i] == value) {
					return true;
				}
			}
			return false;
		}
//		} // syncObject
	}
	public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
		return isMember(value);
	}

	public boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
		return value instanceof Double ? isMember(((Double)value).doubleValue()) : false;
	}


/**
 * It is {@link Aggregate#getByIndexDouble getByIndexDouble} method
 * under the different name.
 */
	public double getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			if (Double.isNaN(element.value)) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return element.value;
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
			double [] myDataArray = (double [])myData;
			if (Double.isNaN(myDataArray[index])) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			return myDataArray[index];
		}
//		} // syncObject
	}

	public double getByIndexDouble(int index) throws SdaiException {
		return getByIndex(index);
	}


	public int getByIndexInt(int index) throws SdaiException {
//		synchronized (syncObject) {
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
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			if (Double.isNaN(element.value)) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if ((element.value - (int)element.value) != 0.) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return (int)element.value;
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
			double [] myDataArray = (double [])myData;
			if (Double.isNaN(myDataArray[index])) {
				throw new SdaiException(SdaiException.VA_NSET);
			}
			if ((myDataArray[index] - (int)myDataArray[index]) != 0.) {
				throw new SdaiException(SdaiException.VT_NVLD);
			}
			return (int)myDataArray[index];
		}
//		} // syncObject
	}

	public Object getByIndexObject(int index) throws SdaiException {
		return new Double(getByIndex(index));
	}


/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, double, EDefined_type []) setByIndex(int, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public void setByIndex(int index,  double value) throws SdaiException {
//		synchronized (syncObject) {
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
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType.express_type == DataType.LIST) {
			index--;
			if (index < 0 || index >= myLength) {
				throw new SdaiException(SdaiException.IX_NVLD, this);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			element.value = value;
		}	else if (myType.express_type == DataType.ARRAY) {
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
			session.undoRedoModifyPrepare(aggr_owner);
			double [] myDataArray = (double [])myData;
			myDataArray[index] = value;
		} else {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		setByIndex(index, value);
	}

	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		setByIndex(index, valueDouble);
	}

	public int testByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
				return 0;
			}
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (index-- > 0) {
				element = element.next;
			}
			if (Double.isNaN(element.value)) {
				return 0;
//			} else if ((element.value - (int)element.value) != 0.) {
			} else {
				return 3;
//			} else {
//				return 2;
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
			double [] myDataArray = (double [])myData;
			if (Double.isNaN(myDataArray[index])) {
				return 0;
			} else if ((myDataArray[index] - (int)myDataArray[index]) != 0.) {
				return 3;
			} else {
				return 2;
			}
		}
//		} // syncObject
	}
	public int testByIndex(int index, EDefined_type select[]) throws SdaiException {
		return testByIndex(index);
	}


	public void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
		if (myType.express_type != DataType.ARRAY) {
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
		EBound bound = ((CArray_type)myType).getLower_index(null);
		int lower_index = bound.getBound_value(null);
		index -= lower_index;
		if (index < 0 || index >= myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		double [] myDataArray = (double [])myData;
		myDataArray[index] = Double.NaN;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, double, EDefined_type []) addByIndex(int, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public void addByIndex(int index, double value) throws SdaiException {
//		synchronized (syncObject) {
		if (Double.isNaN(value)) {
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
		if (myData == null) {
			myData = new ListElementDouble[2];
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		ListElementDouble [] myDataList = (ListElementDouble [])myData;
		ListElementDouble new_element = new ListElementDouble(value);
		if (myLength == 0) {
			((ListElementDouble [])myData)[0] = ((ListElementDouble [])myData)[1] = new_element;
		} else if (index == myLength) {
			((ListElementDouble [])myData)[1].next = new_element;
			((ListElementDouble [])myData)[1] = new_element;
		} else if (index == 0) {
			new_element.next = myDataList[0];
			((ListElementDouble [])myData)[0] = new_element;
		}	else {
			ListElementDouble element = myDataList[0];
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
//		} // syncObject
	}
	public void addByIndex(int index, double value, EDefined_type select[])
			throws SdaiException {
		addByIndex(index, value);
	}

	public void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addByIndex(index, valueDouble);
	}

	public void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
		ListElementDouble [] myDataList = (ListElementDouble [])myData;
		if (myLength == 1) {
			((ListElementDouble [])myData)[0] = ((ListElementDouble [])myData)[1] = null;
		} else if (index == 0) {
			((ListElementDouble [])myData)[0] = myDataList[0].next;
		} else {
			ListElementDouble element = myDataList[0];
			while (--index > 0) {
				element = element.next;
			}
			if (index == myLength - 1) {
				element.next = null;
				((ListElementDouble [])myData)[1] = element;
			} else {
				element.next = element.next.next;
			}
		}
		myLength--;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		}
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(double, EDefined_type []) addUnordered(double, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public void addUnordered(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (Double.isNaN(value)) {
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
		if (myType.express_type == DataType.SET && isMember(value)) {
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
			initializeData(ln);
		}
		if (aggr_owner != null) {
			session.undoRedoModifyPrepare(aggr_owner);
		}
		double [] myDataArray = (double [])myData;
		if (myLength >= myDataArray.length) {						
			ensureCapacity(myLength + 1);
			myDataArray = (double [])myData;
		}
		myDataArray[myLength] = value;
		myLength++;
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
		}
//		} // syncObject
	}
	public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
		addUnordered(value);
	}

	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addUnordered(valueDouble);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(double, EDefined_type []) removeUnordered(double, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public void removeUnordered(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (Double.isNaN(value)) {
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
		double [] myDataArray = (double [])myData;
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
//		} // syncObject
	}
	public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
		removeUnordered(value);
	}

	public void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		removeUnordered(valueDouble);
	}

	public int testCurrentMember(SdaiIterator iter) throws SdaiException {
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
		double value;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null) {
//				return 0;
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((ListElementDouble)iter.myElement).value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
//				return 0;
				throw new SdaiException(SdaiException.IR_NSET, iter);

			}
			double [] myDataArray = (double [])myData;
			value = myDataArray[iter.myIndex];
		}
		if (Double.isNaN(value)) {
			return 0;
//		} else if ((value - (int)value) != 0.) {
		} else {
			return 3;
//		} else {
//			return 2;
		}
//		} // syncObject
	}
	public int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
		return testCurrentMember(iter);
	}


/**
 * It is {@link Aggregate#getCurrentMemberDouble getCurrentMemberDouble} method
 * under the different name.
 */
	public double getCurrentMember(SdaiIterator iter) throws SdaiException {
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
		double value;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((ListElementDouble)iter.myElement).value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			double [] myDataArray = (double [])myData;
			value = myDataArray[iter.myIndex];
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return value;
//		} // syncObject
	}
	public double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException {
		return getCurrentMember(iter);
	}

	public Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
		return new Double(getCurrentMember(iter));
	}

	public int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
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
		double value;
		if (myType.express_type == DataType.LIST) {
			if (iter.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			value = ((ListElementDouble)iter.myElement).value;
		} else {
			if (iter.myIndex < 0 || iter.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, iter);
			}
			double [] myDataArray = (double [])myData;
			value = myDataArray[iter.myIndex];
		}
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		if ((value - (int)value) != 0.) {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
		return (int)value;
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, double, EDefined_type []) setCurrentMember(SdaiIterator, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public void setCurrentMember(SdaiIterator it, double value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (Double.isNaN(value)) {
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
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType.express_type == DataType.LIST) {
			if (it.myElement == null) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			((ListElementDouble)it.myElement).value = value;
		} else {
			if (it.myIndex < 0 || it.myIndex >= myLength) {
				throw new SdaiException(SdaiException.IR_NSET, it);
			}
			if (myType.express_type == DataType.SET && isMember(value)) {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			session.undoRedoModifyPrepare(aggr_owner);
			double [] myDataArray = (double [])myData;
			myDataArray[it.myIndex] = value;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public void setCurrentMember(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		setCurrentMember(it, value);
	}

	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		setCurrentMember(it, valueDouble);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addBefore(SdaiIterator, double, EDefined_type []) addBefore(SdaiIterator, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public void addBefore(SdaiIterator it, double value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (Double.isNaN(value)) {
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
			myData = new ListElementDouble[2];
		}
		session.undoRedoModifyPrepare(aggr_owner);
		ListElementDouble [] myDataList = (ListElementDouble [])myData;
		ListElementDouble new_element = new ListElementDouble(value);
		if (myLength == 0) {
			((ListElementDouble [])myData)[0] = ((ListElementDouble [])myData)[1] = new_element;
			it.myElement = null;
			it.behind = true;
		} else if (it.myElement == null && it.behind) {
			((ListElementDouble [])myData)[1].next = new_element;
			((ListElementDouble [])myData)[1] = new_element;
		} else if (it.myElement == null) {
			new_element.next = myDataList[0];
			((ListElementDouble [])myData)[0] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == myDataList[0]) {
			new_element.next = myDataList[0];
			((ListElementDouble [])myData)[0] = new_element;
		}	else {
			ListElementDouble element = myDataList[0];
			while (element.next != it.myElement) {
				element=element.next;
			}
			new_element.next=element.next;
			element.next = new_element;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);	
//		} // syncObject	
	}
	public void addBefore(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		addBefore(it, value);
	}

	public void addBefore(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addBefore(it, valueDouble);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addAfter(SdaiIterator, double, EDefined_type []) addAfter(SdaiIterator, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public void addAfter(SdaiIterator it, double value) throws SdaiException {
//		synchronized (syncObject) {
		if (it == null) {
			throw new SdaiException(SdaiException.IR_NEXS);
		}
		if (Double.isNaN(value)) {
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
			myData = new ListElementDouble[2];
		}
		session.undoRedoModifyPrepare(aggr_owner);
		ListElementDouble [] myDataList = (ListElementDouble [])myData;
		ListElementDouble new_element = new ListElementDouble(value);
		if (myLength == 0) {
			((ListElementDouble [])myData)[0] = ((ListElementDouble [])myData)[1] = new_element;
			it.myElement = null;
			it.behind = false;
		} else if (it.myElement == null && it.behind) {
			((ListElementDouble [])myData)[1].next = new_element;
			((ListElementDouble [])myData)[1] = new_element;
			it.myElement = new_element;
		} else if (it.myElement == null) {
			new_element.next = myDataList[0];
			((ListElementDouble [])myData)[0] = new_element;
		} else if (it.myElement == myDataList[1]) {
			((ListElementDouble [])myData)[1].next = new_element;
			((ListElementDouble [])myData)[1] = new_element;
		}	else {
			new_element.next = ((ListElementDouble)it.myElement).next;
			((ListElementDouble)it.myElement).next = new_element;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public void addAfter(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		addAfter(it, value);
	}

	public void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addAfter(it, valueDouble);
	}

	public void clear() throws SdaiException {
//		synchronized (syncObject) {
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
				((ListElementDouble [])myData)[0] = null;
				((ListElementDouble [])myData)[1] = null;
			}
		} else if (myType.express_type == DataType.ARRAY) {
			double [] myDataArray = (double [])myData;
			for (int i = 0; i < myLength; i++) {
				myDataArray[i] = Double.NaN;
			}
		} else {
			myLength = 0;
		}
		if (myType.shift != SdaiSession.PRIVATE_AGGR) {
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		}
//		} // syncObject
	}



	void initializeData(int bound) {
		if (myData != null) {
			return;
		}
		myLength = 0;
		if (bound == Integer.MAX_VALUE) {
			myData = new double[2];
		} else {
			myData = new double[bound];
			if (myType.express_type == DataType.ARRAY) {
				double [] myDataArray = (double [])myData;
				for (int i = 0; i < bound; i++) {
					myDataArray[i] = Double.NaN;
				}
				myLength = bound;
			}
		}
	}


	void ensureCapacity(int demand) {
		double [] myDataArray = (double [])myData;
		int new_length = myDataArray.length * 2;
		if (demand > new_length) {
			new_length = demand;
		}
		double [] new_array = new double[new_length];
		System.arraycopy(myDataArray, 0, new_array, 0, myLength);
		myData = new_array;
	}


	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
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
		if (element_type.express_type != DataType.NUMBER && element_type.express_type != DataType.REAL) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		for (int i = 0; i < val.length; i++) {
			double for_adding = val.nested_values[i].real;
			if (Double.isNaN(for_adding)) {
				continue;
			}
			switch (type) {
			case 0:  
				setForNonList(for_adding, i, inst);
				break;
			case 1:  
				addAtTheEnd(for_adding);
				break;
			}
		}
	}


	void setForNonList(double value, int index, CEntity inst) throws SdaiException {
		double [] myDataArray;
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
				initializeData(ln);
			}
			myDataArray = (double [])myData;
			if (myLength >= myDataArray.length) {						
				ensureCapacity(myLength + 1);
			}
			index = myLength;
			myLength++;
		}
		myDataArray = (double [])myData;
		myDataArray[index] = value;
	}


	void addAtTheEnd(double value) throws SdaiException {
		if (myData == null) {
			myData = new ListElementDouble[2];
		}
		ListElementDouble new_element = new ListElementDouble(value);
		if (myLength == 0) {
			((ListElementDouble [])myData)[0] = ((ListElementDouble [])myData)[1] = new_element;
		} else {
			((ListElementDouble [])myData)[1].next = new_element;
			((ListElementDouble [])myData)[1] = new_element;
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
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			j = 0;
			element = element.next;
			while (element != null) {
				if (!Double.isNaN(element.value) && check_list_for_uniqueness(element.value, j)) {
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
			double [] myDataArray = (double [])myData;
			for (j = 1; j < myLength; j++) {
				if (Double.isNaN(myDataArray[j])) {
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
	private boolean check_list_for_uniqueness(double value, int last_index) {
		int i = 0;
		ListElementDouble [] myDataList = (ListElementDouble [])myData;
		ListElementDouble element = myDataList[0];
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
		double [] myDataArray = (double [])myData;
		for (int j = 0; j < myLength; j++) {
			if (Double.isNaN(myDataArray[j])) {
				return true;
			}
		}
		return false;
	}


/**
 * Returns a <code>String</code> representing this aggregate instance.

 * <P><B>Example:</B>
 * <P><TT><pre>    Aggregate agg = ...;
 *    System.out.println("aggregate: " + agg);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    aggregate: (9.05,3.0,5.67,0.52);</pre>

 * @return the <code>String</code> representing this aggregate instance.
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

		StaticFields staticFields = StaticFields.get();
		boolean first = true;
		int str_index = -1;
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		if (myLength <= 0) {
			staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
			return new String(staticFields.instance_as_string, 0, str_index + 1);
		}
		if (myType.express_type == DataType.LIST) {
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			while (element != null) {
				str_index = get_value(staticFields, first, element.value, str_index);
				first = false;
				element = element.next;
			}
		} else {
			double [] myDataArray = (double [])myData;
			for (int i = 0; i < myLength; i++) {
				if (myDataArray == null) {
					str_index = get_value(staticFields, first, Double.NaN, str_index);
				} else {
					str_index = get_value(staticFields, first, myDataArray[i], str_index);
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


	private int get_value(StaticFields staticFields, boolean first, double d_value, int index) {
		if (Double.isNaN(d_value)) {
			if (index + 2 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + 3);
			}
			if (!first) {
				staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
			}
			staticFields.instance_as_string[++index] = PhFileReader.DOLLAR_SIGN;
		} else {
			String str = Double.toString(d_value);
			if (index + str.length() + 1 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(staticFields, index + 1, index + str.length() + 2);
			}
			if (!first) {
				staticFields.instance_as_string[++index] = PhFileReader.COMMA_b;
			}
			for (int i = 0; i < str.length(); i++) {
				staticFields.instance_as_string[++index] = (byte)str.charAt(i);
			}
		}
		return index;
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		int i;
		if (aggr_type.express_type == DataType.LIST) {
			ListElementDouble [] myDataList = (ListElementDouble [])myData;
			ListElementDouble element = myDataList[0];
			i = 0;
			while (element != null) {
				val.nested_values[i++].set(context, element.value);
				element = element.next;
			}
		} else {
			double [] myDataArray = (double [])myData;
			for (i = 0; i < myLength; i++) {
				val.nested_values[i].set(context, myDataArray[i]);
			}
		}
	}


}
