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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.Updateable;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.EGPageRef;
import jsdai.express_g.exp2.ui.PCSpeedStatistics;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.event.DrawListener;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.event.VectorListener;

/**
 * @(#) CommandHandler.java
 */

/**
 * implements all event handling interfaces
 */
public class CommandHandler implements Updateable, Paging, PageListener,
		HelpListener, KeyListener, MouseListener, MouseMoveListener, MouseTrackListener, TraverseListener {
  private Command command = null;

  private Stack commandStack = new Stack();

  private DrawComparator drawComparator = new DrawComparator();
  private Vector drawable = new Vector(1);
  private TreeSet drawableAll = new TreeSet(drawComparator);
  private boolean drawableValid = true;
  private TreeSet vectorListener = new TreeSet(drawComparator);
  private TreeSet drawListener = new TreeSet(drawComparator);
  private TreeSet pageListener = new TreeSet(drawComparator);

  protected PropertySharing prop;

  public CommandHandler(PropertySharing prop) {
    this.prop = prop;
//    visualPage = new VisualPage[]{};//new VisualPage(prop, 0), new VisualPage(prop, 1)};
    drawable.add(new TreeSet(drawComparator));
  }

  public void addVectorListener(VectorListener listener) {
    vectorListener.add(listener);
  }

  public boolean removeVectorListener(VectorListener listener) {
    return vectorListener.remove(listener);
  }

  public void addDrawListener(DrawListener listener) {
    drawListener.add(listener);
  }

  public boolean removeDrawListener(DrawListener listener) {
    return drawListener.remove(listener);
  }

  public void addPageListener(PageListener listener) {
    pageListener.add(listener);
  }

  public boolean removePageListener(PageListener listener) {
    return pageListener.remove(listener);
  }

  public boolean isDrawableValid() {
    return drawableValid;
  }

  public Iterator drawableRevIterator() {
    Vector drawrev = prop.handler().drawableCopy();
    Collections.reverse(drawrev);
    return drawrev.iterator();
  }

  public Iterator drawableRevIterator(int page) {
    if (page < 0) page = 0;
    while (drawable.size() <= page) drawable.add(new TreeSet());
    Vector draw = new Vector();
    draw.addAll(drawableAll);
    draw.addAll((Collection)drawable.get(page));
    Collections.reverse(draw);
    return draw.iterator();
  }

  public Iterator drawableIterator() {
    return drawableCopy().iterator();
  }
  
  public Iterator drawableIterator(int page) {
    if (page < 0) page = 0;
    while (drawable.size() <= page) drawable.add(new TreeSet(drawComparator));
    Vector draw = new Vector();
    draw.addAll(drawableAll);
    draw.addAll((Collection)drawable.get(page));
    return draw.iterator();
  }

  public Collection drawable(int page) {
  	TreeSet col = null;
  	if (page == Paging.ANY_PAGE) col = drawableAll; else 
  		if ((page >= 0) && (page <= getMaxPage())) col = (TreeSet)drawable.get(page);
  	if (col != null) col = (TreeSet)col.clone();
  	return col;
  }

  public int drawableSize(int page) {
  	TreeSet col = null;
  	if (page == Paging.ANY_PAGE) col = drawableAll; else 
  		if ((page >= 0) && (page <= getMaxPage())) col = (TreeSet)drawable.get(page);
  	return col == null ? 0 : col.size();
  }

  public int drawableSizeCP() {
  	TreeSet col = null;
  	if (page_nr == Paging.ANY_PAGE) col = drawableAll; else 
  		if ((page_nr >= 0) && (page_nr <= getMaxPage())) col = (TreeSet)drawable.get(page_nr);
  	return col == null ? 0 : col.size();
  }
  
  public Vector drawableCopy() {
    Vector draw = new Vector();
    draw.addAll(drawableAll);
    Iterator iter1 = drawable.iterator();
    while (iter1.hasNext()) {
      Iterator iter2 = ((TreeSet)iter1.next()).iterator();
      while (iter2.hasNext()) draw.add((Object)iter2.next());
    }
    return draw;
  }

  public void drawableClear() {
    Iterator iter = drawable.iterator();
    while (iter.hasNext()) ((Collection)iter.next()).clear();

    iter = vectorListener.iterator();
    while (iter.hasNext()) {
      VectorListener listener = (VectorListener)iter.next();
      listener.vectorRemoved(null);
    }
    drawableAll.clear();
    drawableAll.addAll(prop.getNonMovableDrawObjects());
    prop.setModified(true);
  }
/*
  public AbstractDraw drawableGet(int nr) {
    return (AbstractDraw)drawable.get(nr);
  }
*/
/*
  public int drawableSize() {
    return drawable.size();
  }
*/
  public void drawableAdd(AbstractDraw item) {
    int page = item.getPage();
    if (page == ANY_PAGE) {
      drawableAll.add(item);
    }
    else {
      if (page < 0) page = 0;
      while (drawable.size() <= page) drawable.add(new TreeSet(drawComparator));
      TreeSet draw = (TreeSet)drawable.get(page);
      draw.add(item);
    }

    item.addPageChangeListener(this);
    drawableValid = false;
    Iterator iter = vectorListener.iterator();
    while (iter.hasNext()) {
      VectorListener listener = (VectorListener)iter.next();
      listener.vectorAdded(item);
    }
    if (item instanceof AbstractEGObject) prop.setModified(true);
    
  }

  public void drawableAddR(AbstractDraw item) {
  	drawableAdd(item);
  }

  public boolean drawableRemove(AbstractDraw item) {
    int page = item.getPage();
    boolean rezult;
    if (page == ANY_PAGE) rezult = drawableAll.remove(item);
    else {
      if (page < 0)page = 0;
      if (page >= drawable.size())rezult = false;
      else {
      	TreeSet draw = (TreeSet)drawable.get(page);
        rezult = draw.remove(item);
      }
    }
    if (rezult) {
      item.removePageChangeListener(this);
      drawableValid = false;
      Iterator iter = vectorListener.iterator();
      while (iter.hasNext()) {
        VectorListener listener = (VectorListener)iter.next();
        listener.vectorRemoved(item);
      }
    }
    if (item instanceof AbstractEGObject) prop.setModified(true);
    return rezult;
  }

  /**
   * only for use in Command, no external use allowed
   */
  public void commandDone() {
/*
System.out.println("DONE:");
System.out.print(this.command + " (");
Iterator iter = commandStack.iterator();
while (iter.hasNext()) System.out.print(iter.next() + ", ");
System.out.println(")");
/**/
    if (!commandStack.empty()) {
      Object com = commandStack.pop();
      Command oldCommand = command;
      command = (Command)com;
      prop.status().setStatus(command.getStatus());
      oldCommand.finalizeCommand();
      fireCursorChanged();
    }
/*
System.out.print(this.command + " (");
iter = commandStack.iterator();
while (iter.hasNext()) System.out.print(iter.next() + ", ");
System.out.println(")");
/**/
  }

  public void repaint(boolean shrink) {
    Iterator iter = drawListener.iterator();
    while (iter.hasNext()) {
      DrawListener listener = (DrawListener)iter.next();
      listener.updateSize(shrink);
      listener.repaint();
    }
    drawableValid = true;
  }

  	public void draw(GC g) {
  		long time0 = System.currentTimeMillis();
  		int entity_count = 0;
  		boolean paintFailure = true;
  		while (paintFailure) {
  	  		paintFailure = false;
    		Rectangle image = prop.getPainting().getImageBounds();
  			if (image.width > 10 && image.height > 10) { // making sure that image was created succesfully
  	    		Iterator iter = drawableRevIterator(getPage());
  	  	  		while (iter.hasNext()) {
  	  	  			AbstractDraw item = (AbstractDraw)iter.next();
  	  	  			Rectangle bounds = item.getBounds();
  	  	  	  		boolean locationChanged = false; // to improve performance
  	  	  	  		boolean sizeChanged = false;
  	  	  			if (bounds.x + bounds.width > image.width + image.x) {
  	  	  				bounds.x = image.width + image.x - bounds.width;
  	  	  				locationChanged = true;
  	  	  			}
  	  	  			if (bounds.y + bounds.height > image.height + image.y) {
  	  	  				bounds.y = image.height + image.y - bounds.height;
  	  	  				locationChanged = true;
  	  	  			}
  	  	  			if (bounds.x < image.x) {
  	  	  				bounds.x = image.x;
  	  	  				locationChanged = true;
  	  	  			}
  	  	  			if (bounds.y < image.y) {
  	  	  				bounds.y = image.y;
  	  	  				locationChanged = true;
  	  	  			}
  	  	  			if (bounds.x + bounds.width > image.width + image.x) {
  	  	  				bounds.width = image.width + image.x - bounds.x;
  	  	  				sizeChanged = true;
  	  	  			}
  	  	  			if (bounds.y + bounds.height > image.height + image.y) {
  	  	  				bounds.height = image.height + image.y - bounds.y;
  	  	  				sizeChanged = true;
  	  	  			}
  	  	  			
  	  	  			if (locationChanged/* && !(item instanceof VisualPage)*/) {
  	  	  				item.setLocation(new Point(bounds.x, bounds.y));
  	  	  	  			paintFailure = true;
  	  	  			}
  	  	  			if (sizeChanged) {
  	  	  				item.setSize(new Point(bounds.width, bounds.height));
  	  	  	  			paintFailure = true;
  	  	  			}
  	  	  			
  	  	  			if (!paintFailure) {
  	  	  				item.draw(g);
  	  	  				entity_count++;
  	  	  			}
  	  	  		}
  	  	  		if (paintFailure) {
  	  	  			update(getPage());
//  	  	  			prop.getSelectionHandler().updateSizingPoints();
  	  	  		}
  	  		}
  		}
  		PCSpeedStatistics.add_statistics(entity_count, System.currentTimeMillis() - time0);
  	}

  	public void draw(GC g, Rectangle view) {
  		Iterator iter = drawableRevIterator(getPage());
  		long time0 = System.currentTimeMillis();
  		int entity_count = 0;
  		while (iter.hasNext()) {
  			AbstractDraw item = (AbstractDraw)iter.next();
  			if (view.intersects(item.getBounds())) {
  				item.draw(g);
  				entity_count++;
  			}
  		}
		PCSpeedStatistics.add_statistics(entity_count, System.currentTimeMillis() - time0);
  }

  /**
   * nr is used as page number
   */
  public void update(int nr) {
//    for (int i = 0; i < 1; i++) {
      Iterator iter = drawableIterator(nr);
      while (iter.hasNext()) {
        Updateable item = (Updateable)iter.next();
        item.update(1);
      }
      Collection topItems = prop.getPainting().topItems();
      if (topItems != null) {
          iter = topItems.iterator();
          while (iter.hasNext()) {
              Updateable item = (Updateable)iter.next();
              item.update(1);
          }
      }
      
//    }
  }

  public void update() {
    for (int i = 1; i <= getMaxPage(); i++) update(i);
  }
/**/
  
  private int page_nr = 1;
  /**
   * isOnPage
   * could be called: isDrawingPage()
   * returns true if the given page will be seen
   *
   * @param pgNr int
   * @return boolean
   * @todo Implement this jsdai.paint.Paging method
   */
  public boolean isOnPage(int pgNr) {
    return EGToolKit.pagesIdentical(page_nr, pgNr);
  }

  /**
   * setPage
   *
   * @param pgNr int
   * @todo Implement this jsdai.paint.Paging method
   */
  public void setPage(int pgNr) {
    if (pgNr > getMaxPage()) setMaxPage(pgNr);
    PageChangeEvent e = new PageChangeEvent(this, page_nr, pgNr);
    page_nr = pgNr;
    Iterator iter = pageListener.iterator();
    while (iter.hasNext()) {
      PageListener listener = (PageListener)iter.next();
      listener.pageChanged(e);
    }
//System.err.println("set current = " + page_nr);
  }

  /**
   * getPage
   *
   * @return int
   * @todo Implement this jsdai.paint.Paging method
   */
  public int getPage() {
    return page_nr;
  }

  private ArrayList visualPage = new ArrayList();

  public int getMaxPage() {
    return visualPage.size() - 1;
  }

  public void setMaxPage(int maxPage) {
  	boolean modified = false; 
  	while (visualPage.size() > maxPage + 1) {
  		VisualPage vp = (VisualPage)visualPage.remove(visualPage.size() - 1);
    	drawableRemove(vp);
  		vp.eliminate();
  		modified = true;
  	}
  	while (visualPage.size() <= maxPage) {
  		VisualPage vp = new VisualPage(prop, visualPage.size());
  		visualPage.add(vp);
    	drawableAdd(vp);
  		modified = true;
  	}
  	if (modified) prop.setModified(true);
  	/*
//System.err.println("set max(" + visualPage.length + ") = " + maxPage);
    VisualPage[] newVP = new VisualPage[maxPage + 1];
    System.arraycopy(visualPage, 0, newVP, 0, Math.min(visualPage.length, maxPage + 1));
    for (int i = maxPage + 1; i < visualPage.length; i++) {
    	drawableRemove(visualPage[i]);
    	visualPage[i].eliminate();
    }
    for (int i = visualPage.length; i <= maxPage; i++) {
    	newVP[i] = new VisualPage(prop, i);
    	drawableAdd(newVP[i]);
    }
    if (newVP.length != visualPage.length) prop.setModified(true);
    visualPage = newVP;/**/
  }

  public VisualPage getCurrentPage() {
//System.err.println("get page = " + (page_nr - 1));
    return (VisualPage)visualPage.get(page_nr);
  }

  public VisualPage setVisualPage(VisualPage visualPage) {
  	int pgNr = visualPage.getPage();
    if (pgNr >= this.visualPage.size()) setMaxPage(pgNr);
    int index = this.visualPage.indexOf(visualPage);
    if (index != pgNr) {
        VisualPage vpOld = (VisualPage)this.visualPage.set(pgNr, visualPage);
        if (index < 0) { 
            if (vpOld != null) {
            	drawableRemove(vpOld);
            }
            drawableAdd(visualPage);
            return vpOld;
        } else { // duplicate page
        	vpOld.setPage(index);
        	this.visualPage.set(index, vpOld); // replacing with old page
            return null;
        }
    } else
        return null;
  }

  public VisualPage getVisualPage(int nr) {
  	if (nr < 0 || nr >= visualPage.size()) return null;
  	else return (VisualPage)visualPage.get(nr);
  }

  /**
   * true if command stopped
   */
  public boolean stopCommand() {
//System.out.println("STOP?");
    boolean out = false;
    if (!commandStack.empty()) out = command.stop();
    return out;
  }

  /**
   * true if command started
  public boolean startCommand(Command command) {
    boolean out = false;
    prop.status().setStatus(command.getStatus());
    if (this.command == null) {
      this.command = command;
      command.init(prop);
      out = true;
    } else if (this.command.interrupt()) {
      commandStack.push(this.command);
      this.command = command;
      command.init(prop);
      out = true;
    } else if (this.command.stop()) {
      commandStack.push(this.command);
      this.command = command;
      command.init(prop);
      out = true;
    }
    prop.status().setStatus(this.command.getStatus());
    if (out) command.start();
    return out;
  }
  /**
   * true if command started
   */
  public boolean startCommand(Command command) {
/*
System.out.println("START:");
System.out.print(this.command + " (");
Iterator iter = commandStack.iterator();
while (iter.hasNext()) System.out.print(iter.next() + ", ");
System.out.println(")");
/**/
    boolean out = false;
    command.init(prop);
    prop.status().setStatus(command.getStatus());
    if (this.command == null) {
      this.command = command;
      out = true;
    } else if (this.command.interrupt()) {
      commandStack.push(this.command);
      this.command = command;
      out = true;
    } else if (this.command.stop()) {
      commandStack.push(this.command);
      this.command = command;
      out = true;
    }
    prop.status().setStatus(this.command.getStatus());
//System.out.println("<0XO><06>command: " + command);
    if (out) command.start();
/*
System.out.print(this.command + " (");
iter = commandStack.iterator();
while (iter.hasNext()) System.out.print(iter.next() + ", ");
System.out.println(")");
/**/
    return out;
  }

  public Command getRunningCommand() {
    return command;
  }

  public void fireCursorChanged(Cursor cursor) {
  	if (prop.getPainting() instanceof Control) {
  		Control control = (Control)prop.getPainting();
  		control.getDisplay().syncExec(new Runnable_SetCursor(control, cursor));
  	}
  }
  
  private static class Runnable_SetCursor implements Runnable {
  	private Control control;
  	private Cursor cursor;
  	
  	public Runnable_SetCursor(Control control, Cursor cursor) {
  		this.control = control;
  		this.cursor = cursor;
  	}
  	
	public void run() {
  		control.setCursor(cursor);
	}
  }
  
  public void fireCursorChanged() {
  	if (command != null) 
  		fireCursorChanged(command.getCursor());
  }
  
  /**
   * pageChanged
   *
   * @param pgNr int
   */
  public void pageChanged(PageChangeEvent e) {
    if (e.getSource() instanceof AbstractDraw) {
      AbstractDraw item = (AbstractDraw)e.getSource();
      int page = e.getOldPage();
      boolean exists;
      if (page == ANY_PAGE) {
      	exists = drawableAll.remove(item);
      } else {
        if (page < 0) page = 0;
        while (drawable.size() <= page) drawable.add(new TreeSet(drawComparator));
        TreeSet draw = (TreeSet)drawable.get(page);
        exists = draw.remove(item);
      }
//      System.out.println("PAGE CHANGED: " + e);
      if (exists) {
//        System.out.println("   EXISTS on page " + page);
        page = e.getNewPage();
        if (page == ANY_PAGE) {
          drawableAll.add(item);
        } else {
          if (page < 0) page = 0;
          while (drawable.size() <= page) drawable.add(new TreeSet(drawComparator));
          TreeSet draw = (TreeSet)drawable.get(page);
          draw.add(item);
        }
      }
    }
  }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.HelpListener#helpRequested(org.eclipse.swt.events.HelpEvent)
	 */
	public void helpRequested(HelpEvent e) {
		if (command instanceof HelpListener)
			((HelpListener)command).helpRequested(e);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if (command instanceof MouseMoveListener)
			((MouseMoveListener)command).mouseMove(e);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseEnter(MouseEvent e) {
		if (command instanceof MouseTrackListener)
			((MouseTrackListener)command).mouseEnter(e);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseExit(MouseEvent e) {
		if (command instanceof MouseTrackListener)
			((MouseTrackListener)command).mouseExit(e);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover(MouseEvent e) {
		if (command instanceof MouseTrackListener)
			((MouseTrackListener)command).mouseHover(e);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse.swt.events.TraverseEvent)
	 */
	public void keyTraversed(TraverseEvent e) {
		if (command instanceof TraverseListener)
			((TraverseListener)command).keyTraversed(e);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (command instanceof KeyListener)
			((KeyListener)command).keyPressed(e);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (command instanceof KeyListener)
			((KeyListener)command).keyReleased(e);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		if (command instanceof MouseListener)
			((MouseListener)command).mouseDoubleClick(e);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if (command instanceof MouseListener)
			((MouseListener)command).mouseDown(e);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if (command instanceof MouseListener)
			((MouseListener)command).mouseUp(e);
	}
	
	public void dispose() {
		vectorListener.clear();
		pageListener.clear();
		drawListener.clear();
		commandStack.clear();
		command = null;
		drawableClear();
		drawable.clear();
		drawableAll.clear();
		drawComparator = null;
		visualPage = null;
	}
	
	public void updatePageRefText() {
		Iterator iter = drawableIterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (item instanceof EGPageRef)
				((EGPageRef)item).updateWrapper();
		}
	}
}
