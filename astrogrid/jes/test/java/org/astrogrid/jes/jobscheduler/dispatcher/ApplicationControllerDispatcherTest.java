/*$Id: ApplicationControllerDispatcherTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.impl.workflow.JobImpl;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.locator.MockLocator;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

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
    
    public void testDispatchStep(JobStep js) throws Exception {
        disp.dispatchStep(null,js);
    }
    /** @todo - broken at the moment - need to sort out community / authentication, etc.*/
    public void testBuildUser(JobStep js) throws Exception {
        User u = disp.buildUser(js);
        assertNotNull(u);
        //assertNotNull(u.getToken());
        //assertNotNull(u.getAccount());
        //assertNotNull(u.getGroup());
    }
    public void testBuildParameterValues(JobStep js) throws Exception{
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
        Job job =new JobImpl(w);
        JobStep js = (JobStep)job.getJobSteps().next();
        assertNotNull(js); 
        testDispatchStep(js);
        testBuildParameterValues(js);        
        testBuildUser(js);

    }
}


/* 
$Log: ApplicationControllerDispatcherTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code
 
*/