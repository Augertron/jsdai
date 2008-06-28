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
 EXPRESS type STRING. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class A_string extends CAggregate {

	A_string() {
		super();
	}

	A_string(EAggregation_type provided_type, SdaiCommon owner) {
		super((AggregationType)provided_type, owner);
	}



/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type String and the second parameter is dropped.
 */
	public boolean isMember(String value) throws SdaiException {
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
 * with return value of type String instead of Object.
 */
	public String getByIndex(int index) throws SdaiException {
		return (String)getByIndexObject(index);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setByIndex(int, Object, EDefined_type []) setByIndex(int, Object, EDefined_type [])}
 * method - the second parameter is of type String and the third parameter is dropped.
 */
	public void setByIndex(int index, String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setByIndex(index, value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
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
 * method - the second parameter is of type String and the third parameter is dropped.
 */
	public void addByIndex(int index, String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addByIndex(index, value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
//		} // syncObject
	}

	void addByIndexPrivate(int index, String value) throws SdaiException {
		if (value == null) {
			throw new SdaiException(SdaiException.VA_NSET);
		}
		index--;
		if (index < 0 || index > myLength) {
			throw new SdaiException(SdaiException.IX_NVLD, this);
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
//		addByIndex(index, value, null);
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
 * method - the first parameter is of type String and the second parameter is dropped.
 */
	public void addUnordered(String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addUnordered(value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
//		} // syncObject
	}

	void addUnorderedPrivate(String value) throws SdaiException {
		addUnordered(value, null);
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
 * method - the first parameter is of type String and the second parameter is dropped.
 */
	public void removeUnordered(String value) throws SdaiException {
		removeUnordered(value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
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
 * with return value of type String instead of Object.
 */
	public String getCurrentMember(SdaiIterator iter) throws SdaiException {
		return (String)getCurrentMemberObject(iter);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#setCurrentMember(SdaiIterator, Object, EDefined_type []) setCurrentMember(SdaiIterator, Object, EDefined_type [])}
 * method - the second parameter is of type String and the third parameter is dropped.
 */
	public void setCurrentMember(SdaiIterator iter, String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		setCurrentMember(iter, value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
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
 * method - the second parameter is of type String and the third parameter is dropped.
 */
	public void addBefore(SdaiIterator iter, String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addBefore(iter, value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
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
 * method - the second parameter is of type String and the third parameter is dropped.
 */
	public void addAfter(SdaiIterator iter, String value) throws SdaiException {
//		synchronized (syncObject) {
		if (myType.shift != SdaiSession.PRIVATE_AGGR && !checkValue(value)) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		addAfter(iter, value, null);
		if (myType.shift == SdaiSession.PRIVATE_AGGR && myType == SdaiSession.listTypeSpecial) {
			SdaiCommon own = getOwner();
			if (own instanceof SdaiRepository) {
				((SdaiRepository)own).modified = true;
			} else if (own instanceof SdaiModel) {
				((SdaiModel)own).modified_outside_contents = true;
			} else if (own instanceof SchemaInstance) {
				((SchemaInstance)own).modified = true;
			}
		}
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


	boolean check_A_string() throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		if (type.express_type != DataType.STRING) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		StringType str_type = (StringType)type;

		if (myLength <= 0) {
			return true;
		}
		int i;
		Object [] myDataA;
		if (myType == null || myType.express_type == DataType.LIST) {
			ListElement element;
			if (myLength == 1) {
				if (!str_type.check_width((String)myData)) {
					return false;
				}
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				for (i = 0; i < 2; i++) {
					if (!str_type.check_width((String)myDataA[i])) {
						return false;
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
					if (!str_type.check_width((String)element.object)) {
						return false;
					}
					element = element.next;
				}
			}
		} else {
			if (myLength == 1) {
				if (!str_type.check_width((String)myData)) {
					return false;
				}
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					if (!str_type.check_width((String)myDataA[i])) {
						return false;
					}
				}
			}
		}
		return true;
	}


	private boolean checkValue(String value) throws SdaiException {
		DataType type = (DataType)myType.getElement_type(null);
		if (type.express_type == DataType.DEFINED_TYPE) {
			while (type.express_type == DataType.DEFINED_TYPE) {
				type = (DataType)((CDefined_type)type).getDomain(null);
			}
		}
		if (type.express_type != DataType.STRING) {
			throw new SdaiException(SdaiException.SY_ERR);
		}
		return ((StringType)type).check_width(value);
	}


	void getValue(Value val, AggregationType aggr_type, SdaiContext context) throws SdaiException {
		if (myLength <= 0) {
			return;
		}
		int i;
		Object [] myDataA;
		if (aggr_type == null || aggr_type.express_type == DataType.LIST) {
			ListElement element;
			if (myLength == 1) {
				val.nested_values[0].set(context, (String)myData);
			} else if (myLength == 2) {
				myDataA = (Object [])myData;
				for (i = 0; i < 2; i++) {
					val.nested_values[i].set(context, (String)myDataA[i]);
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
					val.nested_values[i++].set(context, (String)element.object);
					element = element.next;
				}
			}
		} else {
			if (myLength == 1) {
				val.nested_values[0].set(context, (String)myData);
			} else {
				myDataA = (Object [])myData;
				for (i = 0; i < myLength; i++) {
					val.nested_values[i].set(context, (String)myDataA[i]);
				}
			}
		}
	}


}
