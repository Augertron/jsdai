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

package jsdai.express_g.editors.outline;

import java.util.Iterator;

import org.eclipse.swt.graphics.Image;

import jsdai.express_g.common.Resources;
import jsdai.express_g.common.TableTreeObject;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGConstraint;
import jsdai.express_g.exp2.eg.EGDefined;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGEnumerated;
import jsdai.express_g.exp2.eg.EGPage;
import jsdai.express_g.exp2.eg.EGSchema;
import jsdai.express_g.exp2.eg.EGSelect;
import jsdai.express_g.exp2.eg.EGSimple;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class EGEOTreeItem extends TableTreeObject implements ISorterName {
	protected String sortName = null;

	private static final int ICON_TYPE_UNDEFINED = -2;
	private static final int ICON_TYPE_UNAVAILABLE = -1;
	private int icon_type = ICON_TYPE_UNDEFINED;
	
	final static String[] COLUMNS = {"", "name", "page-def", "page-ref"};
	public final static int INDEX_NAME = 1;
	public final static int INDEX_ICON = 1;
	public final static int INDEX_PGDEF = 2;
	public final static int INDEX_PGREF = 3;

	protected PropertySharing prop;
	
	/**
	 * @param userObject
	 */
	public EGEOTreeItem(PropertySharing prop) {
		super();
		this.prop = prop;
	}

	/**
	 * @param userObject
	 */
	public EGEOTreeItem(PropertySharing prop, Object userObject) {
		super(userObject);
		this.prop = prop;
	}

	/**
	 * @param userObject
	 * @param parent
	 */
	public EGEOTreeItem(PropertySharing prop, Object userObject, TableTreeObject parent) {
		super(userObject, parent);
		this.prop = prop;
	}
	
	/**
	 * 
	 * @param parent
	 * @param name
	 * @param sortName
	 * @param icon
	 */
	public EGEOTreeItem(PropertySharing prop, TableTreeObject parent, String name, String sortName, Image icon) {
		super(null, parent);
		this.prop = prop;
		setText(name);
		setImage(icon);
		setSortName(sortName);
	}

	/**
	 * 
	 * @param parent
	 * @param name
	 * @param sortName
	 * @param icon_type
	 */
	public EGEOTreeItem(PropertySharing prop, TableTreeObject parent, String name, String sortName, int icon_type) {
		super(null, parent);
		this.prop = prop;
		setText(name);
		setSortName(sortName);
		this.icon_type = icon_type;
	}
	
	public String getText(int col) {
		String text = "";
		Object uo = getUserObject();
		if (uo instanceof AbstractEGObject) {
			AbstractEGObject object = (AbstractEGObject)uo;
			switch (col) {
				case INDEX_NAME :
					text = object.getName();
					break;
				case INDEX_PGDEF :
					text = String.valueOf(object.getPage());
					break;
				case INDEX_PGREF :
					break;
			}
		} else text = super.getText(col);
		return text;
	}
	
	public void setImage(Image image) {
		setImage(image, INDEX_ICON);
	}
	
	public Image getImage() {
		return getImage(INDEX_ICON);
	}
	
	public String getText() {
		String text = getText(INDEX_NAME);
		String pgDef = getText(INDEX_PGDEF);
		if (!pgDef.equals("") && !pgDef.equals("0")) {
			text += " (" + pgDef + ")";
		}
		String pgRef = getText(INDEX_PGREF);
		if (!pgRef.equals("")) {
			text += " {" + pgRef + "}";
		}
		return text;
	}
	
	public Image getImage(int col) {
		Image image = super.getImage(col);
		if (image != null) return image;
		
		image = getOverlayedImage();
		return image;
	}
	
	/**
	 * get visible error state for this tree node
	 * returns true if item is not placed yet or any of its children has error state true 
	 * @return
	 */
	public boolean getErrorState() {
		Object object = getUserObject();
		boolean error = false;
		if (object instanceof Paging) {
			error = ((Paging)object).getPage() == Paging.INVISIBLE_PAGE;
		    if (object instanceof EGEntityRef) {
		    	error = !error;
		    }
		    /**	2005-09-08 changed error state to all connected wires not only attributes
		     *  that means all subsupertypes, users of entity and entity attributes
		     */   
		    else if (!error && object instanceof AbstractEGBox) {
		    	Iterator wit = ((AbstractEGBox)object).getWires().iterator();
		    	while (!error && wit.hasNext()) {
		    		Wire wire = (Wire)wit.next();
		    		error = wire.getRelation().getPage() == Paging.INVISIBLE_PAGE;
		    	}		    	
		    }
		}
 		if ((!error) && hasChildren()) { // check on children
			Object[] children = getChildren();
			for (int i = 0; (i < children.length) && !error; i++) {
				if (children[i] instanceof EGEOTreeItem)
					error = ((EGEOTreeItem)children[i]).getErrorState(); 
			}
		}
		return error;
	}
	
	/**
	 * creates an overlayed image depending on error state and icon type
	 * @return
	 */
	private Image getOverlayedImage() {
		Image image = null;
		int type = getIconType();
		boolean error = getErrorState();
		if (type >= 0) {
			if (error) {
				int err_type;
				if ((prop.getEditMode() & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0)
					err_type = Resources.WARNING_OVERLAY;
				else
					err_type = Resources.ERROR_OVERLAY;
		    	image = Resources.getImage(new int[][]{
		    			{type, 0, 0},
						{err_type, 0, 8}});
			} else {
				image = Resources.getImage(type);
			}
		}
		return image;
	}
	
	/**
	 * i
	 * @return
	 */
	protected int getIconType() {
		if (icon_type == ICON_TYPE_UNDEFINED) {
			icon_type = ICON_TYPE_UNAVAILABLE;
			Object object = getUserObject();
		    if (object instanceof EGEntity) {
		    	icon_type = Resources.ENTITY;
		    } else
		    if (object instanceof EGDefined) {
		    	icon_type = Resources.DEFINED;
		    } else
		    if (object instanceof EGSchema) {
		    	icon_type = Resources.SCHEMA;
		    } else
		    if (object instanceof EGEntityRef) {
		    	icon_type = Resources.INTERFACED;
		    } else
		    if (object instanceof EGEnumerated) {
		    	icon_type = Resources.ENUMERATION;
		    } else
		    if (object instanceof EGSelect) {
		    	icon_type = Resources.SELECTION;
		    } else
		    if (object instanceof AbstractEGRelation) {
		    	icon_type = Resources.ATTRIBUTE;
		    } else
		    if (object instanceof EGConstraint) {
		    	icon_type = Resources.CONSTRAINT;
		    } else
		    if (object instanceof EGSimple) {

		    } else
		    if (object instanceof EGPage) {

		    }
		}
		return icon_type;
	}
	
	/**
	 * get non visible name for sorting
	 * @return
	 */
	public String getSortName() {
		String text = getText(INDEX_NAME);
		if (sortName != null) text = sortName + text;
		return text;
	}
	
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.common.TableTreeObject#setText(java.lang.String)
	 */
	public void setText(String text) {
		setText(text, INDEX_NAME);
	}
}
