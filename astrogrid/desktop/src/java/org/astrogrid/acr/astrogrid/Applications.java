/*$Id: Applications.java,v 1.6 2005/06/23 09:08:26 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/** Interface to the CEA system
 * @todo decide whether to use ivorns, or string names,
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Mar-2005
 *
 */
public interface Applications {
    /** list names of applications 
     * @return a list of the names of available applications
     * @throws WorkflowInterfaceException*/
    String[] list() throws WorkflowInterfaceException;
    
    /** list summaries of applications 
     * @return list details of the available applications
     * @throws WorkflowInterfaceException*/
    ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException;

    /** get the application descrption for a named application */
    public ApplicationDescription getApplicationDescription(String name) throws WorkflowInterfaceException ;
    /** get the description for an application description */
    public String getInfoFor(ApplicationDescription descr) ;
        
    
   /** access formatted iniformation about an application 
 * @param applicationName
 * @return formatted, human-readable information about the application
 * @throws WorkflowInterfaceException*/
    String getInfo(Ivorn applicationName) throws WorkflowInterfaceException;
    
   /** Create a template tool document, suitable for calling this application 
 * @param applicationName the application to create the template for
 * @param interfaceName interface of this application to create a template from.
 * @return a tool document.
 * @throws WorkflowInterfaceException*/
    Tool getToolTemplate(Ivorn applicationName, String interfaceName)
            throws WorkflowInterfaceException;
    
    public Hashtable convertToolToStruct(Tool document) ;
    public Tool convertStructToTool(Map struct) ;
    
  /**Validate a tool document against an application 
                description 
 * @param document tool document to validate
 * @return true if valid, else false, or an exception.
 * @throws WorkflowInterfaceException
 * @throws MarshalException
 * @throws ValidationException
 * @throws ToolValidationException*/
    boolean validateTool(Tool document) throws WorkflowInterfaceException,
            MarshalException, ValidationException, ToolValidationException;

    /** Validate a tool document (referenced by url) against 
                an application description 
     * @param applicationName name of the application to validate against.
     * @param toolDocumentURL location of a resource containing the tool document to validate
     * @return true if valid, else false or exception.
     * @throws WorkflowInterfaceException
     * @throws MarshalException
     * @throws ValidationException
     * @throws ToolValidationException
     * @throws IOException*/
    boolean validateToolFile(URL toolDocumentURL)
            throws WorkflowInterfaceException, MarshalException, ValidationException,
            ToolValidationException, IOException;
    
    /** list services that support an application 
     * @param applicatonName application to search servers for.
     * @return list of registry summaries of cea servers that support this application
     * @throws WorkflowInterfaceException
     * @throws RegistryException
     * @throws TransformerException
     * @throws ParserCo, nfigurationExcepti
     * @throws TransformerExceptiononTransformerException*/
    ResourceData[] listProvidersOf(Ivorn applicatonName) throws WorkflowInterfaceException, RegistryException, ParserConfigurationException, TransformerException;
     
    
    /** execute application on arbitrary cea server 
     * @param document tool document to execute
     * @return  a new unique execution id 
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws RegistryException
     * @throws CEADelegateException*/
    String execute(Tool document) throws URISyntaxException, RegistryException, ParserConfigurationException, TransformerException, CEADelegateException;
    
    /** execute application on named cea server 
     * @param document tool document to execute
     * @param server cea server to execute on
     * @return  a new unique execution id 
     * @throws Tr, ansformerExceptionURISyntaxException
     * @throws ParserConfigurationException
     * @throws CEADelegateException
     * @thro
     * @throws URISyntaxExceptionws RegistryException*/
    String executeOn(Tool document, Ivorn server) throws RegistryException, ParserConfigurationException, TransformerException,URISyntaxException, CEADelegateException;

    /** variant of {@link #execute} where tool document is referenced by URL */
    public String executeFile(URL toolFile) throws IOException, MarshalException, ValidationException, URISyntaxException, RegistryException, ParserConfigurationException, TransformerException, CEADelegateException ;

    /** variant of {@link #executeOn} where tool document is referenced by URL */
    public String executeOnFile(URL toolFile, Ivorn server) throws IOException, MarshalException, ValidationException, RegistryException, ParserConfigurationException, TransformerException, URISyntaxException, CEADelegateException ;
        
    /** abort execution of an application 
     * @param executionId id of execution to abort
     * @return true if abort is successful. 
     * @throws URISyntaxExceptionCEADelegateException
   
     * @throws CEADelegateExceptionws RegistryException, */
    boolean abort(String executionId) throws RegistryException, URISyntaxException,CEADelegateException;
    
    /** get summary of this application exeuction 
     * @param executionId id of application to query 
     * @return summary of this exeuction*/
    ExecutionSummaryType getExecutionSummary(String executionId) throws RegistryException, URISyntaxException,CEADelegateException;
    
    /** get results of the applicationi exeuction 
     * @param executionid id of application to query 
     * @return results of this execution*/
    ParameterValue[] getResults(String executionid) throws RegistryException, URISyntaxException,CEADelegateException;
    
    /** query progress of application execution 
     * @param executionId id of application to query 
     * @return progress information about this execution - a String 'Running', 'Pending', 'Completed', etc. */
    String checkExecutionProgress(String executionId) throws RegistryException, URISyntaxException,CEADelegateException;
    
    
    
    
    
    
    
    
}

/* 
 $Log: Applications.java,v $
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