/*$Id: WorkflowManager.java,v 1.3 2004/03/01 15:03:38 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.util.Iterator;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public interface WorkflowManager {
    //object creation methods
    /** create a tool object */
    Tool createTool(String communitySnippet,String name);
    /** create a workflow object */
    Workflow createWorkflow(String communitySnippet,String name,String description);

    // interacting with JES
    /** delete a workflow from Jes 
     * @todo find out whether this is really from myspace*/
    void deleteWorkflow(String communitySnippet, String name) throws WorkflowInterfaceException;
    void insertParameterValue( Tool tool
                                              , String paramName
                                              , String oldValue
                                              , String newValue
                                              , String direction ) throws WorkflowInterfaceException;
   /** @todo document */                                             
    void insertToolIntoStep( String stepActivityKey
                                              , Tool tool
                                              , Workflow workflow ) throws WorkflowInterfaceException;
    /** @todo document */
    String readQuery( String communitySnippet
                                  , String name ) throws WorkflowInterfaceException ;
    /** @todo document */
    Iterator readQueryList( String communitySnippet
                                        , String filter ) throws WorkflowInterfaceException;
    /** @todo document */ 
    Iterator readToolList( String communitySnippet ) throws WorkflowInterfaceException;
    /** @todo document */
    Workflow readWorkflow( String communitySnippet
                                       , String name ) throws WorkflowInterfaceException;
    
    Iterator readWorkflowList( String communitySnippet
                                           , String filter ) throws WorkflowInterfaceException;
    void saveWorkflow( String communitySnippet
                                      , Workflow workflow ) throws WorkflowInterfaceException;
    void submitWorkflow( String communitySnippet
                                        , Workflow workflow ) throws WorkflowInterfaceException;                               
    
    // plus add bits required by Jes   
}


/* 
$Log: WorkflowManager.java,v $
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