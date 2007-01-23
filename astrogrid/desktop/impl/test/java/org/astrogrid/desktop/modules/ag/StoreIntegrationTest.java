/*$Id: StoreIntegrationTest.java,v 1.3 2007/01/23 11:53:38 nw Exp $
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

import jdbm.RecordManager;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

public class StoreIntegrationTest extends InARTestCase {

    /*
     * Test method for 'org.astrogrid.desktop.modules.ag.StoreImpl.getManager()'
     */
    public void testGetManager() {
        RecordManager m = store.getManager();
        assertNotNull(m);
    }
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(StoreIntegrationTest.class),true); 
    }    
    protected void setUp() throws Exception {
        super.setUp();
         store = (StoreInternal)getHivemindRegistry().getService(StoreInternal.class);        
    }
    
    protected void tearDown() throws Exception {
    	super.tearDown();
    	store = null;
    }
    
    protected StoreInternal store;
}


/* 
$Log: StoreIntegrationTest.java,v $
Revision 1.3  2007/01/23 11:53:38  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.2  2007/01/09 16:12:19  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.2.1  2006/03/28 13:47:35  nw
first webstartable version.
 
*/