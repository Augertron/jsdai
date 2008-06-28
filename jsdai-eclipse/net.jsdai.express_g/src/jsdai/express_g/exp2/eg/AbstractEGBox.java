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

package jsdai.express_g.exp2.eg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import jsdai.SExpress_g_schema.EData_type_placement;
import jsdai.SExpress_g_schema.ELocation;
import jsdai.SExpress_g_schema.ESize;
import jsdai.express_g.common.TextWrapper;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.IXMLDefinition;
import jsdai.express_g.exp2.Paging;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @(#) AbstractEGBox.java
 */

public abstract class AbstractEGBox extends AbstractEGObject implements IXMLDefinition {
	public static final int basicWidth = 40;
  	public static final int basicHeight = 25;
  	public int textInset = 3;
	public static final String NAME_DELIMITERS = "_ \n\t\r.,/;:|";
	protected TextWrapper wrapper = null;

  protected Vector wires = new Vector();
  
  	public AbstractEGBox(PropertySharing prop) {
  		super(prop);
		wrapper = new TextWrapper(getText(), "");
		wrapper.setStyle(TextWrapper.STYLE_DELIM_AT_BACK);
  	}
  	
  	protected AbstractEGBox() {
  		super();
		wrapper = new TextWrapper(getText(), "");
		wrapper.setStyle(TextWrapper.STYLE_DELIM_AT_BACK);
  	}
  	
  	/**
  	 * created for debugging use only
  	 * @return
  	 */
  	public TextWrapper getWrapper() {
  		return wrapper;
  	}
  
  protected void drawInvisibleBox(GC g) {
    ColorSchema schema = isSelected() ? selectedSchema : nonVisibleSchema;
    schema.apply(g);
	g.setFont(prop().getFont1());

    Rectangle bounds = getBounds();
    g.fillRectangle(bounds);
    g.drawRectangle(bounds);
  }

  	public void draw(GC g) {
  		Rectangle bounds = getBounds();
  		
  		if (isVisible()) {
  			ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
  			schema.apply(g);
  			g.setFont(prop().getFont1());
  	  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
  	  			wrapper.setGC(g);
  	  			if (isLabelNew()) {
  	  				setBounds(new Rectangle(bounds.x, bounds.y, 0, 0));
  	  			} else {
  	  	  			setBounds(bounds);
  	  			}
  	  			bounds = getBounds();
  	  		}

		    g.fillRectangle(bounds);
		    g.drawRectangle(bounds);
		    
		    Rectangle textPlace = getTextPlace();
			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				g.drawString(wrapper.getLine(i), 
						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
			}
  		} else {
  			drawInvisibleBox(g);
  		}
  	}
  	
  	public int getWireCount() {
  		return wires.size();
  	}

  public Vector getWires() {
    return (Vector)wires.clone();
  }

  public Wire getWire(AbstractEGRelation relation, boolean second) {
    int i = wires.indexOf(relation);
    if (second) i = wires.indexOf(relation, i + 1);
/*
if (i < 0) System.err.println(relation + "<" + relation.getClass() + "> INDEX < 0 OF " + wires.size() + " IN " + this + " <" + this.getClass() + "> " + (second?"searching second":"no second"));
if (i < 0) {
  Iterator wit = relation.getParent().getWires().iterator();
System.err.println("PARENT: " + relation.getParent() + " <" + relation.getParent().getClass().getName() + ">");
  while (wit.hasNext()) {
    Wire wire = (Wire)wit.next();
    System.err.println(wire.getRelation().getParent() + " <" + wire.getRelation().getParent().getClass().getName() + "> -"
                       + (wire.getRelation() instanceof EGRelationSimple?"O":"E") +
                       wire.getRelation().getChild() + " <" + wire.getRelation().getChild().getClass().getName() + ">");
  }
System.err.println("-" + (relation instanceof EGRelationSimple?"O":"E"));
System.err.println("CHILD: " + relation.getChild() + " <" + relation.getChild().getClass().getName() + ">");
  wit = relation.getChild().getWires().iterator();
  while (wit.hasNext()) {
    Wire wire = (Wire)wit.next();
    System.err.println(wire.getRelation().getParent() + " <" + wire.getRelation().getParent().getClass().getName() + "> -"
                       + (wire.getRelation() instanceof EGRelationSimple?"O":"E") +
                       wire.getRelation().getChild() + " <" + wire.getRelation().getChild().getClass().getName() + ">");
  }
System.err.println();
System.err.println("THIS: " + this + " <" + this.getClass().getName() + ">");
  wit = this.getWires().iterator();
  while (wit.hasNext()) {
    Wire wire = (Wire)wit.next();
    System.err.println(wire.getRelation().getParent() + " <" + wire.getRelation().getParent().getClass().getName() + "> -"
                       + (wire.getRelation() instanceof EGRelationSimple?"O":"E") +
                       wire.getRelation().getChild() + " <" + wire.getRelation().getChild().getClass().getName() + ">");
  }
}
*/
    return (Wire)wires.get(i);
  }

  /**
   * relation must contain not null parent
   * @param relation AbstractEGRelation
   */
  public void addWire(AbstractEGRelation relation) {
    boolean att = relation.getParent() == this;
    if (wires.contains(relation)) att = false;
    Wire wire = new Wire(this, relation, att);
    if (att) wires.add(wire);
    else wires.add(0, wire);
    groupingChanged();
  }

  public boolean removeWire(AbstractEGRelation relation) {
    groupingChanged();
    return wires.remove(relation);
  }

  public int getDirection(AbstractEGRelation relation, boolean second) {
    return getWire(relation, second).getDirection();
  }

  /**
   * true if something changed
   * @return
   */
  public boolean sortWiresByNearest() {
    Iterator iter = wires.iterator();
    boolean changed = false;
    while (iter.hasNext()) changed |= ((Wire)iter.next()).updateAngle();
    if (changed) Collections.sort(wires);
    return changed;
  }

  public Point getPosition(AbstractEGRelation relation, boolean second) {
    Wire wire = getWire(relation, second);
    int direction = wire.getDirection();
    Iterator iter = wires.iterator();
    Wire wire2 = null;
    int count = 0;
    HashSet groups = new HashSet();
    while ((iter.hasNext())&&(wire != wire2)&&
    		!((wire2 != null) && wire.isAttribute() && wire2.isAttribute() && 
    		(wire.getRelation().getGroup() == wire2.getRelation().getGroup()) &&
			(wire2.getDirection() == direction) )) {
      wire2 = (Wire)iter.next();
/**@todo uncoment if ref to N page is not visible*/
      if ((!wire2.getRelation().isOnPage(INVISIBLE_PAGE))&&(wire2.getDirection() == direction)
      		&&(!wire2.isAttribute() || !groups.contains(wire2.getRelation().getGroup()))) {
      	count++;
      	groups.add(wire2.getRelation().getGroup());
      }
    }
    int size = count;
//    count = groups.indexOf(wire.getRelation().getGroup()) + 1;
    while (iter.hasNext()) {
      wire2 = (Wire)iter.next();
/**@todo uncoment if ref to N page is not visible*/
      if ((!wire2.getRelation().isOnPage(INVISIBLE_PAGE))&&(wire2.getDirection() == direction)
      		&&(!wire2.isAttribute() || !groups.contains(wire2.getRelation().getGroup()))) {
      	size++;
      	groups.add(wire2.getRelation().getGroup());
      }
    }
    //size++; // one for empty space
    
    Rectangle bounds = getBounds();
    Point p = new Point(0, 0);
    double dd;
    switch (direction) {
      case Wire.DIRECTION_RIGHT :
        dd = (double)bounds.height / (double)size;
        p.x = bounds.x + bounds.width + 1;
        p.y = bounds.y + (int)((count - 0.5) * dd);
        break;
      case Wire.DIRECTION_TOP :
        dd = (double)bounds.width / (double)size;
        p.x = bounds.x + (int)((count - 0.5) * dd);
        p.y = bounds.y;
        break;
      case Wire.DIRECTION_LEFT :
        dd = (double)bounds.height / (double)size;
        p.x = bounds.x;
        p.y = bounds.y + (int)((size - count + 0.5) * dd);
        break;
      case Wire.DIRECTION_BOTTOM :
        dd = (double)bounds.width / (double)size;
        p.x = bounds.x + (int)((size - count + 0.5) * dd);
        p.y = bounds.y + bounds.height + 1;
        break;
    }
    return p;
  }
  
  	private boolean groupingChanged = false;
  	void groupingChanged() {
  		groupingChanged = true;
  	}

  	public void update(int nr) {
  		if ((getPage() != INVISIBLE_PAGE)&&(nr >= 0)) {
  			if (!isUpdating()) {
  				setUpdating(true);
  				HashSet rels = new HashSet();
  				Iterator wit = getWires().iterator();  // sets mid hint point for all wires
  				while (wit.hasNext()) {
  					Wire wire = (Wire)wit.next();
  					if (wire.getRelation().updateAction()) {
  						rels.add(wire);
  					}
  				}
  				if (sortWiresByNearest() || groupingChanged) {  // rearanges directions and angles (depends on: hint)
  					wit = getWires().iterator(); 
  				} else {
  					wit = rels.iterator();
  				}
  				while (wit.hasNext()) { // repaint wires
  					Wire wire = (Wire)wit.next();
  					wire.getRelation().updateRelationPlacement();
  				}
  				
  				wit = rels.iterator();  // send update to wires
  				while (wit.hasNext()) {
  					Wire wire = (Wire)wit.next();
  					wire.getRelation().update(nr);// - 1);
  				}
  				groupingChanged = false;
  				setUpdating(false);
  			}
  		}
  	}
  
/*  
  private boolean reset_update = false;
  private int reseted_count = 0;
  public void update(int nr) {
//	System.err.println("B want to update: " + this);
    if ((!isOnPage(INVISIBLE_PAGE))&&(nr >= 0)) {
//      System.err.println("B updating " + wires.size() + " wires");
      if (isUpdating()) {
        if (reseted_count < wires.size() * wires.size() * 2) {
          reset_update = false;
          reseted_count++;
        } else {
          System.err.println("update loop in " + this);
        }
      } else {
        setUpdating(true);
        while (!reset_update) {
          reset_update = true;
          Iterator iter = wires.iterator();
          while ((iter.hasNext())&&(reset_update)) {
            Wire wire = (Wire)iter.next();
            wire.getRelation().update(nr - 1);
          }
        }
        reset_update = false;
        setUpdating(false);
        reseted_count = 0;
//	  System.err.println("B finished " + this + " : " + update_control_continue());
      }
    }
  }*/

  /**
   * understud as location
   * x, y - from top left corner
   * width (height) - sum of both left and right (top and bottom) location
   * bottom right location = width - x, height - y
   * @return
   */
  public Rectangle getTextInsets() {
    return new Rectangle(textInset, textInset, 2*textInset, 2*textInset);
  }

  	public Rectangle getTextPlace() {
  		Rectangle place = getBounds();
  		Rectangle insets = getTextInsets();
  		place.x += insets.x;
  		place.y += insets.y;
  		place.width -= insets.width;
  		place.height -= insets.height;
  		return place;
  	}
  
  protected Rectangle textRect = new Rectangle(0, 0, 0, 0);

  protected void setTextRect(Rectangle rect) {
    textRect.x = rect.x;
    textRect.y = rect.y;
    textRect.width = rect.width;
    textRect.height = rect.height;
  }

  protected Rectangle getTextRect() {
    return textRect;
  }
  
  protected void superSetSize(Point size) {
  	super.setSize(size);
  }

  /**
   * setSize
   *
   * @param size Dimension
   */
  	public void setSize(Point size) {
//System.err.println("SET SIZE ON " + this + " to " + size);
//System.err.println("WRAPPER valid " + wrapper.isGCValid());
//new Throwable().printStackTrace();

  		if (isVisible()) {
  			Rectangle insets = null;
  			int errCount = 0;
  			Point size0 = new Point(-1, -1);
  			Point size1 = getSize();
  			while ((!size0.equals(size1))&&(errCount++ < 100)) {
  				insets = getTextInsets();
  				wrapper.setWidth(size.x - insets.width);
  				size.x = Math.max(size.x, wrapper.getMinWidth() + insets.width);
  				size.y = Math.max(size.y, wrapper.getLineHeight() * wrapper.getLineCount() + insets.height);
  				super.setSize(size);
  				size0 = size1;
  				size1 = getSize();
  			}
  			if (!size0.equals(size1)) {
//  			System.err.println("Failed to set item size properly");
  				new Exception("Failed to set item size properly").printStackTrace();
  			}
  		} else super.setSize(size);
  	}
/**/
  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    if (isOnPage(INVISIBLE_PAGE)) {
      setDefinitionPlacement(null);
    } else {
      EData_type_placement entity_placement = (EData_type_placement)modelEG.createEntityInstance(EData_type_placement.class);
      Point cloc = getLocation();
      if (cloc.x != 0 || cloc.y != 0) {
        ELocation loc = (ELocation)modelEG.createEntityInstance(ELocation.class);
        loc.setX(null, cloc.x);
        loc.setY(null, cloc.y);
        entity_placement.setObject_location(null, loc);
      }

      Point csize;
      if (isLabelNew()) csize = new Point(0, 0);
    	else csize = getSize();
      if (csize.x != 0 || csize.y != 0) {
        ESize size = (ESize)modelEG.createEntityInstance(ESize.class);
        size.setWidth(null, csize.x);
        size.setHeight(null, csize.y);
        entity_placement.setObject_size(null, size);
      }

      if (getVisible() != VISIBLE_UNSET)
    	  entity_placement.setVisible(null, isVisible());

      entity_placement.setRepresented_object(null, getDefinition());

      /** TODO Colors disabled
      EColor color = (EColor)modelEG.createEntityInstance(EColor.class);
      color.setRed(null, simpleSchema.background.getRed());
      color.setGreen(null, simpleSchema.background.getGreen());
      color.setBlue(null, simpleSchema.background.getBlue());
      entity_placement.setRepresentation_color(null, color);
      */

      setDefinitionPlacement(entity_placement);
    }
  }

  /**
   * clearRelated
   */
  public void eliminate() {
    Iterator iter = getWires().iterator();
    while (iter.hasNext()) {
      Wire wire = (Wire)iter.next();
      wire.getRelation().eliminate();
    }
    setPage(Paging.NO_PAGE);
    super.eliminate();
  }

  public String toString() {
    return getName() + " " + super.toString();
  }
/*
  public void firePageChanged(PageChangeEvent e) {
    super.firePageChanged(e);
    Iterator iter = getWires().iterator();
    while (iter.hasNext()) {
      Wire wire = (Wire)iter.next();
      if (wire.isAttribute()) {
        wire.getRelation().setPage(e.getNewPage());
      }
    }
  }
*/
/*  public void setPage(int pgNr) {
    Iterator iter = getWires().iterator();
    while (iter.hasNext()) {
      Wire wire = (Wire)iter.next();
      if (wire.isAttribute()) wire.getRelation().setPage(pgNr);
    }
    super.setPage(pgNr);
  }
/**/

  /**
   * override size 
   * object has fixed size if it's drawed as invisible fake rectangle
   * the size of fake rectangle depends on size of circle of relation
   */
  public Point getSize() {
    Point size;
    if (isVisible()) size = super.getSize();
    else {
      size = new Point(AbstractEGRelation.circleR * 5, AbstractEGRelation.circleR * 5);
    }
//System.err.println("GET SIZE OF " + this + " = " + size);    
    return size;
  }
  
  public Point superGetSize() {
  	return super.getSize();
  }
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Named#setName(java.lang.String)
	 */
	public void setName(String name) {
		super.setName(name);
		wrapper.setText(getText());
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#setLabelNew(boolean)
	 */
	public void setLabelNew(boolean isNew) {
		super.setLabelNew(isNew);
		if (wrapper != null) wrapper.setDelimiters(isNew ? "" : NAME_DELIMITERS);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition()
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		Rectangle bounds = getBounds();
		bounds.x -= startat.x;
		bounds.y -= startat.y;
		String href = "";
		if ("".equals(schema_ext)) {
			href = " href=\"../../resources/" + schema_name.toLowerCase() + "/" + 
				schema_name.toLowerCase() + doc_file + "#" + schema_name.toLowerCase() +
				"." + getName().toLowerCase(); 
		} else {
			href = " href=\"../" + schema_name.toLowerCase() + "/sys/" + doc_file + "#"
			+ schema_name + "_" + schema_ext + "." + getName().toLowerCase(); 
		}
		String text = "<img.area shape=\"rect\" coords=\"" 
			+ bounds.x + "," + bounds.y + "," + (bounds.x + bounds.width) + "," + (bounds.y + bounds.height) 
			+ "\"" + href + "\" />";
		return text;
	}
	
	/**
	 * returns center point of this box
	 * @return
	 */
	public Point getCenterPoint() {
		Rectangle bounds = getBounds();
		return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Paging#setPage(int)
	 */
	public void setPage(int pgNr) {
		super.setPage(pgNr);
	}
}
