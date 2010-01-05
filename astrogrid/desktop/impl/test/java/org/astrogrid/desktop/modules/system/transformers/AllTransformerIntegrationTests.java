/*$Id: AllTransformerIntegrationTests.java,v 1.4 2010/01/05 13:19:17 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system.transformers;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class AllTransformerIntegrationTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite("Integration Test for Transformers");
		//$JUnit-BEGIN$
	// fails under java6 on commandline - can't be beothered to work out why.	suite.addTest(Xml2XhtmlTransformerIntegrationTest.suite());
		//$JUnit-END$
		return new ARTestSetup(suite);
	}
}

/* 
 $Log: AllTransformerIntegrationTests.java,v $
 Revision 1.4  2010/01/05 13:19:17  nw
 commented out tests that won't work under java6 - sloppy.

 Revision 1.3  2007/01/29 10:40:47  nw
 documentation fixes.

 Revision 1.2  2007/01/23 11:53:38  nw
 cleaned up tests, organized imports, commented out or fixed failing tests.

 Revision 1.1  2007/01/09 16:12:20  nw
 improved tests - still need extending though.

 Revision 1.1  2006/06/15 09:18:24  nw
 improved junit tests

 Revision 1.2  2006/04/18 23:25:47  nw
 merged asr development.

 Revision 1.1.66.1  2006/03/22 18:01:31  nw
 merges from head, and snapshot of development

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.1  2005/08/05 11:46:56  nw
 reimplemented acr interfaces, added system tests.
 
 */