/*$Id: AllTests.java,v 1.1 2004/07/07 09:17:40 KevinBenson Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.sec.querier;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.cds.querier");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(EgsoQuerierTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/