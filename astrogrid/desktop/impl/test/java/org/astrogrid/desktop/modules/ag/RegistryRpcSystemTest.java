/*$Id: RegistryRpcSystemTest.java,v 1.3 2005/11/04 10:14:26 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpRpcTest;

import org.apache.axis.utils.XMLUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** exercises ag registry component via xmlrpc.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class RegistryRpcSystemTest extends RegistrySystemTest implements Registry {
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg =getACR();
        WebServer serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        assertNotNull(client); 
        v = new Vector();
        registry = this; // test class implements reg interface.
    }

    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RegistryRpcSystemTest.class),true);
    }
    /**
     * @param testURI2
     * @return
     * @throws ServiceException
     */
    public URL resolveIdentifier(URI testURI2) throws ServiceException {
        v.clear();
        v.add(testURI2.toString());
        try {
            String result = (String)client.execute("astrogrid.registry.resolveIdentifier",v);
            return new URL(result);
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }

    /**
     * @param testURI2
     * @return
     * @throws ServiceException
     */
    public Document getRecord(URI testURI2) throws ServiceException{
        v.clear();
        v.add(testURI2.toString());
        try {
            String result = (String)client.execute("astrogrid.registry.getRecord",v);
            InputStream is = new ByteArrayInputStream(result.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);

        } 

    }


    /**
     * @param testURI2
     * @return
     * @throws ServiceException
     */
    public ResourceInformation getResourceInformation(URI testURI2) throws ServiceException{
        v.clear();
        v.add(testURI2.toString());
        try { 
        Map m =  (Map)client.execute("astrogrid.registry.getResourceInformation",v);
        return createResourceInformation(m);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param m
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    private ResourceInformation createResourceInformation(Map m) throws URISyntaxException, MalformedURLException {
        URL url = m.containsKey("accessURL") ? new URL((String)m.get("accessURL")) : null;
        URL logo = m.containsKey("logoURL") ? new URL((String)m.get("logoURL")): null;
        return new ResourceInformation(new URI((String)m.get("id"))
                ,(String)m.get("title")
                ,(String)m.get("description")
                ,url
                ,logo
                );
    }
    /**
     * @param query_string
     * @return
     * @throws ServiceException
     */
    public Document adqlSearch(String query) throws ServiceException {
        v.clear();
        v.add(query);
        try {
        String s =  (String)client.execute("astrogrid.registry.adqlSearch",v);
        InputStream is = new ByteArrayInputStream(s.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Registry#adqlSearchRI(java.net.URI)
     */
    public ResourceInformation[] adqlSearchRI(String arg0) throws NotFoundException, ServiceException {
        v.clear();
        v.add(arg0);
        try {
        List s =  (List)client.execute("astrogrid.registry.adqlSearchRI",v);
        ResourceInformation[] result = new ResourceInformation[s.size()];
        for (int i = 0; i < s.size(); i++) {
            result[i] = createResourceInformation((Map)s.get(i));
        }
        return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }    
    /**
     * @see org.astrogrid.acr.astrogrid.Registry#xquery(java.lang.String)
     */
    public Document xquerySearch(String xquery) throws ServiceException {
        v.clear();
        v.add(xquery);
        try {
        String s =  (String)client.execute("astrogrid.registry.xquerySearch",v);
        InputStream is = new ByteArrayInputStream(s.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Registry#searchForRecords(java.lang.String)
     */
    public Document searchForRecords(String arg0) throws ServiceException {
        return adqlSearch(arg0);
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Registry#keywordSearch(java.lang.String, boolean)
     */
    public Document keywordSearch(String arg0, boolean arg1) throws ServiceException {
        v.clear();
        v.add(arg0);
        v.add(new Boolean(arg1));
        try {
        String s =  (String)client.execute("astrogrid.registry.keywordSearch",v);
        InputStream is = new ByteArrayInputStream(s.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Registry#keywordSearchRI(java.lang.String, boolean)
     */
    public ResourceInformation[] keywordSearchRI(String arg0, boolean arg1) throws ServiceException {
        v.clear();
        v.add(arg0);
        v.add(new Boolean(arg1));
        try {
            List s =  (List)client.execute("astrogrid.registry.keywordSearchRI",v);
            ResourceInformation[] result = new ResourceInformation[s.size()];
            for (int i = 0; i < s.size(); i++) {
                result[i] = createResourceInformation((Map)s.get(i));
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}


/* 
$Log: RegistryRpcSystemTest.java,v $
Revision 1.3  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/