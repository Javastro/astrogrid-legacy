/*$Id: BackgroundWorker.java,v 1.1 2005/09/02 14:03:34 nw Exp $
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker;

import java.lang.reflect.InvocationTargetException;


/** abstract class for all long-running operations - all requests to VO services should be done in instances of this class.
      * <p/>
      * Extends the standard SwingWorker, but integrates with the ui component - takes care of starting and  stopping busy indicator, progress message.
      * @todo later will extend so a popup 'cancel' dialogue appears if the op takes a _very_ long time. (i.e. is stalled)  there's a way for the user to recover
      * @todo integrate with glass pane / hourglass cursor in cases where it should be a blocking operation (but still must be in a background thread so that other
      * UI windows are responsive, and the UI is repainted).     
      * @author Noel Winstanley nw@jb.man.ac.uk 02-Apr-2005
      *
      */
    public abstract class BackgroundWorker extends SwingWorker {
        /** string displayed in status bar while operation is in progress */
        protected final String msg;
        /** ui component that this operation is displaying progress in */
        protected final UIComponent parent;
        /**
         *  Construct a new BackgroundOperation
         * @param parent - UICOmponent to report progress in.
         * @param msg message to display in status bar in parent UIComponent.
         */
        public BackgroundWorker(UIComponent parent,String msg) {
            super();
            this.parent = parent;
            this.msg = msg;
        }
        /** start the background operation running in a separate thread.
         * safe to call this mehod from the event-dispatch thread.
         * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#start()
         */
        public final  synchronized void start() {
            parent.setBusy(true);
            parent.setStatusMessage(msg);            
            super.start();
        }        
        /** implement this method to define the computation to execute on the background thread. 
         * @return some result, which is then passed to {@link #doFinished(Object)}. To return multiple results, use member variables in the subclass.
         * @see EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker#construct()
         */
        protected abstract Object construct() throws Exception;
        
        /** once the background operation in {@link #construct()} completes without an exception, this method is executed in the event-dispatch thread
         * <p>
         * null implementation - override this method to update the UI with the results of the background operation
         * @param result the result of the background computation in {@link #construct()}
         */
        protected void doFinished(Object result) {
        }
        /** this method is always exectued, last of all, in the event dispatch thread. - it's similar to a <tt>finally</tt> block.
         * <p>
         *  If {@link #construct()} completes successfully, this methods is executed <b>after</b> {@link #doFinished(Object)}.
         * On the other hand, if {@link #construct()} fails with an exception, this method is still executed.
         * <p>
         * null implementation - override this method to do cleanup operations in the ui.
         *
         */
        protected void doAlways() {
        }
            protected final void finished() {
                try {
                    Object result = this.get();
                    doFinished(result);
                } catch (InterruptedException e) {
                    // not bothered.
                } catch (InvocationTargetException e) {
                    Throwable ex= e.getCause() != null ? e.getCause() : e;
                    parent.showError(msg + ": Failed",ex);
                } finally {
                    parent.setBusy(false);//@todo - should this be before we get the result?
                    parent.setStatusMessage("");
                    doAlways();
                }
        }
        

}

/* 
$Log: BackgroundWorker.java,v $
Revision 1.1  2005/09/02 14:03:34  nw
javadocs for impl
 
*/