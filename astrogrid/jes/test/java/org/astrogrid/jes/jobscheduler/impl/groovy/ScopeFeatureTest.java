/*$Id: ScopeFeatureTest.java,v 1.1 2004/08/03 14:27:38 nw Exp $
 * Created on 03-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test out the scope language construct
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2004
 *
 */
public class ScopeFeatureTest extends AbstractTestForFeature {

    /** Construct a new ScopeFeatureTest
     * @param name
     */
    public ScopeFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${42}");
        Set uni = new Set();
        uni.setVar("uni"); // an uninitialized var.
        
        Scope scope = new Scope();
        Sequence seq = new Sequence();
        scope.setActivity(seq);
        
        Set y = new Set();
        y.setVar("y");
        y.setValue("${x + 2}");
        
        Set ini = new Set();
        ini.setVar("uni");
        ini.setValue("${y}");
        
        Script sc = new Script();
        sc.setBody("x = y + 2; print(x == 46 && y == 44 )"); 
        
        seq.addActivity(y);
        seq.addActivity(ini);
        seq.addActivity(sc);
        
        
        Script finalScript = new Script();
//      check that x has been updated in nested scope,
//        y has been removed after scope finished
//      and uni should contain value set in nested scope.
        finalScript.setBody("print (x == 46 && y == null && uni == 44)");        
        wf.getSequence().addActivity(x);
        wf.getSequence().addActivity(uni);
        wf.getSequence().addActivity(scope);
        wf.getSequence().addActivity(finalScript);
        
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Sequence seq = (Sequence)((Scope)result.getSequence().getActivity(2)).getActivity();
        Script script = (Script)seq.getActivity(2);
        assertScriptCompletedWithMessage(script,"true");
        script = (Script)result.getSequence().getActivity(3);
        assertScriptCompletedWithMessage(script,"true");
    }

}


/* 
$Log: ScopeFeatureTest.java,v $
Revision 1.1  2004/08/03 14:27:38  nw
added set/unset/scope features.
 
*/