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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public class DialogSelectPageList extends Dialog {
	private Table table = null;
	private String[] pages = null;
//	private int lastSelection = 1; // first line of table is selected by default
	protected int newPage;
	protected int oldPage;

	public DialogSelectPageList(Shell parent) {
		super(parent);
	}
	
	/**
	 * Opens a modal dialog - page selection
	 * @param oldPage
	 * @return new page or old if unchanged or erroneus
	 */
	public int open(int oldPage, String[] pages) {
		if (pages == null)
			this.pages = null;
		else
			this.pages = (String[])pages.clone();
		this.oldPage = oldPage;
		int page = oldPage;
		if (table != null && !table.isDisposed()) {
			for (int i = 0; i < this.pages.length; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, String.valueOf(i + 1));
				item.setText(1, this.pages[i]);
			}
			table.setSelection(page - 1);
			this.pages = null;
		}
		if (open() == OK) {
			page = newPage;
		} else {
		}
		this.pages = null;
		return page;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		if (control instanceof Composite) {
			Composite comp = (Composite)control;
			comp.setLayout(new FillLayout());
			table = new Table(comp, SWT.SINGLE | SWT.FULL_SELECTION);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			
			TableColumn index = new TableColumn(table, SWT.RIGHT);
			index.setText("No.");
			index.pack();
			index.setResizable(false);
			TableColumn name = new TableColumn(table, SWT.LEFT);
			name.setText("Page name");
			name.setWidth(index.getWidth() * 12);
			name.setResizable(false);
			
			if (pages != null) {
				for (int i = 0; i < pages.length; i++) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, String.valueOf(i + 1));
					item.setText(1, pages[i]);
				}
				table.setSelection(oldPage - 1);
			}
			
			table.addMouseListener(new MouseAdapter() {
				public void mouseDoubleClick(MouseEvent e) {
					DialogSelectPageList.this.okPressed();
				}
			});
			table.addKeyListener(new KeyAdapter() {
				
				/* (non-Javadoc)
				 * @see org.eclipse.swt.events.KeyAdapter#keyPressed(org.eclipse.swt.events.KeyEvent)
				 */
				public void keyPressed(KeyEvent e) {
					if (e.character == SWT.ESC) { // cancel
						DialogSelectPageList.this.cancelPressed();
					}
				}
			});
		} else {
//			System.err.println("Create page selection dialog failed. Not Composite: " + control);
		}
		getShell().setText("Select page");
		return control;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#close()
	 */
	public boolean close() {
		newPage = table.getSelectionIndex() + 1;
		return super.close();
	}
}
