/*$Id: WorkflowStore.java,v 1.3 2004/03/03 11:15:23 nw Exp $
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

/** A component that can store and retreive workflow documents
 * (i.e. myspace)
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface WorkflowStore {

    /** delete a workflow from the store
     * 
     * @param acc account details of the owner of the workflow
     * @param name name of the workflow
     * @throws WorkflowInterfaceException
     */
    void deleteWorkflow(Account acc, String name) throws WorkflowInterfaceException;

    /** Read a query from the store
     * 
     * @param acc account details of the ownder of the query
     * @param name name of the query
     * @return a string representation of the query. will never return null
     * @throws WorkflowInterfaceException if fails to read query
     */
    String readQuery( Account acc
                                  , String name ) throws WorkflowInterfaceException ;
    /** return list of names of queries present in the store 
     * @deprecated - use {@link listQueries}, which returns a strongly-typed array*/
    Iterator readQueryList( Account acc ) throws WorkflowInterfaceException;
    
    /** return list of names of queries present in the store
     * 
     * @param acc account details for the owner of the list of queries
     * @return list of query names. never returns null
     * @throws WorkflowInterfaceException
     */
    String[] listQueries(Account acc) throws WorkflowInterfaceException;
                                           

    /** read workflow from the store 
     * 
     * @param acc account details for the owner of the workflow document 
     * @param name name of the workflow document
     * @return workflow document object. will never return null
     * @throws WorkflowInterfaceException if document can't be loaded
     */
    Workflow readWorkflow( Account acc
                                       , String name ) throws WorkflowInterfaceException;
    /** read list of workflow names from myspace
     * @deprecated - use {@link listWorkflows} which returns a strongly-typed array */
    Iterator readWorkflowList( Account acc ) throws WorkflowInterfaceException;
   /** read list of workflow names from myspace 
    * 
    * @param acc account details for the owner of the workflow documents listed
    * @return list of workflow names. never returns null
    * @throws WorkflowInterfaceException
    */                                        
   String[] listWorkflows(Account acc) throws WorkflowInterfaceException;                                         
    /** save workflow to myspace 
     * 
     * @param acc account details for the owner of the workflow
     * @param workflow workflow document to save
     * @throws WorkflowInterfaceException
     */           
    void saveWorkflow( Account acc
                                      , Workflow workflow ) throws WorkflowInterfaceException;

}


/* 
$Log: WorkflowStore.java,v $
Revision 1.3  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/