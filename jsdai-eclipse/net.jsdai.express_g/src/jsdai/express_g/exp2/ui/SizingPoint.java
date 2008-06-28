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

package jsdai.express_g.exp2.ui;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class SizingPoint extends AbstractDraw {
  protected AbstractDraw sizingItem;
  protected int place;

  public static final int PLACE_TOP_RIGHT = 0;
  public static final int PLACE_TOP_LEFT = 1;
  public static final int PLACE_BOTTOM_LEFT = 2;
  public static final int PLACE_BOTTOM_RIGHT = 3;
  public static final int PLACE_RELATION_HINT = 4;
  public static final int PLACE_RELATION_TEXT = 5;

  public static final int pointSize = 7;

  public SizingPoint(AbstractDraw item, int place) {
    simpleSchema.foreground = ColorSchema.COLOR_LIGHT_GREEN;
    selectedSchema.foreground = ColorSchema.COLOR_LIGHT_RED;
    selectedSchema.background = selectedSchema.foreground;
    sizingItem = item;
    this.place = place;
    setSize(new Point(pointSize, pointSize));
  }

  public void update(int nr) {
    Point loc = getLocation();
    switch (place) {
      case PLACE_TOP_RIGHT :
        loc.x = sizingItem.getBounds().x + sizingItem.getBounds().width;
        loc.y = sizingItem.getBounds().y - pointSize;
        break;
      case PLACE_TOP_LEFT :
        loc.x = sizingItem.getBounds().x  - pointSize;
        loc.y = sizingItem.getBounds().y - pointSize;
        break;
      case PLACE_BOTTOM_LEFT :
        loc.x = sizingItem.getBounds().x - pointSize;
        loc.y = sizingItem.getBounds().y + sizingItem.getBounds().height;
        break;
      case PLACE_BOTTOM_RIGHT :
        loc.x = sizingItem.getBounds().x + sizingItem.getBounds().width;
        loc.y = sizingItem.getBounds().y + sizingItem.getBounds().height;
        break;
      case PLACE_RELATION_HINT :
        Point ph = ((AbstractEGRelation)sizingItem).getHintPoint();
        loc.x = ph.x - pointSize/2;
        loc.y = ph.y - pointSize/2;
        break;
      case PLACE_RELATION_TEXT :
        Point pl = ((AbstractEGRelation)sizingItem).getNamePlace();
        loc.x = pl.x - pointSize/2;
        loc.y = pl.y - pointSize/2;
        break;
    }
    setLocation(loc);
//    if (!EGToolKit.pagesIdentical(getPage(), sizingItem.getPage())) setPage(sizingItem.getPage());
  }

  	/**
  	 * grazina krypti is tasko 1 i taska 2
  	 * kryptis apibreziama kaip matematinis koordinaciu ketvirtis, kuriame yra taskas 2 
  	 * (taskas 1 laikomas koordinaciu centru)
  	 * @param p1
  	 * @param p2
  	 * @return
  	 *
  	private int direction(Point p1, Point p2) {
  		int direction;
  		if (p1.x < p2.x) {
  			if (p1.y < p2.y) {
  				direction = 1;
  			} else {
  				direction = 4;
  			}
  		} else {
  			if (p1.y < p2.y) {
  				direction = 2;
  			} else {
  				direction = 3;
  			}
  		}
  		return direction;
  	}
  
  	/**
  	 * draw sizing point
  	 * @param g
  	 * @see jsdai.express_g.exp2.Drawable#draw(org.eclipse.swt.graphics.GC)
  	 */
  public void draw(GC g) {
    selectedSchema.apply(g);
    Rectangle bounds = getBounds();
  	if (place == PLACE_RELATION_TEXT) {
  		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
  		g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);
  	} else {
  		g.fillRectangle(bounds);
  		g.drawRectangle(bounds);
  	}
    // hint lines for relation placement hint
  	/*/
  	if ((place == PLACE_RELATION_HINT)&&(sizingItem instanceof EGRelationSimple)) {
  		EGRelationSimple rel = (EGRelationSimple)sizingItem;
  	    simpleSchema.apply(g);
  	    Point hint = rel.getHintPoint();
  	    Point hw = rel.getHint1();
  	    g.drawOval(hint.x - pointSize/2, hint.y - pointSize/2, pointSize, pointSize);
  	    g.drawOval(hint.x - pointSize/2 - hw.x, hint.y - pointSize/2 - hw.y, pointSize, pointSize);
  	    AbstractEGBox parent = rel.getParent();
  	    AbstractEGBox child = rel.getChild();
  	    Point centerP = parent.getCenterPoint();
  	    Point centerC = child.getCenterPoint();
  	    Rectangle boundsP = parent.getBounds();
  	    Rectangle boundsC = child.getBounds();
  	    Point r1 = new Point(boundsP.x, boundsP.y);
  	    Point r2 = new Point(boundsC.x, boundsC.y);
  	    switch (direction(centerP, centerC)) {
  	    	case 1 :
  	    		r1.x += boundsP.width;
  	    		r1.y += boundsP.height;
  	    		break;
  	    	case 2 :
  	    		r1.y += boundsP.height;
  	    		r2.x += boundsC.width;
  	    		break;
  	    	case 3 :
  	    		r2.x += boundsC.width;
  	    		r2.y += boundsC.height;
  	    		break;
  	    	case 4 :
  	    		r1.x += boundsP.width;
  	    		r2.y += boundsC.height;
  	    		break;
  	    }
  	    
  	    g.drawLine(Math.min(boundsP.x, boundsC.x), r1.y, Math.max(boundsP.x + boundsP.width, boundsC.x + boundsC.width), r1.y);
  	    g.drawLine(Math.min(boundsP.x, boundsC.x), r2.y, Math.max(boundsP.x + boundsP.width, boundsC.x + boundsC.width), r2.y);
  	    g.drawLine(r1.x, Math.min(boundsP.y, boundsC.y), r1.x, Math.max(boundsP.y + boundsP.height, boundsC.y + boundsC.height));
  	    g.drawLine(r2.x, Math.min(boundsP.y, boundsC.y), r2.x, Math.max(boundsP.y + boundsP.height, boundsC.y + boundsC.height));
  	    g.drawLine(r1.x, r1.y, r2.x, r2.y);
  	}
  	    /**/
  }

  public void translateSizing(Point loc) {
    translate(loc);
    Rectangle rect = sizingItem.getBounds();
    Rectangle bounds = getBounds();
    switch (place) {
      case PLACE_TOP_RIGHT :
        rect.width = bounds.x - rect.x;
        rect.height = rect.y + rect.height - bounds.y - pointSize;
        rect.y = bounds.y + pointSize;
        sizingItem.setBounds(rect);
        bounds = sizingItem.getBounds();
        if (bounds.height != rect.height) {
        	Point locat = new Point(bounds.x, bounds.y + rect.height - bounds.height);
        	sizingItem.setLocation(locat);
        }
        break;
      case PLACE_TOP_LEFT :
        rect.width += rect.x - bounds.x - pointSize;
        rect.height += rect.y - bounds.y - pointSize;
        rect.x = bounds.x + pointSize;
        rect.y = bounds.y + pointSize;
        sizingItem.setBounds(rect);
        bounds = sizingItem.getBounds();
        if ((bounds.width != rect.width)||(bounds.height != rect.height)) {
        	Point locat = new Point(bounds.x + rect.width - bounds.width, bounds.y + rect.height - bounds.height);
        	sizingItem.setLocation(locat);
        }
        break;
      case PLACE_BOTTOM_LEFT :
        rect.width += rect.x - bounds.x - pointSize;
        rect.height = bounds.y - rect.y;
        rect.x = bounds.x + pointSize;
        sizingItem.setBounds(rect);
        bounds = sizingItem.getBounds();
        if (bounds.width != rect.width) {
        	Point locat = new Point(bounds.x + rect.width - bounds.width, bounds.y);
        	sizingItem.setLocation(locat);
        }
        break;
      case PLACE_BOTTOM_RIGHT :
        rect.width = bounds.x - rect.x;
        rect.height = bounds.y - rect.y;
        sizingItem.setBounds(rect);
        break;
      case PLACE_RELATION_HINT :
        Point ph = getLocation();
        ph.x += pointSize/2;
        ph.y += pointSize/2;
        ((AbstractEGRelation)sizingItem).setHintPoint(ph);
        break;
      case PLACE_RELATION_TEXT :
        Point pl = getLocation();
        pl.x += pointSize/2;
        pl.y += pointSize/2;
        ((AbstractEGRelation)sizingItem).setNamePlace(pl);
        break;
    }
    if (sizingItem instanceof AbstractEGObject)
      ((AbstractEGObject)sizingItem).setLabelNew(false);
  }

}
