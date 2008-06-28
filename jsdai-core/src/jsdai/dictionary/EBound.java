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

// Java interface for entity bound

package jsdai.dictionary;
import jsdai.lang.*;

public interface EBound extends EEntity {

	/// methods for attribute:bound_value, base type: INTEGER
	public boolean testBound_value(EBound type) throws SdaiException;
	public int getBound_value(EBound type) throws SdaiException;
	public int getBound_value(EBound type, SdaiContext context) throws SdaiException;
	public void setBound_value(EBound type, int value) throws SdaiException;
	public void unsetBound_value(EBound type) throws SdaiException;

}
