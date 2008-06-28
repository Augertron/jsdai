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

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.Printable;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class EGPageFooter extends AbstractDraw implements Printable {
  public static final int DEFAULT_HEIGHT = 15;
  protected PropertySharing prop;

  /**
   * 
   */
  public EGPageFooter(PropertySharing prop) {
    this.prop = prop;
    setPage(ANY_PAGE);
    simpleSchema.foreground = ColorSchema.COLOR_LIGHT_GRAY;
    simpleSchema.lineStyle = ColorSchema.DASHED_LINE;
    selectedSchema.foreground = ColorSchema.COLOR_BLACK;
    selectedSchema.background = ColorSchema.COLOR_WHITE;
  }
  
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.eg.AbstractEGObject#getText()
   */
  public String getName() {
    int pgNr = prop.handler().getPage();
    String text = "Figure " + pgNr + " - EXPRESS-G diagram of the " + 
    	prop.handler().getVisualPage(pgNr).getName() + " - " + ((Named)prop).getName() + 
    	" schema (" + pgNr + " of " + prop.handler().getMaxPage() + ")";
    return text;
  }
  
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.Updateable#update(int)
   */
  public void update(int nr) {
  }
  
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.Drawable#draw(java.awt.Graphics2D)
   */
  public void draw(GC g) {
    ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
    schema.apply(g);
	g.setFont(prop.getFont1());
    Rectangle bounds = getBounds();
    String text = getName();
    Point textRect = g.stringExtent(text);
    g.drawString(text, bounds.x + (bounds.width - textRect.x)/2, bounds.y + (bounds.height - textRect.y)/2);
    if (!isSelected()) g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
  }
  
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.Printable#print(java.awt.Graphics2D)
   */
  public void print(GC g) {
    setSelected(true);
    draw(g);
    setSelected(false);
  }

  /**
   * objectAt
   *
   * @param p Point
   * @return boolean
   */
  public boolean objectAt(Point p) {
    return false;
  }

  /**
   * objectAt
   *
   * @param r Rectangle
   * @return boolean
   */
  public boolean objectAt(Rectangle r) {
    return false;
  }
}
