/*$Id: MockJobFactoryImpl.java,v 1.6 2004/03/15 01:29:13 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.Iterator;

import junit.framework.Test;

/** Mock, makes AbstractJobFactoryImpl non-abstract. 
 * <p />
 * Can also be configured to fail on jobCreation / update / delete..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class MockJobFactoryImpl extends AbstractJobFactoryImpl implements ComponentDescriptor {
    /** Construct a new MockJobFactoryImpl
     * 
     */
    public MockJobFactoryImpl() {
        this(true);
    }
    /**
     *  Construct a new MockJobFactoryImpl
     * @param willSucceed set to false to make methods throw.
     */
    public MockJobFactoryImpl(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    protected boolean willSucceed;
    /**
     * @see org.astrogrid.jes.job.JobFactory#createJob(org.astrogrid.jes.job.SubmitJobRequest)
     */
    public Workflow createJob(SubmitJobRequest req) throws JobException {
            if (! willSucceed) {
                throw new JobException("You wanted me to fail");
            }
            return buildJob(req);
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Workflow findJob(JobURN jobURN) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
        return null;
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(Account acc) throws JobException {
        if (! willSucceed) {
            throw new JobException("You wanted me to fail");
        }
        return new Iterator() {

            public void remove() {
            }

            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };        
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#deleteJob(org.astrogrid.jes.job.Job)
     */
    public void deleteJob(Workflow job) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(Workflow job) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Mock Job Factory";
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
$Log: MockJobFactoryImpl.java,v $
Revision 1.6  2004/03/15 01:29:13  nw
factored component descriptor out into separate package

Revision 1.5  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.2  2004/02/17 15:47:04  nw
updated to throw exceptions when jobs are not found

Revision 1.1.2.1  2004/02/12 15:42:16  nw
started testing castor code.
 
*/