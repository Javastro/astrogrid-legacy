/*
 * $Id: HttpDirectExecutionTest.java,v 1.2 2004/09/14 16:35:56 jdt Exp $
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

package org.astrogrid.applications.integration.http.helloYou;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.File;

/** test simplest possible execution of a command-line app - direct, no external references involved.
 * @author jdt
 * @version $Name:  $
 * @since iteration5
 */
public class HttpDirectExecutionTest extends AbstractRunTestForCEA {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(HttpDirectExecutionTest.class);


   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public HttpDirectExecutionTest(String arg0) {
      super(new HttpProviderServerInfo(),arg0);
   }

   protected void populateTool(Tool tool) throws Exception{
        serverInfo.populateDirectTool(tool);
      }                                   



protected void checkResults(ResultListType results) throws Exception {
    assertNotNull(results);
    softAssertEquals("more than one result returned",1,results.getResultCount());
    ParameterValue result = results.getResult(0);
    assertNotNull(result);
    String value = result.getValue();

	logger.debug("checkResults() - Value of results: : value = " + value);

    assertNotNull(value);
    assertTrue("expected message not found in execution output\n" + value,value.indexOf(HttpProviderServerInfo.TEST_CONTENTS) != -1);           
}
   
   


}
