/*$Id: AbstractJobStepTest.java,v 1.2 2003/08/22 15:50:31 nw Exp $
 * Created on 22-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.impl.abstr;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.datacenter.job.JobStep;
import org.astrogrid.datacenter.job.JobStepTestSpec;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;

/** tests the abstract implementation of a job step
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Aug-2003
 *
 */
public class AbstractJobStepTest extends JobStepTestSpec {

    /**
     * Constructor for AbstractJobStepTest.
     * @param arg0
     */
    public AbstractJobStepTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AbstractJobStepTest.class);
    }
    public static Test suite() {
        return new TestSuite(AbstractJobStepTest.class);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobStepTestSpec#createJobStep()
     */
    protected JobStep createJobStep() {
        return new AbstractJobStep() {;};
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobStepTestSpec#createQuery()
     */
    protected Query createQuery() {
        try {
        return new Query(null,null);
        } catch (QueryException e) {
            fail("Could not create query " + e.getMessage());
        }
        return null;
    }
    

}


/* 
$Log: AbstractJobStepTest.java,v $
Revision 1.2  2003/08/22 15:50:31  nw
tidied imports

Revision 1.1  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep
 
*/