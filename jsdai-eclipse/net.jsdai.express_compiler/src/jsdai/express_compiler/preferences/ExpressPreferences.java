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

package jsdai.express_compiler.preferences;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ExpressPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	static ExpressPreferences fExpressPreferences;

  public static final String AUTO_SWITCH_PERSPECTIVE = "express.autoSwitchPerspective";
	public static final String DELETE_ALL_JSDAI_MARKERS = "express.deleteAllJSDAIMarkers";
	public static final String DELETE_ALL_EXPRESS_MARKERS = "express.deleteAllExpressMarkers";
	public static final String DELETE_ALL_P21_MARKERS = "express.deleteAllP21Markers";

	BooleanFieldEditor fDeleteAll;
	BooleanFieldEditor fDeleteExpress;
	BooleanFieldEditor fDeleteP21;
	


  public ExpressPreferences() {
    super(GRID);
		setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
    setDescription("General settings for JSDAI Express development");
  	initializeDefaults();
		fExpressPreferences = this;

	}


	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();

		store.setDefault(AUTO_SWITCH_PERSPECTIVE, "ask");
		store.setDefault(DELETE_ALL_JSDAI_MARKERS, true);
		store.setDefault(DELETE_ALL_EXPRESS_MARKERS, false);
		store.setDefault(DELETE_ALL_P21_MARKERS, false);

	}

  protected void createFieldEditors() {




    // add if needed anything for general preferences

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFieldEditorParent(), IExpressCompilerHelpContextIds.EXPRESS_PREFERENCE_PAGE);


		addField(new RadioGroupFieldEditor(
				AUTO_SWITCH_PERSPECTIVE, 
				ExpressCompilerPlugin.getResourceString("preferences.autoSwitchPerspective"), 
                1,
                new String[][] {
						{"ask", "ask"},
						{"switch to the Express perspective", "switch"},
						{"leave the current perspective", "leave"}
				},
				getFieldEditorParent(),
				true
			)
		);


   fDeleteAll = new BooleanFieldEditor(DELETE_ALL_JSDAI_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllJSDAIMarkers"), getFieldEditorParent());

	 fDeleteExpress = new BooleanFieldEditor(DELETE_ALL_EXPRESS_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllExpressMarkers"), getFieldEditorParent());

	 fDeleteP21 = new BooleanFieldEditor(DELETE_ALL_P21_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllP21Markers"), getFieldEditorParent());

//		addField(new BooleanFieldEditor(DELETE_ALL_JSDAI_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllJSDAIMarkers"), getFieldEditorParent()));
		addField(fDeleteAll);
//		addField(new BooleanFieldEditor(DELETE_ALL_EXPRESS_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllExpressMarkers"), getFieldEditorParent()));
		addField(fDeleteExpress);
//		addField(new BooleanFieldEditor(DELETE_ALL_P21_MARKERS, ExpressCompilerPlugin.getResourceString("preferences.deleteAllP21Markers"), getFieldEditorParent()));
		addField(fDeleteP21);

//System.out.println("<><> in createFieldEditors, fDeleteAll: " + fDeleteAll.getBooleanValue());

//		fDeleteExpress.setEnabled(!fDeleteAll.getBooleanValue(), getFieldEditorParent());
//		fDeleteP21.setEnabled(!fDeleteAll.getBooleanValue(), getFieldEditorParent());



		boolean current = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_JSDAI_MARKERS);

// System.out.println("<><> in createFieldEditors, fDeleteAll: " + current);

//System.out.println("in init, current: " + current);
//System.out.println("in init, fDeleteExpress: " + fDeleteExpress);
//System.out.println("in init, parent: " + getFieldEditorParent());

 

		fDeleteExpress.setEnabled(!current, getFieldEditorParent());
		fDeleteP21.setEnabled(!current, getFieldEditorParent());
		

  }
	public static ExpressPreferences getExpressPreferences() {
		return fExpressPreferences;
	}

  public void init(IWorkbench workbench) {
    // see above, also may be needed or not


  }

  protected void performDefaults() {
      super.performDefaults();
      fDeleteExpress.setEnabled(false, getFieldEditorParent());
      fDeleteP21.setEnabled(false, getFieldEditorParent());

  }

  
  
  
	 public void propertyChange(PropertyChangeEvent event) {
		 super.propertyChange(event);
				boolean current = fDeleteAll.getBooleanValue();
//				boolean current = ExpressCompilerPlugin.getDefault().getPreferenceStore().getBoolean(ExpressPreferences.DELETE_ALL_JSDAI_MARKERS);
// System.out.println("property changed: " + current);
				fDeleteExpress.setEnabled(!current, getFieldEditorParent());
				fDeleteP21.setEnabled(!current, getFieldEditorParent());

	}


}
