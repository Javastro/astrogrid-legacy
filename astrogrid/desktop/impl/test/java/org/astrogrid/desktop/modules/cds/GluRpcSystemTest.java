/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Glu;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 10, 20061:42:20 AM
 */
public class GluRpcSystemTest extends GluSystemTest implements Glu {
	  protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        super.glu = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;

	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(GluRpcSystemTest.class));
	    }
	public String getURLfromTag(String arg0) throws ServiceException {
		v.clear();
		v.add(arg0);
		try {
			return (String)client.execute("cds.glu.getURLfromTag",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
