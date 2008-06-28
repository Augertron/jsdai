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

import jsdai.SExtended_dictionary_schema.ESchema_definition;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.lang.SdaiException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * Deletes all diagram
 * 
 * @author Mantas Balnys
 *
 */
public class DeleteDiagram extends RunnableOnView {

	/**
	 * @param editor
	 * @param viewer
	 */
	public DeleteDiagram(SdaiEditor editor, Viewer viewer) {
		super(editor, viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,	InterruptedException {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if (obj instanceof ModelHandler) {
				if (MessageDialog.openQuestion(getShell(), "Confirm delete diagram", "Are you sure you want to delete this diagram?")) {
					ModelHandler mhEG = (ModelHandler)obj;
					RepositoryHandler rh = mhEG.getRepositoryHandler();
					String schemaName = null;
					try {
						ESchema_definition schemaDef = mhEG.getSchema_definition();
						if (schemaDef != null)
							schemaName = schemaDef.getName(null);
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
					RepositoryChanger changerR = null;
					RepositoryChanger changerW = new RepositoryChanger(new String[]{mhEG.getName()}, "Deleting diagram");
					String msg = null;
					if (schemaName != null) {
						changerR = new RepositoryChanger(new String[]{schemaName}, "Deleting diagram");
						msg = rh.startROChanger(changerR);
					}
					if (msg != null) {
						MessageDialog.openWarning(getShell(), "Delete Diagram Action", msg);
					} else {
						msg = rh.startRWChanger(changerW);
						if (msg != null) {
							MessageDialog.openWarning(getShell(), "Delete Diagram Action", msg);
						} else {
							rh.deleteModel(mhEG.getName());
							try {
								rh.update();
							} catch (SdaiException sex) {
								SdaieditPlugin.log(sex);
								SdaieditPlugin.console(sex.toString());
							}
							refresh();
							if (schemaName == null)
								selectInView(rh);
							else
								selectInView(rh.getModelHandler(schemaName));
						}
						changerW.done();
						rh.endRWChanger(changerW);
					}
					if (changerR != null) {
						changerR.done();
						rh.endROChanger(changerR);
					}
				}
			} else {
//				System.err.println("wrong item for DeleteDiagram action: " + obj);
			}
		} else {
//			System.err.println("wrong selection for DeleteDiagram action: " + selection);
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.action.runnable.AbstractRunnable#acceptsCurrentSelection()
	 */
	public boolean acceptsCurrentSelection() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if (obj instanceof ModelHandler) {
				ModelHandler mh = (ModelHandler)obj;
				if (mh.getDiagram_definition() != null)
					return true;
			}
		}
		return false;
	}
}
