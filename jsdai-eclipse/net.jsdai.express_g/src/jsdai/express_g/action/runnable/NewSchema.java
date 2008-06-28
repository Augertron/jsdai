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
import jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SchemaNameValidator;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.editors.SdaiEditorInput;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Mantas Balnys
 *
 */
public class NewSchema extends RunnableOnView {
	/**
	 * 
	 * @param editor
	 * @param viewer
	 * @param editMode
	 */
	public NewSchema(SdaiEditor editor, Viewer viewer) {
		super(editor, viewer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,	InterruptedException {
		RepositoryHandler rh = editor.getRepositoryHandler();
		InputDialog input = new InputDialog(getShell(), 
					"Express-G Editor", "Enter name for schema", null, 
					new SchemaNameValidator(rh.getModels()));
		if (input.open() == InputDialog.OK) {
			String schemaName = input.getValue();
			SdaiRepository repository = rh.getRepository();
			try {
				SdaiModel modelDict = repository.createSdaiModel(schemaName.toUpperCase() + "_DICTIONARY_DATA", 
						SExtended_dictionary_schema.class);
				modelDict.startReadWriteAccess();   
				ESchema_definition schemaD = (ESchema_definition)modelDict.createEntityInstance(ESchema_definition.class);
				schemaD.setName(null, schemaName);
			} catch (SdaiException sex) {
				SdaieditPlugin.log(sex);
				SdaieditPlugin.console(sex.toString());
			}
   
			try {
				rh.update();
			} catch (SdaiException sex) {
				SdaieditPlugin.log(sex);
				SdaieditPlugin.console(sex.toString());
			}
			refresh();
			selectInView(rh.getModelHandler(schemaName));
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
				if (obj instanceof RepositoryHandler) return true;
			}
		}
		return false;
	}
}
