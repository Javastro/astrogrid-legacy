/*$Id: ConfigurationRpcIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcClient;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class ConfigurationRpcIntegrationTest extends ConfigurationIntegrationTest implements Configuration {
    protected void setUp() throws Exception {
        super.setUp();
        WebServer serv = (WebServer)getACR().getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
        super.conf = this;
    }
    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ConfigurationRpcIntegrationTest.class));
    }

	public String getKey(String arg0) {
		v.clear();
		v.add(arg0);
		try {
			String s =  (String)client.execute("system.configuration.getKey",v);
			return s.equals("NULL") ? null : s;
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}

	public Map list() throws ACRException {
		v.clear();
		try {
			return (Map)client.execute("system.configuration.list",v);
		} catch (Exception e) {
			throw new ACRException(e.getMessage());
		}
	}

	public String[] listKeys() throws ACRException {
		v.clear();
		try {
			Vector a = (Vector)client.execute("system.configuration.listKeys",v);
			return (String[])a.toArray(new String[]{});
		} catch (Exception e) {
			throw new ACRException(e.getMessage());
		}
	}

	public void removeKey(String arg0) {
		v.clear();
		v.add(arg0);
		try {
			client.execute("system.configuration.removeKey",v);
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");		
		}
	}

	public boolean setKey(String arg0, String arg1) {
		v.clear();
		v.add(arg0);
		v.add(arg1);
		try {
			return( (Boolean)client.execute("system.configuration.setKey",v)).booleanValue()	;
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}
    
  
}


/* 
$Log: ConfigurationRpcIntegrationTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/