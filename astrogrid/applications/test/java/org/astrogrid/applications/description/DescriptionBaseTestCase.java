/*
 * $Id: DescriptionBaseTestCase.java,v 1.1 2003/12/05 22:52:16 pah Exp $
 * 
 * Created on 04-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
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

import org.astrogrid.applications.common.config.ConfigLoader;
import org.astrogrid.applications.manager.CommandLineApplicationController;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionBaseTestCase extends TestCase {

   /**
    * 
    */
   public DescriptionBaseTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public DescriptionBaseTestCase(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }

   protected CommandLineApplicationController ac;

   protected final String testFile = "TestApplicationConfig.xml";

   protected void setUp() throws Exception {
      super.setUp();
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG);
      urlconfig = (this.getClass()).getResource(testFile);
      assertNotNull("cannot find the input test file " + testFile, urlconfig);
   
      URI uri = new URI(urlconfig.toString());
      inputFile = new File(uri);
      ac = new CommandLineApplicationController();
      assertNotNull("Cannot create application controller", ac);
   }

   protected URL urlconfig;

   protected File inputFile;

   protected String TESTAPPNAME = TestAppConst.TESTAPP_NAME;
   
}
