/*
 * $Id: WFOApplicationConfigLoadTest.java,v 1.2 2004/03/23 12:51:25 pah Exp $
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

package org.astrogrid.applications.description;

import java.io.IOException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.BaseTestCase;
import org.astrogrid.applications.beans.v1.CommandLineApplication;
import org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig;

import junit.framework.TestCase;
import junit.framework.TestFailure;

/**
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
   
   public void testloadApplicationConfig() throws IOException, MarshalException, ValidationException
   {
      Unmarshaller um = new Unmarshaller(CommandLineExecutionControllerConfig.class);
     
      InputSource saxis;

      saxis = new InputSource(inputFile.openStream());
      CommandLineExecutionControllerConfig clconf =  (CommandLineExecutionControllerConfig)um.unmarshal( saxis);
      assertNotNull(clconf); // TODO do something more sophisticated - look at some values...
      assertEquals("number of test applications", 2, clconf.getApplicationCount());
      CommandLineApplication application = clconf.getApplication(0);
      assertNotNull(application);
      assertEquals("application name", "testapp", application.getName());

   }

}
