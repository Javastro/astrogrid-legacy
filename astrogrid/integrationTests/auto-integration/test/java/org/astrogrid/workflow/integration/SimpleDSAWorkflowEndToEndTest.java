/*$Id: SimpleDSAWorkflowEndToEndTest.java,v 1.3 2004/07/01 11:47:39 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.applications.integration.datacenter.DataCenterProviderServerInfo;
import org.astrogrid.integration.*;
import org.astrogrid.io.Piper;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

/** end-to-end test of workfow - usecase of creating and submitting a workflow.
 * <p>
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class SimpleDSAWorkflowEndToEndTest extends AbstractTestForSimpleWorkflow {

    /** Construct a new SimpleDSAWorkflowEndToEndTest
     * @param info
     * @param arg0
     */
    public SimpleDSAWorkflowEndToEndTest( String arg0) {
        super(new DataCenterProviderServerInfo(), arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForSimpleWorkflow#configureToolParameters(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected void configureToolParameters(Tool tool) {
        info.populateDirectTool(tool);
    }
    /** @todo add more result checking */
    public void checkExecutionResults(Workflow wf)  {
    super.checkExecutionResults(wf);
    // get the result, check its what we expect.
    String value = (String)wf.findXPathValue("sequence/activity/tool/output/parameter/value");
    softAssertNotNull(value);
    
    }
 

}


/* 
$Log: SimpleDSAWorkflowEndToEndTest.java,v $
Revision 1.3  2004/07/01 11:47:39  nw
cea refactor

Revision 1.2  2004/05/26 14:48:46  nw
changed ivorn to correct format.

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