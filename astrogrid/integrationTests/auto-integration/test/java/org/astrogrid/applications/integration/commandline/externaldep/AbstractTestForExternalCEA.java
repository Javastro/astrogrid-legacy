/*
 * $Id: AbstractTestForExternalCEA.java,v 1.1 2004/10/12 12:58:02 pah Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration.commandline.externaldep;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.applications.integration.AbstractTestForCEA;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * Base class for running tests on the real CEA applications. It deals with finding the application server etc.
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractTestForExternalCEA extends AbstractTestForIntegration {

    protected CommonExecutionConnectorClient delegate;

    /**
     * @param arg0
     */
    public AbstractTestForExternalCEA(ExternalAppInfo info,String arg0) {
       super(arg0);
       this.appInfo = info;
    }
    protected final ExternalAppInfo appInfo;

    public void testExecute() throws Exception {
        try {
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        org.astrogrid.portal.workflow.intf.ApplicationDescription descr = reg.getDescriptionFor(appInfo.getApplicationName());
        assertNotNull("could not get application description",descr);
        InterfacesType intfs = descr.getInterfaces();        
        Interface intf = (Interface)intfs.findXPathValue("interface[name='"+appInfo.getApplicationInterfaceName()+"']");
        assertNotNull("could not find desired interface");
        Tool tool = descr.createToolFromInterface(intf);
        assertNotNull("tool is null",tool);
        populateTool(tool);
        try {
         descr.validate(tool);
        } catch (Exception e) {
            softFail("constructed tool failed to validate" + e.getMessage());
        }
        // initialize the application  
        JobIdentifierType id = new JobIdentifierType(); 
        id.setValue(this.getClass().getName());    
        
        String execId = delegate.init(tool,id);
        assertNotNull(execId);
        
        // check status, etc.
        MessageType status = delegate.queryExecutionStatus(execId);
        assertNotNull(status);
        softAssertEquals("Application should be in initializing phase",ExecutionPhase.INITIALIZING,status.getPhase());
        // execute the application
        delegate.execute(execId);
        // too sensitive to timing.
       // softAssertTrue("Appllication should now be running",ExecutionPhase.RUNNING_TYPE <= delegate.queryExecutionStatus(execId).getPhase().getType());
        
        // wait to finish - by polling.
        status = delegate.queryExecutionStatus(execId);
        assertNotNull(status);
        int i = 0;
        while (status.getPhase().getType() <=  ExecutionPhase.RUNNING_TYPE) {
          Thread.sleep(2000);
          status = delegate.queryExecutionStatus(execId);
          if(i++ == 20)
          {
             softFail("application has taken too long to complete");
          }
       }
       assertEquals("the application has not completed successfully", ExecutionPhase.COMPLETED, status.getPhase());
       
       //      
       ResultListType results = delegate.getResults(execId);
       assertNotNull(results);     
       checkResults(results);
       
       // test get a summary for the task.
       ExecutionSummaryType execSummary = delegate.getExecutionSumary(execId);      
       softAssertEquals(execId,execSummary.getExecutionId());
       // could have more here..
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }     
    

    
     /**
     * Make checks on the results that are expected....
     */
    protected abstract void checkResults(ResultListType results) throws Exception; 

    /**
     * @param tool
     */
    protected abstract void populateTool(Tool tool) throws Exception;


}
