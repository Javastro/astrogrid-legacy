/*$Id: ActivityStatusStoreTest.java,v 1.3 2004/08/03 14:27:38 nw Exp $
 * Created on 27-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class ActivityStatusStoreTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        store = new ActivityStatusStore();
    }
    
    protected ActivityStatusStore store;


    
    public void testStandardMethods() {
        store.toString();
        assertEquals(store,store);
        assertEquals(store.hashCode(),store.hashCode());
    }
    /*@todo renable later.
    public void testBaseMergeVars() {
        Vars vars = new Vars();
        vars.set("foo","fred");
        vars.set("bar",new Integer(1));    
        store.setEnv("foo",vars);
        List list = new ArrayList();
        list.add("foo");
        Vars vars1 = store.mergeVars(list);
        assertNotSame(vars,vars1);
        assertEquals(vars,vars1);
    }
    
    public void testMergeVars() {
        Vars vars = new Vars();
        vars.set("foo","fred");
        vars.set("bar",new Integer(1));    
        store.setEnv("foo",vars);
        List list = new ArrayList();
        list.add("foo");
        
        Vars vars1  = new Vars();
        vars.set("foo","barney");
        vars.set("wibble","42");
        store.setEnv("bar",vars1);
        list.add("bar");
        Vars vars2 = store.mergeVars(list);
        //assertEquals(3,vars2.e.size());
        assertEquals("barney",vars2.get("foo")); // i.e. later bindings override earlier ones.
        assertEquals("42",vars2.get("wibble"));
        assertEquals(new Integer(1),vars2.get("bar"));
    }
    */
        

}


/* 
$Log: ActivityStatusStoreTest.java,v $
Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.
 
*/