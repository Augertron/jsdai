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

package jsdai.express_g.common;

import jsdai.express_g.widgets.Spinner;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class DialogSelectPage extends Dialog {
	protected int oldPage;
	protected int maxPage;
	protected int newPage;
	protected Spinner spinner;

	public DialogSelectPage(Shell parent) {
		super(parent);
	}
	
	/**
	 * Opens a modal dialog - page selection
	 * @param oldPage
	 * @return new page or old if unchanged or erroneus
	 */
	public int open(int oldPage, int maxPage) {
		this.oldPage = oldPage;
		this.maxPage = maxPage;
		int page = oldPage;
		if (open() == OK) {
			page = newPage;
		}
		return page;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		if (control instanceof Composite) {
			Composite comp = (Composite)control;
			Label label = new Label(comp, SWT.HORIZONTAL | SWT.SHADOW_NONE | SWT.RIGHT);
			label.setText("Select page: ");
			comp.setLayout(new FillLayout());
			spinner = new Spinner(comp, SWT.NONE);
			spinner.setMinimum(1);
			spinner.setMaximum(maxPage);
			spinner.setSelection(oldPage);
		} else {
//			System.err.println("Create page selection dialog failed. Not Composite: " + control);
		}
		return control;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#close()
	 */
	public boolean close() {
		newPage = spinner.getSelection();
		return super.close();
	}
}
