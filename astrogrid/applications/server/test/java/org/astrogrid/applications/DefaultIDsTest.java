/*$Id: DefaultIDsTest.java,v 1.3 2008/09/13 09:51:05 pah Exp $
 * Created on 21-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications;

import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Jul-2005
 *
 */
public class DefaultIDsTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        u = new User();
        id = new DefaultIDs("foo","bar",u);
    }
    DefaultIDs id;
    User u;

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
   
    
    public void testGetId() {
        assertEquals("bar",id.getId());
    }

    public void testGetJobStepId() {
        assertEquals("foo",id.getJobStepId());
    }

    public void testGetUser() {
        assertEquals(u,id.getUser());
    }

}


/* 
$Log: DefaultIDsTest.java,v $
Revision 1.3  2008/09/13 09:51:05  pah
code cleanup

Revision 1.2  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.1.2.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.
 
*/