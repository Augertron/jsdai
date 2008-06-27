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

// Special class for schema definition

package jsdai.mapping;
import jsdai.lang.*;

import java.lang.reflect.*;

public class SMapping extends SSuper {

	public static final String time_stamp = "2002-08-28 T07:25:19";
	public static final String version = "3.1, build 999, 2002-08-13";
	public static final SSuper ss = SSuper.initSuper(new SMapping());

	protected CEntity makeInstanceX(Class c) throws java.lang.InstantiationException, java.lang.IllegalAccessException {
			return (CEntity)c.newInstance();
	}

	protected void setDataField(Class cl, String name, Object value) throws NoSuchFieldException, IllegalAccessException {
		Field fd = cl.getDeclaredField(name);
		fd.set(null, value);
	}

	protected Object getObject(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		return field.get(obj);
	}
	protected int getInt(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		return field.getInt(obj);
	}
	protected double getDouble(Object obj, Field field) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		return field.getDouble(obj);
	}

	protected void setObject(Object obj, Field field, Object value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		field.set(obj, value);
	}
	protected void setInt(Object obj, Field field, int value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		field.setInt(obj, value);
	}
	protected void setDouble(Object obj, Field field, double value) throws java.lang.IllegalArgumentException, java.lang.IllegalAccessException {
		field.setDouble(obj, value);
	}

}
