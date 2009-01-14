
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
