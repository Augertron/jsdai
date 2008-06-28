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

import jsdai.express_g.editors.SdaiEditor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;

/**
 * Extended AbstractRunnable to work exceptionaly on eclipse Viewer parts
 * 
 * @author Mantas Balnys
 *
 */
public abstract class RunnableOnView extends AbstractRunnable {
	protected SdaiEditor editor;
	protected Viewer viewer;
	
	/**
	 * 
	 * @param editor active editor
	 * @param viewer selected view
	 */
	public RunnableOnView(SdaiEditor editor, Viewer viewer) {
		super();
		this.viewer = viewer;
		this.editor = editor;
	}

	/**
	 * @return shell of viewer
	 * @see jsdai.express_g.action.runnable.AbstractRunnable#getShell()
	 */
	public Shell getShell() {
		return viewer.getControl().getShell();
	}

	/**
	 * provides selection in the viewer
	 * @return
	 */
	public ISelection getSelection() {
		return viewer.getSelection();
	}
	
	/**
	 * refresh viewer after changes
	 *
	 */
	public void refresh() {
		viewer.refresh();
		editor.updateModifiedStatus();
	}
	
	/**
	 * reverse action to set selection on viewer
	 * @param item
	 */
	public void selectInView(Object item) {
		viewer.setSelection(new StructuredSelection(item), true);
	}
}
