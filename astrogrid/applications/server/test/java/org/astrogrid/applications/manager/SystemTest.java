/*$Id: SystemTest.java,v 1.8 2005/08/10 17:45:10 clq2 Exp $
 * Created on 09-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.MockMonitor;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary;
import org.astrogrid.applications.javaclass.SampleJavaClassApplications;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;

import junit.framework.TestCase;

/** test the DefaultCommonExecutionController - with a javaclass backend
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jun-2004
 *
 */
public class SystemTest extends TestCase {
    /**
     * Constructor for DefaultCommonExecutionControllerTest.
     * @param arg0
     */
    public SystemTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        history = new InMemoryExecutionHistory();
        idgen = new InMemoryIdGen();
        DefaultProtocolLibrary protocolLib = new DefaultProtocolLibrary();
        protocolLib.addProtocol(new FileProtocol());
        assertTrue(protocolLib.isProtocolSupported("file"));
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib,new TestAuthorityResolver());
        lib = new JavaClassApplicationDescriptionLibrary(SampleJavaClassApplications.class,env);
        controller = new DefaultExecutionController(lib,history);
      ApplicationEnvironmentRetriver executionRetriever = new DefaultApplicationEnvironmentRetriever(history);
      querier = new DefaultQueryService(history, executionRetriever);
        
        // construct the tool object
        ApplicationDescription sum = lib.getDescription("org.astrogrid.test/sum");
           assertNotNull(sum);
           ApplicationInterface iface = sum.getInterfaces()[0];
           String[] inputParameterNames = iface.getArrayofInputs();
           assertNotNull(iface);
           tool = new Tool();
           tool.setName(sum.getName());           
           Input input = new Input();
           tool.setInput(input);
           Output output = new Output();
           tool.setOutput(output);
           ParameterValue a = new ParameterValue();
           ParameterValue b = new ParameterValue();        
           a.setName(inputParameterNames[0]);
           b.setName(inputParameterNames[1]);
           b.setIndirect(true);
           a.setIndirect(false);
           a.setValue("1");
           File valFile = File.createTempFile("SystemTest-Input",null);
           //valFile.deleteOnExit();
           PrintWriter pw = new PrintWriter(new FileWriter(valFile));
           pw.print(2); 
           pw.close();
           b.setValue(valFile.toURI().toString());
           input.addParameter(a);
           input.addParameter(b); 
           ParameterValue result = new ParameterValue();
           result.setName(iface.getArrayofOutputs()[0]);
           result.setIndirect(true);
           File resultFile = File.createTempFile("SystemTest-Results",null);
           //resultFile.deleteOnExit();
           result.setValue(resultFile.toURI().toString());
           output.addParameter(result);
    }
    protected ExecutionHistory history;
    protected ApplicationDescriptionLibrary lib;
    protected IdGen idgen;
    protected ExecutionController controller;
    protected QueryService querier;
    protected Tool tool;
    
    public void testRun() throws Exception {
        String id = controller.init(tool,"jobStep");
        assertNotNull(id);
        MockMonitor monitor = new MockMonitor();
        Application app = history.getApplicationFromCurrentSet(id);
        app.addObserver(monitor);  
        URI testURI = new URI(JobMonitorDelegate.TEST_URI);
        querier.registerProgressListener(id,testURI );
    // sadly this one won't accept the test uri.    
    //    querier.registerResultsListener(id,testURI);
        
        // do a bit of querying
        MessageType message = querier.queryExecutionStatus(id);
        assertNotNull(message);
        assertEquals(ExecutionPhase.INITIALIZING,message.getPhase());
        ExecutionSummaryType summary = querier.getSummary(id);
        assertEquals(ExecutionPhase.INITIALIZING,summary.getStatus());
        ResultListType results = querier.getResults(id);
        assertNotNull(results);
        assertEquals(0,results.getResultCount()); // nothing been computed yet.
        // set the thing running.
        assertTrue(controller.execute(id));
        monitor.waitFor(20);
        assertTrue(monitor.sawExit);
        message = querier.queryExecutionStatus(id);
        assertNotNull(message);        
        assertEquals(ExecutionPhase.COMPLETED, message.getPhase());
        summary =  querier.getSummary(id);
        assertEquals(ExecutionPhase.COMPLETED,summary.getStatus());
         results = querier.getResults(id);
        assertNotNull(results);
        assertEquals(1,results.getResultCount());
        ParameterValue r1 = results.getResult(0);
        assertNotNull(r1);
        File resultFile = new File(new URI(r1.getValue()));
        assertTrue(resultFile.exists());
        BufferedReader br = new BufferedReader(new FileReader(resultFile));        
        String resultValue = br.readLine();        
        System.out.println(resultValue);
        assertEquals(3,Integer.valueOf(resultValue).intValue());              
        }
    
    public void testAbortBeforeExecute() throws Exception {
        String id = controller.init(tool,"jobStep");
        controller.abort(id); // don't care if we can't abort, just as long as it doesn't throw.
    }
    public void testAbortAfterExecute() throws Exception {

        String id = controller.init(tool,"jobStep");
        controller.execute(id);
        controller.abort(id); // don't care if we can't abort, just as long as it doesn't throw.
        
    }
}


/* 
$Log: SystemTest.java,v $
Revision 1.8  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.7.8.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.

Revision 1.7  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.6.68.1  2005/06/09 08:47:33  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.6.54.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.6  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.5.16.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.5  2004/09/17 01:22:12  nw
updated tests

Revision 1.4.52.1  2004/09/14 13:44:37  nw
added tests for thread pool implementation

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/22 16:35:17  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/