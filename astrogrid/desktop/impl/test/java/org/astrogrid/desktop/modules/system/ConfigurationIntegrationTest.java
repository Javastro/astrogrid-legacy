/*$Id: ConfigurationIntegrationTest.java,v 1.5 2009/03/26 18:01:21 nw Exp $
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

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test the configuration component in a running acr.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Mar-2005
 *
 */
public class ConfigurationIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        assertNotNull(reg);
        conf = (Configuration)reg.getService(Configuration.class);
        assertNotNull(conf);
    }
    @Override
    protected void tearDown() throws Exception {
    	super.tearDown();
    	conf = null;
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
        return new ARTestSetup(new TestSuite(ConfigurationIntegrationTest.class));
    }

}


/* 
$Log: ConfigurationIntegrationTest.java,v $
Revision 1.5  2009/03/26 18:01:21  nw
added override annotations

Revision 1.4  2007/01/29 10:42:48  nw
tidied.

Revision 1.3  2007/01/23 11:53:36  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.3  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/