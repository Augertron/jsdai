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

import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author Mantas Balnys
 *
 */
public class EGText extends AbstractEGBox {

	/**
	 * @param prop
	 */
	public EGText(PropertySharing prop) {
		super(prop);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.eg.AbstractEGObject#eliminate()
	 */
	public void eliminate() {
	    super.eliminate();
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Updateable#update(int)
	 */
	public void update(int nr) {
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Drawable#draw(org.eclipse.swt.graphics.GC)
	 */
	public void draw(GC g) {
		super.draw(g);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Selection#objectAt(org.eclipse.swt.graphics.Point)
	 */
	public boolean objectAt(Point p) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.Selection#objectAt(org.eclipse.swt.graphics.Rectangle)
	 */
	public boolean objectAt(Rectangle r) {
		return false;
	}
}
