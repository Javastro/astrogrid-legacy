/*$Id: ApplicationsRpcSystemTest.java,v 1.1 2005/08/09 17:33:07 nw Exp $
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

import org.apache.xmlrpc.XmlRpcClient;
import org.w3c.dom.Document;

import java.net.URI;
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
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#listFully()
     */
    public ApplicationInformation[] listFully() throws ServiceException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getApplicationInformation(java.net.URI)
     */
    public ApplicationInformation getApplicationInformation(URI applicationName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getDocumentation(java.net.URI)
     */
    public String getDocumentation(URI applicationName) throws ServiceException, NotFoundException,
            InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#createTemplateDocument(java.net.URI, java.lang.String)
     */
    public Document createTemplateDocument(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#createTemplateStruct(java.net.URI, java.lang.String)
     */
    public Map createTemplateStruct(URI applicationName, String interfaceName)
            throws ServiceException, NotFoundException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#convertDocumentToStruct(org.w3c.dom.Document)
     */
    public Map convertDocumentToStruct(Document document) throws InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#convertStructToDocument(java.util.Map)
     */
    public Document convertStructToDocument(Map struct) throws InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#validate(org.w3c.dom.Document)
     */
    public void validate(Document document) throws ServiceException, InvalidArgumentException {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#validateStored(java.net.URI)
     */
    public void validateStored(URI documentLocation) throws ServiceException,
            InvalidArgumentException, NotFoundException {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#listProvidersOf(java.net.URI)
     */
    public ResourceInformation[] listProvidersOf(URI applicationId) throws ServiceException,
            NotFoundException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submit(org.w3c.dom.Document)
     */
    public URI submit(Document document) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitTo(org.w3c.dom.Document, java.net.URI)
     */
    public URI submitTo(Document document, URI server) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitStored(java.net.URI)
     */
    public URI submitStored(URI documentLocation) throws NotFoundException,
            InvalidArgumentException, SecurityException, ServiceException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#submitStoredTo(java.net.URI, java.net.URI)
     */
    public URI submitStoredTo(URI documentLocation, URI server) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#cancel(java.net.URI)
     */
    public void cancel(URI executionId) throws NotFoundException, InvalidArgumentException,
            ServiceException, SecurityException {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getExecutionInformation(java.net.URI)
     */
    public ExecutionInformation getExecutionInformation(URI executionId) throws ServiceException,
            NotFoundException, SecurityException, InvalidArgumentException {
        return null;
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Applications#getResults(java.net.URI)
     */
    public Map getResults(URI executionid) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        return null;
    }

}


/* 
$Log: ApplicationsRpcSystemTest.java,v $
Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/