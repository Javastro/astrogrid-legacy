/*$Id: FlowEmptyFeatureTest.java,v 1.4 2004/11/05 16:52:42 jdt Exp $
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
import org.astrogrid.workflow.beans.v1.Workflow;

/** test behaviur of workflow containing an empty flow.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class FlowEmptyFeatureTest extends AbstractTestForFeature {

    /** Construct a new EmptyFlowFeatureTest
     * @param name
     */
    public FlowEmptyFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Flow f = new Flow();
        wf.getSequence().addActivity(f);

        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        super.assertWorkflowCompleted(result);
    }

}


/* 
$Log: FlowEmptyFeatureTest.java,v $
Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.18.1  2004/11/05 16:07:41  nw
tidied imports

Revision 1.3  2004/09/16 12:04:21  nw
*** empty log message ***

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/