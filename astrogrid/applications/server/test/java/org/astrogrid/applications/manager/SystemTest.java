/*$Id: SystemTest.java,v 1.3 2004/07/22 16:35:17 nw Exp $
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
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary;
import org.astrogrid.applications.javaclass.SampleJavaClassApplications;
import org.astrogrid.applications.javaclass.TestCommunity;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;
import org.astrogrid.applications.parameter.indirect.FileProtocol;
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
        DefaultIndirectionProtocolLibrary protocolLib = new DefaultIndirectionProtocolLibrary();
        protocolLib.addProtocol(new FileProtocol());
        assertTrue(protocolLib.isProtocolSupported("file"));
        ApplicationDescriptionEnvironment env = new ApplicationDescriptionEnvironment(idgen,protocolLib);
        lib = new JavaClassApplicationDescriptionLibrary(SampleJavaClassApplications.class,new TestCommunity(),env);
        controller = new DefaultExecutionController(lib,history);
        querier = new DefaultQueryService(history);
        
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
           valFile.deleteOnExit();
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
           resultFile.deleteOnExit();
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
        assertTrue(controller.execute(id));
        monitor.waitFor(20);
        assertTrue(monitor.sawExit);
        MessageType message = querier.queryExecutionStatus(id);
        assertNotNull(message);        
        assertEquals(ExecutionPhase.COMPLETED, message.getPhase());
        ExecutionSummaryType summary =  querier.getSummary(id);
        assertEquals(ExecutionPhase.COMPLETED,summary.getStatus());
        ResultListType results = querier.getResults(id);
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