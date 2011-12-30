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
import jsdai.express_g.exp2.eg.Agregate;
import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * @author Mantas Balnys
 *
 */
public class DialogAgregate extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Text textTo = null;
	protected Button radio1 = null;
	protected Composite panelAgregate = null;
	protected Button buttonUnique = null;
	protected Button radio4 = null;
	protected Label labelFT = null;
	protected Button radio2 = null;
	protected Button buttonCardinality = null;
	protected Text textFrom = null;
	protected Button radio3 = null;
	protected Button buttonOptional = null;
	protected Button radio5 = null;
	protected Group groupType = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	protected DialogAgregate dialogCard = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogAgregate(Composite parent, Named object, PropertySharing prop) {
		super(parent, object, prop);
		setTitle("Agregate properties");
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#uiInit()
	 */
	protected void uiInit() {
		super.uiInit();
		// START VISUALS_INITIALIZATION
		// init visuals
		Composite parent = super.getExtend();
		panelAgregate = new Composite(parent, SWT.NULL);
		groupType = new Group(panelAgregate, SWT.NULL);
		radio1 = new Button(groupType, SWT.RADIO);
		radio2 = new Button(groupType, SWT.RADIO);
		radio3 = new Button(groupType, SWT.RADIO);
		radio4 = new Button(groupType, SWT.RADIO);
		radio5 = new Button(groupType, SWT.RADIO);
		textFrom = new Text(panelAgregate, SWT.BORDER);
		labelFT = new Label(panelAgregate, SWT.CENTER);
		textTo = new Text(panelAgregate, SWT.BORDER);
		buttonOptional = new Button(panelAgregate, SWT.CHECK);
		buttonUnique = new Button(panelAgregate, SWT.CHECK);
		buttonCardinality = new Button(panelAgregate, SWT.PUSH);
		panelExtend = new Composite(parent, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_4 = new FormData();
		FormData FormData_5 = new FormData();
		FormData FormData_6 = new FormData();
		FormData FormData_13 = new FormData();
		FormData FormData_14 = new FormData();
		FormData FormData_3 = new FormData();
		FormData FormData_7 = new FormData();
		FormData FormData_9 = new FormData();
		FormData FormData_8 = new FormData();
		FormData FormData_10 = new FormData();
		FormData FormData_11 = new FormData();
		FormData FormData_12 = new FormData();
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
		FormData_13.right = new FormAttachment(100, 0);
		FormData_13.top = new FormAttachment(radio3, 0, 0);
		FormData_13.left = new FormAttachment(0, 0);
		FormData_14.right = new FormAttachment(100, 0);
		FormData_14.top = new FormAttachment(radio4, 0, 0);
		FormData_14.left = new FormAttachment(0, 0);
		FormData_14.bottom = new FormAttachment(100, 0);
		FormData_3.right = new FormAttachment(100, 0);
		FormData_3.top = new FormAttachment(0, 0);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_7.right = new FormAttachment(labelFT, 0, 0);
		FormData_7.top = new FormAttachment(groupType, 0, 0);
		FormData_7.left = new FormAttachment(0, 0);
		FormData_9.top = new FormAttachment(groupType, 0, 0);
		FormData_9.left = new FormAttachment(groupType, 0, 16777216);
		FormData_8.right = new FormAttachment(100, 0);
		FormData_8.top = new FormAttachment(groupType, 0, 0);
		FormData_8.left = new FormAttachment(labelFT, 0, 0);
		FormData_10.right = new FormAttachment(100, 0);
		FormData_10.top = new FormAttachment(textFrom, 0, 0);
		FormData_10.left = new FormAttachment(0, 0);
		FormData_11.right = new FormAttachment(100, 0);
		FormData_11.top = new FormAttachment(buttonOptional, 0, 0);
		FormData_11.left = new FormAttachment(0, 0);
		FormData_12.right = new FormAttachment(100, 0);
		FormData_12.top = new FormAttachment(buttonUnique, 0, 0);
		FormData_12.left = new FormAttachment(0, 0);
		FormData_12.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelAgregate, 0, 0);
		dataExtend.height = 0;
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		
		// set properties
		panelAgregate.setLayoutData(FormData_1);
		groupType.setLayoutData(FormData_3);
		groupType.setText("Type:");
		radio1.setLayoutData(FormData_4);
		radio1.setText("Simple");
		radio2.setLayoutData(FormData_5);
		radio2.setText("Array");
		radio3.setLayoutData(FormData_6);
		radio3.setText("Set");
		radio4.setLayoutData(FormData_13);
		radio4.setText("Bag");
		radio5.setLayoutData(FormData_14);
		radio5.setText("List");
		textFrom.setLayoutData(FormData_7);
		labelFT.setLayoutData(FormData_9);
		labelFT.setText(":");
		textTo.setLayoutData(FormData_8);
		buttonOptional.setLayoutData(FormData_10);
		buttonOptional.setText("Optional");
		buttonUnique.setLayoutData(FormData_11);
		buttonUnique.setText("Unique");
		buttonCardinality.setLayoutData(FormData_12);
		buttonCardinality.setText("Cardinality");
		panelExtend.setLayoutData(dataExtend);
		groupType.setLayout(new FormLayout());
		panelAgregate.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		radio1.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		          buttonCardinality.setEnabled(false);
		          textFrom.setEnabled(false);
		          textTo.setEnabled(false);
		          buttonOptional.setEnabled(false);
		          buttonUnique.setEnabled(false);
			}
		});
		
		radio2.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		          buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
		          textFrom.setEnabled(true);
		          textTo.setEnabled(true);
		          buttonOptional.setEnabled(true);
		          buttonUnique.setEnabled(true);
			}
		});
		
		radio3.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		          buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
		          textFrom.setEnabled(true);
		          textTo.setEnabled(true);
		          buttonOptional.setEnabled(false);
		          buttonUnique.setEnabled(false);
			}
		});
		
		radio4.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		          buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
		          textFrom.setEnabled(true);
		          textTo.setEnabled(true);
		          buttonOptional.setEnabled(false);
		          buttonUnique.setEnabled(false);
			}
		});
		
		radio5.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		          buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
		          textFrom.setEnabled(true);
		          textTo.setEnabled(true);
		          buttonOptional.setEnabled(false);
		          buttonUnique.setEnabled(true);
			}
		});
		
		buttonCardinality.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		        if ((dialogCard == null)||(dialogCard.isDisposed())) dialogCard = new DialogAgregate(panelAgregate, ((Agregate)object).getNext(), prop);
		        if (dialogCard.object == null) {
		        	dialogCard.object = new Agregate(((Agregate)object).getAgregation_type(), null, "", Agregate.TYPE_SIMPLE, Agregate.BOUND_NONE, "",  Agregate.BOUND_NONE, "", false, false);
		        }
		        dialogCard.open();
			}
		});
		
		// END EVENT_INITIALIZATION
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
	    buttonOptional.setSelection(((Agregate)object).isOptional());
	    buttonUnique.setSelection(((Agregate)object).isUnique());
	    if (((Agregate)object).getMinBound() == Agregate.BOUND_NONE) textFrom.setText("?");
	    	else textFrom.setText(String.valueOf(((Agregate)object).getMinBound()));
	    if (((Agregate)object).getMaxBound() == Agregate.BOUND_NONE) textTo.setText("?");
	    	else textTo.setText(String.valueOf(((Agregate)object).getMaxBound()));
	    switch (((Agregate)object).getType()) {
	      	case Agregate.TYPE_ARRAY :
	      		radio2.setSelection(true);
	      		buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
	      		textFrom.setEnabled(true);
	      		textTo.setEnabled(true);
	      		buttonOptional.setEnabled(true);
	      		buttonUnique.setEnabled(true);
	      		break;
	      	case Agregate.TYPE_BAG :
	      		radio4.setSelection(true);
	      		buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
	      		textFrom.setEnabled(true);
	      		textTo.setEnabled(true);
	      		buttonOptional.setEnabled(false);
	      		buttonUnique.setEnabled(false);
	      		break;
	      	case Agregate.TYPE_LIST :
	      		radio5.setSelection(true);
	      		buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
	      		textFrom.setEnabled(true);
	      		textTo.setEnabled(true);
	      		buttonOptional.setEnabled(false);
	      		buttonUnique.setEnabled(true);
	      		break;
	      	case Agregate.TYPE_SET :
	      		radio3.setSelection(true);
	      		buttonCardinality.setEnabled(((Agregate)object).getAgregation_type() != null);
	      		textFrom.setEnabled(true);
	      		textTo.setEnabled(true);
	      		buttonOptional.setEnabled(false);
	      		buttonUnique.setEnabled(false);
	      		break;
	      	default :
	      		radio1.setSelection(true);
	      		buttonCardinality.setEnabled(false);
	      		textFrom.setEnabled(false);
	      		textTo.setEnabled(false);
	      		buttonOptional.setEnabled(false);
	      		buttonUnique.setEnabled(false);
	      		break;
	    }
	    if (((Agregate)object).getAgregation_type() == null) {
	    	radio2.setEnabled(false);
	    	radio5.setEnabled(false);
			if ((prop.getEditMode() & PropertySharing.MODE_EDIT) == 0) setEnabledName(true);
	    } else
			setEnabledName(false);
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
		textTo.setEditable(edit);
		radio1.setEnabled(edit);
//		panelAgregate.setEnabled(edit);
		buttonUnique.setEnabled(edit);
		radio4.setEnabled(edit);
//		labelFT.setEnabled(edit);
		radio2.setEnabled(edit);
//		buttonCardinality.setEnabled(edit);
		textFrom.setEditable(edit);
		radio3.setEnabled(edit);
		buttonOptional.setEnabled(edit);
		radio5.setEnabled(edit);
//		groupType.setEnabled(edit);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
	    ((Agregate)object).setOptional(buttonOptional.getSelection());
	    ((Agregate)object).setUnique(buttonUnique.getSelection());
	    int minBound = Agregate.BOUND_NONE;
	    String minBoundStr = "";
	    try { minBound = Integer.parseInt(textFrom.getText()); } catch (NumberFormatException ex) {}
	    ((Agregate)object).setMinBound(minBound, minBoundStr);
	    int maxBound = Agregate.BOUND_NONE;
	    String maxBoundStr = "";
	    try { maxBound = Integer.parseInt(textTo.getText()); } catch (NumberFormatException ex) {}
	    ((Agregate)object).setMaxBound(maxBound, maxBoundStr);
	    if (radio1.getSelection()) {
	      ((Agregate)object).setType(Agregate.TYPE_SIMPLE);
	      ((Agregate)object).setNext(null);
	    } else {
	      Agregate next = ((Agregate)object).getNext();
	      if (dialogCard == null) {
	        if (next == null) next = new Agregate(((Agregate)object).getAgregation_type(), null, "", Agregate.TYPE_SIMPLE, Agregate.BOUND_NONE, "", Agregate.BOUND_NONE, "", false, false);
	      } else {
	        next = (Agregate)dialogCard.object;
	      }
	      ((Agregate)object).setNext(next);
	      if (radio2.getSelection()) ((Agregate)object).setType(Agregate.TYPE_ARRAY); else
	      if (radio4.getSelection()) ((Agregate)object).setType(Agregate.TYPE_BAG); else
	      if (radio5.getSelection()) ((Agregate)object).setType(Agregate.TYPE_LIST); else
	      	((Agregate)object).setType(Agregate.TYPE_SET);
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
