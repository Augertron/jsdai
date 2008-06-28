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

import jsdai.express_g.exp2.eg.AbstractEGBox;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * @(#) NewBoxCommand.java
 */

public class NewBoxCommand extends CommandAdapter {
  private AbstractEGBox box;
  private boolean multiple = false;
  private Class boxClass = null;

  public NewBoxCommand(CommandInvoker invoker, AbstractEGBox box) {
    super(invoker);
    this.box = box;
  }

  public NewBoxCommand(CommandInvoker invoker, Class boxClass, boolean multiple) {
    super(invoker);
    this.multiple = multiple;
    this.boxClass = boxClass;
    try {
      this.box = (AbstractEGBox)boxClass.newInstance();
    }
    catch (InstantiationException ex) {
        prop.status().log(ex);
        stop();
      }
    catch (IllegalAccessException ex) {
      prop.status().log(ex);
      stop();
    }
  }

  public String getStatus() {
    return "Creating " + box.getUIName();
  }

  /**
   * stop
   *
   * @return boolean
   * @todo Implement this jsdai.paint.ui.Command method
   */
  public boolean stop() {
    exitCode = STOPPED;
    prop.handler().commandDone();
    return true;
  }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
	    box.setLocation(new Point(e.x, e.y));
	    box.setPage(prop.handler().getPage());
	    prop.handler().drawableAdd(box);
	    box.draw(prop.getPainting().getLastGraphics());
	    if (multiple) {
	      try {
	        this.box = (AbstractEGBox)boxClass.newInstance();
	      }
	      catch (InstantiationException ex) {
	          prop.status().log(ex);
	          stop();
	        }
	      catch (IllegalAccessException ex) {
	        prop.status().log(ex);
	        stop();
	      }
	    } else prop.handler().commandDone();
	    prop.handler().repaint(false);
	}
}
