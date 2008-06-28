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

package jsdai.express_compiler;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ExpressPerspectiveFactory implements IPerspectiveFactory {
  
  /**
   * Constructs a new Default layout engine.
   */
  public ExpressPerspectiveFactory() {
    super();
  }


	public void createInitialLayout(IPageLayout layout) {
    String editorArea = layout.getEditorArea();
    IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
    folder.addView(IPageLayout.ID_RES_NAV);
    IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
    outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
//    outputfolder.addView("net.jsdai.express_compiler.views.ExpressConsoleView");
//    outputfolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
    outputfolder.addView("org.eclipse.ui.console.ConsoleView");
    
//    layout.addActionSet("net.jsdai.express_compiler.actionSet");
		
//		layout.addPerspectiveShortcut("net.jsdai.express_g.perspective");
		
	}

/*

  public void createInitialLayout(IPageLayout layout) {
    String editorArea = layout.getEditorArea();
    
    IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
    folder.addView(JavaUI.ID_PACKAGES);
    folder.addView(JavaUI.ID_TYPE_HIERARCHY);
    folder.addPlaceholder(IPageLayout.ID_RES_NAV);
    
    IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
    outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
    outputfolder.addView(JavaUI.ID_JAVADOC_VIEW);
    outputfolder.addView(JavaUI.ID_SOURCE_VIEW);
    outputfolder.addPlaceholder(NewSearchUI.SEARCH_VIEW_ID);
    outputfolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
    outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
    outputfolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
    
    layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, (float)0.75, editorArea);
    
    layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
    layout.addActionSet(JavaUI.ID_ACTION_SET);
    layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
    layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
    
    // views - java
    layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
    layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
    layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);
    layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW);

    // views - search
    layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);
    
    // views - debugging
    layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);

    // views - standard workbench
    layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
    layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
    layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        
    // new actions - Java project creation wizard
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewEnumCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");   //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard"); //$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
    layout.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
  }

*/
}
