/*
 * $Id: AbstractDelegateTestCase.java,v 1.4 2004/04/24 10:37:07 pah Exp $
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

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.applications.manager.WorkFlowUsingTestCase;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

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

   private Ivorn outIvorn;

   private Ivorn targetIvorn;

   /**
       * Constructor for ApplicationControllerDelegateTest.
       * @param arg0
       */
   protected JobIdentifierType jobstepID = new JobIdentifierType("dummyjob");

   protected String executionid;

   protected Credentials credentials; //TODO must try to use credentials in the tests


   protected String applicationid;

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
      applicationid = TestAppConst.TESTAPP_NAME;
      VoSpaceClient client = new VoSpaceClient(user);
      assertNotNull(client); 
      targetIvorn = createIVORN("testInFile");
      assertNotNull(targetIvorn);      
      outIvorn = createIVORN("outfile"); 
      assertNotNull(outIvorn);
      
      //write out some contents
      
      byte[] cont = TESTCONTENTS.getBytes();
      client.putBytes(cont, 0, cont.length, outIvorn, false);
      
        

   }

   public final void testListApplications() throws CEADelegateException {
         ApplicationList apps = delegate.listApplications();
         assertNotNull("application name list", apps);
         assertEquals("number of test applications", 2, apps.getApplicationDefnCount());

   }

   public final void testExecuteApplication() throws CEADelegateException {
      executionid = delegate.execute(tool, jobstepID, monitorURL);
      assertTrue("executionid invalid", executionid.equals("-1"));
   }

   public final void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   public final void testReturnRegistryEntry() throws CEADelegateException {
      String regent = delegate.returnRegistryEntry();
   }
   
   public final void testExecutionMySpaceUsingApplication() throws CEADelegateException
   {
      executionid = delegate.execute(tool, jobstepID, monitorURL);
      assertTrue("executionid invalid", executionid.equals("-1"));
      
   }
   

}
