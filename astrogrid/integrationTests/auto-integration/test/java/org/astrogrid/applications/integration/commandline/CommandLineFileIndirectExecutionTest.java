/*
 * $Id: CommandLineFileIndirectExecutionTest.java,v 1.6 2005/07/05 10:54:36 jdt Exp $
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
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;

/** test commandiline app with file-based indirect parametesrs.
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 * 
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA
 * @see org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo
 */
public class CommandLineFileIndirectExecutionTest extends AbstractRunTestForCEA {


   private File tmpInfile;
   private File tmpOutFile;
   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public CommandLineFileIndirectExecutionTest(String arg0) {
      super(new CommandLineProviderServerInfo(), arg0);
   }

   /**
    * @param tool
    */
   protected void populateTool(Tool tool) throws Exception{
        serverInfo.populateIndirectTool(tool,tmpInfile.toURI().toString(),tmpOutFile.toURI().toString());
      }                                   

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      tmpInfile = File.createTempFile("ApplictionsInstallationTest-input","txt");
      tmpOutFile = File.createTempFile("ApplicationsInstallationTest-output","txt");
      tmpInfile.createNewFile();
      tmpOutFile.createNewFile();
      tmpInfile.deleteOnExit();
      tmpOutFile.deleteOnExit();        
      PrintWriter pw = new PrintWriter(new FileWriter(tmpInfile));
      assertNotNull(pw);
      pw.println(CommandLineProviderServerInfo.TEST_CONTENTS);
      pw.close();  
   }

/**
 * @see org.astrogrid.applications.integration.AbstractRunTestForCEA#checkResults(org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
 */
protected void checkResults(ResultListType results) throws Exception {
    assertNotNull(results);
    softAssertEquals("three results should be returned",3,results.getResultCount());
    ParameterValue result= (ParameterValue)results.findXPathValue("result[name='P3']");
    //TODO should really test P2 output also.
    assertNotNull(result);
    softAssertTrue(result.getIndirect());
    String filepath = result.getValue();
    softAssertEquals(filepath,tmpOutFile.toURI().toString());
    // read it in then.
    Reader in = new FileReader(new File(new URI(filepath)));
    assertNotNull(in);
    StringWriter out = new StringWriter();
    Piper.pipe(in,out);
    out.close();
    assertNotNull(out.toString()); // unlikely.
    assertTrue("expected message not found in execution output\n+" + out.toString(),out.toString().indexOf(CommandLineProviderServerInfo.TEST_CONTENTS) != -1);           
  }
          
}
   

