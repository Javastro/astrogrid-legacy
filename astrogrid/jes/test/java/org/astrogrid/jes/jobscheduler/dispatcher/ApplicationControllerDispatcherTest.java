/*$Id: ApplicationControllerDispatcherTest.java,v 1.4 2004/03/05 16:16:55 nw Exp $
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

import org.astrogrid.applications.delegate.beans.ParameterValues;
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
        URL monitorURL = new URL("http://www.nowhere.org");
        disp = new ApplicationControllerDispatcher(locator,monitorURL);
    }
    protected ApplicationControllerDispatcher disp;
    
    public void testDispatchStep(Workflow w,Step js) throws Exception {
        disp.dispatchStep(w,js);
    }

    public void testBuildParameterValues(Step js) throws Exception{
        ParameterValues val = disp.buildParameterValues(js);
        assertNotNull(val);
        assertNotNull(val.getMethodName());
        assertNotNull(val.getParameterSpec());
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
        Step js = (Step)JesUtil.getJobSteps(w).next();
        assertNotNull(js); 
        testDispatchStep(w,js);
        testBuildParameterValues(js);   

    }
}


/* 
$Log: ApplicationControllerDispatcherTest.java,v $
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