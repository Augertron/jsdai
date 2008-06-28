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

/** Specialized supertype Aggregate, only subtyped by select aggregates */
public class AEntitySelect extends AEntity 
{

/**
 * Constructs a new object of aggregate <code>AEntitySelect</code>
 * without aggregation type. The aggregates of such kind are used
 * as non-persistent lists.
 * @see Aggregate#getAggregationType
 */
	public AEntitySelect() {
		super();
	}



/**
 * It is {@link Aggregate#getByIndexObject getByIndexObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public EEntity getByIndex(int index) throws SdaiException {
//		synchronized (syncObject) {
		Object value = getByIndexObject(index);
		if (value instanceof EEntity) {
			return (EEntity)value;
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}

/**
 * It is {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
 * with return value of type <code>EEntity</code> instead of <code>Object</code>.
 * @throws SdaiException VT_NVLD, value type invalid.
 */
	public EEntity getCurrentMember(jsdai.lang.SdaiIterator iter) throws jsdai.lang.SdaiException {
//		synchronized (syncObject) {
		Object value = getCurrentMemberObject(iter);
		if (value instanceof EEntity) {
			return (EEntity)value;
		} else {
			throw new SdaiException(SdaiException.VT_NVLD);
		}
//		} // syncObject
	}

}
