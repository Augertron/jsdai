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

import java.util.Hashtable;

import jsdai.express_g.SdaieditPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Mantas Balnys
 *
 */
public final class Resources {
	public static final int ENTITY = 0;
	public static final int ENTITY2 = 1;
	public static final int INTERFACED = 2;
	public static final int INTERFACED2 = 3;
	public static final int SCHEMA = 4;
	public static final int SCHEMA2 = 5;
	public static final int DEFINED = 6;
	public static final int DEFINED2 = 7;
	public static final int ENUMERATION = 8;
	public static final int ENUMERATION2 = 9;
	public static final int SELECTION = 10;
	public static final int SELECTION2 = 11;
	public static final int ATTRIBUTE = 12;
	public static final int ATTRIBUTE2 = 13;
	public static final int INHERITANCE = 14;
	public static final int SIMPLE = 15;
	public static final int MOUSE = 16;
	public static final int JSDAI = 17;
	public static final int MODEL_SCHEMA = 18;
	public static final int MODEL_LAYOUT = 19;
	public static final int MODEL_OTHER = 20;
	public static final int CONSTRAINT = 21;
	public static final int CONSTRAINT2 = 22;
	public static final int MODEL_LAYOUT_C = 23;
	public static final int DELETE = 24;
	public static final int ADD = 25;
	public static final int MOVE = 26;
	public static final int MODEL_LAYOUT_P = 27;
	public static final int MODEL_LAYOUT_X = 28;
	public static final int ERROR_OVERLAY = 29;
	public static final int WARNING_OVERLAY = 30;
	public static final int LETTER_C8 = 31;
	public static final int LETTER_P8 = 32;
	public static final int LETTER_L8 = 33;
	public static final int LETTER_S8 = 34;
	public static final int SCHEMA_CURRENT = 35;
	public static final int EXG_FILE = 36;
	public static final int GENERATE_XML = 37;
	
	private static final String[] files = {"entity.gif", "entity_r.gif", "entity_ref.gif", "entity_ref_r.gif",
			"schema.gif", "schema_r.gif", "defined.gif", "defined_r.gif", "enumeration.gif", "enumeration_r.gif",
			"select.gif", "select_r.gif", "relation.gif", "relation_r.gif", "inheritance.gif", "simple.gif",
			"mouse.GIF", "jsdai_icon_16.png", "model_schema.gif", "diagram_blank.gif", "model.gif",
			"constraint.gif", "constraint_r.gif", "jsdai_schema_C.png", "delete_obj.gif", "add_obj.gif", 
			"move.gif", "jsdai_schema_P.png", "jsdai_schema_X.png", "error_co.gif", "warning_co.gif",
			"diagram_c.gif", "diagram_p.gif", "diagram_l.gif", "diagram_s.gif", "schema_current.gif", "exg_file.gif", "generate_xml.gif"}; 
	private static final int TOTAL = files.length;
	
	private static Hashtable imageDescriptors = new Hashtable(); 
	private static Hashtable images = new Hashtable(); 

	/**
	 * creates and returns image descriptor for given type
	 * @param type
	 * @return
	 */
	public static final ImageDescriptor getImageDescriptor(int type) {
		Integer index = new Integer(type);
		ImageDescriptor imageDescriptor = (ImageDescriptor)imageDescriptors.get(index);
		if (imageDescriptor == null) {
//System.out.println("NEW DESCRIPTOR: " + index);
			imageDescriptor = SdaieditPlugin.imageDescriptorFromPlugin(SdaieditPlugin.ID_SDAIEDIT, "icons/" + files[type]);
//			imageDescriptor = ImageDescriptor.createFromFile(Resources.class, files[type]);
			imageDescriptors.put(index, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * creates and returns image for given type
	 * (uses getImageDescriptor)
	 * @param type
	 * @return
	 */
	public static final Image getImage(int type) {
		Integer index = new Integer(type);
		Image image = (Image)images.get(index);
		if (image == null || image.isDisposed()) {
//System.out.println("NEW IMAGE: " + index);			
			image = getImageDescriptor(type).createImage();
			images.put(index, image);
		}
		return image;
	}

	/**
	 * creates and returns image descriptor for multiple image types
	 * @param types
	 * @return
	 */
	public static final ImageDescriptor getImageDescriptor(int[] types) {
		Object index = getIndex(types);
		ImageDescriptor imageDescriptor = (ImageDescriptor)imageDescriptors.get(index);
		if (imageDescriptor == null) {
//System.out.println("NEW DESCRIPTOR: " + index);			
			MultiImageDescriptor desc = new MultiImageDescriptor();
			for (int i = 0; i < types.length; i++) {
				desc.addImageData(getImageDescriptor(types[i]).getImageData());
			}
			imageDescriptor = desc;
			imageDescriptors.put(index, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * creates and returns image for multiple image types
	 * (uses getImageDescriptor)
	 * @param types
	 * @return
	 */
	public static final Image getImage(int[] types) {
		Object index = getIndex(types);
		Image image = (Image)images.get(index);
		if (image == null || image.isDisposed()) {
//System.out.println("NEW IMAGE: " + index);			
			image = getImageDescriptor(types).createImage();
			images.put(index, image);
		}
		return image;
	}
	
	private static final Object getIndex(int[] types) {
		long index = 0;
		for (int i = 0; i < types.length; i++) index = index * TOTAL + types[i];
		return new Long(index);
	}
	
	private static final Object getIndex(int[][] types) {
		long index = 0;
		for (int i = 0; i < types.length; i++) 
			index = (index * (TOTAL + 1) << 8) + types[i][0] << 8 + (types[i][1] % 16) << 4 + types[i][2] % 16;
		return new Long(index);
	}
	
	/**
	 * creates and returns image descriptor for multiple image types
	 * @param types array of 3 integer types: 0 - image type, 1 - x placement, 2 - y placement 
	 * @return
	 */
	public static final ImageDescriptor getImageDescriptor(int[][] types) {
		Object index = getIndex(types);
		ImageDescriptor imageDescriptor = (ImageDescriptor)imageDescriptors.get(index);
		if (imageDescriptor == null) {
//System.out.println("NEW DESCRIPTOR: " + index);			
			MultiImageDescriptor desc = new MultiImageDescriptor();
			for (int i = 0; i < types.length; i++) {
				desc.addImageData(getImageDescriptor(types[i][0]).getImageData(), types[i][1], types[i][2]);
			}
			imageDescriptor = desc;
			imageDescriptors.put(index, imageDescriptor);
		}
		return imageDescriptor;
	}
	
	/**
	 * creates and returns image for multiple image types
	 * (uses getImageDescriptor)
	 * @param types array of 3 integer types: 0 - image type, 1 - x placement, 2 - y placement 
	 * @return
	 */
	public static final Image getImage(int[][] types) {
		Object index = getIndex(types);
		Image image = (Image)images.get(index);
		if (image == null || image.isDisposed()) {
//System.out.println("NEW IMAGE: " + index);			
			image = getImageDescriptor(types).createImage();
			images.put(index, image);
		}
		return image;
	}
	
	/**
	 * Complex image types
	 */
	
	private static final int y1 = 0;
	private static final int y2 = 8;
	private static final int x1 = 9;
	private static final int x2 = 9;
	
	
	public static final int[][] MODEL_LAYOUT_COMPLETE_SHORT = new int[][]{{Resources.MODEL_LAYOUT, 0, 0},
		{Resources.LETTER_C8, x1, y1}, {Resources.LETTER_S8, x2, y2}};
	
	public static final int[][] MODEL_LAYOUT_PARTIAL_SHORT = new int[][]{{Resources.MODEL_LAYOUT, 0, 0},
		{Resources.LETTER_P8, x1, y1}, {Resources.LETTER_S8, x2, y2}};

	public static final int[][] MODEL_LAYOUT_COMPLETE_LONG = new int[][]{{Resources.MODEL_LAYOUT, 0, 0},
		{Resources.LETTER_C8, x1, y1}, {Resources.LETTER_L8, x2, y2}};

	public static final int[][] MODEL_LAYOUT_PARTIAL_LONG = new int[][]{{Resources.MODEL_LAYOUT, 0, 0},
		{Resources.LETTER_P8, x1, y1}, {Resources.LETTER_L8, x2, y2}};
	
}
