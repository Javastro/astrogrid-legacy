/*$Id: ApplicationControllerDispatcherTest.java,v 1.12 2004/08/09 17:31:11 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.locator.MockLocator;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/** Unit test for the application controller dispatcher.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class ApplicationControllerDispatcherTest extends AbstractTestWorkflowInputs{
    /**
     * Constructor for ApplicationControllerDispatcherTest.
     * @param arg0
     */
    public ApplicationControllerDispatcherTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        Locator locator = new MockLocator();
        final URI monitorURL =  new URI("http://www.nowhere.org");
        ApplicationControllerDispatcher.Endpoints monitor = new ApplicationControllerDispatcher.Endpoints() {

            public URI monitorEndpoint() {
                return monitorURL;
            }

            public URI resultListenerEndpoint() {
                return monitorURL;
            }
        };
        disp = new ApplicationControllerDispatcher(locator,monitor);
    }
    protected ApplicationControllerDispatcher disp;
    
    public void testDispatchStep(Workflow w,Step js) throws Exception {
        disp.dispatchStep(w,js.getTool(),"someID");
    }

    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
       Workflow w = Workflow.unmarshalWorkflow(new InputStreamReader(is));
       // add in an execution block.
       JobExecutionRecord jex = new JobExecutionRecord();
       JobURN urn = new JobURN();
        urn.setContent("jes:dummy:job");
       jex.setJobId(urn);
       w.setJobExecutionRecord(jex);
       if (getInputFileNumber() == EMPTY_WORKFLOW) {
           assertFalse(JesUtil.getJobSteps(w).hasNext());
           return;
       }
        Step js = (Step)JesUtil.getJobSteps(w).next();
        assertNotNull(js); 
        testDispatchStep(w,js);

    }
}


/* 
$Log: ApplicationControllerDispatcherTest.java,v $
Revision 1.12  2004/08/09 17:31:11  nw
adjusted interface, to work better with dynamically-generated states.

Revision 1.11  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.9  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.8  2004/07/01 11:20:07  nw
updated interface with cea - part of cea componentization

Revision 1.7  2004/03/24 11:47:13  pah
removed the buildParameters test - not needed as the new CEC delegate uses workflow objects directly

Revision 1.6  2004/03/10 14:37:35  nw
adjusted tests to handle an empty workflow

Revision 1.5  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.4  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code
 
*/