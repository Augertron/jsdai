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
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiEditorOutline extends ContentOutlinePage implements CommandInvoker {
	private SdaiEditor editor;
	
	/**
	 * 
	 */
	public SdaiEditorOutline(SdaiEditor editor) {
		super();
		this.editor = editor;
	}
	
	private static class MyViewerSorter extends ViewerSorter {
		public int category(Object element) {
			if (element instanceof RepositoryHandler) {
				return 1;
			}
			if (element instanceof ModelHandler) {
				if (((ModelHandler)element).getDiagram_definition() != null) {
					int mode = PropertySharing.MODE_LAYOUT_COMPLETE;
					try {
						Object value = ((ModelHandler)element).getProperties().get(PropertySharing.PROP_EDITING_MODE);
						if (value instanceof String) mode = Integer.parseInt((String)value);
					} catch (NumberFormatException e) {
						SdaieditPlugin.log(e);
					}
					if ((mode & PropertySharing.MODE_LONGFORM_MASK) != 0) {
						if ((mode & PropertySharing.MODE_LAYOUT_COMPLETE_MASK) != 0) {
							return 5;
						} else {
							return 6;
						}
					} else {
						if ((mode & PropertySharing.MODE_LAYOUT_COMPLETE_MASK) != 0) {
							return 3;
						} else {
							return 4;
						}
					}
				}
				if (((ModelHandler)element).getSchema_definition() != null) 
					return 2;
			}
			return 7;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new SdaiContentProvider());
		viewer.setInput(this);
		viewer.setLabelProvider(new SdaiLabelProvider());
		viewer.setSorter(new MyViewerSorter());
		
		new SdaiOutlineContextMenu(editor, viewer);
		
		viewer.expandToLevel(2);
//		WorkbenchHelp.setHelp(viewer.getControl(), SdaieditPlugin.ID_SDAIEDIT + ".InternalOutlineContextId");
	}
	
	/**
	 * @return Returns the repositoryHandler.
	 */
	public RepositoryHandler getRepositoryHandler() {
		return editor.getRepositoryHandler();
	}
	
	public void refresh() {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) viewer.refresh();
	}
	
	public void commandDone(Command command) {
	}
}
