/*$Id: FlowFeatureTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 29-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test behaviour of flow - in particular scopes and merging rules.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class FlowFeatureTest extends AbstractTestForFeature {

    /** Construct a new FlowFeatureTest
     * @param name
     */
    public FlowFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Script initScript = new Script();
        initScript.setBody("vars.set('x',1)");
        wf.getSequence().addActivity(initScript);
        Flow f = new Flow();
        wf.getSequence().addActivity(f);
        Script sc = new Script();
        sc.setBody("x=x+1;print(x);");
        f.addActivity(sc);
        Script sc1 = new Script();
        sc1.setBody("x=x+10; print(x);");
        f.addActivity(sc1);
        Script finalScript = new Script();
        finalScript.setBody("print(x);"); // test which value we got
        wf.getSequence().addActivity(finalScript);
        return wf;        
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script branch1 = (Script)((Flow)result.getSequence().getActivity(1)).getActivity(0);
        assertScriptCompletedWithMessage(branch1,"2");
        Script branch2 = (Script)((Flow)result.getSequence().getActivity(1)).getActivity(1);
        assertScriptCompletedWithMessage(branch2,"11"); // note, not 12.
        Script finalS = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(finalS,"11");       
        
        
    }

}


/* 
$Log: FlowFeatureTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/