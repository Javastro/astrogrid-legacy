/*$Id: ApiHelpRpcIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
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
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** repeat of apihelp tests, via xmlrpc interface - exercise this transport, and make sure configuration is correct
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class ApiHelpRpcIntegrationTest extends ApiHelpIntegrationTest implements ApiHelp {
    protected void setUp() throws Exception {
        super.setUp();
        WebServer serv = (WebServer)getACR().getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
        help = this ; // test clas iimplements service interface
    }
    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApiHelpRpcIntegrationTest.class));
    }
// need a careful implementation of this. irritating.
	public Object callFunction(String arg0, int arg1, Object[] arg2) throws ACRException, InvalidArgumentException, NotFoundException {
		if (arg0 == null) {
			throw new InvalidArgumentException("name canot be null");
		}
		v.clear();
		v.add(arg0);
		v.add(Integer.toString(arg1));
		//v.add(new Integer(arg1));
		Vector v1 = new Vector();
		if (arg2 != null) {
			for (int i = 0; i < arg2.length; i++) {
				v1.add(arg2[i] == null ? "NULL" : arg2[i].toString());
			}
		}
		v.add(v1);
		try {
			return client.execute("system.apihelp.callFunction",v);
		} catch (Exception x) {
			//try and deduce what kind of exception it was.
			String s = x.getMessage();
			if (s.indexOf("NotFound") != -1) {
				throw new NotFoundException(s);
			} else if (s.indexOf("InvalidArgument") != -1) {
				throw new InvalidArgumentException(s);
			} else {
				throw new ACRException(s);
			}
		}
	}

	public String componentHelp(String arg0) throws NotFoundException {

		v.clear();
		v.add(arg0);
		try {
			return (String)client.execute("system.apihelp.componentHelp",v);
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public String interfaceClassName(String arg0) throws NotFoundException {
		v.clear();
		v.add(arg0);
		try {
			return (String)client.execute("system.apihelp.interfaceClassName",v);
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public String[] listComponentsOfModule(String arg0) throws NotFoundException {
		v.clear();
		v.add(arg0);
		try {
			Vector a = (Vector)client.execute("system.apihelp.listComponentsOfModule",v);
			return (String[])a.toArray(new String[]{});
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public String[] listMethods() {
		v.clear();
		try {
			Vector a = (Vector)client.execute("system.apihelp.listMethods",v);
			return (String[])a.toArray(new String[]{});
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");
		}
	}

	public String[] listMethodsOfComponent(String arg0) throws NotFoundException {
		v.clear();
		v.add(arg0);
		try {
			Vector a = (Vector)client.execute("system.apihelp.listMethodsOfComponent",v);
			return (String[])a.toArray(new String[]{});
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public ModuleDescriptor[] listModuleDescriptors() {
		fail("can't call this method over xmlrpc at the moment - causes whole AR to crash!!");
		throw new RuntimeException("never reached");
	}

	public String[] listModules() {
		v.clear();
		try {
			Vector a = (Vector)client.execute("system.apihelp.listModules",v);
			return (String[])a.toArray(new String[]{});
		} catch (Exception e) {
			fail(e.getMessage());
			throw new RuntimeException("never reached");
		}
	}

	public String methodHelp(String arg0) throws NotFoundException {

		v.clear();
		v.add(arg0);
		try {
			return (String)client.execute("system.apihelp.methodHelp",v);
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public String[][] methodSignature(String arg0) throws NotFoundException {
		v.clear();
		v.add(arg0);
		try {
			Vector a = (Vector)client.execute("system.apihelp.methodSignature",v);
			for (int i = 0; i < a.size(); i++) {
				Vector b = (Vector)a.get(i);
				a.set(i,b.toArray(new String[]{}));
			}	
			return (String[][])a.toArray(new String[][]{});
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public String moduleHelp(String arg0) throws NotFoundException {

		v.clear();
		v.add(arg0);
		try {
			return (String)client.execute("system.apihelp.moduleHelp",v);
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}
    
    
}


/* 
$Log: ApiHelpRpcIntegrationTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/