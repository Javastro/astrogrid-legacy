/*$Id: SequenceFeatureTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test exection of a sequence of actions.
 * also tests variable propagationo between a sequence of scripts.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class SequenceFeatureTest extends AbstractTestForFeature {

    /** Construct a new SequenceFeatureTest
     * @param name
     */
    public SequenceFeatureTest(String name) {
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
        Sequence seq = new Sequence();
        wf.getSequence().addActivity(seq);
        Script sc1 = new Script();
        sc1.setBody("x = x + 1;vars.set('y',1);print(x);");
        seq.addActivity(sc1);
        Script sc2 = new Script();
        sc2.setBody("x = x * 10; y = y + 10;print(x);");
        seq.addActivity(sc2);
        Script finalScript = new Script();
        finalScript.setBody("print(x);print(y)");
        wf.getSequence().addActivity(finalScript);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {

        assertWorkflowCompleted(result);
        
        Script initScript  = (Script)result.getSequence().getActivity(0);
        assertScriptCompleted(initScript);
        
        Sequence seq = (Sequence)result.getSequence().getActivity(1);
        
        Script sc1 = (Script)seq.getActivity(0);
        assertScriptCompletedWithMessage(sc1,"2");
        
        Script sc2 = (Script)seq.getActivity(1);
        assertScriptCompletedWithMessage(sc2,"20");
        
        Script finalScript = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(finalScript,"2011");
    }

}


/* 
$Log: SequenceFeatureTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/