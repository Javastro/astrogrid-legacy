/*$Id: DataCenterFileIndirectExecutionTest.java,v 1.5 2004/11/19 14:17:56 clq2 Exp $
 * Created on 30-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.datacenter;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.AbstractRunTestForCEA;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.io.Piper;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public class DataCenterFileIndirectExecutionTest extends AbstractRunTestForCEA {

    /** Construct a new DataCenterFileIndirectExecutionTest
     * @param info
     * @param arg0
     */
    public DataCenterFileIndirectExecutionTest( String arg0) {
        super(new DataCenterProviderServerInfo(), arg0);
    }
    /**
       * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#populateTool(org.astrogrid.workflow.beans.v1.Tool)
       */
      protected void populateTool(Tool tool) throws Exception {
          serverInfo.populateIndirectTool(tool, inputFile.toURI().toString(),outputFile.toURI().toString());
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
          softAssertEquals(filePath,outputFile.toURI().toString());
          InputStream is = new FileInputStream(new File(new URI(filePath)));
          AstrogridAssert.assertVotable(is);     
      }

      /**
       * @see junit.framework.TestCase#setUp()
       */
      protected void setUp() throws Exception {
          super.setUp();
          inputFile = File.createTempFile("DataCenterFileIndirectExecutionTest-input",null);
          outputFile = File.createTempFile("DataCenterFileIndirectExecutionTest-output",null);
          inputFile.createNewFile();
          outputFile.createNewFile();
         // inputFile.deleteOnExit();
         // outputFile.deleteOnExit();
          Writer w = new FileWriter(inputFile);
          Reader r = new InputStreamReader(this.getClass().getResourceAsStream(DataCenterProviderServerInfo.SAMPLE_QUERY_RESOURCE));
          Piper.pipe(r,w);
          w.close();
      }
    
      protected File inputFile;
      protected File outputFile;
}


/* 
$Log: DataCenterFileIndirectExecutionTest.java,v $
Revision 1.5  2004/11/19 14:17:56  clq2
roll back beforeMergenww-itn07-659

Revision 1.3  2004/08/27 13:16:52  nw
used AstrogridAssert to check results more thoroughly.

Revision 1.2  2004/07/20 01:59:25  nw
testing new datacenter cea

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/