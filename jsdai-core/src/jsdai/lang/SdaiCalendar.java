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
import java.text.*;

/**
 * This class gives methods to convert time values from <code>long</code> to
 * <code>String</code> representation and vice versa.
 * The format of the time description by string is specified in ISO 8601.
 * @see "ISO 10303-21::9.2.2 file_name"
 */
public class SdaiCalendar extends java.util.GregorianCalendar {

	SimpleDateFormat dformat;

	/**
	 * Creates a new <code>SdaiCalendar</code>.
	 */
	public SdaiCalendar() {
		super();
		if (dformat == null) {
			dformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}
	}


/**
 * Converts the time value specified by <code>String</code> to
 * <code>long</code> equivalent. The format of the description by string
 * is specified in ISO 8601 (for details, see ISO 10303-21::9.2.2 file_name).
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiCalendar cal = ...;
 *    String time = "2000-06-25T06:04:02";
 *    long time_as_long = cal.timeStampToLong(time);
 *    System.out.println("Time: " + time_as_long);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The following line will be printed:
 * <pre>    Time: 961909442000</pre>
 * @param time_stamp the time value written as string according to rules
 * specified in ISO 8601.
 * @return time as <code>long</code> variable.
 * @see #longToTimeStamp
 * @see "ISO 10303-21::9.2.2 file_name."
 */
	public long timeStampToLong(String time_stamp) {
//		synchronized (SdaiCommon.syncObject) {
		String str;
		if (time_stamp.length() > 19) {
			str = time_stamp.substring(0,10) + time_stamp.substring(11);
		} else {
			str = time_stamp;
		}
		ParsePosition position = new ParsePosition(0);
		Date dt = dformat.parse(str, position);
		if (dt == null) {
			return -1;
		}
		setTime(dt);
		return getTimeInMillis();
//		} // syncObject
	}


/**
 * Converts the time value from <code>long</code> to <code>String</code>
 * representation. The format of the time description by string
 * is specified in ISO 8601 (for details, see ISO 10303-21::9.2.2 file_name).
 * <P><B>Example:</B>
 * <P><TT><pre>    SdaiCalendar cal = ...;
 *    long time_as_long = 961909442886;
 *    String time = cal.longToTimeStamp(time_as_long);
 *    System.out.println("Time: " + time);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The following line will be printed:
 * <pre>    Time: 2000-06-25T06:04:02</pre>
 * @param time the time as <code>long</code> number.
 * @return the time value written as string according to rules specified in ISO 8601.
 * @see #timeStampToLong
 * @see "ISO 10303-21::9.2.2 file_name."
 */
	public String longToTimeStamp(long time) {
//		synchronized (SdaiCommon.syncObject) {
		setTimeInMillis(time);
		return dformat.format(getTime());
//		} // syncObject
	}
}
