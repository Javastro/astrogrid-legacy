/*$Id: RemoteProcessManager.java,v 1.1 2005/11/10 12:13:52 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.util.Map;

/**Manages the execution , tracking, and control of remote processes
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Nov-2005
 *
 */
public interface RemoteProcessManager {
    
    /** list all remote processes known to belng to the current user
      @return list of identifiers for the user's remote processes - both running and completed    
     */
    URI[] list() throws ServiceException;
    
    /** Submit a document for execution.
     * <p>
     * No particular  server is specified - the system will choose a suitable server.
     * @param document the document to execute - a program / script / workflow / tool document
     * @return  a new unique execution id 
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this document
     * @throws NotFoundException if no service that can execute this document is found
     * @throws InvalidArgumentException if the document is malformed in some way or not of a recognized format
     * @see #submitStored(URI)
     * @see #submitTo(Document, URI)
     * */
    URI submit(Document document) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
    
    /** Submit a tool document for execution  on a named CEA server 
     * @param document tool document to execute
     * @param server CEA server to execute on
     * @return  a new unique execution id 
     * @throws NotFoundException if the specified CEA server could not be found
     * @throws InvalidArgumentException if the tool document is malformed, or the server is inacessible.
     * @throws ServiceException  if an error occurs communicating with the server
     * @throws SecurityException if user is prevented from executing this application.
     * @see #submitStored(URI)
     * @see #submitStoredTo(URI, URI)
     * */
    URI submitTo(Document document, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException;

    /** variant of {@link #submit} where tool document is stored somewhere and referenced by URI 
     * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @return a new unique execution id
     * @throws InvalidArgumentException if the tool document is inacessible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no provider of this application is found
     */
     URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException ;

    /** variant of {@link #submitTo} where tool document is referenced by URL 
     *      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @param server CEA server to execute on
     * @return a new unique execution id
     * @throws NotFoundException if the specified CEA server could not be found
     * @throws InvalidArgumentException if the tool document is inacessible or ther service is inacesssible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * 
     * */
    URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException ;
        
    /** halt execution of an application 
     * @param executionId id of execution to cancel
     * @throws NotFoundException if this application cannot be found.
     * @throws InvalidArgumentException if the execution id is malformed
     * @throws ServiceException if an error occurs while communicating with server
     * @throws SecurityException if the user is not permitted to cancel this application
     * */
    void halt(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;

    /** delete all record of a job from the job server
     * 
     * @param jobURN identifier of the job to delete 
     * @throws NotFoundException if the job could not be found
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job.
     */
    void delete(URI jobURN) throws NotFoundException, ServiceException,InvalidArgumentException, SecurityException;
    
    
    /** get  information about an application execution
     * @param executionId id of application to query 
     * @return summary of this execution
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.
     * @xmlrpc will return a struct containing keys documented in {@link ExecutionInformation}
     * */
    ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    
    /** return an array of messages received from a remote process 
     * 
     * @param executionId
     * @return
     * @throws ServiceException
     * @throws NotFoundException
     */
    ExecutionMessage[] getMessages(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    /** Retreive results of the application execution 
     * @param executionid id of application to query 
     * @return results of this execution (name - value pairs). Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned
     * will be the URI pointing to the location where the results are stored.
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.*/
    Map getResults(URI executionid) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;               
    
    //@todo document
    void addRemoteProcessListener(URI executionId,RemoteProcessListener l);
    void removeRemoteProcessListener(URI executionId,RemoteProcessListener l);
    
}


/* 
$Log: RemoteProcessManager.java,v $
Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/