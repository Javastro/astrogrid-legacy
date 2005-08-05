/*$Id: AllTests.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.desktop.framework.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.desktop.modules.system - against single ACR instance");
        suite.addTestSuite(ConfigurationTest.class);
        suite.addTestSuite(ConfigurationRmiTest.class);
        suite.addTestSuite(ConfigurationRpcTest.class);
        suite.addTestSuite(WebServerTest.class);
        suite.addTestSuite(WebServerRmiTest.class);
        suite.addTestSuite(WebServerRpcTest.class);
        suite.addTestSuite(ApiHelpTest.class);
        suite.addTestSuite(ApiHelpRmiTest.class);
        suite.addTestSuite(ApiHelpRpcTest.class);
        suite.addTestSuite(HtmlServletTest.class);
        suite.addTestSuite(XmlRpcServletTest.class);       
        return new ACRTestSetup(suite);
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/