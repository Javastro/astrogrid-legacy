/*$Id: ApplicationsRpcSystemTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;

import org.apache.axis.utils.XMLUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class ApplicationsRpcSystemTest extends ApplicationsSystemTest implements Applications {
    protected void setUp() throws Exception {
        super.setUp();       
        WebServer serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
       apps = this; // test class implements reg interface.
    }

    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApplicationsRpcSystemTest.class),true);
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

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#listFully()
     */
    public ApplicationInformation[] listFully() throws ServiceException {
        v.clear();
        try {
            List l = (List)client.execute("astrogrid.applications.listFully",v);
            ApplicationInformation[] result = new ApplicationInformation[l.size()];
            for (int i = 0; i < l.size(); i++) {
                result[i] = create((Map)l.get(i));
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }
    
    private ApplicationInformation create(Map m) throws ServiceException {
        try {
            return new ApplicationInformation (
                    new URI((String)m.get("id"))
                    ,(String)m.get("name")
                    ,(String[]) ((List)m.get("interfaces")).toArray(new String[]{})
                    );
        } catch (URISyntaxException e) {
            throw new ServiceException(e);

        }        
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
        try {
            return (Map)client.execute("astrogrid.applications.getTemplateStruct",v);
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
                result[i] =  new ResourceInformation(new URI((String)m.get("id"))
                        ,(String)m.get("title")
                        ,(String)m.get("description")
                        ,new URL((String)m.get("accessURL"))        
                        );
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
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

}


/* 
$Log: ApplicationsRpcSystemTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/