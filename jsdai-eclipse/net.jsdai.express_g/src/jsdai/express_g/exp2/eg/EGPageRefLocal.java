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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.LabelListener;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;

/**
 * @author Mantas Balnys
 *
 */
public abstract class EGPageRefLocal extends EGPageRef implements LabelListener, PageListener {

	/**
	 * @param prop
	 * @param referenced
	 */
	public EGPageRefLocal(PropertySharing prop, AbstractEGBox referenced) {
		super(prop, referenced);
	}

	/**
	 * 
	 */
	protected EGPageRefLocal() {
		super();
	}

	/**
	 * draw
	 *
	 * @param g2 Graphics2D
	 */
	public void draw(GC g) {
  		Rectangle bounds = getBounds();
  		
  		if (isVisible()) {
  			ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
  			schema.apply(g);
  			g.setFont(prop().getFont1());
  	  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
  	  			wrapper.setGC(g);
  	  			if (isLabelNew()) {
  	  				setBounds(new Rectangle(bounds.x, bounds.y, 0, 0));
  	  			} else {
  	  	  			setBounds(bounds);
  	  			}
  	  			bounds = getBounds();
  	  		}
	
  			g.fillRoundRectangle(bounds.x, bounds.y, bounds.width, bounds.height, bounds.height - 1, bounds.height - 1);
  			g.drawRoundRectangle(bounds.x, bounds.y, bounds.width, bounds.height, bounds.height - 1, bounds.height - 1);

		    Rectangle textPlace = getTextPlace();
			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				g.drawString(wrapper.getLine(i), 
						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
			}
  		} else {
  			drawInvisibleBox(g);
  		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.LabelListener#labelChanged(java.lang.Object)
	 */
	public void labelChanged(Object invoker) {
		updateWrapper();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
	 */
	public void pageChanged(PageChangeEvent e) {
//System.out.println("<0XO><14>pageChanged: " + e);
		updateWrapper();
	}
	
	/**
	 * getUIName
	 *
	 * @return String
	 */
	public String getUIName() {
		return "Off-page connector";
	}
	
	/**
	 * gets referenced object unique ID
	 * @return
	 */
	public int getReferencedID() {
		if (referenced == null)
			return -1;
		else
			return referenced.getID();
	}

	/**
	 * getTextPlace
	 *
	 * @return Rectangle
	 */
	public Rectangle getTextInsets() {
		Rectangle place = super.getTextInsets();
	    Rectangle bounds = getBounds();
	    place.x += bounds.height / 4;
	    place.width += bounds.height / 2;
	    return place;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Named#getName()
	 */
	public String getName() {
		String text = "link to ";
		if (referenced != null) text += referenced.getName();
		else text += "<null>";
		return text;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGBox#getPosition(jsdai.express_g.exp2.eg.AbstractEGRelation, boolean)
	 */
	public Point getPosition(AbstractEGRelation relation, boolean second) {
		Point pos = super.getPosition(relation, second);
		Point center = getCenterPoint();
		int direction = getDirection(relation, second);
		switch (direction) {
			case (Wire.DIRECTION_BOTTOM) :
			case (Wire.DIRECTION_TOP) :
				pos.y = center.y;
				break;
			case (Wire.DIRECTION_LEFT) :
			case (Wire.DIRECTION_RIGHT) :
				pos.x = center.x;
				break;
		}
		return pos;
	}
	
	
	Object referenceKey = null;
	
	public Object getBundleReferenceKey() {
		if (referenceKey == null) referenceKey = new RefKey();
		return referenceKey;
	}
	
	private class RefKey {
		public boolean equals(Object obj) {
			return obj instanceof RefKey && referenced == ((RefKey)obj).getReferenced() 
				&& (getRelType() == -1 || ((RefKey)obj).getRelType() == -1 
						|| getRelType() == ((RefKey)obj).getRelType());
		}
		
		public int hashCode() {
			if (referenced == null)
				return 0;
			else
				return referenced.hashCode();
		}
		
		public AbstractEGBox getReferenced() {
			return referenced;
		}
		
		public int getRelType() {
			int type = -1;
			Object rel = getFirstRelation();
			if (rel instanceof AbstractEGRelation) {
				type = ((AbstractEGRelation)rel).getType(); 
			}
			return type;
		}
	}

	protected boolean can_be_replaced_by_this(AbstractEGBox box, int rel_type) {
		if (box == this) return false;
		boolean type_ok = true; 
		Object rel = getFirstRelation();
		if (rel instanceof AbstractEGRelation) {
			type_ok = ((AbstractEGRelation)rel).getType() == rel_type;
		}
		return ((box == referenced) || 
			((box instanceof EGPageRef) && (((EGPageRef)box).getReferenced() == referenced)))
			&& type_ok
			/*&& (getConcreteValue() == box.getConcreteValue())*/;
	}
}
