/*$Id: MockPolicy.java,v 1.8 2004/03/18 01:29:17 nw Exp $
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
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.Test;

/** Mock policy - will retun all jobs that aren;'t marked as completed.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MockPolicy extends AbstractPolicy implements Policy{
    /** Construct a new MockPolicy
     * 
     */
    public MockPolicy() {
        super();
        logger.info("Creating Mock Policy - job is always RUNNING, schedules every pending step");
        this.name = "MockPolicy";
        this.description =  "Always returns a running job status, finds all pending job steps";
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public ExecutionPhase currentJobStatus(Workflow job) {
        return ExecutionPhase.RUNNING;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Step nextExecutableStep(Workflow job) {
        registerFunctions(job);
        return (Step)job.findXPathValue("//*[jes:isStep() and jes:latestStatus() = '" + ExecutionPhase.PENDING + "']");
    }
}


/* 
$Log: MockPolicy.java,v $
Revision 1.8  2004/03/18 01:29:17  nw
tidied

Revision 1.7  2004/03/15 01:29:13  nw
factored component descriptor out into separate package

Revision 1.6  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.5.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.5  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

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