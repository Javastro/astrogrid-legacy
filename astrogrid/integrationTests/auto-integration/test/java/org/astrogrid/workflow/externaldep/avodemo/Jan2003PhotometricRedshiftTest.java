/*
 * $Id: Jan2003PhotometricRedshiftTest.java,v 1.2 2004/09/23 08:59:57 pah Exp $
 * 
 * Created on 08-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.externaldep.avodemo;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.astrogrid.applications.avodemo.AVODemoConstants;
import org.astrogrid.applications.avodemo.AVODemoRunner;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 08-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class Jan2003PhotometricRedshiftTest extends AbstractTestForWorkflow {

    private AVODemoRunner avo;
    /**
     * @param applications
     * @param arg0
     */
    public Jan2003PhotometricRedshiftTest(String[] applications, String arg0) {
        super(applications, arg0); 
        
    }
    public static void main(String[] args) {
        junit.textui.TestRunner.run(Jan2003PhotometricRedshiftTest.class);
    }

    /*
     * @see AbstractTestForWorkflow#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Constructor for Jan2003PhotometricRedshiftTest.
     * @param arg0
     */
    public Jan2003PhotometricRedshiftTest(String arg0) {
        
        super(new String[]{AVODemoConstants.DFT, AVODemoConstants.HYPERZ, AVODemoConstants.SEXTRACTOR}, arg0);
        
    }

    protected void buildWorkflow() throws Exception {
      avo = new AVODemoRunner();
      avo.setHemi("s");
      avo.setSector("sect23");
      avo.setUsername(user.getUserId());
      avo.createWorkflow();
      String workflowivorn = avo.getWorkflowIvorn();
      assertNotNull(workflowivorn);
      wf = ag.getWorkflowManager().getWorkflowStore().readWorkflow(user, new Ivorn(workflowivorn));
      
    }
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        Step step = (Step)result.getSequence().getActivity(result.getSequence().getActivityCount()-1);
        assertStepCompleted(step);
        ResultListType val = getResultOfStep(step);
        softAssertEquals("only expected single result", 1, val.getResultCount());
        //read the resulting VOTable       
        VoSpaceClient vos = new VoSpaceClient(user);
        InputStream is = vos.getStream(new Ivorn(val.getResult(0).getValue()));
        AstrogridAssert.assertVotable(is);
    }
}
