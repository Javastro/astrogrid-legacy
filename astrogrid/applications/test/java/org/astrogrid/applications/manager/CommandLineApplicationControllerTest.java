/*
 * $Id: CommandLineApplicationControllerTest.java,v 1.2 2003/12/04 13:26:25 pah Exp $
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

package org.astrogrid.applications.manager;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationControllerTest extends TestCase {

   private CommandLineApplicationController controller;

   /**
    * Constructor for CommandLineApplicationControllerTest.
    * @param arg0
    */
   public CommandLineApplicationControllerTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CommandLineApplicationControllerTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      controller = new CommandLineApplicationController();
   }

   final public void testExecuteApplication() {
      //TODO Implement executeApplication().
   }

   final public void testGetApplicationDescription() {
      //TODO Implement getApplicationDescription().
   }

   final public void testListApplications() {
      //TODO Implement listApplications().
   }

   final public void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
   }

   final public void testReturnRegistryEntry() {
      //TODO Implement returnRegistryEntry().
   }

   final public void testInitializeApplication() {
      //TODO Implement initializeApplication().
   }

}
