/*$Id: GroovySchedulerSetupTest.java,v 1.3 2004/08/09 17:32:18 nw Exp $
 * Created on 12-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.Extension;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test we can setup - serialize and deserialize a workflow with the extensionis needed for the scripted scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2004
 *
 */
public class GroovySchedulerSetupTest extends AbstractTestForSchedulerImpl {
    /** Construct a new ScriptedSchedulerSetupTest
     * @param arg0
     */
    public GroovySchedulerSetupTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        Workflow wf = fac.findJob(urn);
        assertNotNull("workflow not found",wf);
        GroovySchedulerImpl sched = (GroovySchedulerImpl)scheduler;
        final Workflow wf1 = sched.initializeJob(wf);
        assertNotNull(wf1);
        // wf1 should have id fields. check they're there.
        assertNotNull(wf1.getSequence().getId());
        // now should have load of extensions. check they're all there.
        Extension[] arr = wf1.getJobExecutionRecord().getExtension();
        assertEquals(1,arr.length);
        boolean foundPickle = false;
        for (int i = 0; i < arr.length; i++) {
            Extension e = arr[i];
            if (e.getKey().equals(GroovyInterpreterFactory.EXTENSION_KEY)) {
                foundPickle = true;
               assertNotNull(e.getContent());
            }            
        }
        assertTrue(foundPickle); 

        
        // now check we can read pickle back in again.
        JesInterface env = new JesInterface(wf1,dispatcher,sched); 
        

        GroovyInterpreter interp = (new GroovyInterpreterFactory(new XStreamPickler())).unpickleFrom(env);
        assertNotNull(interp);
        assertTrue(interp.ruleStore.size() > 0); // we've got some rules.
        //@todo add some equality checking in here..
        
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl#createScheduler()
     */
    protected AbstractJobSchedulerImpl createScheduler() throws Exception {

        return new GroovySchedulerImpl(fac,new GroovyTransformers(),dispatcher,new GroovyInterpreterFactory(new XStreamPickler())); 
    }

}


/* 
$Log: GroovySchedulerSetupTest.java,v $
Revision 1.3  2004/08/09 17:32:18  nw
updated due to removing RuleStore

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

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