/*
 * $Id: CmdLineApplicationTest.java,v 1.1 2003/12/11 14:36:16 pah Exp $
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

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CmdLineApplicationTest extends BaseDBTestCase {

   /**
    * Constructor for CmdLineApplicationTest.
    * @param arg0
    */
   public CmdLineApplicationTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CmdLineApplicationTest.class);
   }

   /*
    * @see BaseDBTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   final public void testExecute() {
      //TODO Implement execute().
      fail("no test yet");
   }

   final public void testSetupParameters() {
      //TODO Implement setupParameters().
   }

   final public void testEndApplication() {
      //TODO Implement endApplication().
   }

   final public void testWaitForApplication() {
      //TODO Implement waitForApplication().
   }

   final public void testStartApplication() {
      //TODO Implement startApplication().
   }

}
