/*$Id: SOAPSchedulerNotifier.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobScheduler;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.JobURN;

/** Implementation of scheduler notifier that uses the scheduler delegate to communicate to a scheduler instance via soap.
 * this is how the rough implemnetation does it, but its a bit long winded. Especially as the scheduler and other components have to be co-located to be able to 
 * share the same database.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class SOAPSchedulerNotifier implements SchedulerNotifier {
    /** Construct a new SOAPSchedulerNotifier
     * 
     */
    public SOAPSchedulerNotifier(String endpoint) {        
        delegate = JesDelegateFactory.createJobScheduler(endpoint);
    }
    protected String endpoint;
    protected JobScheduler delegate;
    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.job.Job)
     */
    public void scheduleNewJob(JobURN urn) throws Exception {
        delegate.scheduleNewJob(urn);        
    }
    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobInfo i) throws Exception {
        delegate.resumeJob(i);
    }
}


/* 
$Log: SOAPSchedulerNotifier.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:33:17  nw
removed rough scheduler notifier, replaced with one
using SOAP delegate.

added in-memory concurrent notifier
 
*/