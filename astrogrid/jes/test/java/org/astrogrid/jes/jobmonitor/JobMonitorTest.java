/*$Id: JobMonitorTest.java,v 1.5 2004/03/05 16:16:55 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobmonitor;

import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 * again, will follow same pattern as with job scheduler - find some way to inject a series of requests, and write test suites with different configurations - see what happens when
 * the db is down, can't connect to the scheduler, etc.
 */
public class JobMonitorTest extends TestCase {
    /**
     * Constructor for JobMonitorTest.
     * @param arg0
     */
    public JobMonitorTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() {
        this.nudger = new MockSchedulerNotifier();
        this.monitor = new JobMonitor(nudger);
        this.info = new MessageType();
        this.id = new JobIdentifierType();
        id.setValue("jes:some:urn");
        info.setPhase(ExecutionPhase.RUNNING);
    }
    protected MessageType info;
    protected JobIdentifierType id;
    protected JobMonitor monitor;
    protected MockSchedulerNotifier nudger;
    
    public void testNormal() {        
        monitor.monitorJob(id,info);
        assertEquals(1,nudger.getCallCount());  
    }
    
    public void testNull() {
        monitor.monitorJob(null,null);
        assertEquals(0,nudger.getCallCount());
    }
    

    
    public void testNudgerFailure() {
        // create different configuration..
        nudger = new MockSchedulerNotifier(false); // set to fail
        monitor = new JobMonitor(nudger); 
        monitor.monitorJob(id,info);
        assertEquals(1,nudger.getCallCount());
        // should have barfed internally. check it can accept another call after.
        monitor.monitorJob(id,info);
        assertEquals(2,nudger.getCallCount());
    }
}


/* 
$Log: JobMonitorTest.java,v $
Revision 1.5  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.4  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.4.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.2.4.1  2004/02/17 12:57:11  nw
improved documentation
 
*/