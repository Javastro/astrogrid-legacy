/*
 * $Id: ApplicationControllerDelegateTest.java,v 1.2 2003/12/09 23:01:15 pah Exp $
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

import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.applications.description.TestAppConst;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerDelegateTest extends TestCase {

   /**
    * Constructor for ApplicationControllerDelegateTest.
    * @param arg0
    */
   
   ApplicationControllerDelegate delegate;
   public ApplicationControllerDelegateTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationControllerDelegateTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      delegate = new ApplicationControllerDelegate("http://localhost:8080/astrogrid-applications/services/ApplicationControllerService");
      assertNotNull(delegate);
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
      //TODO Implement initializeApplication().
   }

   final public void testExecuteApplication() {
      //TODO Implement executeApplication().
   }

   final public void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   final public void testReturnRegistryEntry() {
      //TODO Implement returnRegistryEntry().
   }

}
