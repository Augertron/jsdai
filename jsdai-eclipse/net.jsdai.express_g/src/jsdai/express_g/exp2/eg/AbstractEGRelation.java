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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import jsdai.SExpress_g_schema.EColor;
import jsdai.SExpress_g_schema.ELocation;
import jsdai.SExpress_g_schema.ERelation_placement;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogRelationSimple;
import jsdai.express_g.exp2.ui.properties.DialogRelationTree;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public abstract class AbstractEGRelation extends AbstractEGObject {
  private AbstractEGBox parent;
  private Vector child = new Vector();

  public ColorSchema selectedSchema2;
  public ColorSchema simpleSchema2;
  public ColorSchema selectedSchemaOpt;
  public ColorSchema simpleSchemaOpt;

  public static final int snapRelation = 5;

  public static final int TYPE_AGREGATION = 1;
  public static final int TYPE_INHERITANCE = 2;
  protected int type;
  
  private RelationGroup group = new RelationGroup(1);
  
  protected static final float FONT_MULTIPLIER = 0.9f;

  /***************************************************************************
   * Line drawing accessories
   */
  public static final int circleR = 3;

  protected boolean dname = true;
  protected Rectangle namePlace = new Rectangle(0, 0, 0, 0);

  protected boolean dcircle = true;

  protected Rectangle hint = new Rectangle(0, 0, 0, 0);
  
  protected EGText textBox;
  //**************************************************************************/

  public AbstractEGRelation(PropertySharing prop) {
  	super(prop);
//  	simpleSchema.font = ColorSchema.FONT2;
//  	selectedSchema.font = ColorSchema.FONT2;
    selectedSchema2 = new ColorSchema(selectedSchema);
    simpleSchema2 = new ColorSchema(simpleSchema);
    selectedSchemaOpt = new ColorSchema(selectedSchema);
    simpleSchemaOpt = new ColorSchema(simpleSchema);
    
    group.add(this);
//    textBox = new EGText(prop());
//    prop().handler().drawableAddR(textBox);
  }

  public boolean createEditDialog(Composite parent) {
    if (type == TYPE_INHERITANCE) dialog = new DialogRelationTree(parent, this, prop());
    	else dialog = new DialogRelationSimple(parent, this, prop());
    return isEditDialogCreated();
  }
  
  public void setType(int type) {
    this.type = type;
    dialog = null;
  }

  public int getType() {
    return type;
  }
  
  public Point getHintPoint() {
    Point p = new Point(hint.x + hint.width, hint.y + hint.height);
    return p;
  }

  public void setHintPoint(Point hint) {
    this.hint.width = hint.x - this.hint.x;
    this.hint.height = hint.y - this.hint.y;
  }

  public Point getNamePlace() {
    Point p = new Point(namePlace.x + namePlace.width, namePlace.y + namePlace.height);
    return p;
  }

  public void setNamePlace(Point namePlace) {
    this.namePlace.width = namePlace.x - this.namePlace.x;
    this.namePlace.height = namePlace.y - this.namePlace.y;
  }

  public void setHint1(Point hint1) {
//    hint.x = hint1.x;
//    hint.y = hint1.y;
    hint.width = hint1.x;
    hint.height = hint1.y;
  }

  public Point getHint1() {
//    return new Point(hint.x, hint.y);
    return new Point(hint.width, hint.height);
  }

  public void setHint2(Point hint2) {
//    namePlace.x = hint2.x;
//    namePlace.y = hint2.y;
    namePlace.width = hint2.x;
    namePlace.height = hint2.y;
  }

  public Point getHint2() {
//    return new Point(namePlace.x, namePlace.y);
    return new Point(namePlace.width, namePlace.height);
  }

  public AbstractEGBox getParent() {
    return parent;
  }

  /**
   * IT'S IMPORTANT TO SET PARENT BEFORE CHILD!
   * @param parent AbstractEGBox
   */
  public void setParent(AbstractEGBox parent) {
    if (this.parent != null) {
      this.parent.removeWire(this);
    }
    this.parent = parent;
    if (parent != null) {
      parent.addWire(this);
//      setPage(parent.getPage());
    }
/*    else {
      NullPointerException ex = new NullPointerException("parent null Warning");
      ex.printStackTrace();
    }/**/
  }

  public void addChild(AbstractEGBox child) {
    this.child.add(child);
    child.addWire(this);
  }

  	public void setChild(AbstractEGBox child, int index) {
  		if (index == this.child.size()) addChild(child);
  		else {
  			AbstractEGBox box = (AbstractEGBox)this.child.get(index);
  			box.removeWire(this);
  			child.addWire(this);
  			this.child.set(index, child);
  		}
  	}

  public void setChild(AbstractEGBox child) {
    setChild(child, 0);
  }

  public void clearChild() {
    child.clear();
  }

  public AbstractEGBox getChild() {
    if (child.size() == 0) return null;
    else return getChild(0);
  }

  /**
   * Returns all children objects of this relation
   * @return Vector of AbstractEGBox objects, second end of relation.
   */
  public Vector getChilds() {
    return (Vector)child.clone();
  }

  public AbstractEGBox getChild(int index) {
    return (AbstractEGBox)child.get(index);
  }

  public int getChildCount() {
    return child.size();
  }

  public boolean removeChild(AbstractEGBox child) {
    child.removeWire(this);
    return this.child.remove(child);
  }

  public AbstractEGBox removeChild(int index) {
    AbstractEGBox child = (AbstractEGBox)this.child.get(index);
    child.removeWire(this);
    return child;
  }

  private boolean pointOnRightLineSide(Point p, Point p1, Point p2) {
    int a = p2.x - p1.x, b = p2.y - p1.y;
    return b * (p2.x - p.x) > a * (p2.y - p.y);
  }

  protected boolean objectNearLine(Point p, Point p1, Point p2) {
    double angle = Math.atan((double)(p2.y - p1.y) / (double)(p2.x - p1.x));
    if (p1.x > p2.x) {
      if (p1.y < p2.y) angle += Math.PI;
      else angle -= Math.PI;
    }
    double pi4 = Math.PI / 4;
    double r = 1.4142135623730950488016887242097 * snapRelation;
    Point[] prect = new Point[4];
//    for (int i = 0; i < prect.length; i++)prect[i] = new Point();
    prect[0] = new Point((int)(p1.x + r * Math.cos(angle + 3 * pi4)),
    		(int)(p1.y + r * Math.sin(angle + 3 * pi4)));
    prect[1] = new Point((int)(p1.x + r * Math.cos(angle - 3 * pi4)),
    		(int)(p1.y + r * Math.sin(angle - 3 * pi4)));
    prect[2] = new Point((int)(p2.x + r * Math.cos(angle - pi4)),
    		(int)(p2.y + r * Math.sin(angle - pi4)));
    prect[3] = new Point((int)(p2.x + r * Math.cos(angle + pi4)),
    		(int)(p2.y + r * Math.sin(angle + pi4)));
    return pointOnRightLineSide(p, prect[0], prect[1]) &&
        pointOnRightLineSide(p, prect[1], prect[2]) &&
        pointOnRightLineSide(p, prect[2], prect[3]) &&
        pointOnRightLineSide(p, prect[3], prect[0]);
  }

  /**
   * clearRelated
   */
  public void eliminate() {
    if (parent != null) {
      parent.removeWire(this);
    }
    Iterator iter = child.iterator();
    while (iter.hasNext()) {
      AbstractEGBox box = (AbstractEGBox)iter.next();
      box.removeWire(this);
    }
    group.remove(this);
    super.eliminate();
//    prop().handler().drawableRemove(textBox);
  }

  	/**
  	 * 2005-08-23 updated unnecesary information dropping
  	 * 
  	 * @param model
  	 * @param placement
  	 * @throws SdaiException
  	 */
  	protected void updateModel(SdaiModel model, ERelation_placement placement) throws SdaiException {
  		Point p = getHint1();
  		if (p.x != 0 && p.y != 0) {
  			ELocation locSel = (ELocation)model.createEntityInstance(ELocation.class);
  			locSel.setX(null, p.x);
  			locSel.setY(null, p.y);
  			placement.setHint1(null, locSel);
  		}
  	    p = getHint2();
  		if (p.x != 0 && p.y != 0) {
  			ELocation locSel = (ELocation)model.createEntityInstance(ELocation.class);
  			locSel.setX(null, p.x);
  			locSel.setY(null, p.y);
  			placement.setHint2(null, locSel);
  		}

  		if (getVisible() != VISIBLE_UNSET)
      	  placement.setVisible(null, isVisible());
  		
  		if (simpleSchema.background != ColorSchema.COLOR_WHITE) {
  			EColor color = (EColor)model.createEntityInstance(EColor.class);
  			color.setRed(null, simpleSchema.getRedB());
  			color.setGreen(null, simpleSchema.getGreenB());
  			color.setBlue(null, simpleSchema.getBlueB());
  		    placement.setRepresentation_color(null, color);
  		}
  		setDefinitionPlacement(placement);
  		validDict = true;
    
  		//create bundle
  		group.getDefinitionPlacement(null, model);
  	}

  protected abstract void updateRelationPlacement();
  

	abstract boolean updateAction();
	
	public void update(int nr) {
		if ((getPage() != INVISIBLE_PAGE)&&(nr >= 0)) {
			if (!isUpdating()) {
				setUpdating(true);  // send update to related boxes
	          	AbstractEGBox parent = getParent();
				if (parent == null) {
//		          	System.err.println("REL:" + getPage() + ":" + getName() + " parent=null <" + getClass() + ">");
				} else parent.update(nr);// - 1); 
				Iterator cit = getChilds().iterator();
				while (cit.hasNext()) {
					AbstractEGBox child = (AbstractEGBox)cit.next();
					child.update(nr);// - 1);
				}
				setUpdating(false);
			}
		}
	}

/*
  private boolean reset_update = false;
  public void update(int nr) {
//	System.err.println("R updating: " + this);
	if ((!isOnPage(INVISIBLE_PAGE))&&(nr >= 0)) {
	    if (isUpdating()) {
	      reset_update = false;
	    } else {
	      setUpdating(true);
	      while (!reset_update) {
	        reset_update = true;
	        AbstractEGBox parent = getParent();
	        Vector childs = getChilds();
	        parent.update(nr - 1);
	        Iterator iter = childs.iterator();
	        while ((iter.hasNext())&&(reset_update)) 
	          ((AbstractEGBox)iter.next()).update(nr - 1);
	      }     
	      reset_update = false;
	      setUpdating(false);
	      updateRelationPlacement();
	    }
    }
//	System.err.println("R finished " + this + " : " + update_control_continue());
  }
 */ 
  public Selectable selectAsFirst(int type) {
    boolean out = false;
    switch (type) {
      case AbstractEGRelation.TYPE_AGREGATION :
        out = getParent() instanceof EGSelect;
        break;
      case AbstractEGRelation.TYPE_INHERITANCE :
        out = (getParent() instanceof EGEntity)||(getParent() instanceof EGEntityRef);
        break;
    }
    return out?this:null;
  }

  public Selectable selectSecond(int type, Selectable second) {
    return getParent().selectSecond(type, second);
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    if (getType() == TYPE_AGREGATION) return "Relation";
    else return "Inheritance";
  }

  /**
   * getPage
   *
   * @return int
   *
  public int getPage() {
    return (getParent() == null)?NO_PAGE:getParent().getPage();
  }
/*
  public void firePageChanged(PageChangeEvent e) {
    super.firePageChanged(e);
    setPage(e.getNewPage());
  }
*/
  
  	public RelationGroup getGroup() {
  		return group;
  	}
  	
  	public void setGroup(RelationGroup group) {
  		if (this.group != group) {
  			this.group.remove(this);
  			this.group = group;
  			this.group.add(this);
  			getParent().groupingChanged();
  			prop().setModified(true);
  		}
  	}
  	
  	public void addGroup(RelationGroup group) {
  		Iterator iter = ((Collection)group.clone()).iterator();
  		while (iter.hasNext()) {
  			AbstractEGRelation rel = (AbstractEGRelation)iter.next();
  			rel.setGroup(this.group);
  		}
  	}
  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#clearDefinitions()
	 */
	public void clearDefinitions() {
		super.clearDefinitions();
		group.clearDefinitions();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#setDefinition(jsdai.lang.EEntity)
	 */
	public void setDefinition(EEntity entity) {
		super.setDefinition(entity);
		if (entity == null) group.setDefinition(null);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#setDefinitionPlacement(jsdai.lang.EEntity)
	 */
	public void setDefinitionPlacement(EEntity placement) {
		super.setDefinitionPlacement(placement);
		if (placement == null) group.setDefinitionPlacement(null);
	}
	
	protected abstract void updateBounds();
}
