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

// Java interface for entity real_constraint

package jsdai.mapping;
import jsdai.lang.*;

public interface EReal_constraint extends EAttribute_value_constraint {

	/// methods for attribute:constraint_value, base type: REAL
	public boolean testConstraint_value(EReal_constraint type) throws SdaiException;
	public double getConstraint_value(EReal_constraint type) throws SdaiException;
	public void setConstraint_value(EReal_constraint type, double value) throws SdaiException;
	public void unsetConstraint_value(EReal_constraint type) throws SdaiException;

}
