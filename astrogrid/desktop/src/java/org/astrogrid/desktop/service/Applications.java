/*$Id: Applications.java,v 1.2 2005/02/22 01:10:31 nw Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.desktop.service.conversion.CastorBeanUtilsConvertor;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/** Application service.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 *@@ServiceDoc("applications","query the application registry")
 *@todo add way to invoke an application - so query for servers for an application,
 *and dispatch tool document to a server.
 */
public class Applications {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Applications.class);

    // register parameter convertors.
    static {
        ConvertUtils.register(CastorBeanUtilsConvertor.getInstance(),Tool.class);
        ConvertUtils.register(new Converter() {

            public Object convert(Class arg0, Object arg1) {
                if ( arg0 != URL.class) {
                    throw new RuntimeException("Can only convert to URLs " + arg0.getName());
                }                
                try {
                    return new URL(arg1.toString());
                } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                }                
            }
        },URL.class);
    }
    
    /** Construct a new Jes
     * 
     */
    public Applications(Community community){
        this.community = community;
    }
    protected final Community community;

    /** @throws WorkflowInterfaceException
     * @@MethodDoc("list","list names of registered applications")
     * @@.return ReturnDoc("List of application names",rts = ArrayResultTransformerSet.getInstance())
     */
    public String[] list() throws WorkflowInterfaceException {    

        return getAppReg().listApplications();
      
    }

    /**         note that we can't hand on to a reference to astrogrid - even though it'd make things much easier,
        as this reference changes with the user logs in or out.
     * @return
     * @throws WorkflowInterfaceException
     */
    private ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        return community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
    }

    /** @throws WorkflowInterfaceException
     * @@MethodDoc("fullList","list info of each  registered applications")
     * @@.return ReturnDoc("List of application names",rts = ArrayResultTransformerSet.getInstance())
     */
    public ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException {       
        return getAppReg().listUIApplications();
       
    }
    
    /** @@MethodDoc("getRegEntry","Return full registry entry for an application")
     * @@.return ReturnDoc("XML of registry entry",rts = XmlDocumentResultTransformerSet.getInstance())
     * @@.applicationName ParamDoc("applicationName","registry key of the application to query for");
     * @param applicationName
     * @throws WorkflowInterfaceException
     */
    public String getRegEntry(String applicationName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        return XMLUtils.ElementToString(descr.getOriginalVODescription());
    }
    
    /** @@MethodDoc("getInfo","Return information for an application")
     * @@.return ReturnDoc("Description of an application")
     * @@.applicationName ParamDoc("applicationName","registry key of the application to query for");
     * @param applicationName
     * @throws WorkflowInterfaceException
     */
    public String getInfo(String applicationName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        StringBuffer result = new StringBuffer();
        String vr = "http://www.ivoa.net/xml/VOResource/v0.9";
         result.append("Application: ")
                .append(descr.getName())
                .append("\n")
                .append(
                        descr.getOriginalVODescription().getElementsByTagNameNS(vr,"Description").item(0).getFirstChild().getNodeValue())
                .append("\n");
         BaseParameterDefinition[] params = descr.getParameters().getParameter();
         for(int i = 0; i < params.length; i++) {
            BaseParameterDefinition param = params[i];
            result.append("\nParameter ")
                .append("\n")
                .append(param.getUI_Name())
                .append("\n")
                .append(param.getUI_Description().getContent());

         
         if (param.getName() != null && param.getName().trim().length() > 0) result.append("\n\t").append("name :").append(param.getName());
         if (param.getType() != null ) result.append("\n\t").append("type :").append(param.getType());
         if (param.getSubType() != null && param.getSubType().trim().length() > 0) result.append("\n\t").append("subtype :").append(param.getSubType());
         if (param.getUnits() != null && param.getUnits().trim().length() > 0) result.append("\n\t").append("units :").append(param.getUnits());
         if (param.getAcceptEncodings() != null && param.getAcceptEncodings().trim().length() > 0) result.append("\n\t").append("accept encodings :").append(param.getAcceptEncodings());
         if (param.getDefaultValue() != null && param.getDefaultValue().trim().length() > 0) result.append("\n\t").append("default value :").append(param.getDefaultValue());
         if (param.getUCD() != null && param.getUCD().trim().length() > 0) result.append("\n\t").append("UCD :").append(param.getUCD());  
         if (param.getOptionList() != null ) result.append("\n\t").append("option list :").append(param.getOptionList());         

         }
           Interface[] ifaces = descr.getInterfaces().get_interface();
           for (int i = 0; i < ifaces.length; i++) {
               Interface iface = ifaces[i];
               result.append("\nInterface ")
                   .append(iface.getName())
                   .append("\nInputs\n");
               ParameterRef[] prefs = iface.getInput().getPref();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterRef p = prefs[j];
                   result.append("\t ").append(p.getRef()).append(" max ").append(p.getMaxoccurs()).append(", min ").append(p.getMinoccurs()).append("\n");
               }
               result.append("\nOutputs\n");
               prefs = iface.getOutput().getPref();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterRef p = prefs[j];
                   result.append("\t ").append(p.getRef()).append("max ").append(p.getMaxoccurs()).append(", min ").append(p.getMinoccurs()).append("\n");
               }               
           }

         return result.toString();
    }    
    
    /** @@MethodDoc("getToolTemplate","Return a template tool document for an application")
     * @@.return ReturnDoc("Tool Document object",rts=CastorDocumentResultTransformerSet.getInstance())
     * @@.applicationName ParamDoc("applicationName","name of the application to build template for")
     * @@.interfaceName ParamDoc("interfaceName","name of the interface in the application to build template for - use 'default' for default template")
     * @throws WorkflowInterfaceException
     */
    public Tool getToolTemplate(String applicationName, String interfaceName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        if (interfaceName != null && (! interfaceName.equals("default"))) {
            Interface[] ifaces = descr.getInterfaces().get_interface();
            Interface iface = null;
            for (int i = 0; i < ifaces.length; i++) {
                if (ifaces[i].getName().equals(interfaceName)) {
                    iface =ifaces[i];
                }
            }
            if (iface == null) {
                throw new IllegalArgumentException("Cannot find interface named " + interfaceName + " in application " + applicationName);
            }
            return descr.createToolFromInterface(iface);
        } else {
            return descr.createToolFromDefaultInterface();
        }
        
    }
    
    /** @@MethodDoc("validateTool","Validate a tool document against an application description")
     * @@.return ReturnDoc("true if valid. some kind of exception if invalid (will sort that later",rts=BooleanResultTransformerSet.getInstance())
     * @@.applicationName  ParamDoc("applicationName","name of the application to validate the tool document against.")
     * @@.toolDocument ParamDoc("document","content of the tool document to validate")
     */
    public boolean validateTool(String applicationName,Tool document) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        descr.validate(document);
        return true;
    }
    
    /** @throws IOException
     * @@MethodDoc("validateToolFile","Validate a tool document (referenced by url) against an application description")
     * @@.return ReturnDoc("true if valid. some kind of exception if invalid (will sort that later",rts=BooleanResultTransformerSet.getInstance())
     * @@.applicationName  ParamDoc("applicationName","name of the application to validate the tool document against.")
     * @@.toolDocument ParamDoc("toolDocument","content of the tool document to validate")
     */
    public boolean validateToolFile(String applicationName,URL toolDocumentURL) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException, IOException {        
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        Reader r = new InputStreamReader(toolDocumentURL.openStream());
        Tool tool = Tool.unmarshalTool(r);
        descr.validate(tool);
        return true;
    }

    
    
}


/* 
$Log: Applications.java,v $
Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/