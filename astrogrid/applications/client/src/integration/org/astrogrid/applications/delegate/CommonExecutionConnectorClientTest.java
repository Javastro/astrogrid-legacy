/*
 * $Id: CommonExecutionConnectorClientTest.java,v 1.2 2008/09/03 14:19:08 pah Exp $
 * 
 * Created on 17 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;

import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.TestCase;

public class CommonExecutionConnectorClientTest extends TestCase {

    private Tool tool;
    private CommonExecutionConnectorClient cea;
    private String jobId;

    protected void setUp() throws Exception {
	super.setUp();
        cea = DelegateFactory.createDelegate("http://localhost:8888/astrogrid-cea-commandline/services/CommonExecutionConnectorService");
        assertNotNull(cea);
	tool = new Tool();
	tool.setName("ivo://org.astrogrid.unregistered/default");
        tool.setInterface("default");
        Input input = new Input();
        ParameterValue vParameter = new ParameterValue();
        vParameter.setName("in1");
        vParameter.setValue("1.234");
	input.addParameter(vParameter );
	
        vParameter = new ParameterValue();
        vParameter.setName("in2");
        vParameter.setValue("anything");
	input.addParameter(vParameter);
	
        vParameter = new ParameterValue();
        vParameter.setName("in3");
        vParameter.setValue("5");//time to wait til completion.
        input.addParameter(vParameter);

	tool.setInput(input );
        Output output = new Output();
        vParameter = new ParameterValue();
        vParameter.setName("out");
        vParameter.setValue("");
        output.addParameter(vParameter);
	tool.setOutput(output);
	
	assertTrue("tool not valid" ,tool.isValid());
    }

    public void testInit() throws CEADelegateException {
	JobIdentifierType externalId = new JobIdentifierType("externalID");
	jobId = cea.init(tool, externalId );
	assertNotNull(jobId);
    }

//    public void testRegisterProgressListener() {
//	fail("Not yet implemented");
//    }
//
//    public void testRegisterResultsListener() {
//	fail("Not yet implemented");
//    }

    public void testExecute() throws CEADelegateException {
	testInit();
	boolean started = cea.execute(jobId);
	assertTrue("job not started", started);
    }

    public void testAbort() throws CEADelegateException {
	testExecute();
	boolean aborted = cea.abort(jobId);
	assertTrue("job not aborted", aborted);
    }

    public void testGetResults() throws CEADelegateException, InterruptedException {
	testExecute();
	Thread.sleep(6000);
	ResultListType results = cea.getResults(jobId);
        assertEquals("number of results returned", 1,results.getResultCount());
        ParameterValue result = results.getResult(0);
        assertEquals("name of result ", "out", result.getName());
        assertEquals("value of result", "1.234 anything 5", result.getValue());
        
    }

    public void testQueryExecutionStatus() throws CEADelegateException {
	testExecute();
	MessageType status = cea.queryExecutionStatus(jobId);
	assertNotNull(status);
	System.out.println(status);
    }

    public void testGetExecutionSumary() throws CEADelegateException, InterruptedException {
	testGetResults();
	ExecutionSummaryType summary = cea.getExecutionSumary(jobId);
	assertNotNull(summary);
    }

}


/*
 * $Log: CommonExecutionConnectorClientTest.java,v $
 * Revision 1.2  2008/09/03 14:19:08  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/04/17 16:18:14  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing- integration test passing for built in app
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 */
