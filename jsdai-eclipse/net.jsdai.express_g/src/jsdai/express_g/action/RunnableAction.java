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

import java.lang.reflect.InvocationTargetException;

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.action.runnable.AbstractRunnable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;


/**
 * Based on Action and can be added to all eclipse gui elements buttons, menus, etc.
 * This action doesn't do anything by itself, it invokes provided Runnable methods 
 * 
 * Basicaly - this class allows to add standard Runnable methods to eclipse Action
 * 
 * @author Mantas Balnys
 *
 */
public class RunnableAction extends Action {
	private AbstractRunnable runnable = null;

	/**
	 * 
	 */
	public RunnableAction() {
		super();
	}

	/**
	 * @param text
	 */
	public RunnableAction(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param image
	 */
	public RunnableAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * @param text
	 * @param style
	 */
	public RunnableAction(String text, int style) {
		super(text, style);
	}

	/**
	 * 
	 */
	public RunnableAction(AbstractRunnable runnable) {
		this();
		setRunnable(runnable);
	}

	/**
	 * @param text
	 */
	public RunnableAction(String text, AbstractRunnable runnable) {
		this(text);
		setRunnable(runnable);
	}

	/**
	 * @param text
	 * @param image
	 */
	public RunnableAction(String text, ImageDescriptor image, AbstractRunnable runnable) {
		this(text, image);
		setRunnable(runnable);
	}

	/**
	 * @param text
	 * @param style
	 */
	public RunnableAction(String text, int style, AbstractRunnable runnable) {
		this(text, style);
		setRunnable(runnable);
	}

	/**
	 * sets Runnable object
	 * @param runnable
	 */
	public void setRunnable(AbstractRunnable runnable) {
		this.runnable = runnable;
		updateVisibility();
	}
	
	/**
	 * @return Runnable object, shouldn't be null at run time
	 */
	public AbstractRunnable getRunnable() {
		return runnable;
	}

	/**
	 * starts Runnable with progress monitor
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		try {
			new ProgressMonitorDialog(getRunnable().getShell()).run(false, false, getRunnable());
		} catch (InvocationTargetException ite) {
			SdaieditPlugin.log(ite);
			SdaieditPlugin.console(ite.toString());
		} catch (InterruptedException ie) {
			SdaieditPlugin.log(ie);
			SdaieditPlugin.console(ie.toString());
		}
	}
	
	/**
	 * updates Action visibility
	 * @return
	 */
	public boolean updateVisibility() {
		boolean visible = true;
		if (runnable == null) {
			visible = false;
		} else {
			visible = runnable.acceptsCurrentSelection();
		}
		setEnabled(visible);
		return visible;
	}
}
