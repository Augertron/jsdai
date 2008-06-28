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

import antlr.CommonAST;
import antlr.Token;
import antlr.collections.AST;
import antlr.collections.ASTEnumeration;
import jsdai.mappingUtils.paths.ParserAST;

/**
 *
 * Created: Tue Oct 19 15:16:30 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class StateLink extends CommonAST implements ParserAST {
	public final WalkerState state;
	public int line;
	public boolean wasVisited;

	public StateLink(WalkerState state, int line) {
		initialize(MappingPathParser.STATE_LINK, "STATE_LINK");
		this.state = state;
		this.line = line;
		wasVisited = false;
	}

	public StateLink(AST other) {
		setType(other.getType());
		setText(other.getText());
		if(other instanceof ParserAST) {
			line = ((ParserAST)other).getLine();
		}
		if(other instanceof StateLink) {
			state = ((StateLink)other).state;
		} else {
			state = null;
		}
		wasVisited = false;
	}

	// Implementation of jsdai.mappingUtils.paths.ParserAST

	/**
	 * Describe <code>getLine</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Describe <code>setLine</code> method here.
	 *
	 * @param line an <code>int</code> value
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * Describe <code>dupAttachTree</code> method here.
	 *
	 * @return an <code>AST</code> value
	 */
	public AST dupAttachTree() {
		AST newAST = new StateLink(this);
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}

	/**
	 * Describe <code>setInPath</code> method here.
	 *
	 * @param inPath an <code>int</code> value
	 */
	public void setInPath(int inPath) { }

	/**
	 * Describe <code>getInPath</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getInPath() {
		return UNDETERMINED;
	}
	
} // StateLink
