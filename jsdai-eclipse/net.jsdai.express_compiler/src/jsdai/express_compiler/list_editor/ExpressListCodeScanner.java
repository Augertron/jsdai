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

package jsdai.express_compiler.list_editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 *  Express code scanner
 */
//public class ExpressListCodeScanner extends RuleBasedScanner implements IExpressKeywords {
public class ExpressListCodeScanner extends RuleBasedScanner {

//	private TextAttribute fComment;
//	private TextAttribute fKeyword;
//	private TextAttribute fType;
//	private TextAttribute fString;
//  private TextAttribute fOperator;
//  private TextAttribute fBuiltin;
//	private ExpressColorProvider fColorProvider;

	/**
	 * Creates Express code scanner with the given color provider.
	 * 
	 * @param provider the color provider
	 */
	public ExpressListCodeScanner(ExpressListColorProvider provider) {	

//		IToken keyword = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.KEYWORD), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken type = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.TYPE), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken operator = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.OPERATOR), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken builtin = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.BUILTIN), provider.getColor(ExpressColorProvider.BACKGROUND), SWT.BOLD));
//		IToken string = new Token(new TextAttribute(provider.getColor(ExpressColorProvider.STRING)));
		IToken comment = new Token(new TextAttribute(provider.getColor(ExpressListColorProvider.SINGLE_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(provider.getColor(ExpressListColorProvider.DEFAULT)));

		setDefaultReturnToken(other);
		
		List rules = new ArrayList();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("#", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		//rules.add(new SingleLineRule("'", "'", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
//		rules.add(new SingleLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		//Add generic whitespace rule.
		rules.add(new WhitespaceRule(new ExpressListWhiteSpaceDetector()));

		// Add word rule for keywords, types, operators, built-in constants, functions, procedures

		WordRule wordRule= new WordRule(new ExpressListWordDetector(), other);
/*
		for (int i= 0; i < keywords.length; i++)
			wordRule.addWord(keywords[i], keyword);
		for (int i= 0; i < types.length; i++)
			wordRule.addWord(types[i], type);
		for (int i= 0; i < operators.length; i++)
			wordRule.addWord(operators[i], operator);
		for (int i= 0; i < builtins.length; i++)
			wordRule.addWord(builtins[i], builtin);
*/
		rules.add(wordRule);

		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);




	}	

}

