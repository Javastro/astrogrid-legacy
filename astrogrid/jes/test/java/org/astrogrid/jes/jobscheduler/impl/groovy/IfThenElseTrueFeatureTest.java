/*$Id: IfThenElseTrueFeatureTest.java,v 1.1 2004/08/05 09:59:46 nw Exp $
 * Created on 05-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test if statement, when test is true, and there are both then and else branches.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class IfThenElseTrueFeatureTest extends AbstractTestForFeature {

    /** Construct a new IfThenElseTrueFeatureTest
     * @param name
     */
    public IfThenElseTrueFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${true}");
        If i = new If();
        i.setTest("${x}"); // don't need anything more complex - we knoow
        
        Then then = new Then();
        i.setThen(then);
        
        Else el = new Else();
        i.setElse(el);
        
        Script sc = new Script();
        sc.setBody("x=!x;vars.set('y',2);"); // check we can access variables defined outside.        
        then.setActivity(sc);
        
        Script elScript = new Script();
        elScript.setBody("vars.set('y',x);");
        el.setActivity(elScript);
        
        Script endScript = new Script();
        endScript.setBody("print (!x && y == 2)"); // check we can see variables defined within if block.
        
        wf.getSequence().addActivity(x);
        wf.getSequence().addActivity(i);
        wf.getSequence().addActivity(endScript);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");
    }

}


/* 
$Log: IfThenElseTrueFeatureTest.java,v $
Revision 1.1  2004/08/05 09:59:46  nw
tests for if construct
 
*/