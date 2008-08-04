/*$Id: AllSystemTransportTests.java,v 1.4 2008/08/04 16:37:24 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;

/** Tests the system components.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class AllSystemTransportTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllSystemTransportTests.suite());
    }
/** tests the system components */
    public static Test suite() {
        TestSuite suite = new TestSuite("Transport tests for System Module");
        suite.addTestSuite(ConfigurationRmiTransportTest.class);
        suite.addTestSuite(ConfigurationRpcTransportTest.class);
        suite.addTestSuite(WebServerRmiTransportTest.class);
        suite.addTestSuite(ApiHelpRmiTransportTest.class);
        suite.addTestSuite(ApiHelpRpcTransportTest.class);
        return new ARTestSetup(suite);
    }
}


/* 
$Log: AllSystemTransportTests.java,v $
Revision 1.4  2008/08/04 16:37:24  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.3  2007/01/29 10:42:48  nw
tidied.

Revision 1.2  2007/01/23 11:53:37  nw
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

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/