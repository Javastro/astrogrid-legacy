/*$Id: BasicWorkflowBuilder.java,v 1.2 2004/03/11 13:53:36 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.portal.workflow.intf.WorkflowBuilder;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class BasicWorkflowBuilder implements WorkflowBuilder {
    /** Construct a new BasicWorkflowBuilder
     * 
     */
    public BasicWorkflowBuilder() {
        super();
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.WorkflowBuilder#createWorkflow(org.astrogrid.community.beans.v1.Credentials, java.lang.String, java.lang.String)
     */
    public Workflow createWorkflow(Credentials creds, String name, String description)
        throws WorkflowInterfaceException {
        Workflow wf = new Workflow();
        wf.setCredentials(creds);
        wf.setName(name);
        wf.setDescription(description);
        Sequence seq = new Sequence();
        wf.setSequence(seq);
        assert wf != null;
        assert wf.isValid();
        return wf;        
    }
}


/* 
$Log: BasicWorkflowBuilder.java,v $
Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/