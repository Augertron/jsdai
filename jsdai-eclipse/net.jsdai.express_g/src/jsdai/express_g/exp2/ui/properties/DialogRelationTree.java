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

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.EGRelationTree;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogRelationTree extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Button radio2 = null;
	protected Button radio1 = null;
	protected Button radio3 = null;
	protected Composite panelSubtype = null;
	protected Group groupType = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogRelationTree(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Relation properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelSubtype = new Composite(parent, SWT.NULL);
		groupType = new Group(panelSubtype, SWT.NULL);
		radio1 = new Button(groupType, SWT.RADIO);
		radio2 = new Button(groupType, SWT.RADIO);
		radio3 = new Button(groupType, SWT.RADIO);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_4 = new FormData();
		FormData FormData_5 = new FormData();
		FormData FormData_6 = new FormData();
		FormData FormData_3 = new FormData();
		FormData FormData_1 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		FormData_4.right = new FormAttachment(100, 0);
		FormData_4.top = new FormAttachment(0, 0);
		FormData_4.left = new FormAttachment(0, 0);
		FormData_5.right = new FormAttachment(100, 0);
		FormData_5.top = new FormAttachment(radio1, 0, 0);
		FormData_5.left = new FormAttachment(0, 0);
		FormData_6.right = new FormAttachment(100, 0);
		FormData_6.top = new FormAttachment(radio2, 0, 0);
		FormData_6.left = new FormAttachment(0, 0);
		FormData_6.bottom = new FormAttachment(100, 0);
		FormData_3.right = new FormAttachment(100, 0);
		FormData_3.top = new FormAttachment(0, 0);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_3.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelSubtype, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;

		// set properties
		panelSubtype.setLayoutData(FormData_1);
		groupType.setLayoutData(FormData_3);
		groupType.setText("Type:");
		radio1.setLayoutData(FormData_4);
		radio1.setText("ANDOR");
		radio2.setLayoutData(FormData_5);
		radio2.setText("AND");
		radio3.setLayoutData(FormData_6);
		radio3.setText("ONEOF");
		panelExtend.setLayoutData(dataExtend);
		groupType.setLayout(new FormLayout());
		panelSubtype.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		radio1.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				textName.setText("");
			}
		});
		
		radio2.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				textName.setText("&");
			}
		});
		
		radio3.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				textName.setText("1");
			}
		});
		
		// END EVENT_INITIALIZATION
		setEnabledName(false);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
	    if ((((AbstractEGRelation)object).getType() == AbstractEGRelation.TYPE_INHERITANCE)&&
	    		(object instanceof EGRelationTree)) {
	    	panelSubtype.setVisible(true);
	    	switch (((EGRelationTree)object).getSubtype_expression()) {
	        	case EGRelationTree.SUBTYPE_EXPRESSION_AND :
	        		radio2.setSelection(true);
	        		break;
	        	case EGRelationTree.SUBTYPE_EXPRESSION_ANDOR :
	        		radio1.setSelection(true);
        			break;
	        	case EGRelationTree.SUBTYPE_EXPRESSION_ONEOF :
	        		radio3.setSelection(true);
	        		break;
	    	}
	    } else {
	    	radio1.setEnabled(false);
	    	radio2.setEnabled(false);
	    	radio3.setEnabled(false);
	    }
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
		radio2.setEnabled(edit);
		radio1.setEnabled(edit);
		radio3.setEnabled(edit);
//		panelSubtype.setEnabled(edit);
//		groupType.setEnabled(edit);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
	    if ((((AbstractEGRelation)object).getType() == AbstractEGRelation.TYPE_INHERITANCE)&&
	    		(object instanceof EGRelationTree)) {
	        if (radio2.getSelection()) ((EGRelationTree)object).setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_AND); else
	        if (radio3.getSelection()) ((EGRelationTree)object).setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_ONEOF); else
	        if (radio1.getSelection()) ((EGRelationTree)object).setSubtype_expression(EGRelationTree.SUBTYPE_EXPRESSION_ANDOR);
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
