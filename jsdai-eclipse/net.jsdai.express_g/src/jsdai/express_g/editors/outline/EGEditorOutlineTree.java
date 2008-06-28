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

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.common.TableTreeLabelProvider;
import jsdai.express_g.editors.IExpressGEditor;
import jsdai.express_g.exp2.ui.ContextMenu;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.navigator.LocalSelectionTransfer;

/**
 * @author Mantas Balnys
 *
 */
public class EGEditorOutlineTree implements IContentOutlinePage, DragSourceListener {
	private TreeViewer tree = null;
	private EGEOContentProviderMS content = null;
	
	protected PropertySharing prop;
	private IExpressGEditor editor;

	/**
	 * 
	 */
	public EGEditorOutlineTree(IExpressGEditor editor) {
		this.editor = editor;
		this.prop = editor.getProperties();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		tree = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		tree.setLabelProvider(new TableTreeLabelProvider());
		tree.setSorter(new EGEOSorter());
		content = new EGEOContentProviderMS(prop, tree);
		
		addSelectionChangedListener(content);
		
		DragSource drag = new DragSource(tree.getTree(), DND.DROP_MOVE);
		drag.setTransfer(new Transfer[]{LocalSelectionTransfer.getInstance()});
		drag.addDragListener(this);

		createActions();
		
		tree.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editor.getProperties().getSelectionHandler().mouseDoubleClick(null);
			}
		});

	    fillinMenu(tree.getTree());

	    WorkbenchHelp.setHelp(tree.getControl(), SdaieditPlugin.ID_SDAIEDIT + ".ExpressG_OutlineContextId");
	}
	
	private void fillinMenu(Control control) {
	    ContextMenu cMenu = new ContextMenu(prop);
	    Menu menu = cMenu.createContextMenu(control);
	    control.setMenu(menu);
	}
	
	private void createActions() {
		// TODO
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		content.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return tree.getControl();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setActionBars(org.eclipse.ui.IActionBars)
	 */
	public void setActionBars(IActionBars actionBars) {
// TODO Editing mode		actionBars.getToolBarManager().add(newSchema);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() {
		tree.getTree().setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		tree.addSelectionChangedListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return content.getSelection();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		tree.removeSelectionChangedListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		content.setSelection(selection);
	}

	public void dragFinished(DragSourceEvent event) {
		LocalSelectionTransfer.getInstance().setSelection(null);
	}
	
	public void dragSetData(DragSourceEvent event) {
		event.data = getSelection();
	}
	
	public void dragStart(DragSourceEvent event) {
		LocalSelectionTransfer.getInstance().setSelection(getSelection());
	}
}
