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

import java.util.Iterator;
import java.util.Vector;

import jsdai.SExtended_dictionary_schema.EDefined_type;
import jsdai.SExtended_dictionary_schema.EEntity_select_type;
import jsdai.SExtended_dictionary_schema.EExtended_select_type;
import jsdai.SExtended_dictionary_schema.EExtensible_select_type;
import jsdai.SExtended_dictionary_schema.ENon_extensible_select_type;
import jsdai.SExtended_dictionary_schema.ESelect_type;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @(#) EGSelect.java
 */

public class EGSelect extends AbstractEGBox implements IDefined_type {

  public static final int TYPE_NON_EXTENSIBLE = 1;
  public static final int TYPE_EXTENSIBLE = 2;
  public static final int TYPE_EXTENDED = 4;
  public static final int TYPE_ENTITY = 8;
  protected int type;

  private static int SELECT_TYPE_COUNTING = 1;

  public EGSelect(PropertySharing prop) {
  	super(prop);
    simpleSchema.lineStyle = 
    selectedSchema.lineStyle = ColorSchema.DASHED_LINE;
    setName("Select_type_" + (SELECT_TYPE_COUNTING++));
    setType(TYPE_EXTENSIBLE);
  }

  public EGSelect(PropertySharing prop, String name, Rectangle bounds, int type) {
    this(prop);
    setBounds(bounds);
    setName(name);
    setType(type);
  }

  public EGSelect(PropertySharing prop, String name, Point location, int type) {
    this(prop);
    setLocation(location);
    setName(name);
    setType(type);
  }

  /**
   * draw
   *
   * @param g2 Graphics2D
   * @todo Implement this jsdai.paint.Drawable method
   */
  public void draw(GC g) {
    super.draw(g);
    Rectangle bounds = getBounds();
    int x = bounds.x + bounds.width / 10;
    g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
  }
  
  // select definition, standart definition contains defined type
  private EEntity hidenDefinition;
  
  public EEntity getHidenDefinition() {
    return hidenDefinition;
  }
  
  public void setHidenDefinition(EEntity select) {
    hidenDefinition = select;
  }

  public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
    if (modelDict != null) {
	    Class clas = EExtensible_select_type.class;
	    if ((type & TYPE_ENTITY) != 0)
	    	clas = EEntity_select_type.class;
	    if ((type & TYPE_EXTENDED) != 0)
	        clas = EExtended_select_type.class;
	    if ((type & TYPE_NON_EXTENSIBLE) != 0)
	        clas = ENon_extensible_select_type.class;
	    if ((type & TYPE_EXTENSIBLE) != 0)
	        clas = EExtensible_select_type.class;
	    ESelect_type e1;
	    e1 = (ESelect_type)modelDict.createEntityInstance(clas);
	    e1.setName(null, "_SELECT_" + getName());
	    setDefinition(e1);
	    // hiding select under defined type 
	    EDefined_type ed;
	    ed = (EDefined_type)modelDict.createEntityInstance(EDefined_type.class);
	    ed.setName(null, getName());
	    ed.setDomain(null, e1);
	    setDefinition(ed);
	    if (!"".equals(shortName)) ed.setShort_name(null, shortName);
	    hidenDefinition = e1;
    }
    super.updateModel(modelDict, modelEG);
    /*
if (getName().equalsIgnoreCase("documented_element_select")) {
	System.err.println("ENTITY:" + eplacement);
	new Throwable().printStackTrace();
}*/
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
    fireLabelChanged();
  }

  /**
   * getTextPlace
   *
   * @return Rectangle
   */
  public Rectangle getTextInsets() {
    Rectangle place = super.getTextInsets();
    Rectangle bounds = getBounds();
    place.x += bounds.width / 10;
    place.width += bounds.width / 10;
    return place;
  }

  public String getText() {
    String text = super.getText();
    if ((type & TYPE_EXTENSIBLE) != 0) text = "(EX) " + text;
 		
 		if ((type & TYPE_ENTITY) != 0) text = "*" + text;
 		// text = "BEBE";
    return text;
  }

  public Selectable selectAsFirst(int type) {
    Selectable out = null;
    if (type == AbstractEGRelation.TYPE_AGREGATION) {
      Vector wires = getWires();
      Iterator iter = wires.iterator();
      while ((iter.hasNext())&&(out == null)) {
        Wire wire = (Wire)iter.next();
        if (wire.isAttribute()) out = wire.getRelation();
      }
      if (out == null) out = this;
    }
    return out;
  }

  public Selectable selectSecond(int type, Selectable second) {
    return ((second instanceof EGEntity)||(second instanceof EGDefined))?second:null;
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Select type";
  }
}
