/*$Id: MockJobScheduler.java,v 1.5 2004/03/07 21:04:39 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.comm.JobScheduler;
import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import java.rmi.RemoteException;

import junit.framework.Test;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MockJobScheduler implements JobScheduler , ComponentDescriptor{
    /** Construct a new MockJobScheduler
     * 
     */
    public MockJobScheduler() {
        this(true);
    }
    public MockJobScheduler(boolean willSucceed) {
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
    public void scheduleNewJob(JobURN request) throws RemoteException {
        callCount++;
        if (! willSucceed) {
            throw new RemoteException("You wanted me to fail");
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void resumeJob(JobIdentifierType arg0, MessageType arg1) throws RemoteException {
        callCount++;
        if (! willSucceed) {
            throw new RemoteException("you wanted me to fail");
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
}


/* 
$Log: MockJobScheduler.java,v $
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