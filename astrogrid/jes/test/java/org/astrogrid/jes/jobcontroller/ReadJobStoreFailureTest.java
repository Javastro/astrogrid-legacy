/*$Id: ReadJobStoreFailureTest.java,v 1.1 2004/03/09 14:23:54 nw Exp $
 * Created on 08-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.MockJobFactoryImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Test behaviour of read jobs whern the store fails
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class ReadJobStoreFailureTest extends AbstractTestForJobController {
    /** Construct a new ReadJobStoreFailureTest
     * @param s
     */
    public ReadJobStoreFailureTest(String s) {
        super(s);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        try {
            String result = jc.readJob(JesUtil.castor2axis(urn)).getValue();
            fail("expected to throw");
        } catch (JesFault e) {
        }        
    }
    
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#createJobFactory()
     */
    protected AbstractJobFactoryImpl createJobFactory() {
        return new MockJobFactoryImpl();
        
    }
}


/* 
$Log: ReadJobStoreFailureTest.java,v $
Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/