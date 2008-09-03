/*$Id: SystemTest.java,v 1.13 2008/09/03 14:19:04 pah Exp $
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

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import junit.framework.TestCase;

import net.ivoa.uws.ExecutionPhase;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.TestAuthorityResolver;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.ListOfParameterValues;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.DefaultExecutionEnvRetriever;
import org.astrogrid.applications.javaclass.BaseJavaClassConfiguration;
import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary;
import org.astrogrid.applications.javainternal.BuiltInApplicationDescriptionTest;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.manager.persist.InMemoryExecutionHistory;
import org.astrogrid.applications.parameter.protocol.DefaultProtocolLibrary;
import org.astrogrid.applications.parameter.protocol.FileProtocol;
import org.astrogrid.applications.parameter.protocol.Protocol;
import org.astrogrid.applications.test.MockMonitor;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;

/** test the DefaultCommonExecutionController - with a javaclass backend
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jun-2004
 *
 */
public class SystemTest extends BuiltInApplicationDescriptionTest {
    /*
     * @see TestCase#setUp()
     */
    public void setUp() throws Exception {
        super.setUp();
        controller = new DefaultExecutionController(lib,history, new DefaultExecutionPolicy());
        ApplicationEnvironmentRetriver executionRetriever = new DefaultExecutionEnvRetriever(history, conf);
        querier = new DefaultQueryService(history, executionRetriever);
        
        // construct the tool object
        ApplicationDescription sum = lib.getDescription("ivo://org.astrogrid.unregistered/default");
           assertNotNull(sum);
           ApplicationInterface iface = sum.getInterfaces()[0];
           assertNotNull(iface);
     }
    protected ExecutionController controller;
    protected QueryService querier;
   
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
        assertEquals(ExecutionPhase.PENDING,message.getPhase());
        ExecutionSummaryType summary = querier.getSummary(id);
        assertEquals(ExecutionPhase.PENDING,summary.getPhase());
        org.astrogrid.applications.description.execution.ResultListType results = querier.getResults(id);
        assertNotNull(results);
        assertEquals(0,results.getResult().size()); // nothing been computed yet.
        // set the thing running.
        assertTrue(controller.execute(id));
        monitor.waitFor(20);
        assertTrue(monitor.sawExit);
        message = querier.queryExecutionStatus(id);
        assertNotNull(message);        
        assertEquals(ExecutionPhase.COMPLETED, message.getPhase());
        summary =  querier.getSummary(id);
        assertEquals(ExecutionPhase.COMPLETED,summary.getPhase());
         results = querier.getResults(id);
        assertNotNull(results);
        assertEquals(1,results.getResult().size());
        ParameterValue r1 = results.getResult().get(1);//todo or 0
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

    @Override
    protected ParameterValue buildParameter2() {
        ParameterValue b = new ParameterValue();        
        b.setId("in2");
        b.setIndirect(true);
        File valFile;
	//valFile.deleteOnExit();
	PrintWriter pw;
	try {
	    valFile = File.createTempFile("SystemTest-Input",null);
	    pw = new PrintWriter(new FileWriter(valFile));
	        pw.print(getPar2value()); 
	        pw.close();
	        b.setValue(valFile.toURI().toString());
	} catch (IOException e) {
	   fail("problem creating the indirect file");
	}
        return b;
    }
}


/* 
$Log: SystemTest.java,v $
Revision 1.13  2008/09/03 14:19:04  pah
result of merge of pah_cea_1611 branch

Revision 1.12.2.9  2008/08/29 07:28:30  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.12.2.8  2008/06/10 20:01:39  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.12.2.7  2008/05/17 16:45:01  pah
tidy tests to make sure more are passing

Revision 1.12.2.6  2008/05/01 15:22:48  pah
updates to tool

Revision 1.12.2.5  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.12.2.4  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.12.2.3  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.12.2.2  2008/03/26 17:15:40  pah
Unit tests pass

Revision 1.12.2.1  2008/03/19 23:10:55  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.12  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.11  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.9  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.8.20.1  2006/02/01 12:09:54  gtr
Refactored and fixed to allow the tests to work with the new configuration.

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