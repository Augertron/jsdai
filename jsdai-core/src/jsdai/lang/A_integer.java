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
 EXPRESS type integer. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_integer extends A_integerPrimitive {

	A_integer() {
		super();
	}

	A_integer(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		super(provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(int, EDefined_type []) isMember(int, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final boolean isMember(int value) throws SdaiException {
//		synchronized (syncObject) {
			return isMemberInternal(value);
//		} // syncObject
	}
	public final boolean isMember(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return isMemberInternal(value);
//		} // syncObject
	}

	public final boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return value instanceof Integer ? isMemberInternal(((Integer)value).intValue()) : false;
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getByIndexInt getByIndexInt} method
 * under the different name.
 */
	public final int getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			return getByIndexInternal(index, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final int getByIndexInt(int index) throws SdaiException {
//		synchronized (syncObject) {
			return getByIndexInternal(index, Integer.MIN_VALUE);
//		} // syncObject
	}

	public Object getByIndexObject(int index) throws SdaiException {
//		synchronized (syncObject) {
			return new Integer(getByIndexInternal(index, Integer.MIN_VALUE));
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, int, EDefined_type []) setByIndex(int, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void setByIndex(int index,  int value) throws SdaiException {
//		synchronized (syncObject) {
			setByIndexInternal(index, value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			setByIndexInternal(index, value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			setByIndexInternal(index, valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final int testByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			return testByIndexInternal(index, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final int testByIndex(int index, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return testByIndexInternal(index, Integer.MIN_VALUE);
//		} // syncObject
	}


	public final void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			unsetValueByIndexInternal(index, Integer.MIN_VALUE);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, int, EDefined_type []) addByIndex(int, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addByIndex(int index, int value) throws SdaiException {
//		synchronized (syncObject) {
			addByIndexInternal(index, value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void addByIndex(int index, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			addByIndexInternal(index, value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			addByIndexInternal(index, valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			removeByIndexInternal(index);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(int, EDefined_type []) addUnordered(int, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final void addUnordered(int value) throws SdaiException {
//		synchronized (syncObject) {
			addUnorderedInternal(value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void addUnordered(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			addUnorderedInternal(value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			addUnorderedInternal(valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(int, EDefined_type []) removeUnordered(int, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final void removeUnordered(int value) throws SdaiException {
//		synchronized (syncObject) {
			removeUnorderedInternal(value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			removeUnorderedInternal(value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			removeUnorderedInternal(valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final int testCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return testCurrentMemberInternal(iter, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return testCurrentMemberInternal(iter, Integer.MIN_VALUE);
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getCurrentMemberInt getCurrentMemberInt} method
 * under the different name.
 */
	public final int getCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return getCurrentMemberInternal(iter, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return getCurrentMemberInternal(iter, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return new Integer(getCurrentMemberInternal(iter, Integer.MIN_VALUE));
//		} // syncObject
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, int, EDefined_type []) setCurrentMember(SdaiIterator, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void setCurrentMember(SdaiIterator iter, int value) throws SdaiException {
//		synchronized (syncObject) {
			setCurrentMemberInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			setCurrentMemberInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			setCurrentMemberInternal(it, valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addBefore(SdaiIterator, int, EDefined_type []) addBefore(SdaiIterator, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addBefore(SdaiIterator iter, int value) throws SdaiException {
//		synchronized (syncObject) {
			addBeforeInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void addBefore(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			addBeforeInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void addBefore(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			addBeforeInternal(it, valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addAfter(SdaiIterator, int, EDefined_type []) addAfter(SdaiIterator, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addAfter(SdaiIterator iter, int value) throws SdaiException {
//		synchronized (syncObject) {
			addAfterInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}
	public final void addAfter(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			addAfterInternal(iter, value, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
//		synchronized (syncObject) {
			int valueInt;
			try {
				valueInt = ((Integer)value).intValue();
			} catch(ClassCastException e) {
				throw new SdaiException(SdaiException.VT_NVLD, e);
			}
			addAfterInternal(it, valueInt, Integer.MIN_VALUE);
//		} // syncObject
	}

	public final void clear() throws SdaiException {
//		synchronized (syncObject) {
			clearInternal(Integer.MIN_VALUE);
//		} // syncObject
	}




	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
		setValueInternal(aggr_type, inst, val, mixed, Integer.MIN_VALUE);
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		int i;
		if (aggr_type.express_type == DataType.LIST) {
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			i = 0;
			while (element != null) {
				val.nested_values[i++].set(context, element.value);
				element = element.next;
			}
		} else {
			int [] myDataArray = (int [])myData;
			for (i = 0; i < myLength; i++) {
				val.nested_values[i].set(context, myDataArray[i]);
			}
		}
	}


}
