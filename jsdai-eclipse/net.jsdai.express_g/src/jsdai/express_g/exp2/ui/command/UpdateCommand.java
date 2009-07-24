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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.swt.graphics.GC;

import jsdai.express_g.exp2.AbstractDraw;
import jsdai.express_g.exp2.EGToolKit;

/**
 * @author Mantas Balnys
 *
 */
public class UpdateCommand extends AbstractCommand {

	/**
	 * @param invoker
	 */
	public UpdateCommand(CommandInvoker invoker) {
		super(invoker);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.command.Command#start()
	 */
	public void start() {
		int max = prop.handler().getMaxPage();
		for (int pgNr = 1; pgNr <= max; pgNr++) {
			Collection items = prop.handler().drawable(pgNr);
		    Collection changed = EGToolKit.PageRef.changePage(prop, items, max + 1);
//System.out.println("<0XO><07>start-pgNr: " + pgNr + ", items: " + items + ", prop: " + prop);
		    changed.addAll(EGToolKit.PageRef.changePage(prop, items, pgNr));
		    Iterator iter = changed.iterator();
		    GC g = prop.getPainting().getLastGraphics();
		    while (iter.hasNext()) {
		    	Object item = iter.next();
		    	if (item instanceof AbstractDraw) {
		    		((AbstractDraw)item).draw(g);
		    		((AbstractDraw)item).update(1);
		    	}
		    }
		}
	    prop.handler().update();
	    prop.handler().repaint(true);
		
		prop.handler().commandDone();
	}
}
