/*$Id: MockPolicy.java,v 1.3 2004/03/03 01:13:42 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Policy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Mock policy - will retun all jobs that aren;'t marked as completed.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MockPolicy implements Policy {
    /** Construct a new MockPolicy
     * 
     */
    public MockPolicy() {
        super();
    }
    /**
     * @see org.astrogrid.jes.policy.SchedulingPolicy#calculateJobStatus(org.astrogrid.jes.job.Job)
     */
    public ExecutionPhase calculateJobStatus(Job job) {
        return ExecutionPhase.RUNNING;
    }
    /**
     * @see org.astrogrid.jes.policy.SchedulingPolicy#calculateDispatchableCandidates(org.astrogrid.jes.job.Job)
     */
    public Iterator calculateDispatchableCandidates(Job job) {
        List result = new ArrayList();
        for (Iterator i = job.getJobSteps(); i.hasNext(); ) {
            JobStep js = (JobStep)i.next();
            if (ExecutionPhase.UNKNOWN.equals(js.getStatus())) {
                result.add(js);
            }
        }
        //System.out.println("*** candidates length :" + result.size());
        return result.iterator();
        
    }
}


/* 
$Log: MockPolicy.java,v $
Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/