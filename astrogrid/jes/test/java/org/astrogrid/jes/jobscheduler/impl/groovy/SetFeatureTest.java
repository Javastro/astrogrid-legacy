/*$Id: SetFeatureTest.java,v 1.1 2004/08/03 14:27:38 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test operation of 'set' tag.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2004
 *
 */
public class SetFeatureTest extends AbstractTestForFeature {

    /** Construct a new SetFeatureTest
     * @param name
     */
    public SetFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x"); // set variable to number.
        x.setValue("${42}");
        Set d = new Set();
        d.setVar("d"); // set variable to object
        d.setValue("${new java.util.Date()}");
        Set y = new Set();
        y.setVar("y"); // set variable based on previous value.
        y.setValue("${x + 1}");
        Set str = new Set();
        str.setVar("str"); // set variable to vanilla string.
        str.setValue("hi there");
        Script sc = new Script();
        sc.setBody("y=y+1; print (x instanceof Integer && x == 42 && d instanceof java.util.Date && y instanceof Integer && y == 44 && str instanceof String && str == 'hi there')");
        wf.getSequence().addActivity(x);
        wf.getSequence().addActivity(d);
        wf.getSequence().addActivity(y);
        wf.getSequence().addActivity(str);
        wf.getSequence().addActivity(sc);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script script = (Script)result.getSequence().getActivity(4);
        assertScriptCompletedWithMessage(script,"true");
    }

}


/* 
$Log: SetFeatureTest.java,v $
Revision 1.1  2004/08/03 14:27:38  nw
added set/unset/scope features.
 
*/