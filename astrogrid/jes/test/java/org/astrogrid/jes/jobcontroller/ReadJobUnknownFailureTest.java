/*$Id: ReadJobUnknownFailureTest.java,v 1.2 2004/12/03 14:47:40 jdt Exp $
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
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Test behaviour when we're lookinf for a urn that doesn't exist.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class ReadJobUnknownFailureTest extends AbstractTestForJobController {
    /** Construct a new ReadJobUnknownFailureTest
     * @param s
     */
    public ReadJobUnknownFailureTest(String s) {
        super(s);
    }

        protected void performTest(JobURN urn) throws Exception {
            try {
                String result = jc.readJob(new org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN("jes:nonexistent:job")).getValue();
                fail("expected to throw");
            } catch (JesFault e) {
            }
        }
}


/* 
$Log: ReadJobUnknownFailureTest.java,v $
Revision 1.2  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.1.128.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/