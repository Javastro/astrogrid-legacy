/*$Id: AllTests.java,v 1.1 2003/12/01 16:51:04 nw Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cds.querier;

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
        suite.addTest(new TestSuite(VizierConeTest.class));
        suite.addTest(new TestSuite(AdqlVizierTranslatorTest.class));
        suite.addTest(new TestSuite(VizierQuerierTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/