/*$Id: SimpleJavaWorkflowEndToEndTest.java,v 1.5 2004/08/17 15:11:50 nw Exp $
 * Created on 23-Jun-2004
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
import org.astrogrid.applications.integration.JavaProviderServerInfo;
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/** creates a workflow with a single step (direct parameters call to java application). submits to jes, inspects results.
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Jun-2004
 *
 */
public class SimpleJavaWorkflowEndToEndTest extends AbstractTestForSimpleWorkflow {

    /** Construct a new SimpleJavaWorkflowEndToEndTest
     * @param info
     * @param arg0
     */
    public SimpleJavaWorkflowEndToEndTest( String arg0) {
        super(new JavaProviderServerInfo() ,arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForSimpleWorkflow#configureToolParameters(org.astrogrid.workflow.beans.v1.Tool)
     */
    protected void configureToolParameters(Tool tool) {
        info.populateDirectTool(tool);
    }

    public void checkExecutionResults(Workflow wf) throws Exception {
    super.checkExecutionResults(wf);
    Step s = (Step)wf.getSequence().getActivity(0);
    assertStepCompleted(s);
    ResultListType res = getResultOfStep(s);
    softAssertEquals("expected 1 result",1,res.getResultCount());
    // get the result, check its what we expect.
    String value = res.getResult(0).getValue();
    softAssertNotNull("result is null",value);
    softAssertTrue("result is empty",value.trim().length() > 0);
    try {
        softAssertEquals("result ins't as expected",42,Integer.parseInt(value));
    } catch (NumberFormatException e) {
        softFail("Can't parse number in result" + e.getMessage());
    }
    }
}


/* 
$Log: SimpleJavaWorkflowEndToEndTest.java,v $
Revision 1.5  2004/08/17 15:11:50  nw
updated some tests

Revision 1.4  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow

Revision 1.3  2004/07/05 18:32:34  nw
fixed tests

Revision 1.2  2004/07/02 09:12:27  nw
better checking of result value

Revision 1.1  2004/07/01 11:47:39  nw
cea refactor
 
*/;