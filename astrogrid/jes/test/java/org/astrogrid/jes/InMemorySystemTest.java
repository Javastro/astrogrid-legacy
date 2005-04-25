/*$Id: InMemorySystemTest.java,v 1.25 2005/04/25 12:13:54 clq2 Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.component.BasicJesComponentManager;
import org.astrogrid.jes.component.JesComponentManager;
import org.astrogrid.jes.component.JesComponentManagerFactory;
import org.astrogrid.jes.delegate.impl.JobControllerDelegateImpl;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.ShortCircuitDispatcher;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovyInterpreterFactory;
import org.astrogrid.jes.jobscheduler.impl.groovy.GroovySchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.alternatives.ImplementationHidingComponentAdapter;
import org.picocontainer.defaults.CachingComponentAdapter;
import org.picocontainer.defaults.DefaultComponentAdapterFactory;
import org.w3c.dom.Document;

import EDU.oswego.cs.dl.util.concurrent.CyclicBarrier;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

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
    
    protected class TestComponentManager extends BasicJesComponentManager {
        public TestComponentManager() {
            super();    
            MutablePicoContainer pico = super.getContainer();
            DefaultComponentAdapterFactory fac = new DefaultComponentAdapterFactory();
            // need to remove existing registrations.
            
            // disptcher that short-circuits back to a monitor.                     
            pico.unregisterComponent(Dispatcher.class);
            pico.registerComponent(
                    new ImplementationHidingComponentAdapter(
                            new CachingComponentAdapter(
                                    fac.createComponentAdapter(Dispatcher.class,ShortCircuitDispatcher.class,null)
                                    )
                             ,false)
                   );
                                  
                       
            // scheduler that notifies of completion by releasing a barrier
            pico.unregisterComponent(SCHEDULER_ENGINE);

            pico.registerComponent(
               new ImplementationHidingComponentAdapter( // use this to allow us to tye the knot.
                    new CachingComponentAdapter(
                        fac.createComponentAdapter(SCHEDULER_ENGINE,ObservableJobScheduler.class,null)
                        //new DefaultComponentAdapterFactory(JobScheduler.class,ObservableJobScheduler.class)
                    )
                  ,false) // if true, imprlementation-hiding-component-adapter is in 'strict' mode, and doesn't like our choice of component key.
                );
            
            pico.registerComponentInstance(barrier);
                             
        }            
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        JesComponentManager cm = new TestComponentManager();        
        JesComponentManagerFactory._setInstance(cm);
    
    }
        protected Account acc = new Account();        
        {
            acc.setCommunity("jodrell");
            acc.setName("nww74");    
        }
    /** time to wait for scheduler to process job */
    protected static int WAIT_SECONDS = 60;
    /** time to wait for scheduler to complete writing back */
    protected static int LAG_SECONDS = 1;
    
    protected JobController getController() throws Exception{
        return JesComponentManagerFactory.getInstance().getController();
    }
    

    
    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        // check we've an empty store.
        org.astrogrid.jes.delegate.JobController controller = new JobControllerDelegateImpl(getController());
        WorkflowSummaryType[] list = controller.listJobs(acc);
        assertNotNull(list);
        assertEquals(0,list.length);
        // parse the document, feed it into the system.
        assertNotNull(is);
        Workflow wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        org.astrogrid.workflow.beans.v1.execution.JobURN urn = controller.submitWorkflow(wf);
        assertNotNull(urn);
        // check it's in the system.
        list = controller.listJobs(acc);
        assertNotNull(list);
        assertEquals(1,list.length);
        
     try {
        barrier.attemptBarrier(Sync.ONE_SECOND * WAIT_SECONDS);
        Thread.sleep(Sync.ONE_SECOND * LAG_SECONDS);
        } catch (TimeoutException te) {
            try {                
                Workflow job = controller.readJob(urn);
                dumpDocument(job,"workflow-timeout");
            } catch (Exception e) {
                System.out.println("everything's going wrong today");
                e.printStackTrace();
            }
            fail("timed out waiting for the scheduler to complete");
        }
        assertFalse("timed out waiting for the scheduler to complete",barrier.broken()); // if this is false, meanst that we timed out - need to increase the duration?
         
        list = controller.listJobs(acc);
        assertNotNull(list);
        assertEquals(1,list.length);
        /** @todo remove the next three lines later. .. */
       // Document dom = XMLUtils.newDocument();
        //Marshaller.marshal(list[0],dom);
        //XMLUtils.PrettyDocumentToStream(dom,System.out);
        
        assertNotNull(list[0].getStartTime());
        assertNotNull(list[0].getFinishTime());
        assertEquals(ExecutionPhase.COMPLETED,list[0].getStatus());
        
        Workflow job = controller.readJob(urn);
        assertNotNull(job);
        dumpDocument(job,"workflow-output");

        
        // verify that all steps have been run
        for (Iterator i = JesUtil.getJobSteps(job); i.hasNext(); ) {
            Step step = (Step)i.next();
            StepExecutionRecord exRec = JesUtil.getLatestOrNewRecord(step);
            assertTrue(exRec.getMessageCount() > 0);
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
    public static class ObservableJobScheduler extends GroovySchedulerImpl{

        /** Construct a new ObservableJobScheduler
         * @param factory
         * @param transformers
         * @param dispatcher
         * @param interpFactory
         */
        public ObservableJobScheduler(JobFactory factory, Transformers transformers, Dispatcher dispatcher, GroovyInterpreterFactory interpFactory,CyclicBarrier barrier) {
            super(factory, transformers, dispatcher, interpFactory);
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
Revision 1.25  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.24.20.2  2005/04/12 17:07:19  nw
altered to check readJobList(), etc

Revision 1.24.20.1  2005/04/11 13:57:56  nw
altered to use fileJobFactory instead of InMemoryJobFactory - more realistic

Revision 1.24  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.23.22.1  2005/03/11 14:23:07  nw
works now - using pico 1.1

Revision 1.23  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.22.32.1  2004/12/01 21:46:35  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.22  2004/09/16 21:44:42  nw
got this test working again - had to add in result listener

Revision 1.21  2004/08/18 21:52:24  nw
worked on tests

Revision 1.20  2004/08/13 09:09:40  nw
tidied imports

Revision 1.19  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.18.20.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.18  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.17  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.16  2004/07/01 11:19:57  nw
fix for change in picocontainer

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