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

class PathExtractorM extends Lexer;
options {
	charVocabulary = '\3'..'\177';
	k = 6;
	filter = SCARF;
}
{
	static public final int NO_MAPPING = 0;
	static public final int ENTITY_MAPPING = 1;
	static public final int ATTRIBUTE_MAPPING = 2;
	private boolean done = false;
	private PrintWriter out;
	private int mappingType = NO_MAPPING;
	private boolean isEntityMapping = false;
	private String attributeTargetTypeName = null;
	private String extId = null;

	public void uponEOF() throws TokenStreamException, CharStreamException {
        done = true;
	}

	public static void main(String[] args) throws Exception {
		try {
			File sourceFiles[] = new File(args[0]).listFiles();
			String armSchemaName = args.length == 4 ? args[2].toUpperCase() : "AP214_ARM";
			String aimSchemaName = args.length == 4 ? args[3].toUpperCase() : "AUTOMOTIVE_DESIGN";
			String armSchemaNameLc = armSchemaName.toLowerCase();

			PrintWriter mainOut = (
				new PrintWriter(new FileWriter(new File(args[1], armSchemaNameLc + ".path"))));
			mainOut.println("schema_mapping " + armSchemaNameLc + " (" + 
				armSchemaName + ", " + aimSchemaName + ");");
// 			PrintWriter mainOut = new PrintWriter(new FileWriter(new File(args[1], "ap212.path")));
// 			mainOut.println("schema_mapping ap212_arm (AP212_ARM, ELECTROTECHNICAL_DESIGN);");
			mainOut.println();
			for(int i = 0; i < sourceFiles.length; i++) {
				File sourceFile = sourceFiles[i];
				String targetFileName = sourceFile.getName();
				if(!targetFileName.endsWith(".m")) continue;
				targetFileName = targetFileName.substring(0, targetFileName.length() - 1) + "path";
				File targetFile = new File(args[1], targetFileName);
				PathExtractorM lexer = new PathExtractorM(new FileReader(sourceFile));
				lexer.setFilename(sourceFile.getCanonicalPath());
				lexer.out = new PrintWriter(new FileWriter(targetFile));
				while ( !lexer.done ) {
					lexer.nextToken();
				}
				if(lexer.isEntityMapping) {
					lexer.out.print(" end_entity_mapping;");
				}
				lexer.out.print(" end_uof_mapping;"); 
				lexer.out.close();
				mainOut.println("include '" + targetFileName + "';"); 
			}
			mainOut.println();
			mainOut.println("end_schema_mapping;");
			mainOut.close();
		}
		catch(TokenStreamRecognitionException e) {
			System.err.println(e.recog);
		}
		catch(TokenStreamException e) {
			System.err.println(e);
		}
	}
}

INSTR_START	:
		{ getColumn() == 1}?
		"$" (UOF_MAPPING | ENTITY_ATTRIBUTE_MAPPING | AIM_ELEMENT | PATH | END)
  ;

protected
UOF_MAPPING	:
		"uo%" { setCommitToPath(true); } id:ID "@"
		{ out.print(" uof_mapping " + id.getText() + " (" + id.getText() + ");"); }
	;

protected
ENTITY_ATTRIBUTE_MAPPING	:
		"ae%" { setCommitToPath(true); } s1:ID 
		( (WS)+ "to" (WS)+ s2:ID (WS)+ "(as" (WS)+ s3:ID ")" )?
		"@"
		{	String sText1 = s1.getText();
			if(Character.isUpperCase(sText1.charAt(0))) {
				if(isEntityMapping) {
					out.print(" end_entity_mapping;");
				}
				out.print(" entity_mapping " + sText1.toLowerCase() + " (" + sText1.toLowerCase());
				mappingType = ENTITY_MAPPING;
				isEntityMapping = true;
			} else {
				mappingType = ATTRIBUTE_MAPPING;
				if(s2 == null) {
					out.print(" attribute_mapping " + sText1 + " (" + sText1);
					attributeTargetTypeName = null;
				} else {
					out.print(" attribute_mapping " + s3.getText() + "_" + s2.getText() + 
						" (" + s3.getText());
					attributeTargetTypeName = s2.getText();
				}
			}
			extId = null;
		}
	;

protected
AIM_ELEMENT	:
		"ai%" { setCommitToPath(true); } 
		{ out.print(", "); }
		EXTSTRING[false]
		{	if(mappingType == ATTRIBUTE_MAPPING && attributeTargetTypeName != null) {
				out.print(", " + attributeTargetTypeName);
			}
			out.print(");");
		}
		"@"
	;

protected
PATH	:
		"rp%" 
		{	setCommitToPath(true); }
		(	{	if(mappingType == ENTITY_MAPPING) {
					out.print(" mapping_constraints; ");
				}
				if(extId != null) {
					out.print("(" + extId + ")(");
				}
			}
			EXTSTRING[true]
			{	if(extId != null) {
					out.print(" )");
				}
				if(mappingType == ENTITY_MAPPING) {
					out.print(" end_mapping_constraints;");
				}
			}
		)?
		"@"
	;

protected
END	:
		"end"
		{	if(mappingType == ATTRIBUTE_MAPPING) {
				out.print(" end_attribute_mapping;");
			}
		}
	;


protected
EXTSTRING[boolean path]	:
		(EXTSYMBOL[path])+
		{ out.print($getText); }
	;

protected
EXTSYMBOL[boolean path]	:
		(	("(see NOTE") => "(see NOTE"! { $append("(** see NOTE"); } (~(')'))+ ')'! { $append(" *)"); }
		|	{!path}? ("PATH") => "PATH" { $setText("$PATH"); }
		|	{!path}? ("IDENTICAL MAPPING") => "IDENTICAL MAPPING" { $setText("$IDENTICAL_MAPPING"); } 
// 		|	{!path}? ("([") => "([" e:EXTID "]" { extId = e.getText(); $setText("(*([" + extId + "]*)");  }
// 		|	{!path}? ("[PATH])") => "[PATH])" { $setText("(*[PATH])*)"); }
// 		|	{!path}? ("(PATH)") => "(PATH)" { $setText("(*(PATH)*)"); }
		|	'$'!
			(	"co%"! { $append("(** "); } (~('|'))+ '|'! { $append(" *)"); }
			|	(	"or%" (~('|'))* '|'
				|	"so%" (~('@'))* '@'
				)
				{ $setText(""); }
			)
		|	("(*") => COMMENT { $setText(""); }
		|	(	options {
					generateAmbigWarnings = false;
				}
			: "\r\n"
			| '\r'
			| '\n'
			) { newline(); }
		|	~('@'|'$'|'\r'|'\n')
		)
	;

protected
EXTID	:
		(~(']' | '\r' | '\n'))+
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
		"(*"
		(	options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2) != ')' }? '*'
		|	'\r' '\n'		{newline(); out.println(); }
		|	'\r'			{newline(); out.println(); }
		|	'\n'			{newline(); out.println(); }
		|	~('*'|'\n'|'\r')
		)*
		"*)"
		{$setType(Token.SKIP);}
	;
