/*$Id: DynamicWorkflowTest.java,v 1.1 2004/08/27 13:51:41 nw Exp $
 * Created on 27-Aug-2004
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
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Test the ability of scripts to dynamically add / alter parameters to a tool call. 
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Aug-2004
 *
 */
public class DynamicWorkflowTest extends AbstractTestForWorkflow {

    /** Construct a new DynamicWorkflowTest
     * @param applications
     * @param arg0
     */
    public DynamicWorkflowTest(String arg0) {
        super(new String[]{SUM}, arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName(this.getClass().getName());
        Script sc = new Script();
        sc.setBody(
                "sumStep = jes.getSteps().find{it.getName() == 'sumStep'}; // find next step" +
                "inputs = sumStep.getTool().getInput(); // get set of inputs. " +
                "inputs.getParameter(0).setValue('11'); // zap value of existing parameter." +
                "p = jes.newParameter();" +
                "p.setName('parameter-1');" +
                "p.setIndirect(false);" +
                "p.setValue('4');" +
                "inputs.addParameter(p);//add new parameter"                
                );
        
        wf.getSequence().addActivity(sc);
        
        ApplicationDescription descr = reg.getDescriptionFor(SUM);
        Tool sumTool = descr.createToolFromDefaultInterface();
        // setup some parameters only.
        // setup parameters
        ParameterValue pval = (ParameterValue)sumTool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("9"); 
        pval.setIndirect(false);
        // will set parameter-1 by script.
        pval = (ParameterValue)sumTool.findXPathValue("output/parameter[name='result']");
        pval.setIndirect(false);       
        
        Step sum = new Step();
        sum.setResultVar("sumResults");
        sum.setDescription("call sum tool");
        sum.setName("sumStep");
        sum.setTool(sumTool);
        wf.getSequence().addActivity(sum);
        
        Script checkScript = new Script();
        checkScript.setBody("print (sumResults != null && sumResults.result != null && sumResults.result == '15')");
        wf.getSequence().addActivity(checkScript);        
    }

    public void checkExecutionResults(Workflow result) throws Exception {
        
        super.checkExecutionResults(result); // checks workflow completed, at least.
        Step step = (Step)result.getSequence().getActivity(1);
        
        // have a look at the execution of the step.
        assertStepCompleted(step);

        ResultListType rList = getResultOfStep(step); 
        assertEquals(1,rList.getResultCount());
        assertEquals("15",rList.getResult(0).getValue());  // ok, so cea returned the correct result - so variables are being passed in.


        Script sc = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(sc,"true");                    
    }

}


/* 
$Log: DynamicWorkflowTest.java,v $
Revision 1.1  2004/08/27 13:51:41  nw
added test that checks operation of dynamically-modified workflows.
 
*/