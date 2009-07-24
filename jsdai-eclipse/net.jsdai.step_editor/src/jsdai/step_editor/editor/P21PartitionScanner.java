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

package jsdai.step_editor.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/*

   perhaps we might want to use a separate partition for complex instances

a complex instannce is a thing that starts with #nn...nn=( 
and ends with ;  - or with ); but it is enough to know just ;
The start part is a bit tricky, because it is of variable length

We could establish this partition:
=( - start
; - end

perhaps it is enough?
The initial instance  to the left of = then would always be in default partition and could not be differentiated by color,
but the rest of the line could.
Or, we could check if there is ( after = and assign a special token for such instance number as opposed to other instance numbers,
it could take colors and attributes from the same property that instance numbers inside complex do.

So, do we have a simple solution?
Perhaps

Now then, if we haves such a partition, then it is sipmlified to recognize entities versus types
any identifier starting with letter (or underscore, I guess) that starts immediately after =
is entity, otherwise - type
That is in the default partition,
in complex entity partition  entity starts  immediately after =(

    

*/


public class P21PartitionScanner extends RuleBasedPartitionScanner {

	/**
	 * Detector for empty comments - P21 multi-line comments are the same as in java
	 */
	static class EmptyCommentDetector implements IWordDetector {

		/*
		 * @see IWordDetector#isWordStart
		 */
		public boolean isWordStart(char c) {
			return (c == '/');
		}

		/*
		 * @see IWordDetector#isWordPart
		 */
		public boolean isWordPart(char c) {
			return (c == '*' || c == '/');
		}
	}

	/**
	 * Word rule for empty comments - again, P21 multi-line comments are the same as in java
	 */
	static class EmptyCommentRule extends WordRule implements IPredicateRule {

		private IToken fSuccessToken;
		/**
		 * Constructor for EmptyCommentRule.
		 * @param successToken
		 */
		public EmptyCommentRule(IToken successToken) {
			super(new EmptyCommentDetector());
			fSuccessToken= successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}

		/*
		 * @see IPredicateRule#evaluate(ICharacterScanner, boolean)
		 */
		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return evaluate(scanner);
		}

		/*
		 * @see IPredicateRule#getSuccessToken()
		 */
		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}

	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public P21PartitionScanner() {
		super();

//		IToken string= new Token(JAVA_STRING);
//		IToken character= new Token(JAVA_CHARACTER);
//		IToken javaDoc= new Token(JAVA_DOC);
		IToken multiLineComment= new Token(P21Constants.P21_MULTILINE_COMMENT);
//		IToken singleLineComment= new Token(JAVA_SINGLE_LINE_COMMENT);


		IToken complexInstance = new Token(P21Constants.P21_COMPLEX_INSTANCE);


		List rules= new ArrayList();

		// Add rule for single line comments.
		// are there single line comments in p21?
		//		rules.add(new EndOfLineRule("//", singleLineComment)); //$NON-NLS-1$

		// Add rule for strings. - in p21, only single quote strings?
//		rules.add(new SingleLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add rule for character constants.
//		rules.add(new SingleLineRule("'", "'", character, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add special case for empty multiline comments word rule.
		EmptyCommentRule wordRule= new EmptyCommentRule(multiLineComment);
		rules.add(wordRule);

		// Add rules for multi-line comments - the same as in java
		rules.add(new MultiLineRule("/*", "*/", multiLineComment)); //$NON-NLS-1$ //$NON-NLS-2$


		// Add rule for the partition for complex instances
		rules.add(new MultiLineRule("=(",";", complexInstance));

		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
	
	
	

}