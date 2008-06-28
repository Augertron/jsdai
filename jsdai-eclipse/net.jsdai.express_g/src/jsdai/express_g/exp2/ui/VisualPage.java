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

package jsdai.express_g.exp2.ui;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import jsdai.SExpress_g_schema.EData_type_placement;
import jsdai.SExpress_g_schema.ELocation;
import jsdai.SExpress_g_schema.EPage;
import jsdai.SExpress_g_schema.EProperty;
import jsdai.SExpress_g_schema.ESize;
import jsdai.express_g.common.StaticTools;
import jsdai.express_g.exp2.ColorSchema;
import jsdai.express_g.exp2.Drawable;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.properties.DialogPage;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;

/**
 * <p>
 * Title: JSDAI Express-G
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Mantas Balnys
 * @version 1.0
 */

public class VisualPage extends AbstractEGBox implements PageListener {
	private String printMask = null;
	private Rectangle margins = new Rectangle(20, 20, 40, 70);
	public static final String DEFAULT_PAGE_MASK = PropertySharing.PAGE_MASK_NAME + 
		" (" + PropertySharing.PAGE_MASK_PAGE +	" of " + PropertySharing.PAGE_MASK_MAX_PAGE + ")"; 

	public ColorSchema invisibleSchema = new ColorSchema(ColorSchema.COLOR_GRAY, ColorSchema.COLOR_DARK_WHITE);
	public ColorSchema invisibleSelectedSchema = new ColorSchema(selectedSchema.foreground, invisibleSchema.background);
	
	protected VisualPage(PropertySharing prop) {
		super(prop);
		setLabelNew(false);
		selectedSchema.background = ColorSchema.COLOR_WHITE;
		validDict = true;
		super.setLocation(new Point(SizingPoint.pointSize, SizingPoint.pointSize));
	}

	public VisualPage(PropertySharing prop, int pgNr) {
		this(prop);
		setPage(pgNr);
		VisualPage vp = prop.handler().getVisualPage(pgNr - 1);
		if (vp == null) {
			internalPageSetSize(new Point(745, 1061));
		} else {
			internalPageSetSize(vp.getSize());
			internalPageSetVisible(vp.getVisible());
		}
		validDict = true;
		prop.handler().addPageListener(this);
	}
	
	public void setPrintMask(String mask) {
	  	if (!StaticTools.equalStrings(printMask, mask)) {
	  		printMask = mask;
	  		if (prop() != null) prop().setModified(true);
			wrapper.setText(getText());
	  		fireLabelChanged();
	  	}
	}
	
	public String getPrintMask() {
		return printMask;
	}

  	public void draw(GC g) {
  		Rectangle bounds = getBounds();
  		
		ColorSchema schema = isSelected() ? isVisible() ? selectedSchema : invisibleSelectedSchema : isVisible() ? simpleSchema : invisibleSchema;
		schema.apply(g);
		g.setFont(prop().getFont1());
  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
  			wrapper.setGC(g);
  		}

  		g.fillRectangle(bounds);
  		g.drawRectangle(bounds);
		Rectangle margins = getRealMargins();
		if ((bounds.width - margins.width > 0) && (bounds.height - margins.height > 0)) {
			g.setLineStyle(SWT.LINE_DASH);
			g.setForeground(ColorSchema.COLOR[ColorSchema.COLOR_DARK_WHITE]);
			g.drawRectangle(new Rectangle(margins.x, margins.y, bounds.width - margins.width, bounds.height - margins.height));
			schema.apply(g);
		}
		    
  		Rectangle textPlace = getTextPlace();
  		int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
  		for (int i = 0; i < wrapper.getLineCount(); i++) {
  			g.drawString(wrapper.getLine(i), 
  					textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
					textStartAt	+ i * wrapper.getLineHeight(), true);
  		}
  	}

  	/**
	 * updateModel
	 * 
	 * @param modelDict SdaiModel
	 * @param modelEG SdaiModel
	 * @throws SdaiException
	 * @todo Implement this jsdai.paint.eg.SDAIdicSchema method
	 */
	public void updateModel(SdaiModel modelDict, SdaiModel modelEG)
			throws SdaiException {
		EPage page = (EPage)modelEG.createEntityInstance(EPage.class);
		page.setPage_number(null, getPage());
		//    page.setDiagram(null, modelEG.getUnderlyingSchema());
		setDefinition(page);
		
		if (printMask != null) {
			EProperty property = (EProperty)modelEG.createEntityInstance(EProperty.class);
			property.setName(null, PropertySharing.PROP_SPECIFIC_PAGE_MASK + getPage());
			property.setData(null, printMask);
		}
		
	    if (isOnPage(INVISIBLE_PAGE)) {
	        setDefinitionPlacement(null);
	    } else {
			page.setComment(null, getName());
			Point csize = getSize();  
	        if (csize.x != 0 || csize.y != 0) {
				ESize esize = (ESize)modelEG.createEntityInstance(ESize.class);
				esize.setWidth(null, csize.x);
				esize.setHeight(null, csize.y);
				page.setPage_size(null, esize);
			}

			EData_type_placement entity_placement = (EData_type_placement)modelEG.createEntityInstance(EData_type_placement.class);
			Rectangle margins = getMargins();
	        if (margins.x != 0 || margins.y != 0) {
	          ELocation loc = (ELocation)modelEG.createEntityInstance(ELocation.class);
	          loc.setX(null, margins.x);
	          loc.setY(null, margins.y);
	          entity_placement.setObject_location(null, loc);
	        }

	        if (margins.width != 0 || margins.height != 0) {
	          ESize size = (ESize)modelEG.createEntityInstance(ESize.class);
	          size.setWidth(null, margins.width);
	          size.setHeight(null, margins.height);
	          entity_placement.setObject_size(null, size);
	        }

	        if (getVisible() != VISIBLE_UNSET)
	      	  entity_placement.setVisible(null, isVisible());

	        entity_placement.setRepresented_object(null, getDefinition());

	        /** TODO Colors disabled
	        EColor color = (EColor)modelEG.createEntityInstance(EColor.class);
	        color.setRed(null, simpleSchema.background.getRed());
	        color.setGreen(null, simpleSchema.background.getGreen());
	        color.setBlue(null, simpleSchema.background.getBlue());
	        entity_placement.setRepresentation_color(null, color);
	        */

	        setDefinitionPlacement(entity_placement);
	    }
	}

	public void update(int n) {

	}

	/**
	 * set size to rounding rect around all EG objects
	 * 
	 * @return
	 */
	public Rectangle getMinRoundingBox() {
		Rectangle draw = null;
		Iterator iter = prop().handler().drawableIterator(getPage());
		while (iter.hasNext()) {
			Object object = iter.next();
			if (object instanceof VisualPage) {
				// skip page frame object
			} else
			if (object instanceof AbstractEGObject) {
				if (draw == null)
					draw = ((Drawable)object).getBounds();
				else
					draw.add(((Drawable)object).getBounds());
			}
		}
		//	    if (draw == null) setBounds(new Rectangle(0, 0, 0, 0));
		//	    else setBounds(draw);
		return draw;
	}

	public boolean createEditDialog(Composite parent) {
		dialog = new DialogPage(parent, this, prop());
		return isEditDialogCreated();
	}
/*	
	public void setLocation(Point location) {
		// disable moving of page object
	}
*/
	public boolean locked() {
	  	return true;
	}
	  
	/**
	 * getTextPlace
	 *
	 * @return Rectangle
	 */
	public Rectangle getTextInsets() {
	    Rectangle place = super.getTextInsets();
	    Rectangle bounds = getBounds();
	    Rectangle margins = getMargins();
	    place.y = bounds.height - margins.height + margins.y 
	    	- wrapper.getLineHeight() * wrapper.getLineCount();
	    place.height = bounds.height - wrapper.getLineHeight() * wrapper.getLineCount();
	    place.x += margins.x;
	    place.width += margins.width;
	    return place;
	}
	
	public String getText() {
		String text = getPrintMask();
		if (text == null) {
			text = prop().properties().getProperty(PropertySharing.PROP_DEFAULT_PAGE_MASK, DEFAULT_PAGE_MASK);
		}
		text = text.replaceAll(PropertySharing.PAGE_MASK_NAME, getName());
		text = text.replaceAll(PropertySharing.PAGE_MASK_PAGE, String.valueOf(EGToolKit.renumber(prop(), getPage())));
		text = text.replaceAll(PropertySharing.PAGE_MASK_MAX_PAGE, String.valueOf(EGToolKit.renumberMaxPage(prop(), getPage())));
		return text;
	}
	
	public void pageChanged(PageChangeEvent e) {
		wrapper.setText(getText());
		fireLabelChanged();
	}
	
	private void internalPageSetSize(Point size) {
		superSetSize(size);
		wrapper.setWidth(size.x - getTextInsets().width);
	}
	
	public void setSize(Point size) {
		if (prop().isPagesSameSize()) {
			for (int i = 1; i <= prop().handler().getMaxPage(); i++)
				prop().handler().getVisualPage(i).internalPageSetSize(size);
		} else {
			internalPageSetSize(size);
		}
	}

	public Point getSize() {
		return superGetSize();
	}
	
	public void setPage(int pgNr) {
		super.setPage(pgNr);
		wrapper.setText(getText());
		fireLabelChanged();
	}
	
	public void eliminate() {
		prop().handler().removePageListener(this);
		super.eliminate();
	}

	public boolean objectAt(Rectangle r) {
		return false;
	}
	
	public String getXMLDefinition(Point startat, String schema_name,
			String schema_ext, String doc_file) {
		return null;
	}
	
	public boolean canBeInvisible() {
		return true;
	}
	
	public String toString() {
		String text = super.toString();
		if (isEliminated()) text = "[eliminated]" + text;
		return text;
	}
	
	private void internalPageSetVisible(short visible) {
		super.setVisible(visible);
	}

	public void setVisible(short visible) {
		if (prop().isPagesSameSize()) {
			for (int i = 1; i <= prop().handler().getMaxPage(); i++)
				prop().handler().getVisualPage(i).internalPageSetVisible(visible);
		} else {
			internalPageSetVisible(visible);
		}
	}
	
	private void internalPageSetMargins(Rectangle margins) {
		this.margins = margins;
	}
	
	public void setMargins(Rectangle margins0) {
		if (prop().isPagesSameSize()) {
			for (int i = 1; i <= prop().handler().getMaxPage(); i++)
				prop().handler().getVisualPage(i).internalPageSetMargins(margins);
		} else {
			internalPageSetMargins(margins);
		}
	}
	
	public Rectangle getMargins() {
		return margins;
	}
	
	public Rectangle getRealMargins() {
		Point loc = getLocation();
		return new Rectangle(margins.x + loc.x, margins.y + loc.y, margins.width, margins.height);
	}
	
	public void print(GC g) {
		if (isVisible()) {
			simpleSchema.apply(g);
			g.setFont(prop().getFont1());
	  		if ((!wrapper.isGCValid())||(!g.equals(wrapper.getGC()))) {
  				wrapper.setGC(g);
  			}

	  		Rectangle textPlace = getTextPlace();
  			int textStartAt = textPlace.y + (textPlace.height - wrapper.getLineHeight() * wrapper.getLineCount()) / 2;
  			for (int i = 0; i < wrapper.getLineCount(); i++) {
  				g.drawString(wrapper.getLine(i), 
  						textPlace.x + (textPlace.width - wrapper.getLineWidth(i)) / 2, 
						textStartAt	+ i * wrapper.getLineHeight(), true);
  			}
  		}
		
	}
	
	public void setLabelNew(boolean isNew) {
		super.setLabelNew(false);
	}
	
	public void setLocation(Point location) {
	}
}