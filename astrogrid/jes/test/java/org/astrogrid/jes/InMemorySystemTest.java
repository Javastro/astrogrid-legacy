/*$Id: InMemorySystemTest.java,v 1.15 2004/03/18 16:47:03 pah Exp $
 * Created on 19-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import EDU.oswego.cs.dl.util.concurrent.CyclicBarrier;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.DefaultComponentAdapterFactory;
import org.picocontainer.defaults.ImplementationHidingComponentAdapter;
import org.w3c.dom.Document;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.jes.component.BasicComponentManager;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.ShortCircuitDispatcher;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.WorkflowString;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/** Test that builds entire system (out of in-memory components), and feeds workflow documents into it.
 * <p>
 * tests behaviour. communication between components via direct method call - so no SOAP being tested here.
 * <p/>
 * tests that workflows fed into the system to get processed, and checks that all jobs in a workflow are executed.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class InMemorySystemTest extends AbstractTestWorkflowInputs {
    protected CyclicBarrier barrier = new CyclicBarrier(2);

    /** Construct a new InMemorySystemTest
     * @param arg
     */
    public InMemorySystemTest(String arg) {
        super(arg);
    }
    
    protected class TestComponentManager extends BasicComponentManager {
        public TestComponentManager() {
            super();    
            MutablePicoContainer pico = super.getContainer();
            // need to remove existing registrations.
            
            // disptcher that short-circuits back to a monitor.                     
            pico.unregisterComponent(Dispatcher.class);
            pico.registerComponentImplementation(Dispatcher.class,ShortCircuitDispatcher.class);
                       
            // scheduler that notifies of completion by releasing a barrier
            pico.unregisterComponent(SCHEDULER_ENGINE);
            DefaultComponentAdapterFactory fac = new DefaultComponentAdapterFactory();
            pico.registerComponent(
                new ImplementationHidingComponentAdapter(
                    new CachingComponentAdapter(
                        fac.createComponentAdapter(SCHEDULER_ENGINE,ObservableJobScheduler.class,null)
                        //new DefaultComponentAdapterFactory(JobScheduler.class,ObservableJobScheduler.class)
                    )
                  )
                );
            
            pico.registerComponentInstance(barrier);
                             
        }            
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        ComponentManager cm = new TestComponentManager();        
        ComponentManagerFactory._setInstance(cm);
    }
        
    protected static int WAIT_SECONDS = 5;
    
    protected JobController getController() throws Exception{
        return ComponentManagerFactory.getInstance().getController();
    }
    

    
    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        // parse the document, feed it into the system.
        String docString = FileResourceLoader.streamToString(is);
        assertNotNull(docString);
        JobURN urn = getController().submitWorkflow(new WorkflowString(docString));
        assertNotNull(urn);

        JobFactory fac = ComponentManagerFactory.getInstance().getFacade().getJobFactory();        
        // now wait for notification that the system has finished processing.
        try {
        barrier.attemptBarrier(Sync.ONE_SECOND * WAIT_SECONDS);
        } catch (TimeoutException te) {
            try {
                Workflow job = fac.findJob(Axis2Castor.convert(urn));
                dumpDocument(job,"workflow-timeout");
            } catch (Exception e) {
                System.out.println("everything's going wrong today");
                e.printStackTrace();
            }
            fail("timed out waiting for the scheduler to complete");
        }
        assertFalse("timed out waiting for the scheduler to complete",barrier.broken()); // if this is false, meanst that we timed out - need to increase the duration?
         
        Workflow job =  fac.findJob(Axis2Castor.convert(urn));
        assertNotNull(job);
        dumpDocument(job,"workflow-output");

        
        // verify that all steps have been run
        for (Iterator i = JesUtil.getJobSteps(job); i.hasNext(); ) {
            Step step = (Step)i.next();
            StepExecutionRecord exRec = JesUtil.getLatestOrNewRecord(step);
            assertEquals(1,exRec.getMessageCount());
            assertEquals(ExecutionPhase.COMPLETED,exRec.getStatus());
        }

        assertEquals(ExecutionPhase.COMPLETED,job.getJobExecutionRecord().getStatus());
         
    }
    
  protected void dumpDocument(Workflow job,String filename) throws ParserConfigurationException, MarshalException, ValidationException, IOException {        
    Document doc = XMLUtils.newDocument();
    Marshaller.marshal(job,doc);
    OutputStream outS = new FileOutputStream(filename + "-" + resourceNum + ".xml");
    XMLUtils.PrettyDocumentToStream(doc,outS);
    outS.close();    
  }
    /** extended job scheduler that will notify us when tasks are complete. 
     *does this by entering barrier when done - which releases other (presumbably blocked) thread.*/
    public static class ObservableJobScheduler extends org.astrogrid.jes.jobscheduler.impl.SchedulerImpl {

        /** Construct a new ObservableJobScheduler
         * @param facade
         * @param dispatcher
         * @param policy
         */
        public ObservableJobScheduler(BeanFacade facade, Dispatcher dispatcher, Policy policy,CyclicBarrier barrier){
            super(facade, dispatcher, policy);
            this.barrier = barrier;

        }
        protected CyclicBarrier barrier;
        /**
         * @see org.astrogrid.jes.jobscheduler.JobScheduler#notifyJobFinished(org.astrogrid.jes.job.Job)
         */
        public void notifyJobFinished(Workflow job) {   
            try {
                barrier.barrier();
            } catch (InterruptedException e) {
                throw new RuntimeException("Barrier broken");
            }
        }


    }
}


/* 
$Log: InMemorySystemTest.java,v $
Revision 1.15  2004/03/18 16:47:03  pah
fixed cvs confic induced errors (I think!)

Revision 1.14  2004/03/18 16:43:05  pah
moved the axis2castor stuff to the common project under beans package

Revision 1.13  2004/03/18 01:30:15  nw
improved reporting when there's a timeout

Revision 1.12  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.11  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.10  2004/03/14 23:03:16  nw
fixed to fit in with naming change in picocontainer

Revision 1.9  2004/03/09 14:42:18  nw
updated to track version change in picocontainer

Revision 1.8  2004/03/09 14:24:16  nw
upgraded to new job controller wsdl

Revision 1.7  2004/03/08 00:37:23  nw
tidied up

Revision 1.6  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.5.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.5  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/