/*$Id: BasicComponentManagerTest.java,v 1.3 2004/03/15 00:06:57 nw Exp $
 * Created on 27-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

import org.picocontainer.PicoException;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Feb-2004
 *
 */
public class BasicComponentManagerTest extends TestCase {
    /**
     * Constructor for ComponentManagerTest.
     * @param arg0
     */
    public BasicComponentManagerTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        cm = new BasicComponentManager();
    }
    
    protected ComponentManager cm;

    /** verify container is consistent */
    public void testVerify() {
        try {
            cm.getContainer().verify();
        } catch (PicoException e) {
            fail(e.getMessage());
        }
    }

    public void testGetFacade() {
        assertNotNull(cm.getFacade());
    }
    
    public void testGetContainer() {
        assertNotNull(cm.getContainer());
    }
    
    public void testGetController() {
        assertNotNull(cm.getController());
    }
    
    public void testGetMonitor() {
        assertNotNull(cm.getMonitor());
    }
    
    public void testGetNotifier() {
        assertNotNull(cm.getNotifier());
    }

    
    public void testInformation() {
        String result = cm.information();
        assertNotNull(result);
        System.out.println(result);
    }
    
    public void testGetSuite() {
        Test t = cm.getSuite();
        assertNotNull(t);
        // want to run this, check that it does succeed.
    }
}

/* 
$Log: BasicComponentManagerTest.java,v $
Revision 1.3  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.2  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/