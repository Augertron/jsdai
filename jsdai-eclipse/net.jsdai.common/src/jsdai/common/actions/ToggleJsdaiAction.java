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

package jsdai.common.actions;

import jsdai.common.natures.JSDAIProjectNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * A popup menu action that defines toggling the JSDAI nature of a project
 */
public class ToggleJsdaiAction implements IObjectActionDelegate {
  private IProject fProject;

  	/**
  	 * 
  	 *
  	 */
  	public ToggleJsdaiAction() {
  	}
  
    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction anAction, IWorkbenchPart aTargetPart) {
    // nothing to do here...
    }

  /**
   * @see org.eclipse.ui.IActionDelegate#selectionChanged(IAction, ISelection)
   */
  public void selectionChanged(IAction anAction, ISelection aSelection) {
    if (aSelection instanceof IStructuredSelection) {
      boolean enabled = false;
      Object obj = (((IStructuredSelection)aSelection).getFirstElement());
      if (obj == null) {
        fProject = null;
      } else {
        if (obj instanceof IProject) {
          fProject = (IProject)obj;
        } else {
  
          // In plugin.xml is configured to allow IResource instances
          // which are adaptable to IProject
          fProject = (IProject)((IAdaptable)obj).getAdapter(IProject.class);
        }
        if (fProject != null && fProject.isOpen()) {
          enabled = true;
          if (JSDAIProjectNature.hasNature(fProject)) {
        	  anAction.setText("Switch to Java Project");
          } else {
        	  anAction.setText("Switch to JSDAI Project");
          }
         }
      }
      anAction.setEnabled(enabled);
      
    }
  }

  /**
   * @see org.eclipse.ui.IActionDelegate#run(IAction)
   */
  public void run(IAction anAction) {
    Assert.isNotNull(fProject);
    if (JSDAIProjectNature.hasNature(fProject)) {
      JSDAIProjectNature.removeNature(fProject, null);
	  anAction.setText("Switch to JSDAI Project");
    } else {
    	JSDAIProjectNature.addNature(fProject, null);
	  anAction.setText("Switch to Java Project");
    }
  }
}
