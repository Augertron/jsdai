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
import java.util.Iterator;
import java.util.Vector;

import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.Wire;

import org.eclipse.swt.graphics.Point;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class StringsLayout extends AbstractLayout {

  protected StringsMatrix matrix = new StringsMatrix();
  /**
   * spaces between strings
   * zero for auto spacing
   */
//  protected Point gaps = new Point(AbstractEGBox.basicWidth * 4, AbstractEGBox.basicWidth * 2);
  protected Point gaps = new Point(0, 0);

  public StringsLayout() {
  }

  protected void layoutBox(AbstractEGBox box) {
    if (!matrix.contains(box)) {
      int width = matrix.getWidth();
      matrix.put(box, width, 0);
    }
    Point loc = matrix.indexOf(box);

    Collection supertypes = new Vector();
    Collection subtypes = new Vector();
    Collection attributes = new Vector();
    Collection references = new Vector();

    /* split (use only not placed yet) */
    Iterator iter = box.getWires().iterator();
    while (iter.hasNext()) {
      Wire wire = (Wire)iter.next();
      AbstractEGRelation rel = wire.getRelation();
      if (wire.isAttribute()) {
        Iterator itc = rel.getChilds().iterator();
        while (itc.hasNext()) {
          Object item = itc.next();
          if ((!matrix.contains(item))&&(items.contains(item))) {
            if (rel.getType() == AbstractEGRelation.TYPE_INHERITANCE) subtypes.add(item);
            else attributes.add(item);
          }
        }
      } else {
        Object item = rel.getParent();
        if ((!matrix.contains(item))&&(items.contains(item))) {
          if (rel.getType() == AbstractEGRelation.TYPE_INHERITANCE) supertypes.add(item);
          else references.add(item);
        }
      }

    }

    /**@todo sort */

    int size = 3;
    int left = 1;
    int right = 1;
    if (!references.isEmpty() || subtypes.size() > 1 || supertypes.size() > 1) matrix.insertColsBefore(loc, left);
    if (!attributes.isEmpty() || subtypes.size() > 2 || supertypes.size() > 2) matrix.insertColsAfter(loc, right);

    // put supertypes
    iter = supertypes.iterator();
    if (iter.hasNext()) matrix.insertRowsBefore(loc, (supertypes.size() - 1) / 3 + 1);
    left = 0;
    right = -1;
    while (iter.hasNext()) {
    	matrix.put((Drawable)iter.next(), loc.x + left, loc.y + right);
    	switch (left) {
    		case 0 :
    			left = -1;
    			break;
    		case -1 :
    			left = 1;
    			break;
    		case 1 :
        		left = 0;
        		right--;
    			break;
    	}
    }

    // put subtypes
    iter = subtypes.iterator();
    if (iter.hasNext()) matrix.insertRowsBefore(loc, (supertypes.size() - 1) / 3 + 1);
    left = 0;
    right = 1;
    while (iter.hasNext()) {
    	matrix.put((Drawable)iter.next(), loc.x + left, loc.y + right);
    	switch (left) {
		case 0 :
			left = -1;
			break;
		case -1 :
			left = 1;
			break;
		case 1 :
    		left = 0;
    		right++;
			break;
	}
    }
/*
    int size = Math.max(supertypes.size(), subtypes.size());
    int left = (size - 1)/2;
    int right = (size - 2)/2 + 1;
    matrix.insertColsBefore(loc, left);
    matrix.insertColsAfter(loc, right);

    // put supertypes
    left = (supertypes.size() - 1)/2;
    iter = supertypes.iterator();
    if (iter.hasNext()) matrix.insertRowBefore(loc);
    while (iter.hasNext()) matrix.put((Drawable)iter.next(), loc.x - left--, loc.y - 1);

    // put subtypes
    left = (subtypes.size() - 1)/2;
    iter = subtypes.iterator();
    if (iter.hasNext()) matrix.insertRowAfter(loc);
    while (iter.hasNext()) matrix.put((Drawable)iter.next(), loc.x - left--, loc.y + 1);
*/
    size = Math.max(attributes.size(), references.size());
    left = (size - 1)/2;       // up
    right = (size - 2)/2 + 1;  // down
    matrix.insertRowsBefore(loc, left);
    matrix.insertRowsAfter(loc, right);

    // put references
    left = (references.size() - 1)/2;
    iter = references.iterator();
//    if (iter.hasNext()) matrix.insertColBefore(new Point(loc.x - 1, loc.y));
    while (iter.hasNext()) matrix.put((Drawable)iter.next(), loc.x - 1, loc.y - left--);

    // put attributes
    left = (attributes.size() - 1)/2;
    iter = attributes.iterator();
//    if (iter.hasNext()) matrix.insertColAfter(new Point(loc.x + 1, loc.y));
    while (iter.hasNext()) matrix.put((Drawable)iter.next(), loc.x + 1, loc.y - left--);

  }

  protected void layout() {
    Iterator iter = items.iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if ((item instanceof AbstractEGBox)&&((AbstractEGBox)item).isOnPage(page)) layoutBox((AbstractEGBox)item);
    }
    matrix.setRealPlacement(start, gaps);
  }

}
