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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * @author Mantas Balnys
 *
 */
public class MultiImageDescriptor extends CompositeImageDescriptor {
	private ArrayList images = new ArrayList(1);
	private Point size = new Point(0, 0);

	/**
	 * 
	 */
	public MultiImageDescriptor() {
		super();
	}

	public void addImageData(ImageData imageData) {
		addImageData(imageData, 0, 0);
	}

	public void addImageData(ImageData imageData, int x, int y) {
		images.add(new ImageDataObject(imageData, x, y));
		size.x = Math.max(size.x, x + imageData.width);
		size.y = Math.max(size.y, y + imageData.height);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#drawCompositeImage(int, int)
	 */
	protected void drawCompositeImage(int width, int height) {
		Iterator iter = images.iterator();
		while (iter.hasNext()) {
			ImageDataObject img = (ImageDataObject)iter.next();
			drawImage(img.imageData, img.x, img.y);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor#getSize()
	 */
	protected Point getSize() {
		return size;
	}

	private static class ImageDataObject {
		public ImageData imageData;
		public int x;
		public int y;
		
		/**
		 * @param imageData
		 * @param x
		 * @param y
		 */
		public ImageDataObject(ImageData imageData, int x, int y) {
			this.imageData = imageData;
			this.x = x;
			this.y = y;
		}
	}
}
