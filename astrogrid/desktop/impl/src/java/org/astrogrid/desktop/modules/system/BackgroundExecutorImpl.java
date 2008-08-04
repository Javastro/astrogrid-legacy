/*$Id: BackgroundExecutorImpl.java,v 1.18 2008/08/04 16:37:23 nw Exp $
 * Created on 30-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.security.Principal;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import EDU.oswego.cs.dl.util.concurrent.BoundedPriorityQueue;
import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.Semaphore;
import EDU.oswego.cs.dl.util.concurrent.SynchronousChannel;
import ca.odell.glazedlists.impl.sort.ComparableComparator;

/** Implementation of the background executor. 
 * Provides a thread pool (configurable with keys) that processes new background tasks. Appears to be much faster - UI more responsive - than
 * creating a new background thread for each task.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Nov-2005
 * @TEST improve integration tests. hard to test because of concurrency.
 */
public class BackgroundExecutorImpl implements BackgroundExecutor , ShutdownListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BackgroundExecutorImpl.class);

    public static final int QUEUE_SIZE_DEFAULT = 10000; // ten thousand queued tasks.
    public static final int START_THREADS_DEFAULT = 15;
    
    int queueSize = QUEUE_SIZE_DEFAULT;
    int startThreads = START_THREADS_DEFAULT; // number of threads to start with
    
    /** subclass of the priority queue which tells you if there's consumers blocked waiting for inpu
and has an 'express' slot which bypasses all others. */
    public static class ExpressBoundedPriorityQueue extends BoundedPriorityQueue {

        public ExpressBoundedPriorityQueue(final int arg0) throws Throwable{            
                super(arg0,new ComparableComparator(),ObservableWaiterPreferenceSemaphore.class);
        }
        /** returns true if somethin is blocked waiting for a queue item */
        public boolean isConsumerWaiting() {
            final boolean b = ((ObservableWaiterPreferenceSemaphore)takeGuard_).isConsumerWaiting();         
            return b;
        }
        /** express slot. */
        private final Channel express = new SynchronousChannel();
        
        /** offer an object, but only if it can be taken by a consumer in this time. */
        public boolean offerIfTaken(final Object x,final long msecs) throws InterruptedException {
            if (isConsumerWaiting()) { // there's a task already waiting on the queue - stick it in there.
                return false;
            }
            return express.offer(x,msecs);              
        }
        
        @Override
        public Object poll(final long arg0) throws InterruptedException {
            final Object o = express.poll(0);
            if (o != null) {
                return o;
            }            
            return super.poll(arg0);
        }
        
        @Override
        public Object take() throws InterruptedException {
            final Object o = express.poll(0); // take it, if it's waiting.
            if (o != null) {
                return o;
            }
            return super.take();
        }
    }
    
/** copied from edu.concurrentWaiterPreferneceSemaphore.
 * wanted to extend to allow peek at number of waiting, but it's a final class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 12, 20073:20:41 PM
 */
    public static class ObservableWaiterPreferenceSemaphore extends Semaphore  {

      /** 
       * Create a Semaphore with the given initial number of permits.
      **/

      public ObservableWaiterPreferenceSemaphore(final long initial) {  super(initial); }

      /** Number of waiting threads **/
      protected long waits_ = 0;   

      /** returns true if something is waiting */
      public boolean isConsumerWaiting() {
          return waits_ > 0;
      }
      
      @Override
    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        synchronized(this) {
          /*
            Only take if there are more permits than threads waiting
            for permits. This prevents infinite overtaking.
          */ 
          if (permits_ > waits_) { 
            --permits_;
            return;
          }
          else { 
            ++waits_;
            try { 
              for (;;) {
                wait(); 
                if (permits_ > 0) {
                  --waits_;
                  --permits_;
                  return;
                }
              }
            }
            catch(final InterruptedException ex) { 
              --waits_;
              notify();
              throw ex;
            }
          }
        }
      }

      @Override
    public boolean attempt(final long msecs) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        synchronized(this) {
          if (permits_ > waits_) { 
            --permits_;
            return true;
          }
          else if (msecs <= 0) {
            return false;
        } else {
            ++waits_;
            
            final long startTime = System.currentTimeMillis();
            long waitTime = msecs;
            
            try {
              for (;;) {
                wait(waitTime);
                if (permits_ > 0) {
                  --waits_;
                  --permits_;
                  return true;
                }
                else { // got a time-out or false-alarm notify
                  waitTime = msecs - (System.currentTimeMillis() - startTime);
                  if (waitTime <= 0) {
                    --waits_;
                    return false;
                  }
                }
              }
            }
            catch(final InterruptedException ex) { 
              --waits_;
              notify();
              throw ex;
            }
          }
        }
      }

      @Override
    public synchronized void release() {
        ++permits_;
        notify();
      }

      /** Release N permits **/
      @Override
    public synchronized void release(final long n) {
        permits_ += n;
        for (long i = 0; i < n; ++i) {
            notify();
        }
      }
    }
      
    
    static class TimeoutPooledExecutor extends PooledExecutor {

        private final ExpressBoundedPriorityQueue queue;

        TimeoutPooledExecutor(final ExpressBoundedPriorityQueue arg0,final int maxThreads,final int startThreads,  final SessionManagerInternal ss) {
            super(arg0,maxThreads);
            this.queue = arg0;
            this.ss = ss;
            setMinimumPoolSize(startThreads);
            createThreads(startThreads / 2); // warm-start some
            discardOldestWhenBlocked(); // when queue is full, on new request discard oldest unfulfilled request.                            
        }
        private final SessionManagerInternal ss;
        
        
        /** overridden to run tasks in submitting thread that are submitted after shutdown 
         * as AR shutdown process is unordered, seems best to do this.
         * */
        @Override
        public void shutdownAfterProcessingCurrentlyQueuedTasks() {
            super.shutdownAfterProcessingCurrentlyQueuedTasks(new RunWhenBlocked(){});
        }

        public void interrupt(final Runnable r) {
            // need to find thread that's processing this task.
            for (final Iterator i = threads_.keySet().iterator(); i.hasNext(); ) {
                final TimeoutAwareWorker w = (TimeoutAwareWorker)i.next();
                if (r.equals(w.getCurrentTask())) {
                    final Thread t = (Thread)threads_.get(w);
                    if (t != null) {
                        try { t.interrupt(); } catch (final Exception ex) {
                            //ignored
                        }
                    }
                }
            }
        }
        
        /** overridden to give express execution to Thread.MAX_PRIORITY
         * */ 
        @Override
        public void execute(final Runnable arg0) throws InterruptedException {
            if (arg0 instanceof BackgroundWorker  //always
                    && ((BackgroundWorker)arg0).getInfo().getPriority() == Thread.MAX_PRIORITY // top priority.
                    && ! queue.isConsumerWaiting() // no threads waiting on the queue 
                    && getPoolSize() >= getMinimumPoolSize()) { // confusing terminology - minimumPoolSize is the most threads that will be created ever when using a buffer.
                                                    // ok. so pool won't create any more threads by itself. 
                synchronized(this) {
                    if (queue.offerIfTaken(arg0,300)){  // offer for a little bit on the express queue
                        return;
                    }
                    // ok, all are busy - need to put add another thread.
                    if (getPoolSize() >= getMaximumPoolSize()) {
                        setMaximumPoolSize(getMaximumPoolSize()+1); // make a bit more size
                    }
                    logger.debug("Adding hi-priority thread");
                    addThread(arg0);
                }
            } else { // fallback to the existing way of doing things.
                super.execute(arg0);
            }
        }
        
        // overridden to provide own custom worker - that provides timeout functionality.
        @Override
        protected void addThread(final Runnable arg0) {
            
            final Worker worker = new TimeoutAwareWorker(arg0);
            // rest copied from parent source.
            final Thread thread = getThreadFactory().newThread(worker);
            thread.setDaemon(true);
            thread.setName("Executor Worker Thread-" + poolSize_);
            thread.setPriority(Thread.MIN_PRIORITY + 2);
            threads_.put(worker,thread);
            ++poolSize_;
            thread.start();
        }

        // copied from parent class.
        protected final class TimeoutAwareWorker extends Worker {
            private final class Timer implements Runnable {

                public void run() {
                    for (;;) {
                        try {
                            Object o;
                            do {
                                o = c.take(); // wait until passed a timeout value.
                            } while (! (o instanceof BackgroundWorker)); // must have gotten out-of-step - lets try again..
                        
                        final BackgroundWorker bw = (BackgroundWorker)o;
                        final Object result = c.poll(bw.getInfo().getTimeout()); // wait for the prescribed timeout.
                        if (result == null) {
                            bw.getControl().setTimedOut(true);
                            executionThread.interrupt(); // abort executioin thread
                        }
                        } catch (final InterruptedException e) {
                            // dunno.will this ever happen?
                            logger.warn("Timer thread interupted");
                        }
                    }
                }
            } // end Timer Runnable.

            protected final Thread timerThread;
            protected final Channel c;
            protected Thread executionThread;
            public Runnable getCurrentTask() {
                return firstTask_;
            }
            /** constructor */
            protected TimeoutAwareWorker(final Runnable firstTask) { 
                super(firstTask);
                this.c = new SynchronousChannel();
                this.timerThread = getThreadFactory().newThread(new Timer());
                this.timerThread.setDaemon(true);
                this.timerThread.setName("Executor Timer Thread-" + poolSize_);
                this.timerThread.setPriority(Thread.MIN_PRIORITY + 3);
                this.timerThread.start();
                }
            
            @Override
            public void run() {
                try {
                    this.executionThread = Thread.currentThread();
                    do {
                        if (firstTask_ != null) {                         
                            if (firstTask_ instanceof BackgroundWorker) {
                            	final BackgroundWorker bw = (BackgroundWorker)firstTask_;
                            	final long timeout = bw.getInfo().getTimeout();
                            	if (timeout  > 0L ) {
                            		c.put(bw);
                            	}
                            	ss.adoptSession(bw.getControl().getPrincipal());
                                firstTask_.run();
                                ss.clearSession();
                                if (timeout > 0L ) {
                                	c.put(NULL_OBJ);
                                }
                            } else { // it's just a runnable. unlikely.
                            	firstTask_.run(); 
                            }
                            firstTask_ = null;
                        }
                        firstTask_ = getTask();
                    } while (firstTask_ != null);                   
                }  catch (final InterruptedException ex) {  // fall through    
                } finally {
                    firstTask_ = null;
                    workerDone(this);
                } 
            }
        }
    }
    
    private static final Object NULL_OBJ = new Object();
    public BackgroundExecutorImpl(final UIContext ui, final SessionManagerInternal ss) {
        this.ui = ui;        
        this.ss = ss;
    }
    
    /** separate initialize method, to start service after it's been configured using setters.
     * 
     * important - call this method before using this component
     *
     */
    public void init() {        
        try {
            this.chan = new ExpressBoundedPriorityQueue(queueSize);
        } catch (final Throwable t) {
            throw new ApplicationRuntimeException("Failed to create executor priority queue",t);
        }        
        // create an executer with a queue of queue size,
        // where we start with startThreads,
        // and, if programmatically requested, we'll add threads up to startThreads*2
        // however, threads won't be added past startThreads in normal use
        // as queuing happens instead.
        this.exec = new TimeoutPooledExecutor(chan,startThreads*2,startThreads,ss);
    }
    private ExpressBoundedPriorityQueue chan;
    private TimeoutPooledExecutor exec;
    private final UIContext ui;
    private final SessionManagerInternal ss;
    
    
    
    public void executeWorker(final BackgroundWorker worker) {
        try {
            logger.debug("executing worker");
        	// this is called by Thread.start(), and any other ways to execute this worker
        	// so we've waited till the last moment, and now capture the permissions / session
        	// of the calling thread, so it'll be passed into the background thread.
        	if (worker.getControl().getPrincipal() == null) { // if a principal has already been set, that's fine
        		final Principal p = ss.currentSession();
        		if (p != null) {
        		    logger.debug("setting principal to " + p.getName());
        		    worker.getControl().setPrincipal(p);
        		}
        	}
            exec.execute(worker);
        } catch (final InterruptedException e) {
            logger.warn("Didn't expect to be interrupted",e);
        }
    }

    /** first converts from a runnable to a BackgroundWorker, attached to the parent ui */
    public void execute(final Runnable arg0) throws InterruptedException {
        this.executeWorker(new BackgroundWorker(ui,"Background Task"){
            @Override
            protected Object construct() throws Exception {
                arg0.run();
                return null;
            }        	
        });
    }

    /**
     * @see org.astrogrid.desktop.modules.system.BackgroundExecutor#interrupt(java.lang.Runnable)
     */
    public void interrupt(final Runnable r) {
        exec.interrupt(r);
    }



    public void setQueueSize(final int queueSize) {
        this.queueSize = queueSize;
    }


    public void setStartThreads(final int startThreads) {
        this.startThreads = startThreads;
    }

    public void halting() {       
        exec.shutdownAfterProcessingCurrentlyQueuedTasks();
    }

    public String lastChance() {
        return null;
    }

    public void executeLaterEDT(final Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
   
    



}


/* 
$Log: BackgroundExecutorImpl.java,v $
Revision 1.18  2008/08/04 16:37:23  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.17  2008/04/25 08:59:01  nw
refactored to ease testing.

Revision 1.16  2008/04/23 11:10:19  nw
marked as needing test.

Revision 1.15  2007/12/12 13:54:15  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.14  2007/11/26 12:01:48  nw
added framework for progress indication for background processes

Revision 1.13  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.12  2007/09/04 18:50:50  nw
Event Dispatch thread related fixes.

Revision 1.11  2007/08/22 22:33:42  nw
Complete - task 144: clear view when logging out and loggin in as different user

Revision 1.10  2007/04/18 15:47:07  nw
tidied up voexplorer, removed front pane.

Revision 1.9  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.8  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.7  2006/06/27 10:38:34  nw
findbugs tweaks

Revision 1.6  2006/06/15 09:53:37  nw
doc fix

Revision 1.5  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.4  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.3.26.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.3.26.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3.26.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.3  2005/12/02 17:05:29  nw
turned up numer of concurrent threads,

Revision 1.2  2005/12/02 13:56:28  nw
tweaked pool parameters.

Revision 1.1  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on
 
*/