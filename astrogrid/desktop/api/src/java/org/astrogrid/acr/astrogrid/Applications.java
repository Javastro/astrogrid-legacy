/*$Id: Applications.java,v 1.6 2006/02/02 14:19:48 nw Exp $
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

/**  Work with remote applications -  Compute and Data Query services.
 * 
 * 
 * The Common Execution Architecture (CEA) provides a uniform way to describe and execute astronomical applications and data services on the VO.
 * This interface provides methods to
 * <ul>
 * <li>Discover available applications</li>
 * <li>Build invocation documents containing the correct parameters</li>
 * <li>Submit invocation documents for execution on remote servers</li>
 * <li>Monitor progress and retreive results of execution</li>
 * </ul>
 * 
 * Each new application invocation is assigned a new globally unique id.
 *  These id's  should be treated as opaque objects - the internal structure is still liable to change.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/applications/design/CEADesignIVOANote.html">Common Execution Architecture - IVOA Proposal</a>
 * <br/>
* @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html#element_tool">Tool Document Schema-Documentation</a>
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/AGParameterDefinition.html#type_parameter">Value Parameter Element Schema-Documentation</a>
 * @see <a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 * <br/>
 * @see <a href="doc-files/run-app-demo.py">Calling CEA services - example python script</a>
 * @see <a href="doc-files/runAppDemo.groovy">Calling CEA services - example groovy script</a>
 * @see <a href="../dialogs/doc-files/example-tool.xml"> Example Tool Document</a>
 * <br/> 
 * @see org.astrogrid.acr.ui.ApplicationLauncher
 * @see org.astrogrid.acr.astrogrid.ApplicationInformation
 * @see org.astrogrid.acr.dialogs.ToolEditor
 * @see org.astrogrid.acr.astrogrid.ExecutionInformation
 * @service astrogrid.applications
 */
public interface Applications {
    /** list remote applications available in the registry. 
     * @return a list of the registry identifiers of available applications
     * @throws ServiceException if error occurs while talking to server 
     * @see Registry#getRecord(URI)  
     * @see Registry#getResourceInformation(URI)
     * @see #getApplicationInformation(URI)
     * 
     * */
    URI[] list() throws ServiceException;
    


    /** @deprecated - use {@link #getRegistryQuery} */
    String getQueryToListApplications(); 
    
    /** helper method - returns the ADQL query that should be passed to the registry to
     * list all available applications.
     * 
     * can be used as a starting point to build up filters, etc.
     * @return an adql query string.
     * @see Registry
     * @since 1.3
     */ 
    String getRegistryQuery();
       
    /** get information for a specific application from the registry. 
     * @param applicationName name of the application to hunt for
     * @return details of this application
     * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed
     * @xmlrpc returns a structure containing attributes of {@link ApplicationInformation}
     * */
    ApplicationInformation getApplicationInformation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException;
   
    /** get formatted information about an application 
 * @param applicationName
 * @return formatted, human-readable information about the application
     * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed
     * @todo make more configurable.
     * */
    String getDocumentation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException;
    
 
   /** create a template invocation document for a particular application.
    * 
    * Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters. 
 * @param applicationName the application to create the template for
 * @param interfaceName interface of this application to create a template from.
 * @return a tool document. (xml form)
  * @throws ServiceException if error occurs when talking to the sever
     * @throws NotFoundException if this application could not be found
     * @throws InvalidArgumentException if the application name is malformed
     * @xmlrpc will return xml document as a string
     * @see #createTemplateStruct(URI, String)
     * @see #validate(Document)
     * */
    Document createTemplateDocument(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException;
    
    
    /** create a template invocation datastucture for a particular application.
     * 
    * Examines the registry entry for this application, and constructs a template document containing fields for the required input and output parameters. 
    * <br />
    * The datastructure returned is equivalent to the document returned by {@link #createTemplateDocument(URI, String)} - 
     * this is a convenience method for scripting languages with minimal
     * xml abilities. 
  * @param applicationName the application to create the template for
  * @param interfaceName interface of this application to create a template from.
  * @return a tool object (structure)
   * @throws ServiceException if error occurs when talking to the sever
      * @throws NotFoundException if this application could not be found
      * @throws InvalidArgumentException if the application name is malformed
      * @see #convertStructToDocument(Map)
      * @see #validate(Document)
      * */
     Map createTemplateStruct(URI applicationName, String interfaceName)
             throws ServiceException, NotFoundException, InvalidArgumentException;
   
     /** convert a invocation document to a invocation structure. 
      * <br />
      * Translates an invocation document between two equvalent forms - a datastructure and a document
     * @param document a tool document
     * @return the equvalent tool structure
     * @throws InvalidArgumentException if  document is malformed
     * @see #convertStructToDocument(Map)
     * */
   Map convertDocumentToStruct(Document document) throws InvalidArgumentException ;
    
    
    /** convert a invocation structure to the equivalent document. 
     * 
     * @param struct a tool structure
     * @return the equivalent tool document
     * @throws InvalidArgumentException if structure cannot be converted to a document
     * @see #convertDocumentToStruct(Document)
     * */
   Document convertStructToDocument(Map struct) throws InvalidArgumentException ;
    
  /**Validate an invocation document against the  application's description
   * <br />
   * Verifies that all required parameters are present.  
 * @param document tool document to validate
 * @throws ServiceException if fails to communicate with server
 * @throws InvalidArgumentException if document is invalid in some way
 * @see #createTemplateDocument(URI, String)*/
    void validate(Document document) throws ServiceException, InvalidArgumentException;

    /** Validate an invocation document (referenced by url) against 
                an application description 
     * @param applicationName name of the application to validate against.
     * @param documentLocation location of a resource containing the tool document to validate
     * @throws ServiceException if fails to communicate with server.
     * @throws InvalidArgumentException if  document is invalid in some way
     * @throws NotFoundException if document cannot be accessed
     * @see #validate(Document)*/
    void validateStored(URI documentLocation)
            throws ServiceException, InvalidArgumentException, NotFoundException;
    
    /** list the remote servers that provides a particular application.
     * 
     *  (It's possible, for CEA especially, that an application may be provided by multiple servers)
     * @param applicationId registry identifier of the application to search servers for.
     * @return list of registry summaries of cea servers that support this application
     * @throws ServiceException if fail to communicate with server
     * @throws NotFoundException if this application cannot be found
     * @throws InvalidArgumentException if the appication id is malformed in some way.*/
    ResourceInformation[] listProvidersOf(URI applicationId) throws ServiceException, NotFoundException, InvalidArgumentException;
     
    
    /** submit an invocation document for execution..
     * 
     * No particular remote server is specified - the system will select a suitable one.
     * @param document tool document to execute
     * @return  a new unique execution id 
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no provider of this application is found
     * @throws InvalidArgumentException if the tool document is invalid in some way
     * @see #submitStored(URI)
     * @see #submitTo(Document, URI)    
     * */
    URI submit(Document document) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
    
    /** submit an invocation document for execution  on a named remote server.
     *  
     * @param document tool document to execute
     * @param server remote server to execute on
     * @return  a new unique execution id 
     * @throws NotFoundException if the specified remote server could not be found
     * @throws InvalidArgumentException if the invocation document is malformed, or the server is inacessible.
     * @throws ServiceException  if an error occurs communicating with the server
     * @throws SecurityException if user is prevented from executing this application.
     * @see #submitStored(URI)
     * @see #submitStoredTo(URI, URI)
     *      * */
    URI submitTo(Document document, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException;

    /** a variant of {@link #submit} where invocation document is stored somewhere and referenced by URI. 
     * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @return a new unique execution id
     * @throws InvalidArgumentException if the tool document is inacessible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application
     * @throws NotFoundException if no provider of this application is found
     */
     URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException ;

    /** variant of {@link #submitTo} where tool document is referenced by URI. 
     *      * @param documentLocation pointer to tool document - may be file:/, http://, ftp:// or ivo:// (myspace) protocols 
     * @param server remote server to execute on
     * @return a new unique execution id
     * @throws NotFoundException if the specified remote server could not be found
     * @throws InvalidArgumentException if the tool document is inacessible or ther service is inacesssible
     * @throws ServiceException if error occurs communicating with servers
     * @throws SecurityException if user is prevented from executing this application 
     * */
    URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException ;
        
    /** cancel execution of an application.
     * @param executionId id of execution to cancel
     * @throws NotFoundException if this application cannot be found.
     * @throws InvalidArgumentException if the execution id is malformed
     * @throws ServiceException if an error occurs while communicating with server
     * @throws SecurityException if the user is not permitted to cancel this application 
     * */
    void cancel(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException;
    
    /** retrive  information about an application execution.
     * @param executionId id of application to query 
     * @return summary of this execution
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.
     * @xmlrpc will return a struct containing keys documented in {@link ExecutionInformation} 
     * */
    ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException;
    
    /** retreive results of the application execution .
     * @param executionid id of application to query 
     * @return results of this execution (name - value pairs). Note that this will only be the actual results for <b>direct</b> output parameters. For output parameters specified as <b>indirect</b>, the value returned
     * will be the URI pointing to the location where the results are stored.
     * @throws ServiceException if error occurs communicating with the server
     * @throws NotFoundException if this application invocation cannot be found
     * @throws SecurityException if the user cannot access ths invocation
     * @throws InvalidArgumentException if the invocation id is malformed in some way.
     */
    Map getResults(URI executionid) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;               
    
    
}

/* 
 $Log: Applications.java,v $
 Revision 1.6  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.5  2005/11/10 12:13:52  nw
 interface changes for lookout.

 Revision 1.4  2005/10/18 16:51:49  nw
 deprecated a badly-named method

 Revision 1.3  2005/08/25 16:59:44  nw
 1.1-beta-3

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

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