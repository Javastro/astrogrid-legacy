/*$Id: AllTests.java,v 1.2 2006/04/18 23:25:45 nw Exp $
 * Created on 21-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Framework tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(ReflectionHelperTest.class);
        //$JUnit-END$
        return suite;
    }

}


/* 
$Log: AllTests.java,v $
Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/