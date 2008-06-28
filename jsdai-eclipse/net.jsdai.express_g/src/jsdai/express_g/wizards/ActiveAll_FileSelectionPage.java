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

package jsdai.express_g.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author Mantas Balnys
 *
 */
public class ActiveAll_FileSelectionPage extends WizardFileSelectionPage {
	private Button selectAll = null;
	private Button selectSingle = null;


	/**
	 * @param projectPath
	 * @param name
	 */
	public ActiveAll_FileSelectionPage(IPath projectPath, String name) {
		super(projectPath, name);
	}

	/**
	 * @param projectPath
	 * @param name
	 * @param label
	 * @param dialog
	 * @param dialogType
	 */
	public ActiveAll_FileSelectionPage(IPath projectPath, String name,
			String label, String dialog, int dialogType, boolean none_available) {
		super(projectPath, name, label, dialog, dialogType, none_available);
	}

	/**
	 * @param projectPath
	 * @param name
	 * @param label
	 * @param dialog
	 * @param dialogType
	 * @param dialogStyle
	 * @param extensions
	 */
	public ActiveAll_FileSelectionPage(IPath projectPath, String name,
			String label, String dialog, int dialogType, int dialogStyle,
			String[] extensions, boolean none_available) {
		super(projectPath, name, label, dialog, dialogType, dialogStyle,
				extensions, none_available);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.wizards.WizardFileSelectionPage#fillInExtension(org.eclipse.swt.widgets.Composite)
	 */
	protected void fillInExtension(Composite parent) {
		parent.setLayout(new FillLayout());
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		selectAll = new Button(group, SWT.RADIO);
		selectAll.setText("Export all schemas in this file");
		selectSingle = new Button(group, SWT.RADIO);
		selectSingle.setText("Export only active schema");
	}
	
	public void setSchemaSelection(boolean enabled) {
		if (enabled)
			selectSingle.setSelection(true);
		else			
			selectAll.setSelection(true);
		selectAll.setEnabled(enabled);
		selectSingle.setEnabled(enabled);
	}
	
	public boolean isExportSingle() {
		return selectSingle.getSelection();
	}
}
