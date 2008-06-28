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

package jsdai.express_g.editors.outline;

import jsdai.express_g.common.Resources;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiLabelProvider implements ILabelProvider {
	private ListenerList listeners = new ListenerList();
	
	/**
	 * 
	 */
	public SdaiLabelProvider() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			if (mh.getDiagram_definition() != null) {
				int edit_mode = 0;
				try {
					edit_mode = Integer.parseInt(mh.getProperties().
							getProperty(PropertySharing.PROP_EDITING_MODE, "0"));
				} catch (NumberFormatException nfe) {}
				if ((edit_mode & PropertySharing.MODE_LONGFORM_MASK) != 0) {
					if ((edit_mode & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0)
						return Resources.getImage(Resources.MODEL_LAYOUT_PARTIAL_LONG);
					else
						return Resources.getImage(Resources.MODEL_LAYOUT_COMPLETE_LONG);
				} else { 
					if ((edit_mode & PropertySharing.MODE_LAYOUT_PARTIAL_MASK) != 0)
						return Resources.getImage(Resources.MODEL_LAYOUT_PARTIAL_SHORT);
					else
						return Resources.getImage(Resources.MODEL_LAYOUT_COMPLETE_SHORT);
				}
			} else
			if (mh.getSchema_definition() != null) {
				return Resources.getImage(Resources.MODEL_SCHEMA);
			} else
				return Resources.getImage(Resources.MODEL_OTHER);
		} else
		if (element instanceof RepositoryHandler) {
			return Resources.getImage(Resources.EXG_FILE);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element == null) {
			return null;
		} else
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			return mh.getName();
		} else
		if (element instanceof RepositoryHandler) {
			RepositoryHandler rh = (RepositoryHandler)element;
			return rh.getRepository().toString();
		}
		return "<unknown> - " + element.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		listeners.clear();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		if (element instanceof ModelHandler) {
			return property.equalsIgnoreCase("name");
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

}
