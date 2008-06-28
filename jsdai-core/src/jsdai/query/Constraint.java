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

package jsdai.query;

import jsdai.lang.SdaiException;

/**
 *
 * Created: Thu May  8 15:34:52 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

abstract public class Constraint extends ConstraintContainer {
	protected Constraint next;
	protected Constraint prev;

	/**
	 * Executes in inverse direction. This method gets called from <code>QueryFwd</code>
	 * when query lib <code>inv</code> is executed. It has to be overriden for constraints
	 * that have specific functionality like <code>fwd</code> and <code>val</code>
	 *
	 * @param context a <code>Context</code> value
	 * @exception SdaiException if an error occurs
	 */
	protected void executeInv(Context context) throws SdaiException {
		execute(context);
	}

	public boolean isValueResult() { return false; }

	public boolean containsValConstraints() { return false; }

	public Constraint getNext(){
		return next;
	}

	static protected void executeConstraint(Constraint constraint, Context context) throws SdaiException {
		constraint.execute(context);
	}

} // Constraint
