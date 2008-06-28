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

import org.eclipse.swt.graphics.Image;

/**
 * @author Mantas Balnys
 *
 */
public class TableTreeObject {
	public static final int DEFAULT_COLUMN = 1;
	protected Object userObject = null;
	protected TableTreeObject parent = null;
	protected ArrayList children = new ArrayList();
	protected ArrayList text = new ArrayList();
	protected ArrayList image = new ArrayList();

	/**
	 * 
	 */
	public TableTreeObject() {
		super();
	}

	/**
	 * 
	 */
	public TableTreeObject(TableTreeObject parent) {
		this();
		setParent(parent);
	}

	/**
	 * 
	 */
	public TableTreeObject(Object userObject) {
		this();
		setUserObject(userObject);
	}

	/**
	 * 
	 */
	public TableTreeObject(Object userObject, TableTreeObject parent) {
		this(parent);
		setUserObject(userObject);
	}

	public Object getParent() {
		return parent;
	}
	
	public Object[] getChildren() {
		return children.toArray();
	}
	
	public boolean hasChildren() {
		return !children.isEmpty();
	}
	
	public void addChild(TableTreeObject child) {
		child.setParent(this);
	}
	
	public void removeChild(TableTreeObject child) {
		child.setParent(null);
	}
	
	public void clearChildren() {
		Iterator iter = children.iterator();
		while (iter.hasNext())
			((TableTreeObject)iter.next()).parent = null;
		children.clear();
	}
	
	public void setParent(TableTreeObject parent) {
		if (this.parent != parent) {
			if (this.parent != null) {
				this.parent.children.remove(this);
			}
			this.parent = parent;
			if (this.parent != null) {
				this.parent.children.add(this);
			}
		}
	}
	
	public String getText() {
		return getText(DEFAULT_COLUMN);
	}

	public String getText(int col) {
		String txt = "";
		try {
			txt = (String)text.get(col);
		} catch (IndexOutOfBoundsException ex) {
			txt = "";
		}
		return txt;
	}
	
	public void setText() {
		this.text.clear();
	}
	
	public void setText(String text) {
		setText(text, DEFAULT_COLUMN);
	}
	
	public void setText(String text, int col) {
		while (col >= this.text.size()) this.text.add("");
		this.text.set(col, text);
	}
	
	public void setText(String[] text) {
		this.text.clear();
		this.text.ensureCapacity(text.length);
		for (int i = 0; i < text.length; i++)
			this.text.add(text[i]);
	}
	
	public Image getImage() {
		return getImage(DEFAULT_COLUMN);
	}
	
	public Image getImage(int col) {
		Image img = null;
		try {
			img = (Image)image.get(col);
		} catch (IndexOutOfBoundsException ex) {
			img = null;
		}
		return img;
	}
	
	public void setImage() {
		this.image.clear();
	}
	
	public void setImage(Image image) {
		setImage(image, DEFAULT_COLUMN);
	}
	
	public void setImage(Image image, int col) {
		while (col >= this.image.size()) this.image.add(null);
		this.image.set(col, image);
	}
	
	public void setImage(String[] image) {
		this.image.clear();
		this.image.ensureCapacity(image.length);
		for (int i = 0; i < image.length; i++)
			this.image.add(image[i]);
	}
	
	/**
	 * disposes this element and all child elements
	 *
	 */
	public void dispose() {
		Iterator iter = children.iterator();
		while (iter.hasNext()) 
			((TableTreeObject)iter.next()).dispose();
		
		// XXX unknown behavior 
		text.clear();
		image.clear();
		children.clear();
	}

	public Object getUserObject() {
		return userObject;
	}
	
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
}
