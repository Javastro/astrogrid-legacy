/*$Id: FullPolicy.java,v 1.1 2004/04/21 16:39:53 nw Exp $
 * Created on 21-Apr-2004
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

/** Full, parallel scheduling policy that respects join conditions.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class FullPolicy extends AbstractPolicy {
    /** Construct a new FullPolicy
     * 
     */
    public FullPolicy() {
        super();
        logger.info("Creating Full Policy");
        this.name =  "FullPolicy";
        this.description = "Executes job steps in a parallel manner where possible. respects join conditions.";          
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public ExecutionPhase currentJobStatus(Workflow job) {
        return null;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public Step nextExecutableStep(Workflow job) {
        return null;
    }
}


/* 
$Log: FullPolicy.java,v $
Revision 1.1  2004/04/21 16:39:53  nw
rewrote policy implementations to use object models
 
*/