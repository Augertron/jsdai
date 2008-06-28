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

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogEntity extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Composite panelAbstract = null;
	protected Button buttonAbstract = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogEntity(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Entity properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelAbstract = new Composite(parent, SWT.NULL);
		buttonAbstract = new Button(panelAbstract, SWT.CHECK);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_8 = new FormData();
		FormData FormData_6 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		FormData_8.right = new FormAttachment(100, 0);
		FormData_8.top = new FormAttachment(0, 0);
		FormData_8.left = new FormAttachment(0, 0);
		FormData_8.bottom = new FormAttachment(100, 0);
		FormData_6.right = new FormAttachment(100, 0);
		FormData_6.top = new FormAttachment(0, 0);
		FormData_6.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelAbstract, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;
		
		// set properties
		panelAbstract.setLayoutData(FormData_6);
		buttonAbstract.setLayoutData(FormData_8);
		buttonAbstract.setText("Abstract");
		panelExtend.setLayoutData(dataExtend);
		panelAbstract.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		// END EVENT_INITIALIZATION
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
		buttonAbstract.setSelection(!((EGEntity)object).isInstantiable());
	    if ((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0) {
	    	buttonAbstract.setEnabled(true);
	    } else {
	    	buttonAbstract.setEnabled(false);
	    }
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
	    ((EGEntity)object).setInstantiable(!buttonAbstract.getSelection());
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.properties.DialogBasic#getExtend()
	 */
	protected Composite getExtend() {
		dataExtend.height = -1;
		return panelExtend;
	}
}
