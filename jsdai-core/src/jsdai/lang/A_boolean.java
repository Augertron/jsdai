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
 EXPRESS type boolean. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_boolean extends A_integerPrimitive {

	A_boolean() {
		super();
	}

	A_boolean(EAggregation_type provided_type, CEntity instance) throws SdaiException {
		super(provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(boolean, EDefined_type []) isMember(boolean, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final boolean isMember(boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		return isMemberInternal(val);
//		} // syncObject
	}
	public final boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
		return isMember(value);
	}

	public final boolean isMember(Object value, EDefined_type select[]) throws SdaiException {
		return value instanceof Boolean ? isMember(((Boolean)value).booleanValue()) : false;
	}


/**
 * It is {@link Aggregate#getByIndexBoolean getByIndexBoolean} method
 * under the different name.
 */
	public final boolean getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		int value = getByIndexInternal(index, 0);
		if (value == 2) {
			return true;
		} else if (value == 1) {
			return false;
		}
		throw new SdaiException(SdaiException.VA_NVLD);
//		} // syncObject
	}
	public final boolean getByIndexBoolean(int index) throws SdaiException {
		return getByIndex(index);
	}

	public Object getByIndexObject(int index) throws SdaiException {
		return Boolean.valueOf(getByIndex(index));
	}


/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, boolean, EDefined_type []) setByIndex(int, boolean, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void setByIndex(int index, boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		setByIndexInternal(index, val, 0);
//		} // syncObject
	}
	public final void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
		setByIndex(index, value);
	}

	public final void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		setByIndex(index, valueBoolean);
	}

	public final int testByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		if (testByIndexInternal(index, 0) > 0) {
			return 4;
		}
		return 0;
//		} // syncObject
	}
	public final int testByIndex(int index, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (testByIndexInternal(index, 0) > 0) {
			return 4;
		}
		return 0;
//		} // syncObject
	}


	public final void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			unsetValueByIndexInternal(index, 0);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, boolean, EDefined_type []) addByIndex(int, boolean, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addByIndex(int index, boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		addByIndexInternal(index, val, 0);
//		} // syncObject
	}
	public final void addByIndex(int index, boolean value, EDefined_type select[])
			throws SdaiException {
		addByIndex(index, value);
	}

	public final void addByIndex(int index, Object value, EDefined_type select[])
			throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addByIndex(index, valueBoolean);
	}

	public final void removeByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			removeByIndexInternal(index);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addUnordered(boolean, EDefined_type []) addUnordered(boolean, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final void addUnordered(boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		addUnorderedInternal(val, 0);
//		} // syncObject
	}
	public final void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		addUnordered(value);
	}

	public final void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addUnordered(valueBoolean);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(boolean, EDefined_type []) removeUnordered(boolean, EDefined_type [])}
 * method - the second parameter is dropped.
 */
	public final void removeUnordered(boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		removeUnorderedInternal(val, 0);
//		} // syncObject
	}
	public final void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
		removeUnordered(value);
	}

	public final void removeUnordered(Object value, EDefined_type select[]) throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		removeUnordered(valueBoolean);
	}

	public final int testCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		if (testCurrentMemberInternal(iter, 0) > 0) {
			return 4;
		}
		return 0;
//		} // syncObject
	}
	public final int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
		if (testCurrentMemberInternal(iter, 0) > 0) {
			return 4;
		}
		return 0;
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getCurrentMemberBoolean getCurrentMemberBoolean} method
 * under the different name.
 */
	public final boolean getCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
		int value = getCurrentMemberInternal(iter, 0);
		if (value == 2) {
			return true;
		} else if (value == 1) {
			return false;
		}
		throw new SdaiException(SdaiException.VA_NVLD);
//		} // syncObject
	}
	public final boolean getCurrentMemberBoolean(SdaiIterator iter) throws SdaiException {
		return getCurrentMember(iter);
	}

	public final Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException {
		return Boolean.valueOf(getCurrentMember(iter));
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, boolean, EDefined_type []) setCurrentMember(SdaiIterator, boolean, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void setCurrentMember(SdaiIterator iter, boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		setCurrentMemberInternal(iter, val, 0);
//		} // syncObject
	}
	public final void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
		setCurrentMember(iter, value);
	}

	public final void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		setCurrentMember(it, valueBoolean);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addBefore(SdaiIterator, boolean, EDefined_type []) addBefore(SdaiIterator, boolean, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addBefore(SdaiIterator iter, boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		addBeforeInternal(iter, val, 0);
//		} // syncObject
	}
	public final void addBefore(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
		addBefore(iter, value);
	}

	public final void addBefore(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addBefore(it, valueBoolean);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#addAfter(SdaiIterator, boolean, EDefined_type []) addAfter(SdaiIterator, boolean, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addAfter(SdaiIterator iter, boolean value) throws SdaiException {
//		synchronized (syncObject) {
		int val;
		if (value) {
			val = 2;
		} else {
			val = 1;
		}
		addAfterInternal(iter, val, 0);
//		} // syncObject
	}
	public final void addAfter(SdaiIterator iter, boolean value, EDefined_type select[])
			throws SdaiException {
		addAfter(iter, value);
	}

	public final void addAfter(SdaiIterator it, Object value, EDefined_type select[])
			throws SdaiException {
		boolean valueBoolean;
		try {
			valueBoolean = ((Boolean)value).booleanValue();
		} catch(ClassCastException e) {
			throw new SdaiException(SdaiException.VT_NVLD, e);
		}
		addAfter(it, valueBoolean);
	}

	public final void clear() throws SdaiException {
//		synchronized (syncObject) {
			clearInternal(0);
//		} // syncObject
	}




	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
		setValueInternal(aggr_type, inst, val, mixed, 0);
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		int i;
		if (aggr_type.express_type == DataType.LIST) {
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			i = 0;
			while (element != null) {
				val.nested_values[i++].setLB(context, element.value);
				element = element.next;
			}
		} else {
			int [] myDataArray = (int [])myData;
			for (i = 0; i < myLength; i++) {
				val.nested_values[i].setLB(context, myDataArray[i]);
			}
		}
	}


}
