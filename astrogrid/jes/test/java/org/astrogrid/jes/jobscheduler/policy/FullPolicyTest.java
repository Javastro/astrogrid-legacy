/*$Id: FullPolicyTest.java,v 1.1 2004/04/21 16:40:21 nw Exp $
 * Created on 21-Apr-2004
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
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class FullPolicyTest extends AbstractTestForPolicy {
    /** Construct a new FullPolicyTest
     * @param arg0
     */
    public FullPolicyTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.AbstractTestForPolicy#createPolicy()
     */
    protected AbstractPolicy createPolicy() {
        return new FullPolicy();
    }
}


/* 
$Log: FullPolicyTest.java,v $
Revision 1.1  2004/04/21 16:40:21  nw
updated tests to exercise new policy implementations
 
*/