/*$Id: AllTests.java,v 1.1 2003/08/28 15:26:44 nw Exp $
 * Created on 28-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Aug-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.adql");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(MarshallingTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2003/08/28 15:26:44  nw
unit tests for adql
 
*/