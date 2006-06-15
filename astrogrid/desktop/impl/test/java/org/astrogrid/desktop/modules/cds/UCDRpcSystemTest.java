/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.UCD;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 10, 20062:14:27 AM
 */
public class UCDRpcSystemTest extends UCDSystemTest implements UCD {

	  protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        super.ucd = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;

	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(UCDRpcSystemTest.class));
	    }

		public String UCDList() throws ServiceException {
			v.clear();
			try {
				return (String)client.execute("cds.ucd.UCDList",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String UCDofCatalog(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.UCDofCatalog",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String assign(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.assign",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String explain(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.explain",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String resolveUCD(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.resolveUCD",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String translate(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.translate",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String upgrade(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.upgrade",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public String validate(String arg0) throws ServiceException {
			v.clear();
			v.add(arg0);
			try {
				return (String)client.execute("cds.ucd.validate",v);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	
}
