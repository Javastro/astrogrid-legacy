/*$Id: AllTests.java,v 1.2 2004/06/16 12:09:21 KevinBenson Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Registry");
        //$JUnit-BEGIN$
       suite.addTest(new TestSuite(RegistryInstallationTest.class));
       suite.addTest(new TestSuite(RegistryFunctionTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2004/06/16 12:09:21  KevinBenson
New installer much like auto-integration except it only install the registry

Revision 1.1.2.1  2004/06/15 10:13:11  KevinBenson
A new installer much like auto-integration area.  That just installs the registry

Revision 1.3  2004/05/07 15:32:36  pah
more registry tests to flush out the fact that new entries are not being added

Revision 1.2  2004/04/15 12:12:28  nw
testing

Revision 1.1  2004/04/15 11:55:16  nw
added registy installation test
 
*/