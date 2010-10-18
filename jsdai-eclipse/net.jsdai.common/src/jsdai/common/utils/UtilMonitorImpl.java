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
package jsdai.common.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jsdai.util.UtilMonitor;

public final class UtilMonitorImpl implements UtilMonitor {
  String message;
  int count;
  int counter;
  int step = 1;
  // BlockingQueue queue;
  List queue;
  private Thread parentThread;
      
  public UtilMonitorImpl(Thread parentThread) {
  	this.message = null;
    this.count = -1;
    this.counter = -1;
//    this.queue = new LinkedBlockingQueue();
    // this.queue = new LinkedList();
    // synrchonizing here is probably not needed
 		this.queue = Collections.synchronizedList(new LinkedList());
    this.parentThread = parentThread;
  }

//	public void subTask(String message, long count, long counter, BlockingQueue queue) {

  /*
  public void subTask(String message, long count, long counter, List queue) {
		synchronized (this) {
			this.message = message;
			this.queue = queue;
      // progress monitor in eclipse allows only int values, so we have to convert long to int and adjust the step of the progress bar 
      if (count > Integer.MAX_VALUE) {
      	step = (int)(count/Integer.MAX_VALUE);
        if (count%Integer.MAX_VALUE > 0) {
        	step++;
        }
        this.count = (int)count/step;
        this.counter = (int)counter/step;
			} else {
				this.count = (int)count;
				this.counter = (int)counter;
			}
		}
		parentThread.interrupt();
	}
*/
	public void worked(int value) {
  }

/*	
	public void passQueue(List queue) {
		synchronized (this) {
			this.queue = queue;
		}
		parentThread.interrupt();
	}
  
*/

	public void subTask(String message, long count, long counter) {
		synchronized (this) {
			this.message = message;

      // progress monitor in eclipse allows only int values, so we have to convert long to int and adjust the step of the progress bar 
      if (count > Integer.MAX_VALUE) {
      	step = (int)(count/Integer.MAX_VALUE);
        if (count%Integer.MAX_VALUE > 0) {
        	step++;
        }
        this.count = (int)count/step;
        this.counter = (int)counter/step;
			} else {
				this.count = (int)count;
				this.counter = (int)counter;
			}
		}
		parentThread.interrupt();
	}


	
	public String getMessage() {
  	return message;
  }

	public int getCount() {
  	return count;
  }

	public int getCounter() {
  	return counter;
  }
    
//	public BlockingQueue getQueue() {
	public List getQueue() {
		return queue;
	}
}

