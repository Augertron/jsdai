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

package com.lksoft.util;

/**
 * This class is an utility class, that helps tracking time and formating elapsed time as string.
 * For example it can be used to track the work time of some process. Usage example:
 *
 * ElapsedTime time = new ElapsedTime();
 * // do something
 * time.stop();
 * System.out.println("Elapsed time: " + time);
 *
 * Time format is as follows:
 * hours hr. minutes min. secunds sec. mili seconds ms. 
 *
 * @author Viktoras Kovaliovas
 */
public class ElapsedTime
{
	private long time;

	public ElapsedTime() {
		time = System.currentTimeMillis();
	}

	public ElapsedTime stop() {
		time = System.currentTimeMillis() - time;
		return this;
	}

	/**
	 * Returns total time in ms.
	 * @return total time in ms.
	 */
	public long getTime() {
		return time;
	}

	public static int getHours(long time) {
		return (int) Math.floor(time / 3600000);
	}

	public int getHours() {
		return getHours(time);
	}

	public static int getMin(long time) {
		return (int) Math.floor(time % 3600000 / 60000);
	}

	public int getMin() {
		return getMin(time);
	}

	public static int getSecs(long time) {
		return (int) Math.floor(time % 3600000 % 60000 / 1000);
	}

	public int getSecs() {
		return getSecs(time);
	}

	public static int getMSecs(long time) {
		return (int) time % 3600000 % 60000 % 1000;
	}

	public int getMSecs() {
		return getMSecs(time);
	}

	public String toString() {
		return getHours() + " hr. " + getMin() + " min. " + getSecs() + " s. " + getMSecs() + " ms.";
	}

	public static String toString(long time) {
		return getHours(time) + " hr. " + getMin(time) + " min. " + getSecs(time) + " s. " + getMSecs(time) + " ms.";
	}
}