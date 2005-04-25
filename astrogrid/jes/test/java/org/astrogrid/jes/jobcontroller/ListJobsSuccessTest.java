/*$Id: ListJobsSuccessTest.java,v 1.7 2005/04/25 12:13:54 clq2 Exp $
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
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.beans.v1.axis.executionrecord.WorkflowSummaryType;
import org.astrogrid.jes.beans.v1.axis.executionrecord._workflowSummaryList;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
/** Test the listJobs method of the job controller
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class ListJobsSuccessTest extends AbstractTestForJobController {
    public final static String COMMUNITY = "jodrell";
    public final static String USERID = "nww74";
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
        this.acc = new _Account();
        acc.setCommunity(new Identifier(ListJobsSuccessTest.COMMUNITY));
        acc.setName(new Identifier(ListJobsSuccessTest.USERID));
    }
    protected _Account acc;
    

    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {
        _workflowSummaryList  l = jc.readJobList(acc);
        WorkflowSummaryType[] rawArr = l.getItem();

        assertNotNull(rawArr);
        // we're creating a new job controller each time, so should expect either 1 or 0 elements in the result list.

        
        assertEquals(1,rawArr.length);
        for (int i = 0; i < rawArr.length; i++) {
            assertNotNull(rawArr[i]);
            assertNotNull(rawArr[i].getJobId());
            assertNotNull(rawArr[i].getWorkflowName());
            assertNotNull(rawArr[i].getDescription());
            assertNotNull(rawArr[i].getMessage());        
            // now do same after converting to castor.
            org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType castor = Axis2Castor.convert(rawArr[i]);
            assertNotNull(castor);

        }
    }

}
/* 
$Log: ListJobsSuccessTest.java,v $
Revision 1.7  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.6.42.1  2005/04/12 17:08:54  nw
fix for listJobs bug

Revision 1.6  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.5.128.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.5  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.4  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/