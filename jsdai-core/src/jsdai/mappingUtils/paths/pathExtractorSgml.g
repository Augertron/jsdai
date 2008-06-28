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

header {
	package jsdai.mappingUtils.paths;
	import java.io.*;
	import antlr.*;
}

class PathExtractorSgml extends Lexer;
options {
	charVocabulary = '\3'..'\177';
	k = 4;
	filter = SCARF;
}
{
	static public final int NO_MAPPING = 0;
	static public final int ENTITY_MAPPING = 1;
	static public final int ATTRIBUTE_MAPPING = 2;
	private static boolean done = false;
	private static PrintWriter out;
	private int mappingType = NO_MAPPING;
	private static boolean isEntityMapping = false;
	private static String attributeTargetTypeName = null;

	public void uponEOF() throws TokenStreamException, CharStreamException {
        done=true;
	}

	public static void main(String[] args) throws Exception {
		try {
			PathExtractorSgml lexer = new PathExtractorSgml(new FileReader(args[0]));
			out = new PrintWriter(new FileWriter(args[1]));
			String armSchemaName = args.length == 4 ? args[2].toUpperCase() : "AP210_ARM";
			String aimSchemaName = (args.length == 4 ? args[3].toUpperCase() : 
				"ELECTRONIC_ASSEMBLY_INTERCONNECT_AND_PACKAGING_DESIGN");
			String armSchemaNameLc = armSchemaName.toLowerCase();
			out.print("schema_mapping " + armSchemaNameLc + 
				" (" + armSchemaName + ", " + aimSchemaName + ");");
			while ( !done ) {
				lexer.nextToken();
			}
			out.print(" end_schema_mapping;"); 
			out.close();
		}
		catch(TokenStreamRecognitionException e) {
			System.err.println("exception: "+e.recog);
		}
		catch(TokenStreamException e) {
			System.err.println("exception: "+e);
		}
	}
}

ELEMENT_ENDS	:
		"</" (PATH_END | UOF_MAPPING_END | MAPPING_ROW_END)
	;

PATH_LINE_OR_START_SELECT	:
		"<ref" ( PATH_LINE | PATH_START)
	;

protected
PATH_LINE	:
		".path.line" (WS)+ { setCommitToPath(true); } "line=" (WS)* s:STRING (WS)* ">"
		{ out.print("\t" + s.getText() + " "); }
	;

protected
PATH_START	:
		"erence.path.col.entry>"
		{	if(mappingType == ENTITY_MAPPING) {
				out.print(" mapping_constraints;");
			}
		}
	;

protected
PATH_END	:
		"reference.path.col.entry>"
		{	if(mappingType == ENTITY_MAPPING) {
				out.print(" end_mapping_constraints;");
			}
		}
	;

UOF_MAPPING_START	:
		"<uof.mapping.sub.tbl" (WS)+ { setCommitToPath(true); } 
		"uof.name.linkend=" (WS)* s:STRING TO_END_OF_ELEMENT ">"
		{ out.print(" uof_mapping " + s.getText() + " (" + s.getText() + ");"); }
	;

protected
UOF_MAPPING_END	:
		"uof.mapping.sub.tbl>"
		{	if(isEntityMapping) {
				out.print(" end_entity_mapping;");
				isEntityMapping = false;
			}
			out.print(" end_uof_mapping;"); }
	;

protected
MAPPING_ROW_END	:
		"mapping.sub.tbl.a" ("ppobj.row>" | "ssert.row>" | "ttr.row")
		{	if(mappingType == ATTRIBUTE_MAPPING) {
				out.print(" end_attribute_mapping;");
			}
		}
	;

ENTITY_OR_ATTRIBUTE_MAPPING_SELECT	:
		"<a"
		( "pp.elem." (ENTITY_ATTRIBUTE_MAPPING_START | ATTRIBUTE_MAPPING1_START | ATTRIBUTE_MAPPING2_START)
		| AIM_ELEMENT
		)
	;

protected
ENTITY_ATTRIBUTE_MAPPING_START	:
		"appobj.col.entry" (WS)+ { setCommitToPath(true); } 
		"appobj.name.linkend=" (WS)* s:STRING TO_END_OF_ELEMENT ">"
		{	String sText = s.getText();
			if(Character.isUpperCase(sText.charAt(0))) {
				if(isEntityMapping) {
					out.print(" end_entity_mapping;");
				}
				//out.println("(* " + getLine() + " *)");
				out.print(" entity_mapping " + sText.toLowerCase() + " (" + sText.toLowerCase());
				mappingType = ENTITY_MAPPING;
				isEntityMapping = true;
			} else {
				//out.println("(* " + getLine() + " *)");
				out.print(" attribute_mapping " + sText + " (" + sText);
				mappingType = ATTRIBUTE_MAPPING;
				attributeTargetTypeName = null;
			}
		}
	;

protected
ATTRIBUTE_MAPPING1_START	:
		"assert.col.entry" (WS)+ { setCommitToPath(true); } 
		"obj1.name.linkend=" (WS)* STRING (WS)*
		"obj2.name.linkend=" (WS)* s2:STRING (WS)* 
		"attr.name.linkend=" (WS)* "\"(as " s3:ID ")\"" TO_END_OF_ELEMENT ">"
		{	//out.println("(* " + getLine() + " *)");
			out.print(" attribute_mapping " + s3.getText() + "_" + s2.getText() + " (" + s3.getText());
			mappingType = ATTRIBUTE_MAPPING;
			attributeTargetTypeName = s2.getText(); }
	;

protected
ATTRIBUTE_MAPPING2_START	:
		"attr.col.entry" (WS)+ { setCommitToPath(true); } 
		"attr.name.linkend=" (WS)* s:STRING (WS)* ">"
		{	//out.println("(* " + getLine() + " *)");
			out.print(" attribute_mapping " + s.getText() + " (" + s.getText());
			mappingType = ATTRIBUTE_MAPPING;
			attributeTargetTypeName = null; }
	;

protected
AIM_ELEMENT	:
		"im.element.col.entry" (WS)+ { setCommitToPath(true); } 
		"name=" (WS)* 
		( ("\"PATH\"") => "\"PATH\""
			{	if(mappingType == ATTRIBUTE_MAPPING) {
					if(attributeTargetTypeName != null) {
						out.print(", (*PATH*), " + attributeTargetTypeName);
					}
					out.print(");");
				} else {
					System.err.println("PATH can't be used in entity mapping in line " + getLine());
					out.print("(*!!ERROR!!*)");
				}
			}
		| ("\"IDENTICAL MAPPING\"") => "\"IDENTICAL MAPPING\""
			{	if(mappingType == ATTRIBUTE_MAPPING) {
					if(attributeTargetTypeName != null) {
						out.print(", (*IDENTICAL MAPPING*), " + attributeTargetTypeName);
					}
					out.print(");");
				} else {
					System.err.println("(*IDENTICAL MAPPING*) can't be used in entity mapping in line " + getLine());
					out.print("(*!!ERROR!!*)");
				}
			}
		| s:STRING
			{	out.print(", " + s.getText());
				if(mappingType == ATTRIBUTE_MAPPING &&attributeTargetTypeName != null) {
					out.print(", " + attributeTargetTypeName);
				}
				out.print(");");
			}
		) TO_END_OF_ELEMENT ">"
	;

STRING	:
		(	'\''! ( ENTITY | ~('\'' | '&' | '\r' | '\n') )* '\''!
		|	'"'! ( ENTITY | ~('\"' | '&' | '\r' | '\n') )* '"'!
		)
		;

protected
ENTITY	:
		'&'! 
		(	"equal" { $setText("="); }
		|	"openangl" { $setText("<"); }
		|	"clsangl" { $setText(">"); }
		)
		';'!
	;

protected
LETTER	:
		'a'..'z' | 'A'..'Z' | '_'
	;

protected
DIGIT	:
		'0'..'9'
	;

protected
ID	:
		LETTER (LETTER | DIGIT)*
	;

protected
TO_END_OF_ELEMENT	:
		(	(WS) => WS
		|	~('>')
		)*
	;

protected
SCARF	:
		(	(WS) => WS	// track line numbers while you scarf
		|	.
		)
	;

protected
WS	:
		(	COMMENT
		|	' '
		|	'\t'
		|	 
			(	options {
					generateAmbigWarnings = false;
				}
			: "\r\n"
			| '\r'
			| '\n'
			) { newline(); out.println(); }
		)
		{ $setType(Token.SKIP); }
	;

protected
COMMENT	:
		"<!--"
		(	options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2) != '-' && LA(3) != '>' }? '-'
		|	'\r' '\n'		{newline(); out.println(); }
		|	'\r'			{newline(); out.println(); }
		|	'\n'			{newline(); out.println(); }
		|	~('-'|'\n'|'\r')
		)*
		"-->"
		{$setType(Token.SKIP);}
	;
