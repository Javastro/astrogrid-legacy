/*
 * $Id: DescriptionLoaderTest.java,v 1.1 2003/11/26 22:07:24 pah Exp $
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
import org.astrogrid.applications.manager.CommandLineApplicationController;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * Tests the description loader. Expects to find a a file TestApplicationConfig.xml in the Classpath
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionLoaderTest extends TestCase {

   private CommandLineApplicationController ac;

   private URL urlconfig;
   private static String testFile = "TestApplicationConfig.xml";

   private DescriptionLoader dl;

   private File inputFile;

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

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      urlconfig = (this.getClass()).getResource(testFile);
      assertNotNull("cannot find the input test file " + testFile, urlconfig);

      URI uri = new URI(urlconfig.toString());
      inputFile = new File(uri);
      ac = new CommandLineApplicationController();
      assertNotNull("Cannot create application controller", ac);
      testDescriptionLoader(); //call this even though it is a trivial test
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
            ac.getApplicationDescriptions().getDescription("testapp");
      }
      catch (ApplicationDescriptionNotFoundException e) {
         throw new AssertionFailedError("expected application testapp not found");
      }
   }

   final public void testCreateDigester() {
      Digester digester = dl.createDigester();
      assertNotNull("could not create digester", digester);
   }

}
