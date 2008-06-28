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

import java.util.Iterator;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.EGEnumerated;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogEnumerated extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Group groupList = null;
	protected Composite panelList = null;
	protected Text textList = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogEnumerated(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Enumeration type properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelList = new Composite(parent, SWT.NULL);
		groupList = new Group(panelList, SWT.NULL);
		textList = new Text(groupList, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_4 = new FormData();
		FormData FormData_3 = new FormData();
		FormData FormData_1 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		FormData_4.right = new FormAttachment(100, 0);
		FormData_4.top = new FormAttachment(0, 0);
		FormData_4.height = 200;
		FormData_4.left = new FormAttachment(0, 0);
		FormData_4.bottom = new FormAttachment(100, 0);
		FormData_3.right = new FormAttachment(100, 0);
		FormData_3.top = new FormAttachment(0, 0);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_3.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelList, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;
		
		// set properties
		panelList.setLayoutData(FormData_1);
		groupList.setLayoutData(FormData_3);
		groupList.setText("Enumeration items");
		textList.setLayoutData(FormData_4);
		panelExtend.setLayoutData(dataExtend);
		groupList.setLayout(new FormLayout());
		panelList.setLayout(new FormLayout());
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
	    textList.setText("");
	    Iterator iter = ((EGEnumerated)object).enumeration_items.iterator();
	    while (iter.hasNext()) textList.append((String)iter.next() + "\n");
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
//		groupList.setEnabled(edit);
//		panelList.setEnabled(edit);
		textList.setEditable(edit);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
	    ((EGEnumerated)object).enumeration_items.clear();
	    StringTokenizer st = new StringTokenizer(textList.getText(), textList.getLineDelimiter());
	    while (st.hasMoreTokens()) ((EGEnumerated)object).enumeration_items.add(st.nextToken());
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.properties.DialogBasic#getExtend()
	 */
	protected Composite getExtend() {
		dataExtend.height = -1;
		return panelExtend;
	}
}
