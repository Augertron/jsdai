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

// Java interface for entity list_type

package jsdai.dictionary;
import jsdai.lang.*;

public interface EList_type extends EVariable_size_aggregation_type {

	/// methods for attribute:unique_flag, base type: BOOLEAN
	public boolean testUnique_flag(EList_type type) throws SdaiException;
	public boolean getUnique_flag(EList_type type) throws SdaiException;
	public void setUnique_flag(EList_type type, boolean value) throws SdaiException;
	public void unsetUnique_flag(EList_type type) throws SdaiException;

}
