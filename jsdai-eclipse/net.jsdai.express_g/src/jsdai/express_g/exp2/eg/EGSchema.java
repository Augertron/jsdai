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
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author Mantas Balnys
 *
 */
public class EGSchema extends AbstractEGBox {

	/**
	 * 
	 */
	public EGSchema(PropertySharing prop, String schema_name) {
		super(prop);
		setName(schema_name);
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
	    	int y = bounds.y + bounds.height / 2;  
	    	g.drawLine(bounds.x, y, bounds.x + bounds.width, y);
	    } else {
	    	drawInvisibleBox(g);
	    }
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
	    place.height += bounds.height / 2;
	    return place;
	}
	
	/**
	 * getUIName
	 *
	 * @return String
	 */
	public String getUIName() {
		return "Schema";
	}
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.IXMLDefinition#getXMLDefinition(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getXMLDefinition(Point startat, String schema_name, String schema_ext, String doc_file) {
		String[] names = EGToolKit.parseSchemaName(getName());
		String name = names[0];
		String ext = names[1];

		Rectangle bounds = getBounds();
		bounds.x -= startat.x;
		bounds.y -= startat.y;
		String text = "<img.area shape=\"rect\" coords=\"" 
			+ bounds.x + "," + bounds.y + "," + (bounds.x + bounds.width) + "," + (bounds.y + bounds.height) 
			+ "\"";
		String href = "";
		if ("".equals(ext)) {
			href = " href=\"../../resources/" + name.toLowerCase() + "/" + name.toLowerCase() + ".xml\""; 
		} else if (name.equals(ext)) { // RR - for extended names in resources - could have changed the above if perhaps
// first version - references to resources are old style - not to expg
			href = " href=\"../../resources/" + name.toLowerCase() + "/" + name.toLowerCase() + ".xml\""; 

// 2nd version - trying references to resources to expg instead
//			href = " href=\"../../resources/" + name.toLowerCase() + "/" + name.toLowerCase(); 
//			if (schema_name.equalsIgnoreCase(name)) {
//				if (prop().handler().getMaxPage() > 1) href += "expg2.xml" + "\"";
//				else
//				  href += ".xml\""; // let's keep the old style here for now
//					// return null; // no xml for schema with single page
//			} else {
//				href += "expg1.xml" + "\"";
//			}

		} else {
			href = " href=\"../" + name.toLowerCase() + "/"	+ ext.toLowerCase(); 
			if (schema_name.equalsIgnoreCase(name)) {
				if (prop().handler().getMaxPage() > 1) href += "expg2.xml" + "\"";
				else
					return null; // no xml for schema with single page
			} else {
				href += "expg1.xml" + "\"";
			}
		}
		text += href + " />";
		return text;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.SDAIdicSchema#clearDefinitions()
	 */
	public void clearDefinitions() {
	    eplacement = null;
	    validDict = false;
	}
}
