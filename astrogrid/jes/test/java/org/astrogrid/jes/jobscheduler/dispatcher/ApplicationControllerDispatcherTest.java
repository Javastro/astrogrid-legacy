/*$Id: ApplicationControllerDispatcherTest.java,v 1.7 2004/03/24 11:47:13 pah Exp $
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
import java.net.URL;

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
        final URL monitorURL =  new URL("http://www.nowhere.org");
        ApplicationControllerDispatcher.MonitorEndpoint monitor = new ApplicationControllerDispatcher.MonitorEndpoint() {

            public URL getURL() {
                return monitorURL;
            }
        };
        disp = new ApplicationControllerDispatcher(locator,monitor);
    }
    protected ApplicationControllerDispatcher disp;
    
    public void testDispatchStep(Workflow w,Step js) throws Exception {
        disp.dispatchStep(w,js);
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