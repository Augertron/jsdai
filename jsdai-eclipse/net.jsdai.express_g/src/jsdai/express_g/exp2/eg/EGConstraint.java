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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import jsdai.express_g.common.TextWrapper;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class EGConstraint extends AbstractEGBox {
	protected TextWrapper wrapper2 = null;
	protected boolean abs = false;
	protected AbstractEGBox referenced = null;

	/**
	 * @param prop
	 */
	public EGConstraint(PropertySharing prop, String name, AbstractEGBox referenced, boolean abs) {
		super(prop);
		setName(name);
		this.referenced = referenced;
		wrapper2 = new TextWrapper("", "");
		wrapper2.setStyle(wrapper.getStyle());
		setAbstract(abs);
	}

	/**
	 * 
	 */
	protected EGConstraint() {
		super();
		wrapper2 = new TextWrapper("", "");
		wrapper2.setStyle(wrapper.getStyle());
	}

	/**
	 * getUIName
	 *
	 * @return String
	 */
	public String getUIName() {
		return "Subtype Constraint";
	}

	public String getText() {
		return "*" + super.getText();
	}

	public boolean isAbstract() {
		return abs;
	}
	
	public void setAbstract(boolean abs) {
		if (this.abs != abs) {
			this.abs = abs;
			if (wrapper2 != null) wrapper2.setText(abs ? "(ABS)" : "");
		}
	}
	
	public AbstractEGBox getReferenced() {
		return referenced;
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
		
  			g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
  			g.drawOval(bounds.x, bounds.y, bounds.width, bounds.height);

		    Rectangle textPlace = getTextPlace();
			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount() 
					- wrapper2.getLineHeight() * wrapper2.getLineCount()) / 2;
			for (int i = 0; i < wrapper2.getLineCount(); i++) {
				g.drawString(wrapper2.getLine(i), 
						textPlace.x + (textPlace.width - wrapper2.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper2.getLineHeight(), true);
			}
			textStartAt += wrapper2.getLineHeight() * wrapper2.getLineCount();
			for (int i = 0; i < wrapper.getLineCount(); i++) {
				g.drawString(wrapper.getLine(i), 
						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
			}
		} else {
			drawInvisibleBox(g);
		}
	}
	  
	/**
	 * setSize
	 *
	 * @param size Dimension
	 */
	public void setSize(Point size) {
		if (isVisible()) {
			Rectangle insets = null;
			int errCount = 0;
			Point size0 = new Point(-1, -1);
			Point size1 = getSize();
			while ((!size0.equals(size1))&&(errCount++ < 100)) {
				insets = getTextInsets();
				wrapper.setWidth(size.x - insets.width);
				wrapper2.setWidth(size.x - insets.width);
				size.x = Math.max(size.x, 
						Math.max(wrapper.getMinWidth() + insets.width, 
								wrapper2.getMinWidth() + insets.width));
				size.y = Math.max(size.y, 
						wrapper.getLineHeight() * wrapper.getLineCount() 
						+ wrapper2.getLineHeight() * wrapper2.getLineCount() 
						+ insets.height);
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
	
  	/**
  	 * created for debugging use only
  	 * @return
  	 */
	public TextWrapper getWrapper2() {
		return wrapper2;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGBox#getTextInsets()
	 */
	public Rectangle getTextInsets() {
		Rectangle inset = super.getTextInsets();
	    Rectangle bounds = getBounds();
		inset.x += bounds.width / 8; 
		inset.width += bounds.width / 4; 
		inset.y += bounds.height / 8; 
		inset.height += bounds.height / 4; 
		return inset;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#updateModel(jsdai.lang.SdaiModel, jsdai.lang.SdaiModel)
	 */
	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {
		// TODO Total cover
		super.updateModel(modelDict, modelEG);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(org.eclipse.swt.graphics.Point, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		Rectangle bounds = getBounds();
		bounds.x -= startat.x;
		bounds.y -= startat.y;
		int dd = bounds.height / 2;
		int x1 = bounds.x;
		int x2 = bounds.x + dd;
		int x3 = bounds.x + bounds.width - dd;
		int x4 = bounds.x + bounds.width;
		int y1 = bounds.y;
		int y2 = bounds.y + bounds.height / 2;
		int y3 = bounds.y + bounds.height;
		String text = null;
	  if (schema_name.equals(schema_ext)) { // RR
			text = "<img.area shape=\"poly\" coords=\"" 
			+ x1 + "," + y2 + "," + x2 + "," + y3 + "," + x3 + "," + y3 + "," + x4 + "," + y2 + "," 
			+ x3 + "," + y1 + "," + x2 + "," + y1 + "," + x1 + "," + y2  
			+ "\" href=\"./" + schema_name.toLowerCase() + ".xml" + "#"
			+ schema_name  + "." + getName()
			+ "\" />";
		} else {	

			text = "<img.area shape=\"poly\" coords=\"" 
			+ x1 + "," + y2 + "," + x2 + "," + y3 + "," + x3 + "," + y3 + "," + x4 + "," + y2 + "," 
			+ x3 + "," + y1 + "," + x2 + "," + y1 + "," + x1 + "," + y2  
			+ "\" href=\"../" + schema_name.toLowerCase() + "/sys/" + doc_file + "#"
			+ schema_name + "_" + schema_ext + "." + getName()
			+ "\" />";
		}
		return text;
	}
}

