/*$Id: WorkflowManagerFactory.java,v 1.2 2004/02/25 10:57:43 nw Exp $
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

import org.astrogrid.portal.workflow.design.LegacyWorkflowManager;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class WorkflowManagerFactory {
    /** Construct a new WorkflowManagerFactory
     * 
     */
    public WorkflowManagerFactory() {
        super();
        theInstance = new LegacyWorkflowManager();
    }
    
    public WorkflowManager getManager() {
        return theInstance;
    }
    
    private static WorkflowManager theInstance; 
}


/* 
$Log: WorkflowManagerFactory.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/