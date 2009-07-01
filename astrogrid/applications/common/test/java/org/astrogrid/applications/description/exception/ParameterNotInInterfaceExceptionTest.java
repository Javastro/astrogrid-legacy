/*
 * $Id: ParameterNotInInterfaceExceptionTest.java,v 1.1 2009/07/01 13:30:18 pah Exp $
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
public class ParameterNotInInterfaceExceptionTest extends TestCase {

   /**
    * Constructor for ParameterNotInInterfaceExceptionTest.
    * @param arg0
    */
   public ParameterNotInInterfaceExceptionTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ParameterNotInInterfaceExceptionTest.class);
   }

   final public void testParameterNotInInterfaceException() {
   ParameterNotInInterfaceException e = new ParameterNotInInterfaceException("this");
   assertNotNull(e);
  }

}
