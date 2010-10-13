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
public class DiagramImportFileSelectionPage extends WizardNameFileSelectionPage {
	private Button checkDict;
	private Button checkEG;
	private boolean enabledDict = true;
	private boolean checkedDict = true;
	private boolean enabledEG = true;
	private boolean checkedEG = true;

	/**
	 * @param projectPath
	 * @param name
	 */
	public DiagramImportFileSelectionPage(IPath projectPath, String name) {
		super(projectPath, name);
	}

	/**
	 * @param projectPath
	 * @param name
	 * @param label
	 * @param dialog
	 * @param dialogType
	 */
	public DiagramImportFileSelectionPage(IPath projectPath, String name,
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
	public DiagramImportFileSelectionPage(IPath projectPath, String name,
			String label, String dialog, int dialogType, int dialogStyle,
			String[] extensions, boolean none_available) {
		super(projectPath, name, label, dialog, dialogType, dialogStyle,
				extensions, none_available);
	}
	
	/**
	 * 
	 */
	protected void fillInExtension_old(Composite parent) {
		parent.setLayout(new FillLayout());
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		checkDict = new Button(group, SWT.CHECK);
		checkDict.setText("Dictionary data");
		checkEG = new Button(group, SWT.CHECK);
		checkEG.setText("Layout (Express-G) data");
		
		checkDict.setSelection(checkedDict);
		checkEG.setSelection(checkedEG);
		checkDict.setEnabled(enabledDict);
		checkEG.setEnabled(enabledEG);
	}

	protected void fillInExtension(Composite parent) {
		parent.setLayout(new FillLayout());
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		checkDict = new Button(group, SWT.CHECK);
		checkDict.setText("Dictionary data");
		checkEG = new Button(group, SWT.CHECK);
		checkEG.setText("Layout (Express-G) data");
		
		checkDict.setSelection(checkedDict);
		checkEG.setSelection(checkedEG);
		checkDict.setEnabled(enabledDict);
		checkEG.setEnabled(enabledEG);
	}
	
	public void setCheckedDict(boolean checked) {
		checkedDict = checked;
		if (checkDict != null) checkDict.setSelection(checked);
	}
	
	public void setCheckedEG(boolean checked) {
		checkedEG = checked;
		if (checkEG != null) checkEG.setSelection(checked);
	}
	
	public boolean isCheckedDict() {
		if (checkDict != null) checkedDict = checkDict.getSelection(); 
		return checkedDict;
	}
	
	public boolean isCheckedEG() {
		if (checkEG != null) checkedEG = checkEG.getSelection(); 
		return checkedEG;
	}
	
	public void setEnabledDict(boolean enabled) {
		enabledDict = enabled;
		if (checkDict != null) checkDict.setEnabled(enabled);
	}
	
	public void setEnabledEG(boolean enabled) {
		enabledEG = enabled;
		if (checkEG != null) checkEG.setEnabled(enabled);
	}
}
