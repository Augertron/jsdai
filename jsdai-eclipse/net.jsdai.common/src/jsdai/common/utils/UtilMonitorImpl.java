

package jsdai.common.utils;

import jsdai.util.UtilMonitor;

public final class UtilMonitorImpl implements UtilMonitor {
	String message;
  int count;
  int counter;
  int step = 1;
  private Thread parentThread;
      
  public UtilMonitorImpl(Thread parentThread) {
  	this.message = null;
    this.count = -1;
    this.counter = -1;
    this.parentThread = parentThread;
  }

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

	public void worked(int value) {
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

}

