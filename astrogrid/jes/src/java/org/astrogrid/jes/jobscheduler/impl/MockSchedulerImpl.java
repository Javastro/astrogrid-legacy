/*$Id: MockSchedulerImpl.java,v 1.4 2004/04/08 14:43:26 nw Exp $
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

import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import junit.framework.Test;

/** Mock implementation of a JobScheduler - just counts the number of times its methods are called.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MockSchedulerImpl implements JobScheduler , ComponentDescriptor{
    /** Construct a new MockJobScheduler
     * 
     */
    public MockSchedulerImpl() {
        this(true);
    }
    public MockSchedulerImpl(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    protected final boolean willSucceed;
    protected int callCount = 0;
    public int getCallCount() {
        return callCount;
    }
    /**
     * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void scheduleNewJob(JobURN request) throws Exception {
        callCount++;
        if (! willSucceed) {
            throw new Exception("You wanted me to fail");
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void resumeJob(JobIdentifierType arg0, MessageType arg1) throws Exception {
        callCount++;
        if (! willSucceed) {
            throw new Exception("you wanted me to fail");
        }
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Mock Job Scheduler";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Mock implementation - does nothing apart from count methods called\n" +
            (willSucceed ? "Set to succeed at each method call" : "Set to fail at each method call: will thrown exceptions");
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#abortJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void abortJob(JobURN jobURN) throws Exception {
        callCount++;
        if (! willSucceed) {
            throw new Exception("you wanted me to fail");
        }
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void deleteJob(JobURN jobURN) throws Exception {
        callCount++;
        if (! willSucceed) {
            throw new Exception("you wanted me to fail");
        }
    }
    /**
     * 
     */
    public void resetCallCount() {
        callCount = 0;
    }
}


/* 
$Log: MockSchedulerImpl.java,v $
Revision 1.4  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.3  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.2  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

Revision 1.1  2004/03/15 00:30:54  nw
factored implemetation of scheduler out of parent package.

Revision 1.6  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.5  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:42  nw
moved from test hierarchy.

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/