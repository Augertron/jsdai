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

import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.collections.AST;
import java.util.HashSet;
import java.util.Set;
import jsdai.SMapping_schema.AEntity_mapping;
import jsdai.lang.SdaiException;
import jsdai.mappingUtils.paths.CommonLowerCaseAST;


/**
 *
 * Created: Thu Sep 30 16:31:07 2004
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public final class EntityMappingAST extends CommonLowerCaseAST implements ParserAST, SourceLocator {
	private int line;
	private String filename;
	private AEntity_mapping uofInstances;
	public EntityMappingAST nextEntityMapping;
	public EntityMappingAST extended;
	
	public EntityMappingAST(Token token) {
		super(token);
		uofInstances = null;
		nextEntityMapping = null;
		extended = null;
	}

	public EntityMappingAST(AST other) {
		setType(other.getType());
		setText(other.getText());
		if(other instanceof ParserAST) {
			line = ((ParserAST)other).getLine();
		}
		if(other instanceof SourceLocator) {
			filename = ((SourceLocator)other).getFilename();
		}
		if(other instanceof EntityMappingAST) {
			uofInstances = ((EntityMappingAST)other).uofInstances;
		}
		// It may not be safe to attach this information from the other
		nextEntityMapping = null;
		extended = null;
	}

    public void setLine(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}

    public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public void setUofInstances(AEntity_mapping uofInstances) {
		this.uofInstances = uofInstances;
	}

	public AEntity_mapping getUofInstances() {
		return uofInstances;
	}

	public AST dupAttachTree() {
		AST newAST = new EntityMappingAST(this);
		newAST.setFirstChild(getFirstChild());
		return newAST;
	}

	public int getInPath() {
		return NOT_IN_PATH;
	}

	public void setInPath(int inPath) { }
	
	public EntityMappingAST matchAlternative(MappingPathParser parser, EntityMappingAST extEntMapAst
											 ) throws RecognitionException, SdaiException {
		if(nextEntityMapping == null) {
			return this;
		} else {
			AST extAim = extEntMapAst.getFirstChild().getNextSibling();
			Set extAimSet = new HashSet();
			for(AST extAimChild = extAim.getFirstChild();
				extAimChild != null;
				extAimChild = extAimChild.getNextSibling()) {

				int extAimChildType = extAimChild.getType();
				if(extAimChildType == MappingPathParser.TEMPLATE_CALL) {
					((TemplateCallAST)extAimChild).resolve(extAim, parser, this);
				} else if(extAimChildType != MappingPathParser.ORIGINAL_LOCATION) {
					if(!(extAimChild instanceof EntityAST)) {
						throw new SemanticException(extAim.getClass() + ": " + extAim.toStringList(),
													extEntMapAst.getFilename(), extEntMapAst.getLine());
					}
					EntityAST entityExtAim = (EntityAST)extAimChild;
					extAimSet.add(entityExtAim.declaration.definition);
				}
			}
			for(EntityMappingAST entMapAst = this;
				entMapAst != null;
				entMapAst = entMapAst.nextEntityMapping) {

				if(entMapAst.matchAlternative(parser, extAimSet)) {
					return entMapAst;
				}
			}
			throw new SemanticException("Matching base entity mapping alternative not found " +
										"for extended mapping: " +
										extEntMapAst.getFirstChild().getFirstChild().getText(),
										extEntMapAst.getFilename(), extEntMapAst.getLine());
		}
	}

	private boolean matchAlternative(MappingPathParser parser, Set extAimSet
									 ) throws RecognitionException, SdaiException {
		AST aim = getFirstChild().getNextSibling();
		AST aimChild = aim.getFirstChild();
		Set aimSet = new HashSet();
		while(aimChild != null) {
			int aimChildType = aimChild.getType();
			if(aimChildType == MappingPathParser.TEMPLATE_CALL) {
				((TemplateCallAST)aimChild).resolve(aim, parser, this);
			} else if(aimChildType != MappingPathParser.ORIGINAL_LOCATION) {
				EntityAST entityAim = (EntityAST)aimChild;
				aimSet.add(entityAim.declaration.definition);
			}
			aimChild = aimChild.getNextSibling();
		}
		return aimSet.containsAll(extAimSet);
	}

} // EntityMappingAST
