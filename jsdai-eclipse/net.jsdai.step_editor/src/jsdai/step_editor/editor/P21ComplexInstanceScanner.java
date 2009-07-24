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

import jsdai.step_editor.preferences.P21EditorPreferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

public class P21ComplexInstanceScanner extends RuleBasedScanner {


	/*
		 what is more special in p21 file?
		 ( , =  we may show these in a separate color
		 $   - unset value, or whatever it is called
		 *  - derived value
		 .SOMETHING. - enumeration probably
		 .T.
		 .F.
		 
		 perhaps we could have all such values in the same group, the same color: .T., .F., *, $, .SOMETHING. - not sure about the last one, because 
		 it is not a fixed keyword, as the others are.
		 
	*/

	
	protected class DelimeterRule implements IRule {

		private final char[] P21_DELIMETERS = { '=', '(', ')', ',', ';' };




		private final IToken fToken;

		public DelimeterRule(IToken token) {
			fToken= token;
		}

		public boolean isDelimeter(char character) {
			for (int index= 0; index < P21_DELIMETERS.length; index++) {
				if (P21_DELIMETERS[index] == character)
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isDelimeter((char) character)) {
				do {
					character= scanner.read();
				} while (isDelimeter((char) character));
				scanner.unread();
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	protected class LogicalValueRule implements IRule {


		private final IToken fToken;

		public LogicalValueRule(IToken token) {
			fToken= token;
		}

		public boolean isLogicalValue(char character) {
			if ('.' == character) {
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isLogicalValue((char) character)) {
				character = scanner.read();
				if ((character == 'T') || (character == 'F') || (character == 'U')) {
//				if ((character == 'T') || (character == 'F')) {
					character = scanner.read();
					if (isLogicalValue((char) character)) {
						return fToken;
					} else {
						scanner.unread();
						scanner.unread();
						scanner.unread();
						return Token.UNDEFINED;
					}
				} else {
					scanner.unread();
					scanner.unread();
					return Token.UNDEFINED;
				}
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}




	protected class EntityRule implements IRule {


		private final IToken fToken;

		public EntityRule(IToken token) {
			fToken= token;
		}

		public boolean isEntity_old(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				/*
						this is a complex instace, so we are in trouble here
						entity may start:    
						=(KUKU   - the 1st entity
						)KUKU    - not 1st entity
						but what if some white space is between entities? - perhaps a variable number of characters then.
						what if the next entity is on the new line?
						perhaps we should try to eliminate possibilities, but it is not so simple, type may be on a new line as well, etc,
						we may need to make a loop backwards while the white space continues
				*/
				
				scanner.unread(); // ready to read the same character again
				scanner.unread(); // ready to read the previous character
				int character2 = scanner.read(); // read the previous character
				scanner.read(); // returned back to where we were, after reading the first character, but now we also have the previous one

				if ((character2 == '(') || (character2 == ')')) {
					// can be only entity here
					return true;
				}
			}
			return false;
		}


		public boolean isEntity(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				
				int count = getPreviousCharacter(scanner); // ready to read the previous character
				
				int character2 = scanner.read(); // read the previous character
				int count2 = 0;
				if (character2 == '(') {
					count2 = getPreviousCharacter(scanner); 
				
				}
				int character3 = scanner.read(); // read the previous character
				for (int i = 0; i < count2; i++) {
					scanner.read(); 
				}
				for (int i = 0; i < count; i++) {
					scanner.read(); 
				}
				if (((character2 == '(') && (character3 == '=')) || (character2 == ')')) {
					// can be only type here
					return true;
				}
			}
			return false;
		}









		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}


		int getPreviousCharacter(ICharacterScanner scanner) {

		/*
		
			(\n\tSOME_TYPE...
		
		*/


			int count = 1;
			scanner.unread(); // ready to read the same character S again
			scanner.unread(); // ready to read the previous character \t
			int character = scanner.read();  // \t read
			while ((character == '\n') || (character == '\r') || (character == '\t') || (character == ' ')) {
				// white space that has to be scipped
				scanner.unread(); // again ready to read the previous character \t  2) iteration: ready to read \n
				scanner.unread(); // ready to read the previous-1 character  \n  2) iteration: ready to read (
				character = scanner.read(); // read previous-1 \n and now ready to read previous \t, character == \n  2) iteration: read (, ready to read \n
// System.out.println("inside getPreviousCharacter - character: " + character);
				count++;
			} 
			scanner.unread(); // ready to read ( again
			// what count should be returned?
			// after the 1st read, will be on \n, will need to read \n, \t and S, so 3 times more
			// count should have value = 3  with 2 while loops
			// what should have been cound with 0 loops, for example:
			// (SOME_TYPE - need 1
			// so, add 1 and then 1 per loop
			return count;
		
			/*
					what if attempt to unread() hits a different partition, perhaps it is wrong to assume that
					read after such an unread will return -1, perhaps unread will do nothing and read will return the same character
					the interface does not provide any indication if unread was successfull
					
					This implementation may then return too high number in count
			*/
		}




		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isEntity((char) character, scanner)) {
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if ((Character.isLetter((char)character)) || (Character.isDigit((char)character)) || (character == '_')) {
						// ok - this continues to be our entity name
					} else {
						scanner.unread();
//System.out.println("p21 default scanner: ENTITY found: " + fToken);
						return fToken;
					}
				} // for 
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	protected class TypeRule implements IRule {


		private final IToken fToken;

		public TypeRule(IToken token) {
			fToken= token;
		}

		public boolean isType_old(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				// may be type, but need to check also the previous character
				// type may start like this:   (KUKU     ,KUKU  
				// - NOTICE that in this partition there are also complex entities starting with (KUKU
				// but they start with =(KUKU, because complex entity instance looks like this:
				//  #123=(KUKU(...)KUKU2(...)KUKU3(...));
				// therefore, if EntityRule recognizes =(KUKU first, then it is safe to use this TypeRule afterwards
				scanner.unread(); // ready to read the same character again
				scanner.unread(); // ready to read the previous character
				int character2 = scanner.read(); // read the previous character
				scanner.read(); // returned back to where we were, after reading the first character, but now we also have the previous one

				if ((character2 == '(') || (character2 == ',')) {
					// can be only type here
					return true;
				}
			}
			return false;
		}

		int getPreviousCharacter(ICharacterScanner scanner) {

		/*
		
			(\n\tSOME_TYPE...
		
		*/


			int count = 1;
			scanner.unread(); // ready to read the same character S again
			scanner.unread(); // ready to read the previous character \t
			int character = scanner.read();  // \t read
			while ((character == '\n') || (character == '\r') || (character == '\t') || (character == ' ')) {
				// white space that has to be scipped
				scanner.unread(); // again ready to read the previous character \t  2) iteration: ready to read \n
				scanner.unread(); // ready to read the previous-1 character  \n  2) iteration: ready to read (
				character = scanner.read(); // read previous-1 \n and now ready to read previous \t, character == \n  2) iteration: read (, ready to read \n
// System.out.println("inside getPreviousCharacter - character: " + character);
				count++;
			} 
			scanner.unread(); // ready to read ( again
			// what count should be returned?
			// after the 1st read, will be on \n, will need to read \n, \t and S, so 3 times more
			// count should have value = 3  with 2 while loops
			// what should have been cound with 0 loops, for example:
			// (SOME_TYPE - need 1
			// so, add 1 and then 1 per loop
	
		return count;
		

		
			/*
					what if attempt to unread() hits a different partition, perhaps it is wrong to assume that
					read after such an unread will return -1, perhaps unread will do nothing and read will return the same character
					the interface does not provide any indication if unread was successfull
					
					This implementation may then return too high number in count
			*/
		}

		public boolean isType(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				// may be type, but need to check also the previous character
				// type may start like this:   (KUKU     ,KUKU
				// - NOTICE that in this partition there are also complex entities starting with (KUKU
				// but they start with =(KUKU, because complex entity instance looks like this:
				//  #123=(KUKU(...)KUKU2(...)KUKU3(...));
				// therefore, if EntityRule recognizes =(KUKU first, then it is safe to use this TypeRule afterwards
				// however, (KUKU may in fact be on two different lines, or some white space in between, or even comment in between - different partition
				// so, assume, if the previous character is comment (-1) then also ok
				// but the privous character means all the white space skipped: \n \r \t  space
				// if not success, then we need to know exactly how many tokens to read forward
				
				int count = getPreviousCharacter(scanner); // ready to read the previous character
				
//				scanner.unread(); // ready to read the same character again
//				scanner.unread(); // ready to read the previous character
				int character2 = scanner.read(); // read the previous character
// System.out.println("after getPreviousCharacter - character2: " + character2 + ", count: " + count);
			  scanner.unread();
			  scanner.unread();
			  int character5 = scanner.read();
			 scanner.read();
			 if (character5 == '/') {
			 	character2 = character5;
			 }
//System.out.println("prev-1: " + character5 + ", prev: " + character6);			  

				// now return back to where we are supposed to be after reading the 1st character
				for (int i = 0; i < count; i++) {
					scanner.read(); 
				}
				if ((character2 == '(') || (character2 == ',') || (character2 == '/')) {
					// can be only type here
					return true;
				}
			}
			return false;
		}



		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isType((char) character, scanner)) {
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if ((Character.isLetter((char)character)) || (Character.isDigit((char)character)) || (character == '_')) {
						// ok - this continues to be our type name
					} else {
						scanner.unread();
//System.out.println("p21 default scanner: TYPE found: " + fToken.);
						return fToken;
					}
				} // for 
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}



	
	
	protected class BinaryValueRule implements IRule {

/*
		BINARY = """" ("0" | "1" | "2" | "3") {HEX} """"
		HEX - from 0 to F
*/
		private final IToken fToken;

		public BinaryValueRule(IToken token) {
			fToken= token;
		}

		public boolean isBinaryValue(char character) {
			if (character == '\"') {
					return true;
			}
			return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {


			int character= scanner.read();
			if (isBinaryValue((char) character)) {
				character = scanner.read();
				// first character has to be 0-3
				if ((character != '0') & (character != '1') && (character != '2') && (character != '3')) {
					// error. The question - do we attempt to find the other end, for better error indication?
					scanner.unread();
					scanner.unread();
					return Token.UNDEFINED;
				}
				for (int unread_count = 3;; unread_count++) {
					character = scanner.read();
					if ((Character.isLetter((char)character)) || (Character.isDigit((char)character))) {
						// possible ok value, as long as the letter is  A-F
						// does p21 allow a-f? not sure
						if (
							(!Character.isDigit((char)character)) 
							&&
							(character != 'A') && (character != 'B') && (character != 'C') && (character != 'D') && (character != 'E') && (character != 'F')
							&&
							(character != 'a') && (character != 'b') && (character != 'c') && (character != 'd') && (character != 'e') && (character != 'f') 
						) {
							// no, not binary
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
						
					} else
					if (character == '\"') {
						// the end of the binary value
						return fToken;
					} else {
						// not binary value, wrong ending, probably an eror in p21 file
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
					}
				} // for 
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}

	protected class EnumerationValueRule implements IRule {


		private final IToken fToken;

		public EnumerationValueRule(IToken token) {
			fToken= token;
		}

		public boolean isEnumerationValue(char character) {
			if ('.' == character) {
					return true;
			}
			return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isEnumerationValue((char) character)) {
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if ((Character.isLetter((char)character)) || (Character.isDigit((char)character)) || (character == '_')) {
						// ok - this continues to be our enumeration value
					} else
					if (isEnumerationValue((char) character)) {
						// the end of enumeration value, if there were any characters in between dots, if not - return undefined
						if (unread_count > 2) {
							// ok
							return fToken;
						} else {
							// not ok
							scanner.unread();
							scanner.unread();
							return Token.UNDEFINED;
						}
					} else {
						// not enumeration value, wrong ending, probably an eror in p21 file
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
					}
				} // for 
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	protected class IntegerValueRule implements IRule {


		private final IToken fToken;

		public IntegerValueRule(IToken token) {
			fToken= token;
		}

		public boolean isIntegerValue(char character) {
			if ((Character.isDigit((char)character)) || (character == '-')) {			
					return true;
			}
			return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isIntegerValue((char) character)) {
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if (Character.isDigit((char)character)) {
						// ok - this continues to be our integer
					} else
					if (character == '.') {
						// try if it is real value instead
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					} else
					if ((character == ',') || (character == ')')) {
						// this is the end condition - are the above two all the possible cases?
						scanner.unread();
						return fToken;
					} else {
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					}
					
				} // for
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	// perhaps extend to IntegerValue, RealValue
	protected class RealValueRule implements IRule {


		private final IToken fToken;

		public RealValueRule(IToken token) {
			fToken= token;
		}

		public boolean isRealValue(char character) {
			// cannot start just with dot
			if ((Character.isDigit((char)character)) || (character == '-')) {			
					return true;
			}
			return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			int previous;
			if (isRealValue((char) character)) {
				
				for (int unread_count = 2, dot_count = 0, e_count = 0, dash_count = 0;; unread_count++) {
					previous = character;
					character = scanner.read();
					if (Character.isDigit((char)character)) {
						// ok - this continues to be our numeric
					} else
					if (character == '.') {
						// may be no more than one such thing. Can it end on it? perhaps
						dot_count++;
						if (dot_count > 1) {
							// busted, let's bail out.
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
						if (e_count > 0) {
							// dot cannot occur after E
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
						if (previous == '-') {
							// cannot occur immediately after -
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					} else
					if ((character == 'E') || (character == 'e')) {
						// may be followed by dash or digit, but no more dots or Es 
						e_count++;
						if (e_count > 1) {
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					} else
					if (character == '-') {
						// may occur immediately after 'E', must be followed by digits
						if ((previous != 'E') && (previous != 'e')) {
							// error - not real value
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
						dash_count++;
						if (dash_count > 1) {  // really cannot happen
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					} else
					if ((character == ',') || (character == ')')) {
						// this is the end condition - are the above two all the possible cases?
						if ((previous == '-') || (previous == 'E') || (previous == 'e')) {
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
						scanner.unread();
						return fToken;
					} else {
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					}
					
				} // for
				
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	protected class UndefinedValueRule implements IRule {


		private final IToken fToken;

		public UndefinedValueRule(IToken token) {
			fToken= token;
		}

		public boolean isUndefined(char character) {
			if ('$' == character) {
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isUndefined((char) character)) {
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}


	protected class RedefinedValueRule implements IRule {


		private final IToken fToken;

		public RedefinedValueRule(IToken token) {
			fToken= token;
		}

		public boolean isRedefined(char character) {
			if ('*' == character) {
					return true;
			}
			return false;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isRedefined((char) character)) {
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}

	protected class InstanceRule implements IRule {

		/** P21 instances in the form #nnnn */

		/** Token to return for this rule */
		private final IToken fToken;

		/**
		 * Creates a new operator rule.
		 *
		 * @param token Token to use for this rule
		 */
		public InstanceRule(IToken token) {
			fToken= token;
		}

		/**
		 * Is this the 1st character of an instance character? 
		 *
		 * @param character Character to determine whether it is an operator character
		 * @return <code>true</code> iff the character is an operator, <code>false</code> otherwise.
		 */
		public boolean isInstanceStart(char character) {
			if ('#' == character) return true;
			else return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}


		/*
		 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
		 */
		public IToken evaluate(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isInstanceStart((char) character)) {
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if (!Character.isDigit((char)character)) {
						if ((character == ')') || (character == ',') || (character == '=')) {
							// ok
							scanner.unread();
							return fToken;
						} else {
							// not ok
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					}
				
				} // for	
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}


		public IToken evaluate_old(ICharacterScanner scanner) {

			int character= scanner.read();
			if (isInstanceStart((char) character)) {
				do {
					character= scanner.read();
				} while (Character.isDigit((char)character));
				scanner.unread();
				return fToken;
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}
	}

	





	public P21ComplexInstanceScanner(P21ColorProvider provider) {


			initRules(provider);

	}

	public void initRules(P21ColorProvider provider) {
		
		IPreferenceStore store = EditorsPlugin.getDefault().getPreferenceStore();


/*		
		boolean iso_bold = store.getBoolean(P21EditorPreferences.idBIso);
		boolean iso_italic = store.getBoolean(P21EditorPreferences.idIIso); 
		boolean iso_strike = store.getBoolean(P21EditorPreferences.idSIso);
		boolean iso_underline = store.getBoolean(P21EditorPreferences.idUIso);
		int iso_flags = 0; 
		if (iso_bold) iso_flags |= SWT.BOLD;
		if (iso_italic) iso_flags |= SWT.ITALIC;
		if (iso_strike) iso_flags |= TextAttribute.STRIKETHROUGH;
		if (iso_underline) iso_flags |= TextAttribute.UNDERLINE;

		boolean section_bold = store.getBoolean(P21EditorPreferences.idBSection);
		boolean section_italic = store.getBoolean(P21EditorPreferences.idISection); 
		boolean section_strike = store.getBoolean(P21EditorPreferences.idSSection);
		boolean section_underline = store.getBoolean(P21EditorPreferences.idUSection);
		int section_flags = 0; 
		if (section_bold) section_flags |= SWT.BOLD;
		if (section_italic) section_flags |= SWT.ITALIC;
		if (section_strike) section_flags |= TextAttribute.STRIKETHROUGH;
		if (section_underline) section_flags |= TextAttribute.UNDERLINE;

		boolean head_bold = store.getBoolean(P21EditorPreferences.idBHeaderKeyword);
		boolean head_italic = store.getBoolean(P21EditorPreferences.idIHeaderKeyword); 
		boolean head_strike = store.getBoolean(P21EditorPreferences.idSHeaderKeyword);
		boolean head_underline = store.getBoolean(P21EditorPreferences.idUHeaderKeyword);
		int head_flags = 0; 
		if (head_bold) head_flags |= SWT.BOLD;
		if (head_italic) head_flags |= SWT.ITALIC;
		if (head_strike) head_flags |= TextAttribute.STRIKETHROUGH;
		if (head_underline) head_flags |= TextAttribute.UNDERLINE;

*/
		boolean instance_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesInstance);
		boolean instance_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesInstance); 
		boolean instance_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesInstance);
		boolean instance_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesInstance);
		int instance_flags = 0; 
		if (instance_bold) instance_flags |= SWT.BOLD;
		if (instance_italic) instance_flags |= SWT.ITALIC;
		if (instance_strike) instance_flags |= TextAttribute.STRIKETHROUGH;
		if (instance_underline) instance_flags |= TextAttribute.UNDERLINE;

		boolean entity_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesEntity);
		boolean entity_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesEntity); 
		boolean entity_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesEntity);
		boolean entity_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesEntity);
		int entity_flags = 0; 
		if (entity_bold) entity_flags |= SWT.BOLD;
		if (entity_italic) entity_flags |= SWT.ITALIC;
		if (entity_strike) entity_flags |= TextAttribute.STRIKETHROUGH;
		if (entity_underline) entity_flags |= TextAttribute.UNDERLINE;

		boolean type_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesType);
		boolean type_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesType); 
		boolean type_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesType);
		boolean type_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesType);
		int type_flags = 0; 
		if (type_bold) type_flags |= SWT.BOLD;
		if (type_italic) type_flags |= SWT.ITALIC;
		if (type_strike) type_flags |= TextAttribute.STRIKETHROUGH;
		if (type_underline) type_flags |= TextAttribute.UNDERLINE;
		
		boolean int_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesInteger);
		boolean int_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesInteger); 
		boolean int_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesInteger);
		boolean int_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesInteger);
		int int_value_flags = 0; 
		if (int_value_bold) int_value_flags |= SWT.BOLD;
		if (int_value_italic) int_value_flags |= SWT.ITALIC;
		if (int_value_strike) int_value_flags |= TextAttribute.STRIKETHROUGH;
		if (int_value_underline) int_value_flags |= TextAttribute.UNDERLINE;

		boolean real_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesReal);
		boolean real_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesReal); 
		boolean real_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesReal);
		boolean real_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesReal);
		int real_value_flags = 0; 
		if (real_value_bold) real_value_flags |= SWT.BOLD;
		if (real_value_italic) real_value_flags |= SWT.ITALIC;
		if (real_value_strike) real_value_flags |= TextAttribute.STRIKETHROUGH;
		if (real_value_underline) real_value_flags |= TextAttribute.UNDERLINE;
		
		boolean log_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesLogical);
		boolean log_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesLogical); 
		boolean log_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesLogical);
		boolean log_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesLogical);
		int log_value_flags = 0; 
		if (log_value_bold) log_value_flags |= SWT.BOLD;
		if (log_value_italic) log_value_flags |= SWT.ITALIC;
		if (log_value_strike) log_value_flags |= TextAttribute.STRIKETHROUGH;
		if (log_value_underline) log_value_flags |= TextAttribute.UNDERLINE;
		
		boolean enum_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesEnumeration);
		boolean enum_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesEnumeration); 
		boolean enum_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesEnumeration);
		boolean enum_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesEnumeration);
		int enum_value_flags = 0; 
		if (enum_value_bold) enum_value_flags |= SWT.BOLD;
		if (enum_value_italic) enum_value_flags |= SWT.ITALIC;
		if (enum_value_strike) enum_value_flags |= TextAttribute.STRIKETHROUGH;
		if (enum_value_underline) enum_value_flags |= TextAttribute.UNDERLINE;
		
		boolean str_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesString);
		boolean str_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesString); 
		boolean str_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesString);
		boolean str_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesString);
		int str_value_flags = 0; 
		if (str_value_bold) str_value_flags |= SWT.BOLD;
		if (str_value_italic) str_value_flags |= SWT.ITALIC;
		if (str_value_strike) str_value_flags |= TextAttribute.STRIKETHROUGH;
		if (str_value_underline) str_value_flags |= TextAttribute.UNDERLINE;
		
		boolean bin_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesBinary);
		boolean bin_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesBinary); 
		boolean bin_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesBinary);
		boolean bin_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesBinary);
		int bin_value_flags = 0; 
		if (bin_value_bold) bin_value_flags |= SWT.BOLD;
		if (bin_value_italic) bin_value_flags |= SWT.ITALIC;
		if (bin_value_strike) bin_value_flags |= TextAttribute.STRIKETHROUGH;
		if (bin_value_underline) bin_value_flags |= TextAttribute.UNDERLINE;

		boolean undef_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesUndefined);
		boolean undef_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesUndefined); 
		boolean undef_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesUndefined);
		boolean undef_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesUndefined);
		int undef_value_flags = 0; 
		if (undef_value_bold) undef_value_flags |= SWT.BOLD;
		if (undef_value_italic) undef_value_flags |= SWT.ITALIC;
		if (undef_value_strike) undef_value_flags |= TextAttribute.STRIKETHROUGH;
		if (undef_value_underline) undef_value_flags |= TextAttribute.UNDERLINE;

		boolean redef_value_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesValuesRedefined);
		boolean redef_value_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesValuesRedefined); 
		boolean redef_value_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesValuesRedefined);
		boolean redef_value_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesValuesRedefined);
		int redef_value_flags = 0; 
		if (redef_value_bold) redef_value_flags |= SWT.BOLD;
		if (redef_value_italic) redef_value_flags |= SWT.ITALIC;
		if (redef_value_strike) redef_value_flags |= TextAttribute.STRIKETHROUGH;
		if (redef_value_underline) redef_value_flags |= TextAttribute.UNDERLINE;

		
		boolean del_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesDelimeter);
		boolean del_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesDelimeter); 
		boolean del_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesDelimeter);
		boolean del_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesDelimeter);
		int del_flags = 0; 
		if (del_bold) del_flags |= SWT.BOLD;
		if (del_italic) del_flags |= SWT.ITALIC;
		if (del_strike) del_flags |= TextAttribute.STRIKETHROUGH;
		if (del_underline) del_flags |= TextAttribute.UNDERLINE;
		


		boolean error_bold = store.getBoolean(P21EditorPreferences.idBError);
		boolean error_italic = store.getBoolean(P21EditorPreferences.idIError); 
		boolean error_strike = store.getBoolean(P21EditorPreferences.idSError);
		boolean error_underline = store.getBoolean(P21EditorPreferences.idUError);
		int error_flags = 0; 
		if (error_bold) error_flags |= SWT.BOLD;
		if (error_italic) error_flags |= SWT.ITALIC;
		if (error_strike) error_flags |= TextAttribute.STRIKETHROUGH;
		if (error_underline) error_flags |= TextAttribute.UNDERLINE;

		boolean comment_bold = store.getBoolean(P21EditorPreferences.idBComment);
		boolean comment_italic = store.getBoolean(P21EditorPreferences.idIComment); 
		boolean comment_strike = store.getBoolean(P21EditorPreferences.idSComment);
		boolean comment_underline = store.getBoolean(P21EditorPreferences.idUComment);
		int comment_flags = 0; 
		if (comment_bold) comment_flags |= SWT.BOLD;
		if (comment_italic) comment_flags |= SWT.ITALIC;
		if (comment_strike) comment_flags |= TextAttribute.STRIKETHROUGH;
		if (comment_underline) comment_flags |= TextAttribute.UNDERLINE;




		
//		IToken k_iso = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgIso)), provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgIso)), iso_flags));
//		IToken k_section = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSection)), provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSection)), section_flags));
//		IToken k_header = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgHeaderKeyword)), provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgHeaderKeyword)), head_flags));

		Color bgColor = !store.getBoolean(P21EditorPreferences.idTrComment) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesInstance)) : null;
		IToken k_instance = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesInstance)), bgColor, instance_flags));
//		IToken k_complex = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesInstance)), provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesInstance)), complex_instance_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesEntity) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesEntity)) : null;
		IToken k_entity = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesEntity)), bgColor, entity_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesType) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesType)) : null;
		IToken k_type = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesType)), bgColor, type_flags));
		
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesInteger) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesInteger)) : null;
		IToken k_integer_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesInteger)), bgColor, int_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesReal) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesReal)) : null;
		IToken k_real_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesReal)), bgColor, real_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesLogical) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesLogical)) : null;
		IToken k_logical_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesLogical)), bgColor, log_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesEnumeration) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesEnumeration)) : null;
		IToken k_enumeration_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesEnumeration)), bgColor, enum_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesString) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesString)) : null;
		IToken k_string_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesString)), bgColor, str_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesBinary) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesBinary)) : null;
		IToken k_binary_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesBinary)), bgColor, bin_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesUndefined) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesUndefined)) : null;
		IToken k_undefined_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesUndefined)), bgColor, undef_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesValuesRedefined) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesValuesRedefined)) : null;
		IToken k_redefined_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesValuesRedefined)), bgColor, redef_value_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesDelimeter) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesDelimeter)) : null;
		IToken k_delimeter = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesDelimeter)), bgColor, del_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrComment) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComment)) : null;
		IToken k_comment = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComment)), bgColor, comment_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrError) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgError)) : null;
		IToken k_error = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgError)), bgColor, error_flags));
		
		
		

// old ---------------------------------------


		
		
		
/*
		IToken k_iso = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_ISO_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_ISO_BG_COLOR)), iso_flags));
		IToken k_section = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_SECTION_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_SECTION_BG_COLOR)), section_flags));
		IToken k_file = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_FILE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_FILE_BG_COLOR)), file_flags));
		IToken k_instance = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_INSTANCE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_INSTANCE_BG_COLOR)), instance_flags));
		IToken k_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_VALUE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_VALUE_BG_COLOR)), value_flags));
		IToken k_delimeter = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DELIMETER_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DELIMETER_BG_COLOR)), delimeter_flags));
		IToken k_string = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_STRING_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_STRING_BG_COLOR)), string_flags));
		IToken k_other = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DEFAULT_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DEFAULT_BG_COLOR)), default_flags));
*/

/*		
		
		
		IToken k_iso = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_ISO_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_ISO_BG_COLOR)), iso_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_section = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_SECTION_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_SECTION_BG_COLOR)), section_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_file = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_FILE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_FILE_BG_COLOR)), file_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_instance = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_INSTANCE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_INSTANCE_BG_COLOR)), instance_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_VALUE_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_VALUE_BG_COLOR)), value_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_delimeter = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DELIMETER_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DELIMETER_BG_COLOR)), delimeter_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_string = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_STRING_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_STRING_BG_COLOR)), string_flags | SWT.ITALIC | SWT.BOLD));
		IToken k_other = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DEFAULT_COLOR)), provider.getColor(provider.getColorPreference(P21EditorPreferences.P21_DEFAULT_BG_COLOR)), default_flags | SWT.ITALIC | SWT.BOLD));
*/

/*

		IToken iso  = new Token(new TextAttribute(provider.getColor(P21ColorProvider.KEYWORD1)));
		IToken keyword2  = new Token(new TextAttribute(provider.getColor(P21ColorProvider.KEYWORD2)));
		IToken keyword3  = new Token(new TextAttribute(provider.getColor(P21ColorProvider.KEYWORD3)));
		IToken string    = new Token(new TextAttribute(provider.getColor(P21ColorProvider.STRING)));
		IToken constant  = new Token(new TextAttribute(provider.getColor(P21ColorProvider.CONSTANT)));
		IToken instance  = new Token(new TextAttribute(provider.getColor(P21ColorProvider.INSTANCE)));
		IToken delimeter = new Token(new TextAttribute(provider.getColor(P21ColorProvider.DELIMETER)));
		
		IToken other= new Token(new TextAttribute(provider.getColor(P21ColorProvider.DEFAULT)));
*/

		setDefaultReturnToken(k_error);
		
		List rules= new ArrayList();

		// Add rule for single line comments.
//		rules.add(new EndOfLineRule("//", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
//		rules.add(new SingleLineRule("\"", "\"", string, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
		rules.add(new SingleLineRule("'", "'", k_string_value, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new P21WhitespaceDetector()));


		rules.add(new MultiLineRule("/*", "*/", k_comment)); 


		rules.add(new BinaryValueRule(k_binary_value));


		rules.add(new InstanceRule(k_instance));

		rules.add(new EntityRule(k_entity));
		rules.add(new TypeRule(k_type));

		rules.add(new DelimeterRule(k_delimeter));
		rules.add(new UndefinedValueRule(k_undefined_value));
		rules.add(new RedefinedValueRule(k_redefined_value));
		rules.add(new LogicalValueRule(k_logical_value));
		rules.add(new EnumerationValueRule(k_enumeration_value));
		rules.add(new RealValueRule(k_real_value));
		rules.add(new IntegerValueRule(k_integer_value));


//		rules.add(new ConstantRule(k_value));
		
		// Add word rule for keywords, types, and constants.
		WordRule wordRule= new WordRule(new P21WordDetector(), k_error);

/*		
		for (int i= 0; i < P21Constants.HEADER_KEYWORDS.length; i++)
			wordRule.addWord(P21Constants.HEADER_KEYWORDS[i], k_header);

*/
/*		
		for (int i= 0; i < P21Constants.KEYWORDS2.length; i++)
			wordRule.addWord(P21Constants.KEYWORDS2[i], k_section);
*/
/*		
		for (int i= 0; i < P21Constants.KEYWORDS3.length; i++)
			wordRule.addWord(P21Constants.KEYWORDS3[i], k_iso);
*/
//		for (int i= 0; i < P21Constants.CONSTANTS.length; i++)
//			wordRule.addWord(P21Constants.CONSTANTS[i], constant);

		rules.add(wordRule);
		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}
