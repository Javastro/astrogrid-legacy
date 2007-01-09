/*$Id: ApplicationsRpcTransportTest.java,v 1.1 2007/01/09 16:12:18 nw Exp $
 * Created on 09-Aug-2005
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
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;

import org.apache.axis.utils.XMLUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class ApplicationsRpcTransportTest extends ApplicationsSystemTest implements Applications {
    protected void setUp() throws Exception {
        super.setUp();       
        WebServer serv = (WebServer)getACR().getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
       apps = this; // test class implements reg interface.
    }

    protected XmlRpcClient client;
    protected Vector v ;

    protected void tearDown() throws Exception {
    	super.tearDown();
    	client = null;
    	v = null;
    }
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ApplicationsRpcTransportTest.class),true);
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Applications#list()
     */
    public URI[] list() throws ServiceException {
        v.clear();
        try {
            List l = (List)client.execute("astrogrid.applications.list",v);
            URI[] result = new URI[l.size()];
            for (int i = 0; i < l.size(); i++) {
                result[i] = new URI(l.get(i).toString());                       
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    
    private ApplicationInformation create(Map m) throws ServiceException {
        try {
            Map parameters = createParameters((Map)m.get("parameters"));
            InterfaceBean[] interfaces = createInterfaces((List)m.get("interfaces"));
            URL logo = m.containsKey("logoURL") ? new URL((String)m.get("logoURL")): null;            
            return new ApplicationInformation (
                    new URI((String)m.get("id"))
                    ,(String)m.get("name")
                    ,(String)m.get("description")
                    ,parameters
                    ,interfaces
                    ,null
                    ,logo
                    );
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        } catch (MalformedURLException e) {
            throw new ServiceException(e);            
        }        
        }
    private Map createParameters(Map m)  {
        Map result = new HashMap();
        for (Iterator i = result.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            result.put(e.getKey(),createParameter((Map)e.getValue()));
        }
        return result;
    }
    
    private InterfaceBean[] createInterfaces(List l) {
        InterfaceBean[] result = new InterfaceBean[l.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = createInterface((Map)l.get(i));
        }
        return result;
    }
    
    private ParameterBean createParameter(Map m) {
        String[] options = (String[])((List)m.get("options")).toArray(new String[]{});
        return new ParameterBean(
                (String)m.get("name")
                ,(String)m.get("uiName")
                ,(String)m.get("description")
                ,(String)m.get("ucd")
                ,(String)m.get("defaultValue")
                ,(String)m.get("units")
                ,(String)m.get("type")
                ,(String)m.get("subType")
                ,options                
                );
    }
    
    private InterfaceBean createInterface(Map m) {
        ParameterReferenceBean[] inputs = createParameterReferences((List)m.get("inputs"));
        ParameterReferenceBean[] outputs = createParameterReferences((List)m.get("outputs"));
        return new InterfaceBean(
                (String)m.get("name")
                ,inputs
                ,outputs
                );
    }
    
    private ParameterReferenceBean[] createParameterReferences(List l) {
        ParameterReferenceBean[] result = new ParameterReferenceBean[l.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = createParameterReference((Map)l.get(i));
        }
        return result;
    }
    
    private ParameterReferenceBean createParameterReference(Map m) {
        return new ParameterReferenceBean(
                m.get("ref").toString()
                ,Integer.parseInt(m.get("max").toString())
                ,Integer.parseInt(m.get("min").toString())
                );
    }

    
    
    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getApplicationInformation(java.net.URI)
     */
    public ApplicationInformation getApplicationInformation(URI applicationName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(applicationName.toString());
        try {
            Map m = (Map)client.execute("astrogrid.applications.getApplicationInformation",v);
            return create(m);
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }
    
	public CeaApplication getCeaApplication(URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
		throw new ServiceException("Not implemented - can't be arsed");
	}

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getDocumentation(java.net.URI)
     */
    public String getDocumentation(URI applicationName) throws ServiceException, NotFoundException,
            InvalidArgumentException {
        v.clear();
        v.add(applicationName.toString());
        try {
            return (String)client.execute("astrogrid.applications.getDocumentation",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#createTemplateDocument(java.net.URI, java.lang.String)
     */
    public Document createTemplateDocument(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(applicationName.toString());
        v.add(interfaceName);
        try {
            String s =  (String)client.execute("astrogrid.applications.createTemplateDocument",v);
            InputStream is = new ByteArrayInputStream(s.getBytes());
            return XMLUtils.newDocument(is); 
        } catch (Exception e) {
            throw new ServiceException(e);
        }  
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#createTemplateStruct(java.net.URI, java.lang.String)
     */
    public Map createTemplateStruct(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(applicationName.toString());
        v.add(interfaceName);
        try {
            return (Map)client.execute("astrogrid.applications.createTemplateStruct",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }  
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#convertDocumentToStruct(org.w3c.dom.Document)
     */
    public Map convertDocumentToStruct(Document document) throws InvalidArgumentException {
        v.clear();
        v.add(XMLUtils.DocumentToString(document));
        try {
            return (Map)client.execute("astrogrid.applications.convertDocumentToStruct",v);
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }  
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#convertStructToDocument(java.util.Map)
     */
    public Document convertStructToDocument(Map struct) throws InvalidArgumentException {
        v.clear();
        v.add(struct);
        try {
            String s = (String)client.execute("astrogrid.applications.convertStructToDocument",v);
            InputStream is = new ByteArrayInputStream(s.getBytes());
            return XMLUtils.newDocument(is);             
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }  
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#validate(org.w3c.dom.Document)
     */
    public void validate(Document document) throws ServiceException, InvalidArgumentException {
        v.clear();
        v.add(XMLUtils.DocumentToString(document));
        try {
            client.execute("astrogrid.applications.validate",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#validateStored(java.net.URI)
     */
    public void validateStored(URI documentLocation) throws ServiceException,
            InvalidArgumentException, NotFoundException {
        v.clear();
        v.add(documentLocation.toString());
        try {
            client.execute("astrogrid.applications.validateStored",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }            
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#listProvidersOf(java.net.URI)
     */
    public ResourceInformation[] listProvidersOf(URI applicationId) throws ServiceException,
            NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(applicationId.toString());
        try {
            List l = (List)client.execute("astrogrid.applications.listProvidersOf",v);
            ResourceInformation[] result = new ResourceInformation[l.size()];
            for (int i = 0; i < l.size(); i++) {
                Map m = ((Map)l.get(i));
                URL logo = m.containsKey("logoURL") ? new URL((String)m.get("logoURL")): null;                
                result[i] =  new ResourceInformation(new URI((String)m.get("id"))
                        ,(String)m.get("title")
                        ,(String)m.get("description")
                        ,new URL((String)m.get("accessURL"))        
                        ,logo
                        );
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }
    
	public Service[] listServersProviding(URI arg0) throws ServiceException, NotFoundException, InvalidArgumentException {
		throw new ServiceException("Can't be bothered to implemnt");
	}


    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submit(org.w3c.dom.Document)
     */
    public URI submit(Document document) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(XMLUtils.DocumentToString(document));
        try {
            String s = (String)client.execute("astrogrid.applications.submit",v);
            return new URI(s);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitTo(org.w3c.dom.Document, java.net.URI)
     */
    public URI submitTo(Document document, URI server) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(XMLUtils.DocumentToString(document));
        v.add(server.toString());
        try {
            String s= (String)client.execute("astrogrid.applications.submitTo",v);
            return new URI(s);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitStored(java.net.URI)
     */
    public URI submitStored(URI documentLocation) throws NotFoundException,
            InvalidArgumentException, SecurityException, ServiceException {
        v.clear();
        v.add(documentLocation.toString());
        try {
            String s = (String)client.execute("astrogrid.applications.submitStored",v);
            return new URI(s);
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitStoredTo(java.net.URI, java.net.URI)
     */
    public URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(documentLocation.toString());
        v.add(server.toString());
        try {
            String s= (String)client.execute("astrogrid.applications.submitStoredTo",v);
            return new URI(s);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#cancel(java.net.URI)
     */
    public void cancel(URI executionId) throws NotFoundException, InvalidArgumentException,
            ServiceException, SecurityException {
        v.clear();
        v.add(executionId.toString());
        try {
            client.execute("astrogrid.applications.cancel",v);            
        } catch (Exception e) {
            throw new ServiceException(e);
        }           
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getExecutionInformation(java.net.URI)
     */
    public ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException,
            NotFoundException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(executionId.toString());
        try {
            Map m =  (Map)client.execute("astrogrid.applications.getExecutionInformation",v);
            return createEI(m);
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }  
    }

    private ExecutionInformation createEI(Map m) throws ServiceException {
        try {
            return new ExecutionInformation (
                    new URI((String)m.get("id"))
                    ,(String)m.get("name")
                    ,(String)m.get("description")
                    ,(String)m.get("status")
                    ,(Date)m.get("startTime")
                    ,(Date)m.get("finishTime")
                    );
        } catch (URISyntaxException e) {
            throw new ServiceException(e);

        }
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getResults(java.net.URI)
     */
    public Map getResults(URI executionid) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(executionid.toString());
        try {
            return (Map)client.execute("astrogrid.applications.getResults",v);
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }  
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getQueryToListApplications()
     */
    public String getQueryToListApplications() {
        v.clear();
        try {
            return (String)client.execute("astrogrid.applications.getQueryToListApplications",v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getRegistryQuery()
     */
    public String getRegistryQuery() {
        v.clear();
        try {
            return (String)client.execute("astrogrid.applications.getRegistryQuery",v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getRegistryAdqlQuery() {
        v.clear();
        try {
            return (String)client.execute("astrogrid.applications.getRegistryAdqlQuery",v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getRegistryXQuery() {
        v.clear();
        try {
            return (String)client.execute("astrogrid.applications.getRegistryXQuery",v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


/* 
$Log: ApplicationsRpcTransportTest.java,v $
Revision 1.1  2007/01/09 16:12:18  nw
improved tests - still need extending though.

Revision 1.7  2006/08/15 10:28:48  nw
migrated from old to new registry models.

Revision 1.6  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.5  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.4  2005/10/18 16:52:31  nw
deprecated a badly-named method

Revision 1.3  2005/10/13 18:33:05  nw
bugfix to tetst

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/