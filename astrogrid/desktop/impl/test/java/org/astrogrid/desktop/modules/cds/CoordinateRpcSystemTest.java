/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Coordinate;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpRpcIntegrationTest;

/** test coordinate component over xmlrpc
 * @author Noel Winstanley
 * @since Jun 10, 20061:36:13 AM
 */
public class CoordinateRpcSystemTest extends CoordinateSystemTest implements Coordinate {
	  protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        super.coord = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;

	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(CoordinateRpcSystemTest.class));
	    }
	public String convert(double arg0, double arg1, double arg2, int arg3)
			throws ServiceException {
		v.clear();
		v.add(new Double(arg0));
		v.add(new Double(arg1));
		v.add(new Double(arg2));
		v.add(new Integer(arg3));
		try {
			return (String)client.execute("cds.coordinate.convert",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public String convertE(int arg0, int arg1, double arg2, double arg3,
			double arg4, int arg5, double arg6, double arg7)
			throws ServiceException {
		v.clear();
		v.add(new Integer(arg0));
		v.add(new Integer(arg1));
		v.add(new Double(arg2));
		v.add(new Double(arg3));
		v.add(new Double(arg4));
		v.add(new Integer(arg5));
		v.add(new Double(arg6));
		v.add(new Double(arg7));
		try {
			return (String)client.execute("cds.coordinate.convertE",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public String convertL(double arg0, double arg1, int arg2)
			throws ServiceException {
		v.clear();
		v.add(new Double(arg0));
		v.add(new Double(arg1));
		v.add(new Integer(arg2));
		try {
			return (String)client.execute("cds.coordinate.convertL",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public String convertLE(int arg0, int arg1, double arg2, double arg3,
			int arg4, double arg5, double arg6) throws ServiceException {
		v.clear();
		v.add(new Integer(arg0));
		v.add(new Integer(arg1));
		v.add(new Double(arg2));
		v.add(new Double(arg3));
		v.add(new Integer(arg4));
		v.add(new Double(arg5));
		v.add(new Double(arg6));
		try {
			return (String)client.execute("cds.coordinate.convertLE",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
