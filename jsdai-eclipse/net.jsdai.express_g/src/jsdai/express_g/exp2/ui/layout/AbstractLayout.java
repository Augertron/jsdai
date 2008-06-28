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

package jsdai.express_g.exp2.ui.layout;

import java.util.Collection;
import java.util.Vector;

import jsdai.express_g.exp2.Paging;

import org.eclipse.swt.graphics.Point;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public abstract class AbstractLayout implements Layout {
  protected final static Collection ZERO_LENGHT_COLLECTION = new Vector(0);
  protected Collection items = ZERO_LENGHT_COLLECTION;
  protected Point start = new Point(0, 0);
  protected int page = Paging.ANY_PAGE;

  public AbstractLayout() {
  }

  /**
   *
   * @param collection Collection of Drawable
   * @param start Point starting point of layout
   */
  public void layout(Collection collection, Point start, int page) {
  	this.page = page;
    if (collection == null) items = ZERO_LENGHT_COLLECTION;
    else items = collection;
    this.start = start;
    layout();
  }

  protected abstract void layout();

}
