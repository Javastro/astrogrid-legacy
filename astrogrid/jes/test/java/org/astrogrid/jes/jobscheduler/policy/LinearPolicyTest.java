/*$Id: LinearPolicyTest.java,v 1.4 2004/03/18 10:56:28 nw Exp $
 * Created on 04-Mar-2004
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
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class LinearPolicyTest extends AbstractTestForPolicy {
    /** Construct a new LinearPolicyTest
     * 
     */
    public LinearPolicyTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
       super.setUp();
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.policy.AbstractTestForPolicy#createPolicy()
     */
    protected AbstractPolicy createPolicy() {
        return new LinearPolicy();
    }
   
    
}


/* 
$Log: LinearPolicyTest.java,v $
Revision 1.4  2004/03/18 10:56:28  nw
factored out an abstract test class

Revision 1.3  2004/03/18 01:30:34  nw
tidied

Revision 1.2  2004/03/10 14:37:35  nw
adjusted tests to handle an empty workflow

Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/