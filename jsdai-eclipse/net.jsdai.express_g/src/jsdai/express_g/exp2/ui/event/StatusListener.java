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

package jsdai.express_g.exp2.ui.event;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public interface StatusListener {
  /**
   * set status string
   * @param text String
   */
  public void setStatus(String text);

  /**
   * set job done status
   * @param job parts done.
   * @see IProgressMonitor
   */
  public void setStatus(int job);

  /**
   * set progress monitor
   * pass null to unset
   * @param progress
   */
  public void setProgressMonitor(IProgressMonitor progress);
  
  /**
   * get active progress monitor
   * can be null if thre is no active progress monitor
   * @return
   */
  public IProgressMonitor getProgressMonitor();
  
  /**
   * 
   * @param name task name
   * @param total total job units
   * @see IProgressMonitor
   */
  public void setStatus(String name, int total);
  
  /**
   * should print message to console
   * @param message
   */
  public void log(String message);
  
  public void log(Throwable t);

}
