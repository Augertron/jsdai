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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.LabelListener;

/**
 * @author Mantas Balnys
 *
 */
public class EGPageRefToSimple extends EGPageRef implements LabelListener {

	/**
	 * @param prop
	 * @param referenced
	 */
	public EGPageRefToSimple(PropertySharing prop, EGSimple referenced) {
		super(prop, referenced);
	}
	
	protected EGPageRefToSimple() {
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.AbstractDraw#setProp(jsdai.express_g.exp2.ui.PropertySharing)
	 */
	protected void setProp(PropertySharing prop) {
		super.setProp(prop);
		setVisible(prop.getSimpleTypeVisibilityDefault());
	}

	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.LabelListener#labelChanged(java.lang.Object)
	 */
	public void labelChanged(Object invoker) {
		updateWrapper();
	}

	/**
	 * getUIName
	 *
	 * @return String
	 */
	public String getUIName() {
		return "Simple type";
	}	
	  
	public void print(GC g) {
		boolean sel = isSelected();
		setSelected(false);
		draw(g, true);
		setSelected(sel);
	}
	
	public void draw(GC g) {
		draw(g, false);
	}
		
	/**
	 * draw
	 * 
	 * @param g2 Graphics2D
	 */
	public void draw(GC g, boolean print) {
		Rectangle bounds = getBounds();
  		
		switch (getVisible()) {
			case VISIBLE_TRUE :
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
			    int x = bounds.x + bounds.width - bounds.width / 10;
			    g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
				break;
				
			case VISIBLE_FALSE :
				if (!print) drawInvisibleBox(g);
				break;
			case VISIBLE_UNSET :
				ColorSchema schema3 = isSelected() ? selectedSchema : nonVisibleSchema;
		  	    schema3.apply(g);
			  	g.setFont(prop().getFont1());
	  	  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
	  	  			wrapper.setGC(g);
	  	  			if (isLabelNew()) {
	  	  				setBounds(new Rectangle(bounds.x, bounds.y, 0, 0));
	  	  			} else {
	  	  	  			setBounds(bounds);
	  	  			}
//	  	  			bounds = getBounds();
	  	  		}

	  	  		Rectangle bounds3 = getBounds();
	  	  		if (!print) { 
	  	  			g.fillRectangle(bounds3);
	  	  			g.drawRectangle(bounds3);
				    int x2 = bounds3.x + bounds3.width - bounds3.width / 10;
				    g.drawLine(x2, bounds3.y, x2, bounds3.y + bounds3.height);
	  	  		}

				schema3 = isSelected() ? selectedSchema : simpleSchema;
		  	    schema3.apply(g);
			  	g.setFont(prop().getFont1());
	  	  		
	  	    	Rectangle textPlace2 = getTextPlace();
				int textStartAt2 = textPlace2.y + (textPlace2.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
				for (int i = 0; i < wrapper.getLineCount(); i++) {
					g.drawString(wrapper.getLine(i), 
							textPlace2.x + (textPlace2.width - wrapper.getLineWidth(i)) / 2, 
							textStartAt2 + i * wrapper.getLineHeight(), true);
				}
				break;
		}
		
/*		
  		if (isVisible()) {
  			ColorSchema schema = isSelected() ? selectedSchema : simpleSchema;
  			schema.apply(g);
  			g.setFont(prop.getFont1());
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
		    int x = bounds.x + bounds.width - bounds.width / 10;
		    g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
  		} else {
  			drawInvisibleBox(g);
  		}*/
	}

	public String getText() {
		if (getVisible() == VISIBLE_UNSET) {
			String value = getConcreteValue();
			if (value == null) value = ""; 
			return value;
		} else {
			return getName();
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#setVisible(short)
	 */
	public void setVisible(short visible) {
		super.setVisible(visible);
		updateWrapper();
		if (prop() != null) prop().setSimpleTypeVisibilityDefault(visible);
	}
	
	/**
	 * getTextPlace
	 *
	 * @return Rectangle
	 */
	public Rectangle getTextInsets() {
		Rectangle place = super.getTextInsets();
	    Rectangle bounds = getBounds();
	    place.width += bounds.width / 10;
	    return place;
	}

	public boolean createEditDialog(Composite parent) {
		if (referenced != null) {
			referenced.createEditDialog(parent);
			dialog = referenced.dialog;
		}
	    return isEditDialogCreated();
	}
	  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Named#getName()
	 */
	public String getName() {
		if (referenced != null) return referenced.getName();
		else return "";
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#eliminate()
	 */
	public void eliminate() {
		if (referenced != null) referenced.removeLabelListener(this);
		super.eliminate();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.EGPageRef#setReferenced(jsdai.express_g.exp2.eg.AbstractEGBox)
	 */
	public void setReferenced(AbstractEGBox referenced) {
		if (this.referenced != referenced) {
			if (this.referenced != null) this.referenced.removeLabelListener(this);
			referenced.addLabelListener(this);
			super.setReferenced(referenced);
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#getConcreteValues()
	 */
	public String[] getConcreteValues() {
		String[] values = super.getConcreteValues();
		Iterator iter = getWires().iterator();
		while (values == null && iter.hasNext()) {
			Wire wire = (Wire)iter.next();
			values = wire.getRelation().getConcreteValues();
		}
		return values;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGBox#addWire(jsdai.express_g.exp2.eg.AbstractEGRelation)
	 */
	public void addWire(AbstractEGRelation relation) {
		super.addWire(relation);
		updateWrapper();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGBox#removeWire(jsdai.express_g.exp2.eg.AbstractEGRelation)
	 */
	public boolean removeWire(AbstractEGRelation relation) {
		boolean ok = super.removeWire(relation);
		updateWrapper();
		return ok;
	}
	
	Object referenceKey = null;
	
	public Object getBundleReferenceKey() {
		if (referenceKey == null) referenceKey = new RefKey();
		return referenceKey;
	}
	
	private class RefKey {
		public boolean equals(Object obj) {
			return obj instanceof RefKey && referenced == ((RefKey)obj).getReferenced();
		}
		
		public int hashCode() {
			if (referenced == null)
				return 0;
			else
				return referenced.hashCode();
		}
		
		public AbstractEGBox getReferenced() {
			return referenced;
		}
	}
	
	protected boolean can_be_replaced_by_this(AbstractEGBox box, int rel_type) {
		if (box == this) return false;
		return ((box == referenced) || 
			((box instanceof EGPageRef) && (((EGPageRef)box).getReferenced() == referenced)))
			/*&& (getConcreteValue() == box.getConcreteValue())*/;
	}
}
