/*$Id: DatacenterApplicationTest.java,v 1.1 2004/07/13 17:11:32 nw Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import junit.framework.TestCase;

/** Test whatever we can of the application object.
 * difficult one to test - as its a boundary class between cea and datacenter - and each takes different approaches to interfaces, mocking, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationTest extends TestCase {
    /**
     * Constructor for DatacenterApplicationTest.
     * @param arg0
     */
    public DatacenterApplicationTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
}


/* 
$Log: DatacenterApplicationTest.java,v $
Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/