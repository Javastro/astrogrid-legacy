/*
 * $Id: WFOApplicationConfigLoadTest.java,v 1.4 2005/07/05 08:26:56 clq2 Exp $
 * 
 * Created on 17-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import java.io.IOException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.beans.v1.cea.implementation.CommandLineExecutionControllerConfig;

/**
 * Tests whether castor is capable of direct loading of the application config file.
 * @TODO this test fails because castor is not capable of doing this yet without some massaging of the input xml - maybe one day - in fact digester is used !
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class WFOApplicationConfigLoadTest extends DescriptionBaseTestCase {

   /**
    * Constructor for WFOApplicationConfigLoadTest.
    * @param arg0
    */
   public WFOApplicationConfigLoadTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(WFOApplicationConfigLoadTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }
   
   public void testloadApplicationConfig()
   {
      Unmarshaller um = new Unmarshaller(CommandLineExecutionControllerConfig.class);
     
      InputSource saxis;

      try {
         saxis = new InputSource(inputFile.openStream());
         CommandLineExecutionControllerConfig clconf =  (CommandLineExecutionControllerConfig)um.unmarshal( saxis);
         fail("hey - castor managed to unmarshall a CEA commandline config file - take a look at using castor rather than digester - this is success!!");
         assertNotNull(clconf); // TODO do something more sophisticated - look at some values...
         assertEquals("number of test applications", 3, clconf.getApplicationCount());
         org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication application = clconf.getApplication(0);
         assertNotNull(application);
         assertEquals("application name", "fake.authority.id/testapp", application.getName());
         
      } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         //currently expecting castor to fail for this, so failuer is normal - 
      }

   }

}
