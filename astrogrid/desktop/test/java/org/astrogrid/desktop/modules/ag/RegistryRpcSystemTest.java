/*$Id: RegistryRpcSystemTest.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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
import java.net.URI;
import java.net.URL;
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
        return new ResourceInformation(new URI((String)m.get("ivorn"))
                ,(String)m.get("title")
                ,(String)m.get("description")
                ,new URL((String)m.get("accessURL"))
                );
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param query_string
     * @return
     * @throws ServiceException
     */
    public Document search(String query) throws ServiceException {
        v.clear();
        v.add(query);
        try {
        String s =  (String)client.execute("astrogrid.registry.search",v);
        InputStream is = new ByteArrayInputStream(s.getBytes());
        return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}


/* 
$Log: RegistryRpcSystemTest.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/