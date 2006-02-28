/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.astrogrid.common.namegen.NameGen;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * @author jdt
 */
public class AbstractPlasticTestBase extends TestCase {

	protected static final URI SENDER_ID = URI.create("ivo://junit");

	protected MessengerImpl messenger;

	protected Executor executor;

	protected NameGen idGenerator;

	protected WebServer web;

	protected RmiServer rmi;
	
	protected BrowserControl browser;
	
	protected Configuration config;

	public void setUp() {
		messenger = new MessengerImpl();
		executor = new DirectExecutor();
		idGenerator = new InMemoryNameGen();
		web = new WebServer() {

			public String getUrlRoot() {
				return "http://127.0.0.1:" + getPort() + "/" + getKey() + "/";
			}

			public String getKey() {
				return "foobar";
			}

			public int getPort() {
				return 8001;
			}

		};
		rmi = new RmiServer() {

			public int getPort() {
				return 1234;
			}

		};

		config = new Configuration() {
			Map store = new HashMap();
			public boolean setKey(String arg0, String arg1) {
				store.put(arg0,arg1);
				return true;
			}

			public String getKey(String arg0) {
				return (String) store.get(arg0);
			}

			public String[] listKeys() throws ACRException {
				return (String[]) store.keySet().toArray(new String[]{});
			}

			public Map list() throws ACRException {
				return store;
			}

			public void removeKey(String arg0) {
				store.remove(arg0);
			}
			
		};
	}

}
