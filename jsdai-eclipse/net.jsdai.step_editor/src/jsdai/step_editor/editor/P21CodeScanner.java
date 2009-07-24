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
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

public class P21CodeScanner extends RuleBasedScanner {

		static int print_count = 0;

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


	protected class IsoRule implements IRule {

		/*
		
			ISO-10303-21
			END-ISO-10303-21;
		
		
		*/

		private final char[] iso_start = { 'I', 'S', 'O', '-', '1', '0', '3', '0', '3', '-', '2', '1' };
		private final char[] iso_end = { 'E', 'N', 'D', '-' };



		private final IToken fToken;

		public IsoRule(IToken token) {
			fToken= token;
		}

		public boolean isIso(char character) {
			return true;
			/*
			for (int index= 0; index < P21_DELIMETERS.length; index++) {
				if (P21_DELIMETERS[index] == character)
					return true;
			}
			*/
			//return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		public IToken evaluate(ICharacterScanner scanner) {


			int character = scanner.read();
			int unread_count = 1;
			if (character == 'E') {
				for (int i = 1; i < iso_end.length; i++) {
					character = scanner.read();
					unread_count++;
					if (!(character == iso_end[i])) {
						// no, not our token
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					}
				} // for
				for (int j = 0; j < iso_start.length; j++) {
					character = scanner.read();
					unread_count++;
					if (!(character == iso_start[j])) {
						// no, not our token
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					}
				}
//				scanner.unread();
				return fToken;
			

			} else
			if (character == 'I') {
				for (int i = 1; i < iso_start.length; i++) {
					character = scanner.read();
					unread_count++;
					if (!(character == iso_start[i])) {
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					}
				}	
//				scanner.unread();
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

		public boolean isEntity(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				// may be entity, but need to check also the previous character
				scanner.unread(); // ready to read the same character again
				scanner.unread(); // ready to read the previous character
				int character2 = scanner.read(); // read the previous character
				scanner.read(); // returned back to where we were, after reading the first character, but now we also have the previous one

				if (character2 == '=') {
					// can be only entity here
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
				// type may start like this:   (KUKU     ,KUKU  - NOTICE that in this partition there are no complex entities starting with (KUKU
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


		int getPreviousCharacterEnhanced(ICharacterScanner scanner, int character) {

			
//					need to handle white space intermixed with multiple comments, such as:
//					(\t/* comment 1 */ \n\t /* comment 2 */ \n\tSOME_TYPE...  - and similar

//			scanner.unread(); // initial
			boolean in_comment = false;
			int count = 0;			
			int previous;
			
			for (;;) {
				scanner.unread();
				scanner.unread();
				previous = character;
				character = scanner.read();
				count++;
				if (character == '/') {
					if (in_comment) {
						if (previous == '*') {
							// end of comment
							in_comment = false;
						} else {
							//  just / in the middle of a comment, so what
						}
					} else {
						// possible comment, but probably nothing to do, unless, if not comment, then  error
					}
				} else
				if (character == '*') {
					if (in_comment) {
						 // possible start of comment, but probably do nothing at this point
						 // also possible * in the middle of the comment, so what
					} else {
						if (previous == '/') {
							in_comment = true;
						} else {
							// encountered * not in comment - return as result
							scanner.unread();
							return count;
						}
					}
				} else
				if (previous == '/') {
					// encountered '/' not in comment - end here
					// don't care what character is as long as it is not *
						if (!in_comment) {
							scanner.unread();
							return count;
						}
				} else
				if (in_comment) {
					// don't care what is in it as long as character is not * or '/'
				} else 
				if ((character == '\n') || (character == '\r') || (character == '\t') || (character == ' ')) {
					// white space here
				} else {
					// probably something not in comment and significant, end here
						scanner.unread();
						return count;
				}
			} // for
			
		}



		public boolean isType(char character, ICharacterScanner scanner) {
			if ((Character.isLetter((char)character)) || (character == '_')) {
				// may be type, but need to check also the previous character
				// type may start like this:   (KUKU     ,KUKU  - NOTICE that in this partition there are no complex entities starting with (KUKU
				// however, (KUKU may in fact be on two different lines, or some white space in between, or even comment in between - different partition
				// so, assume, if the previous character is comment (-1) then also ok
				// but the privous character means all the white space skipped: \n \r \t  space
				// if not success, then we need to know exactly how many tokens to read forward
				
	//			int count = getPreviousCharacter(scanner); // ready to read the previous character
				int count = getPreviousCharacterEnhanced(scanner, character); // ready to read the previous character
				
//				scanner.unread(); // ready to read the same character again
//				scanner.unread(); // ready to read the previous character
				int character2 = scanner.read(); // read the previous character
// System.out.println("after getPreviousCharacter - character2: " + character2 + ", count: " + count);

				// now return back to where we are supposed to be after reading the 1st character
				for (int i = 0; i < count; i++) {
					scanner.read(); 
				}
//				if ((character2 == '(') || (character2 == ',') || (character2 == '/')) {
				if ((character2 == '(') || (character2 == ',')) {
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
						// why this check is here at all? RealValueRule is run before this one, so this cannot occur
						// try if it is real value instead
						unread(scanner, unread_count);		
						return Token.UNDEFINED;
					} else
//					if ((character == ',') || (character == ')')) {
					if ((character == ',') || (character == ')') || 
							(character == ' ') || (character == '\n') || (character == '\t') || (character == '\r')) {
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
				
				boolean has_to_end = false;
				for (int unread_count = 2, dot_count = 0, e_count = 0, dash_count = 0;; unread_count++) {
					previous = character;
					character = scanner.read();
					if ((Character.isDigit((char)character)) && !has_to_end) {
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
//					} else
//					if ((character == ' ') || (character == '\n') || (character == '\t') || (character == '\r')) {
//						// ok only if after one or more of these there is the end (see next)
//						// not ok, if digits continue
//						has_to_end = true;
					} else 
					if ((character == ',') || (character == ')')) {
//					if ((character == ',') || (character == ')') ||
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




	

	protected class EntityxxxRule implements IRule {

	/*
		we have entities and types
		types may be :
		(SOME_TYPE(
		,SOME_TYPE(
		entities may be:
		#123=SOME_ENTITY(
		#123=(SOME_ENTITY( - if complex - notice that in this case partition starts with =(
		                     also notice that this case is encountered only by complex entity partition code scanner, not by this one.
		                     
   So here: 
   =SOME( - entity
   ,some( - type
   (some( - type
   .some. - enumeration
   
   		                     
		
		if we can unread character that we did not even read  - then we can know if it is entity or type
		
		
		Other things to consider:
		
		enumeration:   .SOME_ENUMERATION.
		logical:
		.T.
		.F.
		.U.
		numeric:
		123.45
		-123
		( and , separators.
		also $ * have special meaning
		 
	
	*		
		
		
		/** Token to return for this rule */
		private final IToken fToken;

		/**
		 * Creates a new operator rule.
		 *
		 * @param token Token to use for this rule
		 */
		public EntityxxxRule(IToken token) {
			fToken= token;
		}

		/**
		 * Is this the 1st character of an instance character? 
		 *
		 * @param character Character to determine whether it is an operator character
		 * @return <code>true</code> iff the character is an operator, <code>false</code> otherwise.
		 */
		public boolean isEntityStart(char character) {
			if ('#' == character) return true;
			else return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

//		void printDebug(String str) {
//			if ((print_count > 50) && (print_count < 200)) {
//				System.out.println(str);
//			}
//			print_count++;
//		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			if (isEntityStart((char) character)) {
				// ok, so we have the first character of an instance here, may be our complex or not	
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if (!Character.isDigit((char)character)) {
						if (character == -1) {
							// it is a good indication, that we hit a different partition, 
							// such as the complex instance partition, unless somebody cared to write this:
							// #123/*comment*/=CARTESIAN_POINT(....
							// in that case it may or may not be a complex instance, depends what follows that embedded comment:
							// #123/*comment*/=(...
							// or even
							//  #123/* comment */=/* more comments */(...
							// so we just consider that we found a complex instance, we don't care about the above exceptions
							scanner.unread();
							return fToken;
						} else {
							// no, this is just another instance, we don't even care if it is the first one on the line or not
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					} // not a digit
				} // for
			} else {
				scanner.unread();
				return Token.UNDEFINED;
			}
		}

	}




	

	protected class ComplexInstanceRule implements IRule {


		/** 
		 * 
		 * 
		 * 
		 * 
		 * P21 instances in the form #nnnn|complex partition 
		 * 
		 * this rule has to be added BEFORE InstanceRule 
		 * 
		 * This instance should have been in the complex partition in the first place,
		 * but it was so easy to define the complex partition when leaving this instance behind :)
		 * 
		 * 
		 * 
		 * /

		
		
		
		/** Token to return for this rule */
		private final IToken fToken;

		/**
		 * Creates a new operator rule.
		 *
		 * @param token Token to use for this rule
		 */
		public ComplexInstanceRule(IToken token) {
			fToken= token;
		}

		/**
		 * Is this the 1st character of an instance character? 
		 *
		 * @param character Character to determine whether it is an operator character
		 * @return <code>true</code> iff the character is an operator, <code>false</code> otherwise.
		 */
		public boolean isComplexInstanceStart(char character) {
			if ('#' == character) return true;
			else return false;
		}

		void unread(ICharacterScanner scanner, int count) {
			for (int i = 0; i < count; i++) {
				scanner.unread();
			}
		}

		void printDebug(String str) {
			if ((print_count > 50) && (print_count < 200)) {
				System.out.println(str);
			}
			print_count++;
		}

		public IToken evaluate(ICharacterScanner scanner) {

			int character = scanner.read();
			if (isComplexInstanceStart((char) character)) {
				// ok, so we have the first character of an instance here, may be our complex or not	
				for (int unread_count = 2;; unread_count++) {
					character = scanner.read();
					if (!Character.isDigit((char)character)) {
						if (character == -1) {
							// it is a good indication, that we hit a different partition, 
							// such as the complex instance partition, unless somebody cared to write this:
							// #123/*comment*/=CARTESIAN_POINT(....
							// in that case it may or may not be a complex instance, depends what follows that embedded comment:
							// #123/*comment*/=(...
							// or even
							//  #123/* comment */=/* more comments */(...
							// so we just consider that we found a complex instance, we don't care about the above exceptions
							scanner.unread();
							return fToken;
						} else {
							// no, this is just another instance, we don't even care if it is the first one on the line or not
							unread(scanner, unread_count);		
							return Token.UNDEFINED;
						}
					} // not a digit
				} // for
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
			int digit_count = 0;
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
					} else {
						digit_count++;
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




	public P21CodeScanner(P21ColorProvider provider) {


			initRules(provider);

	}

	public void initRules(P21ColorProvider provider) {
		
		IPreferenceStore store = EditorsPlugin.getDefault().getPreferenceStore();

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


		boolean instance_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesInstance);
		boolean instance_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesInstance); 
		boolean instance_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesInstance);
		boolean instance_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesInstance);
		int instance_flags = 0; 
		if (instance_bold) instance_flags |= SWT.BOLD;
		if (instance_italic) instance_flags |= SWT.ITALIC;
		if (instance_strike) instance_flags |= TextAttribute.STRIKETHROUGH;
		if (instance_underline) instance_flags |= TextAttribute.UNDERLINE;

		boolean entity_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesEntity);
		boolean entity_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesEntity); 
		boolean entity_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesEntity);
		boolean entity_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesEntity);
		int entity_flags = 0; 
		if (entity_bold) entity_flags |= SWT.BOLD;
		if (entity_italic) entity_flags |= SWT.ITALIC;
		if (entity_strike) entity_flags |= TextAttribute.STRIKETHROUGH;
		if (entity_underline) entity_flags |= TextAttribute.UNDERLINE;

		boolean type_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesType);
		boolean type_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesType); 
		boolean type_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesType);
		boolean type_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesType);
		int type_flags = 0; 
		if (type_bold) type_flags |= SWT.BOLD;
		if (type_italic) type_flags |= SWT.ITALIC;
		if (type_strike) type_flags |= TextAttribute.STRIKETHROUGH;
		if (type_underline) type_flags |= TextAttribute.UNDERLINE;
		
		boolean int_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesInteger);
		boolean int_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesInteger); 
		boolean int_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesInteger);
		boolean int_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesInteger);
		int int_value_flags = 0; 
		if (int_value_bold) int_value_flags |= SWT.BOLD;
		if (int_value_italic) int_value_flags |= SWT.ITALIC;
		if (int_value_strike) int_value_flags |= TextAttribute.STRIKETHROUGH;
		if (int_value_underline) int_value_flags |= TextAttribute.UNDERLINE;

		boolean real_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesReal);
		boolean real_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesReal); 
		boolean real_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesReal);
		boolean real_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesReal);
		int real_value_flags = 0; 
		if (real_value_bold) real_value_flags |= SWT.BOLD;
		if (real_value_italic) real_value_flags |= SWT.ITALIC;
		if (real_value_strike) real_value_flags |= TextAttribute.STRIKETHROUGH;
		if (real_value_underline) real_value_flags |= TextAttribute.UNDERLINE;
		
		boolean log_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesLogical);
		boolean log_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesLogical); 
		boolean log_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesLogical);
		boolean log_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesLogical);
		int log_value_flags = 0; 
		if (log_value_bold) log_value_flags |= SWT.BOLD;
		if (log_value_italic) log_value_flags |= SWT.ITALIC;
		if (log_value_strike) log_value_flags |= TextAttribute.STRIKETHROUGH;
		if (log_value_underline) log_value_flags |= TextAttribute.UNDERLINE;
		
		boolean enum_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesEnumeration);
		boolean enum_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesEnumeration); 
		boolean enum_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesEnumeration);
		boolean enum_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesEnumeration);
		int enum_value_flags = 0; 
		if (enum_value_bold) enum_value_flags |= SWT.BOLD;
		if (enum_value_italic) enum_value_flags |= SWT.ITALIC;
		if (enum_value_strike) enum_value_flags |= TextAttribute.STRIKETHROUGH;
		if (enum_value_underline) enum_value_flags |= TextAttribute.UNDERLINE;
		
		boolean str_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesString);
		boolean str_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesString); 
		boolean str_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesString);
		boolean str_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesString);
		int str_value_flags = 0; 
		if (str_value_bold) str_value_flags |= SWT.BOLD;
		if (str_value_italic) str_value_flags |= SWT.ITALIC;
		if (str_value_strike) str_value_flags |= TextAttribute.STRIKETHROUGH;
		if (str_value_underline) str_value_flags |= TextAttribute.UNDERLINE;
		
		boolean bin_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesBinary);
		boolean bin_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesBinary); 
		boolean bin_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesBinary);
		boolean bin_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesBinary);
		int bin_value_flags = 0; 
		if (bin_value_bold) bin_value_flags |= SWT.BOLD;
		if (bin_value_italic) bin_value_flags |= SWT.ITALIC;
		if (bin_value_strike) bin_value_flags |= TextAttribute.STRIKETHROUGH;
		if (bin_value_underline) bin_value_flags |= TextAttribute.UNDERLINE;

		boolean undef_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesUndefined);
		boolean undef_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesUndefined); 
		boolean undef_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesUndefined);
		boolean undef_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesUndefined);
		int undef_value_flags = 0; 
		if (undef_value_bold) undef_value_flags |= SWT.BOLD;
		if (undef_value_italic) undef_value_flags |= SWT.ITALIC;
		if (undef_value_strike) undef_value_flags |= TextAttribute.STRIKETHROUGH;
		if (undef_value_underline) undef_value_flags |= TextAttribute.UNDERLINE;

		boolean redef_value_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesValuesRedefined);
		boolean redef_value_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesValuesRedefined); 
		boolean redef_value_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesValuesRedefined);
		boolean redef_value_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesValuesRedefined);
		int redef_value_flags = 0; 
		if (redef_value_bold) redef_value_flags |= SWT.BOLD;
		if (redef_value_italic) redef_value_flags |= SWT.ITALIC;
		if (redef_value_strike) redef_value_flags |= TextAttribute.STRIKETHROUGH;
		if (redef_value_underline) redef_value_flags |= TextAttribute.UNDERLINE;

		
		boolean del_bold = store.getBoolean(P21EditorPreferences.idBSimpleEntitiesDelimeter);
		boolean del_italic = store.getBoolean(P21EditorPreferences.idISimpleEntitiesDelimeter); 
		boolean del_strike = store.getBoolean(P21EditorPreferences.idSSimpleEntitiesDelimeter);
		boolean del_underline = store.getBoolean(P21EditorPreferences.idUSimpleEntitiesDelimeter);
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

		boolean complex_instance_bold = store.getBoolean(P21EditorPreferences.idBComplexEntitiesInstance);
		boolean complex_instance_italic = store.getBoolean(P21EditorPreferences.idIComplexEntitiesInstance); 
		boolean complex_instance_strike = store.getBoolean(P21EditorPreferences.idSComplexEntitiesInstance);
		boolean complex_instance_underline = store.getBoolean(P21EditorPreferences.idUComplexEntitiesInstance);
		int complex_instance_flags = 0; 
		if (complex_instance_bold) complex_instance_flags |= SWT.BOLD;
		if (complex_instance_italic) complex_instance_flags |= SWT.ITALIC;
		if (complex_instance_strike) complex_instance_flags |= TextAttribute.STRIKETHROUGH;
		if (complex_instance_underline) complex_instance_flags |= TextAttribute.UNDERLINE;
		
		
		
/*

		make these different token types

		iso - better make a spec rule rather than allowing - in words
		section
		file
		
		and then, two sets for single and complex instances:
		
		instance #123
		entity CARTESIAN_POINT
		type   SOME_TYPE
		undefined value $
		redefined value *
		logical value  .T. .F. .U.
		enumeration value
		integer value  123 -123
		real value     123.5  123.0 123.  -123.0 0.123 -0.123  123.45E5 123-E12  -0.123-E4
		binary value  "101110"
		string value  '....'
		delimeters  = ( ) , ;


*/


		Color bgColor = !store.getBoolean(P21EditorPreferences.idTrIso) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgIso)) : null;
		IToken k_iso = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgIso)), bgColor, iso_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSection) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSection)) : null;
		IToken k_section = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSection)), bgColor, section_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrHeaderKeyword) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgHeaderKeyword)) : null;
		IToken k_header = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgHeaderKeyword)), bgColor, head_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesInstance) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesInstance)) : null;
		IToken k_instance = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesInstance)), bgColor, instance_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrComplexEntitiesInstance) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgComplexEntitiesInstance)) : null;
		IToken k_complex = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgComplexEntitiesInstance)), bgColor, complex_instance_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesEntity) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesEntity)) : null;
		IToken k_entity = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesEntity)), bgColor, entity_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesType) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesType)) : null;
		IToken k_type = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesType)), bgColor, type_flags));
		
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesInteger) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesInteger)) : null;
		IToken k_integer_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesInteger)), bgColor, int_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesReal) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesReal)) : null;
		IToken k_real_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesReal)), bgColor, real_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesLogical) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesLogical)) : null;
		IToken k_logical_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesLogical)), bgColor, log_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesEnumeration) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesEnumeration)) : null;
		IToken k_enumeration_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesEnumeration)), bgColor, enum_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesString) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesString)) : null;
		IToken k_string_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesString)), bgColor, str_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesBinary) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesBinary)) : null;
		IToken k_binary_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesBinary)), bgColor, bin_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesUndefined) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesUndefined)) : null;
		IToken k_undefined_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesUndefined)), bgColor, undef_value_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesValuesRedefined) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesValuesRedefined)) : null;
		IToken k_redefined_value = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesValuesRedefined)), bgColor, redef_value_flags));

		bgColor = !store.getBoolean(P21EditorPreferences.idTrSimpleEntitiesDelimeter) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgSimpleEntitiesDelimeter)) : null;
		IToken k_delimeter = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgSimpleEntitiesDelimeter)), bgColor, del_flags));
		bgColor = !store.getBoolean(P21EditorPreferences.idTrError) ? provider.getColor(provider.getColorPreference(P21EditorPreferences.idBgError)) : null;
		IToken k_error = new Token(new TextAttribute(provider.getColor(provider.getColorPreference(P21EditorPreferences.idFgError)), bgColor, error_flags));



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
		// will have to implement my own rule to catch illegal characters inside the binary

//		rules.add(new SingleLineRule("\"", "\"", k_binary_value, '\\')); //$NON-NLS-2$ //$NON-NLS-1$ 

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new P21WhitespaceDetector()));

		rules.add(new IsoRule(k_iso));


		rules.add(new BinaryValueRule(k_binary_value));
		rules.add(new ComplexInstanceRule(k_complex));
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
		

		
/*
		for (int i= 0; i < P21Constants.KEYWORDS3.length; i++)
			wordRule.addWord(P21Constants.KEYWORDS3[i], k_iso);
*/

//		for (int i= 0; i < P21Constants.CONSTANTS.length; i++)
//			wordRule.addWord(P21Constants.CONSTANTS[i], constant);


		// Add word rule for keywords, types, and constants.
		WordRule wordRule= new WordRule(new P21WordDetector(), k_error);

		for (int i= 0; i < P21Constants.HEADER_KEYWORDS.length; i++)
			wordRule.addWord(P21Constants.HEADER_KEYWORDS[i], k_header);

		for (int i= 0; i < P21Constants.SECTION_KEYWORDS.length; i++)
			wordRule.addWord(P21Constants.SECTION_KEYWORDS[i], k_section);

		rules.add(wordRule);


		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}

}
