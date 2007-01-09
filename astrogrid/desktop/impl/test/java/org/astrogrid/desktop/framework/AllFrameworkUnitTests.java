/*$Id: AllFrameworkUnitTests.java,v 1.1 2007/01/09 16:12:21 nw Exp $
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

/** tests for the framework package */
public class AllFrameworkUnitTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllFrameworkUnitTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Unit tests for Framework");
		suite.addTestSuite(ReflectionHelperUnitTest.class);
		suite.addTestSuite(ShutdownImplUnitTest.class);

		return suite;
	}

}

/* 
 $Log: AllFrameworkUnitTests.java,v $
 Revision 1.1  2007/01/09 16:12:21  nw
 improved tests - still need extending though.

 Revision 1.1  2006/06/15 09:18:24  nw
 improved junit tests

 Revision 1.2  2006/04/18 23:25:45  nw
 merged asr development.

 Revision 1.1.2.1  2006/03/22 18:01:31  nw
 merges from head, and snapshot of development
 
 */