/*$Id: FlowPolicy.java,v 1.2 2004/04/08 14:43:26 nw Exp $
 * Created on 18-Mar-2004
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
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Policy implementation that executes flows in parallel, and respects join conditions.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2004
 *
 */
public class FlowPolicy extends AbstractPolicy {
    /** Construct a new FlowPolicy
     * 
     */
    public FlowPolicy() {
        super();
           logger.info("Creating Flow Policy");
           this.name =  "FlowPolicy";
           this.description = "Executes job steps in a parallel manner where possible. respects join conditions.";       
 
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public ExecutionPhase currentJobStatus(Workflow job) {
        logger.error("Flow Policy: unimplemented");
        return null;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Step nextExecutableStep(Workflow job) {
        logger.error("Flow Policy: unimplemented");
        return null;
    }
}


/* 
$Log: FlowPolicy.java,v $
Revision 1.2  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.1  2004/03/18 10:55:55  nw
code stub - needs to be implemented
 
*/