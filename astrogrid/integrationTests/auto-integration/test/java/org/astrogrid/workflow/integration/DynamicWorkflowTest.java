/*$Id: DynamicWorkflowTest.java,v 1.7 2005/03/14 22:03:53 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Test the ability of scripts to dynamically add / alter parameters to a tool call. 
 * <P>
 * calls the SUM application
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
                "sumStep = jes.getSteps().find{it.getName() == 'sumStep'}; // find next step\n" +
                "inputs = sumStep.getTool().getInput(); // get set of inputs.\n " +
                "inputs.getParameter(0).setValue('11'); // zap value of existing parameter.\n" +
                "p = jes.newParameter();\n" +
                "p.setName('parameter-1');\n" +
                "p.setIndirect(false);\n" +
                "p.setValue('4');\n" +
                "inputs.addParameter(p);//add new parameter\n"                
                );
        
        wf.getSequence().addActivity(sc);
        
        ApplicationDescription descr = reg.getDescriptionFor(SUM);
        Tool sumTool = descr.createToolFromDefaultInterface();
        // setup some parameters only.
        // setup parameters
        ParameterValue pval = (ParameterValue)sumTool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("9"); 
        pval.setIndirect(false);
        // will set parameter-1 by script. - remove it from tool template.
        pval = (ParameterValue)sumTool.findXPathValue("input/parameter[name='parameter-1']");
        sumTool.getInput().removeParameter(pval);
        
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
Revision 1.7  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.6.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.6  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.3.52.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.3  2004/08/28 11:14:50  nw
tweaks

Revision 1.2  2004/08/28 09:58:07  nw
forgot to add \n's to script.

Revision 1.1  2004/08/27 13:51:41  nw
added test that checks operation of dynamically-modified workflows.
 
*/