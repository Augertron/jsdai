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

package jsdai.express_g.exp2.ui.panels;

import java.util.Collection;

import jsdai.express_g.exp2.ui.event.DrawListener;
import jsdai.express_g.exp2.ui.event.PageListener;
import jsdai.express_g.exp2.ui.event.SelectionListener;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * @author Mantas Balnys
 *
 */
public interface IPaintPanel extends DrawListener, PageListener, SelectionListener {

	public GC getLastGraphics();
	
	public void dispose();
	
	/**
	 * redraw contents without refreshing data
	 *
	 */
	public void redraw();

	/**
	 * @return Returns the topItems.
	 */
	public Collection topItems();
	
	/**
	 * @return maximum drawable panel size
	 */
	public Rectangle getImageBounds();
	
}
