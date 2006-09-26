package org.astrogrid.io.trace;

/**
 * Insert the type's description here.
 * Creation date: (18/01/00 13:56:10)
 * @author:
 */

public class TraceOutputStream extends java.io.FilterOutputStream  {
   private TraceWindow traceWindow = null;
   private boolean on = false;
   /**
    * TraceInputStream constructor comment.
    * @param in java.io.InputStream
    */
   public TraceOutputStream(java.io.OutputStream out) {
      super(out);
   }
   /**
    * Insert the method's description here.
    * Creation date: (18/01/00 14:01:27)
    * @return trace.TraceWindow
    */
   private TraceWindow getTraceWindow() {
      
      if (traceWindow == null) {
         traceWindow = new TraceWindow();
         traceWindow.setTitle(toString());
         traceWindow.show();
      }
      
      return traceWindow;
   }
   /**
    * Insert the method's description here.
    * Creation date: (18/01/00 19:47:05)
    */
   public void newLine() {
      
      if (on) getTraceWindow().newLine();
      
   }
   /**
    * Insert the method's description here.
    * Creation date: (20/01/00 16:22:46)
    * @param newOn boolean
    */
   public void setState(boolean newOn) {
      
      on = newOn;
   }
   /**
    * Insert the method's description here.
    * Creation date: (18/01/00 14:04:58)
    * @param i int
    * @exception java.io.IOException The exception description.
    */
   public void write(int i) throws java.io.IOException {
      
      super.write(i);
      
      if (on) {
         if (getTraceWindow().isShowing())
            getTraceWindow().write(i);
         else {
            setState(false);
            traceWindow = null;
         }
      }
      
   }
}
