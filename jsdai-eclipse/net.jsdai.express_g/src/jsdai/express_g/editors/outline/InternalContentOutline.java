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

package jsdai.express_g.editors.outline;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;

/**
 * @author Mantas Balnys
 *
 */
public class InternalContentOutline extends ContentOutline implements IInternalPartListener {
	/**
	 * 
	 */
	public InternalContentOutline() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	public void partActivated(IWorkbenchPart part) {
		Object obj = part.getAdapter(IInternalPartProvider.class);		
		if (obj instanceof IInternalPartProvider) {
			((IInternalPartProvider)obj).addInternalPartListener(5, this);
			internalPartChanged(((IInternalPartProvider)obj).getActiveInternalPart());
		}
//		super.partActivated(part);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.editors.outline.IInternalPartListener#internalPartChanged(org.eclipse.core.runtime.IAdaptable)
	 */
	public void internalPartChanged(IWorkbenchPart part) {
		super.partActivated(part);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.PageBookView#isImportant(org.eclipse.ui.IWorkbenchPart)
	 */
	protected boolean isImportant(IWorkbenchPart part) {
		return super.isImportant(part) || (part instanceof FakeWorkbenchPart);
	}
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
//		WorkbenchHelp.setHelp(getPageBook(), SdaieditPlugin.ID_SDAIEDIT + ".InternalOutlineContextId");
	}
}
