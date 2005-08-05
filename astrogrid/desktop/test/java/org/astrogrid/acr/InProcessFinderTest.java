/*$Id: InProcessFinderTest.java,v 1.1 2005/08/05 11:46:56 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.Configuration;

import java.util.Map;
import java.util.prefs.BackingStoreException;

import junit.framework.TestCase;

/** test finders ability to start off an acr instance.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class InProcessFinderTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        finder = new Finder();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        Shutdown sh =(Shutdown) finder.find().getService(Shutdown.class);
        sh.halt();
    }

    Finder finder;
    
    public void testInProcessFinder() throws ACRException, BackingStoreException {
        ACR acr = finder.find();
        assertNotNull(acr);
        Configuration conf = (Configuration)acr.getService(Configuration.class);
        assertNotNull(conf);
        Map m = conf.list();
        assertNotNull(m);
        assertTrue(m.size() > 0);
        
        ACR acr1 = finder.find();
        assertSame(acr,acr1);
    }

}


/* 
$Log: InProcessFinderTest.java,v $
Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/