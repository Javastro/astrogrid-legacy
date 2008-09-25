/*$Id: RemoteProcessManager.java,v 1.9 2008/09/25 16:02:04 nw Exp $
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

import java.net.URI;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

/** AR Service: A general manager for the execution , monitoring, and control of all remote processes.
 *
 * RemoteProcessManager unifies the  functionality in {@link org.astrogrid.acr.astrogrid.Jobs} and {@link org.astrogrid.acr.astrogrid.Applications}
 * and provides additional features - notably ability to register callbacks for progress notifications. It is still valid to use the <tt>Jobs</tt> or <tt>Applications</tt>, 
 * however, this interface also knows how to invoke other kinds of service, and provides a uniform interface to cea, cone, siap, ssap services.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 * 
 * @note As JES is no longer supported, and we no longer provide cea-style querying of cone and siap, 
 * maybe this interface should be retired?

 * @service astrogrid.processManager
 */
public interface RemoteProcessManager {
    
    /** list current remote processes  belonging to the current user
      @return list of identifiers for the user's remote processes that are currently being managed by AR
     */
    URI[] list() throws ServiceException;
    
    /** Submit a document for execution.
     * 
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
    
    /** Submit a document for execution  on a named server 
     * @param document the document to execute - a workflow, cea task, etc
     * @param server server to execute on
     * @return  a new unique execution id 
     * @throws NotFoundException if the specified server could not be found
     * @throws InvalidArgumentException if the document is malformed, or the server is inappropriate
     * @throws ServiceException  if an error occurs communicating with the server
     * @throws SecurityException if user is prevented from executing this application.
     * @see #submitStored(URI)
     * @see #submitStoredTo(URI, URI)
     * */
    URI submitTo(Document document, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException;

    /** variant of {@link #submit} where document is stored somewhere and referenced by URI 
     * @param documentLocation pointer to document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @return a new unique execution id
     * @throws InvalidArgumentException if the document is inacessible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no server able to execute this document is found
     */
     URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException ;

    /** variant of {@link #submitTo} where document is referenced by URL 
     *      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @param server to execute on
     * @return a new unique execution id
     * @throws NotFoundException if the specified server could not be found
     * @throws InvalidArgumentException if the document is inacessible or ther service is inacesssible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * 
     * */
    URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException ;
        
    /** halt execution of a process
     * @param executionId id of execution to cancel
     * @throws NotFoundException if this application cannot be found.
     * @throws InvalidArgumentException if the execution id is malformed
     * @throws ServiceException if an error occurs while communicating with server
     * @throws SecurityException if the user is not permitted to cancel this application
     * */
    void halt(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;

    /** delete all record of a process
     * 
     * @param executionId identifier of the process to delete 
     * @throws NotFoundException if the process could not be found
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job.
     */
    void delete(URI executionId) throws NotFoundException, ServiceException,InvalidArgumentException, SecurityException;
    
    
    /** get  information about an process execution
     * @param executionId id of application to query 
     * @return summary of this execution
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.
     * @xmlrpc will return a struct containing keys documented in {@link ExecutionInformation}
     * */
    ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    
    /** return the messages received from a remote process 
     * 
     * @param executionId id of process to query/
     * @return an array of all messages received from process     
     * @throws ServiceException
     * @throws NotFoundException
     * @xmlrpc will return a list of structs containing keys documented in {@ExecutionMessage}
     */
    ExecutionMessage[] getMessages(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    /** Retreive results of the application execution 
     * @param executionid id of application to query 
     * @return results of this execution (name - value pairs). 
     * Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned
     * will be the URI pointing to the location where the results are stored.
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.*/
    Map getResults(URI executionid) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;               
    
    /** convenience method to retreive a single result from an application executioin.
     * 
     * equaivalent to <tt>getResults(execId).get(resultName)</tt>, but may be more convenient from some scripting languages, or cases
     * where there's only a single result returned.
     * @param executionId id of the application to query
     * @param resultName name of the result to return. If the name is null or unrecognized AND this application only returns one result, that is returned.
     * @return a result. Never null.
     * @throws ServiceException if error occurs communicating with server
     * @throws SecurityException if the user cannot access this process
     * @throws NotFoundException if the application cannot be found, or the resultname is not recognized where there's more than one result.
     * @throws InvalidArgumentException if the execution id is malformed in some way.
     */
    public String getSingleResult(URI executionId, String resultName) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
    /** register interest in the execution of a remote process 
     * 
     * @param executionId id of process to watch (null signifies 'all') 
     * @param l a listener object 
     * @xmlrpc not available - technology constraints don't permit listeners.
     */
    void addRemoteProcessListener(URI executionId,RemoteProcessListener l);
    /** stop listening to events from a remote process
     * 
     * @param executionId id of process to remove attention on (null signifies all)
     * @param l the listener object to be removed. 
     */
    void removeRemoteProcessListener(URI executionId,RemoteProcessListener l);
    
}


/* 
$Log: RemoteProcessManager.java,v $
Revision 1.9  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.8  2007/07/16 12:19:16  nw
Complete - task 91: make remoteprocessmanager a full fledged ar member

Revision 1.7  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.6  2007/01/24 14:04:44  nw
updated my email address

Revision 1.5  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.4  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.3  2005/11/24 01:18:42  nw
merged in final changes from release branch.

Revision 1.2.2.1  2005/11/23 18:07:11  nw
added new method

Revision 1.2  2005/11/11 10:09:01  nw
improved javadoc

Revision 1.1  2005/11/10 12:13:52  nw
interface changes for lookout.
 
*/