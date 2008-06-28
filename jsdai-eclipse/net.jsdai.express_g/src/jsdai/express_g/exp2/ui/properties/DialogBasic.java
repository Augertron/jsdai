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

import jsdai.express_g.exp2.Named;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

public class DialogBasic extends Dialog {
	private String title = null;
	private Image titleImage = null;
	
	// START VISUALS_DECLARATION
	static ColorDialog colorDialog = null;

	protected Composite panelRoot = null;
	protected Label labelName = null;
	protected Text textName = null;
	protected Composite panelName = null;
	protected Button buttonColor = null;
	// END VISUALS_DECLARATION
	private Composite panelExtend = null;
	private FormData dataExtend = null;

	
	protected Named object = null;
	protected PropertySharing prop;

	public DialogBasic(Composite parent, Named object, PropertySharing prop) {
		super(parent.getShell());
		this.object = object;
		this.prop = prop;
		setTitle("Properties");
 	}
	
	/**
	 * override for extension point
	 * @return parent composite
	 */
	protected Composite getExtend() {
		dataExtend.height = -1;
		return panelExtend;
	}
	
	protected void uiInit() {
		// START VISUALS_INITIALIZATION
		// init visuals
		panelName = new Composite(panelRoot, SWT.NULL);
		labelName = new Label(panelName, SWT.CENTER);
		textName = new Text(panelName, SWT.BORDER);
		buttonColor = new Button(panelName, SWT.PUSH);
		panelExtend = new Composite(panelRoot, SWT.NULL);
		
		// init nonviusuals
		FormData FormData_3 = new FormData();
		FormData FormData_4 = new FormData();
		FormData FormData_5 = new FormData();
		FormData FormData_1 = new FormData();
		dataExtend = new FormData();
		
		// set fields
		FormData_3.top = new FormAttachment(textName, 0, 16777216);
		FormData_3.left = new FormAttachment(0, 0);
		FormData_4.right = new FormAttachment(buttonColor, 0, 0);
		FormData_4.top = new FormAttachment(0, 0);
		FormData_4.left = new FormAttachment(labelName, 0, 0);
		FormData_4.bottom = new FormAttachment(100, 0);
		FormData_5.right = new FormAttachment(100, 0);
		FormData_5.top = new FormAttachment(0, 0);
		FormData_5.bottom = new FormAttachment(100, 0);
		FormData_1.right = new FormAttachment(100, 0);
		FormData_1.top = new FormAttachment(0, 0);
		FormData_1.left = new FormAttachment(0, 0);
		dataExtend.right = new FormAttachment(100, 0);
		dataExtend.top = new FormAttachment(panelName, 0, 0);
		dataExtend.left = new FormAttachment(0, 0);
		dataExtend.bottom = new FormAttachment(100, 0);
		dataExtend.height = 0;
		
		// set properties
		panelName.setLayoutData(FormData_1);
		labelName.setLayoutData(FormData_3);
		labelName.setText("Name:");
		textName.setLayoutData(FormData_4);
		buttonColor.setLayoutData(FormData_5);
		buttonColor.setText("color");
		panelExtend.setLayoutData(dataExtend);
		panelName.setLayout(new FormLayout());
		panelRoot.setLayout(new FormLayout());
		// END VISUALS_INITIALIZATION

		// START EVENT_INITIALIZATION		
		buttonColor.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				if (colorDialog == null) colorDialog = new ColorDialog(getParentShell(), SWT.DEFAULT);
				colorDialog.setRGB(buttonColor.getBackground().getRGB());
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					buttonColor.setBackground(new Color(e.display, rgb));
					buttonColor.setForeground(new Color(e.display, rgb));
					buttonColor.redraw();
					buttonColor.update();
				}
			}
		});
		//FIX
	    buttonColor.setEnabled(false);

		// END EVENT_INITIALIZATION
	}

	protected Control createDialogArea(Composite parent) {
		panelRoot = (Composite)super.createDialogArea(parent);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		panelRoot.setLayoutData(gridData);
//		panelRoot.setBackground(ColorSchema.COLOR_RED);
		uiInit();
	    updateDialog();
		return panelRoot;
	}

	protected void updateDialog() {
	    textName.setText(object.getName());
/* FIX	    if (object instanceof AbstractEGBox) {
	    	Color color = ((AbstractDraw)object).simpleSchema.background;
		    buttonColor.setBackground(color);
	    } else if (object instanceof AbstractEGRelation) {
	    	Color color = ((AbstractDraw)object).simpleSchema.foreground;
		    buttonColor.setBackground(color);
	    } else {
	    	buttonColor.setVisible(false);
	    }*/
	    if ((prop.getEditMode() & PropertySharing.MODE_EDIT) != 0) {
	    	setEnabledName(true);
	    } else {
	    	setEnabledName(false);
	    }
	}

	protected void updateObject() {
		if (!object.getName().equals(textName.getText()))
			object.setName(textName.getText());
/* FIX	    if (object instanceof AbstractEGBox) {
	    	((AbstractDraw)object).simpleSchema.background = buttonColor.getBackground();
	    	((AbstractDraw)object).selectedSchema.background = EGToolKit.darker(((AbstractDraw)object).simpleSchema.background);
	    } else if (object instanceof AbstractEGRelation) {
	    	((AbstractEGRelation)object).simpleSchema.foreground = 
	    	((AbstractEGRelation)object).simpleSchema2.foreground = 
	    	((AbstractEGRelation)object).simpleSchemaOpt.foreground = buttonColor.getBackground();
	    	((AbstractEGRelation)object).selectedSchema.foreground = 
	    	((AbstractEGRelation)object).selectedSchema2.foreground = 
	    	((AbstractEGRelation)object).selectedSchemaOpt.foreground = EGToolKit.darker(((AbstractDraw)object).simpleSchema.foreground);
	    } else if (object instanceof AbstractDraw) {
	    	((AbstractDraw)object).simpleSchema.background = buttonColor.getBackground();
	    	((AbstractDraw)object).selectedSchema.background = EGToolKit.darker(((AbstractDraw)object).simpleSchema.background);
	    }*/
	}
	  

    protected void okPressed() {
		updateObject();
		super.okPressed();
	}
    
    /**
     * returns true, only and only if the main panel was disposed
     * @return
     */
    public boolean isDisposed() {
    	return (panelRoot != null)&&(panelRoot.isDisposed());
    }
    
    protected void setEnabledName(boolean enabled) {
//		labelName.setEnabled(enabled);
//		textName.setEnabled(enabled);
    	textName.setEditable(enabled);
    }
    
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null)
			shell.setText(title);
		if (titleImage != null)
			shell.setImage(titleImage);
	}
	
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return Returns the titleImage.
	 */
	public Image getTitleImage() {
		return titleImage;
	}
	
	/**
	 * @param titleImage The titleImage to set.
	 */
	public void setTitleImage(Image titleImage) {
		this.titleImage = titleImage;
	}
}
