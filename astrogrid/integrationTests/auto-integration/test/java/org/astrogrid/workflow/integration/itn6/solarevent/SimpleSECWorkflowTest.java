/*$Id: SimpleSECWorkflowTest.java,v 1.1 2004/08/12 13:33:34 nw Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.integration.AbstractTestForWorkflow;

/** test single step workflow that calls the sec.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class SimpleSECWorkflowTest  extends AbstractTestForWorkflow implements SolarEventKeys{

    /** Construct a new SimpleSECWorkflowTest
     * @param applications
     * @param arg0
     */
    public SimpleSECWorkflowTest( String arg0) {
        super(new String[]{SEC_APP}, arg0);
    }

    /**
     * @see org.astrogrid.workflow.integration.AbstractTestForWorkflow#buildWorkflow()
     */
    protected void buildWorkflow() throws WorkflowInterfaceException {
        wf.setName(this.getClass().getName());
    }
    public void checkExecutionResults(Workflow result) throws Exception {
        super.checkExecutionResults(result);
    }


}


/* 
$Log: SimpleSECWorkflowTest.java,v $
Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/