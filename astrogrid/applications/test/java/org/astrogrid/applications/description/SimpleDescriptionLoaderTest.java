/*
 * $Id: SimpleDescriptionLoaderTest.java,v 1.2 2003/12/05 22:52:16 pah Exp $
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

import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.applications.manager.CommandLineApplicationController;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class SimpleDescriptionLoaderTest extends DescriptionBaseTestCase {

   SimpleDescriptionLoader dl = null;
   /**
    * Constructor for SimpleDescriptionLoaderTest.
    * @param arg0
    */
   public SimpleDescriptionLoaderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(SimpleDescriptionLoaderTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      dl = new SimpleDescriptionLoader(ac);
   }

   final public void testLoadDescription() {
      dl.loadDescription(inputFile);
      SimpleApplicationDescription appdesc = ac.getApplicationDescription(TESTAPPNAME);
      assertNotNull(appdesc); // should do something more sophisticated
      
   }

}
