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

package jsdai.express_g.editors;

import jsdai.express_g.action.RunnableAction;
import jsdai.express_g.action.runnable.PageDelete;
import jsdai.express_g.action.runnable.PageInsert;
import jsdai.express_g.action.runnable.PageMove;
import jsdai.express_g.common.Resources;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Mantas Balnys
 *
 */
public class PageHandlingContextMenu extends MenuManager {
	private PropertySharing prop;

	private RunnableAction pageInsert;
	private RunnableAction pageDelete;
	private RunnableAction pageMove;
	
	/**
	 * 
	 */
	public PageHandlingContextMenu(Control control, PropertySharing prop) {
		super();
		this.prop = prop;
		init();
		Menu menu = createContextMenu(control);
		control.setMenu(menu);
	}

	/**
	 * @param text
	 *
	public PageHandlingContextMenu(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param id
	 *
	public PageHandlingContextMenu(String text, String id) {
		super(text, id);
	}
	
	/**
	 * 
	 *
	 */
	private void init() {
		pageInsert = new RunnableAction("Insert", 
				Resources.getImageDescriptor(Resources.ADD), 
				new PageInsert(prop));
		this.add(pageInsert);
		pageDelete = new RunnableAction("Delete", 
				Resources.getImageDescriptor(Resources.DELETE), 
				new PageDelete(prop));
		this.add(pageDelete);
		pageMove = new RunnableAction("Move", 
				Resources.getImageDescriptor(Resources.MOVE), 
				new PageMove(prop));
		this.add(pageMove);
	}


}
