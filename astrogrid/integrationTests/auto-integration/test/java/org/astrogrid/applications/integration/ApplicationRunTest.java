/*
 * $Id: ApplicationRunTest.java,v 1.1 2004/05/17 12:37:31 pah Exp $
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

package org.astrogrid.applications.integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ApplicationRunTest extends AbstractRunTestForApplications {


   VoSpaceClient voclient;
   private File tmpInfile;
   private File tmpOutFile;
   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public ApplicationRunTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationRunTest.class);
   }

   /*
    * @see AbstractTestForIntegration#setUp()
    */
  
   
   /**
    * @param tool
    */
   protected void populateTool(Tool tool) throws Exception{
 
         ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
         pval.setValue("1"); // wait one second...
         pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P2']");
         pval.setValue("30.5");
         pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P4']");
         pval.setValue("test string");
         pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
         pval.setValue(tmpInfile.getAbsolutePath());
         pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
         pval.setValue(tmpOutFile.getAbsolutePath());
      }                                   

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      tmpInfile = File.createTempFile("ApplictionsInstallationTest-input","txt");
      tmpOutFile = File.createTempFile("ApplicationsInstallationTest-output","txt");
      PrintWriter pw = new PrintWriter(new FileOutputStream(tmpInfile));
      assertNotNull(pw);
      pw.println(TEST_CONTENTS);
      pw.close();          

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.integration.AbstractRunTestForApplications#checkResults()
    */
   protected void checkResults() throws IOException {
      
      FileInputStream is = new FileInputStream(tmpOutFile);
      assertNotNull(is);
      boolean test = checkOutContent(is);
      assertTrue("the test string was not found in the application output", test);
          
      
   }
   

}
