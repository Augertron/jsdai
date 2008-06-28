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

import java.util.Vector;

import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.lang.SdaiException;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Mantas Balnys
 *
 */
public class SdaiContentProvider implements ITreeContentProvider {
//	private RepositoryHandler rh = null;
	
	/**
	 * 
	 */
	public SdaiContentProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof RepositoryHandler) {
			RepositoryHandler rh = (RepositoryHandler)element; 
			String[] schemas = rh.getSchemas();
			String[] diagrams = rh.getSchemaDiagrams(null);
			Vector sh = new Vector();
			int j = 0;
			while (j < schemas.length) {
				while ((j < schemas.length) && (schemas[j] == null)) j++;
				if (j < schemas.length) {
					sh.add(rh.getModelHandler(schemas[j]));
					j++;
				}
			}
			j = 0;
			if (diagrams != null) while (j < diagrams.length) {
				if (diagrams[j] != null)
					sh.add(rh.getModelHandler(diagrams[j]));
				j++;
			}
			return sh.toArray();
		} else 
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			if ((mh.getDiagram_definition() != null) || (mh.getSchema_definition() == null)) {
				// layout nodes does not have children
				// unrecognized models does not have children
				return null;
			} else {
				RepositoryHandler rh = mh.getRepositoryHandler();
				String[] diagrams = rh.getDiagrams(mh.getName());
				Object[] diagramHandlers = new Object[diagrams.length];
				for (int i = 0; i < diagrams.length; i++)
					diagramHandlers[i] = rh.getModelHandler(diagrams[i]);
				return diagramHandlers;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			if ((mh.getDiagram_definition() != null) && (mh.getSchema_definition() != null)) {
				try { 
					String schemaName = mh.getSchema_definition().getName(null); 
					RepositoryHandler rh = mh.getRepositoryHandler();
					return rh.getModelHandler(schemaName);
				} catch (SdaiException sex) {
					return null;
				}
			} else {
				return mh.getRepositoryHandler();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof RepositoryHandler) {
			RepositoryHandler rh = (RepositoryHandler)element; 
			return rh.getSchemas().length > 0;
		} else 
		if (element instanceof ModelHandler) {
			ModelHandler mh = (ModelHandler)element;
			if ((mh.getDiagram_definition() != null) || (mh.getSchema_definition() == null)) {
				// layout nodes does not have children
				// unrecognized models does not have children
				return false;
			} else {
				RepositoryHandler rh = mh.getRepositoryHandler();
				return rh.getDiagrams(mh.getName()).length > 0;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SdaiEditorOutline) {
			return new Object[]{((SdaiEditorOutline)inputElement).getRepositoryHandler()};
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
