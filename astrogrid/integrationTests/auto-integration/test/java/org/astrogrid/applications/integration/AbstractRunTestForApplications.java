/*
 * $Id: AbstractRunTestForApplications.java,v 1.1 2004/05/17 12:37:31 pah Exp $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 14-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractRunTestForApplications
   extends AbstractTestForApplications {

   /**
    * @param arg0
    */
   public AbstractRunTestForApplications(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   public void testExecute() throws Exception {
       ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
       org.astrogrid.portal.workflow.intf.ApplicationDescription descr = reg.getDescriptionFor(applicationName());
       assertNotNull("could not get application description",descr);
       Tool tool = descr.createToolFromDefaultInterface();
       assertNotNull("tool is null",tool);
       populateTool(tool);
       descr.validate(tool);
        
       JobIdentifierType id = new JobIdentifierType(); // not too bothered about this.
       id.setValue(this.getClass().getName());
       String returnEndpoint =null;      
       String execId = delegate.execute(tool,id,returnEndpoint);
       assertNotNull(execId);
       
       MessageType status = delegate.queryExecutionStatus(execId);
       assertNotNull(status);
       int i = 0;
       while (status.getPhase() == ExecutionPhase.RUNNING || status.getPhase() == ExecutionPhase.PENDING) {
         Thread.sleep(2000);
         status = delegate.queryExecutionStatus(execId);
         if(i++ == 20)
         {
            fail("application has taken too long to complete");
         }
      }
      assertTrue("the application has not completed successfully", status.getPhase()== ExecutionPhase.COMPLETED);
      
      checkResults();
      
   }

   /**
    * Make checks on the results that are expected....
    */
   protected abstract void checkResults() throws Exception; 

   /**
    * @param tool
    */
   protected abstract void populateTool(Tool tool) throws Exception;

   protected static final String TEST_CONTENTS = "test contents";

   /**
       * Checks the stream contents for the test string, which should have been written out by the test application.
       * @param is
       * @return
       * @throws IOException
       */
   protected boolean checkOutContent(InputStream is) throws IOException {
      boolean retval = false;
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      assertNotNull(br);
      String line;
      while ((line = br.readLine())!= null) {
         if(line.indexOf(TEST_CONTENTS) != -1)
         {
            retval = true;
         }
      }
      
      return retval;
   }

}
