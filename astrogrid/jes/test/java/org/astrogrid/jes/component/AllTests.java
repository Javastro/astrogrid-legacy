/*$Id: AllTests.java,v 1.1 2004/03/03 01:13:42 nw Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.component");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ComponentManagerTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/