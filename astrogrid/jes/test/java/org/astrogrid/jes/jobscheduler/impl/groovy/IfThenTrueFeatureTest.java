/*$Id: IfThenTrueFeatureTest.java,v 1.2 2004/12/09 16:39:12 clq2 Exp $
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

import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Test if statement, when no else, and test evaluates to true.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class IfThenTrueFeatureTest extends AbstractTestForFeature {

    /** Construct a new IfThenTrueFeatureTest
     * @param name
     */
    public IfThenTrueFeatureTest(String name) {
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
        
        Script sc = new Script();
        sc.setBody("x=!x;"); // check we can access variables defined outside.        
        then.setActivity(sc);
        
        Script endScript = new Script();
        endScript.setBody("print (!x)"); // check we can see variables defined within if block.
        
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
$Log: IfThenTrueFeatureTest.java,v $
Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.76.1  2004/12/09 14:42:54  nw
made more robust.
still got looping bug though.

Revision 1.1  2004/08/05 09:59:46  nw
tests for if construct
 
*/