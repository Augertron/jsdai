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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import jsdai.express_g.common.TextWrapper;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 * @deprecated
 */
public class EGPageToRef extends EGPageTo implements IPageOverrider {
	protected TextWrapper wrapper2 = null;
  
	public EGPageToRef(PropertySharing prop, EGPageFrom pageTo) {
	  	super(prop, pageTo);
		wrapper2 = new TextWrapper("", "");
		wrapper2.setStyle(wrapper.getStyle());
	}

	public EGPageToRef(PropertySharing prop, EGPageFrom pageTo, Rectangle bounds) {
	  	this(prop, pageTo);
	    setBounds(bounds);
	}

	public EGPageToRef(PropertySharing prop, EGPageFrom pageTo, Point location) {
		this(prop, pageTo);
	    setLocation(location);
	}

	public void setType(int type) {
		EGEntityRef ref = (EGEntityRef)getReferencedObject();
		ref.setType(type);
	}
  
	public int getType() {
		AbstractEGBox ref = getReferencedObject();
		return ref instanceof EGEntityRef ? ((EGEntityRef)ref).getType() : EGEntityRef.TYPE_USED;
	}

  	public void setSchemaName(String schemaName) {
		EGEntityRef ref = (EGEntityRef)getReferencedObject();
		ref.setSchemaName(schemaName);
		wrapper.setText(getText());
  	}
  
  	public String getSchemaName() {
		AbstractEGBox ref = getReferencedObject();
		return ref instanceof EGEntityRef ? ((EGEntityRef)ref).getSchemaName() : "";
  	}	
  
  	public AbstractEGBox getEGEntity() {
		EGEntityRef ref = (EGEntityRef)getReferencedObject();
		return ref.getEGEntity();
  	}
  
  	public void setRename(String rename) {
		EGEntityRef ref = (EGEntityRef)getReferencedObject();
		ref.setRename(rename);
		wrapper2.setText(rename);
  	}
  
  	public String getRename() {
		AbstractEGBox ref = getReferencedObject();
		return ref instanceof EGEntityRef ? ((EGEntityRef)ref).getRename() : "";
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
  			if (getType() == EGEntityRef.TYPE_REFERENCED) g.setLineStyle(SWT.LINE_DASH);
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
  		} else {
  			drawInvisibleBox(g);
  		}
//  	 XXX EG item marked for deletion
        g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_RED]);
        g.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
        g.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);
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
    Rectangle place = new Rectangle(textInset, textInset, 2*textInset, 2*textInset);
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
    return new Rectangle(textInset, textInset, 2*textInset, 2*textInset);
  }

  public boolean createEditDialog(Composite parent) {
	EGEntityRef ref = (EGEntityRef)getReferencedObject();
	ref.createEditDialog(parent);
    dialog = ref.dialog;
    return isEditDialogCreated();
  }
  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		return null;
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
		
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.EGPage#updateWrapper()
	 */
	void updateWrapper() {
		super.updateWrapper();
		if (wrapper2 != null) wrapper2.setText(getRename());
	}
}
