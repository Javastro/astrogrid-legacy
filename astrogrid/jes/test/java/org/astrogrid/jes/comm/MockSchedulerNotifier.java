/*$Id: MockSchedulerNotifier.java,v 1.4 2004/03/07 21:04:39 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;

import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import junit.framework.Test;

/** Mock implementation of a scheduler notifier. doesn't communicate. 
 * <p />
 * can be instructred to fail.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class MockSchedulerNotifier implements SchedulerNotifier, ComponentDescriptor {
    /** Construct a new MockSchedulerNotifier
     * 
     */
    public MockSchedulerNotifier() {
        this(true);
    }
    protected int count = 0;
    /**
     *  Construct a new MockSchedulerNotifier
     * @param willSucceed if false, will fail with exception.
     */
    public MockSchedulerNotifier(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    protected final boolean willSucceed;


    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void resumeJob(JobIdentifierType id, MessageType mt) throws Exception {
        count++;
        if (! this.willSucceed) {
            throw new Exception("you wanted me to fail");
        }
    }
    
    public int getCallCount() {
        return count;
    }
    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#scheduleNewJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void scheduleNewJob(JobURN urn) throws Exception {
        count++;
        if (!this.willSucceed) {
            throw new Exception("you wanted me to fail");
        }        
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Mock Scheduler Notifier";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Mock, just counts number of method calls\n"
            + (willSucceed ? "configured to succeed on method call" : "configured to fail on method call, throwing an exception");
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: MockSchedulerNotifier.java,v $
Revision 1.4  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.3.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/12 12:55:21  nw
factored out component used by web services to notify the scheduler. added to IoC pattern
 
*/