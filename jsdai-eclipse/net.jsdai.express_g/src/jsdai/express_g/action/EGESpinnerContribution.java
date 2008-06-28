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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.PageChangeEvent;
import jsdai.express_g.exp2.ui.event.PageListener;

/**
 * SpinnerContribution is specialized to work with opened Diagram
 * 
 * @author Mantas Balnys
 *
 */
public class EGESpinnerContribution extends SpinnerContribution implements
		PageListener, SelectionListener {
	private PropertySharing prop = null;

	/**
	 * @param id
	 */
	public EGESpinnerContribution(String id) {
		super(id);
	}

	public void setProperties(PropertySharing prop) {
		if ((spinner != null) && !spinner.isDisposed()) {
			if (prop == null) {
				spinner.setMinimum(0);
				spinner.setMaximum(0);
				spinner.setSelection(0);
				if (this.prop != null) this.prop.handler().removePageListener(this);
			} else {
				spinner.setMinimum(1);
				spinner.setMaximum(prop.handler().getMaxPage() + 1);
				spinner.setSelection(prop.handler().getPage());
				prop.handler().addPageListener(this);
			}
		}
		this.prop = prop;
	}
	
	public PropertySharing getProperties() {
		return prop;
	}
	
	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.PageListener#pageChanged(jsdai.express_g.exp2.ui.event.PageChangeEvent)
	 */
	public void pageChanged(PageChangeEvent e) {
		if (e.getSource() != this) {
			spinner.setSelection(e.getNewPage());
			spinner.setMaximum(prop.handler().getMaxPage() + 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (prop != null) { 
			prop.handler().setPage(spinner.getSelection());
			spinner.setMaximum(prop.handler().getMaxPage() + 1);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ControlContribution#createControl(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite parent) {
		Control sp = super.createControl(parent);
		spinner.addSelectionListener(this);
		setProperties(prop);
		return sp;
	}
	
}
