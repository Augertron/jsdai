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

package jsdai.express_g.perspectives;

import jsdai.express_g.SdaieditPlugin;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * @author Mantas Balnys
 *
 */
public class PerspectiveFactory implements IPerspectiveFactory {
	public static final String ID = "net.jsdai.express_g.perspective"; 

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
		// Add perspectives
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		try {
			layout.addPerspectiveShortcut("net.jsdai.express_compiler.ExpressPerspective"); // external - Express Compiler
		} catch (Throwable t) {};
		
		// Add views in show view menu
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(SdaieditPlugin.ID_EXPRESS_G_OUTLINE);
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		
		// Get the editor area.
		String editorArea = layout.getEditorArea();

		// Top left: Resource Navigator view and Bookmarks view placeholder
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.2f, editorArea);
		topLeft.addView(IPageLayout.ID_RES_NAV);
		topLeft.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		// Bottom left: Console, Properties, etc.
		IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f,	"topLeft");
		bottomLeft.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottomLeft.addView(IPageLayout.ID_PROP_SHEET);
		bottomLeft.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
		bottomLeft.addPlaceholder(IPageLayout.ID_TASK_LIST);

//		layout.setFixed(true); // all lower added items will be fixed - wouldn't have close button
		
		// Top right: Outline
		IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.75f, editorArea);
		topRight.addView(IPageLayout.ID_OUTLINE);

		// Bottom right: Express-G Outline
		IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.60f, "topRight");
		bottomRight.addView(SdaieditPlugin.ID_EXPRESS_G_OUTLINE);
		bottomRight.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);


		// Add Actions
		try {
			layout.addActionSet("net.jsdai.express_compiler.actionSet"); // external - Express Compiler
		} catch (Throwable t) {};
		
	}

}
