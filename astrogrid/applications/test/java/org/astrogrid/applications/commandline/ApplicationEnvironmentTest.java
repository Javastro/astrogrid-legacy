/*
 * $Id: ApplicationEnvironmentTest.java,v 1.1 2003/12/11 14:36:16 pah Exp $
 * 
 * Created on 11-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.common.config.BaseDBTestCase;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationEnvironmentTest extends BaseDBTestCase {

   /**
    * Constructor for ApplicationEnvironmentTest.
    * @param arg0
    */
   public ApplicationEnvironmentTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationEnvironmentTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      
   }

   final public void testGetErrorLog() {
      //TODO Implement getErrorLog().
      fail("no test yet");
      
   }

   final public void testGetExecutionDirectory() {
      //TODO Implement getExecutionDirectory().
   }

   final public void testGetExecutionId() {
      //TODO Implement getExecutionId().
   }

   final public void testGetOutputLog() {
      //TODO Implement getOutputLog().
   }

}
