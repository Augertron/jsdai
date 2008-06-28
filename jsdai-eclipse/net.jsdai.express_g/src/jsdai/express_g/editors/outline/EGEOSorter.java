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

package jsdai.express_g.editors.outline;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @author Mantas Balnys
 *
 */
public class EGEOSorter extends ViewerSorter {

	/**
	 * 
	 */
	public EGEOSorter() {
		super();
	}

	/**
	 * @param collator
	 */
	public EGEOSorter(Collator collator) {
		super(collator);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public int compare(Viewer viewer, Object e1, Object e2) {

		int cat1 = category(e1);
		int cat2 = category(e2);

		if (cat1 != cat2)
			return cat1 - cat2;

		// cat1 == cat2

		String name1;
		String name2;

		if (e1 instanceof ISorterName) {
			name1 = ((ISorterName)e1).getSortName();
		} else {
			name1 = e1.toString();
		}
		if (e2 instanceof ISorterName) {
			name2 = ((ISorterName)e2).getSortName();
		} else {
			name2 = e2.toString();
		}

		if (name1 == null)
			name1 = "";//$NON-NLS-1$
		if (name2 == null)
			name2 = "";//$NON-NLS-1$
		return collator.compare(name1, name2);
	}
}
