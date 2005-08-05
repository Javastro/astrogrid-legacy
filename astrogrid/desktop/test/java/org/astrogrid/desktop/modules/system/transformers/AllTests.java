/*$Id: AllTests.java,v 1.1 2005/08/05 11:46:56 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.transformers;

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
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.system.transformers");
        //$JUnit-BEGIN$
        suite.addTestSuite(TypeStructureTransformerTest.class);
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/