/*$Id: ApplicationControllerIdDispatcher.java,v 1.2 2004/07/09 09:30:28 nw Exp $
 * Created on 12-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

/** same dispatcher, but we passing in a token containing id, rather than xpath.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2004
 *
 */
public class ApplicationControllerIdDispatcher extends ApplicationControllerDispatcher {
    /** Construct a new ApplicationControllerIdDispatcher
     * @param locator
     * @param endpoint
     */
    public ApplicationControllerIdDispatcher(Locator locator, Endpoints endpoint) {
        super(locator, endpoint);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher#createToken(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step)
     */
    protected JobIdentifierType createToken(Workflow job, Step js) {
        return  JesUtil.createJobId(job.getJobExecutionRecord().getJobId(), js.getId());
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Extends Application Controller Dispatcher, but identifies step by ID rather than XPATH."
            + super.getDescription();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Application Controller ID Dispatcher";
    }

}


/* 
$Log: ApplicationControllerIdDispatcher.java,v $
Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:20  nw
first checkin of prototype scrpting workflow interpreter
 
*/