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
 Specialized class implementing <code>Aggregate</code> containing at most three 
 members of the EXPRESS type real. See <a href="Aggregate.html">Aggregate</a> for 
 detailed description of methods whose specializations are given here.
 */
class A_double3 extends A_double {

	double double1;
	double double2;
	double double3;

	static final int NO_ELEMENT_BEFORE   = 0;

	A_double3() {
		super();
	}

	A_double3(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		super((AggregationType)provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(double, EDefined_type []) isMember(double, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final boolean isMember(double value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
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
		switch(myLength) {
			case 0:
				return false;
			case 1:
				if (value == double1) {
					return true;
				}
				return false;
			case 2:
				if (value == double1 || value == double2) {
					return true;
				}
				return false;
			case 3:
				if (value == double1 || value == double2 || value == double3) {
					return true;
				}
				return false;
		}
		return false;
//		} // syncObject
	}
	public final boolean isMember(double value, EDefined_type select[]) throws SdaiException {
		return isMember(value);
	}

	public final boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
		return value instanceof Double ? isMember(((Double)value).doubleValue()) : false;
	}


/**
 * It is {@link Aggregate#getByIndexDouble getByIndexDouble} method
 * under the different name.
 */
	public final double getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
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
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			index -= (lower_index - 1);
		}
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		switch(index) {
			case 1:
				if (Double.isNaN(double1)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double1;
			case 2:
				if (Double.isNaN(double2)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double2;
			case 3:
				if (Double.isNaN(double3)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double3;
		}
		return Double.NaN;
//		} // syncObject
	}

	public final double getByIndexDouble(int index) throws SdaiException {
		return getByIndex(index);
	}


	public final int getByIndexInt(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
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
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			index -= (lower_index - 1);
		}
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		switch(index) {
			case 1:
				if (Double.isNaN(double1)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double1 - (int)double1) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double1;
			case 2:
				if (Double.isNaN(double2)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double2 - (int)double2) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double2;
			case 3:
				if (Double.isNaN(double3)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double3 - (int)double3) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double3;
		}
		return Integer.MIN_VALUE;
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
	public final void setByIndex(int index,  double value) throws SdaiException {
//		synchronized (syncObject) {
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
		if (Double.isNaN(value)) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		SdaiSession session = owning_model.repository.session;
		if (session.undo_redo_file != null && session.forbid_undo_on_SdaiEvent) {
			String base = SdaiSession.line_separator + AdditionalMessages.UR_MOPO;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (!(myType.express_type == DataType.LIST || myType.express_type == DataType.ARRAY)) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			index -= (lower_index - 1);
		}
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(index) {
			case 1:
				double1 = value;
				break;
			case 2:
				double2 = value;
				break;
			case 3:
				double3 = value;
				break;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public final void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		setByIndex(index, value);
	}

	public final void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		setByIndex(index, valueDouble);
	}

	public final int testByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
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
		if (myType.express_type == DataType.ARRAY) {
			EBound bound = ((CArray_type)myType).getLower_index(null);
			int lower_index = bound.getBound_value(null);
			index -= (lower_index - 1);
		}
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		switch(index) {
			case 1:
				if (Double.isNaN(double1)) {
					return 0;
				} else {
					return 3;
				}
			case 2:
				if (Double.isNaN(double2)) {
					return 0;
				} else {
					return 3;
				}
			case 3:
				if (Double.isNaN(double3)) {
					return 0;
				} else {
					return 3;
				}
		}
		return 0;
//		} // syncObject
	}
	public final int testByIndex(int index, EDefined_type select[]) throws SdaiException {
		return testByIndex(index);
	}


	public final void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
		if (myType.express_type != DataType.ARRAY) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		EBound bound = ((CArray_type)myType).getLower_index(null);
		int lower_index = bound.getBound_value(null);
		index -= (lower_index - 1);
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(index) {
			case 1:
				double1 = Double.NaN;
				break;
			case 2:
				double2 = Double.NaN;
				break;
			case 3:
				double3 = Double.NaN;
				break;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, double, EDefined_type []) addByIndex(int, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addByIndex(int index, double value) throws SdaiException {
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
		if (myType.express_type != DataType.LIST) {
			throw new SdaiException(SdaiException.AI_NVLD, this);
		}
		if (myLength >= 3) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (index <= 0 || index > myLength+1) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(myLength) {
			case 0:
				double1 = value;
				break;
			case 1:
				if (index == 1) {
					double2 = double1;
					double1 = value;
				} else {
					double2 = value;
				}
				break;
			case 2:
				switch(index) {
					case 1:
						double3 = double2;
						double2 = double1;
						double1 = value;
						break;
					case 2:
						double3 = double2;
						double2 = value;
						break;
					case 3:
						double3 = value;
						break;
				}
				break;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
//		myLength++;
//		} // syncObject
	}
	public final void addByIndex(int index, double value, EDefined_type select[])
			throws SdaiException {
		addByIndex(index, value);
	}

	public final void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addByIndex(index, valueDouble);
	}

	public final void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
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
		if (index <= 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(myLength) {
			case 2:
				if (index == 1) {
					double1 = double2;
				}
				break;
			case 3:
				if (index == 1) {
					double1 = double2;
					double2 = double3;
				} else if (index == 2) {
					double2 = double3;
				}
				break;
		}
		myLength--;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(double, EDefined_type []) addUnordered(double, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final void addUnordered(double value) throws SdaiException {
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
		if (myLength >= 3) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (myType.express_type == DataType.SET && isMember(value)) {
			return;
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(myLength) {
			case 0:
				double1 = value;
				break;
			case 1:
				double2 = value;
				break;
			case 2:
				double3 = value;
				break;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);			
//		} // syncObject
	}
	public final void addUnordered(double value, EDefined_type select[]) throws SdaiException {
		addUnordered(value);
	}

	public final void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
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
	public final void removeUnordered(double value) throws SdaiException {
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
		session.undoRedoModifyPrepare(aggr_owner);
		boolean found = false;
		switch(myLength) {
			case 1:
				if (double1 == value) {
					found = true;
				}
				break;
			case 2:
				if (double1 == value) {
					double1 = double2;
					found = true;
				} else if (double2 == value) {
					found = true;
				}
				break;
			case 3:
				if (double1 == value) {
					double1 = double3;
					found = true;
				} else if (double2 == value) {
					double2 = double3;
					found = true;
				} else if (double3 == value) {
					found = true;
				}
				break;
		}
		if (found) {
			myLength--;
			owner.modified();
			fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
		} else {
			throw new SdaiException(SdaiException.VA_NEXS);
		}
//		} // syncObject
	}
	public final void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
		removeUnordered(value);
	}

	public final void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		removeUnordered(valueDouble);
	}

	public final int testCurrentMember(SdaiIterator iter) throws SdaiException {
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
		if (iter.myIndex <= 0 || iter.myIndex > myLength) {
			throw new SdaiException(SdaiException.IR_NSET, iter);
		}
		switch(iter.myIndex) {
			case 1:
				if (Double.isNaN(double1)) {
					return 0;
				} else {
					return 3;
				}
			case 2:
				if (Double.isNaN(double2)) {
					return 0;
				} else {
					return 3;
				}
			case 3:
				if (Double.isNaN(double3)) {
					return 0;
				} else {
					return 3;
				}
		}
		return 0;
//		} // syncObject
	}
	public final int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
		return testCurrentMember(iter);
	}


/**
 * It is {@link Aggregate#getCurrentMemberDouble getCurrentMemberDouble} method
 * under the different name.
 */
	public final double getCurrentMember(SdaiIterator iter) throws SdaiException {
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
		if (iter.myIndex <= 0 || iter.myIndex > myLength) {
			throw new SdaiException(SdaiException.IR_NSET, iter);
		}
		switch(iter.myIndex) {
			case 1:
				if (Double.isNaN(double1)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double1;
			case 2:
				if (Double.isNaN(double2)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double2;
			case 3:
				if (Double.isNaN(double3)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				return double3;
		}
		return Double.NaN;
//		} // syncObject
	}
	public final double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException {
		return getCurrentMember(iter);
	}

	public final Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
		return new Double(getCurrentMember(iter));
	}

	public final int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
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
		if (iter.myIndex <= 0 || iter.myIndex > myLength) {
			throw new SdaiException(SdaiException.IR_NSET, iter);
		}
		switch(iter.myIndex) {
			case 1:
				if (Double.isNaN(double1)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double1 - (int)double1) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double1;
			case 2:
				if (Double.isNaN(double2)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double2 - (int)double2) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double2;
			case 3:
				if (Double.isNaN(double3)) {
					throw new SdaiException(SdaiException.VA_NSET);
				}
				if ((double3 - (int)double3) != 0.) {
					throw new SdaiException(SdaiException.VT_NVLD);
				}
				return (int)double3;
		}
		return Integer.MIN_VALUE;
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, double, EDefined_type []) setCurrentMember(SdaiIterator, double, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void setCurrentMember(SdaiIterator it, double value) throws SdaiException {
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
		if (it.myAggregate != this) {
			String base = SdaiSession.line_separator + AdditionalMessages.IR_IMPR;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		if (it.myIndex <= 0 || it.myIndex > myLength) {
			throw new SdaiException(SdaiException.IR_NSET, it);
		}
		if (myType.express_type == DataType.SET && isMember(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(it.myIndex) {
			case 1:
				double1 = value;
				break;
			case 2:
				double2 = value;
				break;
			case 3:
				double3 = value;
				break;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public final void setCurrentMember(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		setCurrentMember(it, value);
	}

	public final void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
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
	public final void addBefore(SdaiIterator it, double value) throws SdaiException {
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
		if (myLength >= 3) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(myLength) {
			case 0:
				double1 = value;
				it.myIndex = 2;
				break;
			case 1:
				switch(it.myIndex) {
					case NO_ELEMENT_BEFORE:
						double2 = double1;
						double1 = value;
						it.myIndex = 1;
						break;
					case 1:
						double2 = double1;
						double1 = value;
						it.myIndex = 2;
						break;
					case 2:
						double2 = value;
						it.myIndex = 3;
						break;
				}
				break;
			case 2:
				switch(it.myIndex) {
					case NO_ELEMENT_BEFORE:
						double3 = double2;
						double2 = double1;
						double1 = value;
						it.myIndex = 1;
						break;
					case 1:
						double3 = double2;
						double2 = double1;
						double1 = value;
						it.myIndex = 2;
						break;
					case 2:
						double3 = double2;
						double2 = value;
						it.myIndex = 3;
						break;
					case 3:
						double3 = value;
						it.myIndex = 4;
						break;
				}
				break;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);	
//		} // syncObject	
	}
	public final void addBefore(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		addBefore(it, value);
	}

	public final void addBefore(SdaiIterator it, Object value, EDefined_type select[])
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
	public final void addAfter(SdaiIterator it, double value) throws SdaiException {
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
		if (myLength >= 3) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_BVL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		session.undoRedoModifyPrepare(aggr_owner);
		switch(myLength) {
			case 0:
				double1 = value;
				it.myIndex = NO_ELEMENT_BEFORE;
				break;
			case 1:
				switch(it.myIndex) {
					case NO_ELEMENT_BEFORE:
						double2 = double1;
						double1 = value;
						break;
					case 1:
						double2 = value;
						break;
					case 2:
						double2 = value;
						it.myIndex = 2;
						break;
				}
				break;
			case 2:
				switch(it.myIndex) {
					case NO_ELEMENT_BEFORE:
						double3 = double2;
						double2 = double1;
						double1 = value;
						break;
					case 1:
						double3 = double2;
						double2 = value;
						break;
					case 2:
						double3 = value;
						break;
					case 3:
						double3 = value;
						it.myIndex = 3;
						break;
				}
				break;
		}
		myLength++;
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}
	public final void addAfter(SdaiIterator it, double value, EDefined_type select[])
			throws SdaiException {
		addAfter(it, value);
	}

	public final void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		double valueDouble;
		try {
			valueDouble = ((Double)value).doubleValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addAfter(it, valueDouble);
	}

	public final void clear() throws SdaiException {
//		synchronized (syncObject) {
		if (myType == null) {
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
		session.undoRedoModifyPrepare(aggr_owner);
		if (myType.express_type == DataType.ARRAY) {
			double1 = double2 = double3 = Double.NaN;
		} else {
			myLength = 0;
		}
		owner.modified();
		fireSdaiEvent(SdaiEvent.MODIFIED, -1, null);
//		} // syncObject
	}




	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
		myType = aggr_type;
		owner = inst;
		DataType element_type = (DataType)aggr_type.getElement_type(null);
		while (element_type.express_type == DataType.DEFINED_TYPE) {
			element_type = (DataType)((CDefined_type)element_type).getDomain(null);
		}
		if (element_type.express_type != DataType.NUMBER && element_type.express_type != DataType.REAL) {
			String base = SdaiSession.line_separator + AdditionalMessages.AI_WVAL;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		int k = 0;
		for (int i = 0; i < val.length; i++) {
			double for_adding = val.nested_values[i].getDouble(null);
			if (Double.isNaN(for_adding) && myType.express_type != DataType.ARRAY) {
				if (inst != null) {
					StaticFields staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
						inst.instance_identifier, staticFields.current_attribute);
				}
				continue;
			}
			switch (k) {
				case 0:
					double1 = for_adding;
					break;
				case 1:
					double2 = for_adding;
					break;
				case 2:
					double3 = for_adding;
					break;
			}
			k++;
		}
		if (myType.express_type != DataType.ARRAY) {
			myLength = k;
		}
	}


	void setValueSimple(CEntity inst, Value val, boolean version) throws SdaiException {
		int k = 0;
		for (int i = 0; i < val.length; i++) {
			double for_adding = val.nested_values[i].getDouble(null);
			if (Double.isNaN(for_adding) && myType.express_type != DataType.ARRAY) {
				if (version) {
					if (inst != null) {
						StaticFields staticFields = StaticFields.get();
						EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_UNSV, 
							inst.instance_identifier, staticFields.current_attribute);
					}
					continue;
				} else {
					throw new SdaiException(SdaiException.AI_NVLD);
				}
			}
			switch (k) {
				case 0:
					double1 = for_adding;
					break;
				case 1:
					double2 = for_adding;
					break;
				case 2:
					double3 = for_adding;
					break;
			}
			k++;
		}
		if (myType.express_type != DataType.ARRAY) {
			myLength = k;
		}
	}


	void setForNonList(double value, int index, CEntity inst) throws SdaiException {
		if (myType.express_type == DataType.ARRAY) {
			int lower_index = ((CArray_type)myType).getLower_index(null).getBound_value(null);
			int upper_index = ((CArray_type)myType).getUpper_index(null).getBound_value(null);
			if (index > upper_index - lower_index) {
				if (inst != null) {
					StaticFields staticFields = StaticFields.get();
					EntityValue.printWarningToLogo(inst.owning_model.repository.session, AdditionalMessages.RD_EXAR, 
						inst.instance_identifier, staticFields.current_attribute);
				}
				return;
			}
		} else {
			myLength++;
		}
		switch (index) {
			case 0:
				double1 = value;
				break;
			case 1:
				double2 = value;
				break;
			case 2:
				double3 = value;
				break;
			case 3:
				throw new SdaiException(SdaiException.SY_ERR);
		}
	}


	void addAtTheEnd(double value) throws SdaiException {
		switch (myLength) {
			case 0:
				double1 = value;
				break;
			case 1:
				double2 = value;
				break;
			case 2:
				double3 = value;
				break;
			case 3:
				throw new SdaiException(SdaiException.SY_ERR);
		}
		myLength++;
	}


	boolean checkUniquenessViolation(AggregationType aggr_type) throws SdaiException {
		if (aggr_type.express_type == DataType.BAG) {
			return false;
		}
		if (aggr_type.express_type == DataType.ARRAY) {
			if (!((CArray_type)aggr_type).getUnique_flag(null)) {
				return false;
			}
		} else if (aggr_type.express_type == DataType.LIST) {
			if (!((CList_type)aggr_type).getUnique_flag(null)) {
				return false;
			}
		}
		switch(myLength) {
			case 2:
				if (double1 == double2) {
					return true;
				}
				return false;
			case 3:
				if (double1 == double2 || double1 == double3 || double2 == double3) {
					return true;
				}
				return false;
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
		switch(myLength) {
			case 1:
				if (Double.isNaN(double1)) {
					return true;
				}
				return false;
			case 2:
				if (Double.isNaN(double1) || Double.isNaN(double2)) {
					return true;
				}
				return false;
			case 3:
				if (Double.isNaN(double1) || Double.isNaN(double2) || Double.isNaN(double3)) {
					return true;
				}
				return false;
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

		StaticFields staticFields = StaticFields.get();
		int str_index = -1;
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		if (myLength <= 0) {
			staticFields.instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
			return new String(staticFields.instance_as_string, 0, str_index + 1);
		}
		if (Double.isNaN(double1)) {
			str_index = get_value(staticFields, true, Double.NaN, str_index);
		} else {
			str_index = get_value(staticFields, true, double1, str_index);
		}
		if (myLength > 1) {
			if (Double.isNaN(double2)) {
				str_index = get_value(staticFields, false, Double.NaN, str_index);
			} else {
				str_index = get_value(staticFields, false, double2, str_index);
			}
			if (myLength > 2) {
				if (Double.isNaN(double3)) {
					str_index = get_value(staticFields, false, Double.NaN, str_index);
				} else {
					str_index = get_value(staticFields, false, double3, str_index);
				}
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
		if (myLength > 0) {
			val.nested_values[0].set(context, double1);
			if (myLength > 1) {
				val.nested_values[1].set(context, double2);
				if (myLength > 2) {
					val.nested_values[2].set(context, double3);
				}
			}
		}
	}


}
