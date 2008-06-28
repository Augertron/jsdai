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

package jsdai.express_g.action;

import jsdai.express_g.widgets.Spinner;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


/**
 * GUI item
 * combines text and spinner to one ControlContribution item
 * 
 * @author Mantas Balnys
 *
 */
public class SpinnerContribution extends ControlContribution {
	protected Spinner spinner;
	private Composite composite;
	private boolean visible = true;
	private boolean enabled = true;

	/**
	 * @param id
	 */
	public SpinnerContribution(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ControlContribution#createControl(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		Label label = new Label(composite, SWT.HORIZONTAL | SWT.SHADOW_NONE | SWT.RIGHT);
		label.setText("Page: ");
		composite.setLayout(new FillLayout());
		spinner = new Spinner(composite, SWT.NONE);
		composite.setVisible(visible);
		return composite;
	}
	
	public void setEnabled(boolean enabled) {
		if ((composite != null) && !composite.isDisposed()) composite.setEnabled(enabled);
		if ((spinner != null) && !spinner.isDisposed()) spinner.setEnabled(enabled);
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IContributionItem#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if ((composite != null) && !composite.isDisposed()) composite.setVisible(visible);
		this.visible = visible;
		super.setVisible(visible);
	}
	
	public Spinner getSpinner() {
		return spinner;
	}
	
}
