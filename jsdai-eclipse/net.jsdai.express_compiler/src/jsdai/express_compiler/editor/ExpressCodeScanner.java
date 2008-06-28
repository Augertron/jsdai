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

import java.util.ArrayList;
import java.util.List;

import jsdai.express_compiler.preferences.ExpressEditorPreferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

/**
 *  Express code scanner
 */
public class ExpressCodeScanner extends RuleBasedScanner implements IExpressKeywords {

//	private TextAttribute fComment;
//	private TextAttribute fKeyword;
//	private TextAttribute fType;
//	private TextAttribute fString;
//  private TextAttribute fOperator;
//  private TextAttribute fBuiltin;
//	private ExpressColorProvider fColorProvider;



/* express symbols used for operators and delimeters

. , ; :
* + - =
% ’ \ /
< > [ ]
{ } | e
( ) <= <>
>= <* := ||
** -- (* *)
:=: :<>:

*/


	protected class OperatorRule implements IRule {

		/** EXPRESS operators */
		// private final char[] EXPRESS_OPERATORS = { ';', '(', ')', '{', '}', '.', '=', '/', '\\', '+', '-', '*', '[', ']', '<', '>', ':', '?', '!', ',', '|', '&', '^', '%', '~'};
		private final char[] EXPRESS_OPERATORS = { '.', ',', ';', ':',
                                         			 '*', '+', '-', '=', 
			                                         '%', '\u2019', '\\', '/', 
			                                         '<', '>', '[', ']', 
			                                         '{', '}', '|', // 'e',  // see what happens with e - better remove, or have to implement spec treatement when it is the first letter of a word
			                                         '(', ')' };




		/** Token to return for this rule */
		private final IToken fToken;

		/**
		 * Creates a new operator rule.
		 *
		 * @param token Token to use for this rule
		 */
		public OperatorRule(IToken token) {
			fToken= token;
		}

		/**
		 * Is this character an operator character?
		 *
		 * @param character Character to determine whether it is an operator character
		 * @return <code>true</code> iff the character is an operator, <code>false</code> otherwise.
		 */
		public boolean isOperator(char character) {
			for (int index= 0; index < EXPRESS_OPERATORS.length; index++) {
				if (EXPRESS_OPERATORS[index] == character)
					return true;
			}
			return false;
		}

		/*
		 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
		 */
		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isOperator((char) character)) {
				do {
					character= scanner.read();
				} while (isOperator((char) character));
				scanner.unread();
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}










	/**
	 * Creates Express code scanner with the given color provider.
	 * 
	 * @param provider the color provider
	 */
	public ExpressCodeScanner(ExpressColorProvider provider) {	


/*
//		IToken keyword = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.KEYWORD)));
		// let's have more bang
		IToken keyword = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.KEYWORD), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken type = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.TYPE)));
		IToken type = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.TYPE), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken operator = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.OPERATOR)));
		IToken operator = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.OPERATOR), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken builtin = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.BUILTIN)));
		IToken builtin = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.BUILTIN), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
		IToken string = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.STRING)));
		IToken comment = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.SINGLE_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.DEFAULT)));
*/



		// let's have more bang

			initRules(provider);

		}
		
		public void initRules(ExpressColorProvider provider) {
		
		IPreferenceStore store = EditorsPlugin.getDefault().getPreferenceStore();

		boolean keyword_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_KEYWORD_BOLD);
		boolean keyword_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_KEYWORD_ITALIC); 
		boolean keyword_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_KEYWORD_STRIKE);
		boolean keyword_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_KEYWORD_UNDERLINE);
		int keyword_flags = 0; 
		if (keyword_bold) keyword_flags |= SWT.BOLD;
		if (keyword_italic) keyword_flags |= SWT.ITALIC;
		if (keyword_strike) keyword_flags |= TextAttribute.STRIKETHROUGH;
		if (keyword_underline) keyword_flags |= TextAttribute.UNDERLINE;

		boolean type_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_TYPE_BOLD);
		boolean type_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_TYPE_ITALIC); 
		boolean type_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_TYPE_STRIKE);
		boolean type_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_TYPE_UNDERLINE);
		int type_flags = 0; 
		if (type_bold) type_flags |= SWT.BOLD;
		if (type_italic) type_flags |= SWT.ITALIC;
		if (type_strike) type_flags |= TextAttribute.STRIKETHROUGH;
		if (type_underline) type_flags |= TextAttribute.UNDERLINE;
		
		boolean op_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_OPERATOR_BOLD);
		boolean op_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_OPERATOR_ITALIC); 
		boolean op_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_OPERATOR_STRIKE);
		boolean op_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_OPERATOR_UNDERLINE);
		int op_flags = 0; 
		if (op_bold) op_flags |= SWT.BOLD;
		if (op_italic) op_flags |= SWT.ITALIC;
		if (op_strike) op_flags |= TextAttribute.STRIKETHROUGH;
		if (op_underline) op_flags |= TextAttribute.UNDERLINE;

		boolean bc_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_BOLD);
		boolean bc_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_ITALIC); 
		boolean bc_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_STRIKE);
		boolean bc_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_UNDERLINE);
		int bc_flags = 0; 
		if (bc_bold) bc_flags |= SWT.BOLD;
		if (bc_italic) bc_flags |= SWT.ITALIC;
		if (bc_strike) bc_flags |= TextAttribute.STRIKETHROUGH;
		if (bc_underline) bc_flags |= TextAttribute.UNDERLINE;
		
		boolean bf_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_BOLD);
		boolean bf_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_ITALIC); 
		boolean bf_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_STRIKE);
		boolean bf_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_UNDERLINE);
		int bf_flags = 0; 
		if (bf_bold) bf_flags |= SWT.BOLD;
		if (bf_italic) bf_flags |= SWT.ITALIC;
		if (bf_strike) bf_flags |= TextAttribute.STRIKETHROUGH;
		if (bf_underline) bf_flags |= TextAttribute.UNDERLINE;

		boolean bp_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_BOLD);
		boolean bp_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_ITALIC); 
		boolean bp_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_STRIKE);
		boolean bp_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_UNDERLINE);
		int bp_flags = 0; 
		if (bp_bold) bp_flags |= SWT.BOLD;
		if (bp_italic) bp_flags |= SWT.ITALIC;
		if (bp_strike) bp_flags |= TextAttribute.STRIKETHROUGH;
		if (bp_underline) bp_flags |= TextAttribute.UNDERLINE;

/*
		boolean slc_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_SINGLE_BOLD);
		boolean slc_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_SINGLE_ITALIC); 
		boolean slc_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_SINGLE_STRIKE);
		boolean slc_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_SINGLE_UNDERLINE);
		int slc_flags = 0; 
		if (slc_bold) slc_flags |= SWT.BOLD;
		if (slc_italic) slc_flags |= SWT.ITALIC;
		if (slc_strike) slc_flags |= TextAttribute.STRIKETHROUGH;
		if (slc_underline) slc_flags |= TextAttribute.UNDERLINE;
*/

/*

		boolean str_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_STRING_BOLD);
		boolean str_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_STRING_ITALIC); 
		boolean str_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_STRING_STRIKE);
		boolean str_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_STRING_UNDERLINE);
		int str_flags = 0; 
		if (str_bold) str_flags |= SWT.BOLD;
		if (str_italic) str_flags |= SWT.ITALIC;
		if (str_strike) str_flags |= TextAttribute.STRIKETHROUGH;
		if (str_underline) str_flags |= TextAttribute.UNDERLINE;

*/
		
		boolean default_bold = store.getBoolean(ExpressEditorPreferences.EXPRESS_DEFAULT_BOLD);
		boolean default_italic = store.getBoolean(ExpressEditorPreferences.EXPRESS_DEFAULT_ITALIC); 
		boolean default_strike = store.getBoolean(ExpressEditorPreferences.EXPRESS_DEFAULT_STRIKE);
		boolean default_underline = store.getBoolean(ExpressEditorPreferences.EXPRESS_DEFAULT_UNDERLINE);
		int default_flags = 0; 
		if (default_bold) default_flags |= SWT.BOLD;
		if (default_italic) default_flags |= SWT.ITALIC;
		if (default_strike) default_flags |= TextAttribute.STRIKETHROUGH;
		if (default_underline) default_flags |= TextAttribute.UNDERLINE;

		
		Color keyword_bg = null;
		boolean keyword_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_KEYWORD_TRANSPARENT);
		if (!keyword_transparent) {
			keyword_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_KEYWORD_BG_COLOR));		
		}
		Color type_bg = null;
		boolean type_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_TYPE_TRANSPARENT);
		if (!type_transparent) {
			type_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_TYPE_BG_COLOR));		
		}
		Color op_bg = null;
		boolean op_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_OPERATOR_TRANSPARENT);
		if (!op_transparent) {
			op_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_OPERATOR_BG_COLOR));
		}
		Color bc_bg = null;
		boolean bc_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_TRANSPARENT);
		if (!bc_transparent) {
			bc_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_BG_COLOR));
		}
		Color bf_bg = null;
		boolean bf_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_TRANSPARENT);
		if (!bf_transparent) {
			bf_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_BG_COLOR));
		}
		Color bp_bg = null;
		boolean bp_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_TRANSPARENT);
		if (!bp_transparent) {
			bp_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_BG_COLOR));
		}
		Color default_bg = null;
		boolean default_transparent = store.getBoolean(ExpressEditorPreferences.EXPRESS_DEFAULT_TRANSPARENT);
		if (!default_transparent) {
			default_bg = provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_DEFAULT_BG_COLOR));
		}
		
		IToken keyword = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_KEYWORD_COLOR)), keyword_bg, keyword_flags));
		IToken type = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_TYPE_COLOR)), type_bg, type_flags));
		IToken operator = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_OPERATOR_COLOR)), op_bg, op_flags));
		IToken builtin_constant = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_CONSTANT_COLOR)), bc_bg, bc_flags));
		IToken builtin_function = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_FUNCTION_COLOR)), bf_bg, bf_flags));
		IToken builtin_procedure = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_BUILTIN_PROCEDURE_COLOR)), bp_bg, bp_flags));
		
//		IToken string = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_STRING_COLOR))));

//		IToken string = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_STRING_COLOR)), provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_STRING_BG_COLOR)), str_flags));

//		IToken comment = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_SINGLE_COLOR))));

//		IToken comment = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_SINGLE_COLOR)), provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_SINGLE_BG_COLOR)), slc_flags));

//		IToken other = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_DEFAULT_COLOR))));
		IToken other = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(ExpressEditorPreferences.EXPRESS_DEFAULT_COLOR)), default_bg, default_flags));








		setDefaultReturnToken(other);
		
		List rules = new ArrayList();

		// Add rule for single line comments.
//		rules.add(new EndOfLineRule("--", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
//		rules.add(new SingleLineRule("'", "'", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
//		rules.add(new SingleLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new ExpressWhiteSpaceDetector()));


		// Add rule for express operators
		rules.add(new OperatorRule(operator));



		// Add word rule for keywords, types, operators, built-in constants, functions, procedures
//		WordRule wordRule= new WordRule(new ExpressWordDetector(), other);
		ExpressWordRule wordRule= new ExpressWordRule(new ExpressWordDetector(), other);

		for (int i= 0; i < keywords.length; i++)
			wordRule.addWord(keywords[i], keyword);

		for (int i= 0; i < types.length; i++)
			wordRule.addWord(types[i], type);

		for (int i= 0; i < operators.length; i++)
			wordRule.addWord(operators[i], operator);

		for (int i= 0; i < builtin_constants.length; i++)
			wordRule.addWord(builtin_constants[i], builtin_constant);

		for (int i= 0; i < builtin_functions.length; i++)
			wordRule.addWord(builtin_functions[i], builtin_function);

		for (int i= 0; i < builtin_procedures.length; i++)
			wordRule.addWord(builtin_procedures[i], builtin_procedure);

		rules.add(wordRule);

		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);




	}	

}

