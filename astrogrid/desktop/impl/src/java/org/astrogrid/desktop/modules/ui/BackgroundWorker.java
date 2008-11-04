/*$Id: BackgroundWorker.java,v 1.21 2008/11/04 14:35:51 nw Exp $
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

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.Timer;

import junit.framework.AssertionFailedError;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.ui.ProgressDialogue;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

import EDU.oswego.cs.dl.util.concurrent.Callable;
import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;


/** base class for all long-running tasks. 
 * All requests to VO services should be done in instances of this class.
      * <p/>
      *Based on standard implementation of SwingWorker (EDU.oswego.cs.dl.util.concurrent.misc.SwingWorker) 
      *but integrates with the ui component - takes care of starting and  stopping busy indicator, progress message.
      *also has concept of priority, and is executed by pooled executor, instead of by creating a new thread.
      * @author Noel Winstanley noel.winstanley@manchester.ac.uk 02-Apr-2005
      *
      */
    public abstract class BackgroundWorker  extends Observable implements Runnable, Comparable, WorkerProgressReporter{
        


        /** Enumeration of timeout values. defined in terms of a
         * 'timeout factor' which is defined in application preferences.
         * Consider this factor to have a default value of 5
         * @author Noel.Winstanley@manchester.ac.uk
         * @since Nov 26, 20071:23:37 PM
         */
        public static class TimeoutEnum {
            private final long factor;

            private TimeoutEnum(final long factor){
                this.factor = factor;
            }
            private TimeoutEnum(final TimeoutEnum basis, final long factor) {
                this.factor = basis.factor * factor;
            }
            public String toString() {
                return "Timeout = " + factor + " * ${performance.TimoutFactor}";
            }
        }
            /** timeout constants - considers time executing, not time spent on the queue.
             * 
             * */
        public final static TimeoutEnum INSANELY_SHORT_TIMEOUT = new TimeoutEnum(200); // 1s, assuming scaling factor of 5
        public final static TimeoutEnum VERY_SHORT_TIMEOUT = new TimeoutEnum(INSANELY_SHORT_TIMEOUT, 10); // 10 s, assuming scaling factor of 5;
        public final static TimeoutEnum SHORT_TIMEOUT = new TimeoutEnum(VERY_SHORT_TIMEOUT ,6); //1 min
        public final static TimeoutEnum DEFAULT_TIMEOUT = new TimeoutEnum(SHORT_TIMEOUT,2); // 2min
        public final static TimeoutEnum LONG_TIMEOUT = new TimeoutEnum(DEFAULT_TIMEOUT ,5); //10 min
        public final static TimeoutEnum VERY_LONG_TIMEOUT = new TimeoutEnum(LONG_TIMEOUT ,6); //1hr
        
        /** class used to get information on the current status of the worker
         * used within the monitoring framework - not for use by subclasses of background worker.
         * @author Noel.Winstanley@manchester.ac.uk
         * @since Nov 26, 20073:28:03 PM
         */
        public final class Info {
            /** returns true if this is a 'system' process */
            public final boolean isSystem() {
                return system;
            }            
            /** return the priority of this worker.
             * If priority == Thread.MAX_PRIORITY then this thread will always be executed immediately.
             * otherwise, if the thread pool is all busy, it'll be added to a prioritized queue, which selects next task based on priority.
             * @return
             */
            public final int getPriority() {
                return priority;
            }            
            /**
             * Returns timeout period in milliseconds. Timeout is the
             * maximum time to wait for worker to complete. There is
             * no time limit if timeout is <code>0</code> (default).
             */
            public final long getTimeout() {
                return timeout;
            }     
            /** get the status of this background process - one of the int constants in this class */
            public final int getStatus() {
                return status;
            }

            
            public final String getWorkerTitle() {
                return workerTitle;
            }        
            
            public final int getMaxProgress() {
                return maxVaue;
            }
            public final int getCurrentProgress() {
                return currentValue;
            }
            public final List getProgressMessages() {
                return progressMessages;
            }
            
        }
        
        public final Info getInfo() {
            return info;
        }
        private ProgressDialogue progressDialogue;
        private final Info info = new Info();
        /** collection of 'controlling' merthods - used by the execution framework, not to be called by subclasses */
        public final class Control {
            /** show a progress dialogue - must be called on the EDT */
            public final void showSingleDialogue() {
                if (GraphicsEnvironment.isHeadless()) {
                    return; // don't even create the dialogue if it's headless
                }
                if (progressDialogue == null) {
                    progressDialogue= ProgressDialogue.newProgressDialogue(BackgroundWorker.this);
                }
                progressDialogue.show();
            }
            
            /** part of the implementation - do not call */
            public final void setTimedOut(final boolean b) {
                timedout = b;
            }            
            /** part of the implementation */
            public final Principal getPrincipal() {
                return principal;
            }
            /** part of the implementation */
            public final void setPrincipal(final Principal p) {
                principal = p;
            }            
        }
        private final Control control = new Control();
        public final Control getControl() {
            return control;
        }
        
        /** string displayed in status bar while operation is in progress */
        protected final String workerTitle;
        /** ui component that this operation is displaying progress in */
        protected final UIComponent parent;
        /** executor that's running this background worker*/
        protected final BackgroundExecutor executor;
        /** flag to indicate that this is a 'system' task - only used within UI */
        private boolean system;
        
        /** Holds the value to be returned by the <code>get</code> method. */
        private final FutureResult result = new FutureResult();

        /** Maximum time to wait for worker to complete. */
        private final long timeout;
        private final long unqId;
        private static long idGen = 0L;
        /** priority for this tasks */
        private final int priority;
        public BackgroundWorker(final UIContext context, final String msg) {
        	this(context.findMainWindow(),msg);
        	system = true;
        }
        public BackgroundWorker(final UIContext context, final String msg, final TimeoutEnum timeout) {
        	this(context.findMainWindow(),msg,timeout);
        	system = true;
        }        
        public BackgroundWorker(final UIContext context, final String msg, final int priority) {
            this(context.findMainWindow(),msg,priority);
            system = true;
        }  
        public BackgroundWorker(final UIContext context, final String msg, final TimeoutEnum timeout,final int priority) {
            this(context.findMainWindow(),msg,timeout,priority);
            system = true;
        }          
        public BackgroundWorker(final UIComponent parent,final String msg) {
            this(parent,msg,DEFAULT_TIMEOUT,Thread.NORM_PRIORITY);
        }
        public BackgroundWorker(final UIComponent parent,final String msg, final TimeoutEnum timeout) {
            this(parent,msg,timeout,Thread.NORM_PRIORITY);
        }
        public BackgroundWorker(final UIComponent parent,final String msg, final int priority) {
            this(parent,msg,DEFAULT_TIMEOUT,priority);
        }
       
        // container for the principal - that is the session / user whose privileges to use to execute this task.
        private Principal principal;
  
        
        
        /**
         *  Construct a new BackgroundOperation
         * @param parent - UICOmponent to report progress in.
         * @param msg message to display in status bar in parent UIComponentImpl.
         */
        public BackgroundWorker(final UIComponent parent,final String msg, final TimeoutEnum timeout, final int priority) {
            super();
            this.parent = parent;
            this.executor = parent.getContext().getExecutor();
            this.workerTitle = msg;
            long tout = 5 * timeout.factor;
            if (parent.getContext() != null && parent.getContext().getConfiguration() != null) {
                final String val = parent.getContext().getConfiguration().getKey("performance.timeoutFactor");
                if (val != null) {
                    try {
                        tout = Long.parseLong(val) * timeout.factor;
                    } catch (final NumberFormatException e) { 
                    }
                }
            }
            this.timeout = tout;
            this.priority = priority;
            this.unqId = ++idGen;
            
        }
        
        public static final int PENDING = 0;
        public static final int RUNNING = 1;
        public static final int COMPLETED = 2;
        private volatile int status = PENDING;

        
        private final void setStatus(final int i) {
            this.status = i;
            setChanged();
            notifyObservers();
        }
        
        /** overridden to ensure calls on EDT */
        public final void notifyObservers() {
                executor.executeLaterEDT(new Runnable() {

                    public void run() {
                        BackgroundWorker.super.notifyObservers();
                    }
                });
            
        }

        
            
        
        /**
         * Calls the <code>construct</code> method to compute the result,
         * and then invokes the <code>finished</code> method on the event
         * dispatch thread.
         */
        public  final void run() {
            if (!run) {
                return; // halt here if has been cancelled while on queue            
            }
            executor.executeLaterEDT(new Runnable() {
            // notify ui that we're starting to execute
                public void run() {
                    setStatus(RUNNING);
                    // display a progress monitor, if requested.
                    if (wouldLikeIndividualMonitor) {
                        final String val = parent.getContext().getConfiguration().getKey("performance.showProgressDialogueAfter");
                        int t = 5;
                        try {
                            t = Integer.parseInt(val);
                        } catch (final NumberFormatException e) {
                            // fallback to default
                        }
                        if (t == 0) {
                            getControl().showSingleDialogue();
                        } else if (t > 0) {
                            final Timer timer = new Timer(t * 1000,new ActionListener() {
                                public void actionPerformed(final ActionEvent e) {
                                    if (status != COMPLETED) {
                                        getControl().showSingleDialogue();
                                    }
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                } 
            });
            result.setter(new Callable() {// execute the computation
                public Object call() throws Exception {
                    return construct();
                }
            }).run();           
            executor.executeLaterEDT(new Runnable() {
                // finish off by updating ui.
                public void run() {
                    try {
                        final Object o = get();
                        doFinished(o);
                    } catch (final InterruptedException e) {
                        parent.setStatusMessage(workerTitle + " - Interrupted");
                       
                    } catch (final InvocationTargetException e) {                    
                        final Throwable ex= e.getCause() != null ? e.getCause() : e;
                        if (InterruptedException.class.isAssignableFrom(ex.getClass())) {
                            // it's a wrapped interruption, caused by the user pressing cancel, or a timeout
                            if (timedout) {
                                parent.setStatusMessage(workerTitle + " - Timed Out");
                                doError(ex);
                            } else {
                                parent.setStatusMessage(workerTitle + " - Interrupted");
                            }
                        } else if (AssertionError.class.isAssignableFrom(ex.getClass())) {
                            throw (AssertionError)ex;   
                        } else if (AssertionFailedError.class.isAssignableFrom(ex.getClass())) {
                            throw (AssertionFailedError)ex;
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
        
        private volatile boolean timedout = false;
        
        private volatile boolean run = true;
        /**
         * Starts the worker thread.
         */
        public final void start() {
                   executor.executeLaterEDT(new Runnable() {
					public void run() {
					    parent.getContext().getTasksList().add(BackgroundWorker.this);
					    //@todo think when's best to set the status message...
					    parent.setStatusMessage(workerTitle);
					    executor.executeWorker(BackgroundWorker.this);
					}
        		});
        }

        /**
         * Stops the worker . haven't got a handle on the thread, so can't easily interrupt it.
         * @todo find a way to really halt the thread.
         */
        public final void interrupt() {
            run = false; // will halt a task if it hasn't already been executed.
            executor.interrupt(this); // will try to halt a running task
            result.setException(new InterruptedException());
            executor.executeLaterEDT(new Runnable() {
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
        public final Object get()
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
        public final Object timedGet(final long msecs) 
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
        public final InvocationTargetException getException() {
            return result.getException();
        }

        /**
         * Return whether the <code>get</code> method is ready to
         * return a value.
         *
         * @return true if a value or exception has been set. else false
         */
        public final boolean isReady() {
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
        protected void doFinished(final Object result) {
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
        // controls whether messages are shown in a modal, or a transient dialogue.
        private boolean transientMessages= false;
        /** subclasses should call this method in the constuctor to indicate that
         * error messages are to be displayed in transient popups, rather than modal dialogues.
         * set to <tt>false</tt> by default
         * @param b
         */
        protected final void setTransient(final boolean b) {
            transientMessages = b;
        }

        
        /** can be repeatedly called by background workers (from any thread) to 
         * report the progress of the task
         * @param s
         */
        public final void reportProgress(final String s) {
            // only most recent message is persisted.
            progressMessages.add(s);
            // add logging here too?
            setChanged();
            notifyObservers();
        }
        private final List progressMessages = new ArrayList(10);
        
      

        /** for workers which can determine how long a task is going to take,
         * use this method for determinate progress reporting
         * callable from any thread.
         * @param newCurrentValue current progress
         * @param newMaxValue maximum value for progress (i.e. when currentValue reaches this, task will have completed)
         */
        public final void setProgress(final int newCurrentValue, final int newMaxValue) {
            if (newCurrentValue != currentValue) {
                currentValue = newCurrentValue;              
                setChanged();
            }
            if (newMaxValue != maxVaue) {
                maxVaue = newMaxValue;
                setChanged();
            }
            notifyObservers(); // will only occur if at least one of the values has changed.
        }
        private int currentValue = MAX_VALUE_UNSPECIFIED;
        private int maxVaue = MAX_VALUE_UNSPECIFIED;
        public static final int MAX_VALUE_UNSPECIFIED= -1;
        /** Subclasses should call this in the constructor to indicate that the UI might like to display this background worker in a stand-alone
         * progress dialogue - i.e. it's a high-value or important user-triggered action
         */
        private boolean wouldLikeIndividualMonitor = false;
        protected final void setWouldLikeIndividualMonitor(final boolean b) {
            wouldLikeIndividualMonitor = true;
        }
        /**
         * defalt implementation of do error - displays an error message, and will give an option to retry if possible. 
         * override to handle errors differently.
         * behaviour is controlled by {@link #setTransient(boolean)} and {@link #setRetriable(boolean)}
         */
        protected void doError(final Throwable ex) {
            final String t = "An error occurred while " + workerTitle.toLowerCase();
            if (transientMessages) {
                    parent.showTransientError(t,ExceptionFormatter.formatException(ex));
            } else {
                    parent.showError(t,ex);                
            }
        }
        /** implmeentaiton of comparable */
        public final int compareTo(final Object o) {
                final BackgroundWorker other = (BackgroundWorker)o;
                int val = other.priority - this.priority; // higher priority => higher value. the queue takes the _least_ value - so reversed here.
                if (val == 0) {
                    val =(int)( this.unqId - other.unqId); // smallest id (i.e. oldest task) takes priority 
                }
                return val;
        }


        /** access the <tt>parent</tt> field - which denotes the owner of this backgrnd process */
        public final UIComponent getParent() {
            return this.parent;
        }     
        
        
}

/* 
$Log: BackgroundWorker.java,v $
Revision 1.21  2008/11/04 14:35:51  nw
javadoc polishing

Revision 1.20  2008/04/25 08:58:47  nw
refactored to ease testing.

Revision 1.19  2008/04/23 11:11:45  nw
adaptations for headless

Revision 1.18  2007/12/12 13:54:14  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.17  2007/11/27 07:09:51  nw
integrate commons.io

Revision 1.16  2007/11/26 14:44:45  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.15  2007/11/26 12:01:48  nw
added framework for progress indication for background processes

Revision 1.14  2007/10/07 10:41:36  nw
imporved error message formatting.

Revision 1.13  2007/09/21 16:35:14  nw
improved error reporting,
various code-review tweaks.

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