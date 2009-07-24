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

import jsdai.express_g.action.RunnableAction;
import jsdai.express_g.action.runnable.DeleteDiagram;
import jsdai.express_g.action.runnable.DeleteSchema;
import jsdai.express_g.action.runnable.GeneratingXml;
import jsdai.express_g.action.runnable.NewDiagram;
import jsdai.express_g.action.runnable.NewSchema;
import jsdai.express_g.common.Resources;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiOutlineContextMenu extends MenuManager implements IMenuListener {
	private Viewer viewer;
	private SdaiEditor editor;
	
	private MenuManager newDiagram;
	private RunnableAction newDiagramCompleteShort;
	private RunnableAction newDiagramPartialShort;
	private RunnableAction newDiagramCompleteLong;
	private RunnableAction newDiagramPartialLong;
	private RunnableAction deleteDiagram;
	private RunnableAction newSchema;
	private RunnableAction deleteSchema;
	private RunnableAction generateXml;
	
	/**
	 * 
	 */
	public SdaiOutlineContextMenu(SdaiEditor editor, Viewer viewer) {
		super();
		this.viewer = viewer;
		this.editor = editor;
		init();
		Menu menu = createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		addMenuListener(this);
	}

	/**
	 * @param text
	 *
	public SdaiOutlineContextMenu(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param id
	 *
	public SdaiOutlineContextMenu(String text, String id) {
		super(text, id);
	}
	
	/**
	 * 
	 *
	 */
	private void init() {
//		generateXml = new RunnableAction("Generate XML", 
//				Resources.getImageDescriptor(Resources.GENERATE_XML), 
//				new GeneratingXml(editor, viewer, PropertySharing.MODE_XML));
		//generateXml = new RunnableAction("Generate XML", 
		//		new GeneratingXml(editor, viewer, PropertySharing.MODE_XML));
		newDiagram = new MenuManager("New Diagram");
		newDiagramCompleteShort = new RunnableAction("Complete Short", 
				Resources.getImageDescriptor(Resources.MODEL_LAYOUT_COMPLETE_SHORT), 
				new NewDiagram(editor, viewer, PropertySharing.MODE_LAYOUT_COMPLETE));
		newDiagramPartialShort = new RunnableAction("Partial Short", 
				Resources.getImageDescriptor(Resources.MODEL_LAYOUT_PARTIAL_SHORT), 
				new NewDiagram(editor, viewer, PropertySharing.MODE_LAYOUT_PARTIAL));
		newDiagramCompleteLong = new RunnableAction("Complete Long", 
				Resources.getImageDescriptor(Resources.MODEL_LAYOUT_COMPLETE_LONG), 
				new NewDiagram(editor, viewer, PropertySharing.MODE_LAYOUT_COMPLETE_LONG));
		newDiagramPartialLong = new RunnableAction("Partial Long", 
				Resources.getImageDescriptor(Resources.MODEL_LAYOUT_PARTIAL_LONG), 
				new NewDiagram(editor, viewer, PropertySharing.MODE_LAYOUT_PARTIAL_LONG));
		
		deleteDiagram = new RunnableAction("Delete Diagram", 
				Resources.getImageDescriptor(Resources.DELETE), 
				new DeleteDiagram(editor, viewer));

		newSchema = new RunnableAction("New Schema", 
				Resources.getImageDescriptor(Resources.MODEL_SCHEMA), 
				new NewSchema(editor, viewer));
		deleteSchema = new RunnableAction("Delete Schema", 
				Resources.getImageDescriptor(Resources.DELETE), 
				new DeleteSchema(editor, viewer));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		newDiagramCompleteShort.updateVisibility();
		newDiagramPartialShort.updateVisibility();
		newDiagramCompleteLong.updateVisibility();
		newDiagramPartialLong.updateVisibility();
		newDiagram.removeAll();
		if (newDiagramCompleteShort.isEnabled()) newDiagram.add(newDiagramCompleteShort);
		if (newDiagramPartialShort.isEnabled()) newDiagram.add(newDiagramPartialShort);
		if (newDiagramCompleteLong.isEnabled()) newDiagram.add(newDiagramCompleteLong);
		if (newDiagramPartialLong.isEnabled()) newDiagram.add(newDiagramPartialLong);
		//generateXml.updateVisibility();
		deleteDiagram.updateVisibility();
		newSchema.updateVisibility();
		deleteSchema.updateVisibility();
		this.removeAll();
		if (!newDiagram.isEmpty()) this.add(newDiagram);
		if (deleteDiagram.isEnabled()) this.add(deleteDiagram);
		if (newSchema.isEnabled()) this.add(newSchema);
		if (deleteSchema.isEnabled()) this.add(deleteSchema);
		//if (generateXml.isEnabled()) this.add(generateXml);
	}
}
