/*$Id: MockPolicy.java,v 1.4 2004/03/04 01:57:35 nw Exp $
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
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

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
    public ExecutionPhase calculateJobStatus(Workflow job) {
        return ExecutionPhase.RUNNING;
    }
    /**
     * @see org.astrogrid.jes.policy.SchedulingPolicy#calculateDispatchableCandidates(org.astrogrid.jes.job.Job)
     */
    public Iterator calculateDispatchableCandidates(Workflow job) {
        List result = new ArrayList();
        for (Iterator i = JesUtil.getJobSteps(job); i.hasNext(); ) {
            Step js = (Step)i.next();
            ExecutionPhase status = JesUtil.getLatestRecord(js).getStatus();
            if (ExecutionPhase.UNKNOWN.equals(status)) {
                result.add(js);
            }
        }
        //System.out.println("*** candidates length :" + result.size());
        return result.iterator();
        
    }
}


/* 
$Log: MockPolicy.java,v $
Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/