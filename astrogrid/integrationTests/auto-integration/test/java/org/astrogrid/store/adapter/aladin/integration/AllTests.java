/*$Id: AllTests.java,v 1.2 2004/11/11 17:54:18 clq2 Exp $
 * Created on 11-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.store.adapter.aladin.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2004
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "StoreClient and Aladin Integration");
        //$JUnit-BEGIN$
        suite.addTestSuite(IterationSixAladinAdapterTest.class);
        suite.addTestSuite(OddBehaviourOfStoreClientTest.class);
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2004/11/11 17:54:18  clq2
nww-660

Revision 1.1.2.1  2004/11/11 13:10:05  nw
tests for aladin adapter
 
*/