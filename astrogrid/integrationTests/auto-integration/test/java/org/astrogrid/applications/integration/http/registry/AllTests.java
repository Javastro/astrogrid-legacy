/*$Id: AllTests.java,v 1.2 2004/09/02 11:18:09 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.integration.http.registry;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A suite of all the tests in this package
 * @author jdt
 *
 */
public class AllTests {
	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.class);
	}
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for HTTP CEA Provider");
		//$JUnit-BEGIN$
		suite.addTestSuite(RegistryQuerierImplTest.class);
		//$JUnit-END$
		return suite;
	}
}

/* 
 $Log: AllTests.java,v $
 Revision 1.2  2004/09/02 11:18:09  jdt
 Merges from case 3 branch for SIAP.

 Revision 1.1.2.2  2004/08/23 13:18:02  jdt
 More tests, and some package info.

 */