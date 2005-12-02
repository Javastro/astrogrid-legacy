/*$Id: BackgroundExecutorImpl.java,v 1.3 2005/12/02 17:05:29 nw Exp $
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

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import java.util.Comparator;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import EDU.oswego.cs.dl.util.concurrent.BoundedPriorityQueue;
import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.SynchronousChannel;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Nov-2005
 *
 */
public class BackgroundExecutorImpl implements BackgroundExecutor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BackgroundExecutorImpl.class);

    public static int QUEUE_SIZE = 10000; // ten thousand queued tasks.
    final static class TimeoutPooledExecutor extends PooledExecutor {
        TimeoutPooledExecutor(Channel arg0) {
            super(arg0,30); // when queue is full, up to 30 concurrent threads.
            setMinimumPoolSize(15);
            createThreads(15); // start with 20 threads. keep-alive is a minute by default.
            discardOldestWhenBlocked(); // when queue is full, on new request discard oldest unfulfilled request.                
            setThreadFactory(new ThreadFactory() {
                public Thread newThread(Runnable arg0) {
                    Thread t = new Thread(arg0);
                    t.setPriority(Thread.MIN_PRIORITY+2);
                    t.setName("Pool-Thread-" + System.currentTimeMillis());
                    return t;
                }
            });
            
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
            threads_.put(worker,thread);
            ++poolSize_;
            thread.start();
        }

        // copied from parent class.
        protected class TimeoutAwareWorker extends Worker {
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
                            // dunno. @todo work out what to do here.
                            logger.warn("Timer thread interupted");
                        }
                    }
                }
            }

            protected final Thread timerThread;
            protected final Channel c;
            protected Thread executionThread;
            public Runnable getCurrentTask() {
                return firstTask_;
            }
            protected TimeoutAwareWorker(Runnable firstTask) { 
                super(firstTask);
                this.c = new SynchronousChannel();
                this.timerThread = getThreadFactory().newThread(new Timer());
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
        // configure the executor as follows - 10 requests processed at same time, rest queued, increasing to  15 simultaneous when queue is full.        
        // I'm guessing that most background tasks will be io-blocked for the majority of the time (soap call, http get, etc)
        // so can afford to have more concurrency here. but don't want too much - as all the request may saturate network / servers.
        this.chan = new BoundedPriorityQueue(QUEUE_SIZE);
        this.exec = new TimeoutPooledExecutor(chan);
        this.ui = ui;        
    }
    private final BoundedPriorityQueue chan;
    private final TimeoutPooledExecutor exec;
    private final UIInternal ui;
    
    
    
    public void executeWorker(BackgroundWorker worker) {
        try {
            exec.execute(worker);
        } catch (InterruptedException e) {
            logger.warn("Didn't expect to be interrupted",e);
        }
    }

    public void execute(Runnable arg0) throws InterruptedException {
        this.executeWorker(ui.wrap(arg0));
    }

    /**
     * @see org.astrogrid.desktop.modules.system.BackgroundExecutor#interrupt(java.lang.Runnable)
     */
    public void interrupt(Runnable r) {
        exec.interrupt(r);
    }
   
    



}


/* 
$Log: BackgroundExecutorImpl.java,v $
Revision 1.3  2005/12/02 17:05:29  nw
turned up numer of concurrent threads,

Revision 1.2  2005/12/02 13:56:28  nw
tweaked pool parameters.

Revision 1.1  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on
 
*/