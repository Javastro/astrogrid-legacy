/*$Id: AllTests.java,v 1.2 2005/02/10 14:17:21 jdt Exp $
 * Created on 20-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.common;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Jan-2005
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.filemanager.common");
        //$JUnit-BEGIN$
        suite.addTestSuite(FileManagerPropertiesTestCase.class);
        suite.addTestSuite(FileManagerMockTestCase.class);
        suite.addTestSuite(FileManagerPropertyFilterTestCase.class);
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2005/02/10 14:17:21  jdt
merge from  filemanager-nww-903

Revision 1.1.2.1  2005/02/10 13:12:57  nw
One test to rule them all..
 
*/