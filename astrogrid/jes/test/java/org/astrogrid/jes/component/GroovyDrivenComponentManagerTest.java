/*$Id: GroovyDrivenComponentManagerTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 28-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.astrogrid.jes.component.production.GroovyComponentManager;

import org.picocontainer.PicoException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2004
 *
 */
public class GroovyDrivenComponentManagerTest extends
        TestCase {

    /** Construct a new GroovyDrivenComponentManagerTest
     * @param arg0
     */
    public GroovyDrivenComponentManagerTest(String arg0) {
        super(arg0);
    }

    protected void setUp() {
        cm = new GroovyComponentManager();
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
$Log: GroovyDrivenComponentManagerTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation
 
*/