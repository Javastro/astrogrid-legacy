/*$Id: AbstractDelegateTest.java,v 1.2 2004/02/09 11:41:44 nw Exp $
 * Created on 09-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.Delegate;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public class AbstractDelegateTest extends TestCase {
    /**
     * Constructor for AbstractDelegateTest.
     * @param arg0
     */
    public AbstractDelegateTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        del = new AbstractDelegate(){
        };
    }
    protected AbstractDelegate del;
    public void testSetTargetEndPoint() {
        del.setTargetEndPoint(null);
        assertNull(del.getTargetEndPoint());
        del.setTargetEndPoint(" foo  ");
        assertEquals(" foo  ",del.getTargetEndPoint());        
    }
    public void testSetTimeout() {
        del.setTimeout(0);
        assertEquals(0,del.getTimeout());
    }
    public void testIsTestDelegateRequired() {
        assertTrue(AbstractDelegate.isTestDelegateRequired(null));
        assertTrue(AbstractDelegate.isTestDelegateRequired(""));
        assertTrue(AbstractDelegate.isTestDelegateRequired(" "));
        assertTrue(AbstractDelegate.isTestDelegateRequired(Delegate.TEST_URI));
        assertFalse(AbstractDelegate.isTestDelegateRequired("http://a.differend.url/endpoint"));
    }
}


/* 
$Log: AbstractDelegateTest.java,v $
Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/09 11:19:28  nw
end of this branch - have a set of oo-based delegates.
not ready for use until innards are updated too.
 
*/