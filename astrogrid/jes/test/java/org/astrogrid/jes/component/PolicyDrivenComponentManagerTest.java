/*$Id: PolicyDrivenComponentManagerTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.jes.component.production.PolicyDrivenComponentManager;

import org.picocontainer.PicoException;

import junit.framework.TestCase;

/** Simplpe test to verify that policy driven component manager registers a consistent set of components.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class PolicyDrivenComponentManagerTest extends TestCase {
    /**
     * Constructor for ProductionComponentManagerTest.
     * @param arg0
     */
    public PolicyDrivenComponentManagerTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        cm = new PolicyDrivenComponentManager();

    }
    protected JesComponentManager cm;
    public void testVerify() {
        try {
            cm.getContainer().verify();
        } catch (PicoException e) {
            fail(e.getMessage());
        }
    }
    public void testDescription() {
        cm.start();        
        String info = cm.information();
        assertNotNull(info);
        System.out.println(info);
    }
    
}


/* 
$Log: PolicyDrivenComponentManagerTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer
 
*/