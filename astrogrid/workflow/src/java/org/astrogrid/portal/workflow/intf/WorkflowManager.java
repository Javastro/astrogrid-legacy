/*$Id: WorkflowManager.java,v 1.6 2004/03/09 15:33:00 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;


/** Container object that collects the various components used to interact with workflows.
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface WorkflowManager{

    /** get a component to build workflows. never null*/
    public WorkflowBuilder getWorkflowBuilder();
    /** get a component to access a store. never null */
    public WorkflowStore getWorkflowStore();
    /** get a component to query a registry. never null */
    public ApplicationRegistry getToolRegistry();
    /** get a component to manage jobs - executoions of workflows. never null */
    public JobExecutionService getJobExecutionService();
    
}


/* 
$Log: WorkflowManager.java,v $
Revision 1.6  2004/03/09 15:33:00  nw
renamed toolRegistry to ApplicationRegistry

Revision 1.5  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.4  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.3.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish

Revision 1.3  2004/03/01 15:03:38  nw
simplified by removing facade - will expose object model directly

Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.2  2004/02/24 21:57:00  nw
refined the method types

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/