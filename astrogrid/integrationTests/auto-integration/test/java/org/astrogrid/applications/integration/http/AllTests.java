/*$Id: AllTests.java,v 1.3 2004/09/14 16:35:15 jdt Exp $
 * Created on 29-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.integration.http;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
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
		suite.addTestSuite(HttpServerInstallationTest.class);
		//$JUnit-END$
		return suite;
	}
}

/* 
 $Log: AllTests.java,v $
 Revision 1.3  2004/09/14 16:35:15  jdt
 Added tests for an http-post service.

 Revision 1.2  2004/09/02 11:18:09  jdt
 Merges from case 3 branch for SIAP.

 Revision 1.1.22.1  2004/08/20 00:36:19  jdt
 moved HelloWorld tests

 Revision 1.1  2004/07/01 11:43:33  nw
 cea refactor
 
 */