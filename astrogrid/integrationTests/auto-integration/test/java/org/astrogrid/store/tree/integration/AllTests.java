/*$Id: AllTests.java,v 1.3 2005/03/11 13:36:22 clq2 Exp $
 * Created on 16-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.tree.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Nov-2004
 *@deprecated remove this package when we get shot of myspace
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.store.tree.integration");
        //$JUnit-BEGIN$
        suite.addTestSuite(IterationSixTreeClientTest.class);
        suite.addTestSuite(OddBehaviourOfStoreClientTest.class);
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.3  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.2.40.1  2005/03/10 19:33:16  nw
marked tests for removal once myspace is replaced by filemanager.

Revision 1.2  2004/11/17 16:22:34  clq2
nww-itn07-704a

Revision 1.1.2.1  2004/11/16 17:24:42  nw
added tests for new treeclient interface to myspace.
 
*/