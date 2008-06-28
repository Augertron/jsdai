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

import org.eclipse.jface.text.rules.*;

/**
 *  This scanner recognizes the Express multi line comments and our spec comments,
 *  however, when we will replace our comments with special edition 2 tags, this thing will have to be changed as well
 */

public class ExpressListPartitionScanner extends RuleBasedPartitionScanner {

//	public final static String EXPRESS_MULTILINE_COMMENT = "__express_multiline_comment"; //$NON-NLS-1$
//	public final static String EXPRESS_SPEC_COMMENT = "__express_spec_comment"; //$NON-NLS-1$
//	public final static String[] EXPRESS_PARTITION_TYPES = new String[] {EXPRESS_MULTILINE_COMMENT, EXPRESS_SPEC_COMMENT};
	public final static String[] EXPRESS_PARTITION_TYPES = new String[] {};

	/**
	 * Detector for empty comments.
	 */
	static class EmptyCommentDetector implements IWordDetector {

		/* (non-Javadoc)
		* Method declared on IWordDetector
	 	*/
		public boolean isWordStart(char c) {
			return (c == '(');
		}

		/* (non-Javadoc)
		* Method declared on IWordDetector
	 	*/
		public boolean isWordPart(char c) {
			return (c == '*' || c == '(');
		}
	}

	static class WordPredicateRule extends WordRule implements IPredicateRule {
		
		private IToken fSuccessToken;
		
		public WordPredicateRule(IToken successToken) {
			super(new EmptyCommentDetector());
			fSuccessToken= successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}
		
		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(ICharacterScanner, boolean)
		 */
		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
		 */
		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}
	
	
	
	
	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public ExpressListPartitionScanner() {
		super();

//		IToken specComment = new Token(EXPRESS_SPEC_COMMENT);
//		IToken multiComment = new Token(EXPRESS_MULTILINE_COMMENT);

		List rules = new ArrayList();


		// Add rule for single line comments.
		rules.add(new EndOfLineRule("#", Token.UNDEFINED)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		// express uses single quotes for strings but double quotes also for encoded strings? - check
//		rules.add(new SingleLineRule("'", "'", Token.UNDEFINED, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
//		rules.add(new SingleLineRule("\"", "\"", Token.UNDEFINED, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add special case word rule.
//		WordPatternRule wordRule= new WordPatternRule(new EmptyCommentDetector(), "/*", "*/", comment); //$NON-NLS-2$ //$NON-NLS-1$
//		rules.add(wordRule);
		// doing it with a special inner class
//		rules.add(new WordPredicateRule(multiComment));

		// Add rules for multi-line comments 
//		rules.add(new MultiLineRule("(**", "*)", specComment, (char) 0, true)); //$NON-NLS-1$ //$NON-NLS-2$
//		rules.add(new MultiLineRule("(*", "*)", multiComment)); //$NON-NLS-1$ //$NON-NLS-2$
//		rules.add(new MultiLineRule("(*", "*)", multiComment, (char) 0, true)); //$NON-NLS-1$ //$NON-NLS-2$


		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}


}

