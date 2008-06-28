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
import java.util.Iterator;

/**
 * This class provides a way to manage method calls cache used by mapping operations.
 * It is employed by some JSDAI tools and is not required for normal JSDAI usage.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public class MethodCallsCacheManager {
	static final Object CACHE_GET_MAPPED_ATTRIBUTE = new CacheId("CACHE_GET_MAPPED_ATTRIBUTE");
	static final Object CACHE_TEST_MAPPED_ATTRIBUTE = new CacheId("CACHE_TEST_MAPPED_ATTRIBUTE");
	static final Object CACHE_TEST_MAPPED_ENTITY = new CacheId("CACHE_TEST_MAPPED_ENTITY");
    static final Object CACHE_FIND_MAPPED_USERS = new CacheId("CACHE_FIND_MAPPED_USERS");
    static final Object CACHE_TEST_MAPPING_PATH = new CacheId("CACHE_TEST_MAPPING_PATH");

	private MethodCallsCacheManager() { }

	/**
	 * The constant to use for <code>setThreadId</code> method.
	 */
	public static final int SWING_CREATED_THREAD = 0;

	/**
	 * The constant to use for <code>setThreadId</code> method.
	 */
	public static final int OWN_CREATED_THREAD = 1;

// 	static ThreadLocal threadList = new ThreadLocal();
	static ThreadLocal threadId = new ThreadLocal();
    static ThreadLocal parent = new ThreadLocal();

	/**
	 * Sets thread identifier.
	 *
	 * @param id The id code
	 */
	public static void setThreadId(int id) {
		Integer idInt = new Integer(id);
		threadId.set(idInt);
	}

    /**
	 * Sets parent object.
	 *
	 * @param _parent The parent
	 */
	public static void setParent(Object _parent) {
		parent.set(_parent);
	}

    /**
	 * Returns the parent object.
	 *
	 * @return The parent as an <code>Object</code>
	 */
	public static Object getParent() {
		return parent.get();
	}

//     protected static int getThreadId() {
// 		Integer val = (Integer) threadId.get();

// 		if (val == null) {
// 			return SWING_CREATED_THREAD;
// 		}

// 		return val.intValue();
// 	}

// 	private static List methodCallsCaches() {
// /*		if (getThreadId() != OWN_CREATED_THREAD) {
// 			return new LinkedList(); // do not use methodCallsCache from EventDispatch thread!
// 		}
// */
// 		List val = (List) threadList.get();
		
// 		if (val == null) {
// 			val = new LinkedList();
// 			threadList.set(val);
// 			createCaches();
// 		}

// 		return val;
// 	}

// 	public static void clear(SdaiSession session) {
// 		for (int i = 0; i < methodCallsCaches().size(); i++) {
// 			MethodCallsCache cache = methodCallsCache(i);
			
// 			if (cache.session == session) {
// 				cache.clear();
// 			}
// 		}
// 	}
	
// 	public static void addCache(MethodCallsCache cache) {
// 		methodCallsCaches().add(cache);
// 	}

// 	static private void createCaches() {
// 		SdaiSession currSession = SdaiSession.getSession();
// 		addCache(new MethodCallsCache(currSession, CACHE_GET_MAPPED_ATTRIBUTE)); // getMappedAttributeCache
// 		addCache(new MethodCallsCache(currSession, CACHE_TEST_MAPPED_ATTRIBUTE)); // testMappedAttributeCache
//         addCache(new MethodCallsCache(currSession, CACHE_GO_THROUGH_PATH)); // goThroughPathCache
//         addCache(new MethodCallsCache(currSession, CACHE_FIND_MAPPED_USERS)); // findMappedUsers
// 	}
	
// 	static MethodCallsCache methodCallsCache(int index) {
// 		return (MethodCallsCache) methodCallsCaches().get(index);
// 	}
	
// 	static MethodCallsCache methodCallsCache(SdaiSession session, int cacheId) {
// 		for (int i = 0; i < methodCallsCaches().size(); i++) {
// 			MethodCallsCache cache = methodCallsCache(i);
// 			if ((cache.session == session) && (cache.cacheId == cacheId)) {
// 				return cache;
// 			}
// 		}
		
// 		return null;
// 	}

	static private void createCaches(SdaiSession session) {
		synchronized (session) {
			Map cacheMap;
			if(session.methodCallsCacheMap == null) {
				cacheMap = session.methodCallsCacheMap = new HashMap();
				//FIXME: comment out this line to stop printing statistics
				if(false) {
					hookStatistics(session);
				}
			} else {
				cacheMap = session.methodCallsCacheMap;
			}
			session.methodCallsCacheInUse = false;
			cacheMap.put(CACHE_GET_MAPPED_ATTRIBUTE,  // getMappedAttributeCache
						 new MethodCallsCache(session, Implementation.mappingReadCacheSize));
			cacheMap.put(CACHE_TEST_MAPPED_ATTRIBUTE, // testMappedAttributeCache
						 new MethodCallsCache(session, Implementation.mappingReadCacheSize));
			cacheMap.put(CACHE_TEST_MAPPED_ENTITY, // testMappedEntityCache
						 new MethodCallsCache(session, Implementation.mappingReadCacheSize));
			cacheMap.put(CACHE_FIND_MAPPED_USERS,     // findMappedUsers
						 new MethodCallsCache(session, Implementation.mappingReadCacheSize));
			cacheMap.put(CACHE_TEST_MAPPING_PATH,     // testMappingPath
						 new MethodCallsCache(session, Implementation.mappingReadCacheSize));
		}
	}

	static MethodCallsCache methodCallsCache(SdaiSession session, 
											 Object cacheId) {
		synchronized (session) {
			if(session.methodCallsCacheMap == null/* || session.methodCallsCacheMap.size() == 0*/) {
				createCaches(session);
			}
			return (MethodCallsCache)session.methodCallsCacheMap.get(cacheId);
		}
	}

	/**
	 * Clears the caches associated with SDAI session.
	 *
	 * @param session The session to clear the caches for
	 */
	public static void clear(SdaiSession session) {
		if(session.methodCallsCacheInUse) {
			synchronized (session) {
// 				session.methodCallsCacheMap.clear();
				session.methodCallsCacheInUse = false;
				Iterator cacheIter = session.methodCallsCacheMap.values().iterator();
				while(cacheIter.hasNext()) {
					MethodCallsCache cache = (MethodCallsCache)cacheIter.next();
					cache.clear();
				}
			}
		}
	}

	private static class PrintStatisticThread extends Thread {
		private SdaiSession session;

		PrintStatisticThread(SdaiSession session) {
			this.session = session;
		}

		public void run() {
			Iterator cacheIter = session.methodCallsCacheMap.entrySet().iterator();
			while(cacheIter.hasNext()) {
				Map.Entry entry = (Map.Entry)cacheIter.next();
				MethodCallsCache cache = (MethodCallsCache)entry.getValue();
				System.out.println(entry.getKey() + ": size = " + cache.getCacheSize() +
								   " gets: " + cache.getCallsCount() +
								   " sets: " + cache.getUniqueCalls() +
								   " hits: " + cache.getHits());
			}
			System.out.println("testMappedEntityMillis: " + ObjectMapping.testMappedEntityMillis);
			System.out.println("findMappedUsersMillis: " + ObjectMapping.findMappedUsersMillis);
			System.out.println("testMappedUsersMillis: " + ObjectMapping.testMappedUsersMillis);
			System.out.println("getMappedAttributeMillis: " + ObjectMapping.getMappedAttributeMillis);
			System.out.println("testMappedAttributeMillis: " + ObjectMapping.testMappedAttributeMillis);
			System.out.println("findMostSpecificMappingsMillis: " + 
							   Mapping.findMostSpecificMappingsMillis);
			System.out.println("addMostSpecificMappingsMillis: " + Mapping.addMostSpecificMappingsMillis);
			System.out.println("getMappedAttributeEntityMillis: " + 
							   ObjectMapping.getMappedAttributeEntityMillis);
			//System.out.println(": " + );
		}
	}

	private static void hookStatistics(SdaiSession session) {
		Runtime.getRuntime().addShutdownHook(new PrintStatisticThread(session));
	}

	private static class CacheId {
		private String string;

		private CacheId(String string) {
			this.string = string;
		}

		public String toString() {
			return string;
		}
	}
}


