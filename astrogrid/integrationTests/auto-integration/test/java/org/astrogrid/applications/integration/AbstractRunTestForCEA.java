/*
 * $Id: AbstractRunTestForCEA.java,v 1.1 2004/07/01 11:43:33 nw Exp $
 * 
 * Created on 14-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration;

import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.workflow.beans.v1.Tool;

/** Abstract test case for executing applications.
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractRunTestForCEA
   extends AbstractTestForCEA {

   /**
    * @param arg0
    */
   public AbstractRunTestForCEA(ServerInfo info,String arg0) {
      super(info.getServerSearchString(),arg0);
      this.serverInfo = info;
   }
   protected final ServerInfo serverInfo;

   public void testExecute() throws Exception {
       ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
       org.astrogrid.portal.workflow.intf.ApplicationDescription descr = reg.getDescriptionFor(serverInfo.getApplicationName());
       assertNotNull("could not get application description",descr);
       Tool tool = descr.createToolFromDefaultInterface();
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
       softAssertEquals("Application should be in pending phase",ExecutionPhase.PENDING,status.getPhase());
       // execute the application
       delegate.execute(execId);
       softAssertTrue("Appllication should now be running",ExecutionPhase.RUNNING_TYPE <= delegate.queryExecutionStatus(execId).getPhase().getType());
       
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
      softAssertEquals("the application has not completed successfully", ExecutionPhase.COMPLETED, status.getPhase());
      
      //      
      ResultListType results = delegate.getResults(execId);
      assertNotNull(results);     
      checkResults(results);
      
      // test get a summary for the task.
      ExecutionSummaryType execSummary = delegate.getExecutionSumary(execId);      
      softAssertEquals(execId,execSummary.getExecutionId());
      // could have more here..
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
