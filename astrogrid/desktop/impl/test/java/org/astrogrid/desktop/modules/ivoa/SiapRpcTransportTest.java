/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Oct 10, 20061:02:07 PM
 */
public class SiapRpcTransportTest extends SiapRmiTransportTest implements Siap {
	 protected void setUp() throws Exception {
	        super.setUp();
	        WebServer serv = (WebServer)getACR().getService(WebServer.class);
	        assertNotNull(serv);
	        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
	        v = new Vector();
	        siap = this ; // test clas iimplements service interface
	    }
	    protected XmlRpcClient client;
	    protected Vector v ;
	    protected void tearDown() throws Exception {
	    	super.tearDown();
	    	client = null;
	    	v = null;
	    }
	    public static Test suite() {
	        return new ARTestSetup(new TestSuite(SiapRpcTransportTest.class));
	    }
		public URL constructQuery(URI arg0, double arg1, double arg2, double arg3) throws InvalidArgumentException, NotFoundException {

			v.clear();
			v.add(arg0.toString());
			v.add(new Double(arg1));
			v.add(new Double(arg2));
			v.add(new Double(arg3));
			try {
				String s =  (String)client.execute("ivoa.siap.constructQuery",v);
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

		public URL constructQueryF(URI arg0, double arg1, double arg2, double arg3, String arg4) throws InvalidArgumentException, NotFoundException {

			v.clear();
			v.add(arg0.toString());
			v.add(new Double(arg1));
			v.add(new Double(arg2));
			v.add(new Double(arg3));
			v.add(arg4);
			try {
				String s =  (String)client.execute("ivoa.siap.constructQueryF",v);
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

		public URL constructQueryS(URI arg0, double arg1, double arg2, double arg3, double arg4) throws InvalidArgumentException, NotFoundException {

			v.clear();
			v.add(arg0.toString());
			v.add(new Double(arg1));
			v.add(new Double(arg2));
			v.add(new Double(arg3));
			v.add(new Double(arg4));
			try {
				String s =  (String)client.execute("ivoa.siap.constructQueryS",v);
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

		public URL constructQuerySF(URI arg0, double arg1, double arg2, double arg3, double arg4, String arg5) throws InvalidArgumentException, NotFoundException {

			v.clear();
			v.add(arg0.toString());
			v.add(new Double(arg1));
			v.add(new Double(arg2));
			v.add(new Double(arg3));
			v.add(new Double(arg4));
			v.add(arg5);
			try {
				String s =  (String)client.execute("ivoa.siap.constructQuerySF",v);
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
	    public URL addOption(URL arg0, String arg1, String arg2) throws InvalidArgumentException {

			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			v.add(arg2.toString());
			try {
				String s =  (String)client.execute("ivoa.siap.addOption",v);
				return new URL(s);
			} catch (Exception e) {
				throw new InvalidArgumentException(e);
			}
		}
	    

		public String getRegistryQuery() {

			v.clear();
			try {
				return  (String)client.execute("ivoa.siap.getRegistryQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}

		public Map[] execute(URL arg0) throws ServiceException {
			v.clear();
			v.add(arg0.toString());
			try {
				List l = (List)client.execute("ivoa.siap.execute",v);
				return (Map[])l.toArray(new Map[]{});
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		public Document getResults(URL arg0) throws ServiceException {
			return executeVotable(arg0);
		}
		public Document executeVotable(URL arg0) throws ServiceException {		
			v.clear();
			v.add(arg0.toString());
			try {
				String s =  (String)client.execute("ivoa.siap.executeVotable",v);
				return DomHelper.newDocument(s);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}

		public void saveResults(URL arg0, URI arg1) throws SecurityException, ServiceException, InvalidArgumentException {
			executeAndSave(arg0,arg1);
		}
		public void executeAndSave(URL arg0, URI arg1) throws SecurityException, ServiceException, InvalidArgumentException {
					
			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			try {
				client.execute("ivoa.siap.executeAndSave",v);

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
				return  (String)client.execute("ivoa.siap.getRegistryAdqlQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}

		public String getRegistryXQuery() {

			v.clear();
			try {
				return  (String)client.execute("ivoa.siap.getRegistryXQuery",v);
			} catch (Exception e) {
				fail(e.getMessage());
				throw new RuntimeException("never reached");
			}
		}



		public int saveDatasets(URL arg0, URI arg1) throws SecurityException, ServiceException, InvalidArgumentException {
			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			try {
				return ((Integer)client.execute("ivoa.siap.saveDatasets",v)).intValue();
			} catch (Exception e) {
				fail(e.getMessage());
				return 0; //never here
			}
		}

		public int saveDatasetsSubset(URL arg0, URI arg1, List arg2) throws SecurityException, ServiceException, InvalidArgumentException {
			v.clear();
			v.add(arg0.toString());
			v.add(arg1.toString());
			v.add(new Vector(arg2));
			try {
				return ((Integer)client.execute("ivoa.siap.saveDatasetsSubset",v)).intValue();
			} catch (Exception e) {
				fail(e.getMessage());
				return 0; // never here.
			}
		}


	    

}
