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
package jsdai.express_compiler.utils;


import jsdai.expressCompiler.ECMonitor;

//    public static final class ECMonitorImpl implements ECMonitor {
    public class ECMonitorImpl implements ECMonitor {
      String message;
      private Thread parentThread;
      
      public ECMonitorImpl(Thread parentThread) {
        this.message = null;
        this.parentThread = parentThread;
      }

      public void subTask(String message) {
        synchronized (this) {
          this.message = message;
        }
        parentThread.interrupt();
      }

      public void worked(int value) {
      }
    
    	public String getMessage() {
    		return message;
    	}
    
    }
