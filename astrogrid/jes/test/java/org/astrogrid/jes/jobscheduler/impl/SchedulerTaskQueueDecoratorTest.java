/*$Id: SchedulerTaskQueueDecoratorTest.java,v 1.2 2004/07/01 21:15:00 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class SchedulerTaskQueueDecoratorTest extends TestCase {
    /**
     * Constructor for MemoryQueueSchedulerNotifierTest.
     * @param arg0
     */
    public SchedulerTaskQueueDecoratorTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        js = new MockSchedulerImpl();
        q = new SchedulerTaskQueueDecorator(js);
    }
    
    protected SchedulerTaskQueueDecorator q;
    protected MockSchedulerImpl js;
    
    public void testSimpleNewJob() throws Exception {
        JobURN urn = new JobURN("jes:some:urn");
        q.scheduleNewJob(urn);
        q.addTask(new Runnable() {

            public void run() {
                System.out.println("In last task");
              assertEquals(1,js.getCallCount());  
            }
        });
        // don't know what else we can do. nothing observable from this end.
    }
    
    public void testSimpleResumeJob() throws Exception {
        q.resumeJob(new JobIdentifierType(),new MessageType());
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
$Log: SchedulerTaskQueueDecoratorTest.java,v $
Revision 1.2  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.1  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.4  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/