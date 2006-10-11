/**
 * 
 */
package org.astrogrid.desktop.modules.cds;

import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.cds.Sesame;
import org.astrogrid.acr.cds.SesamePositionBean;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ACRTestSetup;

/**
 * @author Noel Winstanley
 * @since Jun 10, 20062:09:39 AM
 */
public class SesameRpcSystemTest extends SesameSystemTest implements Sesame {
	  protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        super.sesame = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;

	    public static Test suite() {
	        return new ACRTestSetup(new TestSuite(SesameRpcSystemTest.class));
	    }
	public String sesame(String arg0, String arg1) throws ServiceException {
		v.clear();
		v.add(arg0);
		v.add(arg1);
		try {
			return  (String)client.execute("cds.sesame.sesame",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public String sesameChooseService(String arg0, String arg1, boolean arg2,
			String arg3) throws ServiceException {
		v.clear();
		v.add(arg0);
		v.add(arg1);
		v.add(new Boolean(arg2));
		v.add(arg3);
		try {
			return (String)client.execute("cds.sesame.sesameChooseService",v);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	public SesamePositionBean resolve(String arg0) throws ServiceException, NotFoundException {
		v.clear();
		v.add(arg0);
		try {
			SesamePositionBean res = new SesamePositionBean();
			Map m = (Map)client.execute("cds.sesame.resolve",v);
			BeanUtils.copyProperties(res,m); //@todo use this more in testing.
			return res;
		} catch (Exception e) {
			if (e.getMessage().indexOf("NotFoundException") != -1) {
				throw new NotFoundException(e.getMessage());
			} else {
				throw new ServiceException(e);
			}
		}
	}

}
