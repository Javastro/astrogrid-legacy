/*$Id: AllTests.java,v 1.4 2004/07/30 15:42:34 nw Exp $
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
        suite.addTestSuite(BasicComponentManagerTest.class);
        suite.addTestSuite(GroovyDrivenComponentManagerTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.4  2004/07/30 15:42:34  nw
 merged in branch nww-itn06-bz#441 (groovy scripting)

 Revision 1.3.20.2  2004/07/30 15:10:04  nw
 removed policy-based implementation,
 adjusted tests, etc to use groovy implementation

 Revision 1.3.20.1  2004/07/28 16:24:23  nw
 finished groovy beans.
 moved useful tests from old python package.
 removed python implemntation

 Revision 1.3  2004/07/09 09:32:12  nw
 merged in scripting workflow interpreter from branch
 nww-x-workflow-extensions

 Revision 1.2  2004/03/07 21:04:38  nw
 merged in nww-itn05-pico - adds picocontainer

 Revision 1.1.4.1  2004/03/07 20:42:31  nw
 updated tests to work with picocontainer

 Revision 1.1  2004/03/03 01:13:42  nw
 updated jes to work with regenerated workflow object model
 
 */