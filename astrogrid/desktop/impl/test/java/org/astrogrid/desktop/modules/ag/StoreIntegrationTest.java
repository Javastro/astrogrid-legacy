/*$Id: StoreIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
 * Created on 24-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ACRTestSetup;

import jdbm.RecordManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StoreIntegrationTest extends TestCase {

    /*
     * Test method for 'org.astrogrid.desktop.modules.ag.StoreImpl.getManager()'
     */
    public void testGetManager() {
        RecordManager m = store.getManager();
        assertNotNull(m);
    }
    protected ACR getACR() throws Exception {
        return ACRTestSetup.acrFactory.getACR();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(StoreIntegrationTest.class),true); 
    }    
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg =getACR();
         store = (StoreInternal)reg.getService(StoreInternal.class);        
    }
    protected StoreInternal store;
}


/* 
$Log: StoreIntegrationTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.2.1  2006/03/28 13:47:35  nw
first webstartable version.
 
*/