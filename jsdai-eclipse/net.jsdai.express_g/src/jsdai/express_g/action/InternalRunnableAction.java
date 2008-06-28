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

import jsdai.express_g.action.runnable.AbstractRunnable;
import jsdai.express_g.action.runnable.ApplicationRunnable;
import jsdai.express_g.exp2.ui.PropertySharing;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Extends RunnableAction to work with opened Diagram
 * 
 * @author Mantas Balnys
 *
 */
public class InternalRunnableAction extends RunnableAction {
	private PropertySharing prop = null;

	/**
	 * 
	 */
	public InternalRunnableAction() {
		super();
	}

	/**
	 * @param text
	 */
	public InternalRunnableAction(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param image
	 */
	public InternalRunnableAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * @param text
	 * @param style
	 */
	public InternalRunnableAction(String text, int style) {
		super(text, style);
	}

	/**
	 * @param runnable
	 */
	public InternalRunnableAction(AbstractRunnable runnable) {
		super(runnable);
	}

	/**
	 * @param text
	 * @param runnable
	 */
	public InternalRunnableAction(String text, AbstractRunnable runnable) {
		super(text, runnable);
	}

	/**
	 * @param text
	 * @param image
	 * @param runnable
	 */
	public InternalRunnableAction(String text, ImageDescriptor image,
			AbstractRunnable runnable) {
		super(text, image, runnable);
	}

	/**
	 * @param text
	 * @param style
	 * @param runnable
	 */
	public InternalRunnableAction(String text, int style,
			AbstractRunnable runnable) {
		super(text, style, runnable);
	}

	/**
	 * sets up Diagram data and starts Runnable with progress monitor
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		AbstractRunnable runnable = getRunnable();
		ApplicationRunnable apprun = null; 
		if (runnable instanceof ApplicationRunnable)
			apprun = (ApplicationRunnable)getRunnable();
		if (apprun != null) {
			apprun.setProp(prop);
		}
		super.run();
	}
	
	public PropertySharing getProp() {
		return prop;
	}
	
	public void setProp(PropertySharing prop) {
		this.prop = prop;
		updateVisibility();
	}
	
	/**
	 * updates Action visibility
	 * @return
	 */
	public boolean updateVisibility() {
		boolean visible = super.updateVisibility();
		if (visible && prop == null) {
			visible = false;
			setEnabled(visible);
		}
		return visible;
	}
}
