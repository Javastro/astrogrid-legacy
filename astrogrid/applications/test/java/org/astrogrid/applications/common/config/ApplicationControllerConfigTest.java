/*
 * $Id: ApplicationControllerConfigTest.java,v 1.2 2003/12/31 00:56:17 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerConfigTest extends TestCase {

   /**
    * Constructor for ApplicationControllerConfigTest.
    * @param arg0
    */
   public ApplicationControllerConfigTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationControllerConfigTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   final public void testConfig() {
      ApplicationControllerConfig config = ApplicationControllerConfig.getInstance();
      assertNotNull(config);
       
   }

}
