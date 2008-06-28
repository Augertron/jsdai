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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.Selection;
import jsdai.express_g.exp2.Updateable;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.ui.PCSpeedStatistics;
import jsdai.express_g.exp2.ui.SimpleRect;
import jsdai.express_g.exp2.ui.SizingPoint;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.panels.PaintPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author Mantas Balnys
 * 
 */
public class FastSelectCommand extends SelectCommand {
	protected boolean itemsMoved = false;
	
	/**
	 * increases drawing speed, decreases gui effects
	 * 
	 * 1 and more - disabled box updating on move
	 * 2 and more - disabled relation updating on move
	 *  
	 */
	public int FAST_DRAW_LEVEL = 0;
	
	/**
	 * @param invoker
	 */
	public FastSelectCommand(CommandInvoker invoker) {
		super(invoker);
	}

	/**
	 * full copy of supertype, if changing supertype method synchronize changes
	 * here also
	 */
	public void mouseDown(MouseEvent e) {
		dragging = true;
		boolean found = false;
		Selection item = null;
		Point epoint = new Point(e.x, e.y);

		// search through top level items XXX
		Iterator iter = prop.getPainting().topItems().iterator();
		while (!found && iter.hasNext()) {
			item = (Selection) iter.next();
			if (((Paging) item).isOnPage(prop.handler().getPage())) {
				if (!(item instanceof VisualPage)) // skipping Page frame
					found = item.objectAt(epoint);
			}
		}
		
		// search through drawable eg objects
		if (!found) { 
			iter = prop.handler().drawableIterator(prop.handler().getPage());
			while (!found && iter.hasNext()) {
				item = (Selection) iter.next();
				if (((Paging) item).isOnPage(prop.handler().getPage())) {
					if (!(item instanceof VisualPage)) // skipping Page frame
						found = item.objectAt(epoint);
				}
			}
		}

		if (!found)
			item = null;
		if (item instanceof SizingPoint)
			draggingSizingPoint = (SizingPoint) item;
		else
			draggingSizingPoint = null;

		if (!found) {
			selDrag = true;
			selRect.setLocation(epoint);
			selRect.setSize(new Point(0, 0));
//				prop.handler().drawableAdd(selRect); XXX
			prop.getPainting().topItems().add(selRect);
		} else 	if (item.isSelected()) {
			if ((e.stateMask & SWT.SHIFT) != 0 || (e.stateMask & SWT.CONTROL) != 0) {
				// if (sizingItem != null) deleteSizingPoints();
				removeSelected(item);
				// prop.handler().repaint(false);
			}
		} else {
			// if (sizingItem != null) deleteSizingPoints();
			if ((e.stateMask & SWT.SHIFT) != 0 || (e.stateMask & SWT.CONTROL) != 0) {
				addSelected(item);
			} else {
				setSelected(item);
				// if (item instanceof AbstractEGObject)
				// createSizingPoints(item);
			}
//			prop.handler().repaint(false); XXX
		}
		mouseP0.x = e.x;
		mouseP0.y = e.y;
		prop.getPainting().redraw();
	}

	/**
	 * full copy of supertype, if changing supertype method synchronize changes
	 * here also
	 */
	public void mouseUp(MouseEvent e) {
		dragging = false;
		if (selDrag) {
			selDrag = false;
//			prop.handler().drawableRemove(selRect); XXX
			prop.getPainting().topItems().remove(selRect);
			Iterator iter = prop.handler().drawableIterator(
					prop.handler().getPage());
			HashSet sel = new HashSet();
			Rectangle rectangle = selRect.getBounds();
			int currentPage = prop.handler().getPage();
			while (iter.hasNext()) {
				Selection item = (Selection) iter.next();
				if (((Paging) item).isOnPage(currentPage))
					if (item.objectAt(rectangle))
						sel.add(item);
			}
			if (e != null && sel.size() == 0) { // try select Page frame if nothing else is
									// selected
				VisualPage page = prop.handler().getCurrentPage();
				if (page.objectAt(new Point(e.x, e.y)))
					sel.add(page);
			}
			if (e != null && (e.stateMask & SWT.SHIFT) != 0) {
				addSelected(sel);
			} else
				setSelected(sel);
			// prop.handler().repaint(true);
			prop.getPainting().redraw();
		} else {
			if (itemsMoved) {
				Iterator iter = selected.iterator();
				while (iter.hasNext()) {
					Selection item = (Selection) iter.next();
//					if (canBeOnTop(item)) 
					((Updateable)item).update(3);
				}
				prop.handler().repaint(true);
				itemsMoved = false;
			}
		}
//		prop.handler().repaint(true);
	}

	/**
	 * full copy of supertype, if changing supertype method synchronize changes
	 * here also
	 */
	public void mouseMove(MouseEvent e) {
		if (dragging) {
			if (selDrag) {
				Point loc = new Point(mouseP0.x, mouseP0.y);
				loc.x += Math.min(0, e.x - mouseP0.x);
				loc.y += Math.min(0, e.y - mouseP0.y);
				selRect.setLocation(loc);
				Point size = new Point(Math.abs(mouseP0.x - e.x), Math
						.abs(mouseP0.y - e.y));
				selRect.setSize(size);
//				prop.getPainting().repaint();  XXX
				prop.getPainting().redraw();
			} else if (selected.size() > 0) {
				itemsMoved = true;
				Point loc = new Point(e.x - mouseP0.x, e.y - mouseP0.y);
				if (draggingSizingPoint == null) {
					Iterator iter = selected.iterator();
					while (iter.hasNext()) {
						AbstractDraw item = (AbstractDraw)iter.next();
						item.translate(loc);
						if (FAST_DRAW_LEVEL <= 0) item.update(3);
					}
					for (int i = 0; i < 4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
					if (FAST_DRAW_LEVEL > 0) prop.getPainting().redraw();
						else prop.handler().repaint(false);
				} else {
					draggingSizingPoint.translateSizing(loc);
					if (sizingItem instanceof AbstractEGRelation) {
						if (FAST_DRAW_LEVEL > 1) {
							prop.getPainting().redraw();
						} else {
							((Drawable)sizingItem).draw(prop.getPainting().getLastGraphics());
							((Updateable)sizingItem).update(2); 
							for (int i = 0; i < 4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
							prop.getPainting().repaint();
						}
					} else {
						if (!canBeOnTop(sizingItem)) {
							((Drawable)sizingItem).draw(prop.getPainting().getLastGraphics());
							((Updateable)sizingItem).update(2); 
							for (int i = 0; i < 4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
							prop.getPainting().repaint();
						} else {
							for (int i = 0; i < 4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
							prop.getPainting().redraw();
						}
					}
/*					
					if (canBeOnTop(sizingItem)) 
						prop.getPainting().redraw();
					else 
						prop.getPainting().repaint();*/
				}
				mouseP0.x = e.x;
				mouseP0.y = e.y;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.command.SelectCommand#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover(MouseEvent e) {
		if ((dragging)&&(draggingSizingPoint != null)) {
			if (sizingItem instanceof AbstractEGRelation) {
				if (FAST_DRAW_LEVEL > 1) { // upgrade 2005-10-27
					((Drawable)sizingItem).draw(prop.getPainting().getLastGraphics());
					((Updateable)sizingItem).update(2); 
					for (int i = 0; i < 4; i++) if (sizingPoint[i] != null) sizingPoint[i].update();
					prop.getPainting().repaint();
				}
			} else {
				Point newloc = draggingSizingPoint.getLocation();
				newloc.x += draggingSizingPoint.getSize().x / 2;
				newloc.y += draggingSizingPoint.getSize().y / 2;
				mouseP0.x = newloc.x;
				mouseP0.y = newloc.y;
				e.display.setCursorLocation(((PaintPanel)prop.getPainting()).toDisplay(newloc));
			}
		}
	}

	protected void createSizingPoints(AbstractDraw item) {
	    sizingItem = item;
//		if (canBeOnTop(item)) prop.getPainting().topItems().add(sizingItem); // XXX
//		if (!(item instanceof VisualPage)) prop.getPainting().topItems().add(sizingItem); // XXX
	    if (item instanceof AbstractEGRelation) {
	    	sizingPoint[0] = new SizingPoint(sizingItem, SizingPoint.PLACE_RELATION_HINT);
	    	sizingPoint[0].setSelected(true);
//	    	selected.add(sizingPoint[0]);
	    	//prop.handler().drawableAdd(sizingPoint[0]); XXX
	    	prop.getPainting().topItems().add(sizingPoint[0]);
	    	sizingPoint[0].update();
	    	sizingPoint[0].setPage(((Paging)item).getPage());
	    	sizingPoint[1] = new SizingPoint(sizingItem, SizingPoint.PLACE_RELATION_TEXT);
	    	sizingPoint[1].setSelected(true);
//	    	selected.add(sizingPoint[1]);
	    	//prop.handler().drawableAdd(sizingPoint[1]); XXX
			prop.getPainting().topItems().add(sizingPoint[1]);
			sizingPoint[1].update();
			sizingPoint[1].setPage(((Paging)item).getPage());
	    } else {
	      for (int i = 0; i < 4; i++) {
	        sizingPoint[i] = new SizingPoint(sizingItem, i);
	        sizingPoint[i].setPage(((Paging)item).getPage());
	        sizingPoint[i].setSelected(true);
//	        selected.add(sizingPoint[i]);
//	        prop.handler().drawableAdd(sizingPoint[i]); XXX
			prop.getPainting().topItems().add(sizingPoint[i]);
	        sizingPoint[i].update();
	      }
	    }
	  }

	protected void deleteSizingPoints() {
//		prop.getPainting().topItems().remove(sizingItem); // XXX
		sizingItem = null;
	    for (int i=0; i<4; i++) if (sizingPoint[i] != null) {
	    	//prop.handler().drawableRemove(sizingPoint[i]); XXX
	    	prop.getPainting().topItems().remove(sizingPoint[i]);
//	    	selected.remove(sizingPoint[i]);
	    	sizingPoint[i] = null;
	    }
	}
	
	protected boolean canBeOnTop(Object item) {
		return ((item instanceof SimpleRect)
				|| (item instanceof SizingPoint)
				|| ((FAST_DRAW_LEVEL > 0) && !(
				(item == null)
				|| (item instanceof AbstractEGRelation)
				|| (item instanceof VisualPage)
				)));
	}
	

	/**
	 * 
	 */
	public void setSelected(Object invoker, Set selected) {
	    if ((this.selected.size() != selected.size())||(!this.selected.containsAll(selected))) {
	    	GC gc = prop.getPainting().getLastGraphics();
	    	if (sizingItem != null) deleteSizingPoints();
	    	boolean needs_full_repaint = PCSpeedStatistics.get_draw_time(prop.handler().drawableSizeCP()) < PCSpeedStatistics.TIME_TO_REDRAW_ON_DESELECT;
    		boolean repaint_relation = PCSpeedStatistics.get_draw_time(prop.handler().drawableSizeCP()) < PCSpeedStatistics.TIME_TO_REDRAW_ON_DESELECT_RELATION;
	    	int current_page = prop.handler().getPage();
	    	Set oldSel = getSelected();
	    	Iterator iter = this.selected.iterator();
	    	while (iter.hasNext()) { //XXX
	    		AbstractDraw item = (AbstractDraw)iter.next();
	    		item.setSelected(false);
	    		if (canBeOnTop(item)) {
	    			prop.getPainting().topItems().remove(item);
	    		}
	    		if (!needs_full_repaint) {  // FIX 2006-01-05 deselected item overlaps other items (incorrect drawing order) 
	    			if (repaint_relation && item instanceof AbstractEGRelation) needs_full_repaint = true;
	    			else if (!(item instanceof VisualPage) && item.isOnPage(current_page)) item.draw(gc); //XXX
	    		} 
	    	}
	    	this.selected.clear();
	    	
			if (PCSpeedStatistics.get_draw_time(prop.handler().drawableSizeCP()) < PCSpeedStatistics.TIME_TO_UPDATEABLE_MOVE)
				FAST_DRAW_LEVEL = 0;
			else
				FAST_DRAW_LEVEL = 2;
	    	
	    	this.selected.addAll(selected);
	    	iter = selected.iterator();
	    	while (iter.hasNext()) { //XXX
	    		AbstractDraw item = (AbstractDraw)iter.next();
	    		item.setSelected(true);
	    		if (canBeOnTop(item)) {
	    			prop.getPainting().topItems().add(item);
	    		}
	    		if (!needs_full_repaint) {
	    			if (repaint_relation && item instanceof AbstractEGRelation) needs_full_repaint = true;
	    			if (!(item instanceof VisualPage) && item.isOnPage(current_page)) item.draw(gc); //XXX
	    		}
	    	}
	    	if (selected.size() == 1) createSizingPoints((AbstractDraw)selected.iterator().next());
	    	prop.status().setStatus(getStatus());
//	      prop.setModified(true);
	    	fireSelectionChanged(invoker, oldSel, selected);
	    	if (needs_full_repaint) prop.handler().repaint(false);
	    }
	}

}



