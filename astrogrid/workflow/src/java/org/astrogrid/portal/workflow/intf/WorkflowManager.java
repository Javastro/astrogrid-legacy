/*$Id: WorkflowManager.java,v 1.2 2004/02/25 10:57:43 nw Exp $
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

import java.util.Iterator;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface WorkflowManager {
    ITool createTool(String communitySnippet,String name);
    IWorkflow createWorkflow(String communitySnippet,String name,String description);
    IWorkflow createWorkflowFromTemplate(String communitySnippet, String name, String description,String templateName)
        throws WorkflowInterfaceException;
    void deleteWorkflow(String communitySnippet, String name) throws WorkflowInterfaceException;
    void insertParameterValue( ITool tool
                                              , String paramName
                                              , String oldValue
                                              , String newValue
                                              , String direction ) throws WorkflowInterfaceException;
    void insertToolIntoStep( String stepActivityKey
                                              , ITool tool
                                              , IWorkflow workflow ) throws WorkflowInterfaceException;  
    String readQuery( String communitySnippet
                                  , String name ) throws WorkflowInterfaceException ;
    Iterator readQueryList( String communitySnippet
                                        , String filter ) throws WorkflowInterfaceException; 
    Iterator readToolList( String communitySnippet ) throws WorkflowInterfaceException;
    IWorkflow readWorkflow( String communitySnippet
                                       , String name ) throws WorkflowInterfaceException;
    Iterator readWorkflowList( String communitySnippet
                                           , String filter ) throws WorkflowInterfaceException;
    void saveWorkflow( String communitySnippet
                                      , IWorkflow workflow ) throws WorkflowInterfaceException;
    void submitWorkflow( String communitySnippet
                                        , IWorkflow workflow ) throws WorkflowInterfaceException;                               
    
       
}


/* 
$Log: WorkflowManager.java,v $
Revision 1.2  2004/02/25 10:57:43  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.2  2004/02/24 21:57:00  nw
refined the method types

Revision 1.1.2.1  2004/02/24 15:35:46  nw
extracted public interface from each implementation class.
altered types to reference interface rather than implementation whever possible.
added factory and manager facade at the front
 
*/