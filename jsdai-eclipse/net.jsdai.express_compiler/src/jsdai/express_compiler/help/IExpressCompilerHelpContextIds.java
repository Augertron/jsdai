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

package jsdai.express_compiler.help;

import jsdai.express_compiler.ExpressCompilerPlugin;

public interface IExpressCompilerHelpContextIds {
//  public static final String PREFIX = ExpressCompilerPlugin.getUniqueIdentifier() + "."; //$NON-NLS-1$
  public static final String PREFIX = ExpressCompilerPlugin.PLUGIN_ID + "."; //$NON-NLS-1$
  
  public static final String PROJECT_WIZARD_MAIN_TAB = PREFIX + "new_proj_wiz_main"; //$NON-NLS-1$
  public static final String PROJECT_WIZARD_PERMANENT_TAB = PREFIX + "new_proj_wiz_permanent"; //$NON-NLS-1$
  public static final String PROJECT_WIZARD_TEMP_TAB = PREFIX + "new_proj_wiz_temp"; //$NON-NLS-1$
  public static final String PROJECT_WIZARD_COMPILER_TAB = PREFIX + "new_proj_wiz_compiler"; //$NON-NLS-1$
  public static final String PROJECT_WIZARD_OTHER_TAB = PREFIX + "new_proj_wiz_other"; //$NON-NLS-1$

  public static final String PROP_MAIN_TAB = PREFIX + "prop_main"; //$NON-NLS-1$
  public static final String PROP_PERMANENT_TAB = PREFIX + "prop_permanent"; //$NON-NLS-1$
  public static final String PROP_TEMP_TAB = PREFIX + "prop_temp"; //$NON-NLS-1$
  public static final String PROP_COMPILER_TAB = PREFIX + "prop_compiler"; //$NON-NLS-1$
  public static final String PROP_OTHER_TAB = PREFIX + "prop_other"; //$NON-NLS-1$

  public static final String PREF_MAIN_TAB = PREFIX + "pref_main"; //$NON-NLS-1$
  public static final String PREF_PERMANENT_TAB = PREFIX + "pref_permanent"; //$NON-NLS-1$
  public static final String PREF_TEMP_TAB = PREFIX + "pref_temp"; //$NON-NLS-1$
  public static final String PREF_COMPILER_TAB = PREFIX + "pref_compiler"; //$NON-NLS-1$
  public static final String PREF_OTHER_TAB = PREFIX + "pref_other"; //$NON-NLS-1$

  public static final String EXPRESS_PROJECT_WIZ_NAME_PAGE = PREFIX + "express_project_wiz_name_page";

	public static final String OPEN_EXPRESS_PERSPECTIVE_ACTION = PREFIX + "open_express_perspective_action";
  
  // preferences
  
	public static final String EXPRESS_PREFERENCE_PAGE = PREFIX + "express_preference_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_PROJECT_PREFERENCE_PAGE = PREFIX + "express_project_preference_page_context"; //$NON-NLS-1$

	public static final String EXPRESS_EDITORS_PREFERENCE_PAGE = PREFIX + "express_editors_preference_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_EDITOR_PREFERENCE_PAGE = PREFIX + "express_editor_preference_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_LIST_EDITOR_PREFERENCE_PAGE = PREFIX + "express_list_editor_preference_page_context"; //$NON-NLS-1$
	public static final String P21_EDITOR_PREFERENCE_PAGE = PREFIX + "p21_editor_preference_page_context"; //$NON-NLS-1$
	public static final String COMPLEX_ENTITY_EDITOR_PREFERENCE_PAGE = PREFIX + "complex_entity_editor_preference_page_context"; //$NON-NLS-1$

	public static final String EXPRESS_COMPILER_PREFERENCE_PAGE = PREFIX + "express_compiler_preference_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_COMPILER_IO_PREFERENCE_PAGE = PREFIX + "express_compiler_io_preference_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_DOC_PREFERENCE_PAGE = PREFIX + "express_doc_preference_page_context"; //$NON-NLS-1$
	public static final String TOOLS_PREFERENCE_PAGE = PREFIX + "tools_preference_page_context"; //$NON-NLS-1$
	public static final String VALIDATION_PREFERENCE_PAGE = PREFIX + "validation_preference_page_context"; //$NON-NLS-1$

  // properties

	public static final String EXPRESS_PROJECT_PROPERTY_PAGE = PREFIX + "express_project_property_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_PROJECT_COMPILER_PROPERTY_PAGE = PREFIX + "express_project_compiler_property_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_PROJECT_IO_PROPERTY_PAGE = PREFIX + "express_project_io_property_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_PROJECT_PERMANENT_PROPERTY_PAGE = PREFIX + "express_project_permanent_property_page_context"; //$NON-NLS-1$
	public static final String EXPRESS_PROJECT_TEMP_PROPERTY_PAGE = PREFIX + "express_project_temp_property_page_context"; //$NON-NLS-1$
  	
  // wizards

	public static final String NEW_EXPRESS_FILE_WIZARD_PAGE = PREFIX + "new_express_file_wizard_page_context"; //$NON-NLS-1$
	public static final String NEW_EXPRESS_PROJECT_WIZARD_MAIN_PAGE = PREFIX + "new_express_project_wizard_main_page_context"; //$NON-NLS-1$
	public static final String NEW_EXPRESS_PROJECT_WIZARD_PERMANENT_PAGE = PREFIX + "new_express_project_wizard_permanent_page_context"; //$NON-NLS-1$
	public static final String NEW_EXPRESS_PROJECT_WIZARD_IO_PAGE = PREFIX + "new_express_project_wizard_io_page_context"; //$NON-NLS-1$
	public static final String NEW_EXPRESS_PROJECT_WIZARD_OPTION_PAGE = PREFIX + "new_express_project_wizard_option_page_context"; //$NON-NLS-1$
	public static final String NEW_EXPRESS_PROJECT_WIZARD_TEMP_PAGE = PREFIX + "new_express_project_wizard_temp_page_context"; //$NON-NLS-1$
  	
  // editors

	public static final String EXPRESS_EDITOR = PREFIX + "express_editor_context"; //$NON-NLS-1$
	public static final String EXPRESS_LIST_EDITOR = PREFIX + "express_list_editor_context"; //$NON-NLS-1$
	public static final String P21_EDITOR = PREFIX + "p21_editor_context"; //$NON-NLS-1$
	public static final String COMPLEX_ENTITY_EDITOR = PREFIX + "complex_entity_editor_context"; //$NON-NLS-1$

  	
  // actions
  
  
  
}
