/*$Id: ConfigurationTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.framework.ACRTestSetup;
import org.astrogrid.desktop.framework.MutableACR;

import java.util.Map;
import java.util.prefs.BackingStoreException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** test the configuration component in a running acr.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 *
 */
public class ConfigurationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        assertNotNull(reg);
        conf = (Configuration)reg.getService(Configuration.class);
        assertNotNull(conf);
    }
    
    /**
     * @return
     */
    protected ACR getACR() throws Exception{
        return (ACR)ACRTestSetup.pico.getACR();
    }

    protected Configuration conf;
    
    public void testGetSetRemove() {
        conf.setKey("test","x");
        assertEquals("x",conf.getKey("test"));
        conf.removeKey("test");
        assertNull(conf.getKey("test"));
    }
    
    public void testList() throws Exception {
        Map m = conf.list();
        assertNotNull(m);
    }
    
    public void testListKeys() throws Exception {
        String[] keys = conf.listKeys();
        assertNotNull(keys);
        assertTrue(keys.length > 0);
    }
    
    
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ConfigurationTest.class));
    }

}


/* 
$Log: ConfigurationTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/