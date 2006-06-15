/*$Id: JobsRpcSystemTest.java,v 1.4 2006/06/15 09:18:24 nw Exp $
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
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

import org.apache.axis.utils.XMLUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.omg.CORBA.SystemException;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

/** exercise xmlrpc interface to  jobs component.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2005
 *
 */
public class JobsRpcSystemTest extends JobsSystemTest implements Jobs {
    protected void setUp() throws Exception {
        super.setUp();       
        WebServer serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
       jobs = this; // test class implements reg interface.
    }

    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(JobsRpcSystemTest.class),true);
    }
    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#list()
     */
    public URI[] list() throws ServiceException {
        v.clear();
        try {
            List l = (List)client.execute("astrogrid.jobs.list",v);
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
     * @see org.astrogrid.acr.astrogrid.Jobs#listFully()
     */
    public ExecutionInformation[] listFully() throws ServiceException {
        v.clear();
        try {
            List l = (List)client.execute("astrogrid.jobs.listFully",v);
            ExecutionInformation[] result = new ExecutionInformation[l.size()];
            for (int i = 0; i < l.size(); i++) {
                result[i] = create((Map)l.get(i));
            }
            return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }       
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#createJob()
     */
    public Document createJob() throws ServiceException {
        v.clear();
        try { 
            String doc = (String)client.execute("astrogrid.jobs.createJob",v);
            InputStream is= new ByteArrayInputStream(doc.getBytes());
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
    
    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#getJobTranscript(java.net.URI)
     */
    public Document getJobTranscript(URI jobURN) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(jobURN.toString());
        try {
            String doc = (String) client.execute("astrogrid.jobs.getJobTranscript",v);
            InputStream is = new ByteArrayInputStream(doc.getBytes());
            return XMLUtils.newDocument(is);
  
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#getJobInformation(java.net.URI)
     */
    public ExecutionInformation getJobInformation(URI jobURN) throws ServiceException,
            SecurityException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(jobURN.toString());
        try {
            Map m = (Map) client.execute("astrogrid.jobs.getJobInformation",v);
            return create(m);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#cancelJob(java.net.URI)
     */
    public void cancelJob(URI jobURN) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(jobURN.toString());
        try {
            client.execute("astrogrid.jobs.cancelJob",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }             
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#deleteJob(java.net.URI)
     */
    public void deleteJob(URI jobURN) throws NotFoundException, ServiceException, SecurityException {
        v.clear();
        v.add(jobURN.toString());
        try {
            client.execute("astrogrid.jobs.deleteJob",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }        
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#submitJob(org.w3c.dom.Document)
     */
    public URI submitJob(Document workflow) throws ServiceException, InvalidArgumentException {
        v.clear();
        v.add(  XMLUtils.DocumentToString(workflow));
        try {
            String u = (String)client.execute("astrogrid.jobs.submitJob",v);
            return new URI(u);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }          
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#submitStoredJob(java.net.URI)
     */
    public URI submitStoredJob(URI workflowReference) throws ServiceException,
            InvalidArgumentException {
        v.clear();
        v.add(workflowReference.toString());
        try {
            String u = (String)client.execute("astrogrid.jobs.submitStoredJob",v);
            return new URI(u);
        } catch (Exception e) {
            throw new ServiceException(e);
        }     
    }
    
    private ExecutionInformation create(Map m) throws ServiceException {
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
     * @see org.astrogrid.acr.astrogrid.Jobs#wrapTask(org.w3c.dom.Document)
     */
    public Document wrapTask(Document arg0) throws ServiceException {
        //@todo implement
        throw new RuntimeException("Not implemented");
    }


}


/* 
$Log: JobsRpcSystemTest.java,v $
Revision 1.4  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.3  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.2.24.1  2005/11/23 05:01:13  nw
updated for new methods.

Revision 1.2  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/09 17:33:07  nw
finished system tests for ag components.
 
*/