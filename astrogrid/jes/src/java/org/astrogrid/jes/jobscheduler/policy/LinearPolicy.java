/*$Id: LinearPolicy.java,v 1.3 2004/03/10 14:37:21 nw Exp $
 * Created on 04-Mar-2004
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
import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.util.Iterator;

import junit.framework.Test;

/** Policy that executes all jobs in a  purely linear fashion. ignores data deps.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class LinearPolicy extends AbstractPolicy implements Policy , ComponentDescriptor{
    /** Construct a new LinearPolicy
     * 
     */
    public LinearPolicy() {
        super();
        logger.info("Creating Linear Policy");
    }
    /** returns
     * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public ExecutionPhase currentJobStatus(Workflow job) {
        registerFunctions(job);
        // special case for empty workflow
        if (job.findXPathValue("//*[jes:isStep()]") == null) {
            return ExecutionPhase.COMPLETED;
        }  
        Iterator i = job.findXPathIterator("//*[jes:isStep()]/stepExecutionRecord/status");
        boolean runningFound = false;       
        boolean pendingFound = false;
        boolean noStatusFound = true;
        while (i.hasNext()) {
            noStatusFound = false;
            ExecutionPhase s = (ExecutionPhase)i.next();
            if (s.equals(ExecutionPhase.ERROR) || s.equals(ExecutionPhase.UNKNOWN)) {
                return s;
            }
            if (s.equals(ExecutionPhase.RUNNING)) {
                runningFound = true;
            }
            if (s.equals(ExecutionPhase.PENDING)) {
                pendingFound = true;
            }
        }

        if (runningFound) {
            return ExecutionPhase.RUNNING;
        }
        if (noStatusFound || pendingFound) { // and no running, or error...
            return ExecutionPhase.PENDING;
        } 
        // everything must be completed then..
        return ExecutionPhase.COMPLETED;
   }      

    /** Returns a step that either has no execution record, or an execution record that has pending status.
     * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Step nextExecutableStep(Workflow job) {
        registerFunctions(job);
        return (Step)job.findXPathValue("//*[jes:isPendingStep()]");
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "LinearPolicy";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Executes job steps in a top-to-bottom, purely sequential manner. treats flows as sequences, ignored data dependencies between steps";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    
   
    
}


/* 
$Log: LinearPolicy.java,v $
Revision 1.3  2004/03/10 14:37:21  nw
fixed to retrun 'completed' immediately when it sees an empty workflow

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.1  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/