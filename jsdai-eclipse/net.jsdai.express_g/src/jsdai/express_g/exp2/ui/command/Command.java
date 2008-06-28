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
 * @(#) Command.java
 */

public interface Command {
  public void init(PropertySharing prop);

  public boolean interrupt();

  public void start();

  public boolean stop();

  public String getStatus();

  public CommandInvoker getInvoker();

  public void finalizeCommand();

  public static final int NORMAL = 0;
  public static final int INTERUPTED = -1;
  public static final int STOPPED = -2;

  public void setExitCode(int exit);

  public int getExitCode();
  
  public Cursor getCursor();
}
