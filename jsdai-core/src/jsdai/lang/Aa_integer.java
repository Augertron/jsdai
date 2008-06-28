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
 Specialized class implementing double nested <code>Aggregate</code> for
 members of the EXPRESS type INTEGER. See <a href="Aggregate.html">Aggregate</a> 
 for detailed description of methods whose specializations are given here.
 */
public class Aa_integer extends CAggregate {

	Aa_integer() {
		super();
	}

	Aa_integer(EAggregation_type provided_type, CEntity instance) {
		super((AggregationType)provided_type, instance);
	}



/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>A_integer</code> and the second parameter is dropped.
 */
	public boolean isMember(A_integer value) throws SdaiException {
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
 * with return value of type <code>A_integer</code> instead of <code>Object</code>.
 */
	public A_integer getByIndex(int index) throws SdaiException {
		return (A_integer)getByIndexObject(index);
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
 * {@link Aggregate#createAggregateByIndex(int, EDefined_type []) createAggregateByIndex(int, EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the second parameter is dropped.
 */
	public A_integer createAggregateByIndex(int index) throws SdaiException {
		return (A_integer)createAggregateByIndex(index, null);
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

/**
 * It is a specialization of 
 * {@link Aggregate#addAggregateByIndex(int, EDefined_type []) addAggregateByIndex(int, EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the second parameter is dropped.
 */
	public A_integer addAggregateByIndex(int index) throws SdaiException {
		return (A_integer)addAggregateByIndex(index, null);
	}



					/*		Operations with an unordered collection			*/


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
 * {@link Aggregate#createAggregateUnordered(EDefined_type []) createAggregateUnordered(EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the parameter is dropped.
 */
	public A_integer createAggregateUnordered() throws SdaiException {
		return (A_integer)createAggregateUnordered(null);
	}

/**
 * It is a specialization of 
 * {@link Aggregate#removeUnordered(Object, EDefined_type []) removeUnordered(Object, EDefined_type [])}
 * method - the first parameter is of type <code>A_integer</code> and the second parameter is dropped.
 */
	public void removeUnordered(A_integer value) throws SdaiException {
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
 * It is equivalent to {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>A_integer</code> instead of <code>Object</code>.
 */
	public A_integer getCurrentMember(SdaiIterator iter) throws SdaiException {
		return (A_integer)getCurrentMemberObject(iter);
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
 * {@link Aggregate#createAggregateAsCurrentMember(SdaiIterator, EDefined_type []) createAggregateAsCurrentMember(SdaiIterator, EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the second parameter is dropped.
 */
	public A_integer createAggregateAsCurrentMember(SdaiIterator iter) throws SdaiException {
		return (A_integer)createAggregateAsCurrentMember(iter, null);
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
 * {@link Aggregate#createAggregateBefore(SdaiIterator, EDefined_type []) createAggregateBefore(SdaiIterator, EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the second parameter is dropped.
 */
	public A_integer createAggregateBefore(SdaiIterator iter) throws SdaiException {
		return (A_integer)createAggregateBefore(iter, null);
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

/**
 * It is a specialization of 
 * {@link Aggregate#createAggregateAfter(SdaiIterator, EDefined_type []) createAggregateAfter(SdaiIterator, EDefined_type [])}
 * method - the return value is of type <code>A_integer</code> instead of <code>Object</code>
 * and the second parameter is dropped.
 */
	public A_integer createAggregateAfter(SdaiIterator iter) throws SdaiException {
		return (A_integer)createAggregateAfter(iter, null);
	}


/*	public String toString() {
		synchronized (syncObject) {
		try {
			return getAsString();
		} catch (SdaiException e) {
			return super.toString();
		}
		} // syncObject
	}*/


/**
	This method is doing a job of the public method <code>toString</code>.
*/
/*	String getAsString() throws SdaiException {
		int str_index = -1;
		CEntity owning_instance = getOwningInstance();

		if (owning_instance == null) {
			return toStringInteger2Aggregate(str_index);
		} else {
			return super.getAsString();
		}
	}


	private String toStringInteger2Aggregate(int str_index) throws SdaiException {
		if (instance_as_string == null) {
			instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
		boolean first = true;
		if (it1 == null) {
			it1 = createIterator();
		} else {
			attachIterator(it1);
		}
		while (it1.next()) {
			A_integer int_aggr = ((Aa_integer)this).getCurrentMember(it1);
			if (str_index + 2 >= instance_as_string.length) {
				enlarge_instance_string(str_index + 1, str_index + 3);
			}
			if (!first) {
				instance_as_string[++str_index] = PhFileReader.COMMA_b;
			}
			first = false;
			instance_as_string[++str_index] = PhFileReader.LEFT_PARENTHESIS;
			boolean first_interior = true;
			if (it2 == null) {
				it2 = int_aggr.createIterator();
			} else {
				int_aggr.attachIterator(it2);
			}
			while (it2.next()) {
				if (!first_interior) {
					if (str_index + 1 >= instance_as_string.length) {
						enlarge_instance_string(str_index + 1, str_index + 2);
					}
					instance_as_string[++str_index] = PhFileReader.COMMA_b;
				}
				first_interior = false;
				str_index = long_to_byte_array(int_aggr.getCurrentMember(it2), str_index);
			}
			if (str_index + 1 >= instance_as_string.length) {
				enlarge_instance_string(str_index + 1, str_index + 2);
			}
			instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		}
		if (str_index + 1 >= instance_as_string.length) {
			enlarge_instance_string(str_index + 1, str_index + 2);
		}
		instance_as_string[++str_index] = PhFileReader.RIGHT_PARENTHESIS;
		return new String(instance_as_string, 0, str_index + 1);
	}*/


}
