/*$Id: LinearPolicy.java,v 1.8 2004/04/08 14:43:26 nw Exp $
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
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.util.Iterator;

/** Policy that executes all jobs in a  purely linear fashion, with no concurrency.
 * <p>
 * Doesn't consider join conditions - will run all steps.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class LinearPolicy extends AbstractPolicy implements Policy {
    /** Construct a new LinearPolicy
     * 
     */
    public LinearPolicy() {
        super();
        logger.info("Creating Linear Policy");
        this.name =  "LinearPolicy";
        this.description = "Executes job steps in a top-to-bottom, purely sequential manner. treats flows as sequences. ignores join conditions.";       
    }
    /** 
     * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public ExecutionPhase currentJobStatus(Workflow job) {
        logger.debug("LinearPolicy::currentJobStatus()");
        registerFunctions(job);
        // special case for empty workflow
        if (job.findXPathValue("//*[jes:isStep()]") == null) {
            logger.debug("empty workflow - so automatically completed");
            return ExecutionPhase.COMPLETED;
        }  
        Iterator i = job.findXPathIterator("//*[jes:isStep()]");
        
        boolean runningFound = false;       
        boolean pendingFound = false;
        boolean noStatusFound = true;
        while (i.hasNext()) {
            noStatusFound = false;
            Step step = (Step)i.next();
            ExecutionPhase s = getLatestExecutionPhase(step,ExecutionPhase.PENDING);
            if (s.equals(ExecutionPhase.ERROR) || s.equals(ExecutionPhase.UNKNOWN)) {
                logger.debug("saw error or unknown - returning immediately");
                return s;
            }
            if (s.equals(ExecutionPhase.RUNNING)) {
                logger.debug("saw running");
                runningFound = true;
            }
            if (s.equals(ExecutionPhase.PENDING)) {
                logger.debug("saw pending");
                pendingFound = true;
            }
        }

        if (runningFound) {
            logger.debug("returning 'RUNNING'");            
            return ExecutionPhase.RUNNING;
        }
        if (noStatusFound || pendingFound) { // and no running, or error...
            logger.debug("returning 'PENDING'");
            return ExecutionPhase.PENDING;
        } 
        // everything must be completed then..
        logger.debug("returning 'COMPLETED'");
        return ExecutionPhase.COMPLETED;
   }      

    /** Returns a step that either has no execution record, or an execution record that has pending status.
     * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Step nextExecutableStep(Workflow job) {
        logger.debug("LinearPolicy::nextExecutableStep");
        registerFunctions(job);

        Iterator i = job.findXPathIterator("//*[jes:isStep()]"); // returns flattened list of all steps - we are relying on the order these are returned in really.
        boolean justSeenComplete = true; // necessary, special case for first step.
        while (i.hasNext()) {
            Step s = (Step)i.next();
            ExecutionPhase phase = getLatestExecutionPhase(s,ExecutionPhase.PENDING);
                if (justSeenComplete && phase.equals(ExecutionPhase.PENDING)) {
                    logger.debug("found candidate");
                    return s;                
                }
                if (phase.equals(ExecutionPhase.COMPLETED)) {
                    justSeenComplete = true;
                } else {
                    justSeenComplete = false;
                }         
          }       
        logger.debug("no executable steps found");
        return null;
    }

    
   
    
}


/* 
$Log: LinearPolicy.java,v $
Revision 1.8  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.7  2004/03/18 10:54:52  nw
factored helper method into base class

Revision 1.6  2004/03/18 01:28:43  nw
corrected implement - does what it claims to now.

Revision 1.5  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.4  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

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