/*$Id: AllTests.java,v 1.1 2003/09/05 01:04:26 nw Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.queriers");
        //$JUnit-BEGIN$
        suite.addTest(QuerierTest.suite());
        suite.addTest(QueryTranslatorTest.suite());
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2003/09/05 01:04:26  nw
bind things together
 
*/