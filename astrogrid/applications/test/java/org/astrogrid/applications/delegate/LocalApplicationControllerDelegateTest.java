/*
 * $Id: LocalApplicationControllerDelegateTest.java,v 1.2 2003/12/17 17:16:54 pah Exp $
 * 
 * Created on 08-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;

import java.rmi.RemoteException;

import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.applications.description.TestAppConst;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class LocalApplicationControllerDelegateTest extends BaseDBTestCase {

   /**
    * Constructor for ApplicationControllerDelegateTest.
    * @param arg0
    */
   
   private String executionid;
   private User user;
   private ParameterValues parameters;
   private String applicationid;
   private String monitorURL;
   ApplicationControllerDelegate delegate;
   private static String endpoint ="local:///ApplicationControllerService";// "http://localhost:8080/astrogrid-applications/services/ApplicationControllerService";
   public LocalApplicationControllerDelegateTest(String arg0) {
      super(arg0);
      
   }

   public static void main(String[] args) {
      //TODO set the endpoint from here?
      junit.textui.TestRunner.run(LocalApplicationControllerDelegateTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      //TODO need to parameterize the path below to get it to work in maven environment
      //This seems to bomb out after the initial call to set
      String[] args = {"-d","-l",endpoint, "/data/work/astrogrid/src/applications/generated/wsdd/ApplicationControllerService/deploy.wsdd"}; 
      org.apache.axis.client.AdminClient.main(args); 


      delegate = new ApplicationControllerDelegate(endpoint);
      assertNotNull(delegate);
      user = new User(); 
      user.setAccount("noone");
      user.setGroup("nogroup");
      user.setToken("notoken");

     monitorURL="JESMonitorDummy";
     applicationid = TestAppConst.TESTAPP_NAME;
     parameters = new ParameterValues();
  }


   final public void testListApplications() {
      try {
         String[] apps = delegate.listApplications();
         assertNotNull("application name list",apps);
         assertEquals("number of test applications", 2, apps.length);
      }
      catch (RemoteException e) {
         fail("unknown exception");
         e.printStackTrace();
      }
   }

   final public void testGetApplicationDescription() {
      
      try {
         SimpleApplicationDescription desc = delegate.getApplicationDescription(TestAppConst.TESTAPP_NAME);
         assertNotNull("Application description",desc);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }

   final public void testInitializeApplication() {
      parameters.setMethodName(TestAppConst.MAIN_INTERFACE);
      parameters.setParameterSpec(TestAppConst.PARAMETERSPEC1);
      try {
         executionid = delegate.initializeApplication(applicationid, "nojobstep", monitorURL, user, parameters);
         assertTrue("executionid invalid", executionid.equals("-1"));
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
   }

   final public void testExecuteApplication() {
      System.out.println("testing execution");
      testInitializeApplication();
      try {
         delegate.executeApplication(executionid);
      }
      catch (RemoteException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   final public void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   final public void testReturnRegistryEntry() {
      //TODO Implement returnRegistryEntry().
   }

}
