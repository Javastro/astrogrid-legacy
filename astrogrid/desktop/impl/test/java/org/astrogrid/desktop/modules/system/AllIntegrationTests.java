/*$Id: AllIntegrationTests.java,v 1.1 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** Tests the system components.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class AllIntegrationTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllIntegrationTests.suite());
    }
/** tests the system components */
    public static Test suite() {
        TestSuite suite = new TestSuite("Integration tests for System Module");
        suite.addTestSuite(ConfigurationIntegrationTest.class);
        suite.addTestSuite(ConfigurationRmiIntegrationTest.class);
        suite.addTestSuite(ConfigurationRpcIntegrationTest.class);
        suite.addTestSuite(WebServerIntegrationTest.class);
        suite.addTestSuite(WebServerRmiIntegrationTest.class);
        suite.addTestSuite(WebServerRpcIntegrationTest.class);
        suite.addTestSuite(ApiHelpIntegrationTest.class);
        suite.addTestSuite(ApiHelpRmiIntegrationTest.class);
        suite.addTestSuite(ApiHelpRpcIntegrationTest.class);
        suite.addTestSuite(HtmlTransportIntegrationTest.class);
        suite.addTestSuite(XmlRpcTransportIntegrationTest.class);    
        suite.addTestSuite(BackgroundExecutorIntegrationTest.class);
        suite.addTestSuite(UIInternalIntegrationTest.class);
        suite.addTestSuite(RmiTransportIntegrationTest.class);
        return new ACRTestSetup(suite);
    }
}


/* 
$Log: AllIntegrationTests.java,v $
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