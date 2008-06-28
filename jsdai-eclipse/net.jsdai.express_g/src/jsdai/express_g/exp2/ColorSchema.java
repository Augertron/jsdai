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

package jsdai.express_g.exp2;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @(#) ColorSchema.java
 */

public class ColorSchema {
	
  public static final int STROKE_WIDTH = 1;
  public static final int STROKE_WIDTH2 = 2;

//  public static final Font FONT1 = new Font(Display.getDefault(), "Times New Roman", 10, SWT.NORMAL);
//  public static final Font FONT2 = new Font(Display.getDefault(), "Times New Roman", 8, SWT.NORMAL);
//  public static final Font FONT1 = new Font(Display.getDefault(), "Arial", 9, SWT.NORMAL);
//  public static final Font FONT2 = new Font(Display.getDefault(), "Arial", 8, SWT.NORMAL);
  
  public static final int PALETTE_SIZE = 256; // 6x6x6 colors + 40 grayscale
  
  public static RGB[] PALETTE = new RGB[PALETTE_SIZE];

  public static Color[] COLOR = new Color[PALETTE_SIZE];
/*  = new RGB[] {
  		//main colors
/*00* 		new RGB(0,0,0),
/*01*		new RGB(255,255,255),
/*02*		new RGB(255,0,0),
/*03*		new RGB(0,255,0),
/*04*		new RGB(0,0,255),
/*05*		new RGB(0,255,255),
/*06*		new RGB(255,0,255),
/*07*		new RGB(255,255,0),
		//midle colors 1
/*08* 		new RGB(128,0,0),
/*09*		new RGB(128,255,255),
/*10*		new RGB(128,0,255),
/*11*		new RGB(128,255,0),
/*12* 		new RGB(0,128,0),
/*13*		new RGB(255,128,255),
/*14*		new RGB(0,128,255),
/*15*		new RGB(255,128,0),
/*16* 		new RGB(0,0,128),
/*17*		new RGB(255,255,128),
/*18*		new RGB(0,255,128),
/*19*		new RGB(255,0,128),
		//midle colors 2
/*20*		new RGB(0,128,128),
/*21*		new RGB(128,0,128),
/*22*		new RGB(128,128,0),
/*23*		new RGB(255,128,128),
/*24*		new RGB(128,255,128),
/*25*		new RGB(128,128,255),
		//midle colors 3
/*26*		new RGB(128,128,128),
		//grays
/*27*		new RGB(32,32,32),
/*28*		new RGB(64,64,64),
/*29*		new RGB(96,96,96),
/*30*		new RGB(160,160,160),
/*31*		new RGB(192,192,192),
/*32*		new RGB(224,224,224),
  };*/
  
  
  private static final int normalize256(int value) {
	  if (value > 255) value %= 256;
	  else if (value < 0) value = 256 + value % 256;
//if (value < 0 || value > 255) System.out.println("value error: " + value);	  
	  return value;
  }
  
  /**
   * gets nearest color index from indexed palette
   * if parameter value is out of range the module of value is taken
   * @param red 0-63
   * @param green 0-63
   * @param blue 0-63
   * @return
   */
  public static final int get_nearest32(int red, int green, int blue) {
	  int nred = normalize32(red);
	  int ngreen = normalize32(green);
	  int nblue = normalize32(blue);
	  int index;
	  if (nred == ngreen && ngreen == nblue) { // grayscale (reversed - white = 215, black = 255)
		  index = 215 + (int)((31.3 - nred) / 0.775);
	  } else { // Internet default colors 6x6x6
		  index = ((int)((nred + 3) / 6.2)) * 36 
		  	+ ((int)((ngreen + 3) / 6.2)) * 6 
		  	+ (int)((nblue + 3) / 6.2);
	  }
//SdaieditPlugin.log(red + "," + green + "," + blue + "->" + index, IStatus.INFO);	  
	  return index;
  }
  
  private static final int normalize32(int value) {
	  if (value > 31) value %= 32;
	  else if (value < 0) value = 32 + value % 32;
	  return value;
  }
  
  /**
   * gets nearest color index from indexed palette
   * if parameter value is out of range the module of value is taken
   * @param red 0-255
   * @param green 0-255
   * @param blue 0-255
   * @return
   */
  public static final int get_nearest256(int red, int green, int blue) {
	  int nred = normalize256(red);
	  int ngreen = normalize256(green);
	  int nblue = normalize256(blue);
	  int index;
	  if (nred == ngreen && ngreen == nblue) { // grayscale (reversed - white = 215, black = 255)
		  index = 215 + (int)((258 - nred) / 6.375);
	  } else { // Internet default colors 6x6x6
		  index = ((int)((normalize256(red) + 25) / 51)) * 36 
		  	+ ((int)((normalize256(green) + 25) / 51)) * 6 
		  	+ (int)((normalize256(blue) + 25) / 51);
	  }
	  return index;
  }
  
  /**
   * converts indexed palette to RGB
   * @param index 0-PALETTE_SIZE
   * @return
   */
  public static final RGB get_color(int index) {
	  if (index > 214) { // grayscale (reversed - white = 215, black = 255)
		  int gray = (int)((255 - index) * 6.375);
		  return new RGB(gray, gray, gray);
	  } else { // Internet default colors 6x6x6
		  int blue = index % 6 * 51;	
		  index /= 6;
		  int green = index % 6 * 51;	
		  index /= 6;
		  int red = index % 6 * 51;	
		  return new RGB(red, green, blue);
	  }
  }
  
  static {
	  for (int i = 0; i < PALETTE_SIZE; i++) {
		  PALETTE[i] = get_color(i);
		  COLOR[i] = new Color(Display.getCurrent(), PALETTE[i]);
	  }
  }
  
  public static final int COLOR_WHITE = get_nearest256(255, 255, 255);
  public static final int COLOR_DARK_WHITE = get_nearest256(204, 204, 204);
  public static final int COLOR_DARK_GRAY = get_nearest256(51, 51, 51);
  public static final int COLOR_GRAY = get_nearest256(153, 153, 153);
  public static final int COLOR_LIGHT_GRAY = get_nearest256(204, 204, 204);
  public static final int COLOR_BLACK = get_nearest256(0, 0, 0);
  public static final int COLOR_RED = get_nearest256(255, 0, 0);
  public static final int COLOR_LIGHT_RED = get_nearest256(255, 153, 0);
  public static final int COLOR_YELLOW = get_nearest256(255, 255, 0);
  public static final int COLOR_GREEN = get_nearest256(0, 255, 0);
  public static final int COLOR_BLUE = get_nearest256(0, 0, 255);
  public static final int COLOR_LIGHT_GREEN = get_nearest256(102, 255, 102);

  
 /*/ 
  public static final Color COLOR_WHITE = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_DARK_WHITE = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_DARK_GRAY = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_GRAY = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_LIGHT_GRAY = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_BLACK = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_RED = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_LIGHT_RED = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_YELLOW = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_GREEN = new Color(Display.getDefault(), new RGB(0, 0, 0));
  public static final Color COLOR_LIGHT_GREEN = new Color(Display.getDefault(), new RGB(0, 0, 0));
 /**/ 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public int background = COLOR_WHITE;

  public int foreground = COLOR_BLACK;
  
  public int lineWidth = STROKE_WIDTH;
  
  public int[] lineStyle = null;
  
  public static final int[] DASHED_LINE = new int[]{5, 5};
  
//  public Font font = FONT1;

  	public ColorSchema() {
//  		font = new Font(Display.getDefault(), "Times New Roman", 10, SWT.NORMAL);
  	}

  public ColorSchema(ColorSchema schema) {
  	this();
    background = schema.background;
    foreground = schema.foreground;
    lineWidth = schema.lineWidth;
    lineStyle = schema.lineStyle;
//    font = schema.font;
  }

  public ColorSchema(int foreground, int background) {
  	this();
    this.foreground = foreground;
    this.background = background;
  }

  public ColorSchema(int foreground, int background, int lineWidth) {
    this(foreground, background);
    this.lineWidth = lineWidth;
  }

  public ColorSchema(int foreground, int background, int lineWidth, int[] lineStyle) {
    this(foreground, background, lineWidth);
    this.lineStyle = lineStyle;
  }
  
  public int getRedF() {
	  return PALETTE[foreground].red;
  }
  
  public int getGreenF() {
	  return PALETTE[foreground].green;
  }
  
  public int getBlueF() {
	  return PALETTE[foreground].blue;
  }
  
  public int getRedB() {
	  return PALETTE[background].red;
  }
  
  public int getGreenB() {
	  return PALETTE[background].green;
  }
  
  public int getBlueB() {
	  return PALETTE[background].blue;
  }

  public void apply(GC g) {
  	g.setBackground(COLOR[background]);
  	g.setForeground(COLOR[foreground]);
  	g.setLineDash(lineStyle);
  	g.setLineWidth(lineWidth);
//  	if (font != null) g.setFont(font);
  }

}

