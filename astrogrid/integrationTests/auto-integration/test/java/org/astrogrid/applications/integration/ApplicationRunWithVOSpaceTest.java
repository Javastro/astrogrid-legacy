/*
 * $Id: ApplicationRunWithVOSpaceTest.java,v 1.2 2004/05/17 22:54:59 pah Exp $
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
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ApplicationRunWithVOSpaceTest extends AbstractRunTestForApplications {

   private Ivorn inputIvorn;
   private Ivorn targetIvorn;
   private VoSpaceClient client;
   /**
    * Constructor for ApplicationRunTest.
    * @param arg0
    */
   public ApplicationRunWithVOSpaceTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationRunWithVOSpaceTest.class);
   }

   /*
    * @see AbstractTestForIntegration#setUp()
    */

   /**
    * @param tool
    */
   protected void populateTool(Tool tool) throws Exception {

      ParameterValue pval =
         (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
      pval.setValue("1"); // wait one second...
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P2']");
      pval.setValue("30.5");
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P4']");
      pval.setValue("test string");
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
      pval.setValue(inputIvorn.toString());
      pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
      pval.setValue(targetIvorn.toString());
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
      byte[] bytes = TEST_CONTENTS.getBytes();
      client.putBytes(bytes, 0, bytes.length, inputIvorn, false);

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.integration.AbstractRunTestForApplications#checkResults()
    */
   protected void checkResults() throws IOException {

      InputStream is = client.getStream(targetIvorn);
      assertNotNull(is);
      boolean test = checkOutContent(is);
      assertTrue("the test string was not found in the application output", test);

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.integration.AbstractTestForApplications#applicationName()
    */
   protected String applicationName() {
      return TESTAPP;  
       }

}
