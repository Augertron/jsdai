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

import jsdai.express_g.common.Resources;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

/**
 * @author Mantas Balnys
 *
 */
public class RepositoryHandlerInput implements IEditorInput, IPersistableElement {
	private RepositoryHandler repositoryHandler = null;
	private ModelHandler modelHandler = null;
	private boolean readonly;
	
	/**
	 * 
	 */
	public RepositoryHandlerInput(RepositoryHandler repositoryHandler, boolean readonly) {
		this.repositoryHandler = repositoryHandler;
		this.readonly = readonly;
	}
	
	/**
	 * 
	 */
	public RepositoryHandlerInput(ModelHandler modelHandler, boolean readonly) {
		this(modelHandler.getRepositoryHandler(), readonly);
		this.modelHandler = modelHandler;
	}
	
	public boolean isReadonly() {
		return readonly;
	}
	
	public RepositoryHandler getRepositoryHandler() {
		return repositoryHandler;
	}
	
	public ModelHandler getModelHandler() {
		return modelHandler;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return (repositoryHandler.getRepository() != null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return Resources.getImageDescriptor(Resources.JSDAI);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		if (modelHandler != null) return modelHandler.getName();
		else return repositoryHandler.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return this;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return repositoryHandler.getRepoPath().toOSString() + " - " 
		+ repositoryHandler.getRepository().toString() + 
		(modelHandler == null ? "" : " - " + modelHandler.getName());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPersistableElement#getFactoryId()
	 */
	public String getFactoryId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPersistableElement#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(IPersistableElement.class))
			return this;
		return null;
	}

}
