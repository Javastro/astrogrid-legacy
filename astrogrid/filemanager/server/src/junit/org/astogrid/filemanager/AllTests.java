/*$Id: AllTests.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 18-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astogrid.filemanager;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2005
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astogrid.filemanager");
        //$JUnit-BEGIN$

        //$JUnit-END$

        suite.addTest(org.astrogrid.filemanager.client.AllTests.suite());        
        suite.addTest(org.astrogrid.filemanager.client.delegate.AllTests.suite());     
        suite.addTest(org.astrogrid.filemanager.resolver.AllTests.suite());    
        suite.addTest(org.astrogrid.filemanager.nodestore.AllTests.suite());      
        suite.addTest(org.astrogrid.filemanager.store.tree.AllTests.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:36  nw
split code inito client and server projoects again.

Revision 1.1.2.4  2005/03/01 15:07:37  nw
close to finished now.

Revision 1.1.2.3  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.2  2005/02/25 12:33:27  nw
finished transactional store

Revision 1.1.2.1  2005/02/18 15:50:15  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/