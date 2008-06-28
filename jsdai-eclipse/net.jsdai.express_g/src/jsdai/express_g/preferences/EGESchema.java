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
import jsdai.express_g.editors.ExpressGEditor;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Mantas Balnys
 *
 */
public class EGESchema extends FieldEditorPreferencePage implements	IWorkbenchPreferencePage {
	private IPreferenceStore store = new PreferenceStore(SdaieditPlugin.getDefault().getStateLocation().append("pref.tmp").toOSString());
	protected PropertySharing prop = null;
	
	/**
	 * @param style
	 */
	public EGESchema() {
		super(GRID);
		setPreferenceStore(store);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		IPreferenceStore store = getPreferenceStore();
		if (prop != null) {
			Font f = prop.getFont1();
			if (f != null) {
				store.setValue(PropertySharing.PROP_FONT1, f.getFontData()[0].toString());
			}
			f = prop.getFont2();
			if (f != null) {
				store.setValue(PropertySharing.PROP_FONT2, f.getFontData()[0].toString());
			}
			
			addField(new FontFieldEditor(PropertySharing.PROP_FONT1, "Entity name font:", "(EX) Entity_for_font_1", getFieldEditorParent()));
			addField(new FontFieldEditor(PropertySharing.PROP_FONT2, "Attribute name font:", "attribute SET [1:?], (INV) related", getFieldEditorParent()));
			addField(new IntegerFieldEditor(PropertySharing.PROP_PAGE_RENUMBER, 
					"Restart page numbering after (0 for none)", getFieldEditorParent()));
		} else {
			setDescription("No active schema found");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		IEditorPart editor = SdaieditPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof SdaiEditor) {
			IWorkbenchPart part = ((SdaiEditor)editor).getActiveInternalPart();
			if (part instanceof ExpressGEditor) {
				prop = ((ExpressGEditor)part).getProperties();
				IPreferenceStore defstore = SdaieditPlugin.getDefault().getPreferenceStore();
				int ren = 0;
				if (defstore != null) {
					ren = defstore.getInt(PropertySharing.PROP_DEFAULT_PAGE_RENUMBER);

					store.setDefault(PropertySharing.PROP_FONT1, defstore.getString(PropertySharing.PROP_DEFAULT_FONT1));
					store.setDefault(PropertySharing.PROP_FONT2, defstore.getString(PropertySharing.PROP_DEFAULT_FONT2));
				}
				store.setDefault(PropertySharing.PROP_PAGE_RENUMBER, ren);
				store.setValue(PropertySharing.PROP_PAGE_RENUMBER, prop.getPageRenumber());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean ok = super.performOk();
		if (prop != null) {
			String f1 = store.getString(PropertySharing.PROP_FONT1).replaceAll(";", "");
			String f2 = store.getString(PropertySharing.PROP_FONT2).replaceAll(";", "");
			prop.setPageRenumber(store.getInt(PropertySharing.PROP_PAGE_RENUMBER));
			prop.setFont1(new Font(Display.getDefault(), new FontData(f1)));
			prop.setFont2(new Font(Display.getDefault(), new FontData(f2)));
			prop.setModified(true);
			prop.handler().repaint(false);
			prop.handler().update();
			prop.handler().repaint(true);
		}
		return ok;
	}
}
