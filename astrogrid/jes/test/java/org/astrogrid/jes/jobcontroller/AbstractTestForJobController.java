/*$Id: AbstractTestForJobController.java,v 1.3 2004/03/09 14:23:54 nw Exp $
 * Created on 17-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.IOException;
import java.io.InputStream;

/**Abstract base class for testing the job controller - handles the donkey work of feeding the controller jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public abstract class AbstractTestForJobController extends AbstractTestWorkflowInputs {
    /** Construct a new AbstractTest
     * 
     */
    public AbstractTestForJobController(String s) {
        super(s);
    }
    
    
    protected void setUp() throws Exception {
        super.setUp();
        fac = createJobFactory();
        facade = createFacade(fac);
        nudger = createNotifier();
        jc = new JobController(facade,nudger);
    }
    
    /**
     *Override to set up different test conditions
     */
    protected SchedulerNotifier createNotifier() {
        return new MockSchedulerNotifier();
    }


    /**
     * @param fac
     * @return
     */
    protected BeanFacade createFacade(AbstractJobFactoryImpl fac) {
        return new CastorBeanFacade(fac);
    }


    /**
     * @return
     */
    protected AbstractJobFactoryImpl createJobFactory() {
        return new InMemoryJobFactoryImpl();
    }


    /** fill this in to test behaviour of job submission */
    protected abstract void performTest(JobURN urn) throws Exception;


    protected AbstractJobFactoryImpl fac;


    protected BeanFacade facade;


    protected SchedulerNotifier nudger;


    protected JobController jc;


    /**
     * @see org.astrogrid.jes.impl.workflow.WorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        JobURN urn = submitJob(is);
        performTest(urn);
        
    }

    protected Exception seenException; 
    /** helper method to submit a job */
    protected JobURN submitJob(InputStream is) throws Exception {
        String workflowXML = FileResourceLoader.streamToString(is);
        SubmitJobRequest req = facade.createSubmitJobRequest(workflowXML);
        assertNotNull(req);
        try {
            return JesUtil.axis2castor(jc.submitJob(req));
        } catch (IOException e) {
            seenException = e;
            return null;
        }
    }
}


/* 
$Log: AbstractTestForJobController.java,v $
Revision 1.3  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/