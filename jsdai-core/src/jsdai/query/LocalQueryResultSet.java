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

package jsdai.query;

import java.util.Iterator;
import java.util.Set;
import jsdai.lang.QueryResultSet;
import jsdai.lang.SdaiException;

/**
 *
 * Created: Tue Jun 10 11:40:47 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

final class LocalQueryResultSet implements QueryResultSet {
	private final LocalContext mainContext;
	private Iterator mainListIter;
	private Iterator mainSetIter;
	private LocalContext childContexts[];
	private Iterator childListIters[];
	private Object items[];

	LocalQueryResultSet(LocalContext mainContext) {
		this.mainContext = mainContext;
		int childCount = 0;
		Context childContext = mainContext.childContext;
		while(childContext != null) {
			childContext = childContext.next;
			childCount++;
		}
		childContexts = new LocalContext[childCount];
		childCount = 0;
		childContext = mainContext.childContext;
		while(childContext != null) {
			childContexts[childCount++] = (LocalContext)childContext;
			childContext = childContext.next;
		}
		mainListIter = mainContext.currentTypes.iterator();
		if(mainListIter.hasNext()) {
			mainSetIter = ((Set)mainListIter.next()).iterator();
		} else {
			mainSetIter = null;
		}
		childListIters = new Iterator[childCount];
		for(int i = 0; i < childContexts.length; i++) {
			childListIters[i] = childContexts[i].currentTypes.iterator();
		}
		items = new Object[childCount + 1];
	}
	
	// Implementation of jsdai.lang.QueryResultSet

	/**
	 * Describe <code>next</code> method here.
	 *
	 * @return a <code>boolean</code> value
	 * @exception SdaiException if an error occurs
	 */
	public boolean next() throws SdaiException {
		if(mainSetIter == null) {
			return false;
		}
		if(!mainSetIter.hasNext()) {
			if(mainListIter.hasNext()) {
				mainSetIter = ((Set)mainListIter.next()).iterator();
			} else {
				mainSetIter = null;
				return false;
			}
		}
		if(mainSetIter.hasNext()) {
			items[0] = mainSetIter.next();
			for(int i = 0; i < childListIters.length; i++) {
				items[i + 1] = convertItem(childListIters[i].next());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Describe <code>getItem</code> method here.
	 *
	 * @param item an <code>int</code> value
	 * @return an <code>Object</code> value
	 * @exception SdaiException if an error occurs
	 */
	public Object getItem(int item) throws SdaiException {
		return items[item - mainContext.resultSetOffset];
	}

	public ItemStruct getItemStruct(int itemPos, ItemStruct itemStruct) throws SdaiException {
		throw new SdaiException(SdaiException.FN_NAVL);
	}

	public void close() throws SdaiException {
		mainListIter = null;
		mainSetIter = null;
	}

	private static Object convertItem(Object item) throws SdaiException {
		if(item instanceof Set) {
			Set itemSet = (Set)item;
			if(itemSet.isEmpty()) {
				return null;
			} else if(itemSet.size() == 1) {
				Iterator itemSetIter =  itemSet.iterator();
				return itemSetIter.next();
			} else {
				return itemSet.toArray();
			}
		} else {
			return item;
		}
	}

} // LocalQueryResultSet
