/*$Id: SimpleJavaWorkflowEndToEndTest.java,v 1.2 2004/07/02 09:12:27 nw Exp $
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

    public void checkExecutionResults(Workflow wf)  {
    super.checkExecutionResults(wf);
    // get the result, check its what we expect.
    String value = (String)wf.findXPathValue("sequence/activity/tool/output/parameter/value");
    softAssertNotNull("result is null",value);
    softAssertTrue("result is empty",value.trim().length() > 0);
    softAssertEquals("result ins't as expected",42,Integer.parseInt(value));
    }
}


/* 
$Log: SimpleJavaWorkflowEndToEndTest.java,v $
Revision 1.2  2004/07/02 09:12:27  nw
better checking of result value

Revision 1.1  2004/07/01 11:47:39  nw
cea refactor
 
*/;