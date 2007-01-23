/*$Id: AllFrameworkIntegrationTests.java,v 1.2 2007/01/23 11:53:38 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/** tests for the framework package */
public class AllFrameworkIntegrationTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllFrameworkIntegrationTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Integration tests for Framework");
		suite.addTest(ShutdownImplIntegrationTest.suite());
		return new ARTestSetup(suite);
	}

}

/* 
 $Log: AllFrameworkIntegrationTests.java,v $
 Revision 1.2  2007/01/23 11:53:38  nw
 cleaned up tests, organized imports, commented out or fixed failing tests.

 Revision 1.1  2007/01/09 16:12:21  nw
 improved tests - still need extending though.

 Revision 1.1  2006/06/15 09:18:24  nw
 improved junit tests

 Revision 1.2  2006/04/18 23:25:45  nw
 merged asr development.

 Revision 1.1.2.1  2006/03/22 18:01:31  nw
 merges from head, and snapshot of development
 
 */