/*
 * $Id: CommandLineDirectExecutionTest.java,v 1.6 2005/07/05 10:54:36 jdt Exp $
 * 
 * Created on 11-May-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.integration.commandline;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.File;

/** test simplest possible execution of a command-line app - direct, no external references involved.
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo
 */
public class CommandLineDirectExecutionTest extends AbstractRunTestForCEA {


   private File tmpInfile;
   private File tmpOutFile;
   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public CommandLineDirectExecutionTest(String arg0) {
      super(new CommandLineProviderServerInfo(),arg0);
   }

   protected void populateTool(Tool tool) throws Exception{
        serverInfo.populateDirectTool(tool);
      }                                   



protected void checkResults(ResultListType results) throws Exception {
    assertNotNull(results);
    softAssertEquals("should be 3 results",3,results.getResultCount());
    ParameterValue result = (ParameterValue)results.findXPathValue("result[name='P3']");
    assertNotNull(result);
    String value = result.getValue();
    assertNotNull(value);
    assertTrue("expected message not found in execution output\n" + value,value.indexOf(CommandLineProviderServerInfo.TEST_CONTENTS) != -1);           
}
   
   


}
