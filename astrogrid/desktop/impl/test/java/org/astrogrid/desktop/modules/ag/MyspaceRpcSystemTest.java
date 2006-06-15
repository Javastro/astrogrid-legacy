/*$Id: MyspaceRpcSystemTest.java,v 1.8 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.NodeInformation;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

/** exercise xmlrpc interface to myspace component.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class MyspaceRpcSystemTest extends MyspaceSystemTest implements Myspace{
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg =getACR();
        WebServer serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
        myspace = this; // test class implements reg interface.
    }

    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(MyspaceRpcSystemTest.class),true);
    }

    public URI getHome() throws SecurityException, ServiceException, NotFoundException {
        v.clear();
        try {
            String result = (String)client.execute("astrogrid.myspace.getHome",v);
            return new URI(result);
        } catch (Exception e) {
            throw new ServiceException(e);
        } 

    }

    public boolean exists(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           return ((Boolean)client.execute("astrogrid.myspace.exists",v)).booleanValue();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public NodeInformation getNodeInformation(URI ivorn) throws ServiceException, NotFoundException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           Map m = (Map)client.execute("astrogrid.myspace.getNodeInformation",v);
           return buildNodeInformation(m);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
  
    /**
     * @param m
     * @return
     * @throws URISyntaxException
     */
    private NodeInformation buildNodeInformation(Map m) throws URISyntaxException {
        Calendar create =Calendar.getInstance();
           Calendar modify = Calendar.getInstance();
           URI contentLocation = m.containsKey("contentLocation") ? new URI((String)m.get("contentLocation")) : null;
           create.setTime((Date)m.get("createDate"));
           modify.setTime((Date)m.get("modifyDate"));           
           return new NodeInformation(
                   (String)m.get("name")
                   ,new URI((String)m.get("id"))
                   ,new Long( ((Number)m.get("size")).longValue())
                   ,create
                   ,modify
                   ,(Map)m.get("attributes")
                   ,((Boolean)m.get("file")).booleanValue()  
                   ,contentLocation
                   );
    }

    public void createFile(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           client.execute("astrogrid.myspace.createFile",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }        
    }

    public void createFolder(URI ivorn) throws ServiceException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           client.execute("astrogrid.myspace.createFolder",v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }          
    }

    public URI createChildFolder(URI parentIvorn, String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(parentIvorn.toString());
        v.add(name);
        try {
           return new URI( (String)client.execute("astrogrid.myspace.createChildFolder",v));
        } catch (Exception e) {
            throw new ServiceException(e);
        }          
    }

    public URI createChildFile(URI parentIvorn, String name) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
        v.clear();
        v.add(parentIvorn.toString());
        v.add(name);
        try {
           return new URI((String)client.execute("astrogrid.myspace.createChildFile",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }          
    }

    public URI getParent(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        try {
           return new URI((String)client.execute("astrogrid.myspace.getParent",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }     
    }

    public String[] list(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           List l = (List)client.execute("astrogrid.myspace.list",v);
           String[] result = new String[l.size()];
           for (int i = 0; i < l.size(); i++) {
               result[i] = (String)l.get(i);
           }
           return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }  
    }


    public URI[] listIvorns(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           List l = (List)client.execute("astrogrid.myspace.listIvorns",v);
           URI[] result = new URI[l.size()];
           for (int i = 0; i < l.size(); i++) {
               result[i] =new URI( (String)l.get(i));
           }
           return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }

    public NodeInformation[] listNodeInformation(URI ivorn) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
           List l = (List)client.execute("astrogrid.myspace.listNodeInformation",v);
           NodeInformation[] result = new NodeInformation[l.size()];
           for (int i = 0; i < l.size(); i++) {
               result[i] =buildNodeInformation((Map)l.get(i));

           }
           return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }
    public void refresh(URI ivorn) throws SecurityException, ServiceException, NotFoundException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
            client.execute("astrogrid.myspace.refresh",v);           
        } catch (Exception e) {
            throw new ServiceException(e);
        }             
    }

    public void delete(URI ivorn) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException {
        v.clear();
        v.add(ivorn.toString());
        try {
            client.execute("astrogrid.myspace.delete",v);           
        } catch (Exception e) {
            throw new ServiceException(e);
        }            
    }

    public URI rename(URI srcIvorn, String newName) throws NotFoundException, SecurityException, ServiceException, InvalidArgumentException {
        v.clear();
        v.add(srcIvorn.toString());
        v.add(newName);
        try {
           return new URI((String)client.execute("astrogrid.myspace.rename",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }         
    }

    public URI move(URI srcIvorn, URI newParentIvorn, String newName) throws NotFoundException, InvalidArgumentException, SecurityException, ServiceException {
        v.clear();
        v.add(srcIvorn.toString());
        v.add(newParentIvorn.toString());
        v.add(newName);
        try {
           return new URI((String)client.execute("astrogrid.myspace.move",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }         
    }

    public void changeStore(URI srcIvorn, URI storeIvorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(srcIvorn.toString());
        v.add(storeIvorn.toString());
        try {
           client.execute("astrogrid.myspace.changeStore",v);           
        } catch (Exception e) {
            throw new ServiceException(e);
        }            
    }

    public URI copy(URI srcIvorn, URI newParentIvorn, String newName) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(srcIvorn.toString());
        v.add(newParentIvorn.toString());
        v.add(newName);
        try {
           return new URI((String)client.execute("astrogrid.myspace.copy",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }    
    }

    public URL getReadContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        try {
           return new URL((String)client.execute("astrogrid.myspace.getReadContentURL",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }   
    }

    public URL getWriteContentURL(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        try {
           return new URL((String)client.execute("astrogrid.myspace.getWriteContentURL",v));           
        } catch (Exception e) {
            throw new ServiceException(e);
        }  
    }

    public void copyContentToURL(URI ivorn, URL destination) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        v.add(destination.toString());
        try {
           client.execute("astrogrid.myspace.copyContentToURL",v);           
        } catch (Exception e) {
            throw new ServiceException(e);
        }            
    }

    public void copyURLToContent(URL src, URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(src.toString());
        v.add(ivorn.toString());
        try {
           client.execute("astrogrid.myspace.copyURLToContent",v);           
        } catch (Exception e) {
            throw new ServiceException(e);
        }    
    }

    public ResourceInformation[] listAvailableStores() throws ServiceException {
        v.clear();
        try {
           List l = (List)client.execute("astrogrid.myspace.listAvailableStores",v);
           ResourceInformation[] result = new ResourceInformation[l.size()];
           for (int i = 0; i < l.size(); i++) {
               Map m = (Map)l.get(i);
               URL logo = m.containsKey("logoURL") ? new URL((String)m.get("logoURL")): null;             
               result[i] =new ResourceInformation(new URI((String)m.get("id"))
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

    /**
     * @see org.astrogrid.acr.astrogrid.Myspace#read(java.net.URI)
     */
    public String read(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        try {
            return (String) client.execute("astrogrid.myspace.read", v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
            
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Myspace#write(java.net.URI, java.lang.String)
     */
    public void write(URI ivorn, String content) throws InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        v.add(content);
        try {
            client.execute("astrogrid.myspace.write", v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }        
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Myspace#readBinary(java.net.URI)
     */
    public byte[] readBinary(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        try {
            return (byte[]) client.execute("astrogrid.myspace.readBinary", v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Myspace#writeBinary(java.net.URI, byte[])
     */
    public void writeBinary(URI ivorn, byte[] content) throws InvalidArgumentException, ServiceException, SecurityException {
        v.clear();
        v.add(ivorn.toString());
        v.add(content);
        try {
            client.execute("astrogrid.myspace.writeBinary", v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }             
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Myspace#transferCompleted(java.net.URI)
     */
    public void transferCompleted(URI ivorn) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException, NotApplicableException {
        v.clear();
        v.add(ivorn.toString());
        try {
            client.execute("astrogrid.myspace.transferCompleted", v);
        } catch (Exception e) {
            throw new ServiceException(e);
        }      
    }


}


/* 
$Log: MyspaceRpcSystemTest.java,v $
Revision 1.8  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.7  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.6  2005/10/13 18:33:47  nw
fixes supporting getWriteContentURL

Revision 1.5  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.2.16.1  2005/10/10 18:12:37  nw
merged kev's datascope lite.

Revision 1.4  2005/10/10 12:10:15  KevinBenson
should not need the writeStream anymore here that method is now in an internal interface

Revision 1.3  2005/10/06 10:52:13  nw
quick fix necessary to make the system compile.

Revision 1.2  2005/08/16 13:19:32  nw
fixes for 1.1-beta-2

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/