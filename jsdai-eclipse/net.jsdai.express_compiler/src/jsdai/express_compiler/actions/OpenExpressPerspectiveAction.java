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

package jsdai.express_compiler.actions;

import jsdai.express_compiler.ExpressCompilerPlugin;
import jsdai.express_compiler.help.IExpressCompilerHelpContextIds;

import org.eclipse.jface.action.Action;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.PlatformUI;

/**
 * Action to open Express perspective.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class OpenExpressPerspectiveAction extends Action {

  /**
   * Create a new <code>OpenExpressPerspectiveAction</code>.
   */
  public OpenExpressPerspectiveAction() {
    PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IExpressCompilerHelpContextIds.OPEN_EXPRESS_PERSPECTIVE_ACTION);
  }

  public void run() {
    IWorkbench workbench= ExpressCompilerPlugin.getDefault().getWorkbench();
    IWorkbenchWindow window= workbench.getActiveWorkbenchWindow();
    IWorkbenchPage page= window.getActivePage();
    IAdaptable input;
    if (page != null)
      input= page.getInput();
    else
      input= ResourcesPlugin.getWorkspace().getRoot();
    try {
      workbench.showPerspective("net.jsdai.express_compiler.ExpressPerspective", window, input);
    } catch (WorkbenchException e) {
				ExpressCompilerPlugin.log(e);
//System.out.println("exception when showing express perspective: " + e);
    	//      ExceptionHandler.handle(e, window.getShell(), 
//        ActionMessages.OpenExpressPerspectiveAction_dialog_title, 
//        ActionMessages.OpenExpressPerspectiveAction_error_open_failed); 
    }
  }
}
