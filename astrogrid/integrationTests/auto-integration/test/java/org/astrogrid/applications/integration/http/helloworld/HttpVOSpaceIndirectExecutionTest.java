/*
 * $Id: HttpVOSpaceIndirectExecutionTest.java,v 1.4 2004/09/15 17:09:01 jdt Exp $
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

package org.astrogrid.applications.integration.http.helloworld;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author jdt
 * @version $Name:  $
 * @since iteration5
 */
public class HttpVOSpaceIndirectExecutionTest extends AbstractRunTestForCEA {

   private Ivorn inputIvorn;
   private Ivorn targetIvorn;
   private VoSpaceClient client;
   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public HttpVOSpaceIndirectExecutionTest(String arg0) {
      super(new HttpProviderServerInfo(),arg0);
   }


   protected void populateTool(Tool tool) throws Exception {
       serverInfo.populateIndirectTool(tool,inputIvorn.toString(),targetIvorn.toString());
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      targetIvorn = createIVORN("/ApplicationRunWithVOSpaceTest-output");
      inputIvorn = createIVORN("/ApplicationRunWithVOSpaceTest-input");

      // write to myspace...
      client = new VoSpaceClient(user);
      assertNotNull(client);
   }




/**
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
 */
protected void checkResults(ResultListType results) throws Exception {
    assertNotNull(results);
    softAssertEquals("more than one result returned",1,results.getResultCount());
    ParameterValue result = results.getResult(0);
    assertNotNull(result);
    softAssertTrue(result.getIndirect());
    String filePath = result.getValue();
    softAssertEquals(filePath,targetIvorn.toString());
    client = new VoSpaceClient(user);
    Reader in = new InputStreamReader(client.getStream( new Ivorn(filePath)));
    assertNotNull(in);
     StringWriter out = new StringWriter();
     Piper.pipe(in,out);
     out.close();
     assertNotNull(out.toString()); // unlikely.
     assertTrue("expected message not found in execution output\n+" + out.toString(),out.toString().indexOf(HttpProviderServerInfo.TEST_CONTENTS) != -1);           
   
}

}
