/*
 * $Id: AllTests.java,v 1.1 2003/08/19 00:04:38 pah Exp $
 * 
 * Created on 19-Aug-2003 by pah
 *
 * Copyright ©2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.services.authentication;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class AllTests {

   public static Test suite() {
      TestSuite suite =
         new TestSuite("Test for org.astrogrid.community.services.authentication");
      //$JUnit-BEGIN$
      suite.addTest(new TestSuite(SimpleAuthenticatonServiceTestCase.class));
      //$JUnit-END$
      return suite;
   }
}
