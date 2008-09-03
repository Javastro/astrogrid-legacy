/*$Id: AllTests.java,v 1.3 2008/09/03 14:19:06 pah Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.idgen;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for idgen");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(InMemoryIdGenTest.class));
        suite.addTest(new TestSuite(GloballyUniqueIdGenTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.3  2008/09/03 14:19:06  pah
result of merge of pah_cea_1611 branch

Revision 1.2.286.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/