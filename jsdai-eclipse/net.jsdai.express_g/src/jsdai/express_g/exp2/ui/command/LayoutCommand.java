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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.layout.*;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class LayoutCommand extends CommandAdapter {
  protected Collection selected = null;
  public static final Point NO_LOCATION = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
  protected Point location = NO_LOCATION;

  public LayoutCommand(CommandInvoker invoker) {
    super(invoker);
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public Point getLocation() {
    return location;
  }

  /**
   * init
   *
   * @param prop PropertySharing
   */
  public void init(PropertySharing prop) {
    super.init(prop);
    selected = prop.getSelectionHandler().getSelected();
  }
/*
  public void layoutBox(AbstractEGBox box, Enumeration place) {
    if (!box.isOnPage(Paging.ANY_PAGE)) {
//System.out.println("creating box: " + box + "<" + box.getClass().getName() + ">");
      box.setPage(prop.handler().getPage());
      box.setLocation((Point)place.nextElement());
      if ((box instanceof EGDefined)||(box instanceof EGSelect)) {
        Iterator iter = box.getWires().iterator();
        while (iter.hasNext()) {
          Wire wire = (Wire)iter.next();
          if (wire.isAttribute()) layoutRelation(wire.getRelation(), place);
        }
      }
    }
//else System.out.println("box exists: " + box + "<" + box.getClass().getName() + ">");
  }

  private EGRelationSimple layoutRelation(EGRelationSimple relation, Enumeration place) {
//System.out.println("simple");
    if (!relation.getChild().isOnPage(Paging.ANY_PAGE)) layoutBox(relation.getChild(), place);
    EGRelationSimple rel2 = (EGRelationSimple)EGToolKit.createRelation(prop, relation.getParent(), relation.getChild(), relation.getType());
    rel2.setAgregate(relation.getAgregate());
    rel2.setDerived(relation.isDerived());
    rel2.setInverse(relation.getInverse());
    rel2.setOptional(relation.isOptional());
    return rel2;
  }

  private EGRelationTree layoutRelation(EGRelationTree relation, Enumeration place) {
//System.out.println("tree");
    Iterator iter = relation.getChilds().iterator();
    AbstractEGRelation rel2 = null;
    while (iter.hasNext()) {
      AbstractEGBox box = (AbstractEGBox)iter.next();
      if (!box.isOnPage(Paging.ANY_PAGE)) layoutBox(box, place);
      if (rel2 == null) rel2 = EGToolKit.createRelation(prop, relation.getParent(), box, relation.getType());
      else rel2 = EGToolKit.createRelation(prop, rel2, box, relation.getType());
    }
    ((EGRelationTree)rel2).setSubtype_expression(relation.getSubtype_expression());
    return (EGRelationTree)rel2;
  }

  public void layoutRelation(AbstractEGRelation relation, Enumeration place) {
    if (relation.getParent().isOnPage(Paging.ANY_PAGE)) {
//System.out.println("creating relation: " + relation.getParent() + "<" + relation.getParent().getClass().getName() + ">" + " -o " + relation.getChild() + "<" + relation.getChild().getClass().getName() + ">");
      AbstractEGRelation rel2 = null;
      if (relation instanceof EGRelationSimple) rel2 = layoutRelation((EGRelationSimple)relation, place);
      else  rel2 = layoutRelation((EGRelationTree)relation, place);
      rel2.setHint1(relation.getHint1());
      rel2.setHint2(relation.getHint2());
      rel2.setName(relation.getName());
      rel2.setShortName(relation.getShortName());
      prop.getSelectionHandler().delete(relation);
    }
//else System.out.println("relation exists: " + relation.getParent() + "<" + relation.getParent().getClass().getName() + ">" + " -o " + relation.getChild() + "<" + relation.getChild().getClass().getName() + ">");
  }
/**/
  /**
   * start
   *
   *
  public void start() {
    Enumeration place = EGToolKit.getObjectPlacementMatrix(AbstractEGBox.basicWidth * 4, AbstractEGBox.basicWidth * 2);
    Iterator iter = selected.iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGBox) layoutBox((AbstractEGBox)item, place);
      else if (item instanceof AbstractEGRelation) layoutRelation((AbstractEGRelation)item, place);
    }
    prop.handler().commandDone();
  }

	/**
	 * @author Mantas Balnys
	 *
	 */
  	/** start
  	 *
  	 */
    public void start() {
		if (location == NO_LOCATION) {
			setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_CROSS));
		} else {
			action();
		}
    }
      
    private void action() {
    	Collection col = selected;//EGToolKit.updateForSimpleTypes(selected);
//    	System.out.println();
//    	System.out.println("LAYOUT:");
//    	for (Iterator i = col.iterator(); i.hasNext(); System.out.println(i.next()));
    	Collection hash = new LinkedHashSet(col);
    	hash = EGToolKit.PageRef.changePage(prop, hash, prop.handler().getPage());

    	Iterator iter = hash.iterator();
    	GC g = prop.getPainting().getLastGraphics();
    	while (iter.hasNext()) {
    		Object item = iter.next();
    		if (item instanceof AbstractEGBox) {
    			((AbstractEGBox)item).draw(g);
    		}
    	}

    	StringsLayout layout = new StringsLayout();
    	layout.layout(hash, location, prop.handler().getPage());
    	
    	prop.handler().update(prop.handler().getPage());
    	prop.handler().repaint(true);
    	prop.handler().commandDone();
    }

	public void mouseUp(MouseEvent e) {
		if (location == NO_LOCATION) {
			location = new Point(e.x, e.y);
			action();
		}
	}
}
