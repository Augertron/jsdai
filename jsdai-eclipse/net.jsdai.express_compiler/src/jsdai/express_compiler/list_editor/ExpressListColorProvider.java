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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 *  color provider for express editor
 */
public class ExpressListColorProvider {

	// may want light gray
	public static final RGB BACKGROUND = new RGB(255, 255, 255);

//	public static final RGB MULTI_LINE_COMMENT = new RGB(64, 128, 128);
	public static final RGB SINGLE_LINE_COMMENT = new RGB(64, 128, 128);
	
	public static final RGB DEFAULT = new RGB(0, 0, 0);
//	public static final RGB KEYWORD = new RGB(127, 0, 85);
//	public static final RGB TYPE = new RGB(64, 0, 200);
//	public static final RGB OPERATOR = new RGB(200, 0, 0);
//	public static final RGB BUILTIN = new RGB(64, 64, 200);
//	public static final RGB STRING = new RGB(0, 0, 255);

	protected Map fColorTable= new HashMap(10);

	/**
	 * Release all the colour resources
	 */	
	public void dispose() {
		Iterator e= fColorTable.values().iterator();
		while (e.hasNext())
			 ((Color) e.next()).dispose();
	}
	
	/**
	 * Return the color that is stored in the color table under the given RGB
	 * value.
	 * 
	 * @param rgb the RGB value
	 * @return the color stored in the color table for the given RGB value
	 */
	public Color getColor(RGB rgb) {
		Color color= (Color) fColorTable.get(rgb);
		if (color == null) {
			color= new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}



}


