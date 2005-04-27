/*$Id: ApplicationsImpl.java,v 1.3 2005/04/27 13:42:40 clq2 Exp $
 * Created on 31-Jan-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/** Application service.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 */
public class ApplicationsImpl implements Applications, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationsImpl.class);


    
    /** Construct a new 
     * 
     */
    public ApplicationsImpl(Community community){
        this.community = community;
        community.addUserLoginListener(this);
    }
    protected final Community community;
    protected ApplicationRegistry reg;


    public String[] list() throws WorkflowInterfaceException {    
        return getAppReg().listApplications();
      
    }


    private ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        if (reg == null) {
         reg = community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
        } 
        return reg;
    }


    public ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException {
        return getAppReg().listUIApplications();
    }
    

    public String getRegEntry(String applicationName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        return XMLUtils.ElementToString(descr.getOriginalVODescription());
    }

    public String getInfo(String applicationName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        StringBuffer result = new StringBuffer();
        String vr = "http://www.ivoa.net/xml/VOResource/v0.9";
         result.append("Application: ")
                .append(descr.getName())
                .append("\n")
                //@todo doesn't work with v10. fix.
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
    

    public boolean validateTool(String applicationName,Tool document) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        descr.validate(document);
        return true;

    }
    
  
    public boolean validateToolFile(String applicationName,URL toolDocumentURL) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException, IOException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(applicationName);
        Reader r = new InputStreamReader(toolDocumentURL.openStream());
        Tool tool = Tool.unmarshalTool(r);
        descr.validate(tool);
        return true;
    }


    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }


    /** remove registry on user logout.
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        reg = null;
    }
    


    
    
}


/* 
$Log: ApplicationsImpl.java,v $
Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.2  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2.2.1  2005/04/22 10:54:36  nw
added missing methods to vospace.
made a start at getting applications working again.

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/