/*$Id: AbstractInProcessDispatcher.java,v 1.2 2005/03/13 07:13:39 clq2 Exp $
 * Created on 11-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentManager;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.inprocess.InProcessQueryService;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Abstract class for all dispatchers that dispatch a step to a cea application in the in-process cea server.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Mar-2005
 *
 */
public abstract class AbstractInProcessDispatcher implements Dispatcher {


    public AbstractInProcessDispatcher(CEAComponentManager cea) {
        super();
        this.cea = cea;
    }
    protected final CEAComponentManager cea;    

    /** Dispatching any internal application follows the same pattern - massage the tool parameters as needed,
     * create the cea application call 
     * register in-process resylts and progress listeners,
     * start the cea application running. 
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Tool, java.lang.String)
     */
    public void dispatchStep(Workflow wf, Tool tool, String id)
            throws JesException {
        try {
            tool = transformTool(tool); 
        String ceaId = cea.getExecutionController().init(tool,id);
        cea.getQueryService().registerProgressListener(ceaId,InProcessQueryService.INPROCESS_URI);
        cea.getQueryService().registerResultsListener(ceaId,InProcessQueryService.INPROCESS_URI);
        //start it running.
        cea.getExecutionController().execute(ceaId);
        } catch (CeaException e) {
            throw new JesException("Could not dispatch job to inprocess cea service",e);
        }
    }
    
    /** extension point that externders can implement to massage structure of tool document before its submitted to 
     * the in-process cea.
     * @param tool 
     */
    protected abstract Tool transformTool(Tool tool) ;
    
    
}


/* 
$Log: AbstractInProcessDispatcher.java,v $
Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.2.1  2005/03/11 14:04:03  nw
added new kinds of dispatcher.
 
*/