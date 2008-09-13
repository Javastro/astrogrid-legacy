/*$Id: InMemoryIdGenTest.java,v 1.3 2008/09/13 09:51:04 pah Exp $
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

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class InMemoryIdGenTest extends TestCase {
    /**
     * Constructor for InMemoryIdGenTest.
     * @param arg0
     */
    public InMemoryIdGenTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        id = new InMemoryIdGen();
    }
    protected IdGen id;
    public void testGetNewID() {
        String a = id.getNewID();
        assertNotNull(a);
        String b = id.getNewID();
        assertNotNull(b);
        assertFalse(a.equals(b));
    }
}


/* 
$Log: InMemoryIdGenTest.java,v $
Revision 1.3  2008/09/13 09:51:04  pah
code cleanup

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/