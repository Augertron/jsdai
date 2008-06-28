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

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedRegion;

public final class ExpressSpecialScanner implements IExpressTokens {

	public static final int NOT_FOUND= -1;
	public static final int UNBOUND= -2;

	IDocument fDocument;
	String fPartitioner;
	String fPartition;
	
	int fPos;
	char fChar;
	String fWord;
	int fWordLength;

	private static final char LPAREN= '(';
	private static final char RPAREN= ')';
	private static final char LBRACKET= '[';
	private static final char RBRACKET= ']';

	private static final char LBRACE= '{';
	private static final char RBRACE= '}';
	private static final char LANGLE= '<';
	private static final char RANGLE= '>';

	private static final char SEMICOLON= ';';




	// condition for the scanner to stop, has to be extended by specific conditions
	private static abstract class StopCondition {
		
		// returns true if the stop condition is met, false otherwise, ch is the character @ the current position
		public abstract boolean stop(char ch, int position, boolean forward);

		public int nextPosition(int position, boolean forward) {
			return forward ? position + 1 : position - 1;
		}
	}


  // Stops upon a non-whitespace, any partition type
	private static class NonWhitespace extends StopCondition {
		public boolean stop(char ch, int position, boolean forward) {
			return !Character.isWhitespace(ch);
		}
	}

	// Stops upon a non-whitespace character in the default partition
	private final class NonWhitespaceDefaultPartition extends NonWhitespace {
		public boolean stop(char ch, int position, boolean forward) {
			return super.stop(ch, position, true) && isDefaultPartition(position);
		}
		
		// if non-default partition, skips the partition till its end 
		public int nextPosition(int position, boolean forward) {
			ITypedRegion partition = getPartition(position);
			if (fPartition.equals(partition.getType()))
				return super.nextPosition(position, forward);

			if (forward) {
				int end = partition.getOffset() + partition.getLength();
				if (position < end)
					return end;
			} else {
				int offset = partition.getOffset();
				if (position > offset)
					return offset - 1;
			}
			return super.nextPosition(position, forward);
		}
	}


	// Stops upon a non-java identifier in any partition - as Character class has methods for java identifiers, I use them for express as well, for now
	private static class NonJavaIdentifierPart extends StopCondition {
		public boolean stop(char ch, int position, boolean forward) {
			return !Character.isJavaIdentifierPart(ch);
		}
	}

	// Stops upon a non-java identifier character in the default partition
	private final class NonJavaIdentifierPartDefaultPartition extends NonJavaIdentifierPart {
		public boolean stop(char ch, int position, boolean forward) {
			return super.stop(ch, position, true) || !isDefaultPartition(position);
		}

		// if in non-default partition, skip till the end of it
		public int nextPosition(int position, boolean forward) {
			ITypedRegion partition= getPartition(position);
			if (fPartition.equals(partition.getType()))
				return super.nextPosition(position, forward);

			if (forward) {
				int end= partition.getOffset() + partition.getLength();
				if (position < end)
					return end;
			} else {
				int offset= partition.getOffset();
				if (position > offset)
					return offset - 1;
			}
			return super.nextPosition(position, forward);
		}
	}

	/* preset stop conditions */
	private final StopCondition fNonWSDefaultPart= new NonWhitespaceDefaultPartition();
	// private final static StopCondition fNonWS= new NonWhitespace();
	private final StopCondition fNonIdent= new NonJavaIdentifierPartDefaultPartition();



	public ExpressSpecialScanner(IDocument document, String partitioner, String partition) {
		Assert.isNotNull(document);
		Assert.isNotNull(partitioner);
		Assert.isNotNull(partition);
		fDocument= document;
		fPartitioner = partitioner;
		fPartition= partition;
	}

	public ExpressSpecialScanner(IDocument document) {
		this(document, ExpressConstants.EXPRESS_PARTITIONER, IDocument.DEFAULT_CONTENT_TYPE);
	}

	public int getPosition() {
		return fPos;
	}

	public int nextToken(int start, int bound) {
		int pos = scanForward(start, bound, fNonWSDefaultPart);
		if (pos == NOT_FOUND)
			return TokenEOF;

		fPos++;

		switch (fChar) {
			case LPAREN:
				return TokenLPAREN;
			case RPAREN:
				return TokenRPAREN;
			case LBRACKET:
				return TokenLBRACKET;
			case RBRACKET:
				return TokenRBRACKET;

			case LBRACE:
				return TokenLBRACE;
			case RBRACE:
				return TokenRBRACE;
			case LANGLE:
				return TokenLESS;
			case RANGLE:
				return TokenGREATER;

			case SEMICOLON:
				return TokenSEMICOLON;

		}

		// else check multi-character tokens
		if (Character.isJavaIdentifierPart(fChar)) {
			// assume an ident or keyword
			int from = pos, to;
			pos = scanForward(pos + 1, bound, fNonIdent);
			if (pos == NOT_FOUND)
				to = bound == UNBOUND ? fDocument.getLength() : bound;
			else
				to = pos;

			String identOrKeyword;
			try {
				identOrKeyword = fDocument.get(from, to - from);
			} catch (BadLocationException e) {
				return TokenEOF;
			}

			return getToken(identOrKeyword);


		} else {
			// operators, number literals etc
			return TokenOTHER;
		}
	}

	public int previousToken(int start, int bound) {
		int pos = scanBackward(start, bound, fNonWSDefaultPart);
		if (pos == NOT_FOUND)
			return TokenEOF;

		fPos--;

		switch (fChar) {
			case LPAREN:
				return TokenLPAREN;
			case RPAREN:
				return TokenRPAREN;
			case LBRACKET:
				return TokenLBRACKET;
			case RBRACKET:
				return TokenRBRACKET;

			case LBRACE:
				return TokenLBRACE;
			case RBRACE:
				return TokenRBRACE;
			case LANGLE:
				return TokenLESS;
			case RANGLE:
				return TokenGREATER;

			case SEMICOLON:
				return TokenSEMICOLON;

		}

		// else check multi-character tokens
		if (Character.isJavaIdentifierPart(fChar)) {
			// assume an ident or keyword
			int from, to = pos + 1;
			pos= scanBackward(pos - 1, bound, fNonIdent);
			if (pos == NOT_FOUND)
				from = bound == UNBOUND ? 0 : bound + 1;
			else
				from = pos + 1;

			String identOrKeyword;
			try {
				identOrKeyword = fDocument.get(from, to - from);
			} catch (BadLocationException e) {
				return TokenEOF;
			}

			return getToken(identOrKeyword);


		} else {
			// operators, number literals etc
			return TokenOTHER;
		}

	}

	public String getTokenString() {
		if (fWord == null) {
			char [] chars = new char[1];
			chars[0] = fChar;
			return (new String(chars));
		}
		
		return fWord;
	}

	public int getTokenLength() {
		return fWordLength;
	}

	// not needed frequently
  public int getCurrentToken() {
  	return getToken(getTokenString());
  }


	private int getToken(String s) {
		Assert.isNotNull(s);
		
		fWord = s;
		fWordLength = s.length();

		switch (s.length()) {
			case 2:
				if ("IF".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenIF;
				break;
			case 3:
				if ("END".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND;
				break;
			case 4:
				if ("ELSE".equalsIgnoreCase(s)) //$NON-NLS-1$
//					return TokenELSE;
					return TokenOTHER;
				if ("RULE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenRULE;
				if ("TYPE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenTYPE;
				if ("CASE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenCASE;
				break;
			case 5:
				if ("LOCAL".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenLOCAL;
				if ("ELSIF".equalsIgnoreCase(s)) //$NON-NLS-1$
//					return TokenELSIF;
					return TokenOTHER;
				if ("BEGIN".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenBEGIN;
				if ("ALIAS".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenALIAS;
				break;
			case 6:
				if ("ENTITY".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenENTITY;
				if ("REPEAT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenREPEAT;
				if ("END_IF".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_IF;
				if ("SCHEMA".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenSCHEMA;
				break;
			case 8:
				if ("END_RULE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_RULE;
				if ("FUNCTION".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenFUNCTION;
				if ("END_TYPE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_TYPE;
				if ("END_CASE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_CASE;
				if ("CONSTANT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenCONSTANT;
				break;
			case 9:
				if ("END_LOCAL".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_LOCAL;
				if ("PROCEDURE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenPROCEDURE;
				if ("END_ALIAS".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_ALIAS;
				break;
			case 10:
				if ("END_ENTITY".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_ENTITY;
				if ("END_REPEAT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_REPEAT;
				if ("END_SCHEMA".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_SCHEMA;
				break;
			case 12:
				if ("END_FUNCTION".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_FUNCTION;
				if ("END_CONSTANT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_CONSTANT;
				break;
			case 13:
				if ("END_PROCEDURE".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_PROCEDURE;
				break;
			case 18:
				if ("SUBTYPE_CONSTRAINT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenSUBTYPE_CONSTRAINT;
				break;
			case 22:
				if ("END_SUBTYPE_CONSTRAINT".equalsIgnoreCase(s)) //$NON-NLS-1$
					return TokenEND_SUBTYPE_CONSTRAINT;
				break;
		}
		return TokenOTHER;
	}

    // start - included, bound - up to but excluded
	public int scanForward(int start, int bound, StopCondition condition) {
		Assert.isTrue(start >= 0);

		if (bound == UNBOUND)
			bound= fDocument.getLength();

		Assert.isTrue(bound <= fDocument.getLength());

		try {
			fPos= start;
			while (fPos < bound) {

				fChar= fDocument.getChar(fPos);
				fWord = null;
				if (condition.stop(fChar, fPos, true))
					return fPos;

				fPos= condition.nextPosition(fPos, true);
			}
		} catch (BadLocationException e) {
		}
		return NOT_FOUND;
	}

    // start - included, bound - down to but excluded
	public int scanBackward(int start, int bound, StopCondition condition) {
		if (bound == UNBOUND)
			bound= -1;

		Assert.isTrue(bound >= -1);
		Assert.isTrue(start < fDocument.getLength() );

		try {
			fPos= start;
			while (fPos > bound) {

				fChar= fDocument.getChar(fPos);
				fWord = null;
				if (condition.stop(fChar, fPos, false))
					return fPos;

				fPos= condition.nextPosition(fPos, false);
			}
		} catch (BadLocationException e) {
		}
		return NOT_FOUND;
	}
	
	
	private ITypedRegion getPartition(int position) {
		Assert.isTrue(position >= 0);
		Assert.isTrue(position <= fDocument.getLength());

		try {
			return TextUtilities.getPartition(fDocument, fPartitioner, position, false);
		} catch (BadLocationException e) {
			return new TypedRegion(position, 0, "__no_partition_error_"); //$NON-NLS-1$
		}

	}
	
	
	public boolean isDefaultPartition(int position) {
		Assert.isTrue(position >= 0);
		Assert.isTrue(position <= fDocument.getLength());

		try {
			return fPartition.equals(TextUtilities.getContentType(fDocument, fPartitioner, position, false));
		} catch (BadLocationException e) {
			return false;
		}
	}


}


