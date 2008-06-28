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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ICharacterPairMatcher;

/*

	let's try to match the following things:
	
( - )
[ - ]
{ - } *** not needed but may have as well
BEGIN - END
ALIAS - END_ALIAS
CASE - END_CASE
CONSTANT - END_CONSTANT
CONTEXT - END_CONTEXT ****************** amendment keyword, ignore for now
	CREATE - END_CREATE *** (Express X)
	DEPENDENT_MAP - END_DEPENDENT_MAP *** (Express X)
ENTITY - END_ENTITY
FUNCTION - END_FUNCTION
IF - END_IF, ignore ELSIF and ELSE keywords altogether (perhaps the 1st implementation for now?)
	or:
	IF - END_IF, if ELSIF or ELSE not encountered
	IF - ELSIF, if ELSIF encountered
	IF - ELSE, if ELSIF not encountered and ELSE encountered
	ELSIF - END_IF, if ELSIF or ELSE not encountered
	ELSIF - ELSIF, if ELSIF encountered
	ELSIF - ELSE - if ELSIF not encountered and ELSE encountered
	ELSE - END_IF
	also deal with nested inner IF statements.
	also define the corresponding search backwards strategy

LOCAL - END_LOCAL
	MAP - END_MAP *** (Express X)
MODEL - END_MODEL ************** amendment keyword, ignore for now
PROCEDURE - END_PROCEDURE
REPEAT - END_REPEAT
RULE - END_RULE
SCHEMA - END_SCHEMA
	SCHEMA_MAP - END_SCHEMA_MAP *** (Express X)
	SCHEMA_VIEW - END_SCHEMA_VIEW *** (Express X)
SUBTYPE_CONSTRAINT - END_SUBTYPE_CONSTRAINT
TYPE - END_TYPE
	VIEW - END_VIEW *** (Express X)
(* - *)  ** may also have for comments	- however, perhaps enough ( - ), it may be used for (* - *) as well
	
	

*/

// do we need to implement this interface?
public class ExpressPairMatcher implements IExpressTokens, ICharacterPairMatcher {
//public class ExpressPairMatcher implements IExpressTokens {

	protected char[] fPairs;
	protected String[] fStringPairs;
	ExpressSpecialScanner fScanner;
	
	protected static final int[] fTokenPairs = { 
		TokenLPAREN, TokenRPAREN,
		TokenLBRACKET, TokenRBRACKET,
		TokenLBRACE, TokenRBRACE,
		TokenLESS, TokenGREATER,
		TokenBEGIN, TokenEND,
		TokenALIAS, TokenEND_ALIAS,
		TokenCASE, TokenEND_CASE,
		TokenCONSTANT, TokenEND_CONSTANT,
		TokenENTITY, TokenEND_ENTITY,
		TokenFUNCTION, TokenEND_FUNCTION,
		TokenIF, TokenEND_IF,
		//TokenELSIF, TokenELSE,
		TokenLOCAL, TokenEND_LOCAL,
		TokenPROCEDURE, TokenEND_PROCEDURE,
		TokenREPEAT, TokenEND_REPEAT,
		TokenRULE, TokenEND_RULE,
		TokenSCHEMA, TokenEND_SCHEMA,
		TokenSUBTYPE_CONSTRAINT, TokenEND_SUBTYPE_CONSTRAINT,
		TokenTYPE, TokenEND_TYPE
	};	
		

	protected IDocument fDocument;
	protected int fOffset;

	protected int fStartPos;
	protected int fEndPos;
	protected int fAnchor;

  protected int fFirst;
	protected int fFirstStringLength;


	public ExpressPairMatcher(char[] pairs) {
		fPairs = pairs;
	}

	public ExpressPairMatcher(String[] pairs) {
		fStringPairs = pairs;
	}

	public ExpressPairMatcher(char[] pairs, String[] str_pairs) {
		fPairs = pairs;
		fStringPairs = str_pairs;
	}


	public IRegion match(IDocument document, int offset) {

		fOffset = offset;

		if (fOffset < 0)
			return null;

		fDocument= document;

//		if (fDocument != null && matchPairsAt() && fStartPos != fEndPos)
		if (fDocument != null && matchChars() && fStartPos != fEndPos)
			return new Region(fStartPos, fEndPos - fStartPos + 1);

		return null;
	}

	public int getAnchor() {
		return fAnchor;
	}


	public IRegion matchPair(IDocument document, int offset) {

		fOffset = offset;

		if (fOffset < 0)
			return null;

		fDocument= document;

//		if (fDocument != null && matchPairsAt() && fStartPos != fEndPos)
		if (fDocument != null && matchPairs() && fStartPos != fEndPos)
			return new Region(fStartPos, fEndPos - fStartPos + 1);

		return null;
	}


	public IRegion matchPairJump(IDocument document, int offset) {

		fOffset = offset;

		if (fOffset < 0)
			return null;

		fDocument= document;

//		if (fDocument != null && matchPairsAt() && fStartPos != fEndPos)
		if (fDocument != null && matchPairs() && fStartPos != fEndPos) {
		
		String last_token_string = "";
		int last_token = -10;
		if (fScanner != null) {
			last_token_string = fScanner.getTokenString();
//			last_token = fScanner.getCurrentToken();
			
		}
//System.out.println("last token: " + last_token + ", string: " + last_token_string);	
		
			return new Region(fStartPos-1, fEndPos - fStartPos + last_token_string.length() + 1);
		}
		return null;
	}


	public IRegion matchPairWhole(IDocument document, int offset) {

		fOffset = offset;

		if (fOffset < 0)
			return null;

		fDocument= document;

//		if (fDocument != null && matchPairsAt() && fStartPos != fEndPos)
		if (fDocument != null && matchPairs() && fStartPos != fEndPos) {
		
		String last_token_string = "";
		int last_token = -10;
		if (fScanner != null) {
			last_token_string = fScanner.getTokenString();
//			last_token = fScanner.getCurrentToken();
			
		}
//System.out.println("last token: " + last_token + ", string: " + last_token_string);	
		
		int x1 = 0;
		int x2 = 0;

		if (fAnchor == LEFT) { // find closing
			// first - left, last - right
			x1 = fStartPos - fFirstStringLength;
		} else
		if (fAnchor == RIGHT) { // find opening
			// first - right, last - left
			x1 = fStartPos - last_token_string.length();
		}

		
		x2 = fEndPos - fStartPos + + fFirstStringLength + last_token_string.length() + 1; 

		if (fFirst < 100) {
			x2 += 1;
			if (fFirst < 10) {
				x1 -= 1;
			}
		}
		
		
			return new Region(x1, x2);
		}
		return null;
	}







	/*
			better try to match single character stuff first, as characters, not as strings,
			and if no match, only then try matching strings, this way it will work faster for parentheses and brackets
	*/
	protected boolean matchPairsAt() {

		fStartPos= -1;
		fEndPos= -1;

		



		// the longest word seems to be 21 characters: END_SUBTYPE_CONSTRAINT, just add a couple more without need
		// or we could do even better - to get exact length needed
		// however, that would require to read to string multiple times as opposed to a single time.

		int word_length = 23;
		int preceding_offset = 0;
		int preceding_length = fOffset; // or fOffset - 1
		if (fOffset > word_length) {
			preceding_offset = fOffset - word_length;
			preceding_length = word_length;
		} else {
//System.out.println("fOffset: " + fOffset);	
		}

		try {
		
			// ok, so first try with single-character braces
			
			if (fOffset - 1 >= 0) {
			
				int depth = 0;
				char c_test = fDocument.getChar(fOffset-1);
				int current_pos;
				for (int i = 0; i < fPairs.length; i += 2) {
					if (c_test == fPairs[i]) {
						if (c_test == '(') {
							// if it is (* then ignore, because we want to trigger it on *, not on (
							if (fDocument.getChar(fOffset) == '*') {
								continue;
							}
						} else 
						if (c_test == '*') {
							// ok, need to check if it is (*
							if (fOffset - 2 >= 0) {
								if (fDocument.getChar(fOffset - 2) == '(') {
								} else {
									// no, not a comment
									continue;
								}
							} else {
								// no, cannot be a comment
								continue;
							}
						}
						// opening char found - search to the right for the closing one 
						current_pos = fOffset;
						for (;;) {
							char current = fDocument.getChar(current_pos++);
							if (current == fPairs[i]) {
								// another opening
								depth++;
							} else
							if (current == fPairs[i+1]) {
								// closing one
								if (depth == 0) {
									// found
									fStartPos = fOffset;
									fEndPos = current_pos - 1;
									break;
								} else {
									depth--;
								}
							} else {
								// not an interesting character
							}
							if (current_pos > fDocument.getLength()-1) {
								break;
							}
						}
						break;
					} else
					if (c_test == fPairs[i+1]) {
						// closing char found - search to the left for the opening one
						current_pos = fOffset-2;
						for (;;) {
							char current = fDocument.getChar(current_pos--);
							if (current == fPairs[i+1]) {
								// another closing
								depth++;
							} else
							if (current == fPairs[i]) {
								// opening one
								if (depth == 0) {
									// found
									fStartPos = current_pos + 2;
									fEndPos = fOffset - 1;
									break;
								} else {
									depth--;
								}
							} else {
								// not an interesting character
							}
							if (current_pos < 0) {
								break;
							}
						}
						break;
					}
					
				}
			}
		
		
			if ((fStartPos >= 0) && (fEndPos > 0) && (fEndPos > fStartPos)) {
				return true;
			}
			
	    //preceding_offset = fOffset - 30;
	    //preceding_length = 30;
			String str_preceding = fDocument.get(preceding_offset, preceding_length).toUpperCase();
			int str_length = str_preceding.length();
	
			// ok, go through all the word-type braces
			for (int i = 0; i < fStringPairs.length; i += 2) {
				String start_string = fStringPairs[i];
				String end_string = fStringPairs[i+1];

				int ind_start = str_preceding.lastIndexOf(start_string);
				if (ind_start - 1 >= 0) {
					if (Character.isJavaIdentifierPart(str_preceding.charAt(ind_start-1))) {
						ind_start = -1;
					}
				}
				int ind_end = str_preceding.lastIndexOf(end_string);
				if (ind_end - 1 >= 0) {
					if (Character.isJavaIdentifierPart(str_preceding.charAt(ind_end-1))) {
						ind_end = -1;
					}
				}
				if (Character.isJavaIdentifierPart(fDocument.getChar(fOffset))) {
					ind_start = -1;
					ind_end = -1;
				}


//				int ind_start = lastIndexOf(str_preceding, start_string, str_preceding.length());
//				int ind_end = lastIndexOf(str_preceding, end_string, str_preceding.length());

				if (ind_end >= 0) {
					int end_offset = ind_end + end_string.length();
				if (end_offset == str_length) {
					// found end word, need to search backwards now
//System.out.println("found: " + end_string + ", search backwards");				
					
//						fEndPos = fOffset - end_string.length() - 1;
						fEndPos = fOffset - end_string.length();
						if (fEndPos < 0) {
							fEndPos = 0;
						}
//System.out.println("before backwards ");				
						fStartPos = searchForOpeningWord(start_string, end_string, fEndPos, fDocument);
				} else {
					// still may be forward word
					if (ind_start >= 0) {
						int start_offset = ind_start + start_string.length(); // 6 - length of ENTITY
						if (start_offset == str_length) {
							// found start word need to search forward now
//System.out.println("found " + start_string + " , search forwards");				
							fStartPos = fOffset;
							fEndPos = searchForClosingWord(start_string, end_string, fStartPos, fDocument);
						}
					}
				}
			} else 
			if (ind_start >= 0) {
				int start_offset = ind_start + start_string.length(); // 6 - length of ENTITY
					if (start_offset == str_length) {
					// found start word need to search forward now
//System.out.println("2 found " + start_string + ", search forwards");				
						fStartPos = fOffset;
						fEndPos = searchForClosingWord(start_string, end_string, fStartPos, fDocument);

				}
			}



			} // for - through all the word pairs
					

		} catch (BadLocationException x) {
//System.out.println("bad location: " + x);				
	
			return false;
		}
			

		if ((fStartPos >= 0) && (fEndPos > 0) && (fEndPos > fStartPos)) {
			return true;
		}
		return false;
	}


//###################### 2nd version



	protected boolean matchChars() {

		fStartPos= -1;
		fEndPos= -1;

		if (fOffset <= 0) {
			return false;
		}

	try {

		ExpressSpecialScanner scanner = new ExpressSpecialScanner(fDocument);
		int previous = scanner.previousToken(fOffset - 1, 0);
// System.out.println("prev token: " + previous);				
		if (previous <= 0) {
			return false;
		}
		int position = scanner.getPosition();
		if (!scanner.isDefaultPartition(position)) {
			// this check may not needed, because scanner will not return position outside its partition
			// may be interested also in multi-line comment end, such as  *) if we want to select multi-line comment
			// for now, do nothing
			return false;
		}
		
		
		// if it is a multi-char token, then fOffset cannot be a part of a word
		if (previous > 100) {
			if (Character.isJavaIdentifierPart(fDocument.getChar(fOffset))) {
				return false;
			}
		}
		int index = getTokenIndex(previous);
//		String token_str = getTokenString(index);
		String token_str = scanner.getTokenString();

//System.out.println("fOffset: " + fOffset + ", position: " + position + ", sum: " + (position + token_str.length()) + ", str: " + token_str);
		if (position + token_str.length()  != fOffset -1) {
			return false;
		} 
		
//		System.out.println("something found: " + token_str);

		
		if (previous < 10) {
			
  		fStartPos = fOffset;
			fAnchor = LEFT;
			fEndPos = findMatchingClosing(scanner, fDocument, index, fOffset); 
//			System.out.println("found opening, search for closing to the right");
		} else
		if ((previous  > 10) && (previous  < 20)) {
			fEndPos = position + 1;
			fAnchor = RIGHT;
			fStartPos = findMatchingOpening(scanner, fDocument, index-1, position);			
//			System.out.println("found closing, search for opening to the left");
			fStartPos -= 1;
		}
		


		} catch (BadLocationException x) {
//System.out.println("bad location: " + x);				
	
			return false;
		}

		if ((fStartPos >= 0) && (fEndPos > 0) && (fEndPos > fStartPos)) {
			return true;
		}
		return false;

}



	protected boolean matchPairs() {

		fStartPos= -1;
		fEndPos= -1;

		if (fOffset <= 0) {
			return false;
		}

	try {

		ExpressSpecialScanner scanner = new ExpressSpecialScanner(fDocument);
		fScanner = scanner;
		int previous = scanner.previousToken(fOffset - 1, 0);
// System.out.println("prev token: " + previous);				
		if (previous <= 0) {
			return false;
		}
		int position = scanner.getPosition();
		if (!scanner.isDefaultPartition(position)) {
			// this check may not needed, because scanner will not return position outside its partition
			// may be interested also in multi-line comment end, such as  *) if we want to select multi-line comment
			// for now, do nothing
			return false;
		}
		
		
		// if it is a multi-char token, then fOffset cannot be a part of a word
		if (previous > 100) {
			if (Character.isJavaIdentifierPart(fDocument.getChar(fOffset))) {
				return false;
			}
		}
		int index = getTokenIndex(previous);
//		String token_str = getTokenString(index);
		String token_str = scanner.getTokenString();

//System.out.println("fOffset: " + fOffset + ", position: " + position + ", sum: " + (position + token_str.length()) + ", str: " + token_str);
		if (position + token_str.length()  != fOffset -1) {
			return false;
		} 
		
//		System.out.println("something found: " + token_str);

		if ((previous < 10) || ((previous > 100) && (previous < 200))) {
			
  		fStartPos = fOffset;
			fAnchor = LEFT;
			fFirst = previous;
			fFirstStringLength = scanner.getTokenLength();
			fEndPos = findMatchingClosing(scanner, fDocument, index, fOffset); 
//			System.out.println("found opening, search for closing to the right");
		} else
		if (((previous  > 10) && (previous  < 20)) || ((previous > 200) && (previous < 300))) {
			fEndPos = position + 1;
			fAnchor = RIGHT;
			fFirst = previous;
			fFirstStringLength = scanner.getTokenLength();
			fStartPos = findMatchingOpening(scanner, fDocument, index-1, position);			
//			System.out.println("found closing, search for opening to the left");
		}
		


		} catch (BadLocationException x) {
//System.out.println("bad location: " + x);				
	
			return false;
		}

		if ((fStartPos >= 0) && (fEndPos > 0) && (fEndPos > fStartPos)) {
			return true;
		}
		return false;

}

	int findMatchingOpening(ExpressSpecialScanner scanner, IDocument document, int index, int start) {

		int current_token;
		int current_position;
		current_position = start;

		int depth = 0;
		for (;;) {
			current_token = scanner.previousToken(current_position, -1);
// System.out.println("current_token: " + current_token + ", current_position: " + current_position);
			if (current_token == fTokenPairs[index+1]) {
				// another closing
				depth++;
			} else
			if (current_token == fTokenPairs[index]) {
				if (depth == 0) {
					// found!
					int start_position = scanner.getPosition();
					if (current_token < 100) {
						start_position += 2;
					} else {
						start_position += scanner.getTokenLength() + 1;
					 }
					return start_position;
				} else {
					depth--;
				}
			} else 
			if (current_token == TokenEOF) {
				// no  match
				break;
			}
			current_position = scanner.getPosition(); 
		}
		return -1;
	}

	int findMatchingClosing(ExpressSpecialScanner scanner, IDocument document, int index, int start) {
		
		int current_token;
		int current_position;
		current_position = start;

		int depth = 0;
		for (;;) {
			current_token = scanner.nextToken(current_position, document.getLength());
			if (current_token == fTokenPairs[index]) {
				// another opening
				depth++;
			} else
			if (current_token == fTokenPairs[index+1]) {
				if (depth == 0) {
					// found!
					int end_position = scanner.getPosition();
					if (current_token < 100) {
						end_position--;
					} else {
						end_position -= scanner.getTokenLength();
					}
					return end_position;
				} else {
					depth--;
				}
			} else 
			if (current_token == TokenEOF) {
				// no  match
				break;
			}
			current_position = scanner.getPosition(); 
		}
		return -1;
	}


//###################### 2nd version


		

	int searchForOpeningWord(String start_string, String end_string, int end_pos, IDocument document)  throws BadLocationException {

//System.out.println("inside backwards ");				

		int start_pos = -1;
		
		
//		String str_content = document.get(0, end_pos+1).toUpperCase();
		String str_content = document.get(0, end_pos).toUpperCase();
//		String str_content = document.get(start_pos, document.getLength() - start_pos).toUpperCase();
//System.out.println("content: " + str_content);
		
		int current_pos = str_content.length()-1;
		int depth = 0;
//		for (int j = 0; j < 1000; j++) {
		for (;;) {
//System.out.println("<before both>");
			int index_start = lastIndexOf(str_content, start_string, current_pos);
//System.out.println("<in between both>");
			int index_end = lastIndexOf(str_content, end_string, current_pos);
//System.out.println("<after both>");
			if (index_start < 0) {
				// no such matching opening word
				return -1;
			}

// System.out.println("forward: " + end_string + ", current_pos: " + current_pos + ", index_start: " + index_start + ", index_end: " + index_end + ", depth: " + depth);

			if (index_end >= 0) {
				// another closing found
				if (index_start < index_end) {
						// and it is before the opening one
						depth++;
						current_pos = index_end - 1; // or add -1
				} else {
					// cannot be at the same position, so the opening one is first
						if (depth == 0) {
							// done!
//							return start_pos + index_start + start_string.length();
							return start_pos + index_start + start_string.length() + 1;
						} else {
							depth--;
							current_pos = index_start - 1;  // -1 could be added
						}
					}
			} else {
				// no more closing, so just the starting one
				if (depth == 0) {
					// ok - found match
//					return start_pos + index_start + start_string.length();
					return start_pos + index_start + start_string.length() + 1;
				} else {
					depth--;
					current_pos = index_start - 1; // -1
				}
			}
		} // for

//		return end_pos;
	} 


	int searchForClosingWord(String start_string, String end_string, int start_pos, IDocument document)  throws BadLocationException {
		int end_pos = -1;
		
		String str_content = document.get(start_pos, document.getLength() - start_pos).toUpperCase();
//System.out.println("content: " + str_content);
		
		int current_pos = 0;
		int depth = 0;
		for (;;) {
			int index_start = indexOf(str_content, start_string, current_pos);
			int index_end = indexOf(str_content, end_string, current_pos);
			if (index_end < 0) {
				// no such matching closing word
				return -1;
			}

// System.out.println("forward: " + end_string + ", current_pos: " + current_pos + ", index_start: " + index_start + ", index_end: " + index_end + ", depth: " + depth);

			if (index_start >= 0) {
				// another opening found
				if (index_start < index_end) {
						// and it is before the closing one
						depth++;
						current_pos = index_start + start_string.length();
				} else {
					// cannot be at the same position, so the closing one is first
						if (depth == 0) {
							// done!
							return start_pos + index_end;
						} else {
							depth--;
							current_pos = index_end + end_string.length();
						}
					}
			} else {
				// no more opening, so just the ending one
				if (depth == 0) {
					// ok - found match
					return start_pos + index_end;
				} else {
					depth--;
					current_pos = index_end + end_string.length();
				}
			}
		} // for

//		return end_pos;
	} 



	/*
		 we want to find words only, separated by delimeters, but not parts of words,
		 we don't want to match SCHEMA with test_end_schema_1, but with END_SCHEMA only
		 so if we use String.indexOf() we also need to check the enclosing characters if they are word characters or not
	*/
	int indexOf(String str, String word, int from_index) {
		int current_index = from_index;
		char c_open;
		char c_close;
		int result = -1;
		
		for (;;) {
			if (current_index >= str.length()) {
				return -1;
			}
			result = str.indexOf(word, current_index);
			if (result < 0) return -1;
			if (result > 0) {
				c_open = str.charAt(result-1);
				if (Character.isJavaIdentifierPart(c_open)) {
					// not good, this is not our match, try again
					current_index = result + word.length();
					continue;
				}
			}
			if (result + word.length() < str.length()) {
				c_close = str.charAt(result + word.length());		
				if (Character.isJavaIdentifierPart(c_close)) {
					// not good, this is not our match, try again
					current_index = result + word.length();
					continue;
				}
			}
			return result;
		} // for
	}

/*
	int lastIndexOf(String str, String word) {
		int result = str.lastIndexOf(word);
		return result;
	}
*/

	int lastIndexOf(String str, String word, int from_index) {

/*
		test how lostIndexOf really works
*/

//		String test_string = "   green      green      ";
//		int index_green_1 = test_string.lastIndexOf("green");
//		int index_green_2 = test_string.lastIndexOf("green", 11);
//		int index_blue = test_string.lastIndexOf("blue", 11);
	
//	System.out.println("green 1: " + index_green_1 + ", green 2: " + index_green_2 + ", blue: " + index_blue);

		int current_index = from_index;
		char c_open;
		char c_close;
		int result = -1;
		
//		for (int j = 0; j < 1000; j++) {
		for (;;) {
			if (current_index <= 0) {
				return -1;
			}
			result = str.lastIndexOf(word, current_index);
//System.out.println("<result>: " + result);
			if (result < 0) return -1;
			if (result > 0) {
				c_open = str.charAt(result-1);
				if (Character.isJavaIdentifierPart(c_open)) {
					// not good, this is not our match, try again
					current_index = result - 1; // or easily result - 1
//System.out.println("<continue 1>");
					continue;
				}
			}
			if (result + word.length() < str.length()) {
				c_close = str.charAt(result + word.length());		
				if (Character.isJavaIdentifierPart(c_close)) {
					// not good, this is not our match, try again
					current_index = result - 1; // or better result - 1
//System.out.println("<continue 2>");
					continue;
				}
			}
			return result;
		} // for
//	return -1;
	}

	int getTokenIndex(int token_value) {
		for (int i = 0; i < fTokenPairs.length; i++) {
			if (fTokenPairs[i] == token_value) {
				return i;
			}
		}
		return -1;
	}
	
	public void dispose() {
		clear();
		fDocument= null;
	}

	public void clear() {
	}
	
	

}

