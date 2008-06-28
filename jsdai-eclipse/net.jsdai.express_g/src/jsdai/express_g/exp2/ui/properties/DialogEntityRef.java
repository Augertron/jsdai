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

package jsdai.express_g.exp2.ui.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.EGEntityRef;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogEntityRef extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Button radioIMPLICIT = null;
	protected Group groupRefStyle = null;
	protected Text textAlias = null;
	protected Button radioUSED = null;
	protected Button radioREFERENCED = null;
	protected Composite panelRef = null;
	protected Label labelAlias = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogEntityRef(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Referenced object properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelRef = new Composite(parent, SWT.NULL);
		labelAlias = new Label(panelRef, SWT.CENTER);
		textAlias = new Text(panelRef, SWT.BORDER);
		groupRefStyle = new Group(panelRef, SWT.NULL);
		radioUSED = new Button(groupRefStyle, SWT.RADIO);
		radioREFERENCED = new Button(groupRefStyle, SWT.RADIO);
		radioIMPLICIT = new Button(groupRefStyle, SWT.RADIO);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_3 = new FormData();
		FormData FormData_4 = new FormData();
		FormData FormData_6 = new FormData();
		FormData FormData_7 = new FormData();
		FormData FormData_8 = new FormData();
		FormData FormData_5 = new FormData();
		FormData FormData_9 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		FormData_3.top = new FormAttachment(textAlias, 0, 16777216);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_4.right = new FormAttachment(100, 0);
		FormData_4.top = new FormAttachment(0, 0);
		FormData_4.left = new FormAttachment(labelAlias, 0, 0);
		FormData_6.right = new FormAttachment(100, 0);
		FormData_6.top = new FormAttachment(0, 0);
		FormData_6.left = new FormAttachment(0, 0);
		FormData_7.right = new FormAttachment(100, 0);
		FormData_7.top = new FormAttachment(radioUSED, 0, 0);
		FormData_7.left = new FormAttachment(0, 0);
		FormData_8.right = new FormAttachment(100, 0);
		FormData_8.top = new FormAttachment(radioREFERENCED, 0, 0);
		FormData_8.left = new FormAttachment(0, 0);
		FormData_8.bottom = new FormAttachment(100, 0);
		FormData_5.right = new FormAttachment(100, 0);
		FormData_5.top = new FormAttachment(textAlias, 0, 0);
		FormData_5.left = new FormAttachment(0, 0);
		FormData_5.bottom = new FormAttachment(100, 0);
		FormData_9.right = new FormAttachment(100, 0);
		FormData_9.top = new FormAttachment(0, 0);
		FormData_9.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelRef, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;
		
		// set properties
		panelRef.setLayoutData(FormData_9);
		labelAlias.setLayoutData(FormData_3);
		labelAlias.setText("Alias:");
		textAlias.setLayoutData(FormData_4);
//		textAlias.setText("alias"); // FIX try to delete
		groupRefStyle.setLayoutData(FormData_5);
		groupRefStyle.setText("usage type:");
		radioUSED.setLayoutData(FormData_6);
		radioUSED.setText("Used");
		radioREFERENCED.setLayoutData(FormData_7);
		radioREFERENCED.setText("Referenced");
		radioIMPLICIT.setEnabled(false);
		radioIMPLICIT.setLayoutData(FormData_8);
		radioIMPLICIT.setText("Implicit");
		panelExtend.setLayoutData(panelExtend);
		radioIMPLICIT.setVisible(true);
		groupRefStyle.setLayout(new FormLayout());
		panelRef.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		panelExtend.setLayoutData(dataExtend);
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		// END EVENT_INITIALIZATION
		setEnabledName(false);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
		EGEntityRef ref = (EGEntityRef)object;
		textAlias.setText(ref.getRename());
		switch (ref.getType()) {
			case EGEntityRef.TYPE_USED :
				radioUSED.setSelection(true);
				break;
			case EGEntityRef.TYPE_REFERENCED :
				radioREFERENCED.setSelection(true);
				break;
			case EGEntityRef.TYPE_IMPLICIT :
				radioIMPLICIT.setSelection(true);
				break;
		}
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
		radioIMPLICIT.setEnabled(edit);
//		groupRefStyle.setEnabled(edit);
		textAlias.setEditable(edit);
		radioUSED.setEnabled(edit);
		radioREFERENCED.setEnabled(edit);
//		panelRef.setEnabled(edit);
//		labelAlias.setEnabled(edit);
	}
	
	protected void updateObject() {
		super.updateObject();
		EGEntityRef ref = (EGEntityRef)object;
		ref.setRename(textAlias.getText());
		if (radioUSED.getSelection()) {
			ref.setType(EGEntityRef.TYPE_USED);
		} else
		if (radioREFERENCED.getSelection()) {
			ref.setType(EGEntityRef.TYPE_REFERENCED);
		} else
		if (radioIMPLICIT.getSelection()) {
			ref.setType(EGEntityRef.TYPE_IMPLICIT);
		}
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.properties.DialogBasic#getExtend()
	 */
	protected Composite getExtend() {
		dataExtend.height = -1;
		return panelExtend;
	}
}
