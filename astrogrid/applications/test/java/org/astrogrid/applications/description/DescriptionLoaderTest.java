/*
 * $Id: DescriptionLoaderTest.java,v 1.10 2004/01/27 15:33:29 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.digester.Digester;

import org
   .astrogrid
   .applications
   .description
   .exception
   .ApplicationDescriptionNotFoundException;
import org
   .astrogrid
   .applications
   .description
   .exception
   .InterfaceDescriptionNotFoundException;
import org
   .astrogrid
   .applications
   .description
   .exception
   .ParameterDescriptionNotFoundException;
import org
   .astrogrid
   .applications
   .description
   .exception
   .ParameterNotInInterfaceException;
import org.astrogrid.applications.manager.CommandLineApplicationController;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Tests the description loader. Expects to find a a file TestApplicationConfig.xml in the Classpath
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionLoaderTest extends DescriptionBaseTestCase {

   private DescriptionLoader dl;

   /**
    * Constructor for DescriptionLoaderTest.
    * @param arg0
    */
   public DescriptionLoaderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(DescriptionLoaderTest.class);
   }

   final public void testDescriptionLoader() {
      dl = new DescriptionLoader(ac);
      assertNotNull("cannot create the DescriptionLoader", dl);
   }

   final public void testLoadDescription() {
      boolean success = dl.loadDescription(inputFile);

      assertTrue("The load failed", success);
      try {
         ApplicationDescription ad =
            ac.getApplicationDescriptions().getDescription(TESTAPPNAME);
            assertEquals("instance class",ad.getInstanceClass(),null);
            
            assertTrue("execution path", ad.getExecutionPath().endsWith( "testapp.sh")); //this is dependent on the actual location
         ParameterDescription[] params = ad.getParameters();
         assertNotNull("no parameters returned", params);
         assertEquals("there should be 12 parameters ", 12,params.length);
         //now look at the parameters in detail
         ParameterDescription p1 = params[0];

         try {
            ParameterDescription p2 = ad.getParameter(params[0].getName());
            assertSame(
               "parameters should be same via two different retrieval methods",
               p1,
               p2);
            p1 = ad.getParameter("P1"); // get p1 ready for later
         }
         catch (ParameterDescriptionNotFoundException e1) {
            fail("did not find a parameter that it should");
         }
         // try getting a parameter that should not be there
         try {
            ParameterDescription p3 = ad.getParameter("silly");
            fail("getting non existant parameter should throw exception");
         }
         catch (ParameterDescriptionNotFoundException e2) {
            // do nothing this should get here
         }

         // lets look at a some of the properties
         assertEquals("commandposition", 1, p1.getCommandPosition());
         System.out.println("name:" + p1.getName());
         System.out.println("desc:" + p1.getDisplayDescription());
         ApplicationInterface intf = null;
         try {
            intf = ad.getInterface("I1");
         }
         catch (InterfaceDescriptionNotFoundException e3) {
            fail("could not find the I1 interface");
         }
         assertNotNull("the inferface object is null", intf);

         String[] pds = intf.getArrayofInputs();
         assertEquals("interfaace -wrong number of input parametes", 4, pds.length);
         assertEquals("input parameter name", "P1", pds[0]);
         try {
            ParameterDescription inp1 = intf.getInputParameter("P2");
         }
         catch (ParameterNotInInterfaceException e4) {
            fail("paramter not found");
         }
         String[] pd2s = intf.getArrayofOutputs();
          assertEquals("wrong number of output parametes",1, pd2s.length);
          assertEquals("output parameter name", "P3", pd2s[0]);
          try {
             ParameterDescription inp1 = intf.getOutputParameter("P3");
          }
          catch (ParameterNotInInterfaceException e4) {
             fail("paramter not found");
          }

      }
      catch (ApplicationDescriptionNotFoundException e) {
         fail("expected application testapp not found");
      }
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      testDescriptionLoader(); //call this even though it is a trivial test

   }

}
