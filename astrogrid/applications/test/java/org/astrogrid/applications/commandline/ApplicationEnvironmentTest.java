/*
 * $Id: ApplicationEnvironmentTest.java,v 1.2 2004/03/23 19:46:04 pah Exp $
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

import java.io.File;

import org.astrogrid.applications.common.config.BaseDBTestCase;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationEnvironmentTest extends BaseDBTestCase {

   private CmdLineApplicationEnvironment env;

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
      env = new CmdLineApplicationEnvironment(config);
      assertNotNull(env);
      
      
   }

   final public void testGetErrorLog() {
     File f = env.getErrorLog();
     assertNotNull(f);
      
   }

   final public void testGetExecutionDirectory() {
     File f = env.getExecutionDirectory();
     assertNotNull(f);
   }

   final public void testGetExecutionId() {
     int id = env.getExecutionId();
     int id2 = env.getExecutionId();
     
     assertTrue("a particular environment should always return the same executionid", id == id2);
   }

   final public void testGetOutputLog() {
      File f = env.getOutputLog();
      assertNotNull(f);
   }

}
