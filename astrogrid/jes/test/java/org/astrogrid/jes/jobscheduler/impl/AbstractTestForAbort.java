/*$Id: AbstractTestForAbort.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 08-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test abort job method call.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Apr-2004
 *
 */
public abstract class AbstractTestForAbort extends AbstractTestForSchedulerImpl {
    /**
     * Constructor for AbortJobTest.
     * @param arg0
     */
    public AbstractTestForAbort(String arg0) {
        super(arg0);
    }

    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        // we know it already exists. 
        scheduler.abortJob(JesUtil.castor2axis(urn));
        Workflow wf = this.fac.findJob(urn);
        assertEquals(ExecutionPhase.ERROR,wf.getJobExecutionRecord().getStatus());
        MessageType[] messages = wf.getJobExecutionRecord().getMessage();
        assertTrue(messages.length > 0);
        MessageType lastMessage = messages[messages.length-1];
        assertNotNull(lastMessage);
        assertTrue(lastMessage.getContent().startsWith("Aborted"));
    }
}


/* 
$Log: AbstractTestForAbort.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.2  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1  2004/04/08 14:47:12  nw
added delete and abort job functionality
 
*/