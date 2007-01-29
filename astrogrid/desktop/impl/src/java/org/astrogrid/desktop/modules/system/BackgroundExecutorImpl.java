/*$Id: BackgroundExecutorImpl.java,v 1.8 2007/01/29 11:11:36 nw Exp $
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

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import EDU.oswego.cs.dl.util.concurrent.BoundedPriorityQueue;
import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.SynchronousChannel;

/** Implementation of the background executor. 
 * Provides a thread pool (configurable with keys) that processes new background tasks. Appears to be much faster - UI more responsive - than
 * creating a new background thread for each task.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Nov-2005
 */
public class BackgroundExecutorImpl implements BackgroundExecutor , ShutdownListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BackgroundExecutorImpl.class);

    public static final int QUEUE_SIZE_DEFAULT = 10000; // ten thousand queued tasks.
    public static final int START_THREADS_DEFAULT = 15;
    public static final int MAX_THREADS_DEFAULT = 30;
    
    int queueSize = QUEUE_SIZE_DEFAULT;
    int startThreads = START_THREADS_DEFAULT; // number of threads to start with
    int maxThreads = MAX_THREADS_DEFAULT; // maximum number of threads to produce - when queue is full
    
    
    static class TimeoutPooledExecutor extends PooledExecutor {
        TimeoutPooledExecutor(Channel arg0,int maxThreads,int startThreads) {
            super(arg0,maxThreads); 
            setMinimumPoolSize(startThreads);
            createThreads(startThreads); // start with 20 threads. keep-alive is a minute by default.
            discardOldestWhenBlocked(); // when queue is full, on new request discard oldest unfulfilled request.                            
        }

        public void interrupt(Runnable r) {
            // need to find thread that's processing this task.
            for (Iterator i = threads_.keySet().iterator(); i.hasNext(); ) {
                TimeoutAwareWorker w = (TimeoutAwareWorker)i.next();
                if (r.equals(w.getCurrentTask())) {
                    Thread t = (Thread)threads_.get(w);
                    if (t != null) {
                        try { t.interrupt(); } catch (Exception ex) { }
                    }
                }
            }
        }
        
        // overridden to provide own custom worker - that provides timeout functionality.
        protected void addThread(Runnable arg0) {
            
            Worker worker = new TimeoutAwareWorker(arg0);
            // rest copied from parent source.
            Thread thread = getThreadFactory().newThread(worker);
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
                            } while (! (o instanceof Long)); // must have gotten out-of-step - lets try again..
                        
                        Long l = (Long)o;
                        Object result = c.poll(l.longValue()); // wait for the prescribed timeout.
                        if (result == null) {
                            executionThread.interrupt(); // abort executioin thread
                        }
                        } catch (InterruptedException e) {
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
            protected TimeoutAwareWorker(Runnable firstTask) { 
                super(firstTask);
                this.c = new SynchronousChannel();
                this.timerThread = getThreadFactory().newThread(new Timer());
                this.timerThread.setDaemon(true);
                this.timerThread.setName("Executor Timer Thread-" + poolSize_);
                this.timerThread.setPriority(Thread.MIN_PRIORITY + 3);
                this.timerThread.start();
                }
            
            public void run() {
                try {
                    this.executionThread = Thread.currentThread();
                    long timeout;
                    do {
                        if (firstTask_ != null) {                         
                            if (firstTask_ instanceof BackgroundWorker && (timeout = ((BackgroundWorker)firstTask_).getTimeout()) > 0L ) {
                                c.put(new Long(timeout));
                                firstTask_.run();
                                c.put(firstTask_); //throw anything handy at it.
                            } else {
                                firstTask_.run();
                            }
                            firstTask_ = null;
                        }
                        firstTask_ = getTask();
                    } while (firstTask_ != null);                   
                }  catch (InterruptedException ex) {  // fall through
                } finally {
                    firstTask_ = null;
                    workerDone(this);
                } 
            }
        }
    }
    
    public BackgroundExecutorImpl(UIInternal ui) {
        this.ui = ui;        
    }
    
    /** separate initialize method, to start service after it's been configured using setters.
     * 
     * important - call this method before using this component
     *
     */
    public void init() {
        this.chan = new BoundedPriorityQueue(queueSize);
        this.exec = new TimeoutPooledExecutor(chan,maxThreads,startThreads);
    }
    private BoundedPriorityQueue chan;
    private TimeoutPooledExecutor exec;
    private final UIInternal ui;
    
    
    
    public void executeWorker(BackgroundWorker worker) {
        try {
            exec.execute(worker);
        } catch (InterruptedException e) {
            logger.warn("Didn't expect to be interrupted",e);
        }
    }

    /** first converts from a runnable to a BackgroundWorker, attached to the parent ui */
    public void execute(Runnable arg0) throws InterruptedException {
        this.executeWorker(ui.wrap(arg0));
    }

    /**
     * @see org.astrogrid.desktop.modules.system.BackgroundExecutor#interrupt(java.lang.Runnable)
     */
    public void interrupt(Runnable r) {
        exec.interrupt(r);
    }



    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }



    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }


    public void setStartThreads(int startThreads) {
        this.startThreads = startThreads;
    }

    public void halting() {       
        exec.shutdownAfterProcessingCurrentlyQueuedTasks();
    }

    public String lastChance() {
        return null;
    }
   
    



}


/* 
$Log: BackgroundExecutorImpl.java,v $
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