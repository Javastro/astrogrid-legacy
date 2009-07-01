/*
 * $Id: InterfaceDescriptionNotFoundExceptionTest.java,v 1.1 2009/07/01 13:30:17 pah Exp $
 * 
 * Created on 27-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.exception;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class InterfaceDescriptionNotFoundExceptionTest extends TestCase {

   /**
    * Constructor for InterfaceDescriptionNotFoundExceptionTest.
    * @param arg0
    */
   public InterfaceDescriptionNotFoundExceptionTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(InterfaceDescriptionNotFoundExceptionTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();
   }

   /*
    * Test for void InterfaceDescriptionNotFoundException(String)
    */
   final public void testInterfaceDescriptionNotFoundExceptionString() {
      InterfaceDescriptionNotFoundException e = new InterfaceDescriptionNotFoundException("that");
      assertNotNull(e);
   }

}
