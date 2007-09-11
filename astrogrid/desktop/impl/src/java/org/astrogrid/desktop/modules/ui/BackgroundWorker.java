/*$Id: BackgroundWorker.java,v 1.12 2007/09/11 12:08:53 nw Exp $
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

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Observable;

import javax.swing.SwingUtilities;

import org.astrogrid.desktop.modules.system.ui.UIContext;

import EDU.oswego.cs.dl.util.concurrent.Callable;
import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;


/** abstract class for all long-running operations - all requests to VO services should be done in instances of this class.
      * <p/>
      *Based on standard implementation of SwingWorker (EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker) 
      *but integrates with the ui component - takes care of starting and  stopping busy indicator, progress message.
      *also has concept of priority, and is executed by pooled executor, instead of by creating a new thread.
      *
      * @todo integrate with glass pane / hourglass cursor in cases where it should be a blocking operation (but still must be in a background thread so that other
      * UI windows are responsive, and the UI is repainted).     
      * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Apr-2005
      *
      */
    public abstract class BackgroundWorker  extends Observable implements Runnable, Comparable  {
        /** default timeout for tasks - all requests should timeout, rather than blocking indefinately 
         * this is a timeout for execution time - doesn't consider time spent on queue
         * 
         * */
        public final static long DEFAULT_TIMEOUT = 1000 * 60 * 2; // 2 minutes
        /** string displayed in status bar while operation is in progress */
        protected final String msg;
        /** ui component that this operation is displaying progress in */
        protected final UIComponent parent;
        /** flag to indicate that this is a 'special' task - only used within UI */
        protected boolean special;
        
        /** Holds the value to be returned by the <code>get</code> method. */
        private final FutureResult result = new FutureResult();

        /** Maximum time to wait for worker to complete. */
        private final long timeout;
        private final long timestamp;
        private static long timestampGen = 0L;
        /** priority for this tasks */
        private int priority;
        public BackgroundWorker(UIContext context, String msg) {
        	this(context.findMainWindow(),msg);
        	special = true;
        }
        public BackgroundWorker(UIContext context, String msg, long msecs) {
        	this(context.findMainWindow(),msg,msecs);
        	special = true;
        }        
        public BackgroundWorker(UIComponent parent,String msg) {
            this(parent,msg,DEFAULT_TIMEOUT,Thread.NORM_PRIORITY);
        }
        public BackgroundWorker(UIComponent parent,String msg, long msecs) {
            this(parent,msg,msecs,Thread.NORM_PRIORITY);
        }
        public BackgroundWorker(UIComponent parent,String msg, int priority) {
            this(parent,msg,DEFAULT_TIMEOUT,priority);
        }
       
        // container for the principal - that is the session / user whose privileges to use to execute this task.
        private Principal principal;
        public Principal getPrincipal() {
        	return principal;
        }
        public void setPrincipal(Principal p) {
        	this.principal = p;
        }
        
        
        /**
         *  Construct a new BackgroundOperation
         * @param parent - UICOmponent to report progress in.
         * @param msg message to display in status bar in parent UIComponentImpl.
         */
        public BackgroundWorker(UIComponent parent,String msg, long msecs, int priority) {
            super();
            this.parent = parent;
            this.msg = msg;
            if (msecs < 0) {
                throw new IllegalArgumentException("timeout = " + msecs);
            }
            this.timeout = msecs;
            this.priority = priority;
            this.timestamp = ++timestampGen;
            
        }
        
        public static final int PENDING = 0;
        public static final int RUNNING = 1;
        public static final int COMPLETED = 2;
        private volatile int status = PENDING;
        /** get the status of this background process - one of the int constants in this class */
        public int getStatus() {
            return status;
        }
        
        void setStatus(int i) {
            this.status = i;
            super.setChanged();
            super.notifyObservers();
        }

        
        public final String getMessage() {
            return msg;
        }
        
        public final int getPriority() {
            return priority;
        }
        
        /**
         * Returns timeout period in milliseconds. Timeout is the
         * maximum time to wait for worker to complete. There is
         * no time limit if timeout is <code>0</code> (default).
         */
        public long getTimeout() {
            return timeout;
        }                
        
        /**
         * Calls the <code>construct</code> method to compute the result,
         * and then invokes the <code>finished</code> method on the event
         * dispatch thread.
         */
        public void run() {
            if (!run) return; // halt here if has been cancelled while on queue            
            SwingUtilities.invokeLater(new Runnable() {// notify ui that we're starting to execute
                public void run() {
                    setStatus(RUNNING);
                } 
            });
            result.setter(new Callable() {// execute the computation
                public Object call() throws Exception {
                    return construct();
                }
            }).run();           
            SwingUtilities.invokeLater(new Runnable() {// finish off by updating ui.
                public void run() {
                    try {
                        Object o = get();
                        doFinished(o);
                    } catch (InterruptedException e) {
                        parent.setStatusMessage(msg + " - Interrupted");
                    } catch (InvocationTargetException e) {                    
                        Throwable ex= e.getCause() != null ? e.getCause() : e;
                        if (InterruptedException.class.isAssignableFrom(ex.getClass())) {
                            // it's a wrapped interruption, caused by the user pressing cancel.
                            parent.setStatusMessage(msg + " - Interrupted");
                        } else { // it's some kind of failure                    
                            doError(ex);
                        }
                    } finally {
                        parent.getContext().getTasksList().remove(BackgroundWorker.this);
                        doAlways();
                        setStatus(COMPLETED);
                    }
                }
            });
        }
        private volatile boolean run = true;
        /**
         * Starts the worker thread.
         */
        public void start() {
        	if (! SwingUtilities.isEventDispatchThread()) {
        		SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						start();
					}
        		});
        	} else {
        		parent.getContext().getTasksList().add(this);
        		//@todo think when's best to set the status message...
        		parent.setStatusMessage(this.getMessage());
        		parent.getContext().getExecutor().executeWorker(this);
        	}
        }

        /**
         * Stops the worker . haven't got a handle on the thread, so can't easily interrupt it.
         * @todo find a way to really halt the thread.
         */
        public void interrupt() {
            run = false; // will halt a task if it hasn't already been executed.
            parent.getContext().getExecutor().interrupt(this); // will try to halt a running task
            result.setException(new InterruptedException());
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
            parent.getContext().getTasksList().remove(BackgroundWorker.this);
                }
            });
            
        }
    

        /**
         * Return the value created by the <code>construct</code> method,
         * waiting if necessary until it is ready.
         *
         * @return the value created by the <code>construct</code> method
         * @exception InterruptedException if current thread was interrupted
         * @exception InvocationTargetException if the constructing thread
         * encountered an exception or was interrupted.
         */
        public Object get()
        throws InterruptedException, InvocationTargetException {
            return result.get();
        }

        /**
         * Wait at most msecs to access the constructed result.
         * @return current value
         * @exception TimeoutException if not ready after msecs
         * @exception InterruptedException if current thread has been interrupted
         * @exception InvocationTargetException if the constructing thread
         * encountered an exception or was interrupted.
         */
        public Object timedGet(long msecs) 
        throws TimeoutException, InterruptedException, InvocationTargetException {
            return result.timedGet(msecs);
        }

        /**
         * Get the exception, or null if there isn't one (yet).
         * This does not wait until the worker is ready, so should
         * ordinarily only be called if you know it is.
         * @return the exception encountered by the <code>construct</code>
         * method wrapped in an InvocationTargetException
         */
        public InvocationTargetException getException() {
            return result.getException();
        }

        /**
         * Return whether the <code>get</code> method is ready to
         * return a value.
         *
         * @return true if a value or exception has been set. else false
         */
        public boolean isReady() {
            return result.isReady();
        }
        
        /** implement this method to define the computation to execute on the background thread. 
         * @return some result, which is then passed to {@link #doFinished(Object)}. To return multiple results, use member variables in the subclass.
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

        /**
         * defalt implementation of do error - displays an error message. override to handle errors differently.
         */
        protected void doError(Throwable ex) {
            parent.showError(msg + ": Failed",ex);
        }
        
        public int compareTo(Object o) {
                BackgroundWorker other = (BackgroundWorker)o;
                int val = other.priority - this.priority; // higher priority => higher value. the queue takes the _least_ value - so reversed here.
                if (val == 0) {
                    val =(int)( this.timestamp - other.timestamp); // smallest timestamp takes priority 
                }
                return val;
        }            
}

/* 
$Log: BackgroundWorker.java,v $
Revision 1.12  2007/09/11 12:08:53  nw
conccurency tweajs.

Revision 1.11  2007/06/18 16:47:58  nw
javadoc fixes.

Revision 1.10  2007/04/18 15:47:05  nw
tidied up voexplorer, removed front pane.

Revision 1.9  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.8  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.7  2007/01/10 19:12:16  nw
integrated with preferences.

Revision 1.6  2006/08/31 21:31:37  nw
minor tweaks and doc fixes.

Revision 1.5  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.4.26.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4  2005/12/02 13:42:18  nw
improved task-reporting system

Revision 1.3  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.2.14.1  2005/11/23 04:42:15  nw
doc fix.

Revision 1.2  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.10.1  2005/10/12 09:21:38  nw
added java help system

Revision 1.1  2005/09/02 14:03:34  nw
javadocs for impl
 
*/