/*$Id: FlowPolicyTest.java,v 1.1 2004/03/18 10:56:47 nw Exp $
 * Created on 18-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2004
 *
 */
public class FlowPolicyTest extends AbstractTestForPolicy {
    /**
     * Constructor for FlowPolicyTest.
     * @param arg0
     */
    public FlowPolicyTest(String arg0) {
        super(arg0);
    }
    /*
     * @see AbstractTestForPolicy#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.AbstractTestForPolicy#createPolicy()
     */
    protected AbstractPolicy createPolicy() {
        return new FlowPolicy();
    }
}


/* 
$Log: FlowPolicyTest.java,v $
Revision 1.1  2004/03/18 10:56:47  nw
added tests for other policy implementations
 
*/