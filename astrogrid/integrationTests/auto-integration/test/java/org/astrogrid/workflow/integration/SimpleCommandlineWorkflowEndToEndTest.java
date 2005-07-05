/*$Id: SimpleCommandlineWorkflowEndToEndTest.java,v 1.20 2005/07/05 10:54:36 jdt Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
/**
 * Test for a workflow that has a single step that calls the TESTAPP application
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 * @author Paul Harrison pah@jb.man.ac.uk 23-Apr-2004
 *
 */
public class SimpleCommandlineWorkflowEndToEndTest extends AbstractTestForSimpleWorkflow {

    /**
       * Constructor for WorkflowManagerIntegrationTest.
       * @param arg0
       */
    public SimpleCommandlineWorkflowEndToEndTest(String arg0) {
        super(new CommandLineProviderServerInfo()  ,arg0);
    }


    /**
    * Fine tune the parameter values... 
    */
    protected void configureToolParameters(Tool tool) {
        info.populateDirectTool(tool);
    }
    
    public void checkExecutionResults(Workflow wf)  throws Exception {
        super.checkExecutionResults(wf);
        // get the result, check its what we expect.
        Step s = (Step)wf.getSequence().getActivity(0);
        assertStepCompleted(s);
        ResultListType res = getResultOfStep(s);
        softAssertEquals("wrong number of results",3,res.getResultCount());
        String value =((ParameterValue)res.findXPathValue("result[name='P3']")).getValue();
        softAssertTrue("result doesn't contain expected value",value.indexOf(CommandLineProviderServerInfo.TEST_CONTENTS) != -1);
    }
}
/* 
$Log: SimpleCommandlineWorkflowEndToEndTest.java,v $
Revision 1.20  2005/07/05 10:54:36  jdt
int_pah_559b

Revision 1.19.42.1  2005/06/09 21:18:23  pah
now returns 3 results

Revision 1.19  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.18.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.18  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.15.26.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.15  2004/09/22 00:42:26  nw
fixed paul's bug.

Revision 1.14  2004/09/02 17:12:03  pah
update for new test application which does more

Revision 1.13  2004/08/17 15:11:50  nw
updated some tests

Revision 1.12  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow

Revision 1.11  2004/07/01 11:47:39  nw
cea refactor

Revision 1.10  2004/05/19 14:09:35  nw
calmed this test down a bit - stress tested a bit.

Revision 1.9  2004/05/17 17:06:03  nw
got this one working.

Revision 1.8  2004/05/11 12:27:26  pah
print the last message to stdout on completion...

Revision 1.7  2004/04/26 12:17:17  nw
improved reporting of urns

Revision 1.6  2004/04/25 21:26:47  pah
made the temp file names windows friendly..

Revision 1.5  2004/04/25 21:10:11  pah
change timings slightly to give the app time to finish

Revision 1.4  2004/04/25 20:54:22  pah
make use the local file store rather than myspace

Revision 1.3  2004/04/23 22:40:54  pah
more tweaks

Revision 1.2  2004/04/23 16:12:49  pah
added the myspace testapp test

Revision 1.1  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.9  2004/04/22 08:58:38  nw
improved

Revision 1.8  2004/04/21 13:43:43  nw
tidied imports

Revision 1.7  2004/04/20 14:48:21  nw
simplified myspace storepoint.

Revision 1.6  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.5  2004/04/15 23:11:20  nw
tweaks

Revision 1.4  2004/04/14 16:42:37  nw
fixed tests to break more sensibly

Revision 1.3  2004/04/14 15:28:47  nw
updated tests to fit with new WorkspaceStore interface

Revision 1.2  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.1  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.4  2004/04/06 15:35:28  nw
altered order of things happining

Revision 1.3  2004/04/06 12:08:30  nw
fixes

Revision 1.2  2004/03/17 01:14:37  nw
removed possible infinite loop

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/