/*
 * $Id: ApplicationControllerDelegateTest.java,v 1.1 2003/12/08 23:01:53 pah Exp $
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
      delegate = new ApplicationControllerDelegate(null);
      assertNotNull(delegate);
   }


   final public void testListApplications() {
      try {
         String[] apps = delegate.listApplications();
      }
      catch (RemoteException e) {
         fail("unknown exception");
         e.printStackTrace();
      }
   }

   final public void testGetApplicationDescription() {
      //TODO Implement getApplicationDescription().
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
