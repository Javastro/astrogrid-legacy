/*$Id: AllTests.java,v 1.4 2004/04/15 23:11:20 nw Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.integration;

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
        TestSuite suite = new TestSuite("Integration Tests");
        //$JUnit-BEGIN$
        //$JUnit-END$
        suite.addTest(org.astrogrid.installation.integration.AllTests.suite());
        suite.addTest(org.astrogrid.registry.integration.AllTests.suite());
        suite.addTest(org.astrogrid.workflow.integration.AllTests.suite());
        suite.addTest(org.astrogrid.store.integration.AllTests.suite());
        suite.addTest(org.astrogrid.portal.integration.AllTests.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.4  2004/04/15 23:11:20  nw
tweaks

Revision 1.3  2004/04/15 12:27:11  nw
drags tests together

Revision 1.2  2004/04/15 12:12:28  nw
testing

Revision 1.1  2004/04/15 11:55:30  nw
binds everything together
 
*/