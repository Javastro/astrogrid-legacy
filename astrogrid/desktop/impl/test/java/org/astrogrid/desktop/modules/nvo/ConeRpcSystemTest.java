/**
 * 
 */
package org.astrogrid.desktop.modules.nvo;

import java.net.URI;
import java.net.URL;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Jun 10, 200610:31:04 AM
 */
public class ConeRpcSystemTest extends ConeRmiSystemTest implements Cone{
	 protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        cone = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;

	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(ConeRpcSystemTest.class));
	    }

		public URL addOption(URL arg0, String arg1, String arg2) throws InvalidArgumentException {

			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			v.add(arg2.toString());
			try {
				String s =  (String)client.execute("nvo.cone.addOption",v);
				return new URL(s);
			} catch (Exception e) {
				throw new InvalidArgumentException(e);
			}
		}

		public URL constructQuery(URI arg0, double arg1, double arg2, double arg3) throws InvalidArgumentException, NotFoundException {

			v.clear();
			v.add(arg0.toString());
			v.add(new Double(arg1));
			v.add(new Double(arg2));
			v.add(new Double(arg3));
			try {
				String s =  (String)client.execute("nvo.cone.constructQuery",v);
				return new URL(s);
			} catch (Exception x) {
				//try and deduce what kind of exception it was.
				String s = x.getMessage();
				if (s.indexOf("NotFound") != -1) {
					throw new NotFoundException(s);
				} else  {
					throw new InvalidArgumentException(s);
				} 
			}
		}

		public String getRegistryQuery() {

			v.clear();
			try {
				return  (String)client.execute("nvo.cone.getRegistryQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}

		public Document getResults(URL arg0) throws ServiceException {

			v.clear();
			v.add(arg0.toString());
			try {
				String s =  (String)client.execute("nvo.cone.addOption",v);
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public void saveResults(URL arg0, URI arg1) throws SecurityException, ServiceException, InvalidArgumentException {

			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			try {
				client.execute("nvo.cone.saveResults",v);

			} catch (Exception x) {
				//try and deduce what kind of exception it was.
				String s = x.getMessage();
				if (s.indexOf("Security") != -1) {
					throw new SecurityException(s);
				} else  if (s.indexOf("InvalidArgument") != -1) {
					throw new InvalidArgumentException(s);
				} else {
					throw new ServiceException(s);
				}
			}
		}

		public String getRegistryAdqlQuery() {

			v.clear();
			try {
				return  (String)client.execute("nvo.cone.getRegistryAdqlQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}

		public String getRegistryXQuery() {

			v.clear();
			try {
				return  (String)client.execute("nvo.cone.getRegistryXQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}

}
