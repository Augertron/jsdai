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

package jsdai.express_g.exp2.ui.panels;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.ui.ContextMenu;
import jsdai.express_g.exp2.ui.PCSpeedStatistics;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.SimpleRect;
import jsdai.express_g.exp2.ui.SizingPoint;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.SelectionEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Mantas Balnys
 *
 */
public class PaintPanel extends Canvas implements IPaintPanel, PaintListener {
	
	public long DRAW_TIME_LIMIT = 500; // ms
	public long FAST_DRAW_TIME_LIMIT = 100; // ms
	private SimpleRect sr = new SimpleRect(); // special rounding rect for fast drawwing on very big selection
	public String WAITING_STRING = "DRAWING...";
	public Color WAITING_COLOR = ColorSchema.COLOR[ColorSchema.COLOR_GREEN];
	public Font WAITING_FONT = new Font(Display.getCurrent(), "Arial", 30, SWT.ITALIC | SWT.BOLD);
	
	private PropertySharing prop;
	private ScrolledComposite scroll;
	private GC gc = null;
	private Rectangle imageSize = new Rectangle(0, 0, 0, 0);
	private Rectangle lastImageSize = new Rectangle(0, 0, 0, 0);
	private Image image = null;
//	private Rectangle view = new Rectangle(0, 0, 0, 0);
	private Display display;
	
	private Collection topItems = new HashSet();
	
	/**
	 * @param parent
	 * @param style
	 */
	public PaintPanel(ScrolledComposite parent, int style) {
		super(parent, style | SWT.NO_BACKGROUND);
		display = getDisplay();
		scroll = parent;
		
		sr.setSelected(true);
//		setBackground(ColorSchema.COLOR_WHITE);
	}

	public Point getCenterLocation() {
		Rectangle rect = getClientArea();
		Point p = new Point(rect.width / 2 + rect.x, rect.height / 2 + rect.y);
		return p;
	}
	
	private void internalRepaint() {
		initImage();
		if ((image != null) && !image.isDisposed()) {
			gc = getLastGraphics();
			gc.setBackground(ColorSchema.COLOR[ColorSchema.COLOR_LIGHT_GRAY]);
			gc.fillRectangle(image.getBounds());
			prop.handler().draw(gc);
		}
	}
	
	public void repaint() {
//		internalRepaint();
		closeImage();
		if (redrawRunnable == null) redrawRunnable = new RedrawRunnable();
		display.asyncExec(redrawRunnable);
	}
	
	private RedrawRunnable redrawRunnable = null;
	
	private class RedrawRunnable implements Runnable {
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			redraw();
		}
	}
	
	public void setCenterLocation(Point location) {
		Rectangle rect = getClientArea();
		setLocation((int)(rect.width / 2 - location.x), (int)(rect.height / 2 - location.y));
	}
	
	public void updateSize(boolean shrink) {
		int INSET = 10;
	    Rectangle visible = scroll.getClientArea();
	    Rectangle draw = new Rectangle(0, 0, 1, 1);
	    if (!shrink) draw.add(new Rectangle(imageSize.x, imageSize.y, imageSize.width - INSET, imageSize.height - INSET));
	    Iterator iter = prop.handler().drawableIterator(prop.handler().getPage());
	    while (iter.hasNext()) {
	    	Drawable item = (Drawable)iter.next();
	    	Rectangle itemBounds = item.getBounds();
	    	if ((itemBounds.width > EGToolKit.MAX_PAGE_WIDTH) || (itemBounds.height > EGToolKit.MAX_PAGE_HEIGHT)) {
	    		// skip big sized bounds (usualy damaged)
	    	} else {
		    	draw.add(itemBounds);
	    	}
	    }
	    draw.width += INSET;
	    draw.height += INSET;
	    
	    // set up with visible:
	    draw.add(visible);
	    if ((imageSize.width != draw.width)||(imageSize.height != draw.height)) {
	    	closeImage();
			imageSize = draw;
//	    	setSize(draw.width, draw.height);
	    }
	}
	
	public void pageChanged(PageChangeEvent e) {
		updateSize(true);
		repaint();
	}
	
	public void selectionChanged(SelectionEvent e) {
//		redraw();
	}
	
	private void closeImage() {
		if (gc != null && !gc.isDisposed()) gc.dispose();
		gc = null;
		if (image != null && !image.isDisposed()) image.dispose();
		image = null;
	}
	
	/**
	 * frees up some resources when panel is inactive
	 *
	 */
	public void suspend() {
		closeImage();
	}
	
	private void initImageInternal(int page_width, int page_height) {
		closeImage();
		try {
			image = new Image(display, page_width, page_height);
		} catch (SWTException e) {
			image = null;
			prop.status().log(e);
		} catch (SWTError e) {
			image = null;
			prop.status().log(e);
		}
	}
	
	private void initImage() {
		int page_height = Math.min(imageSize.height, EGToolKit.MAX_PAGE_HEIGHT);
		int page_width = Math.min(imageSize.width, EGToolKit.MAX_PAGE_SIZE / page_height);
		initImageInternal(page_width, page_height);
		if (image == null || image.isDisposed()) {
			page_height = lastImageSize.height;
			page_width = lastImageSize.width;
		}
		while ((image == null || image.isDisposed()) && page_height > 10 && page_width > 10) {
			initImageInternal(page_width, page_height);
			if (image == null || image.isDisposed()) {
				page_height = (int)(page_height / Math.sqrt(2));
				page_width = (int)(page_width / Math.sqrt(2));
			}
		}
		if (image != null && !image.isDisposed()) {
			setSize(page_width, page_height);
			lastImageSize = new Rectangle(0, 0, page_width, page_height);
		}
	}
	
	public GC getLastGraphics() {
		if ((gc == null)||(gc.isDisposed())) {
			if (image == null || image.isDisposed()) initImage();
			if (image != null && !image.isDisposed()) 
				gc = new GC(image);
			else 
				gc = new GC(this);
			try { // needs "cairo" library
				gc.setAntialias(SWT.ON);
				gc.setTextAntialias(SWT.ON);
			} catch (SWTException e) {
				// Ignore antialiasing problems
				//SdaieditPlugin.log(e); // DEBUG
			}

		}
		return gc;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl(PaintEvent e) {
		if (image == null || image.isDisposed()) {
// temporary image
			if (PCSpeedStatistics.get_draw_time(prop.handler().drawableSizeCP()) > DRAW_TIME_LIMIT) {
				e.gc.setForeground(WAITING_COLOR);
				e.gc.setFont(WAITING_FONT);
				Rectangle visible = scroll.getClientArea();
				Point start = scroll.getOrigin();
				e.gc.drawString(WAITING_STRING, (int)(start.x + visible.width / 2.5), (int)(start.y + visible.height / 2.5), true);
			}
// ------------			
			updateSize(true);
			internalRepaint();
		}
//		e.gc.setBackground(ColorSchema.COLOR_LIGHT_GRAY);
//		e.gc.fillRectangle(getBounds());
		if (image != null && !image.isDisposed()) {
			e.gc.drawImage(image, 0, 0);
			if (topItems != null) {
				Iterator iter = topItems.iterator();
				if (PCSpeedStatistics.get_draw_time(topItems.size()) > FAST_DRAW_TIME_LIMIT) {
					Rectangle rect = null;
					while (iter.hasNext()) {
						Object item = iter.next();
						if ((item instanceof Drawable) && !((item instanceof Paging) && !((Paging)item).isOnPage(prop.handler().getPage()))) {
							if (rect == null) rect = ((Drawable)item).getBounds();
							else rect.add(((Drawable)item).getBounds());
						}
					}
					sr.setBounds(rect);
					sr.draw(e.gc);
				} else {
					while (iter.hasNext()) {
						Object item = iter.next();
						if ((item instanceof Drawable) && !((item instanceof Paging) && !((Paging)item).isOnPage(prop.handler().getPage()))) {
							((Drawable)item).draw(e.gc);
						}
					}
				}
				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
//System.err.println("DISPOSING PaintPanel");		
		closeImage();
		WAITING_FONT.dispose();
		super.dispose();
	}
	
	public void setProperties(PropertySharing prop) {
		this.prop = prop;
		prop.setPainting(this);
		
	    prop.handler().addDrawListener(this);
	    prop.handler().addPageListener(this);
	    prop.getSelectionHandler().addSelectionListener(this);
	    addPaintListener(this);
	    addHelpListener(prop.handler());
	    addKeyListener(prop.handler());
	    addMouseListener(prop.handler());
	    addMouseMoveListener(prop.handler());
	    addMouseTrackListener(prop.handler());
	    addTraverseListener(prop.handler());
	    
	    ContextMenu cMenu = new ContextMenu(prop);
	    Menu menu = cMenu.createContextMenu(this);
	    setMenu(menu);
	}

	/**
	 * @return Returns the topItems.
	 */
	public Collection topItems() {
		return topItems;
	}
	
	/**
	 * @return maximum drawable panel size
	 */
	public Rectangle getImageBounds() {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		if (image != null && !image.isDisposed()) {
			Rectangle imageBounds = image.getBounds();
			bounds.x = imageBounds.x + SizingPoint.pointSize; 
			bounds.y = imageBounds.y + SizingPoint.pointSize; 
			bounds.width = imageBounds.width - SizingPoint.pointSize * 2;
			bounds.height = imageBounds.height - SizingPoint.pointSize * 2;
		}
		return bounds;
//		return new Rectangle(imageSize.x, imageSize.y, imageSize.width - SizingPoint.pointSize, imageSize.height - SizingPoint.pointSize);
	}

}
