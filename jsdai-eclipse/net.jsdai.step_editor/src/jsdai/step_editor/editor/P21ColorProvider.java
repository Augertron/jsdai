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

import java.util.*;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;

/**
 * Manager for colors used in the Java editor
 */
public class P21ColorProvider {

	private IPreferenceStore fPreferences;

	public static final RGB MULTI_LINE_COMMENT= new RGB(100, 100, 100);
//	public static final RGB SINGLE_LINE_COMMENT= new RGB(128, 128, 0);
	public static final RGB KEYWORD1 = new RGB(0, 0, 128);
	public static final RGB KEYWORD2 = new RGB(0, 128, 0);
	public static final RGB KEYWORD3 = new RGB(128, 128, 0);
	public static final RGB STRING = new RGB(0, 128, 128);
	public static final RGB INSTANCE = new RGB(128, 0, 0);
	public static final RGB DELIMETER = new RGB(255, 0, 0);
		public static final RGB CONSTANT = new RGB(0, 0, 255);
	
	public static final RGB DEFAULT = new RGB(0, 0, 0);

	protected Map fColorTable= new HashMap(10);

	/**
	 * Release all of the color resources held onto by the receiver.
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
	protected RGB getColorPreference(String color_category)
	{
		if (fPreferences == null) {
			//		fPreferences = ExpressCompilerPlugin.getDefault().getPreferenceStore();
      fPreferences = EditorsPlugin.getDefault().getPreferenceStore();
		}

		String rgb_string = fPreferences.getString(color_category);

		if (rgb_string.length() <= 0)
		{
			rgb_string = fPreferences.getDefaultString(color_category);
			if(rgb_string.length() <= 0) 
			{
				rgb_string = "0,0,0";
			}
		}
		return StringConverter.asRGB(rgb_string);
	}



}
