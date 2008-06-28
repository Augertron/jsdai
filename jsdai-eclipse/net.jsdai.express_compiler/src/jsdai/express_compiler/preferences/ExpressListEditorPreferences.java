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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ExpressListEditorPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public ExpressListEditorPreferences() {
    super(GRID);
	setPreferenceStore(ExpressCompilerPlugin.getDefault().getPreferenceStore());
    setDescription("Express list editor preferences");
  }

  protected void createFieldEditors() {
    // add if needed anything for general preferences
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFieldEditorParent(), IExpressCompilerHelpContextIds.EXPRESS_LIST_EDITOR_PREFERENCE_PAGE);

  }

  public void init(IWorkbench workbench) {
    // see above, also may be needed or not

  }

}
