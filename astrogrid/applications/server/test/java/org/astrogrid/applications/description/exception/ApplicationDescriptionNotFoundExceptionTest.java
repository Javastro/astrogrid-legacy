/*
 * $Id: ApplicationDescriptionNotFoundExceptionTest.java,v 1.3 2008/09/13 09:51:06 pah Exp $
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
public class ApplicationDescriptionNotFoundExceptionTest extends TestCase {

   /**
    * Constructor for ApplicationDescriptionNotFoundExceptionTest.
    * @param arg0
    */
   public ApplicationDescriptionNotFoundExceptionTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ApplicationDescriptionNotFoundExceptionTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();
   }

   /*
    * Test for String toString()
    */
   final public void testToString() {
      
      ApplicationDescriptionNotFoundException e = new ApplicationDescriptionNotFoundException("daft");
      assertNotNull(e);
      String result = e.toString();
      assertNotNull(result);
   }

}
