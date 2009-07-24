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

package jsdai.step_editor.help;

import jsdai.step_editor.Step_editorPlugin;

public interface IStep_editorHelpContextIds {
  public static final String PREFIX = Step_editorPlugin.PLUGIN_ID + "."; //$NON-NLS-1$
  

  public static final String PREF_MAIN_TAB = PREFIX + "pref_main"; //$NON-NLS-1$
  public static final String PREF_PERMANENT_TAB = PREFIX + "pref_permanent"; //$NON-NLS-1$
  public static final String PREF_TEMP_TAB = PREFIX + "pref_temp"; //$NON-NLS-1$
  public static final String PREF_COMPILER_TAB = PREFIX + "pref_compiler"; //$NON-NLS-1$
  public static final String PREF_OTHER_TAB = PREFIX + "pref_other"; //$NON-NLS-1$

  
  // preferences
  
	public static final String STEP_EDITOR_PREFERENCE_PAGE = PREFIX + "step_editor_preference_page_context"; //$NON-NLS-1$

	public static final String P21_EDITOR_PREFERENCE_PAGE = PREFIX + "p21_editor_preference_page_context"; //$NON-NLS-1$
  
    public static final String P21_EDITOR = PREFIX + "p21_editor_context"; //$NON-NLS-1$
  
  
}
