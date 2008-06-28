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

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.eg.AbstractEGRelation;
import jsdai.express_g.exp2.eg.Agregate;
import jsdai.express_g.exp2.eg.EGDefined;
import jsdai.express_g.exp2.eg.EGEntity;
import jsdai.express_g.exp2.eg.EGRelationSimple;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.properties.DialogAgregate;

/**
 * @author Mantas Balnys
 *
 */
public class DialogRelationSimple extends DialogBasic {
	// START VISUALS_DECLARATION
	protected Composite panelSimple = null;
	protected Button buttonDerive = null;
	protected Composite parent = null;
	protected Button buttonCardinality = null;
	protected Button buttonCardinalityI = null;
	protected Composite panelInverse = null;
	protected Button buttonOptional = null;
	protected Button buttonInverse = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	protected DialogAgregate dialogCard = null;
	protected DialogAgregate dialogCardInverse = null;

	/**
	 * @param parent
	 * @param object
	 */
	public DialogRelationSimple(Composite parent, Named object, PropertySharing prop) {
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
		parent = super.getExtend();
		panelSimple = new Composite(parent, SWT.NULL);
		buttonOptional = new Button(panelSimple, SWT.CHECK);
		buttonDerive = new Button(panelSimple, SWT.CHECK);
		buttonCardinality = new Button(panelSimple, SWT.PUSH);
		panelExtend = new Composite(parent, SWT.NULL);
		panelInverse = new Composite(parent, SWT.NULL);
		buttonInverse = new Button(panelInverse, SWT.CHECK);
		buttonCardinalityI = new Button(panelInverse, SWT.PUSH);
		
		// init nonviusuals
		FormData FormData_10 = new FormData();
		FormData FormData_11 = new FormData();
		FormData FormData_12 = new FormData();
		FormData FormData_1 = new FormData();
		dataExtend = new FormData();
		FormData FormData_4 = new FormData();
		FormData FormData_5 = new FormData();
		FormData FormData_3 = new FormData();
		
		// set fields
		FormData_10.right = new FormAttachment(100, 0);
		FormData_10.top = new FormAttachment(0, 0);
		FormData_10.left = new FormAttachment(0, 0);
		FormData_11.right = new FormAttachment(100, 0);
		FormData_11.top = new FormAttachment(buttonOptional, 0, 0);
		FormData_11.left = new FormAttachment(0, 0);
		FormData_12.right = new FormAttachment(100, 0);
		FormData_12.top = new FormAttachment(buttonDerive, 0, 0);
		FormData_12.left = new FormAttachment(0, 0);
		FormData_12.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelInverse, 0, 0);
		dataExtend.height = 0;
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		FormData_4.right = new FormAttachment(100, 0);
		FormData_4.top = new FormAttachment(0, 0);
		FormData_4.left = new FormAttachment(0, 0);
		FormData_5.right = new FormAttachment(100, 0);
		FormData_5.top = new FormAttachment(buttonInverse, 0, 0);
		FormData_5.left = new FormAttachment(0, 0);
		FormData_5.bottom = new FormAttachment(100, 0);
		FormData_3.right = new FormAttachment(100, 0);
		FormData_3.top = new FormAttachment(panelSimple, 0, 0);
		FormData_3.left = new FormAttachment(0, 0);
		
		// set properties
		panelSimple.setLayoutData(FormData_1);
		buttonOptional.setLayoutData(FormData_10);
		buttonOptional.setText("Optional");
		buttonDerive.setLayoutData(FormData_11);
		buttonDerive.setText("Derive");
		buttonCardinality.setLayoutData(FormData_12);
		buttonCardinality.setText("Cardinality");
		panelExtend.setLayoutData(dataExtend);
		panelInverse.setLayoutData(FormData_3);
		buttonInverse.setLayoutData(FormData_4);
		buttonInverse.setText("Inverse");
		buttonCardinalityI.setLayoutData(FormData_5);
		buttonCardinalityI.setText("Cardinality");
		panelInverse.setLayout(new FormLayout());
		panelSimple.setLayout(new FormLayout());
		parent.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		buttonDerive.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		        buttonOptional.setEnabled(!buttonDerive.getSelection());
//		        jButtonCard.setEnabled(!jCheckBoxDerive.isSelected());
		        buttonInverse.setEnabled(!buttonDerive.getSelection() && (((AbstractEGRelation)object).getChild() instanceof EGEntity));
		        buttonCardinalityI.setEnabled(!buttonDerive.getSelection() && buttonInverse.getSelection());
			}
		});
		
		buttonCardinality.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		        if ((dialogCard == null)||(dialogCard.isDisposed())) dialogCard = new DialogAgregate(panelSimple, ((EGRelationSimple)object).getAgregate(), prop);
		        dialogCard.open();
			}
		});
		
		buttonInverse.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		        buttonCardinalityI.setEnabled(buttonInverse.getSelection());
			}
		});
		
		buttonCardinalityI.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
		        if ((dialogCard == null)||(dialogCard.isDisposed())) dialogCardInverse = new DialogAgregate(panelInverse, ((EGRelationSimple)object).getInverse(), prop);
		        if (dialogCardInverse.object == null) {
		        	dialogCardInverse.object = new Agregate(null, null, "", Agregate.TYPE_SIMPLE,
		              Agregate.BOUND_NONE, Agregate.BOUND_NONE, false, false);
		        }
		        dialogCardInverse.open();
			}
		});
		
		// END EVENT_INITIALIZATION
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateDialog()
	 */
	protected void updateDialog() {
		super.updateDialog();
		boolean inverse = false;
	    if (object instanceof EGRelationSimple) {
	    	buttonOptional.setSelection(((EGRelationSimple)object).isOptional());
		    inverse = ((EGRelationSimple)object).getInverse() != null;
	    }
	    buttonInverse.setSelection(inverse);
	    if ((((AbstractEGRelation)object).getType() == AbstractEGRelation.TYPE_AGREGATION)&&
	    		((((AbstractEGRelation)object).getParent() instanceof EGEntity) ||
				(((AbstractEGRelation)object).getParent() instanceof EGDefined))) {
//	System.out.println("TRUE");
			setEnabledName((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);

	    	buttonDerive.setEnabled(true);
	    	buttonCardinality.setEnabled(true);
	    	buttonInverse.setEnabled(true);

	    	buttonDerive.setSelection(((EGRelationSimple)object).isDerived());
	        buttonOptional.setEnabled(!buttonDerive.getSelection());
	        buttonInverse.setEnabled(!buttonDerive.getSelection() && (((AbstractEGRelation)object).getChild() instanceof EGEntity));
	        buttonCardinalityI.setEnabled(!buttonDerive.getSelection() && buttonInverse.getSelection());
	    } else {
//	System.out.println("FALSE");
			setEnabledName(false);

	    	buttonDerive.setEnabled(false);
	    	buttonCardinality.setEnabled(false);
	    	buttonOptional.setEnabled(false);
	    	buttonInverse.setEnabled(false);
	    	buttonCardinalityI.setEnabled(false);

	    }
	    setEnabledEdit((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0);
	}
	
	private void setEnabledEdit(boolean edit) {
//		panelSimple.setEnabled(edit);
		buttonDerive.setEnabled(edit);
//		parent.setEnabled(edit);
//		buttonCardinality.setEnabled(edit);
//		buttonCardinalityI.setEnabled(edit);
//		panelInverse.setEnabled(edit);
		buttonOptional.setEnabled(edit);
		buttonInverse.setEnabled(edit);
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.DialogBasic#updateObject()
	 */
	protected void updateObject() {
		super.updateObject();
		if (object instanceof EGRelationSimple) {
			boolean derive = buttonDerive.getSelection() && buttonDerive.isEnabled();
			((EGRelationSimple)object).setDerived(derive);
			if (derive) {
				buttonOptional.setSelection(false);
				buttonInverse.setSelection(false);
			}
			((EGRelationSimple)object).setOptional(buttonOptional.getSelection());
			if (buttonInverse.getSelection()) {
				Agregate inverse = ((EGRelationSimple)object).getInverse();
				if (dialogCardInverse == null) {
					if (inverse == null) inverse = new Agregate(null, null, "", Agregate.TYPE_SIMPLE, Agregate.BOUND_NONE, Agregate.BOUND_NONE, false, false);
				} else {
					inverse = (Agregate)dialogCardInverse.object;
				}
				((EGRelationSimple)object).setInverse(inverse);
			} else {
				((EGRelationSimple)object).setInverse(null);
			}
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
