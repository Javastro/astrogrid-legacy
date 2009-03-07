/*
 * $Id: StressTest.java,v 1.2 2009/03/07 08:55:27 pah Exp $
 * 
 * Created on 20 Feb 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StressTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
 
        int corepool = 100;
        int maxpool = 100;
        long keepalive = Long.MAX_VALUE ;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workqueue = new SynchronousQueue<Runnable>(true);
        ExecutorService executor =  new ThreadPoolExecutor(corepool, maxpool,keepalive, unit, workqueue);//Executors.newCachedThreadPool();
        
        for (int i = 0; i < 5; i++) {
            ExerciseUWS client = new ExerciseUWS();
            executor.execute(client);
        }
        executor.shutdown();
    }

}


/*
 * $Log: StressTest.java,v $
 * Revision 1.2  2009/03/07 08:55:27  pah
 * stress testing code first checkin funny
 *
 * Revision 1.1  2009/02/26 11:17:41  pah
 * first attempt at a stress test
 *
 */
