/*$Id: WorkflowStore.java,v 1.2 2004/03/03 01:36:38 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.util.Iterator;

/** Interface to a component that can store / retreive workflow documents from a store
 * (i.e. myspace)
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface WorkflowStore {

    /** delete a workflow from Myspace */
    void deleteWorkflow(Account acc, String name) throws WorkflowInterfaceException;

    /** Read a query from myspace 
     * @todo - stronger type?*/
    String readQuery( Account acc
                                  , String name ) throws WorkflowInterfaceException ;
    /** return list of names of queries present in myspace 
     * @deprecated - use {@link listQueries}, which returns a strongly-typed array*/
    Iterator readQueryList( Account acc ) throws WorkflowInterfaceException;
    /** return list of names of queries present in myspace */
    String[] listQueries(Account acc) throws WorkflowInterfaceException;
                                           

    /** read workflow from myspace */
    Workflow readWorkflow( Account acc
                                       , String name ) throws WorkflowInterfaceException;
    /** read list of workflow names from myspace
     * @deprecated - use {@link listWorkflows} which returns a strongly-typed array */
    Iterator readWorkflowList( Account acc ) throws WorkflowInterfaceException;
   /** read list of workflow names from myspace */                                           
   String[] listWorkflows(Account acc) throws WorkflowInterfaceException;                                         
    /** save workflow to myspace */                                           
    void saveWorkflow( Account acc
                                      , Workflow workflow ) throws WorkflowInterfaceException;

}


/* 
$Log: WorkflowStore.java,v $
Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/