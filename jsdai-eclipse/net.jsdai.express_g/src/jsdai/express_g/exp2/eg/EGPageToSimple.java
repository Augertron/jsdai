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

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @(#) EGSimple.java
 * @deprecated
 */

public class EGPageToSimple extends EGPageTo implements IPageOverrider {

  public EGPageToSimple(PropertySharing prop, EGPageFrom pageTo) {
  	super(prop, pageTo);
  }

  public EGPageToSimple(PropertySharing prop, EGPageFrom pageTo, Rectangle bounds) {
  	this(prop, pageTo);
    setBounds(bounds);
  }

  public EGPageToSimple(PropertySharing prop, EGPageFrom pageTo, Point location) {
  	this(prop, pageTo);
    setLocation(location);
  }

  /**
   * draw
   *
   * @param g2 Graphics2D
   * @todo Implement this jsdai.paint.Drawable method
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

		    g.fillRectangle(bounds);
		    g.drawRectangle(bounds);
		    
		    Rectangle textPlace = getTextPlace();
			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				g.drawString(wrapper.getLine(i), 
						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
			}
		    int x = bounds.x + bounds.width - bounds.width / 10;
		    g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
  		} else {
  			drawInvisibleBox(g);
  		}
//  	 XXX EG item marked for deletion
        g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_RED]);
        g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
        g.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);
  }

  public int getType() {
   	EGSimple simple = (EGSimple)getReferencedObject();
    return simple.getType();
  }

  public void setType(int type) {
   	EGSimple simple = (EGSimple)getReferencedObject();
   	simple.setType(type);
  }

  /**
   * getTextPlace
   *
   * @return Rectangle
   * @todo Implement this jsdai.paint.eg.AbstractEGBox method
   */
  public Rectangle getTextInsets() {
    Rectangle place = new Rectangle(textInset, textInset, 2*textInset, 2*textInset);
    Rectangle bounds = getBounds();
    place.width += bounds.width / 10;
    return place;
  }

  public boolean createEditDialog(Composite parent) {
   	EGSimple simple = (EGSimple)getReferencedObject();
   	simple.createEditDialog(parent);
   	dialog = simple.dialog;
    return isEditDialogCreated();
  }

  public int getType_width() {
   	EGSimple simple = (EGSimple)getReferencedObject();
    return simple.getType_width();
  }

  public boolean isType_width_fixed() {
   	EGSimple simple = (EGSimple)getReferencedObject();
    return simple.isType_width_fixed();
  }

  public void setType_width(int type_width) {
   	EGSimple simple = (EGSimple)getReferencedObject();
   	simple.setType_width(type_width);
  }

  public void setType_width_fixed(boolean type_width_fixed) {
   	EGSimple simple = (EGSimple)getReferencedObject();
   	simple.setType_width_fixed(type_width_fixed);
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Simple type";
  }
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
   */
  
  public boolean canBeInvisible() {
   	EGSimple simple = (EGSimple)getReferencedObject();
    return simple.canBeInvisible();
  }
  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#getText()
	 */
	public String getText() {
		return getName();
	}
}
