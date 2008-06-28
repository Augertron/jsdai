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
 type <code>EntityExtent</code>. See <a href="Aggregate.html">Aggregate</a> for detailed 
 description of methods whose specializations are given here.
 */
public class AEntityExtent extends SessionAggregate
{
	AEntityExtent() {
		super();
	}

	AEntityExtent(EAggregation_type provided_type, SdaiCommon instance) {
		super(provided_type, instance);
	}


/**
 * It is a specialization of 
 * {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])}
 * method - the first parameter is of type <code>EntityExtent</code> and the
 * second parameter is dropped.
 */
	public boolean isMember(EntityExtent value) throws SdaiException {
		return isMember(value, null);
	}


	public void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addByIndex(int index, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addUnordered(Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>EntityExtent</code> instead of <code>Object</code>.
 */
	public EntityExtent getCurrentMember(SdaiIterator iter) throws SdaiException {
		return (EntityExtent)getCurrentMemberObject(iter);
	}


	public void setCurrentMember(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addBefore(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}


	public void addAfter(SdaiIterator it, Object value, EDefined_type select[]) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

/**
 * Returns a description of this <code>AEntityExtent</code> as a <code>String</code>.
 * It includes constant string "EntityExtents: " and a list of names
 * of entity definitions whose instances are contained in the folders(extents)
 * of this aggregate.
 * <P><B>Example:</B>
 * <P><TT><pre>    AEntityExtent extent_aggr = ...;
 *    System.out.println(extent_aggr);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The string printed will be like this:
 * <pre>    EntityExtents: cartesian_point, vector, direction, line </pre>
 * @return the <code>String</code> representing this <code>AEntityExtent</code>.
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
		for (i = 0; i < ENTITY_EXTENTS_LENGTH; i++) {
			staticFields.instance_as_string[++str_index] = ENTITY_EXTENTS[i];
		}
		staticFields.instance_as_string[++str_index] = PhFileReader.COLON;
		boolean first = true;
		for (i = 1; i <= myLength; i++) {
			EntityExtent extent = (EntityExtent)getByIndexObject(i);
			String name = extent.definition.name;
			int ln = name.length();
			if (str_index + ln + 2 >= staticFields.instance_as_string.length) {
				enlarge_instance_string(str_index + 1, str_index + ln + 3);
			}
			if (!first) {
				staticFields.instance_as_string[++str_index] = PhFileReader.COMMA_b;
			}
			staticFields.instance_as_string[++str_index] = PhFileReader.SPACE;
			first = false;
			str_index = write_string(name, str_index);
		}
		return new String(staticFields.instance_as_string, 0, str_index + 1);
	}

}
