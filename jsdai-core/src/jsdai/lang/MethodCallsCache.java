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

import java.util.*;
import org.apache.commons.collections.LRUMap;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/*public*/ class MethodCallsCache {
	/*public*/ static final Object nullValue = new Object();
	private SdaiSession session;
    private Map mainMap;
	private int cacheSize;
	private long callsCount;
	private long uniqueCalls;
	private long hits;
	
	MethodCallsCache(SdaiSession session, int cacheSize) {
		this.session = session;
		this.cacheSize = cacheSize;
// 		mainMap = new HashMap();
		mainMap = new LRUMap(cacheSize);
	}
    
//     public void clear() {
//         mainMap = null;
// 		callsCount = uniqueCalls = 0;
//     }
 
//     Map getMainMap() {
//         if (mainMap == null) {
//             mainMap = new HashMap();
//         }
        
//         return mainMap;
//     }
    
//     Map getValueMapById(Map map, Object id) {
//         Object o = map.get(id);
//         Map valueMap = (Map) o;
            
//         if (valueMap == null) {
//             valueMap = new HashMap();
//             map.put(id, valueMap);
//         }
            
//         return valueMap;
//     }
        
    /*public*/ Object getValueByIds(Object ids[]) {
		callsCount++;
		Object value = mainMap.get(new IdArray(ids));
		if(value != null){
			hits++;
		}
		return value;
    }
        
    /*public*/ void setValueByIds(Object ids[], Object value) {
		uniqueCalls++;
		session.methodCallsCacheInUse = true;
		if(value == null) {
			value = nullValue;
		}
// 		if(mainMap.size() < cacheSize) {
			mainMap.put(new IdArray(ids), value);
// 		}
    }

	int getCacheSize() {
		return mainMap.size();
	}
	
	long getHits() {
		return hits;
	}
	
	/*public*/ long getCallsCount() {
		return callsCount;
	}

	void incCallsCount() {
		callsCount++;
	}
	
	/*public*/ long getUniqueCalls() {
		return uniqueCalls;
	}

    void clear() {
        mainMap.clear();
    }

	private static class IdArray {
		Object ids[];
		IdArray(Object ids[]) {
			this.ids = ids;
		}

		public boolean equals(Object obj) {
			if(obj instanceof IdArray) {
				IdArray other = (IdArray)obj;
				return new EqualsBuilder().append(ids, other.ids).isEquals();
			}
			return false;
		}

		public int hashCode() {
			return new HashCodeBuilder(1877, 2953).append(ids).toHashCode();
		}
	}
}
