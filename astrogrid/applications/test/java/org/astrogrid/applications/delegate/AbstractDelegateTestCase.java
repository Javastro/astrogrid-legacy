/*
 * $Id: AbstractDelegateTestCase.java,v 1.7 2004/04/30 11:19:19 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.delegate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.applications.manager.WorkFlowUsingTestCase;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractDelegateTestCase extends WorkFlowUsingTestCase {

   /**
    * 
    */
   public AbstractDelegateTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public AbstractDelegateTestCase(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   private Tool runtool;

   private String INFILENAME;

   private File infile;

   private String OUTFILENAME;

   private File outfile;

   private Ivorn outIvorn;

   private Ivorn targetIvorn;

   /**
       * Constructor for ApplicationControllerDelegateTest.
       * @param arg0
       */
   protected JobIdentifierType jobstepID = new JobIdentifierType("dummyjob");

   protected String executionid;

   protected Credentials credentials; //TODO must try to use credentials in the tests


   protected String monitorURL;

   protected CommonExecutionConnectorClient delegate;
   
   protected final String TESTCONTENTS = "this is the test contents written back";

   protected String endpoint;

   protected void setUp() throws Exception {
      super.setUp();
      //TODO need to parameterize the path below to get it to work in maven environment
      //This seems to bomb out after the initial call to set

      delegate = DelegateFactory.createDelegate(endpoint);
      assertNotNull(delegate);

      monitorURL = null; // that will stop any monitor call
      runtool = tool2;
      outfile = File.createTempFile("scworkout", ".tmp");
      assertNotNull(outfile);
      OUTFILENAME = outfile.getAbsolutePath();
      System.out.println("output file is "+OUTFILENAME);
      infile =File.createTempFile("scworkin", ".tmp");
      assertNotNull(infile);
      INFILENAME = infile.getAbsolutePath();
      PrintWriter pw = new PrintWriter(new FileOutputStream(infile));
      assertNotNull(pw);
      pw.println(TESTCONTENTS);
      pw.close();
      
      ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
      pval.setValue(INFILENAME);
      pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
      pval.setValue(OUTFILENAME);

        

   }

   public final void testListApplications() throws CEADelegateException {
         ApplicationList apps = delegate.listApplications();
         assertNotNull("application name list", apps);
         assertEquals("number of test applications", 2, apps.getApplicationDefnCount());

   }

   public final void testExecuteApplication() throws CEADelegateException {
      executionid = delegate.execute(runtool, jobstepID, monitorURL);
      assertTrue("executionid invalid", !executionid.equals("-1"));
   }

   public final void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   public final void testReturnRegistryEntry() throws CEADelegateException {
      String regent = delegate.returnRegistryEntry();
   }
   
  

}
