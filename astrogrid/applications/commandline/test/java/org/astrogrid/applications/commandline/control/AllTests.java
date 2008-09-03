/*
 * $Id: AllTests.java,v 1.2 2008/09/03 14:18:58 pah Exp $
 * 
 * Created on 25 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.control;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
	TestSuite suite = new TestSuite(
		"Test for org.astrogrid.applications.commandline.control");
	//$JUnit-BEGIN$
	suite.addTestSuite(CommandLineCECControlTest.class);
	//$JUnit-END$
	return suite;
    }

}


/*
 * $Log: AllTests.java,v $
 * Revision 1.2  2008/09/03 14:18:58  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/03/26 17:29:51  pah
 * Unit tests pass
 *
 */
