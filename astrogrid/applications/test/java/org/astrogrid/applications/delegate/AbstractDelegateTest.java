/*
 * $Id: AbstractDelegateTest.java,v 1.2 2004/03/23 12:51:26 pah Exp $
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

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class AbstractDelegateTest extends WorkFlowUsingTestCase {

   /**
    * 
    */
   public AbstractDelegateTest() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public AbstractDelegateTest(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   /**
       * Constructor for ApplicationControllerDelegateTest.
       * @param arg0
       */
   protected JobIdentifierType jobstepID;

   protected String executionid;

   protected Credentials credentials; //TODO must try to use credentials in the tests


   protected String applicationid;

   protected String monitorURL;

   protected CommonExecutionConnectorClient delegate;

   protected String endpoint;

   protected void setUp() throws Exception {
      super.setUp();
      //TODO need to parameterize the path below to get it to work in maven environment
      //This seems to bomb out after the initial call to set

      delegate = DelegateFactory.createDelegate(endpoint);
      assertNotNull(delegate);

      monitorURL = "JESMonitorDummy";
      applicationid = TestAppConst.TESTAPP_NAME;
   }

   public final void testListApplications() {
      try {
         ApplicationList apps = delegate.listApplications();
         assertNotNull("application name list", apps);
         assertEquals("number of test applications", 2, apps.getApplicationDefnCount());

      }
      catch (CEADelegateException e) {
         fail("unknown exception");
         e.printStackTrace();
      }
   }

   public final void testExecuteApplication() throws CEADelegateException {
      executionid = delegate.execute(tool, jobstepID, monitorURL);
      assertTrue("executionid invalid", executionid.equals("-1"));
   }

   public final void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   public final void testReturnRegistryEntry() {
      //TODO Implement returnRegistryEntry().
   }
   

}
