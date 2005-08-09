/*$Id: ApplicationsImpl.java,v 1.10 2005/08/09 17:33:07 nw Exp $
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


import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ApplicationsInternal;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
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
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/** Application service.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 * @todo later - remove use of applicationRegistry where possible - go direct - use xquery to get just the data needed.
 * @todo later - add stream-returning method (@link #read} to myspaceInternal.. use consisstently throughout.
 * @todo refine exception reporting.
 */
public class ApplicationsImpl implements ApplicationsInternal, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationsImpl.class);


    
    /** Construct a new 
     * 
     */
    public ApplicationsImpl(CommunityInternal community, Myspace vos, Registry reg){
        this.community = community;
        this.vos = vos;
        this.reg = reg;
        community.addUserLoginListener(this);
    }
    protected final Myspace vos;
    protected final CommunityInternal community;
    protected final Registry reg;
    protected ApplicationRegistry appReg;
    

    private CommonExecutionConnectorClient createCEADelegate(URL endpoint) {
        return community.getEnv().getAstrogrid().createCeaClient(endpoint.toString());
    }
    
    private ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        if (appReg == null) {
         appReg = community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
        }                        
        return appReg;
    }
   
    public URI[] list() throws ServiceException {   
        try {
        String[] arr =  getAppReg().listApplications();
        URI[] result = new URI[arr.length];
        for (int i= 0;i < arr.length; i++) {
            result[i] = new URI(arr[i]);
        }
        return result;
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        }              
    }

    public ApplicationInformation[] listFully() throws ServiceException {
        try {
            ApplicationDescriptionSummary[] arr =  getAppReg().listUIApplications();
            ApplicationInformation[] result = new ApplicationInformation[arr.length];
            for (int i= 0;i < arr.length; i++) {
                result[i] = new ApplicationInformation(
                        new URI(arr[i].getName())
                        , arr[i].getUIName()
                        ,arr[i].getInterfaceNames()
                        );
            }
            return result;
            } catch (WorkflowInterfaceException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new ServiceException(e);
            }           
    }
    
    public ApplicationDescription getApplicationDescription(String name) throws WorkflowInterfaceException {
        return getAppReg().getDescriptionFor(name);
    }
    
    //@todo improve this - very inefficient, but will do for now.
    public ApplicationInformation getApplicationInformation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException{
        try {
            ApplicationDescriptionSummary[] arr =  getAppReg().listUIApplications();
            for (int i= 0;i < arr.length; i++) {
                if (arr[i].getName().equals(applicationName.toString())) {
                    return new ApplicationInformation(
                        new URI(arr[i].getName())
                        , arr[i].getUIName()
                        ,arr[i].getInterfaceNames()
                        );
                }
            }            
            } catch (WorkflowInterfaceException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new ServiceException(e);
            }
            throw new NotFoundException(applicationName.toString());
    }
    
    
    
  public String getDocumentation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException {
      try {
        ApplicationDescription descr = getAppReg().getDescriptionFor(munge(applicationName));
        logger.debug("Created application description");
        return getInfoFor(descr);
      } catch (WorkflowInterfaceException e) {
          throw new ServiceException(e);
      }
  }
  
  //@todo improve later - use xpath?
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
  private String munge(URI application) {     
      return application.toString().substring(6);// drops ivo:// prefix
  }
  
  
  
    public Document createTemplateDocument(URI applicationName, String interfaceName) throws ServiceException, NotFoundException, InvalidArgumentException {
        try {
        ApplicationDescription descr = getAppReg().getDescriptionFor(munge(applicationName));
        Tool t;
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
             t = descr.createToolFromInterface(iface);
        } else {
            t = descr.createToolFromDefaultInterface();
        }
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        return doc;
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (MarshalException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
    
    public Map createTemplateStruct(URI applicationName, String interfaceName)
    throws ServiceException, NotFoundException, InvalidArgumentException {
        Document t = createTemplateDocument(applicationName,interfaceName);
        return convertDocumentToStruct(t);
    }
    
    public Map convertDocumentToStruct(Document doc) throws InvalidArgumentException {
        
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);    
        Hashtable result = new Hashtable();
        result.put("interface",document.getInterface());
        result.put("name",document.getName());
        Hashtable inputs= new Hashtable();
        Hashtable outputs = new Hashtable();
        result.put("input",inputs);
        for (int i = 0; i < document.getInput().getParameterCount(); i++) {
            convertParameterToHash(inputs, document.getInput().getParameter(i));
        }
        result.put("output",outputs);
        for (int i = 0; i < document.getOutput().getParameterCount(); i++) {
            convertParameterToHash(outputs, document.getOutput().getParameter(i));
        }        
        return result;
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }        
    }
    
    private void convertParameterToHash(Hashtable collection,ParameterValue v) {
        Hashtable result = new Hashtable();
        collection.put(v.getName(),result);
        result.put("value",v.getValue());
        result.put("indirect",Boolean.valueOf( v.getIndirect()));
    }
    
    private ParameterValue convertHashToParameter(String name,Map h) {
        ParameterValue v= new ParameterValue();
        v.setName(name);
        v.setIndirect(((Boolean)h.get("indirect")).booleanValue());
        v.setValue(h.get("value").toString());
        return v;
    }

    private ParameterValue[] convertParams(Map inputHash) {
        ParameterValue[] arr = new ParameterValue[inputHash.size()];
        Iterator i = inputHash.entrySet().iterator();
        for (int ix = 0; ix < arr.length; ix++) {
            Map.Entry e =(Map.Entry) i.next();
            arr[ix] =convertHashToParameter(e.getKey().toString(),(Map)e.getValue());                        
        }
        return arr;
    }
    
    public Document convertStructToDocument(Map struct) throws InvalidArgumentException {
        Tool t = new Tool();
        t.setName(struct.get("name").toString());
        t.setInterface(struct.get("interface").toString());
        org.astrogrid.workflow.beans.v1.Input input = new org.astrogrid.workflow.beans.v1.Input();
        
        Map paramHash= (Map)struct.get("input");
        input.setParameter(convertParams(paramHash));
        t.setInput(input);
        
        Output output = new Output();
        paramHash = (Map)struct.get("output");               
       output.setParameter(convertParams(paramHash));
        t.setOutput(output);

        try {
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(t,doc);
            return doc;
        } catch (ParserConfigurationException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) { 
            throw new InvalidArgumentException(e);
        }
    }

    public void validate(Document document) throws ServiceException, InvalidArgumentException {
        try {
        Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,document);
        ApplicationDescription descr = getAppReg().getDescriptionFor(t.getName());
        descr.validate(t);
        } catch (ToolValidationException e) {
            throw new InvalidArgumentException(e);
        } catch (WorkflowInterfaceException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
    }
    
  
    public void validateStored(URI documentLocation) throws ServiceException, InvalidArgumentException, NotFoundException {
        Document doc;
        try {
        if (documentLocation.getScheme().equals("uri")) {
            String s = vos.read(documentLocation);
            InputStream r = new ByteArrayInputStream(s.getBytes());
            doc = XMLUtils.newDocument(r);
        } else {
            InputStream is = documentLocation.toURL().openStream();
            doc = XMLUtils.newDocument(is);
        }
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (SAXException e) {
            throw new InvalidArgumentException(e);
        } catch (IOException e) {
            throw new NotFoundException(e);
        } catch (InvalidArgumentException e) {
            throw new NotFoundException(e);
        } catch (ServiceException e) {
            throw new NotFoundException(e);
        } catch (SecurityException e) {
            throw new NotFoundException(e);
        }
        validate(doc);
        

    }
    /*
     * XPath parsing cribbed from http://www.cafeconleche.org/books/xmljava/chapters/ch16s05.html
     * 
     */
    public ResourceInformation[] listProvidersOf(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException {
        logger.debug("in listProviders");
        
           reg.getRecord(applicationName); // verify the application exists.

       logger.debug("Verified applicaion exists");
       // what about namespaces??
        String query = "Select * from Registry where @status = 'active' and cea:ManagedApplications/cea:ApplicationReference='"
            + applicationName + "'";
        try {
        Document results = reg.searchForRecords(query);
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
        ResourceInformation[] resourceDatas= new ResourceInformation[resources.getLength()];
        for (int i = 0; i < resourceDatas.length; i++) {
            Element resource = (Element)resources.item(i);
            URI id = null;
            URL url = null;
            try {
                id = new URI(XPathAPI.eval(resource,"vr:identifier",namespaceNode).str());
            } catch (URISyntaxException e1) {
                logger.warn("URISyntaxException",e1);
            } 
            String title = XPathAPI.eval(resource,"vr:title",namespaceNode).str();
            try {
                String str = XPathAPI.eval(resource,"vr:interface/vr:accessURL",namespaceNode).str().trim();
                logger.debug("URL:" + str);
                url = new URL(str);
            } catch (MalformedURLException e2) {
                logger.warn("MalformedURLException",e2);
            } 
            String description = XPathAPI.eval(resource,"vr:content/vr:description",namespaceNode).str();
            resourceDatas[i] = new ResourceInformation(
                    id
                    ,title
                    ,description
                    ,url
                    );
        }
        return resourceDatas;
        } catch (TransformerException e) {
            throw new ServiceException(e);
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        }
    }
    
    private InputStream read(URI loc) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        URL url ;
        try {
        if (loc.getScheme() == null ||  loc.getScheme().equals("ivo")) {
            url = vos.getReadContentURL(loc);
        } else {
       
                url = loc.toURL()          ;
        }
        return url.openStream();
            } catch (MalformedURLException e) {
                throw new InvalidArgumentException(e);
            } catch (IOException e) {
                throw new NotFoundException(e);
            }             
    }
    
    public URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException {
        InputStream r = read(documentLocation);
        try {
        Document doc = XMLUtils.newDocument(r);
        return submit(doc);
        } catch (IOException e) {
            throw new NotFoundException(e);
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (SAXException e) {
            throw new InvalidArgumentException(e);
        }
    }
    
   

    
    public URI submitStoredTo(URI documentLocation, URI server) throws InvalidArgumentException, ServiceException, SecurityException, NotFoundException {
        InputStream r = read(documentLocation);
        try {
        Document doc = XMLUtils.newDocument(r);
        return submitTo(doc,server);
        } catch (IOException e) {
            throw new NotFoundException(e);
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (SAXException e) {
            throw new InvalidArgumentException(e);
        }
    } 
    

    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        // munge name in document, if incorrect..       
        URI application; // urg.
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            application = new URI("ivo://" + document.getName());
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        logger.debug(application.toString());
        logger.debug(document.getName());
        
        ResourceInformation[] arr = listProvidersOf(application);
        if (arr.length == 0) {
            throw new IllegalArgumentException(application +" has no registered providers");
        }
        List l =  Arrays.asList(arr);
        Collections.shuffle(l);
        ResourceInformation target = (ResourceInformation)l.get(0);
        return doExecute(application, document, target);
    }
    
    public URI submitTo(Document doc, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException {
        // munge name in document, if incorrect..       
        URI application; // urg.
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            application = new URI("ivo://" + document.getName());
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        logger.debug(application.toString());
        logger.debug(document.getName());
        ResourceInformation[] arr = listProvidersOf(application);
        ResourceInformation target = null;
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i].getId().equals(server)) { 
                target = arr[i];
                break;
            }
        }
        if (target == null) {
            throw new InvalidArgumentException(server + " does not provide application " + application);            
        }
        return doExecute(application, document, target);
        
    }   
    // primitive implementation method.
    private URI doExecute(URI application, Tool document, ResourceInformation server) throws ServiceException  {
        CommonExecutionConnectorClient delegate = createCEADelegate(server.getAccessURL());
        // fudge some kind of job id type. hope this will do.
        try {
        JobIdentifierType jid = new JobIdentifierType(community.getEnv().getUserIvorn().toString() + application.toString());
        String primId = delegate.init(document,jid);
        if (!delegate.execute(primId)) {
            throw new CEADelegateException("Failed to start application, for unknown reason");
        }
        return new URI(server.getId().getScheme(),server.getId().getSchemeSpecificPart(),primId);
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        }  
        
    }
    
    private URI getService(URI executionId) throws InvalidArgumentException  {
        try {
            /*
            int pos = executionId.indexOf('#');
            return new Ivorn(executionId.substring(0,pos));
            */
            //@todo check null is ok here.
            return new URI(executionId.getScheme(),executionId.getSchemeSpecificPart(),null);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        }
       
    }
    
    private String getId(URI executionId) {
        return executionId.getFragment();
        /*
        int pos = executionId.indexOf('#');
        return executionId.substring(pos+1);*/
    }
    
 
    public void cancel(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        try {
            delegate.abort(getId(executionId));
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        }
    }  
    
    public ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException {
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        try {
        ExecutionSummaryType ex =  delegate.getExecutionSumary(getId(executionId));
        //@todo see if we can improve information retirned.
        return new ExecutionInformation(
                executionId
                ,ex.getApplicationName()
                ,"Single Application"
                ,ex.getStatus().toString()
                ,null
                ,null
                );
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        }        
    }
    public Map getResults(URI executionId) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);
        try {
        ResultListType results =  delegate.getResults(getId(executionId));
        Map map = new HashMap();
        ParameterValue[] vals = results.getResult();        
        for (int i = 0; i < vals.length; i++) {
            map.put(vals[i].getName(),vals[i].getValue());
        }
        return map;
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        }          
    }
   
    public void userLogin(UserLoginEvent e) {
    }

    public void userLogout(UserLoginEvent e) {
        appReg = null;
    }
       
    
}


/* 
$Log: ApplicationsImpl.java,v $
Revision 1.10  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.9  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.8  2005/06/23 09:08:26  nw
changes for 1.0.3 release

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