/*$Id: ListJobsSuccessTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 17-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;
import org.astrogrid.jes.types.v1.ListCriteria;
import org.astrogrid.jes.types.v1.SubmissionResponse;
import org.astrogrid.jes.types.v1.WorkflowList;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.StringReader;
/** Test the listJobs method of the job controller
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class ListJobsSuccessTest extends AbstractTestForJobController {
    public final static String COMMUNITY = "jodrell";
    public final static String USERID = "nww";
    protected ListCriteria criteria;
    /**
     * Constructor for ListJobsTest.
     * @param arg0
     */
    public ListJobsSuccessTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.criteria = new ListCriteria();
        criteria.setCommunity(COMMUNITY);
        criteria.setUserId(USERID);
    }
    public void testCriteria() {
        assertNotNull(criteria);
        assertNotNull(criteria.getCommunity());
        assertNotNull(criteria.getUserId());
    }    
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(SubmissionResponse subResp) throws Exception {
        assertTrue(subResp.isSubmissionSuccessful());
        WorkflowList wl = jc.readJobList(criteria);
        assertNotNull(wl);
        assertNull(wl.getMessage()); //i.e. no errors
        assertEquals(wl.getUserId(), USERID);
        assertEquals(wl.getCommunity(), COMMUNITY);
        String[] rawArr = wl.getWorkflow();
        assertNotNull(rawArr);
        // we're creating a new job controller each time, so should expect either 1 or 0 elements in the result list.
        assertTrue(rawArr.length >= 0 && rawArr.length <= 1);
        for (int i = 0; i < rawArr.length; i++) {
            assertNotNull(rawArr[i]);
            Workflow wf = Workflow.unmarshalWorkflow(new StringReader(rawArr[i]));
            assertNotNull(wf);
            assertEquals(USERID, wf.getCommunity().getCredentials().getAccount());
            assertEquals(COMMUNITY, wf.getCommunity().getCredentials().getGroup());
        }
    }

}
/* 
$Log: ListJobsSuccessTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/