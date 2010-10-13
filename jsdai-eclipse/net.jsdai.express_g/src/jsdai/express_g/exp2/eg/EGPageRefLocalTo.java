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

package jsdai.express_g.exp2.eg;

import java.util.Iterator;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class EGPageRefLocalTo extends EGPageRefLocal {
	private EGPageRefLocalFrom linkedRef = null;

	/**
	 * @param prop
	 * @param referenced
	 */
	public EGPageRefLocalTo(PropertySharing prop, AbstractEGBox referenced) {
		super(prop, referenced);
	}

	/**
	 * 
	 */
	protected EGPageRefLocalTo() {
		super();
	}

	public String getText() {
		String text = "";
		if (referenced == null) 
			text += -1; 
		else {
			text += EGToolKit.convertPageNrToString(EGToolKit.renumber(prop(), referenced.getPage())); 
//System.out.print("<EGPageRefLocalTo> referenced.getPage(): " + referenced.getPage() + ", EGToolKit.renumber(prop(), referenced.getPage()): " + EGToolKit.renumber(prop(), referenced.getPage()) + ", text: " + text);
		}
		text += ", " + getReferencedID() + " ";
		if (referenced == null) text += "<null>"; else text += referenced.getName();
//System.out.println(", text: " + text);
	     return text;
	    //return "X3";
		  // this one - new VisualPage based implementation gives 1 instead of N (N=0)
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		int pg = -1;
		if (referenced != null) pg = referenced.getPage();
		String text = null;
		if (pg > 0) {
			Rectangle bounds = getBounds();
			bounds.x -= startat.x;
			bounds.y -= startat.y;
			int dd = bounds.height / 3;
			int x1 = bounds.x;
			int x2 = bounds.x + dd;
			int x3 = bounds.x + bounds.width - dd;
			int x4 = bounds.x + bounds.width;
			int y1 = bounds.y;
			int y2 = bounds.y + bounds.height / 2;
			int y3 = bounds.y + bounds.height;
			text = "<img.area shape=\"poly\" coords=\"" 
				+ x1 + "," + y2 + "," + x2 + "," + y3 + "," + x3 + "," + y3 + "," + x4 + "," + y2 + "," 
					+ x3 + "," + y1 + "," + x2 + "," + y1 + "," + x1 + "," + y2  
				+ "\" href=\"./" + schema_ext.toLowerCase() + "expg" + pg + ".xml"
				+ "\" />";
		}
		return text;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#eliminate()
	 */
	public void eliminate() {
		if (this.referenced != null) {
			this.referenced.removeLabelListener(this);
			this.referenced.removePageChangeListener(this);
		}
		if (linkedRef != null)
			linkedRef.removeLinkedRef(this);
		super.eliminate();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.EGPageRef#setReferenced(jsdai.express_g.exp2.eg.AbstractEGBox)
	 */
	public void setReferenced(AbstractEGBox referenced) {
		if (this.referenced != referenced) {
			if (this.referenced != null) {
				this.referenced.removeLabelListener(this);
				this.referenced.removePageChangeListener(this);
			}
			referenced.addLabelListener(this);
			referenced.addPageChangeListener(this);
			super.setReferenced(referenced);
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
	 */
	public boolean canBeInvisible() {
		return (referenced != null)&&(referenced.canBeInvisible());
	}
	
	void setLinkedRef(EGPageRefLocalFrom linked) {
		linkedRef = linked;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.EGPageRef#setDataFrom(jsdai.express_g.exp2.eg.EGPageRef)
	 */
	protected void setDataFrom(EGPageRef other) {
		super.setDataFrom(other);
		((EGPageRefLocalTo)other).linkedRef.addLinkedRef(this);
	}
	
	public boolean addReferencedRelation(AbstractEGRelation relation) {
		AbstractEGBox box = relation.getParent();
		boolean ok = false;
		if (can_be_replaced_by_this(box, relation.getType())) {
			relation.setParent(this);
			ok = true;
		} else {
			Iterator childs = relation.getChilds().iterator();
			while (!ok && (childs.hasNext())) {
				box = (AbstractEGBox)childs.next();
				if (can_be_replaced_by_this(box, relation.getType())) {
					relation.removeChild(box);
					relation.addChild(this);
					ok = true;
				}
			}
		}
		if ((ok)&&(box instanceof EGPageRefLocalTo)) {
			if (linkedRef != null) // UPGRADE added 2005-11-07 because of some crashes in file opening, not tested 
				linkedRef.removeLinkedRef((EGPageRefLocalTo)box);
		}
		return ok;
	}
}
