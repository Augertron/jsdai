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
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

/**
 * Double click strategy aware of Express identifier syntax rules.
 */

// public class ExpressDoubleClickStrategy extends DefaultTextDoubleClickStrategy implements ITextDoubleClickStrategy {


public class ExpressDoubleClickStrategy implements ITextDoubleClickStrategy {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.ITextDoubleClickStrategy#doubleClicked(org.eclipse.jface.text.ITextViewer)
	 */

/*
 
 	public void doubleClicked(ITextViewer viewer) {
		// TODO Auto-generated method stub
		
	}
*/


///########################################## 




	protected static final char[] EXPRESSCHARPAIRS = 
		{ '{', '}', // not really express, but so what
			'(', ')', 
			'*', ')',  //  (*    *)
			'[', ']', 
			'<', '>'  // not really express, but so what
		};

	protected static final String[] EXPRESCHARSWORDSPAIRS = 
		{ 
			// may include or not this part
//			"{", "}", // not really express, but so what
//			"(", ")", 
//			"[", "]", 
//			"<", ">",  // not really express, but so what
			// real words here
			"BEGIN", "END",
			"ALIAS", "END_ALIAS",
			"CASE", "END_CASE",
			"CONSTANT", "END_CONSTANT",
			"ENTITY", "END_ENTITY",
			"FUNCTION", "END_FUNCTION",
			"IF", "END_IF",
			"LOCAL", "END_LOCAL",
			"PROCEDURE", "END_PROCEDURE",
			"REPEAT", "END_REPEAT",
			"RULE", "END_RULE",
			"SCHEMA", "END_SCHEMA",
			"SUBTYPE_CONSTRAINT", "END_SUBTYPE_CONSTRAINT",
			"TYPE", "END_TYPE"
		};

	protected static final String[] EXPRESWORDSPAIRS = 
		{ 
			// may include or not this part
			"{", "}", // not really express, but so what
			"(", ")", 
			"[", "]", 
			"<", ">",  // not really express, but so what
			// real words here
			"BEGIN", "END",
			"ALIAS", "END_ALIAS",
			"CASE", "END_CASE",
			"CONSTANT", "END_CONSTANT",
			"ENTITY", "END_ENTITY",
			"FUNCTION", "END_FUNCTION",
			"IF", "END_IF",
			"LOCAL", "END_LOCAL",
			"PROCEDURE", "END_PROCEDURE",
			"REPEAT", "END_REPEAT",
			"RULE", "END_RULE",
			"SCHEMA", "END_SCHEMA",
			"SUBTYPE_CONSTRAINT", "END_SUBTYPE_CONSTRAINT",
			"TYPE", "END_TYPE"
		};

	
//	protected ExpressPairMatcher fPairMatcher= new ExpressPairMatcher(EXPRESCHARSWORDSPAIRS);
	protected ExpressPairMatcher fPairMatcher= new ExpressPairMatcher(EXPRESSCHARPAIRS, EXPRESWORDSPAIRS);
	protected final ExpressIdentifierDetector fWordDetector= new ExpressIdentifierDetector();



	/**
	 * @see ITextDoubleClickStrategy#doubleClicked
	 */
	public void doubleClicked(ITextViewer textViewer) {

		int offset = textViewer.getSelectedRange().x;

		if (offset < 0)
			return;

		IDocument document = textViewer.getDocument();

// let's tray te print the partition type of the offset point
	
//	ITypedRegion treg = PartitionUtils.getPartition(ExpressConstants.EXPRESS_PARTITIONER, document, null, offset, false);
//System.out.println("partition type: " + treg.getType());



		IRegion region = fPairMatcher.matchPair(document, offset);
		if (region != null && region.getLength() >= 2) {
			// textViewer.setSelectedRange(region.getOffset() + 1, region.getLength() - 2);
			textViewer.setSelectedRange(region.getOffset(), region.getLength() - 1);
		} else {
			region= selectWord(document, offset);
			textViewer.setSelectedRange(region.getOffset(), region.getLength());
		}
	}

	protected IRegion selectWord(IDocument document, int anchor) {
		return fWordDetector.getWordSelection(document, anchor);
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.ISourceVersionDependent#setSourceVersion(java.lang.String)
	 */
/*
	public void setSourceVersion(String version) {
		fPairMatcher.setSourceVersion(version);
		fWordDetector.setSourceVersion(version);
	}
*/
}


//private static final class ExpressIdentifierDetector {
class ExpressIdentifierDetector {

	private boolean fSelectAnnotations;

	private static final int UNKNOWN= -1;

	/* states */
	private static final int WS= 0;
	private static final int ID= 1;
	private static final int IDS= 2;
	private static final int AT= 3;

	/* directions */
	private static final int FORWARD= 0;
	private static final int BACKWARD= 1;

	/** The current state. */
	private int fState;
	/**
	 * The state at the anchor (if already detected by going the other way),
	 * or <code>UNKNOWN</code>.
	 */
	private int fAnchorState;
	/** The current direction. */
	private int fDirection;
	/** The start of the detected word. */
	private int fStart;
	/** The end of the word. */
	private int fEnd;

	/**
	 * Initializes the detector at offset <code>anchor</code>.
	 *
	 * @param anchor the offset of the double click
	 */
	private void setAnchor(int anchor) {
		fState= UNKNOWN;
		fAnchorState= UNKNOWN;
		fDirection= UNKNOWN;
		fStart= anchor;
		fEnd= anchor - 1;
	}

	private boolean isAt(char c) {
		return fSelectAnnotations && c == '@';
	}

	private boolean isIdentifierStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	private boolean isIdentifierPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

	private boolean isWhitespace(char c) {
		return fSelectAnnotations && Character.isWhitespace(c);
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.ISourceVersionDependent#setSourceVersion(java.lang.String)
	 */
/*
	public void setSourceVersion(String version) {
//		if (JavaCore.VERSION_1_5.compareTo(version) <= 0)
		if (false)
			fSelectAnnotations= true;
		else
			fSelectAnnotations= false;
	}
*/
	/**
	 * Try to add a character to the word going backward. Only call after
	 * forward calls!
	 *
	 * @param c the character to add
	 * @param offset the offset of the character
	 * @return <code>true</code> if further characters may be added to the
	 *         word
	 */
	private boolean backward(char c, int offset) {
		checkDirection(BACKWARD);
		switch (fState) {
			case AT:
				return false;
			case IDS:
				if (isAt(c)) {
					fStart= offset;
					fState= AT;
					return false;
				}
				if (isWhitespace(c)) {
					fState= WS;
					return true;
				}
				// fall through ID
			case ID:
				if (isIdentifierStart(c)) {
					fStart= offset;
					fState= IDS;
					return true;
				}
				if (isIdentifierPart(c)) {
					fStart= offset;
					fState= ID;
					return true;
				}
				return false;
			case WS:
				if (isWhitespace(c)) {
					return true;
				}
				if (isAt(c)) {
					fStart= offset;
					fState= AT;
					return false;
				}
				return false;
			default:
				return false;
		}
	}

	/**
	 * Try to add a character to the word going forward.
	 *
	 * @param c the character to add
	 * @param offset the offset of the character
	 * @return <code>true</code> if further characters may be added to the
	 *         word
	 */
	private boolean forward(char c, int offset) {
		checkDirection(FORWARD);
		switch (fState) {
			case WS:
			case AT:
				if (isWhitespace(c)) {
					fState= WS;
					return true;
				}
				if (isIdentifierStart(c)) {
					fEnd= offset;
					fState= IDS;
					return true;
				}
				return false;
			case IDS:
			case ID:
				if (isIdentifierStart(c)) {
					fEnd= offset;
					fState= IDS;
					return true;
				}
				if (isIdentifierPart(c)) {
					fEnd= offset;
					fState= ID;
					return true;
				}
				return false;
			case UNKNOWN:
				if (isIdentifierStart(c)) {
					fEnd= offset;
					fState= IDS;
					fAnchorState= fState;
					return true;
				}
				if (isIdentifierPart(c)) {
					fEnd= offset;
					fState= ID;
					fAnchorState= fState;
					return true;
				}
				if (isWhitespace(c)) {
					fState= WS;
					fAnchorState= fState;
					return true;
				}
				if (isAt(c)) {
					fStart= offset;
					fState= AT;
					fAnchorState= fState;
					return true;
				}
				return false;
			default:
				return false;
		}
	}

	/**
	 * If the direction changes, set state to be the previous anchor state.
	 *
	 * @param direction the new direction
	 */
	private void checkDirection(int direction) {
		if (fDirection == direction)
			return;

		if (direction == FORWARD) {
			if (fStart <= fEnd)
				fState= fAnchorState;
			else
				fState= UNKNOWN;
		} else if (direction == BACKWARD) {
			if (fEnd >= fStart)
				fState= fAnchorState;
			else
				fState= UNKNOWN;
		}

		fDirection= direction;
	}

	/**
	 * Returns the region containing <code>anchor</code> that is a java
	 * word.
	 *
	 * @param document the document from which to read characters
	 * @param anchor the offset around which to select a word
	 * @return the region describing a java word around <code>anchor</code>
	 */
	public IRegion getWordSelection(IDocument document, int anchor) {

		try {

			final int min= 0;
			final int max= document.getLength();
			setAnchor(anchor);

			char c;

			int offset= anchor;
			while (offset < max) {
				c= document.getChar(offset);
				if (!forward(c, offset))
					break;
				++offset;
			}

			offset= anchor; // use to not select the previous word when right behind it
//			offset= anchor - 1; // use to select the previous word when right behind it
			while (offset >= min) {
				c= document.getChar(offset);
				if (!backward(c, offset))
					break;
				--offset;
			}

			return new Region(fStart, fEnd - fStart + 1);

		} catch (BadLocationException x) {
			return new Region(anchor, 0);
		}
	}

}

