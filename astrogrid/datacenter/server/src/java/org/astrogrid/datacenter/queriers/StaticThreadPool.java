/*$Id: StaticThreadPool.java,v 1.1 2004/09/17 01:26:12 nw Exp $
 * Created on 16-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/** Apologies in advance to martin - he's going to hate this.
 * JUst an experiment to see if a more structured way of dealing with threads leads to better performance in the tests (especially getting rid of those outOfMemoryErrors).
 * 
 * uses the thread pool implemented as part of the <tt>concurrent</tt> package
 * <a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html">http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html</a>
 * <p>
 * at moment configured with an infinite buffer (as we don't ever want to drop any requests), a maximum of 10 concurrent threads, and always 3 threads on standby
 *  - so quite modest. later can add in configuration properties to tune this up.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Sep-2004
 *@todo apologize to martin ;)
 */
public class StaticThreadPool {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(StaticThreadPool.class);

    /** Construct a new StaticThreadPool
     * 
     */
    private StaticThreadPool() {
        super();
    }
    private static final PooledExecutor pool = new PooledExecutor(new LinkedQueue(),10);
    static {
        pool.setMinimumPoolSize(3);
    }
    public static void shutdown(){
        logger.info("shutting down execution pool");
        pool.shutdownNow();
    }
    
    public static void execute(Runnable r) {
        try {
            pool.execute(r);
        } catch (InterruptedException e) {
            logger.warn("was interrupted when trying to execute a runnable");
        }
    }
    
    

}


/* 
$Log: StaticThreadPool.java,v $
Revision 1.1  2004/09/17 01:26:12  nw
altered querier manager to use a threadpool
 
*/