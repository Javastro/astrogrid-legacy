/*
 * $Id: CommandLineDescriptionsLoaderTest.java,v 1.2 2004/07/01 11:07:59 nw Exp $
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

package org.astrogrid.applications.commandline.digester;

import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.commandline.DescriptionBaseTestCase;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.manager.idgen.InMemoryIdGen;
import org.astrogrid.applications.parameter.indirect.DefaultIndirectionProtocolLibrary;

import org.picocontainer.PicoException;
import org.picocontainer.defaults.ConstructorInjectionComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;
import java.net.URL;

/**
 * Tests the description loader. Expects to find a a file TestApplicationConfig.xml in the Classpath
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineDescriptionsLoaderTest extends DescriptionBaseTestCase {

   protected CommandLineDescriptionsLoader dl;

   /**
    * Constructor for DescriptionLoaderTest.
    * @param arg0
    */
   public CommandLineDescriptionsLoaderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CommandLineDescriptionsLoaderTest.class);
   }


   protected void setUp() throws Exception {
      super.setUp();
      final File workingDir = File.createTempFile("DescriptionLoaderTest",null);
      workingDir.delete();
      workingDir.mkdir();
      assertTrue(workingDir.exists());
      workingDir.deleteOnExit();
      DefaultPicoContainer container = new DefaultPicoContainer();
      container.registerComponent(
        new ConstructorInjectionComponentAdapter(CommandLineApplicationDescription.class,CommandLineApplicationDescription.class)
      );
      container.registerComponentImplementation(InMemoryIdGen.class);
      container.registerComponentImplementation(DefaultIndirectionProtocolLibrary.class);
      container.registerComponentImplementation(ApplicationDescriptionEnvironment.class);
                        
      try {
          container.verify();
      } catch (PicoException t) {
          t.printStackTrace();
          fail("Container misconfigured");
      }
      dl = new CommandLineDescriptionsLoader(new CommandLineDescriptionsLoader.DescriptionURL() {
    
        public URL getURL() {
            return inputFile;
        }
      },new CommandLineApplicationDescriptionFactory(container));
      assertNotNull("cannot create the DescriptionLoader", dl); 

   }

   final public void testLoadDescription() throws ApplicationDescriptionNotLoadedException {

      try {
         ApplicationDescription x =
            dl.getDescription(TESTAPPNAME);
        assertTrue(x instanceof CommandLineApplicationDescription);
        CommandLineApplicationDescription ad = (CommandLineApplicationDescription)x;
            assertEquals("instance class",ad.getInstanceClass(),null);
            
            assertTrue("execution path", ad.getExecutionPath().endsWith( "testapp.sh")); //this is dependent on the actual location
         ParameterDescription[] params = ad.getParameterDescriptions();
         assertNotNull("no parameters returned", params);
         assertEquals("there should be 14 parameters ", 14,params.length);
         //now look at the parameters in detail
         ParameterDescription p1 = params[0];

         try {
            ParameterDescription p2 = ad.getParameterDescription(params[0].getName());
            assertSame(
               "parameters should be same via two different retrieval methods",
               p1,
               p2);
            p1 = ad.getParameterDescription("P1"); // get p1 ready for later
         }
         catch (ParameterDescriptionNotFoundException e1) {
            fail("did not find a parameter that it should");
         }
         // try getting a parameter that should not be there
         try {
            ParameterDescription p3 = ad.getParameterDescription("silly");
            fail("getting non existant parameter should throw exception");
         }
         catch (ParameterDescriptionNotFoundException e2) {
            // do nothing this should get here
         }
         assertTrue(p1 instanceof CommandLineParameterDescription);
        
         // lets look at a some of the properties
         assertEquals("commandposition", 1, ((CommandLineParameterDescription)p1).getCommandPosition());
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
             assertTrue(inp1 instanceof CommandLineParameterDescription);
             assertTrue(((CommandLineParameterDescription)inp1).isFile());
          }
          catch (ParameterNotInInterfaceException e4) {
             fail("paramter not found");
          }

      }
      catch (ApplicationDescriptionNotFoundException e) {
         fail("expected test application "+TESTAPPNAME+"not found");
      }
   }



}
