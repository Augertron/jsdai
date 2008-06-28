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

package jsdai.express_g.common;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Mantas Balnys
 *
 */
public class TableTreeContentProvider implements ITreeContentProvider {
	private TableTreeObject root = null;
	private boolean invisible_root = false;

	/**
	 * 
	 */
	public TableTreeContentProvider() {
	}

	/**
	 * 
	 */
	public TableTreeContentProvider(TableTreeObject root) {
		this.root = root;
	}
	
	public void setInvisibleRoot(boolean invisible_root) {
		this.invisible_root = invisible_root;
	}
	
	public boolean isInvisibleRoot() {
		return invisible_root;
	}
	
	public TableTreeObject setRoot(TableTreeObject root) {
		TableTreeObject oldRoot = this.root;
		this.root = root;
		return oldRoot;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof TableTreeObject) {
			return ((TableTreeObject)element).getChildren();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if (element instanceof TableTreeObject) {
			Object parent = ((TableTreeObject)element).getParent();
			if ((invisible_root)&&(parent == root)) parent = null;
			return parent;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof TableTreeObject) {
			return ((TableTreeObject)element).hasChildren();
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof TableTreeObject) {
			return getChildren(inputElement);
		} else {
			if (invisible_root) 
				return root.getChildren();
			else
				return new Object[]{root};
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		root.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
