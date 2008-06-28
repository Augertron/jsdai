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

package jsdai.beans;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;

import java.lang.reflect.*;

public class MappingOperationsThreadManager {
	protected static InvokeMethodWorker invokeMethodWorker = null;
	protected static TestMappedAttributeWorker testMappedAttributeWorker = null;
	protected static FindMappedUsersWorker findMappedUsersWorker = null;

    static public Object invokeMethod(Object scope, Class methodClass, String methodName, Class[] paramTypes, Object[] params, boolean wait) {
        try {
			if (methodClass == null) {
                methodClass = scope.getClass();
            }
            Method method = methodClass.getDeclaredMethod(methodName, paramTypes);
			Object rv = MappingOperationsThreadManager.invokeMethod(method, scope, params, wait);
			return rv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

	static public Object invokeMethod(Object scope, String methodName, Class[] paramTypes, Object[] params) {
        return invokeMethod(scope, null, methodName, paramTypes, params, true);
	}

	static protected Object invokeMethod(Method method, Object object, Object[] params, boolean wait) {
		invokeMethodWorker = new InvokeMethodWorker();
		invokeMethodWorker.setParams(method, object, params);

        if (wait) {
            invokeMethodWorker.setWait(wait);
        }

		invokeMethodWorker.start(); // required for SwingWorker 3
		return invokeMethodWorker.rv;
	}

	protected static class InvokeMethodWorker extends SwingWorker {
		Method method;
		Object object;
	    Object[] params;
		Object rv;
        Object parent;

        public void setParams(Method _method, Object _object, Object[] _params) {
			method = _method;
			object = _object;
			params = _params;
		}

		public Object construct() {
			try {
                MethodCallsCacheManager.setParent(object);
                MethodCallsCacheManager.setThreadId(MethodCallsCacheManager.OWN_CREATED_THREAD);
				rv = method.invoke(object, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	protected static class TestMappedAttributeWorker extends SwingWorker {
		EEntity instance;
		EGeneric_attribute_mapping sourceAttribute;
		ASdaiModel targetDomain;
	    ASdaiModel mappingDomain;
		int mode;
		boolean rv;

		public void setParams(EEntity _instance, EGeneric_attribute_mapping _sourceAttribute,
							  ASdaiModel _targetDomain, ASdaiModel _mappingDomain, int _mode) {
			instance = _instance;
			sourceAttribute = _sourceAttribute;
			targetDomain = _targetDomain;
			mappingDomain = _mappingDomain;
			mode = _mode;
		}

		public Object construct() {
			try {
				MethodCallsCacheManager.setThreadId(MethodCallsCacheManager.OWN_CREATED_THREAD);
				rv = instance.testMappedAttribute(sourceAttribute, targetDomain, mappingDomain, mode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	public static boolean testMappedAttribute(EEntity instance, EGeneric_attribute_mapping sourceAttribute, ASdaiModel targetDomain, ASdaiModel mappingDomain, int mode) {
		testMappedAttributeWorker = new TestMappedAttributeWorker();
		testMappedAttributeWorker.setParams(instance, sourceAttribute, targetDomain, mappingDomain, mode);
		testMappedAttributeWorker.setWait(true);
		testMappedAttributeWorker.start();
		return testMappedAttributeWorker.rv;
	}

	protected static class FindMappedUsersWorker extends SwingWorker {
		EEntity instance;
		EEntity_mapping source_type;
		AAttribute_mapping attribute;
	    ASdaiModel data_domain;
		ASdaiModel mapping_domain;
		AAttribute_mapping users;
		int mode;
		AEntity rv;

		public void setParams(EEntity _instance, EEntity_mapping _source_type, AAttribute_mapping _attribute,
									   ASdaiModel _data_domain, ASdaiModel _mapping_domain, AAttribute_mapping _users,
										int _mode) {
			instance = _instance;
			source_type = _source_type;
			attribute = _attribute;
			data_domain = _data_domain;
			mapping_domain = _mapping_domain;
			users = _users;
			mode = _mode;
		}

		public Object construct() {
			try {
				MethodCallsCacheManager.setThreadId(MethodCallsCacheManager.OWN_CREATED_THREAD);
				rv = instance.findMappedUsers(source_type, attribute, data_domain, mapping_domain, users, mode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	public static AEntity findMappedUsers(EEntity instance, EEntity_mapping source_type, AAttribute_mapping attribute,
									   ASdaiModel data_domain, ASdaiModel mapping_domain, AAttribute_mapping users,
										int mode) {
		findMappedUsersWorker = new FindMappedUsersWorker();
		findMappedUsersWorker.setParams(instance, source_type, attribute, data_domain, mapping_domain, users, mode);
		findMappedUsersWorker.setWait(true);
		findMappedUsersWorker.start();
		return findMappedUsersWorker.rv;
	}
}
