/*$Id: MockJobFactoryImpl.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.types.v1.JobURN;

import java.util.Iterator;

/** Mock, makes AbstractJobFactoryImpl non-abstract. 
 * <p />
 * Can also be configured to fail on jobCreation / update / delete..
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public class MockJobFactoryImpl extends AbstractJobFactoryImpl {
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
    public Job createJob(SubmitJobRequest req) throws JobException {
            if (! willSucceed) {
                throw new JobException("You wanted me to fail");
            }
            return buildJob(req);
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findJob(java.lang.String)
     */
    public Job findJob(JobURN jobURN) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
        return null;
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
     */
    public Iterator findUserJobs(String userid, String community, String jobListXML) throws JobException {
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
    public JobURN deleteJob(Job job) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
        return job.getId();
    }
    /**
     * @see org.astrogrid.jes.job.JobFactory#updateJob(org.astrogrid.jes.job.Job)
     */
    public void updateJob(Job job) throws JobException {
        if (! willSucceed) {
            throw new NotFoundException("You wanted me to fail");
        }
    }
}


/* 
$Log: MockJobFactoryImpl.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.2  2004/02/17 15:47:04  nw
updated to throw exceptions when jobs are not found

Revision 1.1.2.1  2004/02/12 15:42:16  nw
started testing castor code.
 
*/