/*$Id: AllTests.java,v 1.4 2004/12/07 16:50:33 jdt Exp $
 * Created on 22-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import org.astrogrid.scripting.groovy.GroovyTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.scripting");
        //$JUnit-BEGIN$
        suite.addTestSuite(ServicesTest.class);
        suite.addTestSuite(ObjectBuilderTest.class);
        suite.addTestSuite(ToolboxTest.class);
        //$JUnit-END$
        suite.addTest(org.astrogrid.scripting.table.AllTests.suite());
        suite.addTest(GroovyTest.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.4  2004/12/07 16:50:33  jdt
merges from scripting-nww-805

Revision 1.3.2.1  2004/12/07 14:47:58  nw
got table manipulation working.

Revision 1.3  2004/12/06 20:03:03  clq2
nww_807a

Revision 1.2.4.1  2004/12/06 13:27:47  nw
fixes to improvide integration with external values and starTables.

Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/