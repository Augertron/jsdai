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

import jsdai.SExpress_g_schema.EAttribute_placement;
import jsdai.SExpress_g_schema.EConstraint_relation_placement;
import jsdai.SExpress_g_schema.EDefined_relation_placement;
import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.SExpress_g_schema.EPage_reference_from;
import jsdai.SExpress_g_schema.EPage_reference_to;
import jsdai.SExpress_g_schema.EPage_relation;
import jsdai.SExpress_g_schema.ESchema_relation_placement;
import jsdai.SExpress_g_schema.ESelect_relation_placement;
import jsdai.SExpress_g_schema.ESupertype_placement;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAttribute;
import jsdai.SExtended_dictionary_schema.EBound;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EDerived_attribute;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.EExplicit_attribute;
import jsdai.SExtended_dictionary_schema.EExtended_enumeration_type;
import jsdai.SExtended_dictionary_schema.EExtended_select_type;
import jsdai.SExtended_dictionary_schema.EExtensible_enumeration_type;
import jsdai.SExtended_dictionary_schema.EExtensible_select_type;
import jsdai.SExtended_dictionary_schema.EInteger_bound;
import jsdai.SExtended_dictionary_schema.EInverse_attribute;
import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.StaticTools;
import jsdai.express_g.common.TextWrapper;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.LabelListener;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class EGRelationSimple extends AbstractEGRelation implements LabelListener {
  protected boolean optional = false;
  protected boolean derived = false;
  protected String redeclaring = null;
  protected String redeclaringInverse = null;
  protected Agregate agregate = new Agregate(null);

  protected Agregate inverse = null;
  
	protected TextWrapper wrapper = null;

	// RR
	protected boolean fRestricted = false;
	private boolean showRestricted = true;
	private boolean flag_inverted_in_supertype = false;  // the issue could have been handled by parent_of_inverted alone
	private String parent_of_inverted = null; 
  
  protected Point[] pline = new Point[]{
      new Point(0, 0),
      new Point(0, 0),
      new Point(0, 0),
      new Point(0, 0),
      new Point(0, 0)
  };

  protected Point pcircle = new Point(0, 0);

  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
  	if (this.optional != optional) {
  		this.optional = optional;
  		if (prop() != null) prop().setModified(true);
  	}
  }

  public Agregate getInverse() {
    return inverse;
  }

  public void setInverse(Agregate inverse) {
//System.out.println("EGRS - setInverse - 01: " + inverse);  
  	if (this.inverse != inverse) {
  	    this.inverse = inverse;
  	    wrapper.setText(getText());
  		updateBounds();
  		if (prop() != null) prop().setModified(true);
  	}
  }

	// RR version 	
  public void setInverse(Agregate inverse, boolean flag_inverted_in_supertype, String parent_of_inverted) {
//System.out.println("EGRS - setInverse - 01: " + inverse);  
  	this.flag_inverted_in_supertype = flag_inverted_in_supertype;
		this.parent_of_inverted = parent_of_inverted;
  	if (this.inverse != inverse) {
  	    this.inverse = inverse;
  	    wrapper.setText(getText());
  		updateBounds();
  		if (prop() != null) prop().setModified(true);
  	}
  }


  public boolean isDerived() {
    return derived;
  }

  public void setDerived(boolean derived) {
  	if (this.derived != derived) {
  	    this.derived = derived;
  	    wrapper.setText(getText());
  		updateBounds();
  		if (prop() != null) prop().setModified(true);
  	}
  }

  public Agregate getAgregate() {
    return agregate;
  }

  public void setAgregate(Agregate agregate) {
  	if (this.agregate != agregate) {
  		if (this.agregate != null) this.agregate.removeLabelListener(this);
  	    this.agregate = agregate;
  	    if (agregate != null) agregate.addLabelListener(this);
  	    wrapper.setText(getText());
		updateBounds();
		if (prop() != null) prop().setModified(true);
  	}
  }

  public void setChild(AbstractEGBox child, int index) {
    super.setChild(child, index);
    agregate.setAgregation_type(child);
  }

  public void addChild(AbstractEGBox child) {
    super.addChild(child);
    agregate.setAgregation_type(child);
  }

  public EGRelationSimple(PropertySharing prop, AbstractEGBox parent, AbstractEGBox child, int type) {
  	super(prop);
	wrapper = new TextWrapper(getText(), "\n");
	wrapper.setStyle(TextWrapper.STYLE_NO_DELIM);

    selectedSchema2.lineWidth =
    simpleSchema2.lineWidth = ColorSchema.STROKE_WIDTH2;
    selectedSchemaOpt.lineStyle =
    simpleSchemaOpt.lineStyle = ColorSchema.DASHED_LINE;

    setParent(parent);
    setChild(child);
    this.type = type;
  }

  public EGRelationSimple(PropertySharing prop, AbstractEGBox parent, AbstractEGBox child, int type, boolean optional, boolean derive, String redeclaring) {
    this(prop, parent, child, type);
    setOptional(optional);
    setDerived(derive);
    setRedeclaring(redeclaring);
  }

  public EGRelationSimple(PropertySharing prop, String name, AbstractEGBox parent, AbstractEGBox child, int type, boolean optional, boolean derive, String redeclaring) {
    this(prop, parent, child, type, optional, derive, redeclaring);
    setName(name);
  }
  // RR
  public EGRelationSimple(PropertySharing prop, String name, AbstractEGBox parent, AbstractEGBox child, int type, boolean optional, boolean derive, String redeclaring, boolean fRestricted) {
    this(prop, parent, child, type, optional, derive, redeclaring);
    setName(name);
		setRestricted(fRestricted);
  }

  public EGRelationSimple(PropertySharing prop, EGRelationTree relation) {
    this(prop, relation.getParent(), relation.getChild(), relation.getType());
    setDefinition(relation.getDefinition());
    setName(relation.getName());
    setHint1(relation.getHint1());
    setHint2(relation.getHint2());
    setPage(relation.getPage());
  }

  protected void updateRelationPlacement() {
    AbstractEGBox parent = getParent();
    AbstractEGBox child = getChild();

    // RR - adding additional checks to protect from overcorrection, for this, we will compare corrected values with the image bounds
    Rectangle image = prop().getPainting().getImageBounds();
    
    pline[0] = parent.getPosition(this, false);
    pline[4] = child.getPosition(this, parent == child);
    // solving one point problem: upgrade closed 2006-01-04 (no visual snapping, added box place changing)
//    if (Math.abs(pline[0].x - pline[4].x) <= 1) pline[4].x = pline[0].x;
//    if (Math.abs(pline[0].y - pline[4].y) <= 1) pline[4].y = pline[0].y;
    
    
//    ****************** SNAPPING START ******************************* 
    int dd = pline[0].x - pline[4].x;
    if (dd != 0 && Math.abs(dd) <= 3) {  // original
    	if (parent.isSelected() && !child.isSelected()) {
//    		pline[0].x -= dd;  // original location
    		Point location = parent.getLocation();
// correction in draw():
//    		  	if (bounds.x < image.x)  bounds.x = image.x;

    		// here,  corrected location.x should not be < than image.x, if <, then do not correct at all  /check in draw(): if (bounds.x < image.x)
    		//  bounds.x -> location.x
    		// so,    location.x < image.x    - violation, not to have a violation, the opposite:   location.x >= image.x, and we need not to have a violation with dd correction: location.x - dd >= image.x
    		if (location.x - dd >= image.x) {
    			location.x -= dd;
    			parent.setLocation(location);
    			pline[0].x -= dd;  // wait with correcting this value until we establish if we correct anything - move it down
				} else {  // TO DISABLE SNAP AT THE OPPOSITE END, REMOVE THIS else
					// the snap is not possible, but perhaps we can attempt to snap the box at the other end of the wire?
					// this snap would be done to the opposite direction and clearly no additional check is needed as both boxes must be close to the left edge
					
					//DISABLING Point location2 = child.getLocation();
					//DISABLING location2.x += dd;
					//DISABLING child.setLocation(location2);
					//DISABLING pline[4].x += dd;
				}
    	} else 
       	if (!parent.isSelected() && child.isSelected()) {
//    		pline[4].x += dd; - original location
    		Point location = child.getLocation();
    		Rectangle boundsr = child.getBounds(); // RR - for this check we need more than location
    		// here, checking the right edge, violation:   if (boundsr.x + boundsr.width > image.width + image.x)
    		// no violation: if (boundsr.x + boundsr.width <= image.width + image.x)
    		// no violation with the snap correction present:  if (boundsr.x + dd + boundsr.width <= image.width + image.x)
        if (boundsr.x + dd + boundsr.width <= image.width + image.x) {
    			location.x += dd;
    			child.setLocation(location);
    			pline[4].x += dd;
    		} else { // snap at the other end - TO DISABLE IT, REMOVE THIS else

    			//DISABLING Point location2 = parent.getLocation();
    			//DISABLING location2.x -= dd;
    			//DISABLING parent.setLocation(location2);
    			//DISABLING pline[0].x -= dd; 
    		}
    	}  
    }
    dd = pline[0].y - pline[4].y;
    if (dd != 0 && Math.abs(dd) <= 2) {
    	if (parent.isSelected() && !child.isSelected()) {
//    		pline[0].y -= dd;  // original location
    		Point location = parent.getLocation();
				// violation: if (bounds.y < image.y)
				// no violation: if (location.y >= image.y)
				// no violation with the snap correction present: if (location.y - dd >= image.y)
				if (location.y - dd >= image.y) {
    			location.y -= dd;
    			parent.setLocation(location);
    			pline[0].y -= dd;
    		} else { // SNAPPING AT THE OTHER END - TO DISABLE IT, REMOVE THIS else

    			//DISABLING Point location2 = child.getLocation();
    			//DISABLING location2.y += dd;
    			//DISABLING child.setLocation(location2);
    			//DISABLING pline[4].y += dd;
    		}
    	} else 
       	if (!parent.isSelected() && child.isSelected()) {
//    		pline[4].y += dd; // original location
    		Point location = child.getLocation();
    		Rectangle boundsr = child.getBounds(); // RR - for this check we need more than location
    		// checking bottom edge violation: if (bounds.y + bounds.height > image.height + image.y)
    		// no violation: if (boundsr.y + boundsr.height <= image.height + image.y)
    		// no violation with the snap correction present: if (boundsr.y  + dd + boundsr.height <= image.height + image.y)
				if (boundsr.y  + dd + boundsr.height <= image.height + image.y) {
    			location.y += dd;
    			child.setLocation(location);
    			pline[4].y += dd;
    		} else { // SNAPPING AT THE OTHER END - TO DISABLE IT, REMOVE THIS else

    			//DISABLING Point location2 = parent.getLocation();
    			//DISABLING location2.y -= dd;
    			//DISABLING parent.setLocation(location2);
    			//DISABLING pline[0].y -= dd;
    		}
    	}  
    }
//  ****************** SNAPPING END ******************************* 
    // setting other points
    pline[1] = new Point(pline[0].x, pline[0].y);
    pline[2] = new Point(pline[0].x, pline[0].y);
    pline[3] = new Point(pline[0].x, pline[0].y);
    Point locHint = getHintPoint();
//    Point midle = new Point((pline[0].x + pline[4].x) / 2, (pline[0].y + pline[4].y) / 2);
    
//    midle.setLocation((getParent().getBounds().getCenterX() + getChild().getBounds().getCenterX())/2, (getParent().getBounds().getCenterY() + getChild().getBounds().getCenterY())/2);
    switch (parent.getDirection(this, false)) {
      case Wire.DIRECTION_BOTTOM :
        switch (child.getDirection(this, parent == child)) {
          case Wire.DIRECTION_BOTTOM :
            pline[2].x = pline[0].x;
          	pline[2].y = hint.y + hint.height;
            pline[3].x = pline[4].x;
            pline[3].y = hint.y + hint.height;
            break;
          case Wire.DIRECTION_LEFT :
          	if (parent == child) {
          		pline[1].y = locHint.y;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = locHint.x;
          	} else {
          		pline[3].x = pline[0].x;
          	}
      		pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_RIGHT :
          	if (parent == child) {
          		pline[1].y = locHint.y;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = locHint.x;
          		pline[3].y = pline[4].y;
          	} else {
          		pline[3].x = pline[0].x;
          		pline[3].y = pline[4].y;
          	}
            break;
          case Wire.DIRECTION_TOP :
            pline[2].x = pline[0].x;
          	pline[2].y = locHint.y;
            pline[3].x = pline[4].x;
            pline[3].y = locHint.y;
            break;
        }
        break;
      case Wire.DIRECTION_TOP :
        switch (child.getDirection(this, parent == child)) {
          case Wire.DIRECTION_BOTTOM :
            pline[2].x = pline[0].x;
          	pline[2].y = locHint.y;
            pline[3].x = pline[4].x;
            pline[3].y = locHint.y;
            break;
          case Wire.DIRECTION_LEFT :
          	if (parent == child) {
          		pline[1].y = locHint.y;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = locHint.x;
          	} else {
          		pline[3].x = pline[0].x;
          	}
      		pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_RIGHT :
          	if (parent == child) {
          		pline[1].y = locHint.y;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = locHint.x;
          		pline[3].y = pline[4].y;
          	} else {
          		pline[3].x = pline[0].x;
          		pline[3].y = pline[4].y;
          	}
            break;
          case Wire.DIRECTION_TOP :
            pline[2].x = pline[0].x;
          	pline[2].y = hint.y + hint.height;
            pline[3].x = pline[4].x;
            pline[3].y = hint.y + hint.height;
            break;
        }
        break;
      case Wire.DIRECTION_LEFT :
        switch (child.getDirection(this, parent == child)) {
          case Wire.DIRECTION_BOTTOM :
          	if (parent == child) {
          		pline[1].x = locHint.x;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = pline[4].x;
          		pline[3].y = locHint.y;
          	} else {
          		pline[3].x = pline[4].x;
          		pline[3].y = pline[0].y;
          	}
            break;
          case Wire.DIRECTION_LEFT :
            pline[2].x = hint.x + hint.width;
          	pline[2].y = pline[0].y;
            pline[3].x = hint.x + hint.width;
            pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_RIGHT :
            pline[2].x = locHint.x;
          	pline[2].y = pline[0].y;
            pline[3].x = locHint.x;
            pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_TOP :
          	if (parent == child) {
          		pline[1].x = locHint.x;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = pline[4].x;
          		pline[3].y = locHint.y;
          	} else {
          		pline[3].x = pline[4].x;
          		pline[3].y = pline[0].y;
          	}
            break;
        }
        break;
      case Wire.DIRECTION_RIGHT :
        switch (child.getDirection(this, parent == child)) {
          case Wire.DIRECTION_BOTTOM :
          	if (parent == child) {
          		pline[1].x = locHint.x;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = pline[4].x;
          		pline[3].y = locHint.y;
          	} else {
          		pline[3].x = pline[4].x;
          		pline[3].y = pline[0].y;
          	}
            break;
          case Wire.DIRECTION_LEFT :
            pline[2].x = locHint.x;
          	pline[2].y = pline[0].y;
            pline[3].x = locHint.x;
            pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_RIGHT :
            pline[2].x = hint.x + hint.width;
          	pline[2].y = pline[0].y;
            pline[3].x = hint.x + hint.width;
            pline[3].y = pline[4].y;
            break;
          case Wire.DIRECTION_TOP :
          	if (parent == child) {
          		pline[1].x = locHint.x;
          		pline[2].x = locHint.x;
          		pline[2].y = locHint.y;
          		pline[3].x = pline[4].x;
          		pline[3].y = locHint.y;
          	} else {
          		pline[3].x = pline[4].x;
          		pline[3].y = pline[0].y;
          	}
            break;
        }
        break;
    }
    
//    hint.x = midle.x;
//    hint.y = midle.y; //locHint);

    // counting Point pc, based on Points p1 and p2
    double dist12 = EGToolKit.distance(pline[3], pline[4]);
    if (dist12 == 0) {
    	pcircle.x = pline[4].x;
    	pcircle.y = pline[4].y;
    } else {
    	pcircle.x = (int)(pline[4].x - (pline[4].x - pline[3].x) * circleR / dist12);
    	pcircle.y = (int)(pline[4].y - (pline[4].y - pline[3].y) * circleR / dist12);
    }

    Point nameLoc = new Point((pline[2].x + pline[3].x)/2 + 4, 
    		pline[2].equals(pline[0]) ? pline[3].y : Math.min(pline[2].y, pline[3].y) - 1);
    if (parent instanceof EGSchema) {
    	nameLoc.x += 3;
    	nameLoc.y -= 10;
    }
    namePlace.x = nameLoc.x;
    namePlace.y = nameLoc.y;

    updateBounds();
  }

  	boolean updateAction() {
  		if (isOnPage(INVISIBLE_PAGE)) return false;
  		AbstractEGBox parent = getParent();
  		AbstractEGBox child = getChild();
		Point midle = new Point(0, 0);
  		if (parent == child) { // self relation
  			midle.x = parent.getLocation().x - 20;
  			midle.y = parent.getCenterPoint().y;
  		} else if (getGroup().size() > 1) { // relation in bundle
  			midle.x = parent.getCenterPoint().x;
  			midle.y = child.getCenterPoint().y;
  		} else { // simple relation
  			Rectangle bounds1 = parent.getBounds();
  			Rectangle bounds2 = child.getBounds();
        	Point p1 = new Point(bounds1.x, bounds1.y);
        	Point p2 = new Point(bounds2.x, bounds2.y);
        	Point c1 = new Point(bounds1.x + bounds1.width / 2, bounds1.y + bounds1.height / 2);
        	Point c2 = new Point(bounds2.x + bounds2.width / 2, bounds2.y + bounds2.height / 2);
        	if (c2.x > c1.x) {
        		if (c2.y > c1.y) { // apacia - desine
            		p1.x += bounds1.width;
            		p1.y += bounds1.height;
        		} else { // virsus - desine
            		p1.x += bounds1.width;
            		p2.y += bounds2.height;
        		}
        	} else {
        		if (c2.y > c1.y) { // apacia - kaire
            		p2.x += bounds2.width;
            		p1.y += bounds1.height;
        		} else { // virsus - kaire
            		p2.x += bounds2.width;
            		p2.y += bounds2.height;
        		}
        	}
  			midle.x = (p1.x + p2.x) / 2;
  			midle.y = (p1.y + p2.y) / 2;
  		}
  		
		if ((midle.x != hint.x)||(midle.y != hint.y)) {
			hint.x = midle.x;
			hint.y = midle.y;
			return true;
		} else 
			return false;
  	}
  
/*  
  public void update(int nr) {
    final double EPSILON = 0.1; // constant to compare doubles

    if (!isUpdating()) {
      AbstractEGBox parent = getParent();
      AbstractEGBox child = getChild();
      boolean self = parent == child;
    
      // center points
      Point ppc = parent.getCenterPoint();
      Point pcc = parent.getCenterPoint();
      Point midle = new Point(0, 0);
      // current state
      Point pp0 = parent.getPosition(this, false);
      Point pc0 = child.getPosition(this, self);
      // try resort related boxes
      boolean changedP = parent.sortWiresByNearest();
      boolean changedC = child.sortWiresByNearest();
      boolean changed = changedP | changedC;
      // new state
      Point pp = parent.getPosition(this, false);
      Point pc = child.getPosition(this, self);
      int err_counter = 0;
      // while curent state != new state
      while ((changed)&&(EGToolKit.distance(pp0, pp) > EPSILON)&&(EGToolKit.distance(pc0, pc) > EPSILON)) {
        pp0 = pp;
        pc0 = pc;
        // recount hint point
        midle.x = (pp.x + pc.x) / 2;
        midle.y = (pp.y + pc.y) / 2;
        hint.x = midle.x;
        hint.y = midle.y;

        // get new state
        if (parent.sortWiresByNearest()) {
          changedP = true;
          changed = true;
          pp = parent.getPosition(this, false);
        }
        if (child.sortWiresByNearest()) {
          changedC = true;
          changed = true;
          pc = child.getPosition(this, self);
        }

        err_counter++;
        if (err_counter > 100) {
          System.err.println("update loop in " + this);
          changed = false;
        }
      }
      // update boxes (with related relations) if changed
      setUpdating(true);
      if (changedP) parent.update(2);
      if ((changedC)&&(!self)) child.update(2);
      setUpdating(false);
      updateRelationPlacement();
    }
/*
    if (parent == child) {

      } else {
        Point midle = new Point();
        // current state
        Point pp0 = parent.getPosition(this, false);
        Point pc0 = child.getPosition(this, false);
        // try resort related boxes
        boolean changedP = parent.sortWiresByNearest();
        boolean changedC = child.sortWiresByNearest();
        boolean changed = changedP | changedC;
        // new state
        Point pp = parent.getPosition(this, false);
        Point pc = child.getPosition(this, false);
        int err_counter = 0;
        // while curent state != new state
        while ((changed)&&(pp0.distance(pp) > EPSILON)&&(pc0.distance(pc) > EPSILON)) {
          pp0 = pp;
          pc0 = pc;
          // recount hint point
          midle.setLocation((pp.getX() + pc.getX()) / 2, (pp.getY() + pc.getY()) / 2);
          hint.setLocation(midle);

          // get new state
          if (parent.sortWiresByNearest()) {
            changedP = true;
            changed = true;
            pp = parent.getPosition(this, false);
          }
          if (child.sortWiresByNearest()) {
            changedC = true;
            changed = true;
            pc = child.getPosition(this, false);
          }

          err_counter++;
          if (err_counter > 100) {
            System.err.println("update loop");
            changed = false;
          }
        }
        // update boxes (with related relations) if changed
        setUpdating(true);
        if (changedP) parent.update(2);
        if (changedC) child.update(2);
        setUpdating(false);
        updateRelationPlacement();
      }
    }
    */
//  }
  
  public void draw(GC g) {
    ColorSchema schema;
    if (!isVisible()) {
      schema = isSelected() ? selectedSchema : nonVisibleSchema;
    } else if (type == TYPE_INHERITANCE) {
      schema = isSelected() ? selectedSchema2 : simpleSchema2;
    } else {
      if (optional)
        schema = isSelected() ? selectedSchemaOpt : simpleSchemaOpt;
      else
        schema = isSelected() ? selectedSchema : simpleSchema;
    }
    schema.apply(g);
	g.setFont(prop().getFont2());
    for (int i=0; i<pline.length - 1; i++)
    	g.drawLine(pline[i].x, pline[i].y, pline[i+1].x, pline[i+1].y);

    if (dcircle) {
    	if (getChild() instanceof EGPageRefLocalTo) {
    		
    	} else
    	if (getParent() instanceof EGConstraint) {
    		Point pl = pline[pline.length - 1];
    		double angle = EGToolKit.angle(pline[pline.length - 2], pl);
//System.out.println("<<RR>>SIMPLE - angle: " + angle);
//System.out.println("<<RR>>SIMPLE - x1: " + pline[pline.length - 2].x + ", y1: " + pline[pline.length - 2].y + ", x2: " + pl.x + ", y2: " +  pl.y);
    		if (!Double.isNaN(angle)) {
        		Point pp = new Point(0, 0);
        		pp.x = (int)(pl.x - circleR * 2 * Math.cos(angle + Math.PI / 6));
        		pp.y = (int)(pl.y - circleR * 2 * Math.sin(angle + Math.PI / 6));
// System.out.println("<<RR-HH>>");
        		// drawing first half of an arrow - RR
//System.out.println("<<RR>>SIMPLE - arrow 1 - x1: " + pp.x + ", y1: " + pp.y + ", x2: " + pl.x + ", y2: " + pl.y);
        		g.drawLine(pp.x, pp.y, pl.x, pl.y);
        		pp.x = (int)(pl.x - circleR * 2 * Math.cos(angle - Math.PI / 6));
        		pp.y = (int)(pl.y - circleR * 2 * Math.sin(angle - Math.PI / 6));
        		// drawing 2nd half of an arrow - RR
//System.out.println("<<RR>>SIMPLE - arrow 2 - x1: " + pp.x + ", y1: " + pp.y + ", x2: " + pl.x + ", y2: " + pl.y);
        		g.drawLine(pp.x, pp.y, pl.x, pl.y);
    		}
    	} else {
    		g.fillOval(pcircle.x - circleR, pcircle.y - circleR, 2 * circleR, 2 * circleR);
    		if (optional) {
    			schema = isSelected() ? selectedSchema : simpleSchema;
    			schema.apply(g);
    		}
    		g.drawOval(pcircle.x - circleR, pcircle.y - circleR, 2 * circleR, 2 * circleR);
    	}
    }

    	if (dname) {
	  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
  	  			wrapper.setGC(g);
  	  			updateBounds();
	  		}
	  		Point hint = getNamePlace();
			int textStartAt = hint.y - wrapper.getLineHeight() * wrapper.getLineCount();
			if (getParent() instanceof EGSchema) {
				for (int i = 0; i < wrapper.getLineCount(); i++) {
					g.drawString(wrapper.getLine(i), 
							hint.x + 4, 
							textStartAt	+ i * wrapper.getLineHeight());
				}
				if (wrapper.getLineCount() > 0) {
					int lineY = pline[pline.length - 2].y;
					g.drawLine(hint.x, textStartAt, hint.x, lineY);
					g.drawLine(hint.x, textStartAt, hint.x, textStartAt	+ wrapper.getLineHeight() * wrapper.getLineCount());
					int dy = (textStartAt + hint.y) / 2 > lineY ? 10 : -10;
					g.setBackground(g.getForeground());
					g.fillPolygon(new int[]{hint.x, lineY, hint.x - 5, lineY + dy, hint.x + 5, lineY + dy});
				}
			} else {
				for (int i = 0; i < wrapper.getLineCount(); i++) {
					g.drawString(wrapper.getLine(i), 
							hint.x - wrapper.getLineWidth(i) / 2, 
							textStartAt	+ i * wrapper.getLineHeight());
				}
			}
/*  	  			
      String text = getName();
      if (text != "") text += " ";
      if (derived) text = "(DER) " + text;
      if (redeclaring) text = "(RT) " + text;
      text += agregate.getText();
      Point textRect = g.stringExtent(text);
      g.drawString(text, 
      		namePlace.x + namePlace.width - textRect.x / 2 + 2,
			namePlace.y + namePlace.height - textRect.y - 2);
      if (inverse != null) {
        text = "(INV) " + inverse.getText();
        textRect = g.stringExtent(text);
        g.drawString(text, 
        		namePlace.x + namePlace.width - textRect.x/2,
				namePlace.y + namePlace.height + 2);
      }
      */
    	}

//    	g.setForeground(ColorSchema.COLOR_RED);
//    	g.drawRectangle(getBounds());
  }

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EEntity ep = getParent().getDefinition(modelDict, modelEG);
    if (getParent() instanceof EGSelect) {
      ep = ((EGSelect)getParent()).getHidenDefinition();
    } else 
    if (getParent() instanceof EGEnumerated) {
        ep = ((EGEnumerated)getParent()).getHidenDefinition();
    } 
    EEntity ec = getChild().getDefinition(modelDict, modelEG);

    if (getParent() instanceof EGPageRefLocalFrom) {
        EPage_relation pgrel = (EPage_relation)modelEG.createEntityInstance(EPage_relation.class);
        setDefinition(pgrel);
        updateModel(modelEG, pgrel);
        EPage_reference_bundle epp = (EPage_reference_bundle)getParent().getDefinitionPlacement(modelDict, modelEG);
        pgrel.setParent(null, epp);
        pgrel.setChild(null, ec);
    } else
    if (ep instanceof EPage_reference_from) {
      EPage_reference_from epp = (EPage_reference_from)ep;
      EPage_relation pgrel = (EPage_relation)modelEG.createEntityInstance(EPage_relation.class);
      pgrel.setParent(null, epp);
      pgrel.setChild(null, ec);
      epp.setExtended_relation(null, pgrel);
      setDefinition(pgrel);
      updateModel(modelEG, pgrel);
    } else {
      if (ec instanceof EPage_reference_to) {
        EPage_reference_to ecp = (EPage_reference_to)ec;
        ec = ((EGPageTo)getChild()).getReferencedObject().getDefinition(modelDict, modelEG);
      }
      switch (type) {
        case TYPE_AGREGATION:
          if (ep instanceof ESelect_type) {
            if (modelDict != null) {
              ANamed_type selection;
              if (((ESelect_type)ep).testLocal_selections(null)) {
                selection = ((ESelect_type)ep).getLocal_selections(null);
              } else {
                selection = ((ESelect_type)ep).createLocal_selections(null);
              }
              selection.addUnordered(ec);
              setDefinition(ep);
            }
              
            ESelect_relation_placement placeSel = (ESelect_relation_placement)
                modelEG.createEntityInstance(ESelect_relation_placement.class);
            placeSel.setParent(null, (ESelect_type)ep);
            updateModel(modelEG, placeSel);
          } else if (ep instanceof EDefined_type) {
            if (modelDict != null) {
              ((EDefined_type)ep).setDomain(null, agregate.getSDAIObject(modelDict, modelEG));
              setDefinition(ep);
            }

            EDefined_relation_placement placeSel = (EDefined_relation_placement)
                modelEG.createEntityInstance(EDefined_relation_placement.class);
            placeSel.setParent(null, (EDefined_type)ep);
            updateModel(modelEG, placeSel);
          } else if (ep instanceof ESchema_definition) {
              ESchema_relation_placement placeSel = (ESchema_relation_placement)
                  modelEG.createEntityInstance(ESchema_relation_placement.class);
              placeSel.setParent(null, (ESchema_definition)ep);
              placeSel.setChild(null, (ESchema_definition)ec);
              placeSel.setRelation_type(null, isOptional()?1:0);
              setDefinition(placeSel);
              updateModel(modelEG, placeSel);
          } else if (ep instanceof ESub_supertype_constraint) {
            EConstraint_relation_placement placeCon = (EConstraint_relation_placement)modelEG.createEntityInstance(EConstraint_relation_placement.class);
            placeCon.setParent(null, (ESub_supertype_constraint)ep);
            placeCon.setChild(null, (EData_type)ec);
            updateModel(modelEG, placeCon);
          } else {
            EAttribute_placement placeAttr = (EAttribute_placement)modelEG.
                createEntityInstance(EAttribute_placement.class);
            if (derived) {
              if (modelDict != null) {
                EDerived_attribute da = (EDerived_attribute)modelDict.
                	createEntityInstance(EDerived_attribute.class);
                da.setParent(null, (EEntity_or_view_definition)ep);
                da.setDomain(null, agregate.getSDAIObject(modelDict, modelEG));
                if (!"".equals(getName())) da.setName(null, getName());
                setDefinition(da);
              }
            } else {
              if (modelDict != null) {
                EExplicit_attribute ea = (EExplicit_attribute)modelDict.createEntityInstance(EExplicit_attribute.class);
                ea.setParent(null, (EEntity_or_view_definition)ep);
                ea.setDomain(null, agregate.getSDAIObject(modelDict, modelEG));
                ea.setOptional_flag(null, isOptional());
                if (!"".equals(getName())) ea.setName(null, getName());
                setDefinition(ea);

                if (inverse != null) {
// System.out.println("<EGRS> inverse 01: " + inverse);
                  if (ec instanceof EEntity_definition) {
// System.out.println("<EGRS> inverse 02: " + ec);
                    EInverse_attribute ia = (EInverse_attribute)modelDict.createEntityInstance(EInverse_attribute.class);
                    ia.setParent(null, (EEntity_or_view_definition)ep);
                    ia.setDomain(null, (EEntity_definition)ec);
                    ia.setInverted_attr(null, ea);
// System.out.println("<EGRS> inverse 03: " + ea);
                    if (!"".equals(inverse.getName())) ia.setName(null, inverse.getName());
                    if (inverse.getMinBound() != Agregate.BOUND_NONE) {
                      EBound boundMin = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
                      boundMin.setBound_value(null, inverse.getMinBound());
                      ia.setMin_cardinality(null, boundMin);
                    }
                    if (inverse.getMaxBound() != Agregate.BOUND_NONE) {
                      EBound boundMax = (EBound)modelDict.createEntityInstance(EInteger_bound.class);
                      boundMax.setBound_value(null, inverse.getMaxBound());
                      ia.setMax_cardinality(null, boundMax);
                    }
                    ia.setDuplicates(null, inverse.getType() == Agregate.TYPE_BAG);
                  } //else System.err.println("Invalid type for inverse attribute: " + ec);
                }
              }
            }
            Object attr_def = getDefinition();
            if (attr_def instanceof EAttribute)
            	placeAttr.setRepresented_object(null, (EAttribute)attr_def);
            else
            	SdaieditPlugin.log("No definition for relation:" + this + "\ninstance:" + placeAttr, IStatus.ERROR);
           	updateModel(modelEG, placeAttr);
          }
          break;
        case TYPE_INHERITANCE:
          if (modelDict != null) {
          	if (ep instanceof EEntity_definition) {
          		AEntity_or_view_definition spt;
/**@todo changed parent with child*/
          		if (((EEntity_or_view_definition)ec).testGeneric_supertypes(null)) {
          			spt = ((EEntity_or_view_definition)ec).getGeneric_supertypes(null);
          		} else {
          			spt = ((EEntity_or_view_definition)ec).createGeneric_supertypes(null);
          		}
          		spt.addUnordered(ep);
          		setDefinition(ec);
          	} else if (ep instanceof EExtensible_select_type) {
    			((EExtended_select_type)ec).setIs_based_on(null, (EDefined_type)((EGSelect)getParent()).getDefinition());
          	} else if (ep instanceof EExtensible_enumeration_type) {
    			((EExtended_enumeration_type)ec).setIs_based_on(null, (EDefined_type)((EGEnumerated)getParent()).getDefinition());
          	}
          }

          ESupertype_placement placeSup = (ESupertype_placement)modelEG.createEntityInstance(ESupertype_placement.class);
          placeSup.setParent(null, (EData_type)ep);
          placeSup.setChild(null, (EData_type)ec);
          updateModel(modelEG, placeSup);
/**/
          break;
      }
    }
  }

  /**
   * objectAt
   *
   * @param p Point
   * @return boolean
   * @todo Implement this jsdai.paint.Selection method
   */
  public boolean objectAt(Point p) {
    boolean near = false;
    for (int i = 1; (i < pline.length) && (!near); i++)
      if (!pline[i - 1].equals(pline[i]))near = objectNearLine(p, pline[i - 1],
          pline[i]);
    return near;
  }

  public String toString() {
    String name = getName();
    AbstractEGBox parent = getParent();
    AbstractEGBox child = getChild();
    if ("".equals(name)) name = "{" + (parent == null ? "null" : parent.getName()) + " -o " + (child == null ? "null" : child.getName()) + "}";
    return name;
  }
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
   */
  
  public boolean canBeInvisible() {
    return getChild().canBeInvisible() && getParent().canBeInvisible();
  }
  

	// RR - when the defined type is restricted by a where clause
	public boolean isRestricted() {
		return fRestricted;
	}

	// RR
	public void setRestricted(boolean fRestricted) {
		this.fRestricted = fRestricted;
		validDict = false;
		wrapper.setText(getText());
		fireLabelChanged();
	}



  /**
   * @return Returns the redeclaring.
   */
  public String getRedeclaring() {
    return redeclaring;
  }
  
  /**
   * @param redeclaring The redeclaring to set.
   */
  public void setRedeclaring(String redeclaring) {
    this.redeclaring = redeclaring;
    wrapper.setText(getText());
	updateBounds();
  }
  
  /**
   * @param redeclaring The redeclaring to set.
   */
  public void setRedeclaringInverse(String redeclaring) {
    redeclaringInverse = redeclaring;
    wrapper.setText(getText());
	updateBounds();
  }
  
  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Named#setName(java.lang.String)
	 */
	public void setName(String name) {
		super.setName(name);
		wrapper.setText(getText());
		updateBounds();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.LabelListener#labelChanged(java.lang.Object)
	 */
	public void labelChanged(Object invoker) {
		wrapper.setText(getText());
		updateBounds();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#getText()
	 */
	public String getText() {
		String text = getName();
		if (redeclaring != null) {
			if (redeclaring.equals("") || (redeclaring.equalsIgnoreCase(getName()))) text = "(RT) " + text;
			else text = "(RT) " + redeclaring + " > " + text;
		}
		if (derived) text = "(DER) " + text;
		if (text.length() > 0) text += " ";
		text += agregate.getText();
//		if (isConcreteValuesSet()) text += " = " + getConcreteValue();
//System.err.println("TEXT:" + isConcreteValuesSet() + " : " + getConcreteValue());		
		if (inverse != null) {
//System.out.println("IN getText <00-A> inverse: " + inverse);
//System.out.println("IN getText <00>:" + getName());
//if (agregate != null) System.out.println("IN getText <00-B>:" + agregate.getText());
//System.out.println("IN getText <01>:" + text);
// related_shape_aspect 
			if ((flag_inverted_in_supertype) && (parent_of_inverted != null)) {
				String text2 = null;
				if (text.startsWith("(RT)")) {
					text2 = text.substring(4,text.length());
					text = "((RT)" + parent_of_inverted + "." + text2 + ")";
			} else {
					text2 = text;
					text = "(" + parent_of_inverted + "." + text2 + ")";
				}
//				text = "(" + parent_of_inverted + "." + text2 + ")";
			}
			if (!StaticTools.equalStrings(text, "")) text += "\n(INV) ";
//System.out.println("IN getText <02>:" + text);
// related_shape_aspect 
			if (redeclaringInverse != null) {
//System.out.println("IN getText <03>:" + text);
				if (redeclaringInverse.equals("") || (redeclaringInverse.equalsIgnoreCase(getInverse().getName()))) {
					text += "(RT) ";
//System.out.println("IN getText <04>:" + text);
				} else {
					text += "(RT) " + redeclaring + " > ";
//System.out.println("IN getText <05>:" + text);
				}
			}
//System.out.println("IN getText <06>:" + text);
// related_shape_aspect 
	        text += inverse.getText();
//System.out.println("IN getText <07>:" + text);
// related_shape_aspect 
// (INV) basis_relationships S[:]
		}
    // RR
		if (isRestricted())
			text = showRestricted ? "*" + text : text; // attribute, restricted by UNIQUE clause or by WHERE clause

		return text;
	}
  	
  	/**
  	 * created for debugging use only
  	 * @return
  	 */
  	public TextWrapper getWrapper() {
  		return wrapper;
  	}
	
	protected void updateBounds() {
		Rectangle bounds = null;
		for (int i = 0; i < pline.length; i++) {
			if (bounds == null) {
				bounds = new Rectangle(pline[i].x, pline[i].y, 0, 0);
			} else {
				if (bounds.x > pline[i].x) {
					bounds.width += bounds.x - pline[i].x;
					bounds.x = pline[i].x;
				}
				if (bounds.y > pline[i].y) {
					bounds.height += bounds.y - pline[i].y;
					bounds.y = pline[i].y;
				}
				bounds.width = Math.max(bounds.width, pline[i].x - bounds.x);
				bounds.height = Math.max(bounds.height, pline[i].y - bounds.y);
			}
		}
		if (wrapper != null) {
	  		Point hint = getNamePlace();
			Rectangle textRect = new Rectangle(0, hint.y - wrapper.getLineHeight() * wrapper.getLineCount(),
					0, wrapper.getLineHeight() * wrapper.getLineCount());
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				textRect.width = Math.max(textRect.width, wrapper.getLineWidth(i));
			}
			if (getParent() instanceof EGSchema) {
				textRect.x = hint.x;
			} else {
				textRect.x = hint.x - textRect.width / 2;
			}

			if (bounds == null) bounds = textRect;
			else bounds.add(textRect);
		}
		
		if (bounds == null) bounds = new Rectangle(0, 0, 0, 0);
//System.out.println("BOUNDS: " + bounds);		
		setBounds(bounds);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Paging#setPage(int)
	 */
	public void setPage(int pgNr) {
		super.setPage(pgNr);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#setConcreteValues(java.lang.String[])
	 */
	public void setConcreteValues(String[] values) {
		super.setConcreteValues(values);
		wrapper.setText(getText());
		updateBounds();
	}
}
