/*$Id: ApplicationsImpl.java,v 1.4 2005/09/12 15:21:16 nw Exp $
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
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.common.bean.BaseBean;
import org.astrogrid.desktop.modules.dialogs.editors.DatacenterToolEditorPanel;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.CachedXPathAPI;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/** Application service.
 * @author Noel Winstanley nw@jb.man.ac.uk 31-Jan-2005
 * * @todo refine exception reporting.
 */
public class ApplicationsImpl implements ApplicationsInternal,UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ApplicationsImpl.class);
    
    /** Construct a new 
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws DOMException
     * 
     */
    public ApplicationsImpl(CommunityInternal community, MyspaceInternal vos, Registry reg, Adql074 adql) throws DOMException, FactoryConfigurationError, ParserConfigurationException{
        this.community = community;
        this.vos = vos;
        this.reg = reg;
        this.adql = adql;
        community.addUserLoginListener(this);
    }
    protected final MyspaceInternal vos;
    protected final CommunityInternal community;
    protected final Registry reg;
    protected final Adql074 adql;
    /** @deprecated */
    protected ApplicationRegistry appReg;
    


    private CommonExecutionConnectorClient createCEADelegate(URL endpoint) {
       return  DelegateFactory.createDelegate(endpoint.toString());       
    }
    
    /** @deprecated really */
    private synchronized ApplicationRegistry getAppReg() throws WorkflowInterfaceException {
        if (appReg == null) {
         appReg = community.getEnv().getAstrogrid().getWorkflowManager().getToolRegistry();
        }                        
        return appReg;
    }
   
    public URI[] list() throws ServiceException {   
        try {
            Document doc = reg.adqlSearch(getQueryToListApplications());
            NodeList l = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");  
            List results = new ArrayList();
            CachedXPathAPI xpath = new CachedXPathAPI();
            Element namespaceNode = XPathHelper.createNamespaceNode();
            for (int i= 0;i < l.getLength(); i++) {
            try {
                Element el = (Element)l.item(i);                
                results.add(new URI(xpath.eval(el,"vr:identifier",namespaceNode).str()));
            } catch (URISyntaxException e) {
                // oh well, skip it.
            }
        }
        return (URI[])results.toArray(new URI[]{});
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }              
    }
    public String getQueryToListApplications() {
        return "Select * from Registry where " +
    " (@xsi:type = 'cea:CeaApplicationType' or " +
    " @xsi:type = 'cea:CeaHttpApplicationType')" +
    " and @status = 'active'";
    }
    
    /** @deprecated */
    public ApplicationDescription getApplicationDescription(URI name) throws WorkflowInterfaceException {
        return getAppReg().getDescriptionFor(name.toString().substring(6));
    }
    
    /**
     * @throws InvalidArgumentException
     * @throws NotFoundException
     * @throws ServiceException
     * @see org.astrogrid.desktop.modules.ag.ApplicationsInternal#getInfoForTool(org.astrogrid.workflow.beans.v1.Tool)
     */
    public ApplicationInformation getInfoForTool(Tool t) throws ServiceException, NotFoundException, InvalidArgumentException {
        URI uri;
        try {
            uri = new URI(t.getName().startsWith("ivo://") ? t.getName() : "ivo://" + t.getName());
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        }
        return getApplicationInformation(uri);
    }
    
    public ApplicationInformation getApplicationInformation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException{
        ResourceInformation ri = reg.getResourceInformation(applicationName);
        if (ri instanceof ApplicationInformation) {
            return (ApplicationInformation)ri;
        } else {
            throw new InvalidArgumentException("Not a recognized kind of application: " + applicationName);
        }
       
    }
    
    
    
  public String getDocumentation(URI applicationName) throws ServiceException, NotFoundException, InvalidArgumentException {
        ApplicationInformation descr = getApplicationInformation(applicationName);
        return getInfoFor(descr);
  }
  

  public String getInfoFor(ApplicationInformation descr) {      
        StringBuffer result = new StringBuffer();
         result.append("Application: ")
                .append(descr.getName())
                .append("\n")
                .append(descr.getDescription())
                .append("\n");
         logger.debug("Added name and description");
         for(Iterator i = descr.getParameters().values().iterator(); i.hasNext(); ) {
             ParameterBean param = (ParameterBean)i.next();
            result.append("\nParameter ")
                .append("\n")
                .append(param.getUiName())
                .append("\n")
                .append(param.getDescription());

         
         if (param.getName() != null && param.getName().trim().length() > 0) result.append("\n\t").append("name :").append(param.getName());
         if (param.getType() != null ) result.append("\n\t").append("type :").append(param.getType());
         if (param.getSubType() != null && param.getSubType().trim().length() > 0) result.append("\n\t").append("subtype :").append(param.getSubType());
         if (param.getUnits() != null && param.getUnits().trim().length() > 0) result.append("\n\t").append("units :").append(param.getUnits());
         //if (param.getAcceptEncodings() != null && param.getAcceptEncodings().trim().length() > 0) result.append("\n\t").append("accept encodings :").append(param.getAcceptEncodings());
         if (param.getDefaultValue() != null && param.getDefaultValue().trim().length() > 0) result.append("\n\t").append("default value :").append(param.getDefaultValue());
         if (param.getUcd() != null && param.getUcd().trim().length() > 0) result.append("\n\t").append("UCD :").append(param.getUcd());  
         if (param.getOptions() != null ) result.append("\n\t").append("option list :").append(Arrays.asList(param.getOptions()));         

         }
         logger.debug("Added parameters");
           InterfaceBean[] ifaces = descr.getInterfaces();
           for (int i = 0; i < ifaces.length; i++) {
               InterfaceBean iface = ifaces[i];
               result.append("\nInterface ")
                   .append(iface.getName())
                   .append("\nInputs\n");
               ParameterReferenceBean[] prefs = iface.getInputs();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append(" max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }
               result.append("\nOutputs\n");
               prefs = iface.getOutputs();
               for (int j = 0; j < prefs.length; j++) {
                   ParameterReferenceBean p = prefs[j];
                   result.append("\t ").append(p.getRef()).append("max ").append(p.getMax()).append(", min ").append(p.getMin()).append("\n");
               }               
           }
         return result.toString();
    }    
   
  public Document createTemplateDocument(URI applicationName, String interfaceName) throws ServiceException, NotFoundException, InvalidArgumentException {
        try {
            ApplicationInformation descr = getApplicationInformation(applicationName);
        Tool t = createTemplateTool(interfaceName, descr);
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(t,doc);
        return doc;

        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (MarshalException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
  
  /**
 * @param applicationName
 * @param interfaceName
 * @param descr
 * @return
 * @throws IllegalArgumentException
 */
public Tool createTemplateTool(String interfaceName, ApplicationInformation descr) throws IllegalArgumentException {
    if (interfaceName != null && (! interfaceName.equals("default"))) {
        InterfaceBean[] ifaces = descr.getInterfaces();
        InterfaceBean iface = null;
        for (int i = 0; i < ifaces.length; i++) {
            if (ifaces[i].getName().equals(interfaceName)) {
                iface =ifaces[i];
            }
        }
        if (iface == null) {
            throw new IllegalArgumentException("Cannot find interface named " + interfaceName );
        }
         return createTool(descr,iface);
    } else {
        return createTool(descr,null);
    }
}

private Tool createTool(ApplicationInformation descr,InterfaceBean iface) {
      if (iface == null) {
          iface = descr.getInterfaces()[0];
      }
      Tool t = new Tool();
      t.setInterface(iface.getName());
      t.setName(descr.getId().getAuthority() + descr.getId().getPath()); //@todo should I drop the 'ivo' part.? - yes. for now. until cea accept this
      Input input = new Input();
      Output output = new Output();
      t.setInput(input);
      t.setOutput(output);
      
      // populate inputs
      ParameterReferenceBean[] refs = iface.getInputs();
      ParameterValue[] arr = new ParameterValue[refs.length];
      for (int i = 0; i < refs.length; i++) {
          ParameterBean pb = (ParameterBean)descr.getParameters().get(refs[i].getRef());
          arr[i] = createParameterValue(pb);         
      }
      input.setParameter(arr);
      
      // populate outputs.
      refs = iface.getOutputs();
      arr = new ParameterValue[refs.length];
      for (int i = 0; i < refs.length; i++) {
          ParameterBean pb = (ParameterBean) descr.getParameters().get(refs[i].getRef());
          arr[i] = createParameterValue(pb);
          
      }   
      output.setParameter(arr);
      
      // fill in the blanks.
      return t;
  }
  
  private ParameterValue createParameterValue(ParameterBean pb) {
      ParameterValue pv = new ParameterValue();
      pv.setName(pb.getName());
      if (pb.getDefaultValue()!= null) {
          pv.setValue(pb.getDefaultValue());
      } else {
          pv.setValue("");
      }
      return pv;
      
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
    //@todo test.
    public void validate(Document document) throws ServiceException, InvalidArgumentException {
        Tool t;
        try {
            t = (Tool)Unmarshaller.unmarshal(Tool.class,document);
            t.validate();
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        }
        ApplicationInformation desc;
        try {
        URI uri = new URI("ivo://" + t.getName());
        desc = getApplicationInformation(uri);
        } catch(URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);
        }
        InterfaceBean iface = null;
        InterfaceBean[] ifs = desc.getInterfaces();
        for (int i = 0 ; i < ifs.length; i++) {
            if (ifs[i].getName().equals(t.getInterface())) {
                iface = ifs[i];
                break;
            }
        }
        if (iface == null) {
            throw new InvalidArgumentException("Interface " + t.getInterface() + " not found");
        }
        // checkeac parameter against the interface.
            ParameterReferenceBean[] refs = iface.getInputs();            
            BaseBean searchRoot = t.getInput();
            for (int i = 0; i < refs.length; i++) {
                validateReference(desc,refs[i],searchRoot);
            }
            // now the outputs.
            refs = iface.getOutputs();
            searchRoot = t.getOutput();
            for (int i = 0; i < refs.length; i++) {
                validateReference(desc,refs[i],searchRoot);
            }           
    }

    private void validateReference(ApplicationInformation desc, ParameterReferenceBean bean, BaseBean searchRoot) throws InvalidArgumentException {
        Iterator results = searchRoot.findXPathIterator("/parameter[name = '" + bean.getRef() +"']");        
        ParameterBean pb = (ParameterBean)desc.getParameters().get(bean.getRef());
        int count = 0;
        while (results.hasNext()) {
            count++;
            results.next();
        }
        if (count < bean.getMin() || count > bean.getMax()) {
            throw new InvalidArgumentException("Parameter " + bean.getRef() + " occurs " + count +". Should occur between " 
                    + bean.getMin() + " and " + bean.getMax() + " times");
        }
    }
        

    public void validateStored(URI documentLocation) throws ServiceException, InvalidArgumentException, NotFoundException {
        Document doc;
        try {
            InputStream r = vos.getInputStream(documentLocation);
            doc = XMLUtils.newDocument(r);
  
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
           reg.getRecord(applicationName); // verify the application exists.
        String query = "Select * from Registry where @status = 'active' and cea:ManagedApplications/cea:ApplicationReference='"
            + applicationName + "'";
        return reg.adqlSearchRI(query);
  
    }
    
    
    public URI submitStored(URI documentLocation) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException {
        InputStream r = vos.getInputStream(documentLocation);
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
        InputStream r = vos.getInputStream(documentLocation);
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
        Tool document;
        ApplicationInformation info;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            URI application = new URI("ivo://" + document.getName());
            info = getApplicationInformation(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }

        
        ResourceInformation[] arr = listProvidersOf(info.getId());
        if (arr.length == 0) {
            throw new IllegalArgumentException(info.getName() +" has no registered providers");
        }
        List l =  Arrays.asList(arr);
        Collections.shuffle(l);
        ResourceInformation target = (ResourceInformation)l.get(0);
        return doExecute(info, document, target);
    }
    
    public URI submitTo(Document doc, URI server) throws NotFoundException,InvalidArgumentException, ServiceException, SecurityException {
        // munge name in document, if incorrect..       
        ApplicationInformation info;
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            URI application = new URI("ivo://" + document.getName());
            info = getApplicationInformation(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        ResourceInformation[] arr = listProvidersOf(info.getId());
        ResourceInformation target = null;
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i].getId().equals(server)) { 
                target = arr[i];
                break;
            }
        }
        if (target == null) {
            throw new InvalidArgumentException(server + " does not provide application " + info.getName());            
        }
        return doExecute(info, document, target);
        
    }   
    // primitive implementation method.
    private URI doExecute(ApplicationInformation application, Tool document, ResourceInformation server) throws ServiceException  {
        translateQueries(application, document);
        CommonExecutionConnectorClient delegate = createCEADelegate(server.getAccessURL());
        // fudge some kind of job id type. hope this will do.
        try {
        JobIdentifierType jid = new JobIdentifierType(community.getUserInformation().getId().toString() + application.getId().toString());
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
    
    /**
     * @param application
     * @param document
     * @throws ServiceException
     */
    public void translateQueries(ApplicationInformation application, Tool document) throws ServiceException {
        // fiddle any embedded queries..
        String[] adqlParameters = DatacenterToolEditorPanel.listADQLParameters(document.getInterface(),application);
        if (adqlParameters.length > 0) {            
            for (int i = 0; i < adqlParameters.length; i++) {
                ParameterValue val = (ParameterValue) document.findXPathValue("input/parameter[name='" + adqlParameters[i] + "']");
                if (!val.getIndirect()) { // it's an inline value
                    InputStream is = new ByteArrayInputStream(val.getValue().getBytes());
                    // try parsing the value as xml
                    try {
                        XMLUtils.newDocument(is);
                    } catch (Exception e) { // aha - got a string query.
                        Document adqlx = adql.s2x(val.getValue());
                        val.setValue( XMLUtils.DocumentToString(adqlx) );
                    }
                }
                
            }
        }
    }

    private URI getService(URI executionId) throws InvalidArgumentException  {
        try {
            return new URI(executionId.getScheme(),executionId.getSchemeSpecificPart(),null);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        }
       
    }
    
    private String getId(URI executionId) {
        return executionId.getFragment();
    }
    
 
    public void cancel(URI executionId) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try { 
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);        
            delegate.abort(getId(executionId));
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        } catch (NotApplicableException e) {
            throw new InvalidArgumentException(e);
        }
    }  
    
    public ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException {
        try {
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);        
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
        } catch (NotApplicableException e) {
            throw new InvalidArgumentException(e);
        }
    }
    public Map getResults(URI executionId) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        try {
        URL endpoint = reg.resolveIdentifier(getService(executionId));
        CommonExecutionConnectorClient delegate = createCEADelegate(endpoint);        
        ResultListType results =  delegate.getResults(getId(executionId));
        Map map = new HashMap();
        ParameterValue[] vals = results.getResult();        
        for (int i = 0; i < vals.length; i++) {
            map.put(vals[i].getName(),vals[i].getValue());
        }
        return map;
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        } catch (NotApplicableException e) {
            throw new InvalidArgumentException(e);
        }
    }
   
    public void userLogin(UserLoginEvent e) {
    }

    public synchronized void userLogout(UserLoginEvent e) {
        appReg = null;
    }


       


    
    
}


/* 
$Log: ApplicationsImpl.java,v $
Revision 1.4  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.3  2005/09/05 11:08:39  nw
added skeletons for registry and query dialogs

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

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