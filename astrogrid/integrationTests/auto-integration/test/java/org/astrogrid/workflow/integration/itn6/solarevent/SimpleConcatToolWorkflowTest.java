/*$Id: SimpleConcatToolWorkflowTest.java,v 1.1 2004/08/17 13:34:16 nw Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/** simple test to test operation of the concat tool.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class SimpleConcatToolWorkflowTest extends AbstractTestForWorkflow implements SolarEventKeys{

    /** Construct a new SimpleElizabethToolWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleConcatToolWorkflowTest(String arg0) {
        super(new String[]{CONCAT_APP}, arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws WorkflowInterfaceException {
        wf.setName(this.getClass().getName());
        ApplicationDescription desc = reg.getDescriptionFor(CONCAT_APP);
        Tool concatTool = desc.createToolFromDefaultInterface();
        // populate tool
        ParameterValue src = (ParameterValue)concatTool.findXPathValue("input/parameter[name='src']");
        src.setIndirect(false);
        src.setValue("foo");
        
        // 'src' is a repeated parameter - so take some copies, and add in..
        ParameterValue src1 = copyParameter(src);
        src1.setValue("bar");
        concatTool.getInput().addParameter(src1);
        
        ParameterValue src2 = copyParameter(src);
        src2.setValue("choo");
        concatTool.getInput().addParameter(src2);
        
        ParameterValue result = (ParameterValue)concatTool.findXPathValue("output/paramter[name='result']");
        assertNotNull(result);
        result.setIndirect(false); // want to get results back.
        // add to the workflow
        Step s = new Step();
        s.setDescription("Concat tool");
        s.setTool(concatTool);
        wf.getSequence().addActivity(s);
    }
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
        
        Step s= (Step)result.getSequence().getActivity(0);
        assertStepCompleted(s);
        ResultListType r = getResultOfStep(s);
        softAssertEquals("only expected a single result",1,r.getResultCount());
        softAssertEquals("result not as expected","foobarchoo",r.getResult(0).getValue());
        
    }    



}


/* 
$Log: SimpleConcatToolWorkflowTest.java,v $
Revision 1.1  2004/08/17 13:34:16  nw
*** empty log message ***

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/