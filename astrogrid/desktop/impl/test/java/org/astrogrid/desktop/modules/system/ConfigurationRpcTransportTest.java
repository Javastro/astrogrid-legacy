/*$Id: ConfigurationRpcTransportTest.java,v 1.6 2008/08/04 16:37:24 nw Exp $
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

import java.net.URL;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.astrogrid.Fixture;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.ARTestSetup;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 03-Aug-2005
 *
 */
public class ConfigurationRpcTransportTest extends ConfigurationIntegrationTest {
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ConfigurationRpcTransportTest.class));
    } 

    protected void setUp() throws Exception {
        super.setUp();
        super.conf = new ConfigurationXmlRpcClient(Fixture.createXmlRpcClient(getACR()));
    }

    public static class ConfigurationXmlRpcClient implements Configuration {

	
        public ConfigurationXmlRpcClient(XmlRpcClient client) {
            super();
            this.client = client;
        }

        public ConfigurationXmlRpcClient(URL endpoint) {
            super();
            this.client = Fixture.createXmlRpcClient(endpoint);

        }
        
	protected final  XmlRpcClient client;

	public String getKey(String arg0) {
		try {
			String s =  (String)client.execute("system.configuration.getKey",new Object[]{arg0});
			return s.equals("NULL") ? null : s;
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}

	public Map list() throws ACRException {	
		try {
			return (Map)client.execute("system.configuration.list",new Object[]{});
		} catch (Exception e) {
			throw new ACRException(e.getMessage());
		}
	}

	public String[] listKeys() throws ACRException {
		try {
			Object[] a  = (Object[])client.execute("system.configuration.listKeys",new Object[]{});
			String[] arr = new String[a.length];
			for (int i = 0; i < arr.length; i++) {
			    arr[i] = (String)a[i];
			}
			return arr;
		} catch (Exception e) {
			throw new ACRException(e.getMessage());
		}
	}

	public void removeKey(String arg0) {
		try {
			client.execute("system.configuration.removeKey",new Object[]{arg0});
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");		
		}
	}
	public void reset() throws ServiceException {
		throw new RuntimeException("Unimplemented");
	}

    public boolean setKey(String arg0, String arg1) {
		try {
			return( (Boolean)client.execute("system.configuration.setKey",new Object[]{arg0,arg1})).booleanValue()	;
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");			
		}
	}
    }
  
}


/* 
$Log: ConfigurationRpcTransportTest.java,v $
Revision 1.6  2008/08/04 16:37:24  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.5  2007/06/18 17:45:03  nw
fixed test to work with interface change.

Revision 1.4  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.3  2007/01/29 10:42:48  nw
tidied.

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