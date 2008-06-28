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

import jsdai.SExtended_dictionary_schema.EBinary_type;
import jsdai.SExtended_dictionary_schema.EBoolean_type;
import jsdai.SExtended_dictionary_schema.EBound;
import jsdai.SExtended_dictionary_schema.EInteger_bound;
import jsdai.SExtended_dictionary_schema.EInteger_type;
import jsdai.SExtended_dictionary_schema.ELogical_type;
import jsdai.SExtended_dictionary_schema.ENumber_type;
import jsdai.SExtended_dictionary_schema.EReal_type;
import jsdai.SExtended_dictionary_schema.ESimple_type;
import jsdai.SExtended_dictionary_schema.EString_type;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogSimple;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @(#) EGSimple.java
 */

public class EGSimple extends AbstractEGBox implements IHidenRef {
  public static final int TYPE_INTEGER = 0;
  public static final int TYPE_NUMBER = 1;
  public static final int TYPE_BOOLEAN = 2;
  public static final int TYPE_LOGICAL = 3;
  public static final int TYPE_BINARY = 4;
  public static final int TYPE_REAL = 5;
  public static final int TYPE_STRING = 6;
  public static final String[] TYPE_NAME = {
      "INTEGER", "NUMBER", "BOOLEAN", "LOGICAL", "BINARY", "REAL", "STRING"
  };
  protected int type;

  public final static int WIDTH_NONE = Integer.MIN_VALUE;
  protected int type_width = WIDTH_NONE;
  protected boolean type_width_fixed = false;

  public EGSimple(PropertySharing prop, EGSimple simple) {
    this(prop, simple.getBounds(), simple.getType(), simple.getType_width(),simple.isType_width_fixed());
  }

  public EGSimple(PropertySharing prop) {
  	super(prop);
    setType(TYPE_INTEGER);
  }

  public EGSimple(PropertySharing prop, int type) {
    this(prop);
    setType(type);
  }

  public EGSimple(PropertySharing prop, Rectangle bounds, int type, int type_width, boolean type_width_fixed) {
  	super(prop);
    setBounds(bounds);
    setType(type);
    setType_width(type_width);
    setType_width_fixed(type_width_fixed);
  }

  public EGSimple(PropertySharing prop, Point location, int type, int type_width, boolean type_width_fixed) {
    this(prop);
    setLocation(location);
    setType(type);
    setType_width(type_width);
    setType_width_fixed(type_width_fixed);
  }

  /**
   * draw
   *
   * @param g2 Graphics2D
   * @todo Implement this jsdai.paint.Drawable method
   */
  public void draw(GC g) {
    if (isVisible()) {
      super.draw(g);
      Rectangle bounds = getBounds();
      int x = bounds.x + bounds.width - bounds.width / 10;
      g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
// XXX EG item marked for deletion
      g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_RED]);
      g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
      g.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);
      
    } else {
      drawInvisibleBox(g);
    }
  }

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    if (modelDict != null) {
	      Class clas = EInteger_type.class;
	      switch (type) {
	        case TYPE_BINARY:
	          clas = EBinary_type.class;
	          break;
	        case TYPE_BOOLEAN:
	          clas = EBoolean_type.class;
	          break;
	        case TYPE_LOGICAL:
	          clas = ELogical_type.class;
	          break;
	        case TYPE_NUMBER:
	          clas = ENumber_type.class;
	          break;
	        case TYPE_REAL:
	          clas = EReal_type.class;
	          break;
	        case TYPE_STRING:
	          clas = EString_type.class;
	          break;
	      }
	      ESimple_type e1;
	      e1 = (ESimple_type) modelDict.createEntityInstance(clas);
	      switch (type) {
	        case TYPE_BINARY:
	          if (type_width != WIDTH_NONE) {
	            EBound boundBin = (EBound) modelDict.createEntityInstance(EInteger_bound.class);
	            boundBin.setBound_value(null, type_width);
	            ((EBinary_type) e1).setWidth(null, boundBin);
	          }
	          ((EBinary_type) e1).setFixed_width(null, type_width_fixed);
	          break;
	        case TYPE_REAL:
	          if (type_width != WIDTH_NONE) {
	            EBound boundReal = (EBound) modelDict.createEntityInstance(
	                EInteger_bound.class);
	            boundReal.setBound_value(null, type_width);
	            ((EReal_type) e1).setPrecision(null, boundReal);
	          }
	          break;
	        case TYPE_STRING:
	          if (type_width != WIDTH_NONE) {
	            EBound boundStr = (EBound) modelDict.createEntityInstance(
	                EInteger_bound.class);
	            boundStr.setBound_value(null, type_width);
	            ((EString_type) e1).setWidth(null, boundStr);
	          }
	          ((EString_type) e1).setFixed_width(null, type_width_fixed);
	          break;
	      }
	
	      e1.setName(null, getName());
	      setDefinition(e1); 
    }
    super.updateModel(modelDict, modelEG);
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    setName(TYPE_NAME[type]);
    this.type = type;
	fireLabelChanged();
  }

  /**
   * getTextPlace
   *
   * @return Rectangle
   * @todo Implement this jsdai.paint.eg.AbstractEGBox method
   */
  public Rectangle getTextInsets() {
    Rectangle place = super.getTextInsets();
    Rectangle bounds = getBounds();
    place.width += bounds.width / 10;
    return place;
  }

  public boolean createEditDialog(Composite parent) {
    dialog = new DialogSimple(parent, this, prop());
    return isEditDialogCreated();
  }

  public int getType_width() {
    return type_width;
  }

  public boolean isType_width_fixed() {
    return type_width_fixed;
  }

  public void setType_width(int type_width) {
    this.type_width = type_width;
  }

  public void setType_width_fixed(boolean type_width_fixed) {
    this.type_width_fixed = type_width_fixed;
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Simple type";
  }
  /* (non-Javadoc)
   * @see jsdai.express_g.exp.eg.AbstractEGObject#canBeInvisible()
   */
  
  public boolean canBeInvisible() {
    return true;
  }
  
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		return null;
	}
}
