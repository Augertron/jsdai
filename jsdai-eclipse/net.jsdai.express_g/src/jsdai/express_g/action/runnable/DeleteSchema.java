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

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.editors.SdaiEditorInput;
import jsdai.lang.SdaiException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * Deletes schema with all diagrams
 * (editting mode) 
 * 
 * @author Mantas Balnys
 *
 */
public class DeleteSchema extends RunnableOnView {

	/**
	 * @param editor
	 * @param viewer
	 */
	public DeleteSchema(SdaiEditor editor, Viewer viewer) {
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
				ModelHandler mhDict = (ModelHandler)obj;
				if ((mhDict.getDiagram_definition() == null) && 
						(MessageDialog.openQuestion(getShell(), "Confirm delete schema", 
						"Are you sure you want to delete this schema and all diagrams for it?"))) {
					RepositoryHandler rh = mhDict.getRepositoryHandler();
					String schemaName = mhDict.getName();
					String[] models = rh.getSchemaDiagrams(schemaName);
					RepositoryChanger changer = new RepositoryChanger(models, "Deleting schema");
					String msg = rh.startRWChanger(changer);
					if (msg != null) {
						MessageDialog.openWarning(getShell(), "Delete Schema Action", msg);
					} else {
						rh.deleteModel(schemaName);
						try {
							rh.update();
						} catch (SdaiException sex) {
							SdaieditPlugin.log(sex);
							SdaieditPlugin.console(sex.toString());
						}
						refresh();
						selectInView(rh);
					}
					changer.done();
					rh.endRWChanger(changer);
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
		SdaiEditorInput input = (SdaiEditorInput)editor.getEditorInput();
		if (!input.isReadonly()) {
			ISelection selection = getSelection();
			if (selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				if (obj instanceof ModelHandler) {
					ModelHandler mh = (ModelHandler)obj;
					if (mh.getDiagram_definition() == null)
						return true;
				}
			}
		}
		return false;
	}
}
