/*
 * $Id: StoreInstallationTest.java,v 1.1 2004/05/17 12:37:31 pah Exp $
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

package org.astrogrid.store.integration;

import java.util.List;

import org.astrogrid.community.User;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-May-2004
 * @version $Name:  $
 * @since iteration5
 */
public class StoreInstallationTest extends AbstractTestForIntegration {

   private VoSpaceClient client;

   private User testUser;
   private MySpaceIt05Delegate mySpace;

   /**
    * Constructor for StoreInstallationTest.
    * @param arg0
    */
   public StoreInstallationTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(StoreInstallationTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   
      client = new VoSpaceClient(user);
      List mySpaceList = ag.getMyspaces();
      assertNotNull(mySpaceList);
      assertTrue("no myspacce query endpoints", mySpaceList.size() > 0);
      Service service = (Service)mySpaceList.get(0);
      mySpace = (MySpaceIt05Delegate)service.createDelegate();
      assertNotNull(mySpace);


   }
   
   void testUsers()
   {
    
   }

}
