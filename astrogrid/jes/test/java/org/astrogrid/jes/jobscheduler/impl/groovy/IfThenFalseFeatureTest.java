/*$Id: IfThenFalseFeatureTest.java,v 1.2 2004/11/29 20:00:24 clq2 Exp $
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

/** test if statement, when test is false, and single then block.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class IfThenFalseFeatureTest extends AbstractTestForFeature {

    /** Construct a new IfThenFalseFeatureTest
     * @param name
     */
    public IfThenFalseFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${false}");
        If i = new If();
        i.setTest("${x}"); 
        
        Then then = new Then();
        i.setThen(then);
        
        Script sc = new Script();
        sc.setBody("x=!x;vars.set('y',2);"); // shouldn't be called. 
        then.setActivity(sc);
        
        Script endScript = new Script();
        endScript.setBody("try {y == 1 } catch (MissingPropertyException e) {print (!x)}"); // check we can see variables defined within if block.
        
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
$Log: IfThenFalseFeatureTest.java,v $
Revision 1.2  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.1.72.1  2004/11/26 01:31:18  nw
updated dependency on groovy to 1.0-beta7.
updated code and tests to fit.

Revision 1.1  2004/08/05 09:59:46  nw
tests for if construct
 
*/