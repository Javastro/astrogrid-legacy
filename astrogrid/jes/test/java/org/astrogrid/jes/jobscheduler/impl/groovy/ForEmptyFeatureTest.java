/*$Id: ForEmptyFeatureTest.java,v 1.3 2004/11/29 20:00:24 clq2 Exp $
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

import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class ForEmptyFeatureTest extends AbstractTestForFeature {

    /** Construct a new ForEmptyFeatureTest
     * @param name
     */
    public ForEmptyFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set acc = new Set();
        acc.setVar("acc");
        acc.setValue("${0}");
        wf.getSequence().addActivity(acc);
        
        For f = new For();
        f.setVar("i");
        f.setItems("${[]}");
       
        Script sc = new Script();
        sc.setBody("acc = acc + i");
        f.setActivity(sc);
        
        wf.getSequence().addActivity(f);
        
        Script end = new Script();
        end.setBody("try {i == 1} catch (MissingPropertyException e) {print(acc == 0)}"); 
        wf.getSequence().addActivity(end);
        
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script body = (Script)((For)result.getSequence().getActivity(1)).getActivity();
        assertEquals(0,body.getStepExecutionRecordCount());

        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");        
    }

}


/* 
$Log: ForEmptyFeatureTest.java,v $
Revision 1.3  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.2.68.1  2004/11/26 01:31:18  nw
updated dependency on groovy to 1.0-beta7.
updated code and tests to fit.

Revision 1.2  2004/08/13 09:10:05  nw
tidied imports

Revision 1.1  2004/08/05 14:38:30  nw
tests for sequential for construct
 
*/