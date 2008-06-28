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

package jsdai.express_g.exp2.ui.command;

import org.eclipse.swt.graphics.Cursor;

import jsdai.express_g.exp2.ui.PropertySharing;

/**
 * <p>Title: JSDAI Express-G</p>
 * <p>Description: </p>
 * @author Mantas Balnys
 * @version 1.0
 */

public abstract class AbstractCommand implements Command {
  protected PropertySharing prop = null;
  protected CommandInvoker invoker = null;
  protected int exitCode = 0;
  
  private Cursor cursor = null;

  public AbstractCommand(CommandInvoker invoker) {
    this.invoker = invoker;
  }

  public CommandInvoker getInvoker() {
    return invoker;
  }

  /**
   * init
   *
   * @param prop PropertySharing
   */
  public void init(PropertySharing prop) {
    this.prop = prop;
  }

  /**
   * interrupt
   *
   * @return boolean
   */
  public boolean interrupt() {
    return false;
  }

  /**
   * start
   */
  public void start() { }

  /**
   * stop
   *
   * @return boolean
   */
  public boolean stop() {
    return false;
  }

  /**
   * finalize
   *
   */
  public void finalizeCommand() {
//System.out.println("finalizing " + invoker);
  	setCursor(null);
    if (invoker != null) invoker.commandDone(this);
  }

  /**
   * getStatus
   *
   * @return String
   */
  public String getStatus() {
    return "Command";
  }

  public void setExitCode(int exit) {
    exitCode = exit;
  }

  public int getExitCode() {
    return exitCode;
  }
  
  public Cursor getCursor() {
  	return cursor;
  }

  public void setCursor(Cursor cursor) {
  	if (this.cursor != null) {
  		this.cursor.dispose();
  	}
  	this.cursor = cursor;
  	prop.handler().fireCursorChanged(cursor);
  }
}
