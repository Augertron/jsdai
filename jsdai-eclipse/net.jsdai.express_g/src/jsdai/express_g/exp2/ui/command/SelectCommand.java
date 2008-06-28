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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Editable;
import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.Selection;
import jsdai.express_g.exp2.Updateable;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGConstraint;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.eg.EGPage;
import jsdai.express_g.exp2.eg.EGPageFrom;
import jsdai.express_g.exp2.eg.EGPageRefLocal;
import jsdai.express_g.exp2.eg.EGPageRefLocalFrom;
import jsdai.express_g.exp2.eg.EGPageRefLocalTo;
import jsdai.express_g.exp2.eg.EGPageRefToSimple;
import jsdai.express_g.exp2.eg.EGPageTo;
import jsdai.express_g.exp2.eg.EGRelationSimple;
import jsdai.express_g.exp2.eg.EGRelationTree;
import jsdai.express_g.exp2.eg.Wire;
import jsdai.express_g.exp2.ui.ContextMenu;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.SimpleRect;
import jsdai.express_g.exp2.ui.SizingPoint;
import jsdai.express_g.exp2.ui.UINaming;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.event.SelectionEvent;
import jsdai.express_g.exp2.ui.event.SelectionListener;
import jsdai.express_g.exp2.ui.panels.PaintPanel;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class SelectCommand extends CommandAdapter {
  protected ListenerList selectionListeners = new ListenerList();
  protected HashSet selected = new HashSet();
  //protected Vector selected = new Vector();
  protected Point mouseP0 = new Point(0, 0);
  protected SimpleRect selRect = new SimpleRect();
  protected boolean selDrag = false;
  public static final Set EMPTY_SELECTION = new HashSet(0);

  public SelectCommand(CommandInvoker invoker) {
    super(invoker);
  }

  public void addSelectionListener(SelectionListener listener) {
    selectionListeners.add(listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    selectionListeners.remove(listener);
  }

  public void fireSelectionChanged(Object source, Collection oldSel, Collection newSel) {
    Object[] list = selectionListeners.getListeners();
    SelectionEvent e = new SelectionEvent(source, oldSel, newSel);
    for (int i = 0; i < list.length; i++) {
    	SelectionListener listener = (SelectionListener)list[i];
        if (listener != source) listener.selectionChanged(e);
    }    	
  }

  public String getStatus() {
    String name = "";
    if (selected.size() == 1) {
      Object sel = selected.iterator().next();
      name = " " + ((UINaming)sel).getUIName() + " (" + ((Named)sel).getName() + ")";
    }
    return "Select" + name;
  }

  public boolean interrupt() {
    setSelected((Selection)null);
    draggingSizingPoint = null;
    return true;
  }

  public boolean stop() {
    if (interrupt()) {
      prop.handler().commandDone();
      return true;
    }
    return false;
  }

  public void delete(AbstractEGBox box) {
  	if (box.locked()) return;
    if ((prop.getEditMode() & PropertySharing.MODE_LAYOUT_MASK) != 0) {
      Vector list = new Vector(1);
      list.add(box);
      EGToolKit.PageRef.changePage(prop, list, Paging.INVISIBLE_PAGE);
    } else {
      Vector wires = box.getWires();
      Iterator wit = wires.iterator();
      while (wit.hasNext()) {
        Wire wire = (Wire)wit.next();
        if (wire.getRelation()instanceof EGRelationTree) {
          EGRelationTree rt = (EGRelationTree)wire.getRelation();
          if (wire.isAttribute()) {
            Vector child = rt.getChilds();
            rt.eliminate();
            prop.handler().drawableRemove(rt);
            Iterator itCh = child.iterator();
            while (itCh.hasNext())
              ((AbstractEGBox)itCh.next()).update(1);
          } else {
            rt.removeChild(box);
            if (rt.getChildCount() == 1) {
              EGRelationSimple rs = new EGRelationSimple(prop, rt);
              rt.eliminate();
              prop.handler().drawableRemove(rt);
              prop.handler().drawableAdd(rs);
              rs.update(2);
            } else
              rt.update(0);
          }
        } else {
          AbstractEGBox other = null;
          if (wire.isAttribute())
            other = wire.getRelation().getChild();
          else
            other = wire.getRelation().getParent();
          wire.getRelation().eliminate();
          prop.handler().drawableRemove(wire.getRelation());
          if (other instanceof EGPage)
            delete(other);
          else
            other.update(1);
        }
      }
      prop.handler().drawableRemove(box);
      if (box instanceof EGPageFrom) {
        Iterator it = ((EGPageFrom)box).getReferences().iterator();
        box.eliminate();
        while (it.hasNext())
          delete((AbstractEGBox)it.next());
      } else if (box instanceof EGPageTo) {
        EGPageFrom epf = ((EGPageTo)box).getReferenced();
        box.eliminate();
        if (epf.getReferencesCount() < 1)
          delete(epf);
      } else
        box.eliminate();
    }
  }

  public void delete(AbstractEGRelation wire) {
  	if (wire.locked()) return;
    if ((prop.getEditMode() & PropertySharing.MODE_LAYOUT_MASK) != 0) {
      Vector list = new Vector(1);
      list.add(wire);
      EGToolKit.PageRef.changePage(prop, list, Paging.INVISIBLE_PAGE);
    } else {
      Vector box = wire.getChilds();
      AbstractEGBox parent = wire.getParent();
      /*    if (parent instanceof EGPageFrom) delete(parent);
          else*/if (parent != null)
        box.add(parent);
      wire.eliminate();
      prop.handler().drawableRemove(wire);
      Iterator itBox = box.iterator();
      while (itBox.hasNext()) {
        AbstractEGBox b = (AbstractEGBox)itBox.next();
        if (b instanceof EGPage)
          delete(b);
        else
          b.update(1);
      }
    }
  }

  public void deleteSelected() {
//System.err.println("\tdeleting");
//    if (sizingItem != null) deleteSizingPoints();
    if ((prop.getEditMode() & PropertySharing.MODE_LAYOUT_MASK) != 0) {
      EGToolKit.PageRef.changePage(prop, getSelected(), Paging.INVISIBLE_PAGE);
      prop.handler().update(prop.handler().getPage());
    } else {
      Iterator iter = selected.iterator();
      while (iter.hasNext()) {
        Object item = (Object)iter.next();
        if (item instanceof AbstractEGBox)
          delete((AbstractEGBox)item);
        else if (item instanceof AbstractEGRelation)
          delete((AbstractEGRelation)item);
      }
    }
    setSelected((Selection)null);
// FIX none of update redraw should be needed
    prop.handler().update(prop.handler().getPage());
    prop.handler().repaint(true);
//System.err.println("\tdeleted");    
  }
/*
  public void changePage(Collection items, int pgNr) {
    Collection col = EGToolKit.PageRef.changePage(prop, items, pgNr);
    items.clear();
    items.addAll(col);
  }

/*
    HashSet items2 = new HashSet(items);
    Iterator iter = items.iterator();
    while (iter.hasNext()) { // changing page for boxes and relations (attribute)
      Paging item = (Paging)iter.next();
      if (((item instanceof AbstractEGBox)||(item instanceof SizingPoint))&&
          (!item.isOnPage(pgNr))&&(!(item instanceof EGPage))) {
        item.setPage(pgNr);
        if (item instanceof AbstractEGBox) {
          Iterator itw = ((AbstractEGBox)item).getWires().iterator();
          while (itw.hasNext()) {
            Wire wire = (Wire)itw.next();
            AbstractEGRelation rel = wire.getRelation();
//System.out.println(rel + " " + rel.getPage());
            if (!rel.isOnPage(Paging.INVISIBLE_PAGE)) {
              items2.add(rel);
              if (!wire.isAttribute()) {
//              rel.setPage(pgNr);
//            } else {
                AbstractEGBox parent = rel.getParent();
                if (parent instanceof EGPageFrom) {
                  parent.setPage(pgNr);
                  if (!items.contains(parent)) items2.add(parent);
                }
              }
            }
//          }
          }
        }
      }
    }

    iter = items.iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      if (item instanceof AbstractEGRelation) {
        ((AbstractEGRelation)item).setPage(pgNr);
        items2.add(item);
      }
    }

    EGToolKit.PageRef.updatePageRef(prop, items2);

    prop.handler().repaint(true);
  }
/**/
  public void changePageSelected(int pgNr) {
	  Set selected = getSelected();
    Collection changed = EGToolKit.PageRef.changePage(prop, selected, pgNr);
    GC g = prop.getPainting().getLastGraphics();
    Iterator iter = changed.iterator();
    while (iter.hasNext()) {
    	Object item = iter.next();
    	if ((item instanceof AbstractDraw) && !((AbstractDraw)item).isEliminated()) {
    		((AbstractDraw)item).draw(g);
    		((AbstractDraw)item).update(1);
    	}
    }
    setSelected(EMPTY_SELECTION);
    setSelected(selected);
    prop.handler().update(prop.handler().getPage());
    prop.handler().update(pgNr);
    prop.handler().setPage(pgNr);
  }

  public Set getSelected() {
    return (Set)selected.clone();
  }

  protected void addSelected(Selection selected) {
    Set sel = getSelected();
    if (selected != null) sel.add(selected);
    setSelected(this, sel);
  }

  protected void addSelected(Set selected) {
    Set sel = getSelected();
    sel.addAll(selected);
    setSelected(this, sel);
  }

  protected void removeSelected(Selection selected) {
    Set sel = getSelected();
    sel.remove(selected);
    setSelected(this, sel);
  }

  protected void removeSelected(Set selected) {
    Set sel = getSelected();
    sel.removeAll(selected);
    setSelected(this, sel);
  }

  protected void setSelected(Selection selected) {
    HashSet v = new HashSet(1);
    if (selected != null) v.add(selected);
    setSelected(this, v);
  }

  protected void setSelected(Set selected) {
    setSelected(this, selected);
  }

  public void setSelected(Object invoker, Set selected) {
    if ((this.selected.size() != selected.size())||(!this.selected.containsAll(selected))) {
//System.err.println("SELECTED: " + selected);    	
      if (sizingItem != null) deleteSizingPoints();
      Iterator iter = this.selected.iterator();
      while (iter.hasNext()) ((Selection)iter.next()).setSelected(false);
      Collection oldSel = (Collection)this.selected.clone();
      this.selected.clear();
      iter = selected.iterator();
      while (iter.hasNext()) ((Selection)iter.next()).setSelected(true);
      this.selected.addAll(selected);
      if (selected.size() == 1) createSizingPoints((AbstractDraw)selected.iterator().next());
      prop.status().setStatus(getStatus());
//      prop.setModified(true);
      fireSelectionChanged(invoker, oldSel, selected);
    }
  }

  	protected LayoutCommand layoutCommand = null;
  	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
	    if ((e == null)||((e.button == 1)&&(e.stateMask == SWT.NONE))) {
	        showPropertiesMenu();
	        dragging = false;
	    } else
	    if ((e.button == 1)&&(e.stateMask == SWT.CONTROL)) { // bring to this page
	        if (selected.size() > 0) {
	            Object item = selected.iterator().next();
	            if (item instanceof EGPageRefLocal) {
	            	Point loc = ((EGPageRefLocal)item).getCenterPoint();
	            	AbstractEGBox tobring = null;
	            	if (item instanceof EGPageRefLocalTo) tobring = ((EGPageRefLocalTo)item).getReferenced(); else
	                if (item instanceof EGPageRefLocalFrom) {
	                	EGPageRefLocalTo first = (EGPageRefLocalTo)((EGPageRefLocalFrom)item).getLinkedIterator().next();
	            		Iterator wit = first.getWires().iterator();
	            		while (wit.hasNext() && (tobring == null)) {
	            			Wire wire = (Wire)wit.next();
	            			if (!wire.isAttribute())
	            				tobring = wire.getRelation().getParent();
	            		}
	                }
	            	if (tobring != null) {
		    	        setSelected(tobring);
		    	        if (layoutCommand == null) layoutCommand = new LayoutCommand(invoker);
		    	        layoutCommand.setLocation(loc);
		    	        prop.handler().startCommand(layoutCommand);
	            	}
	            } /*else 
	            if (item instanceof EGPage) {
	            	Point loc = ((EGPage)item).getCenterPoint();
	            	AbstractEGBox tobring = null;
	            	if (item instanceof EGPageTo) tobring = ((EGPageTo)item).getReferencedObject(); else
	                if (item instanceof EGPageFrom) {
	                	EGPageTo first = (EGPageTo)((EGPageFrom)item).getReferences().iterator().next();
	                	tobring = first.getRefRelation().getParent();
	                }
	            	if (tobring != null) {
		    	        setSelected(tobring);
		    	        if (layoutCommand == null) layoutCommand = new LayoutCommand(invoker);
		    	        layoutCommand.setInsets(loc);
		    	        prop.handler().startCommand(layoutCommand);
	            	}
	            } */
	        }
	    	
	    }
	}
	
  public void showPropertiesMenu() {
    if (selected.size() > 0) {
//      if (prop.getEditMode() == PropertySharing.MODE_EDIT) {
        Object item = selected.iterator().next();
        if (item instanceof EGPageRefLocal) {
        	int pg = Paging.INVISIBLE_PAGE;
        	if (item instanceof EGPageRefLocalTo) pg = ((EGPageRefLocalTo)item).getReferenced().getPage(); else
           	if (item instanceof EGPageRefLocalFrom) pg = ((EGPageRefLocalTo)((EGPageRefLocalFrom)item).getLinkedIterator().next()).getPage();
           	if (pg != Paging.INVISIBLE_PAGE) prop.handler().setPage(pg);
        } else if (item instanceof Editable) {
          Editable eitem = (Editable)item;
          if (!eitem.isEditDialogCreated()) eitem.createEditDialog((PaintPanel)prop.getPainting());
          eitem.showEditDialog();
          ((Drawable)eitem).draw(prop.getPainting().getLastGraphics());
          ((Updateable)eitem).update(1);
          updateSizingPoints();
          prop.handler().repaint(false);
        }
//      }
    } else {
      VisualPage vp = prop.handler().getVisualPage(prop.handler().getPage());
      if (!vp.isEditDialogCreated()) vp.createEditDialog((PaintPanel)prop.getPainting());
      vp.showEditDialog();
      prop.handler().setPage(prop.handler().getPage());
    }
//    prop.setModified(true);
  }

  protected int maskPage = ContextMenu.MASK_PROPERTIES;
  protected int maskMultiSelection = ContextMenu.MASK_DELETE | ContextMenu.MASK_RESET_PLACEMENT |
      ContextMenu.MASK_LAYOUT | ContextMenu.MASK_MOVE_TO_PAGE | ContextMenu.MASK_AUTO_SIZE |
	  ContextMenu.MASK_ALIGN | ContextMenu.MASK_BUNDLE | ContextMenu.MASK_UNBUNDLE;
  protected int maskPageRef = ContextMenu.MASK_DELETE |
      ContextMenu.MASK_BRING_TO_THIS_PAGE | ContextMenu.MASK_PROPERTIES | ContextMenu.MASK_AUTO_SIZE |
      ContextMenu.MASK_UNBUNDLE;
//  protected int maskPageFrom = maskPage | ContextMenu.MASK_BRING_REFERENCES;
  protected int maskRelation = ContextMenu.MASK_DELETE |
      ContextMenu.MASK_RESET_PLACEMENT | ContextMenu.MASK_PROPERTIES |
      ContextMenu.MASK_UNBUNDLE;
  protected int maskBox = ContextMenu.MASK_DELETE |
      ContextMenu.MASK_MOVE_TO_PAGE | ContextMenu.MASK_PROPERTIES |
      ContextMenu.MASK_BRING_REFERENCES | ContextMenu.MASK_BRING_ATTRIBUTES | ContextMenu.MASK_AUTO_SIZE |
      ContextMenu.MASK_UNBUNDLE;
  protected int maskEntity = maskBox | ContextMenu.MASK_BRING_SUBTYPES |
      ContextMenu.MASK_BRING_SUPERTYPES;

  public int getPopupMask() {
  	int mask = 0;
      if (selected.size() == 0) {

      } else if (sizingItem != null) {
        int visibility = 0;
        Object select = selected.iterator().next();
        if (select instanceof AbstractEGObject) { // visible menu
          AbstractEGObject ego = (AbstractEGObject)select;
          if (ego.canBeInvisible()) {
            switch (ego.getVisible()) {
            	case AbstractEGObject.VISIBLE_UNSET :
            	case AbstractEGObject.VISIBLE_TRUE :
                    visibility = ContextMenu.MASK_SET_INVISIBILE;
            		break;
            	case AbstractEGObject.VISIBLE_FALSE :
                    visibility = ContextMenu.MASK_SET_VISIBILE;
            		break;
            }
          }
          if (ego instanceof EGPageRefToSimple && ((EGPageRefToSimple)ego).isConcreteValuesSet() 
        		  && ((EGPageRefToSimple)ego).getWireCount() == 1) {
              switch (ego.getVisible()) {
              	case AbstractEGObject.VISIBLE_UNSET :
                      visibility = visibility | ContextMenu.MASK_HIDE_CONCRETE;
              		break;
              	case AbstractEGObject.VISIBLE_TRUE :
              	case AbstractEGObject.VISIBLE_FALSE :
                      visibility = visibility | ContextMenu.MASK_SHOW_CONCRETE;
              		break;
              }
            }
        }
        
        if (select instanceof VisualPage) {
        	mask = maskPage | visibility;
        } else
        if (select instanceof EGPageRefLocal) {
          mask = maskPageRef | visibility;
        } else
        if (select instanceof AbstractEGRelation) {
        	mask = maskRelation | visibility;
        } else
        if (select instanceof EGEntity) {
        	mask = maskEntity | visibility;
        } else {
        	//mask = maskBox | visibility;
        	mask = maskEntity | visibility;
        }
      } else {
      	mask = maskMultiSelection | ContextMenu.MASK_SET_VISIBILE | ContextMenu.MASK_SET_INVISIBILE;
      }
      return mask;
  }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		dragging = true;
	    Iterator iter = prop.handler().drawableIterator(prop.handler().getPage());
	    boolean found = false;
	    Selection item = null;
	    Point epoint = new Point(e.x, e.y);
	    while ((iter.hasNext()) && (!found)) {
	      item = (Selection)iter.next();
	      if (((Paging)item).isOnPage(prop.handler().getPage())) {
	      	if (!(item instanceof VisualPage)) // skipping Page frame
	      		found = item.objectAt(epoint);
	      }
	    }
	    if (!found) item = null;
	    if (item instanceof SizingPoint) draggingSizingPoint = (SizingPoint)item;
	    else draggingSizingPoint = null;
	    if ((!found) || (!item.isSelected())) {
	      if (!found) {
	        selDrag = true;
	        selRect.setLocation(epoint);
	        selRect.setSize(new Point(0, 0));
	        prop.handler().drawableAdd(selRect);
	      }
//	      if (sizingItem != null) deleteSizingPoints();
	      if ((e.stateMask & SWT.SHIFT) != 0) {
	        if (item != null) addSelected(item);
	      } else {
	        setSelected(item);
//	        if (item instanceof AbstractEGObject) createSizingPoints(item);
	      }
	      prop.handler().repaint(false);
	    } else {
	      if ((e.stateMask & SWT.SHIFT) != 0) {
//	        if (sizingItem != null) deleteSizingPoints();
	        removeSelected(item);
//	        prop.handler().repaint(false);
	      }
	    }
	    mouseP0.x = e.x;
	    mouseP0.y = e.y;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		dragging = false;
	    if (selDrag) {
	        selDrag = false;
	        prop.handler().drawableRemove(selRect);
	        Iterator iter = prop.handler().drawableIterator(prop.handler().getPage());
	        HashSet sel = new HashSet();
	        Rectangle rectangle = selRect.getBounds();
	        int currentPage = prop.handler().getPage();
	        while (iter.hasNext()) {
	          Selection item = (Selection)iter.next();
	          if (((Paging)item).isOnPage(currentPage))
	            if (item.objectAt(rectangle)) sel.add(item);
	        }
	        	
	        if (e != null && sel.size() == 0) { // try select Page frame if nothing else is selected
	        	VisualPage page = prop.handler().getCurrentPage();
	        	if (page.objectAt(new Point(e.x, e.y))) sel.add(page);
	        }
	        if (e != null && (e.stateMask & SWT.SHIFT) != 0) {
	          addSelected(sel);
	        } else setSelected(sel);
//	        prop.handler().repaint(true);
	    } 
	    prop.handler().repaint(true);
	}
  /**
   * Invoked when a mouse button is pressed on a component and then dragged.
   *
   * @param e MouseEvent
   */

  public void mouseMove(MouseEvent e) {
  	if (dragging) {
  	    if (selDrag) {
  	      Point loc = new Point(mouseP0.x, mouseP0.y);
  	      loc.x += Math.min(0, e.x - mouseP0.x);
  	      loc.y += Math.min(0, e.y - mouseP0.y);
  	      selRect.setLocation(loc);
  	      Point size = new Point(Math.abs(mouseP0.x - e.x), Math.abs(mouseP0.y - e.y));
  	      selRect.setSize(size);
  	      prop.getPainting().repaint();
  	    } else if (selected.size() > 0) {
  	      if (draggingSizingPoint == null) {
  	        Iterator iter = selected.iterator();
  	        while (iter.hasNext()) {
  	          Selection item = (Selection)iter.next();
  	          Point loc = new Point(e.x - mouseP0.x, e.y - mouseP0.y);
  	          item.translate(loc);
  	          ((Updateable)item).update(3);
  	        }
  	        mouseP0.x = e.x;
  	        mouseP0.y = e.y;
  	      } else {
  	        Point loc = new Point(e.x - mouseP0.x, e.y - mouseP0.y);
  	        draggingSizingPoint.translateSizing(loc);
  	        ((Drawable)sizingItem).draw(prop.getPainting().getLastGraphics());
  	        ((Updateable)sizingItem).update(2);
  	        for (int i=0; i<4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
  	        mouseP0.x = e.x;
  	        mouseP0.y = e.y;
  	      }
  	      prop.getPainting().repaint();
//  	      prop.handler().repaint(false);
  	    }
  	}
  }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover(MouseEvent e) {
		if ((dragging)&&(draggingSizingPoint != null)) {
			Point newloc = draggingSizingPoint.getLocation();
			newloc.x += draggingSizingPoint.getSize().x / 2;
			newloc.y += draggingSizingPoint.getSize().y / 2;
			mouseP0.x = newloc.x;
			mouseP0.y = newloc.y;
			e.display.setCursorLocation(((PaintPanel)prop.getPainting()).toDisplay(newloc));
		}
	}
	
  protected SizingPoint draggingSizingPoint = null;
  protected AbstractDraw sizingItem = null;
  protected SizingPoint[] sizingPoint = new SizingPoint[4];
  protected boolean dragging = false;

  protected void createSizingPoints(AbstractDraw item) {
    sizingItem = item;
    if (item instanceof AbstractEGRelation) {
      sizingPoint[0] = new SizingPoint(sizingItem, SizingPoint.PLACE_RELATION_HINT);
      sizingPoint[0].setSelected(true);
      selected.add(sizingPoint[0]);
      prop.handler().drawableAdd(sizingPoint[0]);
      sizingPoint[0].update();
      sizingPoint[0].setPage(((Paging)item).getPage());
      sizingPoint[1] = new SizingPoint(sizingItem, SizingPoint.PLACE_RELATION_TEXT);
      sizingPoint[1].setSelected(true);
      selected.add(sizingPoint[1]);
      prop.handler().drawableAdd(sizingPoint[1]);
      sizingPoint[1].update();
      sizingPoint[1].setPage(((Paging)item).getPage());
    } else {
      for (int i = 0; i < 4; i++) {
        sizingPoint[i] = new SizingPoint(sizingItem, i);
        sizingPoint[i].setPage(((Paging)item).getPage());
        sizingPoint[i].setSelected(true);
        selected.add(sizingPoint[i]);
        prop.handler().drawableAdd(sizingPoint[i]);
        sizingPoint[i].update();
      }
    }
  }

  protected void deleteSizingPoints() {
    sizingItem = null;
    for (int i=0; i<4; i++) if (sizingPoint[i] != null) {
      prop.handler().drawableRemove(sizingPoint[i]);
      selected.remove(sizingPoint[i]);
      sizingPoint[i] = null;
    }
  }

  protected void updateSizingPoints() {
    for (int i = 0; i < 4; i++)
    if (sizingPoint[i] != null) sizingPoint[i].update();
  }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	    switch (e.keyCode) {
	      case SWT.DEL :
	        deleteSelected();
	        break;
	      case SWT.ESC :
	        stop();
	        break;
	      case SWT.PAGE_DOWN :
	        int pg1 = prop.handler().getPage() - 1;
	        if (pg1 < 1) pg1 = 1;
	        prop.handler().setPage(pg1);
	        break;
	      case SWT.PAGE_UP :
	        prop.handler().setPage(prop.handler().getPage() + 1);
	        break;

//	 DEBUG COMMAND SYSTEM
	      case SWT.KEYPAD_0 :
//  	        System.err.println("DRAWABLE SIZE:" + prop.handler().drawableCopy().size());
	    	  break;
	      case SWT.KEYPAD_1 :
//	        System.err.println("DRAWABLE:");
//	        Iterator iter = prop.handler().drawableIterator(prop.handler().getPage());
//	        while (iter.hasNext()) System.err.println(iter.next());
//	        System.err.println();
	        break;
	      case SWT.KEYPAD_2 :
//	        System.err.println("PAGE REF relation count:");
//	        Iterator itp = prop.handler().drawableIterator(prop.handler().getPage());
//	        while (itp.hasNext()) {
//	          Object item = itp.next();
//	          if (item instanceof EGPage)
//	            System.err.println(((EGPage)item).getWires().size() + " : " + item + " <" + item.getClass() + ">");
//	        }
//	        System.err.println();
	        break;
	      case SWT.KEYPAD_3 :
//	        System.err.println("DRAWABLE TEST:");
	        Iterator itdt = prop.handler().drawableIterator();
	        while (itdt.hasNext()) {
	          Object item = itdt.next();
	          if (item instanceof AbstractEGBox) {
//	          	System.err.println("BOX:" + ((AbstractEGBox)item).getPage() + ":" + ((AbstractEGBox)item).getName() + " <" + item.getClass() + ">");
	          } else
	          if (item instanceof AbstractEGRelation) {
	          	AbstractEGBox parent = ((AbstractEGRelation)item).getParent();
//	          	System.err.println("REL:" + ((AbstractEGRelation)item).getPage() + ":" + ((AbstractEGRelation)item).getName() + " parent=" + (parent == null ? "null" : parent.getName()) + " <" + item.getClass() + ">");
//	          	System.err.println(parent == null ? "\tparent null" : "\tparent:" + parent.getPage() + ":" + parent.getName() + " <" + parent.getClass() + ">");
	          	Iterator children = ((AbstractEGRelation)item).getChilds().iterator();
	          	while (children.hasNext()) {
	          		AbstractEGBox child = (AbstractEGBox)children.next();
//		          	System.err.println("\t\t" + child.getPage() + ":" + child.getName() + " <" + child.getClass() + ">");
	          	}
	          } else
	          if (item instanceof Paging) {
//	          	System.err.println("OTH:" + ((Paging)item).getPage() + ":" + item);
	          } else {
//	          	System.err.println("UNK:" + item);
	          }
	        }
//	        System.err.println();
	        break;
	      case SWT.KEYPAD_4 :
//	      	System.err.println("SELECT RELATION BUNDLE");
	      	Collection col2 = getSelected();
      		Object object2 = null;
      		if (col2.size() > 0) object2 = col2.iterator().next();
	      	if (object2 instanceof AbstractEGRelation) {
	      		setSelected(((AbstractEGRelation)object2).getGroup());
		      	prop.handler().repaint(false);
	      	} else {
//		    	System.err.println("available only on single Relation selection");
	      	}
	      	break;
	      case SWT.KEYPAD_5 :
//		      	System.err.println("ITEM DETAILS:");
		      	Iterator it5 = getSelected().iterator();
		      	int index5 = 0;
		      	while (it5.hasNext()) {
		      		Object item = it5.next();
		      		if (!(item instanceof SizingPoint)) {
			      		index5++;
//			      		System.err.println("\tITEM \t" + index5 + ":");
//			      		System.err.println(item);
			      		if (item instanceof AbstractEGObject) {
//			      			System.err.println("  |AbstractEGObject info:");
			      			AbstractEGObject eg = (AbstractEGObject)item;
//			      			System.err.println("id:\t>" + eg.getID() + "<");
//			      			System.err.println("ui name:\t>" + eg.getUIName() + "<");
//			      			System.err.println("name:\t>" + eg.getName() + "<");
//			      			System.err.println("shortName:\t>" + eg.getShortName() + "<");
//			      			System.err.println("text:\t>" + eg.getText() + "<");
//			      			System.err.println("page:\t>" + eg.getPage() + "<");
//			      			System.err.println("locked:\t>" + eg.locked() + "<");
//			      			System.err.println("can be inv:\t>" + eg.canBeInvisible() + "<");
//			      			System.err.println("visible:\t>" + eg.getVisible() + "<");
//			      			System.err.println("concreteValue(" + (eg.getConcreteValues() != null ? String.valueOf(eg.getConcreteValues().length) : "null") + "):\t>" + eg.getConcreteValue() + "<");
//			      			System.err.println("autosize:\t>" + eg.isLabelNew() + "<");
//			      			System.err.println("bounds:\t>" + eg.getBounds() + "<");
//			      			System.err.println("property dialog created:\t>" + eg.isEditDialogCreated() + "<");
//			      			System.err.println("selected:\t>" + eg.isSelected() + "<");
//			      			System.err.println("updating:\t>" + eg.isUpdating() + "<");
//			      			System.err.println("eliminated:\t>" + eg.isEliminated() + "<");
//			      			System.err.println("entity def:\t>" + eg.getDefinition() + "<");
//			      			System.err.println("entity place:\t>" + eg.getDefinitionPlacement() + "<");
//			      			System.err.println("class:\t>" + eg.getClass() + "<");
//			      			System.err.println("hash:\t>" + Integer.toHexString(eg.hashCode()) + "<");
			      		}    		
//			      		System.err.println();
		      		}
		      	}
		      	break;
/*	      case SWT.KEYPAD_7 :
			Object o1 = ExpressUIPlugin.getListener();
			if (o1 instanceof ExpressView) {
				ISelection selection = ((ExpressView)o1).getViewer().getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection)selection;
					Object first = ss.getFirstElement();
					System.err.println("Selection internals:" + first);
					if (first instanceof IExpressElement) {
						IExpressProject exp = ((IExpressElement)first).getExpressProject();
						System.err.println(exp);
						System.err.println(exp.getRepositoryHandler());
					} else {
						System.err.println("Selected not ExpressElement:" + first);
					}
				} else {
					System.err.println("Bad selection type:" + selection);
				}
			} else {
				System.err.println("ExpressView not found:" + o1);
			}
	      	break;
	      case SWT.KEYPAD_8 :
			Object o = ExpressUIPlugin.getListener();
			if (o instanceof ExpressView) {
				TreeItem[] projects = ((ExpressView)o).getViewer().getTree().getItems();
				System.err.println("RepositoryHandler status:");
				for (int i = 0; i < projects.length; i++) {
					Object data = projects[i].getData();
					System.err.println("project:" + projects[i]);
					if (data instanceof ExpressProject) {
						RepositoryHandler repositoryHandler = ((ExpressProject)data).getRepositoryHandler();
						System.err.println(repositoryHandler);
					} else {
						System.err.println("not ExpressProject:" + data);
					}
				}
			} else {
				System.err.println("ExpressView not found:" + o);
			}
	      	break;*/
	      case SWT.KEYPAD_DIVIDE :
	    	  prop.handler().repaint(false);
//	    	  System.err.println("REPAINTED");
	    	  break;
	      case SWT.KEYPAD_MULTIPLY :
	    	  if ((e.stateMask & SWT.CONTROL) != 0) {
		    	  prop.handler().update();
//		    	  System.err.println("UPDATED ALL PAGES");
	    	  } else {
	    		  prop.handler().update(prop.handler().getPage());
//		    	  System.err.println("UPDATED PAGE:" + prop.handler().getPage());
	    	  }
	    	  break;
	      case SWT.KEYPAD_9 :
	      	Collection col = prop.getSelectionHandler().getSelected();
      		Object object = null;
      		if (col.size() > 0) object = col.iterator().next();
	      	if (object instanceof AbstractEGBox) {
//	      		System.err.println("TEXT WRAPPER STATE: " + ((AbstractEGBox)object).getWrapper());
		      	if (object instanceof EGEntityRef) {
//		      		System.err.println("TEXT WRAPPER2 STATE: " + ((EGEntityRef)object).getWrapper2());
		      	} else
		      	if (object instanceof EGConstraint) {
//		      		System.err.println("TEXT WRAPPER2 STATE: " + ((EGConstraint)object).getWrapper2());
		      	}
	      	} else if (object instanceof EGRelationSimple) {
//	      		System.err.println("TEXT WRAPPER STATE: " + ((EGRelationSimple)object).getWrapper());
	      	} else {
//		    	System.err.println("TEXT WRAPPER STATE: available only on single selection");
	      	}
	        break;
	    }
	}
	
	public void start() {
		setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_ARROW));
		super.start();
	}
}
