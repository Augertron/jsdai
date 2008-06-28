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

package jsdai.lang; import jsdai.dictionary.*;

/**
 Specialized class implementing <code>Aggregate</code> for members of
 type <code>SdaiRepository</code>. See <a href="Aggregate.html">Aggregate</a>
 for detailed description of methods whose specializations are given here.
 */
public class ASdaiRepository extends SessionAggregate
{

/**
 * Constructs a new object of aggregate <code>ASdaiRepository</code>
 * without aggregation type. The aggregates of such kind are used
 * as non-persistent lists.
 * @see Aggregate#getAggregationType
 */
	public ASdaiRepository() {
		super();
	}

	ASdaiRepository(EAggregation_type provided_type, SdaiCommon instance) {
		super(provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>SdaiRepository</code> and the
 * second parameter is dropped.
 */
	public boolean isMember(SdaiRepository value) throws SdaiException {
		return isMember(value, null);
	}

/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type <code>SdaiRepository</code> instead of <code>Object</code>.
 */
	public SdaiRepository getByIndex(int index) throws SdaiException {
		return (SdaiRepository)getByIndexObject(index);
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			setByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


	public void addByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			addByIndexCommon(index, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}

	/**
	 * It is a specialization of 
	 * {@link Aggregate#addByIndex(int, Object, EDefined_type []) addByIndex(int, Object, EDefined_type [])}
	 * method - the second parameter is of type <code>SdaiRepository</code>
	 * and the third parameter is dropped.
	 * @since 4.1.0
	 */
	public void addByIndex(int index, SdaiRepository value) throws SdaiException {
		addByIndexCommon(index, value);
	}

	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			addUnorderedCommon(value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}

/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>SdaiRepository</code> instead of <code>Object</code>.
 */
	public SdaiRepository getCurrentMember(SdaiIterator iter) throws SdaiException {
		return (SdaiRepository)getCurrentMemberObject(iter);
	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			setCurrentMemberCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			addBeforeCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		if (value instanceof ASdaiRepository) {
			addAfterCommon(it, value);
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
	}

/**
 * Returns a description of this <code>ASdaiRepository</code> as a <code>String</code>.
 * It includes constant string "SdaiRepositories: " and a list of the names
 * of all repositories contained in this aggregate.
 * <P><B>Example:</B>
 * <P><TT><pre>    ASdaiRepository repo_aggr = ...;
 *    System.out.println(repo_aggr);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    SdaiRepositories: repo, repo2, repo3, my_repo </pre>
 * @return the <code>String</code> representing this <code>ASdaiRepository</code>.
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
		int i;
		int str_index = -1;
		StaticFields staticFields = StaticFields.get();
		if (staticFields.instance_as_string == null) {
			staticFields.instance_as_string = new byte[PhFileWriter.NUMBER_OF_CHARACTERS_IN_INSTANCE];
		}
		for (i = 0; i < REPOSITORIES_LENGTH; i++) {
			staticFields.instance_as_string[++str_index] = REPOSITORIES[i];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.COLON;
		boolean first = true;
		for (i = 1; i <= myLength; i++) {
			SdaiRepository repo = (SdaiRepository)getByIndexObject(i);
			int ln = repo.name.length();
			if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(str_index + 1, str_index + ln + 3);
			}
			if (!first) {
				staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
			}
			staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
			first = false;
			str_index = write_string(repo.name, str_index);
		}
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}

}
