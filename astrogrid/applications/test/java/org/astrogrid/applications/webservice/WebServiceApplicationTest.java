/*
 * $Id: WebServiceApplicationTest.java,v 1.1 2004/01/27 15:33:29 pah Exp $
 * 
 * Created on 27-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.webservice;

import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class WebServiceApplicationTest extends BaseDBTestCase {

   private WebServiceApplication webapp;

   private CommandLineApplicationController appcon;

   /**
    * Constructor for WebServiceApplicationTest.
    * @param arg0
    */
   public WebServiceApplicationTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(WebServiceApplicationTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      appcon = new CommandLineApplicationController();//REFACTORME when we implement this we need a web application controller.
      webapp = new WebServiceApplication(appcon,new User());
   }

   final public void testCompletionStatus() {
     try {
          webapp.completionStatus();
          fail("this has not been tested yet");
      }
      catch (UnsupportedOperationException e) {
         e.printStackTrace();
      }
   }

   final public void testExecute() {
      try {
           webapp.execute();
           fail("this has not been tested yet");
       }
       catch (UnsupportedOperationException e) {
          e.printStackTrace();
       }
      
   }

   final public void testRetrieveResult() {
      try {
          webapp.retrieveResult();
          fail("this has not been tested yet");
      }
      catch (UnsupportedOperationException e) {
         e.printStackTrace();
      }

   }

   final public void testCreateLocalTempFile() {
      try {
           webapp.createLocalTempFile();
           fail("this has not been tested yet");
       }
       catch (UnsupportedOperationException e) {
          e.printStackTrace();
       }
   }


}
