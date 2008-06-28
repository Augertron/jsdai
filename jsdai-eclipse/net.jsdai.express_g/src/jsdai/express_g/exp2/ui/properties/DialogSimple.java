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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.EGSimple;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogSimple extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Button[] radio = null;
	protected Group groupSimple = null;
	protected Label labelSize = null;
	protected Text textSize = null;
	protected Composite panelSimple = null;
	protected Button buttonFixed = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogSimple(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Simple type properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelSimple = new Composite(parent, SWT.NULL);
		groupSimple = new Group(panelSimple, SWT.NULL);
		radio = new Button[EGSimple.TYPE_NAME.length];
		for (int i = 0; i < radio.length; i++) {
			radio[i] = new Button(groupSimple, SWT.RADIO);
		}
		labelSize = new Label(panelSimple, SWT.CENTER);
		textSize = new Text(panelSimple, SWT.BORDER);
		buttonFixed = new Button(panelSimple, SWT.CHECK);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData[] FormData_R = new FormData[radio.length];
		for (int i = 0; i < FormData_R.length; i++) {
			FormData_R[i] = new FormData();
		}
		FormData FormData_3 = new FormData();
		FormData FormData_11 = new FormData();
		FormData FormData_12 = new FormData();
		FormData FormData_13 = new FormData();
		FormData FormData_1 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		for (int i = 0; i < FormData_R.length; i++) {
			FormData_R[i].right = new FormAttachment(100, 0);
			FormData_R[i].left = new FormAttachment(0, 0);
			if (i == 0) {
				FormData_R[i].top = new FormAttachment(0, 1);
			} else {
				FormData_R[i].top = new FormAttachment(radio[i-1], 2, 0);
			}
		}
		FormData_R[FormData_R.length - 1].bottom = new FormAttachment(100, 0);

		FormData_3.right = new FormAttachment(100, 0);
		FormData_3.top = new FormAttachment(0, 0);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_11.left = new FormAttachment(0, 0);
		FormData_11.right = new FormAttachment(textSize, 0, 0);
		FormData_11.top = new FormAttachment(textSize, 0, 16777216);
		FormData_12.right = new FormAttachment(100, 0);
		FormData_12.top = new FormAttachment(groupSimple, 0, 0);
		FormData_13.right = new FormAttachment(100, 0);
		FormData_13.top = new FormAttachment(textSize, 0, 0);
		FormData_13.left = new FormAttachment(0, 0);
		FormData_13.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelSimple, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;

		// set properties
		panelSimple.setLayoutData(FormData_1);
		groupSimple.setLayoutData(FormData_3);
		groupSimple.setText("type");
		for (int i = 0; i < radio.length; i++) {
			radio[i].setLayoutData(FormData_R[i]);
			radio[i].setText(EGSimple.TYPE_NAME[i]);
		}
		labelSize.setLayoutData(FormData_11);
		labelSize.setText("precision:");
		labelSize.setAlignment(SWT.RIGHT);
		textSize.setLayoutData(FormData_12);
		buttonFixed.setLayoutData(FormData_13);
		buttonFixed.setText("fixed");
		panelExtend.setLayoutData(dataExtend);
		groupSimple.setLayout(new FormLayout());
		panelSimple.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION
		for (int i = 0; i < radio.length; i++) {
			radio[i].addSelectionListener(new DialogSimpleChangeListener(i, this));
		}
		
		
		// END EVENT_INITIALIZATION
		setEnabledName(false);
	}

	protected void widgetSelected(int type) {
		textName.setText(radio[type].getText());
	    switch (type) {
	        case EGSimple.TYPE_REAL :
	        	labelSize.setText("precision:");
	          	textSize.setEnabled(true);
	          	buttonFixed.setEnabled(false);
	          	break;
	        case EGSimple.TYPE_STRING :
	        case EGSimple.TYPE_BINARY :
	        	labelSize.setText("width:");
	          	textSize.setEnabled(true);
	          	buttonFixed.setEnabled(true);
	          	break;
	        default :
	        	labelSize.setText("width:");
	          	textSize.setEnabled(false);
	          	buttonFixed.setEnabled(false);
	          	break;
	    }
	}
	
	private static class DialogSimpleChangeListener extends SelectionAdapter {
		private int type;
		private DialogSimple dialog;

		public DialogSimpleChangeListener(int type, DialogSimple dialog) {
		    this.type = type;
		    this.dialog = dialog;
		}

		public void widgetSelected(SelectionEvent e) {
			dialog.widgetSelected(type);
		}
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
	    radio[((EGSimple)object).getType()].setSelection(true);
	    widgetSelected(((EGSimple)object).getType());
	    int width = ((EGSimple)object).getType_width();
	    if (width == EGSimple.WIDTH_NONE) textSize.setText("");
	    	else textSize.setText(String.valueOf(((EGSimple)object).getType_width()));
	    buttonFixed.setSelection(((EGSimple)object).isType_width_fixed());
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
		for (int i = 0; i < radio.length; i++) radio[i].setEnabled(edit);
//		groupSimple.setEnabled(edit);
//		labelSize.setEnabled(edit);
		textSize.setEditable(edit);
//		panelSimple.setEnabled(edit);
		buttonFixed.setEnabled(edit);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
	    for (int i = 0; i < radio.length; i++)
	        if (radio[i].getSelection()) ((EGSimple)object).setType(i);
	    try {
	        int width = Integer.parseInt(textSize.getText());
	        ((EGSimple)object).setType_width(width);
	    } catch (NumberFormatException ex) {
	        ((EGSimple)object).setType_width(EGSimple.WIDTH_NONE);
	    }
	    ((EGSimple)object).setType_width_fixed(buttonFixed.getSelection());
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.properties.DialogBasic#getExtend()
	 */
	protected Composite getExtend() {
		dataExtend.height = -1;
		return panelExtend;
	}
}
