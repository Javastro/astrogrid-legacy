/* $Id: DeploymentTests.java,v 1.1 2004/03/04 19:08:20 jdt Exp $
 * Created on 21-Jan-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.installationtest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author jdt
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DeploymentTests {

  public static Test suite() {
    TestSuite suite =
      new TestSuite("Test for org.astrogrid.jes.installationTests");
    //$JUnit-BEGIN$
    //$JUnit-END$
    return suite;
  }
}

/*
*$Log: DeploymentTests.java,v $
*Revision 1.1  2004/03/04 19:08:20  jdt
*Package name changed to lower case to satisfy coding standards.  mea culpa - didn't read the Book.  Tx Martin.
*
*Revision 1.2  2004/03/04 02:14:08  nw
*removed old instalation tests - will need to write new ones
*
*Revision 1.1  2004/01/21 17:51:34  jdt
*Some installation tests, and some bug fixes of configuration.jsp and some broken links
*
*/