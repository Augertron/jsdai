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

//spinner import jsdai.express_g.action.EGESpinnerContribution;

import jsdai.express_g.action.InternalRunnableAction;
import jsdai.express_g.action.runnable.Delete;
import jsdai.express_g.action.runnable.Print;
import jsdai.express_g.action.runnable.SelectAll;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.command.Command;
import jsdai.express_g.exp2.ui.command.CommandInvoker;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;


/**
 * @author Mantas Balnys
 *
 */
public class SdaiEditorContributor extends InternalEditorActionBarContributor 
		implements CommandInvoker {
//spinner	private EGESpinnerContribution spinner;
	private InternalRunnableAction print;
	private InternalRunnableAction selectAll;
	private InternalRunnableAction delete;

	/**
	 * 
	 */
	public SdaiEditorContributor() {
		super();
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartListener#internalPartChanged(org.eclipse.ui.IWorkbenchPart)
	 */
	public void internalPartChanged(IWorkbenchPart part) {
		ExpressGEditor editor = null;
		if (part instanceof ExpressGEditor) {
			editor = (ExpressGEditor)part;
//spinner			spinner.setProperties(((ExpressGEditor)part).getProperties());
//spinner			spinner.setEnabled(true);
		} else {
//spinner			spinner.setProperties(null);
//spinner			spinner.setEnabled(false);
		}
		PropertySharing prop = null;
		if (editor != null) prop = editor.getProperties(); 
		print.setProp(prop);
		selectAll.setProp(prop);
		delete.setProp(prop);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.IActionBars)
	 */
	public void init(IActionBars bars) {
		createActions();
		createGlobalActions(bars);
		super.init(bars);
	}
	
	private void createActions() {
//spinner		spinner = new EGESpinnerContribution("SdaiEditor.spinner");
		print = new InternalRunnableAction(new Print(this));
		selectAll = new InternalRunnableAction(new SelectAll(this));
		delete = new InternalRunnableAction(new Delete(this));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager toolBarManager) {
//spinner		toolBarManager.add(spinner);
	}
	
	public void commandDone(Command command) {
	}
	
	private void createGlobalActions(IActionBars actionBar) {
		actionBar.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
		actionBar.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
		actionBar.setGlobalActionHandler(ActionFactory.PRINT.getId(), print);
	}
}
