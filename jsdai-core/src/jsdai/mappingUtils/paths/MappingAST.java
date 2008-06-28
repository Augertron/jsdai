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

import antlr.*;
import antlr.collections.AST;

import jsdai.lang.*;
import jsdai.SExtended_dictionary_schema.*;
import jsdai.SMapping_schema.*;

public class MappingAST extends CommonAST implements ParserAST {
	int line;
	int inPath = UNDETERMINED;

	public MappingAST() {
	}

	public MappingAST(Token tok) {
		initialize(tok);
	}

	public MappingAST(AST other) {
		setType(other.getType());
		setText(other.getText());
		if(other instanceof ParserAST) {
			line = ((ParserAST)other).getLine();
			inPath = ((ParserAST)other).getInPath();
		}
	}
	
	public int getLine() { return line; }

	public void setLine(int line) { this.line = line; }

	public void initialize(Token tok) {
		setText(tok.getText());
		setType(tok.getType());
		setLine(tok.getLine());
	}
	
//	public EEntity getMappingEntity() {
//		return mappingEntity;
//	}
//
//	public void setMappingEntity(EEntity mappingEntity) {
//		this.mappingEntity = mappingEntity;
//	}
	
	public AST dupAttachTree() {
		AST newAST = new MappingAST(this);
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}
	
	public void setInPath(int inPath) {
		this.inPath = inPath;
	}
	
	public int getInPath() {
		return inPath;
	}
	
	/** Get the token text for this node  */
	public String getText() {
		String tokenText = super.getText();
		switch(inPath) {
		case IN_PATH:
			tokenText = "(+" + tokenText + ")";
			break;
		case NOT_IN_PATH:
			tokenText = "(-" + tokenText + ")";
			break;
		}
		return tokenText;
	}

	protected final String getPlainText() {
		return super.getText();
	}
	
}
