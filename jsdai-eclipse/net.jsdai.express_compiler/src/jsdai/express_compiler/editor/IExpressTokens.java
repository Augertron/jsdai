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

package jsdai.express_compiler.editor;

/*

	tokens to be used in the special scanner and perhaps in ExpressPairMatcher and other tools

*/

public interface IExpressTokens {
	
	int TokenEOF                    =  -1;

	int TokenOTHER                  =   0;

	int TokenLPAREN                 =   1;
	int TokenRPAREN                 =  11;
	int TokenLBRACKET               =   2;
	int TokenRBRACKET               =  12;

	// not really in express, but still let's use them for matches, etc.
	int TokenLBRACE                 =   3;
	int TokenRBRACE                 =  13;
	int TokenLESS                   =   4;
	int TokenGREATER                =  14;

	int TokenSEMICOLON              =   21; // not needed for our current purposes, but may be useful

	int TokenBEGIN                  =  101;
	int TokenEND                    =  201;
	int TokenALIAS                  =  102; 
	int TokenEND_ALIAS              =  202;
	int TokenCASE                   =  103;
	int TokenEND_CASE               =  203;
	int TokenCONSTANT               =  104;
	int TokenEND_CONSTANT           =  204;

	// CONTEXT - END_CONTEXT ****************** amendment keyword, ignore for now
	// CREATE - END_CREATE *** (Express X)
	// DEPENDENT_MAP - END_DEPENDENT_MAP *** (Express X)

	int TokenENTITY                 =	 105;
	int TokenEND_ENTITY             =  205;
	int TokenFUNCTION               =  106;
	int TokenEND_FUNCTION           =  206;
	int TokenIF                     =  107;
	int TokenEND_IF                 =  207;

	int TokenELSIF                  =  307;
	int TokenELSE                   =  407;

	int TokenLOCAL                  =  108;
	int TokenEND_LOCAL              =  208;

	// MAP - END_MAP *** (Express X)
	// MODEL - END_MODEL ************** amendment keyword, ignore for now

	int TokenPROCEDURE              =  109;
	int TokenEND_PROCEDURE          =  209;
	int TokenREPEAT                 =  110;
	int TokenEND_REPEAT             =  210;
	int TokenRULE                   =  111;
	int TokenEND_RULE               =  211;
	int TokenSCHEMA                 =  112;
	int TokenEND_SCHEMA             =  212;

	// SCHEMA_MAP - END_SCHEMA_MAP *** (Express X)
	// SCHEMA_VIEW - END_SCHEMA_VIEW *** (Express X)

	int TokenSUBTYPE_CONSTRAINT     =  113;
	int TokenEND_SUBTYPE_CONSTRAINT =  213;
	int TokenTYPE                   =  114;
	int TokenEND_TYPE               =  214;
	
	// VIEW - END_VIEW *** (Express X)


}

