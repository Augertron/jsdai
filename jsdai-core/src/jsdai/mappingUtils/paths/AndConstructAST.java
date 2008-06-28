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

package jsdai.mappingUtils.paths;

import java.util.*;

import antlr.*;
import antlr.collections.AST;

/**
 *
 * @author  Vaidas Nargėlas
 * @version 
 */
public class AndConstructAST extends MappingAST {

	public List andLastList;
	public AndEndAST andEnd;
	public boolean intersection;
	public List subpaths;
	public LinkedList remainingStateList;
	public int oldConstraintLevel;
	public boolean oldPathDetected;

	public AndConstructAST(Token token, int type, String name, List andLastList) {
		super(token);
		initialize(type, name);
		this.andLastList = andLastList;
		init();
	}

	public AndConstructAST(AST other) {
		super(other);
		init();
		if(other instanceof AndConstructAST) {
			AndConstructAST sameOther = (AndConstructAST)other;
			if(sameOther.andLastList != null) {
				andLastList = new LinkedList(sameOther.andLastList);
			}
			andEnd = sameOther.andEnd;
		}
	}

	public AST dupAttachTree() {
		AST newAST = new AndConstructAST(this);
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}

	private void init() {
		andEnd = null;
		intersection = false;
		subpaths = null;
		remainingStateList = null;
		oldConstraintLevel = -1;
		oldPathDetected = false;
	}
}
