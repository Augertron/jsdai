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

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

/** This class is for internal JSDAI use only. A user shall not use it within any application. */
public abstract class SSuper {

	SdaiModel model;  // the dictionary SdaiModel to which SSuper relates to
	static XClassMapManager xClassesManager = null;

	protected SSuper() {
	}

	protected abstract CEntity makeInstanceX(Class c)
		throws java.lang.InstantiationException, java.lang.IllegalAccessException;
	protected abstract void setDataField(Class cl, String name, Object value) throws NoSuchFieldException, IllegalAccessException;

	protected abstract Object getObject(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;
	protected abstract int getInt(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;
	protected abstract double getDouble(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;

	protected abstract void setObject(Object obj, Field field, Object value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;
	protected abstract void setInt(Object obj, Field field, int value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;
	protected abstract void setDouble(Object obj, Field field, double value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException;

	protected ESchema_definition getUnderlyingSchema() throws SdaiException {
		return model.described_schema;
	}

	CEntity makeInstance(Class c, SdaiModel model, int typeIndex, long id) throws SdaiException {
		CEntity e;
		try {
			e = makeInstanceX(c);
		} catch (java.lang.IllegalAccessException ex) {
			throw (SdaiException)(new SdaiException(SdaiException.SY_ERR).initCause(ex));
		} catch (java.lang.InstantiationException ex) {
			String base = SdaiSession.line_separator + AdditionalMessages.EI_CREF;
			throw (SdaiException)(new SdaiException(SdaiException.SY_ERR, base).initCause(ex));
		}
		e.owning_model = model;
		if (typeIndex >= 0) {
			if (typeIndex < ((CSchema_definition)model.underlying_schema).owning_model.schemaData.entities.length) {
if (SdaiSession.debug2) System.out.println("  inst created: " + c.getName());
				if (model.lengths[typeIndex] >= model.instances_sim[typeIndex].length) {
					model.ensureCapacity(typeIndex);
				}
				if (id > 0) {
					if (!model.insertInstance(typeIndex, e, id)) {
						throw new SdaiException(SdaiException.SY_ERR);
					}
				} else {
					model.instances_sim[typeIndex][model.lengths[typeIndex]] = e;
					model.invalidate_quick_find();
//					model.sorted[typeIndex] = false;
				}
				model.lengths[typeIndex]++;
			} else {
				throw new SdaiException(SdaiException.SY_ERR);
			}
		}
		return e;
	}

	static protected SSuper initSuper(SSuper ss) {
		return SdaiSession.initSuper(ss);
	}

	private static XClassMapManager getXClassesManager() {
		if (xClassesManager == null) {
			xClassesManager = new XClassMapManager();
		}

		return xClassesManager;
	}

	protected static synchronized Class findXClass(Class mainClass) {
		return getXClassesManager().findXClass(mainClass);
	}

	static class XClassMapManager {
		protected Map classesMap = null;

		public XClassMapManager() {
			classesMap = new HashMap();
		}

		public Class findXClass(Class mainClass) {
			Class XClass = (Class) classesMap.get(mainClass);
			if (XClass == null) {
				String className = mainClass.getName();
				int dotPos = className.lastIndexOf('.');
				if (className.charAt(dotPos + 1) == 'C') {
					String newClassName = className.substring(0, dotPos) + ".Cx" + className.substring(dotPos + 2, className.length());
					try {
						XClass =
							Class.forName(newClassName, true,
									SdaiClassLoaderProvider.getDefault().getClassLoader());
					} catch (ClassNotFoundException e) {}
				}

				if (XClass == null)
					XClass = mainClass;

				classesMap.put(mainClass, XClass);
			}

			return XClass;
		}
	}

}
