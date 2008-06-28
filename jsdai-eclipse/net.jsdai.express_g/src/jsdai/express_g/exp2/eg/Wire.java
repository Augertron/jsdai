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

import jsdai.express_g.exp2.EGToolKit;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class Wire implements Comparable {
  public static final int DIRECTION_TOP = 1;
  public static final int DIRECTION_LEFT = 2;
  public static final int DIRECTION_RIGHT = 0;
  public static final int DIRECTION_BOTTOM = 3;

  public static final double PI = Math.PI;
  public static final double PI2 = PI / 2;
  public static final double PI4 = PI / 4;
  public static final double PI8 = PI / 8;
  public static final double PI180 = PI / 180;

  private AbstractEGBox box;
  private AbstractEGRelation relation;
  private int direction = DIRECTION_BOTTOM;
  // relation is attribute if this object is set as parent in a relation object
  private boolean attribute = false;
  private double angle = Double.NEGATIVE_INFINITY;
  private double distance = Double.NEGATIVE_INFINITY;

  public Wire(AbstractEGBox box, AbstractEGRelation relation, boolean attribute) {
    this.box = box;
    this.relation = relation;
    this.attribute = attribute;
//    updateDirection();
  }

  public AbstractEGRelation getRelation() {
    return relation;
  }

  public boolean isAttribute() {
    return attribute;
  }

  public int getDirection() {
    return direction;
  }

  /**
   * counts if boxes are overlaping by this direction
   * @param x1
   * @param w1
   * @param x2
   * @param w2
   * @return
   */
  private boolean overlap(double x1, double w1, double x2, double w2) {
    boolean ovr = false;
    if (x1 > x2) {
      if (x2 + w2 > x1) ovr = true;
    } else {
      if (x1 + w1 > x2) ovr = true;
    }
    return ovr;
  }

  /**
   * counts if point is overlaped by box
   * @param x
   * @param w
   * @param p
   * @return
   */
  private boolean overlap(double x, double w, double p) {
    boolean ovr = false;
    if ((p > x)&&(p < x + w)) ovr = true;
    return ovr;
  }

  /**
   * 0 - right
   * 1 - top
   * 2 - left
   * 3 - bottom
   */
  private boolean updateDirection() {
  	int directionOld = direction;
  	
    Point hint = relation.getHintPoint();
    Rectangle bounds1 = box.getBounds();
    
    if (relation instanceof EGRelationTree) {
    	Rectangle boundsP = relation.getParent().getBounds();
    	if ((boundsP.contains(hint)) && !isAttribute()) { // sided tree view
    		if (bounds1.x + bounds1.width / 2 < boundsP.x + boundsP.width / 2) {
    	        direction = DIRECTION_RIGHT;
    		} else {
    	        direction = DIRECTION_LEFT;
    		}
    	} else { // standart tree view
    		if ((angle < Math.PI)) { // 0 < angle < PI
    	        direction = DIRECTION_BOTTOM;
    		} else {
    	        direction = DIRECTION_TOP;
    		}
    	}
    	

//      if (isAttribute())direction = DIRECTION_BOTTOM;
//      else direction = DIRECTION_TOP;
    } else {

      double ang = angle;

      if (relation.getChild() == relation.getParent()) { // self relation
          if (isAttribute()) {
            if ((ang > 0) && (ang < PI2)) { // 0 < ang < PI/2
              direction = DIRECTION_RIGHT;
            } else
            if ((ang >= PI2) && (ang < PI)) { // PI/2 < ang < PI
              direction = DIRECTION_BOTTOM;
            } else
            if ((ang >= PI) && (ang < 3*PI2)) { // PI < ang < PI*3/2
              direction = DIRECTION_LEFT;
            } else {
              direction = DIRECTION_TOP;
            }
          } else {
              if ((ang > 0) && (ang < PI2)) { // 0 < ang < PI/2
                  direction = DIRECTION_BOTTOM;
                } else
                if ((ang >= PI2) && (ang < PI)) { // PI/2 < ang < PI
                  direction = DIRECTION_LEFT;
                } else
                if ((ang >= PI) && (ang < 3*PI2)) { // PI < ang < PI*3/2
                  direction = DIRECTION_TOP;
                } else {
                  direction = DIRECTION_RIGHT;
                }
          }
//          System.out.println("self " + isAttribute() + " " + direction + " @=" + ang);        
      } else { // not self relation
    	AbstractEGBox box2;
        if (isAttribute()) box2 = relation.getChild();
        else box2 = relation.getParent();
        Rectangle bounds2 = box2.getBounds();
    	Rectangle total = bounds1.union(bounds2);
//   	TODO uncomment isAttribute when rounding relation will be available
//		+-----+
//		|     |
//		|
//		+-
    	
    	if (hint.x < total.x) { // out off inner space
        	if (hint.y < total.y) {
        		if ((total.x - hint.x > total.y - hint.y)/*^isAttribute()*/) { 
        			direction = DIRECTION_LEFT;
        		} else {
        			direction = DIRECTION_TOP;
        		}
        	} else
           	if (hint.y > total.y + total.height) {
        		if ((total.x - hint.x > hint.y - total.y - total.height)/*^isAttribute()*/) {
        			direction = DIRECTION_LEFT;
        		} else {
        			direction = DIRECTION_BOTTOM;
        		}
           	} else {
           		direction = DIRECTION_LEFT;
           	}            		
        } else 
        if (hint.x > total.x + total.width) {
        	if (hint.y < total.y) {
        		if ((hint.x - total.x - total.width > total.y - hint.y)/*^isAttribute()*/) {
        			direction = DIRECTION_RIGHT;
        		} else {
        			direction = DIRECTION_TOP;
        		}
        	} else
           	if (hint.y > total.y + total.height) {
        		if ((hint.x - total.x - total.width > hint.y - total.y - total.height)/*^isAttribute()*/) {
        			direction = DIRECTION_RIGHT;
        		} else {
        			direction = DIRECTION_BOTTOM;
        		}
           	} else {
           		direction = DIRECTION_RIGHT;
           	}            		
        } else 
        if (hint.y < total.y) { 
        	direction = DIRECTION_TOP;
        } else 
        if (hint.y > total.y + total.height) {
        	direction = DIRECTION_BOTTOM;
        } else 
// overlaping hint            
        if (overlap(bounds1.x, bounds1.width, hint.x)) {
          if (bounds1.y < hint.y) {
            direction = DIRECTION_BOTTOM;
          } else {
            direction = DIRECTION_TOP;
          }
        } else
        if (overlap(bounds1.y, bounds1.height, hint.y)) {
          if (bounds1.x < hint.x) {
            direction = DIRECTION_RIGHT;
          } else {
            direction = DIRECTION_LEFT;
          }
        } else  
// overlaping boxes            
        if (overlap(bounds1.x, bounds1.width, bounds2.x, bounds2.width)) {
        	if (bounds1.y < bounds2.y) {
        		direction = DIRECTION_BOTTOM;
            } else {
            	direction = DIRECTION_TOP;
            }
        } else 
        if (overlap(bounds1.y, bounds1.height, bounds2.y, bounds2.height)) {
        	if (bounds1.x < bounds2.x) {
        		direction = DIRECTION_RIGHT;
        	} else {
        		direction = DIRECTION_LEFT;
        	}
        } else { // inner wire
        	Point p1 = new Point(bounds1.x, bounds1.y);
        	Point p2 = new Point(bounds2.x, bounds2.y);
        	if ((ang > 0)&&(ang < PI2)) {
        		p1.x += bounds1.width;
        		p1.y += bounds1.height;
        		double ah = EGToolKit.angle(p1, hint);
        		double a2 = EGToolKit.angle(p1, p2);
//System.out.println("REL" + (isAttribute() ? " attr:" : " link:") + getRelation() + "$" + Integer.toHexString(getRelation().hashCode()) + " B" + a2 + " H" + ah);            		
        		if (Math.abs(ah - a2) < PI180) {
           			direction = DIRECTION_BOTTOM;
        		} else
        		if (ah < a2) {
           			direction = DIRECTION_BOTTOM;
        		} else {
           			direction = DIRECTION_RIGHT;
        		}
        	} else 
        	if ((ang >= PI2)&&(ang < PI)) {
        		p2.x += bounds2.width;
        		p1.y += bounds1.height;
        		double ah = EGToolKit.angle(p1, hint);
        		double a2 = EGToolKit.angle(p1, p2);
//System.out.println("REL" + (isAttribute() ? " attr:" : " link:") + getRelation() + "$" + Integer.toHexString(getRelation().hashCode()) + " B" + a2 + " H" + ah);            		
          		if (Math.abs(ah - a2) < PI180) {
           			direction = DIRECTION_BOTTOM;
        		} else
        		if (ah < a2) {
           			direction = DIRECTION_LEFT;
        		} else {
           			direction = DIRECTION_BOTTOM;
        		}
        	} else
        	if ((ang >= PI)&&(ang < 3 * PI2)) {
        		p2.x += bounds2.width;
        		p2.y += bounds2.height;
        		double ah = EGToolKit.angle(p1, hint);
        		double a2 = EGToolKit.angle(p1, p2);
//System.out.println("REL" + (isAttribute() ? " attr:" : " link:") + getRelation() + "$" + Integer.toHexString(getRelation().hashCode()) + " B" + a2 + " H" + ah);            		
        		if (Math.abs(ah - a2) < PI180) {
           			direction = DIRECTION_TOP;
        		} else
        		if (ah <= a2) {
           			direction = DIRECTION_LEFT;
        		} else {
           			direction = DIRECTION_TOP;
        		}
        	} else {
        		p1.x += bounds1.width;
        		p2.y += bounds2.height;
        		double ah = EGToolKit.angle(p1, hint);
        		double a2 = EGToolKit.angle(p1, p2);
//System.out.println("REL" + (isAttribute() ? " attr:" : " link:") + getRelation() + "$" + Integer.toHexString(getRelation().hashCode()) + " B" + a2 + " H" + ah);            		
        		if (Math.abs(ah - a2) < PI180) {
           			direction = DIRECTION_TOP;
        		} else
        		if (ah <= a2) {
           			direction = DIRECTION_TOP;
        		} else {
           			direction = DIRECTION_RIGHT;
        		}
        	} 
        }
/*        	boolean near = distance < EGToolKit.distance(p2, hint);
            double angleB = Math.asin((double)(p2.y - p1.y) / EGToolKit.distance(p1, p2));
            if (p1.x > p2.x) {
                if (p1.y > p2.y) angleB =  -PI - angleB;
                else angleB = PI - angleB;
              }
              angleB = normalize(angleB);
        	
        	if ((ang > 0)&&(ang < PI2)) {
        		if (ang < angleB) {
           			direction = near ? DIRECTION_RIGHT : DIRECTION_BOTTOM;
        		} else {
           			direction = near ? DIRECTION_BOTTOM : DIRECTION_RIGHT;
        		}
        	} else 
        	if ((ang >= PI2)&&(ang < PI)) {
        		if (ang < angleB) {
           			direction = near ? DIRECTION_BOTTOM : DIRECTION_LEFT;
        		} else {
           			direction = near ? DIRECTION_LEFT : DIRECTION_BOTTOM;
        		}
        	} else
        	if ((ang >= PI)&&(ang < 3 * PI2)) {
        		if (ang < angleB) {
           			direction = near ? DIRECTION_LEFT : DIRECTION_TOP;
        		} else {
           			direction = near ? DIRECTION_TOP : DIRECTION_LEFT;
        		}
        	} else {
        		if (ang < angleB) {
           			direction = near ? DIRECTION_TOP : DIRECTION_RIGHT;
        		} else {
           			direction = near ? DIRECTION_RIGHT : DIRECTION_TOP;
        		}
        	} 
            System.out.println(Integer.toHexString(this.hashCode()) + ":" + direction + " angles=" + (int)(ang / PI * 180) + " <> " + (int)(angleB / PI * 180) + " near=" + near);*/
        }
        	
/*        if ((ang > PI4) && (ang < 3 * PI4)) { // 0 < ang < 2*PI
          direction = DIRECTION_BOTTOM;
        } else	
        if ((ang >= 3 * PI4)&&(ang <= 5 * PI4)) {
          direction = DIRECTION_LEFT;
        } else
        if ((ang > 5 * PI4)&&(ang < 7 * PI4)) {
          direction = DIRECTION_TOP;
        } else {
          direction = DIRECTION_RIGHT;
        }*/
    }
    return directionOld != direction;
  }

  /**
   * true if changed
   * @return
   */
  public boolean updateAngle() {
    Point p1 = box.getCenterPoint();
    Point p2 = relation.getHintPoint();
/*    if (isAttribute()) {
      Iterator cit = relation.getChilds().iterator();
      int count = 0;
      double cx = 0, cy = 0;
      while (cit.hasNext()) {
        Rectangle rect2 = ((AbstractEGBox)cit.next()).getBounds();
        cx += rect2.getCenterX();
        cy += rect2.getCenterY();
        count++;
      }
      p2.setLocation(cx / count, cy / count);
    } else {
      Rectangle rect2 = relation.getParent().getBounds();
      p2.setLocation(rect2.getCenterX(), rect2.getCenterY());
    }*/
/*    if (isAttribute()) {
      rect = null;
      Iterator iter = getRelation().getChilds().iterator();
      while (iter.hasNext()) if (rect == null) rect = ((AbstractEGBox)iter.next()).getBounds();
      else rect.add(((AbstractEGBox)iter.next()).getBounds());
    } else {
      rect = getRelation().getParent().getBounds();
    }
    p2.setLocation(rect.getCenterX(), rect.getCenterY());*/

    double distanceOld = distance;
    distance = EGToolKit.distance(p1, p2);
    double angleOld = angle;
    angle = Math.asin((double)(p2.y - p1.y) / distance);
    if (p1.x > p2.x) {
      if (p1.y > p2.y) angle =  -PI - angle;
      else angle = PI - angle;
    }
//    if (angleN < -PI4) angleN += 2 * Math.PI;
    angle = EGToolKit.normalizeAngle(angle);
    
    return updateDirection() || (Math.abs(angle - angleOld) > PI180) || (Math.abs(distance - distanceOld) > 0.1);
    
/*    if (Math.abs(angle - angleN) > PI / 90) {
      angle = angleN;
      distance = distanceN;
      updateDirection();
      return true;
    } else if (Math.abs(distance - distanceN) > 0.1) {
      distance = distanceN;
      return true;
    } else
      return false;*/
  }
  
  public double getAngle() {
    return angle;
  }
  
  	/** 
  	 * @return
  	 * @see java.lang.Object#hashCode()
  	 */
  	
  	public int hashCode() {
  		return relation.hashCode();
  	}
  

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *   <code>false</code> otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof AbstractEGRelation) {
      return relation.equals(obj);
    } else return super.equals(obj);
  }

  /**
   * Compares this object with the specified object for order.
   *
   * @param o the Object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is
   *   less than, equal to, or greater than the specified object.
   */
  public int compareTo(Object o) {
    int out = 0;
    int direct = ((Wire)o).getDirection();
    if (direction == direct) {
      double a1 = angle;
      double a2 = ((Wire)o).getAngle();
      if (direction == DIRECTION_RIGHT) { // angle splitting point
        if (a1 > Math.PI) a1 -= 2 * Math.PI;
        if (a2 > Math.PI) a2 -= 2 * Math.PI;
      }
      if (a1 > a2) out = 1; else
        if (a1 < a2) out = -1; else 
        	if (isAttribute() && !((Wire)o).isAttribute()) out = -1; else
            	if (!isAttribute() && ((Wire)o).isAttribute()) out = 1; else { // can't be equal
            		// need to identify relation somehow
            		String name1 = relation.getParent().getName() + relation.getName();
            		String name2 = ((Wire)o).getRelation().getParent().getName() + ((Wire)o).getRelation().getName();
            		out = name1.compareTo(name2);
            		if ((direction == DIRECTION_BOTTOM)||(direction == DIRECTION_RIGHT)) { // reverse comparison for part of the box
            			out = -out;
            		}
        }
    } else 
    if (direction > direct) out = -1;
    else out = 1;
    return out;
  }

}
