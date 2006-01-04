/*$Id: AbstractTestForJobController.java,v 1.12 2006/01/04 09:52:32 clq2 Exp $
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

import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CachingFileJobFactory;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.impl.MockSchedulerImpl;
import org.astrogrid.jes.util.TemporaryBaseDirectory;
import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        nudger = createNotifier();
        jc = new JobController(fac,nudger,new TemporaryBuffer());
    }
    
    /**
     *Override to set up different test conditions
     */
    protected JobScheduler createNotifier() {
        return new MockSchedulerImpl();
    }


 

    /**
     * @return
     */
    protected AbstractJobFactoryImpl createJobFactory() throws Exception {
      TemporaryBaseDirectory d = new TemporaryBaseDirectory();
      return new CachingFileJobFactory(d);
    }


    /** fill this in to test behaviour of job submission */
    protected abstract void performTest(JobURN urn) throws Exception;


    protected AbstractJobFactoryImpl fac;




    protected JobScheduler nudger;


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
        Workflow req = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        try {            
            return Axis2Castor.convert(jc.submitJob(req));
        } catch (IOException e) {
            seenException = e;
            return null;
        }
    }
}


/* 
$Log: AbstractTestForJobController.java,v $
Revision 1.12  2006/01/04 09:52:32  clq2
jes-gtr-1462

Revision 1.11.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.11  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.10.56.2  2005/04/11 16:31:14  nw
updated version of xstream.
added caching to job factory

Revision 1.10.56.1  2005/04/11 13:57:56  nw
altered to use fileJobFactory instead of InMemoryJobFactory - more realistic

Revision 1.10  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.9.64.1  2004/11/05 16:01:01  nw
updated test to provide temporary buffer

Revision 1.9  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.8.20.1  2004/07/30 14:00:10  nw
first working draft

Revision 1.8  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.7  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.6  2004/03/18 16:42:52  pah
moved the axis2castor stuff to the common project under beans package

Revision 1.5  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.4  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.3  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/