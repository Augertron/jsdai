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

package jsdai.express_g.action.runnable;

import java.lang.reflect.InvocationTargetException;

import jsdai.express_g.SdaieditPlugin;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

/**
 * Basic class, parent of all Runnable actions
 * 
 * @author Mantas Balnys
 *
 */
public abstract class AbstractRunnable implements IRunnableWithProgress, Runnable {
	/**
	 * 
	 */
	public AbstractRunnable() {
	}

	/**
	 * overrides interface java.lang.Runnable to start IRunnableWithProgress method
	 * so children classes will have to override only that one
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			run(null);
		} catch (InvocationTargetException ite) {
			SdaieditPlugin.log(ite);
			SdaieditPlugin.console(ite.toString());
		} catch (InterruptedException ie) {
			SdaieditPlugin.log(ie);
			SdaieditPlugin.console(ie.toString());
		}
	}

	/**
	 * associated Shell, can be null in some lower classes
	 * @return
	 */
	public Shell getShell() {
		return null;
	}
	
	/**
	 * if runnable does not accept selection it will not start
	 * @return
	 */
	public boolean acceptsCurrentSelection() {
		return true;
	}
}
