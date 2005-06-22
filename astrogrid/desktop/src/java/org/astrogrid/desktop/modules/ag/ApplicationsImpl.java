/*$Id: ApplicationsImpl.java,v 1.7 2005/06/22 08:48:52 nw Exp $
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
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.RegistrySearchException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.scripting.XMLHelper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
    protected ApplicationRegistry appReg;

    private CommonExecutionConnectorClient createCEADelegate(ResourceData rd) {
        return community.getEnv().getAstrogrid().createCeaClient(rd.getAccessURL().toString());
    }
    private CommonExecutionConnectorClient createCEADelegate(String endpoint) {
        return community.getEnv().getAstrogrid().createCeaClient(endpoint);
    }
    
    private ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        if (appReg == null) {
         appReg = community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
        }                        
        return appReg;
    }
    private RegistryService reg;
    private RegistryService getReg() {
        if (reg == null) {
            reg = community.getEnv().getAstrogrid().createRegistryClient();
        }
        return reg;
    }
    
    public String[] list() throws WorkflowInterfaceException {    
        return getAppReg().listApplications();
      
    }

    public ApplicationDescriptionSummary[] fullList() throws WorkflowInterfaceException {
        return getAppReg().listUIApplications();
    }
    
    public ApplicationDescription getApplicationDescription(String name) throws WorkflowInterfaceException {
        return getAppReg().getDescriptionFor(name);
    }
    
    
  public String getInfo(Ivorn applicationName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(munge(applicationName));
        logger.debug("Created application description");
        return getInfoFor(descr);
  }
  
  public String getInfoFor(ApplicationDescription descr) {      
        StringBuffer result = new StringBuffer();
        String vr = "http://www.ivoa.net/xml/VOResource/v0.10";
         result.append("Application: ")
                .append(descr.getName())
                .append("\n")
                .append(
                        descr.getOriginalVODescription().getElementsByTagNameNS(vr,"description").item(0).getFirstChild().getNodeValue())
                .append("\n");
         logger.debug("Added name and description");
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
         logger.debug("Added parameters");
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
           logger.debug("Completed");
         return result.toString();
    }    
   
  // alter to impedance-match between whether ivo:// is expected or now.
  // at moment, necessary to do this.
  private String munge(Ivorn application) {
      return application.toString().substring(6);// drops ivo:// prefix
  }
  
    public Tool getToolTemplate(Ivorn applicationName, String interfaceName) throws WorkflowInterfaceException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(munge(applicationName));
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
    

    public boolean validateTool(Tool document) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException {
        ApplicationDescription descr = getAppReg().getDescriptionFor(document.getName());
        descr.validate(document);
        return true;

    }
    
  
    public boolean validateToolFile(URL toolDocumentURL) throws WorkflowInterfaceException, MarshalException, ValidationException, ToolValidationException, IOException {
        Reader r = new InputStreamReader(toolDocumentURL.openStream());
        Tool tool = Tool.unmarshalTool(r);
        return validateTool(tool);

    }
    /*
     * XPath parsing cribbed from http://www.cafeconleche.org/books/xmljava/chapters/ch16s05.html
     * 
     */
    public ResourceData[] listProvidersOf(Ivorn applicationName) throws RegistryException, ParserConfigurationException, TransformerException {
        logger.debug("in listProviders");
       try { // verify application exists
           getReg().getResourceByIdentifier(applicationName);
       } catch (RegistryException e) {
           throw new RegistrySearchException("Application " + applicationName +" is not in the registry");
       }
       logger.debug("Verified applicaion exists");
       // what about namespaces??
        String query = "Select * from Registry where @status = 'active' and cea:ManagedApplications/cea:ApplicationReference='"
            + applicationName + "'";
        Document results = getReg().searchFromSADQL(query);
        logger.debug("got  results");       
        
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(true);
        DocumentBuilder builder = fac.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();        
       Document namespaceHolder = impl.createDocument("http://www.ivoa.net/xml/RegistryInterface/v0.1", "f:namespaceMapping",null);
      
       // Document namespaceHolder = DomHelper.newDocument();
        Element namespaceNode = namespaceHolder.getDocumentElement();
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vr","http://www.ivoa.net/xml/VOResource/v0.10");
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns","http://www.ivoa.net/xml/VOResource/v0.10");
        namespaceNode.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vor", "http://www.ivoa.net/xml/RegistryInterface/v0.1");
        NodeList resources = XPathAPI.selectNodeList(results,"//vor:Resource",namespaceNode);
        logger.debug("Found " + resources.getLength());
        ResourceData[] resourceDatas= new ResourceData[resources.getLength()];
        for (int i = 0; i < resourceDatas.length; i++) {
            ResourceData rd = new ResourceData();
            Element resource = (Element)resources.item(i);

            try {
                rd.setIvorn(new Ivorn(XPathAPI.eval(resource,"vr:identifier",namespaceNode).str()));
                System.err.println(rd.getIvorn());
            } catch (URISyntaxException e1) {
                logger.warn("URISyntaxException",e1);
            } 
            rd.setTitle(XPathAPI.eval(resource,"vr:title",namespaceNode).str());
            try {
                String str = XPathAPI.eval(resource,"vr:interface/vr:accessURL",namespaceNode).str().trim();
                logger.debug("URL:" + str);
                rd.setAccessURL(  new URL(str));
            } catch (MalformedURLException e2) {
                logger.warn("MalformedURLException",e2);
            } 
            rd.setDescription( XPathAPI.eval(resource,"vr:content/vr:description",namespaceNode).str());
            resourceDatas[i] = rd;
        }
        return resourceDatas;

    }
    
    
    public String executeFile(URL toolFile) throws IOException, MarshalException, ValidationException, URISyntaxException, RegistryException, ParserConfigurationException, TransformerException, CEADelegateException {
        Reader r = new InputStreamReader(toolFile.openStream());
        Tool tool = Tool.unmarshalTool(r);        
        return execute(tool);
    }
    
   

    
    public String executeOnFile(URL toolFile, Ivorn server) throws IOException, MarshalException, ValidationException, RegistryException, ParserConfigurationException, TransformerException, URISyntaxException, CEADelegateException {
        Reader r = new InputStreamReader(toolFile.openStream());
        Tool tool = Tool.unmarshalTool(r);
        return executeOn(tool,server);
    } 
    

    public String execute(Tool document) throws URISyntaxException, RegistryException, ParserConfigurationException, TransformerException, CEADelegateException {
        // munge name in document, if incorrect..
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
        Ivorn application = new Ivorn("ivo://" + document.getName()); // urg.
        logger.debug(application.toString());
        logger.debug(document.getName());
        
        ResourceData[] arr = listProvidersOf(application);
        if (arr.length == 0) {
            throw new IllegalArgumentException(application +" has no registered providers");
        }
        List l =  Arrays.asList(arr);
        Collections.shuffle(l);
        ResourceData target = (ResourceData)l.get(0);
        return doExecute(application, document, target);
    }
    
    public String executeOn(Tool document, Ivorn server) throws RegistryException, ParserConfigurationException, TransformerException, URISyntaxException, CEADelegateException {
        // munge name in document, if incorrect..
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }        
        Ivorn application = new Ivorn("ivo://" + document.getName()); // gah
        ResourceData[] arr = listProvidersOf(application);
        ResourceData target = null;
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i].getIvorn().toString().equals(server.toString())) { // ivorn doesn't seem t define equals.
                target = arr[i];
                break;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException(server + " does not provide application " + application);            
        }
        return doExecute(application, document, target);
        
    }   
    // primitive implementation method.
    private String doExecute(Ivorn application, Tool document, ResourceData server) throws CEADelegateException {
        CommonExecutionConnectorClient delegate = createCEADelegate(server);
        // fudge some kind of job id type. hope this will do.
        JobIdentifierType jid = new JobIdentifierType(community.getEnv().getUserIvorn().toString() + application.toString());
        String primId = delegate.init(document,jid);
        if (!delegate.execute(primId)) {
            throw new CEADelegateException("Failed to start application, for unknown reason");
        }
        return server.getIvorn().toString() + "#" + primId;
        
    }
    
    private Ivorn getService(String executionId) throws URISyntaxException {
        int pos = executionId.indexOf('#');
        return new Ivorn(executionId.substring(0,pos));
    }
    
    private String getId(String executionId) {
        int pos = executionId.indexOf('#');
        return executionId.substring(pos+1);
    }
    
 
    public boolean abort(String executionId) throws RegistryException, URISyntaxException, CEADelegateException {
        String endpoint = getReg().getEndPointByIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        return delegate.abort(getId(executionId));
    }  
    public ExecutionSummaryType getExecutionSummary(String executionId) throws RegistryException, URISyntaxException, CEADelegateException {
        String endpoint = getReg().getEndPointByIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        return delegate.getExecutionSumary(getId(executionId));
        
    }
    public ParameterValue[] getResults(String executionId) throws RegistryException, URISyntaxException, CEADelegateException {
        String endpoint = getReg().getEndPointByIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        ResultListType results =  delegate.getResults(getId(executionId));
        return results.getResult();
    }
    public String checkExecutionProgress(String executionId) throws CEADelegateException, RegistryException, URISyntaxException {
        String endpoint = getReg().getEndPointByIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        /** returns wrong type when erred - so try something different for now.. 
        MessageType message =  delegate.queryExecutionStatus(getId(executionId));       
        return message.getPhase();
        */
        ExecutionSummaryType s = delegate.getExecutionSumary(getId(executionId));
        return s.getStatus().toString();
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
        appReg = null;
        reg = null;
    }
       
    
}


/* 
$Log: ApplicationsImpl.java,v $
Revision 1.7  2005/06/22 08:48:52  nw
latest changes - for 1.0.3-beta-1

Revision 1.6  2005/06/08 14:51:59  clq2
1111

Revision 1.3.8.4  2005/06/02 14:34:32  nw
first release of application launcher

Revision 1.3.8.3  2005/05/12 15:49:22  nw
litte lix

Revision 1.3.8.2  2005/05/12 12:42:48  nw
finished core applications functionality.

Revision 1.3.8.1  2005/05/12 01:14:33  nw
got applications component half working.

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