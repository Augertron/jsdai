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

package jsdai.express_g.exp2;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import jsdai.express_g.common.StaticTools;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.CommandHandler;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @(#) AbstractDraw.java
 */

public abstract class AbstractDraw implements Updateable, Selectable,
    Paging, IPropertySource {
  private Rectangle bounds = new Rectangle(0, 0, 0, 0);
  public static final String PROP_ID_NAME = "PROP_ID_NAME";
  private String name = "";
  private boolean updating = false;
  private boolean selected = false;
  public ColorSchema selectedSchema = new ColorSchema(ColorSchema.COLOR_RED, ColorSchema.COLOR_LIGHT_GRAY);
  public ColorSchema simpleSchema = new ColorSchema();

  	private static class CHComparator implements Comparator {
  		public int compare(Object o1, Object o2) {
  			String s1 = (o1 instanceof CommandHandler) ? "1" : "2" + Integer.toHexString(o1.hashCode());
  			String s2 = (o2 instanceof CommandHandler) ? "1" : "2" + Integer.toHexString(o2.hashCode());
  			return s1.compareTo(s2);
  		}
  	}
  
  	protected TreeSet pageListeners = new TreeSet(new CHComparator());

  	private PropertySharing prop = null; 

  protected AbstractDraw() {
  }

  public AbstractDraw(PropertySharing prop) {
  	setProp(prop);
  }
  
  public void addPageChangeListener(PageListener listener) {
    pageListeners.add(listener);
  }

  public void removePageChangeListener(PageListener listener) {
    pageListeners.remove(listener);
  }

  public void firePageChanged(PageChangeEvent e) {
    Iterator iter = pageListeners.iterator();
    while (iter.hasNext()) {
//System.out.println("<0XO><12>to page listeners: " + e);
    	((PageListener)iter.next()).pageChanged(e);
    }
  }

  public void update() {
    update(0);
  }

  public boolean isSelected() {
    return selected;
  }

  public boolean objectAt(Point p) {
    return getBounds().contains(p);
  }

  public boolean objectAt(Rectangle r) {
  	Rectangle bounds = getBounds();
    return r.contains(bounds.x, bounds.y) && r.contains(bounds.x + bounds.width, bounds.y + bounds.height);
  }

  /**
  * returns true if changed
  */
  public boolean setSelected(boolean selected) {
    boolean dif = this.selected != selected;
    this.selected = selected;
    return dif;
  }

  public Rectangle getBounds() {
  	Point loc = getLocation();
  	Point siz = getSize();
    return new Rectangle(loc.x, loc.y, siz.x, siz.y);
  }

  public Point getLocation() {
    return new Point(bounds.x, bounds.y);
  }

  public String getName() {
    return name;
  }

  public Point getSize() {
    return new Point(bounds.width, bounds.height);
  }

  public void setBounds(Rectangle bounds) {
    setLocation(new Point(bounds.x, bounds.y));
    setSize(new Point(bounds.width, bounds.height));
  }

  public void translate(Point move) {
    Point p = getLocation();
    p.x += move.x;
    p.y += move.y;
    setLocation(p);
  }

  protected PropertySharing prop() {
	  return prop;
  }
  
  protected void setProp(PropertySharing prop) {
	  this.prop = prop;
  }
  
  public void setLocation(Point location) {
//System.out.println("in setLocation, bounds.x: " + bounds.x + ", location.x: " + location.x + ", bounds.y: " + bounds.y + ", location.y: " + location.y);
  	if ((bounds.x != location.x) || (bounds.y != location.y)) {
  	    bounds.x = location.x;
  	    bounds.y = location.y;
  	  if (prop() != null) prop().setModified(true);
  	}
  }

  public void setName(String name) {
  	if (!StaticTools.equalStrings(this.name, name)) {
  	    this.name = name;
  	  if (prop() != null) prop().setModified(true);
  	}
  }

  public void setSize(Point size) {
  	if ((bounds.width != size.x) || (bounds.height != size.y)) {
  	    bounds.width = size.x;
  	    bounds.height = size.y;
  	  if (prop() != null) prop().setModified(true);
  	}
  }

  public boolean isUpdating() {
    return updating;
  }

  public void setUpdating(boolean updating) {
    this.updating = updating;
  }

  private int page_nr = DEFAULT_PAGE;
  /**
   * isOnPage
   *
   * @param pgNr int
   * @return boolean
   * @todo Implement this jsdai.paint.Paging method
   */
  public boolean isOnPage(int pgNr) {
    return EGToolKit.pagesIdentical(getPage(), pgNr);
  }

  /**
   * returns item behaviour on page
   * locked means item is unmovable by user 
   * @return
   */
  public boolean locked() {
  	return false;
  }
  
  /**
   * setPage
   *
   * @param pgNr int
   */
  public void setPage(int pgNr) {
    PageChangeEvent e = new PageChangeEvent(this, page_nr, pgNr);
    page_nr = pgNr;
    if (prop() != null) prop().setModified(true);
//System.out.println("<0XO><11>sePage: " + pgNr + ", event: " + e);
    firePageChanged(e);
  }

  /**
   * getPage
   *
   * @return int
   */
  public int getPage() {
    return page_nr;
  }

  public Selectable selectAsFirst(int type) {
    return null;
  }

  public Selectable selectSecond(int type, Selectable second) {
    return null;
  }

  protected boolean eliminated = false;
  
  public boolean isEliminated() {
  	return eliminated;
  }
  
  public void eliminate() {
  	eliminated = true;
  }
  
  
  	// TODO properties DEBUG
	public Object getEditableValue() {
		return null;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
			new TextPropertyDescriptor(PROP_ID_NAME, "name")	
		};
	}
	
	public Object getPropertyValue(Object id) {
		if (PROP_ID_NAME.equals(id))
			return getName();
		
		return null;
	}
	
	public boolean isPropertySet(Object id) {
		if (PROP_ID_NAME.equals(id))
			return true;
		
		else return false;
	}
	
	public void resetPropertyValue(Object id) {
	}
	
	public void setPropertyValue(Object id, Object value) {
	}
	// properties
}
