/*
 * $Id: DescriptionBaseTestCase.java,v 1.2 2004/07/01 11:07:59 nw Exp $
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

package org.astrogrid.applications.commandline;

import java.net.URL;

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
   }


   protected void setUp() throws Exception {
      super.setUp();      
      inputFile = this.getClass().getResource("/TestApplicationConfig.xml");
      assertNotNull("application config file not found:", inputFile);
      int i = TestAppConst.TESTAPP_NAME.indexOf("/");
      
      if (i != -1) {
         TESTAPPNAME = TestAppConst.TESTAPP_NAME.substring(i+1);
      }
      else
      {
         TESTAPPNAME = TestAppConst.TESTAPP_NAME;
      }
   }


   protected URL inputFile;

   protected String TESTAPPNAME;

   
}
