/*
 * $Id: CeaControllerConfigTest.java,v 1.2 2004/03/23 12:51:25 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CeaControllerConfigTest extends TestCase {

   private CeaControllerConfig config;

   /**
    * Constructor for CeaControllerConfigTest.
    * @param arg0
    */
   public CeaControllerConfigTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CeaControllerConfigTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      config = CeaControllerConfig.getInstance();
      assertNotNull(config);
      
   }

   /*
    * Test for void CeaControllerConfig()
    */
   final public void testCeaControllerConfig() {
      System.out.println(config.toHTMLReport());
   }


}
