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

package jsdai.express_g.preferences;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.SdaiEditor;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Mantas Balnys
 *
 */
public class EGEAppearance extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	/**
	 * @param style
	 */
	public EGEAppearance() {
		super(GRID);
		setPreferenceStore(SdaieditPlugin.getDefault().getPreferenceStore());
		setDescription("Appearance");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(SdaiEditor.HIDE_AUTO_PERSPECTIVE, "Do not offer to switch perspective when Express-G editor starts", getFieldEditorParent()));
// TODO		addField(new BooleanFieldEditor(SdaiEditor.HIDE_READWRITE, "Do not offer to select Edit or Layout mode", getFieldEditorParent()));
// TODO		addField(new BooleanFieldEditor(SdaiEditor.READWRITE_EXD, "Always use Edit mode, not Layout (does not apply for opened editors)", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean ok = super.performOk();
		SdaieditPlugin.getDefault().savePluginPreferences();
		return ok;
	}
}
