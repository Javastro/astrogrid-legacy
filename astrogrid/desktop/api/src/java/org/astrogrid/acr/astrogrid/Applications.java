/*$Id: Applications.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 21-Mar-2005
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

/** Applications and query services - discover, execute, monitor.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public interface Applications {
    /** list names of applications 
     * @return a list of the identifiers of available applications
     * @throws ServiceException if error occurs while talking to server */
    URI[] list() throws ServiceException;
    
    /** list information about applications 
     * @return an array details of the available applications
     * @throws ServiceException if error occurs while talking to server */
    ApplicationInformation[] listFully() throws ServiceException;
       
    /** get information for a specific application 
     * @param applicationName name of the application to hunt for
     * @return details of this application
     * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed
     * */
    ApplicationInformation getApplicationInformation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException;
   
    /** access formatted iniformation about an application 
 * @param applicationName
 * @return formatted, human-readable information about the application
     * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed
     * */
    String getDocumentation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException;
    
 
   /** Create a template tool document, suitable for calling this application 
 * @param applicationName the application to create the template for
 * @param interfaceName interface of this application to create a template from.
 * @return a tool document. (xml form)
  * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed*/
    Document createTemplateDocument(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException;
    
    
    /** Create a template tool object, suitable for calling this application
     * <p>
     * this method is provided for convenience of constructing calls in scripting languages with minimal
     * xml abilities. 
  * @param applicationName the application to create the template for
  * @param interfaceName interface of this application to create a template from.
  * @return a tool object (structure)
   * @throws ServiceException if error occurs when talking to the sever
      * @throws NotFoundException if this application could not be found
      * @throws InvalidArgumentException if the application name is malformed*/
     Map createTemplateStruct(URI applicationName, String interfaceName)
             throws ServiceException, NotFoundException, InvalidArgumentException;
   
     /** convert a tool document to a tool structure 
     * @param document a tool document
     * @return the equvalent tool structure
     * @throws InvalidArgumentException if  document is malformed*/
   Map convertDocumentToStruct(Document document) throws InvalidArgumentException ;
    
    
    /** convert a tool structure to the equivalent document 
     * @param struct a tool structure
     * @return the equivalent tool document
     * @throws InvalidArgumentException if structure cannot be converted to a document*/
   Document convertStructToDocument(Map struct) throws InvalidArgumentException ;
    
  /**Validate a tool document against an application 
                description 
 * @param document tool document to validate
 * @throws ServiceException if fails to communicate with server
 * @throws InvalidArgumentException if document is invalid in some way*/
    void validate(Document document) throws ServiceException, InvalidArgumentException;

    /** Validate a tool document (referenced by url) against 
                an application description 
     * @param applicationName name of the application to validate against.
     * @param documentLocation location of a resource containing the tool document to validate
     * @throws ServiceException if fails to communicate with server.
     * @throws InvalidArgumentException if  document is invalid in some way
     * @throws NotFoundException if document cannot be accessed*/
    void validateStored(URI documentLocation)
            throws ServiceException, InvalidArgumentException, NotFoundException;
    
    /** list services that support an application 
     * @param applicationId application to search servers for.
     * @return list of registry summaries of cea servers that support this application
     * @throws ServiceException if fail to communicate with server
     * @throws NotFoundException if this application cannot be found
     * @throws InvalidArgumentException if the appication id is malformed in some way.
     * @throws ParserCo, nfigurationExcepti
     * @throws TransformerExceptiononTransformerException*/
    ResourceInformation[] listProvidersOf(URI applicationId) throws ServiceException, NotFoundException, InvalidArgumentException;
     
    
    /** execute application 
     * @param document tool document to execute
     * @return  a new unique execution id 
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no provider of this application is found
     * @throws InvalidArgumentException if the tool document is invalid in some way*/
    URI submit(Document document) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
    
    /** execute application on named cea server 
     * @param document tool document to execute
     * @param server cea server to execute on
     * @return  a new unique execution id 
     * @throws InvalidArgumentException if the tool document is malformed, or the server is inacessible.
     * @throws ServiceException  if an error occurs communicating with the server
     * @throws SecurityException if user is prevented from executing this application.
     * */
    URI submitTo(Document document, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException;

    /** variant of {@link #submit} where tool document is referenced by URL 
     * @throws InvalidArgumentException if the tool document is inacessible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no provider of this application is found
     */
     URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException ;

    /** variant of {@link #submitTo} where tool document is referenced by URL 
     * @throws InvalidArgumentException if the tool document is inacessible or ther service is inacesssible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * 
     * */
    URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException ;
        
    /** abort execution of an application 
     * @param executionId id of execution to abort
     * @throws NotFoundException if this application cannot be found.
     * @throws InvalidArgumentException if the execution id is malformed
     * @throws ServiceException if an error occurs while communicating with server
     * @throws SecurityException if the user is not permitted to cancel this application
     * */
    void cancel(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;
    
    /** get  information about an application execution
     * @param executionId id of application to query 
     * @return summary of this exeuction
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.*/
    ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    
    /** get results of the applicationi exeuction 
     * @param executionid id of application to query 
     * @return results of this execution (name - value pairs)
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.*/
    Map getResults(URI executionid) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;               
    
    
}

/* 
 $Log: Applications.java,v $
 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.7  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.6  2005/06/23 09:08:26  nw
 changes for 1.0.3 release

 Revision 1.5  2005/06/08 14:51:59  clq2
 1111

 Revision 1.2.8.5  2005/06/02 14:34:33  nw
 first release of application launcher

 Revision 1.2.8.4  2005/05/12 15:49:21  nw
 litte lix

 Revision 1.2.8.3  2005/05/12 12:42:48  nw
 finished core applications functionality.

 Revision 1.2.8.2  2005/05/12 01:22:38  nw
 javadoc

 Revision 1.2.8.1  2005/05/12 01:14:33  nw
 got applications component half working.

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */