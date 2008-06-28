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
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.VisualPage;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Mantas Balnys
 *
 */
public class EGESchemaDefault extends FieldEditorPreferencePage implements	IWorkbenchPreferencePage {
	protected IPreferenceStore store = null;
	
	/**
	 * @param style
	 */
	public EGESchemaDefault() {
		super(GRID);
		store = SdaieditPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new FontFieldEditor(PropertySharing.PROP_DEFAULT_FONT1, "Entity name font:", "(EX) Entity_for_font_1", getFieldEditorParent()));
		addField(new FontFieldEditor(PropertySharing.PROP_DEFAULT_FONT2, "Attribute name font:", "attribute SET [1:?], (INV) related", getFieldEditorParent()));
		addField(new StringFieldEditor(PropertySharing.PROP_DEFAULT_PAGE_MASK, 
				"Default page frame text mask,\nuse \"" + PropertySharing.PAGE_MASK_NAME + 
				"\" for page name,\n\"" + PropertySharing.PAGE_MASK_PAGE + "\" for page number and\n\"" 
				+ PropertySharing.PAGE_MASK_MAX_PAGE + "\" for total number of pages\n", 
				getFieldEditorParent()));
		addField(new IntegerFieldEditor(PropertySharing.PROP_DEFAULT_PAGE_RENUMBER, 
				"Restart page numbering after (0 for none)", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		store.setDefault(PropertySharing.PROP_DEFAULT_PAGE_MASK, VisualPage.DEFAULT_PAGE_MASK);
		store.setDefault(PropertySharing.PROP_DEFAULT_FONT1, PropertySharing.DEFAULT_FONT1_DATA);
		store.setDefault(PropertySharing.PROP_DEFAULT_FONT2, PropertySharing.DEFAULT_FONT2_DATA);
		store.setDefault(PropertySharing.PROP_DEFAULT_PAGE_RENUMBER, 0);
		updateApplyButton();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean ok = super.performOk();
//		store.setValue(PropertySharing.PROP_FONT1, store.getString(PropertySharing.PROP_DEFAULT_FONT1));
//		store.setValue(PropertySharing.PROP_FONT2, store.getString(PropertySharing.PROP_DEFAULT_FONT2));
		SdaieditPlugin.getDefault().savePluginPreferences();
		updateApplyButton();
		return ok;
	}
	
	
}
