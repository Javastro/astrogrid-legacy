/*$Id: AllTests.java,v 1.2 2003/11/20 15:47:18 nw Exp $
 * Created on 16-Oct-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.cdsdelegate;

import org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImageTest;
import org.astrogrid.datacenter.cdsdelegate.sesame.DeprecatedSesameDelegateTest;
import org.astrogrid.datacenter.cdsdelegate.sesame.SesameDelegateTest;
import org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDListTest;
import org.astrogrid.datacenter.cdsdelegate.ucdresolver.UCDResolverTest;
import org.astrogrid.datacenter.cdsdelegate.vizier.VizierDelegateTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Oct-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.cdsdelegate");
        //$JUnit-BEGIN$
       // suite.addTestSuite(AladinImageTest.class);
        suite.addTestSuite(SesameDelegateTest.class);
        suite.addTestSuite(DeprecatedSesameDelegateTest.class);
        suite.addTestSuite(UCDListTest.class);
        suite.addTestSuite(UCDResolverTest.class);
        suite.addTestSuite(VizierDelegateTest.class);
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2003/11/20 15:47:18  nw
improved testing

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/10/16 10:11:45  nw
first check in
 
*/