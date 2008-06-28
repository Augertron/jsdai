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

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * @author Mantas Balnys
 *
 */
public class PageChangeEvent {
	private MultiPageEditorPart source;
	private int page;
	private IEditorPart editor;
	private Control control;
	
	/**
	 * @param source
	 * @param page
	 * @param editor
	 * @param control
	 */
	public PageChangeEvent(MultiPageEditorPart source, int page, IEditorPart editor,	Control control) {
		this.source = source;
		this.page = page;
		this.editor = editor;
		this.control = control;
	}

	/**
	 * @return Returns the control.
	 */
	public Control getComposite() {
		return control;
	}
	
	/**
	 * @return Returns the editor.
	 */
	public IEditorPart getEditor() {
		return editor;
	}
	
	/**
	 * @return Returns the page.
	 */
	public int getPage() {
		return page;
	}
	
	/**
	 * @return Returns the source.
	 */
	public MultiPageEditorPart getSource() {
		return source;
	}
}
