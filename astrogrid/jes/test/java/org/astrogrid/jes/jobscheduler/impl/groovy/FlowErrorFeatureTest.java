/*$Id: FlowErrorFeatureTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class FlowErrorFeatureTest extends AbstractTestForFeature {

    /** Construct a new ErrorFlowFeatureTest
     * @param name
     */
    public FlowErrorFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        return null;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
    }

}


/* 
$Log: FlowErrorFeatureTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/