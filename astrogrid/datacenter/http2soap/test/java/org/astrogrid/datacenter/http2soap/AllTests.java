/*$Id: AllTests.java,v 1.1 2003/11/11 14:43:33 nw Exp $
 * Created on 10-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.http2soap.builder.BuilderTest;
import org.astrogrid.datacenter.http2soap.request.HttpRequestTest;
import org.astrogrid.datacenter.http2soap.response.RegExpConvertorTest;
import org.astrogrid.datacenter.http2soap.response.ScriptConvertorTest;
import org.astrogrid.datacenter.http2soap.response.XsltConvertorTest;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Nov-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.http2soap");

        suite.addTest(new TestSuite(DigesterTest.class));
        suite.addTest(new TestSuite(SampleLegacyServiceServerTest.class));

        suite.addTest(new TestSuite(XsltConvertorTest.class));
        suite.addTest(new TestSuite(ScriptConvertorTest.class));
        suite.addTest(new TestSuite(RegExpConvertorTest.class));
        suite.addTest(new TestSuite(BuilderTest.class));
        suite.addTest(new TestSuite(HttpRequestTest.class));
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version
 
*/