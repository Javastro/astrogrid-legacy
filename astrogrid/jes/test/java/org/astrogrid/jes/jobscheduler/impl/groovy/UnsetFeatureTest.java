/*$Id: UnsetFeatureTest.java,v 1.2 2004/11/29 20:00:24 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.Workflow;

/** test operation of 'unset' tag.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2004
 *
 */
public class UnsetFeatureTest extends AbstractTestForFeature {

    /** Construct a new UnsetFeatureTest
     * @param name
     */
    public UnsetFeatureTest(String name) {
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
        Unset u = new Unset();
        u.setVar("x");
        Script sc1 = new Script();
        sc1.setBody("print (x)"); // expected to throw.
        wf.getSequence().addActivity(x);
        wf.getSequence().addActivity(u);
        wf.getSequence().addActivity(sc1);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowError(result);
        Script script = (Script)result.getSequence().getActivity(2);
        assertScriptError(script);
    }

}


/* 
$Log: UnsetFeatureTest.java,v $
Revision 1.2  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.1.72.1  2004/11/26 01:31:18  nw
updated dependency on groovy to 1.0-beta7.
updated code and tests to fit.

Revision 1.1  2004/08/03 14:27:38  nw
added set/unset/scope features.
 
*/