/*$Id: MemoryQueueSchedulerNotifierTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;

import org.astrogrid.jes.jobscheduler.MockJobScheduler;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.JobURN;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MemoryQueueSchedulerNotifierTest extends TestCase {
    /**
     * Constructor for MemoryQueueSchedulerNotifierTest.
     * @param arg0
     */
    public MemoryQueueSchedulerNotifierTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        js = new MockJobScheduler();
        q = new MemoryQueueSchedulerNotifier(js);
    }
    
    protected MemoryQueueSchedulerNotifier q;
    protected MockJobScheduler js;
    
    public void testSimpleNewJob() throws Exception {
        q.scheduleNewJob(new JobURN("jes:some:urn"));
        q.addTask(new Runnable() {

            public void run() {
                System.out.println("In last task");
              assertEquals(1,js.getCallCount());  
            }
        });
        // don't know what else we can do. nothing observable from this end.
    }
    
    public void testSimpleResumeJob() throws Exception {
        q.resumeJob(new JobInfo());
        q.addTask(new Runnable() {

            public void run() {
                System.out.println("In last task");
              assertEquals(1,js.getCallCount());  
            }            
        });
    }
    // would like to stress test this - have an array of threads concurrently addeing tasks to the scheduler queue.
}


/* 
$Log: MemoryQueueSchedulerNotifierTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/