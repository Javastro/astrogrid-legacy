/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.rmi.RemoteException;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.VizieR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Jun 10, 20062:18:46 AM
 */
public class VizierRpcTransportTest extends VizierSystemTest implements VizieR{
	  protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        super.viz = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;
	    protected void tearDown() throws Exception {
	    	super.tearDown();
	    	client = null;
	    	v = null;
	    }
	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(VizierRpcTransportTest.class));
	    }

		public Document cataloguesData(String arg0, double arg1, String arg2, String arg3) throws ServiceException {
			v.clear();
			v.add(arg0);
			v.add(new Double(arg1));
			v.add(arg2);
			v.add(arg3);
			try {
				String s =  (String)client.execute("cds.vizier.cataloguesData",v); 
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public Document cataloguesDataWavelength(String arg0, double arg1, String arg2, String arg3, String arg4) throws ServiceException {
			v.clear();
			v.add(arg0);
			v.add(new Double(arg1));
			v.add(arg2);
			v.add(arg3);
			v.add(arg4);
			try {
				String s =  (String)client.execute("cds.vizier.cataloguesDataWavelength",v); 
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public Document cataloguesMetaData(String arg0, double arg1, String arg2, String arg3) throws ServiceException {
			v.clear();
			v.add(arg0);
			v.add(new Double(arg1));
			v.add(arg2);
			v.add(arg3);
			try {
				String s =  (String)client.execute("cds.vizier.cataloguesMetaData",v); 
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public Document cataloguesMetaDataWavelength(String arg0, double arg1, String arg2, String arg3, String arg4) throws ServiceException {
			v.clear();
			v.add(arg0);
			v.add(new Double(arg1));
			v.add(arg2);
			v.add(arg3);
			v.add(arg4);
			try {
				String s =  (String)client.execute("cds.vizier.cataloguesMetaDataWavelength",v); 
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public Document metaAll() throws ServiceException {
			v.clear();
			try {
				String s =  (String)client.execute("cds.vizier.metaAll",v); 
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}


}
