/*$Id: WhileNestedFeatureTest.java,v 1.2 2004/12/09 16:39:12 clq2 Exp $
 * Created on 09-Dec-2004
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
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Dec-2004
 *
 */
public class WhileNestedFeatureTest extends AbstractTestForFeature {

    /** Construct a new WhileNestedFeatureTest
     * @param name
     */
    public WhileNestedFeatureTest(String name) {
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
$Log: WhileNestedFeatureTest.java,v $
Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.2.1  2004/12/09 16:11:03  nw
fixed for and while loops
 
*/