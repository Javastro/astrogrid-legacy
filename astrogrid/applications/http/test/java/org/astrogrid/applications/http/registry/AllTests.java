/*
 * $Id: AllTests.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on 07-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http.registry;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Paul Harrison (pharriso@eso.org) 07-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class AllTests {

   public static Test suite() {
      TestSuite suite = new TestSuite(
            "Test for org.astrogrid.applications.http.registry");
      //$JUnit-BEGIN$
      suite.addTestSuite(RegistryQuerierImplTest.class);
      //$JUnit-END$
      return suite;
   }
}


/*
 * $Log: AllTests.java,v $
 * Revision 1.2  2005/07/05 08:27:01  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 */
