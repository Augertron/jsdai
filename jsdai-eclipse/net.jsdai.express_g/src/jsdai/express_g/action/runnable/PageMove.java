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

package jsdai.express_g.action.runnable;

import java.lang.reflect.InvocationTargetException;

import jsdai.express_g.common.DialogSelectPageList;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Mantas Balnys
 *
 */
public class PageMove extends AbstractRunnable {
	private DialogSelectPageList dialogSelectPage = null;
	private PropertySharing prop;

	/**
	 * 
	 */
	public PageMove(PropertySharing prop) {
		super();
		this.prop = prop;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,	InterruptedException {
		if (dialogSelectPage == null) dialogSelectPage = new DialogSelectPageList(null);
		int oldPage = prop.handler().getPage();
		int page = dialogSelectPage.open(oldPage, EGToolKit.resolvePageNames(prop, true));
		if (!EGToolKit.movePage(prop, oldPage, page)) {
			MessageDialog.openError(null, "Error", "Page moving failed");
		}
	}

}
