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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import jsdai.SExtended_dictionary_schema.EImplicit_declaration;
import jsdai.SExtended_dictionary_schema.EInterfaced_declaration;
import jsdai.SExtended_dictionary_schema.EReferenced_declaration;
import jsdai.SExtended_dictionary_schema.EUsed_declaration;
import jsdai.express_g.common.TextWrapper;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.Selectable;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogEntityRef;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class EGEntityRef extends AbstractEGBox implements IHidenRef {
	protected TextWrapper wrapper2 = null;

	private String schemaName = "schema";
	private String rename = "";
	public final static int TYPE_USED = 1;
	public final static int TYPE_REFERENCED = 2;
	public final static int TYPE_IMPLICIT = 3;
	private int type = TYPE_USED;
  
	private AbstractEGBox referenced = null;
  
//  protected int midle_height;

//  public EGEntityRef() {
//    Dimension size = new Dimension();
//    size.setSize(basicWidth, basicHeight);
//    setSize(size);
//    setName("Ref_Entity_" + (ENTITY_COUNTING++));
//  }

  public EGEntityRef(PropertySharing prop, AbstractEGBox entity, String schema_name, int type) {
  	super(prop);
	wrapper2 = new TextWrapper("", "");
	wrapper2.setStyle(wrapper.getStyle());

	referenced = entity;
    setBounds(entity.getBounds());
    setType(type);
    setPage(entity.getPage());
    // colors
    this.nonVisibleSchema = entity.nonVisibleSchema;
    this.nonVisibleSchema.lineStyle = ColorSchema.DASHED_LINE;
    this.selectedSchema = entity.selectedSchema;
    this.selectedSchema.lineStyle = ColorSchema.DASHED_LINE;
    this.simpleSchema = entity.simpleSchema;
    this.simpleSchema.lineStyle = ColorSchema.DASHED_LINE;
    
    // moves all relations (+inheritance)
    Iterator itw = entity.getWires().iterator();
    while (itw.hasNext()) {
      Wire wire = (Wire)itw.next();
      AbstractEGRelation rel = wire.getRelation();
      if (wire.isAttribute()) {
        rel.setParent(this);
      } else {
        rel.removeChild(entity);
        rel.addChild(this);
        if (rel instanceof EGRelationSimple) {
        	if (rel.getParent() instanceof EGPageFrom) {
        		EGPageFrom pgfrom = (EGPageFrom)rel.getParent();
        		pgfrom.setReferencedObject(this);
        		Iterator iter = pgfrom.getReferences().iterator();
        		while (iter.hasNext()) {
        			EGPageTo pgto = (EGPageTo)iter.next();
        			pgto.setReferencedObject(this);
            		AbstractEGRelation pgrel = pgto.getRefRelation();
            		if (pgrel instanceof EGRelationSimple) {
                		Agregate agregate = ((EGRelationSimple)pgrel).getAgregate();
                		if (agregate != null) agregate.setAgregation_type(this);
            		}
        		}
        	} 
        	Agregate agregate = ((EGRelationSimple)rel).getAgregate();
        	if (agregate != null) agregate.setAgregation_type(this);
        }
      }
    }
    setName(entity.getName());
    setSchemaName(schema_name);
    setLabelNew(entity.isLabelNew());
  }
/*  
  public EGEntityRef(String name, Point location, String schema_name, int type) {
//    this();
    setLocation(location);
    setName(name);
    setSchemaName(schema_name);
    setType(type);
  }*/

  public void setType(int type) {
    this.type = type;
  }
  
  public int getType() {
    return type;
  }

  	public void setSchemaName(String schemaName) {
  		if (!this.schemaName.equals(schemaName)) {
  			this.schemaName = schemaName;
  			wrapper.setText(getText());
  			fireLabelChanged();
  		}
  	}
  
  public String getSchemaName() {
    return schemaName;
  }
  
  public AbstractEGBox getEGEntity() {
  	return referenced;
  }
  
  public void setRename(String rename) {
    if (!this.rename.equals(rename)) {
      this.rename = rename;
      wrapper2.setText(rename);
		fireLabelChanged();
    }
  }
  
  
  public String getRename() {
    return rename;
  }
  
  public Selectable selectAsFirst(int type) {
    return (type == AbstractEGRelation.TYPE_INHERITANCE)?this:null;
  }

  public Selectable selectSecond(int type, Selectable second) {
    boolean out = false;
    if (type == AbstractEGRelation.TYPE_INHERITANCE) {
      out = second instanceof EGEntity;
    }
    return out?second:null;
  }

  /**
   * getUIName
   *
   * @return String
   */
  public String getUIName() {
    return "Entity Reference";
  }
  
  /**
   * draw
   * 
   * @param g2 Graphics2D
   */
  public void draw(GC g) {
  		Rectangle bounds = getBounds();
  		
  		if (isVisible()) {
  			ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
  			schema.apply(g);
  			g.setFont(prop().getFont1());
  			if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))||(!wrapper2.isGCValid())||(!g.equals(wrapper2.getGC()))) {
  	  			wrapper.setGC(g);
  	  			wrapper2.setGC(g);
  	  			if (isLabelNew()) {
  	  				setBounds(new Rectangle(bounds.x, bounds.y, 0, 0));
  	  			} else {
  	  	  			setBounds(bounds);
  	  			}
  	  			bounds = getBounds();
  	  		}
	
  		    int midle_height = wrapper.getLineHeight() * wrapper.getLineCount();
  			if (type == TYPE_REFERENCED) g.setLineStyle(SWT.LINE_DASH);
  			g.fillRectangle(bounds);
  			g.drawRectangle(bounds);
  			schema.apply(g);
  			g.drawRoundRectangle(bounds.x, bounds.y + (bounds.height - midle_height) / 2, 
  					bounds.width, midle_height, midle_height - 1, midle_height - 1);

		    Rectangle textPlace = getTextPlace();
			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				g.drawString(wrapper.getLine(i), 
						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
			}
			textStartAt += wrapper.getLineHeight() * wrapper.getLineCount();
			for (int i = 0; i < wrapper2.getLineCount(); i++) {
				g.drawString(wrapper2.getLine(i), 
						textPlace.x + (textPlace.width - wrapper2.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper2.getLineHeight(), true);
			}
			// XXX EG item marked for deletion
		      g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_RED]);
		      g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
		      g.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);
  		} else {
  			drawInvisibleBox(g);
  		}
  }

  public String getText() {
    String text = getSchemaName() + "." + getName();
/*    String rename = getRename();
    if ((rename != null)&&(rename != "")) {
      text += " > " + rename;
    }*/
    return text;
  }

  /**
   * getTextPlace
   *
   * @return Rectangle
   */
  public Rectangle getTextInsets() {
    Rectangle place = super.getTextInsets();
    int midle_height = wrapper.getLineHeight() * wrapper.getLineCount();
    place.x += midle_height / 4;
    place.width += midle_height / 2;
    return place;
  }
/**/  

  /**
   * getTextPlace
   *
   * @return Rectangle
   */
  public Rectangle getTextInsets2() {
    return super.getTextInsets();
  }

  public boolean createEditDialog(Composite parent) {
    dialog = new DialogEntityRef(parent, this, prop());
    return isEditDialogCreated();
  }

  	public void updateModel(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
  		if (modelDict != null) {
  			EInterfaced_declaration decl = null;
  			switch (type) {
  				case TYPE_IMPLICIT :
  		  			decl = (EImplicit_declaration)modelDict.createEntityInstance(EImplicit_declaration.class);
  					break;
  				case TYPE_USED :
  		  			decl = (EUsed_declaration)modelDict.createEntityInstance(EUsed_declaration.class);
  					break;
  				case TYPE_REFERENCED :
  				default :
  		  			decl = (EReferenced_declaration)modelDict.createEntityInstance(EReferenced_declaration.class);
  					break;
  			}
  			decl.setDefinition(null, referenced.getDefinition());
  			if ((getRename() != null) && (!"".equals(getRename()))) decl.setAlias_name(null, getRename());
  			setDefinition(decl);
  		}
  		super.updateModel(modelDict, modelEG);
      
/*  	
    if (modelDict != null) {
      EEntity_definition e1;
      e1 = (EEntity_definition)modelDict.createEntityInstance(EEntity_definition.class);
      e1.setName(null, getName());
      setDefinition(e1);
      if (shortName != "") e1.setShort_name(null, shortName);
    }
    
    EEntity_reference schema_ref = (EEntity_reference)modelEG.createEntityInstance(EEntity_reference.class);
    ELocation loc = (ELocation)modelEG.createEntityInstance(ELocation.class);
    Point cloc = getLocation();
    loc.setX(null, cloc.x);
    loc.setY(null, cloc.y);
    schema_ref.setObject_location(null, loc);

    ESize size = (ESize)modelEG.createEntityInstance(ESize.class);
    Point csize;
    if (isLabelNew()) csize = new Point(0, 0);
    	else csize = getSize();
    size.setWidth(null, csize.x);
    size.setHeight(null, csize.y);
    schema_ref.setObject_size(null, size);

    schema_ref.setRepresented_object(null, (EEntity_definition)getDefinition(modelDict, modelEG));
    EColor color = (EColor)modelEG.createEntityInstance(EColor.class);
    color.setRed(null, simpleSchema.background.getRed());
    color.setGreen(null, simpleSchema.background.getGreen());
    color.setBlue(null, simpleSchema.background.getBlue());
    schema_ref.setRepresentation_color(null, color);

    schema_ref.setSchema_name(null, getSchemaName());
    String ren = getRename();
    if ((ren != null)&&(ren != "")) schema_ref.setRedeclared_name(null, ren);
    if (getType() == TYPE_REFERENCED) schema_ref.setReferenced(null, true);
    else schema_ref.setReferenced(null, false);

    setDefinitionPlacement(schema_ref);*/
  }
  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#getDefinition(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public EEntity getDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
	    super.getDefinition(modelDict, modelEG);
	    return referenced.getDefinition();
	}

	public EEntity getThisDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
	    return super.getDefinition(modelDict, modelEG);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#getDefinition()
	 */
	public EEntity getDefinition() {
		return referenced.getDefinition();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		String[] names = EGToolKit.parseSchemaName(getSchemaName());
		return super.getXMLDefinition(startat, names[0], names[1], names[2]);
	}

	  /**
	   * setSize
	   *
	   * @param size Dimension
	   */
	  	public void setSize(Point size) {
	  		if (isVisible()) {
	  			Rectangle insets = null;
	  			Rectangle insets2 = null;
	  			int errCount = 0;
	  			Point size0 = new Point(-1, -1);
	  			Point size1 = getSize();
	  			while ((!size0.equals(size1))&&(errCount++ < 100)) {
	  				insets = getTextInsets();
	  				insets2 = getTextInsets2();
	  				wrapper.setWidth(size.x - insets.width);
	  				wrapper2.setWidth(size.x - insets2.width);
	  				size.x = Math.max(size.x, 
	  						Math.max(wrapper.getMinWidth() + insets.width, 
	  								wrapper2.getMinWidth() + insets2.width));
	  				size.y = Math.max(size.y, 
	  						wrapper.getLineHeight() * wrapper.getLineCount() 
							+ wrapper2.getLineHeight() * wrapper2.getLineCount() * 2 
							+ insets.height + insets2.height);
	  				super.setSize(size);
	  				size0 = size1;
	  				size1 = getSize();
	  			}
	  			if (!size0.equals(size1)) {
//	  			System.err.println("Failed to set item size properly");
	  				new Exception("Failed to set item size properly").printStackTrace();
	  			}
	  		} else super.setSize(size);
	  	}
		
		/* (non-Javadoc)
		 * @see jsdai.express_g.exp2.eg.AbstractEGObject#setLabelNew(boolean)
		 */
		public void setLabelNew(boolean isNew) {
			super.setLabelNew(isNew);
			if (wrapper2 != null) wrapper2.setDelimiters(isNew ? "" : NAME_DELIMITERS);
		}
		
	  	/**
	  	 * created for debugging use only
	  	 * @return
	  	 */
		public TextWrapper getWrapper2() {
			return wrapper2;
		}
}
