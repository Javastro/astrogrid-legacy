/*$Id: AbstractJobTest.java,v 1.1 2003/08/22 10:37:48 nw Exp $
 * Created on 21-Aug-2003
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

import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobTestSpec;
/** tests the abstract implementation of a job.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class AbstractJobTest extends JobTestSpec {

    /**
     * Constructor for AbstractJobTest.
     * @param arg0
     */
    public AbstractJobTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AbstractJobTest.class);
    }
    public static Test suite() {
        return new TestSuite(AbstractJobTest.class);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.job.JobTestSpec#createJob()
     */
    protected Job createJob() {
        return new AbstractJob() {

            public void informJobMonitor() {
               // does nothing.
            }
        };
    }



}


/* 
$Log: AbstractJobTest.java,v $
Revision 1.1  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep
 
*/