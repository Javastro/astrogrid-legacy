/*$Id: GroovyAbortJobTest.java,v 1.6 2004/12/03 14:47:40 jdt Exp $
 * Created on 13-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test abort functionality works in script scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2004
 *
 */
public class GroovyAbortJobTest extends AbstractTestForSchedulerImpl {
    /** Construct a new ScriptedAbortJobTest
     * @param arg0
     */
    public GroovyAbortJobTest(String arg0) {
        super(arg0);
    }
    
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        // we know it already exists. 
        scheduler.abortJob(Castor2Axis.convert(urn));
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
$Log: GroovyAbortJobTest.java,v $
Revision 1.6  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.5.14.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.5  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.4.46.1  2004/11/05 16:08:26  nw
tidied imports

Revision 1.4  2004/08/18 21:50:59  nw
worked on tests

Revision 1.3  2004/08/13 09:10:05  nw
tidied imports

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/