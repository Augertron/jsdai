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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** This class is for internal JSDAI use only. Applications shall not use it.*/
public abstract class PopulationDependentBound extends CEntity implements EBound {

	protected String em_wrong_method_used = SdaiSession.line_separator + AdditionalMessages.DI_WRMP;

	Object class_obj;

	protected PopulationDependentBound() {
		super();
	}


	protected int getBound_valueInternal(SdaiContext context) throws SdaiException {
		if (context == null) {
			throw new SdaiException(SdaiException.SY_ERR, "SDAI context shall be specified");
		}
		String schema = ((EPopulation_dependent_bound)this).getSchema_name(null);
		String method_name = ((EPopulation_dependent_bound)this).getMethod_name(null);
		String entity_name = ((EPopulation_dependent_bound)this).getEntity_name(null);
		if (schema == null) {
			throw new SdaiException(SdaiException.SY_ERR, 
				"Schema is not submitted for population dependent bound");
		}
		String str;
		int var;
		String normalized_sch_name = schema.substring(0,1).toUpperCase() + schema.substring(1).toLowerCase();
		if (entity_name != null) {
			String normalized_ent_name = entity_name.substring(0,1).toUpperCase() + entity_name.substring(1).toLowerCase();
			str = SdaiSession.SCHEMA_PREFIX + normalized_sch_name + ".C" + normalized_ent_name;
			var = 1;
		} else {
			str = SdaiSession.SCHEMA_PREFIX + normalized_sch_name + ".S" + normalized_sch_name;
			var = 2;
		}
		Class cl;
		try {
			cl = Class.forName(str, true, SdaiClassLoaderProvider.getDefault().getClassLoader());
		} catch (ClassNotFoundException ex) {
			throw new SdaiException(SdaiException.SY_ERR, "Special class for Express schema not found: " + str);
		}
		if (class_obj == null) {
			if (var == 1) {
				try {
					class_obj = cl.newInstance();
				} catch (java.lang.InstantiationException ex1) {
					throw new SdaiException(SdaiException.SY_ERR, ex1);
				} catch (java.lang.IllegalAccessException ex2) {
					throw new SdaiException(SdaiException.SY_ERR, ex2);
				}
			} else {
				try {
					Field field = cl.getField("ss");
					class_obj =  (SSuper)field.get(null);
				} catch (java.lang.NoSuchFieldException ex1) {
					throw new SdaiException(SdaiException.SY_ERR, ex1);
				} catch (java.lang.IllegalAccessException ex2) {
					throw new SdaiException(SdaiException.SY_ERR, ex2);
				}
			}
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.param == null) {
			staticFields.param = new Class[1];
			staticFields.param[0] = SdaiContext.class;
			staticFields.arg = new Object [1];
		}
		Method meth;
		try {
			meth = cl.getDeclaredMethod(method_name, staticFields.param);
		} catch (java.lang.NoSuchMethodException ex) {
			throw new SdaiException(SdaiException.SY_ERR, "Method not found: " + method_name);
		}
		staticFields.arg[0] = context;
		Object res;
		try {
			res = meth.invoke(class_obj, staticFields.arg);
		} catch (Exception ex) {
			throw new SdaiException(SdaiException.SY_ERR, ex);
		}
		return ((Value)res).getInteger();
	}

}
