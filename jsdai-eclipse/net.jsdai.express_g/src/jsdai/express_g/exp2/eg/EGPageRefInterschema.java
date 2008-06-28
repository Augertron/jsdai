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
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.LabelListener;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * @author Mantas Balnys
 *
 */
public class EGPageRefInterschema extends EGPageRef implements LabelListener {
	protected TextWrapper wrapper2 = null;
	protected EGEntityRef referenced_ref;

	/**
	 * @param prop
	 * @param referenced
	 */
	public EGPageRefInterschema(PropertySharing prop, EGEntityRef referenced) {
		super(prop, referenced);
	    wrapper2 = new TextWrapper("", "");
	    wrapper2.setStyle(wrapper.getStyle());
		updateWrapper();
	}
	
	protected EGPageRefInterschema() {
	    wrapper2 = new TextWrapper("", "");
	    wrapper2.setStyle(wrapper.getStyle());
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
			if (referenced_ref.getType() == EGEntityRef.TYPE_REFERENCED) g.setLineStyle(SWT.LINE_DASH);
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
	}

	public String getText() {
		if (referenced_ref == null) return "";
	    String text = referenced_ref.getSchemaName() + "." + referenced_ref.getName();
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

	/**
	 * getTextPlace
	 *
	 * @return Rectangle
	 */
	public Rectangle getTextInsets2() {
	    return new Rectangle(textInset, textInset, 2*textInset, 2*textInset);
	}

	public boolean createEditDialog(Composite parent) {
		if (referenced_ref != null) {
			referenced_ref.createEditDialog(parent);
			dialog = referenced_ref.dialog;
		}
	    return isEditDialogCreated();
	}
	  	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		String[] names = EGToolKit.parseSchemaName(referenced_ref.getSchemaName());
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
//		  			System.err.println("Failed to set item size properly");
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
	public void updateWrapper() {
		super.updateWrapper();
		if (wrapper2 != null && referenced_ref != null) wrapper2.setText(referenced_ref.getRename());
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Named#getName()
	 */
	public String getName() {
		if (referenced_ref != null) return referenced_ref.getName();
		else return "";
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#eliminate()
	 */
	public void eliminate() {
		if (referenced_ref != null) referenced_ref.removeLabelListener(this);
		super.eliminate();
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.EGPageRef#setReferenced(jsdai.express_g.exp2.eg.AbstractEGBox)
	 */
	public void setReferenced(AbstractEGBox referenced) {
		if (referenced_ref != referenced && referenced instanceof EGEntityRef) {
			if (referenced_ref != null) referenced_ref.removeLabelListener(this);
			referenced_ref = (EGEntityRef)referenced;
			referenced_ref.addLabelListener(this);
			super.setReferenced(referenced);
		}
	}
	
	protected EEntity getReferencedDefinition(SdaiModel modelDict, SdaiModel modelEG) throws SdaiException {
		return referenced_ref.getThisDefinition(modelDict, modelEG);
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
