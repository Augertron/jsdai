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

package jsdai.express_g.exp2.ui.command;

import java.util.Comparator;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.eg.AbstractEGBox;
import jsdai.express_g.exp2.eg.AbstractEGObject;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.ui.SizingPoint;
import jsdai.express_g.exp2.ui.VisualPage;
import jsdai.express_g.exp2.ui.panels.IPaintPanel;

/**
 * @author Mantas Balnys
 *
 */
public class DrawComparator implements Comparator {

	/**
	 * 
	 */
	public DrawComparator() {
	}

	private String getCompareString(Object o) {
		String c;
		if (o instanceof VisualPage) c = "d9"; else
		if (o instanceof SizingPoint) c = "d1"; else
		if (o instanceof AbstractEGBox) c = "d51"; else
		if (o instanceof AbstractEGRelation) c = "d52"; else
		if (o instanceof AbstractEGObject) c = "d6"; else
		if (o instanceof AbstractDraw) c = "d7"; else
		if (o instanceof IPaintPanel) c = "z5"; else
			c = "o1";
		c += o.getClass().getName();
		return c;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		if (o1 == o2) return 0;
		String c1 = getCompareString(o1);
		String c2 = getCompareString(o2);
		int cmp = c1.compareTo(c2); // cmp must not change in time (must not depend on internal properties of Object)
		if (cmp == 0) {
			cmp = o1.hashCode() < o2.hashCode() ? -1 : 1;
		}
		return cmp;
	}

}
