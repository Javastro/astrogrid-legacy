/*
 * $Id: TestApplicationControllerDelegate.java,v 1.6 2004/03/25 11:28:28 pah Exp $
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


/**
 * This test is intended to be run as an installation test for the service.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class TestApplicationControllerDelegate extends AbstractDelegateTestCase {

   /**
    * Constructor for ApplicationControllerDelegateTest.
    * @param arg0
    */

   private static String localendpoint =
      "http://localhost:8080/astrogrid-applications/services/ApplicationControllerService";

   public TestApplicationControllerDelegate(String arg0) {
      super(arg0);
      endpoint = localendpoint;

   }

   public static void main(String[] args) {
      //TODO set the endpoint from here?
      junit.textui.TestRunner.run(TestApplicationControllerDelegate.class);
      if (args.length > 0) {
         localendpoint = args[0];
      }

   }
}
