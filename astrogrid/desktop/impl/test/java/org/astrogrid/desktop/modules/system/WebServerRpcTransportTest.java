/*$Id: WebServerRpcTransportTest.java,v 1.2 2007/01/23 11:53:37 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class WebServerRpcTransportTest extends WebServerIntegrationTest implements WebServer {
    protected void setUp() throws Exception {
        super.setUp();
        WebServer serv = (WebServer)getACR().getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
        super.serv = this;
    }
    protected XmlRpcClient client;
    protected Vector v ;
    protected void tearDown() throws Exception {
    	super.tearDown();
    	client = null;
    	v = null;
    }

    public static Test suite() {
        return new ARTestSetup(new TestSuite(WebServerRpcTransportTest.class));
    }

	public String getKey() {
		v.clear();
		try {
			return (String)client.execute("system.webserver.getKey",v);
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}

	public int getPort() {
		v.clear();
		try {
			return ((Integer)client.execute("system.webserver.getPort",v)).intValue();
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}

	public String getUrlRoot() {
		v.clear();
		try {
			return (String)client.execute("system.webserver.getUrlRoot",v);
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}
    
}


/* 
$Log: WebServerRpcTransportTest.java,v $
Revision 1.2  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.1  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/