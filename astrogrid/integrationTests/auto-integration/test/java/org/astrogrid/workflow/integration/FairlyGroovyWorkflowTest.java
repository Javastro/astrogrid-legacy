/*$Id: FairlyGroovyWorkflowTest.java,v 1.7 2004/11/24 19:49:22 clq2 Exp $
 * Created on 04-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;


import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.StringReader;

/** integration test that executes a workflow containing some of the new features of the groovy-jes server
 *  - declaration of variables, script blocks, passing variabls into cea calls, accessing results of a cea call, propagating values between cea calls.
 * <p>
 * uses SUM and HELLO_YOU applications.
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Aug-2004
 *
 */
public class FairlyGroovyWorkflowTest extends AbstractTestForWorkflow{

    /** Construct a new ScriptInputOutputWorkflowTest
     * @param applications
     * @param arg0
     */
    public FairlyGroovyWorkflowTest( String arg0) {
        super(new String[]{SUM,HELLO_YOU}, arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws WorkflowInterfaceException {
        wf.setName(this.getClass().getName());
        
        Set x = new Set();
        x.setVar("x");
        x.setValue("2");
        Set y = new Set();
        y.setVar("y");
        y.setValue("5");
        
        wf.getSequence().addActivity(x);
        wf.getSequence().addActivity(y);
        
        ApplicationDescription descr = reg.getDescriptionFor(SUM);
        Tool  sumTool = descr.createToolFromDefaultInterface();
        // setup parameters
        ParameterValue pval = (ParameterValue)sumTool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("${x}"); 
        pval.setIndirect(false);
        pval = (ParameterValue)sumTool.findXPathValue("input/parameter[name='parameter-1']");
        pval.setValue("${y}");
        pval.setIndirect(false);
        pval = (ParameterValue)sumTool.findXPathValue("output/parameter[name='result']");
        pval.setIndirect(false);        
        
        Step sum = new Step();
        sum.setResultVar("sumResults");
        sum.setDescription("call sum tool");
        sum.setName("sum");
        sum.setTool(sumTool);
        wf.getSequence().addActivity(sum);
        
        Script sc = new Script();
        sc.setBody("print (sumResults != null && sumResults.result != null && sumResults.result == '7')");
        wf.getSequence().addActivity(sc);
        
        descr = reg.getDescriptionFor(HELLO_YOU);
        Tool helloTool = descr.createToolFromDefaultInterface();
        pval = (ParameterValue)helloTool.findXPathValue("input/parameter[name='parameter-0']");
        pval.setValue("${sumResults.result}"); 
        Step hello = new Step();
        hello.setDescription("call hello tool");
        hello.setName("hello ");
        hello.setResultVar("ignored"); // but need to specify to require jes to wait until results are returned.
        hello.setTool(helloTool);
        wf.getSequence().addActivity(hello);
    }



    /**
     * @throws ValidationException
     * @throws MarshalException
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#checkExecutionResults(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public void checkExecutionResults(Workflow result) throws Exception {
        
        super.checkExecutionResults(result); // checks workflow completed, at least.
        Step step = (Step)result.getSequence().getActivity(2);
        
        // have a look at the execution of the step.
        assertStepCompleted(step);

        ResultListType rList = getResultOfStep(step); 
        assertEquals(1,rList.getResultCount());
        assertEquals("7",rList.getResult(0).getValue());  // ok, so cea returned the correct result - so variables are being passed in.


        Script sc = (Script)result.getSequence().getActivity(3);
        assertScriptCompletedWithMessage(sc,"true");
        
        step = (Step)result.getSequence().getActivity(4);
        assertStepCompleted(step);

        rList =getResultOfStep(step);
        assertNotNull(rList);
        assertEquals(1,rList.getResultCount());
        assertEquals("Hello 7", rList.getResult(0).getValue()); // proves that results of one cea application can be passed in as parameters to the next.

        
        
       
    }
}


/* 
$Log: FairlyGroovyWorkflowTest.java,v $
Revision 1.7  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.4.56.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.4  2004/08/22 01:25:49  nw
improved concurrent behaviour

Revision 1.3  2004/08/12 21:46:15  nw
cleaned up assertions

Revision 1.2  2004/08/12 14:46:11  nw
fixed buglet

Revision 1.1  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow
 
*/