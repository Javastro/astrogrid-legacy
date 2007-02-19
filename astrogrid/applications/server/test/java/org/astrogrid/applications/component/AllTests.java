/*$Id: AllTests.java,v 1.3 2007/02/19 16:20:32 gtr Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Jun-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.applications.component");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(BaseCEAComponentManagerTest.class));
        suite.addTest(new TestSuite(CEAComponentManagerFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2007/02/19 16:20:32  gtr
Branch apps-gtr-1061 is merged.

Revision 1.2.264.1  2007/01/18 18:18:19  gtr
Javaclass-application stuff is moved to a separate project.

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/