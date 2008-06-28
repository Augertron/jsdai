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

package com.lksoft.util.logging;

import java.util.logging.*;
import java.io.*;

/**
 * @author Viktoras Kovaliovas
 */
public class SimpleFormatter2
	extends SimpleFormatter
{
	private static final StringBuffer sb = new StringBuffer();

	private static final String lineSeparator = System.getProperty("line.separator");

	public SimpleFormatter2() {
		super();
	}

	public synchronized String format(LogRecord record) {
		sb.setLength(0);

		if (record.getLevel() != Level.INFO && record.getLevel() != Level.FINE) {
			sb.append(record.getLevel().toString());
			sb.append(": ");
		}

		sb.append(formatMessage(record));
		sb.append(lineSeparator);

		if (record.getThrown() != null) {
			try {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				record.getThrown().printStackTrace(pw);
				pw.close();
				sb.append(sw.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return sb.toString();
	}
}