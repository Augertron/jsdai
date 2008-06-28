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

package jsdai.express_g.exp2.ui.command;

import java.util.Iterator;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.Selection;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.ui.UINaming;

/**
 * @(#) RelationCommand.java
 */
public class RelationCommand extends CommandAdapter {
  protected Selection first = null, second = null;
  protected Selection selected = null;
  protected boolean multiple = false;

  protected int type;

  public RelationCommand(CommandInvoker invoker, int type) {
    super(invoker);
    this.type = type;
  }

  public RelationCommand(CommandInvoker invoker, int type, boolean multiple) {
    super(invoker);
    this.type = type;
    this.multiple = multiple;
  }

  protected void setFirst(Selection first) {
    this.first = first;
    prop.status().setStatus(getStatus());
  }

  protected void setSecond(Selection second) {
    this.second = second;
    prop.status().setStatus(getStatus());
  }

  public String getStatus() {
    String name;
    if (type == AbstractEGRelation.TYPE_AGREGATION) name = "Relation";
    else name = "Inheritance";
    Selection one = null, two = null;
    if (first == null) {
      if (selected != null) {
        one = ((Selectable)selected).selectAsFirst(type);
      }
    } else {
      one = first;
      if (second == null) {
        if (selected != null) {
          two = ((Selectable)first).selectSecond(type, (Selectable)selected);
        }
      } else two = second;
    }
    if (one != null) {
      name += " <" + ((UINaming)one).getUIName() + " (" + ((Named)one).getName() + ")>";
      if (two != null) {
        name += " -o <" + ((UINaming)two).getUIName() + " (" + ((Named)two).getName() + ")>";
      }
    }
    return "Create " + name;
  }

  /**
   * stop
   *
   * @return boolean
   * @todo Implement this jsdai.paint.ui.Command method
   */
  public boolean stop() {
    if (first != null) first.setSelected(false);
    if (second != null) second.setSelected(false);
    if (selected != null) selected.setSelected(false);
    prop.handler().repaint(true);
    exitCode = STOPPED;
    prop.handler().commandDone();
    return true;
  }

  protected boolean selectFirst(Selection item) {
    setFirst(((Selectable)item).selectAsFirst(type));
    return first != null;
  }

  protected boolean selectSecond(Selection item) {
    setSecond(((Selectable)first).selectSecond(type, (Selectable)item));
    return second != null;
  }

  protected boolean createRelation(Selection first, Selection second) {
    return EGToolKit.createRelation(prop, first, second, type) != null;
  }
/*    if (((Paging)first).isOnPage(((Paging)second).getPage())) {
      AbstractEGRelation r = null;
      if (first instanceof EGRelationSimple) {
        EGRelationSimple rs = (EGRelationSimple)first;
        EGRelationTree rt = new EGRelationTree(rs);
        rt.addChild((AbstractEGBox)second);
        rs.eliminate();
        prop.handler().drawableRemove(rs);
        prop.handler().drawableAdd(rt);
        r = rt;
      } else if (first instanceof EGRelationTree) {
        EGRelationTree rt = (EGRelationTree)first;
        rt.addChild((AbstractEGBox)second);
        r = rt;
      } else {
        r = new EGRelationSimple((AbstractEGBox)first, (AbstractEGBox)second, type);
        prop.handler().drawableAdd(r);
      }
      r.setPage(((Paging)first).getPage());
      r.update(2);
      return true;
    } else {
      Point pLoc = new Point();
      pLoc.setLocation((first.getLocation().getX() + second.getLocation().getX())/2,
          (first.getLocation().getY() + second.getLocation().getY())/2);
      EGPageFrom pgf = getPageReference((AbstractEGBox)second);
      boolean pgfCreated = true;
      if (pgf == null) {
        pgf = new EGPageFrom((AbstractEGBox)second);
        pgf.setPage(((Paging)second).getPage());
        pgf.setLocation(pLoc);
        pgfCreated = createRelation(pgf, second);
      }
      EGPageTo pgt = new EGPageTo(pgf);
      pgt.setPage(((Paging)first).getPage());
      pgt.setLocation(pLoc);
      if (pgfCreated && createRelation(first, pgt)) {
        prop.handler().drawableAdd(pgt);
        prop.handler().drawableAdd(pgf);
//        prop.handler().repaint(false);
        return true;
      } else {
        return false;
      }
    }
  }*/

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
	    if (selected != null) {
	        if (first == null) {
	          if (selectFirst(selected)) {
	            first.setSelected(true);
	            selected = null;
	          }
	        } else if (selectSecond(selected)) {
	          first.setSelected(false);
	          selected.setSelected(false);
	          selected = null;
	          if (createRelation(first, second)) {
	            if (multiple) {
	              setFirst(null);
	              setSecond(null);
	            } else
	              prop.handler().commandDone();
	          } else { // relationship not created
	            setSecond(null);
	            setFirst(null);
	          }
	          prop.handler().repaint(false);
	        }
	      }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
	    Iterator iter = prop.handler().drawableIterator(prop.handler().getPage());
	    boolean found = false;
	    Selection item = null;
	    while ((iter.hasNext())&&(!found)) {
	      item = (Selection)iter.next();
	      if (((Paging)item).isOnPage(prop.handler().getPage()))
	        found = item.objectAt(new Point(e.x, e.y));
	    }
	    if (!found) item = null;
	    if (item != selected) {
	      if ((selected != null)&&(selected != first)) selected.setSelected(false);
	      selected = item;
	      if (selected != null) selected.setSelected(true);
	      prop.handler().repaint(false);
	      prop.status().setStatus(getStatus());
	    }
	}
}
