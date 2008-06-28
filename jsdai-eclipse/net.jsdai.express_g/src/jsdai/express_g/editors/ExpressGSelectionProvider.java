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

package jsdai.express_g.editors;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.exp2.ui.event.SelectionEvent;
import jsdai.express_g.exp2.ui.event.SelectionListener;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Mantas Balnys
 *
 */
public class ExpressGSelectionProvider implements ISelectionProvider, 
		SelectionListener {
	private ListenerList listeners = new ListenerList();
	private PropertySharing prop;
	
	/**
	 * 
	 */
	public ExpressGSelectionProvider(PropertySharing properties) {
		super();
		prop = properties;
		prop.getSelectionHandler().addSelectionListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		Collection sel = prop.getSelectionHandler().getSelected();
		StructuredSelection selection;
		selection = new StructuredSelection(sel.toArray());
		return selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			prop.getSelectionHandler().setSelected(this, 
					new HashSet(Arrays.asList(((IStructuredSelection)selection).toArray())));
		}
	}
	
	public void fireSelectionChanged() {
		Object[] listener = listeners.getListeners();
		SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		for (int i = 0; i < listener.length; i++) 
			((ISelectionChangedListener)listener[i]).selectionChanged(event);
	}

	/* (non-Javadoc)
	 * @see jsdai.express_g.exp2.ui.event.SelectionListener#selectionChanged(jsdai.express_g.exp2.ui.event.SelectionEvent)
	 */
	public void selectionChanged(SelectionEvent e) {
		fireSelectionChanged();
	}
}
