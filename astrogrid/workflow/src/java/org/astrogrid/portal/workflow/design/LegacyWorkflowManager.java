/*$Id: LegacyWorkflowManager.java,v 1.2 2004/02/25 10:57:43 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.intf.*;

import java.util.Iterator;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class LegacyWorkflowManager implements WorkflowManager {
    /** Construct a new LegacyWorkflowManager
     * 
     */
    public LegacyWorkflowManager() {
        super();
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#createTool(java.lang.String, java.lang.String)
     */
    public ITool createTool(String communitySnippet, String name) {
        return Workflow.createTool(communitySnippet,name);
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#createWorkflow(java.lang.String, java.lang.String, java.lang.String)
     */
    public IWorkflow createWorkflow(String communitySnippet, String name, String description) {
        return Workflow.createWorkflow(communitySnippet,name,description);
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#createWorkflowFromTemplate(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public IWorkflow createWorkflowFromTemplate(String communitySnippet, String name, String description, String templateName) throws WorkflowInterfaceException{
        IWorkflow result =  Workflow.createWorkflowFromTemplate(communitySnippet,name,description,templateName);
        if (result == null) {
            throw new WorkflowInterfaceException("OPeration failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#deleteWorkflow(java.lang.String, java.lang.String)
     */
    public void deleteWorkflow(String communitySnippet, String name) throws WorkflowInterfaceException {
        if (!Workflow.deleteWorkflow(communitySnippet,name)) {
            throw new WorkflowInterfaceException("Operation failed");
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#insertParameterValue(org.astrogrid.portal.workflow.design.ITool, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void insertParameterValue(ITool tool, String paramName, String oldValue, String newValue, String direction) throws WorkflowInterfaceException{
        if (!Workflow.insertParameterValue(tool,paramName,oldValue,newValue,direction)) {
            throw new WorkflowInterfaceException("Operation failed");
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#insertToolIntoStep(java.lang.String, org.astrogrid.portal.workflow.design.ITool, org.astrogrid.portal.workflow.design.IWorkflow)
     */
    public void insertToolIntoStep(String stepActivityKey, ITool tool, IWorkflow workflow) throws WorkflowInterfaceException {
        if (!Workflow.insertToolIntoStep(stepActivityKey,tool,workflow)) {
            throw new WorkflowInterfaceException("Operation failed");
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#readQuery(java.lang.String, java.lang.String)
     */
    public String readQuery(String communitySnippet, String name) throws WorkflowInterfaceException{
        String result = Workflow.readQuery(communitySnippet,name);
        if (result == null) {
            throw new WorkflowInterfaceException("Operation failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#readQueryList(java.lang.String, java.lang.String)
     */
    public Iterator readQueryList(String communitySnippet, String filter) throws WorkflowInterfaceException {
        Iterator result = Workflow.readQueryList(communitySnippet,filter);
        if (result == null) {
            throw new WorkflowInterfaceException("Operation failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#readToolList(java.lang.String)
     */
    public Iterator readToolList(String communitySnippet) throws WorkflowInterfaceException{
        Iterator result =  Workflow.readToolList(communitySnippet);
        if (result == null) {
            throw new WorkflowInterfaceException("operation failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#readWorkflow(java.lang.String, java.lang.String)
     */
    public IWorkflow readWorkflow(String communitySnippet, String name) throws WorkflowInterfaceException {
        IWorkflow result = Workflow.readWorkflow(communitySnippet,name);
        if (result == null) {
            throw new WorkflowInterfaceException("Operation failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#readWorkflowList(java.lang.String, java.lang.String)
     */
    public Iterator readWorkflowList(String communitySnippet, String filter) throws WorkflowInterfaceException {        
        Iterator result =  Workflow.readWorkflowList(communitySnippet,filter);
        if (result == null) {
            throw new WorkflowInterfaceException("Operation failed");
        }
        return result;
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#saveWorkflow(java.lang.String, org.astrogrid.portal.workflow.design.IWorkflow)
     */
    public void saveWorkflow(String communitySnippet, IWorkflow workflow) throws WorkflowInterfaceException {
        if (!Workflow.saveWorkflow(communitySnippet,(Workflow)workflow)){
            throw new WorkflowInterfaceException("Operation failed");
        }
    }

    /**
     * @see org.astrogrid.portal.workflow.design.WorkflowManager#submitWorkflow(java.lang.String, org.astrogrid.portal.workflow.design.IWorkflow)
     */
    public void submitWorkflow(String communitySnippet, IWorkflow workflow) throws WorkflowInterfaceException {
        if (!Workflow.submitWorkflow(communitySnippet,(Workflow)workflow)) {
            throw new WorkflowInterfaceException("Operation failed");
        }
    }
}


/* 
$Log: LegacyWorkflowManager.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.2  2004/02/24 21:57:14  nw
updated to interface changes

Revision 1.1.2.1  2004/02/24 15:35:47  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/