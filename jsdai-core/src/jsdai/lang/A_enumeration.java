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
 EXPRESS type ENUMERATION. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_enumeration extends A_integerPrimitive {

	A_enumeration() {
		super();
	}

	A_enumeration(EAggregation_type provided_type, CEntity instance) throws SdaiException {
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
			return getByIndexInternal(index, 0);
//		} // syncObject
	}
	public final int getByIndexInt(int index) throws SdaiException {
//		synchronized (syncObject) {
			return getByIndexInternal(index, 0);
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
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setByIndexInternal(index, value, 0);
//		} // syncObject
	}
	public final void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
		setByIndex(index, value);
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
			return testByIndexInternal(index, 0);
//		} // syncObject
	}
	public final int testByIndex(int index, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return testByIndexInternal(index, 0);
//		} // syncObject
	}


	public final void unsetValueByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
			unsetValueByIndexInternal(index, 0);
//		} // syncObject
	}


/**
 * It is a specialization of 
 * {@link Aggregate#addByIndex(int, int, EDefined_type []) addByIndex(int, int, EDefined_type [])}
 * method - the third parameter is dropped.
 */
	public final void addByIndex(int index, int value) throws SdaiException {
//		synchronized (syncObject) {
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addByIndexInternal(index, value, 0);
//		} // syncObject
	}
	public final void addByIndex(int index, int value, EDefined_type select[])
			throws SdaiException {
		addByIndex(index, value);
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
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addUnorderedInternal(value, 0);
//		} // syncObject
	}
	public final void addUnordered(int value, EDefined_type select[]) throws SdaiException {
		addUnordered(value);
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
			removeUnorderedInternal(value, 0);
//		} // syncObject
	}
	public final void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			removeUnorderedInternal(value, 0);
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
			return testCurrentMemberInternal(iter, 0);
//		} // syncObject
	}
	public final int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException {
//		synchronized (syncObject) {
			return testCurrentMemberInternal(iter, 0);
//		} // syncObject
	}


/**
 * It is {@link Aggregate#getCurrentMemberInt getCurrentMemberInt} method
 * under the different name.
 */
	public final int getCurrentMember(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return getCurrentMemberInternal(iter, 0);
//		} // syncObject
	}
	public final int getCurrentMemberInt(SdaiIterator iter) throws SdaiException {
//		synchronized (syncObject) {
			return getCurrentMemberInternal(iter, 0);
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
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setCurrentMemberInternal(iter, value, 0);
//		} // syncObject
	}
	public final void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
		setCurrentMember(iter, value);
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
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addBeforeInternal(iter, value, 0);
//		} // syncObject
	}
	public final void addBefore(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
		addBefore(iter, value);
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
		if (!checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addAfterInternal(iter, value, 0);
//		} // syncObject
	}
	public final void addAfter(SdaiIterator iter, int value, EDefined_type select[])
			throws SdaiException {
		addAfter(iter, value);
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
			clearInternal(0);
//		} // syncObject
	}




	void setValue(AggregationType aggr_type, CEntity inst, Value val, 
			boolean mixed, boolean inverse) throws SdaiException {
		setValueInternal(aggr_type, inst, val, mixed, 0);
	}


	private boolean checkValue(int value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type == DataType.LOGICAL) {
			return true;
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
			return false;
		}
		return true;
	}


	String getValue(int value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
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
		value--;
		Object [] myDataA;
		Object obj;
		ListElement element;
		if (elements.myLength == 1) {
			obj = elements.myData;
		} else if (elements.myLength == 2) {
			myDataA = (Object [])elements.myData;
			obj = myDataA[value];
		} else {
			if (elements.myLength <= CAggregate.SHORT_AGGR) {
				element = (ListElement)elements.myData;
			} else {
				myDataA = (Object [])elements.myData;
				element = (ListElement)myDataA[0];
			}
			while (value-- > 0) {
				element = element.next;
			}
			obj = element.object;
		}
		if (obj == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		return (String)obj;
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		int i;
		DataType member_type = logical_or_enum(aggr_type);
		EEnumeration_type enumer = null;
		A_string ee = null;
//		SdaiContext context = null;
		if (member_type == null) {
// Print message !!!!!!!!!!!!!!!!!! 
//			String base = SdaiSession.line_separator + AdditionalMessages.SE_ERDD;
//			throw new SdaiException(SdaiException.SY_ERR, base);
		} else if (member_type.express_type >= DataType.ENUMERATION && member_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) {
			enumer = (EEnumeration_type)member_type;
			if (member_type.express_type == DataType.EXTENSIBLE_ENUM || member_type.express_type == DataType.EXTENDED_EXTENSIBLE_ENUM) {
				CEntity owning_inst = getOwningInstance();
				if (owning_inst != null) {
					SdaiModel owning_mod = owning_inst.owning_model;
					if (owning_mod != null) {
						context = owning_mod.repository.session.sdai_context;
					}
				}
				ee = enumer.getElements(null, context);
			} else {
				ee = enumer.getElements(null);
			}
		}
		if (aggr_type.express_type == DataType.LIST) {
			ListElementInteger [] myDataList = (ListElementInteger [])myData;
			ListElementInteger element = myDataList[0];
			i = 0;
			while (element != null) {
				if (enumer == null) {
					val.nested_values[i++].setLB(context, element.value);
				} else {
					if (element.value == 0) {
						val.nested_values[i++].setEnum(context, null);
					} else {
						val.nested_values[i++].setEnum(context, ee.getByIndex(element.value));
					}
				}
				element = element.next;
			}
		} else {
			int [] myDataArray = (int [])myData;
			for (i = 0; i < myLength; i++) {
				if (enumer == null) {
					val.nested_values[i].setLB(context, myDataArray[i]);
				} else {
					if (myDataArray[i] == 0) {
						val.nested_values[i].setEnum(context, null);
					} else {
						val.nested_values[i].setEnum(context, ee.getByIndex(myDataArray[i]));
					}
				}
			}
		}
	}


	private DataType logical_or_enum(AggregationType aggr_type) throws SdaiException {
		DataType type = (DataType)aggr_type.getElement_type(null);
		if (type.express_type == DataType.LOGICAL) {
			return type;
		} else if (type.express_type != DataType.DEFINED_TYPE) {
			return null;
		}
		while (type.express_type == DataType.DEFINED_TYPE) {
			DataType underlying_type = (DataType)((CDefined_type)type).getDomain(null);
			if ( (underlying_type.express_type >= DataType.ENUMERATION && underlying_type.express_type <= DataType.EXTENDED_EXTENSIBLE_ENUM) || 
					underlying_type.express_type == DataType.LOGICAL) {
				return type;
			} else if (underlying_type.express_type == DataType.DEFINED_TYPE) {
				type = underlying_type;
			} else {
				return null;
			}
		}
		return null;
	}


}
