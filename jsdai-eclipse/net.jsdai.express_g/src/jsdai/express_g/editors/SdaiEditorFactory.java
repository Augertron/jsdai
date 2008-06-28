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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;


/**
 * @author Mantas Balnys
 *
 */
public class SdaiEditorFactory implements IElementFactory {
	public static final String ID = "net.jsdai.express_g.editors.SdaiEditorFactory";

	private static final String TAG_PATH = "path";
	private static final String TAG_READONLY = "readonly";

	/**
	 * 
	 */
	public SdaiEditorFactory() {
		super();
	}

	public IAdaptable createElement(IMemento memento) {
		// Get the file name.
		String fileName = memento.getString(TAG_PATH);
		if (fileName == null)
			return null;

		// Get a handle to the IFile...which can be a handle
		// to a resource that does not exist in workspace
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(fileName));
		if (file != null) {
			Integer ro = memento.getInteger(TAG_READONLY);
			boolean readonly = (ro == null || ro.intValue() != 0);
			
			return new SdaiEditorInput(file, readonly);
		} else
			return null;
	}
	
	public static void saveState(IMemento memento, SdaiEditorInput input) {
		memento.putString(TAG_PATH, input.getFile().getFullPath().toOSString());
		memento.putInteger(TAG_READONLY, input.isReadonly() ? 1 : 0);
	}
}
