/*$Id: WorkflowBuilder.java,v 1.2 2004/03/03 01:36:38 nw Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Interface to a component of method to help build workflow documents
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface WorkflowBuilder {

    /** create a workflow object
     * @todo replace community snippet with something else */
    Workflow createWorkflow(Credentials creds,String name,String description);

/* may add more methods here, as needed */
}

/* 
$Log: WorkflowBuilder.java,v $
Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/