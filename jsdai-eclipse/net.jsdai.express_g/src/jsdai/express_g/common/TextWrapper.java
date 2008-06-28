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

package jsdai.express_g.common;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;

/**
 * @author Mantas Balnys
 *
 * Provides methods for text wrapping
 */
public class TextWrapper {
	// wrapping text
	private String text = "";
	// text lines
	private TextLine line = null; 
	private TextLine[] lineA = {};
	// delimiters for text
	private String delimiters = "\r\n\t ";
	// minimal available width of text (updated on text changes)
	private int minWidth = 0;
	// style of delimiter placing
	// do not return delimiters
	public static final int STYLE_NO_DELIM = 1;
	// put delimiters on the end of the word (break after delim)
	public static final int STYLE_DELIM_AT_BACK = 2;
	// put delimiters at front of the word (break before delim)
	public static final int STYLE_DELIM_AT_FRONT = 3;
	private int style = STYLE_NO_DELIM;
	// current width of text 
	private int width = 0;
	// maximum width of text lines
	private int available_width = 0;
	// height of single line
	private int lineHeight = 0;
	// current graphical context
	private GC gc;
	private boolean gcValid = false;
	// used font (may be null if using GC font)
	private Font font = null;
		
	/**
	 * 
	 */
	public TextWrapper(String text) {
		super();
		setText(text);
	}

	/**
	 * 
	 */
	public TextWrapper(String text, String delimiters) {
		this(text);
		setDelimiters(delimiters);
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
		gcValid = false;
	}
	
	protected void updateLine() {
		if (font != null) gc.setFont(font);
		if (line != null) line.dispose();
		line = new TextLine(gc, text, delimiters);
		lineHeight = gc.getFontMetrics().getHeight();
		gcValid = true;
		updateMinWidth();
	}
	
	protected void updateMinWidth() {
		updateParams();
		if (gcValid) {
			TextLine ln = line;
			minWidth = 0;
			while (ln != null) {
				int sz = ln.getSize();
				if (sz > minWidth) minWidth = sz;
				ln = ln.getNext();
			}
		}
	}
	
	protected void updateParams() {
		if (gcValid) {
			line.resetIterator(style, available_width);
			if (line.getPartString().equals("")) {
				lineA = new TextLine[0];
				width = 0;
			} else {
				TextLine ln = line;
				int count = 0;
				while (ln != null) {
					ln = ln.getNextPart();
					count++;
				}
				lineA = new TextLine[count];
				ln = line;
				count = 0;
				width = 0;
				while (ln != null) {
					if (ln.getPartWidth() > width) width = ln.getPartWidth();
					lineA[count] = ln;
					ln = ln.getNextPart();
					count++;
				}
			}
		}
	}
	
	/**
	 * returns count of string lines
	 * @return
	 */
	public int getLineCount() {
		return lineA.length;
	}
	
	/**
	 * returns width of specified part
	 * @param index
	 * @return
	 */
	public int getLineWidth(int index) {
		return lineA[index].getPartWidth();
	}
	
	/**
	 * returns index line text
	 * @param index
	 * @return
	 */
	public String getLine(int index) {
		return lineA[index].getPartString();
	}
	
	public int getLineHeight() {
		return lineHeight;
	}
	
	/**
	 * returns minimal available width of this wrapped text
	 * value is counted when text is set
	 * @return
	 */
	public int getMinWidth() {
		return minWidth;
	}
	
	/**
	 * @return Returns the delimiters.
	 */
	public String getDelimiters() {
		return delimiters;
	}
	
	/**
	 * @param delimiters The delimiters to set.
	 */
	public void setDelimiters(String delimiters) {
		this.delimiters = delimiters;
		gcValid = false;
	}
	
	/**
	 * @return Returns the gc.
	 */
	public GC getGC() {
		return gc;
	}
	
	/**
	 * @param gc The gc to set.
	 */
	public void setGC(GC gc) {
		this.gc = gc;
		updateLine();
	}
	
	/**
	 * used font
	 * may be null if using GC font
	 * @return
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * used font
	 * may be null if using GC font
	 * @param font
	 */
	public void setFont(Font font) {
		if (((this.font == null)&&(font != null))||
				(this.font != null)&&(!this.font.equals(font))) {
			this.font = font;
			if (gcValid) updateLine();
			else gcValid = false;
		}
	}

	/**
	 * @return Returns the style.
	 */
	public int getStyle() {
		return style;
	}
	
	/**
	 * @param style The style to set.
	 */
	public void setStyle(int style) {
		this.style = style;
		updateMinWidth();
	}
	
	public void setWidth(int width) {
		if (width < minWidth) width = minWidth;
		if (width != this.available_width) {
			this.available_width = width;
			updateParams();
		}
	}
	
	
	public boolean isGCValid() {
		return gcValid;
	}
	
	public String toString() {
		String text = "jsdai.express_g.common.TextWrapper(" + Integer.toHexString(hashCode()) + 
			")\nvalid=" + gcValid + " height=" + getLineHeight() + " width=" + getMinWidth() + " lines=" + getLineCount() + "\n";
		
		if (gcValid) {
			for (int i = 0; i < getLineCount(); i++) {
				text += "\"" + getLine(i) + "\" : " + getLineWidth(i) + "\n";
			}
		}
		return text;
	}

	private static class TextLine {
		// next token
		private TextLine next = null;
		// delimiter in front of word
		private String delim1;
		private int size1 = 0;
		// word
		private String line;
		private int sizeT = 0;
		// delimiter after word
		private String delim2;
		private int size2 = 0;
		// style (iterator parameters)
		private int style;
		// width (iterator parameters)
		private int width;
		// iterWidth (iterator parameters)
		private int iterWidth;
		// iterNext (iterator parameters)
		private TextLine iterNext;
		// iterText (iterator parameters)
		private String iterText;
		//
		private boolean first = false;
		
		protected TextLine(String text, String delimiters, GC gc) {
			// parsing delimiters in front of word
			int a = 0;
			while ((a < text.length())&&(delimiters.indexOf(text.charAt(a)) >= 0)) a++;
			delim1 = text.substring(0, a);
			// parsing word
			int b = a;
			while ((b < text.length())&&(delimiters.indexOf(text.charAt(b)) < 0)) b++;
			line = text.substring(a, b);
			// parsing delimiters after word
			int c = b;
			while ((c < text.length())&&(delimiters.indexOf(text.charAt(c)) >= 0)) c++;
			delim2 = text.substring(b, c);
			
			// count sizes
			size1 = sizeOf(gc, delim1);
			sizeT = sizeOf(gc, line);
			size2 = sizeOf(gc, delim2);

			// continue with rest if available
//System.out.println("creating text line: <" + delim1 + ">=" + size1 + " + <" + line + ">=" + sizeT + " + <" + delim2 + ">=" + size2); 
			if (b < text.length())
				next = new TextLine(text.substring(b), delimiters, gc);
		}
		
		public TextLine(GC gc, String text, String delimiters) {
			this(text, delimiters, gc);
			first = true;
		}
		
		/**
		 * counts size of string
		 * @param gc
		 * @param text
		 * @return
		 */
		private int sizeOf(GC gc, String text) {
			int size = 0;
			char[] cc = text.toCharArray();
			for (int i = 0; i < cc.length; i++)
				size +=	gc.getAdvanceWidth(cc[i]);
			return size;
		}
		
		public int getSize() {
			int size = sizeT;
			if (first) size += size1;
			switch (style) {
				case STYLE_DELIM_AT_FRONT :
					if (!first) size += size1;
					break;
				case STYLE_DELIM_AT_BACK :
					size += size2;
					break;
			}
			return size;
		}
		
		protected String getText() {
			String text = line;
			if ((first)&&(style == STYLE_DELIM_AT_BACK)) text = delim1 + text;
			switch (style) {
				case STYLE_DELIM_AT_FRONT :
					text = delim1 + text;
					break;
				case STYLE_DELIM_AT_BACK :
					text = text + delim2;
					break;
			}
			return text;
		}
		
		protected void resetIterInner(int style, int width) {
			this.style = style;
			this.width = width;
			if (next != null) next.resetIterInner(style, width);
		}

		protected void resetIterator() {
			iterWidth = getSize();
			int size = iterWidth;
			iterNext = this;
			iterText = "";
			while ((size < width)&&(iterNext != null)) {
				iterWidth = size;
//				if (style == STYLE_DELIM_AT_BACK) iterText += iterNext.delim1;
				iterText += iterNext.getText();
				iterNext = iterNext.next;
				if (iterNext != null) {
					size += iterNext.getSize();
//					if (style == STYLE_DELIM_AT_FRONT) size += iterNext.size1;
				}
			}
			if (iterNext == this) {
				iterNext = next;
				iterText = getText();
			}
			if (iterNext != null) iterNext.resetIterator();
		}
		
		public void resetIterator(int style, int width) {
			// reset style and width recursively for all parts
			resetIterInner(style, width);
			// recount parts
			resetIterator();
		}
		
		public TextLine getNext() {
			return next;
		}
		
		public TextLine getNextPart() {
			return iterNext;
		}
		
		public int getPartWidth() {
			return iterWidth;
		}
		
		public String getPartString() {
			return iterText;
		}
		
		public void dispose() {
			if (next != null) next.dispose();
			next = null;
		}
	
	}
}