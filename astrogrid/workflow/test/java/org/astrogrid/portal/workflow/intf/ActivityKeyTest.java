/*$Id: ActivityKeyTest.java,v 1.2 2004/03/11 13:53:51 nw Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;


import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class ActivityKeyTest extends TestCase {
    /**
     * Constructor for ActivityKeyTest.
     * @param arg0
     */
    public ActivityKeyTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("workflow.xml"));
        assertNotNull(reader);
        wf = Workflow.unmarshalWorkflow(reader);
        assertNotNull(wf);
        
    }
    protected Workflow wf;
    
    public void testCreateAllKeys() {
        Iterator i = wf.findXPathIterator("//activity");
        assertNotNull(i);
        assertTrue(i.hasNext());
        while(i.hasNext()) {
            BaseBean current = (BaseBean)i.next();
            ActivityKey key = ActivityKey.createKey(wf,current);
            System.out.println(key);
            assertNotNull(key);
            BaseBean other = key.extractFrom(wf);
            assertEquals(current,other);
        }
    }
    
    public void testUnknownKey() {
        ActivityKey key= ActivityKey.createKey(wf,wf.getSequence().getActivity(0));
        assertNotNull(key);
        // zap the activities
        wf.getSequence().clearActivity();
        BaseBean b = key.extractFrom(wf);
        assertNull(b);
        
    }
    
    public void testUnknownBean() {
        BaseBean s = new Step();
        try {
            ActivityKey key = ActivityKey.createKey(wf,s);
            fail("expected to barf");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
    }
        
}


/* 
$Log: ActivityKeyTest.java,v $
Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:51  nw
tests for impls
 
*/