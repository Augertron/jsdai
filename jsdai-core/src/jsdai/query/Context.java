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

import java.util.List;
import jsdai.lang.SdaiException;

/**
 *
 * Created: Thu May  8 15:38:00 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class Context {
	public final SdaiQueryEngine query;
	public Context childContext;
	public Constraint contextConstraint;
	public int resultSetOffset;
	public Context next;

	protected Context(SdaiQueryEngine query) {
		this.query = query;
		this.childContext = null;
		this.resultSetOffset = 1;
		contextConstraint = null;
	}

	protected Context(Context other) {
		query = other.query;
		init(other);
		contextConstraint = null;
	}

	protected void init(Context other) {
		childContext = null;
		resultSetOffset = other.resultSetOffset;
	}

	public void addChildContext(Context context) {
		context.next = childContext;
		childContext = context;
	}

	abstract protected List getValueList();

	abstract protected void childUnion() throws SdaiException;

	abstract protected void childIntersect() throws SdaiException;

	abstract protected void childAnd() throws SdaiException;

	abstract protected void childOr() throws SdaiException;

	abstract protected void childNot() throws SdaiException;

	abstract protected void childProcess() throws SdaiException;

	abstract protected ConstraintFactory getRegConstraintFactory();

	abstract protected Context dup(boolean split, boolean startItems);

	abstract protected Context dupVal();

	abstract protected void assign(Context other) throws SdaiException;

	abstract protected boolean isNarrowedBy(Context other) throws SdaiException;

// 	protected String debug() { return ""; }
} // Context
