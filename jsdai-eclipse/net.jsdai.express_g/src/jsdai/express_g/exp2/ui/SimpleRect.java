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

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class SimpleRect extends AbstractDraw {
  public SimpleRect() {
    simpleSchema.foreground = ColorSchema.COLOR_LIGHT_GRAY;
    setPage(ANY_PAGE);
  }

  public SimpleRect(Rectangle bounds) {
  	this();
  	setBounds(bounds);
  }

  public SimpleRect(int x, int y, int width, int height) {
  	this(new Rectangle(x, y, width, height));
  }
  
  public void update(int nr) {  }

  public void draw(GC g) {
    Rectangle bounds = getBounds();
    ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
    schema.apply(g);
    g.drawRectangle(bounds);
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
