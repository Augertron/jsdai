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

import java.util.Vector;
import java.util.Iterator;

import jsdai.SExpress_g_schema.EConstraint_relation_placement;
import jsdai.SExpress_g_schema.EPage_reference_to;
import jsdai.SExpress_g_schema.ERelation_placement;
import jsdai.SExpress_g_schema.ESelect_relation_placement;
import jsdai.SExpress_g_schema.ESupertype_placement;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_or_subtype_expression;
import jsdai.SExtended_dictionary_schema.ANamed_type;
import jsdai.SExtended_dictionary_schema.EAnd_subtype_expression;
import jsdai.SExtended_dictionary_schema.EAndor_subtype_expression;
import jsdai.SExtended_dictionary_schema.EData_type;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.EOneof_subtype_expression;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.SExtended_dictionary_schema.ESub_supertype_constraint;
import jsdai.SExtended_dictionary_schema.ESubtype_expression;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: LKSoftWare GmbH</p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class EGRelationTree extends AbstractEGRelation {
  public final static int SUBTYPE_EXPRESSION_ONEOF = 1;
  public final static int SUBTYPE_EXPRESSION_AND = 2;
  public final static int SUBTYPE_EXPRESSION_ANDOR = 3;

  protected int subtype_expression = SUBTYPE_EXPRESSION_ANDOR;

  protected Point[] pline = new Point[]{
      new Point(0, 0),
      new Point(0, 0),
      new Point(0, 0)
  };

  protected Vector plines = new Vector(3);
  protected Vector pcircle = new Vector(3);

  public EGRelationTree(PropertySharing prop, AbstractEGBox parent, AbstractEGBox child, int type) {
  	super(prop);
    selectedSchema2.lineWidth = 
    simpleSchema2.lineWidth = ColorSchema.STROKE_WIDTH2;

    setParent(parent);
    setChild(child);
    this.type = type;
  }

  public EGRelationTree(PropertySharing prop, EGRelationSimple relation) {
    this(prop, relation.getParent(), relation.getChild(), relation.getType());
    setDefinition(relation.getDefinition());
    setName(relation.getName());
    setHint1(relation.getHint1());
    setHint2(relation.getHint2());
    setPage(relation.getPage());
  }

  public EGRelationTree(PropertySharing prop, String name, AbstractEGBox parent, AbstractEGBox child, int type) {
    this(prop, parent, child, type);
    setName(name);
  }
  
	boolean updateAction() {
  		if (isOnPage(INVISIBLE_PAGE)) return false;
		Rectangle pb = getParent().getBounds();
		int minX = pb.x + pb.width / 2;
		int maxX = minX;
		Iterator cit = getChilds().iterator();
		int y = Integer.MAX_VALUE;
		while (cit.hasNext()) {
			AbstractEGBox child = (AbstractEGBox)cit.next();
			Rectangle bounds = child.getBounds();
			y = Math.min(y, bounds.y);
			int centerX = bounds.x + bounds.width / 2;
			if (centerX < minX) minX = centerX;
			if (centerX > maxX) maxX = centerX;
		}
		
		Point midle;
        if (special_draw) { // XXX
        	midle = getParent().getCenterPoint();
        } else {
        	midle = new Point((minX + maxX) / 2, (pb.y + pb.height + y) / 2);
        }
		if ((midle.x != hint.x)||(midle.y != hint.y)) {
			hint.x = midle.x;
			hint.y = midle.y;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean special_draw = false;
	
	public void setSpecialDraw(boolean state) {
		special_draw = state;
	}
	
  protected void updateRelationPlacement() {
    AbstractEGBox parent = getParent();
    if (special_draw != parent.getBounds().contains(getHintPoint())) {
    	special_draw = !special_draw;
    	Point midle;
    	if (special_draw) { // conversion (into box)
        	midle = getParent().getCenterPoint();
        	hint.width += hint.x - midle.x;
        	hint.height += hint.y - midle.y;
    	} else { // conversion (out of box)
    		Rectangle pb = getParent().getBounds();
    		int minX = pb.x + pb.width / 2;
    		int maxX = minX;
    		Iterator cit = getChilds().iterator();
    		int y = Integer.MAX_VALUE;
    		while (cit.hasNext()) {
    			AbstractEGBox child = (AbstractEGBox)cit.next();
    			if (child.isOnPage(getPage())) {
    				Rectangle bounds = child.getBounds();
    				y = Math.min(y, bounds.y);
    				int centerX = bounds.x + bounds.width / 2;
    				if (centerX < minX) minX = centerX;
    				if (centerX > maxX) maxX = centerX;
    			}
    		}
    		midle = new Point((minX + maxX) / 2, (pb.y + pb.height + y) / 2);
        	hint.width += hint.x - midle.x;
        	hint.height += hint.y - midle.y;
    	}
		hint.x = midle.x;
		hint.y = midle.y;
    }
    Vector childs = getChilds();
    pline[0] = parent.getPosition(this, false);
    pline[1] = new Point(pline[0].x, pline[0].y);
    pline[2] = new Point(pline[0].x, pline[0].y);
    Iterator iter = childs.iterator();
    plines.clear();
    pcircle.clear();
    while (iter.hasNext()) {
      AbstractEGBox child = (AbstractEGBox)iter.next();
      if (child.isOnPage(getPage())) {
        Point loc = child.getPosition(this, parent == child);
        plines.add(loc);
        if (special_draw) {
            if (loc.y < pline[1].y) {
            	pline[1].y = loc.y;
            }
            if (loc.y > pline[2].y) {
            	pline[2].y = loc.y;
            }
        } else {
            if (loc.x < pline[1].x) {
            	pline[1].x = loc.x;
            	pline[1].y = loc.y;
            }
            if (loc.x > pline[2].x) {
            	pline[2].x = loc.x;
            	pline[2].y = loc.y;
            }
        }
        // counting Point pc, based on Points p1 and p2
        if (!((child instanceof EGPageRefLocalTo)&&!(child instanceof IPageOverrider)&&(child.isVisible()))) {
          Point pc = new Point(loc.x, loc.y); // + ((loc.getY() > hint.getY()) ? -circleR : circleR));
          pcircle.add(pc);
        }
      }
    }
    if (!special_draw) {
        pline[1].y = hint.y + hint.height;
        pline[2].y = pline[1].y;
    }
//    hint.x = hintLoc.x;
//    hint.y = hintLoc.y;
    iter = pcircle.iterator();
    while (iter.hasNext()) {
      Point point = (Point)iter.next();
      if (special_draw) {
      	point.x += ((point.x > pline[0].x) ? -circleR : circleR);
      } else {
      	point.y += ((point.y > (hint.y + hint.height)) ? -circleR : circleR);
      }
    }

    Point nameLoc;
    if (special_draw) {
        nameLoc = new Point(pline[0].x + 5, Integer.MAX_VALUE);
	    Iterator itline = plines.iterator();
	    while (itline.hasNext()) {
	    	Point line = (Point)itline.next();
	      	nameLoc.y = Math.min(nameLoc.y, line.y - 5);
	    }
    } else {
        nameLoc = new Point(pline[0].x + 5, pline[1].y - 5);
    }
    namePlace.x = nameLoc.x;
    namePlace.y = nameLoc.y;
    
    updateBounds();
  }

  /* disabled 2005-01-25
   * due to relation updating failures, was overriding update() in AbstractEGRelation
   * @see AbstractEGRelation.update()
  public void update(int nr) {
    final double EPSILON = 0.1; // constant to compare doubles

    if (!isUpdating()) {
      AbstractEGBox parent = getParent();
    
      // counts hint point ordinate(Y)
      int childY = Integer.MAX_VALUE;
      Vector childs = getChilds();
      Iterator iter = childs.iterator();
      while (iter.hasNext()) {
        int cy = ((AbstractEGBox)iter.next()).getLocation().y;
        if (cy < childY) childY = cy;
      }
      
      Point midle = new Point(0, 0);
      // current state
      Point pp0 = parent.getPosition(this, false);
      // try resort related boxes
      boolean changedP = parent.sortWiresByNearest();
      boolean changed = true;
      // new state
      Point pp = parent.getPosition(this, false);
      int err_counter = 0;
      // while curent state != new state
      while ((changed)&&(EGToolKit.distance(pp0, pp) > EPSILON)) {
        changed = false;
        pp0 = pp;
        // recount hint point
        midle.x = pp.x;
        midle.y = (pp.y + childY) / 2;
        if (special_draw) { // XXX
        	midle = getParent().getCenterPoint();
        }
        hint.x = midle.x;
        hint.y = midle.y;

        // get new state
        if (parent.sortWiresByNearest()) {
          changedP = true;
          changed = true;
          pp = parent.getPosition(this, false);
        }

        err_counter++;
        if (err_counter > 100) {
          System.err.println("update loop in " + this);
          changed = false;
        }
      }
      
      // resort children wires
      boolean[] changedC = new boolean[childs.size()];
      for (int i = 0; i < changedC.length; i++)
        changedC[i] = ((AbstractEGBox)childs.get(i)).sortWiresByNearest();
      // update boxes (with related relations) if changed
      setUpdating(true);
      if (changedP) parent.update(2);
      for (int i = 0; i < changedC.length; i++)
        if (changedC[i]) ((AbstractEGBox)childs.get(i)).update(2);
      setUpdating(false);
      updateRelationPlacement();
    }
  }*/

  public void draw(GC g) {
    ColorSchema schema;
    if (type == TYPE_INHERITANCE) {
      schema = isSelected() ? selectedSchema2 : simpleSchema2;
    } else {
      schema = isSelected() ? selectedSchema : simpleSchema;
    }
    schema.apply(g);
	g.setFont(prop().getFont2());
	
//System.out.println("<<RR>>Vertical Line - x1: " + pline[1].x + ", y1: " + pline[1].y + ", x2: " + pline[2].x + ", y2: " + pline[2].y);
    g.drawLine(pline[1].x, pline[1].y, pline[2].x, pline[2].y);
	if (special_draw) {
	    Iterator itline = plines.iterator();
	    while (itline.hasNext()) {
	      Point line = (Point)itline.next();
//System.out.println("<<RR>>Horizontal Line - x1: " + line.x + ", y1: " + line.y + ", x2: " + pline[0].x + ", y2: " + line.y);
	      g.drawLine(line.x, line.y, pline[0].x, line.y);
	      g.drawLine(pline[0].x, pline[0].y, pline[0].x, line.y);
	    }
	} else {
	    g.drawLine(pline[0].x, pline[0].y, pline[0].x, pline[1].y);
	    Iterator itline = plines.iterator();
	    while (itline.hasNext()) {
	      Point line = (Point)itline.next();
	      g.drawLine(line.x, pline[1].y, line.x, line.y);
	    }
	}
    if (dcircle) {
    	if (getParent() instanceof EGConstraint) {
    		Iterator itline = plines.iterator();
    	    while (itline.hasNext()) {
    	    	Point pl = (Point)itline.next();

//System.out.println("===================");
//System.out.println("pl.x: " + pl.x + " - pline[0].x: " + pline[0].x + " - pline[1].x: " + pline[1].x + " - pline[2].x: " + pline[2].x);
//System.out.println("pl.y: " + pl.y + " - pline[0].y: " + pline[0].y + " - pline[1].y: " + pline[1].y + " - pline[2].y: " + pline[2].y);


	          double angle = 0.;
	          if (pline[0].y == pline[1].y) {
	          	// horizontal, requires RR - example: Assembly_structure_arm
	          	angle = EGToolKit.angle(new Point(pline[0].x,pl.y),pl); // RR - my own version 2 - works on horizontal
	          } else 
	          if (pline[0].y == pline[2].y) {
	          	// also horizontal, requires RR - example: PartTaxonomySchema
	          	angle = EGToolKit.angle(new Point(pline[0].x,pl.y),pl); // RR - my own version 2 - works on horizontal
	          } else {
	          	// vertical, requires original
	        		angle = EGToolKit.angle(new Point(pl.x, pline[1].y), pl); // original - works on vertical
	          }


//        		double angle = EGToolKit.angle(new Point(pl.x, pline[1].y), pl); // original - works on vertical
//System.out.println("angle original: " + angle);
//          	double angle = EGToolKit.angle(new Point(pline[0].x,pl.y),pl); // RR - my own version 2 - works on horizontal
//          	double angle2 = EGToolKit.angle(new Point(pline[0].x,pl.y),pl); // RR - my own version 2 - works on horizontal
//System.out.println("angle RR: " + angle2);
//System.out.println("-------------------");

        		if (!Double.isNaN(angle)) {
            		Point pp = new Point(0, 0);
            		pp.x = (int)(pl.x - circleR * 2 * Math.cos(angle + Math.PI / 6));
            		pp.y = (int)(pl.y - circleR * 2 * Math.sin(angle + Math.PI / 6));
            		g.drawLine(pp.x, pp.y, pl.x, pl.y);
            		pp.x = (int)(pl.x - circleR * 2 * Math.cos(angle - Math.PI / 6));
            		pp.y = (int)(pl.y - circleR * 2 * Math.sin(angle - Math.PI / 6));
            		g.drawLine(pp.x, pp.y, pl.x, pl.y);
        		}
    	    }
    	} else {
    		Iterator itcirc = pcircle.iterator();
   	      	while (itcirc.hasNext()) {
    	        Point pc = (Point)itcirc.next();
    	        g.fillOval(pc.x - circleR, pc.y - circleR, 2 * circleR, 2 * circleR);
    	        g.drawOval(pc.x - circleR, pc.y - circleR, 2 * circleR, 2 * circleR);
   	      	}
    	}
    }

    if ((dname)&&(this.getChildCount() > 1)) {
      String text = getName();
      Point textRect = g.stringExtent(text);
      g.drawString(text, 
      		namePlace.x + namePlace.width - textRect.x / 2 + 2,
			namePlace.y + namePlace.height - textRect.y);
    }
  }

  protected void updateModel(SdaiModel model, ERelation_placement placement) throws SdaiException {
    if (special_draw) { // XXX
		Rectangle pb = getParent().getBounds();
		int minX = pb.x + pb.width / 2;
		int maxX = minX;
		Iterator cit = getChilds().iterator();
		int y = Integer.MAX_VALUE;
		while (cit.hasNext()) {
			AbstractEGBox child = (AbstractEGBox)cit.next();
			Rectangle bounds = child.getBounds();
			y = Math.min(y, bounds.y);
			int centerX = bounds.x + bounds.width / 2;
			if (centerX < minX) minX = centerX;
			if (centerX > maxX) maxX = centerX;
		}
    	Point midle = new Point((minX + maxX) / 2, (pb.y + pb.height + y) / 2);
    	hint.width += hint.x - midle.x;
    	hint.height += hint.y - midle.y;
		hint.x = midle.x;
		hint.y = midle.y;
    	super.updateModel(model, placement);
    	midle = getParent().getCenterPoint();
    	hint.width += hint.x - midle.x;
    	hint.height += hint.y - midle.y;
		hint.x = midle.x;
		hint.y = midle.y;
    } else {
    	super.updateModel(model, placement);
    }
  }

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    EEntity ep = getParent().getDefinition(modelDict, modelEG);
    if (getParent() instanceof EGSelect) {
      ep = ((EGSelect)getParent()).getHidenDefinition();
    }
//    EEntity ec = getChild().getDefinition();

    switch (type) {
      case TYPE_AGREGATION :
        if (ep instanceof ESelect_type) {
          if (modelDict != null) {
            ANamed_type selection;
            if (((ESelect_type)ep).testLocal_selections(null)) {
              selection = ((ESelect_type)ep).getLocal_selections(null);
            } else {
              selection = ((ESelect_type)ep).createLocal_selections(null);
            }

            Iterator its = getChilds().iterator();
            while (its.hasNext()) {
              AbstractEGBox item = (AbstractEGBox)its.next();
              if (item instanceof EGPageTo) { // XXX old page reference
                selection.addUnordered(((EGPageTo)item).getReferencedObject().getDefinition(modelDict, modelEG));
                ((EPage_reference_to)((EGPageTo)item).getDefinition(modelDict, modelEG)).setParent(null, ep);
              }
              else selection.addUnordered(item.getDefinition(modelDict, modelEG));
            }
            setDefinition(ep);
          }
          ESelect_relation_placement placeSel = (ESelect_relation_placement)modelEG.createEntityInstance(ESelect_relation_placement.class);
          placeSel.setParent(null, (ESelect_type)ep);

          updateModel(modelEG, placeSel);
        } else if (ep instanceof ESub_supertype_constraint) {
            EConstraint_relation_placement placeCon = (EConstraint_relation_placement)modelEG.createEntityInstance(EConstraint_relation_placement.class);
            placeCon.setParent(null, (ESub_supertype_constraint)ep);
            if (getChild() instanceof EGPageTo)
                placeCon.setChild(null, (EData_type)((EGPageTo)getChild()).getReferencedObject().getDefinition(modelDict, modelEG));
              else placeCon.setChild(null, (EData_type)getChild().getDefinition(modelDict, modelEG));
            updateModel(modelEG, placeCon);
        }
        break;
      case TYPE_INHERITANCE :
        if (modelDict != null) {
          Iterator itp = getChilds().iterator();
          while (itp.hasNext()) {
            AbstractEGBox item = (AbstractEGBox)itp.next();
            EEntity_or_view_definition ec;
            AEntity_or_view_definition spt;
            if (item instanceof EGPageTo) {
              ec = (EEntity_or_view_definition)((EGPageTo)item).getReferencedObject().getDefinition(modelDict, modelEG);
              ((EPage_reference_to)((EGPageTo)item).getDefinition(modelDict, modelEG)).setParent(null, (EData_type)ep);
            } else ec = (EEntity_or_view_definition)item.getDefinition(modelDict, modelEG);
            if (((EEntity_or_view_definition)ec).testGeneric_supertypes(null)) {
              spt = ((EEntity_or_view_definition)ec).getGeneric_supertypes(null);
            } else {
              spt = ((EEntity_or_view_definition)ec).createGeneric_supertypes(null);
            }
            spt.addUnordered(ep);
            setDefinition(ep);
          }
          ESubtype_expression subexp = null;
          switch (subtype_expression) {
            case SUBTYPE_EXPRESSION_ONEOF :
              subexp = (ESubtype_expression)modelDict.createEntityInstance(EOneof_subtype_expression.class);
              break;
            case SUBTYPE_EXPRESSION_AND :
              subexp = (ESubtype_expression)modelDict.createEntityInstance(EAnd_subtype_expression.class);
              break;
           	case SUBTYPE_EXPRESSION_ANDOR :
        	default:
               subexp = (ESubtype_expression)modelDict.createEntityInstance(EAndor_subtype_expression.class);
               break;
            }
          AEntity_or_view_or_subtype_expression oneExp = subexp.createGeneric_operands(null);
          itp = getChilds().iterator();
          while (itp.hasNext()) {
            AbstractEGBox item = (AbstractEGBox)itp.next();
            if (item instanceof EGPageTo)
              oneExp.addUnordered(((EGPageTo)item).getReferencedObject().getDefinition(modelDict, modelEG));
            else oneExp.addUnordered(item.getDefinition(modelDict, modelEG));
          }
          ESub_supertype_constraint constraint = (ESub_supertype_constraint)modelDict.createEntityInstance(ESub_supertype_constraint.class);
          constraint.setGeneric_supertype(null, (EEntity_definition)ep);
          constraint.setConstraint(null, subexp);
        }

        ESupertype_placement placeSup = (ESupertype_placement)modelEG.createEntityInstance(ESupertype_placement.class);
        placeSup.setParent(null, (EEntity_definition)ep);
        if (getChild() instanceof EGPageTo)
          placeSup.setChild(null, (EEntity_definition)((EGPageTo)getChild()).getReferencedObject().getDefinition(modelDict, modelEG));
        else placeSup.setChild(null, (EEntity_definition)getChild().getDefinition(modelDict, modelEG));

        updateModel(modelEG, placeSup);
        /*
        AEntity_or_view_definition spt;
        if (((EEntity_or_view_definition)ep).testGeneric_supertypes(null)) {
          spt = ((EEntity_or_view_definition)ep).getGeneric_supertypes(null);
        } else {
          spt = ((EEntity_or_view_definition)ep).createGeneric_supertypes(null);
        }

        ESupertype_placement placeSup = (ESupertype_placement)modelEG.createEntityInstance(ESupertype_placement.class);
        placeSup.setParent(null, (EEntity_definition)ep);
        if (getChild() instanceof EGPageTo)
          placeSup.setChild(null, (EEntity_definition)((EGPageTo)getChild()).getReferencedObject().getDefinition(modelDict, modelEG));
        else placeSup.setChild(null, (EEntity_definition)getChild().getDefinition(modelDict, modelEG));

        Iterator itp = getChilds().iterator();
        while (itp.hasNext()) {
          AbstractEGBox item = (AbstractEGBox)itp.next();
          if (item instanceof EGPageTo) {
            spt.addUnordered(((EGPageTo)item).getReferencedObject().getDefinition(modelDict, modelEG));
            ((EPage_reference_to)((EGPageTo)item).getDefinition(modelDict, modelEG)).setParent(null, (EData_type)ep);
          } else spt.addUnordered(item.getDefinition(modelDict, modelEG));
        }
        setDefinition(ep);

        ESubtype_expression subexp = null;
        switch (subtype_expression) {
          case SUBTYPE_EXPRESSION_ONEOF :
            subexp = (ESubtype_expression)modelDict.createEntityInstance(EOneof_subtype_expression.class);
            break;
          case SUBTYPE_EXPRESSION_ANDOR :
            subexp = (ESubtype_expression)modelDict.createEntityInstance(EAndor_subtype_expression.class);
            break;
          case SUBTYPE_EXPRESSION_AND :
            subexp = (ESubtype_expression)modelDict.createEntityInstance(EAnd_subtype_expression.class);
            break;
        }
        AEntity_or_view_or_subtype_expression oneExp = subexp.createGeneric_operands(null);
        itp = getChilds().iterator();
        while (itp.hasNext()) {
          AbstractEGBox item = (AbstractEGBox)itp.next();
          if (item instanceof EGPageTo)
            oneExp.addUnordered(((EGPageTo)item).getReferencedObject().getDefinition(modelDict, modelEG));
          else oneExp.addUnordered(item.getDefinition(modelDict, modelEG));
        }
        ESub_supertype_constraint constraint = (ESub_supertype_constraint)modelDict.createEntityInstance(ESub_supertype_constraint.class);
        constraint.setGeneric_supertype(null, (EEntity_definition)ep);
        constraint.setConstraint(null, subexp);

        updateModel(modelEG, placeSup);
        */
        break;
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
    Point temp = new Point(pline[0].x, pline[1].y);
    near = objectNearLine(p, pline[0], temp)||objectNearLine(p, pline[1], pline[2]);
    Iterator iter = plines.iterator();
    while ((iter.hasNext())&&(!near)) {
      Point p2 = (Point)iter.next();
      if (special_draw) {
        temp.x = pline[0].x;
        temp.y = p2.y;
      } else {
        temp.x = p2.x;
        temp.y = pline[1].y;
      }
      near = objectNearLine(p, p2, temp);
    }
    return near;
  }

  public int getSubtype_expression() {
    return subtype_expression;
  }

  public void setSubtype_expression(int subtype_expression) {
  	if (this.subtype_expression != subtype_expression) {
  		this.subtype_expression = subtype_expression;
  		prop().setModified(true);
  	}
  }

  /**
   * getName
   *
   * @return String
   */
  public String getName() {
    String name = "";
    switch (subtype_expression) {
      case SUBTYPE_EXPRESSION_ONEOF :
        name = "1";
        break;
      case SUBTYPE_EXPRESSION_AND :
        name = "&";
        break;
    }
    return name;
  }

  public String toString() {
    return getParent().getName() + " -E ...(" + getChildCount() + ")";
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
		Iterator iter = plines.iterator();
		while (iter.hasNext()) {
			Point p = (Point)iter.next();
			if (bounds == null) {
				bounds = new Rectangle(p.x, p.y, 0, 0);
			} else {
				bounds.x = Math.min(bounds.x, p.x);
				bounds.y = Math.min(bounds.y, p.y);
				bounds.width = Math.max(bounds.width, p.x - bounds.x);
				bounds.height = Math.max(bounds.height, p.y - bounds.y);
			}
		}

		if (bounds == null) bounds = new Rectangle(0, 0, 0, 0);
		setBounds(bounds);
//System.out.println("Tree-updateBounds - AFTER: " + bounds);
	}
}
