/*$Id: JobStepTestSpec.java,v 1.2 2003/08/22 15:50:31 nw Exp $
 * Created on 22-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.job;

import junit.framework.TestCase;

import org.astrogrid.datacenter.query.Query;

/** test specification for the job step interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
 *
 */
public abstract class JobStepTestSpec extends TestCase {

    /**
     * Constructor for JobStepTestSpec.
     * @param arg0
     */
    public JobStepTestSpec(String arg0) {
        super(arg0);
    }
    /** implementors to define this to return instance of the job step implementatio under test */
    protected abstract JobStep createJobStep();
    /** implementors to define this */
    protected abstract Query createQuery();
    protected JobStep js = createJobStep();
    
    public void testCheckNull() {
        assertNotNull(js);
    }
    
    public void testName() {
        assertNotNull(js.getName());
        js.setName("foo");
        String n1 = js.getName();
        assertNotNull(n1);
        assertEquals(n1,"foo");
    }
    
    public void testQuery() {
        Query q1 = createQuery();
        assertNotNull(q1);
        js.setQuery(q1);
        Query q2 = js.getQuery();
        assertNotNull(q2);
        assertEquals(q1,q2);
    }
    
    public void testStepNumber() {
        assertNotNull(js.getStepNumber());
        js.setStepNumber("foo");
        String n1 = js.getStepNumber();
        assertNotNull(n1);
        assertEquals(n1,"foo");
    }
    

}


/* 
$Log: JobStepTestSpec.java,v $
Revision 1.2  2003/08/22 15:50:31  nw
tidied imports

Revision 1.1  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep
 
*/