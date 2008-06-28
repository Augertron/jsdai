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
 EXPRESS type BINARY. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_binary extends CAggregate {

	A_binary() {
		super();
	}

	A_binary(EAggregation_type provided_type, CEntity instance) {
		super((AggregationType)provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type Binary and the second parameter is dropped.
 */
	public boolean isMember(Binary value) throws SdaiException {
		return isMember(value, null);
	}

	public boolean isMember(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}



					/*		Operations with an ordered collection			*/

/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type Binary instead of Object.
 */
	public Binary getByIndex(int index) throws SdaiException {
		return (Binary)getByIndexObject(index);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, Object, EDefined_type []) setByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type Binary and the third parameter is dropped.
 */
	public void setByIndex(int index, Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setByIndex(index, value, null);
//		} // syncObject
	}

	public void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type Binary and the third parameter is dropped.
 */
	public void addByIndex(int index, Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addByIndex(index, value, null);
//		} // syncObject
	}

	public void addByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}



					/*		Operations with an unordered collection			*/

/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(Object, EDefined_type []) addUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type Binary and the second parameter is dropped.
 */
	public void addUnordered(Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addUnordered(value, null);
//		} // syncObject
	}

	public void addUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(Object, EDefined_type []) removeUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type Binary and the second parameter is dropped.
 */
	public void removeUnordered(Binary value) throws SdaiException {
		removeUnordered(value, null);
	}

	public void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}



					/*		Operations using an iterator			*/

/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type Binary instead of Object.
 */
	public Binary getCurrentMember(SdaiIterator iter) throws SdaiException {
		return (Binary)getCurrentMemberObject(iter);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, Object, EDefined_type []) setCurrentMember(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type Binary and the third parameter is dropped.
 */
	public void setCurrentMember(SdaiIterator iter, Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setCurrentMember(iter, value, null);
//		} // syncObject
	}

	public void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void setCurrentMember(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addBefore(SdaiIterator, Object, EDefined_type []) addBefore(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type Binary and the third parameter is dropped.
 */
	public void addBefore(SdaiIterator iter, Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addBefore(iter, value, null);
//		} // syncObject
	}

	public void addBefore(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addBefore(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addBefore(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addAfter(SdaiIterator, Object, EDefined_type []) addAfter(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type Binary and the third parameter is dropped.
 */
	public void addAfter(SdaiIterator iter, Binary value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addAfter(iter, value, null);
//		} // syncObject
	}

	public void addAfter(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addAfter(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}

	public void addAfter(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.VT_NVLD);
	}


	private boolean checkValue(Binary value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		if (type.express_type != DataType.BINARY) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		CBinary_type bin_type = (CBinary_type)type;
		boolean fixed_width = bin_type.getFixed_width(null);
		if (bin_type.testWidth(null)) {
			EBound bound = bin_type.getWidth(null);
			int bit_bound = bound.getBound_value(null);
			int bit_count = value.getSize();
			if (fixed_width) {
				if (bit_count == bit_bound) {
					return true;
				} else {
					return false;
				}
			} else {
				if (bit_count <= bit_bound) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		if (myLength <= 0) {
			return;
		}
		int i;
		Object [] myDataA;
		if (aggr_type.express_type == DataType.LIST) {
			ListElement element;
			if (myLength == 1) {
				val.nested_values[0].set(context, (Binary)myData);
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				for (i = 0; i < 2; i++) {
					val.nested_values[i].set(context, (Binary)myDataA[i]);
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
					val.nested_values[i++].set(context, (Binary)element.object);
					element = element.next;
				}
			}
		} else {
			if (myLength == 1) {
				val.nested_values[0].set(context, (Binary)myData);
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					val.nested_values[i].set(context, (Binary)myDataA[i]);
				}
			}
		}
	}


}
