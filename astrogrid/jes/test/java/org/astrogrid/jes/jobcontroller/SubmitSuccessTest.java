/* $Id: SubmitSuccessTest.java,v 1.5 2004/03/09 14:23:54 nw Exp $
 * Created on 29-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
 
/** Test class for the job controller. should succeed on each test.
* try submitting a series of workflow documents, and watch what happens.
 */
public class SubmitSuccessTest extends AbstractTestForJobController {
  /**
   * Constructor for JobController.
   * @param arg0 name of test
   */
  public SubmitSuccessTest(final String arg0) {
    super(arg0);
  }

    protected void setUp() throws Exception{
        super.setUp();
    }
    protected void performTest(JobURN urn) throws Exception {
    
    assertNotNull("Result from submitJob should not be null", urn);
    // now check what job store and nudger have seen.
    assertTrue(((MockSchedulerNotifier)nudger).getCallCount() > 0);
    //
    Workflow storedJob = fac.findJob(urn);
    assertNotNull(storedJob);
}
}

/*
*$Log: SubmitSuccessTest.java,v $
*Revision 1.5  2004/03/09 14:23:54  nw
*tests that exercise the job contorller service implememntiton
*
*Revision 1.4  2004/03/04 01:57:35  nw
*major refactor.
*upgraded to latest workflow object model.
*removed internal facade
*replaced community snippet with objects
*
*Revision 1.3  2004/03/03 01:13:42  nw
*updated jes to work with regenerated workflow object model
*
*Revision 1.2  2004/02/27 00:46:03  nw
*merged branch nww-itn05-bz#91
*
*Revision 1.1.2.2  2004/02/19 13:41:25  nw
*added tests for everything :)
*
*Revision 1.1.2.1  2004/02/17 16:51:02  nw
*thorough unit testing for job controller
*
*Revision 1.5.2.2  2004/02/17 12:57:11  nw
*improved documentation
*
*Revision 1.5.2.1  2004/02/12 12:57:00  nw
*updated to fit with IoC
*
*Revision 1.5  2004/02/09 11:41:43  nw
*merged in branch nww-it05-bz#85
*
*Revision 1.4.6.1  2004/02/06 13:48:23  nw
*added test for jobMonitorDelegate
*cleaned up imports
*replaced use of log4j with commons.logging
*
*Revision 1.4  2003/11/14 19:11:54  jdt
*Minor changes to satisfy coding stds.
*
*Revision 1.3  2003/11/14 19:07:48  jdt
*Now cleanly runs through the submit process, all I need to do it check that it's working.
*
*Revision 1.2  2003/11/10 10:48:26  anoncvs
*Refactored out some of the nonsense into a separate class.
*
*Revision 1.1  2003/10/29 16:42:52  jdt
*Initial commit of some JobController test files, and mods to the config file
*so that they get picked up.
*
*/