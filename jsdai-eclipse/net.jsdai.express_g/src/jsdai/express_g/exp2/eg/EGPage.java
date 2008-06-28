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

import jsdai.SExpress_g_schema.ELocation;
import jsdai.SExpress_g_schema.EObject_placement;
import jsdai.SExpress_g_schema.ESize;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @deprecated EGPage.java
 */
public abstract class EGPage extends AbstractEGBox {
  private AbstractEGBox referencedObject = null;

  public EGPage(PropertySharing prop, AbstractEGBox referencedObject) {
  	super(prop);
//    Dimension size = new Dimension();
//    size.setSize(basicWidth, basicHeight);
//    setSize(size);
    setReferencedObject(referencedObject);
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
		// XXX EG item marked for deletion
	      g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_RED]);
	      g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
	      g.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);
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

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EObject_placement page = (EObject_placement)getDefinition(modelDict, modelEG);
    ELocation loc = (ELocation)modelEG.createEntityInstance(ELocation.class);
    Point cloc = getLocation();
    loc.setX(null, cloc.x);
    loc.setY(null, cloc.y);
    page.setObject_location(null, loc);
    if (getVisible() != VISIBLE_UNSET)
  	  page.setVisible(null, isVisible());

    ESize size = (ESize)modelEG.createEntityInstance(ESize.class);
    Point csize;
    if (isLabelNew()) csize = new Point(0, 0);
    	else csize = getSize();
    size.setWidth(null, csize.x);
    size.setHeight(null, csize.y);
    page.setObject_size(null, size);

    setDefinitionPlacement(page);
  }

  public AbstractEGBox getReferencedObject() {
    return referencedObject;
  }

  public void setReferencedObject(AbstractEGBox referencedObject) {
    this.referencedObject = referencedObject;
	updateWrapper();
  }

  public String getName() {
  	if (referencedObject == null) return "null";
  	else return referencedObject.getName();
  }

  public abstract int getRefCount();

  public abstract AbstractEGRelation getRefRelation();

  	void updateWrapper() {
  		wrapper.setText(getText());
  	}
  
  	protected void updatePageRef() {
  		updateWrapper();
  	}
  	
  	
  /**
   * getPage
   *
   * @return int
   *
  public int getPage() {
    return getRefRelation().getPage();
  }
/**/
  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGBox#addWire(jsdai.express_g.exp2.eg.AbstractEGRelation)
	 */
	public void addWire(AbstractEGRelation relation) {
		super.addWire(relation);
	}
}
